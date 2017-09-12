/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetType;
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
 * @author user
 */
public class BudgetTypeManager {

    public String saveBudgetTypeMaster(BudgetType budgetType, String userId) throws Exception {

        if (userId == null) {
            return null;
        }

        User user = new UserManager().fetch(userId);

        String userName = user.getFname() + " " + user.getLname();

        budgetType.setStatus(ApplicationConstants.ACTIVE);
        budgetType.setCreateDate(System.currentTimeMillis() + "");
        budgetType.setUpdateDate(System.currentTimeMillis() + "");
        budgetType.setCreatedBy(userName);
        budgetType.setUpdatedBy(userName);
        String budgetTypeJson = new Gson().toJson(budgetType);
        String result = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_TYPE_TABLE, budgetTypeJson);
        return result;

    }

    public String fetchAllBudgetType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_TYPE_TABLE, conditionMap);

        return result;

    }

    public boolean updateBudgetType(BudgetType budgetType, String id, String userId) throws Exception {

        if (id == null || id.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, id);

        if (dbStr == null) {
            return false;
        }

        List<BudgetType> list = new Gson().fromJson(dbStr, new TypeToken<List<BudgetType>>() {
        }.getType());
        BudgetType dbObj = list.get(0);

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        budgetType.setUpdateDate(System.currentTimeMillis() + "");
        budgetType.setStatus(ApplicationConstants.ACTIVE);
        budgetType.setCreateDate(dbObj.getCreateDate());
        budgetType.setCreatedBy(dbObj.getCreatedBy());
        budgetType.setUpdatedBy(userName);

        String budgetTypeJson = new Gson().toJson(budgetType);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_TYPE_TABLE, id, budgetTypeJson);

        return result;
    }

    public BudgetType fetchBudgetTypee(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, id);
        if (budgetTypeJson == null || budgetTypeJson.isEmpty()) {
            return null;
        }
        List<BudgetType> budgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
        }.getType());
        if (budgetTypeList == null || budgetTypeList.isEmpty()) {
            return null;
        }
        return budgetTypeList.get(0);

    }

    public boolean deleteBudgetType(String id, String userId) throws Exception {

        if (id == null || id.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<BudgetType>() {
        }.getType();
        String dbStr = new BudgetTypeManager().fetchBudgetType(id);
        if (dbStr == null || dbStr.isEmpty()) {
            return false;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        BudgetType dbObj = new Gson().fromJson(dbStr, type);
        dbObj.setStatus(ApplicationConstants.DELETE);
        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        dbObj.setDeletedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_TYPE_TABLE, id, new Gson().toJson(dbObj));

        return result;
    }

    public String fetchBudgetType(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, Id);
        List<BudgetType> list = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }
}
