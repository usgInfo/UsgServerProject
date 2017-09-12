/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Asif
 */
package com.accure.hrms.manager;

import com.accure.usg.common.dto.FileHolder;
import com.accure.hrms.dto.Association;
import com.accure.hrms.dto.Salutation;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.dto.Grade;
import com.accure.hrms.dto.Quarter;
import com.accure.accure.db.MongoFile;
import com.accure.accure.db.Query;
import com.accure.budget.dto.FundType;
import com.accure.common.delete.DeleteDependencyManager;
import com.accure.hrms.dto.Bank;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.finance.manager.DDOManager;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Discipline;
import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeeLeftStatus;
import com.accure.hrms.dto.MaritalStatus;
import com.accure.hrms.dto.PFType;
import com.accure.hrms.dto.Religion;
import com.accure.hrms.dto.Class;
import com.accure.hrms.dto.*;
import com.accure.leave.dto.EmployeeLeaveAssignment;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.dto.LeaveTypeDetails;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.leave.manager.LeaveTypeManager;
import com.accure.user.dto.DdoLocationMap;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Asif
 */
public class EmployeeManager {

    private static final String BASED_ON_DDO = "DDO";
    private final static String BASED_ON_CITY_CITYCATEGORY = "City-CityCategory";
    private static final String BASED_ON_SALARY_TYPE = "Salary Type";
    private static final String BASED_ON_CLASS = "Class";
    private static final String BASED_ON_NATURE_TYPE = "Nature Type";
    private static final String BASED_ON_DDO_CITY_CITYCATEGORY = "DDO-City-CityCategory";
    private static final String BASED_ON_DDO_SALARY_TYPE = "DDO-Salary Type";
    private static final String BASED_ON_DDO_CLASS = "DDO-Class";
    private static final String BASED_ON_DDO_NATURE_TYPE = "DDO-Nature Type";
    private static final String BASED_ON_City_CityCategory_Salary_Type = "City-CityCategory-Salary Type";
    private static final String BASED_ON_CITY_CITYCATEGORY_CLASS = "City-CityCategory-Class";
    private static final String BASED_ON_CITY_CITYCATEGORY_NATURE_TYPE = "City-CityCategory-Nature Type";
    private static final String BASED_ON_SALARY_TYPE_CLASS = "Salary Type-Class";
    private static final String BASED_ON_SALARY_TYPE_NATURE_TYPE = "Salary Type-Nature Type";
    private static final String BASED_ON_CLASS_NATURE_TYPE = "Class-Nature Type";
    private static final String BASED_ON_DDO_CITY_CITYCATEGORY_SALARY_TYPE = "DDO-City-CityCategory-Salary Type";
    private static final String BASED_ON_DDO_CITY_CITYCATEGORY_CLASS = "DDO-City-CityCategory-Class";
    private static final String BASED_ON_DDO_CITY_CITYCATEGORY_NATURE_TYPE = "DDO-City-CityCategory-Nature Type";
    private static final String BASED_ON_CITY_CITYCATEGORY_SALARY_TYPE_CLASS = "City-CityCategory-Salary Type-Class";
    private static final String BASED_ON_CITY_CITYCATEGORY_SALARY_TYPE_NATURE_TYPE = "City-CityCategory-Salary Type-Nature Type";
    private static final String BASED_ON_CITY_CITYCATEGORY_SALARY_TYPE_CLASS_NATURE_TYPE = "City-CityCategory-Salary Type-Class-Nature Type";

    public String save(Employee employee, String userId) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String employeeId = null;
        String statusMessage = "success";
        String employeeCodeStatus = "NotUpdated";
        String employeeCodeUpdate = "";

        HashMap<String, String> emailMap = new HashMap<String, String>();
        emailMap.put("status", ApplicationConstants.ACTIVE);
        emailMap.put("email", employee.getEmail());
        String emailJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, emailMap);
        if (emailJson == null) {
            HashMap<String, String> panMap = new HashMap<String, String>();
            panMap.put("status", ApplicationConstants.ACTIVE);
            panMap.put("panNo", employee.getPanNo());
            String panJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, panMap);
            if (panJson == null) {

                if (postCheckingMethod(employee)) {
                    if (employee.getIncrementDueDate() != null && !employee.getIncrementDueDate().isEmpty()) {
                        employee.setIncrementDueDateInMilliSecond(saveInMilliSecond(employee.getIncrementDueDate()));
                    }
                    if (employee.getDob() != null && !employee.getDob().isEmpty()) {
                        employee.setDobInMillisecond(saveInMilliSecond(employee.getDob()));
                    }
                    if (employee.getDateOfJoining() != null && !employee.getDateOfJoining().isEmpty()) {
                        employee.setDateOfJoiningInMillisecond(saveInMilliSecond(employee.getDateOfJoining()));
                    }
                    if (employee.getDateOfRetirement() != null && !employee.getDateOfRetirement().isEmpty()) {
                        employee.setDateOfRetirementInMillisecond(saveInMilliSecond(employee.getDateOfRetirement()));
                    }
                    if (employee.getDateOfAppointment() != null && !employee.getDateOfAppointment().isEmpty()) {
                        employee.setDateOfAppointmentInMillisecond(saveInMilliSecond(employee.getDateOfAppointment()));
                    }
                    String employeeCode = returnValidEmployeeCode(employee.getEmployeeCode());
                    if (!employeeCode.equalsIgnoreCase(employee.getEmployeeCode())) {
                        employee.setEmployeeCode(employeeCode);
                        employeeCodeStatus = ApplicationConstants.EMPLOYEE_CODE_IS_UPDATED;
                        employeeCodeUpdate = employeeCode;
                        resultMap.put("employeeCodeStatus", employeeCodeStatus);
                        resultMap.put("employeeCodeUpdate", employeeCodeUpdate);
                    }
                    employee.setCreateDate(System.currentTimeMillis() + "");
                    employee.setAssociationDate(System.currentTimeMillis());
                    employee.setStopSalaryDate(System.currentTimeMillis());
                    employee.setStopGPFDate(System.currentTimeMillis());
                    employee.setPtApplicableDate(System.currentTimeMillis());
                    employee.setCreatedBy(userName);
                    employee.setIsSuspended(false);
                    employee.setStatus(ApplicationConstants.ACTIVE);
                    String employeeJson = new Gson().toJson(employee);
                    employeeId = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_TABLE, employeeJson);
                    if (employeeId != null) {
                        employeeHistory(employee, employeeId);
                        resultMap.put("statusMessage", statusMessage);
                    }
                } else {
                    resultMap.put("statusMessage", ApplicationConstants.NO_POST_AVAILABLE);
                    resultMap.put("employeeCodeStatus", employeeCodeStatus);
                    return new Gson().toJson(resultMap);
                }

            } else {
                resultMap.put("statusMessage", "PAN already Existed");
                return new Gson().toJson(resultMap);
            }
        } else {
            resultMap.put("statusMessage", "Email already Existed");
            return new Gson().toJson(resultMap);
        }

        return new Gson().toJson(resultMap);
    }

    /**
     * @author chaitu
     * @description getEmployee() method will get the employee based on empCode
     * @table employee
     * @conditions 1.)employeeCode=123
     * @param String empCode
     * @return Employee
     * @throws Exception
     */
    public Employee getEmployee(String empCode) throws Exception {
        Employee emp = null;
        if (empCode == null) {
            return emp;
        }
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put(ApplicationConstants.EMPLOYEE_CODE, empCode);
        ArrayList<Employee> list = fetch(condition);
        if (list.size() > 0) {
            emp = list.get(0);
        }
        return emp;
    }

    public String fetch(String employeeId) throws Exception {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, employeeId);
        List<Employee> employeeList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        if (employeeList == null || employeeList.size() < 1) {
            return null;
        }
        return new Gson().toJson(employeeList.get(0));
    }

    public ArrayList<Employee> fetch(HashMap<String, String> conditionMap) throws Exception {
        if (conditionMap == null || conditionMap.isEmpty()) {
            return null;
        }

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        if (result != null) {
            ArrayList<Employee> employeeList = new Gson().fromJson(result, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            return employeeList;
        }
        return null;
    }

    public String fetchByEmployeeCode(String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", employeeCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(empList.get(0));
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
            employeeList = getPostingDDO(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getAssociation(employeeList);
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
            employeeList = getPostedDesignation(employeeList);

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
            employeeList = getEmployeeLeftStatus(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getHeadSlab(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getReportingTo(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getLocation(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getClass(employeeList);
        } catch (Exception e) {
        }
        return new Gson().toJson(employeeList);
    }

    public boolean delete(String employeeId, String userId) throws Exception {
        boolean result;
        if (employeeId == null || employeeId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<Employee>() {
        }.getType();
        String employee = new EmployeeManager().fetch(employeeId);
        if (employee == null || employee.isEmpty()) {
            return false;
        }

        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMP_ATTENDANCE_TABLE, "idStr", employeeId) || DeleteDependencyManager.hasDependency(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, "employeePKey", employeeId)) {
            return false;
        } else {
            Employee employeerJson = new Gson().fromJson(employee, type);
            employeerJson.setStatus(ApplicationConstants.INACTIVE);
            result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, employeeId, new Gson().toJson(employeerJson));
            if (result == true) {
                employeeHistory(employeerJson, employeeId);
            }

        }

        return result;
    }

    public String update(Employee newEmployee, String employeeId, String userId) throws Exception {
        boolean email = false;
        boolean pan = false;
        Employee previousEmp = new Gson().fromJson(fetch(employeeId), new TypeToken<Employee>() {
        }.getType());
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

//        previousEmp.setEmployeeCodeM(newEmployee.getEmployeeCodeM());
        previousEmp.setAcnumber(newEmployee.getAcnumber());
        previousEmp.setAssociation(newEmployee.getAssociation());
        if (previousEmp.getAssociation() != null && !previousEmp.getAssociation().isEmpty()) {
            if (!previousEmp.getAssociation().equalsIgnoreCase(newEmployee.getAssociation())) {
                previousEmp.setAssociation(newEmployee.getAssociation());
                previousEmp.setAssociationDate(System.currentTimeMillis());
            }
        } else if (newEmployee.getAssociation() != null && !newEmployee.getAssociation().isEmpty()) {
            previousEmp.setAssociation(newEmployee.getAssociation());
            previousEmp.setAssociationDate(System.currentTimeMillis());
        }

        HashMap<String, String> emailMap = new HashMap<String, String>();
        emailMap.put("status", ApplicationConstants.ACTIVE);
        emailMap.put("email", newEmployee.getEmail());
        String emailJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, emailMap);
        List<Employee> emailList = new Gson().fromJson(emailJson, new TypeToken<List<Employee>>() {
        }.getType());
        //      List<Employee> employeeList = new ArrayList<Employee>();
        if (emailList != null) {
            for (Employee emp : emailList) {
                String str = ((Map<String, String>) emp.getId()).get("$oid");
                //System.out.println("---" + str);
                if (str.equalsIgnoreCase(employeeId)) {
                    email = true;
                }
            }
        }

        if (email == true || emailList == null) {

            HashMap<String, String> panMap = new HashMap<String, String>();
            panMap.put("status", ApplicationConstants.ACTIVE);
            panMap.put("panNo", newEmployee.getPanNo());
            String panJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, panMap);

            List<Employee> panList = new Gson().fromJson(panJson, new TypeToken<List<Employee>>() {
            }.getType());
            //      List<Employee> employeeList = new ArrayList<Employee>();
            if (panList != null) {
                for (Employee emppan : panList) {
                    String panstr = ((Map<String, String>) emppan.getId()).get("$oid");
                    if (panstr.equalsIgnoreCase(employeeId)) {
                        pan = true;
                    }
                }
            }

            if (pan == true || panList == null) {

                boolean isAvailable = isPostAreAvailbleWhileUpdating(previousEmp, newEmployee);
                if (isAvailable == true) {
                    previousEmp.setAuditNumber(newEmployee.getAuditNumber());
                    previousEmp.setBank(newEmployee.getBank());
                    previousEmp.setBasic(newEmployee.getBasic());
                    previousEmp.setBudgetHead(newEmployee.getBudgetHead());
                    previousEmp.setClassMaster(newEmployee.getClassMaster());
                    previousEmp.setCategory(newEmployee.getCategory());
                    previousEmp.setDateOfAppointment(newEmployee.getDateOfAppointment());
                    previousEmp.setDateOfAppointment(newEmployee.getDateOfAppointment());
                    previousEmp.setDateOfRetirement(newEmployee.getDateOfRetirement());
                    previousEmp.setDateOfJoining(newEmployee.getDateOfJoining());
                    if (newEmployee.getDateOfJoining() != null && !newEmployee.getDateOfJoining().isEmpty()) {
                        previousEmp.setDateOfJoiningInMillisecond(saveInMilliSecond(newEmployee.getDateOfJoining()));
                    } else {
                        previousEmp.setDateOfJoiningInMillisecond(0);
                    }
                    previousEmp.setDeductionHeads(newEmployee.getDeductionHeads());
                    previousEmp.setTotalEarnings(newEmployee.getTotalEarnings());
                    previousEmp.setTotalDeductions(newEmployee.getTotalDeductions());
                    previousEmp.setDepartment(newEmployee.getDepartment());
                    previousEmp.setDesignation(newEmployee.getDesignation());
                    previousEmp.setDiscipline(newEmployee.getDiscipline());
                    previousEmp.setDob(newEmployee.getDob());
                    if (newEmployee.getDob() != null && !newEmployee.getDob().isEmpty()) {
                        previousEmp.setDobInMillisecond(saveInMilliSecond(newEmployee.getDob()));
                    } else {
                        previousEmp.setDobInMillisecond(0);
                    }
                    if (newEmployee.getDateOfRetirement() != null && !newEmployee.getDateOfRetirement().isEmpty()) {
                        previousEmp.setDateOfRetirementInMillisecond(saveInMilliSecond(newEmployee.getDateOfRetirement()));
                    } else {
                        previousEmp.setDateOfRetirementInMillisecond(0);
                    }
                    if (newEmployee.getDateOfAppointment() != null && !newEmployee.getDateOfAppointment().isEmpty()) {
                        previousEmp.setDateOfAppointmentInMillisecond(saveInMilliSecond(newEmployee.getDateOfAppointment()));
                    } else {
                        previousEmp.setDateOfAppointmentInMillisecond(0);
                    }
                    previousEmp.setEmail(newEmployee.getEmail());
//        previousEmp.setEmployeeCode(newEmployee.getEmployeeCode());
                    previousEmp.setEmployeeCodeM(newEmployee.getEmployeeCodeM());
                    previousEmp.setEmployeeLeftReason(newEmployee.getEmployeeLeftReason());
                    previousEmp.setEmployeeLeftStatus(newEmployee.getEmployeeLeftStatus());
                    previousEmp.setEmployeeName(newEmployee.getEmployeeName());
                    previousEmp.setEarningHeads(newEmployee.getEarningHeads());
                    previousEmp.setFatherName(newEmployee.getFatherName());
                    previousEmp.setFromDDO(newEmployee.getFromDDO());
                    previousEmp.setFundType(newEmployee.getFundType());
                    previousEmp.setGender(newEmployee.getGender());
                    previousEmp.setGrade(newEmployee.getGrade());
                    previousEmp.setGradePay(newEmployee.getGradePay());
                    previousEmp.setHeadSlab(newEmployee.getHeadSlab());
                    previousEmp.setIncrementDueDate(newEmployee.getIncrementDueDate());
                    if (newEmployee.getIncrementDueDate() != null && !newEmployee.getIncrementDueDate().isEmpty()) {
                        previousEmp.setIncrementDueDateInMilliSecond(saveInMilliSecond(newEmployee.getIncrementDueDate()));
                    } else {
                        previousEmp.setIncrementDueDateInMilliSecond(0);
                    }
                    previousEmp.setIncrementPercentage(newEmployee.getIncrementPercentage());
                    previousEmp.setIsGazetted(newEmployee.getIsGazetted());
                    previousEmp.setIsHandicapped(newEmployee.getIsHandicapped());
                    previousEmp.setIsPgTeacher(newEmployee.getIsPgTeacher());
                    previousEmp.setImageAvailable(newEmployee.isImageAvailable());
                    previousEmp.setLastAppointmentDate(newEmployee.getLastAppointmentDate());
                    previousEmp.setLastJoiningDate(newEmployee.getLastJoiningDate());
                    previousEmp.setLeavingDate(newEmployee.getLeavingDate());
                    previousEmp.setLeavingRemarks(newEmployee.getLeavingRemarks());
                    previousEmp.setLocation(newEmployee.getLocation());
                    previousEmp.setMaritalStatus(newEmployee.getMaritalStatus());
                    previousEmp.setNatureType(newEmployee.getNatureType());
                    previousEmp.setOnDeputation(newEmployee.getOnDeputation());
                    previousEmp.setPanNo(newEmployee.getPanNo());
                    previousEmp.setPayMode(newEmployee.getPayMode());
                    previousEmp.setPersonalFileNo(newEmployee.getPersonalFileNo());
                    previousEmp.setPfBalance(newEmployee.getPfBalance());
                    previousEmp.setPfBank(newEmployee.getPfBank());
                    previousEmp.setPfNumber(newEmployee.getPfNumber());
                    previousEmp.setPfType(newEmployee.getPfType());
                    previousEmp.setPgCode(newEmployee.getPgCode());
                    previousEmp.setPostedDesignation(newEmployee.getPostedDesignation());
                    previousEmp.setPostingCity(newEmployee.getPostingCity());
                    previousEmp.setPostingDDO(newEmployee.getPostingDDO());
                    if (previousEmp.getPtApplicable() != null && !previousEmp.getPtApplicable().isEmpty()) {
                        if (!previousEmp.getPtApplicable().equalsIgnoreCase(newEmployee.getPtApplicable())) {
                            previousEmp.setPtApplicable(newEmployee.getPtApplicable());
                            previousEmp.setPtApplicableDate(Long.parseLong(System.currentTimeMillis() + ""));
                        }
                    } else if (newEmployee.getPtApplicable() != null && !newEmployee.getPtApplicable().isEmpty()) {
                        previousEmp.setPtApplicable(newEmployee.getPtApplicable());
                        previousEmp.setPtApplicableDate(Long.parseLong(System.currentTimeMillis() + ""));
                    }
                    previousEmp.setQuarterNo(newEmployee.getQuarterNo());
                    previousEmp.setReligion(newEmployee.getReligion());
                    previousEmp.setRemarks(newEmployee.getRemarks());
                    previousEmp.setReportingTo(newEmployee.getReportingTo());
                    previousEmp.setSalaryBillType(newEmployee.getSalaryBillType());
                    previousEmp.setSalaryType(newEmployee.getSalaryType());
                    previousEmp.setSalutationOption(newEmployee.getSalutationOption());
                    if (previousEmp.getStopGPF() != null && !previousEmp.getStopGPF().isEmpty()) {
                        if (!previousEmp.getStopGPF().equalsIgnoreCase(newEmployee.getStopGPF())) {
                            previousEmp.setStopGPF(newEmployee.getStopGPF());
                            previousEmp.setStopGPFDate(Long.parseLong(System.currentTimeMillis() + ""));
                        }
                    } else if (newEmployee.getStopGPF() != null && !newEmployee.getStopGPF().isEmpty()) {
                        previousEmp.setStopGPF(newEmployee.getStopGPF());
                        previousEmp.setStopGPFDate(Long.parseLong(System.currentTimeMillis() + ""));
                    }
                    if (previousEmp.getStopSalary() != null && !previousEmp.getStopSalary().isEmpty()) {
                        if (!previousEmp.getStopSalary().equalsIgnoreCase(newEmployee.getStopSalary())) {
                            previousEmp.setStopSalary(newEmployee.getStopSalary());
                            previousEmp.setStopSalaryDate(Long.parseLong(System.currentTimeMillis() + ""));
                        }
                    } else if (newEmployee.getStopSalary() != null && !newEmployee.getStopSalary().isEmpty()) {
                        previousEmp.setStopSalary(newEmployee.getStopSalary());
                        previousEmp.setStopSalaryDate(Long.parseLong(System.currentTimeMillis() + ""));
                    }
                    previousEmp.setUpdateDate(System.currentTimeMillis() + "");
                    previousEmp.setUpdatedBy(userName);

                    previousEmp.setWelfareNo(newEmployee.getWelfareNo());
                    previousEmp.setWorkDetails(newEmployee.getWorkDetails());

                    boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, employeeId, new Gson().toJson(previousEmp));
                    if (result) {
                        employeeHistory(previousEmp, employeeId);
                    }
                    return ApplicationConstants.UPDATED;
                } else {
                    return ApplicationConstants.NO_POST_AVAILABLE;
                }
            } else {
                return ApplicationConstants.PAN_AVAILABLE;
            }
        } else {
            return ApplicationConstants.EMAIL_AVAILABLE;
        }
    }

    public String employeeHistory(Employee emp, String id) throws Exception {
        emp.setpKey(id);
        emp.setId(null);
        String str = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_HISTORY_TABLE, new Gson().toJson(emp));
        return str;
    }

    public String storeFile(FileItem item, String resultId) throws Exception {

        if (item == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ownerID", resultId);
        String json = DBManager.getDbConnection().listAllFilesByCondition("employeeImage", conditionMap);

        InputStream filecontent = item.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(filecontent);
        MongoFile mf = new MongoFile();
        FileHolder fileholder = new FileHolder();
        fileholder.setFileName(item.getName());
        fileholder.setOwnerID(resultId);
        fileholder.setFileExt(FilenameUtils.getExtension(item.getName()));
        fileholder.setDocType("employeeImage");
        fileholder.setFolderName("employeeImage");
        fileholder.setSize(item.getSize() + "");
        fileholder.setInputStream(filecontent);
        fileholder.setBaos(baos);
        fileholder.setMimeType(URLConnection.guessContentTypeFromName(item.getName()));
        fileholder.setStatus("Active");

        String id = DBManager.getDbConnection().storeBinaryFile(fileholder);
        return id;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> employeeList = new Gson().fromJson(result1, new TypeToken<List<Employee>>() {
        }.getType());
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
            employeeList = getPostingDDO(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getAssociation(employeeList);
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
            employeeList = getPostedDesignation(employeeList);

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
            employeeList = getEmployeeLeftStatus(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getHeadSlab(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getReportingTo(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getLocation(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getClass(employeeList);
        } catch (Exception e) {
        }
        String finalresult = new Gson().toJson(employeeList);
        //System.out.println(employeeList);
        return finalresult;
    }

    public static List<Employee> getReligion(List<Employee> employeeList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.RELIGION_TABLE);
        List<Religion> religionList = new Gson().fromJson(result, new TypeToken<List<Religion>>() {
        }.getType());
        for (Iterator<Religion> iterator = religionList.iterator(); iterator.hasNext();) {
            Religion next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getReligion());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            String religionId = employeeList.get(i).getReligion();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(religionId)) {
                    employeeList.get(i).setReligion(value);
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

    public static List<Employee> getMaritalStatus(List<Employee> employeeList) throws Exception {
        Map<String, String> MaritalStatusMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.MARITAL_STATUS_TABLE);
        List<MaritalStatus> religionList = new Gson().fromJson(result, new TypeToken<List<MaritalStatus>>() {
        }.getType());
        for (Iterator<MaritalStatus> iterator = religionList.iterator(); iterator.hasNext();) {
            MaritalStatus next = iterator.next();
            MaritalStatusMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getMaritalStatus());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : MaritalStatusMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getMaritalStatus())) {
                    employeeList.get(i).setMaritalStatus(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<Employee> getBank(List<Employee> employeeList) throws Exception {
        Map<String, String> BankMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BANK_TABLE);
        List<Bank> religionList = new Gson().fromJson(result, new TypeToken<List<Bank>>() {
        }.getType());
        for (Iterator<Bank> iterator = religionList.iterator(); iterator.hasNext();) {
            Bank next = iterator.next();
            BankMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBankname());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : BankMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getBank())) {
                    employeeList.get(i).setBank(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<Employee> getPFBank(List<Employee> employeeList) throws Exception {
        Map<String, String> BankMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BANK_TABLE);
        List<Bank> religionList = new Gson().fromJson(result, new TypeToken<List<Bank>>() {
        }.getType());
        for (Iterator<Bank> iterator = religionList.iterator(); iterator.hasNext();) {
            Bank next = iterator.next();
            BankMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBankname());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : BankMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getPfBank())) {
                    employeeList.get(i).setPfBank(entry.getValue());
                }
            }
        }
        return employeeList;
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

    public static List<Employee> getDiscipline(List<Employee> employeeList) throws Exception {
        Map<String, String> DisciplineMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DISCIPLINE_TABLE);
        List<Discipline> religionList = new Gson().fromJson(result, new TypeToken<List<Discipline>>() {
        }.getType());
        for (Iterator<Discipline> iterator = religionList.iterator(); iterator.hasNext();) {
            Discipline next = iterator.next();
            DisciplineMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDisciplineName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DisciplineMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDiscipline())) {
                    employeeList.get(i).setDiscipline(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<Employee> getNature(List<Employee> employeeList) throws Exception {
        Map<String, String> NatureMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.NATURE_TABLE);
        List<Nature> religionList = new Gson().fromJson(result, new TypeToken<List<Nature>>() {
        }.getType());
        for (Iterator<Nature> iterator = religionList.iterator(); iterator.hasNext();) {
            Nature next = iterator.next();
            NatureMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getNatureName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : NatureMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getNatureType())) {
                    employeeList.get(i).setNatureType(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<Employee> getFundType(List<Employee> employeeList) throws Exception {
        Map<String, String> FundTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            FundTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : FundTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getFundType())) {
                    employeeList.get(i).setFundType(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public List<Employee> getDDO(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getBudgetHead(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getSalaryType(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getCity(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getQuarterNo(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getGrade(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getPFType(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getEmployeeLeftStatus(List<Employee> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.EMPLOYEE_LEFT_STATUS_MASTER);
        List<EmployeeLeftStatus> religionList = new Gson().fromJson(result, new TypeToken<List<EmployeeLeftStatus>>() {
        }.getType());
        for (Iterator<EmployeeLeftStatus> iterator = religionList.iterator(); iterator.hasNext();) {
            EmployeeLeftStatus next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeLeftStatus());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getEmployeeLeftStatus())) {
                    employeeList.get(i).setEmployeeLeftStatus(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public List<Employee> getSalaryBillType(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getHeadSlab(List<Employee> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.HEAD_NAME_MASTER);
        List<HeadName> religionList = new Gson().fromJson(result, new TypeToken<List<HeadName>>() {
        }.getType());
        for (Iterator<HeadName> iterator = religionList.iterator(); iterator.hasNext();) {
            HeadName next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getHeadName());//return objId of Head Name
        }
        //System.out.println("*****************************************************");
        //System.out.println(new Gson().toJson(PFTypeMap));
        //System.out.println("*****************************************************");
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getHeadSlab())) {
                    employeeList.get(i).setHeadSlab(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public List<Employee> getReportingTo(List<Employee> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.EMPLOYEE_TABLE);
        List<Employee> religionList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        for (Iterator<Employee> iterator = religionList.iterator(); iterator.hasNext();) {
            Employee next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getEmployeeName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getReportingTo())) {
                    employeeList.get(i).setReportingTo(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public List<Employee> getLocation(List<Employee> employeeList) throws Exception {
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

    public List<Employee> getPostedDesignation(List<Employee> employeeList) throws Exception {
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
                if (entry.getKey().equals(employeeList.get(i).getPostedDesignation())) {
                    employeeList.get(i).setPostedDesignation(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public List<Employee> getPostingDDO(List<Employee> employeeList) throws Exception {
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
                if (entry.getKey().equals(employeeList.get(i).getPostingDDO())) {
                    employeeList.get(i).setPostingDDO((entry.getValue()));
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

    public String fetchAllBySearch(Employee employeeSearch) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", employeeSearch.getDdo());
        if (employeeSearch.getDepartment() != null) {
            conditionMap.put("department", employeeSearch.getDepartment());
        }
        if (employeeSearch.getDesignation() != null) {
            conditionMap.put("designation", employeeSearch.getDesignation());
        }
        if (employeeSearch.getNatureType() != null) {
            conditionMap.put("natureType", employeeSearch.getNatureType());
        }
        if (employeeSearch.getPostingCity() != null) {
            conditionMap.put("postingCity", employeeSearch.getPostingCity());
        }
        if (employeeSearch.getFundType() != null) {
            conditionMap.put("fundType", employeeSearch.getFundType());
        }
        if (employeeSearch.getBudgetHead() != null) {
            conditionMap.put("budgetHead", employeeSearch.getBudgetHead());
        }

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        List<Employee> employeeList = new Gson().fromJson(result1, new TypeToken<List<Employee>>() {
        }.getType());
        Set<Employee> EmpSet = new HashSet<Employee>();
        try {
            EmpSet.addAll(employeeList);
        } catch (Exception e) {
        }
        if (employeeSearch.getEmployeeName() == null) {
            employeeSearch.setEmployeeName("");
        }
        if (employeeSearch.getEmployeeCode() == null) {
            employeeSearch.setEmployeeCode("");
        }
        if (employeeSearch.getEmployeeCodeM() == null) {
            employeeSearch.setEmployeeCodeM("");
        }
        Set<Employee> EmpSet1 = new HashSet<Employee>();
        //System.out.println(EmpSet.size());

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
            employeeList = getPostingDDO(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getAssociation(employeeList);
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
            employeeList = getPostedDesignation(employeeList);

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
            employeeList = getEmployeeLeftStatus(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getHeadSlab(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getReportingTo(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getLocation(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getClass(employeeList);
        } catch (Exception e) {
        }
        try {
            List<Employee> empli = new EmployeeManager().get(employeeSearch, "0", "1000");

            for (int i = 0; i < empli.size(); i++) {
                for (int j = 0; j < employeeList.size(); j++) {
                    if (employeeList.get(j).getId().equals(empli.get(i).getId())) {
                        employeeList.remove(j);
                    }
                }
            }
            for (int i = 0; i < empli.size(); i++) {
                if (employeeList.contains(empli.get(i).getId())) {
                } else {
                    employeeList.add(empli.get(i));
                }
            }

        } catch (Exception e) {
        }

        String finalresult = new Gson().toJson(employeeList);
        return finalresult;
    }

    private List<Employee> getAssociation(List<Employee> employeeList) throws Exception {
        Map<String, String> SalutationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.ASSOCIATION_TABLE);
        List<Association> Associationlist = new Gson().fromJson(result, new TypeToken<List<Association>>() {
        }.getType());
        for (Iterator<Association> iterator = Associationlist.iterator(); iterator.hasNext();) {
            Association next = iterator.next();
            SalutationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getAssociationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : SalutationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getAssociation())) {
                    employeeList.get(i).setAssociation(entry.getValue());
                }
            }
        }
        return employeeList;

    }

    public MongoFile FetchImage(String employeeId) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ownerID", employeeId);
        String gson = DBManager.getDbConnection().listAllFilesByCondition("employeeImage", conditionMap);
//        JsonParser parser = new JsonParser();
//JsonObject o = parser.parse("{\"a\": \"A\"}").getAsJsonObject();
        JSONArray jsonObj = new JSONArray(gson);
        //System.out.println(jsonObj);
        List<EmployeeFileHolder> efh = new ArrayList<EmployeeFileHolder>();
        for (int i = 0; i < jsonObj.length(); i++) {
            EmployeeFileHolder fh = new EmployeeFileHolder();
            JSONObject obj = (JSONObject) jsonObj.getJSONObject(i).get("uploadDate");
            String dateStr = obj.get("$date") + "";
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
            Date dd = d.parse(dateStr);
            fh.setUploadDate(dd);
            JSONObject idObj = (JSONObject) jsonObj.getJSONObject(i).get("_id");
            String id = idObj.get("$oid").toString();
            fh.setIdStr(id);
            efh.add(fh);
        }
        //System.out.println(efh.size());
        if (efh != null) {
            Collections.sort(efh, new GetLatestEmpImageComparator());
        }
        MongoFile mf = DBManager.getDbConnection().getGridFsFile(efh.get(0).getIdStr(), "employeeImage");
//        //System.out.println(new Gson().toJson(efh));
//        //System.out.println(new Gson().toJson(efh));
//        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
//        String s = jsonObj.getJSONObject(0).get("uploadDate").toString();
//        Gson  gs=new Gson().toJson(obj);
//        //System.out.println(gs);
//        String ssss=((LinkedTreeMap<String, String>) jsonObj.getJSONObject(0).get("uploadDate")).get("$date");
//        //System.out.println(ssss);
//        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        Date dd = d.parse(ssss);
//        //System.out.println(dd);
//          String str= (String) jsonObj.getJSONObject(0).get("uploadDate");2016-09-30T06:09:44.869Z
//      for(int i=0; i<jsonObj.length(); i++){
////          //System.out.println(jsonObj[i].getString("uploadDate"));
//      }
//          
//         Gson gson = new Gson();
//        String json = gson.toJson(gson);
//        List<EmployeeFileHolder> listOfFiles = new Gson().fromJson(json, new TypeToken<List<EmployeeFileHolder>>() {
//        }.getType());
//        
//         Gson gson = new Gson();
//        String json = gson.toJson(gson);
////        Collections.sort(listOfFiles, new GetLatestEmpImageComparator());
//        //System.out.println(new Gson().toJson(listOfFiles));
//        List<FileHolder> fileList = new Gson().fromJson(json, new TypeToken<List<FileHolder>>() {
//        }.getType());
//        Map<String, String> fileMap = new HashMap<String, String>();
//        for (Iterator<FileHolder> iterator = fileList.iterator(); iterator.hasNext();) {
//            FileHolder next = iterator.next();
//            fileMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getOwnerID());
//        }
//        String imageID = "";
//        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
//            imageID = entry.getKey();
//            break;
//        }
//        MongoFile mf = DBManager.getDbConnection().getGridFsFile(imageID, "employeeImage");
        return mf;
    }

    public List<Employee> get(Employee emp, String input_sindex, String input_limit) throws Exception {
        // check input parameter
        String searchKey = emp.getEmployeeName();
        if (searchKey == null || searchKey.isEmpty() || input_sindex == null || input_sindex.isEmpty()
                || input_limit == null || input_limit.isEmpty()) {
            return null;
        }
        List<Employee> result = null;
        Map<String, String> smap = new HashMap<String, String>();
        int sindex = 0;
        int limit = 0;
        if (input_sindex != null && input_limit != null) {
            sindex = Integer.parseInt(input_sindex);
            limit = Integer.parseInt(input_limit);
        }

        if ((searchKey != null || !searchKey.equals("")) && searchKey.length() >= 0) {
            if (searchKey.contains(" ")) {
                String searchStr[] = searchKey.split(" ");
                if (searchStr.length > 1) {
                    searchKey = "(" + searchStr[0] + ")" + "(.*?)" + "(" + searchStr[searchStr.length - 1] + ")";
                } else {
                    searchKey = "(^" + searchKey + "|\\W" + searchKey + ")";
                }
            } else {
                searchKey = "(^" + searchKey + "|\\W" + searchKey + ")";
            }
            String empCode = emp.getEmployeeCode();
            if (empCode.contains(" ")) {
                String searchStr[] = empCode.split(" ");
                if (searchStr.length > 1) {
                    empCode = "(" + searchStr[0] + ")" + "(.*?)" + "(" + searchStr[searchStr.length - 1] + ")";
                } else {
                    empCode = "(^" + empCode + "|\\W" + empCode + ")";
                }
            } else {
                empCode = "(^" + empCode + "|\\W" + empCode + ")";
            }
            String empCodeM = emp.getEmployeeCode();
            if (empCodeM.contains(" ")) {
                String searchStr[] = empCodeM.split(" ");
                if (searchStr.length > 1) {
                    empCodeM = "(" + searchStr[0] + ")" + "(.*?)" + "(" + searchStr[searchStr.length - 1] + ")";
                } else {
                    empCodeM = "(^" + empCodeM + "|\\W" + empCodeM + ")";
                }
            } else {
                empCodeM = "(^" + empCodeM + "|\\W" + empCodeM + ")";
            }
            smap.put(ApplicationConstants.NAME, searchKey);
            smap.put("employeeCode", empCode);
            smap.put("employeeCodeM", empCodeM);
            smap.put(ApplicationConstants.SORTKEY, ApplicationConstants.NAME);
            smap.put(ApplicationConstants.SORTORDER, ApplicationConstants.SORTORDER_TYPE);

            String dbOutput = null;
            if (limit == 0 && sindex == 0) {
                limit = 10;
                // fetch employee from data base
                dbOutput = DBManager.getDbConnection().fetchRowsLike(ApplicationConstants.EMPLOYEE_TABLE, smap, ApplicationConstants.LOGICAL_OR, sindex, limit);
            } else {
                // fetch employee from data base
                dbOutput = DBManager.getDbConnection().fetchRowsLike(ApplicationConstants.EMPLOYEE_TABLE, smap, ApplicationConstants.LOGICAL_OR, sindex, limit);
            }

            if (dbOutput != null) {
                Type type = new TypeToken<List<Employee>>() {
                }.getType();
                result = new Gson().fromJson(dbOutput, type);
            }
        }
        List<Employee> employeeList = result;
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
            employeeList = getPostingDDO(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getAssociation(employeeList);
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
            employeeList = getPostedDesignation(employeeList);

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
            employeeList = getEmployeeLeftStatus(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getHeadSlab(employeeList);

        } catch (Exception e) {
        }
        try {
            employeeList = getReportingTo(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getLocation(employeeList);
        } catch (Exception e) {
        }
        try {

            employeeList = getClass(employeeList);
        } catch (Exception e) {
        }
        return employeeList;
    }

    public List<Employee> searchItFinally(Employee emp) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);

        BasicDBObject regexQuery = new BasicDBObject();

        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
        if (emp.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));
        }
        if (emp.getDepartment() != null) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));
        }
        if (emp.getDesignation() != null) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", emp.getDesignation()));
        }
        if (emp.getNatureType() != null) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", emp.getNatureType()));
        }
        if (emp.getPostingCity() != null) {
            regexQuery.put("postingCity",
                    new BasicDBObject("$regex", emp.getPostingCity()));
        }
        if (emp.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", emp.getFundType()));
        }
        if (emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", emp.getBudgetHead()));
        }
        if (emp.getEmployeeName() != null) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (emp.getEmployeeCode() != null) {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", emp.getEmployeeCode()));
        }
        if (emp.getEmployeeCodeM() != null) {
            regexQuery.put("employeeCodeM",
                    new BasicDBObject("$regex", emp.getEmployeeCodeM()));
        }
        if (emp.getSalaryBillType() != null) {
            regexQuery.put("salaryBillType",
                    new BasicDBObject("$regex", emp.getSalaryBillType()));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        //System.out.println(regexQuery);

        DBCursor cursor2 = collection.find(regexQuery);

        List<Employee> employeeList = new ArrayList<Employee>();
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<Employee>() {
            }.getType();
            Employee em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }
        if (employeeList.size() > 0) {
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
                employeeList = getPostingDDO(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getAssociation(employeeList);
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
                employeeList = getPostedDesignation(employeeList);

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
                employeeList = getEmployeeLeftStatus(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getHeadSlab(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getReportingTo(employeeList);
            } catch (Exception e) {
            }
            try {

                employeeList = getClass(employeeList);
            } catch (Exception e) {
            }
            try {

                employeeList = getLocation(employeeList);
            } catch (Exception e) {
            }
        }
        return employeeList;
    }

    public String fetchAllNames() throws Exception {

        RestClient aql = new RestClient();
        String Active = "\"Active\"";
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + (String) config.getProperty("db-schema") + "/employee`";
        String quString = "select employeeName,employeeCode,ddo,designation from  " + mongoCollectionName + " where  status = " + Active;
        String json = aql.getRestData(ApplicationConstants.END_POINT, quString);
        List<Employee> list = new Gson().fromJson(json, new TypeToken<List<Employee>>() {
        }.getType());
        //System.out.println(new Gson().toJson(list));
        return new Gson().toJson(list);
    }

    public String fetchAllLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_TABLE, conditionMap);
        List<Location> employeeList = new Gson().fromJson(result1, new TypeToken<List<Location>>() {
        }.getType());
        String finalresult = new Gson().toJson(employeeList);
        return finalresult;
    }

    public String fetchEmployeeLocation(String ddo_id) throws Exception {

        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddo_id);
        List<DDO> empList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        List<DDO> employeeList = new ArrayList<DDO>();
        employeeList.add(empList.get(0));
        try {

            employeeList = getLocation1(employeeList);
        } catch (Exception e) {
        }
        return new Gson().toJson(employeeList);
    }

    private List<DDO> getLocation1(List<DDO> employeeList) throws Exception {
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

    public Employee fetchEmployee(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String empJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, rid);
        List<Employee> relationlist = new Gson().fromJson(empJson, new TypeToken<List<Employee>>() {
        }.getType());
        Employee emp = relationlist.get(0);
        return emp;
    }

    private List<Employee> getClass(List<Employee> employeeList) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CLASS_TABLE);
        List<Class> religionList = new Gson().fromJson(result, new TypeToken<List<Class>>() {
        }.getType());
        for (Iterator<Class> iterator = religionList.iterator(); iterator.hasNext();) {
            Class next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getClassMaster())) {
                    employeeList.get(i).setClassMaster(entry.getValue());
                    employeeList.get(i).setClassMasterId(entry.getKey());
                }
            }
        }
        return employeeList;
    }

    private String getEmployeeCode(String ddoCode, String locationCode) throws Exception {
        String employeeCode = "";
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.REGEX, ddoCode + "-" + locationCode);
        conditionMap.put("employeeCode", innerMap);
        //System.out.println(new Gson().toJson(conditionMap));

        RestClient aql = new RestClient();
        String employeeResult = DBManager.getDbConnection().fetchRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + (String) config.getProperty("db-schema") + "/employee`";
        String quString = "select  employeeCode from  " + mongoCollectionName + "  where  employeeCode like " + ddoCode + "-" + locationCode + "-%";
        String EndPoint = (String) config.getProperty("aql_url");
        String json = aql.getRestData(EndPoint, quString);
        if (employeeResult != null) {
        } else {
            //System.out.println("Null");
            employeeCode = ddoCode + "-" + locationCode + "-1";
        }
        return employeeCode;
    }

    public String getEmployeeCodeUsingAQL(String ddoCode, String locationCode) {
        String returnEmployeeCode = ddoCode + ApplicationConstants.DDO_LOCATION_PATTERN_DIVIDER + locationCode + ApplicationConstants.DDO_LOCATION_AUTOINCREMENT_VALUE_PATTERN_DIVIDER;
        RestClient aql = new RestClient();
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + "employee`";
        String likeQuery = "\"%" + ddoCode + ApplicationConstants.DDO_LOCATION_PATTERN_DIVIDER + locationCode + ApplicationConstants.DDO_LOCATION_AUTOINCREMENT_VALUE_PATTERN_DIVIDER + "%\"";
        String quString = "select  " + ApplicationConstants.EMPLOYEE_CODE + " from  " + mongoCollectionName + "  where  " + ApplicationConstants.EMPLOYEE_CODE + " like " + likeQuery + " ";
        String EndPoint = (String) config.getProperty("aql_url");
        //System.out.println(quString);
        String json = aql.getRestData(EndPoint, quString);
        //System.out.println(json);
        List<Employee> li = new Gson().fromJson(json, new TypeToken<List<Employee>>() {
        }.getType());
        int autoIncrementValue = 0;
        for (Iterator<Employee> iterator = li.iterator(); iterator.hasNext();) {
            Employee next = iterator.next();
            String employeeCode = next.getEmployeeCode();
            String[] str = employeeCode.split(ApplicationConstants.DDO_LOCATION_AUTOINCREMENT_VALUE_PATTERN_DIVIDER);
            if (autoIncrementValue < Integer.parseInt(str[1])) {
                autoIncrementValue = Integer.parseInt(str[1]);
            }
        }
        autoIncrementValue++;
//        HashMap<String, String> resultMap = new HashMap<String, String>();
//        resultMap.put("employeeCode", returnEmployeeCode + Integer.toString(autoIncrementValue));
        return returnEmployeeCode + Integer.toString(autoIncrementValue);
    }

    public static boolean postCheckingMethodInAQL(Employee employee) {
        RestClient aql = new RestClient();
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + (String) config.getProperty("db-schema") + "/designationfundtypemaster`";
        String likeddo = "\"%" + employee.getDdo() + "%\"";
        String likeDesignation = "\"%" + employee.getDesignation() + "%\"";
        String likeGrade = "\"%" + employee.getGrade() + "%\"";
        String likeFundType = "\"%" + employee.getFundType() + "%\"";
        String likeBudgetHead = "\"%" + employee.getBudgetHead() + "%\"";
        String likeNatureType = "\"%" + employee.getNatureType() + "%\"";
        String likeDiscipline = "\"%" + employee.getDiscipline() + "%\"";
        String likeCategory = "\"%PostTesting %\"";

        String quString = "select categoryposts from  " + mongoCollectionName + "  where  " + ApplicationConstants.EMP_DDO + " like " + likeddo + " and " + ApplicationConstants.EMP_DESIGNATION + " like " + likeDesignation + " and " + ApplicationConstants.EMP_GRADE + " like " + likeGrade + " and categoryposts.categoory like " + likeCategory + "";
//        String quString = "select  " + ApplicationConstants.DESIGNATION_FUNDTYPE_MAPPING_CATEGORYPOST + "." + ApplicationConstants.POSTS + " from  " + mongoCollectionName + "  where  " + ApplicationConstants.EMP_DDO + " like " + likeddo + " and " + ApplicationConstants.EMP_DESIGNATION + " like " + likeDesignation + " and " + ApplicationConstants.EMP_GRADE + " like " + likeGrade + " ";
        String EndPoint = (String) config.getProperty("aql_url");
        //System.out.println(quString);
        String json = aql.getRestData(EndPoint, quString);
        //System.out.println(json);
        return false;
    }

    public static boolean postCheckingMethod(Employee employee) throws Exception {
        HashMap<String, String> desigcondiMap = new HashMap<String, String>();
        desigcondiMap.put("ddo", employee.getDdo());
        desigcondiMap.put("designation", employee.getDesignation());
        desigcondiMap.put("grade", employee.getGrade());
        desigcondiMap.put("fundType", employee.getFundType());
        desigcondiMap.put("budgetHead", employee.getBudgetHead());
        desigcondiMap.put("natureType", employee.getNatureType());
        desigcondiMap.put("desciplineName", employee.getDiscipline());
        desigcondiMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String desigresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, desigcondiMap);
        if (desigresult == null) {
            return false;
        }
        List<DesignationFundTypeMapping> desiglist = new Gson().fromJson(desigresult, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        if (desiglist == null || desiglist.size() < 1) {
            return false;
        }
        DesignationFundTypeMapping desigsinglist = desiglist.get(0);
        int postForThisCategory = 0;
        List<CategoryPosts> categoryposts = desigsinglist.getCategoryposts();
        for (Iterator<CategoryPosts> iterator = categoryposts.iterator(); iterator.hasNext();) {
            CategoryPosts next = iterator.next();
            String categoryId = employee.getCategory();
            String categoryData = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, categoryId);

            if (categoryData != null) {
                List<Category> categoryList = new Gson().fromJson(categoryData, new TypeToken<List<Category>>() {
                }.getType());
                Category category = categoryList.get(0);
                String categoryNameId = next.getCategoory();
                String categoryNameData = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, categoryNameId);
                if (categoryNameData != null) {
                    List<Category> categoryNameList = new Gson().fromJson(categoryNameData, new TypeToken<List<Category>>() {
                    }.getType());
                    Category cateNameObj = categoryNameList.get(0);
                    String catName = cateNameObj.getCategoryy();
                    if (catName.equalsIgnoreCase(category.getCategoryy())) {
                        postForThisCategory = Integer.parseInt(next.getPosts());
                    }
                }
            }

        }
        if (postForThisCategory < 1) {
            return false;
        } else {
            HashMap<String, String> saveconditionMap = new HashMap<String, String>();
            saveconditionMap.put("ddo", employee.getDdo());
            saveconditionMap.put("designation", employee.getDesignation());
            saveconditionMap.put("grade", employee.getGrade());
            saveconditionMap.put("fundType", employee.getFundType());
            saveconditionMap.put("budgetHead", employee.getBudgetHead());
            saveconditionMap.put("natureType", employee.getNatureType());
            saveconditionMap.put("discipline", employee.getDiscipline());
            saveconditionMap.put("category", employee.getCategory());
            saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String totalNoOfEmpBelongToThisCategory = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, saveconditionMap);
            List<Employee> emplist = new Gson().fromJson(totalNoOfEmpBelongToThisCategory, new TypeToken<List<Employee>>() {
            }.getType());
            if (emplist == null) {
                return true;
            } else if (emplist.size() > 0) {
                if (emplist.size() < postForThisCategory) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public long saveInMilliSecond(String str) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dateInString = str;
            Date date = sdf.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getHeads(Employee empGetHeads) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();

        List<SalaryHead> earningHeads = new ArrayList<SalaryHead>();
        List<SalaryHead> earningHeadss = new ArrayList<SalaryHead>();
        earningHeadss = fetchAllEarningsSalaryHead();
        if (earningHeadss != null) {
            earningHeads.addAll(earningHeadss);
        }
        List<SalaryHead> deductionHeads = new ArrayList<SalaryHead>();
        deductionHeads = fetchAllDeductionsSalaryHead();
        String basicSalaryHeadId = null;
        String gradepaySalaryHeadId = null;
        Long basicAmount = empGetHeads.getBasic();
        double basicAmountInDouble = empGetHeads.getBasic();
        long gradePay = empGetHeads.getGradePay();
        SalaryHead basicEarningHead = fetchBasicEarningHead();
        SalaryHead gardepayEarningHead = fetchGradepayEarningHead();

        if (basicEarningHead != null) {
            basicSalaryHeadId = ((LinkedTreeMap<String, String>) basicEarningHead.getId()).get("$oid");
        } else {
            resultMap.put("statusMessageOfHeads", ApplicationConstants.ADD_BASIC_SALARY_HEAD_FIRST);
            return new Gson().toJson(resultMap);
        }
        if (gardepayEarningHead != null) {
            gradepaySalaryHeadId = ((LinkedTreeMap<String, String>) gardepayEarningHead.getId()).get("$oid");
        }
        if (earningHeads != null && earningHeads.size() > 0) {
            for (Iterator<SalaryHead> iterator = earningHeads.iterator(); iterator.hasNext();) {
                SalaryHead next = iterator.next();
                try {
                    double calculatedAmount = getHeadAmount(next, basicSalaryHeadId, basicAmount, gradePay, empGetHeads, gradepaySalaryHeadId);
                    next.setCalculatedAmount(calculatedAmount);
                } catch (Exception e) {
                    next.setCalculatedAmount(0);
                }
            }
        }
        //System.out.println("**************************************Earning is completed***************************************************");
        if (deductionHeads != null && deductionHeads.size() > 0) {
            for (Iterator<SalaryHead> iterator = deductionHeads.iterator(); iterator.hasNext();) {
                SalaryHead next = iterator.next();
                try {
                    double calculatedAmount = getHeadAmount(next, basicSalaryHeadId, basicAmount, gradePay, empGetHeads, gradepaySalaryHeadId);
                    next.setCalculatedAmount(calculatedAmount);
                } catch (Exception e) {
                    next.setCalculatedAmount(0);
                }
            }
        }
        if (deductionHeads != null) {
            for (Iterator<SalaryHead> iterator = deductionHeads.iterator(); iterator.hasNext();) {
                SalaryHead salHead = iterator.next();
                //System.out.println(salHead.getDescription());
                if (salHead.getShortDescription().equalsIgnoreCase(ApplicationConstants.PT)) {
                    if (empGetHeads.getPtApplicable() != null) {
                        if (empGetHeads.getPtApplicable().equalsIgnoreCase(ApplicationConstants.YES)) {
                        } else if (empGetHeads.getPtApplicable().equalsIgnoreCase(ApplicationConstants.NO)) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                } else if (salHead.getShortDescription().equalsIgnoreCase(ApplicationConstants.GPF)) {
                    if (empGetHeads.getStopGPF().equalsIgnoreCase(ApplicationConstants.YES)) {
                        iterator.remove();
                    }
                } else if (salHead.getShortDescription().equalsIgnoreCase(ApplicationConstants.ASSOCIATION_FEE)) {
                    if (empGetHeads.getAssociation() != null && !empGetHeads.getAssociation().isEmpty()) {
                        salHead.setCalculatedAmount(getAssociationFeeForSHF(empGetHeads.getAssociation()));
                    } else {
                        iterator.remove();
                    }
                }
            }
        }
        basicEarningHead.setCalculatedAmount(basicAmountInDouble);
        earningHeads.add(basicEarningHead);
        if (earningHeads != null) {
            Collections.sort(earningHeads, new SortByDisplayLeave());
        }
        if (deductionHeads != null) {
            Collections.sort(deductionHeads, new SortByDisplayLeave());
        }
//        for (Iterator<SalaryHead> iterator = earningHeads.iterator(); iterator.hasNext();) {
//            SalaryHead next = iterator.next();
//            //System.out.println(next.getDescription());
//            //System.out.println(next.getCalculatedAmount());
//            //System.out.println(new Gson().toJson(next));
//
//        }
        //System.out.println("************************************Deductions is completed**************************************************");

        resultMap.put("EarningHeads", new Gson().toJson(earningHeads));
        resultMap.put("DeductionHeads", new Gson().toJson(deductionHeads));
        return new Gson().toJson(resultMap);
    }

    public static double getHeadAmount(SalaryHead fo, String basicId, Long basicAmount, Long gradePay, Employee empGetHeads, String gradePayId) throws Exception {
        double result = 0.0;
        String basicAmountInString = "" + basicAmount;
        String gradepayAmountInString = "" + gradePay;
        if (fo.getMapping().equalsIgnoreCase(ApplicationConstants.NO)) {
            //System.out.println("Mapping is no");
            String forid = fo.getFormula();
            String forml = null;
            String amount = fo.getAmount();
            if (amount != null && !amount.isEmpty() && (forid == null || forid.isEmpty())) {
//            if ((amount.isEmpty() || amount != null) && (forid == null || forid.isEmpty() || forid.length() < 1)) {
//            if ((amount != "" || amount != null || amount.length() > 1) && (forid == null || forid == "" || forid.length() < 1)) {
                //System.out.println("Inside amount");
                result = Double.parseDouble(amount);
                result = round(fo, result);
                return result;
            } else if ((forid != null && !forid.isEmpty()) && (amount == null || amount.isEmpty())) {
                //System.out.println("Inside formula");
                forml = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, forid);
                List<Formula> formuList = null;
                if (forml != null) {
                    formuList = new Gson().fromJson(forml, new TypeToken<List<Formula>>() {
                    }.getType());
                } else {
                    return result;
                }
                Formula forobj = formuList.get(0);
                String formu = forobj.getHiddenformula();
                List<Integer> liIndexes = new ArrayList<Integer>();
                List<String> idlist = new ArrayList<String>();
                boolean condition = true;
                while (condition) {
                    liIndexes.clear();
                    idlist.clear();
                    condition = false;
                    char[] chrArray = formu.toCharArray();
                    int j = 0;
                    for (int i = 0; i < chrArray.length; i++) {
                        char c = chrArray[i];
                        if (c == '#') {
                            liIndexes.add(i);
                        }
                    }
                    for (int k = 0; k < liIndexes.size(); k = k + 2) {
                        idlist.add(formu.substring(liIndexes.get(k) + 1, liIndexes.get(k + 1)));
                    }
                    for (Iterator<String> iterator = idlist.iterator(); iterator.hasNext();) {
                        String next = iterator.next();
                        if (next.equalsIgnoreCase(basicId)) {
                            formu = formu.replaceAll("#" + basicId + "#", basicAmountInString);
                        } else if (next.equalsIgnoreCase(gradePayId)) {
                            formu = formu.replaceAll("#" + gradePayId + "#", gradepayAmountInString);
                        } else {
                            SalaryHead sal = new Gson().fromJson(new SalaryHeadManager().fetchRawData(next), new TypeToken<SalaryHead>() {
                            }.getType());
                            formu = formu.replaceAll("#" + next + "#", getHeadAmount(sal, basicId, basicAmount, gradePay, empGetHeads, gradePayId) + "");
//                            formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(sal.getFormula()).getHiddenformula());
                        }
                        condition = false;
                        char[] chrArrayForCheck = formu.toCharArray();
                        for (int i = 0; i < chrArrayForCheck.length; i++) {
                            char c = chrArrayForCheck[i];
                            if (c == '#') {
                                condition = true;
                                break;
                            }
                        }
                    }
                }
                result = ExpressionCalculatorManager.calculateTheValue(formu);
                result = round(fo, result);
                return result;
            }
        } else if (fo.getMapping().equalsIgnoreCase(ApplicationConstants.YES)) {
            result = getAmountUsingHeadSlab(fo, basicId, basicAmount, gradePay, empGetHeads, gradePayId);
            return result;
        }
        return result;
    }

    public static double round(SalaryHead fo, double num) {
        double multipleOf = 0.0;
        if (fo.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_NONE)) {
            return num;
        } else if (fo.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_50_PAISE)) {
            multipleOf = 0.5;
        } else if (fo.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_1_RUPEE)) {
            multipleOf = 1.0;
        } else if (fo.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_10_RUPEE)) {
            multipleOf = 10.0;
        } else {
            return num;
        }
        double dd = Math.floor((num + multipleOf / 2) / multipleOf) * multipleOf;
        return dd;
    }

    public static List<SalaryHead> fetchAllEarningsSalaryHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.HEAD_TYPE, ApplicationConstants.EARNING_HEADS);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("active", ApplicationConstants.YES);
        conditionMap.put(ApplicationConstants.IS_BASIC, ApplicationConstants.NO);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return list;
    }

    public static SalaryHead fetchBasicEarningHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.HEAD_TYPE, ApplicationConstants.EARNING_HEADS);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.IS_BASIC, ApplicationConstants.YES);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        if (list != null) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static SalaryHead fetchGradepayEarningHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.HEAD_TYPE, ApplicationConstants.EARNING_HEADS);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("description", ApplicationConstants.GRADE_PAY);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        if (list != null) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<SalaryHead> fetchAllDeductionsSalaryHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.HEAD_TYPE, ApplicationConstants.DEDUCTION_HEADS);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("active", ApplicationConstants.YES);
        conditionMap.put(ApplicationConstants.DEDUCTION_TYPE, ApplicationConstants.DEDUCTION_TYPE_OTHERS);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return list;
    }

    private static double getAmountUsingHeadSlab(SalaryHead fo, String basicId, long basicAmount, long gradePay, Employee empGetHeads, String gradePayId) throws Exception {
        double returnValue = 0;
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.HEAD_SLAB_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        String salaryHeadId = ((LinkedTreeMap<String, String>) fo.getId()).get("$oid");
        regexQuery.put(ApplicationConstants.HEAD_SLAB_HEAD_NAME,
                new BasicDBObject(ApplicationConstants.EQUAL_OPERATOR, salaryHeadId));
        regexQuery.put("fromGPLong",
                new BasicDBObject(ApplicationConstants.LTE_OPERATOR, gradePay));
        regexQuery.put("toGPLong",
                new BasicDBObject(ApplicationConstants.GTE_OPERATOR, gradePay));
        regexQuery.put("fromBasicLong",
                new BasicDBObject(ApplicationConstants.LTE_OPERATOR, basicAmount));
        regexQuery.put("toBasicLong",
                new BasicDBObject(ApplicationConstants.GTE_OPERATOR, basicAmount));
        regexQuery.put(ApplicationConstants.STATUS,
                new BasicDBObject(ApplicationConstants.EQUAL_OPERATOR, "Active"));
        DBCursor cursor = collection.find(regexQuery);
        List<HeadSlab> headsSlabList = new ArrayList<HeadSlab>();
        HeadSlab headSlabForIteration = null;
        while (cursor.hasNext()) {
            DBObject ob = cursor.next();
            Type type = new TypeToken<HeadSlab>() {
            }.getType();
            headSlabForIteration = new Gson().fromJson(ob.toString(), type);
            headsSlabList.add(headSlabForIteration);
        }
        if (headsSlabList != null) {
            HeadSlab headSlab = basedOnQuery(headsSlabList, empGetHeads);
            if (headSlab != null) {
                String amountOrFormula = headSlab.getTypeTwo();
                if (amountOrFormula.equalsIgnoreCase(ApplicationConstants.IS_FORMULA)) {
                    String formulaId = headSlab.getFormulaTwo();
                    String formula = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, formulaId);
                    List<Formula> formuList = null;
                    if (formula != null) {
                        formuList = new Gson().fromJson(formula, new TypeToken<List<Formula>>() {
                        }.getType());
                    } else {
                        return returnValue;
                    }
                    Formula forobj = formuList.get(0);
                    String formu = forobj.getHiddenformula();
                    List<Integer> liIndexes = new ArrayList<Integer>();
                    List<String> idlist = new ArrayList<String>();
                    boolean condition = true;
                    while (condition) {
                        liIndexes.clear();
                        idlist.clear();
                        condition = false;
                        char[] chrArray = formu.toCharArray();
                        int j = 0;
                        for (int i = 0; i < chrArray.length; i++) {
                            char c = chrArray[i];
                            if (c == '#') {
                                liIndexes.add(i);
                            }
                        }
                        for (int k = 0; k < liIndexes.size(); k = k + 2) {
                            idlist.add(formu.substring(liIndexes.get(k) + 1, liIndexes.get(k + 1)));
                        }
                        for (Iterator<String> iterator = idlist.iterator(); iterator.hasNext();) {
                            String next = iterator.next();
                            if (next.equalsIgnoreCase(basicId)) {
                                formu = formu.replaceAll("#" + basicId + "#", basicAmount + "");
                            } else {
                                SalaryHead sal = new Gson().fromJson(new SalaryHeadManager().fetchRawData(next), new TypeToken<SalaryHead>() {
                                }.getType());
                                formu = formu.replaceAll("#" + next + "#", getHeadAmount(sal, basicId, basicAmount, gradePay, empGetHeads, gradePayId) + "");
//                            formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(sal.getFormula()).getHiddenformula());
                            }
                            condition = false;
                            char[] chrArrayForCheck = formu.toCharArray();
                            for (int i = 0; i < chrArrayForCheck.length; i++) {
                                char c = chrArrayForCheck[i];
                                if (c == '#') {
                                    condition = true;
                                    break;
                                }
                            }
                        }
                    }
                    returnValue = ExpressionCalculatorManager.calculateTheValue(formu);
                    returnValue = round(fo, returnValue);
                    long maximumAmount = headSlab.getMaximumAmountLong();
                    long minimumAmount = headSlab.getMinimumAmountLong();
                    if (returnValue >= minimumAmount && returnValue <= maximumAmount) {
                        return returnValue;
                    } else if (returnValue < minimumAmount) {
                        return minimumAmount;
                    } else if (returnValue > maximumAmount) {
                        return maximumAmount;
                    }
                    return returnValue;
                } else {
                    returnValue = headSlab.getAmountLong();
                    return returnValue;
                }
            } else {
                return 0;
            }
        }
        return 0;
    }

    public static HeadSlab basedOnQuery(List<HeadSlab> headSlabList, Employee empGetHeads) throws UnknownHostException, Exception {
        HeadSlab ResultantHeadSlab = null;
        boolean flag = false;
        for (Iterator<HeadSlab> iterator = headSlabList.iterator(); iterator.hasNext();) {
            HeadSlab headSlab = iterator.next();
            if (headSlab != null) {
                String basedOn = headSlab.getBasedOn();
                String cityresult = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, empGetHeads.getPostingCity());
                List<CityMaster> cityList = new Gson().fromJson(cityresult, new TypeToken<List<CityMaster>>() {
                }.getType());
                CityMaster city = null;
                if (cityList != null) {
                    city = cityList.get(0);
                }
                if (city == null) {
                    flag = false;
                }
                String cityCategoryId = city.getCityCategory();
                if (cityCategoryId == null && cityCategoryId.isEmpty()) {
                    flag = false;
                }
                if (basedOn.equalsIgnoreCase(BASED_ON_DDO)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CITY_CITYCATEGORY)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getCityCategory().equals(cityCategoryId)) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_SALARY_TYPE)) {
                    if (headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CLASS)) {
                    if (headSlab.getClas().equalsIgnoreCase(empGetHeads.getClassMaster())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_NATURE_TYPE)) {
                    if (headSlab.getNatureType().equalsIgnoreCase(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_CITY_CITYCATEGORY)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getCityCategory().equals(cityCategoryId)) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_SALARY_TYPE)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getSalaryType().equals(empGetHeads.getSalaryType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_CLASS)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getClas().equals(empGetHeads.getClassMaster())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_NATURE_TYPE)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getNatureType().equals(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_City_CityCategory_Salary_Type)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType()) && headSlab.getCityCategory().equals(cityCategoryId)) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CITY_CITYCATEGORY_CLASS)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getClas().equals(empGetHeads.getClassMaster()) && headSlab.getCityCategory().equals(cityCategoryId)) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CITY_CITYCATEGORY_NATURE_TYPE)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getNatureType().equals(empGetHeads.getNatureType()) && headSlab.getCityCategory().equals(cityCategoryId)) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_SALARY_TYPE_CLASS)) {
                    if (headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType()) && headSlab.getClas().equals(empGetHeads.getClassMaster())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_SALARY_TYPE_NATURE_TYPE)) {
                    if (headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType()) && headSlab.getNatureType().equalsIgnoreCase(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CLASS_NATURE_TYPE)) {
                    if (headSlab.getClas().equalsIgnoreCase(empGetHeads.getClassMaster()) && headSlab.getNatureType().equals(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_CITY_CITYCATEGORY_SALARY_TYPE)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getCity().equals(empGetHeads.getPostingCity()) && headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType()) && headSlab.getCityCategory().equals(cityCategoryId)) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_CITY_CITYCATEGORY_CLASS)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getCityCategory().equals(cityCategoryId) && headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getClas().equalsIgnoreCase(empGetHeads.getClassMaster())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_DDO_CITY_CITYCATEGORY_NATURE_TYPE)) {
                    if (headSlab.getDdo().equalsIgnoreCase(empGetHeads.getDdo()) && headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getCityCategory().equals(cityCategoryId) && headSlab.getNatureType().equals(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CITY_CITYCATEGORY_SALARY_TYPE_CLASS)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CITY_CITYCATEGORY_SALARY_TYPE_NATURE_TYPE)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getCityCategory().equals(cityCategoryId) && headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType()) && headSlab.getNatureType().equals(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                } else if (basedOn.equalsIgnoreCase(BASED_ON_CITY_CITYCATEGORY_SALARY_TYPE_CLASS_NATURE_TYPE)) {
                    if (headSlab.getCity().equalsIgnoreCase(empGetHeads.getPostingCity()) && headSlab.getCityCategory().equals(cityCategoryId) && headSlab.getSalaryType().equalsIgnoreCase(empGetHeads.getSalaryType()) && headSlab.getClas().equalsIgnoreCase(empGetHeads.getClassMaster()) && headSlab.getNatureType().equals(empGetHeads.getNatureType())) {
                        ResultantHeadSlab = headSlab;
                    } else {
                        flag = false;
                    }
                }
            }
            if (ResultantHeadSlab == null) {
                flag = false;
            } else {
                return ResultantHeadSlab;
            }
        }
        if (flag == false) {
            return null;
        } else if (flag == true) {
            if (ResultantHeadSlab != null) {
                return ResultantHeadSlab;
            } else {
                return null;
            }
        }
        return null;
    }

    private static long getAssociationFeeForSHF(String associationId) throws Exception {
        if (associationId != null && !associationId.isEmpty()) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.ASSOCIATION_TABLE, associationId);
            List<Association> Associationlist = new Gson().fromJson(result, new TypeToken<List<Association>>() {
            }.getType());
            if (Associationlist != null) {
                Association associtionObj = Associationlist.get(0);
                if (associtionObj.getStatus().equalsIgnoreCase(ApplicationConstants.ACTIVE)) {
                    try {
                        long associationFee = associtionObj.getFees();
                        //System.out.println("//////////////////////////////////////////////////////////////////////////////////////");
                        //System.out.println("");
                        //System.out.println(associationFee);
                        //System.out.println("//////////////////////////////////////////////////////////////////////////////////////");
                        return associationFee;
                    } catch (Exception e) {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public String fetchForUpdate(String employeeId) throws Exception {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, employeeId);
        List<Employee> employeeList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        if (employeeList == null || employeeList.size() < 1) {
            return null;
        }
        Employee emp = employeeList.get(0);
        List<EarningHeadsDetails> listOfEarningHeads = emp.getEarningHeads();
        if (listOfEarningHeads != null) {
            listOfEarningHeads = fetchSalaryHeadsWhileUpdate(listOfEarningHeads);
            Collections.sort(listOfEarningHeads, new EarningHeadsSortByDisplayLeave());
            emp.setEarningHeads(listOfEarningHeads);
        }
        List<EarningHeadsDetails> listOfDeductionHeads = new ArrayList<EarningHeadsDetails>();
        listOfDeductionHeads = emp.getDeductionHeads();
        if (listOfDeductionHeads != null) {
            listOfDeductionHeads = fetchSalaryHeadsWhileUpdate(listOfDeductionHeads);
            Collections.sort(listOfDeductionHeads, new EarningHeadsSortByDisplayLeave());
            emp.setDeductionHeads(listOfDeductionHeads);
        }
        //System.out.println("**************************");
        return new Gson().toJson(emp);
    }

    public List<EarningHeadsDetails> fetchSalaryHeadsWhileUpdate(List<EarningHeadsDetails> li) throws Exception {
        Map<String, SalaryHead> CityMap = new HashMap<String, SalaryHead>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> liSalaryHead = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = liSalaryHead.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next);
        }
        for (int i = 0; i < li.size(); i++) {
            for (Map.Entry<String, SalaryHead> entry : CityMap.entrySet()) {
                String key = entry.getKey();
                SalaryHead value = entry.getValue();
                if (entry.getKey().equals(li.get(i).getDescription())) {
                    li.get(i).setDescription(value.getDescription());
                    li.get(i).setSalaryHeadId(key);
                    li.get(i).setIsActive(value.getActive());
                    li.get(i).setMapping(value.getMapping());
                    li.get(i).setIsBasic(value.getIsBasic());
                    li.get(i).setDisplayLevel(value.getDisplayLevel());
                }
            }
        }
        return li;
    }

    private static String ec = "";

    public String returnValidEmployeeCode(String employeeCode) throws Exception {
        //System.out.println(employeeCode);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        String resulatantEmployeeCode = new String();
        conditionMap.put("employeeCode", employeeCode);
        //  if (employeeCode != null) {
        String employeeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        if (employeeJson != null) {
            String[] subString = employeeCode.split("-");
            long l = Long.parseLong(subString[subString.length - 1]) + 1;
            for (int i = 0; i < subString.length; i++) {
                String stri = subString[i];
                if (i == subString.length - 1) {
                    resulatantEmployeeCode += "-" + l;
                    returnValidEmployeeCode(resulatantEmployeeCode);
                }
                resulatantEmployeeCode += stri;
            }
        } else {
            ec = employeeCode;
            return employeeCode;
        }
        return ec;
    }

    public static boolean postCheckingMethodInUpdate(Employee employee) throws Exception {
        HashMap<String, String> desigcondiMap = new HashMap<String, String>();
        desigcondiMap.put("ddo", employee.getDdo());
        desigcondiMap.put("designation", employee.getDesignation());
        desigcondiMap.put("grade", employee.getGrade());
        desigcondiMap.put("fundType", employee.getFundType());
        desigcondiMap.put("budgetHead", employee.getBudgetHead());
        desigcondiMap.put("natureType", employee.getNatureType());
        desigcondiMap.put("desciplineName", employee.getDiscipline());
        desigcondiMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String desigresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, desigcondiMap);
        if (desigresult == null) {
            return false;
        }
        List<DesignationFundTypeMapping> desiglist = new Gson().fromJson(desigresult, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        if (desiglist == null || desiglist.size() < 1) {
            return false;
        }
        DesignationFundTypeMapping desigsinglist = desiglist.get(0);
        int postForThisCategory = 0;
        List<CategoryPosts> categoryposts = desigsinglist.getCategoryposts();
        for (Iterator<CategoryPosts> iterator = categoryposts.iterator(); iterator.hasNext();) {
            CategoryPosts next = iterator.next();
            if (next.getCategoory().equals(employee.getCategory())) {
                postForThisCategory = Integer.parseInt(next.getPosts());
            }
        }
        if (postForThisCategory < 1) {
            return false;
        } else {
            HashMap<String, String> saveconditionMap = new HashMap<String, String>();
            saveconditionMap.put("ddo", employee.getDdo());
            saveconditionMap.put("designation", employee.getDesignation());
            saveconditionMap.put("grade", employee.getGrade());
            saveconditionMap.put("fundType", employee.getFundType());
            saveconditionMap.put("budgetHead", employee.getBudgetHead());
            saveconditionMap.put("natureType", employee.getNatureType());
            saveconditionMap.put("discipline", employee.getDiscipline());
            saveconditionMap.put("category", employee.getCategory());
            saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String totalNoOfEmpBelongToThisCategory = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, saveconditionMap);
            List<Employee> emplist = new Gson().fromJson(totalNoOfEmpBelongToThisCategory, new TypeToken<List<Employee>>() {
            }.getType());
            if (emplist == null) {
                return true;
            } else if (emplist.size() > 0) {
                if (emplist.size() <= postForThisCategory) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    private boolean isPostAreAvailbleWhileUpdating(Employee previousEmp, Employee newEmployee) throws Exception {
        //System.out.println(new Gson().toJson(previousEmp));
        //System.out.println("***********************************************");
        //System.out.println(new Gson().toJson(newEmployee));
        if ((previousEmp.getNatureType().equalsIgnoreCase(newEmployee.getNatureType())) && (previousEmp.getCategory().equalsIgnoreCase(newEmployee.getCategory())) && (previousEmp.getDiscipline().equalsIgnoreCase(newEmployee.getDiscipline())) && (previousEmp.getBudgetHead().equalsIgnoreCase(newEmployee.getBudgetHead())) && (previousEmp.getDdo().equalsIgnoreCase(newEmployee.getDdo())) && (previousEmp.getDesignation().equalsIgnoreCase(newEmployee.getDesignation())) && (previousEmp.getGrade().equalsIgnoreCase(newEmployee.getGrade())) && (previousEmp.getFundType().equalsIgnoreCase(newEmployee.getFundType()))) {
            return true;
        } else {
            boolean result = postCheckingMethod(newEmployee);
            return result;
        }
    }

    /**
     * @author chaitu
     * @description fetchEmployees() method will get the employees based on
     * empcodes
     * @table employee
     * @param String[] empCodes
     * @return ArrayList<Employee>
     * @throws Exception
     */
    public static ArrayList<Employee> fetchEmployees(String[] empCodes) throws Exception {
        ArrayList<Employee> empList = null;
        if (empCodes == null || empCodes.length == 0) {
            return empList;
        }

        List<Query> list = new ArrayList<Query>();
        list.add(Common.createNameValueConditionQuery(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE, ApplicationConstants.EQUAL));
        list.add(Common.createNameMatchValuesConditionQuery(ApplicationConstants.EMPLOYEE_CODE, empCodes, ApplicationConstants.LOGICAL_IN));

        //fetch employees from employee table
        String dbOutput = DBManager.getDbConnection().fetchRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, list);
        if (dbOutput != null) {
            empList = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<Employee>>() {
            }.getType());
        }
        return empList;
    }
//For Leave management 

    public JSONObject searchItFinallyV2(Employee emp, String leaveType, String year) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);

        BasicDBObject regexQuery = new BasicDBObject();

        if (emp.getDdo() != null && !emp.getDdo().equalsIgnoreCase("")) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", Pattern.quote(emp.getDdo())).append("$options", "i"));
        }
        if (emp.getLocation() != null && emp.getLocation().length() != 0) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", Pattern.quote(emp.getLocation())).append("$options", "i"));
        }
        if (emp.getDepartment() != null && emp.getDepartment().length() != 0) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", Pattern.quote(emp.getDepartment())).append("$options", "i"));
        }
        if (emp.getDesignation() != null && emp.getDesignation().length() != 0) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", Pattern.quote(emp.getDesignation())).append("$options", "i"));
        }
        if (emp.getNatureType() != null && emp.getNatureType().length() != 0) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", Pattern.quote(emp.getNatureType())).append("$options", "i"));
        }
        if (emp.getPostingCity() != null && emp.getPostingCity().length() != 0) {
            regexQuery.put("postingCity",
                    new BasicDBObject("$regex", Pattern.quote(emp.getPostingCity())).append("$options", "i"));
        }
        if (emp.getFundType() != null && emp.getFundType().length() != 0) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", Pattern.quote(emp.getFundType())).append("$options", "i"));
        }
        if (emp.getBudgetHead() != null && emp.getBudgetHead().length() != 0) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", Pattern.quote(emp.getBudgetHead())).append("$options", "i"));
        }
        if (emp.getEmployeeName() != null && emp.getEmployeeName().length() != 0) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", Pattern.quote(emp.getEmployeeName())).append("$options", "i"));
        }
        if (emp.getEmployeeCode() != null && emp.getEmployeeCode().length() != 0) {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", Pattern.quote(emp.getEmployeeCode())).append("$options", "i"));
        }
        if (emp.getEmployeeCodeM() != null && emp.getEmployeeCodeM().length() != 0) {
            regexQuery.put("employeeCodeM",
                    new BasicDBObject("$regex", Pattern.quote(emp.getEmployeeCodeM())).append("$options", "i"));
        }
        if (emp.getNatureType() != null && emp.getNatureType().length() != 0) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", Pattern.quote(emp.getNatureType())).append("$options", "i"));
        }
        if (emp.getClassMaster() != null && emp.getClassMaster().length() != 0) {
            regexQuery.put("classMaster",
                    new BasicDBObject("$regex", Pattern.quote(emp.getClassMaster())).append("$options", "i"));
        }
        regexQuery.put("EmployeeLeftStatus", ApplicationConstants.NO);
        /*if (emp.getSalaryBillType() != null) {
         regexQuery.put("classMaster",
         new BasicDBObject("$regex", emp.getClassMaster()));
         }*/
        regexQuery.put("status",
                new BasicDBObject("$regex", Pattern.quote("Active")));
        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);
        String leaveTypeJson = new LeaveTypeManager().fetchById(leaveType);
        LeaveTypeMaster leaveTypeObj = new Gson().fromJson(leaveTypeJson, new TypeToken<LeaveTypeMaster>() {
        }.getType());
        List<LeaveTypeDetails> leaveTypeDetails = leaveTypeObj.getLeaveTypeDetails();
        int eligibilityInMonthsInInt = 0;
        String fixedTimeIssueInInt = "No";
        for (Iterator<LeaveTypeDetails> iterator = leaveTypeDetails.iterator(); iterator.hasNext();) {
            LeaveTypeDetails next = iterator.next();
            // && next.getFixedTimeIssue().equals(ApplicationConstants.YES)
            if (next.getNatureType().equals(emp.getNatureType()) && next.getEmployeeCategory().equals(emp.getClassMaster())) {
                String eligibilityInmonthsInString = next.getEligibility();
                if (eligibilityInmonthsInString != null && !eligibilityInmonthsInString.isEmpty() && !eligibilityInmonthsInString.equals("") && !eligibilityInmonthsInString.equals(" ")) {
                    eligibilityInMonthsInInt = Integer.parseInt(eligibilityInmonthsInString);
                }
                if (next.getFixedTimeIssue().equalsIgnoreCase("Yes")) {
                    fixedTimeIssueInInt = next.getTotalTimeIssue();
                }
            }
            if (eligibilityInMonthsInInt != 0) {
                break;
            }
        }

        //Adding the query condition to check the eligiblity 
        if (eligibilityInMonthsInInt != 0) {
            c.add(Calendar.MONTH, -eligibilityInMonthsInInt);
            long timeInmillisecond = c.getTimeInMillis();
            regexQuery.put("dateOfJoiningInMillisecond",
                    new BasicDBObject("$lte", timeInmillisecond));
        }
        if (!leaveTypeObj.getGender().equalsIgnoreCase(ApplicationConstants.GENDER_BOTH)) {
            regexQuery.put("gender", leaveTypeObj.getGender());
        }

        DBCursor cursor2 = collection.find(regexQuery);

        JSONObject obj = new JSONObject();

        List<Employee> employeeList = new ArrayList<Employee>();
        List<EmployeeLeaveAssignment> assigningList = new ArrayList<EmployeeLeaveAssignment>();
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<Employee>() {
            }.getType();
            Employee em = new Gson().fromJson(ob.toString(), type);

            if (!fixedTimeIssueInInt.equalsIgnoreCase("No")) {
                HashMap<String, String> conditionMap = new HashMap<String, String>();
                conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                conditionMap.put("employeeId", ((LinkedTreeMap) em.getId()).get("$oid") + "");
                conditionMap.put("leaveType", leaveType);
                String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
                List<LeaveTransaction> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
                }.getType());
                try {
                    if (list == null || list.size() < Integer.parseInt(fixedTimeIssueInInt)) {
                        employeeList.add(em);
                    }
                } catch (Exception e) {

                }
            } else {
                employeeList.add(em);
            }
        }

        if (employeeList.size() > 0) {

            try {
                employeeList = getDDO(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getPostingDDO(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getNature(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getDesignation(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getPostedDesignation(employeeList);

            } catch (Exception e) {
            }
            try {

                employeeList = getLocation(employeeList);
            } catch (Exception e) {
            }
            String maxLeavePerYearString = "0";
            for (Iterator<LeaveTypeDetails> iterator = leaveTypeDetails.iterator(); iterator.hasNext();) {
                LeaveTypeDetails next = iterator.next();
                if (next.getNatureType().equals(emp.getNatureType()) && next.getEmployeeCategory().equals(emp.getClassMaster())) {
                    maxLeavePerYearString = next.getMaxLeavePerYear();
                }
            }

//            if ((leaveType != null && !leaveType.isEmpty()) && (employeeList != null && employeeList.size() > 0)) {
//                employeeList = checkTheEligibilityCriteria(employeeList, leaveType, emp.getNatureType(), emp.getClassMaster());
//            }
//            filterEmployeeByCategory(employeeList, emp);
            obj.put(ApplicationConstants.LEAVE_ASSIGNED, filterEmployeeByLeaveAssigned(employeeList, leaveType, year));
            for (Iterator<Employee> it = employeeList.iterator(); it.hasNext();) {
                Employee employee = it.next();
                EmployeeLeaveAssignment empAssigningObj = new EmployeeLeaveAssignment();
                empAssigningObj.setEmployeeName(employee.getEmployeeName());
                empAssigningObj.setEmployeeId(((LinkedTreeMap) employee.getId()).get("$oid") + "");
                empAssigningObj.setEmployeeCode(employee.getEmployeeCode());
                empAssigningObj.setEmployeeCodeM(employee.getEmployeeCodeM());
                empAssigningObj.setDdo(employee.getDdo());
                empAssigningObj.setDesignation(employee.getPostedDesignation());
                empAssigningObj.setNatureType(employee.getNatureType());
                empAssigningObj.setEmployeeCategory(employee.getClassMaster());
                empAssigningObj.setLeaveType(leaveType);
                empAssigningObj.setTotalEarnedLeaves(maxLeavePerYearString);
                empAssigningObj.setCurrentYearLeaves("0");
                assigningList.add(empAssigningObj);
            }
            obj.put(ApplicationConstants.LEAVE_UNASSIGNED, assigningList);

        }
        return obj;
    }

    private void filterEmployeeByCategory(List<Employee> empList, Employee empSrchCrtra) {

        for (Employee empFromDb : empList) {
            String empSearchCtgry = empSrchCrtra.getSalaryBillType();
            String isTchng = "no";
            if (empSearchCtgry.equalsIgnoreCase("teaching")) {
                isTchng = "yes";
            }
            String clsMstrName = empFromDb.getClassMaster();

            List<Class> clsMngr = null;

            try {
                Map<String, String> smap = new HashMap<String, String>();
                smap.put("name", clsMstrName);
                clsMngr = new Gson().fromJson(DBManager.getDbConnection().
                        fetchAllRowsByConditions(ApplicationConstants.CLASS_TABLE, smap),
                        new TypeToken<List<Class>>() {
                        }.getType());
            } catch (Exception exception) {
            }

            if (clsMngr != null) {
                Class cls = clsMngr.get(0);

                String isTchngFrmDb = cls.getTeaching();

                if (!isTchng.equalsIgnoreCase(isTchngFrmDb)) {
                    empList.remove(empFromDb);
                }
            }
        }
    }

    private List<EmployeeLeaveAssignment> filterEmployeeByLeaveAssigned(List<Employee> empList, String leaveType, String year) {

        List<EmployeeLeaveAssignment> empLveAssgnList = new ArrayList();

        Map<String, String> filterMap = new HashMap();
        ListIterator<Employee> lItr = empList.listIterator();
        while (lItr.hasNext()) {
            Employee emp = lItr.next();
            String id = ((LinkedTreeMap) emp.getId()).get("$oid") + "";

            try {
                filterMap.clear();
                filterMap.put(ApplicationConstants.EMPLOYEE_LEAVE_ASSGNMNT_ID, id);
                filterMap.put(ApplicationConstants.YEAR, year);
                filterMap.put(ApplicationConstants.LEAVE_TYPE, leaveType);
                filterMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                String dbRow = DBManager.getDbConnection().fetchAllRowsByConditions(
                        ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, filterMap);
                if (dbRow != null) {
                    List<EmployeeLeaveAssignment> empLveAsgndList = new Gson().fromJson(dbRow,
                            new TypeToken<List<EmployeeLeaveAssignment>>() {
                            }.getType());
                    if (empLveAsgndList != null && empLveAsgndList.size() > 0) {
                        EmployeeLeaveAssignment empLveAsgnd = empLveAsgndList.get(0);

                        if (empLveAsgnd != null) {
                            empLveAsgnd.setDesignationId(empLveAsgnd.getDesignation());
                            empLveAsgnd.setNatureTypeId(empLveAsgnd.getNatureType());
                            empLveAsgnd.setDdoId(empLveAsgnd.getDdo());
                            empLveAsgnd.setDdo(emp.getDdo());
                            empLveAsgnd.setEmployeeCodeM(emp.getEmployeeCodeM());
                            empLveAsgnd.setEmployeeName(emp.getEmployeeName());
                            empLveAsgnd.setEmployeeCode(emp.getEmployeeCode());
                            empLveAsgnd.setDesignation(emp.getDesignation());
                            empLveAsgnd.setNatureType(emp.getNatureType());
                            empLveAssgnList.add(empLveAsgnd);
                            lItr.remove();
                        }
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return empLveAssgnList;
    }

    /**
     * @author deepak
     * @description : This method is getting all the employee from employee
     * table and then we are fetching earning and deduction head from salary
     * head table and if that particular head is present its returning true else
     * return false Parameter : Description(String) Result : Boolean(true or
     * false)
     */
    public static boolean checkSalaryHeadInEmployee(String description) throws Exception {
        if (description == null || description.isEmpty()) {
            return false;
        }
        SalaryHead salaryHead = null;
        String earningDescription = "";
        String deductionDescription = "";

        //get employees
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        List<Employee> employeeList = new EmployeeManager().fetch(conditionMap);

        if (employeeList != null && employeeList.size() > 0) {
            for (int x = 0; x < employeeList.size(); x++) {
                Employee employee = employeeList.get(x);

                //Verifying Earning Heads
                List<EarningHeadsDetails> earningHeads = employee.getEarningHeads();
                for (int i = 0; i < earningHeads.size(); i++) {
                    //Getting Salary Heads
                    salaryHead = SalaryHeadManager.get(earningHeads.get(i).getDescription());
                    earningDescription = salaryHead.getShortDescription();
                    if (earningDescription != null && description.equalsIgnoreCase(earningDescription)) {
                        return true;
                    }

                }

                //Verifying Deductions Heads
                List<EarningHeadsDetails> deductionHeads = employee.getDeductionHeads();
                for (int i = 0; i < deductionHeads.size(); i++) {
                    //Getting Salary Heads
                    salaryHead = SalaryHeadManager.get(deductionHeads.get(i).getDescription());
                    deductionDescription = salaryHead.getShortDescription();
                    if (deductionDescription != null && description.equalsIgnoreCase(deductionDescription)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public List<Document> getFilteredEmployeeList(List<HashMap> filterList) throws Exception {

        Document filterDocument = Common.getFilterDocumentForFilterList(filterList);
        FindIterable<Document> iterable = Common.getCollection(ApplicationConstants.EMPLOYEE_TABLE).
                find(filterDocument);
        List<Document> documentList = new ArrayList();
        DDOManager ddoManager = new DDOManager();
        DesignationManager designationManager = new DesignationManager();
        if (iterable != null) {
            MongoCursor<Document> cursor = iterable.iterator();
            if (cursor != null) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    document.put("_id", ((ObjectId) document.get("_id")).toString());
                    if (document.get("ddo") != null) {
                        try {
                            DDO ddo = ddoManager.fetch(document.get("ddo") + "");
                            document.put("ddoname", ddo.getDdoName());
                        } catch (Exception exception) {
                        }
                    }
                    if (document.get("designation") != null) {
                        try {
                            String designationString = designationManager.fetch(document.get("designation") + "");
                            if (designationString != null) {
                                Designation designation = new Gson().fromJson(
                                        designationString, new TypeToken<Designation>() {
                                        }.getType());
                                document.put("designationname", designation.getDesignation());
                            }
                        } catch (Exception exception) {
                        }
                    }
                    documentList.add(document);
                }
            }
        }

        return documentList;
    }

    public String EmpSearch(Employee emp) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);

        BasicDBObject regexQuery = new BasicDBObject();

        if (emp.getDdo() != null && !emp.getDdo().equalsIgnoreCase("")) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", Pattern.quote(emp.getDdo())).
                    append("$options", "i"));
        }
        if (emp.getLocation() != null && !emp.getLocation().equalsIgnoreCase("")) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", Pattern.quote(emp.getLocation())).
                    append("$options", "i"));
        }
        if (emp.getDepartment() != null && !emp.getDepartment().equalsIgnoreCase("")) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", Pattern.quote(emp.getDepartment()))
                    .append("$options", "i"));
        }
        if (emp.getDesignation() != null) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", Pattern.quote(emp.getDesignation())).
                    append("$options", "i"));
        }
        if (emp.getNatureType() != null) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", Pattern.quote(emp.getNatureType())).
                    append("$options", "i"));
        }
        if (emp.getPostingCity() != null) {
            regexQuery.put("postingCity",
                    new BasicDBObject("$regex", Pattern.quote(emp.getPostingCity())).
                    append("$options", "i"));
        }
        if (emp.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", Pattern.quote(emp.getFundType())).
                    append("$options", "i"));
        }
        if (emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", Pattern.quote(emp.getBudgetHead())).
                    append("$options", "i"));
        }
//        if (emp.getEmployeeName() != null) {
//            regexQuery.put("employeeName",
//                    new BasicDBObject("$regex", emp.getEmployeeName()));
//        }
        if (emp.getEmployeeName() != null) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", Pattern.compile(emp.getEmployeeName(), Pattern.CASE_INSENSITIVE)));
        }
        if (emp.getEmployeeCode() != null) {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", Pattern.quote(emp.getEmployeeCode())).
                    append("$options", "i"));
        }
        if (emp.getEmployeeCodeM() != null) {
            regexQuery.put("employeeCodeM",
                    new BasicDBObject("$regex", Pattern.quote(emp.getEmployeeCodeM())).
                    append("$options", "i"));
        }
        if (emp.getSalaryBillType() != null) {
            regexQuery.put("salaryBillType",
                    new BasicDBObject("$regex", Pattern.quote(emp.getSalaryBillType())).
                    append("$options", "i"));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        //System.out.println(regexQuery);

        DBCursor cursor2 = collection.find(regexQuery);

        List<Employee> employeeList = new ArrayList<Employee>();
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<Employee>() {
            }.getType();
            Employee em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }
        if (employeeList.size() > 0) {
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
                employeeList = getPostingDDO(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getAssociation(employeeList);
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
                employeeList = getPostedDesignation(employeeList);

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
                employeeList = getEmployeeLeftStatus(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getHeadSlab(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getReportingTo(employeeList);
            } catch (Exception e) {
            }
            try {

                employeeList = getClass(employeeList);
            } catch (Exception e) {
            }
            try {

                employeeList = getLocation(employeeList);
            } catch (Exception e) {
            }
        }

        return new Gson().toJson(employeeList);
    }

    public String getEmployeeCodeUsingDdoLocationID(String ddoId, String locationId) throws Exception {
        String status = ApplicationConstants.SUCCESS;
        Map<String, String> resultMap = new HashMap<String, String>();
        String getEmployeeCode = null;
        String ddo = DBManager.getDbConnection().fetch(ApplicationConstants.DDO, ddoId);
        String location = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION, locationId);
        //Type casting the List of DDO and location to DDO & Location Objects 
        List<DDO> ddoList = new Gson().fromJson(ddo, new TypeToken<List<DDO>>() {
        }.getType());
        List<Location> locationList = new Gson().fromJson(location, new TypeToken<List<Location>>() {
        }.getType());
        //Selectiong the DDO and Location Objects
        DDO ddoObj = ddoList.get(0);
        Location locationObj = locationList.get(0);
        //Checking Weather the condition of the DDO & LOCATION 
        if (ddoObj.getStatus().equals(ApplicationConstants.ACTIVE) && locationObj.getStatus().equals(ApplicationConstants.ACTIVE)) {
            Map<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.DDO, ddoId);
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String ddoLocationMapping = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
            List<DdoLocationMap> ddoLocationMappedList = new Gson().fromJson(ddoLocationMapping, new TypeToken<List<DdoLocationMap>>() {
            }.getType());
            DdoLocationMap ddoLocationMapObj = ddoLocationMappedList.get(0);
            List<String> listOfLocation = ddoLocationMapObj.getLocation();
            if (listOfLocation.contains(locationId)) {
                getEmployeeCode = getEmployeeCodeUsingAQL(ddoObj.getCode(), locationObj.getLocationCode());
                if (!getEmployeeCode.isEmpty() && !getEmployeeCode.equals(null)) {
                    resultMap.put(ApplicationConstants.EMPLOYEE_CODE, getEmployeeCode);
                    resultMap.put(ApplicationConstants.DDO_ID, ddoId);
                    resultMap.put(ApplicationConstants.DDO_NAME, ddoObj.getDdoName());
                    resultMap.put(ApplicationConstants.LOCATION_ID, locationId);
                    resultMap.put(ApplicationConstants.LOCATION_NAME, locationObj.getLocationName());
                } else {
                    status = ApplicationConstants.EMPLOYEE_CODE_NOT_GENERATED;
                }

            } else {
                status = ApplicationConstants.LOCATION_IS_NOT_MAPPED;
            }

        } else {
            status = ApplicationConstants.DDO_LOCATION_ARE_NOT_ACTIVE;
        }
        resultMap.put(ApplicationConstants.STATUS, status);

        return new Gson().toJson(resultMap);
    }

    public String getReportingToNamesBasedOnDDOLocation(String ddoId, String locationId) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("ddo", ddoId);
        whereQuery.put("location", locationId);
        BasicDBObject fields = new BasicDBObject();
        fields.put("employeeName", 1);
        fields.put("designation", 1);
        fields.put("_id", 1);
        DBCursor cursor = collection.find(whereQuery, fields);
        List<Employee> reportingToList = new ArrayList<Employee>();
        while (cursor.hasNext()) {
            DBObject ob = cursor.next();
            Type type = new TypeToken<Employee>() {
            }.getType();
            Employee em = new Gson().fromJson(ob.toString(), type);
            reportingToList.add(em);
        }
        try {
            reportingToList = getDesignation(reportingToList);
        } catch (Exception e) {
        }
        //System.out.println(new Gson().toJson(reportingToList));
        return new Gson().toJson(reportingToList);
    }

    private List<Employee> checkTheEligibilityCriteria(List<Employee> employeeList, String leaveType, String natureType, String employeeCategory) throws Exception {
        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(referenceDate);

        String leaveTypeJson = new LeaveTypeManager().fetch(leaveType);
        LeaveTypeMaster leaveTypeObj = new Gson().fromJson(leaveTypeJson, new TypeToken<LeaveTypeMaster>() {
        }.getType());
        List<LeaveTypeDetails> leaveTypeDetails = leaveTypeObj.getLeaveTypeDetails();
        int eligibilityInMonthsInInt = 0;
        for (Iterator<LeaveTypeDetails> iterator = leaveTypeDetails.iterator(); iterator.hasNext();) {
            LeaveTypeDetails next = iterator.next();
            if (next.getNatureType().equals(natureType) && next.getEmployeeCategory().equals(employeeCategory) && next.getFixedTimeIssue().equals(ApplicationConstants.YES)) {
                String eligibilityInmonthsInString = next.getEligibility();
                if (eligibilityInmonthsInString != null && !eligibilityInmonthsInString.isEmpty() && !eligibilityInmonthsInString.equals("") && !eligibilityInmonthsInString.equals(" ")) {
                    eligibilityInMonthsInInt = Integer.parseInt(eligibilityInmonthsInString);
                }
            }
            if (eligibilityInMonthsInInt != 0) {
                break;
            }
        }
        if (eligibilityInMonthsInInt == 0) {
            return employeeList;
        } else {
            for (Iterator<Employee> iterator = employeeList.iterator(); iterator.hasNext();) {
                Employee next = iterator.next();
                long joiningDate = next.getDateOfJoiningInMillisecond();
                c.add(Calendar.MONTH, -eligibilityInMonthsInInt);
                long timeInmillisecond = c.getTimeInMillis();
                if (joiningDate > timeInmillisecond) {
                    iterator.remove();
                }
            }
        }
        return employeeList;
    }

    public static void main(String[] args) {
        Date referenceDate = new Date();
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTimeInMillis());
        c.setTime(referenceDate);
        c.add(Calendar.MONTH, -8);
        System.out.println(c.getTimeInMillis());

    }

}
