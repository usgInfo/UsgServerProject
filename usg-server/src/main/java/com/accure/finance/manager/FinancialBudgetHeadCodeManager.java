/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.budget.dto.FundType;
import com.accure.finance.dto.FinancialBudgetHeadCode;

import com.accure.finance.dto.GovtBudgetHead;
import com.accure.finance.dto.UnderBudgetHead;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class FinancialBudgetHeadCodeManager {

    public static void main(String[] args) throws Exception {
        FinancialBudgetHeadCode obj = new FinancialBudgetHeadCode();
        obj.setGovernmentBudgetHead("57611367d6bd8d13529a5982");
        //System.out.println(new FinancialBudgetHeadCodeManager().fetchBySearch(obj));
    }

    public String save(FinancialBudgetHeadCode obj) throws Exception {
        obj.setCreateDate(System.currentTimeMillis() + "");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objJson = new Gson().toJson(obj);
        String objId = DBManager.getDbConnection().insert(ApplicationConstants.FINANCIAL_BUDGET_HEAD_CODE_TABLE, objJson);
        return objId;
    }

    public String fetch(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_BUDGET_HEAD_CODE_TABLE, objId);
        List<FinancialBudgetHeadCode> objList = new Gson().fromJson(result, new TypeToken<List<FinancialBudgetHeadCode>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    public boolean delete(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<FinancialBudgetHeadCode>() {
        }.getType();
        String obj = new FinancialBudgetHeadCodeManager().fetch(objId);
        if (obj == null || obj.isEmpty()) {
            return false;
        }
        FinancialBudgetHeadCode objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_BUDGET_HEAD_CODE_TABLE, objId, new Gson().toJson(objrJson));
        return result;
    }

    public boolean update(FinancialBudgetHeadCode obj, String objId) throws Exception {
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objrJson = new Gson().toJson(obj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_BUDGET_HEAD_CODE_TABLE, objId, objrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_BUDGET_HEAD_CODE_TABLE, conditionMap);
        List<FinancialBudgetHeadCode> li = new Gson().fromJson(result1, new TypeToken<List<FinancialBudgetHeadCode>>() {
        }.getType());
        try {

            li = getGovtBudgetHeadListForFBHC(li);
        } catch (Exception e) {
        }
        try {
            li = getFundTypeListForFBHC(li);

        } catch (Exception e) {
        }
        try {
            li = getUnderBudgetHead(li);

        } catch (Exception e) {
        }

        return new Gson().toJson(li);
    }

    public String fetchBySearch(FinancialBudgetHeadCode bhc) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.FINANCIAL_BUDGET_HEAD_CODE_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        if (bhc.getGovernmentBudgetHead() != null) {
            regexQuery.put("governmentBudgetHead",
                    new BasicDBObject("$regex", bhc.getGovernmentBudgetHead()));
        }
        if (bhc.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", bhc.getFundType()));
        }
        if (bhc.getBudgetHead() != null) {
            regexQuery
                    .put("budgetHead",
                            new BasicDBObject("$regex", bhc.getBudgetHead()));
        }
        if (bhc.getBudgetHeadDescription() != null) {
            regexQuery.put("budgetHeadDescription",
                    new BasicDBObject("$regex", bhc.getBudgetHeadDescription()));
        }
        //System.out.println(regexQuery);
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        DBCursor cursor2 = collection.find(regexQuery);
        List<FinancialBudgetHeadCode> bhcList = new ArrayList<FinancialBudgetHeadCode>();
        while (cursor2.hasNext()) {
            //System.out.println("**********");
            String objJson = new Gson().toJson(cursor2.next());
            Type type = new TypeToken<FinancialBudgetHeadCode>() {
            }.getType();
            FinancialBudgetHeadCode em = new Gson().fromJson(objJson, type);
            bhcList.add(em);
        }
        bhcList = getGovtBudgetHeadListForFBHC(bhcList);
        bhcList = getFundTypeListForFBHC(bhcList);
        //System.out.println(bhcList.size());

        return new Gson().toJson(bhcList);
    }

    private List<FinancialBudgetHeadCode> getGovtBudgetHeadListForFBHC(List<FinancialBudgetHeadCode> li) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE);
        List<GovtBudgetHead> gList = new Gson().fromJson(new GovtBudgetHeadManager().viewGovtBudgetHeadList(), new TypeToken<List<GovtBudgetHead>>() {
        }.getType());
        for (Iterator<GovtBudgetHead> iterator = gList.iterator(); iterator.hasNext();) {
            GovtBudgetHead next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getMajorHead() + "-" + next.getSubMajorHead() + "-" + next.getMinorHead() + "-" + next.getSubMinorHead() + "-" + next.getOrder());
        }
        for (int i = 0; i < li.size(); i++) {
            String gId = li.get(i).getGovernmentBudgetHead();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(gId)) {
                    li.get(i).setGovernmentBudgetHead(value);
                }
            }
        }
        return li;
    }

    private List<FinancialBudgetHeadCode> getFundTypeListForFBHC(List<FinancialBudgetHeadCode> li) throws Exception {
        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < li.size(); i++) {
            String Id = li.get(i).getFundType();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(Id)) {
                    li.get(i).setFundType(value);
                }
            }
        }
        return li;
    }

    private List<FinancialBudgetHeadCode> getUnderBudgetHead(List<FinancialBudgetHeadCode> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.UNDER_BUDGET_HEAD_MASTER);
        List<UnderBudgetHead> List = new Gson().fromJson(result, new TypeToken<List<UnderBudgetHead>>() {
        }.getType());
        for (Iterator<UnderBudgetHead> iterator = List.iterator(); iterator.hasNext();) {
            UnderBudgetHead next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getUnderBudgetHead());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {

                if (entry.getKey().equals(HeadSlabList.get(i).getUnderBudgetHead())) {
                    HeadSlabList.get(i).setUnderBudgetHead(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }
}
