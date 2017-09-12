/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.ChapterVIType;
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
 * @author Asif
 */
public class ChapterVITypeManager {

    public String save(ChapterVIType obj, String loginUserId) throws Exception {
        String result = "";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("chapterVIType", obj.getChapterVIType());
        if (loginUserId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.CHAPTERVI_TYPE_MASTER, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreatedBy(userName);
            String objJson = new Gson().toJson(obj);
            String Id = DBManager.getDbConnection().insert(ApplicationConstants.CHAPTERVI_TYPE_MASTER, objJson);

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
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CHAPTERVI_TYPE_MASTER, Id);
        List<ChapterVIType> objList = new Gson().fromJson(result, new TypeToken<List<ChapterVIType>>() {
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
        Type type = new TypeToken<ChapterVIType>() {
        }.getType();
        String obj = new ChapterVITypeManager().fetch(Id);
        if (obj == null || obj.isEmpty()) {
            return null;
        }
        String status = "";
        ChapterVIType objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.DELETE);
        objrJson.setUpdateDate(System.currentTimeMillis() + "");
        objrJson.setUpdatedBy(userName);

        if (DeleteDependencyManager.hasDependency(ApplicationConstants.SALARY_HEAD_TABLE, "chapterType", Id)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.CHAPTERVI_TYPE_MASTER, Id, new Gson().toJson(objrJson));
            if (result) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
        //boolean result = DBManager.getDbConnection().update(ApplicationConstants.CHAPTERVI_TYPE_MASTER, Id, new Gson().toJso
    }

    public String update(ChapterVIType obj, String Id, String loginUserId) throws Exception {
        String result = "";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("chapterVIType", obj.getChapterVIType());
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname();

        if (loginUserId == null) {
            return null;
        } else if (isDuplicateforUpdate(ApplicationConstants.CHAPTERVI_TYPE_MASTER, conditionMap, Id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            Type type = new TypeToken<ChapterVIType>() {
            }.getType();
            String objstr = new ChapterVITypeManager().fetch(Id);
            ChapterVIType dbObjJson = new Gson().fromJson(objstr, type);
            dbObjJson.setUpdateDate(System.currentTimeMillis() + "");
            dbObjJson.setStatus(ApplicationConstants.ACTIVE);
            dbObjJson.setChapterVIType(obj.getChapterVIType());
            dbObjJson.setUpdatedBy(userName);
            String objrJson = new Gson().toJson(dbObjJson);
            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.CHAPTERVI_TYPE_MASTER, Id, objrJson);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.DUPLICATE;
            }
        }

        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CHAPTERVI_TYPE_MASTER, conditionMap);
        return result1;
    }

    public static void main(String[] args) throws Exception {
        String res = new ChapterVITypeManager().fetchAll();
        //System.out.println(res);
    }
}
