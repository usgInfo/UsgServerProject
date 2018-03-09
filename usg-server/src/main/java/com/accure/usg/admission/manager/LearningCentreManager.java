/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.payroll.dto.LoanApply;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.BoardMaster;
import com.accure.usg.admission.dto.LearningCenter;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author KC
 */
public class LearningCentreManager {
    
    public String createLearningCentre(LearningCenter learningCenter, String loginUserId) throws Exception {

        String result ;
        HashMap map = new HashMap();

        map.put("institution", learningCenter.getInstitution());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (loginUserId == null) {
            result = null;
        }
//       else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LEARNING_CENTRE_TABLE, map))) {
//            result = ApplicationConstants.DATA_EXISTED;
//        }
        else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            learningCenter.setCreateDate(System.currentTimeMillis() + "");
            learningCenter.setUpdateDate(System.currentTimeMillis() + "");
            learningCenter.setStatus(ApplicationConstants.ACTIVE);
            learningCenter.setCreatedBy(userName);
            learningCenter.setUpdatedBy(userName);

            String learningCentreJson = new Gson().toJson(learningCenter);

            String learningCentreId = DBManager.getDbConnection().insert(ApplicationConstants.LEARNING_CENTRE_TABLE, learningCentreJson);
            if (learningCentreId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }
    
    public List<LearningCenter> fetchAllLearningCentre() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String learningCentreJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEARNING_CENTRE_TABLE, conditionMap);
        List<LearningCenter> learningCentreList = new Gson().fromJson(learningCentreJson, new TypeToken<List<LearningCenter>>() {
        }.getType());
        return learningCentreList;
    }
    
    public String updateLearningCentre(LearningCenter learningCentre, String primaryKey, String userId) throws Exception {
        String result = "";
//        if (boardName == null || userId == null) {
//            return null;
//        }
//        if (!boardName.equalsIgnoreCase(null)) {
//            HashMap map = new HashMap();
//            map.put("board", boardName);
//            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BOARD_TABLE, map, primaryKey)) {
//                result = ApplicationConstants.DATA_EXISTED;
//            } else {
                String learningCentreJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEARNING_CENTRE_TABLE, primaryKey);
                List<LearningCenter> list = new Gson().fromJson(learningCentreJson, new TypeToken<List<LearningCenter>>() {
                }.getType());
                LearningCenter dbObject = list.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setInstitution(learningCentre.getInstitution());
                dbObject.setCity(learningCentre.getCity());
                dbObject.setCentreCode(learningCentre.getCentreCode());
                dbObject.setState(learningCentre.getState());
                dbObject.setCentreName(learningCentre.getCentreName());
                dbObject.setCountry(learningCentre.getCountry());
                dbObject.setEmail(learningCentre.getEmail());
                dbObject.setContactNumber(learningCentre.getContactNumber());
                dbObject.setAddress(learningCentre.getAddress());
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEARNING_CENTRE_TABLE, primaryKey, dbObjectJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
//            }
//        }
        return result;

    }
    
    public String deleteLearningCentre(String primaryKey, String userId) throws Exception {
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
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEARNING_CENTRE_TABLE, primaryKey);
            List<LearningCenter> list = new Gson().fromJson(existrelationJson, new TypeToken<List<LearningCenter>>() {
            }.getType());
            LearningCenter dbObject = list.get(0);
            dbObject.setStatus(ApplicationConstants.DELETE);
            dbObject.setUpdateDate(System.currentTimeMillis() + "");
            dbObject.setDeletedBy(userName);
            String dbObjectJson = new Gson().toJson(dbObject);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.LEARNING_CENTRE_TABLE, primaryKey, dbObjectJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;

    }
    
}
