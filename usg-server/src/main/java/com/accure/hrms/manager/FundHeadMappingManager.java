/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.budget.dto.FundType;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.FundHeadMapping;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author upendra
 */
public class FundHeadMappingManager {

    public String save(FundHeadMapping ddoDTO, String userid) throws Exception {
        String ddoresult;
        User user = new UserManager().fetch(userid);
        String fName = user.getFname();
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("fundType", ddoDTO.getFundType());
        saveconditionMap.put("sector", ddoDTO.getSector());
        saveconditionMap.put("headCode", ddoDTO.getHeadCode());
        saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String saveresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, saveconditionMap);

        if (saveresult != null) {
            ddoresult = "existed";
        } else {
            ddoDTO.setCreateDate(System.currentTimeMillis() + "");
            ddoDTO.setUpdateDate(System.currentTimeMillis() + "");
            ddoDTO.setStatus(ApplicationConstants.ACTIVE);
            ddoDTO.setCreatedBy(fName);

            String ddoJson = new Gson().toJson(ddoDTO);

            ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, ddoJson);
        }

        return ddoresult;

    }

    public String viewFHMList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, conditionMap);

        List<FundHeadMapping> fhmList = new Gson().fromJson(result, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        for (FundHeadMapping li : fhmList) {
            try {
                String ftStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, li.getFundType());
                List<FundType> ftList = new Gson().fromJson(ftStr, new TypeToken<List<FundType>>() {
                }.getType());
                FundType ft = ftList.get(0);
                li.setFundType(ft.getDescription());
            } catch (Exception e) {
            }
            try {
                String bsJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SECTOR_TABLE, li.getSector());
                List<BudgetSector> bsList = new Gson().fromJson(bsJson, new TypeToken<List<BudgetSector>>() {
                }.getType());
                BudgetSector bs = bsList.get(0);
                li.setSector(bs.getDescription());
            } catch (Exception e) {
            }

        }
        return new Gson().toJson(fhmList);

    }

    public boolean deleteFHM(String rid) throws Exception {
        String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, rid);
        List<FundHeadMapping> existlist = new Gson().fromJson(existJson, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        FundHeadMapping existObj = existlist.get(0);
        FundHeadMapping fhm = new FundHeadMapping();
        fhm.setFundType(existObj.getFundType());
        fhm.setSector(existObj.getSector());
        fhm.setHeadCode(existObj.getHeadCode());
        fhm.setCreateDate(existObj.getCreateDate());
        fhm.setStatus(ApplicationConstants.DELETE);
        fhm.setUpdateDate(System.currentTimeMillis() + "");
        String jsonStr = new Gson().toJson(fhm);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, rid, jsonStr);
        return status;
    }

    public String update(FundHeadMapping fundHeadMap, String Id, String userid) throws Exception {

        String fResult;
        User user = new UserManager().fetch(userid);
        String fName = user.getFname();
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("fundType", fundHeadMap.getFundType());
        hMap.put("sector", fundHeadMap.getSector());
        hMap.put("headCode", fundHeadMap.getHeadCode());

        if (isDuplicateforUpdate(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, hMap, Id)) {
            fResult = ApplicationConstants.DUPLICATE;
            return fResult;
        }

        String fundheadJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, Id);

        List<FundHeadMapping> fhmList = new Gson().fromJson(fundheadJson, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        FundHeadMapping fhm = fhmList.get(0);
        fundHeadMap.setCreateDate(fhm.getCreateDate());
        fundHeadMap.setCreatedBy(fhm.getCreatedBy());
        fundHeadMap.setUpdatedBy(fName);
        fundHeadMap.setUpdateDate(System.currentTimeMillis() + "");
        fundHeadMap.setStatus(ApplicationConstants.ACTIVE);
        String jsonStr = new Gson().toJson(fundHeadMap);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, Id, jsonStr);
        if (result) {
            fResult = ApplicationConstants.SUCCESS;
        } else {
            fResult = ApplicationConstants.FAIL;
        }

        return fResult;

    }

    public String searchBudgetHeads(String fundtype) throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("fundType", fundtype);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, hMap);

        List<FundHeadMapping> FundHeadlist = new Gson().fromJson(result, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        List<BudgetHeadMaster> budgetList = new ArrayList<BudgetHeadMaster>();
        Set<String> set = new HashSet<String>();
        for (FundHeadMapping dfm : FundHeadlist) {
            set.add(dfm.getHeadCode());
        }

        for (String dfm : set) {
            try {
                String budgetdata = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, dfm);

                List<BudgetHeadMaster> designa = new Gson().fromJson(budgetdata, new TypeToken<List<BudgetHeadMaster>>() {
                }.getType());
                budgetList.add(designa.get(0));

            } catch (Exception e) {
            }
        }
        HashMap<String, String> resultList = new HashMap<String, String>();
        resultList.put("budgetList", new Gson().toJson(budgetList));
        return new Gson().toJson(resultList);
    }

    public String getBudgetHeads(String fundType, String sector) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, conditionMap);
        List<FundHeadMapping> ddoList = new Gson().fromJson(result, new TypeToken<List<FundHeadMapping>>() {
        }.getType());
        List<BudgetHeadMaster> headlist = new ArrayList<BudgetHeadMaster>();
        for (FundHeadMapping cl : ddoList) {
            String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getHeadCode());
            if (result1 != null) {
                List<BudgetHeadMaster> headCodeList = new Gson().fromJson(result1, new TypeToken<List<BudgetHeadMaster>>() {
                }.getType());
                BudgetHeadMaster gal = headCodeList.get(0);
                if (gal.getIsActive().equalsIgnoreCase(ApplicationConstants.YES)) {
                    headlist.add(gal);
                }
            }
            //  headlist.add(cl.getHeadCode());
        }
        return new Gson().toJson(headlist);
    }
}
