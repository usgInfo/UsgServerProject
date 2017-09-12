/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.budget.dto.FundType;
import com.accure.hrms.dto.Bank;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.PFType;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author deepak2310
 */
public class SalarySlipManager {

    public List<Department> fetchAllDapartment() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String departmentJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DEPARTMENT_TABLE, conditionMap);
        List<Department> departmentList = new Gson().fromJson(departmentJson, new TypeToken<List<Department>>() {
        }.getType());
        return departmentList;
    }

    public List<Designation> fetchAllDesignation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String designationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_TABLE, conditionMap);
        List<Designation> designationList = new Gson().fromJson(designationJson, new TypeToken<List<Designation>>() {
        }.getType());
        return designationList;
    }

    public List<Employee> fetchAllEmployeeData() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String postingCityJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> postingCityList = new Gson().fromJson(postingCityJson, new TypeToken<List<Employee>>() {
        }.getType());
        return postingCityList;
    }
    
    public List<PFType> fetchAllPFType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        //System.out.println("conditionMap"+conditionMap);
        String pfTypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PF_TYPE_MASTER,conditionMap);
        //System.out.println("PFType"+pfTypeJson);
        List<PFType> postingCityList = new Gson().fromJson(pfTypeJson, new TypeToken<List<PFType>>() {
        }.getType());
        return postingCityList;
    }
    public List<CityMaster> fetchAllPostingCity() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String postingCityJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CITY_TABLE, conditionMap);
        List<CityMaster> postingCityList = new Gson().fromJson(postingCityJson, new TypeToken<List<CityMaster>>() {
        }.getType());
        return postingCityList;
    }

    public List<FundType> fetchAllFundType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fundTypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_TYPE_TABLE, conditionMap);
        List<FundType> fundTypeList = new Gson().fromJson(fundTypeJson, new TypeToken<List<FundType>>() {
        }.getType());
        return fundTypeList;
    }

    public List<BudgetHeadMaster> fetchAllBudgetHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetHeadJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> budgetHeadList = new Gson().fromJson(budgetHeadJson, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        return budgetHeadList;
    }

    public Set<String> fetchAllBank() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String bankJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_TABLE, conditionMap);
        List<Bank> bankList = new Gson().fromJson(bankJson, new TypeToken<List<Bank>>() {
        }.getType());
        Set<String> bankSet = new HashSet<String>();
        for (Bank bankName : bankList) {
            bankSet.add("<option value='" + bankName.getId() + "'>" + bankName.getBankname() + "</option>");
//            bankSet.add(bankName.getId() + "");
        }
        return bankSet;
    }

    public Set<String> fetchAllGrade() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String gradeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        List<Employee> gradeList = new Gson().fromJson(gradeJson, new TypeToken<List<Employee>>() {
        }.getType());
        Set<String> gradeSet = new HashSet<String>();
        gradeSet.add("<option value='0'>----Select Grade----</option>");
        for (Employee grade : gradeList) {
            gradeSet.add("<option value='" + grade.getId() + "'>" + grade.getGrade() + "</option>");
        }
        return gradeSet;
    }


}
