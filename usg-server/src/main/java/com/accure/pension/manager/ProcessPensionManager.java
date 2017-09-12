/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.FinancialYear;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.ArrearProcess;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.pension.dto.PensionEmployee;
import com.accure.pension.dto.PensionUnProcess;
import com.accure.pension.dto.ProcessPension;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author accure
 */
public class ProcessPensionManager {
    public String save(ProcessPension pension ,String financialYear,String month,String year) throws Exception {
//        Type type = new TypeToken<ProcessPension>() {
//        }.getType();
//        ProcessPension departmentDTO = new Gson().fromJson(pension, type);
        pension.setCreateDate(System.currentTimeMillis() + "");
        pension.setUpdateDate(System.currentTimeMillis() + "");
        pension.setStatus(ApplicationConstants.ACTIVE);
        pension.setPensionStatus("Processed");
         pension.setMonth(month);
          pension.setYear(year);
          String[] FinYearArr = financialYear.split("~");
            String financialYearJson = new ChangeFinancialYearManager().fetchFinancialYear(FinYearArr[0], FinYearArr[1]);
            List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear fyObj = fyList.get(0);
            String fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
            pension.setFinYear(fyId);
        String pensionJson = new Gson().toJson(pension);
 // ----------------------       
//        List<BankReconcilation> bankReconcilationList = new Gson().fromJson(reconcilationReport, new TypeToken<List<BankReconcilation>>() {
//        }.getType());
//        List<BankReconcilation> returnList = new ArrayList<BankReconcilation>();
//        if (bankReconcilationList != null) {
//            for (BankReconcilation bankReconcilation : bankReconcilationList) {
//                if (bankReconcilation.getLedger() != null) {
//                    String parentledgerId = bankReconcilation.getLedger();
//                    if (allParentLedger.containsKey(parentledgerId) && allParentLedger.get(parentledgerId) != null) {
//                        bankReconcilation.setLedgerName(allParentLedger.get(parentledgerId));
//                    }
//                }
//                if (bankReconcilation.getLocation() != null) {
//                    String locationId = bankReconcilation.getLocation();
//                    if (allLocation.containsKey(locationId) && allLocation.get(locationId) != null) {
//                        bankReconcilation.setLocationName(allLocation.get(locationId));
//                    }
//                }
//                returnList.add(bankReconcilation);
//            }
//
//        }
//  ---------------------------
        String deptresult = DBManager.getDbConnection().insert(ApplicationConstants.PROCESS_PENSION_TABLE, pensionJson);
        return deptresult;

    }

    public String search(String department, String Month, String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("department", department);
        conditionMap.put("month",Month);
        conditionMap.put("year", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PROCESS_PENSION_TABLE, conditionMap);
        return result;
        
    }
    public HashMap getSearchResult(String processPensionJsonString,String ddo,String location,String sortBy) throws Exception {

        HashMap outputMap = new HashMap();
        if (processPensionJsonString == null) {
            return null;
        }
        RestClient aql = new RestClient();
        String penEmpTable = ApplicationConstants.USG_DB1 + ApplicationConstants.PENSION_EMPLOYEE_TABLE + "`";
        String ProcessPension = ApplicationConstants.USG_DB1 + ApplicationConstants.PROCESS_PENSION_TABLE + "`";
        //json from UI
        ProcessPension empobj = new Gson().fromJson(processPensionJsonString, new TypeToken<ProcessPension>() {
        }.getType());
       String empCode = "";
        String empname ="";
        String grade = "";
        String department = "";
        String designation ="";
        String posTingCity ="";
         empCode = empobj.getEmployeecode();
         empname = empobj.getEmployeeName();
         grade = empobj.getGrade();
         department = empobj.getDepartment();
         designation = empobj.getDesignation();
         posTingCity = empobj.getNatureType();
         String processedEmpCodes = "";
        List<ProcessPension> processedList = null;
        List<ProcessPension> lockedList = null;
        String cond = "";
        String dynamicCondition = " and emp.DDO=\"" + ddo + "\" and emp.location=\"" + location + "\"";
        if (!empCode.equals(null) && !empCode.equals("")) 
        {
            dynamicCondition = dynamicCondition + " and emp.employeeCode=\"" + empobj.getEmployeecode() + "\"";
        }
        //System.out.println("----after cond");
        if (!empname.equals(null) && !empname.equals("")) 
        {
            dynamicCondition = dynamicCondition + " and emp.employeeName=\"" + empobj.getEmployeeName() + "\"";
        }
//        }
        if (!department.equals(null) && !department.equals("0")) 
        {
            dynamicCondition = dynamicCondition + " and emp.department=\"" + empobj.getDepartment() + "\"";
        }
        if (!grade.equals(null) && !grade.equals("0")) 
        {
            dynamicCondition = dynamicCondition + " and emp.grade=\"" + empobj.getGrade() + "\"";
        }
        if (!designation.equals(null) && !designation.equals("0")) 
        {
            dynamicCondition = dynamicCondition + " and emp.designation=\"" + empobj.getDesignation() + "\"";
        }


        String ProcessPensionQuery = "select emp._id as idStr,emp.employeecode,emp.DDO,emp.pension,emp.familyPension,emp.employeeName,emp.status,emp.location,emp.department,emp.natureType,emp.designation,emp.fundType from " + ProcessPension + ""
                + " as emp where  pensionStatus=\"Processed\" and  emp.lockStatus=false and emp.year=\"" + empobj.getYear()+"\" and emp.month=\""+empobj.getMonth()+"\""+dynamicCondition;
//                + " as emp where  pensionStatus=\"Processed\" and  emp.lockStatus=false " + finYearCond + " and ddo=\"" + empobj.getDdo() + "\" and (fromDateInMilliSecond > " + fromDateInlong + " or fromDateInMilliSecond < " + toDateInlong + ") and (toDateInMilliSecond >" + fromDateInlong +" or toDateInMilliSecond <" + toDateInlong + ")";

        String lockedPensionQuery = "select emp._id as idStr,emp.employeecode,emp.DDO,emp.employeeName,emp.pension,emp.familyPension,emp.status,emp.location,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType from " + ProcessPension + ""
                + " as emp where pensionStatus=\"Processed\" and  emp.lockStatus=true"+ dynamicCondition;
//                + " as emp where pensionStatus=\"Processed\" and  emp.lockStatus=true " + finYearCond + " and ddo=\"" + empobj.getDdo() + "\" and (fromDateInMilliSecond < " + fromDateInlong + " or fromDateInMilliSecond > " + toDateInlong + ") and (toDateInMilliSecond <" + fromDateInlong +" or toDateInMilliSecond >" + toDateInlong + ")";

        String lockedPensionOutput = aql.getRestData(ApplicationConstants.END_POINT, lockedPensionQuery);
        if (lockedPensionOutput != null) 
        {
            lockedList = new Gson().fromJson(lockedPensionOutput, new TypeToken<ArrayList<ProcessPension>>(){}.getType());
            for (ProcessPension as : lockedList) 
            {
                processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeecode() + "\",";
            }
        }
        String ProcessPensionOutput = aql.getRestData(ApplicationConstants.END_POINT, ProcessPensionQuery);
        //System.out.println("---------ProcessPensionOutput---------" + ProcessPensionOutput);
        if (ProcessPensionOutput != null) {
            processedList = new Gson().fromJson(ProcessPensionOutput, new TypeToken<ArrayList<ProcessPension>>() {
            }.getType());
            for (ProcessPension as : processedList) {
                processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeecode() + "\",";
            }
        }
        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
            processedEmpCodes = "(" + processedEmpCodes.substring(0, processedEmpCodes.length() - 1) + ")";
        }
        //fetch employees from employee table
        List<PensionEmployee> empList = null;
        String query = "";
        String autoSalquery = "";
      

     
        String Active = "\"Active\"";
         String stopPension = "\"NO\"";
        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
            query = "select emp._id as idStr,emp.employeecode,emp.employeeName,emp.status,emp.location,emp.pension,emp.familyPension,emp.designation,emp.department,emp.DDO from " + penEmpTable + ""
                    + " as emp where status=" + Active +" and stopPension=" + stopPension +" and emp.employeecode NOT IN " + processedEmpCodes + "" + dynamicCondition;
        } else {
            query = "select emp._id as idStr,emp.employeecode,emp.employeeName,emp.status,emp.designation,emp.location,emp.pension,emp.familyPension,emp.department,emp.postingCity,emp.DDO from " + penEmpTable + ""
                    + " as emp where status=" + Active +" and stopPension=" + stopPension + dynamicCondition;
        }
        String ViewPensionLockedQuery = "select emp._id as idStr,emp.employeecode,emp.employeeName,emp.pension,emp.familyPension,emp.status,emp.location,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.DDO from " + ProcessPension + ""
                + " as emp where pensionStatus=\"Processed\" and  emp.lockStatus=true " + dynamicCondition;
        // getting employees that are not processed
        //System.out.println(query);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, query);
        if (dbOutput != null) {
            empList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<PensionEmployee>>() {}.getType());

        }
       
       Map<String, PensionEmployee> map1 = new HashMap<String, PensionEmployee>();
        Map<String, ProcessPension> arrearMap1 = new HashMap<String, ProcessPension>();
         Map<String, ProcessPension> arrearMap2 = new HashMap<String, ProcessPension>();
        String ViewPensionLockedOutput = aql.getRestData(ApplicationConstants.END_POINT, ViewPensionLockedQuery);
        List<ProcessPension> lockedListData = null;

        if (ViewPensionLockedOutput != null && ViewPensionLockedOutput.length() > 0) 
        {
            lockedListData = new Gson().fromJson(ViewPensionLockedOutput, new TypeToken< ArrayList<ProcessPension>>() { }.getType());
        }
        if (processedList != null) {
            for (ProcessPension processedGenVal : processedList)
            {
                arrearMap1.put(processedGenVal.getEmployeecode(), processedGenVal);
            }
            processedList.clear();
            processedList.addAll(arrearMap1.values());
        }
         if (empList != null)
         {
            for (PensionEmployee pensionEmploy : empList)
            {
                map1.put(pensionEmploy.getEmployeecode(), pensionEmploy);
            }
            empList.clear();
            empList.addAll(map1.values());
        }
         if (lockedListData != null) {
            for (ProcessPension lockedListDataVal : lockedListData)
            {
                arrearMap2.put(lockedListDataVal.getEmployeecode(), lockedListDataVal);
            }
            lockedListData.clear();
            lockedListData.addAll(arrearMap2.values());
        }
         
//        try {
             processedList = getLocationForProcessPension(processedList);
           // processedList = getSalaryTypeforProcessPension(processedList);
            processedList = getDDOforProcessPension(processedList);
            processedList = getDepartmentForProcessPension(processedList);
            processedList = getDesignationforProcessPension(processedList);
//            processedList = getBudgetHeadName(processedList);
//            processedList = getFundTypeArrearprocess(processedList);
//        } catch (Exception e) {
//        }
//        try {
            empList = getLocationForPensionEmploy(empList);
            empList = getDDOForPensionEmploy(empList);
            //empList = getSalaryTypeForPensionEmploy(empList);
            empList = getDepartmentForPensionEmploy(empList);
            empList = getDesignationForPensionEmployee(empList);
//            autoSalempList = getBudgetHeadNameForAutoSal(autoSalempList);
//            autoSalempList = getFundTypeForAutoSal(autoSalempList);
//
//        } catch (Exception e) {
//        }
//        try {
            lockedListData = getDDOforProcessPension(lockedListData);
            lockedListData = getLocationForProcessPension(lockedListData);
           // lockedListData = getSalaryTypeforProcessPension(lockedListData);
            lockedListData = getDepartmentForProcessPension(lockedListData);
            lockedListData = getDesignationforProcessPension(lockedListData);
//            lockedListData = getBudgetHeadName(lockedListData);
//            lockedListData = getFundTypeArrearprocess(lockedListData);
//        } catch (Exception e) {
//        }
        try 
        {
             if (sortBy.equals("Department")) 
             {
            Collections.sort(empList, new SortByEmployeeCode());
        } else if (sortBy.equals("EmployeeCode")) {
            Collections.sort(empList, new SortByEmployeeName());
        } else if (sortBy.equals("EmployeeName")) {
            Collections.sort(empList, new SortByDepartment());
        }
        } catch (Exception e) {
        }
        outputMap.put("notprocessed", empList);
        outputMap.put("processed", processedList);
        outputMap.put("lockedprocess", lockedListData);
        return outputMap;
    }
    
     private static List<ProcessPension> getLocationForProcessPension(List<ProcessPension> employeeList) throws Exception {
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

    public static List<PensionEmployee> getLocationForPensionEmploy(List<PensionEmployee> employeeList) throws Exception {
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

    private static List<ProcessPension> getDDOforProcessPension(List<ProcessPension> employeeList) throws Exception {
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
                if (entry.getKey().equals(employeeList.get(i).getDDO())) {
                    employeeList.get(i).setDDOName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployee> getDDOForPensionEmploy(List<PensionEmployee> employeeList) throws Exception {
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
                if (entry.getKey().equals(employeeList.get(i).getDDO())) {
                    employeeList.get(i).setDDOName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<ProcessPension> getDepartmentForProcessPension(List<ProcessPension> employeeList) throws Exception {
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

    public static List<PensionEmployee> getDepartmentForPensionEmploy(List<PensionEmployee> employeeList) throws Exception {
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
     private List<PensionEmployee> getSalaryTypeForPensionEmploy(List<PensionEmployee> employeeList) throws Exception {
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

    public static List<ProcessPension> getSalaryTypeforProcessPension(List<ProcessPension> employeeList) throws Exception {
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
    public static boolean PensionUnProcessEmployeeData(String processedIds, String userId) throws Exception {

        JSONArray arr = new JSONArray(processedIds);
        String[] ids = new String[arr.length()];
        User user = new UserManager().fetch(userId);
        boolean result = false;
        boolean result2 = false;
        String userName = user.getFname() + " " + user.getLname();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            ids[i] = (String) obj.get("idStr");
        }
        if (ids != null && ids.length > 0) 
        {
            for (String pid : ids) 
            {
                    String processResult = DBManager.getDbConnection().fetch(ApplicationConstants.PROCESS_PENSION_TABLE, pid);
                    List<ProcessPension> lists = new Gson().fromJson(processResult, new TypeToken<List<ProcessPension>>() {
                    }.getType());
                    if (lists != null) {
                        ProcessPension lis1 = lists.get(0);
                        lis1.setArrearStatus("Unprocessed");
                        lis1.setUpdatedBy(userName);
                        lis1.setUpdateDate(Long.toString(System.currentTimeMillis()));
                        String iId = (String) ((LinkedTreeMap) lis1.getId()).get("$oid");
                        String existrelationJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.UNPROCESS_PENSION_TABLE, iId);
                        List<PensionUnProcess> relationlist2 = new Gson().fromJson(existrelationJson2, new TypeToken<List<PensionUnProcess>>() {
                        }.getType());
                        String primaryKey1 = null;
                        if (null == relationlist2)
                        {
                            primaryKey1 = DBManager.getDbConnection().insert(ApplicationConstants.UNPROCESS_PENSION_TABLE, new Gson().toJson(lis1));
                            //System.out.println("----" + primaryKey1);
                        }

                        if ((primaryKey1 != null) || (!relationlist2.isEmpty())) 
                        {
                            //third delete from processed table
                            result2 = DBManager.getDbConnection().delete(ApplicationConstants.PROCESS_PENSION_TABLE, iId);
                        }
                    }
                }
        }
        return true;
    }
      public static List<PensionEmployee> getDesignationForPensionEmployee(List<PensionEmployee> employeeList) throws Exception {
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

    public static List<ProcessPension> getDesignationforProcessPension(List<ProcessPension> employeeList) throws Exception {
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
}
