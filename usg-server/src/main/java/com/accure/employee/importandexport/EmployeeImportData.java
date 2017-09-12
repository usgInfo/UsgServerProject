/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.employee.importandexport;

import com.accure.budget.dto.FundType;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Discipline;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.Grade;
import com.accure.hrms.dto.MaritalStatus;
import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.Religion;
import com.accure.hrms.dto.SalaryHead;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class EmployeeImportData {

    public static String importDataToEmployee(File path) throws FileNotFoundException, IOException, InvalidFormatException, Exception {
        String result = null;
        try {
            FileInputStream inp = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            //System.out.println(rowCount);

            JSONObject json = new JSONObject();
            JSONArray rows = new JSONArray();;
            List<String> employeeIDList = new ArrayList<String>();
            XSSFRow row;
            XSSFRow Headerrow = (XSSFRow) sheet.getRow(0);
            int headerColCount=sheet.getRow(0).getLastCellNum();
            for (int i = 1; i <= rowCount; i++) {
                int colCount = sheet.getRow(i).getLastCellNum();
                JSONArray cells = new JSONArray();
                JSONObject jRow = new JSONObject();
                if(headerColCount!=colCount){
                    return null;
                }else{
                  row = (XSSFRow) sheet.getRow(i);
                  for (int j = 0; j < colCount; j++) {
                    XSSFCell cell = row.getCell(j);
                    String cellValue = "";
                    XSSFCell Headercell = Headerrow.getCell(j);
                    if (cell != null) {
                        int type = cell.getCellType();
                        switch (type) {
                            case Cell.CELL_TYPE_STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                cellValue = String.valueOf((int) cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_BLANK:
                               return null;
                            case Cell.CELL_TYPE_ERROR:
                            default:
                                return null;
                        }

                    }
                
                   
                    jRow.put(Headercell.getStringCellValue(), cellValue);
                    cells.put(jRow);
                }
                }
                rows.put(jRow);
                json.put("rows", rows);

                Type type = new TypeToken<Employee>() {
                }.getType();

                Employee employee = new Gson().fromJson(jRow.toString(), type);
                employee = getReligion(employee);
            
                employee = getMaritalStatus(employee);
                employee = getDDO(employee);
                employee = getDesignation(employee);
                employee = getSalaryType(employee);
                employee = getDiscipline(employee);
                employee = getDepartment(employee);
                employee = getNatureType(employee);
                employee = getFundType(employee);
                employee = getPostedDesignation(employee);
                employee = getGrade(employee);
                
               
                employee.setCreateDate(System.currentTimeMillis() + "");
                employee.setUpdateDate(System.currentTimeMillis() + "");
                employee.setStatus(ApplicationConstants.ACTIVE);
                
                if(employee==null){
                 return null;
                }
                
                String objJson = new Gson().toJson(employee);
                String objId = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_TABLE, objJson);
                 employee=null;
                if(objId!=null && objId!=""){
                employeeIDList.add(objId);
                 }
               
            }
            int count = employeeIDList.size();
            if (rowCount == count) {
               result = "success";
            } else {
                for (int i = 0; i < employeeIDList.size(); i++) {
                DBManager.getDbConnection().deleteDocument(ApplicationConstants.EMPLOYEE_TABLE, employeeIDList.get(i));
                }
                result = "failure";
            }
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static Employee getBudgetHead(Employee employee) throws Exception {
        int count=0;
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> list = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for(Iterator<BudgetHeadMaster> iterator = list.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }

        String cellValue = employee.getBudgetHead();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setBudgetHead(key);
                count++;
            }
           
        } if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getGrade(Employee employee) throws Exception {
        int count=0;
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GRADE_TABLE);
        List<Grade> list = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
        }.getType());
        for (Iterator<Grade> iterator = list.iterator(); iterator.hasNext();) {
            Grade next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getGradeName());
        }

        String cellValue = employee.getGrade();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setGrade(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getPostingCity(Employee employee) throws Exception {
        int count=0;
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CITY_TABLE);
        List< CityMaster> list = new Gson().fromJson(result, new TypeToken<List< CityMaster>>() {
        }.getType());
        for (Iterator< CityMaster> iterator = list.iterator(); iterator.hasNext();) {
            CityMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityName());
        }

        String cellValue = employee.getPostingCity();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setPostingCity(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getPostedDesignation(Employee employee) throws Exception {
        int count=0;
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        List< Designation> list = new Gson().fromJson(result, new TypeToken<List< Designation>>() {
        }.getType());
        for (Iterator< Designation> iterator = list.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }

        String cellValue = employee.getPostedDesignation();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setPostedDesignation(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getLocation(Employee employee) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        int count=0;
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<com.accure.finance.dto.DDO> list = new Gson().fromJson(result, new TypeToken<List< com.accure.finance.dto.DDO>>() {
        }.getType());
        for (Iterator< com.accure.finance.dto.DDO> iterator = list.iterator(); iterator.hasNext();) {
            com.accure.finance.dto.DDO next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocation());
        }

        String cellValue = employee.getLocation();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.equalsIgnoreCase(cellValue)) {
                employee.setLocation(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getFundType(Employee employee) throws Exception {
        int count=0;
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> list = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = list.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }

        String cellValue = employee.getFundType();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setFundType(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getNatureType(Employee employee) throws Exception {
        int count=0;
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.NATURE_TABLE);
        List<Nature> list = new Gson().fromJson(result, new TypeToken<List<Nature>>() {
        }.getType());
        for (Iterator<Nature> iterator = list.iterator(); iterator.hasNext();) {
            Nature next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getNatureName());
        }

        String cellValue = employee.getNatureType();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setNatureType(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getDepartment(Employee employee) throws Exception {
        int count=0;
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DEPARTMENT_TABLE);
        List<Department> DisciplineList = new Gson().fromJson(result, new TypeToken<List<Department>>() {
        }.getType());
        for (Iterator<Department> iterator = DisciplineList.iterator(); iterator.hasNext();) {
            Department next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDepartment());
        }

        String cellValue = employee.getDepartment();

        for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(cellValue)) {
                employee.setDepartment(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getDiscipline(Employee employee) throws Exception {
        int count=0;
        Map<String, String> DisciplineMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DISCIPLINE_TABLE);
        List<Discipline> DisciplineList = new Gson().fromJson(result, new TypeToken<List<Discipline>>() {
        }.getType());
        for (Iterator<Discipline> iterator = DisciplineList.iterator(); iterator.hasNext();) {
            Discipline next = iterator.next();
            DisciplineMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDisciplineName());
        }

        String discipline = employee.getDiscipline();

        for (Map.Entry<String, String> entry : DisciplineMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(discipline)) {
                employee.setDiscipline(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getReligion(Employee employee) throws Exception {
        int count=0;
        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.RELIGION_TABLE);
        List<Religion> religionList = new Gson().fromJson(result, new TypeToken<List<Religion>>() {
        }.getType());
        for (Iterator<Religion> iterator = religionList.iterator(); iterator.hasNext();) {
            Religion next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getReligion());
        }

        String religionId = employee.getReligion();

        for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(religionId)) {
                employee.setReligion(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getSalaryType(Employee employee) throws Exception {
        int count=0;
        Map<String, String> salaryTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> salaryTypeStatus = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = salaryTypeStatus.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            salaryTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        String Value = employee.getSalaryType();

        for (Map.Entry<String, String> entry : salaryTypeMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(Value)) {
                employee.setSalaryType(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getMaritalStatus(Employee employee) throws Exception {
        int count=0;
        Map<String, String> MaritalStatusMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.MARITAL_STATUS_TABLE);
        List<MaritalStatus> maritalStatus = new Gson().fromJson(result, new TypeToken<List<MaritalStatus>>() {
        }.getType());
        for (Iterator<MaritalStatus> iterator = maritalStatus.iterator(); iterator.hasNext();) {
            MaritalStatus next = iterator.next();
            MaritalStatusMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getMaritalStatus());
        }
        String maritalValue = employee.getMaritalStatus();
        for (Map.Entry<String, String> entry : MaritalStatusMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(maritalValue)) {
                employee.setMaritalStatus(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getDesignation(Employee employee) throws Exception {
        int count=0;
        Map<String, String> designationValueStatusMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        List<Designation> designationValueStatus = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        for (Iterator<Designation> iterator = designationValueStatus.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            designationValueStatusMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        String designationValue = employee.getDesignation();
        for (Map.Entry<String, String> entry : designationValueStatusMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value.equalsIgnoreCase(designationValue)) {
                employee.setDesignation(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static Employee getDDO(Employee employee) throws Exception {
        int count=0;
        Map<String, String> ddoStatusMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> ddoStatus = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = ddoStatus.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            ddoStatusMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        String ddoValue = employee.getDdo();

        for (Map.Entry<String, String> entry : ddoStatusMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.equalsIgnoreCase(ddoValue)) {
                employee.setDdo(key);
                count++;
            }
        }if(count==0){
                return null;
            }
        return employee;
    }

    public static void main(String[] args) throws IOException, FileNotFoundException, InvalidFormatException, Exception {
        File file = new File("D:\\sampleEmployeeSheet.xlsx");
       String msg= importDataToEmployee(file);
       //System.out.println(msg);
    }

}
