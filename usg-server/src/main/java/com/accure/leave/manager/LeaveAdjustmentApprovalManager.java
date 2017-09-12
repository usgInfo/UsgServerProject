/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.hrms.dto.Employee;
import com.accure.leave.dto.LeaveAdjustmentApproval;
import com.accure.leave.dto.LeaveAdjustmentRequest;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class LeaveAdjustmentApprovalManager {
    
    public String saveLeaveAdjustmentApproval(LeaveAdjustmentApproval leaveAdjustmentApproval) throws Exception {
      
        leaveAdjustmentApproval.setCreateDate(System.currentTimeMillis() + "");
        leaveAdjustmentApproval.setRequestedDate(System.currentTimeMillis() + "");
        leaveAdjustmentApproval.setUpdateDate(System.currentTimeMillis() + "");
        leaveAdjustmentApproval.setStatus(ApplicationConstants.ACTIVE);
        leaveAdjustmentApproval.setApprovedDate(System.currentTimeMillis() + "");
     
        String leaveAdjustmentApprovalJson = new Gson().toJson(leaveAdjustmentApproval);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_ADJUSTMENT_APPROVAL, leaveAdjustmentApprovalJson);
        if (Id != null) {
            return Id;
        }
        return null; 
}
   
   public String fetchLeaveAdjustmentApproval(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ADJUSTMENT_APPROVAL, Id);
        List<LeaveAdjustmentRequest> leaveAdjustmentApprovalResult = new Gson().fromJson(result, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());

        return new Gson().toJson(leaveAdjustmentApprovalResult.get(0));
    }
   
   public boolean deleteLeaveAdjustmentApproval(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        //User user = new UserManager().fetch(loginUserId);
        //String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<LeaveAdjustmentApproval>() {
        }.getType();
        String leaveAdjustmentApproval = new LeaveAdjustmentApprovalManager().fetchLeaveAdjustmentApproval(Id);
        if (leaveAdjustmentApproval == null || leaveAdjustmentApproval.isEmpty()) {
            return false;
        }
        LeaveAdjustmentApproval leaveAdjustmentApprovalerJson = new Gson().fromJson(leaveAdjustmentApproval, type);
        leaveAdjustmentApprovalerJson.setStatus(ApplicationConstants.INACTIVE);
        
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_APPROVAL, Id, new Gson().toJson(leaveAdjustmentApprovalerJson));
        return result;
    }
    public boolean LeaveAdjustmentApprovalUpdate(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<LeaveAdjustmentRequest>() {
        }.getType();
        String leaveAdjustmentApproval = new LeaveAdjustmentApprovalManager().fetchLeaveAdjustmentApproval(Id);
        if (leaveAdjustmentApproval == null || leaveAdjustmentApproval.isEmpty()) {
            return false;
        }
        LeaveAdjustmentRequest leaveAdjustmentApprovalerJson = new Gson().fromJson(leaveAdjustmentApproval, type);
        leaveAdjustmentApprovalerJson.setleaveStatus("Approved");
        //leaveRequesterJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, Id, new Gson().toJson(leaveAdjustmentApprovalerJson));
        return result;
    }
   
   public boolean updateLeaveAdjustmentApproval(LeaveAdjustmentApproval leaveAdjustmentApproval, String Id) throws Exception {
        //User user = new UserManager().fetch(loginUserId);
        //String userName = user.getFname() + " " + user.getLname();
        leaveAdjustmentApproval.setUpdateDate(System.currentTimeMillis() + "");
        leaveAdjustmentApproval.setStatus(ApplicationConstants.ACTIVE);
        //leaveRequest.setUpdatedBy(userName);
        String leaveAdjustmentApprovalerJson = new Gson().toJson(leaveAdjustmentApproval);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_APPROVAL, Id, leaveAdjustmentApprovalerJson);
        return result;
    }
   
   public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        conditionMap.put("leaveStatus","Approved");
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, conditionMap);
        return result1;
    }
   public String fetchAll(String Condition) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("leaveStatus",Condition);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, conditionMap);
          List<LeaveAdjustmentRequest> leaveAdjustmentRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());
          try {
                   leaveAdjustmentRequestList = getEmployeeName(leaveAdjustmentRequestList);
                   leaveAdjustmentRequestList = getReportedName(leaveAdjustmentRequestList);
       } catch (Exception e) {
       }
        return new Gson().toJson(leaveAdjustmentRequestList);
    }
    
     private List<LeaveAdjustmentRequest> getEmployeeName(List<LeaveAdjustmentRequest> leaveAdjustmentRequestList) throws Exception {
        Map<String, String> LeaveAdjustmentRequestMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.EMPLOYEE_TABLE);
          List<Employee> list = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next= iterator.next();
            LeaveAdjustmentRequestMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeName());
        }
        for (int i = 0; i < leaveAdjustmentRequestList.size(); i++) {
            for (Map.Entry<String, String> entry : LeaveAdjustmentRequestMap.entrySet()) {
                if (entry.getKey().equals(leaveAdjustmentRequestList.get(i).getEmployeeName())) {
                    leaveAdjustmentRequestList.get(i).setEmployeeName(entry.getValue());
                }
            }
        }
        return leaveAdjustmentRequestList;
    }

    private List<LeaveAdjustmentRequest> getReportedName(List<LeaveAdjustmentRequest> leaveAdjustmentRequestList) throws Exception {
        Map<String, String> LeaveAdjustmentRequestMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.EMPLOYEE_TABLE);
          List<Employee> list = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next= iterator.next();
            LeaveAdjustmentRequestMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeName());
        }
        for (int i = 0; i < leaveAdjustmentRequestList.size(); i++) {
            for (Map.Entry<String, String> entry : LeaveAdjustmentRequestMap.entrySet()) {
                if (entry.getKey().equals(leaveAdjustmentRequestList.get(i).getReportingTo())) {
                    leaveAdjustmentRequestList.get(i).setReportingTo(entry.getValue());
                }
            }
        }
        return leaveAdjustmentRequestList;
    }
    
}
