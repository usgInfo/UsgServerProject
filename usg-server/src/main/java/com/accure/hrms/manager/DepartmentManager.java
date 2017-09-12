/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Department;
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
 * @author Shwetha T S
 */
public class DepartmentManager {

    /**
     * This method is to insert <code>Department</code> Object to the <code>ApplicationConstants.DEPARTMENT_TABLE
     * </code>
     *
     * @param department is <code>department</code> field value of
     * <code>Department</code> Object.
     * @param userid primary key value of <code>User</code> Object.
     *
     * @return
     * <p>
     * <code> primary key value of <code>Department</code> Object </code> if
     * insertion is successful or</p><p>
     * <code>ApplicationConstants.FAIL</code> if anyone of the parameters in the
     * save method is null or empty or</p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE_MESSAGE</code> if Department Object
     * list already contains <code>department</code> value</p>
     *
     * @throws java.lang.Exception If either argument is <code>null</code>..
     */
    public String save(String department, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (userid == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("department", department);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.DEPARTMENT_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        Department departmentObj = new Department();
        departmentObj.setDepartment(department);
        departmentObj.setStatus(ApplicationConstants.ACTIVE);
        departmentObj.setCreateDate(System.currentTimeMillis() + "");
        departmentObj.setUpdateDate(System.currentTimeMillis() + "");
        departmentObj.setCreatedBy(userName);
        departmentObj.setUpdatedBy(userName);
        String jsonStr = new Gson().toJson(departmentObj);
        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.DEPARTMENT_TABLE, jsonStr);

        return primaryKey;
    }

    /**
     * This method is to get List of <code>Department</code> Object data.
     *
     * @return <code>Department</code> Object list in JSON String format
     * @throws java.lang.Exception If either argument is <code>null</code>..
     */
    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DEPARTMENT_TABLE, hMap);
        return result;

    }

    /**
     * This method is to update <code>Department</code> Object .
     *
     * @param department is <code>department</code> field value of
     * <code>Department</code> Object.
     * @param primaryKey is primary key value of <code>Department</code> Object.
     * @param userid is primary key value of <code>User</code> Object.
     *
     * @return
     * <p>
     * <code>ApplicationConstants.SUCCESS</code> if updation is successful
     * or</p><p>
     * <code> ApplicationConstants.FAIL
     * </code>if anyone of the parameters in the update method is null or
     * empty.</p><p>
     * <code>ApplicationConstants.DUPLICATE_MESSAGE</code> if Department list
     * already contains <code>department</code> value</p>
     *
     * @throws java.lang.Exception If either of argument is <code>null</code>..
     */
    public String update(String department, String primaryKey, String userid) throws Exception {

        if (primaryKey == null || userid == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("department", department);
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.DEPARTMENT_TABLE, duplicateCheckMap, primaryKey)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, primaryKey);
        List<Department> list = new Gson().fromJson(dbStr, new TypeToken<List<Department>>() {
        }.getType());

        Department dbObj = list.get(0);
        Department dept = new Department();
        dept.setDepartment(department);
        dept.setCreateDate(dbObj.getCreateDate());
        dept.setCreatedBy(dbObj.getCreatedBy());
        dept.setStatus(ApplicationConstants.ACTIVE);
        dept.setUpdateDate(System.currentTimeMillis() + "");
        dept.setUpdatedBy(userName);

        String jsonStr = new Gson().toJson(dept);

        String status;

        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DEPARTMENT_TABLE, primaryKey, jsonStr);
        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;
    }

    /**
     * This method is to update status field of  <code>Department</code> Object.
     *
     * @param primaryKey is primary key value of the <code>Department</code>
     * Object.
     * @param userid is primary key value of the <code>User</code> Object.
     * @return String<p>
     * <code>ApplicationConstants.SUCCESS</code> if successful updation of
     * <code>Department</code> Object</p><p>
     * <code>ApplicationConstants.FAIL</code> if anyone of the parameters in the
     * delete method is null or empty</p><p>
     * <code> ApplicationConstants.DELETE_MESSAGE</code> if primary key value of
     * <code>Department</code> Object is saved in <code>department</code> field
     * of <code>ApplicationConstants.EMPLOYEE_TABLE</code> or
     * <code>department</code> field of
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code></p>
     * @throws java.lang.Exception if either of argument is <code>null</code>.
     */
    public String delete(String primaryKey, String userid) throws Exception {
        String status;

        if (primaryKey == null || userid == null) {
            return ApplicationConstants.FAIL;
        }
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, primaryKey);
        List<Department> list = new Gson().fromJson(dbStr, new TypeToken<List<Department>>() {
        }.getType());

        Department dbObj = list.get(0);

        Department dept = new Department();
        dept.setDepartment(dbObj.getDepartment());
        dept.setCreateDate(dbObj.getCreateDate());
        dept.setCreatedBy(dbObj.getCreatedBy());
        dept.setStatus(ApplicationConstants.DELETE);
        dept.setUpdateDate(System.currentTimeMillis() + "");
        dept.setUpdatedBy(dbObj.getUpdatedBy());
        dept.setDeletedBy(userName);

        String jsonStr = new Gson().toJson(dept);

        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "department", primaryKey) || DeleteDependencyManager.hasDependency(ApplicationConstants.DDO_DEPARTMENT_TABLE, "department", primaryKey)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DEPARTMENT_TABLE, primaryKey, jsonStr);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    /**
     * This method is to search the <code>Department</code> Object data by using primary
     * key value.
     *
     * @param primaryKey is primary key value of the Department Object.
     * @return Department Object.
     *
     * @throws java.lang.Exception if argument is <code>null</code>...
     */
    public Department fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, primaryKey);
        List<Department> li = new Gson().fromJson(result, new TypeToken<List<Department>>() {
        }.getType());
        if (li == null || li.size() < 1) {
            return null;
        }
        return li.get(0);
    }

}
