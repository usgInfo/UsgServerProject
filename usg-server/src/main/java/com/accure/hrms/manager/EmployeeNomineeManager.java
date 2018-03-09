/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Association;
import com.accure.hrms.dto.Bank;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Discipline;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeeNominee;
import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.Nominee;
import com.accure.hrms.dto.PFType;
import com.accure.hrms.dto.Quarter;
import com.accure.hrms.dto.Relation;
import com.accure.hrms.dto.Religion;
import com.accure.hrms.dto.SalaryHead;
import static com.accure.hrms.manager.SalaryHeadManager.getChapterVIType;
import static com.accure.hrms.manager.SalaryHeadManager.getDeductionType;
import static com.accure.hrms.manager.SalaryHeadManager.getFixedHead;
import static com.accure.hrms.manager.SalaryHeadManager.getFormula;
import static com.accure.hrms.manager.SalaryHeadManager.getParentHead;
import static com.accure.hrms.manager.SalaryHeadManager.getSectionPart;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shwetha T S
 */
public class EmployeeNomineeManager {

    /**
     * This method is to Insert <code>EmployeeNominee</code> Object data to
     * <code>ApplicationConstants.NOMINEE_TABLE</code>
     *
     * @param nomineeJson is JSON String of EmployeeNominee Object data
     * @param userId is <code>_id</code> value of <code>User</code> object in
     * hexadecimal String.
     * @return
     * <p>
     * <code>ApplicationConstants.DUPLICATE_EMP_CODE</code> If
     * <code>EmployeeNominee</code> list already contains
     * <code>employeeCode</code> value</p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE_NOMINEES</code> If
     * <code>Nominee</code> list already contains <code>name</code> and
     * <code>relationship</code> or</p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE_PRIMARIES</code> If
     * <code>Nominee</code> list of <code>nomineePrimary</code> already contains
     * value as "YES" or</p>
     * <p>
     * <code>_id</code> value of <code>EmployeeNominee</code> Object in
     * hexadecimal String If Insertion of <code>EmployeeNominee</code> object is
     * successful</p>
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String saveNomineeData(String nomineeJson, String userId) throws Exception {
        String result;
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (nomineeJson == null || nomineeJson.isEmpty()) {
            return null;
        }
        Type type = new TypeToken<EmployeeNominee>() {
        }.getType();
        EmployeeNominee nomineeDTO = new Gson().fromJson(nomineeJson, type);
        if (duplicateEmpCode(nomineeDTO)) {
            return ApplicationConstants.DUPLICATE_EMP_CODE;
        } else if (duplicateNomineeList(nomineeDTO)) {
            return ApplicationConstants.DUPLICATE_NOMINEES;
        } else if (duplicatePrimary(nomineeDTO)) {

            return ApplicationConstants.DUPLICATE_PRIMARIES;
        } else {
            nomineeDTO.setStatus(ApplicationConstants.ACTIVE);
            nomineeDTO.setCreateDate(System.currentTimeMillis() + "");
            nomineeDTO.setUpdateDate(System.currentTimeMillis() + "");
            nomineeDTO.setCreatedBy(userName);
            nomineeDTO.setUpdatedBy(userName);
            String json = new Gson().toJson(nomineeDTO, type);
            result = DBManager.getDbConnection().insert(ApplicationConstants.NOMINEE_TABLE, json);
        }
        return result;
    }

    /**
     * This method is to check whether <code>EmployeeNominee</code> list
     * contains <code>employeeCode</code>.
     *
     * @param empNominee <code>EmployeeNominee</code> Object data
     * @return
     * <p>
     * <code>True</code> If <code>EmployeeNominee</code> list already contains
     * <code>employeeCode</code>
     * @throws Exception
     */
    public boolean duplicateEmpCode(EmployeeNominee empNominee) throws Exception {
        if (empNominee == null) {
            return false;
        }
        boolean res = false;

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NOMINEE_TABLE, conditionMap);
        if (result != null) {
            List<EmployeeNominee> list = new Gson().fromJson(result, new TypeToken<List<EmployeeNominee>>() {
            }.getType());

            for (EmployeeNominee li : list) {
                if (empNominee.getEmployeeCode().equalsIgnoreCase(li.getEmployeeCode())) {
                    return true;
                }
            }
        }

        return res;
    }

    /**
     * This method is to check whether <code>Nominee</code> list already
     * contains <code>name</code> and <code>relation</code>.
     *
     * @param empNominee <code>EmployeeNominee</code> Object data
     * @return
     * <p>
     * <code>True</code> If <code>Nominee</code> list already contains both
     * <code>name</code> and <code>relation</code>
     * </p>
     * @throws Exception
     */
    public boolean duplicateNomineeList(EmployeeNominee empNominee) throws Exception {
        if (empNominee == null) {
            return false;
        }
        boolean res = false;
        List<Nominee> uList = empNominee.getNomineeList();
        for (int i = uList.size() - 1; i >= 0; i--) {
            if ((i - 1) >= 0) {
                if ((uList.get(i).getName().equalsIgnoreCase(uList.get(i - 1).getName())) && (uList.get(i).getRelationShip().equalsIgnoreCase(uList.get(i - 1).getRelationShip()))) {
                    res = true;
                }
            }

        }
        return res;
    }

    /**
     * This method is to check whether <code>Nominee</code> list already
     * contains <code>nomineePrimary</code> value as "YES".
     *
     * @param empNominee <code>EmployeeNominee</code> Object data
     * @return
     * <p>
     * <code>True</code> If <code>EmployeeNominee</code> list already contains
     * <code>nomineePrimary</code> value as "YES" (case insensitive).
     * @throws Exception
     */
    public boolean duplicatePrimary(EmployeeNominee empNominee) throws Exception {
        if (empNominee == null) {
            return false;
        }
        boolean res = false;
        List<Nominee> uList = empNominee.getNomineeList();
        for (int i = uList.size() - 1; i >= 0; i--) {
            if ((i - 1) >= 0) {

                if ((uList.get(i).getNomineePrimary().equalsIgnoreCase(uList.get(i - 1).getNomineePrimary()))) {
                    res = true;
                }
            }

        }
        return res;
    }

    /**
     * This method is to get List of <code>SalaryHead</code>
     *
     * @return List of <code>SalaryHeads</code>
     * @throws Exception
     */
    public String getNomineeType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("forNominee", "Yes");
        conditionMap.put("active", "Yes");
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
       
        return new Gson().toJson(list);
    }

    /**
     * This method is to search <code>EmployeeNominee</code> based on
     * <code>ddo</code> field of <code>EmployeeNominee</code> Object.
     *
     * @param ddo <code>DDO</code> object <code>_id</code> value in hexadecimal
     * String
     * @return List of <code>EmployeeNominee</code>
     * @throws Exception if argument is <code>null</code>..
     */
    public String view(String ddo) throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NOMINEE_TABLE, hMap);
        List<EmployeeNominee> empList = new Gson().fromJson(result, new TypeToken<List<EmployeeNominee>>() {
        }.getType());
        if (result != null) {
            for (EmployeeNominee cl : empList) {
                if (cl.getDepartment() != null) {
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
                    List<Department> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Department>>() {
                    }.getType());

                    Department gal = gaList.get(0);

                    cl.setDepartment(gal.getDepartment());
                }
                if (cl.getDesignation() != null) {
                    String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, cl.getDesignation());
                    List<Designation> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<Designation>>() {
                    }.getType());
                    Designation gal1 = gaList1.get(0);

                    cl.setDesignation(gal1.getDesignation());
                }
            }
        }
        return new Gson().toJson(empList);
    }

    /**
     * This Method is to Search the <code>Employee</code> Object based on
     * <code>_id</code> value of <code>Employee</code> Object in hexadecimal
     * String.
     *
     * @param primaryKey <code>_id</code> value of <code>Employee</code> Object
     * in hexadecimal String.
     * @return <code>EmployeeNominee</code> Object data in JSON String format.
     * @throws Exception if <code>primaryKey</code> value is
     * <code>null</code>...
     */
    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.NOMINEE_TABLE, primaryKey);
        List<EmployeeNominee> empList = new Gson().fromJson(result, new TypeToken<List<EmployeeNominee>>() {
        }.getType());
        if (empList == null || empList.size() < 1) {
            return null;
        }
        return new Gson().toJson(empList.get(0));
    }

    /**
     * This Method is to update <code>EmployeeNominee</code> Object data.
     *
     * @param primaryKey <code>_id</code> value of <code>EmployeeNominee</code>
     * Object in hexadecimal String
     * @param object <code>EmployeeNominee</code> Object data
     * @param userId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String.
     * @return
     * <p>
     * <code>ApplicationConstants.DUPLICATE_NOMINEES</code> If
     * <code>Nominee</code> list already contains <code>name</code> and
     * <code>relationship</code> or</p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE_PRIMARIES</code> If
     * <code>Nominee</code> list of <code>nomineePrimary</code> already contains
     * value as "YES" or</p>
     * <p>
     * <code> ApplicationConstants.SUCCESS</code> If updation of
     * <code>EmployeeNominee</code> object is successful or</p>
     * <p>
     * <code> ApplicationConstants.SUCCESS</code> If updation of
     * <code>EmployeeNominee</code> object is fail</p>
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String update(String primaryKey, EmployeeNominee object, String userId) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty() || userId == null || userId.isEmpty()) {
            return null;
        }
        if (object == null) {
            return null;
        }
        String data = fetch(primaryKey);
        Type type = new TypeToken<EmployeeNominee>() {
        }.getType();
        String result;
        EmployeeNominee dbdataObj = new Gson().fromJson(data, type);

        if (duplicateNomineeList(object)) {
            result = ApplicationConstants.DUPLICATE_NOMINEES;
        } else if (duplicateNomineeList(object)) {
            result = ApplicationConstants.DUPLICATE_PRIMARIES;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            dbdataObj.setDdo(object.getDdo());
            dbdataObj.setDepartment(object.getDepartment());
            dbdataObj.setDesignation(object.getDesignation());
            dbdataObj.setEmployeeCode(object.getEmployeeCode());
            dbdataObj.setNomineeList(object.getNomineeList());
            dbdataObj.setRemarks(object.getRemarks());
            dbdataObj.setUpdateDate(System.currentTimeMillis() + "");
            dbdataObj.setUpdatedBy(userName);
            dbdataObj.setStatus(ApplicationConstants.ACTIVE);
            String nomineejson = new Gson().toJson(dbdataObj);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.NOMINEE_TABLE, primaryKey, nomineejson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    /**
     * This Method is to update <code>status</code> field of
     * <code>EmployeeNominee</code> Object.
     *
     * @param primaryKey <code>_id</code> value of <code>EmployeeNominee</code>
     * Object in hexadecimal String
     *
     * @param userId <code>_id</code> value of  <code>User</code> Object in
     * hexadecimal String
     * @return
     * <p>
     * <code>True</code> If updation of <code>EmployeeNominee</code> Object data
     * is successful
     * </p>
     * <p>
     * <code>False</code> If updation of <code>EmployeeNominee</code> Object
     * data is fail
     * </p>
     * @throws Exception if either of argument is <code>null</code>...
     */
    public boolean delete(String primaryKey, String userId) throws Exception {

        if (primaryKey == null || primaryKey.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<EmployeeNominee>() {
        }.getType();
        String empNom = new EmployeeNomineeManager().fetch(primaryKey);
        if (empNom == null || empNom.isEmpty()) {
            return false;
        }
        EmployeeNominee empNomObj = new Gson().fromJson(empNom, type);
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        empNomObj.setStatus(ApplicationConstants.DELETE);
        empNomObj.setDeletedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.NOMINEE_TABLE, primaryKey, new Gson().toJson(empNomObj));
        return result;
    }

    /**
     * This is to Search  <code>Employee</code> object based on employeeCode
     * field.
     *
     * @param employeeCode employeeCode of <code>Employee</code> Object
     * @return List of Employee in JSON String Format.
     * @throws Exception if argument is <code>null</code>..
     */
    public String getList(String employeeCode) throws Exception {
        if (employeeCode == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("employeeCode", employeeCode);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NOMINEE_TABLE, map);
        List<EmployeeNominee> list = new Gson().fromJson(result, new TypeToken<List<EmployeeNominee>>() {
        }.getType());

        List<Nominee> nlist = new ArrayList<Nominee>();
        for (EmployeeNominee en : list) {

            nlist = en.getNomineeList();
            nlist = getRelation(nlist);

        }

        return new Gson().toJson(nlist);
    }

    /**
     * @param nomineeList List of <code>Nominee</code> .
     * @return Nominee List with relation value.
     * @throws Exception if argument is <code>null</code>..
     */
    public static List<Nominee> getRelation(List<Nominee> nomineeList) throws Exception {

        Map<String, String> RelationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.RELATION_TABLE);
        List<Relation> religionList = new Gson().fromJson(result, new TypeToken<List<Relation>>() {
        }.getType());
        for (Iterator<Relation> iterator = religionList.iterator(); iterator.hasNext();) {
            Relation next = iterator.next();

            RelationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getRelation());
        }
        for (int i = 0; i < nomineeList.size(); i++) {
            String religionId = nomineeList.get(i).getRelationShip();
            for (Map.Entry<String, String> entry : RelationMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(religionId)) {
                    nomineeList.get(i).setRelationShip(value);
                }
            }
        }
        return nomineeList;
    }

    /**
     * This is to Search  <code>Employee</code> object based on employeeCode
     * field of <code>Employee</code> Object.
     *
     * @param empcode employeeCode of <code>Employee</code> Object
     * @return List of Employee in JSON String Format.
     * @throws Exception if argument is <code>null</code>..
     */
    public String getEmpDetails(String empcode) throws Exception {
        if (empcode == null) {
            return null;
        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", empcode);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());

        for (Employee li : empList) {
            if (li.getDepartment() != null && !li.getDepartment().isEmpty()) {
                String deptStr = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, li.getDepartment());
                List<Department> list = new Gson().fromJson(deptStr, new TypeToken<List<Department>>() {
                }.getType());
                Department dept = list.get(0);
                li.setDepartment(dept.getDepartment());
            }

            if (li.getDesignation() != null && !li.getDesignation().isEmpty()) {
                String degnStr = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, li.getDesignation());
                List<Designation> list = new Gson().fromJson(degnStr, new TypeToken<List<Designation>>() {
                }.getType());
                Designation degn = list.get(0);
                li.setDesignation(degn.getDesignation());
            }

            if (li.getQuarterNo() != null && !li.getQuarterNo().isEmpty()) {

                String qtrStr = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_TABLE, li.getQuarterNo());

                List<Quarter> list = new Gson().fromJson(qtrStr, new TypeToken<List<Quarter>>() {
                }.getType());
                Quarter qtr = list.get(0);
                li.setQuarterNo(qtr.getQuarterNo());

            }
            if (li.getReligion() != null && !li.getReligion().isEmpty()) {
                String religionStr = DBManager.getDbConnection().fetch(ApplicationConstants.RELIGION_TABLE, li.getReligion());
                List<Religion> list = new Gson().fromJson(religionStr, new TypeToken<List<Religion>>() {
                }.getType());
                Religion religion = list.get(0);
                li.setReligion(religion.getReligion());
            }

            if (li.getDdo() != null && !li.getDdo().isEmpty()) {
                String ddoStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, li.getDdo());
                List<DDO> list = new Gson().fromJson(ddoStr, new TypeToken<List<DDO>>() {
                }.getType());
                DDO ddo = list.get(0);
                li.setDdo(ddo.getDdoName());
            }
            if (li.getNatureType() != null && !li.getNatureType().isEmpty()) {

                String natureTypeStr = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, li.getNatureType());

                List<Nature> list = new Gson().fromJson(natureTypeStr, new TypeToken<List<Nature>>() {
                }.getType());
                Nature natureType = list.get(0);
                li.setNatureType(natureType.getNatureName());

            }
            if (li.getDiscipline() != null && !li.getDiscipline().isEmpty()) {

                String discplineStr = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, li.getDiscipline());

                List<Discipline> list = new Gson().fromJson(discplineStr, new TypeToken<List<Discipline>>() {
                }.getType());
                Discipline discpline = list.get(0);
                li.setDiscipline(discpline.getDisciplineName());

            }
            if (li.getBudgetHead() != null && !li.getBudgetHead().isEmpty()) {

                String budgetHeadStr = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, li.getBudgetHead());

                List<BudgetHeadMaster> list = new Gson().fromJson(budgetHeadStr, new TypeToken<List<BudgetHeadMaster>>() {
                }.getType());
                BudgetHeadMaster budgetHead = list.get(0);
                li.setBudgetHead(budgetHead.getBudgetHead());

            }
            if (li.getAssociation() != null && !li.getAssociation().isEmpty()) {

                String assStr = DBManager.getDbConnection().fetch(ApplicationConstants.ASSOCIATION_TABLE, li.getAssociation());

                List<Association> list = new Gson().fromJson(assStr, new TypeToken<List<Association>>() {
                }.getType());
                Association ass = list.get(0);
                li.setAssociation(ass.getAssociationName());

            }
            if (li.getBank() != null && !li.getBank().isEmpty()) {

                String bankStr = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_TABLE, li.getBank());

                List<Bank> list = new Gson().fromJson(bankStr, new TypeToken<List<Bank>>() {
                }.getType());
                Bank bank = list.get(0);
                li.setBank(bank.getBankname());

            }

            if (li.getPostingCity() != null && !li.getPostingCity().isEmpty()) {

                String cityStr = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, li.getPostingCity());

                List<CityMaster> list = new Gson().fromJson(cityStr, new TypeToken<List<CityMaster>>() {
                }.getType());
                CityMaster city = list.get(0);
                li.setPostingCity(city.getCityName());

            }

            if (li.getPfType() != null && !li.getPfType().isEmpty()) {

                String pfTypeStr = DBManager.getDbConnection().fetch(ApplicationConstants.PF_TYPE_MASTER, li.getPfType());

                List<PFType> list = new Gson().fromJson(pfTypeStr, new TypeToken<List<PFType>>() {
                }.getType());
                PFType pfType = list.get(0);
                li.setPfType(pfType.getPFType());

            }

        }
        return new Gson().toJson(empList);

    }

    /**
     * This is to search <code>Employee</code> object based on DDO value.
     *
     * @param ddo  <code>_id</code> value of <code>DDO</code> Object in
     * hexadecimal String
     * @return List of Employee in JSON String Format.
     * @throws Exception if argument is <code>null</code>..
     */
    public String getEmpCodes(String ddo) throws Exception {
        String output = null;
        if (ddo == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        if (result != null) {
            List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
            }.getType());

            for (Employee li : empList) {
                String ddoStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, li.getDdo());
                if (ddoStr != null) {
                    List<DDO> list = new Gson().fromJson(ddoStr, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO ddoObj = list.get(0);
                    li.setDdo(ddoObj.getDdoName());
                }
            }
            output = new Gson().toJson(empList);
        }
        return output;
    }

    /**
     * This method is to search <code>Employee</code> Object based on
     * DDO,location and dateOfJoining fields of <code>Employee</code> Object.
     *
     * @param ddo  <code>_id</code> value of <code>DDO</code> Object in
     * hexadecimal String.
     * @param location  <code>_id</code> value of <code>Location</code> Object in
     * hexadecimal String.
     * @param fromDate <code>FinancialYear</code> Object fromDate value .
     * @param toDate <code>FinancialYear</code> Object toDate value.
     * @return List of <code>Employee</code> Object in JSON String format.
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String getEmpCodesBasedOnDDOandLocation(String ddo, String location, String fromDate, String toDate) throws Exception {
        String output;
        if (ddo == null) {
            return null;
        }
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        DBObject condition = new BasicDBObject(2);

        if (ddo != "") {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", ddo));
        }

        if (location != "") {
            regexQuery.put("location",
                    new BasicDBObject("$regex", location));

        }
        regexQuery.put("EmployeeLeftStatus", "No");
        regexQuery.put("status", ApplicationConstants.ACTIVE);
        long fDate = convertDataToMillsec(fromDate);
        long tDate = convertDataToMillsec(toDate);
        condition.put("$lte", tDate);

        if (fDate != 0 && fDate != 0) {
            regexQuery.put("dateOfJoiningInMillisecond",
                    condition);

        }

        DBCursor cursor = collection.find(regexQuery);
        List<Employee> employeeList = new ArrayList<Employee>();
        while (cursor.hasNext()) {
            DBObject ob = cursor.next();
            Type type1 = new TypeToken<Employee>() {
            }.getType();
            Employee em = new Gson().fromJson(ob.toString(), type1);
            employeeList.add(em);
        }

        for (Employee cl : employeeList) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
            if (gaJson != null) {
                List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
                }.getType());
                DDO gal = gaList.get(0);
                cl.setDdo(gal.getDdoName());
            }
        }

        output = new Gson().toJson(employeeList);

        return output;
    }

    /**
     * This method is to convert Date in <code>String</code> format to
     * <code>Long</code> format.
     *
     * @param dateStr Date String
     * @return Date value in Long.
     * @throws ParseException if argument is not in parseable format.
     */
    public static Long convertDataToMillsec(String dateStr) throws ParseException {
        if (null == dateStr || dateStr.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }

}
