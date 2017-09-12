/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.FixedHead;
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
 * @author Asif/shwetha
 */
public class FixedHeadManager {

    public String save(FixedHead obj, String loginUserId) throws Exception {

        String result = "";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("fixedHead", obj.getFixedHead());
        if (loginUserId == null) {
            result = null;
        } 
        else if (hasDuplicateforSave(ApplicationConstants.FIXED_HEAD_MASTER, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        }
        else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreatedBy(userName);
            String objJson = new Gson().toJson(obj);
            String Id = DBManager.getDbConnection().insert(ApplicationConstants.FIXED_HEAD_MASTER, objJson);
            if (Id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.FIXED_HEAD_MASTER, Id);
        List<FixedHead> objList = new Gson().fromJson(result, new TypeToken<List<FixedHead>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    public String delete(String Id, String loginUserId) throws Exception {

        if (Id == null || Id.isEmpty()) {
            return null;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<FixedHead>() {
        }.getType();
        String obj = new FixedHeadManager().fetch(Id);
        if (obj == null || obj.isEmpty()) {
            return null;
        }
        FixedHead objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.DELETE);
        objrJson.setDeletedBy(userName);
        String status = "";
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.SALARY_HEAD_TABLE, "fixedHead", Id)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.FIXED_HEAD_MASTER, Id, new Gson().toJson(objrJson));
            if (result) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }

        return status;
    }

    public String update(FixedHead obj, String Id, String loginUserId) throws Exception {
        String result = "";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("fixedHead", obj.getFixedHead());
        if (Id == null || loginUserId == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.FIXED_HEAD_MASTER, conditionMap,Id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            Type type = new TypeToken<FixedHead>() {
            }.getType();
            String dbObjStr = new FixedHeadManager().fetch(Id);
            if (dbObjStr == null || dbObjStr.isEmpty()) {
                return null;
            }
            FixedHead dbObjJson = new Gson().fromJson(dbObjStr, type);
            dbObjJson.setUpdateDate(System.currentTimeMillis() + "");
            dbObjJson.setStatus(ApplicationConstants.ACTIVE);
            dbObjJson.setUpdatedBy(userName);
            dbObjJson.setFixedHead(obj.getFixedHead());
            String objrJson = new Gson().toJson(dbObjJson);
            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.FIXED_HEAD_MASTER, Id, objrJson);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FIXED_HEAD_MASTER, conditionMap);
        return result1;
    }
}
