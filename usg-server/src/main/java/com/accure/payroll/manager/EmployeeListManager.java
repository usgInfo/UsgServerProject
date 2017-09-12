/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.budget.dto.FundType;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.Grade;
import com.accure.hrms.dto.ListOfEmployees;
import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.PFType;
import com.accure.hrms.dto.Quarter;
import com.accure.hrms.dto.SalaryBillTypeOREmployeeCategory;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.dto.Salutation;
import static com.accure.hrms.manager.EmployeeManager.getBank;
import static com.accure.hrms.manager.EmployeeManager.getDepartment;
import static com.accure.hrms.manager.EmployeeManager.getDesignation;
import static com.accure.hrms.manager.EmployeeManager.getDiscipline;
import static com.accure.hrms.manager.EmployeeManager.getFundType;
import static com.accure.hrms.manager.EmployeeManager.getMaritalStatus;
import static com.accure.hrms.manager.EmployeeManager.getNature;
import static com.accure.hrms.manager.EmployeeManager.getPFBank;
import static com.accure.hrms.manager.EmployeeManager.getReligion;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author deepak2310
 */
public class EmployeeListManager {

    public Set<String> fetchAllDDO() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ddoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, conditionMap);
        List<DDO> doList = new Gson().fromJson(ddoJson, new TypeToken<List<DDO>>() {
        }.getType());

        Map<String, String> DdoMap = new HashMap<String, String>();

        for (Iterator<DDO> iterator = doList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        Set<String> ddoSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : DdoMap.entrySet()) {

            ddoSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return ddoSet;
    }

    public Set<String> fetchAllLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String locationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION, conditionMap);
        List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
        }.getType());

        Map<String, String> DdoMap = new HashMap<String, String>();

        for (Iterator<Location> iterator = locationList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        Set<String> locationSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : DdoMap.entrySet()) {

            locationSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return locationSet;
    }

    public Set<String> fetchAllDepartment() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String departmentJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DEPARTMENT_TABLE, conditionMap);
        List<Department> departmentList = new Gson().fromJson(departmentJson, new TypeToken<List<Department>>() {
        }.getType());
        Map<String, String> DdoMap = new HashMap<String, String>();

        for (Iterator<Department> iterator = departmentList.iterator(); iterator.hasNext();) {
            Department next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDepartment());
        }
        Set<String> departmentSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : DdoMap.entrySet()) {

            departmentSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return departmentSet;
    }

    public Set<String> fetchAllDesignation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String designationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_TABLE, conditionMap);
        List<Designation> designationList = new Gson().fromJson(designationJson, new TypeToken<List<Designation>>() {
        }.getType());
        Map<String, String> designationMap = new HashMap<String, String>();

        for (Iterator<Designation> iterator = designationList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            designationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        Set<String> designationSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : designationMap.entrySet()) {

            designationSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return designationSet;
    }

    public Set<String> fetchAllGrade() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String gradeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GRADE_TABLE, conditionMap);
        List<Grade> gradeList = new Gson().fromJson(gradeJson, new TypeToken<List<Grade>>() {
        }.getType());
        Map<String, String> gradeMap = new HashMap<String, String>();

        for (Iterator<Grade> iterator = gradeList.iterator(); iterator.hasNext();) {
            Grade next = iterator.next();
            gradeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getGradeName());
        }
        Set<String> gradeSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : gradeMap.entrySet()) {

            gradeSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return gradeSet;
    }

    public Set<String> fetchAllPostingCity() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String employeeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CITY_TABLE, conditionMap);
        List<CityMaster> postingCityList = new Gson().fromJson(employeeJson, new TypeToken<List<CityMaster>>() {
        }.getType());
        Map<String, String> postingCityMap = new HashMap<String, String>();

        for (Iterator<CityMaster> iterator = postingCityList.iterator(); iterator.hasNext();) {
            CityMaster next = iterator.next();
            postingCityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityName());
        }
        Set<String> postingCitySet = new HashSet<String>();
        for (Map.Entry<String, String> entry : postingCityMap.entrySet()) {
            postingCitySet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");
        }
        return postingCitySet;
    }

    public Set<String> fetchPfType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String employeeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PF_TYPE_MASTER, conditionMap);
        List<PFType> pfTypeList = new Gson().fromJson(employeeJson, new TypeToken<List<PFType>>() {
        }.getType());
        Map<String, String> pfTypeMap = new HashMap<String, String>();

        for (Iterator<PFType> iterator = pfTypeList.iterator(); iterator.hasNext();) {
            PFType next = iterator.next();
            pfTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getPFType());
        }
        Set<String> pfTypeSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : pfTypeMap.entrySet()) {

            pfTypeSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return pfTypeSet;
    }

    public Set<String> fetchAllFundType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fundTypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_TYPE_TABLE, conditionMap);
        List<FundType> fundTypeList = new Gson().fromJson(fundTypeJson, new TypeToken<List<FundType>>() {
        }.getType());
        Map<String, String> fundTypeMap = new HashMap<String, String>();

        for (Iterator<FundType> iterator = fundTypeList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            fundTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        Set<String> fundTypeSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : fundTypeMap.entrySet()) {

            fundTypeSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return fundTypeSet;
    }

    public Set<String> fetchAllBudgetHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetHeadJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> budgetHeadList = new Gson().fromJson(budgetHeadJson, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        Map<String, String> budgetHeadMap = new HashMap<String, String>();

        for (Iterator<BudgetHeadMaster> iterator = budgetHeadList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            budgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHeadDescription());
        }
        Set<String> budgetheadSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : budgetHeadMap.entrySet()) {

            budgetheadSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return budgetheadSet;
    }

    public Set<String> fetchAllNature() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetHeadJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NATURE_TABLE, conditionMap);
        List<Nature> natureList = new Gson().fromJson(budgetHeadJson, new TypeToken<List<Nature>>() {
        }.getType());
        Map<String, String> natureMap = new HashMap<String, String>();

        for (Iterator<Nature> iterator = natureList.iterator(); iterator.hasNext();) {
            Nature next = iterator.next();
            natureMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getNatureName());
        }
        Set<String> natureSet = new HashSet<String>();
        for (Map.Entry<String, String> entry : natureMap.entrySet()) {

            natureSet.add("<option value='" + entry.getKey() + "'>" + entry.getValue() + "</option>");

        }
        return natureSet;
    }

    public HashMap<String, String> fetchListOfEmployee() throws Exception {
        HashMap employeeListMap = new HashMap();
        employeeListMap.put(ApplicationConstants.DDO, fetchAllDDO());
        employeeListMap.put(ApplicationConstants.LOCATION, fetchAllLocation());
        employeeListMap.put(ApplicationConstants.DEPARTMENT, fetchAllDepartment());
        employeeListMap.put(ApplicationConstants.DESIGNATION, fetchAllDesignation());
        employeeListMap.put(ApplicationConstants.GRADE, fetchAllGrade());
        employeeListMap.put(ApplicationConstants.POSTING_CITY, fetchAllPostingCity());
        employeeListMap.put(ApplicationConstants.PF_TYPE, fetchPfType());
        employeeListMap.put(ApplicationConstants.FUND_TYPE, fetchAllFundType());
        employeeListMap.put(ApplicationConstants.BUDGET_HEAD, fetchAllBudgetHead());
        employeeListMap.put(ApplicationConstants.NATURE, fetchAllNature());
        return employeeListMap;
    }

    public String fetchAllBySearch(String employeeSearch) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        ListOfEmployees empobj = new Gson().fromJson(employeeSearch, new TypeToken<ListOfEmployees>() {
        }.getType());
        RestClient aql = new RestClient();
        String Active = "\"Active\"";

        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";

        String employeeQuery = "";
        if (empobj.getDdo() != null && !empobj.getDdo().isEmpty() && !empobj.getDdo().equals("0")) {
            employeeQuery = employeeQuery + " emp.ddo=\"" + empobj.getDdo() + "\"";
        }

        if (empobj.getEmployeeCode() != null && empobj.getEmployeeCode() != "" && !empobj.getEmployeeCode().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.employeeCode=\"" + empobj.getEmployeeCode() + "\"";
        }
        if (empobj.getEmployeeName() != null && empobj.getEmployeeName() != "" && !empobj.getEmployeeName().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.employeeName=\"" + empobj.getEmployeeName() + "\"";
        }
        if (empobj.getEmployeeCodeM() != null && empobj.getEmployeeCodeM() != "" && !empobj.getEmployeeCodeM().isEmpty()) {
            employeeQuery = employeeQuery + " and emp.employeeCodeM=\"" + empobj.getEmployeeCodeM() + "\"";
        }
        if (empobj.getLocation() != null && !empobj.getLocation().isEmpty() && !empobj.getLocation().equals("0")) {
            employeeQuery = employeeQuery + " and emp.location=\"" + empobj.getLocation() + "\"";
        }
        if (empobj.getDepartment() != null && !empobj.getDepartment().isEmpty() && !empobj.getDepartment().equals("0")) {
            employeeQuery = employeeQuery + " and emp.department=\"" + empobj.getDepartment() + "\"";
        }
        if (empobj.getDesignation() != null && !empobj.getDesignation().isEmpty() && !empobj.getDesignation().equals("0")) {
            employeeQuery = employeeQuery + " and emp.designation=\"" + empobj.getDesignation() + "\"";
        }
        if (empobj.getNatureType() != null && empobj.getNatureType().isEmpty() && !empobj.getNatureType().equals("0")) {
            employeeQuery = employeeQuery + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
        }
        if (empobj.getPostingCity() != null && empobj.getPostingCity().isEmpty() && !empobj.getPostingCity().equals("0")) {
            employeeQuery = employeeQuery + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
        }
        if (empobj.getPfType() != null && empobj.getPfType().isEmpty() && !empobj.getPfType().equals("0")) {
            employeeQuery = employeeQuery + " and emp.pfType=\"" + empobj.getPfType() + "\"";
        }
        if (empobj.getFundType() != null && empobj.getFundType().isEmpty() && !empobj.getFundType().equals("0")) {
            employeeQuery = employeeQuery + " and emp.fundType=\"" + empobj.getFundType() + "\"";
        }
        if (empobj.getBudgetHead() != null && empobj.getBudgetHead().isEmpty() && !empobj.getBudgetHead().equals("0")) {
            employeeQuery = employeeQuery + " and emp.budgetHead=\"" + empobj.getBudgetHead() + "\"";
        }

        if (empobj.getGrade() != null && empobj.getGrade().isEmpty() && !empobj.getGrade().equals("0")) {
            employeeQuery = employeeQuery + " and emp.grade=\"" + empobj.getGrade() + "\"";
        }

        Long fromdob = 0L;
        Long todob = 0L;
        Long fromdoj = 0L;
        Long todoj = 0L;
        Long fromdor = 0L;
        Long todor = 0L;

        if (empobj.getFromDOB() != null && empobj.getFromDOB() != "" && !empobj.getFromDOB().isEmpty()) {
            fromdob = saveInMilliSecondDate(empobj.getFromDOB());
            employeeQuery = employeeQuery + " and emp.dobInMillisecond>=" + fromdob;

        }
        //System.out.println("ghsfds" + empobj.getToDOB());
        if (empobj.getToDOB() != null && empobj.getToDOB() != "" && !empobj.getToDOB().isEmpty()) {
            todob = saveInMilliSecondDate(empobj.getToDOB());
            employeeQuery = employeeQuery + " and emp.dobInMillisecond<=" + todob;
        }
        if (empobj.getFromDOJ() != "" && empobj.getFromDOJ() != null && !empobj.getFromDOJ().isEmpty()) {
            fromdoj = saveInMilliSecondDate(empobj.getFromDOJ());
            employeeQuery = employeeQuery + " and emp.dateOfJoiningInMillisecond>=" + fromdoj;
        }
        if (empobj.getToDOJ() != null && empobj.getToDOJ() != "" && !empobj.getToDOJ().isEmpty()) {
            todoj = saveInMilliSecondDate(empobj.getToDOJ());
            employeeQuery = employeeQuery + " and emp.dateOfJoiningInMillisecond<=" + todoj;
        }
        if (empobj.getFromDOR() != null && empobj.getFromDOR() != "" && !empobj.getFromDOR().isEmpty()) {
            fromdor = saveInMilliSecondDate(empobj.getFromDOR());
            employeeQuery = employeeQuery + " and emp.dateOfRetirementInMillisecond>=" + fromdor;
        }
        if (empobj.getToDOR() != null && empobj.getToDOR() != "" && !empobj.getToDOR().isEmpty()) {
            todor = saveInMilliSecondDate(empobj.getToDOR());
            employeeQuery = employeeQuery + " and emp.dateOfRetirementInMillisecond>=" + todor;
        }
        String empQuery = "select *from " + empTable + ""
                + " as emp where " + employeeQuery + " and emp.status=\"Active\"";

//          String empQuery = "select *from " + empTable + ""
//                + " as emp where " + employeeQuery + " and emp.status=\"Active\"";
        //System.out.println("empQuery" + empQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, empQuery);
        //System.out.println("dbOutput" + dbOutput);

        List<Employee> employeeList = null;
        if (dbOutput != null && !dbOutput.isEmpty() && !dbOutput.equals("[]")) {
            employeeList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<Employee>>() {
            }.getType());
        }
        try {
            employeeList = getReligion(employeeList);
        } catch (Exception e) {
        }
        try {
            employeeList = getSalutation(employeeList);
        } catch (Exception e) {
        }
        try {
            employeeList = getMaritalStatus(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getBank(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getPFBank(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getDDO(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getDepartment(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getDiscipline(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getNature(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getFundType(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getBudgetHead(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getSalaryType(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getDesignation(employeeList);

        } catch (Exception e) {
        }

        try {
            employeeList = getCity(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getQuarterNo(employeeList);

        } catch (Exception e) {
        }

        try {
            employeeList = getGrade(employeeList);
        } catch (Exception e) {
        }
        try {
            employeeList = getPFType(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getSalaryBillType(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getLocation(employeeList);

        } catch (Exception e) {
        }
        String finalresult;
        //System.out.println("size" + employeeList);
        if (employeeList != null && employeeList.size() > 0) {
            finalresult = new Gson().toJson(employeeList);
        } else {
            finalresult = "No Data";
        }

        return finalresult;
    }

    private List<Employee> getLocation(List<Employee> employeeList) throws Exception {
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
                    employeeList.get(i).setLocation(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getSalaryBillType(List<Employee> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER);
        List<SalaryBillTypeOREmployeeCategory> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryBillTypeOREmployeeCategory>>() {
        }.getType());
        for (Iterator<SalaryBillTypeOREmployeeCategory> iterator = religionList.iterator(); iterator.hasNext();) {
            SalaryBillTypeOREmployeeCategory next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getSalaryBillType())) {
                    employeeList.get(i).setSalaryBillType(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getDDO(List<Employee> employeeList) throws Exception {
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
                    employeeList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getBudgetHead(List<Employee> employeeList) throws Exception {
        Map<String, String> BudgetHeadMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            BudgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : BudgetHeadMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getBudgetHead())) {
                    employeeList.get(i).setBudgetHead(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getSalaryType(List<Employee> employeeList) throws Exception {
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
                    employeeList.get(i).setSalaryType(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getCity(List<Employee> employeeList) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CITY_TABLE);
        List<CityMaster> religionList = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());
        for (Iterator<CityMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            CityMaster next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getPostingCity())) {
                    employeeList.get(i).setPostingCity(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getQuarterNo(List<Employee> employeeList) throws Exception {
        Map<String, String> QuarterMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.QUARTER_TABLE);
        List<Quarter> religionList = new Gson().fromJson(result, new TypeToken<List<Quarter>>() {
        }.getType());
        for (Iterator<Quarter> iterator = religionList.iterator(); iterator.hasNext();) {
            Quarter next = iterator.next();
            QuarterMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getQuarterNo());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : QuarterMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getQuarterNo())) {
                    employeeList.get(i).setQuarterNo(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getGrade(List<Employee> employeeList) throws Exception {
        Map<String, String> GradeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GRADE_TABLE);
        List<Grade> religionList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
        }.getType());
        for (Iterator<Grade> iterator = religionList.iterator(); iterator.hasNext();) {
            Grade next = iterator.next();
            GradeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getGradeName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : GradeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getGrade())) {
                    employeeList.get(i).setGrade(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getPFType(List<Employee> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.PF_TYPE_MASTER);
        List<PFType> religionList = new Gson().fromJson(result, new TypeToken<List<PFType>>() {
        }.getType());
        for (Iterator<PFType> iterator = religionList.iterator(); iterator.hasNext();) {
            PFType next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getPFType());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getPfType())) {
                    employeeList.get(i).setPfType(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private List<Employee> getSalutation(List<Employee> employeeList) throws Exception {
        Map<String, String> SalutationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALUTATION_TABLE);
        List<Salutation> religionList = new Gson().fromJson(result, new TypeToken<List<Salutation>>() {
        }.getType());
        for (Iterator<Salutation> iterator = religionList.iterator(); iterator.hasNext();) {
            Salutation next = iterator.next();
            SalutationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getSalutation());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : SalutationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getSalutationOption())) {
                    employeeList.get(i).setSalutationOption(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public ByteArrayOutputStream employeeListPreview(String EmployeeCode, String ddo, String path, String fin) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RestClient aql = new RestClient();
        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";

        String[] employeeCode = null;
        JSONArray arr = null;
        String employeeCodeList = "";

        if (EmployeeCode != null && !EmployeeCode.isEmpty()) {
            arr = new JSONArray(EmployeeCode);
            employeeCode = new String[arr.length()];
        }

        if (employeeCode.length > 0 && employeeCode != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                employeeCode[i] = (String) obj.get("employeeCode");
                employeeCodeList = employeeCodeList + "\"" + employeeCode[i] + "\",";
            }
        }

        if (employeeCodeList != null && !employeeCodeList.isEmpty()) {
            employeeCodeList = "(" + employeeCodeList.substring(0, employeeCodeList.length() - 1) + ")";
        }
        List<Employee> employeeList = null;

        String dDoValue = "emp.ddo=\"" + ddo + "\"";

        String empQuery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.location,emp.department,emp.designation,emp.salaryType,emp.ddo,emp.natureType,emp.postingCity,emp.pfType,emp.fundType,emp.gender,emp.status from " + empTable + ""
                + " as emp where " + dDoValue + " and  emp.employeeCode in " + employeeCodeList + " and emp.status=\"Active\"";

        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, empQuery);
        //System.out.println("dbOutput" + dbOutput);

        if (dbOutput != null && !dbOutput.isEmpty() && !dbOutput.equals("[]")) {
            employeeList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<Employee>>() {
            }.getType());
        }

        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getSalutation(employeeList);
        }

        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getMaritalStatus(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getBank(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getDDO(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getDepartment(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getDiscipline(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getNature(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getFundType(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getBudgetHead(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getSalaryType(employeeList);

        }
        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getDesignation(employeeList);

        }

        if (employeeList != null && employeeList.size() > 0) {
            employeeList = getLocation(employeeList);

        }
        ListOfEmployeeReport lEP = new ListOfEmployeeReport();
        if (employeeList != null && employeeList.size() > 0) {
            bos = lEP.generateEmployeeReport(employeeList, path, fin);
        }

        return bos;
    }

    public long saveInMilliSecondDate(String str) throws ParseException {
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
