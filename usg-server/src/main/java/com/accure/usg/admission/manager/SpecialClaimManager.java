/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.SpecialClaimMaster;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author KC
 */
public class SpecialClaimManager {
    
    public String createSpecialClaim(String specialClaimName, String loginUserId) throws Exception {
        SpecialClaimMaster obj = new SpecialClaimMaster();
        String result;
        HashMap map = new HashMap();
        map.put("status", ApplicationConstants.ACTIVE);
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SPECIAL_CLAIM_TABLE, map);
            if (result != null) {
            List<SpecialClaimMaster> specialclaimList = new Gson().fromJson(result, new TypeToken<List<SpecialClaimMaster>>() {
            }.getType());
            for (int i = 0; i < specialclaimList.size(); i++) {
                String specialClaimNameDb = specialclaimList.get(i).getSpecialClaimName();
                String dupCheck = specialClaimName;
                specialClaimNameDb = specialClaimNameDb.replaceAll(" ", "");
                dupCheck = dupCheck.replaceAll(" ", "");
                if (dupCheck.equalsIgnoreCase(specialClaimNameDb)) {
                    result = ApplicationConstants.DATA_EXISTED;
                }
            }
        }

        if (loginUserId == null) {
            result = null;
        } 
        else if (result == ApplicationConstants.DATA_EXISTED) {
            result = ApplicationConstants.DATA_EXISTED;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            obj.setSpecialClaimName(specialClaimName);
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreatedBy(userName);
            obj.setUpdatedBy(userName);
            obj.setUpdateDate(System.currentTimeMillis() + "");
            String specialClaimJson = new Gson().toJson(obj);
            String specialClaimId = DBManager.getDbConnection().insert(ApplicationConstants.SPECIAL_CLAIM_TABLE, specialClaimJson);
            if (specialClaimId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }
    
    public String viewSpecialClaimMasterList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SPECIAL_CLAIM_TABLE, conditionMap);
        return result;

    }
    
    public String updateSpecialClaimMaster(String specialClaimName, String primaryKey, String userId) throws Exception {
        String result = "";
        if (specialClaimName == null || userId == null) {
            return null;
        }
        if (!specialClaimName.equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
//           <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            map.put("status", ApplicationConstants.ACTIVE);
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SPECIAL_CLAIM_TABLE, map);
            if (result != null) {
            List<SpecialClaimMaster> specialclaimList = new Gson().fromJson(result, new TypeToken<List<SpecialClaimMaster>>() {
            }.getType());
            for (int i = 0; i < specialclaimList.size(); i++) {
                String specialClaimNameDb = specialclaimList.get(i).getSpecialClaimName();
                String dupCheck = specialClaimName;
                specialClaimNameDb = specialClaimNameDb.replaceAll(" ", "");
                dupCheck = dupCheck.replaceAll(" ", "");
                if (dupCheck.equalsIgnoreCase(specialClaimNameDb)) {
                    result = ApplicationConstants.DATA_EXISTED;
                }
            }
        }
         if(result == ApplicationConstants.DATA_EXISTED)
         {
             result = ApplicationConstants.DATA_EXISTED;
         }
            else {
                String specialClaimJson = DBManager.getDbConnection().fetch(ApplicationConstants.SPECIAL_CLAIM_TABLE, primaryKey);
                List<SpecialClaimMaster> list = new Gson().fromJson(specialClaimJson, new TypeToken<List<SpecialClaimMaster>>() {
                }.getType());
                SpecialClaimMaster dbObject = list.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setSpecialClaimName(specialClaimName);
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.SPECIAL_CLAIM_TABLE, primaryKey, dbObjectJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }
    public String deleteSpecialClaimMaster(String primaryKey, String userId) throws Exception {
        String status;
        if (primaryKey == null || userId == null) {
            return null;
        } 
//        else if ((DeleteDependencyManager.hasDependency(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, "majorHead", primaryKey))) {
//            status = ApplicationConstants.DELETE_MESSAGE;
//        } 
        else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.SPECIAL_CLAIM_TABLE, primaryKey);
            List<SpecialClaimMaster> list = new Gson().fromJson(existrelationJson, new TypeToken<List<SpecialClaimMaster>>() {
            }.getType());
            SpecialClaimMaster dbObject = list.get(0);
            dbObject.setStatus(ApplicationConstants.DELETE);
            dbObject.setUpdateDate(System.currentTimeMillis() + "");
            dbObject.setDeletedBy(userName);
            String dbObjectJson = new Gson().toJson(dbObject);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SPECIAL_CLAIM_TABLE, primaryKey, dbObjectJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;

    }

}
