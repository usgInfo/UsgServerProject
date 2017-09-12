/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSubMajorHead;
import com.accure.finance.dto.SubMajorHead;
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
public class BudgetSubMajorHeadManager {

    public String saveSubMajorHead(String name, String code) throws Exception {

     
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SUB_MAJORHEAD_TABLE, conditionMap);
        if (result != null) {
            List<BudgetSubMajorHead> submajlist = new Gson().fromJson(result, new TypeToken<List<BudgetSubMajorHead>>() {
            }.getType());
            for (int i = 0; i < submajlist.size(); i++) {
                if (submajlist.get(i).getSubMajorHead() == name || submajlist.get(i).getSubMajorHead().equals(name)) {
                    return "existed";
                }
            }
        }

        BudgetSubMajorHead submajobj = new BudgetSubMajorHead();
        submajobj.setSubMajorHead(name);
        submajobj.setSubMajorHeadCode(code);
        submajobj.setStatus(ApplicationConstants.ACTIVE);
        submajobj.setCreateDate(System.currentTimeMillis() + "");
        submajobj.setUpdateDate(System.currentTimeMillis() + "");
        String submajjson = new Gson().toJson(submajobj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, submajjson);
        return id;
    }

    public String viewSubMajorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, conditionMap);
        return result;

    }

    public boolean updateSubMajorHead(String name, String code, String rid) throws Exception {
        String submajJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, rid);
        List<BudgetSubMajorHead> submajlist = new Gson().fromJson(submajJson, new TypeToken<List<BudgetSubMajorHead>>() {
        }.getType());
        BudgetSubMajorHead submajor = submajlist.get(0);
        BudgetSubMajorHead submajorobj = new BudgetSubMajorHead();
        submajorobj.setSubMajorHead(name);
        submajorobj.setSubMajorHeadCode(code);
        submajorobj.setCreateDate(submajor.getCreateDate());
        submajorobj.setStatus(ApplicationConstants.ACTIVE);
        submajorobj.setUpdateDate(System.currentTimeMillis() + "");
        String submajorjson = new Gson().toJson(submajorobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, rid, submajorjson);
        return status;
    }

    public boolean deleteSubMajorHead(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, rid);
        List<BudgetSubMajorHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<BudgetSubMajorHead>>() {
        }.getType());
        BudgetSubMajorHead relation = relationlist.get(0);
        BudgetSubMajorHead relationobje = new BudgetSubMajorHead();
        relationobje.setSubMajorHead(relation.getSubMajorHead());
        relationobje.setSubMajorHeadCode(relation.getSubMajorHeadCode());
        relationobje.setCreateDate(relation.getCreateDate());
        relationobje.setStatus(ApplicationConstants.DELETE);
        relationobje.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relationobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_SUB_MAJOR_HEAD_TABLE, rid, relationJson);
        return status;
    }

}
