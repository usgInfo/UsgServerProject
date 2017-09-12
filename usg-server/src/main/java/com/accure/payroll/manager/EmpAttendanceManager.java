/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.db.in.DAO;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.EmpAttendance;
import static com.accure.payroll.manager.EmpAttendanceManager.getDepartmentforAttendance;
import static com.accure.payroll.manager.EmpAttendanceManager.getDesignationforAttendance;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getMilliseconds;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
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
 * @author Mano
 */
public class EmpAttendanceManager {

    static String attendanceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";
    static String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";

    public HashMap getSearchResult(String EmpAttendanceJsonString) throws Exception {

        HashMap outputMap = new HashMap();

        if (EmpAttendanceJsonString == null) {
            return null;
        }
        RestClient aql = new RestClient();

        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        String EmpAttendanceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";

        String processedEmpCodes = "";
        String employeeQuery = "";
        String query = "";

        EmpAttendance empobj = new Gson().fromJson(EmpAttendanceJsonString, new TypeToken<EmpAttendance>() {
        }.getType());

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

        List<Employee> empList = null;
        List<EmpAttendance> processedList = null;
        List<EmpAttendance> lockedList = null;

        String EmpAttendanceQuery = "select emp._id as idStr,emp.isattendanceAdjFlag,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + EmpAttendanceTable + ""
                + " as emp where emp.month= " + empobj.getMonth() + " and emp.status=\"Active\"  and attendaceStatus=\"Processed\" and  emp.lockStatus=false  and emp.year=" + empobj.getYear() + " and ddo=\"" + empobj.getDdo() + "\"" + employeeQuery;

        String lockedAttendanceQuery = "select emp._id as idStr,emp.isattendanceAdjFlag,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + EmpAttendanceTable + ""
                + " as emp where emp.month= " + empobj.getMonth() + " and emp.status=\"Active\" and attendaceStatus=\"Processed\" and  emp.lockStatus=true   and emp.year=" + empobj.getYear() + " and ddo=\"" + empobj.getDdo() + "\"" + employeeQuery;

        String EmpAttendanceOutput = null;
        try {
            EmpAttendanceOutput = aql.getRestData(ApplicationConstants.END_POINT, EmpAttendanceQuery);
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg.equalsIgnoreCase("Failed : HTTP error code : 404")) {

            }
        }

        String lockedAttendanceOutput = aql.getRestData(ApplicationConstants.END_POINT, lockedAttendanceQuery);

        if (lockedAttendanceOutput != null && !lockedAttendanceOutput.isEmpty() && !lockedAttendanceOutput.equals("[]")) {
            lockedList = new Gson().fromJson(lockedAttendanceOutput, new TypeToken<ArrayList<EmpAttendance>>() {
            }.getType());
            if (lockedList != null && lockedList.size() > 0) {
                for (EmpAttendance as : lockedList) {
                    processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeeCode() + "\",";
                }
            }
        }

        if (EmpAttendanceOutput != null && !EmpAttendanceOutput.isEmpty() && !EmpAttendanceOutput.equals("[]")) {
            processedList = new Gson().fromJson(EmpAttendanceOutput, new TypeToken<ArrayList<EmpAttendance>>() {
            }.getType());
            if (processedList != null && processedList.size() > 0) {
                for (EmpAttendance as : processedList) {
                    processedEmpCodes = processedEmpCodes + "\"" + as.getEmployeeCode() + "\",";
                }
            }
        }

        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
            processedEmpCodes = "(" + processedEmpCodes.substring(0, processedEmpCodes.length() - 1) + ")";
        }

//        if (!empobj.getEmployeeName().isEmpty() && empobj.getEmployeeName() != null) {
//            employeeQuery = employeeQuery + " and emp.employeeName=\"" + empobj.getEmployeeName() + "\"";
//        }
//        if (!empobj.getEmployeeCodeM().isEmpty() && empobj.getEmployeeCodeM() != null) {
//            employeeQuery = employeeQuery + " and emp.employeeCodeM=\"" + empobj.getEmployeeCodeM() + "\"";
//        }
//
//        if (!empobj.getLocation().equals("0") && !empobj.getLocation().isEmpty() && empobj.getLocation() != null) {
//            employeeQuery = employeeQuery + " and emp.location=\"" + empobj.getLocation() + "\"";
//        }
//        if (!empobj.getDepartment().equals("0") && !empobj.getDepartment().isEmpty() && empobj.getDepartment() != null) {
//            employeeQuery = employeeQuery + " and emp.department=\"" + empobj.getDepartment() + "\"";
//        }
//        if (!empobj.getDesignation().equals("0") && !empobj.getDesignation().isEmpty() && empobj.getDepartment() != null) {
//            employeeQuery = employeeQuery + " and emp.designation=\"" + empobj.getDesignation() + "\"";
//        }
//        if (!empobj.getNatureType().equals("0") && empobj.getNatureType().isEmpty() && empobj.getNatureType() != null) {
//            employeeQuery = employeeQuery + " and emp.natureType=\"" + empobj.getNatureType() + "\"";
//        }
//        if (!empobj.getPostingCity().equals("0") && empobj.getPostingCity().isEmpty() && empobj.getPostingCity() != null) {
//            employeeQuery = employeeQuery + " and emp.postingCity=\"" + empobj.getPostingCity() + "\"";
//        }
//        if (!empobj.getPfType().equals("0") && empobj.getPfType().isEmpty() && empobj.getPfType() != null) {
//            employeeQuery = employeeQuery + " and emp.pfType=\"" + empobj.getPfType() + "\"";
//        }
//        if (!empobj.getFundType().equals("0") && empobj.getFundType().isEmpty() && empobj.getFundType() != null) {
//            employeeQuery = employeeQuery + " and emp.fundType=\"" + empobj.getFundType() + "\"";
//        }
//        if (!empobj.getBudgetHead().equals("0") && empobj.getBudgetHead().isEmpty() && empobj.getBudgetHead() != null) {
//            employeeQuery = employeeQuery + " and emp.budgetHead=\"" + empobj.getBudgetHead() + "\"";
//        }
        String dDoValue = "emp.ddo=\"" + empobj.getDdo() + "\"";
        int month = empobj.getMonth();
        int year = empobj.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String dateMonth = "";
        if (month < 10) {
            dateMonth = "0" + month;
        } else {
            dateMonth = String.valueOf(month);
        }

        String dateOfAppointment = days + "/" + dateMonth + "/" + year;
        //String todateOfAppointment=days+"/"+dateMonth+"/"+year;
        ////System.out.println(""+dateOfAppointment);
        long dateofApp = saveInMilliSecondAttendance(dateOfAppointment);
        //long tdateofApp= saveInMilliSecondAttendance(todateOfAppointment);

        //System.out.println("query" + empobj.getEmployeeCode());
        if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {

            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName, emp.isSuspended,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + empTable + ""
                    + " as emp where  " + dDoValue + " and emp.status=\"Active\"  and emp.EmployeeLeftStatus =\"No\" and emp.employeeCode not in " + processedEmpCodes + " and emp.dateOfJoiningInMillisecond<=" + dateofApp + " and emp.dateOfRetirementInMillisecond>=" + dateofApp + " " + employeeQuery + "";

            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + empTable + ""
                    + " as emp where " + dDoValue + " and  emp.status=\"Active\" and  emp.EmployeeLeftStatus =\"No\" and  emp.employeeCode not in " + processedEmpCodes + " and emp.dateOfJoiningInMillisecond<=" + dateofApp + " and emp.dateOfRetirementInMillisecond>=" + dateofApp + " " + employeeQuery + "";
        } else {
            if (empobj.getEmployeeCode() != null && empobj.getEmployeeCode() != "" && !empobj.getEmployeeCode().isEmpty()) {
                employeeQuery = employeeQuery + " and emp.employeeCode=\"" + empobj.getEmployeeCode() + "\"";
            }
            query = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.isSuspended ,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo,emp.basic from " + empTable + ""
                    + " as emp where " + dDoValue + "  and  emp.EmployeeLeftStatus =\"No\" and  emp.status=\"Active\" " + "and emp.dateOfJoiningInMillisecond<=" + dateofApp + " and emp.dateOfRetirementInMillisecond>=" + dateofApp + " " + employeeQuery + "";
        }

        System.out.println("query" + query);
        //System.out.println("query" + query);
        String ViewattendanceLockedQuery = "select emp._id as idStr,emp.isattendanceAdjFlag,emp.employeeCode,emp.employeeName,emp.emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.ddo from " + EmpAttendanceTable + ""
                + " as emp where emp.month= " + empobj.getMonth() + " and emp.status=\"Active\" and attendaceStatus=\"Processed\" and  emp.lockStatus=true and emp.year=" + empobj.getYear() + " and ddo=\"" + empobj.getDdo() + "\"";

        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, query);

        //System.out.println("dbOutput" + dbOutput);
        String ViewEmpAttendanceOutput = aql.getRestData(ApplicationConstants.END_POINT, ViewattendanceLockedQuery);
        List<EmpAttendance> lockedListData = null;

        if (ViewEmpAttendanceOutput != null && !ViewEmpAttendanceOutput.isEmpty() && !ViewEmpAttendanceOutput.equals("[]")) {
            lockedListData = new Gson().fromJson(ViewEmpAttendanceOutput, new TypeToken< ArrayList<EmpAttendance>>() {
            }.getType());
        }

        if (dbOutput != null && !dbOutput.isEmpty() && !dbOutput.equals("[]")) {
            empList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<Employee>>() {
            }.getType());
        }

        if (empList != null && empList.size() > 0) {
            empList = getLocation(empList);
        }
        if (processedList != null && processedList.size() > 0) {
            processedList = getLocationforAttendance(processedList);
        }
        if (lockedListData != null && lockedListData.size() > 0) {
            lockedListData = getLocationforAttendance(lockedListData);
        }

        if (empList != null && empList.size() > 0) {
            empList = getDDO(empList);
        }
        if (processedList != null && processedList.size() > 0) {
            processedList = getDDOforAttendance(processedList);
        }
        if (lockedListData != null && lockedListData.size() > 0) {
            lockedListData = getDDOforAttendance(lockedListData);
        }

        if (empList != null && empList.size() > 0) {
            empList = getSalaryType(empList);
        }
        if (processedList != null && processedList.size() > 0) {
            processedList = getSalaryTypeforAttence(processedList);
        }
        if (lockedListData != null && lockedListData.size() > 0) {
            lockedListData = getSalaryTypeforAttence(lockedListData);
        }
        if (empList != null && empList.size() > 0) {
            empList = getDepartment(empList);
        }
        if (processedList != null && processedList.size() > 0) {
            processedList = getDepartmentforAttendance(processedList);
        }
        if (lockedListData != null && lockedListData.size() > 0) {
            lockedListData = getDepartmentforAttendance(lockedListData);
        }

        if (empList != null && empList.size() > 0) {
            empList = getDesignation(empList);
        }
        if (processedList != null && processedList.size() > 0) {
            processedList = getDesignationforAttendance(processedList);
        }
        if (lockedListData != null && lockedListData.size() > 0) {
            lockedListData = getDesignationforAttendance(lockedListData);
        }

        if (processedList != null && processedList.size() > 0) {

            outputMap.put("processed", processedList);
        } else {
            outputMap.put("processed", ApplicationConstants.NO_DATA_FOUND);
        }
        //System.out.println("empList" + empList);

        if (empList != null && empList.size() > 0) {
            outputMap.put("notprocessed", empList);
        } else {
            outputMap.put("notprocessed", ApplicationConstants.NO_DATA_FOUND);
        }
        if (lockedListData != null && lockedListData.size() > 0) {
            outputMap.put("lockedprocess", lockedListData);
        } else {
            outputMap.put("lockedprocess", ApplicationConstants.NO_DATA_FOUND);
        }
        return outputMap;
    }

    public static List<Employee> getDesignation(List<Employee> employeeList) throws Exception {
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
                    employeeList.get(i).setDesignation(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<EmpAttendance> getDesignationforAttendance(List<EmpAttendance> employeeList) throws Exception {
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
                    employeeList.get(i).setDesignation(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<Employee> getDepartment(List<Employee> employeeList) throws Exception {
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

    public static List<EmpAttendance> getDepartmentforAttendance(List<EmpAttendance> employeeList) throws Exception {
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

    private static List<EmpAttendance> getLocationforAttendance(List<EmpAttendance> employeeList) throws Exception {
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

    private static List<Employee> getLocation(List<Employee> employeeList) throws Exception {
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

    private static List<Employee> getDDO(List<Employee> employeeList) throws Exception {
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

    private static List<EmpAttendance> getDDOforAttendance(List<EmpAttendance> employeeList) throws Exception {
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
                    employeeList.get(i).setDdo(entry.getValue());
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

    private List<EmpAttendance> getSalaryTypeforAttence(List<EmpAttendance> employeeList) throws Exception {
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

    public static boolean employeeleavetransactionJoing(String empjson, String month, String year, String ddo) throws Exception {

        boolean result = false;
        RestClient aql = new RestClient();
        String Active = "\"Active\"";
        JSONArray arr = new JSONArray(empjson);
        //System.out.println("empjson" + empjson);
        String[] employeeCode = new String[arr.length()];
        String empTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMPLOYEE_TABLE + "`";
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            employeeCode[i] = (String) obj.get("employeeCode");
        }
        String empCodeStr = "";
        if (employeeCode != null && employeeCode.length > 0) {
            for (String eCode : employeeCode) {
                empCodeStr = empCodeStr + "\"" + eCode + "\",";
            }
            if (empCodeStr != null && !empCodeStr.isEmpty()) {
                empCodeStr = "(" + empCodeStr.substring(0, empCodeStr.length() - 1) + ")";
            }
        }
        ArrayList<Employee> empList = null;

        String dDoValue = "emp.ddo=\"" + ddo + "\"";

        String empQuery = "select emp._id as idStr,emp.employeeCodeM,emp.isSuspended,emp.stopSalary,emp.stopSalaryDate,emp.employeeName,emp.onDeputation,emp.location,emp.department,emp.designation,emp.salaryType,emp.employeeCode,emp.ddo,emp.natureType,emp.postingCity,emp.pfType,emp.fundType,emp.budgetHead,emp.status from " + empTable + ""
                + " as emp where " + dDoValue + " and  emp.employeeCode in " + empCodeStr + " and emp.status=\"Active\"";
        //System.out.println("empQuery" + empQuery);

        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, empQuery);
        //System.out.println("dbOutput" + dbOutput);

        Calendar calendar = Calendar.getInstance();
        if (month.equals("1")) {
            calendar.set(Integer.parseInt(year), Calendar.JANUARY, 1);
        }
        if (month.equals("2")) {
            calendar.set(Integer.parseInt(year), Calendar.FEBRUARY, 1);
        }
        if (month.equals("3")) {
            calendar.set(Integer.parseInt(year), Calendar.MARCH, 1);
        }
        if (month.equals("4")) {
            calendar.set(Integer.parseInt(year), Calendar.APRIL, 1);
        }
        if (month.equals("5")) {
            calendar.set(Integer.parseInt(year), Calendar.MAY, 1);
        }
        if (month.equals("6")) {
            calendar.set(Integer.parseInt(year), Calendar.JUNE, 1);
        }
        if (month.equals("7")) {
            calendar.set(Integer.parseInt(year), Calendar.JULY, 1);
        }
        if (month.equals("8")) {
            calendar.set(Integer.parseInt(year), Calendar.AUGUST, 1);
        }
        if (month.equals("9")) {
            calendar.set(Integer.parseInt(year), Calendar.SEPTEMBER, 1);
        }
        if (month.equals("10")) {
            calendar.set(Integer.parseInt(year), Calendar.OCTOBER, 1);
        }
        if (month.equals("11")) {
            calendar.set(Integer.parseInt(year), Calendar.NOVEMBER, 1);
        }
        if (month.equals("12")) {
            calendar.set(Integer.parseInt(year), Calendar.DECEMBER, 1);
        }

        int totaldays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (month.length() <= 1) {
            month = "0" + month;
        } else {
            month = "" + month;
        }
        String fromDate = "01/" + month + "/" + year;
        String toDate = totaldays + "/" + month + "/" + year;
        Long fdate = getMilliseconds(fromDate);
        Long tdate = getMilliseconds(toDate);

        List datearrayList = new ArrayList();
        int count = 1;
        for (int i = 0; i < totaldays; i++) {
            String date;
            if (count < 9) {
                date = "0" + count;
            } else {
                date = "" + count;
            }
            datearrayList.add(date + "/" + month + "/" + year);
            count++;
        }

        String mongoCollectionName = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_TRANSACTION + "`";

        if (dbOutput != null && !dbOutput.isEmpty() && !dbOutput.equals("[]")) {
            empList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<Employee>>() {
            }.getType());

            if (empList != null && empList.size() > 0) {
                for (Employee emp : empList) {
                    String emcode = "\"" + emp.getEmployeeCode() + "\"";

                    String leaveTypeDescriptionSL = "\"" + ApplicationConstants.SICK_LEAVE + "\"";
                    String leaveTypeDescriptionLWP = "\"" + ApplicationConstants.LEAVE_WITHOOUT_PAY + "\"";
                    int presentdays = 0;
                    int SLDays = 0;
                    int LWPDays = 0;

                    String querySL = "select  SUM(totalLeaveDays) from  " + mongoCollectionName + " where employeeCode = " + emcode + " and fromDateInMilliSecond>= " + fdate + " and  toDateInMilliSecond<=  " + tdate + " and leaveTypeDescription =" + leaveTypeDescriptionSL + " and status =" + Active;
                    String queryLWP = "select leaveWithoutPayDays from " + mongoCollectionName + " where employeeCode = " + emcode + " and fromDateInMilliSecond>= " + fdate + " and  toDateInMilliSecond<=  " + tdate + " and leaveTypeDescription =" + leaveTypeDescriptionLWP + " and status =" + Active;

                    String jsonObjectSL = new RestClient().getRestData(ApplicationConstants.END_POINT, querySL);
                    String jsonObjectLWP = new RestClient().getRestData(ApplicationConstants.END_POINT, queryLWP);

                    if (jsonObjectSL != null && !jsonObjectSL.isEmpty() && !jsonObjectSL.equals("[]")) {
                        JSONArray arraySL = new JSONArray(jsonObjectSL);
                        if (arraySL.length() > 0) {
                            JSONObject slObj = arraySL.getJSONObject(0);
                            SLDays = (Integer) slObj.get("0");
                        }
                    }

                    if (jsonObjectLWP != null && !jsonObjectLWP.isEmpty() && !jsonObjectLWP.equals("[]")) {
                        JSONArray arrayLWP = new JSONArray(jsonObjectLWP);
                        if (arrayLWP.length() > 0) {
                            {
                                for(int i=0;i<arrayLWP.length();i++){
                                JSONObject slObj = arrayLWP.getJSONObject(i);
                              if(!slObj.isNull("leaveWithoutPayDays")){
                                JSONArray lwparrayobject = slObj.getJSONArray("leaveWithoutPayDays");
                                 System.out.println(lwparrayobject.toString());
                                for (int lwpJsonCount = 0; lwpJsonCount < lwparrayobject.length(); lwpJsonCount++) {
                                        if ((datearrayList.contains(lwparrayobject.get(lwpJsonCount)))) {
                                            LWPDays++;
                                        }
                                    }
                                }
                                }
                            }
                        }
                    }

                    presentdays = totaldays - LWPDays + SLDays;
                    boolean onDeputaion = false;
                    if (emp.getOnDeputation().equalsIgnoreCase("Yes")) {
                        onDeputaion = true;
                    }

                    int previousYear = Integer.parseInt(year);
                    int previousMonth = Integer.parseInt(month);
                    if (previousMonth > 1) {
                        previousMonth = --previousMonth;
                    } else {
                        previousYear = --previousYear;
                        previousMonth = 12;
                    }

                    String EmpAttendanceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";

                    EmpAttendance empobj = new EmpAttendance();

                    String employeecode = "emp.employeeCode=\"" + emp.getEmployeeCode() + "\"";

                    String attendancePreviouesAdjQuery = "select emp._id as idStr,emp.isattendanceAdjFlag,emp.isSuspended,emp.stopSalary,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.totalDays,emp.present,emp.sickLeave,emp.leaveWithoutPay,emp.pbsP,emp.pasP,emp.sd,emp.remarks,emp.ddo from " + EmpAttendanceTable + ""
                            + " as emp where  emp.month= " + previousMonth + " and emp.year=" + previousYear + " and emp.status=\"Active\"  and  " + employeecode;

                    String EmpPreviousAttendanceOutput = aql.getRestData(ApplicationConstants.END_POINT, attendancePreviouesAdjQuery);

                    List<EmpAttendance> previousEmpList = null;
                    if (EmpPreviousAttendanceOutput != null && !EmpPreviousAttendanceOutput.isEmpty() && !EmpPreviousAttendanceOutput.equals("[]")) {
                        previousEmpList = new Gson().fromJson(EmpPreviousAttendanceOutput, new TypeToken< ArrayList<EmpAttendance>>() {
                        }.getType());
                    }

                    if (previousEmpList != null && previousEmpList.size() > 0) {
                        for (int k = 0; k < previousEmpList.size(); k++) {
                            if (previousEmpList.get(k).getSd()) {
                                empobj.setSd(true);
                            } else {
                                empobj.setSd(false);
                            }
                        }
                    } else {
                        empobj.setSd(false);
                    }

                    if (emp.getIsSuspended()) {
                        if (previousEmpList != null && previousEmpList.size() > 0) {
                            for (int i = 0; i < previousEmpList.size(); i++) {
                                int pasValue = previousEmpList.get(i).getPasP();
                                int pbsValue = previousEmpList.get(i).getPbsP();
                                if (pasValue != 0 || pbsValue != 0) {
                                    empobj.setPresent(0);
                                    empobj.setTotalDays(totaldays);
                                    empobj.setSickLeave(0);
                                    empobj.setLeaveWithoutPay(0);
                                    empobj.setPasP(totaldays);
                                    empobj.setPbsP(0);
                                    empobj.setIsattendanceAdjFlag(true);
                                }
                                if (pasValue == 0 && pbsValue == 0) {
                                    empobj.setPresent(0);
                                    empobj.setTotalDays(totaldays);
                                    empobj.setSickLeave(0);
                                    empobj.setLeaveWithoutPay(0);
                                    empobj.setPasP(0);
                                    empobj.setPbsP(0);
                                    empobj.setIsattendanceAdjFlag(false);
                                }
                            }
                        } else {
                            empobj.setPresent(presentdays);
                            empobj.setTotalDays(totaldays);
                            empobj.setSickLeave(SLDays);
                            empobj.setLeaveWithoutPay(LWPDays);
                            empobj.setPasP(0);
                            empobj.setPbsP(0);
                            empobj.setIsattendanceAdjFlag(false);

                        }
                    }
                    if (!emp.getIsSuspended()) {
                        empobj.setPresent(presentdays);
                        empobj.setTotalDays(totaldays);
                        empobj.setSickLeave(SLDays);
                        empobj.setLeaveWithoutPay(LWPDays);
                        empobj.setPasP(0);
                        empobj.setPbsP(0);
                        empobj.setIsattendanceAdjFlag(false);

                    }

                    empobj.setMonth(Integer.parseInt(month));
                    empobj.setYear(Integer.parseInt(year));

                    empobj.setEmployeeCode(emp.getEmployeeCode());
                    empobj.setEmployeeCodeM(emp.getEmployeeCodeM());
                    empobj.setLocation(emp.getLocation());
                    empobj.setDepartment(emp.getDepartment());
                    empobj.setDesignation(emp.getDesignation());
                    empobj.setSalaryType(emp.getSalaryType());
                    empobj.setStopSalary(emp.getStopSalary());
                    empobj.setStopSalaryDate(emp.getStopSalaryDate());
                    empobj.setEmployeeName(emp.getEmployeeName());
                    empobj.setDdo(emp.getDdo());
                    empobj.setNatureType(emp.getNatureType());
                    empobj.setPostingCity(emp.getPostingCity());
                    empobj.setPfType(emp.getPfType());
                    empobj.setFundType(emp.getFundType());
                    empobj.setBudgetHead(emp.getBudgetHead());
                    empobj.setLockStatus(false);

                    empobj.setIdStr(emp.getIdStr());
                    empobj.setIdStr(emp.getIdStr());
                    empobj.setOnDeputaion(onDeputaion);

                    empobj.setRemarks("");
                    empobj.setIsSuspended(emp.getIsSuspended());
                    empobj.setCreateDate(Long.toString(System.currentTimeMillis()));
                    empobj.setUpdateDate(Long.toString(System.currentTimeMillis()));
                    empobj.setAttendaceStatus(ApplicationConstants.PROCESSED);
                    empobj.setStatus(ApplicationConstants.ACTIVE);

                    DAO dao = DBManager.getDbConnection();
                    dao.insert(ApplicationConstants.EMP_ATTENDANCE_TABLE, new Gson().toJson(empobj));
//                    dao.close();
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * unprocess attendance() method will expect inputs then it will unprocess
     * the processed attendance & moved to unprocessed attendance table returns
     * the boolean.
     *
     * Ex:-unprocessAttendance({"123","321"},"primaryKey")
     *
     * @author mano
     * @param String[]
     * @param String
     * @return boolean
     * @throws java.lang.Exception
     */
    public static boolean unprocessAttendance(String processedIds, String processedBy) throws Exception {
        boolean result = false;

        String[] employeeCode = null;

        JSONArray arr = null;

        if (processedIds != null && !processedIds.isEmpty()) {
            arr = new JSONArray(processedIds);
            employeeCode = new String[arr.length()];
        }

        User user = new UserManager().fetch(processedBy);
        String userName = user.getFname() + " " + user.getLname();
        if (employeeCode.length > 0 && employeeCode != null) {
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                employeeCode[i] = (String) obj.get("idStr");
                employeeCode[i] = (String) obj.get("idStr");
            }

            for (String pid : employeeCode) {
                EmpAttendance asp = fetchProcessedAttendance(pid);
                asp.setAttendaceStatus("Unprocessed");
                asp.setUpdatedBy(userName);
                asp.setUpdateDate(Long.toString(System.currentTimeMillis()));

                DAO dao = DBManager.getDbConnection();
                String primaryKey = dao.insert(ApplicationConstants.EMP_ATTENDANCE_UNPROCESSTABLE, new Gson().toJson(asp));
                //System.out.println(primaryKey);
                if (primaryKey != null) {
                    result = dao.delete(ApplicationConstants.EMP_ATTENDANCE_TABLE, pid);
                }
                //System.out.println("result" + result);
//                dao.close();
            }
        }
        return result;
    }

    public static EmpAttendance fetchProcessedAttendance(String primaryKey) throws Exception {
        EmpAttendance emp = null;
        if (primaryKey == null) {
            return emp;
        }
        String dbOutput = DBManager.getDbConnection().fetch(ApplicationConstants.EMP_ATTENDANCE_TABLE, primaryKey);
        ArrayList<EmpAttendance> resultList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<EmpAttendance>>() {
        }.getType());
        if (resultList != null && resultList.size() > 0) {
            emp = resultList.get(0);
        }
        return emp;
    }

    public HashMap getSearchResultAttendaceAdj(String EmpAttendanceJsonString) throws Exception {

        if (EmpAttendanceJsonString == null) {
            return null;
        }
        RestClient aql = new RestClient();

        String EmpAttendanceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";
        HashMap outputMap = new HashMap();

        EmpAttendance empobj = new Gson().fromJson(EmpAttendanceJsonString, new TypeToken<EmpAttendance>() {
        }.getType());
        String employeeQuery = "";

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

        String ddoValue = "emp.ddo=\"" + empobj.getDdo() + "\"";
        String attendanceAdjQuery = "select emp._id as idStr,emp.isattendanceAdjFlag,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.totalDays,emp.present,emp.sickLeave,emp.leaveWithoutPay,emp.pbsP,emp.pasP,emp.sd,emp.remarks,emp.ddo,emp.isSuspended from " + EmpAttendanceTable + ""
                + " as emp where " + ddoValue + " and emp.month= " + empobj.getMonth() + " and emp.year=" + empobj.getYear() + " and emp.status=\"Active\"  and  emp.lockStatus=false" + employeeQuery;

//        String attendanceAdjQuery = "select emp._id as idStr,emp.employeeCode,emp.employeeName,emp.employeeCodeM,emp.status,emp.location,emp.salaryType,emp.department,emp.postingCity,emp.natureType,emp.designation,emp.fundType,emp.pfType,emp.budgetHead,emp.totalDays,emp.present,emp.sickLeave,emp.leaveWithoutPay,emp.pbsP,emp.pasP,emp.sd,emp.remarks,emp.ddo from " + EmpAttendanceTable + ""
//                + " as emp where " + ddoValue + " and emp.month= " + empobj.getMonth() + " and emp.year=" + empobj.getYear() + " and emp.lockStatus=false" + employeeQuery;
        //System.out.println("attendanceAdjQuery" + attendanceAdjQuery);
        String EmpAttendanceOutput = aql.getRestData(ApplicationConstants.END_POINT, attendanceAdjQuery);

        //System.out.println("EmpAttendanceOutput" + EmpAttendanceOutput);
        List<EmpAttendance> empList = null;
        if (EmpAttendanceOutput != null && !EmpAttendanceOutput.isEmpty() && !EmpAttendanceOutput.equals("[]")) {
            empList = new Gson().fromJson(EmpAttendanceOutput, new TypeToken< ArrayList<EmpAttendance>>() {
            }.getType());
        }

        if (empList != null && empList.size() > 0) {
            empList = getLocationforAttendance(empList);
        }
        if (empList != null && empList.size() > 0) {
            empList = getDDOforAttendance(empList);
        }
        if (empList != null && empList.size() > 0) {
            empList = getSalaryTypeforAttence(empList);
        }
        if (empList != null && empList.size() > 0) {
            empList = getDepartmentforAttendance(empList);
        }
        if (empList != null && empList.size() > 0) {
            empList = getDesignationforAttendance(empList);
        }

        if (empList != null && empList.size() > 0) {
            outputMap.put("attendanceadjview", empList);
        } else {
            outputMap.put("attendanceadjview", ApplicationConstants.NO_DATA_FOUND);
        }
        return outputMap;
    }

    public static boolean update(String EmpAttendanceJsonString) throws Exception {
        boolean result = false;
        //System.out.println(EmpAttendanceJsonString);

        JSONArray arr = new JSONArray(EmpAttendanceJsonString);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            EmpAttendance empobj = new Gson().fromJson(obj.toString(), new TypeToken<EmpAttendance>() {
            }.getType());
            String existattendanceJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMP_ATTENDANCE_TABLE, empobj.getIdStr());
            List<EmpAttendance> attendancelist = new Gson().fromJson(existattendanceJson, new TypeToken<List<EmpAttendance>>() {
            }.getType());
            if (attendancelist != null && attendancelist.size() > 0) {
                EmpAttendance attendance = attendancelist.get(0);
                attendance.setCreateDate(attendance.getCreateDate());
                attendance.setUpdateDate(System.currentTimeMillis() + "");
                attendance.setStatus(ApplicationConstants.ACTIVE);
                attendance.setPresent(empobj.getPresent());
                attendance.setSickLeave(empobj.getSickLeave());
                attendance.setLeaveWithoutPay(empobj.getLeaveWithoutPay());
                attendance.setPasP(empobj.getPasP());
                attendance.setPbsP(empobj.getPbsP());
                attendance.setSd(empobj.getSd());
                attendance.setRemarks(empobj.getRemarks());
//                attendance.setStopSalary(empobj.getStopSalary());
//                attendance.setStopSalaryDate(empobj.getStopSalaryDate());
                String attendanceJson = new Gson().toJson(attendance);
                result = DBManager.getDbConnection().update(ApplicationConstants.EMP_ATTENDANCE_TABLE, empobj.getIdStr(), attendanceJson);
            }

        }
        return result;
    }

    /**
     * @author chaitu
     * @description getEmployeeAttendance() method will get the employee
     * attendance record for particular month & year
     * @table empattendance
     * @conditions 1.)month=1 2.)year=2016 3.)empcode=123
     * @param String empCode
     * @param int month
     * @param int year
     * @return EmpAttendance
     * @throws Exception
     */
    public static EmpAttendance getEmployeeAttendance(String empCode, int month, int year) throws Exception {
        EmpAttendance result = null;
        if (empCode == null || month == 0 || year == 0) {
            return result;
        }

        String attendanceQuery = "select atten._id as idStr,atten.employeeCode,atten.totalDays,atten.present,atten.sickLeave,atten.leaveWithoutPay,atten.pbsP,atten.pasP,atten.sd,atten.remarks,atten.lockStatus,atten.month,atten.year,atten.attendaceStatus,atten.onDeputaion,atten.isSuspended,atten.createDate,atten.updateDate,atten.status,atten.mode,atten.type,atten.category,atten.createdBy,atten.updatedBy,emp.stopSalary,emp.stopSalaryDate"
                + " from " + attendanceTable + " as atten join " + empTable + " as emp on atten.employeeCode=emp.employeeCode"
                + " where atten.employeeCode=\"" + empCode + "\""
                + " and atten.year=" + year
                + " and atten.month=" + month;

        String attendanceQueryOutput = new RestClient().getRestData(ApplicationConstants.END_POINT, attendanceQuery);
        if (attendanceQueryOutput != null && !attendanceQueryOutput.isEmpty() && !attendanceQueryOutput.equals("[]")) {
            ArrayList<EmpAttendance> attenList = new Gson().fromJson(attendanceQueryOutput, new TypeToken<ArrayList<EmpAttendance>>() {
            }.getType());
            result = attenList.get(0);
        }
        return result;
    }

    /**
     * @author chaitu
     * @description update() method will update the attendance lock status flag
     * @table empattendance
     * @param String attendanceId
     * @param boolean lockStatus
     * @return boolean
     * @throws Exception
     */
    public static boolean update(String attendanceId, boolean lockStatus, String salaryProcessType) throws Exception {
        boolean isAttendanceUpdated = false;
        if (attendanceId == null) {
            return isAttendanceUpdated;
        }
        EmpAttendance empAtt = new EmpAttendanceManager().fetchProcessedAttendance(attendanceId);
        if (salaryProcessType != null && salaryProcessType.equalsIgnoreCase(ApplicationConstants.SALARY)) {
            empAtt.setLockStatus(lockStatus);
            empAtt.setSalaryProcessed(lockStatus);
        } else if (salaryProcessType != null && salaryProcessType.equalsIgnoreCase(ApplicationConstants.PAY_STOP_SALARY)) {
            empAtt.setPayStopSalaryProcessed(lockStatus);
        }
        empAtt.setUpdateDate(Long.toString(System.currentTimeMillis()));
        isAttendanceUpdated = DBManager.getDbConnection().update(ApplicationConstants.EMP_ATTENDANCE_TABLE, attendanceId, new Gson().toJson(empAtt));
        return isAttendanceUpdated;
    }

    public static long saveInMilliSecondAttendance(String str) throws ParseException {
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
