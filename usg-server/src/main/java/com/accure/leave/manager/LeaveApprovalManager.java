/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.hrms.dto.Employee;
import com.accure.leave.dto.LeaveApproval;
import com.accure.leave.dto.LeaveRequest;
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
import java.util.Map;

/**
 *
 * @author User
 */
public class LeaveApprovalManager {
    
   public String saveLeaveApproval(LeaveApproval leaveApproval) throws Exception {
      //  User user = new UserManager().fetch(loginUserId);
       // String userName = user.getFname() + " " + user.getLname();
        leaveApproval.setCreateDate(System.currentTimeMillis() + "");
        leaveApproval.setRequestedDate(System.currentTimeMillis() + "");
        leaveApproval.setUpdateDate(System.currentTimeMillis() + "");
        leaveApproval.setStatus(ApplicationConstants.ACTIVE);
        leaveApproval.setApprovedDate(System.currentTimeMillis() + "");
      //  leaveRequest.setCreatedBy(userName);
        String leaveApprovalJson = new Gson().toJson(leaveApproval);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_APPROVAL, leaveApprovalJson);
        if (Id != null) {
            return Id;
        }
        return null; 
}
   
   public String fetchLeaveApproval(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_REQUEST, Id);
        List<LeaveRequest> leaveApprovalResult = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
//        for (LeaveRequest cl : leaveApprovalResult){
//            if (cl.getRequestedBy() != null) {
//                String leaveApprovalList = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, cl.getRequestedBy());
//                if (leaveApprovalList != null) {
//                    List<LeaveApproval> getRequestedBy = new Gson().fromJson(leaveApprovalList, new TypeToken<List<LeaveApproval>>() {
//                    }.getType());
////                   LeaveRequest gal = getEmployeeName.get(0);
////                   cl.setRequestedBy(gal.getEmployeeName());
//                    LeaveApproval gal = getRequestedBy.get(0);
//                    cl.setRequestedBy(gal.getRequestedBy());
//                }
//            }
//        }
//        if (leaveApprovalList == null || leaveApprovalList.size() < 1) {
//            return null;
//        }
        return new Gson().toJson(leaveApprovalResult.get(0));
    }
   
   public boolean deleteLeaveApproval(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        //User user = new UserManager().fetch(loginUserId);
        //String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<LeaveApproval>() {
        }.getType();
        String leaveApproval = new LeaveApprovalManager().fetchLeaveApproval(Id);
        if (leaveApproval == null || leaveApproval.isEmpty()) {
            return false;
        }
        LeaveApproval leaveApprovalerJson = new Gson().fromJson(leaveApproval, type);
        leaveApprovalerJson.setStatus(ApplicationConstants.INACTIVE);
        //leaveRequesterJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_APPROVAL, Id, new Gson().toJson(leaveApprovalerJson));
        return result;
    }
    public boolean LeaveApprovalUpdate(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<LeaveRequest>() {
        }.getType();
        String leaveApproval = new LeaveApprovalManager().fetchLeaveApproval(Id);
        if (leaveApproval == null || leaveApproval.isEmpty()) {
            return false;
        }
        LeaveRequest leaveApprovalerJson = new Gson().fromJson(leaveApproval, type);
        leaveApprovalerJson.setleaveStatus("Approved");
        //leaveRequesterJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_REQUEST, Id, new Gson().toJson(leaveApprovalerJson));
        return result;
    }
   
   public boolean updateLeaveApproval(LeaveApproval leaveApproval, String Id) throws Exception {
        //User user = new UserManager().fetch(loginUserId);
        //String userName = user.getFname() + " " + user.getLname();
        leaveApproval.setUpdateDate(System.currentTimeMillis() + "");
        leaveApproval.setStatus(ApplicationConstants.ACTIVE);
        //leaveRequest.setUpdatedBy(userName);
        String leaveApprovalerJson = new Gson().toJson(leaveApproval);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_APPROVAL, Id, leaveApprovalerJson);
        return result;
    }
   
   public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        conditionMap.put("leaveStatus","Approved");
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
        return result1;
    }
   public String fetchAll(String Condition) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("leaveStatus",Condition);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_REQUEST, conditionMap);
          List<LeaveRequest> leaveRequestList = new Gson().fromJson(result1, new TypeToken<List<LeaveRequest>>() {
        }.getType());
          try {
                   leaveRequestList = getEmployeeName(leaveRequestList);
                   leaveRequestList = getReportedName(leaveRequestList);
       } catch (Exception e) {
       }
        return new Gson().toJson(leaveRequestList);
    }
    
     private List<LeaveRequest> getEmployeeName(List<LeaveRequest> leaveRequestList) throws Exception {
        Map<String, String> LeaveRequestMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.EMPLOYEE_TABLE);
          List<Employee> list = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next= iterator.next();
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

    private List<LeaveRequest> getReportedName(List<LeaveRequest> leaveRequestList) throws Exception {
        Map<String, String> LeaveRequestMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.EMPLOYEE_TABLE);
          List<Employee> list = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        for (Iterator<Employee> iterator = list.iterator(); iterator.hasNext();) {
            Employee next= iterator.next();
            LeaveRequestMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeName());
        }
        for (int i = 0; i < leaveRequestList.size(); i++) {
            for (Map.Entry<String, String> entry : LeaveRequestMap.entrySet()) {
                if (entry.getKey().equals(leaveRequestList.get(i).getReportingTo())) {
                    leaveRequestList.get(i).setReportingTo(entry.getValue());
                }
            }
        }
        return leaveRequestList;
    }
     public boolean rejectThisLeave(String Id, String userId) throws Exception {
        String lr = new LeaveRequestManager().fetchLeaveRequest(Id);
        User user = new UserManager().fetch(userId);
         String userName = user.getFname() + " " + user.getLname();
        LeaveRequest leaverequestObj = new Gson().fromJson(lr, new TypeToken<LeaveRequest>() {
        }.getType());
        leaverequestObj.setUpdateDate(System.currentTimeMillis() + "");
        leaverequestObj.setUpdatedBy(userName);
        leaverequestObj.setleaveStatus(ApplicationConstants.LEAVE_REJECTED);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_REQUEST, Id, new Gson().toJson(leaverequestObj));
        return result;
    }
}
