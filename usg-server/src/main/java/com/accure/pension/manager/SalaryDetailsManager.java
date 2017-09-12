/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.Earnings;
import com.accure.pension.dto.ManageSalaryDetails;
import com.accure.pension.dto.PensionEmployee;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class SalaryDetailsManager {

    public String fetch(String employeecode, String employeeName) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeecode", employeecode);
        conditionMap.put("employeeName", employeeName);
        conditionMap.put("processStatus", "pension processed");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);

        List<PensionEmployee> empList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());

        return result;
    }

     public String fetchEmployee(String employeecode, String employeeName,int month,int year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("employeecode", employeecode);
        conditionMap.put("employeeName", employeeName);
        conditionMap.put("processStatus", "pension processed");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);

        List<PensionEmployee> empList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());

        return result;
    }

    
    
    
    
    public int searchStringInArray(String month, String[] strArr) {
        for (int j = 0; j < strArr.length; j++) {
            if (strArr[j].equals(month)) {
                return j;
            }
        }
        return -1;
    }

    public String save(ManageSalaryDetails manSalDet) throws Exception {

        manSalDet.setCreateDate(System.currentTimeMillis() + "");
        manSalDet.setUpdateDate(System.currentTimeMillis() + "");
        manSalDet.setStatus(ApplicationConstants.ACTIVE);

        String ddoJson = new Gson().toJson(manSalDet);

        String ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_PENSION_SALARY_DETAILS_TABLE, ddoJson);
        return ddoresult;

    }

    public String view() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_PENSION_SALARY_DETAILS_TABLE, conditionMap);

        return result;

    }

    public boolean delete(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<ManageSalaryDetails>() {
        }.getType();
        String bank = new SalaryDetailsManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        ManageSalaryDetails bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.MANAGE_PENSION_SALARY_DETAILS_TABLE, id, new Gson().toJson(bankJson));

        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.MANAGE_PENSION_SALARY_DETAILS_TABLE, Id);
        List<ManageSalaryDetails> gisList = new Gson().fromJson(result, new TypeToken<List<ManageSalaryDetails>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public boolean update(ManageSalaryDetails headCodeType, String id) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.MANAGE_PENSION_SALARY_DETAILS_TABLE, id);
        List<ManageSalaryDetails> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<ManageSalaryDetails>>() {
        }.getType());
        ManageSalaryDetails relation = relationlist.get(0);
        relation.setCreateDate(relation.getCreateDate());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.MANAGE_PENSION_SALARY_DETAILS_TABLE, id, bankJson);
        return result;
    }

}
