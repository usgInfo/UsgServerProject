/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeePriviousJobDetails;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shwetha T S
 */
public class EmployeePreviousJobDetailManager {

    /**
     * This Method is to insert <code>EmployeePriviousJobDetails</code> Object
     * data to <code>ApplicationConstants.EMPLOYEE_PREVIOUS_JOB_TABLE</code>
     *
     * @param epjd <code>EmployeePriviousJobDetails</code> object data
     * @param userId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String.
     * @return
     * <p>
     * <code>_id</code> value of <code>EmployeePriviousJobDetails</code> Object
     * in hexadecimal String on successful insertion</p>
     * @throws Exception if second argument is <code>null</code>..
     */
    public String saveEmployeePriviousDetails(EmployeePriviousJobDetails epjd, String userId) throws Exception {

        if (epjd == null || userId == null) {
            return null;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        epjd.setCreateDate(System.currentTimeMillis() + "");
        epjd.setUpdateDate(System.currentTimeMillis() + "");
        epjd.setStatus(ApplicationConstants.ACTIVE);
        epjd.setCreatedBy(userName);
        epjd.setUpdatedBy(userName);

        String json = new Gson().toJson(epjd);

        String result = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_PREVIOUS_JOB_TABLE, json);
        return result;
    }

    /**
     * This is to get <code>EmployeePriviousJobDetails</code> Object data using
     * <code>_id</code> value
     *
     * @param primaryKey <code>_id</code> value of
     * <code>EmployeePriviousJobDetails</code> object in hexadecimal string.
     * @return
     * <p>
     * <code>EmployeePriviousJobDetails</code> Object data in JSON String format
     * </p>
     * @throws Exception if argument is <code>null</code>..
     */
    public String fetchEmployeePriviousDetails(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_PREVIOUS_JOB_TABLE, primaryKey);
        List<EmployeePriviousJobDetails> list = new Gson().fromJson(result, new TypeToken<List<EmployeePriviousJobDetails>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }

    /**
     * This method is to update <code>status</code> field of
     * <code>EmployeePriviousJobDetails</code> object.
     *
     * @param primaryKey <code>_id</code> value of
     * <code>EmployeePriviousJobDetails</code> object in hexadecimal String.
     * @param userId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String.
     * @return <code>True</code> if updation is successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public boolean deleteEmployeePriviousDetails(String primaryKey, String userId) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<EmployeePriviousJobDetails>() {
        }.getType();
        String dbStr = new EmployeePreviousJobDetailManager().fetchEmployeePriviousDetails(primaryKey);
        if (dbStr == null || dbStr.isEmpty()) {
            return false;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        EmployeePriviousJobDetails dbObj = new Gson().fromJson(dbStr, type);
        dbObj.setStatus(ApplicationConstants.DELETE);
        dbObj.setDeletedBy(userName);
        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_PREVIOUS_JOB_TABLE, primaryKey, new Gson().toJson(dbObj));
        return result;
    }

    /**
     * This method is to update <code>EmployeePriviousJobDetails</code> object.
     *
     * @param epjd <code>EmployeePriviousJobDetails</code> Object data
     * @param primaryKey <code>_id</code> value of
     * <code>EmployeePriviousJobDetails</code> object in hexadecimal String.
     * @param userId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String.
     * @return <code>True</code> if updation is successful
     * @throws Exception if either second or third argument is
     * <code>null</code>..
     */
    public boolean updateEmployeePriviousDetails(EmployeePriviousJobDetails epjd, String primaryKey, String userId) throws Exception {

        Type type = new TypeToken<EmployeePriviousJobDetails>() {
        }.getType();
        String dbStr = new EmployeePreviousJobDetailManager().fetchEmployeePriviousDetails(primaryKey);
        if (dbStr == null || dbStr.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        EmployeePriviousJobDetails dbObj = new Gson().fromJson(dbStr, type);
        epjd.setCreateDate(dbObj.getCreateDate());
        epjd.setCreatedBy(dbObj.getCreatedBy());
        epjd.setUpdateDate(System.currentTimeMillis() + "");
        epjd.setStatus(ApplicationConstants.ACTIVE);
        epjd.setUpdatedBy(userName);
        String jsonStr = new Gson().toJson(epjd);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_PREVIOUS_JOB_TABLE, primaryKey, jsonStr);
        return result;
    }

    /**
     * This method is to get all <code>EmployeePriviousJobDetails</code> object
     * data based on <code>employeeDDO</code> field.
     *
     * @param ddo <code>_id</code> value of
     * <code>EmployeePriviousJobDetails</code> Object in hexadecimal string
     * @return List of <code>EmployeePriviousJobDetails</code> in JSON String
     * format
     * @throws Exception if argument is <code>null</code>..
     */
    public String fetchAllEmployeePriviousDetails(String ddo) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("employeeDDO", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_PREVIOUS_JOB_TABLE, conditionMap);
        List<EmployeePriviousJobDetails> list = null;
        if (result != null) {
            list = new Gson().fromJson(result, new TypeToken<List<EmployeePriviousJobDetails>>() {
            }.getType());
            list = getDDO(list);
        }
        return new Gson().toJson(list);
    }

    /**
     *
     * @param employeeList <code>EmployeePriviousJobDetails</code> list
     * @return List of <code>EmployeePriviousJobDetails</code>
     * @throws Exception
     */
    private List<EmployeePriviousJobDetails> getDDO(List<EmployeePriviousJobDetails> employeeList) throws Exception {
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
                if (entry.getKey().equals(employeeList.get(i).getEmployeeDDO())) {
                    employeeList.get(i).setEmployeeDDO(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    /**
     * This method is to get <code>Employee</code> object data based on
     * <code>employeeCode</code> value.
     *
     * @param employeeCode <code>employeeCode</code> value of
     * <code>Employee</code> object.
     * @return <code>Employee</code> object in JSON string format.
     * @throws Exception if argument is <code>null</code>..
     */
    public String getEmployeeByEmployeeCode(String employeeCode) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", employeeCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());

        for (Employee cl : empList) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
            List<Department> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Department>>() {
            }.getType());
            Department gal = gaList.get(0);
            cl.setDepartment(gal.getDepartment());
            String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, cl.getDesignation());
            List<Designation> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<Designation>>() {
            }.getType());
            Designation gal1 = gaList1.get(0);
            cl.setDesignation(gal1.getDesignation());

        }
        return new Gson().toJson(empList);

    }

}
