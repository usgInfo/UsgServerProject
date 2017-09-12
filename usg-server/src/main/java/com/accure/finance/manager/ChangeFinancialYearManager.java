/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import java.util.HashMap;

/**
 *
 * @author deepak2310
 */
public class ChangeFinancialYearManager {

    public String fetchCurrentYear() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.FINANCIAL_ACTIVE, ApplicationConstants.YES);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String currentYearJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap);
        return currentYearJson;

    }

    public String fetchFinancialCurrentYear() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.FINANCIAL_ACTIVE, ApplicationConstants.YES);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String currentYearJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, conditionMap);
        return currentYearJson;

    }

    public String fetchAllCurrentYear() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String currentYearJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap);
        return currentYearJson;

    }

    public static String fetchFinancialYear(String fromDate, String toDate) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("fromDate", fromDate.trim());
        conditionMap.put("toDate", toDate.trim());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        //System.out.println(conditionMap.toString());
        String currentYearJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, conditionMap);
        if (currentYearJson == null) {
            return null;
        }
        //System.out.println("-------" + currentYearJson);
        return currentYearJson;
    }

    public static void main(String[] args) throws Exception {
        String result = new ChangeFinancialYearManager().fetchCurrentYear();
        //System.out.println("result: " + result);
    }
}
