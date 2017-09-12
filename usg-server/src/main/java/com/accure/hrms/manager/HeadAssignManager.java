/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.HeadAssign;
import com.accure.hrms.dto.SalaryHead;
import static com.accure.hrms.manager.EmployeeManager.getDepartment;
import static com.accure.hrms.manager.EmployeeManager.getDesignation;
import static com.accure.hrms.manager.EmployeeManager.getDiscipline;
import static com.accure.hrms.manager.EmployeeManager.getFundType;
import static com.accure.hrms.manager.EmployeeManager.getNature;
import static com.accure.hrms.manager.EmployeeManager.getReligion;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;

import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class HeadAssignManager {

    public String fetchEmployeeByDDO(String DDO) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", DDO);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        for (Employee cl : empList) {
            if (cl.getDepartment() != null) {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
                List<Department> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Department>>() {
                }.getType());
                Department gal = gaList.get(0);
                cl.setDepartment(gal.getDepartment());
            }
            System.out.print(cl);
        }
        return new Gson().toJson(empList);

    }

    public List<Employee> searchItFinally(Employee emp) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();

        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
        if (emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", emp.getBudgetHead()));
        }
        if (emp.getDepartment() != null) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));
        }

        if (emp.getDesignation() != null) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", emp.getDesignation()));
        }
        if (emp.getNatureType() != null && emp.getNatureType() != "") {
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

        if (emp.getEmployeeName() != null && emp.getEmployeeName() != "") {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (emp.getEmployeeCode() != null && emp.getEmployeeCode() != "") {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", emp.getEmployeeCode()));
        }
        if (emp.getEmployeeCodeM() != null && emp.getEmployeeCodeM() != "") {
            regexQuery.put("employeeCodeM",
                    new BasicDBObject("$regex", emp.getEmployeeCodeM()));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));

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
            employeeList = getReligion(employeeList);

            employeeList = getDepartment(employeeList);
            employeeList = getDiscipline(employeeList);
            employeeList = getNature(employeeList);
            employeeList = getFundType(employeeList);

            employeeList = getDesignation(employeeList);

        }

        return employeeList;
    }

//    public String Assign(String id, String head) throws Exception {
//        //System.out.println("Manager Id" + id);
//        //System.out.println("Manager Head" + head);
//        String result = null;
//        int count = 0;
//        JSONObject obj = new JSONObject(id);
//        //System.out.println("Id Before Converting" + obj);
//        JSONArray array = obj.getJSONArray("id");
//        //System.out.println("Id After Converting" + array);
//       
//        for (int i = 0; i < array.length(); i++) {
//            Type type = new TypeToken<Employee>() {
//            }.getType();
//            String assStr = new EmployeeManager().fetch((String) array.get(i));
//            //System.out.println("Searching Employee" + assStr);
//            Employee bankJson = new Gson().fromJson(assStr, type);
//            //System.out.println("Employees" + bankJson);
//            if (assStr == null || assStr.isEmpty()) {
//                return null;
//            } else {
//                list.add(bankJson);
//
//            }
//            //System.out.println("List Of Employee Size" + list.size());
//            HeadAssign assign=new HeadAssign();
//            assign.setEmpList(list);
//             assign.setHeadName(head);
//             assign.setStatus(ApplicationConstants.ACTIVE);
//          
//         
//           
//            result = DBManager.getDbConnection().insert(ApplicationConstants.HEAD_ASSIGN_TABLE, new Gson().toJson(assign));
//            //System.out.println("Employee Head Assign List" + result);
////            if (result != null) {
////                count++;
////            }
//        }
//        //System.out.println("Employee Head Assign List" + result);
//        return result;
//
//    }
    public String Assign(Employee newEmployee, String userId, String type) throws Exception {
        Employee previousEmp = new Gson().fromJson(fetch(newEmployee.getpKey()), new TypeToken<Employee>() {
        }.getType());
        String condition = "notMatched";
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (type.equalsIgnoreCase("Overwrite") || type == "Overwrite") {
            List<EarningHeadsDetails> newlist = newEmployee.getEarningHeads();
            List<EarningHeadsDetails> list = previousEmp.getEarningHeads();
            //
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDescription().equalsIgnoreCase(newlist.get(0).getDescription())) {
                    condition = "Matched";
                    list.get(i).setAmount(newlist.get(0).getAmount());

                }
            }
            if (condition.equalsIgnoreCase("notMatched")) {
                list.addAll(newlist);

                previousEmp.setEarningHeads(list);
                previousEmp.setUpdateDate(System.currentTimeMillis() + "");
                previousEmp.setUpdatedBy(userName);

                boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, newEmployee.getpKey(), new Gson().toJson(previousEmp));
                if (result) {
                    new EmployeeManager().employeeHistory(previousEmp, newEmployee.getpKey());
                }
            } else {
                previousEmp.setEarningHeads(list);
                previousEmp.setUpdateDate(System.currentTimeMillis() + "");
                previousEmp.setUpdatedBy(userName);

                boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, newEmployee.getpKey(), new Gson().toJson(previousEmp));
                if (result) {
                    new EmployeeManager().employeeHistory(previousEmp, newEmployee.getpKey());
                }
            }

        } else {
            List<EarningHeadsDetails> newlist = newEmployee.getEarningHeads();
            List<EarningHeadsDetails> list = previousEmp.getEarningHeads();

            for (int i = 0; i < newlist.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (newlist.get(i).getDescription().equalsIgnoreCase(list.get(j).getDescription()) || newlist.get(i).getDescription() == list.get(j).getDescription()) {
                        list.remove(j);
                    }
                }
            }
            previousEmp.setEarningHeads(list);
            previousEmp.setUpdateDate(System.currentTimeMillis() + "");
            previousEmp.setUpdatedBy(userName);

            boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, newEmployee.getpKey(), new Gson().toJson(previousEmp));
            if (result) {
                new EmployeeManager().employeeHistory(previousEmp, newEmployee.getpKey());
            }
        }
        return ApplicationConstants.UPDATED;

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

    public String View() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_ASSIGN_TABLE, conditionMap);
        List<HeadAssign> emp = new Gson().fromJson(result1, new TypeToken<List<HeadAssign>>() {
        }.getType());

        for (HeadAssign cl : emp) {
            if (!(cl.getHeadName().isEmpty())) {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, cl.getHeadName());
                List<SalaryHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<SalaryHead>>() {
                }.getType());
                SalaryHead gal = gaList.get(0);
                //System.out.println(gal);
                cl.setHeadName(gal.getDescription());

            }
            String id = cl.getEmplist();
            String emp1;
            emp1 = new EmployeeManager().fetch(id);
            Type type = new TypeToken<Employee>() {
            }.getType();
            Employee bankJson = new Gson().fromJson(emp1, type);

            String department = bankJson.getDepartment();
            String name = bankJson.getEmployeeName();
            String code = bankJson.getEmployeeCode();
            String ddo = bankJson.getDdo();
            String location = bankJson.getLocation();
            String designation = bankJson.getDesignation();
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, department);

            List<Department> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Department>>() {
            }.getType());
            Department gal = gaList.get(0);
            //System.out.println(gal.getDepartment());
            cl.setDepartment(gal.getDepartment());
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, designation);
            List<Designation> religionList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
            }.getType());
            Designation gal1 = religionList.get(0);
            cl.setDesignation(gal1.getDesignation());
            cl.setDdo(ddo);
            cl.setLocation(location);
            cl.setEmpName(name);
            cl.setEmpCode(code);
        }
        //System.out.println(new Gson().toJson(emp));
        return new Gson().toJson(emp);
    }

    public static void main(String[] args) throws Exception {
        String result = new HeadAssignManager().View();
        //System.out.println(result);
    }

//    public boolean delete(String id) throws Exception {
//        
//        String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.HEAD_ASSIGN_TABLE, id);
//        List<HeadAssign> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<HeadAssign>>() {
//        }.getType());
//        HeadAssign category = categorylist.get(0);
//        HeadAssign categoryobje = new HeadAssign();
//        categoryobje.setEmplist(category.getEmplist());
//        categoryobje.setHeadName(category.getHeadName());
//        categoryobje.setCreateDate(category.getCreateDate());
//        categoryobje.setStatus(ApplicationConstants.DELETE);
//        categoryobje.setUpdateDate(System.currentTimeMillis() + "");
//        String categoryJson = new Gson().toJson(categoryobje);
//        boolean status = DBManager.getDbConnection().update(ApplicationConstants.HEAD_ASSIGN_TABLE, id, categoryJson);
//        return status;
//        
//    }
    public boolean delete(String id) throws Exception {
        boolean status = false;
        int count = 0;
        JSONObject obj = new JSONObject(id);
        JSONArray array = obj.getJSONArray("id");
        //System.out.println(array);
        for (int i = 0; i < array.length(); i++) {
            String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.HEAD_ASSIGN_TABLE, array.getString(i));
            List<HeadAssign> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<HeadAssign>>() {
            }.getType());
            HeadAssign category = categorylist.get(0);
            HeadAssign categoryobje = new HeadAssign();
            categoryobje.setEmplist(category.getEmplist());
            categoryobje.setHeadName(category.getHeadName());
            categoryobje.setCreateDate(category.getCreateDate());
            categoryobje.setStatus(ApplicationConstants.DELETE);
            categoryobje.setUpdateDate(System.currentTimeMillis() + "");
            String categoryJson = new Gson().toJson(categoryobje);
            status = DBManager.getDbConnection().update(ApplicationConstants.HEAD_ASSIGN_TABLE, array.getString(i), categoryJson);
            //System.out.println(status);
        }
        return status;
    }

    public static String AssignImport(String id, String head) throws Exception {
        String result = null;

        HeadAssign headassign = new HeadAssign();
        //System.out.println(id + "," + head);
        headassign.setEmplist(id);
        headassign.setHeadName(head);
        headassign.setCreateDate(System.currentTimeMillis() + "");
        headassign.setUpdateDate(System.currentTimeMillis() + "");
        headassign.setStatus(ApplicationConstants.ACTIVE);
        result = DBManager.getDbConnection().insert(ApplicationConstants.HEAD_ASSIGN_TABLE, new Gson().toJson(headassign));
        return result;
    }

    public String getempHeads(String empcode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("emplist", empcode);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_ASSIGN_TABLE, conditionMap);
        List<HeadAssign> headList = new Gson().fromJson(result, new TypeToken<List<HeadAssign>>() {
        }.getType());
//        for (HeadAssign cl : headList) {
//            if (cl.getHeadName() != null) {
//                String gaJson6 = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, cl.getHeadName());
//
//                List<SalaryHead> gaList6 = new Gson().fromJson(gaJson6, new TypeToken<List<SalaryHead>>() {
//                }.getType());
//                SalaryHead gal6 = gaList6.get(0);
//                cl.setHeadName(gal6.getDescription());
//            }
//        }
        return new Gson().toJson(headList);
    }
}
