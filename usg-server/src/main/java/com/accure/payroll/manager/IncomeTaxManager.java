/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.db.in.DAO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Employee;
import com.accure.payroll.dto.IncomeTax;
import static com.accure.payroll.manager.EmpAttendanceManager.saveInMilliSecondAttendance;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author upendra
 */
public class IncomeTaxManager {

    public boolean save(String incometaxData, String month, String year, String ddo) throws Exception {
        boolean result = false;

        RestClient aql = new RestClient();

        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";

        JSONArray arr = new JSONArray(incometaxData);
        String[] employeeCode = new String[arr.length()];
        String[] it = new String[arr.length()];
        String[] cess = new String[arr.length()];
        String[] total = new String[arr.length()];

        String dDoValue = "emp.ddo=\"" + ddo + "\"";

        for (int i = 0; i < arr.length(); i++) {
            ArrayList<Employee> empList = null;
            JSONObject obj = arr.getJSONObject(i);
            employeeCode[i] = (String) obj.get("employeeCode");
            it[i] = (String) obj.get("it");
            cess[i] = (String) obj.get("educationcess");
            total[i] = (String) obj.get("total");
            int incomeTax = 0;
            double educess = 0;
            double itTotal = 0;
            if (it[i] != null && !it[i].isEmpty() && it[i] != "") {
                incomeTax = Integer.parseInt(it[i]);
            }
            if (cess[i] != null && !cess[i].isEmpty() && cess[i] != "") {
                educess = Double.parseDouble(cess[i]);
            }
            if (total[i] != null && !total[i].isEmpty() && total[i] != "") {
                itTotal = Double.parseDouble(total[i]);
            }

            String empCodeStr = " emp.employeeCode=\"" + employeeCode[i] + "\"";

            String empQuery = "select emp._id as idStr,emp.employeeCodeM,emp.employeeName,emp.pfType,emp.panNo,emp.fundType,emp.onDeputation,emp.location,emp.department,emp.designation,emp.salaryType,emp.employeeCode,emp.ddo,emp.natureType,emp.postingCity,emp.pfType,emp.fundType,emp.budgetHead,emp.status from " + empTable + ""
                    + " as emp where " + empCodeStr + " and  emp.status=\"Active\" and  " + dDoValue;

            String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, empQuery);

            //System.out.println("dbOutput" + dbOutput);
            if (dbOutput != null && !dbOutput.isEmpty() && !dbOutput.equals("[]")) {
                empList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<Employee>>() {
                }.getType());
                if (empList != null && empList.size() > 0) {
                    for (Employee emp : empList) {
                        IncomeTax incomeprocess = new IncomeTax();
                        incomeprocess.setBudgetHead(emp.getBudgetHead());
                        incomeprocess.setIt(incomeTax);
                        incomeprocess.setEducationcess(educess);
                        incomeprocess.setTotal(itTotal);
                        incomeprocess.setLocation(emp.getLocation());
                        incomeprocess.setDepartment(emp.getDepartment());
                        incomeprocess.setDesignation(emp.getDesignation());
                        incomeprocess.setPfType(emp.getPfType());
                        incomeprocess.setFundType(emp.getFundType());
                        incomeprocess.setPanNo(emp.getPanNo());
                        incomeprocess.setMonth(Integer.parseInt(month));
                        incomeprocess.setYear(Integer.parseInt(year));
                        incomeprocess.setNatureType(emp.getNatureType());
                        incomeprocess.setPostingCity(emp.getPostingCity());
                        incomeprocess.setDdo(ddo);
                        incomeprocess.setEmployeeCode(emp.getEmployeeCode());
                        incomeprocess.setEmployeeCodeM(emp.getEmployeeCodeM());
                        incomeprocess.setEmployeeName(emp.getEmployeeName());
                        String incometable = ApplicationConstants.USG_DB1 + ApplicationConstants.INCOMETAX_TABLE + "`";

                        String empCode = "emp.employeeCode=\"" + emp.getEmployeeCode() + "\"";

                        String updateQuery = "select emp._id as idStr,emp.employeeCode,emp.employeeCodeM,emp.employeeName,emp.onDeputation,emp.location,emp.department,emp.designation,emp.salaryType,emp.employeeCode,emp.ddo,emp.natureType,emp.postingCity,emp.pfType,emp.fundType,emp.budgetHead,emp.status from " + incometable + ""
                                + " as emp where " + empCode + " and  emp.year= " + Integer.parseInt(year) + " and emp.month=" + Integer.parseInt(month) + " and emp.status=\"Active\" and " + dDoValue;

                        //System.out.println("update" + updateQuery);
                        String updateOutput = aql.getRestData(ApplicationConstants.END_POINT, updateQuery);

                        if (updateOutput != null && !updateOutput.isEmpty() && !updateOutput.equals("[]")) {
                            List<IncomeTax> updateList = new Gson().fromJson(updateOutput, new TypeToken< ArrayList<IncomeTax>>() {
                            }.getType());
                            if (updateList != null && updateList.size() > 0) {
                                for (IncomeTax updateobj : updateList) {
                                    String id = updateobj.getIdStr();
                                    //System.out.println(id);
                                    incomeprocess.setUpdateDate(Long.toString(System.currentTimeMillis()));
                                    incomeprocess.setIdStr(id);
                                    incomeprocess.setProcessStatus(ApplicationConstants.PROCESSED);
                                    incomeprocess.setStatus(ApplicationConstants.ACTIVE);
                                    DAO dao = DBManager.getDbConnection();
                                    result = dao.update(ApplicationConstants.INCOMETAX_TABLE, id, new Gson().toJson(incomeprocess));
                                    result = true;
                                }
                            }
                        } else {
                            incomeprocess.setCreateDate(Long.toString(System.currentTimeMillis()));
                            incomeprocess.setProcessStatus(ApplicationConstants.PROCESSED);
                            incomeprocess.setStatus(ApplicationConstants.ACTIVE);
                            DAO dao = DBManager.getDbConnection();
                            dao.insert(ApplicationConstants.INCOMETAX_TABLE, new Gson().toJson(incomeprocess));
//                            dao.close();
                            result = true;
                        }
                    }

                }
            }
        }
        return result;
    }

    public String search(String ddo) throws Exception {
        //System.out.println("ddo");
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.INCOMETAX_TABLE, conditionMap);
        return result;

    }

    public HashMap<String, String> fetchAllLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String locationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_TABLE, conditionMap);
        List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
        }.getType());
        conditionMap.clear();
        for (Location location : locationList) {
            conditionMap.put(((Map<String, String>) location.getId()).get("$oid"), location.getLocationName());
        }
        return conditionMap;
    }

    public HashMap getSearchResult(String IncomeTaxJsonString) throws Exception {

        HashMap outputMap = new HashMap();

        if (IncomeTaxJsonString == null) {
            return null;
        }
        RestClient aql = new RestClient();

        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        String inComeTaxTable = ApplicationConstants.USG_DB1 + ApplicationConstants.INCOMETAX_TABLE + "`";

        IncomeTax empobj = new Gson().fromJson(IncomeTaxJsonString, new TypeToken<IncomeTax>() {
        }.getType());

        String processedEmpCodes = "";
        String incomeTaxQuery = "";
        String query = "";

        List<IncomeTax> processedList = null;
        List<IncomeTax> lockedList = null;
        List<IncomeTax> empList = null;
        List<IncomeTax> preEmpList = null;

        if (empobj.getEmployeeCode() != null && empobj.getEmployeeCode() != "" && !empobj.getEmployeeCode().isEmpty()) {
            incomeTaxQuery = incomeTaxQuery + " and emp.employeeCode=\"" + empobj.getEmployeeCode() + "\"";
        }
        if (empobj.getEmployeeName() != null && empobj.getEmployeeName() != "" && !empobj.getEmployeeName().isEmpty()) {
            incomeTaxQuery = incomeTaxQuery + " and emp.employeeName=\"" + empobj.getEmployeeName() + "\"";
            //System.out.println(1);
        }
        if (empobj.getEmployeeCodeM() != null && empobj.getEmployeeCodeM() != "" && !empobj.getEmployeeCodeM().isEmpty()) {
            incomeTaxQuery = incomeTaxQuery + " and emp.employeeCodeM=\"" + empobj.getEmployeeCodeM() + "\"";
        }
        if (empobj.getLocation() != null && !empobj.getLocation().isEmpty() && !empobj.getLocation().equals("")) {
            incomeTaxQuery = incomeTaxQuery + " and emp.location=\"" + empobj.getLocation() + "\"";
        }
        if (empobj.getDepartment() != null && !empobj.getDepartment().isEmpty() && !empobj.getDepartment().equals("")) {
            incomeTaxQuery = incomeTaxQuery + " and emp.department=\"" + empobj.getDepartment() + "\"";
        }
        if (empobj.getDesignation() != null && empobj.getDesignation().isEmpty() && !empobj.getDesignation().equals("")) {
            incomeTaxQuery = incomeTaxQuery + " and emp.designation=\"" + empobj.getDesignation() + "\"";
        }
        if (empobj.getNatureType() != null && !empobj.getNatureType().isEmpty() && !empobj.getNatureType().equals("")) {
            incomeTaxQuery = incomeTaxQuery + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
        }
        if (empobj.getPostingCity() != null && empobj.getPostingCity().isEmpty() && !empobj.getPostingCity().equals("")) {
            incomeTaxQuery = incomeTaxQuery + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
        }

        int month = empobj.getMonth();
        int year = empobj.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        String dateMonth = "";
        if (month < 10) {
            dateMonth = "0" + month;
        } else {
            dateMonth = String.valueOf(month);
        }
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String dateOfAppointment = days + "/" + dateMonth + "/" + year;

        long dateofApp = saveInMilliSecondAttendance(dateOfAppointment);

        String incomeTaxQuerysearch = "select emp._id as idStr,emp.educationcess,emp.total,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.it,emp.location,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.budgetHead,emp.ddo,lockStatus from " + inComeTaxTable + ""
                + " as emp where emp.month= " + empobj.getMonth() + " and emp.year=" + empobj.getYear() + " and  emp.lockStatus=false and  emp.status=\"Active\" and ddo=\"" + empobj.getDdo() + "\"" + incomeTaxQuery;

        String lockedIncomeTaxQuery = "select emp._id as idStr,emp.employeeCode,emp.educationcess,emp.total,emp.employeeName,emp.employeeCodeM,emp.status,emp.it,emp.location,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.budgetHead,emp.ddo,lockStatus from " + inComeTaxTable + ""
                + " as emp where emp.month= " + empobj.getMonth() + " and emp.year=" + empobj.getYear() + " and  emp.lockStatus=true and emp.status=\"Active\" and  ddo=\"" + empobj.getDdo() + "\"" + incomeTaxQuery;

        //System.out.println("lockedIncomeOutput" + lockedIncomeTaxQuery);
        String lockedIncomeOutput = aql.getRestData(ApplicationConstants.END_POINT, lockedIncomeTaxQuery);

        //System.out.println("lockedIncomeOutput" + lockedIncomeOutput);
        if (lockedIncomeOutput != null && !lockedIncomeOutput.isEmpty() && !lockedIncomeOutput.equals("[]")) {
            lockedList = new Gson().fromJson(lockedIncomeOutput, new TypeToken<ArrayList<IncomeTax>>() {
            }.getType());
            if (lockedList != null && lockedList.size() > 0) {
                for (IncomeTax as : lockedList) {
                    processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeeCode() + "\",";
                }
            }
        }

        String empIncomeTaxoutput = aql.getRestData(ApplicationConstants.END_POINT, incomeTaxQuerysearch);

        if (empIncomeTaxoutput != null && !empIncomeTaxoutput.isEmpty() && !empIncomeTaxoutput.equals("[]")) {
            processedList = new Gson().fromJson(empIncomeTaxoutput, new TypeToken<ArrayList<IncomeTax>>() {
            }.getType());
            if (processedList != null && processedList.size() > 0) {
                for (IncomeTax as : processedList) {
                    processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeeCode() + "\",";
                }
            }
        }

        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
            processedEmpCodes = "(" + processedEmpCodes.substring(0, processedEmpCodes.length() - 1) + ")";
        }

        String ddoValue = " emp.ddo=\"" + empobj.getDdo() + "\"";
        String previousmonthQuery = "";

        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {

            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + empTable + ""
                    + " as emp where " + ddoValue + " and emp.status=\"Active\" and  emp.employeeCode not in " + processedEmpCodes + " and emp.dateOfJoiningInMillisecond<=" + dateofApp + "and emp.dateOfRetirementInMillisecond>=" + dateofApp + " " + incomeTaxQuery;
        } else {
            if (!empobj.getEmployeeCode().isEmpty() && empobj.getEmployeeCode() != null) {
                incomeTaxQuery = incomeTaxQuery + " and emp.employeeCode=\"" + empobj.getEmployeeCode() + "\"";
            }
            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo,emp.basic from " + empTable + ""
                    + " as emp where " + ddoValue + " and emp.status=\"Active\" " + "and emp.dateOfJoiningInMillisecond<=" + dateofApp + "and emp.dateOfRetirementInMillisecond>=" + dateofApp + " " + incomeTaxQuery;
        }

        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, query);

        String previousEmployeeCode = "";
        if (dbOutput != null && !dbOutput.isEmpty() && !dbOutput.equals("[]")) {
            empList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<IncomeTax>>() {
            }.getType());

            for (IncomeTax emp : empList) {
                previousEmployeeCode = previousEmployeeCode + "\"" + emp.getEmployeeCode() + "\",";
            }
        }

        if (previousEmployeeCode != null && !previousEmployeeCode.isEmpty()) {
            previousEmployeeCode = "(" + previousEmployeeCode.substring(0, previousEmployeeCode.length() - 1) + ")";
        }

        int previousMonth = empobj.getMonth();
        int previousYear = 0;
        if (previousMonth == 1) {
            previousYear = empobj.getYear() - 1;
            previousMonth = 12;
        } else {
            previousYear = empobj.getYear();
            previousMonth--;
        }

        if (previousEmployeeCode != null && !previousEmployeeCode.isEmpty()) {
            previousmonthQuery = "select emp._id as idStr,emp.educationcess,emp.total,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.it,emp.location,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.budgetHead,emp.ddo,lockStatus from " + inComeTaxTable + ""
                    + " as emp where emp.month= " + previousMonth + " and emp.year=" + previousYear + " and emp.employeeCode  in " + previousEmployeeCode + " and  emp.status=\"Active\" and ddo=\"" + empobj.getDdo() + "\"" + incomeTaxQuery;
        }
        //System.out.println("previousmonthQuery" + previousmonthQuery);

        String previousDbOutput = aql.getRestData(ApplicationConstants.END_POINT, previousmonthQuery);

        //System.out.println("previousDbOutput" + previousDbOutput);
        if (previousDbOutput != null && !previousDbOutput.isEmpty() && !previousDbOutput.equals("[]")) {
            preEmpList = new Gson().fromJson(previousDbOutput, new TypeToken< ArrayList<IncomeTax>>() {
            }.getType());

            for (int i = 0; i < empList.size(); i++) {
                for (int j = 0; j < preEmpList.size(); j++) {
                    if (preEmpList.get(j).getEmployeeCode().equalsIgnoreCase(empList.get(i).getEmployeeCode())) {
                        empList.get(i).setIdStr(empList.get(i).getIdStr());
                        empList.get(i).setTotal(preEmpList.get(j).getTotal());
                        empList.get(i).setEducationcess(preEmpList.get(j).getEducationcess());
                        empList.get(i).setIt(preEmpList.get(j).getIt());
                        break;
                    }
                }
            }
        }

        if (processedList != null && processedList.size() > 0) {
            processedList = getLocation(processedList);
        }
        if (empList != null && empList.size() > 0) {
            empList = getLocation(empList);
        }

        if (processedList != null && processedList.size() > 0) {
            processedList = getDepartment(processedList);
        }
        if (empList != null && empList.size() > 0) {
            empList = getDepartment(empList);
        }

        if (empList != null && empList.size() > 0) {
            outputMap.put("empList", empList);
        } else {
            outputMap.put("empList", ApplicationConstants.NO_DATA_FOUND);
        }
        if (processedList != null && processedList.size() > 0) {
            outputMap.put("incometaxdata", processedList);
        } else {
            outputMap.put("incometaxdata", ApplicationConstants.NO_DATA_FOUND);
        }

        //System.out.println("output" + outputMap);
        return outputMap;
    }

    private static List<IncomeTax> getLocation(List<IncomeTax> employeeList) throws Exception {
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

    public static List<Employee> getEmployeeDepartment(List<Employee> employeeList) throws Exception {
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
                    employeeList.get(i).setDepartment(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    private static List<Employee> getEmployeeLocation(List<Employee> employeeList) throws Exception {
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

    public static List<IncomeTax> getDepartment(List<IncomeTax> employeeList) throws Exception {
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
                    employeeList.get(i).setDepartment(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    /**
     * @author chaitu
     * @description fetch() method will expect input as incomeTaxId(primaryKey)
     * then it will returns the IncomeTax object or null.
     * @table incometaxprocess
     * @param String
     * @return IncomeTax
     * @throws java.lang.Exception
     */
    public static IncomeTax fetch(String incomeTaxId) throws Exception {
        IncomeTax IncomeTax = null;
        if (incomeTaxId == null) {
            return IncomeTax;
        }
        String dbOutput = DBManager.getDbConnection().fetch(ApplicationConstants.INCOMETAX_TABLE, incomeTaxId);
        ArrayList<IncomeTax> resultList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<IncomeTax>>() {
        }.getType());
        IncomeTax = resultList.get(0);
        return IncomeTax;
    }

    /**
     * @author chaitu
     * @description update() method will update the incomeTax lock status flag
     * @table incometaxprocess
     * @param String incomeTaxId
     * @param boolean lockStatus
     * @return boolean
     * @throws Exception
     */
    public static boolean update(String incomeTaxId, boolean lockStatus) throws Exception {
        boolean isIncomeTaxUpdated = false;
        if (incomeTaxId == null) {
            return isIncomeTaxUpdated;
        }

        IncomeTax itax = fetch(incomeTaxId);
        itax.setLockStatus(lockStatus);
        itax.setUpdateDate(Long.toString(System.currentTimeMillis()));
        isIncomeTaxUpdated = DBManager.getDbConnection().update(ApplicationConstants.INCOMETAX_TABLE, incomeTaxId, new Gson().toJson(itax));
        return isIncomeTaxUpdated;
    }

}
