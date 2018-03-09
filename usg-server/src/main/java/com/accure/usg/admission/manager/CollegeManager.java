/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.CollegeMaster;
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
public class CollegeManager {
    
    public String createCollege(String collegeName, String loginUserId) throws Exception {
        CollegeMaster obj = new CollegeMaster();
        String result = null;
//        String result1 = "";
//        HashMap conditionMap = new HashMap();
//        conditionMap.put("status", ApplicationConstants.ACTIVE);
//        result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COLLEGE_TABLE, conditionMap);
//        if (result1 != null) {
//            List<CollegeMaster> collegelist = new Gson().fromJson(result1, new TypeToken<List<CollegeMaster>>() {
//            }.getType());
//            for (int i = 0; i < collegelist.size(); i++) {
//                String collegeNameDb = collegelist.get(i).getCollegeName();
//                String dupCheck = collegeName;
//                collegeNameDb = collegeNameDb.replaceAll(" ", "");
//                dupCheck = dupCheck.replaceAll(" ", "");
//                if (dupCheck.equalsIgnoreCase(collegeNameDb)) {
//                    result = ApplicationConstants.DATA_EXISTED;
//                }
//            }
//        }
        HashMap map = new HashMap();
//        map.put("collegeName", obj.getCollegeName());
        map.put("collegeName", collegeName);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (loginUserId == null) {
            result = null;
        } else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.COLLEGE_TABLE, map))) {
            result = ApplicationConstants.DATA_EXISTED;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            obj.setCollegeName(collegeName);
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreatedBy(userName);
            obj.setUpdatedBy(userName);
            obj.setUpdateDate(System.currentTimeMillis() + "");
            String collegeJson = new Gson().toJson(obj);
            String collegeId = DBManager.getDbConnection().insert(ApplicationConstants.COLLEGE_TABLE, collegeJson);
            if (collegeId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }
    
    public String viewCollegeMasterList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COLLEGE_TABLE, conditionMap);
        return result;

    }
    
    public String updateCollegeMaster(String collegeName, String primaryKey, String userId) throws Exception {
        String result = "";
        if (collegeName == null || userId == null) {
            return null;
        }
        if (!collegeName.equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            map.put("collegeName", collegeName);
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.COLLEGE_TABLE, map, primaryKey)) {
                result = ApplicationConstants.DATA_EXISTED;
            } else {
                String collegeJson = DBManager.getDbConnection().fetch(ApplicationConstants.COLLEGE_TABLE, primaryKey);
                List<CollegeMaster> list = new Gson().fromJson(collegeJson, new TypeToken<List<CollegeMaster>>() {
                }.getType());
                CollegeMaster dbObject = list.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setCollegeName(collegeName);
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.COLLEGE_TABLE, primaryKey, dbObjectJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }
    
    public String deleteCollegeMaster(String primaryKey, String userId) throws Exception {
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
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.COLLEGE_TABLE, primaryKey);
            List<CollegeMaster> list = new Gson().fromJson(existrelationJson, new TypeToken<List<CollegeMaster>>() {
            }.getType());
            CollegeMaster dbObject = list.get(0);
            dbObject.setStatus(ApplicationConstants.DELETE);
            dbObject.setUpdateDate(System.currentTimeMillis() + "");
            dbObject.setDeletedBy(userName);
            String dbObjectJson = new Gson().toJson(dbObject);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.COLLEGE_TABLE, primaryKey, dbObjectJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;

    }
    
}
