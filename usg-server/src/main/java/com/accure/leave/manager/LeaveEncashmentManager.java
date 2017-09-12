/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.leave.dto.EmployeeLeaveAssignment;
import com.accure.leave.dto.LeaveEncashment;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.leave.pojo.LeaveType;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asif
 */
public class LeaveEncashmentManager {
   String attTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_TRANSACTION + "`";
       RestClient aql = new RestClient();
    public String save(LeaveEncashment leaveEncashment, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        leaveEncashment.setCreateDate(System.currentTimeMillis() + "");
        leaveEncashment.setUpdateDate(System.currentTimeMillis() + "");
        leaveEncashment.setStatus(ApplicationConstants.ACTIVE);
        leaveEncashment.setCreatedBy(userName);

        if (leaveEncashment != null && leaveEncashment.getEncashmentDate() != null && !leaveEncashment.getEncashmentDate().isEmpty()) {
            long encashmentDateL = Common.getMillisFromDate(leaveEncashment.getEncashmentDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(encashmentDateL);
            leaveEncashment.setMonth((calendar.get(Calendar.MONTH) + 1));
            leaveEncashment.setYear(calendar.get(Calendar.YEAR));
        }

      

        LeaveType lveType = new LeaveType();
        LeaveTransaction lveTrsctn = lveType.getRcntLveTrnsctn(leaveEncashment.getEmployeeId(),
                leaveEncashment.getLeaveType());
 HashMap<String, String> conditionMap = new HashMap<String, String>();
          List<LeaveTransaction> leaveTransactionStr = new ArrayList<LeaveTransaction>();
        HashMap<String, EmployeeLeaveAssignment> conditionMap1 = new HashMap<String, EmployeeLeaveAssignment>();
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        conditionMap.put("year", activeFinancialyearId);
        conditionMap.put("ddo", leaveEncashment.getDdoId());
        //conditionMap.put("location", leaveEncashment.getLocation());
        conditionMap.put("employeeCode", leaveEncashment.getEmployeeCode());
        conditionMap.put("leaveType", leaveEncashment.getLeaveType());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
          double balanceLeave=0.0;
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap); 
            if(result!=null)
            {
                List<EmployeeLeaveAssignment> leaveAssignmentListStr = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {}.getType()); 
           
             //leaveTransactionStr = new Gson().fromJson(result, new TypeToken<List<LeaveTransaction>>() { }.getType());
            //String elgbltyLvsStr = lveTrsctn.getEligibilityLeaves();
//            if (elgbltyLvsStr != null) {
//                Float elgbltyLvs = Float.parseFloat(elgbltyLvsStr.equalsIgnoreCase("") ? "0" : elgbltyLvsStr);
//                if (elgbltyLvs > 0) {
                LeaveTransaction leaveTransaction=new LeaveTransaction();
                    EmployeeLeaveAssignment assignment =leaveAssignmentListStr.get(0);
                    double lveToBeEncshd = leaveEncashment.getLeaveToBeEncashed();
                    double totalLeaveBlance=leaveEncashment.getLeaveBalance();
                     leaveTransaction.setDdo(leaveEncashment.getDdoId());
                    leaveTransaction.setDepartment(leaveEncashment.getDepartment());
                    leaveTransaction.setDesignation(leaveEncashment.getDesignation());
                    leaveTransaction.setLocation(leaveEncashment.getLocation());
                    leaveTransaction.setPostingCity(leaveEncashment.getPostingCity());
                    leaveTransaction.setEmployeeCodeM(leaveEncashment.getEmployeeCodeM());
                    leaveTransaction.setEmployeeCode(leaveEncashment.getEmployeeCode());
                    leaveTransaction.setEmployeeId(leaveEncashment.getEmployeeId());
                    leaveTransaction.setEmployeeName(leaveEncashment.getEmployeeName());
                    leaveTransaction.setCreateDate(System.currentTimeMillis() + "");
                    leaveTransaction.setLeaveType(assignment.getLeaveType());
                    leaveTransaction.setUpdateDate(System.currentTimeMillis() + "");
                    leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
                    leaveTransaction.setFinancialYear(((LinkedTreeMap) financialYear.getId()).get("$oid")+"");
                    leaveTransaction.setCreatedBy(userName);
                    leaveTransaction.setNatureType(leaveEncashment.getNatureType());
                    leaveTransaction.setEarnedLeaves(assignment.getTotalEarnedLeaves());
                    leaveTransaction.setTotalLeaveDays(String.valueOf(lveToBeEncshd));
                    leaveTransaction.setTotalBalanceLeaves(String.valueOf(totalLeaveBlance));
                    leaveTransaction.setDataFrom("ENCASHMENT");
//                    Float lveToBeEncshd = Float.parseFloat(
//                            (lveToBeEncshdStr == null || lveToBeEncshdStr.equalsIgnoreCase(""))
//                            ? "0" : lveToBeEncshdStr);
//                    if (elgbltyLvs >= lveToBeEncshd) {
//                        lveTrsctn.setEligibilityLeaves((elgbltyLvs - lveToBeEncshd) + "");
//                        lveType.updateRecentLeaveTransaction(leaveEncashment.getEmployeeId(), leaveEncashment.getLeaveType(), lveTrsctn);
//                    }
               // }
            //}
            
            
            List<LeaveTransaction> leaveTransactionStr1 = new ArrayList<LeaveTransaction>();
        HashMap<String, EmployeeLeaveAssignment> conditionMap11 = new HashMap<String, EmployeeLeaveAssignment>();
        HashMap<String, String> condition = new HashMap<String, String>();
         condition.put("financialYear", activeFinancialyearId);
        condition.put("ddo", leaveEncashment.getDdoId());
        //conditionMap.put("location", leaveEncashment.getLocation());
        condition.put("employeeCode", leaveEncashment.getEmployeeCode());
        condition.put("leaveType", leaveEncashment.getLeaveType());
        condition.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String resulta = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, condition); 
        if(resulta!=null)
        {
          leaveTransactionStr1 = new Gson().fromJson(resulta, new TypeToken<List<LeaveTransaction>>() { }.getType());
          long createdDateInMilli=Long.parseLong(leaveTransactionStr1.get(0).getCreateDate());
           balanceLeave=Double.parseDouble(leaveTransactionStr1.get(0).getTotalAppliedLeaves());
          for(LeaveTransaction leaveTransacti:leaveTransactionStr1)
          {
              long createdDate=Long.parseLong(leaveTransacti.getCreateDate());
              if(createdDate>createdDateInMilli)
              {
                 balanceLeave= Double.parseDouble(leaveTransacti.getTotalAppliedLeaves());
              }
          }
          leaveTransaction.setTotalAppliedLeaves(String.valueOf(balanceLeave));
     }
     else
        {
             leaveTransaction.setTotalAppliedLeaves(String.valueOf(0.0));
        }
            
            
             String leaveTransactionJson = new Gson().toJson(leaveTransaction);
            String Id1 = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TRANSACTION, leaveTransactionJson);
            leaveEncashment.setTransactionId(Id1);
        }
        String leaveEncashmentJson = new Gson().toJson(leaveEncashment);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_ENCASHMENT, leaveEncashmentJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ENCASHMENT, Id);
        List<LeaveEncashment> leaveEncashmentList = new Gson().fromJson(result, new TypeToken<List<LeaveEncashment>>() {
        }.getType());
        if (leaveEncashmentList == null || leaveEncashmentList.size() < 1) {
            return null;
        }
        leaveEncashmentList = getLeaveType(leaveEncashmentList);
        return new Gson().toJson(leaveEncashmentList.get(0));
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<LeaveEncashment>() {

        }.getType();
        String leaveEncashment = new LeaveEncashmentManager().fetch(Id);
        if (leaveEncashment == null || leaveEncashment.isEmpty()) {
            return false;
        }
       
        LeaveEncashment leaveEncashmentrJson = new Gson().fromJson(leaveEncashment, type);
         String transactionId=leaveEncashmentrJson.getTransactionId();
         if(transactionId!=null)
         {
             String LeaveTransactionstr=new LeaveTransactionManager().fetch(transactionId);
           if(LeaveTransactionstr!=null)
           {
                 Type type1 = new TypeToken<LeaveTransaction>() {}.getType();
                LeaveTransaction leaveTransactionrJson = new Gson().fromJson(LeaveTransactionstr, type1);
                leaveTransactionrJson.setStatus(ApplicationConstants.INACTIVE);
                   String leaveTransactrJson = new Gson().toJson(leaveTransactionrJson);
                 boolean result1 = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TRANSACTION, transactionId, leaveTransactrJson);
           }
         }
        leaveEncashmentrJson.setStatus(ApplicationConstants.INACTIVE);
        leaveEncashmentrJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ENCASHMENT, Id, new Gson().toJson(leaveEncashmentrJson));
        return result;
    }

    public boolean update(LeaveEncashment leaveEncashment, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        String LeaceEncash=new LeaveEncashmentManager().fetch(Id);
         Type type1 = new TypeToken<LeaveEncashment>() {}.getType();
                LeaveEncashment LeaveEncashment1 = new Gson().fromJson(LeaceEncash, type1);
        if (leaveEncashment != null && leaveEncashment.getEncashmentDate() != null && !leaveEncashment.getEncashmentDate().isEmpty()) {
            long encashmentDateL = Common.getMillisFromDate(leaveEncashment.getEncashmentDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(encashmentDateL);
            LeaveEncashment1.setMonth((calendar.get(Calendar.MONTH) + 1));
            LeaveEncashment1.setYear(calendar.get(Calendar.YEAR));
        }
        LeaveEncashment1.setUpdatedBy(userName);
        LeaveEncashment1.setUpdateDate(System.currentTimeMillis() + "");
        //LeaveEncashment1.setStatus(ApplicationConstants.ACTIVE);
        LeaveEncashment1.setLeaveBalance(leaveEncashment.getLeaveBalance());
         LeaveEncashment1.setEncashmentDate(leaveEncashment.getEncashmentDate());
          LeaveEncashment1.setCashableLeaves(leaveEncashment.getCashableLeaves());
          LeaveEncashment1.setLeaveToBeEncashed(leaveEncashment.getLeaveToBeEncashed());
        LeaveEncashment1.setTotalAmount(leaveEncashment.getTotalAmount());
        String transactionId="";
        if(LeaceEncash!=null)
        {
            transactionId=LeaveEncashment1.getTransactionId();
        }

        if(transactionId!=null)
        {
           String LeaveTransactionstr=new LeaveTransactionManager().fetch(transactionId);
           if(LeaveTransactionstr!=null)
           {
                 Type type = new TypeToken<LeaveTransaction>() {}.getType();
                LeaveTransaction leaveTransactionrJson = new Gson().fromJson(LeaveTransactionstr, type);
                leaveTransactionrJson.setTotalBalanceLeaves(String.valueOf(leaveEncashment.getLeaveBalance()));
                 leaveTransactionrJson.setTotalLeaveDays(String.valueOf(leaveEncashment.getLeaveToBeEncashed()));
                   String leaveTransactrJson = new Gson().toJson(leaveTransactionrJson);
                 boolean result1 = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TRANSACTION, transactionId, leaveTransactrJson);
           }
        }
        String leaveEncashmentrJson = new Gson().toJson(LeaveEncashment1);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ENCASHMENT, Id, leaveEncashmentrJson);
        return result;
    }

    public String fetchAll(String ddo,String location) throws Exception {
        String fResult = "";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddoId",ddo);
        conditionMap.put("location", location);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_ENCASHMENT, conditionMap);
        if (result != null) {
            List<LeaveEncashment> list = new Gson().fromJson(result, new TypeToken<List<LeaveEncashment>>() {}.getType());
            list = getLeaveType(list);
            fResult = new Gson().toJson(list);
        }

        return fResult;

    }

    public static List<LeaveEncashment> getLeaveType(List<LeaveEncashment> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_TYPE_MASTER);
        List<LeaveTypeMaster> LTlist = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        for (Iterator<LeaveTypeMaster> iterator = LTlist.iterator(); iterator.hasNext();) {
            LeaveTypeMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLeaveType());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getLeaveType())) {
                    list.get(i).setLeaveTypeName(entry.getValue());
                }
            }
        }
        return list;
    }

    /**
     * @author chaitu
     * @description update() method will update the LeaveEncashment lock status
     * @table leaveEncashment
     * @param String encashmentId
     * @param boolean lockStatus
     * @return boolean
     * @throws Exception
     */
    public static boolean update(String encashmentId, boolean lockStatus) throws Exception {
        boolean isEncashmentUpdated = false;
        if (encashmentId == null) {
            return isEncashmentUpdated;
        }

        //fetch
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ENCASHMENT, encashmentId);
        List<LeaveEncashment> leaveEncashmentList = new Gson().fromJson(result, new TypeToken<List<LeaveEncashment>>() {
        }.getType());
        LeaveEncashment le = leaveEncashmentList.get(0);
        le.setIsLocked(lockStatus);
        le.setUpdateDate(Long.toString(System.currentTimeMillis()));

        //update
        isEncashmentUpdated = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ENCASHMENT, encashmentId, new Gson().toJson(le));
        return isEncashmentUpdated;
    }
     public String getLeaveAssignmentEmployList(String ddoId,String locationId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, EmployeeLeaveAssignment> conditionMap1 = new HashMap<String, EmployeeLeaveAssignment>();
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        conditionMap.put("year", activeFinancialyearId);
        conditionMap.put("ddo", ddoId);
        //conditionMap.put("ddo", ddoId);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap);
        if(result!=null)
        {
        List<EmployeeLeaveAssignment> leaveAssignmentListStr = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        for (Iterator<EmployeeLeaveAssignment> it = leaveAssignmentListStr.iterator(); it.hasNext();) {
            EmployeeLeaveAssignment employeeLeaveAssignment = it.next();
            employeeLeaveAssignment.setLeaveTypeName(new LeaveTypeManager().fetchObject(employeeLeaveAssignment.getLeaveType()).getLeaveType());
            conditionMap1.put(employeeLeaveAssignment.getEmployeeCode(),employeeLeaveAssignment);
        }
        leaveAssignmentListStr.clear();
        leaveAssignmentListStr.addAll(conditionMap1.values());
         return new Gson().toJson(leaveAssignmentListStr);
        }
        return new Gson().toJson(null);
    }
     public String getLeaveBalanceFromTransaction(String ddo,String location,String employeeCode,String leaveTpe) throws Exception
     {
          HashMap<String, String> conditionMap = new HashMap<String, String>();
          List<LeaveTransaction> leaveTransactionStr = new ArrayList<LeaveTransaction>();
        HashMap<String, EmployeeLeaveAssignment> conditionMap1 = new HashMap<String, EmployeeLeaveAssignment>();
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        conditionMap.put("financialYear", activeFinancialyearId);
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        conditionMap.put("employeeCode", employeeCode);
        conditionMap.put("leaveType", leaveTpe);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
          int balanceLeave=0;
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap); 
        if(result!=null)
        {
          leaveTransactionStr = new Gson().fromJson(result, new TypeToken<List<LeaveTransaction>>() { }.getType());
          long createdDateInMilli=Long.parseLong(leaveTransactionStr.get(0).getCreateDate());
           balanceLeave=Integer.parseInt(leaveTransactionStr.get(0).getTotalBalanceLeaves());
          for(LeaveTransaction leaveTransaction:leaveTransactionStr)
          {
              long createdDate=Long.parseLong(leaveTransaction.getCreateDate());
              if(createdDate>createdDateInMilli)
              {
                 balanceLeave= Integer.parseInt(leaveTransaction.getTotalBalanceLeaves());
              }
          }
     }
     else
        {
            conditionMap.clear();
            conditionMap.put("year", activeFinancialyearId);
            conditionMap.put("ddo", ddo);
           // conditionMap.put("location", location);
            conditionMap.put("employeeCode", employeeCode);
            conditionMap.put("leaveType", leaveTpe);
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result1= DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, conditionMap);
             if(result1!=null)
            {
                List<EmployeeLeaveAssignment> leaveAssignmentListStr = new Gson().fromJson(result1, new TypeToken<List<EmployeeLeaveAssignment>>() {}.getType()); 
                balanceLeave=Integer.parseInt(leaveAssignmentListStr.get(0).getCurrentYearLeaves())+Integer.parseInt(leaveAssignmentListStr.get(0).getTotalEarnedLeaves());
         
            }
        }
         return new Gson().toJson(String.valueOf(balanceLeave));
}
}