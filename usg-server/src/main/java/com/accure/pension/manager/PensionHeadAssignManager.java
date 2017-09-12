/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.HeadAssign;
import com.accure.hrms.dto.SalaryHead;
import com.accure.pension.dto.PensionEmployee;
import com.accure.pension.dto.PensionHeadAssign;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Shwetha T S
 */
public class PensionHeadAssignManager {

    public String searchItFinally(PensionEmployee emp) throws Exception {
        String finalResult = null;
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.PENSION_EMPLOYEE_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        if (emp.getDepartment() != null) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));
        }
        if (emp.getDesignation() != null) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", emp.getDesignation()));
        }
        if (emp.getEmployeeName() != null) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (emp.getEmployeecode() != null) {
            regexQuery.put("employeecode",
                    new BasicDBObject("$regex", emp.getEmployeecode()));
        }
        if (emp.getManualCode() != null) {
            regexQuery.put("manualCode",
                    new BasicDBObject("$regex", emp.getManualCode()));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        regexQuery.put("pensionLeftStatus",
                new BasicDBObject("$regex", "NO"));
        regexQuery.put("stopPension",
                new BasicDBObject("$regex", "NO"));

        DBCursor cursor2 = collection.find(regexQuery);
        List<PensionEmployee> employeeList = new ArrayList<PensionEmployee>();
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();

            Type type = new TypeToken<PensionEmployee>() {
            }.getType();
            PensionEmployee em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }
        try {
            employeeList = getDepartmentOfPensionEmployee(employeeList);
        } catch (Exception ex) {

        }
        try {
            employeeList = getDesignationOfPensionEmployee(employeeList);
        } catch (Exception ex) {

        }
        if (employeeList.size() > 0) {

            finalResult = new Gson().toJson(employeeList);
        }

        return finalResult;
    }

    public String Assign(String id, String head, String userid) throws Exception {

        String result = null;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        JSONObject obj = new JSONObject(id);
        JSONArray array = obj.getJSONArray("id");
        List<PensionHeadAssign> list = new ArrayList<PensionHeadAssign>();
        PensionHeadAssign headassign = new PensionHeadAssign();
        for (int i = 0; i < array.length(); i++) {

            String fetchdbObj = new PensionHeadAssignManager().fetchPensionEmployee(array.getString(i));
            PensionEmployee emp = new Gson().fromJson(fetchdbObj, new TypeToken<PensionEmployee>() {
            }.getType());
            headassign.setDepartment(emp.getDepartment());
            headassign.setDesignation(emp.getDesignation());
            headassign.setEmpCode(emp.getEmployeecode());
            headassign.setEmpName(emp.getEmployeeName());
            headassign.setEmpManualCode(emp.getManualCode());
            headassign.setCreateDate(System.currentTimeMillis() + "");
            headassign.setUpdateDate(System.currentTimeMillis() + "");
            headassign.setUpdatedBy(userName);
            headassign.setCreatedBy(userName);
            headassign.setStatus(ApplicationConstants.ACTIVE);
            headassign.setHeadName(head);
            if (duplicate(headassign.getEmpCode(), head)) {
                continue;
            }
            result = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, new Gson().toJson(headassign));

        }
        return result;
    }

    public boolean duplicate(String employeeCode, String head) throws Exception {

        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, conditionMap);
            if (result1 != null) {
                List<PensionHeadAssign> list = new Gson().fromJson(result1, new TypeToken<List<PensionHeadAssign>>() {
                }.getType());
                PensionHeadAssign pha = new PensionHeadAssign();
                for (PensionHeadAssign li : list) {
                    if (pha.getEmpCode().equals(employeeCode) && pha.getHeadName().equalsIgnoreCase(head)) {

                        res = true;
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String fetchPensionEmployee(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_EMPLOYEE_TABLE, Id);
        List<PensionEmployee> gisList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public String View() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, conditionMap);
        List<PensionHeadAssign> emp = new Gson().fromJson(result1, new TypeToken<List<PensionHeadAssign>>() {
        }.getType());
        try {
            emp = getDepartment(emp);
        } catch (Exception ex) {

        }
        try {
            emp = getDesignation(emp);
        } catch (Exception ex) {

        }
        try {
            emp = getHeadName(emp);
        } catch (Exception ex) {

        }

        //System.out.println(new Gson().toJson(emp));
        return new Gson().toJson(emp);
    }

    public static List<PensionHeadAssign> getDepartment(List<PensionHeadAssign> employeeList) throws Exception {
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

    public static List<PensionHeadAssign> getHeadName(List<PensionHeadAssign> employeeList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> religionList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = religionList.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getShortDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getHeadName())) {
                    employeeList.get(i).setHeadName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<PensionEmployee> getDepartmentOfPensionEmployee(List<PensionEmployee> employeeList) throws Exception {
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

    public static List<PensionEmployee> getDesignationOfPensionEmployee(List<PensionEmployee> employeeList) throws Exception {
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

    public static List<PensionHeadAssign> getDesignation(List<PensionHeadAssign> employeeList) throws Exception {
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

    public boolean delete(String id, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        boolean status = false;
        int count = 0;
        JSONObject obj = new JSONObject(id);
        JSONArray array = obj.getJSONArray("id");
        //System.out.println(array);
        for (int i = 0; i < array.length(); i++) {
            String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, array.getString(i));
            List<PensionHeadAssign> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionHeadAssign>>() {
            }.getType());
            PensionHeadAssign category = categorylist.get(0);
            HeadAssign categoryobje = new HeadAssign();

            categoryobje.setStatus(ApplicationConstants.DELETE);
            categoryobje.setUpdateDate(System.currentTimeMillis() + "");
            category.setUpdatedBy(userName);
            String categoryJson = new Gson().toJson(categoryobje);
            status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, array.getString(i), categoryJson);
            //System.out.println(status);
        }
        return status;
    }

    public boolean update(String id, String head, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        boolean status = false;
        JSONObject obj = new JSONObject(id);
        JSONArray array = obj.getJSONArray("id");

        for (int i = 0; i < array.length(); i++) {
            String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, array.getString(i));
            List<PensionHeadAssign> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionHeadAssign>>() {
            }.getType());
            PensionHeadAssign category = categorylist.get(0);

            category.setHeadName(head);
            category.setStatus(ApplicationConstants.ACTIVE);
            category.setUpdateDate(System.currentTimeMillis() + "");
            category.setUpdatedBy(userName);
            if (duplicate(category.getEmpCode(), head)) {
                continue;
            }
            String categoryjson = new Gson().toJson(category);
            status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_HEAD_ASSIGN_TABLE, array.getString(i), categoryjson);

        }
        return status;
    }
}
