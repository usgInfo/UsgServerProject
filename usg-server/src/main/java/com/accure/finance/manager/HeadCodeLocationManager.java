/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.user.manager.UserManager;
import com.accure.finance.dto.FinancialHeadCodeMapping;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author deepak2310
 */
public class HeadCodeLocationManager {

    public String createHeadCodeMapping(FinancialHeadCodeMapping headCode, String loginUserId) throws Exception {
        String result = "";

        if (headCode.getDdo() == null || headCode.getLocation() == null || headCode.getBudgetHead() == null || loginUserId == null) {
            result = null;

        } else if (!headCode.getBudgetHead().equalsIgnoreCase(null) && !headCode.getLocation().equalsIgnoreCase(null) && !headCode.getDdo().equalsIgnoreCase(null)) {

            HashMap map = new HashMap();

            map.put("ddo", headCode.getDdo());
            map.put("budgetHead", headCode.getBudgetHead());
            map.put("location", headCode.getLocation());

            if (Duplicate.hasDuplicateforSave(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, map)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User user = new UserManager().fetch(loginUserId);
                String userName = user.getFname();

                headCode.setCreateDate(System.currentTimeMillis() + "");
                headCode.setUpdateDate(System.currentTimeMillis() + "");
                headCode.setStatus(ApplicationConstants.ACTIVE);
                headCode.setCreatedBy(userName);

                String headCodeJson = new Gson().toJson(headCode);
                String id = DBManager.getDbConnection().insert(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, headCodeJson);

                if (id != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }

        }
        return result;

    }

    public HashMap<String, String> fetchallLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String locationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_TABLE, conditionMap);
        List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
        }.getType());
        conditionMap.clear();
        for (Location location : locationList) {
            conditionMap.put(((Map<String, String>) location.getId()).get("$oid"), location.getLocationName());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchallDDO() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String DDOJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, conditionMap);
        List<DDO> ddoList = new Gson().fromJson(DDOJson, new TypeToken<List<DDO>>() {
        }.getType());
        conditionMap.clear();
        for (DDO ddo : ddoList) {
            conditionMap.put(((Map<String, String>) ddo.getId()).get("$oid"), ddo.getDdoName());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchALLBudgetHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetHeadJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> budgetHeadList = new Gson().fromJson(budgetHeadJson, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        conditionMap.clear();
        for (BudgetHeadMaster budgetHead : budgetHeadList) {
            conditionMap.put(((Map<String, String>) budgetHead.getId()).get("$oid"), budgetHead.getBudgetHead());
        }
        return conditionMap;
    }

    public List<FinancialHeadCodeMapping> fetchHeadCodeList(HashMap<String, String> filter) throws Exception {

        HashMap<String, String> location = fetchallLocation();
        HashMap<String, String> ddo = fetchallDDO();
        HashMap<String, String> budgetHead = fetchALLBudgetHead();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (filter != null) {
            Common.removeNullAndEmptyValuesInMapAndBuildQuery(filter, null);
            conditionMap.putAll(filter);
        }

        String headCodeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, conditionMap);
        List<FinancialHeadCodeMapping> headCodeList = new Gson().fromJson(headCodeJson, new TypeToken<List<FinancialHeadCodeMapping>>() {
        }.getType());
        List<FinancialHeadCodeMapping> returnList = new ArrayList<FinancialHeadCodeMapping>();
        if (headCodeList != null) {
            for (FinancialHeadCodeMapping headCode : headCodeList) {
                if (headCode.getLocation() != null) {
                    String locationId = headCode.getLocation();
                    if (location.containsKey(locationId) && location.get(locationId) != null) {
                        headCode.setLocationName(location.get(locationId));
                    }
                }
                if (headCode.getDdo() != null) {
                    String ddoId = headCode.getDdo();
                    if (ddo.containsKey(ddoId) && ddo.get(ddoId) != null) {
                        headCode.setDdoName(ddo.get(ddoId));
                    }
                }
                if (headCode.getBudgetHead() != null) {
                    String budgetHeadId = headCode.getBudgetHead();
                    if (budgetHead.containsKey(budgetHeadId) && budgetHead.get(budgetHeadId) != null) {
                        headCode.setBudgetHeadName(budgetHead.get(budgetHeadId));
                    }

                }
                returnList.add(headCode);
            }
        }
        return returnList;

    }

    public FinancialHeadCodeMapping fetch(String headCodeId) throws Exception {
        if (headCodeId == null || headCodeId.isEmpty()) {
            return null;
        }
        String headCodeJson = DBManager.getDbConnection().fetch(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, headCodeId);
        if (headCodeJson == null || headCodeJson.isEmpty()) {
            return null;
        }
        List<FinancialHeadCodeMapping> headCodeList = new Gson().fromJson(headCodeJson, new TypeToken<List<FinancialHeadCodeMapping>>() {
        }.getType());
        if (headCodeList == null || headCodeList.isEmpty()) {
            return null;
        }
        return headCodeList.get(0);

    }

    public String updateHeadCode(FinancialHeadCodeMapping headCode, String loginUserId, String headCodeId) throws Exception {
        String result = "";

        if (headCode.getDdo() == null || headCode.getLocation() == null || headCode.getBudgetHead() == null || loginUserId == null) {
            result = null;

        } else if (!headCode.getBudgetHead().equalsIgnoreCase(null) && !headCode.getLocation().equalsIgnoreCase(null) && !headCode.getDdo().equalsIgnoreCase(null)) {

            HashMap map = new HashMap();

            map.put("ddo", headCode.getDdo());
            map.put("budgetHead", headCode.getBudgetHead());
            map.put("location", headCode.getLocation());

            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, map, headCodeId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User user = new UserManager().fetch(loginUserId);
                String userName = user.getFname();

                FinancialHeadCodeMapping headCodeDB = fetch(headCodeId);
                headCodeDB.setLocation(headCode.getLocation());
                headCodeDB.setDdo(headCode.getDdo());
                headCodeDB.setBudgetHead(headCode.getBudgetHead());
                headCodeDB.setUpdateDate(System.currentTimeMillis() + "");
                headCodeDB.setUpdatedBy(userName);

                String headCodeJson = new Gson().toJson(headCodeDB);

                boolean status = DBManager.getDbConnection().update(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, headCodeId, headCodeJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public boolean deleteHeadCode(String headCodeId, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname();

        FinancialHeadCodeMapping headCodeDB = fetch(headCodeId);
        headCodeDB.setStatus(ApplicationConstants.DELETE);
        headCodeDB.setUpdateDate(System.currentTimeMillis() + "");
        headCodeDB.setUpdatedBy(userName);

        String headCodeJson = new Gson().toJson(headCodeDB);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.HEAD_CODE_LOCATION_MAPPING_TABLE, headCodeId, headCodeJson);
        return status;

    }

}
