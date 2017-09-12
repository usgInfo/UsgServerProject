/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.pojo;

import com.accure.accure.db.Query;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.Formula;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.manager.EmployeeManager;
import static com.accure.hrms.manager.EmployeeManager.fetchAllDeductionsSalaryHead;
import static com.accure.hrms.manager.EmployeeManager.fetchBasicEarningHead;
import static com.accure.hrms.manager.EmployeeManager.fetchGradepayEarningHead;
import static com.accure.hrms.manager.EmployeeManager.getHeadAmount;
import static com.accure.hrms.manager.EmployeeManager.round;
import com.accure.hrms.manager.ExpressionCalculatorManager;
import com.accure.hrms.manager.SalaryHeadManager;
import com.accure.leave.dto.EmployeeLeaveAssignment;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.dto.LeaveTypeDetails;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.leave.dto.LocationWiseHolidayMaster;
import com.accure.leave.manager.FinancialYearManager;
import com.accure.leave.manager.LeaveTypeManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author jhhgj
 */
public class LeaveType {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dateFormatHyp = new SimpleDateFormat("dd-MM-yyyy");
    private final SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
    private final List<String> formulaList = new ArrayList();

    public void setWeeklyOffs(LeaveTransaction lveTrnsctns) {

        Map<String, String> filter = new HashMap();
        filter.put("weeklyOffLocation", lveTrnsctns.getLocation());
        filter.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        try {
            String wekOfMnth = getWekofMnthForDte(lveTrnsctns.getFromDate());

            String weklyOffRecrds = DBManager.getDbConnection().fetchAllRowsByConditions(
                    ApplicationConstants.WEEKLY_OFF_MASTER, filter);

            String dayfWek = dayOfWeek.format(dateFormat.parse(
                    lveTrnsctns.getFromDate()));

            String isOffCoveredStr = null;

            if (weklyOffRecrds != null) {
                JSONParser parser = new JSONParser();
                JSONArray wekOffMstrLst = (JSONArray) parser.parse(weklyOffRecrds);
                if (wekOffMstrLst != null) {
                    JSONObject wekOffMStr = (JSONObject) wekOffMstrLst.get(0);
                    if (wekOffMStr != null) {
                        JSONArray days = (JSONArray) wekOffMStr.get("weeklyoffFormList");
                        if (days != null) {
                            for (Object day : days) {
                                JSONObject jsonDay = (JSONObject) day;
                                if ((jsonDay.get("day") + "").equalsIgnoreCase(dayfWek)) {
                                    String weklyOff = (String) jsonDay.get(wekOfMnth);
                                    if (weklyOff.equalsIgnoreCase("none")) {
                                        return;
                                    } else {
                                        String lveDaysStr = lveTrnsctns.getTotalLeaveDays();
                                        if (lveDaysStr != null) {
                                            Float lveDays = Float.parseFloat(lveDaysStr);
                                            Float applLveDays = Float.parseFloat(lveTrnsctns.getTotalAppliedLeaves()
                                                    == null ? "0" : lveTrnsctns.getTotalAppliedLeaves());
                                            String lveTypeId = lveTrnsctns.getLeaveType();
                                            LeaveTypeDetails lveTypDtls = getLveTypDtls(lveTypeId,
                                                    lveTrnsctns.getNatureType());
                                            if (lveTypDtls != null) {
                                                isOffCoveredStr = lveTypDtls.getOffCovered();
                                                if (isOffCoveredStr != null) {
                                                    if (isOffCoveredStr.equalsIgnoreCase("yes")) {
                                                        if (lveDays > 0) {
                                                            if (weklyOff.equalsIgnoreCase("halfday")) {
                                                                lveTrnsctns.setTotalLeaveDays(lveDays - 0.5f + "");
                                                                lveTrnsctns.setTotalAppliedLeaves(applLveDays - 0.5f + "");
                                                            } else {
                                                                lveTrnsctns.setTotalLeaveDays(lveDays - 1.0f + "");
                                                                lveTrnsctns.setTotalAppliedLeaves(applLveDays - 1.0f + "");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {

        }

    }

    public String getWekofMnthForDte(String strDate) {

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

    public void setLeaveWithoutPay(LeaveTransaction lveTrnsctns) {

        try {
            String lveTypeId = lveTrnsctns.getLeaveType();
            LeaveTypeDetails lveTypDtls = getLveTypDtls(lveTypeId, lveTrnsctns.getNatureType());
            String curLveDysStr = lveTrnsctns.getTotalLeaveDays();
            Float curLveDys = Float.parseFloat(curLveDysStr == null ? "0" : curLveDysStr);
            String empIdStr = lveTrnsctns.getEmployeeId();

            Map<String, String> lveTrnscnFltr = new HashMap();
            lveTrnscnFltr.put("employeeId", empIdStr);
            lveTrnscnFltr.put("leaveType", lveTypeId);
            lveTrnscnFltr.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            int prevlveLmtPrInstnce = DBManager.getDbConnection().getCount(
                    ApplicationConstants.LEAVE_TRANSACTION, lveTrnscnFltr);

            if (lveTypDtls != null) {
                String isFxdTmeIsue = lveTypDtls.getFixedTimeIssue();
                Float lveLmtPrInstnce = Float.parseFloat(lveTypDtls.getLeaveLimitPerInstance()
                        == null ? "0" : lveTypDtls.getLeaveLimitPerInstance());

                String maxLvsPerYearStr = lveTypDtls.getMaxLeavePerYear();

                Map<String, String> filter = new HashMap();
                filter.put("employeeId", lveTrnsctns.getEmployeeId());
                filter.put("leaveType", lveTrnsctns.getLeaveType());
                filter.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

                String prevLeveTrnsctnStr = DBManager.getDbConnection().fetchAllRowsByConditions(
                        ApplicationConstants.LEAVE_TRANSACTION, filter);

                if (prevLeveTrnsctnStr != null) {
                    List<LeaveTransaction> prevLeveTrnsctnLst
                            = new Gson().fromJson(prevLeveTrnsctnStr,
                                    new TypeToken<List<LeaveTransaction>>() {
                                    }.getType());
                    Collections.sort(prevLeveTrnsctnLst, new SortLveTrnsctnOnUpdteDte());
                    LeaveTransaction prevLeveTrnsctn = prevLeveTrnsctnLst.get(0);

                    if (prevLeveTrnsctn != null) {
                        String prevApplLevesStr = prevLeveTrnsctn.getTotalAppliedLeaves();

                        Float prevApplLeves = Float.parseFloat(prevApplLevesStr);
                        Float maxLvsPerYear = Float.parseFloat(maxLvsPerYearStr);
                        Float lveWthtPay = Float.parseFloat(Integer.toString(prevLeveTrnsctn.getNoOfLeaveWithoutPayDays())
                                == null ? "0" : Integer.toString(prevLeveTrnsctn.getNoOfLeaveWithoutPayDays()));
                        Float curTotlLveDays = (curLveDys + prevApplLeves);

//                        if (isFxdTmeIsue != null && isFxdTmeIsue.equalsIgnoreCase("yes")) {
//                            if ((prevlveLmtPrInstnce + 1) > lveLmtPrInstnce) {
//                                lveTrnsctns.setNoOfLeaveWithoutPayDays((float)curLveDys);
//                            }
//                        } else if (curTotlLveDays > maxLvsPerYear) {
//                            lveTrnsctns.setNoOfLeaveWithoutPayDays((lveWthtPay
//                                    + (curTotlLveDays - maxLvsPerYear)) + "");
//                        } else if (curLveDys > lveLmtPrInstnce) {
//                            lveWthtPay = Float.parseFloat(prevLeveTrnsctn.getNoOfLeaveWithoutPayDays()
//                                    == null ? "0" : prevLeveTrnsctn.getNoOfLeaveWithoutPayDays());
//                            lveTrnsctns.setNoOfLeaveWithoutPayDays((lveWthtPay
//                                    + (curLveDys - lveLmtPrInstnce)) + "");
//                        }
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    public LeaveTypeDetails getLveTypDtls(String lveTypeId, String natureType) {

        try {
            String lveType = new LeaveTypeManager().fetchId(lveTypeId);
            if (lveType != null) {
                LeaveTypeMaster lveTypeMStr = new Gson().fromJson(lveType,
                        new TypeToken<LeaveTypeMaster>() {
                        }.getType());
                List<LeaveTypeDetails> lveTypDtlsLst = lveTypeMStr.getLeaveTypeDetails();
                if (lveTypDtlsLst != null) {
                    for (LeaveTypeDetails lveTypDtls : lveTypDtlsLst) {
                        if (lveTypDtls.getNatureType() != null
                                && lveTypDtls.getNatureType().equalsIgnoreCase(natureType)) {

                            return lveTypDtls;
                        }
                    }
                }
            }
        } catch (Exception exception) {
        }

        return null;
    }

    public void setTotalLeaveDays(LeaveTransaction currlveTrnsctns) {

        String empId = currlveTrnsctns.getEmployeeId();
        try {
            if (empId != null) {

                Map<String, String> filter = new HashMap();
                filter.put("employeeId", currlveTrnsctns.getEmployeeId());
                filter.put("leaveType", currlveTrnsctns.getLeaveType());
                filter.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

                String empStr = DBManager.getDbConnection().fetchAllRowsByConditions(
                        ApplicationConstants.LEAVE_TRANSACTION, filter);
                //   String currTotlLveDysStr = currlveTrnsctns.getTotalLeaveDays();
                String currTotlLveDysStr = currlveTrnsctns.getTotalAppliedLeaves();
                Float currTotlLveDys = Float.parseFloat(currTotlLveDysStr.equalsIgnoreCase("")
                        ? "0" : currTotlLveDysStr);
                if (empStr != null) {
                    List<LeaveTransaction> lveTrnsctnLst = new Gson().fromJson(empStr,
                            new TypeToken<List<LeaveTransaction>>() {
                            }.getType());
                    if (lveTrnsctnLst != null && lveTrnsctnLst.size() > 0) {
                        Collections.sort(lveTrnsctnLst, new SortLveTrnsctnOnUpdteDte());
                        LeaveTransaction prevlveTrnsctn = lveTrnsctnLst.get(0);

                        String totlApplLveDysStr = prevlveTrnsctn.getTotalAppliedLeaves();
                        if (!currTotlLveDysStr.equalsIgnoreCase("")
                                || !totlApplLveDysStr.equalsIgnoreCase("")) {

                            Float totlApplLveDys = Float.parseFloat(totlApplLveDysStr);
                            totlApplLveDys += currTotlLveDys;

                            currlveTrnsctns.setTotalAppliedLeaves(totlApplLveDys + "");

                            /*String blnceLvsStr = currlveTrnsctns.getTotalBalanceLeaves();
                             Float blnceLvs = Float.parseFloat(blnceLvsStr == 
                             null ? "0" : blnceLvsStr);
                             currlveTrnsctns.setTotalBalanceLeaves((
                             blnceLvs - totlApplLveDys) + "");*/
                        }
                    }
                }
                currlveTrnsctns.setTotalAppliedLeaves(currTotlLveDys + "");
            }
        } catch (Exception exception) {
        }
    }

    private static enum WeekOfMonth {

        first, second, third, fourth, fifth;

    }

    private class SortLveTrnsctnOnUpdteDte implements Comparator<LeaveTransaction> {

        @Override
        public int compare(LeaveTransaction o1, LeaveTransaction o2) {
            String updDte1Str = o1.getUpdateDate();
            String updDte2Str = o2.getUpdateDate();
            Long updDte1 = 0l;
            Long updDte2 = 0l;
            if (!updDte1Str.equalsIgnoreCase("") || !updDte2Str.equalsIgnoreCase("")) {
                updDte1 = Long.parseLong(o1.getUpdateDate());
                updDte2 = Long.parseLong(o2.getUpdateDate());

            }
            return updDte2.compareTo(updDte1);
        }
    }

    public LeaveTransaction getRcntLveTrnsctn(String empId, String lveTyp) throws Exception {
        double totalLeaveApplied = 0;
        Map<String, String> filter = new HashMap();
        filter.put("employeeId", empId);
        filter.put("leaveType", lveTyp);
        filter.put("financialYear", ((LinkedTreeMap) new FinancialYearManager().fetchActiveLeaveFinancialYear().getId()).get("$oid") + "");
        filter.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        try {
            String weklyOffRecrds = DBManager.getDbConnection().fetchAllRowsByConditions(
                    ApplicationConstants.LEAVE_TRANSACTION, filter);

            if (weklyOffRecrds != null) {
                List<LeaveTransaction> lveTrnsctnLst = new Gson().fromJson(weklyOffRecrds, new TypeToken<List<LeaveTransaction>>() {
                }.getType());

                if (lveTrnsctnLst != null && lveTrnsctnLst.size() > 0) {
//                    Collections.sort(lveTrnsctnLst, new SortLveTrnsctnOnUpdteDte());
                    for (Iterator<LeaveTransaction> it = lveTrnsctnLst.iterator(); it.hasNext();) {
                        LeaveTransaction leaveTransaction = it.next();
                        totalLeaveApplied = totalLeaveApplied + Double.parseDouble(leaveTransaction.getTotalLeaveDays());
                    }
                    lveTrnsctnLst.get(0).setTotalLeaveDays(totalLeaveApplied + "");
                    return lveTrnsctnLst.get(0);
                } else {
                    LeaveTransaction leaveTransaction = new LeaveTransaction();
                    leaveTransaction.setTotalLeaveDays("0");
                    return leaveTransaction;
                }
            } else {
                LeaveTransaction leaveTransaction = new LeaveTransaction();
                leaveTransaction.setTotalLeaveDays("0");
                return leaveTransaction;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    public void updateRecentLeaveTransaction(String empId, String lveTyp, LeaveTransaction newLveTrsctn) throws Exception {

        LeaveTransaction lveTrsctn = getRcntLveTrnsctn(empId, lveTyp);

        try {
            DB db = DBManager.getDB();
            DBCollection collection = db.getCollection(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE);

            BasicDBObject updtObjct = new BasicDBObject();
            updtObjct.put("$set", newLveTrsctn);

            BasicDBObject quryObjct = new BasicDBObject();
            quryObjct.put("_id", new ObjectId(((LinkedTreeMap) lveTrsctn.getId()).get("$oid") + ""));

            collection.update(quryObjct, updtObjct);
        } catch (NumberFormatException numberFormatException) {
        }

    }

    public void setHldys(LeaveTransaction lveTrsctn) {

        try {
            String lctn = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION,
                    lveTrsctn.getLocation());
            if (lctn != null) {
                List<LocationWiseHolidayMaster> lctnWseHldyMstrLst = new Gson().fromJson(lctn,
                        new TypeToken<List<LocationWiseHolidayMaster>>() {
                        }.getType());
                if (lctnWseHldyMstrLst != null && lctnWseHldyMstrLst.size() > 0) {
                    LocationWiseHolidayMaster lctnWseHldyMstr = lctnWseHldyMstrLst.get(0);
                    String hldyFromDateStr = lctnWseHldyMstr.getFromDate();
                    String hldyToDateStr = lctnWseHldyMstr.getToDate();

                    Date hldyFromDate = dateFormatHyp.parse(hldyFromDateStr);
                    Date hldyToDate = dateFormatHyp.parse(hldyToDateStr);

                    String lveTrsctnFrmDteStr = lveTrsctn.getFromDate();
                    String lveTrsctnToDteStr = lveTrsctn.getToDate();

                    Date lveTrsctnFrmDte = dateFormat.parse(lveTrsctnFrmDteStr);
                    Date lveTrsctnToDte = dateFormat.parse(lveTrsctnToDteStr);

                    Float lveDys = Float.parseFloat(lveTrsctn.getTotalLeaveDays() == null
                            ? "0" : lveTrsctn.getTotalLeaveDays());

                    if (lveTrsctnFrmDte.compareTo(hldyFromDate) >= 0
                            && lveTrsctnFrmDte.compareTo(hldyToDate) <= 0) {
                        if (lveTrsctnToDte.compareTo(hldyToDate) <= 0) {
                            lveTrsctn.setTotalLeaveDays("0");
                        } else {
                            float diffInDays = (hldyToDate.getTime()
                                    - lveTrsctnFrmDte.getTime()) / (1000 * 60 * 60 * 24);
                            lveTrsctn.setTotalLeaveDays((lveDys - diffInDays) + "");
                        }
                    } else if (lveTrsctnToDte.compareTo(hldyToDate) <= 0
                            && lveTrsctnToDte.compareTo(hldyFromDate) >= 0) {
                        int diffInDays = (int) ((lveTrsctnToDte.getTime()
                                - hldyFromDate.getTime()) / (1000 * 60 * 60 * 24));
                        lveTrsctn.setTotalLeaveDays((lveDys - diffInDays) + "");
                    }
                }
            }
        } catch (Exception exception) {
        }
    }

    public void setHlfDay(LeaveTransaction lveTrsctn) {

        Float lveDys = Float.parseFloat(lveTrsctn.getTotalLeaveDays() == null
                ? "0" : lveTrsctn.getTotalLeaveDays());
        List<Map<String, String>> dteRmrksAdIsHlfDayLst = lveTrsctn.getDateRemarksAndIsHalfDay();
        if (dteRmrksAdIsHlfDayLst != null) {
            for (Map<String, String> dteRmrksAdIsHlfDay : dteRmrksAdIsHlfDayLst) {
                if (dteRmrksAdIsHlfDay.get("isHalfDay") != null
                        && dteRmrksAdIsHlfDay.get("isHalfDay").equalsIgnoreCase("true")) {
                    if (lveDys > 0f) {
                        lveDys -= 0.5f;
                    }
                }
            }
        }
    }

    public void getEmployeeDetails() {

    }

    public Double calculateEmployeeEncashableLeaves(String ddo,String location,String employeeId,
            String leaveType, String leaveToBeEnchashedStr, String natureType) {
        try {
        HashMap condMap = new HashMap();
        condMap.put("employeeId", employeeId);
        condMap.put("ddo", ddo);
        //condMap.put("location", location);
        condMap.put("leaveType", leaveType);
        String employeeCategory="";
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        condMap.put("year", activeFinancialyearId);
        condMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, condMap);
        List<EmployeeLeaveAssignment> leaveAssignmentListStr = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        if(result!=null)
        {
            natureType=leaveAssignmentListStr.get(0).getNatureType();
            employeeCategory=leaveAssignmentListStr.get(0).getEmployeeCategory();
        }
            Double leaveToBeEnchashed = Double.parseDouble(leaveToBeEnchashedStr);
            EmployeeManager emplMngr = new EmployeeManager();
            String emplyStr = emplMngr.fetch(employeeId);
            if (emplyStr != null) {
            Employee emply = new Gson().fromJson(emplyStr,
            new TypeToken<Employee>() {}.getType());
            long emplyBasic = emply.getBasic();
            long gradePay1 = emply.getGradePay();
            String gradePayId = emply.getGrade();
            SalaryHeadManager slryHedMngr = new SalaryHeadManager();
            LeaveTypeDetails lveTypDtls = getLveTypDetals(leaveType, natureType,employeeCategory);
            if (lveTypDtls != null) 
            {
                String frmlStrng = lveTypDtls.getFormula();
                if (frmlStrng != null)
                {
                    if (!frmlStrng.equalsIgnoreCase("")) 
                    {
                        HashMap<String, String> resultMap = new HashMap<String, String>();
                        List<SalaryHead> earningHeads = new ArrayList<SalaryHead>();
                        List<SalaryHead> earningHeadss = new ArrayList<SalaryHead>();
                        earningHeadss = new EmployeeManager().fetchAllEarningsSalaryHead();
                        if (earningHeadss != null) 
                        {
                            earningHeads.addAll(earningHeadss);
                        }
                        List<SalaryHead> deductionHeads = new ArrayList<SalaryHead>();
                        deductionHeads = fetchAllDeductionsSalaryHead();
                        String basicId = null;
                        String gradepaySalaryHeadId = null;
                        Long basicAmount = emply.getBasic();
                        double basicAmountInDouble = emply.getBasic();
                        long gradePay = emply.getGradePay();
                        SalaryHead basicEarningHead = fetchBasicEarningHead();
                        SalaryHead gardepayEarningHead = fetchGradepayEarningHead();
                         String basicAmountInString = "" + basicAmount;
                        String gradepayAmountInString = "" + gradePay;
                        if (basicEarningHead != null) 
                        {
                            basicId = ((LinkedTreeMap<String, String>) basicEarningHead.getId()).get("$oid");
                        } 
                        if (gardepayEarningHead != null)
                        {
                            gradepaySalaryHeadId = ((LinkedTreeMap<String, String>) gardepayEarningHead.getId()).get("$oid");
                        }
                        String  forml = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, frmlStrng);
                        List<Formula> formuList = null;
                        if (forml != null)
                        {
                            formuList = new Gson().fromJson(forml, new TypeToken<List<Formula>>() {}.getType());
                        }  
                         Formula forobj = formuList.get(0);
                         String formu = forobj.getHiddenformula();
                         List<Integer> liIndexes = new ArrayList<Integer>();
                         List<String> idlist = new ArrayList<String>();
                         boolean condition = true;
                         while (condition) 
                         {
                            liIndexes.clear();
                            idlist.clear();
                            condition = false;
                            char[] chrArray = formu.toCharArray();
                            int j = 0;
                            for (int i = 0; i < chrArray.length; i++) 
                            {
                                char c = chrArray[i];
                                if (c == '#') {
                                    liIndexes.add(i);
                                }
                            }
                            for (int k = 0; k < liIndexes.size(); k = k + 2) 
                            {
                                idlist.add(formu.substring(liIndexes.get(k) + 1, liIndexes.get(k + 1)));
                            }
                            for (Iterator<String> iterator = idlist.iterator(); iterator.hasNext();) 
                            {
                                String next = iterator.next();
                                if (next.equalsIgnoreCase(basicId)) {
                                    formu = formu.replaceAll("#" + basicId + "#", basicAmountInString);
                                } else if (next.equalsIgnoreCase(gradepaySalaryHeadId)) {
                                    formu = formu.replaceAll("#" + gradepaySalaryHeadId + "#", gradepayAmountInString);
                                } else {
                                    SalaryHead sal = new Gson().fromJson(new SalaryHeadManager().fetchRawData(next), new TypeToken<SalaryHead>() {
                                    }.getType());
                                    formu = formu.replaceAll("#" + next + "#", getHeadAmount(sal, basicId, basicAmount, gradePay, emply, gradepaySalaryHeadId) + "");
        //                            formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(sal.getFormula()).getHiddenformula());
                                }
                                condition = false;
                                char[] chrArrayForCheck = formu.toCharArray();
                                for (int i = 0; i < chrArrayForCheck.length; i++)
                                {
                                    char c = chrArrayForCheck[i];
                                    if (c == '#') {
                                        condition = true;
                                        break;
                                    }
                                }
                            }
                        }   
                         return leaveToBeEnchashed * ExpressionCalculatorManager.calculateTheValue(formu);
                        } 
                        else if (lveTypDtls.getAmount() != null && !lveTypDtls.getAmount().equalsIgnoreCase("")) 
                        {
                            return leaveToBeEnchashed
                                    * Double.parseDouble(lveTypDtls.getAmount());
                        }
                    }
                    
                }
            }
        } catch (Exception exception) {
        }

        return 0d;
    }
public LeaveTypeDetails getLveTypDetals(String lveTypeId, String natureType,String employeeCategory) {

        try {
            String lveType = new LeaveTypeManager().fetchId(lveTypeId);
            if (lveType != null) {
                LeaveTypeMaster lveTypeMStr = new Gson().fromJson(lveType,
                        new TypeToken<LeaveTypeMaster>() {
                        }.getType());
                List<LeaveTypeDetails> lveTypDtlsLst = lveTypeMStr.getLeaveTypeDetails();
                if (lveTypDtlsLst != null) {
                    for (LeaveTypeDetails lveTypDtls : lveTypDtlsLst) {
                        if ((lveTypDtls.getEmployeeCategory() != null
                                && lveTypDtls.getEmployeeCategory().equalsIgnoreCase(employeeCategory))&&(lveTypDtls.getNatureType() != null
                                && lveTypDtls.getNatureType().equalsIgnoreCase(natureType))) {

                            return lveTypDtls;
                        }
                    }
                }
            }
        } catch (Exception exception) {
        }

        return null;
    }
    public List<String> getIdListFromFormulaString(String formulaStrng) {

        if (formulaStrng.contains("#")) {
            int startIndx = formulaStrng.indexOf("#");
            int endIndx = formulaStrng.indexOf("#", startIndx);
            formulaList.add(formulaStrng.substring(startIndx, endIndx));

            return getIdListFromFormulaString(formulaStrng.substring(endIndx));
        } else {
             formulaList.add(formulaStrng);
            return formulaList;
        }
    }

    public boolean isIdStringBasic(String idStrng) {

        List<Query> queryLst = new ArrayList();
        Query qry1 = new Query();
        qry1.setColumnName("_id");
        qry1.setColumnValue(idStrng);
        qry1.setCondition("eq");
        queryLst.add(qry1);

        Query qry2 = new Query();
        qry2.setColumnName("isBasic");
        qry2.setColumnValue("Yes");
        qry2.setCondition("eq");
        queryLst.add(qry2);

        Query qry3 = new Query();
        qry3.setColumnName("status");
        qry3.setColumnValue("Active");
        qry3.setCondition("eq");
        queryLst.add(qry3);

        try {
            String slryHedStr = DBManager.getDbConnection().fetchRowsByConditions("salaryhead", queryLst);

            if (slryHedStr != null && !slryHedStr.equalsIgnoreCase("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
        }

        return false;
    }

    public static void main(String args[]) throws ParseException {
        // System.out.println("dayyy:"+new LeaveType().getWekofMnthForDte("31/01/2017"));
     //   System.out.println("Dayyyyyy:" + new LeaveType().getDayofMonth("28/02/2017"));
    }

}
