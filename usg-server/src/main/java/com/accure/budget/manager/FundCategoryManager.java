/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.FundCategory;
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
public class FundCategoryManager {

    public String save(FundCategory object) throws Exception {
        object.setStatus(ApplicationConstants.ACTIVE);
        object.setCreateDate(System.currentTimeMillis() + "");
        object.setUpdateDate(System.currentTimeMillis() + "");
        String json = new Gson().toJson(object);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_FUND_CATEGORY_TABLE, json);
        return id;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FUND_CATEGORY_TABLE, conditionMap);
        return result;
    }

    public boolean update(FundCategory object, String id) throws Exception {
        object.setUpdateDate(System.currentTimeMillis() + "");
        object.setStatus(ApplicationConstants.ACTIVE);
        String json = new Gson().toJson(object);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FUND_CATEGORY_TABLE, id, json);
        return result;
    }

    public boolean delete(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<FundCategory>() {
        }.getType();
        String bank = new FundCategoryManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        FundCategory bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FUND_CATEGORY_TABLE, id, new Gson().toJson(bankJson));
        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FUND_CATEGORY_TABLE, Id);
        List<FundCategory> gisList = new Gson().fromJson(result, new TypeToken<List<FundCategory>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }
}
