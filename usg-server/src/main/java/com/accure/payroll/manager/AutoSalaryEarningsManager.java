package com.accure.payroll.manager;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.manager.SalaryHeadManager;
import com.accure.leave.dto.LeaveEncashment;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.EmpAttendance;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author ankur/chaitu
 */
public class AutoSalaryEarningsManager {

    final static RestClient aql = new RestClient();
    final static String leaveEncashmentTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_ENCASHMENT + "`";

    /**
     * @author chaitu
     * @description getEmployeeEarnings() method will get the employee final
     * deductions
     * @conditions 1.)month=1 2.)year=2016 3.)empcode=123
     * @param Employee emp
     * @param EmpAttendance attendance
     * @param int month
     * @param int year
     * @return Earnings
     * @throws Exception
     */
    protected static Earnings getEmployeeEarnings(Employee emp, EmpAttendance attendance, int month, int year, String stopSalary) throws Exception {
        Earnings earns = null;
        if (emp == null || attendance == null || month == 0 || year == 0) {
            return earns;
        }
        earns = new Earnings();
        String empCode = emp.getEmployeeCode();

        //get employee salary heads
        ArrayList<EarningHeadsDetails> ehd = getEmployeeEarningHeads(emp, attendance, month, year, stopSalary);
        if (ehd != null && ehd.size() > 0) {
            earns.setIsEarningHeads(true);
            earns.setEarningHeads(ehd);
        } else {
            earns.setIsEarningHeads(false);
        }

        //get employee leave encashment
        LeaveEncashment le = getEmployeeLeaveEncashment(empCode, month, year, stopSalary);
        if (le != null) {
            earns.setIsLeaveEncashment(true);
            earns.setLeaveEncashment(le);
        } else {
            earns.setIsLeaveEncashment(false);
        }
        return earns;
    }

    /**
     * @author ankur/chaitu
     * @description getEmployeeEarningHeads() method will get the employee
     * salary earning heads with all conditions
     * @table employee,empattendance,salaryhead
     * @conditions 1.)month=1 2.)year=2016 3.)empCode=123 4.)get the lwp days
     * from empattendance table 5.)get list of earnings heads from employee
     * table 6.)if lwp greater than 0 then deduct all the heads if present
     * dependent is true 7.)if lwp less than or equal 0 then save as it is.
     * @param String empCode
     * @param int month
     * @param int year
     * @return ArrayList<EarningHeadsDetails>
     * @throws Exception
     */
    private static ArrayList<EarningHeadsDetails> getEmployeeEarningHeads(Employee emp, EmpAttendance attendance, int month, int year, String stopSalary) throws Exception {
        if (emp == null || attendance == null || month == 0 || year == 0) {
            return null;
        }
        ArrayList<EarningHeadsDetails> outputList = new ArrayList<EarningHeadsDetails>();

        //get lwp days from attendance from attendance table
        int lwpDays = attendance.getLeaveWithoutPay();
        //get suspension days
        int beforeSuspended = attendance.getPbsP();
        int afterSuspended = attendance.getPasP();

        //getting the employee earnings heads
        List<EarningHeadsDetails> dbList = emp.getEarningHeads();
        if (dbList != null && dbList.size() > 0) {
            for (EarningHeadsDetails ehd : dbList) {
                EarningHeadsDetails ahd = ehd;
                //get actual amount for this salary head
                double amount = ahd.getAmount();

                //verify effectiveFromDate for each salary head
                amount = getEffectiveFromDate(amount, ahd.getEffectiveFromDate(), month, year);
                ahd.setAmount(amount);

                if (amount >= 0) {
                    //get whole description information from salary head table based on primary key
                    SalaryHead salaryHead = SalaryHeadManager.get(ahd.getDescription());
                    if (salaryHead != null) {
                        String rounding = salaryHead.getRounding();
                        ahd.setDescription(salaryHead.getShortDescription());
                        ahd.setDescriptionInfo(salaryHead);

                        //implementing lwp rules
                        //check if lwp>0 and if presentDependentFlag=yes then do deductions for every head                        
                        String presentDependentFlag = salaryHead.getPresentDependent();
                        if (lwpDays > 0 && presentDependentFlag != null && presentDependentFlag.equalsIgnoreCase(ApplicationConstants.YES)) {
                            double lwpDeductedAmount = deductSalaryBasedOnLWP(month, year, lwpDays, amount);
                            if (lwpDeductedAmount != -1) {
                                //rounding
                                double finalAmount = SalarySlipRportPDFGeneration.round(rounding, lwpDeductedAmount);
                                ahd.setAmount(finalAmount);
                            }
                        }

                        //implementing suspension rules
                        //check if pasp>0 || pbsp>0 and if halfOnSuspended=yes then add 50% salary for (pas/p) every earning head
                        String halfOnSuspended = salaryHead.getHalfOnSuspended();
                        if ((beforeSuspended > 0 || afterSuspended > 0) && halfOnSuspended != null && halfOnSuspended.equalsIgnoreCase(ApplicationConstants.YES)) {
                            double suspensionDeductedAmount = deductSalaryBasedOnSuspension(month, year, beforeSuspended, afterSuspended, amount);
                            if (suspensionDeductedAmount != -1) {
                                //rounding
                                double finalAmount = SalarySlipRportPDFGeneration.round(rounding, suspensionDeductedAmount);
                                ahd.setAmount(finalAmount);
                            }
                        }

                        //verify the stopSalary flag
                        if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                            ahd.setAmount(0.0);
                        } else if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                            //rounding
                            double finalAmount = SalarySlipRportPDFGeneration.round(rounding, ahd.getAmount());
                            ahd.setAmount(finalAmount);
                        }
                        outputList.add(ahd);
                    }
                }
            }
        }
        return outputList;
    }

    /**
     * @author chaitu
     * @description deductSalaryBasedOnLWP() method will deduct the amount based
     * on month & year it returns presented no of days salary
     * @conditions 1.)month=1 2.)year=2016 3.)lwp=5 days 4.)amount=1000
     * @param int month
     * @param int year
     * @param int lwp
     * @param double amount
     * @return double
     * @throws Exception
     */
    private static double deductSalaryBasedOnLWP(int month, int year, int lwp, double amount) throws Exception {
        double finalAmount = 0;
        if (month == 0 || year == 0 || lwp == 0 || amount == 0) {
            return -1;
        }

        //getting number of days in a month & year
        int days = Common.getDaysFromMonthAndYear(year, month);
        double oneDaySalary = (amount / days);
        int presentDays = (days - lwp);
        finalAmount = (presentDays * oneDaySalary);
        return finalAmount;
    }

    /**
     * @author chaitu
     * @description deductSalaryBasedOnSuspension() method will deduct the
     * amount based on month & year it returns presented no of days salary
     * @conditions 1.)month=1 2.)year=2016 3.)pbsp=5 4.)pasp=26 5.)amount=10000
     * 6.)if half on suspended is true then give the 50% salary for (pas/p) that
     * earning head
     * @param int month
     * @param int year
     * @param int pbsp
     * @param int pasp
     * @param double amount
     * @return double
     * @throws Exception
     */
    public static double deductSalaryBasedOnSuspension(int month, int year, int pbsp, int pasp, double amount) throws Exception {
        if (month == 0 || year == 0 || amount == 0) {
            return -1;
        }

        //getting number of days in a month & year
        int days = Common.getDaysFromMonthAndYear(year, month);
        double oneDaySalary = (amount / days);
        double finalAmount = 0;
        double pbspFinal = 0;
        double paspFinal = 0;

        if (pbsp > 0) {
            pbspFinal = (pbsp * oneDaySalary);
        }
        if (pasp > 0) {
            paspFinal = (pasp * (oneDaySalary / 2));
        }

        finalAmount = (pbspFinal + paspFinal);
        return finalAmount;
    }

    /**
     * @author chaitu
     * @description getEmployeeLeaveEncashment() method will get the employee
     * leave encashment
     * @table arrearprocess
     * @conditions 1.)empcode=123 2.)month=1 3.)year=2016
     * @param String empCode
     * @param int month
     * @param int year
     * @return LeaveEncashment
     * @throws Exception
     */
    public static LeaveEncashment getEmployeeLeaveEncashment(String empCode, int month, int year, String stopSalary) throws Exception {
        LeaveEncashment le = null;
        if (empCode == null || month == 0 || year == 0) {
            return le;
        }

        String leaveEncashmentTableQuery = "select employeeCode,_id as idStr,leftStatus,leaveType,leaveTypeInfo,encashmentDate,month,year,leaveBalance,cashableLeaves,leaveToBeEncashed,totalAmount,remarks"
                + " from " + leaveEncashmentTable
                + " where month=" + month
                + " and year=" + year
                + " and employeeCode=\"" + empCode + "\""
                + " and isLocked=false"
                + " and totalAmount>0"
                + " and status=\"" + ApplicationConstants.ACTIVE + "\"";

        //get data from leaveEncashment
        String leaveEncashmentTableQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, leaveEncashmentTableQuery);
        if (leaveEncashmentTableQueryOutput != null && !leaveEncashmentTableQueryOutput.isEmpty() && !leaveEncashmentTableQueryOutput.equals("[]")) {
            ArrayList<LeaveEncashment> list = new Gson().fromJson(leaveEncashmentTableQueryOutput, new TypeToken<ArrayList<LeaveEncashment>>() {
            }.getType());
            le = list.get(0);

            //verify the stop salary flag
            if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                le.setTotalAmount(0.0);
            }

            //get leave encashment from salary head master
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("description", "Leave Encashment");
            condition.put("headType", "Earnings");
            SalaryHead salaryHead = SalaryHeadManager.get(condition);
            if (salaryHead != null) {
                le.setLeInfo(salaryHead);
                le.setTotalAmount(SalarySlipRportPDFGeneration.round(salaryHead.getRounding(), le.getTotalAmount()));
            }
        }
        return le;
    }

    /**
     * @author chaitu
     * @description getTotalEarnings() method will get final amount of earnings
     * @param AutoSalaryProcess asp
     * @return double
     * @throws Exception
     */
    public static double getTotalEarnings(AutoSalaryProcess asp) throws Exception {
        double totalEarnings = 0.0;
        //get final earnings amount
        Earnings earns = asp.getEarningsInfo();

        //get earning heads
        if (earns.isIsEarningHeads() && earns.getEarningHeads() != null && earns.getEarningHeads().size() > 0) {
            for (EarningHeadsDetails ehd : earns.getEarningHeads()) {
                totalEarnings += ehd.getAmount();
            }
        }
        //get arrear
        if (asp.isIsArrear() && asp.getArrear() != null) {
            totalEarnings += asp.getArrear().getTotalEarnings();
        }
        //get leave encashment
        if (earns.isIsLeaveEncashment() && earns.getLeaveEncashment() != null) {
            totalEarnings += earns.getLeaveEncashment().getTotalAmount();
        }
        return totalEarnings;
    }

    /**
     * @author chaitu
     * @description getEffectiveFromDate() method will verify each salary head
     * effectiveFromDate is less than the salary processing date
     * @param String effectiveFromDate
     * @param int salaryMonth
     * @param int salaryYear
     * @return boolean
     * @throws Exception
     */
    public static double getEffectiveFromDate(double amount, String effectiveFromDate, int salaryMonth, int salaryYear) throws Exception {
        if (effectiveFromDate == null) {
            return amount;
        }

        //getting number of days in a month & year
        int days = Common.getDaysFromMonthAndYear(salaryYear, salaryMonth);

        //get the salary head effective date
        Date effectiveFrom = Common.getDateFromString(effectiveFromDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(effectiveFrom);
        int effectDay = calendar.get(Calendar.DAY_OF_MONTH);
        int effectMonth = (calendar.get(Calendar.MONTH) + 1);
        int effectYear = calendar.get(Calendar.YEAR);
        long effectTime = calendar.getTimeInMillis();

        //get the salary processing start date
        int salaryDay = days;
        calendar = Calendar.getInstance();
        Date salaryDate = Common.getDateFromString(salaryDay + "/" + salaryMonth + "/" + salaryYear);
        calendar.setTime(salaryDate);
        long salaryTime = calendar.getTimeInMillis();

        if (effectTime < salaryTime) {
            if (salaryMonth == effectMonth && salaryYear == effectYear) {
                double oneDaySalary = (amount / days);
                int totalDaysNeedToProcess = ((days - effectDay) + 1);
                amount = (totalDaysNeedToProcess * oneDaySalary);
            }
        } else {
            amount = -1;
        }
        return amount;
    }
}
