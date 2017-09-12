/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetGovtHead;
import com.accure.finance.dto.MajorHead;
import com.accure.finance.dto.MinorHead;
import com.accure.finance.dto.SubMajorHead;
import com.accure.finance.dto.SubMinorHead;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author upendra
 */
public class BudgetGovtHeadManager {

    public String saveBudgetGovthead(BudgetGovtHead budgetgov) throws Exception {

        budgetgov.setCreateDate(System.currentTimeMillis() + "");
        budgetgov.setUpdateDate(System.currentTimeMillis() + "");
        budgetgov.setStatus(ApplicationConstants.ACTIVE);

        String budgetgovJson = new Gson().toJson(budgetgov);

        String budgetgovid = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_GOVT_BUDGETHEAD_TABLE, budgetgovJson);
        return budgetgovid;
    }

    public String viewBudgetGovtHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_GOVT_BUDGETHEAD_TABLE, conditionMap);
        List<BudgetGovtHead> empList = new Gson().fromJson(result, new TypeToken<List<BudgetGovtHead>>() {
        }.getType());

        for (BudgetGovtHead cl : empList) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, cl.getMajorHead());
            List<MajorHead> gaList = new Gson().fromJson(gaJson, new TypeToken<List<MajorHead>>() {
            }.getType());
            MajorHead gal = gaList.get(0);
            cl.setMajorHead(gal.getMajorHead());
            String minorid = cl.getMinorHead();
            String minorjson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, minorid);
            List<MinorHead> gaList1 = new Gson().fromJson(minorjson, new TypeToken<List<MinorHead>>() {
            }.getType());
            MinorHead gal1 = gaList1.get(0);
            cl.setMinorHead(gal1.getMinorHead());
            String gaJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, cl.getSubMajorHead());
            List<SubMajorHead> gaList2 = new Gson().fromJson(gaJson2, new TypeToken<List<SubMajorHead>>() {
            }.getType());
            SubMajorHead gal2 = gaList2.get(0);
            cl.setSubMajorHead(gal2.getSubMajorHead());
            String gaJson3 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, cl.getSubMinorHead());
            List<SubMinorHead> gaList3 = new Gson().fromJson(gaJson3, new TypeToken<List<SubMinorHead>>() {
            }.getType());
            SubMinorHead gal3 = gaList3.get(0);
            cl.setSubMinorHead(gal3.getSubMinorHead());
        }
        return new Gson().toJson(empList);

    }

    public boolean deleteBudgetGovt(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_GOVT_BUDGETHEAD_TABLE, rid);
        List<BudgetGovtHead> budgetgovtlist = new Gson().fromJson(existrelationJson, new TypeToken<List<BudgetGovtHead>>() {
        }.getType());
        BudgetGovtHead budgetgovt = budgetgovtlist.get(0);
        BudgetGovtHead budgetgovtobje = new BudgetGovtHead();
        budgetgovtobje.setMajorHead(budgetgovt.getMajorHead());
        budgetgovtobje.setMinorHead(budgetgovt.getMinorHead());
        budgetgovtobje.setSubMajorHead(budgetgovt.getSubMajorHead());
        budgetgovtobje.setSubMinorHead(budgetgovt.getSubMinorHead());
        budgetgovtobje.setOrder(budgetgovt.getOrder());
        budgetgovtobje.setRemarks(budgetgovt.getRemarks());
        budgetgovtobje.setCreateDate(budgetgovt.getCreateDate());
        budgetgovtobje.setStatus(ApplicationConstants.DELETE);
        budgetgovtobje.setUpdateDate(System.currentTimeMillis() + "");
        String budgetgovtJson = new Gson().toJson(budgetgovtobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_GOVT_BUDGETHEAD_TABLE, rid, budgetgovtJson);
        return status;
    }

    public boolean updateBudgetGovtHead(BudgetGovtHead bank, String bankId) throws Exception {

        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(bank);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_GOVT_BUDGETHEAD_TABLE, bankId, bankJson);

        return result;
    }

}
