/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.accure.db.MongoFile;
import com.accure.hrms.dto.Nature;
import com.accure.budget.dto.FundType;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Discipline;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeeFileHolder;
import com.accure.hrms.dto.Religion;
import com.accure.hrms.manager.GetLatestEmpImageComparator;
import static com.accure.payroll.manager.EmployeeArrearManager.saveInMilliSecond;
import com.accure.pension.dto.PensionEmployeeDetails;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.dto.FileHolder;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author user
 */
public class PensionManager {

    public String fetch(String employeeId) throws Exception {
        if (employeeId == null || employeeId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, employeeId);
        List<PensionEmployeeDetails> employeeList = new Gson().fromJson(result, new TypeToken<List<PensionEmployeeDetails>>() {
        }.getType());
        if (employeeList == null || employeeList.size() < 1) {
            return null;
        }
        return new Gson().toJson(employeeList.get(0));
    }

    public String save(PensionEmployeeDetails employee, String userId) throws Exception {
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
        String emailJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, emailMap);
        if (emailJson == null) {
            HashMap<String, String> panMap = new HashMap<String, String>();
            panMap.put("status", ApplicationConstants.ACTIVE);
            panMap.put("panNo", employee.getPanNo());
            String panJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, panMap);
            if (panJson == null) {

                employee.setCreateDate(System.currentTimeMillis() + "");
                employee.setCreatedBy(userName);
                employee.setStatus(ApplicationConstants.ACTIVE);
                employee.setDdoName(fetchDDoName(employee.getDdo()));
                employee.setLocationName(fetchLocationName(employee.getLocation()));
                employee.setDepartmentName(fetchDepartmentName(employee.getDepartment()));
                employee.setDesignationName(fetchDesignationName(employee.getDesignation()));

                String employeeJson = new Gson().toJson(employee);
                employeeId = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, employeeJson);
                if (employeeId != null) {
                    resultMap.put("statusMessage", statusMessage);
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

    //Searching employee from pension master and then from employee master for Pension Employee Master
    public HashMap<String, Object> searchPensionEmployee(String employeeData) throws Exception {
        // Initializing Result Map which will have data from both employee and pension master
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        // Initializing List for Pension Master
        List<PensionEmployeeDetails> pensionEmployeesList = new ArrayList<PensionEmployeeDetails>();
        // Initializing List for Employee
        List<Employee> employeesList = new ArrayList<Employee>();
        //  Searching from Pension Master
        Type type = new TypeToken<PensionEmployeeDetails>() {
        }.getType();
        PensionEmployeeDetails pensionEmployee = new Gson().fromJson(employeeData, type);
        DB db = DBManager.getDB();

        BasicDBObject regexQuery = new BasicDBObject();
        if (pensionEmployee.getEmployeeCode() != null && !pensionEmployee.getEmployeeCode().isEmpty()) {
            regexQuery.put("employeeCode", new BasicDBObject("$regex", pensionEmployee.getEmployeeCode()));
        }
        if (pensionEmployee.getEmployeeCodeM() != null && !pensionEmployee.getEmployeeCodeM().isEmpty()) {
            regexQuery.put("employeeCodeM", new BasicDBObject("$regex", pensionEmployee.getEmployeeCodeM()));
        }
        if (pensionEmployee.getEmployeeName() != null && !pensionEmployee.getEmployeeName().isEmpty()) {
            regexQuery.put("employeeName", new BasicDBObject("$regex", pensionEmployee.getEmployeeName()));
        }
        if (pensionEmployee.getDdo() != null && !pensionEmployee.getDdo().isEmpty()) {
            regexQuery.put("ddo", new BasicDBObject("$eq", pensionEmployee.getDdo()));
        }
        if (pensionEmployee.getLocation() != null && !pensionEmployee.getLocation().isEmpty()) {
            regexQuery.put("location", new BasicDBObject("$eq", pensionEmployee.getLocation()));
        }
        if (pensionEmployee.getDepartment() != null && !pensionEmployee.getDepartment().isEmpty() && !pensionEmployee.getDepartment().equals("0")) {
            regexQuery.put("department", new BasicDBObject("$eq", pensionEmployee.getDepartment()));
        }
        if (pensionEmployee.getDesignation() != null && !pensionEmployee.getDesignation().isEmpty() && !pensionEmployee.getDesignation().equals("0")) {
            regexQuery.put("designation", new BasicDBObject("$eq", pensionEmployee.getDesignation()));
        }
        if (pensionEmployee.getNatureType() != null && !pensionEmployee.getNatureType().isEmpty() && !pensionEmployee.getNatureType().equals("0")) {
            regexQuery.put("natureType", new BasicDBObject("$eq", pensionEmployee.getNatureType()));
        }
        regexQuery.put("status", new BasicDBObject("$regex", "Active"));
        DBCollection collection = db.getCollection(ApplicationConstants.PENSION_EMPLOYEE_DETAILS);
        DBCursor cursor = collection.find(regexQuery);
        if (cursor != null && cursor.count() > 0) {
            while (cursor.hasNext()) {
                DBObject ob = cursor.next();
                Type type1 = new TypeToken<PensionEmployeeDetails>() {
                }.getType();
                PensionEmployeeDetails pensionEmployeeList = new Gson().fromJson(ob.toString(), type1);
                pensionEmployeesList.add(pensionEmployeeList);
            }
//            Adding pension List Employee to result map
            resultMap.put("pensionEmployee", pensionEmployeesList);
        }

//            Searching from Employee Master
        Type empType = new TypeToken<Employee>() {
        }.getType();
        Employee employee = new Gson().fromJson(employeeData, empType);

        BasicDBObject regexQuerys = new BasicDBObject();
        if (employee.getEmployeeCode() != null && !employee.getEmployeeCode().isEmpty()) {
            regexQuerys.put("employeeCode", new BasicDBObject("$regex", employee.getEmployeeCode()));
        }
        if (employee.getEmployeeCodeM() != null && !employee.getEmployeeCodeM().isEmpty()) {
            regexQuerys.put("employeeCodeM", new BasicDBObject("$regex", employee.getEmployeeCodeM()));
        }
        if (employee.getEmployeeName() != null && !employee.getEmployeeName().isEmpty()) {
            regexQuerys.put("employeeName", new BasicDBObject("$regex", employee.getEmployeeName()));
        }
        if (employee.getDdo() != null && !employee.getDdo().isEmpty()) {
            regexQuerys.put("ddo", new BasicDBObject("$eq", employee.getDdo()));
        }
        if (employee.getLocation() != null && !employee.getLocation().isEmpty()) {
            regexQuerys.put("location", new BasicDBObject("$eq", employee.getLocation()));
        }
        if (employee.getDepartment() != null && !employee.getDepartment().isEmpty() && !employee.getDepartment().equals("0")) {
            regexQuerys.put("department", new BasicDBObject("$eq", employee.getDepartment()));
        }
        if (employee.getDesignation() != null && !employee.getDesignation().isEmpty() && !employee.getDesignation().equals("0")) {
            regexQuerys.put("designation", new BasicDBObject("$eq", employee.getDesignation()));
        }
        if (employee.getNatureType() != null && !employee.getNatureType().isEmpty() && !employee.getNatureType().equals("0")) {
            regexQuerys.put("natureType", new BasicDBObject("$eq", employee.getNatureType()));
        }
        if (employee.getPostingCity() != null && !employee.getPostingCity().isEmpty() && !employee.getPostingCity().equals("0")) {
            regexQuerys.put("postingCity", new BasicDBObject("$eq", employee.getPostingCity()));
        }
        regexQuerys.put("EmployeeLeftStatus", new BasicDBObject("$regex", "Yes"));
        regexQuerys.put("status", new BasicDBObject("$regex", "Active"));

        //put multiple condtions on same column
        if (employee.getEmployeeLeftReason().equals("0")) {
            DBObject death = new BasicDBObject("EmployeeLeftReason", new BasicDBObject("$regex", "Death"));
            DBObject retirement = new BasicDBObject("EmployeeLeftReason", new BasicDBObject("$regex", "Retirement"));
            DBObject voluntaryRetirement = new BasicDBObject("EmployeeLeftReason", new BasicDBObject("$regex", "Voluntary Retirement"));
            BasicDBList or = new BasicDBList();
            or.add(death);
            or.add(retirement);
            or.add(voluntaryRetirement);
            regexQuerys.put("$or", or);

            //Checking & putting, if Retirement is selected
        } else if (employee.getEmployeeLeftReason().equalsIgnoreCase("Retirement")) {
            regexQuerys.put("EmployeeLeftReason", new BasicDBObject("$eq", employee.getEmployeeLeftReason()));
            //Checking & putting, if Death is selected
        } else if (employee.getEmployeeLeftReason().equalsIgnoreCase("Death")) {
            regexQuerys.put("EmployeeLeftReason", new BasicDBObject("$eq", employee.getEmployeeLeftReason()));
            //Checking & putting, if Voluntary Retirement is selected
        } else if (employee.getEmployeeLeftReason().equalsIgnoreCase("Voluntary Retirement")) {
            regexQuerys.put("EmployeeLeftReason", new BasicDBObject("$eq", employee.getEmployeeLeftReason()));
        }

        DBCollection empCollection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);
        DBCursor cursors = empCollection.find(regexQuerys);
        if (cursors != null && cursors.count() > 0) {
            while (cursors.hasNext()) {
                DBObject obj = cursors.next();
                Type type1 = new TypeToken<Employee>() {
                }.getType();
                Employee employeeList = new Gson().fromJson(obj.toString(), type1);
                employeesList.add(employeeList);
            }

//            Adding employee list to result map
            resultMap.put("employee", employeesList);
        }
        return resultMap;
    }

    public String update(PensionEmployeeDetails newEmployee, String employeeId, String userId) throws Exception {
        boolean email = false;
        boolean pan = false;
        PensionEmployeeDetails previousEmp = new Gson().fromJson(fetch(employeeId), new TypeToken<PensionEmployeeDetails>() {
        }.getType());
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

//        previousEmp.setEmployeeCodeM(newEmployee.getEmployeeCodeM());
        // previousEmp.setAcnumber(newEmployee.getAcnumber());
        //  previousEmp.setAssociation(newEmployee.getAssociation());
//        if (previousEmp.getAssociation() != null && !previousEmp.getAssociation().isEmpty()) {
//            if (!previousEmp.getAssociation().equalsIgnoreCase(newEmployee.getAssociation())) {
//                previousEmp.setAssociation(newEmployee.getAssociation());
//                previousEmp.setAssociationDate(System.currentTimeMillis());
//            }
//        } else if (newEmployee.getAssociation() != null && !newEmployee.getAssociation().isEmpty()) {
//            previousEmp.setAssociation(newEmployee.getAssociation());
//            previousEmp.setAssociationDate(System.currentTimeMillis());
//        }
        HashMap<String, String> emailMap = new HashMap<String, String>();
        emailMap.put("status", ApplicationConstants.ACTIVE);
        emailMap.put("email", newEmployee.getEmail());
        String emailJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, emailMap);
        List<PensionEmployeeDetails> emailList = new Gson().fromJson(emailJson, new TypeToken<List<PensionEmployeeDetails>>() {
        }.getType());
        //      List<Employee> employeeList = new ArrayList<Employee>();
        if (emailList != null) {
            for (PensionEmployeeDetails emp : emailList) {
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
            String panJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, panMap);

            List<PensionEmployeeDetails> panList = new Gson().fromJson(panJson, new TypeToken<List<PensionEmployeeDetails>>() {
            }.getType());
            //      List<PensionEmployeeDetails> employeeList = new ArrayList<PensionEmployeeDetails>();
            if (panList != null) {
                for (PensionEmployeeDetails emppan : panList) {
                    String panstr = ((Map<String, String>) emppan.getId()).get("$oid");
                    if (panstr.equalsIgnoreCase(employeeId)) {
                        pan = true;
                    }
                }
            }

            if (pan == true || panList == null) {

//                boolean isAvailable = isPostAreAvailbleWhileUpdating(previousEmp, newEmployee);
//                if (isAvailable == true) {
                previousEmp.setLastJoiningDate(newEmployee.getLastJoiningDate());
                previousEmp.setLastAppointmentDate(newEmployee.getLastAppointmentDate());
                previousEmp.setDateOfAppointment(newEmployee.getDateOfAppointment());
                previousEmp.setDateOfRetirement(newEmployee.getDateOfRetirement());
                previousEmp.setDateOfJoining(newEmployee.getDateOfJoining());
                previousEmp.setLeavingDate(newEmployee.getLeavingDate());
//                    if (newEmployee.getDateOfJoining() != null && !newEmployee.getDateOfJoining().isEmpty()) {
//                        previousEmp.setDateOfJoiningInMillisecond(saveInMilliSecond(newEmployee.getDateOfJoining()));
//                    } else {
//                        previousEmp.setDateOfJoiningInMillisecond(0);
//                    }
                previousEmp.setDepartment(newEmployee.getDepartment());
                previousEmp.setDesignation(newEmployee.getDesignation());
                previousEmp.setFundType(newEmployee.getFundType());
                previousEmp.setBudgetHead(newEmployee.getBudgetHead());
                previousEmp.setNatureType(newEmployee.getNatureType());
                previousEmp.setDiscipline(newEmployee.getDiscipline());

                if (newEmployee.getDob() != null && !newEmployee.getDob().isEmpty()) {
                    previousEmp.setDobInMillisecond(Long.parseLong(saveInMilliSecond((newEmployee.getDob()))));
                } else {
                    previousEmp.setDobInMillisecond(0);
                }
                if (newEmployee.getDateOfRetirement() != null && !newEmployee.getDateOfRetirement().isEmpty()) {
                    previousEmp.setDateOfRetirementInMillisecond(Long.parseLong(saveInMilliSecond(newEmployee.getDateOfRetirement())));
                } else {
                    previousEmp.setDateOfRetirementInMillisecond(0);
                }
                if (newEmployee.getDateOfAppointment() != null && !newEmployee.getDateOfAppointment().isEmpty()) {
                    previousEmp.setDateOfAppointmentInMillisecond(Long.parseLong(saveInMilliSecond(newEmployee.getDateOfAppointment())));
                } else {
                    previousEmp.setDateOfAppointmentInMillisecond(0);
                }
//        previousEmp.setEmployeeCode(newEmployee.getEmployeeCode());

                //  previousEmp.setIncrementDueDate(newEmployee.getIncrementDueDate());
//                    if (newEmployee.getIncrementDueDate() != null && !newEmployee.getIncrementDueDate().isEmpty()) {
//                        previousEmp.setIncrementDueDateInMilliSecond(saveInMilliSecond(newEmployee.getIncrementDueDate()));
//                    } else {
//                        previousEmp.setIncrementDueDateInMilliSecond(0);
//                    }
//                    previousEmp.setIncrementPercentage(newEmployee.getIncrementPercentage());
//                    previousEmp.setIsGazetted(newEmployee.getIsGazetted());
//                    previousEmp.setIsHandicapped(newEmployee.getIsHandicapped());
//                    previousEmp.setIsPgTeacher(newEmployee.getIsPgTeacher());
                previousEmp.setImageAvailable(newEmployee.isImageAvailable());
                previousEmp.setLeavingRemarks(newEmployee.getLeavingRemarks());
                previousEmp.setLocation(newEmployee.getLocation());

//                    if (previousEmp.getPtApplicable() != null && !previousEmp.getPtApplicable().isEmpty()) {
//                        if (!previousEmp.getPtApplicable().equalsIgnoreCase(newEmployee.getPtApplicable())) {
//                            previousEmp.setPtApplicable(newEmployee.getPtApplicable());
//                            previousEmp.setPtApplicableDate(Long.parseLong(System.currentTimeMillis() + ""));
//                        }
//                    } else if (newEmployee.getPtApplicable() != null && !newEmployee.getPtApplicable().isEmpty()) {
//                        previousEmp.setPtApplicable(newEmployee.getPtApplicable());
//                        previousEmp.setPtApplicableDate(Long.parseLong(System.currentTimeMillis() + ""));
//                    }
                previousEmp.setEmployeeCodeM(newEmployee.getEmployeeCodeM());
                previousEmp.setSalutationOption(newEmployee.getSalutationOption());
                previousEmp.setEmployeeName(newEmployee.getEmployeeName());
                previousEmp.setFatherName(newEmployee.getFatherName());
                previousEmp.setGender(newEmployee.getGender());
                previousEmp.setCategory(newEmployee.getCategory());
                previousEmp.setReligion(newEmployee.getReligion());
                previousEmp.setMaritalStatus(newEmployee.getMaritalStatus());
                previousEmp.setDob(newEmployee.getDob());
                previousEmp.setEmail(newEmployee.getEmail());
                previousEmp.setPanNo(newEmployee.getPanNo());
                previousEmp.setRemarks(newEmployee.getRemarks());
                previousEmp.setGrade(newEmployee.getGrade());
                previousEmp.setGradePay(newEmployee.getGradePay());
                previousEmp.setUpdateDate(System.currentTimeMillis() + "");
                previousEmp.setUpdatedBy(userName);

                boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, employeeId, new Gson().toJson(previousEmp));
                if (result) {

                }
                return ApplicationConstants.UPDATED;
//                } else {
//                    return ApplicationConstants.NO_POST_AVAILABLE;
//                }
            } else {
                return ApplicationConstants.PAN_AVAILABLE;
            }
        } else {
            return ApplicationConstants.EMAIL_AVAILABLE;
        }
    }

    public String EmpSearch(PensionEmployeeDetails employee) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", employee.getDdo());
        conditionMap.put("location", employee.getLocation());
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, conditionMap);
        List<PensionEmployeeDetails> employeeList = new Gson().fromJson(result1, new TypeToken<List<PensionEmployeeDetails>>() {
        }.getType());
        try {
            employeeList = getReligion(employeeList);
        } catch (Exception e) {
        }
//        try {
//            employeeList = getSalutation(employeeList);
//        } catch (Exception e) {
//        }
//        try {
//            employeeList = getMaritalStatus(employeeList);
//
//        } catch (Exception e) {
//        }

        try {
            employeeList = getDDo(employeeList);

        } catch (Exception e) {
        }

        try {
            employeeList = getLocation(employeeList);
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
            employeeList = getDesignation(employeeList);

        } catch (Exception e) {
        }

        try {

            employeeList = getLocation(employeeList);
        } catch (Exception e) {
        }

        String finalresult = new Gson().toJson(employeeList);
        //System.out.println(employeeList);
        return finalresult;
    }

    public static List<PensionEmployeeDetails> getReligion(List<PensionEmployeeDetails> employeeList) throws Exception {

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
                    employeeList.get(i).setReligionName(value);
                }
            }
        }
        return employeeList;
    }

    public List<PensionEmployeeDetails> getBudgetHead(List<PensionEmployeeDetails> employeeList) throws Exception {
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
                    employeeList.get(i).setBudgetHeadName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getDDo(List<PensionEmployeeDetails> employeeList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            String ddoId = employeeList.get(i).getReligion();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(ddoId)) {
                    employeeList.get(i).setDdoName(value);
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getLocation(List<PensionEmployeeDetails> employeeList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            String locationId = employeeList.get(i).getLocation();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(locationId)) {
                    employeeList.get(i).setLocationName(value);
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getDepartment(List<PensionEmployeeDetails> employeeList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DEPARTMENT_TABLE);
        List<Department> religionList = new Gson().fromJson(result, new TypeToken<List<Department>>() {
        }.getType());
        for (Iterator<Department> iterator = religionList.iterator(); iterator.hasNext();) {
            Department next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDepartment());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            String deptId = employeeList.get(i).getDepartment();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(deptId)) {
                    employeeList.get(i).setDepartmentName(value);
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getDesignation(List<PensionEmployeeDetails> employeeList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION);
        List<Designation> religionList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        for (Iterator<Designation> iterator = religionList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            String religionId = employeeList.get(i).getDesignation();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(religionId)) {
                    employeeList.get(i).setDesignationName(value);
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getDiscipline(List<PensionEmployeeDetails> employeeList) throws Exception {
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
                    employeeList.get(i).setDisciplineName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getNature(List<PensionEmployeeDetails> employeeList) throws Exception {
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
                    employeeList.get(i).setNatureTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployeeDetails> getFundType(List<PensionEmployeeDetails> employeeList) throws Exception {
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
                    employeeList.get(i).setFundTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public String fetchDDoName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, id);
        List<DDO> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<DDO>>() {
        }.getType());
        DDO religionobj = religionlist.get(0);
        return religionobj.getDdoName();
    }

    public String fetchLocationName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, id);
        List<Location> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<Location>>() {
        }.getType());
        Location religionobj = religionlist.get(0);
        return religionobj.getLocationName();
    }

    public String fetchDepartmentName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, id);
        List<Department> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<Department>>() {
        }.getType());
        Department religionobj = religionlist.get(0);
        return religionobj.getDepartment();
    }

    public String fetchDesignationName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION, id);
        List<Designation> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<Designation>>() {
        }.getType());
        Designation religionobj = religionlist.get(0);
        return religionobj.getDesignation();
    }

    public String fetchFundTypeName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, id);
        List<FundType> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<FundType>>() {
        }.getType());
        FundType religionobj = religionlist.get(0);
        return religionobj.getFundTypeName();
    }

    public String fetchNatureTypeName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, id);
        List<Nature> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<Nature>>() {
        }.getType());
        Nature religionobj = religionlist.get(0);
        return religionobj.getNatureName();
    }

    public String fetchDisciplineName(String id) throws Exception {
        String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, id);
        List<DDO> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<DDO>>() {
        }.getType());
        DDO religionobj = religionlist.get(0);
        return religionobj.getDdoName();
    }

    public boolean delete(String employeeId, String userId) throws Exception {
        boolean result;
        if (employeeId == null || employeeId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionEmployeeDetails>() {
        }.getType();
        String employee = new PensionManager().fetch(employeeId);
        if (employee == null || employee.isEmpty()) {
            return false;
        }

//        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMP_ATTENDANCE_TABLE, "idStr", employeeId) || DeleteDependencyManager.hasDependency(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, "employeePKey", employeeId)) {
//            return false;
//        } else {
        PensionEmployeeDetails employeerJson = new Gson().fromJson(employee, type);
        employeerJson.setStatus(ApplicationConstants.INACTIVE);
        result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_EMPLOYEE_DETAILS, employeeId, new Gson().toJson(employeerJson));
//            if (result == true) {
//                employeeHistory(employeerJson, employeeId);
//            }

        //}
        return result;
    }

    public String storeFile(FileItem item, String resultId) throws Exception {

        if (item == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ownerID", resultId);
        String json = DBManager.getDbConnection().listAllFilesByCondition("pensionemployeeImage", conditionMap);

        InputStream filecontent = item.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(filecontent);
        MongoFile mf = new MongoFile();
        FileHolder fileholder = new FileHolder();
        fileholder.setFileName(item.getName());
        fileholder.setOwnerID(resultId);
        fileholder.setFileExt(FilenameUtils.getExtension(item.getName()));
        fileholder.setDocType("pensionemployeeImage");
        fileholder.setFolderName("pensionemployeeImage");
        fileholder.setSize(item.getSize() + "");
        fileholder.setInputStream(filecontent);
        fileholder.setBaos(baos);
        fileholder.setMimeType(URLConnection.guessContentTypeFromName(item.getName()));
        fileholder.setStatus("Active");

        String id = DBManager.getDbConnection().storeBinaryFile(fileholder);
        return id;
    }

    public MongoFile FetchImage(String employeeId) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ownerID", employeeId);
        String gson = DBManager.getDbConnection().listAllFilesByCondition("pensionemployeeImage", conditionMap);
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
        MongoFile mf = DBManager.getDbConnection().getGridFsFile(efh.get(0).getIdStr(), "pensionemployeeImage");
        return mf;
    }
}
