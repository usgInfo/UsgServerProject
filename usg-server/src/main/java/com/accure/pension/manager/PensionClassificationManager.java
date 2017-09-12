/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.pension.dto.PensionClassification;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shwetha T S
 */
public class PensionClassificationManager {

    public String save(String object, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (object == null) {
            return null;
        }

        String result;
        if (duplicate(object)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            PensionClassification obj = new PensionClassification();
            obj.setPensionClassification(object);
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setCreatedBy(userName);
            obj.setUpdatedBy(userName);
            String cityCategoryjson = new Gson().toJson(obj);
            result = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, cityCategoryjson);
        }
        return result;
    }

    public boolean duplicate(String classification) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, conditionMap);
            if (result != null) {
                List<PensionClassification> list = new Gson().fromJson(result, new TypeToken<List<PensionClassification>>() {
                }.getType());
                for (PensionClassification li : list) {
                    if (classification.equalsIgnoreCase(li.getPensionClassification())) {
                        res = true;
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String view() throws Exception {
        HashMap<String, String> cityCategoryMap = new HashMap<String, String>();
        cityCategoryMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, cityCategoryMap);
        return result;
    }

    public boolean update(String classification, String cCid, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        boolean result=false;

        if (!duplicate(classification)) {
//            result = false;
//        } else {
            String categoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, cCid);
            List<PensionClassification> categolist = new Gson().fromJson(categoryJson, new TypeToken<List<PensionClassification>>() {
            }.getType());
            PensionClassification categor = categolist.get(0);
            PensionClassification categoryobj = new PensionClassification();
            categoryobj.setPensionClassification(classification);
            categoryobj.setCreateDate(categor.getCreateDate());
            categoryobj.setCreatedBy(categor.getCreatedBy());
            categoryobj.setStatus(ApplicationConstants.ACTIVE);
            categoryobj.setUpdateDate(System.currentTimeMillis() + "");
            categoryobj.setUpdatedBy(userName);
            String categojson = new Gson().toJson(categoryobj);
            result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, cCid, categojson);
       }

        return result;
    }

    public boolean delete(String cCId, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, cCId);
        List<PensionClassification> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionClassification>>() {
        }.getType());
        PensionClassification category = categorylist.get(0);
        
        PensionClassification categoryobje = new PensionClassification();
        categoryobje.setPensionClassification(category.getPensionClassification());
        categoryobje.setCreateDate(category.getCreateDate());
        categoryobje.setCreatedBy(category.getCreatedBy());
        categoryobje.setUpdatedBy(userName);
        categoryobje.setStatus(ApplicationConstants.DELETE);
        categoryobje.setUpdateDate(System.currentTimeMillis() + "");
        String categoryJson = new Gson().toJson(categoryobje);
        
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_CLASSIFICATION_TABLE, cCId, categoryJson);
        return status;
    }

}
