/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.db.in.DAO;
import com.accure.finance.dto.GovernmentLedgerCode;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerCodeMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class LedgerCodeManager {

    public String createLedgerCode(LedgerCodeMaster ledgerCode, String UserId) throws Exception {
        String result = "";

        if (ledgerCode.getLedgerCode() == null || ledgerCode.getDescription() == null || UserId == null) {
            result = null;

        } else if (!ledgerCode.getLedgerCode().equalsIgnoreCase(null) && !ledgerCode.getDescription().equalsIgnoreCase(null)) {

            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("ledgerCode", ledgerCode.getLedgerCode());
            map1.put("description", ledgerCode.getDescription());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.LEDGER_CODE_TABLE, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.LEDGER_CODE_TABLE, map1)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User UserDB = fetch(UserId);
                String name = UserDB.getFname() + " " + UserDB.getLname();

                ledgerCode.setCreateDate(System.currentTimeMillis() + "");
                ledgerCode.setUpdateDate(System.currentTimeMillis() + "");
                ledgerCode.setStatus(ApplicationConstants.ACTIVE);
                ledgerCode.setCreatedBy(name);

                Type type = new TypeToken<LedgerCodeMaster>() {
                }.getType();
                String ledgerCodeJson = new Gson().toJson(ledgerCode, type);

                String ledgerCodeId = DBManager.getDbConnection().insert(ApplicationConstants.LEDGER_CODE_TABLE, ledgerCodeJson);

                if (ledgerCodeId != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }

        }
        return result;

    }

    public User fetch(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        String userJson = DBManager.getDbConnection().fetch(ApplicationConstants.USER_TABLE, userId);
        if (userJson == null || userJson.isEmpty()) {
            return null;
        }
        List<User> userList = new Gson().fromJson(userJson, new TypeToken<List<User>>() {
        }.getType());
        if (userList == null || userList.isEmpty()) {
            return null;
        }
        return userList.get(0);

    }

    public HashMap<String, String> fetchallGovernmentLedgerCode() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String governmentLedgerCodeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, conditionMap);
        List<GovernmentLedgerCode> governmentLedgerCodeList = new Gson().fromJson(governmentLedgerCodeJson, new TypeToken<List<GovernmentLedgerCode>>() {
        }.getType());
        conditionMap.clear();
        for (GovernmentLedgerCode governmentLedgerCode : governmentLedgerCodeList) {
            conditionMap.put(((Map<String, String>) governmentLedgerCode.getId()).get("$oid"), governmentLedgerCode.getGovernmentLedgerCode());
        }
        return conditionMap;
    }

    public List<LedgerCodeMaster> fetchAllLedgerCode() throws Exception {
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetNatureJson = dao.fetchAllRowsByConditions(ApplicationConstants.LEDGER_CODE_TABLE, conditionMap);

        List<LedgerCodeMaster> ledgerList = new Gson().fromJson(budgetNatureJson, new TypeToken<List<LedgerCodeMaster>>() {
        }.getType());

        ledgerList = getLedgerName(ledgerList);
        return ledgerList;
    }

     public List<Map<String, String>> fetchAll() throws Exception {
        List<Map<String, String>> outlist = new ArrayList<Map<String, String>>();

        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetNatureJson = dao.fetchAllRowsByConditions(ApplicationConstants.LEDGER_CODE_TABLE, conditionMap);

        List<LedgerCodeMaster> ledgerList = new Gson().fromJson(budgetNatureJson, new TypeToken<List<LedgerCodeMaster>>() {
        }.getType());
        for (LedgerCodeMaster li : ledgerList) {
            String ledgerName="";
            if ((li.getGovernmentLedgerCode() != null)) {
                    String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, li.getGovernmentLedgerCode());

                    if (jsonStr != null) {

                        List<Ledger> list = new Gson().fromJson(jsonStr, new TypeToken<List<Ledger>>() {
                        }.getType());

                        Ledger ddoObj = list.get(0);
                        ledgerName=ddoObj.getLedgerName();
                       
                    }
                }

            HashMap<String, String> map = new HashMap<String, String>();

            map.put("idStr", li.getGovernmentLedgerCode());
            map.put("ledgerCode", li.getLedgerCode());
            map.put("ledgerName", ledgerName);

            outlist.add(map);

        }

        return outlist;
    }
     
     public List<Map<String, String>> fetchAllLedgerForBudget() throws Exception {
        List<Map<String, String>> outlist = new ArrayList<Map<String, String>>();

        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.BUDGET_HEAD, ApplicationConstants.YES);
        String budgetNatureJson = dao.fetchAllRowsByConditions(ApplicationConstants.LEDGER_CODE_TABLE, conditionMap);

        List<LedgerCodeMaster> ledgerList = new Gson().fromJson(budgetNatureJson, new TypeToken<List<LedgerCodeMaster>>() {
        }.getType());
        for (LedgerCodeMaster li : ledgerList) {
            String ledgerName="";
            if ((li.getGovernmentLedgerCode() != null)) {
                    String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, li.getGovernmentLedgerCode());

                    if (jsonStr != null) {

                        List<Ledger> list = new Gson().fromJson(jsonStr, new TypeToken<List<Ledger>>() {
                        }.getType());

                        Ledger ddoObj = list.get(0);
                        ledgerName=ddoObj.getLedgerName();
                       
                    }
                }

            HashMap<String, String> map = new HashMap<String, String>();

            map.put("idStr", li.getGovernmentLedgerCode());
            map.put("ledgerCode", li.getLedgerCode());
            map.put("ledgerName", ledgerName);

            outlist.add(map);

        }

        return outlist;
    }


   
    public static List<LedgerCodeMaster> getLedgerName(List<LedgerCodeMaster> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEDGER_TABLE);
        List<Ledger> resultList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
        }.getType());
        for (Iterator<Ledger> iterator = resultList.iterator(); iterator.hasNext();) {
            Ledger next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLedgerName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getGovernmentLedgerCode())) {
                    list.get(i).setGovernmentLedgerCode(entry.getValue());
                }
            }
        }
        return list;
    }

    public String updateLedgerCode(LedgerCodeMaster ledgerCode, String userId, String ledgerCodeId) throws Exception {
        String result = "";
        if (ledgerCode.getLedgerCode() == null || ledgerCode.getDescription() == null || userId == null) {
            result = null;
        } else if (!ledgerCode.getLedgerCode().equalsIgnoreCase(null) && !ledgerCode.getDescription().equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("ledgerCode", ledgerCode.getLedgerCode());
            map1.put("description", ledgerCode.getDescription());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.LEDGER_CODE_TABLE, map, ledgerCodeId) || Duplicate.isDuplicateforUpdate(ApplicationConstants.LEDGER_TABLE, map1, ledgerCodeId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User UserDB = fetch(userId);
                String name = UserDB.getFname() + " " + UserDB.getLname();

                LedgerCodeMaster ledgerCodeDB = fetchLedgerCode(ledgerCodeId);

                ledgerCodeDB.setGovernmentLedgerCode(ledgerCode.getGovernmentLedgerCode());
                ledgerCodeDB.setLedgerCode(ledgerCode.getLedgerCode());
                ledgerCodeDB.setDescription(ledgerCode.getDescription());
                ledgerCodeDB.setBudgetHead(ledgerCode.getBudgetHead());
                ledgerCodeDB.setOrderLevel(ledgerCode.getOrderLevel());

                ledgerCodeDB.setUpdateDate(System.currentTimeMillis() + "");
                ledgerCodeDB.setUpdatedBy(name);
                ledgerCodeDB.setStatus(ApplicationConstants.ACTIVE);

                String ledgerCategoryJson = new Gson().toJson(ledgerCodeDB);

                boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEDGER_CODE_TABLE, ledgerCodeId, ledgerCategoryJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public LedgerCodeMaster fetchLedgerCode(String ledgerCodeId) throws Exception {
        if (ledgerCodeId == null || ledgerCodeId.isEmpty()) {
            return null;
        }
        String ledgerCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_CODE_TABLE, ledgerCodeId);
        if (ledgerCategoryJson == null || ledgerCategoryJson.isEmpty()) {
            return null;
        }
        List<LedgerCodeMaster> ledgerCodeList = new Gson().fromJson(ledgerCategoryJson, new TypeToken<List<LedgerCodeMaster>>() {
        }.getType());
        if (ledgerCodeList == null || ledgerCodeList.isEmpty()) {
            return null;
        }
        return ledgerCodeList.get(0);

    }

    public boolean deleteLedgerCode(String codeLedgerId, String currentUserLogin) throws Exception {
        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        LedgerCodeMaster ledgerCode = fetchLedgerCode(codeLedgerId);
        ledgerCode.setStatus(ApplicationConstants.DELETE);
        ledgerCode.setUpdatedBy(userName);

        String ledgerCodeJson = new Gson().toJson(ledgerCode);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEDGER_CODE_TABLE, codeLedgerId, ledgerCodeJson);
        if (status) {
            return true;
        }
        return false;
    }

    public String getLedgerCodeValues() throws Exception {

        String finalResult = "";
        long ledgerCode;
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEDGER_CODE_TABLE);

        if (result == null) {
            ledgerCode = Long.valueOf("1");
        } else {
            List<LedgerCodeMaster> ledgerCodeList = new Gson().fromJson(result, new TypeToken<List<LedgerCodeMaster>>() {
            }.getType());
            LedgerCodeMaster obj = ledgerCodeList.get(ledgerCodeList.size() - 1);
            ledgerCode = Long.parseLong(obj.getLedgerCode()) + 1;
        }

        int length = String.valueOf(ledgerCode).length();

        switch (length) {

            case 1:
                finalResult = "0000" + String.valueOf(ledgerCode);
                break;

            case 2:
                finalResult = "000" + String.valueOf(ledgerCode);
                break;

            case 3:
                finalResult = "00" + String.valueOf(ledgerCode);
                break;

            case 4:
                finalResult = "0" + String.valueOf(ledgerCode);
                break;

            case 5:
                finalResult = String.valueOf(ledgerCode);
                break;

        }

        return finalResult;

    }

    public static void main(String[] args) throws Exception {
        List<Map<String,String>> result = new LedgerCodeManager().fetchAll();
        System.out.println("final result" + result);
    }

}
