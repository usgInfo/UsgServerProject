/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.budget.dto.FundType;
import com.accure.budget.dto.Sector;
import com.accure.finance.dto.FinancialFundHeadMapping;
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
public class FinancialFundHeadMappingManager {

    public String save(FinancialFundHeadMapping obj) throws Exception {
        obj.setCreateDate(System.currentTimeMillis() + "");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objJson = new Gson().toJson(obj);
        String objId = DBManager.getDbConnection().insert(ApplicationConstants.FINANCIAL_FUND_HEAD_MAPPING_TABLE, objJson);
        return objId;
    }

    public String fetch(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_FUND_HEAD_MAPPING_TABLE, objId);
        List<FinancialFundHeadMapping> objList = new Gson().fromJson(result, new TypeToken<List<FinancialFundHeadMapping>>() {
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
        Type type = new TypeToken<FinancialFundHeadMapping>() {
        }.getType();
        String obj = new FinancialFundHeadMappingManager().fetch(objId);
        if (obj == null || obj.isEmpty()) {
            return false;
        }
        FinancialFundHeadMapping objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_FUND_HEAD_MAPPING_TABLE, objId, new Gson().toJson(objrJson));
        return result;
    }

    public boolean update(FinancialFundHeadMapping obj, String objId) throws Exception {
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objrJson = new Gson().toJson(obj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_FUND_HEAD_MAPPING_TABLE, objId, objrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_FUND_HEAD_MAPPING_TABLE, conditionMap);
        List<FinancialFundHeadMapping> ffhmList = new Gson().fromJson(result1, new TypeToken<List<FinancialFundHeadMapping>>() {
        }.getType());
        try {
            ffhmList = new FinancialFundHeadMappingManager().getFundHead(ffhmList);

        } catch (Exception e) {
        }
        try {
            ffhmList = new FinancialFundHeadMappingManager().getSector(ffhmList);
        } catch (Exception e) {
        }
        return new Gson().toJson(ffhmList);
    }

    private List<FinancialFundHeadMapping> getFundHead(List<FinancialFundHeadMapping> ffhmList) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < ffhmList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(ffhmList.get(i).getFundType())) {
                    ffhmList.get(i).setFundType(entry.getValue());
                }
            }
        }
        return ffhmList;
    }

    private List<FinancialFundHeadMapping> getSector(List<FinancialFundHeadMapping> ffhmList) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SECTOR_TABLE);
        List<Sector> religionList = new Gson().fromJson(result, new TypeToken<List<Sector>>() {
        }.getType());
        for (Iterator<Sector> iterator = religionList.iterator(); iterator.hasNext();) {
            Sector next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getSectorName());
        }
        for (int i = 0; i < ffhmList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(ffhmList.get(i).getSector())) {
                    ffhmList.get(i).setSector(entry.getValue());
                }
            }
        }
        return ffhmList;
    }
}
