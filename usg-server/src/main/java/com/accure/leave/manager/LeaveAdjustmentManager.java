/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Nature;
import com.accure.leave.dto.LeaveAdjustment;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class LeaveAdjustmentManager {

    public String searchLeaveEmployee(String leaveEmployeeJson) throws Exception {
        HashMap<String, String> parent = fetchAllNatureType();
        HashMap<String, String> designationParent = fetchAllDesignation();
        DB db = DBManager.getDB();
        String leaveTransactionEmployee = "";
        String leaveData = "";
        Type type = new TypeToken<LeaveTransaction>() {
        }.getType();
        LeaveTransaction leaveTransaction = new Gson().fromJson(leaveEmployeeJson, type);
        BasicDBObject regexQuery = new BasicDBObject();

        if (leaveTransaction.getEmployeeCode() != null && !leaveTransaction.getEmployeeCode().isEmpty()) {
            regexQuery.put("employeeCode", new BasicDBObject("$regex", leaveTransaction.getEmployeeCode()));
        }
        if (leaveTransaction.getEmployeeName() != null && !leaveTransaction.getEmployeeName().isEmpty()) {
            regexQuery.put("employeeName", new BasicDBObject("$regex", leaveTransaction.getEmployeeName()));
        }
        if (leaveTransaction.getDdo() != null && !leaveTransaction.getDdo().isEmpty()) {
            regexQuery.put("ddo", new BasicDBObject("$eq", leaveTransaction.getDdo()));
        }
        if (leaveTransaction.getLocation() != null && !leaveTransaction.getLocation().isEmpty()) {
            regexQuery.put("location", new BasicDBObject("$eq", leaveTransaction.getLocation()));
        }
        if (leaveTransaction.getDepartment() != null && !leaveTransaction.getDepartment().isEmpty() && !leaveTransaction.getDepartment().equals("0")) {
            regexQuery.put("department", new BasicDBObject("$eq", leaveTransaction.getDepartment()));
        }
        if (leaveTransaction.getDesignation() != null && !leaveTransaction.getDesignation().isEmpty() && !leaveTransaction.getDesignation().equals("0")) {
            regexQuery.put("designation", new BasicDBObject("$eq", leaveTransaction.getDesignation()));
        }
        if (leaveTransaction.getNatureType() != null && !leaveTransaction.getNatureType().isEmpty() && !leaveTransaction.getNatureType().equals("0")) {
            regexQuery.put("natureType", new BasicDBObject("$eq", leaveTransaction.getNatureType()));
        }
        if (leaveTransaction.getLeaveType() != null && !leaveTransaction.getLeaveType().isEmpty() && !leaveTransaction.getLeaveType().equals("0")) {
            regexQuery.put("leaveType", new BasicDBObject("$eq", leaveTransaction.getLeaveType()));
        }

        if (!leaveTransaction.getFromDate().isEmpty()) {
            long fromDate = convertToMilliseconds(leaveTransaction.getFromDate());
            regexQuery.put("fromDateInMilliSecond", new BasicDBObject("$gte", fromDate));
        }
        if (!leaveTransaction.getToDate().isEmpty()) {
            long toDate = convertToMilliseconds(leaveTransaction.getToDate());
            regexQuery.put("toDateInMilliSecond", new BasicDBObject("$lte", toDate));
        }
        regexQuery.put("status", new BasicDBObject("$regex", "Active"));
        DBCollection collection = db.getCollection(ApplicationConstants.LEAVE_TRANSACTION);
        DBCursor cursor = collection.find(regexQuery);

        ArrayList<LeaveTransaction> leaveTransactionDataList = new ArrayList<LeaveTransaction>();
        if (cursor != null && cursor.count() > 0) {
            while (cursor.hasNext()) {
                DBObject ob = cursor.next();
                Type LeaveType = new TypeToken<LeaveTransaction>() {
                }.getType();
                LeaveTransaction leaveTransactionData = new Gson().fromJson(ob.toString(), LeaveType);

                if (leaveTransactionDataList.size() > 0) {
                    for (LeaveTransaction leaveTrans : leaveTransactionDataList) {
                        if (leaveTrans.getNatureType() != null) {
                            String id = leaveTrans.getNatureType();
                            if (parent != null && parent.containsKey(id) && parent.get(id) != null) {
                                leaveTrans.setNatureTypeName(parent.get(id));
                            }
                        }
                        if (leaveTrans.getDesignation() != null) {
                            String id = leaveTrans.getDesignation();
                            if (designationParent != null && designationParent.containsKey(id) && designationParent.get(id) != null) {
                                leaveTrans.setDesignationName(designationParent.get(id));
                            }
                        }
                    }
                }
                leaveTransactionDataList.add(leaveTransactionData);
            }
            leaveData = new Gson().toJson(leaveTransactionDataList);
        }

        return leaveData;

    }

    public HashMap<String, String> fetchAllNatureType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String natureTypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NATURE_TABLE, conditionMap);
        List<Nature> natureTypeList = new Gson().fromJson(natureTypeJson, new TypeToken<List<Nature>>() {
        }.getType());
        conditionMap.clear();
        for (Nature natureType : natureTypeList) {
            conditionMap.put(((Map<String, String>) natureType.getId()).get("$oid"), natureType.getNatureName());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchAllDesignation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String designationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_TABLE, conditionMap);
        List<Designation> designationList = new Gson().fromJson(designationJson, new TypeToken<List<Designation>>() {
        }.getType());
        conditionMap.clear();
        for (Designation designation : designationList) {
            conditionMap.put(((Map<String, String>) designation.getId()).get("$oid"), designation.getDesignation());
        }
        return conditionMap;
    }

    public Long convertToMilliseconds(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date date = sdf.parse(str);
        return date.getTime();
    }

    public String createLeaveAdjustment(LeaveAdjustment leaveAdjustment, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        leaveAdjustment.setCreateDate(System.currentTimeMillis() + "");
        leaveAdjustment.setUpdateDate(System.currentTimeMillis() + "");
        leaveAdjustment.setStatus(ApplicationConstants.ACTIVE);
        leaveAdjustment.setCreatedBy(userName);
        String leaveAdjustmentJson = new Gson().toJson(leaveAdjustment);

        String leaveAdjustmentId = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE, leaveAdjustmentJson);
        if (leaveAdjustmentId != null) {
            return leaveAdjustmentId;
        }
        return null;
    }

    public List<LeaveAdjustment> fetchAllLeaveAdjustment() throws Exception {
        BasicDBObject regexQuerys = new BasicDBObject();
        List<LeaveAdjustment> resultList = new ArrayList<LeaveAdjustment>();
        DB db = DBManager.getDB();
        DBObject submit = new BasicDBObject("status", new BasicDBObject("$regex", "Submit"));
        DBObject active = new BasicDBObject("status", new BasicDBObject("$regex", "Active"));
        BasicDBList or = new BasicDBList();
        or.add(submit);
        or.add(active);
        regexQuerys.put("$or", or);
        DBCollection empCollection = db.getCollection(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE);
        DBCursor cursors = empCollection.find(regexQuerys);
        if (cursors != null && cursors.count() > 0) {
            while (cursors.hasNext()) {
                DBObject obj = cursors.next();
                Type type1 = new TypeToken<LeaveAdjustment>() {
                }.getType();
                LeaveAdjustment leaveAdjustmentList = new Gson().fromJson(obj.toString(), type1);
                resultList.add(leaveAdjustmentList);
            }
        }
        return resultList;
    }

    public LeaveAdjustment fetch(String leaveAdjustmentId) throws Exception {
        if (leaveAdjustmentId == null || leaveAdjustmentId.isEmpty()) {
            return null;
        }
        String leaveAdjustmentJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE, leaveAdjustmentId);
        if (leaveAdjustmentJson == null || leaveAdjustmentJson.isEmpty()) {
            return null;
        }
        List<LeaveAdjustment> leaveAdjustmentList = new Gson().fromJson(leaveAdjustmentJson, new TypeToken<List<LeaveAdjustment>>() {
        }.getType());
        if (leaveAdjustmentList == null || leaveAdjustmentList.isEmpty()) {
            return null;
        }
        return leaveAdjustmentList.get(0);
    }

    private Long daysBetween(long one, long two) {
        long difference = (one - two) / 86400000;
        return Math.abs(difference);
    }

    public boolean updateLeaveAdjustment(LeaveAdjustment leaveAdjustment, String userId, String leaveAdjustmentId) throws Exception {
        double leaveBalance = 0.0;
        double cancelLeaves = 0.0;
        //Getting the dates from UI
        String fromDate = leaveAdjustment.getFromDate();
        String toDate = leaveAdjustment.getToDate();
        String requestDate = leaveAdjustment.getRequestDate();
        //Coverting them to milliseconds
        long fromDateInMillis = convertToMilliseconds(fromDate);
        long toDateInMillis = convertToMilliseconds(toDate);
        long requestDateInMillis = convertToMilliseconds(requestDate);
        //Calculating no of days between the new dates given
        List<String> noOfDaysList = new LeaveTransactionManager().getdatesBetweenTwoDates(fromDate, toDate);
        int noOfDays = noOfDaysList.size();
        double Days = (double) noOfDays;
        //Fetching the existing data from database for particular Id
        LeaveAdjustment dbLeaveAdjustmentData = fetch(leaveAdjustmentId);
        double dbAppliedLeaves = Double.parseDouble(dbLeaveAdjustmentData.getTotalAppliedLeaves());
        double dbBalanceLeaves = Double.parseDouble(dbLeaveAdjustmentData.getTotalBalanceLeaves());

//        Now doing computation as per new values
        if (noOfDays > dbAppliedLeaves) {
            double daysDiff = noOfDays - dbAppliedLeaves;
            leaveBalance = dbBalanceLeaves - daysDiff;

        } else if (noOfDays < dbAppliedLeaves) {
            cancelLeaves = dbAppliedLeaves - noOfDays;
            leaveBalance = dbBalanceLeaves + cancelLeaves;

        } else if (noOfDays == dbAppliedLeaves) {
            leaveBalance = dbBalanceLeaves;
        }
//         Setting the values
        if (!leaveAdjustment.getFromDate().isEmpty()) {
            dbLeaveAdjustmentData.setFromDate(leaveAdjustment.getFromDate());
            dbLeaveAdjustmentData.setFromDateInMilliSecond(fromDateInMillis);
        }
        if (!leaveAdjustment.getToDate().isEmpty()) {
            dbLeaveAdjustmentData.setToDate(leaveAdjustment.getToDate());
            dbLeaveAdjustmentData.setToDateInMilliSecond(toDateInMillis);
        }
        dbLeaveAdjustmentData.setTotalAppliedLeaves(Days + "");
        dbLeaveAdjustmentData.setTotalBalanceLeaves(leaveBalance + "");
        dbLeaveAdjustmentData.setTotalCancelledLeaves(cancelLeaves + "");
        if (!leaveAdjustment.getRemarks().isEmpty()) {
            dbLeaveAdjustmentData.setRemarks(leaveAdjustment.getRemarks());
        }
        dbLeaveAdjustmentData.setUpdateDate(System.currentTimeMillis() + "");
        dbLeaveAdjustmentData.setUpdatedBy(userId);

        String leaveAdjustmentJson = new Gson().toJson(dbLeaveAdjustmentData);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE, leaveAdjustmentId, leaveAdjustmentJson);
        return status;
    }

    public boolean deleteLeaveAdjustment(String leaveAdjustmentId, String userId) throws Exception {

        LeaveAdjustment dbLeaveAdjustmentData = fetch(leaveAdjustmentId);
        dbLeaveAdjustmentData.setDeletedBy(userId);
        dbLeaveAdjustmentData.setUpdateDate(System.currentTimeMillis() + "");
        dbLeaveAdjustmentData.setStatus(ApplicationConstants.DELETE);
        String json = new Gson().toJson(dbLeaveAdjustmentData);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE, leaveAdjustmentId, json);
        return status;
    }

    public boolean submitLeaveAdjustment(LeaveTransaction leaveTransactionJson, String userId, String leaveAdjustmentId, String loanTransactionRowId) throws Exception {
        //Getting the data from leave transaction
        String LeaveTransactionJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TRANSACTION, loanTransactionRowId);
        List<LeaveTransaction> leaveTransactionList = new Gson().fromJson(LeaveTransactionJson, new TypeToken<List<LeaveTransaction>>() {
        }.getType());
        LeaveTransaction leaveTransaction = leaveTransactionList.get(0);

        //Computing the new values
        String fromDate = leaveTransactionJson.getFromDate();
        String toDate = leaveTransactionJson.getToDate();
        //Coverting them to milliseconds
        long fromDateInMillis = convertToMilliseconds(fromDate);
        long toDateInMillis = convertToMilliseconds(toDate);
        //Calculating no of days between the new dates given
        List<String> noOfDaysList = new LeaveTransactionManager().getdatesBetweenTwoDates(fromDate, toDate);
        int noOfDays = noOfDaysList.size();
        double Days = (double) noOfDays;
        List<Map<String, String>> dateRemarksAndIsHalfDayList = new LeaveTransactionManager().setList(fromDate, toDate);
        //Updating in leaveTransaction object
        leaveTransaction.setFromDate(fromDate);
        leaveTransaction.setFromDateInMilliSecond(fromDateInMillis);
        leaveTransaction.setToDate(toDate);
        leaveTransaction.setToDateInMilliSecond(toDateInMillis);
        leaveTransaction.setRowId(loanTransactionRowId);
        // leaveTransaction.setTotalAppliedLeaves(Days + "");
        leaveTransaction.setDateRemarksAndIsHalfDay(dateRemarksAndIsHalfDayList);
        leaveTransaction.setUpdateDate(System.currentTimeMillis() + "");
        leaveTransaction.setUpdatedBy(userId);

        String status = new LeaveTransactionManager().update(leaveTransaction, userId);
        if (status != null) {
            String LeaveAdjustmentJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE, leaveAdjustmentId);
            List<LeaveAdjustment> leaveAdjustmentList = new Gson().fromJson(LeaveAdjustmentJson, new TypeToken<List<LeaveAdjustment>>() {
            }.getType());
            LeaveAdjustment leaveAdjustmentObj = leaveAdjustmentList.get(0);
            leaveAdjustmentObj.setStatus(ApplicationConstants.SUBMIT);
            leaveAdjustmentObj.setUpdateDate(System.currentTimeMillis() + "");
            leaveAdjustmentObj.setUpdatedBy(userId);
            String leaveAdjustmentJson = new Gson().toJson(leaveAdjustmentObj);
            boolean leaveAdjustmentStatus = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_ADJUSTMENT_TABLE, leaveAdjustmentId, leaveAdjustmentJson);
            return true;
        }
        return false;
    }

}
