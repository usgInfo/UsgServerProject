/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class PreviousBudgetAmountDetailsManager {

    private static Logger LOGGER = Logger.getLogger(PreviousBudgetAmountDetailsManager.class);
    private PreviousBudgetAmountDetails previousBudgetAmountDetails;

    public List<PreviousBudgetAmountDetails> getPreviousBudgetAmountDetails(HashMap<String, Object> queryFilterMap,
            String selectedYear) {
        List<PreviousBudgetAmountDetails> preBudAmtDetails = new ArrayList();
        try {

            selectedYear = NumberFormat.getInstance().parse(selectedYear).intValue() - 1 + "";

            String bet = ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS;

            RestClient aql = new RestClient();
            String budgetExpenseTable = ApplicationConstants.USG_DB1
                    + bet + "`";

            String budgetExpenseQuery = "select " + bet + ".budgetHead, " + bet + ".budgetHeadName, " + bet + ".ddo, "
                    + bet + ".budgetType, " + bet + ".financialYear, "
                    + bet + ".actualAmount, " + bet + ".ledgerId from " + budgetExpenseTable + " as " + bet;

            Set<Map.Entry<String, Object>> querySet = queryFilterMap.entrySet();
            int i = 0;
            for (Map.Entry<String, Object> queryEntry : querySet) {
                Object value = "";
                if (queryEntry.getKey().equals("financialYear")) {
                    value = selectedYear;
                } else {
                    value = queryEntry.getValue();
                }
                if (i++ == 0) {
                    budgetExpenseQuery += " where " + bet + "." + queryEntry.getKey() + "=\"" + value + "\"";
                } else {
                    budgetExpenseQuery += " and " + bet + "." + queryEntry.getKey() + "=\"" + value + "\"";
                }
            }

            NumberFormat numberFormat = NumberFormat.getInstance();

            String nextFinancialYear = numberFormat.parse(selectedYear).intValue() + 1 + "";

            if (budgetExpenseQuery.contains("where")) {
                budgetExpenseQuery += " and " + bet + ".financialYear >=\"" + selectedYear
                        + "\" and " + bet + ".financialYear<=\"" + nextFinancialYear + "\"";
            } else {
                budgetExpenseQuery += " where " + bet + ".financialYear>=\"" + selectedYear
                        + "\" and " + bet + ".financialYear<=\"" + nextFinancialYear + "\"";
            }

            String resultString = aql.getRestData(ApplicationConstants.END_POINT, budgetExpenseQuery);

            if (resultString != null) {
                preBudAmtDetails = new Gson().fromJson(resultString,
                        new TypeToken<List<PreviousBudgetAmountDetails>>() {
                }.getType());
            }

            return preBudAmtDetails;
        } catch (ParseException parseException) {
            LOGGER.error("getPreviousBudgetAmountDetails", parseException);
        } catch (JsonSyntaxException jsonSyntaxException) {
            LOGGER.error("getPreviousBudgetAmountDetails", jsonSyntaxException);
        }
        return null;
    }

    public PreviousBudgetAmountDetails getPreviousBudgetAmountDetails() {
        return previousBudgetAmountDetails;
    }

    public void setPreviousBudgetAmountDetails(PreviousBudgetAmountDetails previousBudgetAmountDetails) {
        this.previousBudgetAmountDetails = previousBudgetAmountDetails;
    }

}
