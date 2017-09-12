/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.Employee;
import static com.accure.hrms.manager.EmployeeManager.getDepartment;
import static com.accure.hrms.manager.EmployeeManager.getDesignation;
import static com.accure.hrms.manager.EmployeeManager.getReligion;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author upendra
 */
public class EmpDemographicManager {

    public boolean saveDemographicData(Employee bank, String userid, String empid) throws Exception {

        Employee emp = new EmployeeManager().fetchEmployee(empid);
        emp.setCurrentAddress(bank.getCurrentAddress());
        emp.setCurrentContactNo(bank.getCurrentContactNo());
        emp.setPermanentAddress(bank.getPermanentAddress());
        emp.setPermanentContactNo(bank.getPermanentContactNo());
        emp.setIdentificationMarks(bank.getIdentificationMarks());
        emp.setTechQualification(bank.getTechQualification());
        emp.setReligion(bank.getReligion());
        emp.setGender(bank.getGender());
        emp.setCategory(bank.getCategory());
        emp.setHeight(bank.getHeight());
        emp.setScholarship(bank.getScholarship());
        emp.setRefference(bank.getRefference());
        emp.setRemarks(bank.getRemarks());

        // String bankJson = new Gson().toJson(emp);
        boolean status = new EmpDemographicManager().EmpupdateinEmpDemographic(emp, empid, userid);
        return status;
    }

    public String viewDemographicData(String ddo) throws Exception {
        if (ddo == null) {
            return null;
        }

//        User user = new UserManager().fetch(ddo);
//        String ddoId = user.getDdoId();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("updateAt", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        List<Employee> employeeList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        try {
            employeeList = getDepartment(employeeList);

        } catch (Exception e) {
        }
         try {
            employeeList = getReligion(employeeList);
        } catch (Exception e) {
        }
        try {
            employeeList = getDesignation(employeeList);

        } catch (Exception e) {
        }

        return new Gson().toJson(employeeList);

    }

    public boolean deleteDemographicData(String empId, String userid) throws Exception {

        boolean result = new EmployeeManager().delete(empId, userid);
        return result;
    }

    public boolean updateDemographicData(Employee bank, String empId, String Userid) throws Exception {

        Employee emp = new EmployeeManager().fetchEmployee(empId);
        emp.setCurrentAddress(bank.getCurrentAddress());
        emp.setCurrentContactNo(bank.getCurrentContactNo());
        emp.setPermanentAddress(bank.getPermanentAddress());
        emp.setPermanentContactNo(bank.getPermanentContactNo());
        emp.setIdentificationMarks(bank.getIdentificationMarks());
        emp.setTechQualification(bank.getTechQualification());
        emp.setReligion(bank.getReligion());
        emp.setGender(bank.getGender());
        emp.setCategory(bank.getCategory());
        emp.setHeight(bank.getHeight());
        emp.setScholarship(bank.getScholarship());
        emp.setRefference(bank.getRefference());
        emp.setRemarks(bank.getRemarks());

        // String bankJson = new Gson().toJson(emp);
        boolean status = new EmpDemographicManager().EmpupdateinEmpDemographic(emp, empId, Userid);
        return status;
    }

    public boolean EmpupdateinEmpDemographic(Employee newEmployee, String employeeId, String userId) throws Exception {
        Employee previousEmp = new Gson().fromJson(new EmployeeManager().fetch(employeeId), new TypeToken<Employee>() {
        }.getType());
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        previousEmp.setCurrentAddress(newEmployee.getCurrentAddress());
        previousEmp.setCurrentContactNo(newEmployee.getCurrentContactNo());
        previousEmp.setPermanentAddress(newEmployee.getPermanentAddress());
        previousEmp.setPermanentContactNo(newEmployee.getPermanentContactNo());
        previousEmp.setIdentificationMarks(newEmployee.getIdentificationMarks());
        previousEmp.setTechQualification(newEmployee.getTechQualification());
        previousEmp.setHeight(newEmployee.getHeight());
        previousEmp.setScholarship(newEmployee.getScholarship());
        previousEmp.setRefference(newEmployee.getRefference());
        previousEmp.setRemarks(newEmployee.getRemarks());
        previousEmp.setReligion(newEmployee.getReligion());
        previousEmp.setGender(newEmployee.getGender());
        previousEmp.setCategory(newEmployee.getCategory());
        previousEmp.setUpdatedBy(userName);
        previousEmp.setUpdateDate(System.currentTimeMillis() + "");
        previousEmp.setUpdateAt(previousEmp.getDdo());
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, employeeId, new Gson().toJson(previousEmp));
        if (result) {
            new EmployeeManager().employeeHistory(previousEmp, employeeId);
        }
        return result;
    }
}
