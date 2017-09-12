/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.analytics;

import com.accure.budget.dto.CreateBudgetExpense;
import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.finance.dto.FinancialAccounting_FinancialYear;
import static com.accure.user.analytics.AnalyticsManager.convertDataToMillsec;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author accure
 */
public class BudgetAnalyticsManager {

    public String getSanctionAmount(String fy, String ddo, String location) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }

        double incomeSanction = 0.0;
        double expenseSanction = 0.0;

        HashMap<Integer, Long> result = new HashMap<Integer, Long>();
        HashMap<String, HashMap<String, Double>> finalresult = new HashMap<String, HashMap<String, Double>>();
        HashMap<Integer, HashMap<Long, Long>> fyMap = new HashMap<Integer, HashMap<Long, Long>>();
        // get all available FY from fy collection
        String strFy = DBManager.getDbConnection().fetchAll(ApplicationConstants.FINANCIAL_YEAR_TABLE);
        if (null != strFy && !strFy.isEmpty()) {
            List<FinancialAccounting_FinancialYear> financialYear = new Gson().fromJson(strFy, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType());
            for (FinancialAccounting_FinancialYear fYear : financialYear) {
                HashMap<Long, Long> map = new HashMap<Long, Long>();
                map.put(convertDataToMillsec(fYear.getFromDate()), convertDataToMillsec(fYear.getToDate()));
                fyMap.put(Integer.parseInt(fYear.getYear()), map);
                result.put(Integer.parseInt(fYear.getYear()), 0l);
            }
        }
        if (null != ddo && !ddo.isEmpty()) {
            HashMap<String, String> cond = new HashMap<String, String>();
            cond.put("ddo", ddo);
            cond.put("financialYear", fy);
            cond.put("location", location);
            cond.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            //       -------------Income Budget-----------------  
            String paymentList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, cond);
            if (null != paymentList && !paymentList.isEmpty()) {
                List<CreateIncomeBudget> payVouList = new Gson().fromJson(paymentList, new TypeToken<List<CreateIncomeBudget>>() {
                }.getType());
                for (CreateIncomeBudget paymentVouc : payVouList) {
                    incomeSanction += Double.parseDouble(paymentVouc.getSanctionedAmount());
                }
            }

            // ----------------------------Expense Budget----------------------   
            String contraList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, cond);
            if (null != contraList && !contraList.isEmpty()) {
                List<CreateBudgetExpense> conVouList = new Gson().fromJson(contraList, new TypeToken<List<CreateBudgetExpense>>() {
                }.getType());
                if (conVouList != null) {
                    for (CreateBudgetExpense contra : conVouList) {
                        expenseSanction += Double.parseDouble(contra.getSanctionedAmount());
                    }
                }

            }

        }
        HashMap<String, Double> datList = new HashMap<String, Double>();
        datList.put("Income", incomeSanction);
        datList.put("Expense", expenseSanction);
        return new Gson().toJson(datList);

    }

    public static void main(String[] args) throws Exception {
        //System.out.println("output" + new BudgetAnalyticsManager().getSanctionAmount("2016", "582f34960c92ec57796a1e13", "582f34b10c92ec57796a1e1a"));
    }
}
