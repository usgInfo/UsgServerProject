/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.hrms.dto.Employee;
import com.accure.hrms.manager.EmployeeManager;
import com.accure.leave.dto.LeaveRequest;
import com.accure.leave.dto.LeaveRequestDetails;
import com.accure.leave.dto.LeaveTypeDetails;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
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

/**
 *
 * @author User
 * @author Asif
 */
public class LeaveRequestManager {

    public String saveLeaveRequest(LeaveRequest leaveRequest) throws Exception {
        //  User user = new UserManager().fetch(loginUserId);
        // String userName = user.getFname() + " " + user.getLname();
        leaveRequest.setCreateDate(System.currentTimeMillis() + "");
        leaveRequest.setRequestedDate(System.currentTimeMillis() + "");
        leaveRequest.setUpdateDate(System.currentTimeMillis() + "");
        leaveRequest.setStatus(ApplicationConstants.ACTIVE);
        leaveRequest.setApprovedDate(System.currentTimeMillis() + "");
        //  leaveRequest.setCreatedBy(userName);
        String leaveRequestJson = new Gson().toJson(leaveRequest);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_REQUEST, leaveRequestJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String fetchLeaveRequest(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_REQUEST, Id);
        List<LeaveRequest> leaveRequestList = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        if (leaveRequestList == null || leaveRequestList.size() < 1) {
            return null;
        }
        return new Gson().toJson(leaveRequestList.get(0));
    }

    public String fetchLeaveRequest(String employeeName, String leaveType) throws Exception {
        if (employeeName == null || employeeName.isEmpty() && leaveType == null || leaveType.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_REQUEST, employeeName);
        List<LeaveRequest> leaveRequestList = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        if (leaveRequestList == null || leaveRequestList.size() < 1) {
            return null;
        }
        return new Gson().toJson(leaveRequestList.get(0));
    }

    public boolean deleteLeaveRequest(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        //User user = new UserManager().fetch(loginUserId);
        //String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<LeaveRequest>() {
        }.getType();
        String leaveRequest = new LeaveRequestManager().fetchLeaveRequest(Id);
        if (leaveRequest == null || leaveRequest.isEmpty()) {
            return false;
        }
        LeaveRequest leaveRequesterJson = new Gson().fromJson(leaveRequest, type);
        leaveRequesterJson.setStatus(ApplicationConstants.INACTIVE);
        //leaveRequesterJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_REQUEST, Id, new Gson().toJson(leaveRequesterJson));
        return result;
    }

    public boolean updateLeaveRequest(LeaveRequest leaveRequest, String Id) throws Exception {
        //User user = new UserManager().fetch(loginUserId);
        //String userName = user.getFname() + " " + user.getLname();
        leaveRequest.setUpdateDate(System.currentTimeMillis() + "");
        leaveRequest.setStatus(ApplicationConstants.ACTIVE);
        //leaveRequest.setUpdatedBy(userName);
        String leaveRequesterJson = new Gson().toJson(leaveRequest);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_REQUEST, Id, leaveRequesterJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
        List<LeaveRequest> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        leaveRequestList = getEmployeeName(leaveRequestList);
        return result1;
    }

    private List<LeaveRequest> getEmployeeName(List<LeaveRequest> leaveRequestList) throws Exception {
        Map<String, String> LeaveRequestMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_REQUEST);
        List<LeaveRequest> list = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        for (Iterator<LeaveRequest> iterator = list.iterator(); iterator.hasNext();) {
            LeaveRequest next = iterator.next();
            LeaveRequestMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeName());
        }
        for (int i = 0; i < leaveRequestList.size(); i++) {
            for (Map.Entry<String, String> entry : LeaveRequestMap.entrySet()) {
                if (entry.getKey().equals(leaveRequestList.get(i).getEmployeeName())) {
                    leaveRequestList.get(i).setEmployeeName(entry.getValue());
                }
            }
        }
        return leaveRequestList;
    }

    public String fetchAllEmployeeLeaves(String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("employeeCode", employeeCode);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
        List<LeaveRequest> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        leaveRequestList = getEmployeeName(leaveRequestList);
        return result1;
    }

    public String fetchLeavesApprovedByEmployeeId(String employeeId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("employeeId", employeeId);
        conditionMap.put("leaveStatus", ApplicationConstants.APPROVED);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
        List<LeaveRequest> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        leaveRequestList = getEmployeeName(leaveRequestList);
        return result1;
    }

    public String fetchAllEmployeeLeavesByEmpIdLeaveTypeId(String employeeId, String leaveTypeId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("employeeId", employeeId);
        conditionMap.put("leaveTypeId", leaveTypeId);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
        List<LeaveRequest> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        int result = 0;
        for (Iterator<LeaveRequest> iterator = leaveRequestList.iterator(); iterator.hasNext();) {
            LeaveRequest next = iterator.next();
            result = result + Integer.parseInt(next.getTotalLeaveDays());
        }

        //System.out.println("Total leaves Applied" + result);
        leaveRequestList = getEmployeeName(leaveRequestList);

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("totalnoOfleavesApplied", result);
        map.put("appliedLeave", 0);
        return new Gson().toJson(map);
    }

    public List<LeaveRequestDetails> updateEmployeeLeavesByEmpIdLeaveTypeId(List<LeaveRequestDetails> leaverequestDetails, String employeeId) throws Exception {

        for (Iterator<LeaveRequestDetails> iterator = leaverequestDetails.iterator(); iterator.hasNext();) {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            LeaveRequestDetails next = iterator.next();
            String leaveTypeId = next.getLeaveTypeId();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("employeeId", employeeId);
            conditionMap.put("leaveTypeId", leaveTypeId);
            next.setEmployeeId(employeeId);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
            List<LeaveRequest> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveRequest>>() {
            }.getType());
            int approvedLeaves = 0;
            int appliedLeaves = 0;
            int adjustedLeavesRequested = 0;
            int adjustedLeavesApproved = 0;
            try {
                if (leaveRequestList.size() > 0) {
                    for (Iterator<LeaveRequest> iterator1 = leaveRequestList.iterator(); iterator1.hasNext();) {
                        LeaveRequest next1 = iterator1.next();
                        if (next1.getLeaveStatus().equalsIgnoreCase(ApplicationConstants.LEAVE_APPROVED)) {
                            approvedLeaves = approvedLeaves + Integer.parseInt(next1.getTotalLeaveDays());
                        } else if (next1.getLeaveStatus().equalsIgnoreCase(ApplicationConstants.LEAVE_APPLIED)) {
                            appliedLeaves = appliedLeaves + Integer.parseInt(next1.getTotalLeaveDays());
                        } else if (next1.getLeaveStatus().equalsIgnoreCase(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST)) {
                            adjustedLeavesRequested = adjustedLeavesRequested + Integer.parseInt(next1.getTotalLeaveDays());
                        } else if (next1.getLeaveStatus().equalsIgnoreCase(ApplicationConstants.LEAVE_ADJUSTMENT_APPROVAL)) {
                            adjustedLeavesApproved = adjustedLeavesApproved + Integer.parseInt(next1.getTotalLeaveDays());
                        }
                        next.setTotalApprovedLeaves(Integer.toString(approvedLeaves));
                        next.setTotalAppliedLeaves(Integer.toString(appliedLeaves));
                        next.setAdjustedLeavesRequested(Integer.toString(adjustedLeavesRequested));
                        next.setAdjustedLeavesApproved(Integer.toString(adjustedLeavesApproved));
                    }
                }
                next.setBalanceLeaves(Integer.toString(Integer.parseInt(next.getMaxLeavePerYear()) - Integer.parseInt(next.getTotalApprovedLeaves())));
                next.setAppliedBalanceLeaves(Integer.toString(Integer.parseInt(next.getMaxLeavePerYear()) - Integer.parseInt(next.getTotalApprovedLeaves()) - Integer.parseInt(next.getTotalAppliedLeaves())));
            } catch (Exception e) {
                next.setTotalApprovedLeaves(Integer.toString(approvedLeaves));
                next.setTotalAppliedLeaves(Integer.toString(appliedLeaves));
                next.setAdjustedLeavesRequested(Integer.toString(adjustedLeavesRequested));
                next.setAdjustedLeavesApproved(Integer.toString(adjustedLeavesApproved));
                next.setBalanceLeaves(Integer.toString(Integer.parseInt(next.getMaxLeavePerYear()) - Integer.parseInt(next.getTotalApprovedLeaves()) - Integer.parseInt(next.getAdjustedLeavesApproved())));
                next.setAppliedBalanceLeaves(Integer.toString(Integer.parseInt(next.getMaxLeavePerYear()) - Integer.parseInt(next.getTotalApprovedLeaves()) - Integer.parseInt(next.getTotalAppliedLeaves()) - Integer.parseInt(next.getAdjustedLeavesApproved())));
            }
        }
        return leaverequestDetails;
    }

    public String fetchAllLeaveTypesByEmpId(String employeeId) throws Exception {
        List<LeaveRequestDetails> listOfLeavesTobeSent = new ArrayList<LeaveRequestDetails>();

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("employeeId", employeeId);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
        //getting leaves details of employee
        List<LeaveRequest> leavedetailsListofEmploy = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());

        // getting leave Assigned to employee
        List<LeaveRequestDetails> leaveTypeList = getAllLeaveTypesAssignedToEmployee(employeeId);
        //System.out.println("********************************************************************************");
        //System.out.println(new Gson().toJson(leaveTypeList));
        //System.out.println("********************************************************************************");

        leaveTypeList = updateEmployeeLeavesByEmpIdLeaveTypeId(leaveTypeList, employeeId);

        //getting applied and approved leaves of this employee
//        for (Iterator<LeaveTypeMaster> iterator = leaveTypeList.iterator(); iterator.hasNext();) {
//            LeaveRequestDetails leaverequestDetails = new LeaveRequestDetails();
//            LeaveTypeMaster next = iterator.next();
//            leaverequestDetails = getTotalLeavesApprovedAppliedInThisLeaveType(employeeId, next);
//            leaverequestDetails.setEmployeeId(employeeId);
//            listOfLeavesTobeSent.add(leaverequestDetails);
//        }
        return new Gson().toJson(leaveTypeList);
    }

    private List<LeaveRequestDetails> getAllLeaveTypesAssignedToEmployee(String employeeId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String emp = new EmployeeManager().fetch(employeeId);
        Type type = new TypeToken<Employee>() {
        }.getType();
        Employee employeeObj = new Gson().fromJson(emp, type);

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TYPE_MASTER, conditionMap);
        //System.out.println(result1);
        //System.out.println("********************************************************************************");
        List<LeaveRequestDetails> leaverequestDetails = new ArrayList<LeaveRequestDetails>();
        List<LeaveTypeMaster> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        for (Iterator<LeaveTypeMaster> iterator = leaveRequestList.iterator(); iterator.hasNext();) {
            LeaveTypeMaster next = iterator.next();
            List<LeaveTypeDetails> listofLeaveDetails = next.getLeaveTypeDetails();
            for (Iterator<LeaveTypeDetails> iterator1 = listofLeaveDetails.iterator(); iterator1.hasNext();) {
                LeaveTypeDetails next1 = iterator1.next();
                if (next1.getEmployeeCategory().equalsIgnoreCase(employeeObj.getSalaryBillType())) {
                    LeaveRequestDetails lrd = new LeaveRequestDetails();
                    lrd.setMaxLeavePerYear(next1.getMaxLeavePerYear());
                    lrd.setLeavetypeName(next.getLeaveType());
                    lrd.setEmployeeName(employeeObj.getEmployeeName());
                    lrd.setEmployeeCode(employeeObj.getEmployeeCode());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), "");
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        lrd.setLeaveTypeId(entry.getKey());
                    }
                    leaverequestDetails.add(lrd);
                }
            }
        }
        return leaverequestDetails;
    }

    public static void main(String[] args) throws Exception {
//        new LeaveRequestManager().fetchAllEmployeeLeavesByEmpIdLeaveTypeId("57a099eeaaf9160a7259556e", "5790d6616e6299d60c39f375");
        //System.out.println(new Gson().toJson(new LeaveRequestManager().fetchAllLeaveTypesByEmpId("579a42943c6c312cd3f89c77")));

    }

    private LeaveRequestDetails getTotalLeavesApprovedAppliedInThisLeaveType(String employeeId, LeaveTypeMaster leaveType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean updateAdjustmentDetails(LeaveRequest leaveRequest, String Id, String userId) throws Exception {
        String lr = new LeaveRequestManager().fetchLeaveRequest(Id);
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        LeaveRequest leaverequestObj = new Gson().fromJson(lr, new TypeToken<LeaveRequest>() {
        }.getType());
        leaverequestObj.setAdjustmentFromDate(leaveRequest.getAdjustmentFromDate());
        leaverequestObj.setAdjustmentToDate(leaveRequest.getAdjustmentToDate());
        leaverequestObj.setAdjustmentRemarks(leaveRequest.getAdjustmentRemarks());
        leaverequestObj.setAdjustmentReportingTo(leaveRequest.getAdjustmentReportingTo());
        leaverequestObj.setTotalAdjustmentLeaves(leaveRequest.getTotalAdjustmentLeaves());
        leaverequestObj.setAdjustedRequestedDate(leaveRequest.getAdjustedRequestedDate());
        leaverequestObj.setAdjustmentStatus(ApplicationConstants.ADJUSTMENT_REQUESTED);
        leaverequestObj.setLeaveStatus(ApplicationConstants.ADJUSTMENT_REQUESTED);
        leaverequestObj.setUpdateDate(System.currentTimeMillis() + "");
        leaverequestObj.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_REQUEST, Id, new Gson().toJson(leaverequestObj));
        //System.out.println("In the save " + result);
        return result;
    }

}
