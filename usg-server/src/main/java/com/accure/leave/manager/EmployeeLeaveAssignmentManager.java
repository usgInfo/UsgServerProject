/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.manager.EmployeeManager;
import com.accure.leave.dto.EmployeeLeaveAssignment;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author user
 */
public class EmployeeLeaveAssignmentManager {

    public String save(EmployeeLeaveAssignment leaveAssignment, String loginUserId) throws Exception {
        //System.out.println(new Gson().toJson(leaveAssignment));
        User user = new UserManager().fetch(loginUserId);
        Employee emp = new EmployeeManager().fetchEmployee(leaveAssignment.getEmployeeId());
        leaveAssignment.setDdo(emp.getDdo());
        leaveAssignment.setDesignation(emp.getDesignation());
        leaveAssignment.setNatureType(emp.getNatureType());
        leaveAssignment.setEmployeeCategory(emp.getClassMaster());
        String userName = user.getFname() + " " + user.getLname();
        leaveAssignment.setCreateDate(System.currentTimeMillis() + "");
        leaveAssignment.setUpdateDate(System.currentTimeMillis() + "");
        leaveAssignment.setStatus(ApplicationConstants.ACTIVE);
        leaveAssignment.setCreatedBy(userName);
        String leaveAssignmentJson = new Gson().toJson(leaveAssignment);

        String Id = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, leaveAssignmentJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String updateAssign(EmployeeLeaveAssignment leaveAssignment, String loginUserId) throws Exception {

        String status = null;
        User user = new UserManager().fetch(loginUserId);
        HashMap map = new HashMap();
        map.put("employeeId", leaveAssignment.getEmployeeId());
        map.put("leaveType", leaveAssignment.getLeaveType());
        map.put("financialYear", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LEAVE_TRANSACTION, map))) {
            status = ApplicationConstants.RECORD_MAPPED;
        } else {
            String assigJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, leaveAssignment.getRowId());
            List<EmployeeLeaveAssignment> formulalist = new Gson().fromJson(assigJson, new TypeToken<List<EmployeeLeaveAssignment>>() {
            }.getType());
            EmployeeLeaveAssignment dbObj = formulalist.get(0);
            EmployeeLeaveAssignment assignData = fetchLeaveAssign(leaveAssignment.getRowId());
            assignData.setDdo(leaveAssignment.getDdo());
            assignData.setDesignation(leaveAssignment.getDesignation());
            assignData.setNatureType(leaveAssignment.getNatureType());
            assignData.setEmployeeCategory(leaveAssignment.getEmployeeCategory());
            assignData.setCurrentYearLeaves(leaveAssignment.getCurrentYearLeaves());
            assignData.setTotalEarnedLeaves(leaveAssignment.getTotalEarnedLeaves());
            String userName = user.getFname() + " " + user.getLname();
            assignData.setCreatedBy(dbObj.getCreatedBy());
            assignData.setCreateDate(dbObj.getCreateDate());
            assignData.setUpdateDate(System.currentTimeMillis() + "");
            assignData.setUpdatedBy(userName);
            assignData.setStatus(ApplicationConstants.ACTIVE);
            assignData.setCreatedBy(userName);
            String leaveAssignmentJson = new Gson().toJson(assignData);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, leaveAssignment.getRowId(), leaveAssignmentJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, Id);
        List<EmployeeLeaveAssignment> leaveAssignmentList = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        if (leaveAssignmentList == null || leaveAssignmentList.size() < 1) {
            return null;
        }
        return new Gson().toJson(leaveAssignmentList.get(0));
    }

    public EmployeeLeaveAssignment fetchObject(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, Id);
        List<EmployeeLeaveAssignment> leaveAssignmentList = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        if (leaveAssignmentList == null || leaveAssignmentList.size() < 1) {
            return null;
        }
        return leaveAssignmentList.get(0);
    }

    public EmployeeLeaveAssignment fetchLeaveAssign(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String relationJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, rid);
        List<EmployeeLeaveAssignment> relationlist = new Gson().fromJson(relationJson, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        EmployeeLeaveAssignment relation = relationlist.get(0);
        return relation;
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<EmployeeLeaveAssignment>() {
        }.getType();
        String leaveAssignment = new LeaveTypeManager().fetch(Id);
        if (leaveAssignment == null || leaveAssignment.isEmpty()) {
            return false;
        }
        EmployeeLeaveAssignment leaveAssignmentrJson = new Gson().fromJson(leaveAssignment, type);
        leaveAssignmentrJson.setStatus(ApplicationConstants.INACTIVE);
        leaveAssignmentrJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, Id, new Gson().toJson(leaveAssignmentrJson));
        return result;
    }

    public String leaveAssginDelete(String Id, String loginUserId) throws Exception {
        String status = null;
        EmployeeLeaveAssignment assignData = fetchLeaveAssign(Id);
        HashMap map = new HashMap();
        map.put("employeeId", assignData.getEmployeeId());
        map.put("leaveType", assignData.getLeaveType());
        map.put("financialYear", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LEAVE_TRANSACTION, map))) {
            status = ApplicationConstants.RECORD_MAPPED;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            assignData.setStatus(ApplicationConstants.INACTIVE);
            assignData.setUpdatedBy(userName);
            assignData.setUpdateDate(System.currentTimeMillis() + "");
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, Id, new Gson().toJson(assignData));

            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public boolean update(EmployeeLeaveAssignment leaveAssignment, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        leaveAssignment.setUpdateDate(System.currentTimeMillis() + "");
        leaveAssignment.setStatus(ApplicationConstants.ACTIVE);
        leaveAssignment.setUpdatedBy(userName);
        String leaveAssignmentrJson = new Gson().toJson(leaveAssignment);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, Id, leaveAssignmentrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap);
        return result1;
    }

    public String getLeaveTypesFromLeaveAssignment(String emplopyeeId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeId", emplopyeeId);
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        conditionMap.put("year", activeFinancialyearId);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap);
        List<EmployeeLeaveAssignment> leaveAssignmentListStr = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        for (Iterator<EmployeeLeaveAssignment> it = leaveAssignmentListStr.iterator(); it.hasNext();) {
            EmployeeLeaveAssignment employeeLeaveAssignment = it.next();
            employeeLeaveAssignment.setLeaveTypeName(new LeaveTypeManager().fetchObject(employeeLeaveAssignment.getLeaveType()).getLeaveType());
        }
        return new Gson().toJson(leaveAssignmentListStr);
    }

    public String getDataforTrasc(String Id, String empId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeId", empId);
        conditionMap.put("leaveType", Id);
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        conditionMap.put("year", activeFinancialyearId);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap);

        List<EmployeeLeaveAssignment> leaveTypeList = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        if (leaveTypeList == null || leaveTypeList.size() < 1) {
            return null;
        }
//        leaveTypeList = getNatureType(leaveTypeList);
//        leaveTypeList = getEmployeeCategory(leaveTypeList);
        return new Gson().toJson(leaveTypeList.get(0));
    }
}
