/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.DdoDepartmentMap;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeeDepartmentMapping;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shwetha T S
 */
public class EmployeeDepartmentMappingManager {

    /**
     * This method is to insert <code>EmployeeDepartmentMapping</code> Object
     * data to <code>ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING</code>
     *
     * @param empDeptMapping EmployeeDepartmentMapping Object data
     * @param userId p<code>_id</code> value of <code>User</code> Object in
     * hexadecimal string
     * @return
     * <p>
     * <code>ApplicationConstants.FAIL</code> if either of argument is null or
     * empty or</p>
     * <p>
     * <code>ApplicationConstants.SUCCESS</code>If
     * <code>EmployeeDepartmentMapping</code> Object insertion is successful or
     * </p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE</code>If
     * <code>EmployeeDepartmentMapping</code> list already contains
     * <code>employeeCode</code> value</p>
     * @throws Exception if either of argument is <code>null</code>...
     */
    public String save(EmployeeDepartmentMapping empDeptMapping, String userId) throws Exception {

        String result = "";

        if (userId == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("employeeCode", empDeptMapping.getEmployeeCode());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        empDeptMapping.setStatus(ApplicationConstants.ACTIVE);
        empDeptMapping.setCreateDate(System.currentTimeMillis() + "");
        empDeptMapping.setUpdateDate(System.currentTimeMillis() + "");
        empDeptMapping.setCreatedBy(userName);
        empDeptMapping.setUpdatedBy(userName);
        empDeptMapping.setEmployeeId(getEmployeeIds(empDeptMapping.getEmployeeCode()));

        String json = new Gson().toJson(empDeptMapping);

        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, json);

        if (primaryKey != null) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

    /**
     * This method is to get List of <code>EmployeeDepartmentMapping</code>
     * Object.
     *
     * @param ddo is <code>_id</code> value of <code>DDO</code> Object in
     * hexadecimal string.
     * @param location is <code>_id</code> value of <code>Location</code> Object
     * in hexadecimal string.
     * @return List of <code>EmployeeDepartmentMapping</code> Object.
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String view(String ddo, String location) throws Exception {

        List<EmployeeDepartmentMapping> empDeptlist = new ArrayList<EmployeeDepartmentMapping>();

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("ddo", ddo);
        hMap.put("location", location);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, hMap);

        if (result != null) {

            empDeptlist = new Gson().fromJson(result, new TypeToken<List<EmployeeDepartmentMapping>>() {
            }.getType());

            for (EmployeeDepartmentMapping empDeptMapping : empDeptlist) {

                if ((empDeptMapping.getDepartment() != null)) {

                    List<String> li = empDeptMapping.getDepartment();

                    for (int i = 0; i < li.size(); i++) {

                        String next = li.get(i);
                        String deptlist = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, next);

                        if (deptlist != null) {

                            List<Department> list = new Gson().fromJson(deptlist, new TypeToken<List<Department>>() {
                            }.getType());

                            Department dept = list.get(0);
                            next = dept.getDepartment();
                            li.set(i, next);
                        }
                    }
                }

                if ((empDeptMapping.getDdo() != null)) {
                    String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, empDeptMapping.getDdo());

                    if (jsonStr != null) {

                        List<DDO> list = new Gson().fromJson(jsonStr, new TypeToken<List<DDO>>() {
                        }.getType());

                        DDO ddoObj = list.get(0);
                        empDeptMapping.setDdo(ddoObj.getDdoName());
                    }
                }
                if ((empDeptMapping.getLocation() != null)) {
                    String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, empDeptMapping.getLocation());
                    if (jsonStr != null) {
                        List<Location> list = new Gson().fromJson(jsonStr, new TypeToken<List<Location>>() {
                        }.getType());
                        Location locObj = list.get(0);
                        empDeptMapping.setLocation(locObj.getLocationName());
                    }
                }

            }
        }
        return new Gson().toJson(empDeptlist);
    }

    /**
     * This method is to update <code>EmployeeDepartmentMapping</code> Object
     * data by using primary key value.
     *
     * @param empDeptMapping <code>EmployeeDepartmentMapping</code> object data
     * @param userId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string.
     * @param primaryKey <code>_id</code> value of
     * <code>EmployeeDepartmentMapping</code> Object in hexadecimal string.
     * @return
     * <p>
     * <code>ApplicationConstants.DUPLICATE</code>If list of
     * EmployeeDepartmentMapping already contains <code>employeeCode</code>
     * value</p>
     * <p>
     * <code>ApplicationConstants.SUCCESS</code>If EmployeeDepartmentMapping
     * object updation is successful</p>
     * <p>
     * <code>ApplicationConstants.FAIL</code> If anyone of parameter in the
     * update method is null or empty </p>
     * @throws java.lang.Exception if either of argument is <code>null</code>...
     */
    public String update(EmployeeDepartmentMapping empDeptMapping, String userId, String primaryKey) throws Exception {
        String result = "";
        if (userId == null || primaryKey == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("employeeCode", empDeptMapping.getEmployeeCode());

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + "" + user.getLname();

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, primaryKey);

        if (dbStr == null) {
            return ApplicationConstants.FAIL;
        }
        List<EmployeeDepartmentMapping> list = new Gson().fromJson(dbStr, new TypeToken<List<EmployeeDepartmentMapping>>() {
        }.getType());
        EmployeeDepartmentMapping dbObj = list.get(0);

        empDeptMapping.setStatus(ApplicationConstants.ACTIVE);
        empDeptMapping.setEmployeeId(getEmployeeIds(empDeptMapping.getEmployeeCode()));
        empDeptMapping.setCreateDate(dbObj.getCreateDate());
        empDeptMapping.setCreatedBy(dbObj.getCreatedBy());
        empDeptMapping.setUpdateDate(System.currentTimeMillis() + "");
        empDeptMapping.setUpdatedBy(userName);

        String json = new Gson().toJson(empDeptMapping);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, primaryKey, json);
        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

    /**
     * This method is to update <code>status</code> field of
     * EmployeeDepartmentMapping Object.
     *
     * @param primaryKey is <code>_id</code> value of
     * <code>EmployeeDepartmentMapping</code> Object Object in hexadecimal
     * string.
     * @param userid is <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string
     * @return
     * <p>
     * <code>ApplicationConstants.FAIL</code> if anyone of parameter in the
     * delete method is null or empty or </p>
     * <p>
     * <code>ApplicationConstants.SUCCESS</code>If updation is successful</p>
     *
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String delete(String primaryKey, String userid) throws Exception {

        if (primaryKey == null || userid == null) {
            return ApplicationConstants.FAIL;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String ddoJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, primaryKey);

        if (ddoJson == null) {
            return ApplicationConstants.FAIL;
        }
        List<EmployeeDepartmentMapping> list = new Gson().fromJson(ddoJson, new TypeToken<List<EmployeeDepartmentMapping>>() {
        }.getType());
        EmployeeDepartmentMapping empDept = list.get(0);
        empDept.setStatus(ApplicationConstants.DELETE);
        empDept.setUpdateDate(System.currentTimeMillis() + "");
        empDept.setUpdatedBy(userName);
        String status = "";

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, primaryKey, new Gson().toJson(empDept));
        if (result) {
            status = ApplicationConstants.SUCCESS;
        }

        return status;

    }

    /**
     * This method is to get <code>_id</code> value of <code>Employee</code>
     * Object based on <code>employeeCode</code>
     *
     * @param employeeCode is <code>employeeCode</code> value
     * <code>Employee</code> Object
     * @return <code>_id</code> value of  <code>Employee</code> Object in
     * hexadecimal string.
     * @throws Exception if argument is <code>null</code>..
     */
    private String getEmployeeIds(String employeeCode) throws Exception {
        String employeeId = null;
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("employeeCode", employeeCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, hMap);

        if (result != null) {
            List<Employee> emplist = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
            }.getType());

            Employee emp = emplist.get(0);
            employeeId = ((LinkedTreeMap<String, String>) emp.getId()).get("$oid");

        }
        return employeeId;
    }

    public String getDDODepartments(String ddo) throws Exception {
        List<String> deptIdlist = new ArrayList<String>();
        List<Department> deptList = new ArrayList<Department>();

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_DEPARTMENT_TABLE, hMap);

        if (result != null) {

            List<DdoDepartmentMap> ddoDeptlist = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
            }.getType());

            for (String str : ddoDeptlist.get(0).getDepartmentList()) {
                deptIdlist.add(str);
            }

            for (String deptId : deptIdlist) {
                String deptJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, deptId);
                if (deptJson != null) {
                    List<Department> departlist = new Gson().fromJson(deptJson, new TypeToken<List<Department>>() {
                    }.getType());
                    deptList.add(departlist.get(0));

                }

            }
        }
        return new Gson().toJson(deptList);

    }

    public String getDepartments(String empid) throws Exception {
        HashMap<String, String> DeptData = new HashMap<String, String>();
        HashMap<String, HashMap<String, String>> finalResult = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> hMap = new HashMap<String, String>();
        String dept;
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("employeeId", empid);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, hMap);
        List<EmployeeDepartmentMapping> empDeptlist = new Gson().fromJson(result, new TypeToken<List<EmployeeDepartmentMapping>>() {
        }.getType());
        EmployeeDepartmentMapping empData = empDeptlist.get(0);
        if (empData.getDepartment() != null) {
            List<String> deptList = empData.getDepartment();
            for (int i = 0; i < deptList.size(); i++) {
                String deptlist = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, deptList.get(i));
                if (deptlist != null) {
                    List<Department> gaList1 = new Gson().fromJson(deptlist, new TypeToken<List<Department>>() {
                    }.getType());
                    Department gal1 = gaList1.get(0);
                    dept = gal1.getDepartment();
                    DeptData.put(deptList.get(i), dept);
                }
            }
        }

        finalResult.put("FinalResult", DeptData);
        return new Gson().toJson(finalResult);
    }

    public String getDepartmentsforUser(String empid) throws Exception {
        HashMap<String, String> DeptData = new HashMap<String, String>();
        HashMap<String, HashMap<String, String>> finalResult = new HashMap<String, HashMap<String, String>>();

        String dept;

        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_TABLE, empid);
        List<Employee> empDeptlist = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        Employee empData = empDeptlist.get(0);
        if (empData.getDepartment() != null) {

            String deptlist = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, empData.getDepartment());
            if (deptlist != null) {
                List<Department> gaList1 = new Gson().fromJson(deptlist, new TypeToken<List<Department>>() {
                }.getType());
                Department gal1 = gaList1.get(0);
                dept = gal1.getDepartment();
                DeptData.put(empData.getDepartment(), dept);
            }

        }

        finalResult.put("FinalResult", DeptData);
        return new Gson().toJson(finalResult);
    }

}
