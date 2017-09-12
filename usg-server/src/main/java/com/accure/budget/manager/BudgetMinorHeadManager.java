/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetMinorHead;
import com.accure.finance.dto.MinorHead;
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
public class BudgetMinorHeadManager {

    public String saveMinorHead(String major, String code) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, conditionMap);
        if (result != null) {
            List<BudgetMinorHead> minorlist = new Gson().fromJson(result, new TypeToken<List<BudgetMinorHead>>() {
            }.getType());
            for (int i = 0; i < minorlist.size(); i++) {
                if (minorlist.get(i).getMinorHead() == major || minorlist.get(i).getMinorHead().equals(major)) {
                    return "existed";
                }
            }
        }

        BudgetMinorHead minorobj = new BudgetMinorHead();
        minorobj.setMinorHead(major);
        minorobj.setMinorHeadCode(code);
        minorobj.setStatus(ApplicationConstants.ACTIVE);
        minorobj.setCreateDate(System.currentTimeMillis() + "");
        minorobj.setUpdateDate(System.currentTimeMillis() + "");
        String minorjson = new Gson().toJson(minorobj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, minorjson);
        return id;
    }

    public String viewMinorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, conditionMap);
        return result;

    }

    public boolean updateMinorHead(String minor, String code, String rid) throws Exception {
        String relationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, rid);
        List<BudgetMinorHead> relationlist = new Gson().fromJson(relationJson, new TypeToken<List<BudgetMinorHead>>() {
        }.getType());
        BudgetMinorHead relation = relationlist.get(0);
        BudgetMinorHead minorobj = new BudgetMinorHead();
        minorobj.setMinorHead(minor);
        minorobj.setMinorHeadCode(code);
        minorobj.setCreateDate(relation.getCreateDate());
        minorobj.setStatus(ApplicationConstants.ACTIVE);
        minorobj.setUpdateDate(System.currentTimeMillis() + "");
        String relationjson = new Gson().toJson(minorobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, rid, relationjson);
        return status;
    }

    public boolean deleteMinorHead(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, rid);
        List<BudgetMinorHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<BudgetMinorHead>>() {
        }.getType());
        BudgetMinorHead relation = relationlist.get(0);
        BudgetMinorHead relationobje = new BudgetMinorHead();
        relationobje.setMinorHead(relation.getMinorHead());
        relationobje.setMinorHeadCode(relation.getMinorHeadCode());
        relationobje.setCreateDate(relation.getCreateDate());
        relationobje.setStatus(ApplicationConstants.DELETE);
        relationobje.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relationobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_MINOR_HEAD_TABLE, rid, relationJson);
        return status;
    }

}
