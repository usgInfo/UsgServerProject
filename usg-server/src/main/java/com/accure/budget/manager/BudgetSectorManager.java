/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class BudgetSectorManager {

    public String save(BudgetSector budgetSector, String userId) throws Exception {

        if (userId == null) {
            return null;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + "" + user.getLname();
        budgetSector.setStatus(ApplicationConstants.ACTIVE);
        budgetSector.setCreateDate(System.currentTimeMillis() + "");
        budgetSector.setUpdateDate(System.currentTimeMillis() + "");
        budgetSector.setCreatedBy(userName);
        budgetSector.setUpdatedBy(userName);
        String sectorJson = new Gson().toJson(budgetSector);
        String result = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_SECTOR_TABLE, sectorJson);
        return result;

    }

    public HashMap<String, String> fetchAllUnderSector() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String sectorJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_SECTOR_TABLE, conditionMap);
        List<BudgetSector> sectorList = new Gson().fromJson(sectorJson, new TypeToken<List<BudgetSector>>() {
        }.getType());
        conditionMap.clear();
        for (BudgetSector underSector : sectorList) {
            conditionMap.put(((Map<String, String>) underSector.getId()).get("$oid"), underSector.getSectorCode());
        }
        return conditionMap;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_SECTOR_TABLE, conditionMap);

        return result;

    }

    public List<BudgetSector> fetchAllSector() throws Exception {
        HashMap<String, String> parent = fetchAllUnderSector();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetSectorJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_SECTOR_TABLE, conditionMap);
        List<BudgetSector> budgetSectorList = new Gson().fromJson(budgetSectorJson, new TypeToken<List<BudgetSector>>() {
        }.getType());
        List<BudgetSector> returnList = new ArrayList<BudgetSector>();
        if (budgetSectorList != null) {
            for (BudgetSector sector : budgetSectorList) {
                if (sector.getUnderSector() != null) {
                    String id = sector.getUnderSector();
                    if (parent.containsKey(id) && parent.get(id) != null) {
                        sector.setUnderSectorName(parent.get(id));
                    }
                }
                returnList.add(sector);
            }
        }
        return returnList;
    }

    public boolean update(BudgetSector budgetSector, String id, String userId) throws Exception {

        if (userId == null || id == null) {
            return false;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + "" + user.getLname();
        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SECTOR_TABLE, id);

        if (dbStr == null) {
            return false;
        }

        List<BudgetSector> list = new Gson().fromJson(dbStr, new TypeToken<List<BudgetSector>>() {
        }.getType());

        BudgetSector dbObj = list.get(0);

        budgetSector.setUpdateDate(System.currentTimeMillis() + "");
        budgetSector.setCreateDate(dbObj.getCreateDate());
        budgetSector.setCreatedBy(dbObj.getCreatedBy());
        budgetSector.setStatus(ApplicationConstants.ACTIVE);
        budgetSector.setUpdatedBy(userName);
        String budgetSectorJson = new Gson().toJson(budgetSector);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_SECTOR_TABLE, id, budgetSectorJson);

        return result;
    }

    public boolean delete(String id, String userId) throws Exception {

        if (id == null || id.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + "" + user.getLname();
        Type type = new TypeToken<BudgetSector>() {
        }.getType();
        String dbStr = new BudgetSectorManager().fetch(id);
        if (dbStr == null || dbStr.isEmpty()) {
            return false;
        }
        BudgetSector sector = new Gson().fromJson(dbStr, type);
        sector.setStatus(ApplicationConstants.DELETE);
        sector.setUpdateDate(System.currentTimeMillis() + "");
        sector.setDeletedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_SECTOR_TABLE, id, new Gson().toJson(sector));

        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SECTOR_TABLE, Id);
        List<BudgetSector> list = new Gson().fromJson(result, new TypeToken<List<BudgetSector>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }
}
