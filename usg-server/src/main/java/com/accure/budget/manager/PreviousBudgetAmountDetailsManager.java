/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.PreviousBudgetAmountDetails;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class PreviousBudgetAmountDetailsManager {

    public String getAllPreviousYears(String fromDate, String toDate) throws Exception {

        String FYTable = ApplicationConstants.USG_DB1 + ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE + "`";
        String FYQuery = "select emp._id as idStr,emp.fromDate,emp.toDate from " + FYTable + ""
                + " as emp where emp.fromDate < \"" + fromDate + "\"   and emp.status=\"Active\"";
        RestClient aql = new RestClient();
        String FYOutput = aql.getRestData(ApplicationConstants.END_POINT, FYQuery);

        return FYOutput;
    }

    public String save(PreviousBudgetAmountDetails extraProvsion, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        extraProvsion.setCreateDate(System.currentTimeMillis() + "");
        extraProvsion.setUpdateDate(System.currentTimeMillis() + "");
        extraProvsion.setStatus(ApplicationConstants.ACTIVE);
        extraProvsion.setCreatedBy(userName);
        String extraProvsionJson = new Gson().toJson(extraProvsion);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, extraProvsionJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String View(String fundType, String budgetHead, String budgetType, String ddo, String location, String financialYear, String sector, String department, String type) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("ddo", ddo);
        map.put("location", location);
        map.put("financialYear", financialYear);
        map.put("fundType", fundType);
        if (!"".equalsIgnoreCase(budgetHead) && budgetHead != null) {
            map.put("budgetHead", budgetHead);
        }

        map.put("sector", sector);
        map.put("department", department);
        map.put("budgetNature", type);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map);

        if (result == null) {
            return null;
        }

        List<PreviousBudgetAmountDetails> list = new Gson().fromJson(result, new TypeToken<List<PreviousBudgetAmountDetails>>() {
        }.getType());

//        list = getDDO(list);
//        list = getBudgetHead(list);
        return new Gson().toJson(list);
    }

    private List<PreviousBudgetAmountDetails> getDDO(List<PreviousBudgetAmountDetails> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getDdo())) {
                    list.get(i).setDdo(entry.getValue());
                }
            }
        }
        return list;
    }

    private List<PreviousBudgetAmountDetails> getBudgetHead(List<PreviousBudgetAmountDetails> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetHead())) {
                    list.get(i).setBudgetHead(entry.getValue());
                }
            }
        }
        return list;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, Id);
        List<PreviousBudgetAmountDetails> gisList = new Gson().fromJson(result, new TypeToken<List<PreviousBudgetAmountDetails>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public boolean update(PreviousBudgetAmountDetails extraIncomeUpdate, String id, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        extraIncomeUpdate.setUpdateDate(System.currentTimeMillis() + "");
        extraIncomeUpdate.setStatus(ApplicationConstants.ACTIVE);
        extraIncomeUpdate.setUpdatedBy(userName);
        String SanctionIncomeBudgetrJson = new Gson().toJson(extraIncomeUpdate);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id, SanctionIncomeBudgetrJson);

        return result;
    }

    public String Delete(String id, String userId) throws Exception {
        String result;
        if (id == null || userId == null) {
            return null;
        }

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id);

        if (dbStr == null) {
            return null;
        }

        List<PreviousBudgetAmountDetails> list = new Gson().fromJson(dbStr, new TypeToken<List<PreviousBudgetAmountDetails>>() {
        }.getType());

        PreviousBudgetAmountDetails obj = list.get(0);
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        obj.setStatus(ApplicationConstants.DELETE);
        obj.setDeletedBy(userName);
        obj.setUpdateDate(System.currentTimeMillis() + "");

        String json = new Gson().toJson(obj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id, json);

        if (status) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

    public String getLedgersBasedOnHeads(String fundType, String budgetHead, String budgetType, String ddo, String location, String financialYear, String sector, String department, String type) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("ddo", ddo);
        map.put("location", location);
        map.put("financialYear", financialYear);
        map.put("fundType", fundType);
        map.put("sector", sector);
        map.put("budgetNature", type);
//            map.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
        map.put("department", department);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String prviousActualbudget = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map);
        if (prviousActualbudget == null) {
            conditionMap.put("FundType", fundType);
            conditionMap.put("budgetType", "Yes");
            if (budgetHead != "" && budgetHead != null) {
                conditionMap.put("budgetHeadCode", budgetHead);
            }
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
            List<Ledger> ddoList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
            }.getType());
            List<Ledger> headlist = new ArrayList<Ledger>();
            if (ddoList != null) {
                conditionMap.clear();
                for (Ledger ledger : ddoList) {

                    String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, ledger.getBudgetHeadCode());
                    if (result1 != null) {
                        List<BudgetHeadMaster> gisList = new Gson().fromJson(result1, new TypeToken<List<BudgetHeadMaster>>() {
                        }.getType());
                        ledger.setBudgetHeadName(gisList.get(0).getBudgetHead());
                    }
                    HashMap<String, String> conditionMap1 = new HashMap<String, String>();
                    conditionMap1.put("parentLedger", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                    conditionMap1.put("ledgerCategory", type);
                    conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    String budgetJson11 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CATEGORY_TABLE, conditionMap1);
                    if (budgetJson11 != null) {
                        headlist.add(ledger);
                    }
                }
                if (headlist.size() == 0) {
                    return new Gson().toJson(ApplicationConstants.NO_DATA_FOUND);
                } else {
                    return new Gson().toJson(headlist);
                }
            }
        } else {
            return new Gson().toJson(ApplicationConstants.DATA_EXISTED);
        }
        return new Gson().toJson(ApplicationConstants.NO_DATA_FOUND);
    }
}
