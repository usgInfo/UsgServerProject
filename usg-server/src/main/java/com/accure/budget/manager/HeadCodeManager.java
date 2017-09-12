/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetGovtHead;
import com.accure.budget.dto.FundType;
import com.accure.budget.dto.HeadCode;

import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class HeadCodeManager {

    public String save(HeadCode headCode) throws Exception {
        Type type = new TypeToken<HeadCode>() {
        }.getType();
        HeadCode assObj = new HeadCode();
        headCode.setStatus(ApplicationConstants.ACTIVE);
        headCode.setCreateDate(System.currentTimeMillis() + "");
        headCode.setUpdateDate(System.currentTimeMillis() + "");
        String GISjson = new Gson().toJson(headCode);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.HEAD_CODE_TABLE, GISjson);
        return id;

    }

    public boolean update(HeadCode headCodeType, String id) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.HEAD_CODE_TABLE, id);
        List<HeadCode> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<HeadCode>>() {
        }.getType());
        HeadCode relation = relationlist.get(0);
        relation.setCreateDate(relation.getCreateDate());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HEAD_CODE_TABLE, id, bankJson);

        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_CODE_TABLE, conditionMap);
        List<HeadCode> empList = new Gson().fromJson(result, new TypeToken<List<HeadCode>>() {
        }.getType());
        for (HeadCode cl : empList) {
            try {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, cl.getFundType());
                List<FundType> gaList = new Gson().fromJson(gaJson, new TypeToken<List<FundType>>() {
                }.getType());
                FundType gal = gaList.get(0);

                cl.setFundType(gal.getDescription());
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList);
    }

    private List<HeadCode> getGovtBudgetHeadListForBudgetHeadMapping(List<HeadCode> li) throws Exception {

        Map<String, String> getGovtBudgetHeadMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_GOVT_BUDGETHEAD_TABLE);
        List<BudgetGovtHead> gList = new Gson().fromJson(new BudgetGovtHeadManager().viewBudgetGovtHeadList(), new TypeToken<List<BudgetGovtHead>>() {
        }.getType());
        for (Iterator<BudgetGovtHead> iterator = gList.iterator(); iterator.hasNext();) {
            BudgetGovtHead next = iterator.next();
            getGovtBudgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getMajorHead() + "-" + next.getSubMajorHead() + "-" + next.getMinorHead() + "-" + next.getSubMinorHead() + "-" + next.getOrder());
        }
        for (int i = 0; i < li.size(); i++) {
            String gId = li.get(i).getGovtBudgetHead();
            for (Map.Entry<String, String> entry : getGovtBudgetHeadMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(gId)) {
                    //System.out.println(value);
                    li.get(i).setGovtBudgetHead(value);
                }
            }
            //System.out.println(li);
        }
        return li;
    }

    public boolean delete(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<HeadCode>() {
        }.getType();
        String bank = new HeadCodeManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        HeadCode bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HEAD_CODE_TABLE, id, new Gson().toJson(bankJson));

        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.HEAD_CODE_TABLE, Id);
        List<HeadCode> gisList = new Gson().fromJson(result, new TypeToken<List<HeadCode>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public String Search(String head, String fundtype) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("govtBudgetHead", head);
        conditionMap.put("fundType", fundtype);

        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_CODE_TABLE, conditionMap);

//        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BANK_TABLE);
        return result1;

    }

    public List<HeadCode> fetchAllBudget() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_CODE_TABLE, conditionMap);
        //System.out.println(budgetJson);
        List<HeadCode> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<HeadCode>>() {
        }.getType());
        return ledgerList;

    }

    public static void main(String[] args) throws Exception {
        List<HeadCode> out = new HeadCodeManager().fetchAllBudget();
        List<HeadCode> out1 = new HeadCodeManager().getGovtBudgetHeadListForBudgetHeadMapping(out);
        String res = new HeadCodeManager().fetchAll();
        //System.out.println(res);

    }
}
