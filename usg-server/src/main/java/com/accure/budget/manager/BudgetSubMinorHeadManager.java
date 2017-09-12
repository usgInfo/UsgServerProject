/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSubMinorHead;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Upendra
 */
public class BudgetSubMinorHeadManager {

    public String saveSubMinorHead(String name, String code) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, conditionMap);
        if (result != null) {
            List<BudgetSubMinorHead> subminorlist = new Gson().fromJson(result, new TypeToken<List<BudgetSubMinorHead>>() {
            }.getType());
            for (int i = 0; i < subminorlist.size(); i++) {
                if (subminorlist.get(i).getSubMinorHead() == name || subminorlist.get(i).getSubMinorHead().equals(name)) {
                    return "existed";
                }
            }
        }

        BudgetSubMinorHead subminorobj = new BudgetSubMinorHead();
        subminorobj.setSubMinorHead(name);
        subminorobj.setSubMinorHeadCode(code);
        subminorobj.setStatus(ApplicationConstants.ACTIVE);
        subminorobj.setCreateDate(System.currentTimeMillis() + "");
        subminorobj.setUpdateDate(System.currentTimeMillis() + "");
        String subminorjson = new Gson().toJson(subminorobj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, subminorjson);
        return id;
    }

    public String viewSubMinorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, conditionMap);
        return result;

    }

    public boolean updateSubMinorHead(String name, String code, String rid) throws Exception {
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, rid);
        List<BudgetSubMinorHead> resultlist = new Gson().fromJson(result, new TypeToken<List<BudgetSubMinorHead>>() {
        }.getType());
        BudgetSubMinorHead subminor = resultlist.get(0);
        BudgetSubMinorHead subminorobj = new BudgetSubMinorHead();
        subminorobj.setSubMinorHead(name);
        subminorobj.setSubMinorHeadCode(code);
        subminorobj.setCreateDate(subminor.getCreateDate());
        subminorobj.setStatus(ApplicationConstants.ACTIVE);
        subminorobj.setUpdateDate(System.currentTimeMillis() + "");
        String subminorjson = new Gson().toJson(subminorobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, rid, subminorjson);
        return status;
    }

    public boolean deleteSubMinorHead(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, rid);
        List<BudgetSubMinorHead> subminorlist = new Gson().fromJson(existrelationJson, new TypeToken<List<BudgetSubMinorHead>>() {
        }.getType());
        BudgetSubMinorHead subminor = subminorlist.get(0);
        BudgetSubMinorHead subminorobje = new BudgetSubMinorHead();
        subminorobje.setSubMinorHead(subminor.getSubMinorHead());
        subminorobje.setSubMinorHeadCode(subminor.getSubMinorHeadCode());
        subminorobje.setCreateDate(subminor.getCreateDate());
        subminorobje.setStatus(ApplicationConstants.DELETE);
        subminorobje.setUpdateDate(System.currentTimeMillis() + "");
        String subminorJson = new Gson().toJson(subminorobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_SUB_MINOR_HEAD_TABLE, rid, subminorJson);
        return status;
    }

}
