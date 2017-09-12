/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tautosalarylate file, choose Tools | Tautosalarylates
 * and open the tautosalarylate in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.Location;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author user
 */
public class SalarySlipRegisterReportManager {

    public HashMap getSearchResult(String EmpAttendanceJsonString) throws Exception {

        RestClient aql = new RestClient();

        HashMap outputMap = new HashMap();

        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";

        AutoSalaryProcess autosalaryprocessObj = new Gson().fromJson(EmpAttendanceJsonString, new TypeToken<AutoSalaryProcess>() {
        }.getType());

        String autoSalaryQuery = "";
        List<SalarySlipRegisterReport> salarySlipRegisterList = null;

        if (autosalaryprocessObj.getEmployeeCode() != null && autosalaryprocessObj.getEmployeeCode() != "" && !autosalaryprocessObj.getEmployeeCode().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeCode=\"" + autosalaryprocessObj.getEmployeeCode() + "\"";
        }
        if (autosalaryprocessObj.getEmployeeName() != null && autosalaryprocessObj.getEmployeeName() != "" && !autosalaryprocessObj.getEmployeeName().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeName=\"" + autosalaryprocessObj.getEmployeeName() + "\"";
        }
        if (autosalaryprocessObj.getEmployeeCodeM() != null && autosalaryprocessObj.getEmployeeCodeM() != "" && !autosalaryprocessObj.getEmployeeCodeM().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeCodeM=\"" + autosalaryprocessObj.getEmployeeCodeM() + "\"";
        }
        if (autosalaryprocessObj.getLocation() != null && !autosalaryprocessObj.getLocation().isEmpty() && !autosalaryprocessObj.getLocation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.location=\"" + autosalaryprocessObj.getLocation() + "\"";
        }
        if (autosalaryprocessObj.getDepartment() != null && !autosalaryprocessObj.getDepartment().isEmpty() && !autosalaryprocessObj.getDepartment().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.department=\"" + autosalaryprocessObj.getDepartment() + "\"";
        }
        if (autosalaryprocessObj.getDesignation() != null && !autosalaryprocessObj.getDesignation().isEmpty() && !autosalaryprocessObj.getDesignation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.designation=\"" + autosalaryprocessObj.getDesignation() + "\"";
        }
        if (autosalaryprocessObj.getNatureType() != null && autosalaryprocessObj.getNatureType().isEmpty() && !autosalaryprocessObj.getNatureType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.natureType=\"" + autosalaryprocessObj.getNatureType() + "\"";
        }
        if (autosalaryprocessObj.getPostingCity() != null && autosalaryprocessObj.getPostingCity().isEmpty() && !autosalaryprocessObj.getPostingCity().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.postingCity=\"" + autosalaryprocessObj.getPostingCity() + "\"";
        }
        if (autosalaryprocessObj.getPfType() != null && autosalaryprocessObj.getPfType().isEmpty() && !autosalaryprocessObj.getPfType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.pfType=\"" + autosalaryprocessObj.getPfType() + "\"";
        }
        if (autosalaryprocessObj.getFundType() != null && autosalaryprocessObj.getFundType().isEmpty() && !autosalaryprocessObj.getFundType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.fundType=\"" + autosalaryprocessObj.getFundType() + "\"";
        }
        if (autosalaryprocessObj.getBudgetHead() != null && autosalaryprocessObj.getBudgetHead().isEmpty() && !autosalaryprocessObj.getBudgetHead().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.budgetHead=\"" + autosalaryprocessObj.getBudgetHead() + "\"";
        }
        if (autosalaryprocessObj.getSalaryProcessType() != null && autosalaryprocessObj.getSalaryProcessType() != "" && !autosalaryprocessObj.getSalaryProcessType().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.salaryProcessType=\"" + autosalaryprocessObj.getSalaryProcessType() + "\"";
        }

        //System.out.println("autoSalaryQuery" + autoSalaryQuery);
        String autoSalaryProcessQuery = "select autosalary._id as idStr,autosalary.month,autosalary.salaryProcessType,autosalary.gradeName,autosalary.pfTypeName,autosalary.ddoName,autosalary.year,autosalary.department,autosalary.location,autosalary.employeeCode,autosalary.ddo,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                + " as autosalary where autosalary.month= " + autosalaryprocessObj.getMonth() + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + autosalaryprocessObj.getYear() + " and autosalary.ddo=\"" + autosalaryprocessObj.getDdo() + "\"" + autoSalaryQuery;

        //System.out.println("autoSalaryProcessQuery" + autoSalaryProcessQuery);
        String autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
        //System.out.println("autosalaryOutput" + autosalaryOutput);

        if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
            salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken< ArrayList<SalarySlipRegisterReport>>() {
            }.getType());

        }
        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            salarySlipRegisterList = getLocation(salarySlipRegisterList);
        }
        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            salarySlipRegisterList = getDepartment(salarySlipRegisterList);
        }
        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            salarySlipRegisterList = getSalaryType(salarySlipRegisterList);
        }
        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            salarySlipRegisterList = getDesignation(salarySlipRegisterList);
        }

        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {

            outputMap.put("salaryreportData", salarySlipRegisterList);
        } else {
            outputMap.put("salaryreportData", ApplicationConstants.NO_DATA_FOUND);

        }
        //System.out.println("\"outputMap\"" + outputMap);
        return outputMap;
    }

    public static List<SalarySlipRegisterReport> getDesignation(List<SalarySlipRegisterReport> employeeList) throws Exception {
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

    public static List<SalarySlipRegisterReport> getDepartment(List<SalarySlipRegisterReport> employeeList) throws Exception {
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

    public static List<SalarySlipRegisterReport> getLocation(List<SalarySlipRegisterReport> employeeList) throws Exception {
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

    public static List<SalarySlipRegisterReport> getSalaryType(List<SalarySlipRegisterReport> employeeList) throws Exception {
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

    public ByteArrayOutputStream generationPdfSalarySlip(String id, String ddo, String month, String year, String path, String financialyearstart, String financialyearEnd) throws DocumentException, FileNotFoundException, Exception {
        RestClient aql = new RestClient();
        List<SalarySlipRegisterReport> salarySlipRegisterList = null;
        ByteArrayOutputStream result = null;
        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
        int monthInt = 0;
        int yearInt = 0;
        //System.out.println("month" + month);
        if (month != null && month != "" && !month.isEmpty()) {
            monthInt = Integer.parseInt(month);
        }
        if (year != null && year != "" && !year.isEmpty()) {
            yearInt = Integer.parseInt(year);
        }
        //System.out.println("" + id);

        String autoSalaryProcessQuery = "select autosalary._id as idStr,autosalary.ddo,autosalary.salaryProcessType,autosalary.pfTypeName,autosalary.gradeName,autosalary.acnumber,autosalary.departmentName,autosalary.ddoName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions,autosalary.isArrear,autosalary.arrear from " + autoSalaryTable + ""
                + " as autosalary where autosalary._id= OID(\"" + id + "\") and autosalary.ddo=\"" + ddo + "\" and autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
        //System.out.println("autoSalaryProcessQuery" + autoSalaryProcessQuery);

        String autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
        //System.out.println("autosalaryOutput" + autosalaryOutput);

        if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
            salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
            }.getType());
        }

        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            salarySlipRegisterList = getDesignation(salarySlipRegisterList);
        }

        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            SalarySlipRportPDFGeneration salaryslipreport = new SalarySlipRportPDFGeneration();

            result = salaryslipreport.generateSalaryReport(salarySlipRegisterList, path, financialyearstart, financialyearEnd);

        }
        return result;
    }

    public ByteArrayOutputStream generationPdfDifferenceSalarySlip(String id, String ddo, String month, String year, String path, String fin, String processType) throws DocumentException, FileNotFoundException, Exception {
        RestClient aql = new RestClient();
        List<SalarySlipRegisterReport> salarySlipRegisterList = null;
        List<SalarySlipRegisterReport> preSalarySlipRegisterList = null;
        ByteArrayOutputStream result = null;
        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
        int monthInt = 0;
        int yearInt = 0;

        int currentmonth = 0;
        if (month != null && month != "" && !month.isEmpty()) {
            currentmonth = Integer.parseInt(month);
        }
        if (year != null && year != "" && !year.isEmpty()) {
            yearInt = Integer.parseInt(year);
        }

        
        String autoSalaryProcessQuery = null;

        autoSalaryProcessQuery = "select  autosalary._id as idStr,autosalary.salaryProcessType,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                + " as autosalary where autosalary.employeeCode= \"" + id + "\"  and autosalary.ddo=\"" + ddo + "\"  and  autosalary.month= " + currentmonth + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";

        if (currentmonth == 1) {
            monthInt = 12;
            yearInt = yearInt - 1;
        } else {
            monthInt = currentmonth - 1;
        }

        //System.out.println("autoSalaryProcessQuery" + autoSalaryProcessQuery);
        String autoSalaryPreviousProcessQuery = null;

        autoSalaryPreviousProcessQuery = "select  autosalary._id as idStr,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                + " as autosalary where autosalary.employeeCode= \"" + id + "\" and autosalary.ddo=\"" + ddo + "\" and  autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";

        //System.out.println("autoSalaryPreviousProcessQuery" + autoSalaryPreviousProcessQuery);
        String autosalaryOutput = null;
        String autopresalaryOutput = null;
        autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);

        autopresalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryPreviousProcessQuery);

        //System.out.println("autosalaryOutput" + autosalaryOutput);
        //System.out.println("autopresalaryOutput" + autopresalaryOutput);
        if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
            salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
            }.getType());
        }

        if (autopresalaryOutput != null && !autopresalaryOutput.isEmpty() && !autopresalaryOutput.equals("[]")) {
            preSalarySlipRegisterList = new Gson().fromJson(autopresalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
            }.getType());

        }
        if(preSalarySlipRegisterList != null && preSalarySlipRegisterList.size() > 0){
        if ((salarySlipRegisterList.size() == 2) && (preSalarySlipRegisterList.size() == 2)) {  
            autosalaryOutput = null;
            autopresalaryOutput = null;
            autoSalaryProcessQuery = "select  autosalary._id as idStr,autosalary.salaryProcessType,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\"  and autosalary.ddo=\"" + ddo + "\"  and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + currentmonth + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            processType = "paystopsalary";
            autoSalaryPreviousProcessQuery = "select  autosalary._id as idStr,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\" and autosalary.ddo=\"" + ddo + "\" and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
            autopresalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryPreviousProcessQuery);
            salarySlipRegisterList.clear();
            preSalarySlipRegisterList.clear();

            if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
                salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());
            }

            if (autopresalaryOutput != null && !autopresalaryOutput.isEmpty() && !autopresalaryOutput.equals("[]")) {
                preSalarySlipRegisterList = new Gson().fromJson(autopresalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());

            }

        } else if ((salarySlipRegisterList.size() == 1) && (preSalarySlipRegisterList.size() == 1)) {
            autosalaryOutput = null;
            autopresalaryOutput = null;
            processType = "salary";
            autoSalaryProcessQuery = "select  autosalary._id as idStr,autosalary.salaryProcessType,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\"  and autosalary.ddo=\"" + ddo + "\"  and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + currentmonth + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";

            autoSalaryPreviousProcessQuery = "select  autosalary._id as idStr,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\" and autosalary.ddo=\"" + ddo + "\" and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
            autopresalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryPreviousProcessQuery);
            salarySlipRegisterList.clear();
            preSalarySlipRegisterList.clear();

            if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
                salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());
            }

            if (autopresalaryOutput != null && !autopresalaryOutput.isEmpty() && !autopresalaryOutput.equals("[]")) {
                preSalarySlipRegisterList = new Gson().fromJson(autopresalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());

            }

        } else if ((salarySlipRegisterList.size() == 1) && (preSalarySlipRegisterList.size() == 2)) {
            autosalaryOutput = null;
            autopresalaryOutput = null;
            autoSalaryProcessQuery = "select  autosalary._id as idStr,autosalary.salaryProcessType,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\"  and autosalary.ddo=\"" + ddo + "\"  and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + currentmonth + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            processType = "paystopsalary";
            autoSalaryPreviousProcessQuery = "select  autosalary._id as idStr,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\" and autosalary.ddo=\"" + ddo + "\" and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
            autopresalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryPreviousProcessQuery);
            salarySlipRegisterList.clear();
            preSalarySlipRegisterList.clear();

            if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
                salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());
            }

            if (autopresalaryOutput != null && !autopresalaryOutput.isEmpty() && !autopresalaryOutput.equals("[]")) {
                preSalarySlipRegisterList = new Gson().fromJson(autopresalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());

            }

        } else if ((salarySlipRegisterList.size() == 2) && (preSalarySlipRegisterList.size() == 1)) {
            autosalaryOutput = null;
            autopresalaryOutput = null;

            autoSalaryProcessQuery = "select  autosalary._id as idStr,autosalary.salaryProcessType,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\"  and autosalary.ddo=\"" + ddo + "\"  and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + currentmonth + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";

            processType = "salary";
            autoSalaryPreviousProcessQuery = "select  autosalary._id as idStr,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\" and autosalary.ddo=\"" + ddo + "\" and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
            autopresalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryPreviousProcessQuery);
            salarySlipRegisterList.clear();
            preSalarySlipRegisterList.clear();

            if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
                salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());
            }

            if (autopresalaryOutput != null && !autopresalaryOutput.isEmpty() && !autopresalaryOutput.equals("[]")) {
                preSalarySlipRegisterList = new Gson().fromJson(autopresalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());

            }

        } 
        }else {
            autosalaryOutput = null;
            autoSalaryProcessQuery = "select  autosalary._id as idStr,autosalary.salaryProcessType,autosalary.fundTypeName,autosalary.departmentName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions from " + autoSalaryTable + ""
                    + " as autosalary where autosalary.employeeCode= \"" + id + "\"  and autosalary.ddo=\"" + ddo + "\"  and autosalary.salaryProcessType=\"" + processType + "\"  and  autosalary.month= " + currentmonth + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";
            autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
           
          if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
                salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                }.getType());
            }
        }

        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            salarySlipRegisterList = getDesignation(salarySlipRegisterList);
        }

        if (preSalarySlipRegisterList != null && preSalarySlipRegisterList.size() > 0) {
            preSalarySlipRegisterList = getDesignation(preSalarySlipRegisterList);
        }
        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {

            result = SalaryDifferenceReport.generateSalaryReport(preSalarySlipRegisterList, salarySlipRegisterList, month, year, path, fin);

        }
        return result;
    }

    public ByteArrayOutputStream getSearchResult(String EmpAttendanceJsonString, String month, String year, String reporttype, String path, String fin) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<SalarySlipRegisterReport> salarySlipRegisterList = null;

        RestClient aql = new RestClient();

        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";

        SalarySlipRegisterReport autosalaryObj = new Gson().fromJson(EmpAttendanceJsonString, new TypeToken<SalarySlipRegisterReport>() {
        }.getType());
        String autoSalaryQuery = "";
        if (autosalaryObj.getEmployeeCode() != null && autosalaryObj.getEmployeeCode() != "" && !autosalaryObj.getEmployeeCode().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeCode=\"" + autosalaryObj.getEmployeeCode() + "\"";
        }
        if (autosalaryObj.getEmployeeName() != null && autosalaryObj.getEmployeeName() != "" && !autosalaryObj.getEmployeeName().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeName=\"" + autosalaryObj.getEmployeeName() + "\"";
        }
        if (autosalaryObj.getEmployeeCodeM() != null && autosalaryObj.getEmployeeCodeM() != "" && !autosalaryObj.getEmployeeCodeM().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.employeeCodeM=\"" + autosalaryObj.getEmployeeCodeM() + "\"";
        }
        if (autosalaryObj.getLocation() != null && !autosalaryObj.getLocation().isEmpty() && !autosalaryObj.getLocation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.location=\"" + autosalaryObj.getLocation() + "\"";
        }
        if (autosalaryObj.getDepartment() != null && !autosalaryObj.getDepartment().isEmpty() && !autosalaryObj.getDepartment().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.department=\"" + autosalaryObj.getDepartment() + "\"";
        }
        if (autosalaryObj.getDesignation() != null && !autosalaryObj.getDesignation().isEmpty() && !autosalaryObj.getDesignation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.designation=\"" + autosalaryObj.getDesignation() + "\"";
        }
        if (autosalaryObj.getNatureType() != null && autosalaryObj.getNatureType().isEmpty() && !autosalaryObj.getNatureType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.natureType=\"" + autosalaryObj.getNatureType() + "\"";
        }
        if (autosalaryObj.getPostingCity() != null && autosalaryObj.getPostingCity().isEmpty() && !autosalaryObj.getPostingCity().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.postingCity=\"" + autosalaryObj.getPostingCity() + "\"";
        }
        if (autosalaryObj.getPfType() != null && autosalaryObj.getPfType().isEmpty() && !autosalaryObj.getPfType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.pfType=\"" + autosalaryObj.getPfType() + "\"";
        }
        if (autosalaryObj.getFundType() != null && autosalaryObj.getFundType().isEmpty() && !autosalaryObj.getFundType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.fundType=\"" + autosalaryObj.getFundType() + "\"";
        }
        if (autosalaryObj.getBudgetHead() != null && autosalaryObj.getBudgetHead().isEmpty() && !autosalaryObj.getBudgetHead().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.budgetHead=\"" + autosalaryObj.getBudgetHead() + "\"";
        }
        if (autosalaryObj.getSalaryProcessType() != null && autosalaryObj.getSalaryProcessType() != "" && !autosalaryObj.getSalaryProcessType().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autosalary.salaryProcessType=\"" + autosalaryObj.getSalaryProcessType() + "\"";
        }

        String autoSalaryProcessQuery = "select * from " + autoSalaryTable + ""
                + " as autosalary where autosalary.status=\"Processed\" and  autosalary.month=" + month + " and autosalary.year=" + year + " and ddo=\"" + autosalaryObj.getDdo() + "\"" + autoSalaryQuery;

        //System.out.println("" + autoSalaryProcessQuery);
        String autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
        //System.out.println("ouput" + autosalaryOutput);

        if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
            salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken< ArrayList<SalarySlipRegisterReport>>() {
            }.getType());

        }
        if (reporttype.equalsIgnoreCase("MonthlySalaryRegister")) {
            MontlySalaryRegisterReport msr = new MontlySalaryRegisterReport();
            bos = msr.generateSalaryRegisterReport(salarySlipRegisterList, path, Integer.parseInt(month), Integer.parseInt(year), fin);
        }
        if (reporttype.equalsIgnoreCase("SalaryEarningRegister")) {
            SalaryEarningRegisterPDFGeneration msr = new SalaryEarningRegisterPDFGeneration();
            bos = msr.generateSalaryRegisterReport(salarySlipRegisterList, path, Integer.parseInt(month), Integer.parseInt(year), fin);
        }

        return bos;
    }

    public List<SalaryHead> getAllEarningHeads() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", ApplicationConstants.ACTIVE);
        map.put("active", "Yes");
        map.put("headType", "Earnings");
        map.put("showOnRegister", "Yes");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, map);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return religionList;
    }

    public List<SalaryHead> getAllDeductionHeads() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", ApplicationConstants.ACTIVE);
        map.put("active", "Yes");
        map.put("headType", "Deductions");
        map.put("showOnRegister", "Yes");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, map);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return religionList;
    }

}
