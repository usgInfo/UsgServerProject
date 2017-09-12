/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.Nature;
import com.accure.leave.dto.EmployeeLeaveAssignment;
import com.accure.leave.dto.LeaveCarryForward;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.dto.LeaveTypeDetails;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.leave.pojo.LeaveType;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.midi.SysexMessage;

/**
 *
 * @author Asif
 */
public class LeaveCarryForwardManager {

    public String save(LeaveCarryForward leaveCarryForward, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        //Check the assignment is done or not .
        //if not assign the leaves first then add to carry forward ... 
        //if assigned and not carry forwarded , just update the Current year leaves

        leaveCarryForward.setCreateDate(System.currentTimeMillis() + "");
        leaveCarryForward.setUpdateDate(System.currentTimeMillis() + "");
        leaveCarryForward.setStatus(ApplicationConstants.ACTIVE);
        leaveCarryForward.setCreatedBy(userName);
        String leaveCarryForwardJson = new Gson().toJson(leaveCarryForward);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_CARRY_FORWARD, leaveCarryForwardJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    //Check the assignment is done or not .
    //if not > Assign the leaves first then add to carry forward ... assignmentStatus=BeforeAssignment 
    //if assigned and not carry forwarded , just update the Current year leaves..........assignmentStatus=AfterAssignment
    public boolean saveWithVaidatingRules(LeaveCarryForward leaveCarryForward, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.EMPLOYEE_ID, leaveCarryForward.getEmployeeId());
        conditionMap.put(ApplicationConstants.YEAR, leaveCarryForward.getToYear());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap);
        List<EmployeeLeaveAssignment> leaveAssignedList = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        if (leaveAssignedList != null && leaveAssignedList.size() > 0) {
            EmployeeLeaveAssignment empLeaveAssigned = leaveAssignedList.get(0);
            String leaveAssignmentobjId = ((LinkedTreeMap) empLeaveAssigned.getId()).get("$oid") + "";
//            empLeaveAssigned.setCurrentYearLeaves(leaveCarryForward.getCarryForwardingLeaves() + "");////////////////////////////Checking This
            empLeaveAssigned.setUpdateDate(System.currentTimeMillis() + "");
            empLeaveAssigned.setUpdatedBy(userName);
            String assignedObjId = ((LinkedTreeMap) empLeaveAssigned.getId()).get("$oid") + "";
            empLeaveAssigned.setId(null);

            //Setting Up the current year leaves starts here
            LeaveTypeMaster leaveObj = new LeaveTypeManager().fetchRawObject(leaveCarryForward.getLeaveType());
            List<LeaveTypeDetails> leaveTypeDetailsList = leaveObj.getLeaveTypeDetails();
//            String totalEarnedLeavesFromAssignmentInString = empLeaveAssigned.getTotalEarnedLeaves();// example 12
            String maxLeaveBalanceInString = "0";
            String maxLeavePerYearInString = "0";
            String carryForwardLimitPerInstanceInString = "0";
            //getting MaxLeave balance from the leave type

            for (Iterator<LeaveTypeDetails> it = leaveTypeDetailsList.iterator(); it.hasNext();) {
                LeaveTypeDetails leaveTypeDetails = it.next();
                if (leaveTypeDetails.getCarryForward().equals(ApplicationConstants.YES) && leaveTypeDetails.getEmployeeCategory().equals(leaveCarryForward.getEmployeeCategory()) && (leaveTypeDetails.getNatureType().equals(leaveCarryForward.getNatureType()))) {
                    maxLeaveBalanceInString = leaveTypeDetails.getMaxLeaveBalance();
                    maxLeavePerYearInString = leaveTypeDetails.getMaxLeavePerYear();
                    carryForwardLimitPerInstanceInString = leaveTypeDetails.getCarryForwardLimit();
                    break;
                }
            }
            double maxLeavePerYear = Integer.parseInt(maxLeavePerYearInString);
//            double totalEarnedLeavesFromAssignment = Double.parseDouble(totalEarnedLeavesFromAssignmentInString);
            int maxLeaveBalance = Integer.parseInt(maxLeaveBalanceInString);
            int carryForwardLimitPerInstance = Integer.parseInt(carryForwardLimitPerInstanceInString);
            double leavesAvailed = 0;
            try {
                leavesAvailed = Double.parseDouble(leaveCarryForward.getLeaveAvailed());//example 2
            } catch (Exception e) {
            }
            double leavesEligibleFromEarnedLeaves = Double.parseDouble(leaveCarryForward.getTotalEarnedLeaves()) - leavesAvailed;
            if (leavesEligibleFromEarnedLeaves > carryForwardLimitPerInstance) {
                leavesEligibleFromEarnedLeaves = carryForwardLimitPerInstance;
            }
            System.out.println("Eligible Leaves" + leavesEligibleFromEarnedLeaves);
            double carryforwardingleavesCount = 0;
            carryforwardingleavesCount = Double.parseDouble(leaveCarryForward.getCurrentYearLeaves()) + leavesEligibleFromEarnedLeaves;

            System.out.println("Carry Forward Count" + carryforwardingleavesCount);
            if (carryforwardingleavesCount + maxLeavePerYear > maxLeaveBalance) {
                carryforwardingleavesCount = maxLeaveBalance - maxLeavePerYear;
            }
            System.out.println("Setting up the leaves" + carryforwardingleavesCount);

            empLeaveAssigned.setCurrentYearLeaves(carryforwardingleavesCount + "");
            //Previous
//            double sumOfTotalearnedPLUSCurrentYearLeaves = totalEarnedLeavesFromAssignment + leaveCarryForward.getCarryForwardingLeaves();
//            if (sumOfTotalearnedPLUSCurrentYearLeaves > maxLeaveBalance) {
//                double diff = maxLeaveBalance - totalEarnedLeavesFromAssignment;
//                if (diff > carryForwardLimitPerInstance) {
//                    empLeaveAssigned.setCurrentYearLeaves(carryForwardLimitPerInstanceInString);
//                } else {
//                    empLeaveAssigned.setCurrentYearLeaves(diff + "");
//                }
//            } else {
//                if (leaveCarryForward.getCarryForwardingLeaves() > carryForwardLimitPerInstance) {
//                    empLeaveAssigned.setCurrentYearLeaves(carryForwardLimitPerInstanceInString);
//                } else {
//                    empLeaveAssigned.setCurrentYearLeaves(leaveCarryForward.getCarryForwardingLeaves() + "");
//                }
//            }

            //Setting Up the current year leaves ends here
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, assignedObjId, new Gson().toJson(empLeaveAssigned));
            if (flag == true) {
                leaveCarryForward.setAssignmentId(leaveAssignmentobjId);
                leaveCarryForward.setCreateDate(System.currentTimeMillis() + "");
                leaveCarryForward.setCreateDate(System.currentTimeMillis() + "");
                leaveCarryForward.setUpdateDate(System.currentTimeMillis() + "");
                leaveCarryForward.setStatus(ApplicationConstants.ACTIVE);
                leaveCarryForward.setCurrentYearLeaves(empLeaveAssigned.getCurrentYearLeaves());
                leaveCarryForward.setCreatedBy(userName);

                //This two Constants used for tracking , weather carry forward for that emoloyee is done before assignment or after assignment
                //While Unprocess of the carry forward, if After_assignment is there , then current year leaves=0
                //While Unprocess of the carry forward, if Before_assignment is there , delete the record in assignment with assignment id
                leaveCarryForward.setCarryForwardDone(ApplicationConstants.AFTER_ASSIGNMENT);

                //Inserting record in transaction table to keep track the balanace leaves
                LeaveTransaction leaveTransaction = new LeaveTransaction();
                leaveTransaction.setDataFrom(ApplicationConstants.LEAVE_CARRY_FORWARD);
                leaveTransaction.setDdo(leaveCarryForward.getDdo());
                leaveTransaction.setDepartment(leaveCarryForward.getDepartment());
                leaveTransaction.setDesignation(leaveCarryForward.getDesignation());
                leaveTransaction.setEarnedLeaves(leaveCarryForward.getTotalEarnedLeaves());
                leaveTransaction.setEmployeeCode(leaveCarryForward.getEmployeeCode());
                leaveTransaction.setEmployeeCodeM(leaveCarryForward.getEmployeeCodeM());
                leaveTransaction.setEmployeeId(leaveCarryForward.getEmployeeId());
                leaveTransaction.setEmployeeName(leaveCarryForward.getEmployeeName());
                leaveTransaction.setFinancialYear(leaveCarryForward.getFromYear());
                leaveTransaction.setLeaveType(leaveCarryForward.getLeaveType());
                leaveTransaction.setNatureType(leaveCarryForward.getNatureType());
                leaveTransaction.setPostingCity(leaveCarryForward.getPostingCity());
                leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
                leaveTransaction.setTotalAppliedLeaves(leaveCarryForward.getCurrentYearLeaves());
                leaveTransaction.setTotalLeaveDays(leaveCarryForward.getCurrentYearLeaves());
                double bal = leaveCarryForward.getCarryForwardingLeaves() - Double.parseDouble(leaveCarryForward.getCurrentYearLeaves());
                leaveTransaction.setTotalBalanceLeaves(bal + "");
                leaveTransaction.setCreatedBy(userName);
                leaveTransaction.setCreateDate(System.currentTimeMillis() + "");
                String leaveTransactionInsertedId = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TRANSACTION, new Gson().toJson(leaveTransaction));
                leaveCarryForward.setTransactionId(leaveTransactionInsertedId);
                if (leaveTransactionInsertedId != null && !leaveTransactionInsertedId.isEmpty()) {
                } else {
                    return false;
                }
                String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_CARRY_FORWARD, new Gson().toJson(leaveCarryForward));
                if (Id != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } else {
            EmployeeLeaveAssignment employeeLeaveAssigned = new EmployeeLeaveAssignment();
            employeeLeaveAssigned.setCreateDate(System.currentTimeMillis() + "");
            employeeLeaveAssigned.setUpdateDate(System.currentTimeMillis() + "");
            employeeLeaveAssigned.setCreatedBy(userName);
            employeeLeaveAssigned.setStatus(ApplicationConstants.ACTIVE);
            employeeLeaveAssigned.setDdo(leaveCarryForward.getDdo());
            employeeLeaveAssigned.setDesignation(leaveCarryForward.getDesignation());
            employeeLeaveAssigned.setEmployeeCategory(leaveCarryForward.getEmployeeCategory());
            employeeLeaveAssigned.setEmployeeCode(leaveCarryForward.getEmployeeCode());
            employeeLeaveAssigned.setEmployeeCodeM(leaveCarryForward.getEmployeeCodeM());
            employeeLeaveAssigned.setEmployeeId(leaveCarryForward.getEmployeeId());
            employeeLeaveAssigned.setEmployeeName(leaveCarryForward.getEmployeeName());
            employeeLeaveAssigned.setLeaveType(leaveCarryForward.getLeaveType());
            employeeLeaveAssigned.setNatureType(leaveCarryForward.getNatureType());
            employeeLeaveAssigned.setYear(leaveCarryForward.getToYear());
            //to set earned leaves
            LeaveTypeMaster leaveObj = new LeaveTypeManager().fetchRawObject(leaveCarryForward.getLeaveType());
            List<LeaveTypeDetails> leaveTypeDetailsList = leaveObj.getLeaveTypeDetails();
            String maxLeavePerYearInString = "0";
            String maxLeaveBalanceInString = "0";
            String carryForwardLimitPerInstanceInString = "0";
            //getting MaxLeave balance from the leave type

            for (Iterator<LeaveTypeDetails> it = leaveTypeDetailsList.iterator(); it.hasNext();) {
                LeaveTypeDetails leaveTypeDetails = it.next();
                if (leaveTypeDetails.getCarryForward().equals(ApplicationConstants.YES) && leaveTypeDetails.getEmployeeCategory().equals(leaveCarryForward.getEmployeeCategory()) && (leaveTypeDetails.getNatureType().equals(leaveCarryForward.getNatureType()))) {
                    maxLeavePerYearInString = leaveTypeDetails.getMaxLeavePerYear();
                    maxLeaveBalanceInString = leaveTypeDetails.getMaxLeaveBalance();
                    carryForwardLimitPerInstanceInString = leaveTypeDetails.getCarryForwardLimit();
                    break;
                }
            }
            employeeLeaveAssigned.setTotalEarnedLeaves(maxLeavePerYearInString);
            double maxLeavePerYear = Integer.parseInt(maxLeavePerYearInString);
            double maxLeaveBalance = Integer.parseInt(maxLeaveBalanceInString);
            int carryForwardLimitPerInstance = Integer.parseInt(carryForwardLimitPerInstanceInString);
            double leavesAvailed = 0;
            try {
                leavesAvailed = Double.parseDouble(leaveCarryForward.getLeaveAvailed());
            } catch (Exception e) {
            }
            double leavesEligibleFromEarnedLeaves = Double.parseDouble(leaveCarryForward.getTotalEarnedLeaves()) - leavesAvailed;
            if (leavesEligibleFromEarnedLeaves > carryForwardLimitPerInstance) {
                leavesEligibleFromEarnedLeaves = carryForwardLimitPerInstance;
            }
            System.out.println("Eligible Leaves" + leavesEligibleFromEarnedLeaves);
            double carryforwardingleavesCount = 0;
            carryforwardingleavesCount = Double.parseDouble(leaveCarryForward.getCurrentYearLeaves()) + leavesEligibleFromEarnedLeaves;

            System.out.println("Carry Forward Count" + carryforwardingleavesCount);
            if (carryforwardingleavesCount + maxLeavePerYear > maxLeaveBalance) {
                carryforwardingleavesCount = maxLeaveBalance - maxLeavePerYear;
            }
            System.out.println("Setting up the leaves" + carryforwardingleavesCount);

            employeeLeaveAssigned.setCurrentYearLeaves(carryforwardingleavesCount + "");
            //Previous Logic
//            double sumOfTotalearnedPLUSCurrentYearLeaves = maxLeavePerYear + leaveCarryForward.getCarryForwardingLeaves();
//            if (sumOfTotalearnedPLUSCurrentYearLeaves > maxLeaveBalance) {
//                double diff = maxLeaveBalance - maxLeavePerYear;
//                if (diff > carryForwardLimitPerInstance) {
//                    employeeLeaveAssigned.setCurrentYearLeaves(carryForwardLimitPerInstanceInString);
//                } else {
//                    employeeLeaveAssigned.setCurrentYearLeaves(diff + "");
//                }
//            } else {
//                if (leaveCarryForward.getCarryForwardingLeaves() > carryForwardLimitPerInstance) {
//                    employeeLeaveAssigned.setCurrentYearLeaves(carryForwardLimitPerInstanceInString);
//                } else {
//                    employeeLeaveAssigned.setCurrentYearLeaves(leaveCarryForward.getCarryForwardingLeaves() + "");
//                }
//            }
            String resultid = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, new Gson().toJson(employeeLeaveAssigned));

            if (resultid != null) {
                leaveCarryForward.setAssignmentId(resultid);
                leaveCarryForward.setCurrentYearLeaves(carryforwardingleavesCount + "");
                //This two Constants used for tracking , weather carry forward for that emoloyee is done before assignment or after assignment
                //While Unprocess of the carry forward, if After_assignment is there , then current year leaves=0
                //While Unprocess of the carry forward, if Before_assignment is there , delete the record in assignment with assignmentId
                leaveCarryForward.setCarryForwardDone(ApplicationConstants.BEFORE_ASSIGNMENT);

                //Inserting record in transaction table to keep track the balanace leaves
                LeaveTransaction leaveTransaction = new LeaveTransaction();
                leaveTransaction.setDataFrom(ApplicationConstants.LEAVE_CARRY_FORWARD);
                leaveTransaction.setDdo(leaveCarryForward.getDdo());
                leaveTransaction.setDepartment(leaveCarryForward.getDepartment());
                leaveTransaction.setDesignation(leaveCarryForward.getDesignation());
                leaveTransaction.setEarnedLeaves(leaveCarryForward.getTotalEarnedLeaves());
                leaveTransaction.setEmployeeCode(leaveCarryForward.getEmployeeCode());
                leaveTransaction.setEmployeeCodeM(leaveCarryForward.getEmployeeCodeM());
                leaveTransaction.setEmployeeId(leaveCarryForward.getEmployeeId());
                leaveTransaction.setEmployeeName(leaveCarryForward.getEmployeeName());
                leaveTransaction.setFinancialYear(leaveCarryForward.getFromYear());
                leaveTransaction.setLeaveType(leaveCarryForward.getLeaveType());
                leaveTransaction.setNatureType(leaveCarryForward.getNatureType());
                leaveTransaction.setPostingCity(leaveCarryForward.getPostingCity());
                leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
                leaveTransaction.setTotalAppliedLeaves(leaveCarryForward.getCurrentYearLeaves());
                leaveTransaction.setTotalLeaveDays(leaveCarryForward.getCurrentYearLeaves());
                double bal = leaveCarryForward.getCarryForwardingLeaves() - Double.parseDouble(leaveCarryForward.getCurrentYearLeaves());
                leaveTransaction.setTotalBalanceLeaves(bal + "");
                leaveTransaction.setCreatedBy(userName);
                leaveTransaction.setCreateDate(System.currentTimeMillis() + "");
                String leaveTransactionInsertedId = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TRANSACTION, new Gson().toJson(leaveTransaction));
                leaveCarryForward.setTransactionId(leaveTransactionInsertedId);
                if (leaveTransactionInsertedId != null && !leaveTransactionInsertedId.isEmpty()) {
                } else {
                    return false;
                }
                String carryForwardId = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_CARRY_FORWARD, new Gson().toJson(leaveCarryForward));
                if (carryForwardId != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_CARRY_FORWARD, Id);
        List<LeaveCarryForward> leaveCarryForwardList = new Gson().fromJson(result, new TypeToken<List<LeaveCarryForward>>() {
        }.getType());
        if (leaveCarryForwardList == null || leaveCarryForwardList.size() < 1) {
            return null;
        }
        return new Gson().toJson(leaveCarryForwardList.get(0));
    }

    public LeaveCarryForward fetchDataObject(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_CARRY_FORWARD, Id);
        List<LeaveCarryForward> leaveCarryForwardList = new Gson().fromJson(result, new TypeToken<List<LeaveCarryForward>>() {
        }.getType());
        if (leaveCarryForwardList == null || leaveCarryForwardList.size() < 1) {
            return null;
        }
        return leaveCarryForwardList.get(0);
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<LeaveCarryForward>() {

        }.getType();
        String leaveCarryForward = new LeaveCarryForwardManager().fetch(Id);
        if (leaveCarryForward == null || leaveCarryForward.isEmpty()) {
            return false;
        }
        LeaveCarryForward leaveCarryForwardrJson = new Gson().fromJson(leaveCarryForward, type);
        leaveCarryForwardrJson.setStatus(ApplicationConstants.INACTIVE);
        leaveCarryForwardrJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_CARRY_FORWARD, Id, new Gson().toJson(leaveCarryForwardrJson));
        return result;
    }

    public boolean update(LeaveCarryForward leaveCarryForward, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        leaveCarryForward.setUpdateDate(System.currentTimeMillis() + "");
        leaveCarryForward.setStatus(ApplicationConstants.ACTIVE);
        leaveCarryForward.setUpdatedBy(userName);
        String leaveCarryForwardrJson = new Gson().toJson(leaveCarryForward);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_CARRY_FORWARD, Id, leaveCarryForwardrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_CARRY_FORWARD, conditionMap);
        return result1;
    }

    public String getSearchResult(String EmpCarryForwardJsonString) throws Exception {
        System.out.println("Inside" + EmpCarryForwardJsonString);
        String leaveAssignment = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT + "`";

        if (EmpCarryForwardJsonString == null) {
            return null;
        }
        RestClient aql = new RestClient();

        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        String empLeaveCarryForward = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_CARRY_FORWARD + "`";

        String EmpCodeIds = "";
        String employeeQuery = "";
        String assignmentQuery = "";
        String carryForwardQuery = "";

        LeaveCarryForward empobj = new Gson().fromJson(EmpCarryForwardJsonString, new TypeToken<LeaveCarryForward>() {
        }.getType());
        employeeQuery = " emp.status=\"" + ApplicationConstants.ACTIVE + "\"";
        if (empobj.getEmployeeCode() != null && empobj.getEmployeeCode() != "" && !empobj.getEmployeeCode().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.employeeCode like \"" + empobj.getEmployeeCode() + "%\"";
        }
        if (empobj.getEmployeeName() != null && empobj.getEmployeeName() != "" && !empobj.getEmployeeName().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.employeeName=\"" + empobj.getEmployeeName() + "\"";
        }
        if (empobj.getEmployeeCodeM() != null && empobj.getEmployeeCodeM() != "" && !empobj.getEmployeeCodeM().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.employeeCodeM=\"" + empobj.getEmployeeCodeM() + "\"";
        }
        if (empobj.getDdo() != null && empobj.getDdo() != "" && !empobj.getDdo().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.ddo=\"" + empobj.getDdo() + "\"";
        }
        if (empobj.getLocation() != null && !empobj.getLocation().isEmpty() && !empobj.getLocation().equals("0")) {
            employeeQuery = employeeQuery + " and emp.location=\"" + empobj.getLocation() + "\"";
        }
        if (empobj.getDepartment() != null && !empobj.getDepartment().isEmpty() && !empobj.getDepartment().equals("0")) {
            employeeQuery = employeeQuery + " and emp.department=\"" + empobj.getDepartment() + "\"";
        }
        if (empobj.getDesignation() != null && !empobj.getDesignation().isEmpty() && !empobj.getDesignation().equals("0")) {
            employeeQuery = employeeQuery + " and emp.designation=\"" + empobj.getDesignation() + "\"";
        }
        if (empobj.getNatureType() != null && !empobj.getNatureType().isEmpty() && !empobj.getNatureType().equals("0")) {
            employeeQuery = employeeQuery + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
        }
        if (empobj.getEmployeeCategory() != null && !empobj.getEmployeeCategory().isEmpty() && !empobj.getEmployeeCategory().equals("0")) {
            employeeQuery = employeeQuery + " and emp.classMaster=\"" + empobj.getEmployeeCategory() + "\"";
        }
        if (empobj.getPostingCity() != null && !empobj.getPostingCity().isEmpty() && !empobj.getPostingCity().equals("0")) {
            employeeQuery = employeeQuery + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
        }

        List<Employee> empList = null;
        List<EmployeeLeaveAssignment> assignedList = null;
        List<LeaveCarryForward> carryForwardedList = null;
        String empSearch = "select emp._id as idStr ,designation,ddo,department,natureType,employeeCode,employeeCodeM,employeeName  from " + empTable + ""
                + " as emp where " + employeeQuery;
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(empSearch);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
        String employeeSearch = null;
        try {
            employeeSearch = aql.getRestData(ApplicationConstants.END_POINT, empSearch);
            System.out.println(employeeSearch);
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.equalsIgnoreCase("Failed : HTTP error code : 404")) {

            }
        }
        System.out.println("-----------------------------------------------------------Employee Starts Search----------------------------------------------------------------------------");
        System.out.println(employeeSearch);
        System.out.println("-----------------------------------------------------------Employee Ends Search----------------------------------------------------------------------------");
        empList = new Gson().fromJson(employeeSearch, new TypeToken<List<Employee>>() {
        }.getType());
        HashMap<String, Employee> employeemap = new HashMap<String, Employee>();
        for (Employee as : empList) {
            EmpCodeIds = EmpCodeIds + "\"" + as.getIdStr() + "\",";
            employeemap.put(as.getIdStr(), as);
        }
        if (EmpCodeIds != null && !EmpCodeIds.isEmpty()) {
            EmpCodeIds = "(" + EmpCodeIds.substring(0, EmpCodeIds.length() - 1) + ")";
        }
        assignmentQuery = "select employeeId,totalEarnedLeaves,currentYearLeaves  from " + leaveAssignment + ""
                + " as emp where  emp.employeeId in " + EmpCodeIds + " and year=\"" + empobj.getFromYear() + "\" and leaveType=\"" + empobj.getLeaveType() + "\"";

        carryForwardQuery = "select emp._id as idString,employeeId,designation,ddo,natureType,totalEarnedLeaves,currentYearLeaves,employeeName,employeeCode,employeeCodeM  from " + empLeaveCarryForward + ""
                + " as emp where  emp.employeeId in " + EmpCodeIds + " and fromYear=\"" + empobj.getFromYear() + "\" and toYear=\"" + empobj.getToYear() + "\" and leaveType=\"" + empobj.getLeaveType() + "\"";
        String employeeLeaveAssignedList = null;
        try {
            employeeLeaveAssignedList = aql.getRestData(ApplicationConstants.END_POINT, assignmentQuery);
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.equalsIgnoreCase("Failed : HTTP error code : 404")) {
            }
        }
        System.out.println("-----------------------------------------------------------Employee Starts Leave Query ----------------------------------------------------------------------------");
        System.out.println(assignmentQuery);
        System.out.println("-----------------------------------------------------------Employee Starts Leave Assignment----------------------------------------------------------------------------");
        System.out.println(employeeLeaveAssignedList);
        System.out.println("-----------------------------------------------------------Employee Ends Leave Assignment----------------------------------------------------------------------------");
        String employeeLeaveCarryForwardedList = null;
        try {
            employeeLeaveCarryForwardedList = aql.getRestData(ApplicationConstants.END_POINT, carryForwardQuery);
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.equalsIgnoreCase("Failed : HTTP error code : 404")) {
            }
        }
        System.out.println("-----------------------------------------------------------Employee Leave Carry Forward----------------------------------------------------------------------------");
        System.out.println(employeeLeaveCarryForwardedList);
        System.out.println("-----------------------------------------------------------Employee Leave Carry Forward----------------------------------------------------------------------------");

        assignedList = new Gson().fromJson(employeeLeaveAssignedList, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());

        carryForwardedList = new Gson().fromJson(employeeLeaveCarryForwardedList, new TypeToken<List<LeaveCarryForward>>() {
        }.getType());
        boolean condition = false;
        if (carryForwardedList != null && carryForwardedList.size() > 0) {
            condition = true;
        }
        if (condition != false) {
            for (Iterator<LeaveCarryForward> it = carryForwardedList.iterator(); it.hasNext();) {
                LeaveCarryForward leaveCarryForward = it.next();
                if (assignedList != null && assignedList.size() > 0) {
                    for (Iterator<EmployeeLeaveAssignment> itt = assignedList.iterator(); itt.hasNext();) {
                        EmployeeLeaveAssignment employeeLeaveAssignment = itt.next();
                        if (employeeLeaveAssignment.getEmployeeId().equals(leaveCarryForward.getEmployeeId())) {
                            itt.remove();
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(assignedList.size());
        List<LeaveCarryForward> notCarryForwardedFinalList = new ArrayList<LeaveCarryForward>();
        System.out.println("last for loop");
        if (assignedList != null && assignedList.size() > 0) {
            for (Iterator<EmployeeLeaveAssignment> itt = assignedList.iterator(); itt.hasNext();) {
                EmployeeLeaveAssignment employeeLeaveAssignment = itt.next();
                System.out.println(employeeLeaveAssignment.getEmployeeId() + "&&&&&&&&&&&&&&&&&&&&&7");
                if (employeemap.get(employeeLeaveAssignment.getEmployeeId()) != null) {
                    System.out.println("Hello Hello");
                    LeaveCarryForward lcf = new LeaveCarryForward();
                    Employee em = employeemap.get(employeeLeaveAssignment.getEmployeeId());
                    lcf.setEmployeeId(employeeLeaveAssignment.getEmployeeId());
                    lcf.setEmployeeName(em.getEmployeeName());
                    lcf.setEmployeeCodeM(em.getEmployeeCodeM());
                    lcf.setEmployeeCode(em.getEmployeeCode());
                    lcf.setNatureType(em.getNatureType());
                    lcf.setDdo(em.getDdo());
                    lcf.setDesignation(em.getDesignation());
                    lcf.setNatureType(em.getNatureType());
                    lcf.setCurrentYearLeaves(employeeLeaveAssignment.getCurrentYearLeaves());
                    lcf.setTotalEarnedLeaves(employeeLeaveAssignment.getTotalEarnedLeaves());
                    lcf.setEmployeeCategory(empobj.getEmployeeCategory());
                    lcf.setFromYear(empobj.getFromYear());
                    lcf.setToYear(empobj.getToYear());
                    lcf.setLeaveType(empobj.getLeaveType());
                    notCarryForwardedFinalList.add(lcf);
                }
            }
        }
        if (notCarryForwardedFinalList != null && notCarryForwardedFinalList.size() > 0) {
            notCarryForwardedFinalList = setAvailedLeaves(notCarryForwardedFinalList, empobj.getLeaveType(), empobj.getFromYear());
//            notCarryForwardedFinalList = setCarryForwardLeaves(notCarryForwardedFinalList, empobj);
            notCarryForwardedFinalList = setDesignationName(notCarryForwardedFinalList);
        }
        carryForwardedList = setDDODesignationNatureTypeToCarryForwardedList(carryForwardedList);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("notCarryForwarded", notCarryForwardedFinalList);
        resultMap.put("CarryForwarded", carryForwardedList);

        return new Gson().toJson(resultMap);
    }

    private static List<LeaveCarryForward> setAvailedLeaves(List<LeaveCarryForward> notCarryForwardedFinalList, String leaveType, String year) {
        Map<String, String> filter = new HashMap();
        for (Iterator<LeaveCarryForward> it = notCarryForwardedFinalList.iterator(); it.hasNext();) {
            LeaveCarryForward leaveCarryForward = it.next();
            filter.put("employeeId", leaveCarryForward.getEmployeeId());
            filter.put("leaveType", leaveType);
            filter.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            filter.put("financialYear", year);
            System.out.println(new Gson().toJson(filter));

            try {
                String transactionLisr = DBManager.getDbConnection().fetchAllRowsByConditions(
                        ApplicationConstants.LEAVE_TRANSACTION, filter);

                if (transactionLisr != null) {
                    List<LeaveTransaction> lveTrnsctnLst = new Gson().fromJson(transactionLisr, new TypeToken<List<LeaveTransaction>>() {
                    }.getType());
                    double totalAvailedLeaves = 0;
                    if (lveTrnsctnLst != null && lveTrnsctnLst.size() > 0) {
                        for (Iterator<LeaveTransaction> itte = lveTrnsctnLst.iterator(); itte.hasNext();) {
                            LeaveTransaction leaveTransaction = itte.next();
                            System.out.println("---------------------------------------------------------------------");
                            totalAvailedLeaves = totalAvailedLeaves + Double.parseDouble(leaveTransaction.getTotalLeaveDays());
                            System.out.println(totalAvailedLeaves);
                        }
                        leaveCarryForward.setLeaveAvailed(totalAvailedLeaves + "");
                    } else {
                        leaveCarryForward.setLeaveAvailed(totalAvailedLeaves + "");
                    }
                } else {
                    leaveCarryForward.setLeaveAvailed("0");
                }
            } catch (Exception exception) {
                leaveCarryForward.setLeaveAvailed("0");
            }
        }
        return notCarryForwardedFinalList;

    }

    private List<LeaveCarryForward> setCarryForwardLeaves(List<LeaveCarryForward> notCarryForwardedFinalList, LeaveCarryForward empSearchObj) throws Exception {
        LeaveTypeMaster leaveObj = new LeaveTypeManager().fetchRawObject(empSearchObj.getLeaveType());
        List<LeaveTypeDetails> leaveTypeDetailsList = leaveObj.getLeaveTypeDetails();
        String maxLeaveBalanceInString = "0";
        //getting MaxLeave balance from the leave type
        for (Iterator<LeaveTypeDetails> it = leaveTypeDetailsList.iterator(); it.hasNext();) {
            LeaveTypeDetails leaveTypeDetails = it.next();
            if (leaveTypeDetails.getCarryForward().equals(ApplicationConstants.YES) && leaveTypeDetails.getEmployeeCategory().equals(empSearchObj.getEmployeeCategory()) && (leaveTypeDetails.getNatureType().equals(empSearchObj.getNatureType()))) {
                maxLeaveBalanceInString = leaveTypeDetails.getMaxLeaveBalance();
            }
        }
        double MaxLeaveBalance = Double.parseDouble(maxLeaveBalanceInString);

        for (Iterator<LeaveCarryForward> it = notCarryForwardedFinalList.iterator(); it.hasNext();) {
            LeaveCarryForward leaveCarryForward = it.next();
            //if he availed more than earning leaves , he cant able to forward his leaves
            if (Double.parseDouble(leaveCarryForward.getLeaveAvailed()) >= Double.parseDouble(leaveCarryForward.getTotalEarnedLeaves())) {
                it.remove();
            } else {
                double balanceLeaves = Double.parseDouble(leaveCarryForward.getTotalEarnedLeaves()) + Double.parseDouble(leaveCarryForward.getCurrentYearLeaves()) - Double.parseDouble(leaveCarryForward.getLeaveAvailed());
                if (balanceLeaves <= MaxLeaveBalance) {
                    leaveCarryForward.setCarryForwardingLeaves(balanceLeaves);
                } else {
                    leaveCarryForward.setCarryForwardingLeaves(MaxLeaveBalance);
                }
            }
        }
        return notCarryForwardedFinalList;
    }

    private List<LeaveCarryForward> setDesignationName(List<LeaveCarryForward> notCarryForwardedFinalList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        List<Designation> religionList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        for (Iterator<Designation> iterator = religionList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        for (int i = 0; i < notCarryForwardedFinalList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(notCarryForwardedFinalList.get(i).getDesignation())) {
                    notCarryForwardedFinalList.get(i).setDesignationName(entry.getValue());
                }
            }
        }
        return notCarryForwardedFinalList;
    }

    private List<LeaveCarryForward> setDDODesignationNatureTypeToCarryForwardedList(List<LeaveCarryForward> carryForwardedList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        Map<String, String> DDOMap = new HashMap<String, String>();
        Map<String, String> NatureMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        String resultDDO = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO);
        String resultNature = DBManager.getDbConnection().fetchAll(ApplicationConstants.NATURE_TABLE);
        List<Designation> designationList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        List<DDO> ddoList = new Gson().fromJson(resultDDO, new TypeToken<List<DDO>>() {
        }.getType());
        List<Nature> natureList = new Gson().fromJson(resultNature, new TypeToken<List<Nature>>() {
        }.getType());
        for (Iterator<Designation> iterator = designationList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        for (Iterator<DDO> iterator = ddoList.iterator(); iterator.hasNext();) {
            DDO next1 = iterator.next();
            DDOMap.put(((LinkedTreeMap<String, String>) next1.getId()).get("$oid"), next1.getDdoName());
        }
        for (Iterator<Nature> iterator = natureList.iterator(); iterator.hasNext();) {
            Nature next2 = iterator.next();
            NatureMap.put(((LinkedTreeMap<String, String>) next2.getId()).get("$oid"), next2.getNatureName());
        }
        for (int i = 0; i < carryForwardedList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(carryForwardedList.get(i).getDesignation())) {
                    carryForwardedList.get(i).setDesignation(entry.getValue());
                }
            }
            for (Map.Entry<String, String> entry : DDOMap.entrySet()) {
                if (entry.getKey().equals(carryForwardedList.get(i).getDdo())) {
                    carryForwardedList.get(i).setDdo(entry.getValue());
                }
            }
            for (Map.Entry<String, String> entry : NatureMap.entrySet()) {
                if (entry.getKey().equals(carryForwardedList.get(i).getNatureType())) {
                    carryForwardedList.get(i).setNatureType(entry.getValue());
                }
            }
        }
        return carryForwardedList;
    }

    public boolean upProcess(String next, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        LeaveCarryForward leavecarryForward = new LeaveCarryForwardManager().fetchDataObject(next);
        //This transaction id is used to delete transaction record which is inserted during carry forward
        String leaveTransactionid = leavecarryForward.getTransactionId();

        String leaveAssignmentObjId = leavecarryForward.getAssignmentId();
        if (leavecarryForward.getCarryForwardDone().equals(ApplicationConstants.AFTER_ASSIGNMENT)) {
            EmployeeLeaveAssignment leaveAssignmentObj = new EmployeeLeaveAssignmentManager().fetchObject(leaveAssignmentObjId);
            leaveAssignmentObj.setCurrentYearLeaves("0");
            leaveAssignmentObj.setUpdateDate(System.currentTimeMillis() + "");
            leaveAssignmentObj.setUpdatedBy(userName);
            boolean flagforUpdate = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, leaveAssignmentObjId, new Gson().toJson(leaveAssignmentObj));
            if (flagforUpdate == true) {
                boolean flagForDelete = DBManager.getDbConnection().delete(ApplicationConstants.LEAVE_CARRY_FORWARD, next);
                boolean flagOfDeleteFromTransaction = DBManager.getDbConnection().delete(ApplicationConstants.LEAVE_TRANSACTION, leaveTransactionid);
                if (flagForDelete == true && flagOfDeleteFromTransaction == true) {
                    return flagForDelete;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (leavecarryForward.getCarryForwardDone().equals(ApplicationConstants.BEFORE_ASSIGNMENT)) {
            boolean flagForDeleteassignment = DBManager.getDbConnection().delete(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, leaveAssignmentObjId);
            if (flagForDeleteassignment) {
                boolean flagForDeletecarryForward = DBManager.getDbConnection().delete(ApplicationConstants.LEAVE_CARRY_FORWARD, next);
                boolean flagOfDeleteFromTransaction = DBManager.getDbConnection().delete(ApplicationConstants.LEAVE_TRANSACTION, leaveTransactionid);
                if (flagForDeletecarryForward == true && flagOfDeleteFromTransaction == true) {
                    return flagForDeletecarryForward;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
