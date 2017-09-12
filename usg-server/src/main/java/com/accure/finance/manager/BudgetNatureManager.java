/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.db.in.DAO;
import com.accure.finance.dto.BudgetNature;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author deepak2310
 */
public class BudgetNatureManager {

    public String createBudgetNature(BudgetNature budgetNature, String UserId) throws Exception {

        User UserDB = new UserManager().fetch(UserId);
        String name = UserDB.getFname() + " " + UserDB.getLname();

        budgetNature.setCreateDate(System.currentTimeMillis() + "");
        budgetNature.setUpdateDate(System.currentTimeMillis() + "");
        budgetNature.setStatus(ApplicationConstants.ACTIVE);
        budgetNature.setCreatedBy(name);

        Type type = new TypeToken<BudgetNature>() {
        }.getType();
        String budgetNatureJson = new Gson().toJson(budgetNature, type);

        String budgetNatureId = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_NATURE_TABLE, budgetNatureJson);
        return budgetNatureId;
    }

    public List<BudgetNature> fetchAllBudgetNature() throws Exception {
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetNatureJson = dao.fetchAllRowsByConditions(ApplicationConstants.BUDGET_NATURE_TABLE, conditionMap);
        //dao.close();
        List<BudgetNature> budgetNatureList = new Gson().fromJson(budgetNatureJson, new TypeToken<List<BudgetNature>>() {
        }.getType());
        return budgetNatureList;
    }

    public boolean updateBudgetNature(BudgetNature budgetNature, String userId, String budgetNatureId) throws Exception {

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        BudgetNature budgetNatureDb = fetch(budgetNatureId);
        if (budgetNatureDb.getBudgetNatureName() != null || !budgetNatureDb.getBudgetNatureName().isEmpty()) {
            budgetNatureDb.setBudgetNatureName(budgetNature.getBudgetNatureName());
        }
        if (budgetNatureDb.getDescription() != null || !budgetNatureDb.getDescription().isEmpty()) {
            budgetNatureDb.setDescription(budgetNature.getDescription());
        }
        budgetNatureDb.setUpdateDate(System.currentTimeMillis() + "");
        budgetNatureDb.setUpdatedBy(userName);
        budgetNatureDb.setStatus(ApplicationConstants.ACTIVE);

        String budgetNatureJson = new Gson().toJson(budgetNatureDb);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_NATURE_TABLE, budgetNatureId, budgetNatureJson);
        if (status) {
            return true;
        }
        return false;
    }

    public BudgetNature fetch(String budgetNatureId) throws Exception {
        if (budgetNatureId == null || budgetNatureId.isEmpty()) {
            return null;
        }
        String budgetNatureJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_NATURE_TABLE, budgetNatureId);
        if (budgetNatureJson == null || budgetNatureJson.isEmpty()) {
            return null;
        }
        List<BudgetNature> budgetNatureList = new Gson().fromJson(budgetNatureJson, new TypeToken<List<BudgetNature>>() {
        }.getType());
        if (budgetNatureList == null || budgetNatureList.isEmpty()) {
            return null;
        }
        return budgetNatureList.get(0);

    }

    public boolean deleteBudgetNature(String budgetNatureId, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        BudgetNature budgetNature = fetch(budgetNatureId);
        budgetNature.setStatus(ApplicationConstants.DELETE);
        budgetNature.setUpdatedBy(userName);

        String budgetNatureJson = new Gson().toJson(budgetNature);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_NATURE_TABLE, budgetNatureId, budgetNatureJson);
        if (status) {
            return true;
        }
        return false;
    }

}
