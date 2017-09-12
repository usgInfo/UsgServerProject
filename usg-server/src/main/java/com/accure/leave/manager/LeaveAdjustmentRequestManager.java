/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.leave.dto.LeaveAdjustmentRequest;
import com.accure.leave.dto.LeaveRequest;
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
public class LeaveAdjustmentRequestManager {
    public String save(LeaveAdjustmentRequest leaveAdjustmentRequest) throws Exception {
        leaveAdjustmentRequest.setCreateDate(System.currentTimeMillis() + "");
        leaveAdjustmentRequest.setUpdateDate(System.currentTimeMillis() + "");
        leaveAdjustmentRequest.setStatus(ApplicationConstants.ACTIVE);

        String leaveAdjustmentRequestJson = new Gson().toJson(leaveAdjustmentRequest);

        String leaveAdjustmentRequestId = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, leaveAdjustmentRequestJson);
        return leaveAdjustmentRequestId;
    }
    
    public String fetch(String leaveAdjustmentRequestId) throws Exception {
        if (leaveAdjustmentRequestId == null || leaveAdjustmentRequestId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, leaveAdjustmentRequestId);
        List<LeaveAdjustmentRequest> list = new Gson().fromJson(result, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());
//        try {
//            list = getReportingTo(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getLeaveRequest(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getLeaveType(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getFromDate(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getToDate(list);
//
//        } catch (Exception e) {
//        }
        
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }
    
    public String fetch(String employeeName,String leaveType) throws Exception {
        if (employeeName == null || employeeName.isEmpty() && leaveType == null ||leaveType.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, employeeName);
        List<LeaveAdjustmentRequest> list = new Gson().fromJson(result, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }
    
    
    public boolean delete(String leaveAdjustmentRequestId) throws Exception {
        if (leaveAdjustmentRequestId == null || leaveAdjustmentRequestId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<LeaveAdjustmentRequest>() {
        }.getType();
        String leaveAdjustmentRequest = new LeaveAdjustmentRequestManager().fetch(leaveAdjustmentRequestId);
        if (leaveAdjustmentRequest == null || leaveAdjustmentRequest.isEmpty()) {
            return false;
        }
        LeaveAdjustmentRequest leaveAdjustmenterRequestJson = new Gson().fromJson(leaveAdjustmentRequest, type);
        leaveAdjustmenterRequestJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, leaveAdjustmentRequestId, new Gson().toJson(leaveAdjustmenterRequestJson));
        return result;
    }
    
    public boolean update(LeaveAdjustmentRequest leaveAdjustmentRequest, String leaveAdjustmentRequestId) throws Exception {
        leaveAdjustmentRequest.setUpdateDate(System.currentTimeMillis() + "");
        leaveAdjustmentRequest.setStatus(ApplicationConstants.ACTIVE);
        String leaveAdjustmenterRequestJson = new Gson().toJson(leaveAdjustmentRequest);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, leaveAdjustmentRequestId, leaveAdjustmenterRequestJson);
        return result;
    }
    
    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST, conditionMap);
        List<LeaveAdjustmentRequest> list = new Gson().fromJson(result1, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());
        list = getEmployeeName(list);
        return result1;
//        try {
//            list = getReportingTo(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getLeaveRequest(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getLeaveType(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getFromDate(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getToDate(list);
//
//        } catch (Exception e) {
//        }

        //return new Gson().toJson(list);
    }
    
    private List<LeaveAdjustmentRequest> getEmployeeName(List<LeaveAdjustmentRequest> list) throws Exception {
        Map<String, String> LeaveAdjustmentRequestMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST);
          List<LeaveAdjustmentRequest> list1 = new Gson().fromJson(result, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());
        for (Iterator<LeaveAdjustmentRequest> iterator = list1.iterator(); iterator.hasNext();) {
            LeaveAdjustmentRequest next= iterator.next();
            LeaveAdjustmentRequestMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : LeaveAdjustmentRequestMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getEmployeeName())) {
                    list.get(i).setEmployeeName(entry.getValue());
                }
            }
        }
        return list;
    }
    
    /*public static List<LeaveAdjustmentRequest> getReportingTo(List<LeaveAdjustmentRequest> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST);
        List<LeaveRequest> resultListist = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        for (Iterator<LeaveRequest> iterator = resultListist.iterator(); iterator.hasNext();) {
            LeaveRequest next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getReportingTo());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getReportingTo())) {
                    list.get(i).setReportingTo(entry.getValue());
                }
            }
        }
        return list;
    }
    public static List<LeaveAdjustmentRequest> getLeaveType(List<LeaveAdjustmentRequest> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST);
        List<LeaveRequest> resultListist = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        for (Iterator<LeaveRequest> iterator = resultListist.iterator(); iterator.hasNext();) {
            LeaveRequest next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLeaveType());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getLeaveType())) {
                    list.get(i).setLeaveType(entry.getValue());
                }
            }
        }
        return list;
    }
    
    public static List<LeaveAdjustmentRequest> getFromDate(List<LeaveAdjustmentRequest> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST);
        List<LeaveRequest> resultListist = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        for (Iterator<LeaveRequest> iterator = resultListist.iterator(); iterator.hasNext();) {
            LeaveRequest next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getFromDate());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFromDate())) {
                    list.get(i).setFromDate(entry.getValue());
                }
            }
        }
        return list;
    }
    public static List<LeaveAdjustmentRequest> getToDate(List<LeaveAdjustmentRequest> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST);
        List<LeaveRequest> resultListist = new Gson().fromJson(result, new TypeToken<List<LeaveRequest>>() {
        }.getType());
        for (Iterator<LeaveRequest> iterator = resultListist.iterator(); iterator.hasNext();) {
            LeaveRequest next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getToDate());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getToDate())) {
                    list.get(i).setToDate(entry.getValue());
                }
            }
        }
        return list;
    }
    public static List<LeaveAdjustmentRequest> getLeaveRequest(List<LeaveAdjustmentRequest> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_ADJUSTMENT_REQUEST);
        List<LeaveAdjustmentRequest> resultListist = new Gson().fromJson(result, new TypeToken<List<LeaveAdjustmentRequest>>() {
        }.getType());
        for (Iterator<LeaveAdjustmentRequest> iterator = resultListist.iterator(); iterator.hasNext();) {
            LeaveAdjustmentRequest next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLeaveRequest());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getLeaveRequest())) {
                    list.get(i).setLeaveRequest(entry.getValue());
                }
            }
        }
        return list;
    }*/
    
    public static void main(String[] args) throws Exception {
        //System.out.println(new LeaveAdjustmentRequestManager().fetchAll());
    }
}
