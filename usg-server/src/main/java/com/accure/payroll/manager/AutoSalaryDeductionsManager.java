package com.accure.payroll.manager;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.manager.SalaryHeadManager;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.EmpAttendance;
import com.accure.payroll.dto.IncomeTax;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;
import static com.accure.payroll.manager.AutoSalaryEarningsManager.getEffectiveFromDate;
import com.accure.usg.common.manager.DBManager;
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
 *
 * @author chaitu
 */
public class AutoSalaryDeductionsManager {

    final static RestClient aql = new RestClient();
    final static String incomeTaxTable = ApplicationConstants.USG_DB1 + ApplicationConstants.INCOMETAX_TABLE + "`";
    final static String insuranceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE + "`";
    final static String insuranceHistoryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.INSURANCE_TRANSACTIONS_HISTORY_TABLE + "`";
    final static String loanSupressTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LOAN_TRANSACTION_TABLE + "`";
    final static String loanPaymentTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LOAN_PAYMENT_TABLE + "`";

    /**
     * @author chaitu
     * @description getEmployeeDeductions() method will get the employee final
     * deductions
     * @param Employee emp
     * @param EmpAttendance attendance
     * @param int month
     * @param int year
     * @return Deductions
     * @throws Exception
     */
    protected static Deductions getEmployeeDeductions(Employee emp, EmpAttendance attendance, int month, int year, String stopSalary) throws Exception {
        Deductions deds = null;
        if (emp == null || attendance == null || month == 0 || year == 0) {
            return deds;
        }
        deds = new Deductions();
        String empCode = emp.getEmployeeCode();

        //get employee salary heads
        ArrayList<EarningHeadsDetails> ehd = getEmployeeDeductionHeads(emp, attendance, month, year, stopSalary);
        if (ehd != null && ehd.size() > 0) {
            deds.setIsDeductionHeads(true);
            deds.setDeductionHeads(ehd);
        } else {
            deds.setIsDeductionHeads(false);
        }

        //get employee loan
        ArrayList<LoanPayment> lp = getEmployeeLoan(empCode, month, year, stopSalary);
        if (lp != null && lp.size() > 0) {
            deds.setIsLoan(true);
            deds.setLoan(lp);
        } else {
            deds.setIsLoan(false);
        }

        //get employee insurance
        ArrayList<InsuranceTransactions> its = getEmployeeInsurance(empCode, month, year, stopSalary);
        if (its != null && its.size() > 0) {
            deds.setIsInsurance(true);
            deds.setInsurance(its);
        } else {
            deds.setIsInsurance(false);
        }

        //get employee income tax
        IncomeTax itax = getEmployeeIncomeTax(empCode, month, year, stopSalary);
        if (itax != null) {
            deds.setIsIncomeTax(true);
            deds.setIncomeTax(itax);
        } else {
            deds.setIsIncomeTax(false);
        }
        return deds;
    }

    /**
     * @author chaitu
     * @description getEmployeeDeductionHeads() method will return the employee
     * salary deduction heads with conditions
     * @table employee,empattendance,salaryhead
     * @conditions 1.)month=1 2.)year=2016 3.)empCode=123 4.)get list of
     * deductions from employee table 5.)get the stop deduction flag from
     * attendance table for given month,year,empcode 6.)if stop deduction flag
     * is true then add all heads & skip the salary head name (if found others)
     * from salary heads else if stop deduction flag is false add all.
     * @param Employee emp
     * @param EmpAttendance attendance
     * @param int month
     * @param int year
     * @return ArrayList<EarningHeadsDetails>
     * @throws Exception
     */
    private static ArrayList<EarningHeadsDetails> getEmployeeDeductionHeads(Employee emp, EmpAttendance attendance, int month, int year, String stopSalary) throws Exception {
        if (emp == null || attendance == null || month == 0 || year == 0) {
            return null;
        }
        ArrayList<EarningHeadsDetails> outputList = new ArrayList<EarningHeadsDetails>();

        //get suspension days
        int beforeSuspended = attendance.getPbsP();
        int afterSuspended = attendance.getPasP();

        //get stop deduction flag from attendance table
        boolean stopDeduction = attendance.getSd();

        //getting the employee deduction heads
        List<EarningHeadsDetails> dbList = emp.getDeductionHeads();
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
                        //if the stop deduction flag is "true" & the deduction type is "others" skip this record
                        if (stopDeduction && salaryHead.getDeductionType() != null && salaryHead.getDeductionType().equalsIgnoreCase(ApplicationConstants.OTHERS)) {
                            continue;
                        }
                        String rounding = salaryHead.getRounding();
                        //add to the list
                        ahd.setDescription(salaryHead.getShortDescription());
                        ahd.setDescriptionInfo(salaryHead);

                        //implementing suspension rules
                        //check if pasp>0 || pbsp>0 and if halfOnSuspended=yes then add 50% salary for (pas/p) every earning head
                        String halfOnSuspended = salaryHead.getHalfOnSuspended();
                        if ((beforeSuspended > 0 || afterSuspended > 0) && halfOnSuspended != null && halfOnSuspended.equalsIgnoreCase(ApplicationConstants.YES)) {
                            double suspensionDeductedAmount = AutoSalaryEarningsManager.deductSalaryBasedOnSuspension(month, year, beforeSuspended, afterSuspended, amount);
                            if (suspensionDeductedAmount != -1) {
                                //rounding
                                double finalAmount = SalarySlipRportPDFGeneration.round(rounding, suspensionDeductedAmount);
                                ahd.setAmount(finalAmount);
                            }
                        }

                        //verify the stop salary flag
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
     * @description getEmployeeInsurance() method will get the employee
     * insurance transactions
     * @conditions 1.)empCode=123 2.)checked=false 3.)dated must be greater than
     * currenttime 4.)effectType=monthly every month it will deduct
     * 5.)effectType=quarterly every quarter(3,9,6,12) it will deduct
     * 6.)effectType=yearly every year(3rd month) it will deduct.
     * @table insuranceTransactionTable
     * @param String empCode
     * @param int month
     * @param int year
     * @return InsuranceTransactions
     * @throws Exception
     */
    private static ArrayList<InsuranceTransactions> getEmployeeInsurance(String empCode, int month, int year, String stopSalary) throws Exception {
        ArrayList<InsuranceTransactions> outputList = null;
        if (empCode == null || month == 0 || year == 0) {
            return outputList;
        }

        String insuranceTableQuery = "select empCode,_id as idStr,dated,inscName,policyNumber,installmentAmount,totalContribution,lastInscDate,totalInstallment,effectType,remarks,checked,createDate,updateDate,status,mode,type,category,createdBy,updatedBy"
                + " from " + insuranceTable
                + " where empCode=\"" + empCode + "\""
                + " and status=\"" + ApplicationConstants.ACTIVE + "\""
                + " and checked=false";

        //get data from INSURANCE_TRANSACTIONS_TABLE
        String insuranceTableOutput = aql.getRestData(ApplicationConstants.END_POINT, insuranceTableQuery);
        if (insuranceTableOutput != null && !insuranceTableOutput.isEmpty() && !insuranceTableOutput.equals("[]")) {
            ArrayList<InsuranceTransactions> dbList = new Gson().fromJson(insuranceTableOutput, new TypeToken<ArrayList<InsuranceTransactions>>() {
            }.getType());
            outputList = new ArrayList<InsuranceTransactions>();

            for (InsuranceTransactions insT : dbList) {
                //verifying the condition lat insurance date should not be greater than current date
                long lastInsuranceTime = Common.getMillisFromDate(insT.getLastInscDate());
                long currentTime = System.currentTimeMillis();
                if (lastInsuranceTime > currentTime) {
                    InsuranceTransactions output = null;
                    String effectType = insT.getEffectType();

                    //validating effect type
                    if (effectType != null) {
                        if (effectType.equalsIgnoreCase(ApplicationConstants.MONTHLY)) {
                            //if type is monthly deduct every month
                            output = insT;
                        }
                        if (effectType.equalsIgnoreCase(ApplicationConstants.QUARTERLY)) {
                            //if type is quarterly deduct in 3,6,9,12 months only
                            if (month == 3 || month == 6 || month == 9 || month == 12) {
                                output = insT;
                            }
                        }
                        if (effectType.equalsIgnoreCase(ApplicationConstants.YEARLY)) {
                            //if type is yearly deduct in 3rd month of the year only
                            if (month == 3) {
                                output = insT;
                            }
                        }
                    }

                    if (output != null) {
                        String insPKey = output.getIdStr();
                        SalaryHead salaryHead = SalaryHeadManager.get(output.getInscName());
                        output.setInsInfo(salaryHead);

                        //verify the stopSalary flag
                        if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                            output.setInstallmentAmount(0);
                        } else if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                            //rounding
                            output.setInstallmentAmount((int) SalarySlipRportPDFGeneration.round(salaryHead.getRounding(), output.getInstallmentAmount()));
                            //add contribution count +1
                            if (output.getTotalContribution() >= 0) {
                                output.setTotalContribution((output.getTotalContribution() + 1));
                                InsuranceTransactionManager.updateTotalContributions(insPKey, +1);
                            }
                            //adding into history table
                            output.setMonth(month);
                            output.setYear(year);
                            output.setId(null);
                            output.setIdStr(null);
                            String insHistoryPKey = DBManager.getDbConnection().insert(ApplicationConstants.INSURANCE_TRANSACTIONS_HISTORY_TABLE, new Gson().toJson(output));
                            output.setIdStr(insHistoryPKey);
                            output.setId(insPKey);
                        }
                        outputList.add(output);
                    }
                }
            }
        }
        return outputList;
    }

    /**
     * @author chaitu
     * @description getEmployeeIncomeTax() method will get the employee income
     * tax
     * @table incometaxprocess
     * @conditions 1.)month=1 2.)year=2016 3.)empcode=123 4.)it>0
     * @param String empCode
     * @param int month
     * @param int year
     * @return IncomeTax
     * @throws Exception
     */
    private static IncomeTax getEmployeeIncomeTax(String empCode, int month, int year, String stopSalary) throws Exception {
        IncomeTax output = null;
        if (empCode == null || month == 0 || year == 0) {
            return output;
        }

        String incomeTaxTableQuery = "select employeeCode,_id as idStr,month,year,it,total,educationcess,createDate,updateDate,lockStatus,processStatus"
                + " from " + incomeTaxTable
                + " where month=" + month
                + " and year=" + year
                + " and employeeCode=\"" + empCode + "\""
                + " and status=\"" + ApplicationConstants.ACTIVE + "\""
                + " and total>0";

        //get data from INCOMETAX_TABLE
        String incomeTaxTableOutput = aql.getRestData(ApplicationConstants.END_POINT, incomeTaxTableQuery);
        if (incomeTaxTableOutput != null && !incomeTaxTableOutput.isEmpty() && !incomeTaxTableOutput.equals("[]")) {
            ArrayList<IncomeTax> list = new Gson().fromJson(incomeTaxTableOutput, new TypeToken<ArrayList<IncomeTax>>() {
            }.getType());
            output = list.get(0);

            //verify the stopSalary flag
            if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                output.setTotal(0.0);
            }
        }
        return output;
    }

    /**
     * @author chaitu
     * @description getEmployeeLoan() method will get the employee loan
     * @table loanSupress,loanpayment
     * @conditions 1.)month=1 2.)year=2016 3.)empcode=123 4.)it>0
     * @param String empCode
     * @param int month
     * @param int year
     * @return LoanPayment
     * @throws Exception
     */
    private static ArrayList<LoanPayment> getEmployeeLoan(String empCode, int month, int year, String stopSalary) throws Exception {
        ArrayList<LoanPayment> output = null;
        if (empCode == null || month == 0 || year == 0) {
            return output;
        }

        String loanSuppressQuery = "select applyNo"
                + " from " + loanSupressTable
                + " where isSuppressed=\"Yes\""
                + " and empCode=\"" + empCode + "\""
                + " and status=\"" + ApplicationConstants.ACTIVE + "\"";
        String suppressedApplyNo = "";
        //get data from loanSupress
        String loanSuppressQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, loanSuppressQuery);
        if (loanSuppressQueryOutput != null && !loanSuppressQueryOutput.isEmpty() && !loanSuppressQueryOutput.equals("[]")) {
            //if suppress flag is true for any applyNo then don't deduct            
            ArrayList<HashMap> suppressList = new Gson().fromJson(loanSuppressQueryOutput, new TypeToken<ArrayList<HashMap>>() {
            }.getType());
            for (HashMap map : suppressList) {
                suppressedApplyNo = suppressedApplyNo + "\"" + map.get("applyNo") + "\",";
            }
            if (suppressedApplyNo != null && !suppressedApplyNo.isEmpty()) {
                suppressedApplyNo = "(" + suppressedApplyNo.substring(0, suppressedApplyNo.length() - 1) + ")";
            }
        }

        //if suppress flag is false for any applyNo then deduct          
        String loanPaymentQuery = "select * from " + loanPaymentTable + " as lpt1"
                + " join (select empCode,max(created) as maxDate,applyNo from " + loanPaymentTable + " where status=\"" + ApplicationConstants.ACTIVE + "\" and empCode=\"" + empCode + "\" and (balanceAmount>=0 or balanceInterest>=0) group by empCode,applyNo) as lpt2"
                + " on (lpt1.empCode = lpt2.empCode) and (lpt1.created = lpt2.maxDate)"
                + " where lpt1.status=\"" + ApplicationConstants.ACTIVE + "\""
                + " and lpt1.empCode=\"" + empCode + "\""
                + " and (lpt1.balanceAmount>=0 or lpt1.balanceInterest>=0)";

        if (suppressedApplyNo != null && !suppressedApplyNo.equals("")) {
            loanPaymentQuery = loanPaymentQuery + " and lpt1.applyNo not in " + suppressedApplyNo;
        }

        //get the loan balance amount or balance interest from loanpayment table
        String loanPaymentQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, loanPaymentQuery);
        if (loanPaymentQueryOutput
                != null && !loanPaymentQueryOutput.isEmpty()
                && !loanPaymentQueryOutput.equals("[]")) {
            output = new ArrayList<LoanPayment>();
            ArrayList<HashMap> tempList = new Gson().fromJson(loanPaymentQueryOutput, new TypeToken<ArrayList<HashMap>>() {
            }.getType());
            for (HashMap map : tempList) {
                if (map.containsKey(ApplicationConstants.VALUE) && map.get(ApplicationConstants.VALUE) != null) {
                    LoanPayment lp = new Gson().fromJson(new Gson().toJson(map.get(ApplicationConstants.VALUE)), new TypeToken<LoanPayment>() {
                    }.getType());
                    LoanPayment deductedLP = getDeductedLoan(lp, empCode, month, year, stopSalary);
                    if (deductedLP != null) {
                        //get loan from salary head master
                        SalaryHead salaryHead = SalaryHeadManager.get(deductedLP.getLoanType());
                        if (salaryHead != null) {
                            deductedLP.setLoanInfo(salaryHead);
                            //rounding the loan amount
                            deductedLP.setPaidAmount(SalarySlipRportPDFGeneration.round(salaryHead.getRounding(), deductedLP.getPaidAmount()));
                            //rounding the interest amount
                            deductedLP.setInterestPaid(SalarySlipRportPDFGeneration.roundTwoDecimals(deductedLP.getInterestPaid()));
                            output.add(deductedLP);
                        }
                    }
                }
            }
        }
        return output;
    }

    /**
     * @author chaitu
     * @description getDeductedLoan() method will get the employee loan after
     * deductions
     * @param LoanPayment dblp
     * @return LoanPayment
     * @throws Exception
     */
    private static LoanPayment getDeductedLoan(LoanPayment dblp, String empCode, int month, int year, String stopSalary) throws Exception {
        if (dblp == null) {
            return null;
        }

        //verify the condition loan apply date should be greater than current date        
        long loanApplyDateTime = Common.getMillisFromDate(dblp.getLoanDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month - 1));
        long salaryProcessingDate = calendar.getTimeInMillis();

        if (loanApplyDateTime != 0 && salaryProcessingDate >= loanApplyDateTime) {
            //deducting loan amount
            double balanceAmount = dblp.getBalanceAmount();
            double installmentAmount = dblp.getInstallmentAmount();
            double balanceInstallments = dblp.getBalanceNoOfInstallments();
            boolean balanceFlag = false;

            //verify the stop salary flag as well
            if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO) && balanceAmount > 0 && installmentAmount > 0 && balanceInstallments > 0) {
                //if balance amount is greater than installment amount
                if (balanceAmount >= installmentAmount) {
                    dblp.setBalanceAmount((balanceAmount - installmentAmount));
                    dblp.setPaidAmount(installmentAmount);
                } else if (balanceAmount < installmentAmount) {
                    dblp.setBalanceAmount(0);
                    dblp.setPaidAmount(balanceAmount);
                }
                dblp.setBalanceNoOfInstallments((balanceInstallments - 1));
            } else {
                balanceFlag = true;
            }

            //deducting loan interest amount
            double balanceInterest = dblp.getBalanceInterest();
            double interestInstallmentAmount = dblp.getInterestInstallmentAmount();
            double balanceInterestInstallments = dblp.getBalanceNoOfInterestInstallments();
            boolean balanceInterestFlag = false;
            //verify the stop salary flag as well
            if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO) && balanceInterest > 0 && interestInstallmentAmount > 0 && balanceInterestInstallments > 0) {
                if (balanceInterest >= interestInstallmentAmount) {
                    dblp.setBalanceInterest((balanceInterest - interestInstallmentAmount));
                    dblp.setInterestPaid(interestInstallmentAmount);
                } else if (balanceInterest < interestInstallmentAmount) {
                    dblp.setBalanceInterest(0);
                    dblp.setInterestPaid(balanceInterest);
                }
                dblp.setBalanceNoOfInterestInstallments((balanceInterestInstallments - 1));
            } else {
                balanceInterestFlag = true;
            }

            //balance & balanceInterest is not greater than to 0 then skip
            if (balanceFlag && balanceInterestFlag) {
                return null;
            }

            //before deduct verify once again with this month,year,applyno already deducted or not
            String loanPaymentExistsQuery = "select *,_id as idStr from " + loanPaymentTable
                    + " where status=\"" + ApplicationConstants.ACTIVE + "\""
                    + " and empCode=\"" + empCode + "\""
                    + " and paymentMode=\"" + ApplicationConstants.SALARY + "\""
                    + " and applyNo=\"" + dblp.getApplyNo() + "\""
                    + " and month=" + month
                    + " and year=" + year
                    + " order by updated desc";

            String loanPaymentExistsQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, loanPaymentExistsQuery);
            if (loanPaymentExistsQueryOutput != null && !loanPaymentExistsQueryOutput.isEmpty() && !loanPaymentExistsQueryOutput.equals("[]")) {
                //if loan is deducted for this month & year & applyNo then don't deduct
                ArrayList<HashMap> tempList = new Gson().fromJson(loanPaymentExistsQueryOutput, new TypeToken<ArrayList<HashMap>>() {
                }.getType());
                HashMap map = tempList.get(0);
                if (map.containsKey(ApplicationConstants.VALUE) && map.get(ApplicationConstants.VALUE) != null) {
                    LoanPayment lp = new Gson().fromJson(new Gson().toJson(map.get(ApplicationConstants.VALUE)), new TypeToken<LoanPayment>() {
                    }.getType());
                    lp.setId(null);
                    lp.setDdo(null);
                    lp.setEmpName(null);
                    lp.setDesignation(null);
                    return lp;
                }
            } else if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                //if loan is not deducted for this month & year & applyNo then deduct
                dblp.setPaymentMode(ApplicationConstants.SALARY);
                dblp.setMonth(month);
                dblp.setYear(year);
                dblp.setCreated(System.currentTimeMillis());
                dblp.setUpdated(System.currentTimeMillis());
                dblp.setId(null);
                dblp.setIdStr(null);
                //inserting after deductions in loan payment table
                String loanId = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_PAYMENT_TABLE, new Gson().toJson(dblp));
                dblp.setIdStr(loanId);

                //remove unwanted data before storing in auto salary table
                dblp.setDdo(null);
                dblp.setEmpName(null);
                dblp.setDesignation(null);
            }

            //verify the stop salary flag
            if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                dblp.setPaidAmount(0.0);
                dblp.setInterestPaid(0.0);
            }
            return dblp;
        } else {
            return null;
        }
    }

    /**
     * @author chaitu
     * @description getTotalDeductions() method will get final amount of
     * deductions
     * @param AutoSalaryProcess asp
     * @return double
     * @throws Exception
     */
    public static double getTotalDeductions(AutoSalaryProcess asp) throws Exception {
        double totalDeductions = 0.0;
        //get final deductions amount
        Deductions deds = asp.getDeductionsInfo();

        //get deduction heads
        if (deds.isIsDeductionHeads() && deds.getDeductionHeads() != null && deds.getDeductionHeads().size() > 0) {
            for (EarningHeadsDetails ehd : deds.getDeductionHeads()) {
                totalDeductions += ehd.getAmount();
            }
        }
        //get insurance
        if (deds.isIsInsurance() && deds.getInsurance() != null && deds.getInsurance().size() > 0) {
            for (InsuranceTransactions its : deds.getInsurance()) {
                totalDeductions += its.getInstallmentAmount();
            }
        }
        //get income tax
        if (deds.isIsIncomeTax() && deds.getIncomeTax() != null) {
            totalDeductions += deds.getIncomeTax().getTotal();
        }
        //get loan
        if (deds.isIsLoan() && deds.getLoan() != null && deds.getLoan().size() > 0) {
            for (LoanPayment lp : deds.getLoan()) {
                totalDeductions += lp.getPaidAmount();
                totalDeductions += lp.getInterestPaid();
            }
        }
        //get arrear
        if (asp.isIsArrear() && asp.getArrear() != null) {
            totalDeductions += asp.getArrear().getTotalDeductions();
        }
        return totalDeductions;
    }
}
