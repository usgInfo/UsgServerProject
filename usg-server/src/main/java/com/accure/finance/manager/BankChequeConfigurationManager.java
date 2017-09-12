/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.BankChequeConfiguration;
import com.accure.finance.dto.BankName;
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
 * @author Shwetha T S
 */
public class BankChequeConfigurationManager {

    public String save(BankChequeConfiguration object, String userId) throws Exception {

        String result = "";
        HashMap map = new HashMap();
        if (object == null || userId == null) {
            result = null;
        }
        if (!(object.getBank().equalsIgnoreCase(null))) {
            map.put("bank", object.getBank());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, map)) {
                result = ApplicationConstants.DUPLICATE;
            } else if (!(userId).equalsIgnoreCase(null)) {
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();

                object.setCreateDate(System.currentTimeMillis() + "");
                object.setUpdateDate(System.currentTimeMillis() + "");
                object.setStatus(ApplicationConstants.ACTIVE);
                object.setCreatedBy(userName);
                object.setUpdatedBy(userName);

                String objectJson = new Gson().toJson(object);

                String fResult = DBManager.getDbConnection().insert(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, objectJson);

                if (fResult != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }

        }
        return result;

    }

    public String fetch(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, objId);
        List<BankChequeConfiguration> objList = new Gson().fromJson(result, new TypeToken<List<BankChequeConfiguration>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    public String delete(String objId, String userId) throws Exception {
        String result = "";
        if (objId == null || objId.isEmpty()) {
            return null;
        }
        Type type = new TypeToken<BankChequeConfiguration>() {
        }.getType();
        String obj = new BankChequeConfigurationManager().fetch(objId);
        if (obj == null || obj.isEmpty()) {
            return null;
        } else if (userId != null || userId.isEmpty()) {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            BankChequeConfiguration objrJson = new Gson().fromJson(obj, type);
            objrJson.setStatus(ApplicationConstants.DELETE);
            objrJson.setDeletedBy(userName);
            objrJson.setUpdateDate(System.currentTimeMillis() + "");
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, objId, new Gson().toJson(objrJson));
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    public String update(BankChequeConfiguration object, String objId, String userId) throws Exception {

        String result = "";
        HashMap map = new HashMap();
        if (object == null || userId == null) {
            result = null;
        }
        if (!(object.getBank().equalsIgnoreCase(null))) {
            map.put("bank", object.getBank());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, map,objId)) {
                result = ApplicationConstants.DUPLICATE;

            } else if (!(userId).equalsIgnoreCase(null)) {
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                String dbStr = new BankChequeConfigurationManager().fetch(objId);
                if (dbStr == null || dbStr.isEmpty()) {
                    return null;
                }
                BankChequeConfiguration dbObject = new Gson().fromJson(dbStr, new TypeToken<BankChequeConfiguration>() {
                }.getType());
                object.setUpdateDate(System.currentTimeMillis() + "");
                object.setStatus(ApplicationConstants.ACTIVE);
                object.setUpdatedBy(userName);
                object.setCreateDate(dbObject.getCreateDate());
                object.setCreatedBy(dbObject.getCreatedBy());
                String objectJson = new Gson().toJson(object);

                boolean status = DBManager.getDbConnection().update(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, objId, objectJson);

                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }

        }
        return result;

    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, conditionMap);
        List<BankChequeConfiguration> list = new Gson().fromJson(result1, new TypeToken<List<BankChequeConfiguration>>() {
        }.getType());
        for (BankChequeConfiguration cl : list) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_NAME_TABLE, cl.getBank());
            List<BankName> gaList = new Gson().fromJson(gaJson, new TypeToken<List<BankName>>() {
            }.getType());
            BankName gal = gaList.get(0);
            cl.setBank(gal.getBankName());
        }
        return new Gson().toJson(list);
    }
}
