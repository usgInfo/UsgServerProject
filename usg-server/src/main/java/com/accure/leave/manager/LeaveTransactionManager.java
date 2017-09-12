/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.hrms.dto.Employee;
import com.accure.hrms.manager.EmployeeManager;
import com.accure.leave.dto.FinancialYear;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.dto.LeaveTypeDetails;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.leave.dto.LocationWiseHolidayMaster;
import com.accure.leave.dto.LocationWiseHolidayMasterList;
import com.accure.leave.dto.WeeklyOffMaster;
import com.accure.leave.dto.WeeklyOffMasterList;
import com.accure.leave.pojo.LeaveType;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Asif
 */
public class LeaveTransactionManager {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

    public String save(LeaveTransaction leaveTransaction, String loginUserId) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        User user = new UserManager().fetch(loginUserId);
        String checkDate = leaveTransaction.getFromDate();
        String[] parts = checkDate.split("/");
        String moonth = parts[1];
        if (moonth.startsWith("0")) {
            moonth = moonth.substring(0, 0) + moonth.substring(0 + 1);
        }
        String year = parts[2];
        HashMap<String, String> attendCondi = new HashMap<String, String>();
        attendCondi.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        attendCondi.put("idStr", leaveTransaction.getEmployeeId());
        attendCondi.put("month", moonth);
        attendCondi.put("year", year);
        String attenResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMP_ATTENDANCE_TABLE, attendCondi);
        //Check Attendence for that employee
        if (new LeaveTransactionManager().checkDateAllLeaveType(leaveTransaction) == null) {
            if (attenResult == null) {
                String userName = user.getFname() + " " + user.getLname();
                String totalTimeCondi = "No";
                String lwpid = "No";
                String leaveLimitInst = "0";
                double finalleave = 0;
                ArrayList<String> finalleavedays = new ArrayList<String>();
                String leavetypee = leaveTransaction.getLeaveType();
                String leavetypeeDes = leaveTransaction.getLeaveTypeDescription();
                Employee emp = new EmployeeManager().fetchEmployee(leaveTransaction.getEmployeeId());
                String leaveType = new LeaveTypeManager().fetchById(leaveTransaction.getLeaveType());
                Type type = new TypeToken<LeaveTypeMaster>() {
                }.getType();
                String offCoveredFlag = "";
                LeaveTypeMaster leaveTyperJson = new Gson().fromJson(leaveType, type);
                List<LeaveTypeDetails> li = leaveTyperJson.getLeaveTypeDetails();
                if (li.size() != 0) {
                    for (int i = 0; i < li.size(); i++) {
                        if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                            if (li.get(i).getFixedTimeIssue().equalsIgnoreCase("Yes")) {
                                totalTimeCondi = li.get(i).getTotalTimeIssue();
                            }
                            offCoveredFlag = li.get(i).getOffCovered();

                        }
                    }
                }
                //check Fixed Time Issue
                if (!totalTimeCondi.equalsIgnoreCase("No")) {
                    HashMap<String, String> conditionMap = new HashMap<String, String>();
                    conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    conditionMap.put("employeeId", leaveTransaction.getEmployeeId());
                    conditionMap.put("leaveType", leaveTransaction.getLeaveType());
                    String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
                    List<LeaveTransaction> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
                    }.getType());
                    try {
                        if (list.size() >= Integer.parseInt(totalTimeCondi)) {
                            resultMap.put("statusMessage", "LeaveFixedTime");
                            return new Gson().toJson(resultMap);
                        }
                    } catch (Exception e) {

                    }
                }
                //Fixed Time issue condition End

                leaveTransaction = setTotalAppledLeaves(leaveTransaction, offCoveredFlag);

                if (li.size() != 0) {
                    for (int i = 0; i < li.size(); i++) {
                        if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                            leaveLimitInst = li.get(i).getLeaveLimitPerInstance();
                        }
                    }
                }
                if (!leaveLimitInst.equalsIgnoreCase("No")) {
                    if (Double.parseDouble(leaveLimitInst) < Double.parseDouble(leaveTransaction.getTotalLeaveDays())) {
//                double myDb = Double.parseDouble(leaveTransaction.getTotalLeaveDays());
//                int myInt = (int) myDb;
                        try {
                            if (((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") != null) {
                                HashMap<String, String> lwpCondition = new HashMap<String, String>();
                                lwpCondition.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                                lwpCondition.put("employeeId", leaveTransaction.getEmployeeId());
                                lwpCondition.put("leaveType", ((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") + "");
                                lwpCondition.put("year", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
                                String lwpResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, lwpCondition);
                                //    lwpCondition.setLeaveType(((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") + "");
                                if (lwpResult != null) {
                                    int removeele = Integer.parseInt(leaveLimitInst);
                                    ArrayList<String> finalLwp = leaveTransaction.getLeaveWithoutPayDays();
                                    for (int i = 0; i < removeele; i++) {
                                        finalleavedays.add(finalLwp.get(0));
                                        finalLwp.remove(0);
                                    }
                                    leaveTransaction.setLeaveWithoutPayDays(finalLwp);
                                    leaveTransaction.setNoOfLeaveWithoutPayDays(finalLwp.size());
                                    int countdays = finalLwp.size();
                                    String leaveDays = leaveTransaction.getTotalLeaveDays();
                                    finalleave = Double.parseDouble(leaveDays) - (double) countdays;
                                    leaveTransaction.setTotalLeaveDays((double) countdays - leaveTransaction.getLwpHalfDays() + "");
                                    leaveTransaction.setTotalAppliedLeaves((double) countdays - leaveTransaction.getLwpHalfDays() + "");
                                    leaveTransaction.setFromDate(finalLwp.get(0) + "");
                                    leaveTransaction.setToDate(finalLwp.get(finalLwp.size() - 1) + "");
                                    leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(finalLwp.get(0)));
                                    leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(finalLwp.get(finalLwp.size() - 1)));
                                    leaveTransaction.setRequestingDateInMillisecond(Long.parseLong(System.currentTimeMillis() + ""));
                                    leaveTransaction.setRequestingDate(MilliSecondToDDMMYYYV2(System.currentTimeMillis() + ""));
                                    leaveTransaction.setLeaveTypeDescription("LWP");
                                    leaveTransaction.setLeaveType("LWP");
                                    leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
                                    leaveTransaction.setCreatedBy(userName);
                                    leaveTransaction.setNatureType(emp.getNatureType());
                                    leaveTransaction.setFinancialYear(((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
                                    try {
                                        if (((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") != null) {
                                            leaveTransaction.setLeaveType(((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") + "");
                                        }
                                    } catch (Exception e) {

                                    }
                                    String leaveTransactionJson = new Gson().toJson(leaveTransaction);
                                    lwpid = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TRANSACTION, leaveTransactionJson);

                                } else {
                                    resultMap.put("statusMessage", "LeaveAssignLWP");
                                    return new Gson().toJson(resultMap);
                                }
                            } else {

                                resultMap.put("statusMessage", "LeaveTypeLWP");
                                return new Gson().toJson(resultMap);

                            }
                        } catch (Exception e) {

                        }

                    } else {
                        leaveTransaction.setLeaveWithoutPayDays(null);
                        leaveTransaction.setFromDate(leaveTransaction.getFromDate());
                        leaveTransaction.setToDate(leaveTransaction.getToDate());
                        leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
                        leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
                    }
                } else {
                    leaveTransaction.setLeaveWithoutPayDays(null);
                    leaveTransaction.setFromDate(leaveTransaction.getFromDate());
                    leaveTransaction.setToDate(leaveTransaction.getToDate());
                    leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
                    leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
                }
                try {
                    if (finalleavedays.size() != 0) {
                        leaveTransaction.setLwpId(lwpid + "");
                        leaveTransaction.setTotalLeaveDays(finalleave + "");
                        leaveTransaction.setTotalAppliedLeaves(finalleave + "");
                        leaveTransaction.setFromDate(finalleavedays.get(0) + "");
                        leaveTransaction.setToDate(finalleavedays.get(finalleavedays.size() - 1) + "");
                        leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(finalleavedays.get(0)));
                        leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(finalleavedays.get(finalleavedays.size() - 1)));
                    }
                } catch (Exception e) {

                }
                leaveTransaction.setDdo(emp.getDdo());
                leaveTransaction.setDepartment(emp.getDepartment());
                leaveTransaction.setDesignation(emp.getDesignation());
                leaveTransaction.setLocation(emp.getLocation());
                leaveTransaction.setPostingCity(emp.getPostingCity());
                leaveTransaction.setEmployeeCodeM(emp.getEmployeeCodeM());
                leaveTransaction.setEmployeeCode(emp.getEmployeeCode());
                leaveTransaction.setLeaveTypeDescription(leavetypeeDes + "");
                leaveTransaction.setLeaveType(leavetypee + "");
//            leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
//            leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
                leaveTransaction.setRequestingDateInMillisecond(Long.parseLong(System.currentTimeMillis() + ""));
                leaveTransaction.setRequestingDate(MilliSecondToDDMMYYYV2(System.currentTimeMillis() + ""));
                leaveTransaction.setCreateDate(System.currentTimeMillis() + "");
                leaveTransaction.setUpdateDate(System.currentTimeMillis() + "");
                leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
                leaveTransaction.setCreatedBy(userName);
                leaveTransaction.setNatureType(emp.getNatureType());

                LeaveType lt = new LeaveType();
                lt.setHldys(leaveTransaction);
                //   lt.setWeeklyOffs(leaveTransaction);
                //   lt.setLeaveWithoutPay(leaveTransaction);
                lt.setHlfDay(leaveTransaction);
                lt.setTotalLeaveDays(leaveTransaction);
                //Seeting up the total applied leaves based on half day check box
                //
                leaveTransaction.setFinancialYear(((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
                String leaveTransactionJson = new Gson().toJson(leaveTransaction);
                String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TRANSACTION, leaveTransactionJson);
                if (Id != null) {
                    resultMap.put("statusMessage", "success");
                }
                return new Gson().toJson(resultMap);
            }
            resultMap.put("statusMessage", "Attendance");
            return new Gson().toJson(resultMap);
        }
        resultMap.put("statusMessage", new LeaveTransactionManager().checkDateAllLeaveType(leaveTransaction));
        return new Gson().toJson(resultMap);
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TRANSACTION, Id);
        List<LeaveTransaction> leaveTransactionList = new Gson().fromJson(result, new TypeToken<List<LeaveTransaction>>() {
        }.getType());
        if (leaveTransactionList == null || leaveTransactionList.size() < 1) {
            return null;
        }
        return new Gson().toJson(leaveTransactionList.get(0));
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<LeaveTransaction>() {
        }.getType();
        String leaveTransaction = new LeaveTransactionManager().fetch(Id);
        if (leaveTransaction == null || leaveTransaction.isEmpty()) {
            return false;
        }
        LeaveTransaction leaveTransactionrJson = new Gson().fromJson(leaveTransaction, type);
        leaveTransactionrJson.setStatus(ApplicationConstants.INACTIVE);
        leaveTransactionrJson.setUpdatedBy(userName);
        leaveTransactionrJson.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TRANSACTION, Id, new Gson().toJson(leaveTransactionrJson));
        return result;
    }

    public String update(LeaveTransaction leaveTransaction, String loginUserId) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        String assigJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TRANSACTION, leaveTransaction.getRowId());
        List<LeaveTransaction> formulalist = new Gson().fromJson(assigJson, new TypeToken<List<LeaveTransaction>>() {
        }.getType());
        LeaveTransaction dbObj = formulalist.get(0);
        LeaveTransaction assignData = fetchLeaveTransc(leaveTransaction.getRowId());
        String lwpid = "No";
        double finalleave = 0;
        String totalTimeCondi = "No";
        String leaveLimitInst = "0";
        ArrayList<String> finalleavedays = new ArrayList<String>();
        String leavetypee = assignData.getLeaveType();
        String leavetypeeDes = assignData.getLeaveTypeDescription();
        Employee emp = new EmployeeManager().fetchEmployee(leaveTransaction.getEmployeeId());
        String leaveType = new LeaveTypeManager().fetchById(leaveTransaction.getLeaveType());
        Type type = new TypeToken<LeaveTypeMaster>() {
        }.getType();
        String offCoveredFlag = "";
        LeaveTypeMaster leaveTyperJson = new Gson().fromJson(leaveType, type);
        List<LeaveTypeDetails> li = leaveTyperJson.getLeaveTypeDetails();
        if (li.size() != 0) {
            for (int i = 0; i < li.size(); i++) {
                if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                    if (li.get(i).getFixedTimeIssue().equalsIgnoreCase("Yes")) {
                        totalTimeCondi = li.get(i).getTotalTimeIssue();
                    }
                    offCoveredFlag = li.get(i).getOffCovered();
                }
            }
        }
        if (!totalTimeCondi.equalsIgnoreCase("No")) {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("employeeId", leaveTransaction.getEmployeeId());
            conditionMap.put("leaveType", leaveTransaction.getLeaveType());
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
            List<LeaveTransaction> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
            }.getType());
            try {
                if (list.size() >= Integer.parseInt(totalTimeCondi)) {
                    resultMap.put("statusMessage", "LeaveFixedTime");
                    return new Gson().toJson(resultMap);
                }
            } catch (Exception e) {

            }
        }
        leaveTransaction = setTotalAppledLeaves(leaveTransaction, offCoveredFlag);

        if (li.size() != 0) {
            for (int i = 0; i < li.size(); i++) {
                if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                    leaveLimitInst = li.get(i).getLeaveLimitPerInstance();
                }
            }
        }
//        if (!leaveLimitInst.equalsIgnoreCase("No")) {
//            if (Double.parseDouble(leaveLimitInst) < Double.parseDouble(leaveTransaction.getTotalLeaveDays())) {
//                resultMap.put("statusMessage", "LeaveLimit");
//                return new Gson().toJson(resultMap);
//            }
//        }
//        assignData.setTotalAppliedLeaves(leaveTransaction.getTotalAppliedLeaves() + "");
//        assignData.setTotalBalanceLeaves(leaveTransaction.getTotalBalanceLeaves() + "");
//        assignData.setTotalLeaveDays(leaveTransaction.getTotalLeaveDays() + "");
//        assignData.setTotalAppliedLeaves(leaveTransaction.getTotalAppliedLeaves() + "");
//        assignData.setDateRemarksAndIsHalfDay(leaveTransaction.getDateRemarksAndIsHalfDay());
//        assignData.setFromDate(leaveTransaction.getFromDate());
//        assignData.setToDate(leaveTransaction.getToDate());
//        assignData.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
//        assignData.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
//        assignData.setRequestingDateInMillisecond(Long.parseLong(System.currentTimeMillis() + ""));
//        assignData.setRequestingDate(MilliSecondToDDMMYYYV2(System.currentTimeMillis() + ""));
//        assignData.setUpdateDate(System.currentTimeMillis() + "");
//        assignData.setUpdatedBy(userName);
//
//        String leaveTransactionJson = new Gson().toJson(assignData);
//        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TRANSACTION, leaveTransaction.getRowId(), leaveTransactionJson);
//        if (result) {
//            resultMap.put("statusMessage", "success");
//        } else {
//            resultMap.put("statusMessage", "fail");
//        }
//        return new Gson().toJson(resultMap);

        if (!leaveLimitInst.equalsIgnoreCase("No")) {
            if (Double.parseDouble(leaveLimitInst) < Double.parseDouble(leaveTransaction.getTotalLeaveDays())) {
//                double myDb = Double.parseDouble(leaveTransaction.getTotalLeaveDays());
//                int myInt = (int) myDb;
                try {
                    if (((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") != null) {
                        HashMap<String, String> lwpCondition = new HashMap<String, String>();
                        lwpCondition.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                        lwpCondition.put("employeeId", leaveTransaction.getEmployeeId());
                        lwpCondition.put("leaveType", ((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") + "");
                        lwpCondition.put("year", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
                        String lwpResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, lwpCondition);
                        //    lwpCondition.setLeaveType(((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") + "");
                        if (lwpResult != null) {
                            int removeele = Integer.parseInt(leaveLimitInst);
                            ArrayList<String> finalLwp = leaveTransaction.getLeaveWithoutPayDays();
                            for (int i = 0; i < removeele; i++) {
                                finalleavedays.add(finalLwp.get(0));
                                finalLwp.remove(0);
                            }
                            leaveTransaction.setLeaveWithoutPayDays(finalLwp);
                            leaveTransaction.setNoOfLeaveWithoutPayDays(finalLwp.size());
                            int countdays = finalLwp.size();
                            String leaveDays = leaveTransaction.getTotalLeaveDays();
                            finalleave = Double.parseDouble(leaveDays) - (double) countdays;
                            leaveTransaction.setTotalLeaveDays((double) countdays + "");
                            leaveTransaction.setTotalAppliedLeaves((double) countdays + "");
                            leaveTransaction.setFromDate(finalLwp.get(0) + "");
                            leaveTransaction.setToDate(finalLwp.get(finalLwp.size() - 1) + "");
                            leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(finalLwp.get(0)));
                            leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(finalLwp.get(finalLwp.size() - 1)));
                            leaveTransaction.setRequestingDateInMillisecond(Long.parseLong(System.currentTimeMillis() + ""));
                            leaveTransaction.setRequestingDate(MilliSecondToDDMMYYYV2(System.currentTimeMillis() + ""));
                            leaveTransaction.setLeaveTypeDescription("LWP");
                            leaveTransaction.setLeaveType("LWP");
                            leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
                            leaveTransaction.setCreatedBy(userName);
                            leaveTransaction.setNatureType(emp.getNatureType());
                            leaveTransaction.setFinancialYear(((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
                            try {
                                if (((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") != null) {
                                    leaveTransaction.setLeaveType(((LinkedTreeMap) new LeaveTypeManager().fetchLWPId().getId()).get("$oid") + "");
                                }
                            } catch (Exception e) {

                            }
                            String leaveTransactionJson = new Gson().toJson(leaveTransaction);
                            lwpid = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TRANSACTION, leaveTransactionJson);

                        } else {
                            resultMap.put("statusMessage", "LeaveAssignLWP");
                            return new Gson().toJson(resultMap);
                        }
                    } else {

                        resultMap.put("statusMessage", "LeaveTypeLWP");
                        return new Gson().toJson(resultMap);

                    }
                } catch (Exception e) {

                }

            } else {
                leaveTransaction.setLeaveWithoutPayDays(null);
                leaveTransaction.setFromDate(leaveTransaction.getFromDate());
                leaveTransaction.setToDate(leaveTransaction.getToDate());
                leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
                leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
            }
        } else {
            leaveTransaction.setLeaveWithoutPayDays(null);
            leaveTransaction.setFromDate(leaveTransaction.getFromDate());
            leaveTransaction.setToDate(leaveTransaction.getToDate());
            leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
            leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
        }
        try {
            if (finalleavedays.size() != 0) {
                leaveTransaction.setLwpId(lwpid + "");
                leaveTransaction.setTotalLeaveDays(finalleave + "");
                leaveTransaction.setTotalAppliedLeaves(finalleave + "");
                leaveTransaction.setFromDate(finalleavedays.get(0) + "");
                leaveTransaction.setToDate(finalleavedays.get(finalleavedays.size() - 1) + "");
                leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(finalleavedays.get(0)));
                leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(finalleavedays.get(finalleavedays.size() - 1)));
            }
        } catch (Exception e) {

        }
        leaveTransaction.setDdo(emp.getDdo());
        leaveTransaction.setDepartment(emp.getDepartment());
        leaveTransaction.setDesignation(emp.getDesignation());
        leaveTransaction.setLocation(emp.getLocation());
        leaveTransaction.setPostingCity(emp.getPostingCity());
        leaveTransaction.setEmployeeCodeM(emp.getEmployeeCodeM());
        leaveTransaction.setEmployeeCode(emp.getEmployeeCode());
        leaveTransaction.setLeaveTypeDescription(leavetypeeDes + "");
        leaveTransaction.setLeaveType(leavetypee + "");
//            leaveTransaction.setFromDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getFromDate()));
//            leaveTransaction.setToDateInMilliSecond(DDMMYYYYToMilliSecond1(leaveTransaction.getToDate()));
        leaveTransaction.setRequestingDateInMillisecond(Long.parseLong(System.currentTimeMillis() + ""));
        leaveTransaction.setRequestingDate(MilliSecondToDDMMYYYV2(System.currentTimeMillis() + ""));
        leaveTransaction.setCreateDate(System.currentTimeMillis() + "");
        leaveTransaction.setUpdateDate(System.currentTimeMillis() + "");
        leaveTransaction.setStatus(ApplicationConstants.ACTIVE);
        leaveTransaction.setCreatedBy(userName);
        leaveTransaction.setNatureType(emp.getNatureType());

//                LeaveType lt = new LeaveType();
//                lt.setHldys(leaveTransaction);
//                //   lt.setWeeklyOffs(leaveTransaction);
//                //   lt.setLeaveWithoutPay(leaveTransaction);
//                lt.setHlfDay(leaveTransaction);
//                lt.setTotalLeaveDays(leaveTransaction);
        //Seeting up the total applied leaves based on half day check box
        //
        leaveTransaction.setFinancialYear(((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
        String leaveTransactionJson = new Gson().toJson(leaveTransaction);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TRANSACTION, leaveTransaction.getRowId(), leaveTransactionJson);
        if (result) {
            resultMap.put("statusMessage", "success");
        } else {
            resultMap.put("statusMessage", "fail");
        }
        return new Gson().toJson(resultMap);

    }

    public LeaveTransaction fetchLeaveTransc(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String transJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TRANSACTION, rid);
        List<LeaveTransaction> Transclist = new Gson().fromJson(transJson, new TypeToken<List<LeaveTransaction>>() {
        }.getType());
        LeaveTransaction transnleave = Transclist.get(0);
        return transnleave;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
        return result1;
    }

    public String MilliSecondToDDMMYYY(String str) {
        long foo = Long.parseLong(str);
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public String MilliSecondToDDMMYYYV2(String str) {
        long foo = Long.parseLong(str);
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public Long DDMMYYYYToMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    public Long DDMMYYYYToMilliSecond1(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    public String splitdat() {
        String leavedays = "2.0";
        String leavedays1 = "";
        for (int i = 0; i < leavedays.length(); i++) {
            if (leavedays.charAt(i) != '.') {
                leavedays1 = leavedays1 + leavedays.charAt(i);
            } else {
                break;
            }
//            leavedays1=leavedays1+leavedays.charAt(i);
        }

        return leavedays1;

    }

    public List getdatesBetweenTwoDates(String frDt, String toDt) throws ParseException {
        List<Date> dates = new ArrayList<Date>();
        List finaldates = new ArrayList();
        DateFormat formatter;
        try {
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = (Date) formatter.parse(frDt);
            Date endDate = (Date) formatter.parse(toDt);
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }
            for (int i = 0; i < dates.size(); i++) {
                Date lDate = (Date) dates.get(i);
                String ds = formatter.format(lDate);
                finaldates.add(ds);
                System.out.println(" Date is ..." + ds);
            }

        } catch (Exception e) {
        }
        return finaldates;
    }

    public String fetchAll(String employeeId, String leaveTypeId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.EMPLOYEE_ID, employeeId);
        conditionMap.put("leaveType", leaveTypeId);
        FinancialYear leaveFinacialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        conditionMap.put("financialYear", ((LinkedTreeMap) leaveFinacialYear.getId()).get("$oid") + "");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
        JSONArray jSONArrayResult = null;
        if (result1 != null) {
            jSONArrayResult = new JSONArray(result1);

            List<LeaveTransaction> leveTralist = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
            }.getType());
            for (int i = 0; i < leveTralist.size(); i++) {
                LeaveTransaction leaveadata = leveTralist.get(i);
                try {
                    if (leaveadata.getLwpId() != null) {
                        String lwpData = fetch(leaveadata.getLwpId());

                        if (lwpData != null && !lwpData.isEmpty()) {
                            JSONObject leaveTrJSONObject = new JSONObject(lwpData);
                            jSONArrayResult.put(leaveTrJSONObject);
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        if (jSONArrayResult != null && jSONArrayResult.length() > 0) {
            return jSONArrayResult.toString();
        } else {
            return null;
        }

    }

    public LeaveTransaction setTotalAppledLeaves(LeaveTransaction leaveTransaction, String offCoveredFlag) throws Exception {
        //Check leaves Location wise holiday master condition
        ArrayList<String> lwpleaves = new ArrayList<String>();
        List<Map<String, String>> totalappliedLeaves = leaveTransaction.getDateRemarksAndIsHalfDay();
        for (Iterator<Map<String, String>> it = totalappliedLeaves.iterator(); it.hasNext();) {
            Map<String, String> map = it.next();
            lwpleaves.add(map.get("date"));

        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("locationWiseHoliday", leaveTransaction.getLocation());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("year", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
        List list3 = null;
        List datelist1;
        List<String> weeklyOffHalfDayList = new ArrayList<String>();
        int holidayCount = 0;
        double weekOffCount = 0;
        try {
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, conditionMap);
            List<LocationWiseHolidayMasterList> LoHMlist = new Gson().fromJson(result1, new TypeToken<List<LocationWiseHolidayMasterList>>() {
            }.getType());
            LocationWiseHolidayMasterList locationJson = LoHMlist.get(0);
            List<LocationWiseHolidayMaster> li = locationJson.getLocationWiseHolidayFormList();

            List datelist = new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate());
            for (int i = 0; i < li.size(); i++) {
                datelist1 = new LeaveTransactionManager().getdatesBetweenTwoDates(li.get(i).getFromDate(), li.get(i).getToDate());
                list3 = new ArrayList(datelist1);
                list3.retainAll(datelist);
                if (list3.size() != 0) {
                    for (Iterator<String> it = lwpleaves.iterator(); it.hasNext();) {
                        if (list3.contains(it.next())) {
                            it.remove();
                        }
                    }
                }
                holidayCount = holidayCount + list3.size();

                if (list3.size() == 0) {
                    datelist1.clear();
                }

            }
        } catch (Exception e) {

        }

        //Leaves check weekly off master condition  
        if (offCoveredFlag.equalsIgnoreCase(ApplicationConstants.NO)) {
            HashMap<String, String> conditionWeekoff = new HashMap<String, String>();
            conditionWeekoff.put("weeklyOffLocation", leaveTransaction.getLocation());
            conditionWeekoff.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            try {
                String weekoffresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.WEEKLY_OFF_MASTER, conditionWeekoff);
                List<WeeklyOffMasterList> weekoffresultlist = new Gson().fromJson(weekoffresult, new TypeToken<List<WeeklyOffMasterList>>() {
                }.getType());
                WeeklyOffMasterList weekoffJson = weekoffresultlist.get(0);
                List<WeeklyOffMaster> weekoffJsonList = weekoffJson.getWeeklyoffFormList();

//                List<String> DateList = new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate());
                ArrayList<String> DateList = new ArrayList<String>();
                DateList.addAll(lwpleaves);
                for (int k = 0; k < DateList.size(); k++) {
                    String getWeek = new LeaveTransactionManager().getWeekofDate(DateList.get(k));
                    String getDay = new LeaveTransactionManager().getDayofMonth(DateList.get(k));
                    for (int j = 0; j < weekoffJsonList.size(); j++) {
                        if (getDay.equalsIgnoreCase(weekoffJsonList.get(j).getDay())) {
                            if (getWeek.equalsIgnoreCase("first")) {
                                if (weekoffJsonList.get(j).getFirst().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getFirst().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("second")) {
                                if (weekoffJsonList.get(j).getSecond().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getSecond().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("third")) {
                                if (weekoffJsonList.get(j).getThird().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getThird().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("fourth")) {
                                if (weekoffJsonList.get(j).getFourth().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getFourth().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("fifth")) {
                                if (weekoffJsonList.get(j).getFifth().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getFifth().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            }

                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        List<Map<String, String>> appliedLeavesTotal = leaveTransaction.getDateRemarksAndIsHalfDay();
        double count = 0.0;
        double lwpHalfDaycount = 0.0;
        for (Iterator<Map<String, String>> it = appliedLeavesTotal.iterator(); it.hasNext();) {
            Map<String, String> map = it.next();

            if (map.get("isHalfDay").equalsIgnoreCase(ApplicationConstants.TRUE)) {
                if (weeklyOffHalfDayList.contains(map.get("date"))) {
                    count = count + 0.0;
                } else if (map.get("leavetype").equalsIgnoreCase("LWP")) {
                    count = count + 0.0;
                    lwpHalfDaycount = lwpHalfDaycount + 0.5;
                } else {
                    count = count + 0.5;
                }

            } else {
                count++;
            }
        }
        double d = (double) holidayCount + weekOffCount;
        count = count - d;
        // String leavedays = Double.toString(count);
//        String leavedays1 = "";
//        for (int i = 0; i < leavedays.length(); i++) {
//            if (leavedays.charAt(i) != '.') {
//                leavedays1 = leavedays1 + leavedays.charAt(i);
//            } else {
//                break;
//            }
//        }
        leaveTransaction.setLeaveWithoutPayDays(lwpleaves);
        leaveTransaction.setLwpHalfDays(lwpHalfDaycount);
        leaveTransaction.setTotalLeaveDays(count + "");
        leaveTransaction.setTotalAppliedLeaves(count + "");
        return leaveTransaction;
    }

    public String getWeekofDate(String strDate) {

        try {
            Date d = dateFormat.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(d.getTime());

            int wekFMnth = cal.get(Calendar.WEEK_OF_MONTH);

            switch (wekFMnth) {
                case 1:
                    return WeekOfMonth.first.toString();
                case 2:
                    return WeekOfMonth.second.toString();
                case 3:
                    return WeekOfMonth.third.toString();
                case 4:
                    return WeekOfMonth.fourth.toString();
                case 5:
                    return WeekOfMonth.fifth.toString();
                default:
                    break;
            }

        } catch (ParseException parseException) {
        }

        return null;

    }

    private static enum WeekOfMonth {

        first, second, third, fourth, fifth;

    }

//    public String getDayofMonth(String now1) {
//        Date now = new Date(now1);
//        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week abbreviated
//        System.out.println(simpleDateformat.format(now));
//        return simpleDateformat.format(now);
//    }
    public String getDayofMonth(String now1) throws ParseException {
        // String input_date="01/08/2012";
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        Date dt1 = format1.parse(now1);
        DateFormat format2 = new SimpleDateFormat("EEEE");
        String finalDay = format2.format(dt1);
        return finalDay;
    }

    public static void main(String[] args) throws ParseException, Exception {

        System.out.println("resulttttttt" + new LeaveTransactionManager().setList("20/02/2017", "24/02/2017"));
    }

    public String fetchDateType(LeaveTransaction leaveTransaction) throws Exception {
//        if (rid == null) {
//            return null;
//        }
//        String transJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TRANSACTION, rid);
//        List<LeaveTransaction> Transclist = new Gson().fromJson(transJson, new TypeToken<List<LeaveTransaction>>() {
//        }.getType());
//        LeaveTransaction transnleave = Transclist.get(0);
//        return transnleave;
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String totalTimeCondi = "No";
        String leaveLimitInst = "0";
        Employee emp = new EmployeeManager().fetchEmployee(leaveTransaction.getEmployeeId());
        String leaveType = new LeaveTypeManager().fetchById(leaveTransaction.getLeaveType());
        Type type = new TypeToken<LeaveTypeMaster>() {
        }.getType();
        String offCoveredFlag = "";
        LeaveTypeMaster leaveTyperJson = new Gson().fromJson(leaveType, type);
        List<LeaveTypeDetails> li = leaveTyperJson.getLeaveTypeDetails();
        if (li.size() != 0) {
            for (int i = 0; i < li.size(); i++) {
                if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                    if (li.get(i).getFixedTimeIssue().equalsIgnoreCase("Yes")) {
                        totalTimeCondi = li.get(i).getTotalTimeIssue();
                    }
                    offCoveredFlag = li.get(i).getOffCovered();

                }
            }
        }
        if (!totalTimeCondi.equalsIgnoreCase("No")) {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("employeeId", leaveTransaction.getEmployeeId());
            conditionMap.put("leaveType", leaveTransaction.getLeaveType());
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
            List<LeaveTransaction> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
            }.getType());
            try {
                if (list.size() >= Integer.parseInt(totalTimeCondi)) {
                    resultMap.put("statusMessage", "LeaveFixedTime");
                    return new Gson().toJson(resultMap);
                }
            } catch (Exception e) {

            }
        }
        leaveTransaction = setTotalAppled(leaveTransaction, offCoveredFlag);

        if (li.size() != 0) {
            for (int i = 0; i < li.size(); i++) {
                if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                    leaveLimitInst = li.get(i).getLeaveLimitPerInstance();
                }
            }
        }
        if (!leaveLimitInst.equalsIgnoreCase("No")) {
            if (Double.parseDouble(leaveLimitInst) < Double.parseDouble(leaveTransaction.getTotalLeaveDays())) {
//                double myDb = Double.parseDouble(leaveTransaction.getTotalLeaveDays());
//                int myInt = (int) myDb;
                int removeele = Integer.parseInt(leaveLimitInst);
                ArrayList<String> finalLwp = leaveTransaction.getLeaveWithoutPayDays();
                for (int i = 0; i < removeele; i++) {
                    finalLwp.remove(0);
                }
                leaveTransaction.setLeaveWithoutPayDays(finalLwp);
                leaveTransaction.setNoOfLeaveWithoutPayDays(finalLwp.size());
                int countdays = finalLwp.size();
                String leaveDays = leaveTransaction.getTotalLeaveDays();
                double finalleave = Double.parseDouble(leaveDays) - (double) countdays;
                leaveTransaction.setTotalLeaveDays(finalleave + "");
                leaveTransaction.setTotalAppliedLeaves(finalleave + "");
            } else {
                leaveTransaction.setLeaveWithoutPayDays(null);
            }
        } else {
            leaveTransaction.setLeaveWithoutPayDays(null);
        }
        HashMap<String, ArrayList> resultList = new HashMap<String, ArrayList>();

        resultList.put("LWP", leaveTransaction.getLeaveWithoutPayDays());

        //   resultList.put(ApplicationConstants.PAYMENT_VOUCHER, payCount);
        //System.out.println(new Gson().toJson(result));
        return new Gson().toJson(resultList);
    }

    public LeaveTransaction setTotalAppled(LeaveTransaction leaveTransaction, String offCoveredFlag) throws Exception {
        //Check leaves Location wise holiday master condition
        ArrayList<String> lwpleaves = new ArrayList<String>(new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate()));
        //  lwpleaves=new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate());
//        List<Map<String, String>> totalappliedLeaves = leaveTransaction.getDateRemarksAndIsHalfDay();
//        for (Iterator<Map<String, String>> it = totalappliedLeaves.iterator(); it.hasNext();) {
//            Map<String, String> map = it.next();
//            lwpleaves.add(map.get("date"));
//
//        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("locationWiseHoliday", leaveTransaction.getLocation());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("year", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
        List list3 = null;
        List datelist1;
        List<String> weeklyOffHalfDayList = new ArrayList<String>();
        int holidayCount = 0;
        double weekOffCount = 0;
        try {
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, conditionMap);
            List<LocationWiseHolidayMasterList> LoHMlist = new Gson().fromJson(result1, new TypeToken<List<LocationWiseHolidayMasterList>>() {
            }.getType());
            LocationWiseHolidayMasterList locationJson = LoHMlist.get(0);
            List<LocationWiseHolidayMaster> li = locationJson.getLocationWiseHolidayFormList();

            List datelist = new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate());
            for (int i = 0; i < li.size(); i++) {
                datelist1 = new LeaveTransactionManager().getdatesBetweenTwoDates(li.get(i).getFromDate(), li.get(i).getToDate());
                list3 = new ArrayList(datelist1);
                list3.retainAll(datelist);
                if (list3.size() != 0) {
                    for (Iterator<String> it = lwpleaves.iterator(); it.hasNext();) {
                        if (list3.contains(it.next())) {
                            it.remove();
                        }
                    }
                }
                holidayCount = holidayCount + list3.size();

                if (list3.size() == 0) {
                    datelist1.clear();
                }

            }
        } catch (Exception e) {

        }

        //Leaves check weekly off master condition  
        if (offCoveredFlag.equalsIgnoreCase(ApplicationConstants.NO)) {
            HashMap<String, String> conditionWeekoff = new HashMap<String, String>();
            conditionWeekoff.put("weeklyOffLocation", leaveTransaction.getLocation());
            conditionWeekoff.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            try {
                String weekoffresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.WEEKLY_OFF_MASTER, conditionWeekoff);
                List<WeeklyOffMasterList> weekoffresultlist = new Gson().fromJson(weekoffresult, new TypeToken<List<WeeklyOffMasterList>>() {
                }.getType());
                WeeklyOffMasterList weekoffJson = weekoffresultlist.get(0);
                List<WeeklyOffMaster> weekoffJsonList = weekoffJson.getWeeklyoffFormList();
                ArrayList<String> DateList = new ArrayList<String>();
                DateList.addAll(lwpleaves);
                // List<String> DateList = new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate());
                for (int k = 0; k < DateList.size(); k++) {
                    String getWeek = new LeaveTransactionManager().getWeekofDate(DateList.get(k));
                    String getDay = new LeaveTransactionManager().getDayofMonth(DateList.get(k));
                    for (int j = 0; j < weekoffJsonList.size(); j++) {
                        if (getDay.equalsIgnoreCase(weekoffJsonList.get(j).getDay())) {
                            if (getWeek.equalsIgnoreCase("first")) {
                                if (weekoffJsonList.get(j).getFirst().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getFirst().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("second")) {
                                if (weekoffJsonList.get(j).getSecond().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getSecond().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("third")) {
                                if (weekoffJsonList.get(j).getThird().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getThird().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("fourth")) {
                                if (weekoffJsonList.get(j).getFourth().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getFourth().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            } else if (getWeek.equalsIgnoreCase("fifth")) {
                                if (weekoffJsonList.get(j).getFifth().equalsIgnoreCase("FullDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    lwpleaves.remove(DateList.get(k));
                                } else if (weekoffJsonList.get(j).getFifth().equalsIgnoreCase("HalfDay")) {
                                    weekOffCount = weekOffCount + 1;
                                    weeklyOffHalfDayList.add(DateList.get(k));
                                    lwpleaves.remove(DateList.get(k));
                                } else {
                                    weekOffCount = weekOffCount + 0;
                                }

                            }

                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        //    List<Map<String, String>> appliedLeavesTotal = leaveTransaction.getDateRemarksAndIsHalfDay();
        List<String> toDateList = new LeaveTransactionManager().getdatesBetweenTwoDates(leaveTransaction.getFromDate(), leaveTransaction.getToDate());
        double count = (double) toDateList.size();
//        for (Iterator<Map<String, String>> it = appliedLeavesTotal.iterator(); it.hasNext();) {
//            Map<String, String> map = it.next();
//
//            if (map.get("isHalfDay").equalsIgnoreCase(ApplicationConstants.TRUE)) {
//                if (weeklyOffHalfDayList.contains(map.get("date"))) {
//                    count = count + 0.0;
//                } else {
//                    count = count + 0.5;
//                }
//
//            } else {
//                count++;
//            }
//        }
        double d = (double) holidayCount + weekOffCount;
        count = count - d;
        // String leavedays = Double.toString(count);
//        String leavedays1 = "";
//        for (int i = 0; i < leavedays.length(); i++) {
//            if (leavedays.charAt(i) != '.') {
//                leavedays1 = leavedays1 + leavedays.charAt(i);
//            } else {
//                break;
//            }
//        }
        leaveTransaction.setLeaveWithoutPayDays(lwpleaves);
        leaveTransaction.setTotalLeaveDays(count + "");
        leaveTransaction.setTotalAppliedLeaves(count + "");
        return leaveTransaction;
    }

    public String checkDateAllLeaveType(LeaveTransaction leaveTransaction) throws Exception {
        ArrayList<String> lwpleaves = new ArrayList<String>();
        ArrayList<String> datelist = new ArrayList<String>();
        ArrayList<String> list3 = new ArrayList<String>();
        List<Map<String, String>> totalappliedLeaves = leaveTransaction.getDateRemarksAndIsHalfDay();
        for (Iterator<Map<String, String>> it = totalappliedLeaves.iterator(); it.hasNext();) {
            Map<String, String> map = it.next();
            lwpleaves.add(map.get("date"));

        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.EMPLOYEE_ID, leaveTransaction.getEmployeeId());
        FinancialYear leaveFinacialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        conditionMap.put("financialYear", ((LinkedTreeMap) leaveFinacialYear.getId()).get("$oid") + "");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
        if (result1 != null) {
            List<LeaveTransaction> leveTralist = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
            }.getType());
            for (int i = 0; i < leveTralist.size(); i++) {
                LeaveTransaction leaveadata = leveTralist.get(i);
                List<Map<String, String>> totalLeaves = leaveadata.getDateRemarksAndIsHalfDay();
                if (totalLeaves != null) {
                    for (Iterator<Map<String, String>> it = totalLeaves.iterator(); it.hasNext();) {
                        Map<String, String> map = it.next();
                        datelist.add(map.get("date"));

                    }
                }

            }
        }
        list3 = new ArrayList(lwpleaves);
        list3.retainAll(datelist);
        if (list3.size() != 0) {
            return list3.get(0);
        }
        return null;
    }

    public String checkLWPrules(LeaveTransaction leaveTransaction) throws Exception {

        if (getLWPcount(leaveTransaction)) {

        }
        return null;
    }

    public boolean getLWPcount(LeaveTransaction leaveTransaction) throws Exception {
        double count = 0;
        String maxLeave = null;
        Employee emp = new EmployeeManager().fetchEmployee(leaveTransaction.getEmployeeId());
        String leaveType = new LeaveTypeManager().fetchById(leaveTransaction.getLeaveType());
        Type type = new TypeToken<LeaveTypeMaster>() {
        }.getType();
        LeaveTypeMaster leaveTyperJson = new Gson().fromJson(leaveType, type);
        List<LeaveTypeDetails> li = leaveTyperJson.getLeaveTypeDetails();
        if (li.size() != 0) {
            for (int i = 0; i < li.size(); i++) {
                if (emp.getNatureType().equalsIgnoreCase(li.get(i).getNatureType()) && emp.getClassMaster().equalsIgnoreCase(li.get(i).getEmployeeCategory())) {
                    maxLeave = li.get(i).getMaxLeavePerYear();
                }
            }
        }

        if (maxLeave != null) {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.EMPLOYEE_ID, leaveTransaction.getEmployeeId());
            FinancialYear leaveFinacialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
            conditionMap.put("financialYear", ((LinkedTreeMap) leaveFinacialYear.getId()).get("$oid") + "");
            conditionMap.put("leaveType", leaveTransaction.getLeaveType());
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
            if (result1 != null) {
                List<LeaveTransaction> leveTralist = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
                }.getType());
                for (int i = 0; i < leveTralist.size(); i++) {
                    LeaveTransaction leaveadata = leveTralist.get(i);
                    count = count + Double.parseDouble(leaveadata.getTotalAppliedLeaves());
                }
            }

        }
        if (Double.parseDouble(maxLeave) > count) {
            return true;
        }
        return false;
    }

    public List setList(String frmdt, String toDt) throws ParseException {
        List datelist = new LeaveTransactionManager().getdatesBetweenTwoDates(frmdt, toDt);
        List<Map<String, String>> finaldt = new ArrayList<Map<String, String>>();
        for (int i = 0; i < datelist.size(); i++) {
            Map<String, String> hshdt = new HashMap<String, String>();
            hshdt.put("date", datelist.get(i).toString());
            hshdt.put("reason", "");
            hshdt.put("isHalfDay", "false");
            finaldt.add(hshdt);
        }

        return finaldt;
    }
}
