/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.UnderBudgetHead;
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
 * @author Asif
 */
public class UnderBudgetHeadManager {
     public String save(UnderBudgetHead obj, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        obj.setCreateDate(System.currentTimeMillis() + "");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        obj.setCreatedBy(userName);
        String objJson = new Gson().toJson(obj);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.UNDER_BUDGET_HEAD_MASTER, objJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.UNDER_BUDGET_HEAD_MASTER, Id);
        List<UnderBudgetHead> objList = new Gson().fromJson(result, new TypeToken<List<UnderBudgetHead>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<UnderBudgetHead>() {
        }.getType();
        String obj = new UnderBudgetHeadManager().fetch(Id);
        if (obj == null || obj.isEmpty()) {
            return false;
        }
        UnderBudgetHead objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.INACTIVE);
        objrJson.setUpdatedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.UNDER_BUDGET_HEAD_MASTER, Id, new Gson().toJson(objrJson));
        return result;
    }

    public boolean update(UnderBudgetHead obj, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        obj.setUpdatedBy(userName);
        String objrJson = new Gson().toJson(obj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.UNDER_BUDGET_HEAD_MASTER, Id, objrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.UNDER_BUDGET_HEAD_MASTER, conditionMap);
        return result1;
    }
}
