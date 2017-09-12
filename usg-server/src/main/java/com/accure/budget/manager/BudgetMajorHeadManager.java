/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetMajorHead;
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
public class BudgetMajorHeadManager {

    public String saveMajorHead(String head, String code) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, conditionMap);
        if (result != null) {
            List<BudgetMajorHead> categolist = new Gson().fromJson(result, new TypeToken<List<BudgetMajorHead>>() {
            }.getType());
            for (int i = 0; i < categolist.size(); i++) {
                if (categolist.get(i).getMajorHead() == head || categolist.get(i).getMajorHead().equals(head)) {
                    return "existed";
                }
            }
        }

        BudgetMajorHead majorobj = new BudgetMajorHead();
        majorobj.setMajorHead(head);
        majorobj.setMajorHeadCode(code);
        majorobj.setStatus(ApplicationConstants.ACTIVE);
        majorobj.setCreateDate(System.currentTimeMillis() + "");
        majorobj.setUpdateDate(System.currentTimeMillis() + "");
        String majorjson = new Gson().toJson(majorobj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, majorjson);
        return id;
    }

    public String viewMajorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, conditionMap);
        return result;

    }

    public boolean updateMajorHead(String major, String remarks, String rid) throws Exception {
        String relationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, rid);
        List<BudgetMajorHead> relationlist = new Gson().fromJson(relationJson, new TypeToken<List<BudgetMajorHead>>() {
        }.getType());
        BudgetMajorHead relation = relationlist.get(0);
        BudgetMajorHead relationobj = new BudgetMajorHead();
        relationobj.setMajorHead(major);
        relationobj.setMajorHeadCode(remarks);
        relationobj.setCreateDate(relation.getCreateDate());
        relationobj.setStatus(ApplicationConstants.ACTIVE);
        relationobj.setUpdateDate(System.currentTimeMillis() + "");
        String relationjson = new Gson().toJson(relationobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, rid, relationjson);
        return status;
    }

    public boolean deleteMajorHead(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, rid);
        List<BudgetMajorHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<BudgetMajorHead>>() {
        }.getType());
        BudgetMajorHead relation = relationlist.get(0);
        BudgetMajorHead relationobje = new BudgetMajorHead();
        relationobje.setMajorHead(relation.getMajorHead());
        relationobje.setMajorHeadCode(relation.getMajorHeadCode());
        relationobje.setCreateDate(relation.getCreateDate());
        relationobje.setStatus(ApplicationConstants.DELETE);
        relationobje.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relationobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_MAJOR_HEAD_TABLE, rid, relationJson);
        return status;
    }

}
