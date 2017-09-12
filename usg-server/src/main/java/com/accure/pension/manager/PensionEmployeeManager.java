/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforUpdate;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.manager.ExpressionCalculatorManager;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.usg.common.manager.DBManager;
import com.accure.pension.dto.PensionEmployee;
import com.accure.pension.dto.PensionType;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

//import org.apache.commons.configuration.PropertiesConfiguration;
/**
 *
 * @author user
 */
public class PensionEmployeeManager {

    public String savePensionEmployeeData(String nomineeJson, String empcode) throws Exception {
//        if (nomineeJson == null || nomineeJson.isEmpty()) {
//            return null;
//        }
        Type type = new TypeToken<PensionEmployee>() {
        }.getType();
        PensionEmployee nomineeDTO = new Gson().fromJson(nomineeJson, type);
        nomineeDTO.setEmployeecode(empcode);
        String emp = new PensionEmployeeManager().fetchByEmployeeCode(empcode);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", empcode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());

//        for (Employee cl : empList) {
////             String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
////            List<Department> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Department>>() {
////            }.getType());
////            Department gal = gaList.get(0);
////           cl.setDepartment(gal.getDepartment());
////           String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, cl.getDesignation());
////            List<Designation> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<Designation>>() {
////           }.getType());
////          Designation gal1 = gaList1.get(0);
////            cl.setDesignation(gal1.getDesignation());
//            nomineeDTO.setDDO(cl.getDdo());
//            nomineeDTO.setDateofBirth(cl.getDob());
//            nomineeDTO.setDepartment(cl.getDepartment());
//            nomineeDTO.setDesignation(cl.getDesignation());
//            nomineeDTO.setEmployeeName(cl.getEmployeeName());
//            nomineeDTO.setFatherName(cl.getFatherName());
//            nomineeDTO.setAppointmentDate(cl.getDateOfAppointment());
//            nomineeDTO.setReligion(cl.getReligion());
//
//        }
        nomineeDTO.setStatus(ApplicationConstants.ACTIVE);
        nomineeDTO.setCreateDate(System.currentTimeMillis() + "");
        nomineeDTO.setUpdateDate(System.currentTimeMillis() + "");
        String json = new Gson().toJson(nomineeDTO, type);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_EMPLOYEE_TABLE, json);
        return id;

    }

    public String save(PensionEmployee association, String userId) throws Exception {
        String result;
        if (userId == null) {
            return null;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("employeecode", association.getEmployeecode());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (hasDuplicateforSave(ApplicationConstants.PENSION_EMPLOYEE_TABLE, map)) {
            result = ApplicationConstants.DUPLICATE;

        } else {

            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            association.setStatus(ApplicationConstants.ACTIVE);
            association.setCreateDate(System.currentTimeMillis() + "");
            association.setUpdateDate(System.currentTimeMillis() + "");
            association.setCreatedBy(userName);
            association.setUpdatedBy(userName);

            String GISjson = new Gson().toJson(association);
            String id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_EMPLOYEE_TABLE, GISjson);

            if (id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;

    }

    public static List<PensionEmployee> getDepartment(List<PensionEmployee> employeeList) throws Exception {
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

    public static List<PensionEmployee> getDesignation(List<PensionEmployee> employeeList) throws Exception {
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

    public static List<PensionEmployee> getPensionType(List<PensionEmployee> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.PENSION_TYPE_TABLE);
        List<PensionType> religionList = new Gson().fromJson(result, new TypeToken<List<PensionType>>() {
        }.getType());
        for (Iterator<PensionType> iterator = religionList.iterator(); iterator.hasNext();) {
            PensionType next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getPensionType());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getPensionType())) {
                    employeeList.get(i).setPensionType(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    /**
     * @author ankur This method calculates Pension and Family Pension
     * @conditions 1)If formula="Pension", then calculate pension by taking
     * Pension formula from Formula Master. 2)If formula="Family Pension", then
     * calculate Family Pension formula from Formula Master .
     * @param payScale is String type corresponding 6th CPC Pay Scale
     * @param gradePay is String type Grade Pay of employee
     * @param formula is String type formula taken from Formula Master
     * @return Long type calculated Pension Amount
     */
    public Double calculatePension(String payScale, String gradePay, String formula) throws Exception {
        String pS[] = payScale.split("-");
        Double gp = Double.parseDouble(gradePay);
        Double ps = Double.parseDouble(pS[0]);
        Double pension = 0d;
        HashMap<String, String> hMap = new HashMap<String, String>();
        boolean b = formula.equals("Pension");
        if (formula.equals("Pension")) {
            hMap.put(ApplicationConstants.DESCRIPTION, ApplicationConstants.PENSION);
            hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        } else if (formula.equals("Family Pension")) {
            hMap.put(ApplicationConstants.DESCRIPTION, ApplicationConstants.FAMILY_PENSION);
            hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        }
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FORMULA_TABLE, hMap);

        String rs1 = result.substring(1, result.length() - 1);
        //JSONObject json = (JSONObject) JSONSerializer.toJSON(data);
        JSONObject json = new JSONObject(rs1);
        String formula1 = json.getString("hiddenformula");
        String ids[] = formula1.split("#");
        String id1 = "", id2 = "";
        int i = 1;
        for (String id : ids) {
            //System.out.println("ids: "+id);
            if (i == 1 && id.length() > 14) {
                id1 = id;
                i++;
            } else if (id.length() > 14 && i > 1) {
                id2 = id;
            }
        }
        String result2 = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id1);
        String result3 = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id2);
        String rs2 = result2.substring(1, result2.length() - 1);
        String rs3 = result3.substring(1, result3.length() - 1);
        JSONObject j1 = new JSONObject(rs2);
        JSONObject j2 = new JSONObject(rs3);
        String f1 = j1.getString("description");
        String f2 = j2.getString("description");
        if (f1.equals("Pay Scale")) {
            //System.out.println("Inside equals");
            formula1 = formula1.replace("" + id1, "" + ps);
        }
        if (f2.equals("Grade Pay")) {
            //System.out.println("Grade Pay: "+gp);
            formula1 = formula1.replace("" + id2, "" + gp);
        }
        //System.out.println("f1:----"+f1);
        //System.out.println("f1:----"+f2);
        String formula2 = formula1.replace("#", "");
        //System.out.println("Formula: " + formula2);
        pension = new ExpressionCalculatorManager().calculateTheValue(formula2);
        try {
            //ScriptEngineManager mgr = new ScriptEngineManager();
            // ScriptEngine engine = mgr.getEngineByName("JavaScript");
            //System.out.println(engine.eval(formula2).toString());
            // pension = Double.parseDouble(engine.eval(formula2).toString());
            // System.out.println("Pension: " + pension);
        } catch (Exception e) {
        }

//        if (status == "Alive") {
//            pension = (ps + gp) / 2;
//        } //Family Pension(status=="Dead")
//        else {
//            pension = ((ps + gp) * 3) / 10;
//        }
        return pension;
    }

    public String viewPensionEmployeeData() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        PensionEmployee categoryobje = new PensionEmployee();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, hMap);

        List<PensionEmployee> employeeList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());
        try {
            employeeList = getDepartment(employeeList);
        } catch (Exception e) {
        }
        try {
            employeeList = getDesignation(employeeList);
        } catch (Exception e) {
        }
        try {
            employeeList = getPensionType(employeeList);

        } catch (Exception e) {
        }
        String finalresult = new Gson().toJson(employeeList);

        return finalresult;

    }

    public boolean deletePensionEmployeeMaster(String id, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_EMPLOYEE_TABLE, id);
        List<PensionEmployee> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionEmployee>>() {
        }.getType());
        PensionEmployee categoryobje = categorylist.get(0);

        categoryobje.setStatus(ApplicationConstants.DELETE);
        categoryobje.setUpdatedBy(userName);
        categoryobje.setUpdateDate(System.currentTimeMillis() + "");
        String categoryJson = new Gson().toJson(categoryobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_EMPLOYEE_TABLE, id, categoryJson);
        return status;

        //     boolean status = DBManager.getDbConnection().deleteDocument(GISjson, gisGroup);
    }

    public String fetchByEmployeeCode(String employeeCode) throws Exception {
        PensionEmployee nomineeDTO = new PensionEmployee();
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
            nomineeDTO.setDDO(cl.getDdo());
            nomineeDTO.setDateofBirth(cl.getDob());
            nomineeDTO.setDepartment(cl.getDepartment());
            nomineeDTO.setDesignation(cl.getDesignation());
            nomineeDTO.setEmployeeName(cl.getEmployeeName());
            nomineeDTO.setFatherName(cl.getFatherName());
            nomineeDTO.setAppointmentDate(cl.getDateOfAppointment());
            nomineeDTO.setReligion(cl.getReligion());
        }
        return new Gson().toJson(empList);
    }

    public String updatePensionEmployeeMaster(PensionEmployee association, String id, String userId) throws Exception {

        String result="";
        if (userId == null) {
            return null;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("employeecode", association.getEmployeecode());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

//        if (isDuplicateforUpdate(ApplicationConstants.PENSION_EMPLOYEE_TABLE, map, id)) {
//            result = ApplicationConstants.DUPLICATE;
//
//        } else {

            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_EMPLOYEE_TABLE, id);
            List<PensionEmployee> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionEmployee>>() {
            }.getType());
            PensionEmployee categoryobje = categorylist.get(0);

            association.setStatus(ApplicationConstants.ACTIVE);
            association.setCreateDate(categoryobje.getCreateDate());
            association.setUpdateDate(System.currentTimeMillis() + "");
            association.setCreatedBy(categoryobje.getCreatedBy());
            association.setUpdatedBy(userName);

            String classJson = new Gson().toJson(association);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_EMPLOYEE_TABLE, id, classJson);

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
    //   }
        return result;

    }

    /*public List<Employee> searchItFinally(PensionEmployee emp) throws Exception {
     /
     DB db = DBManager.getDB();
     DBCollection collection = db.getCollection(ApplicationConstants.PENSION_EMPLOYEE_TABLE);

     BasicDBObject regexQuery = new BasicDBObject();

     //        if (emp.getDdo() != null) {
     //            regexQuery.put("ddo",
     //                    new BasicDBObject("$regex", emp.getDdo()));
     //        }
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
     //        if (emp.getNatureType() != null) {
     //            regexQuery.put("natureType",
     //                    new BasicDBObject("$regex", emp.getNatureType()));
     //        }
     //        if (emp.getPostingCity() != null) {
     //            regexQuery.put("postingCity",
     //                    new BasicDBObject("$regex", emp.getPostingCity()));
     //        }
     //        if (emp.getFundType() != null) {
     //            regexQuery.put("fundType",
     //                    new BasicDBObject("$regex", emp.getFundType()));
     //        }
     //        if (emp.getBudgetHead() != null) {
     //            regexQuery.put("budgetHead",
     //                    new BasicDBObject("$regex", emp.getBudgetHead()));
     //        }
     if (emp.getEmployeeName() != null) {
     regexQuery.put("employeeName",
     new BasicDBObject("$regex", emp.getEmployeeName()));
     }
     //        if (emp.getEmployeeCode() != null) {
     //            regexQuery.put("employeeCode",
     //                    new BasicDBObject("$regex", emp.getEmployeeCode()));
     //        }
     //        if (emp.getEmployeeCodeM() != null) {
     //            regexQuery.put("employeeCodeM",
     //                    new BasicDBObject("$regex", emp.getEmployeeCodeM()));
     //        }
     //        if (emp.getSalaryBillType() != null) {
     //            regexQuery.put("salaryBillType",
     //                    new BasicDBObject("$regex", emp.getSalaryBillType()));
     //        }
        
     regexQuery.put("status",
     new BasicDBObject("$regex", "Active"));
     //System.out.println(regexQuery);

     DBCursor cursor2 = collection.find(regexQuery);

     List<PensionEmployee> pensionEmployeeList = new ArrayList<PensionEmployee>();
     while (cursor2.hasNext()) {
     DBObject ob = cursor2.next();
     Type type = new TypeToken<Employee>() {
     }.getType();
     PensionEmployee em = new Gson().fromJson(ob.toString(), type);
     pensionEmployeeList.add(em);
     }
     if (pensionEmployeeList.size() > 0) {
     //            try {
     //                employeeList = getReligion(employeeList);
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getSalutation(employeeList);
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getMaritalStatus(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getBank(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getPFBank(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getDDO(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getPostingDDO(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getAssociation(employeeList);
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getDepartment(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getDiscipline(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getNature(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getFundType(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getBudgetHead(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getSalaryType(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     try {
     pensionEmployeeList = getDesignation(pensionEmployeeList);

     } catch (Exception e) {
     }
     //            try {
     //                employeeList = getPostedDesignation(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getCity(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getQuarterNo(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //
     //            try {
     //                employeeList = getGrade(employeeList);
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getPFType(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getSalaryBillType(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getEmployeeLeftStatus(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getHeadSlab(employeeList);
     //
     //            } catch (Exception e) {
     //            }
     //            try {
     //                employeeList = getReportingTo(employeeList);
     //            } catch (Exception e) {
     //            }
     //            try {
     //
     //                employeeList = getLocation(employeeList);
     //            } catch (Exception e) {
     //            }
     //            employeeList = getReligion(employeeList);
     //            employeeList = getSalutation(employeeList);
     //            employeeList = getMaritalStatus(employeeList);
     //            employeeList = getBank(employeeList);
     //            employeeList = getPFBank(employeeList);
     //            employeeList = getDDO(employeeList);
     //            employeeList = getPostingDDO(employeeList);
     //            employeeList = getDepartment(employeeList);
     //            employeeList = getDiscipline(employeeList);
     //            employeeList = getNature(employeeList);
     //            employeeList = getFundType(employeeList);
     //            employeeList = getBudgetHead(employeeList);
     //            employeeList = getSalaryType(employeeList);
     //            employeeList = getDesignation(employeeList);
     //            employeeList = getPostedDesignation(employeeList);
     //            employeeList = getCity(employeeList);
     //            employeeList = getQuarterNo(employeeList);
     //            employeeList = getGrade(employeeList);
     //            employeeList = getPFType(employeeList);
     //            employeeList = getSalaryBillType(employeeList);
     //            employeeList = getEmployeeLeftStatus(employeeList);
     //            employeeList = getHeadSlab(employeeList);
     //            employeeList = getReportingTo(employeeList);
     //            employeeList = getLocation(employeeList);
     }
     //        //System.out.println(pensionEmployeeList.size() + "in Finally Search It");
     //        //System.out.println(pensionEmployeeList.get(0).getId() + "*******************");
     //        return pensionEmployeeList;
     }*/
    public String search(String department) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("stopPension", "NO");

        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);
        return result;

    }

    public String search() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("isArrearProcess", "YES");

        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);
        return result;

    }

    public static void main(String[] args) throws Exception {
        //String result1 = new PensionEmployeeManager().fetchByEmployeeCode("ASB-89");
        //System.out.println(result1);
        //       new PensionEmployeeManager().calculatePension("37400-67000", "10000", "Family Pension");
    }
}
