/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.common.duplicate.Duplicate;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.GISGroup;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.SuppressedList;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shwetha T S
 */
public class InsuranceTransactionManager {

    /**
     *
     * @return @throws Exception
     */
    public String fetchInsuranceName() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("deductionType", "Insurance");
        conditionMap.put("headType", "Deductions");
        conditionMap.put("active", "Yes");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        return result;
    }

    public String save(InsuranceTransactions incTrans, String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        String result;
        if (incTrans == null) {
            return null;
        } else if (validateDuplicate(incTrans.getEmpCode(), incTrans.getInscName(), incTrans.getPolicyNumber())) {
            result = ApplicationConstants.DUPLICATE;
        } else if (retirementDateValidation(incTrans.getLastInscDate(), incTrans.getEmpCode())) {
            result = ApplicationConstants.RETIREMENT_DATE_VALIDATE_FOR_INSC_TRANS;
        } else {
            incTrans.setStatus(ApplicationConstants.ACTIVE);
            incTrans.setCreateDate(System.currentTimeMillis() + "");
            incTrans.setUpdateDate(System.currentTimeMillis() + "");
            incTrans.setCreatedBy(userName);
            incTrans.setUpdatedBy(userName);
            List<SuppressedList> spsdlist = new ArrayList<SuppressedList>();

            SuppressedList cls = new SuppressedList();

            if (incTrans.getChecked() == true) {

                cls.setIsSuppressed(true);
                cls.setSuppressedDate(System.currentTimeMillis());
                cls.setUnSuppressedDate(0);
                spsdlist.add(cls);
                incTrans.setSuppressedList(spsdlist);

            } else if (incTrans.getChecked() == false) {

                cls.setIsSuppressed(false);
                cls.setUnSuppressedDate(System.currentTimeMillis());
                cls.setSuppressedDate(0);
                spsdlist.add(cls);
                incTrans.setSuppressedList(spsdlist);
            }
            String jsonStr = new Gson().toJson(incTrans);
            result = DBManager.getDbConnection().insert(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, jsonStr);
            if (result != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    public boolean validateDuplicate(String empCode, String inscName, String policyNumber) throws Exception {

        boolean status = false;

        String salaryHead = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, inscName);
        if (salaryHead != null) {
            List<SalaryHead> salaryHeadlist = new Gson().fromJson(salaryHead, new TypeToken<List<SalaryHead>>() {
            }.getType());
            SalaryHead salaryHeadObj = salaryHeadlist.get(0);
            if (salaryHeadObj.getShortDescription().equalsIgnoreCase(ApplicationConstants.LIC)) {
                HashMap duplicaateCheckMap = new HashMap();
                duplicaateCheckMap.put("empCode", empCode);
                duplicaateCheckMap.put("inscName", inscName);
                duplicaateCheckMap.put("policyNumber", policyNumber);

                if (Duplicate.hasDuplicateforSave(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, duplicaateCheckMap)) {
                    status = true;
                }
            } else if (salaryHeadObj.getShortDescription().equalsIgnoreCase(ApplicationConstants.GS_LIS)) {
                HashMap duplicaateCheckMap = new HashMap();
                duplicaateCheckMap.put("empCode", empCode);
                duplicaateCheckMap.put("inscName", inscName);

                if (Duplicate.hasDuplicateforSave(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, duplicaateCheckMap)) {
                    status = true;
                }
            }

        }
        return status;
    }

    public boolean validateDuplicateUpdate(String empCode, String inscName, String policyNumber, String id) throws Exception {
        boolean status = false;

        String salaryHead = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, inscName);
        if (salaryHead != null) {
            List<SalaryHead> salaryHeadlist = new Gson().fromJson(salaryHead, new TypeToken<List<SalaryHead>>() {
            }.getType());
            SalaryHead salaryHeadObj = salaryHeadlist.get(0);
            if (salaryHeadObj.getShortDescription().equalsIgnoreCase(ApplicationConstants.LIC)) {
                HashMap duplicaateCheckMap = new HashMap();
                duplicaateCheckMap.put("empCode", empCode);
                duplicaateCheckMap.put("inscName", inscName);
                duplicaateCheckMap.put("policyNumber", policyNumber);

                if (isDuplicateforUpdate(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, duplicaateCheckMap, id)) {
                    status = true;
                }
            } else if (salaryHeadObj.getShortDescription().equalsIgnoreCase(ApplicationConstants.GS_LIS)) {
                HashMap duplicaateCheckMap = new HashMap();
                duplicaateCheckMap.put("empCode", empCode);
                duplicaateCheckMap.put("inscName", inscName);

                if (isDuplicateforUpdate(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, duplicaateCheckMap, id)) {
                    status = true;
                }
            }

        }
        return status;
    }

    public String fetchAll(String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("empCode", employeeCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, conditionMap);

        if (result == null || result.isEmpty()) {
            return null;
        }
        List<InsuranceTransactions> list = new Gson().fromJson(result, new TypeToken<List<InsuranceTransactions>>() {
        }.getType());

        list = getDDO(list);

        list = getInsuranceName(list);

        list = getDepartment(list);

        list = getDesignation(list);

        String finalresult = new Gson().toJson(list);

        return finalresult;

    }

    public static List<InsuranceTransactions> getDDO(List<InsuranceTransactions> employeeList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDdo())) {
                    employeeList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<InsuranceTransactions> getInsuranceName(List<InsuranceTransactions> employeeList) throws Exception {
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
                if (entry.getKey().equals(employeeList.get(i).getInscName())) {
                    employeeList.get(i).setInscName(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public String update(InsuranceTransactions incTrans, String id, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        String data = fetch(id);
        Type type = new TypeToken<InsuranceTransactions>() {
        }.getType();

        String result;
        if (id == null || userid == null || incTrans == null) {
            result = null;
        } else if (validateDuplicateUpdate(incTrans.getEmpCode(), incTrans.getInscName(), incTrans.getPolicyNumber(), id)) {
            result = ApplicationConstants.DUPLICATE;
        } else if (retirementDateValidation(incTrans.getLastInscDate(), incTrans.getEmpCode())) {
            result = ApplicationConstants.RETIREMENT_DATE_VALIDATE_FOR_INSC_TRANS;
        } else {
            InsuranceTransactions dbdataObj = new Gson().fromJson(data, type);
            dbdataObj.setDdo(incTrans.getDdo());
            dbdataObj.setEmpCode(incTrans.getEmpCode());
            dbdataObj.setDepartment(incTrans.getDepartment());
            dbdataObj.setDesignation(incTrans.getDesignation());
            dbdataObj.setChecked(incTrans.getChecked());
            dbdataObj.setUpdateDate(System.currentTimeMillis() + "");
            dbdataObj.setEffectType(incTrans.getEffectType());
            dbdataObj.setInscName(incTrans.getInscName());
            dbdataObj.setInscDetails(incTrans.getInscDetails());
            dbdataObj.setLastInscDate(incTrans.getLastInscDate());
            dbdataObj.setInstallmentAmount(incTrans.getInstallmentAmount());
            dbdataObj.setPolicyNumber(incTrans.getPolicyNumber());
            dbdataObj.setRemarks(incTrans.getRemarks());
            dbdataObj.setDated(incTrans.getDated());
            dbdataObj.setUpdateDate(System.currentTimeMillis() + "");
            dbdataObj.setStatus(ApplicationConstants.ACTIVE);
            dbdataObj.setUpdatedBy(userName);

            List<SuppressedList> spsdlist = dbdataObj.getSuppressedList();
            if (incTrans.getChecked() == true) {

                SuppressedList cls = new SuppressedList();
                cls.setIsSuppressed(true);
                cls.setSuppressedDate(System.currentTimeMillis());
                cls.setUnSuppressedDate(0);
                spsdlist.add(cls);
            } else if (incTrans.getChecked() == false) {

                SuppressedList cls = new SuppressedList();
                cls.setIsSuppressed(false);
                cls.setSuppressedDate(0);
                cls.setUnSuppressedDate(System.currentTimeMillis());
                spsdlist.add(cls);
            }

            dbdataObj.setSuppressedList(spsdlist);
            String incTransjson = new Gson().toJson(dbdataObj);
            boolean fresult = DBManager.getDbConnection().update(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, id, incTransjson);
            if (fresult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    public String getListOfAutoSalaryEmployee(String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", employeeCode);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LIST_FOR_AUTO_SALARY, conditionMap);
        return result;
    }

    public boolean delete(String id, String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        boolean result;
        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<InsuranceTransactions>() {
        }.getType();
        String bank = new InsuranceTransactionManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        InsuranceTransactions bankJson = new Gson().fromJson(bank, type);
        bankJson.setUpdateDate(System.currentTimeMillis() + "");
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setDeletedBy(userName);
        result = DBManager.getDbConnection().update(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, id, new Gson().toJson(bankJson));
        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, Id);
        List<InsuranceTransactions> list = new Gson().fromJson(result, new TypeToken<List<InsuranceTransactions>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }

    public static InsuranceTransactions get(String Id) throws Exception {
        InsuranceTransactions its = null;
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, Id);
        if (result != null) {
            List<InsuranceTransactions> gisList = new Gson().fromJson(result, new TypeToken<List<InsuranceTransactions>>() {
            }.getType());
            its = gisList.get(0);
        }
        return its;
    }

    public static boolean updateTotalContributions(String id, int contributions) throws Exception {
        if (id == null) {
            return false;
        }
        boolean status = false;
        InsuranceTransactions its = get(id);
        if (its != null) {
            if (contributions == 1) {
                its.setTotalContribution((its.getTotalContribution() + 1));
            } else if (contributions == -1) {
                its.setTotalContribution((its.getTotalContribution() - 1));
            }
            status = DBManager.getDbConnection().update(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, id, new Gson().toJson(its));
        }
        return status;
    }

    public String getEmployeeInEmployeeTable(String employeeCode, String ddo) throws Exception {

        HashMap conditionMap = new HashMap();
        conditionMap.put("employeeCode", employeeCode);
        conditionMap.put("ddo", ddo);
        conditionMap.put("isSuspended", false);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        if (result == null) {
            return null;
        }
        List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
        }.getType());
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(empList.get(0));

        employeeList = getDepartmentOfEmployee(employeeList);

        employeeList = getDesignationOfEmployee(employeeList);

        return new Gson().toJson(employeeList);
    }

    public String getEmployeeInInsuranceTransactionTable(String employeeCode) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("empCode", employeeCode);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, conditionMap);
        
        if(result==null){
            return null;
        }
        List<InsuranceTransactions> employeeList = new Gson().fromJson(result, new TypeToken<List<InsuranceTransactions>>() {
        }.getType());
        List<InsuranceTransactions> empList = new ArrayList<InsuranceTransactions>();
        empList.add(employeeList.get(employeeList.size() - 1));
       
            empList = getDepartment(empList);

      
            empList = getDesignation(empList);

       
        return new Gson().toJson(empList);
    }

    public static List<InsuranceTransactions> getDepartment(List<InsuranceTransactions> employeeList) throws Exception {
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

    public static List<Employee> getDesignationOfEmployee(List<Employee> employeeList) throws Exception {
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

    public static List<Employee> getDepartmentOfEmployee(List<Employee> employeeList) throws Exception {
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

    public static List<InsuranceTransactions> getDesignation(List<InsuranceTransactions> employeeList) throws Exception {
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

    public String getInscEmp(String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("empCode", employeeCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, conditionMap);
        return result;
    }

    public String InsuranceEmployeeDetails(String employeeCode, String ddo) throws Exception {
        String result ;
        if (getInscEmp(employeeCode) != null) {
            result = getEmployeeInInsuranceTransactionTable(employeeCode);
        } else {
            result = getEmployeeInEmployeeTable(employeeCode, ddo);
        }
        return result;
    }

    public boolean duplicate(InsuranceTransactions association) throws Exception {
        boolean res = false;
        
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("empCode", association.getEmpCode());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.INSURANCE_TRANSACTIONS_TABLE, conditionMap);
            if (result != null) {
                List<InsuranceTransactions> list = new Gson().fromJson(result, new TypeToken<List<InsuranceTransactions>>() {
                }.getType());

                for (InsuranceTransactions li : list) {

                    if (association.getEmpCode().equalsIgnoreCase(li.getEmpCode()) && association.getInscName().equalsIgnoreCase(li.getInscName()) && association.getPolicyNumber() == li.getPolicyNumber()) {
                        res = true;
                    }

                }
            }

     
        return res;
    }

    public boolean retirementDateValidation(String lastInsuranceDate, String empCode) throws Exception {
        boolean result1 = false;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", empCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        if (result != null) {
            List<Employee> empList = new Gson().fromJson(result, new TypeToken<List<Employee>>() {
            }.getType());
            String retDate = null;
            for (Employee emp : empList) {
                retDate = emp.getDateOfRetirement();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");

            Date fDate = formatter.parse(lastInsuranceDate);
            Date tDate = formatter.parse(retDate);
            if (tDate.compareTo(fDate) < 0) {
                result1 = true;
            }
        }
        return result1;
    }

    public String getInstallmentAmount(String ddo, String employeeCode, String insuranceName) throws Exception {
        String result = "";
        String salaryHead = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, insuranceName);

        if (salaryHead != null) {
            List<SalaryHead> salaryHeadlist = new Gson().fromJson(salaryHead, new TypeToken<List<SalaryHead>>() {
            }.getType());
            SalaryHead salaryHeadObj = salaryHeadlist.get(0);
            if (salaryHeadObj.getShortDescription().equalsIgnoreCase(ApplicationConstants.GS_LIS)) {
                String employeeStr = new InsuranceTransactionManager().getEmployeeInEmployeeTable(employeeCode, ddo);
                if (employeeStr != null) {
                    List<Employee> empList = new Gson().fromJson(employeeStr, new TypeToken<List<Employee>>() {
                    }.getType());
                    Employee empObj = empList.get(0);
                    if (empObj.getGradePay() != 0) {
                        result = new Gson().toJson(getRateOfSubscription(empObj.getGradePay()));

                    }
                }
            }
        }

        return result;
    }

    public GISGroup getRateOfSubscription(long gradePay) throws Exception {
        GISGroup result = null;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String GISGroupStr = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GIS_GROUP_TABLE, conditionMap);
        if (GISGroupStr != null) {
            List<GISGroup> list = new Gson().fromJson(GISGroupStr, new TypeToken<List<GISGroup>>() {
            }.getType());
            for (GISGroup li : list) {
                Long fromGradePay = Long.parseLong(li.getGradePayFrom());
                Long toGradePay = Long.parseLong(li.getGradePayFrom());
                if (gradePay >= fromGradePay || gradePay <= toGradePay) {
                    return li;
                }
            }
        }
        return result;
    }

    public boolean validateSalaryProcessedOrNot(String empCode) throws Exception {
        boolean fResult = false;
        Calendar now = Calendar.getInstance();
        int month = (now.get(Calendar.MONTH) + 1);
        int year = now.get(Calendar.YEAR);
        if (month == 0) {
            month = 12;
            year = now.get(Calendar.YEAR) - 1;
        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeeCode", empCode);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, conditionMap);
        if (result != null) {

            List<AutoSalaryProcess> empList = new Gson().fromJson(result, new TypeToken<List<AutoSalaryProcess>>() {
            }.getType());

            for (AutoSalaryProcess asp : empList) {
                if ((asp.getMonth() == month) && (asp.getYear() == year)) {
                    fResult = true;
                }
            }
        }

        return fResult;
    }

}
