/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.FundHeadMapping;
import com.accure.budget.dto.FundType;
import com.accure.budget.dto.Sector;

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
public class FundHeadMappingManager {

    public String save(String deptlist) throws Exception {
        Type type = new TypeToken<FundHeadMapping>() {
        }.getType();
        FundHeadMapping ddoDTO = new Gson().fromJson(deptlist, type);
        ddoDTO.setCreateDate(System.currentTimeMillis() + "");
        ddoDTO.setUpdateDate(System.currentTimeMillis() + "");
        ddoDTO.setStatus(ApplicationConstants.ACTIVE);
        String ddoJson = new Gson().toJson(ddoDTO);
        String ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_FUND_HEAD_MAPPING_TABLE, ddoJson);
        return ddoresult;

    }

    public String viewFHMList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FUND_HEAD_MAPPING_TABLE, conditionMap);
        List<FundHeadMapping> empList = new Gson().fromJson(result, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        for (FundHeadMapping cl : empList) {
            try {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, cl.getFundType());
                List<FundType> gaList = new Gson().fromJson(gaJson, new TypeToken<List<FundType>>() {
                }.getType());
                FundType gal = gaList.get(0);
                cl.setFundType(gal.getDescription());
            } catch (Exception e) {
            }
            try {
                String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.SECTOR_TABLE, cl.getSector());
                List<Sector> gaList1;
                gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<Sector>>() {
                }.getType());
                Sector gal1 = gaList1.get(0);
                cl.setSector(gal1.getSectorName());
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList);

        // return result;
    }

    public boolean deleteFHM(String rid) throws Exception {
        String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FUND_HEAD_MAPPING_TABLE, rid);
        List<FundHeadMapping> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        FundHeadMapping category = categorylist.get(0);
        FundHeadMapping categoryobje = new FundHeadMapping();
        categoryobje.setFundType(category.getFundType());
        categoryobje.setSector(category.getSector());
        categoryobje.setHeadCode(category.getHeadCode());
        categoryobje.setCreateDate(category.getCreateDate());
        categoryobje.setStatus(ApplicationConstants.DELETE);
        categoryobje.setUpdateDate(System.currentTimeMillis() + "");
        String categoryJson = new Gson().toJson(categoryobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FUND_HEAD_MAPPING_TABLE, rid, categoryJson);
        return status;
    }

    public boolean update(FundHeadMapping relation1, String bankId) throws Exception {

        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FUND_HEAD_MAPPING_TABLE, bankId);
        List<FundHeadMapping> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        FundHeadMapping relation = relationlist.get(0);
        relation1.setCreateDate(relation.getCreateDate());
        relation1.setUpdateDate(System.currentTimeMillis() + "");
        relation1.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(relation1);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FUND_HEAD_MAPPING_TABLE, bankId, bankJson);

        return result;
    }

    public static void main(String[] args) throws Exception {
        String result = new FundHeadMappingManager().viewFHMList();
        //System.out.println(result);
    }

}
