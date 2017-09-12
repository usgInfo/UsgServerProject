/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.budget.dto.FundType;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.ArrearConfig;
import com.accure.payroll.dto.ArrearProcess;
import com.accure.payroll.dto.ArrearUnProcess;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.Earnings;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.FinancialYear;
import static com.accure.hrms.manager.EmployeeManager.round;
import com.accure.payroll.dto.Deductions;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 *
 * @author Misha Thomas
 */
public class EmployeeArrearManager {

    public static ArrearProcess fetch(String primaryKey) throws Exception {
        ArrearProcess ap = null;
        if (primaryKey == null || primaryKey.isEmpty()) {
            return ap;
        }
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_PROCESS_TABLE, primaryKey);
        if (existrelationJson != null) {
            List<ArrearProcess> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<ArrearProcess>>() {
            }.getType());
            ap = relationlist.get(0);
        }
        return ap;
    }

    public HashMap getSearchResult(String ArrearProcessJsonString, String financialYear) throws Exception {

        HashMap outputMap = new HashMap();
        if (ArrearProcessJsonString == null) {
            return null;
        }
        RestClient aql = new RestClient();
        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        String autoSalTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
        String ArrearProcessTable = ApplicationConstants.USG_DB1 + ApplicationConstants.ARREAR_PROCESS_TABLE + "`";
        //json from UI
        ArrearProcess empobj = new Gson().fromJson(ArrearProcessJsonString, new TypeToken<ArrearProcess>() {
        }.getType());
        int month1 = empobj.getFromMonth() + 1;
        int month2 = empobj.getToMonth() + 1;
        int year1 = empobj.getFromYear();
        int year2 = empobj.getToYear();
        String fromDate1 = month1 + "-" + year1;
        String toDate1 = month2 + "-" + year2;
        List<String> dates = getdatesbetweeninterval(fromDate1, toDate1);
        Map<String, String> map2 = new HashMap<String, String>();
        for (String date : dates) {
            String[] splitString = date.split("-");
            map2.put(splitString[0], splitString[1]);
        }
//        String fromDate = empobj.getFromDate();
//        String toDate = empobj.getToDate();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
//        Date startDate = formatter.parse(fromDate);
//        Date endDate = formatter.parse(fromDate);
//        Calendar startCalendar = new GregorianCalendar();
//        startCalendar.setTime(startDate);
//        Calendar endCalendar = new GregorianCalendar();
//        endCalendar.setTime(endDate);
//        Calendar cal = Calendar.getInstance();
//        Calendar cal1 = Calendar.getInstance();
//        cal.setTime(startDate);
//        cal1.setTime(endDate);

        String processedEmpCodes = "";
        String autoSalryEmpCodes = "";
        List<ArrearProcess> processedList = null;
        List<ArrearProcess> lockedList = null;
        String cond = "";
        String[] FinYearArr = financialYear.split("~");
        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialYear(FinYearArr[0], FinYearArr[1]);
        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyObj = fyList.get(0);
        String fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
        //System.out.println("-------------search---fyId-------" + fyId);
        String finYearCond = " and emp.finacialYear=\"" + fyId + "\"";
        if (!map2.isEmpty()) {
            for (Map.Entry<String, String> entry : map2.entrySet()) {
                cond = cond + "(emp.month= " + Integer.parseInt(entry.getKey()) + " and emp.year= " + Integer.parseInt(entry.getValue()) + ") OR ";
            }
            cond = cond.substring(0, cond.length() - 3);
        }
          String cond111 = "and emp.arrearType=\"" + empobj.getArrearType()+ "\"";
        long fromDateInlong = getInMilliSecond(empobj.getFromDate());
        long toDateInlong = getInMilliSecond(empobj.getToDate());
        String ArrearProcessQuery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo,emp.basic,emp.totalEarnings,emp.totalDeductions,emp.netPay from " + ArrearProcessTable + ""
                + " as emp where  arrearStatus=\"Processed\" and  emp.lockStatus=false " + finYearCond + " and ddo=\"" + empobj.getDdo() + "\" " + " and (" + cond + ")";
//                + " as emp where  arrearStatus=\"Processed\" and  emp.lockStatus=false " + finYearCond + " and ddo=\"" + empobj.getDdo() + "\" and (fromDateInMilliSecond > " + fromDateInlong + " or fromDateInMilliSecond < " + toDateInlong + ") and (toDateInMilliSecond >" + fromDateInlong +" or toDateInMilliSecond <" + toDateInlong + ")";

        String lockedArrearQuery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo,emp.basic,emp.totalEarnings,emp.totalDeductions,emp.netPay from " + ArrearProcessTable + ""
                + " as emp where arrearStatus=\"Processed\" and  emp.lockStatus=true " + finYearCond + " and ddo=\"" + empobj.getDdo()+ "\" " + " and (" + cond + ")";
//                + " as emp where arrearStatus=\"Processed\" and  emp.lockStatus=true " + finYearCond + " and ddo=\"" + empobj.getDdo() + "\" and (fromDateInMilliSecond < " + fromDateInlong + " or fromDateInMilliSecond > " + toDateInlong + ") and (toDateInMilliSecond <" + fromDateInlong +" or toDateInMilliSecond >" + toDateInlong + ")";

        String lockedArrearOutput = aql.getRestData(ApplicationConstants.END_POINT, lockedArrearQuery);
        if (lockedArrearOutput != null) {
            lockedList = new Gson().fromJson(lockedArrearOutput, new TypeToken<ArrayList<ArrearProcess>>() {
            }.getType());
            for (ArrearProcess as : lockedList) {
                processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeeCode() + "\",";
            }
        }
        String ArrearProcessOutput = aql.getRestData(ApplicationConstants.END_POINT, ArrearProcessQuery);
        //System.out.println("---------ArrearProcessOutput---------" + ArrearProcessOutput);
        if (ArrearProcessOutput != null) {
            processedList = new Gson().fromJson(ArrearProcessOutput, new TypeToken<ArrayList<ArrearProcess>>() {
            }.getType());
            for (ArrearProcess as : processedList) {
                processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeeCode() + "\",";
            }
        }
        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
            processedEmpCodes = "(" + processedEmpCodes.substring(0, processedEmpCodes.length() - 1) + ")";
        }
        //fetch employees from employee table
        List<Employee> empList = null;
        List<AutoSalaryProcess> autoSalempList = null;
        String query = "";
        String autoSalquery = "";
        String empCode = empobj.getEmployeeCode();
        String empname = empobj.getEmployeeName();
        String empCodeM = empobj.getEmployeeCodeM();
        String location = empobj.getLocation();
        String department = empobj.getDepartment();
        String designation = empobj.getDesignation();
        String posTingCity = empobj.getNatureType();
        String arrearType=empobj.getArrearType();
        String dynamicCondition = " and emp.ddo=\"" + empobj.getDdo() + "\"";
        //System.out.println("----befor cond");
        if (empCode != null && empCode != "") {
            dynamicCondition = dynamicCondition + " and emp.employeeCode=\"" + empobj.getEmployeeCode() + "\"";
        }
//         if (arrearType != null && arrearType != "") {
//            dynamicCondition = dynamicCondition + " and emp.arrearType=\"" + arrearType+ "\"";
//        }
        //System.out.println("----after cond");
        if (!empobj.getEmployeeName().isEmpty()) {
            dynamicCondition = dynamicCondition + " and emp.employeeName=\"" + empobj.getEmployeeName() + "\"";
        }
        if (!empobj.getEmployeeCodeM().isEmpty()) {
            dynamicCondition = dynamicCondition + " and emp.employeeCodeM=\"" + empobj.getEmployeeCodeM() + "\"";
        }
        if (!empobj.getLocation().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.location=\"" + empobj.getLocation() + "\"";
        }
        if (!empobj.getDepartment().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.department=\"" + empobj.getDepartment() + "\"";
        }
        if (!empobj.getDesignation().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.designation=\"" + empobj.getDesignation() + "\"";
        }
        if (!empobj.getNatureType().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
        }
        if (!empobj.getPostingCity().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
        }
        String Active = "\"Active\"";
        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + empTable + ""
                    + " as emp where emp.employeeCode not in " + processedEmpCodes + "" + dynamicCondition;
        } else {
            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo,emp.basic from " + empTable + ""
                    + " as emp where status=" + Active + dynamicCondition;
        }
        String ViewattendanceLockedQuery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + ArrearProcessTable + ""
                + " as emp where arrearStatus=\"Processed\" and  emp.lockStatus=true " + dynamicCondition;
        // getting employees that are not processed
        //System.out.println(query);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, query);
        if (dbOutput != null) {
            empList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            for (Employee as1 : empList) {
                autoSalryEmpCodes = autoSalryEmpCodes + "\"" + as1.getEmployeeCode() + "\",";
            }
        }
        if (autoSalryEmpCodes != null && !autoSalryEmpCodes.isEmpty()) {
            autoSalryEmpCodes = "(" + autoSalryEmpCodes.substring(0, autoSalryEmpCodes.length() - 1) + ")";
        }
        String salaryProcessType = "\"salary\"";
        if (autoSalryEmpCodes != null && !autoSalryEmpCodes.isEmpty()) {
            autoSalquery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + autoSalTable + ""
                    //  + " as emp where  month between  " + month1 + " and " + month2;
                    + " as emp where emp.employeeCode IN " + autoSalryEmpCodes + dynamicCondition + " and (" + cond + ") and isLocked=false and salaryProcessType=" + salaryProcessType;
        }
//        else
//        {
//            autoSalquery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + autoSalTable + ""
//                  //  + " as emp where  month between  " + month1 + " and " + month2;
//                   + " as emp where isLocked=true and ("+cond+")" ; 
//        }
        Map<String, AutoSalaryProcess> map1 = new HashMap<String, AutoSalaryProcess>();
        Map<String, ArrearProcess> arrearMap1 = new HashMap<String, ArrearProcess>();
        if (autoSalquery != "") {
            String dbOutput1 = aql.getRestData(ApplicationConstants.END_POINT, autoSalquery);
            if (dbOutput1 != null && dbOutput1.length() > 0) {
                autoSalempList = new Gson().fromJson(dbOutput1, new TypeToken< ArrayList<AutoSalaryProcess>>() {
                }.getType());
                for (AutoSalaryProcess autoSalaryProcess : autoSalempList) {
                    map1.put(autoSalaryProcess.getEmployeeCode(), autoSalaryProcess);
                }
            }
            autoSalempList.clear();
            autoSalempList.addAll(map1.values());
        }
        String ViewArrearLockedOutput = aql.getRestData(ApplicationConstants.END_POINT, ViewattendanceLockedQuery);
        List<ArrearProcess> lockedListData = null;

        if (ViewArrearLockedOutput != null && ViewArrearLockedOutput.length() > 0) {
            lockedListData = new Gson().fromJson(ViewArrearLockedOutput, new TypeToken< ArrayList<ArrearProcess>>() {
            }.getType());
        }
        if (processedList != null) {
            for (ArrearProcess processedGenVal : processedList) {
                arrearMap1.put(processedGenVal.getEmployeeCode(), processedGenVal);
            }
            processedList.clear();
            processedList.addAll(arrearMap1.values());
        }
        try {
            processedList = getLocationForArrearProcess(processedList);
            processedList = getSalaryTypeforArrearProces(processedList);
            processedList = getDDOforArrearProcess(processedList);
            processedList = getDepartmentforArrear(processedList);
            processedList = getDesignationforArrearprocess(processedList);
            processedList = getBudgetHeadName(processedList);
            processedList = getFundTypeArrearprocess(processedList);
        } catch (Exception e) {
        }
        try {
            autoSalempList = getLocationForAutoSalry(autoSalempList);
            autoSalempList = getDDOforAutoSalry(autoSalempList);
            autoSalempList = getSalaryTypeForAutoSalry(autoSalempList);
            autoSalempList = getDepartmentforAutoSalry(autoSalempList);
            autoSalempList = getDesignationForSalry(autoSalempList);
            autoSalempList = getBudgetHeadNameForAutoSal(autoSalempList);
            autoSalempList = getFundTypeForAutoSal(autoSalempList);

        } catch (Exception e) {
        }
        try {
            lockedListData = getDDOforArrearProcess(lockedListData);
            lockedListData = getLocationForArrearProcess(lockedListData);
            lockedListData = getSalaryTypeforArrearProces(lockedListData);
            lockedListData = getDepartmentforArrear(lockedListData);
            lockedListData = getDesignationforArrearprocess(lockedListData);
            lockedListData = getBudgetHeadName(lockedListData);
            lockedListData = getFundTypeArrearprocess(lockedListData);
        } catch (Exception e) {
        }
        try {
        } catch (Exception e) {
        }
        outputMap.put("notprocessed", autoSalempList);
        outputMap.put("processed", processedList);
        outputMap.put("lockedprocess", lockedListData);
        return outputMap;
    }

    public static List<ArrearProcess> getBudgetHeadName(List<ArrearProcess> employeeList) throws Exception {
        Map<String, String> budgetHeadMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            budgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : budgetHeadMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getBudgetHead())) {
                    employeeList.get(i).setBudgetHeadName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<AutoSalaryProcess> getBudgetHeadNameForAutoSal(List<AutoSalaryProcess> employeeList) throws Exception {
        Map<String, String> budgetHeadMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            budgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : budgetHeadMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getBudgetHead())) {
                    employeeList.get(i).setBudgetHeadName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ArrearProcess> getFundTypeArrearprocess(List<ArrearProcess> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getFundType())) {
                    employeeList.get(i).setFundTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<AutoSalaryProcess> getFundTypeForAutoSal(List<AutoSalaryProcess> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getFundType())) {
                    employeeList.get(i).setFundTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private static List<AutoSalaryProcess> getLocationForAutoSalry(List<AutoSalaryProcess> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getLocation())) {
                    employeeList.get(i).setLocationName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ArrearProcess> getLocationForArrearProcess(List<ArrearProcess> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getLocation())) {
                    employeeList.get(i).setLocationName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private static List<AutoSalaryProcess> getDDOforAutoSalry(List<AutoSalaryProcess> employeeList) throws Exception {
        //System.out.println(employeeList.size());
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDdo())) {
                    employeeList.get(i).setDdoName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ArrearProcess> getDDOforArrearProcess(List<ArrearProcess> employeeList) throws Exception {
        //System.out.println(employeeList.size());
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDdo())) {
                    employeeList.get(i).setDdoName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<AutoSalaryProcess> getDepartmentforAutoSalry(List<AutoSalaryProcess> employeeList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DEPARTMENT_TABLE);
        List<Department> religionList = new Gson().fromJson(result, new TypeToken<List<Department>>() {
        }.getType());
        for (Iterator<Department> iterator = religionList.iterator(); iterator.hasNext();) {
            Department next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDepartment());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDepartment())) {
                    employeeList.get(i).setDepartmentName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ArrearProcess> getDepartmentforArrear(List<ArrearProcess> employeeList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DEPARTMENT_TABLE);
        List<Department> religionList = new Gson().fromJson(result, new TypeToken<List<Department>>() {
        }.getType());
        for (Iterator<Department> iterator = religionList.iterator(); iterator.hasNext();) {
            Department next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDepartment());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDepartment())) {
                    employeeList.get(i).setDepartmentName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<AutoSalaryProcess> getSalaryTypeForAutoSalry(List<AutoSalaryProcess> employeeList) throws Exception {
        Map<String, String> SalaryTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = religionList.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            SalaryTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : SalaryTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getSalaryType())) {
                    employeeList.get(i).setSalaryTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ArrearProcess> getSalaryTypeforArrearProces(List<ArrearProcess> employeeList) throws Exception {
        Map<String, String> SalaryTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = religionList.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            SalaryTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : SalaryTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getSalaryType())) {
                    employeeList.get(i).setSalaryTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<AutoSalaryProcess> getDesignationForSalry(List<AutoSalaryProcess> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        List<Designation> religionList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        for (Iterator<Designation> iterator = religionList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDesignation())) {
                    employeeList.get(i).setDesignationName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ArrearProcess> getDesignationforArrearprocess(List<ArrearProcess> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        List<Designation> religionList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        for (Iterator<Designation> iterator = religionList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDesignation())) {
                    employeeList.get(i).setDesignationName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String saveProcessArrearData(ArrearProcess arrearprocess, String month, String year, String arrearType, String fromDate, String toDate, String financialYear) throws Exception {
        try {
            String ddo = arrearprocess.getDdo();
            String empcode = arrearprocess.getEmployeeCode();
            Date demoDate = new Date();
            Date demoDate1 = new Date();
            String[] d = fromDate.split("/");
            String[] d1 = toDate.split("/");
            demoDate.setDate(Integer.valueOf(d[0]));
            demoDate.setMonth((Integer.valueOf(d[1]) - 1));
            demoDate.setYear(Integer.valueOf(d[2]));
            demoDate1.setDate(Integer.valueOf(d1[0]));
            demoDate1.setMonth((Integer.valueOf(d1[1]) - 1));
            demoDate1.setYear(Integer.valueOf(d1[2]));
           System.out.println("--demoDate-------" + demoDate);
           System.out.println("--demoDate1-------" + demoDate1);
            int noOfDays = daysBetween(demoDate, demoDate1);
            Double noOfDayStr = Double.valueOf(String.valueOf(noOfDays)) + 1.0;
            System.out.println("--noOfDays-------" + noOfDays);
            int mon1 = Integer.parseInt(d[1]);
            int mon2 = Integer.parseInt(d1[1]);
            int year1 = Integer.parseInt(d[2]);
            int year2 = Integer.parseInt(d1[2]);
            String fromDate1 = mon1 + "-" + year1;
            String toDate1 = mon2 + "-" + year2;
            String[] FinYearArr = financialYear.split("~");
            String financialYearJson = new ChangeFinancialYearManager().fetchFinancialYear(FinYearArr[0], FinYearArr[1]);
            List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear fyObj = fyList.get(0);
            String fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
            //System.out.println("----------------fyId-------" + fyId);
            List<String> dates = getdatesbetweeninterval(fromDate1, toDate1);
            Map<String, String> map2 = new HashMap<String, String>();
            for (String date : dates) {
                String[] splitString = date.split("-");
                map2.put(splitString[0], splitString[1]);
            }
            String totalDays = "";
            if ((mon1) % 2 == 0) {
                totalDays = "30";
            } else {
                totalDays = "31";
            }
            int iDay = 1;
            //Create a calendar object and set year and month
            Calendar mycal = new GregorianCalendar(year1, mon1 - 1, iDay);
            // Get the number of days in that month
            int daysInMonth = 0;
            daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
            if (daysInMonth != 0) {
                totalDays = String.valueOf(daysInMonth);
            }
            System.out.println("------daysInMonth-------" + daysInMonth);
            List<String> confiHeads = new ArrayList<String>();
            HashMap<String, Double> EmpHeadmap = new HashMap<String, Double>();
            HashMap<String, Double> finalMap = new HashMap<String, Double>();
            HashMap<String, Double> dedFinalMap = new HashMap<String, Double>();
            HashMap<String, String> condMap = new HashMap<String, String>();
            condMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            condMap.put("ddo", ddo);
            condMap.put("status", ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_CONFIG_TABLE, condMap);
            if (result != null) {
                try {
                    List<ArrearConfig> lists = new Gson().fromJson(result, new TypeToken<List<ArrearConfig>>() {
                    }.getType());
                    for (ArrearConfig arrearConfig : lists) {
                        String head = arrearConfig.getHead();
                        confiHeads.add(head);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            List<EarningHeadsDetails> headLists = null;
            List<EarningHeadsDetails> dedheadLists = null;
            Employee emp = null;
             int joiningmonth=0;
             int joiningDay=0;
            HashMap<String, String> empcondMap = new HashMap<String, String>();
            empcondMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            empcondMap.put("ddo", ddo);
            empcondMap.put("employeeCode", empcode);
            empcondMap.put("status", ApplicationConstants.ACTIVE);
            String empResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, empcondMap);
            if (empResult != null) {
                List<Employee> emplists = new Gson().fromJson(empResult, new TypeToken<List<Employee>>() {
                }.getType());
                emp = emplists.get(0);
                headLists = emp.getEarningHeads();
                dedheadLists = emp.getDeductionHeads();
                String dateOfJoining=emp.getDateOfJoining();
                 String[] joiningMonthArr = dateOfJoining.split("/");
                  joiningmonth=Integer.parseInt(joiningMonthArr[1]);
                  joiningDay=Integer.parseInt(joiningMonthArr[0]);
//                arrearprocess.setPrevEarningHeads(headLists);
//                arrearprocess.setPrevDeductionHeads(dedheadLists);
                for (EarningHeadsDetails earningHead : headLists) {
                    for (String arrearConHed : confiHeads) {
                        String empHead = earningHead.getDescription();
                        if (arrearConHed.equals(empHead)) {
                            EmpHeadmap.put(empHead, earningHead.getAmount());
                        }
                    }
                }
                for (EarningHeadsDetails dedHead : dedheadLists) {
                    for (String arrearConHed : confiHeads) {
                        String empHead = dedHead.getDescription();
                        if (arrearConHed.equals(empHead)) {
                            EmpHeadmap.put(empHead, dedHead.getAmount());
                        }
                    }
                }

            }
            if (arrearType.equals("Month")) {
                for (Map.Entry<String, String> entry1 : map2.entrySet()) {
                    List<EarningHeadsDetails> salheadList = null;
                    List<EarningHeadsDetails> dedSalheadList = null;
                    double totarrearAmount = 0.0;
                    HashMap salCondMap = new HashMap();
                    salCondMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    salCondMap.put("ddo", ddo);
                    salCondMap.put("employeeCode", empcode);
                    salCondMap.put("status", "Processed");
                    salCondMap.put("month", Integer.parseInt(entry1.getKey()));
                    salCondMap.put("year", Integer.parseInt(entry1.getValue()));
                    salCondMap.put("salaryProcessType", "salary");
                    String salResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, salCondMap);
                    List<AutoSalaryProcess> sallists = new Gson().fromJson(salResult, new TypeToken<List<AutoSalaryProcess>>() {
                    }.getType());
                    if (sallists != null) {
                       
                        AutoSalaryProcess salEmp = sallists.get(0);
                        arrearprocess.setIdStr((String) ((LinkedTreeMap) salEmp.getId()).get("$oid"));
                        Earnings salheadLists = salEmp.getEarningsInfo();
                        Deductions dedSalheadLists = salEmp.getDeductionsInfo();
                        boolean isEarningHeads = salheadLists.isIsEarningHeads();
                        boolean isDeductionHeads = dedSalheadLists.isIsDeductionHeads();
                        double netEarnings = 0.0;
                        double netDed = 0.0;
                        if (!EmpHeadmap.isEmpty()) {
                            if ((isEarningHeads) || (isDeductionHeads)) {

                                for (Map.Entry<String, Double> entry : EmpHeadmap.entrySet()) {
                                    String empHead = entry.getKey();
                                    Double empHeadValue = entry.getValue();
                                    //System.out.println("-------empHead_Id-------" + empHead);
                                    //System.out.println("-------empHead_value-------" + empHeadValue);
                                    double diffarrearAmount = 0.0;
                                    if (isEarningHeads) {
                                        salheadList = salheadLists.getEarningHeads();
                                        if (!salheadList.isEmpty()) {
                                            for (EarningHeadsDetails salearningHead : salheadList) {
                                                String salEmpHead1 = salearningHead.getSalaryHeadId();
                                                SalaryHead descrptionInfo = salearningHead.getDescriptionInfo();
                                                //System.out.println("--------before-----------------");
                                                String salEmpHead = (String) ((LinkedTreeMap) descrptionInfo.getId()).get("$oid");
                                                //System.out.println("--------after-----------------");
                                                //System.out.println("-------salHead_Id-------" + salEmpHead1);
                                                if (salEmpHead.equals(empHead)) {
                                                    double salHeadVal = salearningHead.getAmount();
                                                    //System.out.println("-------salHead_value-------" + salHeadVal);
                                                      if(joiningmonth==Integer.parseInt(entry1.getKey()))
                                                        {
                                                            String totalDays1=String.valueOf((Integer.parseInt(totalDays)+1)-joiningDay);
//                                                             double diffarrear= empHeadValue - salHeadVal;
//                                                               double empHeadValueForOneDay = diffarrear / Double.parseDouble(totalDays);
//                                                                diffarrearAmount = empHeadValueForOneDay * Double.parseDouble(totalDays);
                                                             diffarrearAmount= ((empHeadValue/Double.parseDouble(totalDays)*Double.parseDouble(totalDays1))) - salHeadVal;
                                                        }
                                                      else
                                                      {
                                                    diffarrearAmount = empHeadValue - salHeadVal;
                                                      }
                                                    diffarrearAmount = round(descrptionInfo, diffarrearAmount);
                                                    salearningHead.setAmount(diffarrearAmount);

                                                }
                                            }
                                            finalMap.put(empHead, diffarrearAmount);
                                        }
                                    }

                                    if (isDeductionHeads) {
                                        dedSalheadList = dedSalheadLists.getDeductionHeads();
                                        if (!dedSalheadList.isEmpty()) {
                                            for (EarningHeadsDetails salDeductionHead : dedSalheadList) {
                                                String salEmpHead1 = salDeductionHead.getSalaryHeadId();
                                                SalaryHead descrptionInfo = salDeductionHead.getDescriptionInfo();
                                                //System.out.println("--------before-----------------");
                                                String salEmpDedHead = (String) ((LinkedTreeMap) descrptionInfo.getId()).get("$oid");
                                                //System.out.println("--------after-----------------");
                                                //System.out.println("-------salHead_Id-------" + salEmpHead1);
                                                if (salEmpDedHead.equals(empHead)) {
                                                    double salHeadVal = salDeductionHead.getAmount();
                                                    //System.out.println("-------salHead_value-------" + salHeadVal);
                                                     if(joiningmonth==Integer.parseInt(entry1.getKey()))
                                                        {
//                                                            totalDays=String.valueOf((Integer.parseInt(totalDays)-joiningDay)+1);
//                                                             double diffarrear= empHeadValue - salHeadVal;
//                                                               double empHeadValueForOneDay = diffarrear / Double.parseDouble(totalDays);
//                                                                diffarrearAmount = empHeadValueForOneDay * Double.parseDouble(totalDays);
                                                            String totalDays1=String.valueOf((Integer.parseInt(totalDays)+1)-joiningDay);
                                                             diffarrearAmount= ((empHeadValue/Double.parseDouble(totalDays)*Double.parseDouble(totalDays1))) - salHeadVal;
                                                        }
                                                     else
                                                     {
                                                    diffarrearAmount = empHeadValue - salHeadVal;
                                                     }
                                                    diffarrearAmount = round(descrptionInfo, diffarrearAmount);
                                                    salDeductionHead.setAmount(diffarrearAmount);

                                                }
                                            }
                                            dedFinalMap.put(empHead, diffarrearAmount);
                                        }
                                    }
                                }
                                if (!headLists.isEmpty()) {
                                    for (EarningHeadsDetails earningHead : headLists) {
                                        String headName = earningHead.getDescription();
                                        if (finalMap.containsKey(headName)) {
                                            Double headVal = finalMap.get(headName);

                                            earningHead.setAmount(headVal);
                                        } else {
                                            earningHead.setAmount(0.0);
                                        }
                                    }

                                    for (EarningHeadsDetails earningHead : headLists) {
                                        String headName = earningHead.getDescription();
                                        Double headVal = earningHead.getAmount();
                                        netEarnings = netEarnings + headVal;
                                    }
                                    //System.out.println("-------headLists-------" + headLists);
                                    arrearprocess.setEarningHeads(headLists);
                                    arrearprocess.setTotalEarnings(netEarnings);
                                }

                                if (!dedheadLists.isEmpty()) {
                                    for (EarningHeadsDetails dedHead : dedheadLists) {
                                        String headName = dedHead.getDescription();
                                        if (dedFinalMap.containsKey(headName)) {
                                            Double headVal = dedFinalMap.get(headName);
                                            dedHead.setAmount(headVal);
                                        } else {
                                            dedHead.setAmount(0.0);
                                        }
                                    }
                                    for (EarningHeadsDetails dedHead : dedheadLists) {
                                        String headName = dedHead.getDescription();
                                        Double headVal = dedHead.getAmount();
                                        netDed = netDed + headVal;
                                    }
                                    arrearprocess.setDeductionHeads(dedheadLists);
                                    arrearprocess.setTotalDeductions(netDed);
                                }
                                arrearprocess.setFromDate(fromDate);
                                arrearprocess.setToDate(toDate);
                                arrearprocess.setFromDateInMilliSecond(getInMilliSecond(fromDate));
                                arrearprocess.setToDateInMilliSecond(getInMilliSecond(toDate));
                                arrearprocess.setNetPay(netEarnings - netDed);
                                String finYear = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
//                                String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
//                                List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
//                                }.getType());
//                                FinancialYear fyObj = fyList.get(0);
//                                String fyId =  (String) ((LinkedTreeMap) fyObj.getId()).get("$oid");
                                arrearprocess.setCreatedBy("");
                                arrearprocess.setFinacialYear(fyId);
                                arrearprocess.setArrearType(arrearType);
                                arrearprocess.setMonth(Integer.parseInt(entry1.getKey()));
                                arrearprocess.setYear(Integer.parseInt(entry1.getValue()));
                                arrearprocess.setEmployeeCodeM(emp.getEmployeeCodeM());
                                arrearprocess.setEmployeeName(emp.getEmployeeName());
                                arrearprocess.setLocation(emp.getLocation());
                                arrearprocess.setDepartment(emp.getDepartment());
                                arrearprocess.setDesignation(emp.getDesignation());
                                arrearprocess.setSalaryType(emp.getSalaryType());
                                arrearprocess.setNatureType(emp.getNatureType());
                                arrearprocess.setPostingCity(emp.getPostingCity());
                                arrearprocess.setPfType(emp.getPfType());
                                arrearprocess.setFundType(emp.getFundType());
                                arrearprocess.setBudgetHead(emp.getBudgetHead());
                                Long cirDateInMillis = System.currentTimeMillis();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(cirDateInMillis);
                                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                arrearprocess.setOpenBalance(0);
                                arrearprocess.setArrearDate(formatter.format(calendar.getTime()));
                                arrearprocess.setCreateDate(System.currentTimeMillis() + "");
                                arrearprocess.setCreateDate(System.currentTimeMillis() + "");
                                arrearprocess.setPayMonth(Integer.parseInt(month));
                                arrearprocess.setPayYear(Integer.parseInt(year));
                                arrearprocess.setArrearStatus("Processed");
                                arrearprocess.setLockStatus(Boolean.FALSE);
                                arrearprocess.setStatus(ApplicationConstants.ACTIVE);
                                String arrearProcessJson = new Gson().toJson(arrearprocess);
                                String ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_PROCESS_TABLE, arrearProcessJson);
//                                 //System.out.println("-------finalMap-------"+finalMap);
                            }
                        }
                    }
                }
            } else if (arrearType.equals("day")) {
                HashMap salCondMap = new HashMap();
                salCondMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                salCondMap.put("ddo", ddo);
                salCondMap.put("employeeCode", empcode);
                salCondMap.put("status", "Processed");
                salCondMap.put("month", mon1);
                salCondMap.put("year", year1);
                salCondMap.put("salaryProcessType", "salary");
                String salResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, salCondMap);
                List<AutoSalaryProcess> sallists = new Gson().fromJson(salResult, new TypeToken<List<AutoSalaryProcess>>() {
                }.getType());
                if (!EmpHeadmap.isEmpty()) {

                    if (sallists != null) {
                        
                        for (Map.Entry<String, Double> entry : EmpHeadmap.entrySet()) {
                            String empHead = entry.getKey();
                            Double empHeadValue = entry.getValue();
                            AutoSalaryProcess salEmp = sallists.get(0);
                            Earnings salheadLists = salEmp.getEarningsInfo();
                            boolean isEarningHeads = salheadLists.isIsEarningHeads();
                            Deductions dedSalheadLists = salEmp.getDeductionsInfo();
                            boolean isDeductionHeads = dedSalheadLists.isIsDeductionHeads();
                            if (isEarningHeads) {
                                List<EarningHeadsDetails> salheadList = salheadLists.getEarningHeads();
                                double totarrearAmount = 0.0;
                                double noOfDaysHeadValue = 0.0;
                                for (EarningHeadsDetails salearningHead : salheadList) {
                                    SalaryHead descrptionInfo = salearningHead.getDescriptionInfo();
                                    String salEmpHead = (String) ((LinkedTreeMap) descrptionInfo.getId()).get("$oid");
                                    if (salEmpHead.equals(empHead)) {
                                        double diffarrearAmount = 0.0;
                                        double salHeadVal = salearningHead.getAmount();
                                         if(joiningmonth==mon1)
                                    {
                                        String totalDaysInJoiningMonth=String.valueOf((Integer.parseInt(totalDays)+1)-joiningDay);
                                        empHeadValue=(empHeadValue/Double.parseDouble(totalDays))*noOfDayStr;
                                        salHeadVal=(salHeadVal/ Double.parseDouble(totalDaysInJoiningMonth))* noOfDayStr;
                                         diffarrearAmount= empHeadValue -salHeadVal;
                                    }
                                         else
                                         {
                                        empHeadValue=(empHeadValue/Double.parseDouble(totalDays))*noOfDayStr;
                                        salHeadVal=(salHeadVal/ Double.parseDouble(totalDays))* noOfDayStr;
                                         diffarrearAmount= empHeadValue -salHeadVal; 
                                         }
                                        noOfDaysHeadValue = round(descrptionInfo, diffarrearAmount);
                        
//                                        diffarrearAmount = empHeadValue - salHeadVal;
//                                        double empHeadValueForOneDay = diffarrearAmount / Double.parseDouble(totalDays);
//                                        noOfDaysHeadValue = empHeadValueForOneDay * noOfDayStr;
                                     
                                        finalMap.put(empHead, noOfDaysHeadValue);
                                    }
                                }
                            }
                            if (isDeductionHeads) {
                                List<EarningHeadsDetails> dedSalheadList = dedSalheadLists.getDeductionHeads();
                                double totarrearAmount = 0.0;
                                double noOfDaysHeadValue = 0.0;
                                for (EarningHeadsDetails salDeductionHead : dedSalheadList) {
                                    SalaryHead descrptionInfo = salDeductionHead.getDescriptionInfo();
                                    String salDedEmpHead = (String) ((LinkedTreeMap) descrptionInfo.getId()).get("$oid");
                                    if (salDedEmpHead.equals(empHead)) {
                                        double diffarrearAmount = 0.0;
                                        double salHeadVal = salDeductionHead.getAmount();
                                          String totalDays1=String.valueOf((Integer.parseInt(totalDays)+1)-joiningDay);
                                            if(joiningmonth==mon1)
                                        {
                                            String totalDaysInJoiningMonth=String.valueOf((Integer.parseInt(totalDays)+1)-joiningDay);
                                            empHeadValue=(empHeadValue/Double.parseDouble(totalDays))*noOfDayStr;
                                            salHeadVal=(salHeadVal/ Double.parseDouble(totalDaysInJoiningMonth))* noOfDayStr;
                                             diffarrearAmount= empHeadValue -salHeadVal;
                                        }
                                         else
                                         {
                                        empHeadValue=(empHeadValue/Double.parseDouble(totalDays))*noOfDayStr;
                                        salHeadVal=(salHeadVal/ Double.parseDouble(totalDays))* noOfDayStr;
                                         diffarrearAmount= empHeadValue -salHeadVal; 
                                         }
//                                         empHeadValue=(empHeadValue/Double.parseDouble(totalDays))*noOfDayStr;
//                                        salHeadVal=(salHeadVal/ Double.parseDouble(totalDays1))* noOfDayStr;
//                                        diffarrearAmount = empHeadValue - salHeadVal;
                                        //double empHeadValueForOneDay = diffarrearAmount / Double.parseDouble(totalDays);
                                      //  noOfDaysHeadValue = empHeadValueForOneDay * noOfDayStr;
                                        //System.out.println("-----noOfDaysHeadValue befor---" + noOfDaysHeadValue);
                                        noOfDaysHeadValue = round(descrptionInfo, diffarrearAmount);
                                        //System.out.println("-----noOfDaysHeadValue after---" + noOfDaysHeadValue);
                                        dedFinalMap.put(empHead, noOfDaysHeadValue);
                                    }
                                }
                            }
                        }
                        double netDed = 0.0;
                        double netEarnings = 0.0;
                        if (!headLists.isEmpty()) {
                            for (EarningHeadsDetails earningHead : headLists) {
                                String headName = earningHead.getDescription();
                                if (finalMap.containsKey(headName)) {
                                    Double headVal = finalMap.get(headName);
                                    earningHead.setAmount(headVal);
                                } else {
                                    earningHead.setAmount(0.0);
                                }
                            }
                            for (EarningHeadsDetails earningHead : headLists) {
                                String headName = earningHead.getDescription();
                                Double headVal = earningHead.getAmount();
                                netEarnings = netEarnings + headVal;
                            }
                        }
                        if (!dedheadLists.isEmpty()) {
                            for (EarningHeadsDetails dedHead : dedheadLists) {
                                String headName = dedHead.getDescription();
                                if (dedFinalMap.containsKey(headName)) {
                                    Double headVal = dedFinalMap.get(headName);
                                    dedHead.setAmount(headVal);
                                } else {
                                    dedHead.setAmount(0.0);
                                }
                            }
                            for (EarningHeadsDetails dedHead : dedheadLists) {
                                String headName = dedHead.getDescription();
                                Double headVal = dedHead.getAmount();
                                netDed = netDed + headVal;
                            }
                        }

                        if (!finalMap.isEmpty() || !dedFinalMap.isEmpty()) {
                            arrearprocess.setEarningHeads(headLists);
                            arrearprocess.setDeductionHeads(dedheadLists);
                            arrearprocess.setTotalEarnings(netEarnings);
                            arrearprocess.setTotalDeductions(netDed);
                            arrearprocess.setNetPay(netEarnings - netDed);
                            arrearprocess.setFromDate(fromDate);
                            arrearprocess.setToDate(toDate);
                            arrearprocess.setFromDateInMilliSecond(getInMilliSecond(fromDate));
                            arrearprocess.setToDateInMilliSecond(getInMilliSecond(toDate));
                            String finYear = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
//                        String financialYearJson = new ChangeFinancialYearManager().fetchFinancialCurrentYear();
//                        List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
//                        }.getType());
//                        FinancialYear fyObj = fyList.get(0);
//                        String fyId =(String) ((LinkedTreeMap) fyObj.getId()).get("$oid");
                            arrearprocess.setCreatedBy("");
                            arrearprocess.setFinacialYear(fyId);
                            arrearprocess.setArrearType(arrearType);
                            arrearprocess.setMonth(mon1);
                            arrearprocess.setYear(year1);
                            arrearprocess.setEmployeeCodeM(emp.getEmployeeCodeM());
                            arrearprocess.setEmployeeName(emp.getEmployeeName());
                            arrearprocess.setLocation(emp.getLocation());
                            arrearprocess.setDepartment(emp.getDepartment());
                            arrearprocess.setDesignation(emp.getDesignation());
                            arrearprocess.setSalaryType(emp.getSalaryType());
                            arrearprocess.setNatureType(emp.getNatureType());
                            arrearprocess.setPostingCity(emp.getPostingCity());
                            arrearprocess.setPfType(emp.getPfType());
                            arrearprocess.setFundType(emp.getFundType());
                            arrearprocess.setBudgetHead(emp.getBudgetHead());
                            Long cirDateInMillis = System.currentTimeMillis();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(cirDateInMillis);
                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            arrearprocess.setOpenBalance(0);
                            arrearprocess.setArrearDate(formatter.format(calendar.getTime()));
                            arrearprocess.setCreateDate(System.currentTimeMillis() + "");
                            arrearprocess.setCreateDate(System.currentTimeMillis() + "");
                            arrearprocess.setPayMonth(Integer.parseInt(month));
                            arrearprocess.setPayYear(Integer.parseInt(year));
                            arrearprocess.setArrearStatus("Processed");
                            arrearprocess.setLockStatus(Boolean.FALSE);
                            arrearprocess.setStatus(ApplicationConstants.ACTIVE);
                            String arrearProcessJson = new Gson().toJson(arrearprocess);
                            String ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_PROCESS_TABLE, arrearProcessJson);
                        }
                    }

                }
            }

            return "success";
//            }
        } catch (Exception e) {
            //System.out.println("---" + e.getMessage());
        }

        // Save data into Salary Increment Table
        return "";
    }

    public static void main(String[] args) {
        String str = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from `/usg_dbm/usg/autosalaryprocess` as emp where emp.employeeCode  in ('A','ww','987') and emp.ddo='579a0012694df0809f7e2e29' and month between  7 and 11";
        RestClient aql = new RestClient();
        //System.out.println();
        String dbOutput1 = aql.getRestData(ApplicationConstants.END_POINT, str);
        //System.out.println(dbOutput1);
    }

    public static boolean saveUnProcessArrearData(String processedIds, String userId, String fromDate, String toDate) throws Exception {

        JSONArray arr = new JSONArray(processedIds);
        String[] employeeCode = new String[arr.length()];
        User user = new UserManager().fetch(userId);
        boolean result = false;
        boolean result2 = false;
        String userName = user.getFname() + " " + user.getLname();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            employeeCode[i] = (String) obj.get("idStr");
        }
        if (employeeCode != null && employeeCode.length > 0) {
            for (String pid : employeeCode) {
                String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_PROCESS_TABLE, pid);
                List<ArrearProcess> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<ArrearProcess>>() {
                }.getType());
                ArrearProcess relation = relationlist.get(0);
                relation.setArrearStatus("Unprocessed");
                relation.setUpdatedBy(userName);
                relation.setUpdateDate(Long.toString(System.currentTimeMillis()));
                String empCode = relation.getEmployeeCode();
                String ddo = relation.getDdo();
//                 String fromDate=relation.getFromDate();
//                String toDate=relation.getToDate();
                Date demoDate = new Date();
                Date demoDate1 = new Date();
                String[] d = fromDate.split("/");
                String[] d1 = toDate.split("/");
                demoDate.setDate(Integer.valueOf(d[0]));
                demoDate.setMonth((Integer.valueOf(d[1]) - 1));
                demoDate.setYear(Integer.valueOf(d[2]));
                demoDate1.setDate(Integer.valueOf(d1[0]));
                demoDate1.setMonth((Integer.valueOf(d1[1]) - 1));
                demoDate1.setYear(Integer.valueOf(d1[2]));
                //System.out.println("--demoDate-------" + demoDate);
                //System.out.println("--demoDate1-------" + demoDate1);
                //second insert into unprocessed table
                String fromDate1 = d[1] + "-" + d[2];
                String toDate1 = d1[1] + "-" + d1[2];
                List<String> dates = getdatesbetweeninterval(fromDate1, toDate1);
                Map<String, String> map2 = new HashMap<String, String>();
                for (String date : dates) {
                    String[] splitString = date.split("-");
                    map2.put(splitString[0], splitString[1]);
                }

//                 String existrelationJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_UN_PROCESS_TABLE, pid);
//                List<ArrearUnProcess> relationlist1 = new Gson().fromJson(existrelationJson1, new TypeToken<List<ArrearUnProcess>>() { }.getType());
//                String primaryKey=null;
//                if(null ==relationlist1)
//                {
//                      primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_UN_PROCESS_TABLE, new Gson().toJson(relation));
//                //System.out.println(primaryKey);
//                }
//               
//                if ((primaryKey != null) ||(!relationlist1.isEmpty())) {
//                    //third delete from processed table
//                    result = DBManager.getDbConnection().deleteDocument(ApplicationConstants.ARREAR_PROCESS_TABLE, pid);
//                }
                for (Map.Entry<String, String> entry : map2.entrySet()) {
                    int month = Integer.parseInt(entry.getKey());
                    int year = Integer.parseInt(entry.getValue());
                    HashMap empcondMap = new HashMap();
                    empcondMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    empcondMap.put("ddo", ddo);
                    empcondMap.put("employeeCode", empCode);
                    empcondMap.put("month", month);
                    empcondMap.put("year", year);
                    empcondMap.put("status", ApplicationConstants.ACTIVE);
                    String processResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_PROCESS_TABLE, empcondMap);
                    List<ArrearProcess> lists = new Gson().fromJson(processResult, new TypeToken<List<ArrearProcess>>() {
                    }.getType());
                    if (lists != null) {
                        ArrearProcess lis1 = lists.get(0);
                        lis1.setArrearStatus("Unprocessed");
                        lis1.setUpdatedBy(userName);
                        lis1.setUpdateDate(Long.toString(System.currentTimeMillis()));
                        String iId = (String) ((LinkedTreeMap) lis1.getId()).get("$oid");
                        String existrelationJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_UN_PROCESS_TABLE, iId);
                        List<ArrearUnProcess> relationlist2 = new Gson().fromJson(existrelationJson2, new TypeToken<List<ArrearUnProcess>>() {
                        }.getType());
                        String primaryKey1 = null;
                        if (null == relationlist2) {
                            primaryKey1 = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_UN_PROCESS_TABLE, new Gson().toJson(lis1));
                            //System.out.println("----" + primaryKey1);
                        }

                        if ((primaryKey1 != null) || (!relationlist2.isEmpty())) {
                            //third delete from processed table
                            result2 = DBManager.getDbConnection().delete(ApplicationConstants.ARREAR_PROCESS_TABLE, iId);
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String getSearchResultFromArrearProcess(String employeeJson) throws Exception {
//        String employeeJson
        if (employeeJson == null) {
            return null;
        }
        RestClient aql = new RestClient();
        String ArrearProcessTable = ApplicationConstants.USG_DB1 + ApplicationConstants.ARREAR_PROCESS_TABLE + "`";
        ArrearProcess empobj = new Gson().fromJson(employeeJson, new TypeToken<ArrearProcess>() {
        }.getType());
        String query = "";
        String autoSalquery = "";
        int fromYear = 0;
        int toYear = 0;
        int fromMonth = 0;
        int toMonth = 0;
//        String arrearDate=empobj.getArrearDate();
//         String arrearDateMilli = saveInMilliSecond(arrearDate);
//         String nextDate=getNextDate(arrearDate);
//         String nextDateMilli = saveInMilliSecond(nextDate);
        String empCode = empobj.getEmployeeCode();
        String empname = empobj.getEmployeeName();
        String location = empobj.getLocation();
        String department = empobj.getDepartment();
        String designation = empobj.getDesignation();
        String posTingCity = empobj.getNatureType();
        int payYear = empobj.getFromYear();
        int payMonth = empobj.getFromMonth();
        String arrearType = empobj.getArrearType();
        String dynamicCondition = " and emp.ddo=\"" + empobj.getDdo() + "\"";
        if (empCode != null && empCode != "") {
            dynamicCondition = dynamicCondition + " and emp.employeeCode= \"" + empobj.getEmployeeCode() + "\"";
        }
        if (!empobj.getEmployeeName().equals("")) {
            dynamicCondition = dynamicCondition + " and emp.employeeName= \"" + empobj.getEmployeeName() + "\"";
        }
        String empCodeM = empobj.getEmployeeCodeM();
        if (!empCodeM.equals("")) {
            dynamicCondition = dynamicCondition + " and emp.employeeCodeM=\"" + empobj.getEmployeeCodeM() + "\"";
        }

        if (!empobj.getLocation().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.location=\"" + empobj.getLocation() + "\"";
        }
        if (!empobj.getDepartment().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.department= \"" + empobj.getDepartment() + "\"";
        }
        if (!empobj.getArrearType().equals(null)) {
            dynamicCondition = dynamicCondition + " and emp.arrearType= \"" + empobj.getArrearType() + "\"";
        }
//        if (!empobj.getNatureType().equals("0")) {
//            dynamicCondition = dynamicCondition + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
//        }
//        if (!empobj.getPostingCity().equals("0")) {
//            dynamicCondition = dynamicCondition + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
//        }
//        if (!empobj.getPfType().equals("0")) {
//            dynamicCondition = dynamicCondition + " and emp.pfType=\"" + empobj.getPfType() + "\"";
//        }
//        if (!empobj.getFundType().equals("0")) {
//            dynamicCondition = dynamicCondition + " and emp.fundType=\"" + empobj.getFundType() + "\"";
//        }
//        if (!empobj.getBudgetHead().equals("0")) {
//            dynamicCondition = dynamicCondition + " and emp.budgetHead=\"" + empobj.getBudgetHead() + "\"";
//        }
        if (!empobj.getDesignation().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.designation=\"" + empobj.getDesignation() + "\"";
        }
        if (!empobj.getNatureType().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
        }
        if (!empobj.getPostingCity().equals("0")) {
            dynamicCondition = dynamicCondition + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
        }
        if (payMonth != 0) {
            dynamicCondition = dynamicCondition + " and payMonth= " + payMonth;
        }
        if (payYear != 0) {
            dynamicCondition = dynamicCondition + " and payYear= " + payYear;
        }
//         if(payYear !=0 )
//         {
//           dynamicCondition = dynamicCondition + " and payYear= " +payYear;  
//         }
        String Active = "\"Active\"";
        if (dynamicCondition != null && !dynamicCondition.isEmpty()) {
            query = "select emp._id as idStr,emp.employeeCode,emp.arrearType,emp.employeeName,emp.earningHeads,emp.month,emp.year,emp.deductionHeads,emp.totalEarnings,emp.totalDeductions,emp.netPay,emp.finacialYear,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + ArrearProcessTable + ""
                    + " as emp where arrearStatus=\"Processed\" and  emp.lockStatus=false " + dynamicCondition;
        } else {
            query = "select emp._id as idStr,emp.employeeCode,emp.arrearType,emp.month,emp.year,emp.earningHeads,emp.deductionHeads,emp.totalEarnings,emp.totalDeductions,emp.netPay,emp.finacialYear,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + ArrearProcessTable + ""
                    + " as emp where arrearStatus=\"Processed\" and  emp.lockStatus=false ";
        }
        // getting employees that are not processed
        //System.out.println("----" + query);
//        query="select * from "+ ArrearProcessTable ;
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, query);
        List<ArrearProcess> empList = null;
        List<ArrearProcess> empList1 = new ArrayList<ArrearProcess>();

        if (dbOutput != null && !dbOutput.isEmpty()) {
            empList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<ArrearProcess>>() {
            }.getType());
            if (empList.size() != 0) {

            }
            Map<String, ArrearProcess> map1 = new HashMap<String, ArrearProcess>();
            for (ArrearProcess arrearProcess1 : empList) {
                map1.put(arrearProcess1.getEmployeeCode(), arrearProcess1);
            }
            empList.clear();
            empList.addAll(map1.values());

            List<String> confiHeads = new ArrayList<String>();
            HashMap<String, String> condMap = new HashMap<String, String>();
            condMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            condMap.put("ddo", empobj.getDdo());
            condMap.put("status", ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_CONFIG_TABLE, condMap);
            if (result != null) {
                try {
                    List<ArrearConfig> lists = new Gson().fromJson(result, new TypeToken<List<ArrearConfig>>() {
                    }.getType());
                    for (ArrearConfig arrearConfig : lists) {
                        String head = arrearConfig.getHead();
                        confiHeads.add(head);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            for (ArrearProcess arrearProcess : empList) {
                List<EarningHeadsDetails> salEarnList = new ArrayList<EarningHeadsDetails>();
                List<EarningHeadsDetails> salDeddheadList = new ArrayList<EarningHeadsDetails>();
                if (arrearProcess.getEarningHeads() != null) {
                    for (EarningHeadsDetails earningHead : arrearProcess.getEarningHeads()) {
                        String headName = earningHead.getDescription();
                        if (confiHeads.contains(headName)) {
                            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, headName);
                            List<SalaryHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<SalaryHead>>() {
                            }.getType());
                            SalaryHead gal = gaList.get(0);
                            earningHead.setHeadName(gal.getDescription());
                            salEarnList.add(earningHead);
                        }

                    }
                }

                if (arrearProcess.getDeductionHeads() != null) {
                    for (EarningHeadsDetails dedhead : arrearProcess.getDeductionHeads()) {
                        String headName = dedhead.getDescription();
                        if (confiHeads.contains(headName)) {
                            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, headName);
                            List<SalaryHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<SalaryHead>>() {
                            }.getType());
                            SalaryHead gal = gaList.get(0);
                            dedhead.setHeadName(gal.getDescription());
                            salDeddheadList.add(dedhead);
                        }

                    }

                }
                arrearProcess.setEarningHeads(salEarnList);
                arrearProcess.setDeductionHeads(salDeddheadList);
                empList1.add(arrearProcess);
            }
            try {
                empList1 = getLocationForArrearProcess(empList1);
            } catch (Exception e) {
            }
            try {
                empList1 = getDDOforArrearProcess(empList1);
            } catch (Exception e) {
            }
            try {
                empList1 = getSalaryTypeforArrearProces(empList1);
            } catch (Exception e) {
            }

            try {
                empList1 = getDepartmentforArrear(empList1);
            } catch (Exception e) {
            }

            try {
                empList1 = getDesignationforArrearprocess(empList1);
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList1);
    }

    public static String saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return Long.toString(date.getTime());
    }

    public static String getNextDate(String curDate) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = format.parse(curDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return format.format(calendar.getTime());
    }

//    public static String saveProcessArrearData(ArrearProcess arrearprocess, String month, String year) throws Exception 
//    {
//        try {
//            String ddo = arrearprocess.getDdo();
//            String empcode = arrearprocess.getEmployeeCode();
////            HashMap<String, String> conditionMap = new HashMap<String, String>();
////            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
////            conditionMap.put("ddo", ddo);
////            conditionMap.put("empcode", empcode);
////            conditionMap.put("status", ApplicationConstants.ACTIVE);
////            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_PROCESS_TABLE, conditionMap);
////            if (result != null) {
////                List<ArrearProcess> list = new Gson().fromJson(result, new TypeToken<List<ArrearProcess>>() {
////                }.getType());
////                ArrearProcess arrear = list.get(0);
////                arrear.setUpdateDate(System.currentTimeMillis() + "");
////                arrearprocess.setArrearStatus(ApplicationConstants.TRUE);
////                String iId = ((LinkedTreeMap<String, String>) arrear.getId()).get("$oid");
////                String bankJson = new Gson().toJson(arrear);
////                boolean result1 = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, iId, bankJson);
////
////            } else {
//                arrearprocess.setCreatedBy("");
//                arrearprocess.setCreateDate(System.currentTimeMillis() + "");
//                 arrearprocess.setCreateDate(System.currentTimeMillis() + "");
//                arrearprocess.setMonth(Integer.parseInt(month));
//                arrearprocess.setYear(Integer.parseInt(year));
//                arrearprocess.setArrearStatus("Processed");
//                arrearprocess.setLockStatus(Boolean.FALSE);
//                arrearprocess.setStatus(ApplicationConstants.ACTIVE);
//
//                String arrearProcessJson = new Gson().toJson(arrearprocess);
//
//                String ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_PROCESS_TABLE, arrearProcessJson);
//                return ddoresult;
////            }
//        } catch (Exception e) {
//        }
//
//        // Save data into Salary Increment Table
//        return "";
//    }
    public static List<String> getdatesbetweeninterval(String fromDate1, String toDate1) throws Exception {
        DateFormat formater = new SimpleDateFormat("M-yyyy");
        List dates = new ArrayList();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(fromDate1));
            finishCalendar.setTime(formater.parse(toDate1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date1 = formater.format(finishCalendar.getTime()).toUpperCase();
        while (beginCalendar.before(finishCalendar)) {
            // add one month to date per loop
            String date = formater.format(beginCalendar.getTime()).toUpperCase();
            //System.out.println(date);
            dates.add(date);
            beginCalendar.add(Calendar.MONTH, 1);
        }
        dates.add(date1);
        return dates;
    }

    public static String saveArrearHeadsData(ArrearProcess earningHeads, String id) throws Exception {
//        List<EarningHeadsDetails> earnlist = new Gson().fromJson(earningHeads, new TypeToken<List<ArrearProcess>>() {}.getType());
//        List<EarningHeadsDetails> dedlist = new Gson().fromJson(deductionHeads, new TypeToken<List<ArrearProcess>>() {}.getType());
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_PROCESS_TABLE, id);
        List<ArrearProcess> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<ArrearProcess>>() {
        }.getType());
        ArrearProcess relation = relationlist.get(0);
        List<EarningHeadsDetails> headLists = null;
        List<EarningHeadsDetails> dedheadLists = null;
        headLists = relation.getEarningHeads();
        dedheadLists = relation.getDeductionHeads();

        List<EarningHeadsDetails> earnlist = earningHeads.getEarningHeads();
        List<EarningHeadsDetails> dedlist = earningHeads.getDeductionHeads();
        if (earnlist.size() != 0) {
            for (EarningHeadsDetails earningHead : headLists) {
                String headName = earningHead.getDescription();
                for (EarningHeadsDetails earningHead1 : earnlist) {
                    String headName1 = earningHead1.getDescription();
                    if (headName1.equals(headName)) {
                        Double headVal = earningHead1.getAmount();
                        earningHead.setAmount(headVal);
                    }
                }
            }
        }
        if (dedlist.size() != 0) {
            for (EarningHeadsDetails dedHead : dedheadLists) {
                String dedheadName = dedHead.getDescription();
                for (EarningHeadsDetails dedHead1 : dedlist) {
                    String dedheadName1 = dedHead1.getDescription();
                    if (dedheadName1.equals(dedheadName)) {
                        Double headVal = dedHead1.getAmount();
                        dedHead.setAmount(headVal);
                    }
                }
            }
        }
        relation.setEarningHeads(headLists);
        relation.setDeductionHeads(dedheadLists);
        relation.setDeductionHeads(dedheadLists);
        relation.setTotalEarnings(earningHeads.getTotalEarnings());
        relation.setTotalDeductions(earningHeads.getTotalDeductions());
        relation.setNetPay(earningHeads.getNetPay());
        boolean isAttendanceUpdated = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, id, new Gson().toJson(relation));
        return "success";
    }

    public static String getArraerProcssedMonAndYear(String ddo, String empCode) throws Exception {

        try {
            HashMap condMap = new HashMap();
            condMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            condMap.put("ddo", ddo);
            condMap.put("employeeCode", empCode);
            condMap.put("status", ApplicationConstants.ACTIVE);
            condMap.put("lockStatus", false);
            List<ArrearProcess> empList = null;
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_PROCESS_TABLE, condMap);
            if (result != null) {
                empList = new Gson().fromJson(result, new TypeToken<List<ArrearProcess>>() {
                }.getType());
                return new Gson().toJson(empList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getEmployeeHeadsFromArrear(String employeeJson) throws Exception {
//        String employeeJson
        if (employeeJson == null) {
            return null;
        }
        RestClient aql = new RestClient();
        String ArrearProcessTable = ApplicationConstants.USG_DB1 + ApplicationConstants.ARREAR_PROCESS_TABLE + "`";
        ArrearProcess empobj = new Gson().fromJson(employeeJson, new TypeToken<ArrearProcess>() {
        }.getType());
        String query = "";
        String autoSalquery = "";
        int month = empobj.getMonth();
        int year = empobj.getYear();
        String dynamicCondition = " and emp.ddo=\"" + empobj.getDdo() + "\"";
        if (!empobj.getEmployeeCode().isEmpty()) {
            dynamicCondition = dynamicCondition + " and emp.employeeCode= \"" + empobj.getEmployeeCode() + "\"";
        }
        if (month != 0) {
            dynamicCondition = dynamicCondition + " and month= " + month;
        }
        if (year != 0) {
            dynamicCondition = dynamicCondition + " and year= " + year;
        }
        if (dynamicCondition != null && !dynamicCondition.isEmpty()) {
            query = "select emp._id as idStr,emp.employeeCode,emp.arrearType,emp.employeeName,emp.earningHeads,emp.month,emp.year,emp.deductionHeads,emp.totalEarnings,emp.netPay,emp.totalDeductions,emp.finacialYear,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + ArrearProcessTable + ""
                    + " as emp where arrearStatus=\"Processed\" and status=\"Active\" and  emp.lockStatus=false " + dynamicCondition;
        }

        //System.out.println(query);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, query);
        List<ArrearProcess> empList = null;
        if (dbOutput != null && !dbOutput.isEmpty()) {
            empList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<ArrearProcess>>() {
            }.getType());
            if (empList.size() != 0) {
                Map<String, ArrearProcess> map1 = new HashMap<String, ArrearProcess>();
                for (ArrearProcess arrearProcess1 : empList) {
                    map1.put(arrearProcess1.getEmployeeCode(), arrearProcess1);
                }
                empList.clear();
                empList.addAll(map1.values());
                List<String> confiHeads = new ArrayList<String>();
                HashMap<String, String> condMap = new HashMap<String, String>();
                condMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                condMap.put("ddo", empobj.getDdo());
                condMap.put("status", ApplicationConstants.ACTIVE);
                String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_CONFIG_TABLE, condMap);
                if (result != null) {
                    try {
                        List<ArrearConfig> lists = new Gson().fromJson(result, new TypeToken<List<ArrearConfig>>() {
                        }.getType());
                        for (ArrearConfig arrearConfig : lists) {
                            String head = arrearConfig.getHead();
                            confiHeads.add(head);
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
                for (ArrearProcess arrearProcess : empList) {
                    List<EarningHeadsDetails> salEarnList = new ArrayList<EarningHeadsDetails>();
                    List<EarningHeadsDetails> salDeddheadList = new ArrayList<EarningHeadsDetails>();
                    if (arrearProcess.getEarningHeads() != null) {
                        for (EarningHeadsDetails earningHead : arrearProcess.getEarningHeads()) {
                            String headName = earningHead.getDescription();
                            if (confiHeads.contains(headName)) {
                                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, headName);
                                List<SalaryHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                SalaryHead gal = gaList.get(0);
                                earningHead.setHeadName(gal.getDescription());
                                salEarnList.add(earningHead);
                            }
                        }
                    }
                    if (arrearProcess.getDeductionHeads() != null) {
                        for (EarningHeadsDetails dedhead : arrearProcess.getDeductionHeads()) {
                            String headName = dedhead.getDescription();
                            if (confiHeads.contains(headName)) {
                                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, headName);
                                List<SalaryHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<SalaryHead>>() {
                                }.getType());
                                SalaryHead gal = gaList.get(0);
                                dedhead.setHeadName(gal.getDescription());
                                salDeddheadList.add(dedhead);
                            }
                        }
                    }
                    arrearProcess.setEarningHeads(salEarnList);
                    arrearProcess.setDeductionHeads(salDeddheadList);
                }
            }
            try {
                empList = getLocationForArrearProcess(empList);
            } catch (Exception e) {
            }
            try {
                empList = getDDOforArrearProcess(empList);
            } catch (Exception e) {
            }
            try {
                empList = getSalaryTypeforArrearProces(empList);
            } catch (Exception e) {
            }

            try {
                empList = getDepartmentforArrear(empList);
            } catch (Exception e) {
            }

            try {
                empList = getDesignationforArrearprocess(empList);
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList);
    }

    public static long getInMilliSecond(String str) throws ParseException {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateInString = str;
            Date date = sdf.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }
}
