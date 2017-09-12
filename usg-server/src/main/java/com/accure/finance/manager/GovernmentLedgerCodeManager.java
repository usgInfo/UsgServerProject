/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.db.in.DAO;
import com.accure.finance.dto.GovernmentLedgerCode;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author accure
 */
public class GovernmentLedgerCodeManager {
    
//    public String createGovernmentLedgerCode(GovernmentLedgerCode governmentLedgerCategory, String UserId) throws Exception {
//
//        User UserDB = fetch(UserId);
//        String name = UserDB.getFname() + " " + UserDB.getLname();
//
//        governmentLedgerCategory.setCreateDate(System.currentTimeMillis() + "");
//        governmentLedgerCategory.setUpdateDate(System.currentTimeMillis() + "");
//        governmentLedgerCategory.setStatus(ApplicationConstants.ACTIVE);
//        governmentLedgerCategory.setCreatedBy(name);
//
//        Type type = new TypeToken<GovernmentLedgerCode>() {
//        }.getType();
//        String governmentLedgerCodeJson = new Gson().toJson(governmentLedgerCategory, type);
//
//        String ledgerId = DBManager.getDbConnection().insert(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, governmentLedgerCodeJson);
//        return ledgerId;
//    }
    
    public String createGovernmentLedgerCode(GovernmentLedgerCode governmentLedgerCode, String UserId) throws Exception {

        User UserDB = new UserManager().fetch(UserId);
        String name = UserDB.getFname() + " " + UserDB.getLname();

        governmentLedgerCode.setCreateDate(System.currentTimeMillis() + "");
        governmentLedgerCode.setUpdateDate(System.currentTimeMillis() + "");
        governmentLedgerCode.setStatus(ApplicationConstants.ACTIVE);
        governmentLedgerCode.setCreatedBy(name);

        Type type = new TypeToken<GovernmentLedgerCode>() {
        }.getType();
        String governmentLedgerCodeJson = new Gson().toJson(governmentLedgerCode, type);

        String governmentLedgerCodeId = DBManager.getDbConnection().insert(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, governmentLedgerCodeJson);
        return governmentLedgerCodeId;
    }
    
    public List<GovernmentLedgerCode> fetchAllGovernmentLedgerCode() throws Exception {
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String governmentLedgerCodeJson = dao.fetchAllRowsByConditions(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, conditionMap);
//        dao.close();
        List<GovernmentLedgerCode> governmentLedgerCodeList = new Gson().fromJson(governmentLedgerCodeJson, new TypeToken<List<GovernmentLedgerCode>>() {
        }.getType());
        return governmentLedgerCodeList;
    }
    
    public boolean updateGovernmentLedgerCode(GovernmentLedgerCode governmentLedgerCode, String userId, String governmentLedgerCodeId) throws Exception {

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        GovernmentLedgerCode governmentLedgerCodeDb = fetch(governmentLedgerCodeId);
        if (governmentLedgerCodeDb.getGovernmentLedgerCode() != null || !governmentLedgerCodeDb.getGovernmentLedgerCode().isEmpty()) {
            governmentLedgerCodeDb.setGovernmentLedgerCode(governmentLedgerCode.getGovernmentLedgerCode());
        }
        if (governmentLedgerCodeDb.getDescription() != null || !governmentLedgerCodeDb.getDescription().isEmpty()) {
            governmentLedgerCodeDb.setDescription(governmentLedgerCode.getDescription());
        }
//        if (governmentLedgerCodeDb.getOrderLevel() != null || !governmentLedgerCodeDb.getOrderLevel().isEmpty()) {
//            governmentLedgerCodeDb.setOrderLevel(governmentLedgerCode.getOrderLevel());
//        }
        if (governmentLedgerCodeDb.getBudgetHead() != null || !governmentLedgerCodeDb.getBudgetHead().isEmpty()) {
            governmentLedgerCodeDb.setBudgetHead(governmentLedgerCode.getBudgetHead());
        }

        //if (governmentLedgerCodeDb.getOrderLevel()!= null || !governmentLedgerCodeDb.getOrderLevel()== null) {
            governmentLedgerCodeDb.setOrderLevel(governmentLedgerCode.getOrderLevel());
        //}
//        if (governmentLedgerCodeDb.getBudgetHead() != false) {
//            governmentLedgerCodeDb.setBudgetHead(governmentLedgerCode.getBudgetHead());
//        }
        governmentLedgerCodeDb.setUpdateDate(System.currentTimeMillis() + "");
        governmentLedgerCodeDb.setUpdatedBy(userName);
        governmentLedgerCodeDb.setStatus(ApplicationConstants.ACTIVE);

        //String governmentLedgerCodeJson = new Gson().toJson(governmentLedgerCodeDb);

        //boolean status = DBManager.getDbConnection().update(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, governmentLedgerCodeId, governmentLedgerCodeJson);
        boolean status = update(governmentLedgerCodeDb, governmentLedgerCodeId);
        if (status) {
            return true;
        }
        return false;
    }
    
    public boolean update(GovernmentLedgerCode governmentLedgerCode, String governmentLedgerCodeId) throws Exception {
        Type type = new TypeToken<GovernmentLedgerCode>() {
        }.getType();
        String governmentLedgerCodeJson = new Gson().toJson(governmentLedgerCode, type);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, governmentLedgerCodeId, governmentLedgerCodeJson);
        return status;
    }
    public GovernmentLedgerCode fetch(String governmentLedgerCodeId) throws Exception {
        if (governmentLedgerCodeId == null || governmentLedgerCodeId.isEmpty()) {
            return null;
        }
        String governmentLedgerCodeJson = DBManager.getDbConnection().fetch(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, governmentLedgerCodeId);
        if (governmentLedgerCodeJson == null || governmentLedgerCodeJson.isEmpty()) {
            return null;
        }
        List<GovernmentLedgerCode> governmentLedgerCodeList = new Gson().fromJson(governmentLedgerCodeJson, new TypeToken<List<GovernmentLedgerCode>>() {
        }.getType());
        if (governmentLedgerCodeList == null || governmentLedgerCodeList.isEmpty()) {
            return null;
        }
        return governmentLedgerCodeList.get(0);

    }
    
    public boolean deleteGovernmentLedgerCode(String governmentLedgerCodeId, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        GovernmentLedgerCode governmentLedgerCode = fetch(governmentLedgerCodeId);
        governmentLedgerCode.setStatus(ApplicationConstants.DELETE);
        governmentLedgerCode.setUpdatedBy(userName);

        String governmentLedgerCodeJson = new Gson().toJson(governmentLedgerCode);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.GOVERNMENT_LEDGER_CODE_TABLE, governmentLedgerCodeId, governmentLedgerCodeJson);
        if (status) {
            return true;
        }
        return false;
    }
//    public User fetch(String userId) throws Exception {
//        if (userId == null || userId.isEmpty()) {
//            return null;
//        }
//        String userJson = DBManager.getDbConnection().fetch(ApplicationConstants.USER_TABLE, userId);
//        if (userJson == null || userJson.isEmpty()) {
//            return null;
//        }
//        List<User> userList = new Gson().fromJson(userJson, new TypeToken<List<User>>() {
//        }.getType());
//        if (userList == null || userList.isEmpty()) {
//            return null;
//        }
//        return userList.get(0);
//
//    }
}
