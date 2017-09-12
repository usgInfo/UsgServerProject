/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.CityCategoryMaster;
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
 * @author 
 */
public class CityCategoryManager {

    /**
     * Inserts documents into
     * <code>ApplicationConstants.CITY_CATEGORY_TABLE</code> collection.If
     * document list already contains <code>cityCategory</code> field value,
     * document will not save into collection
     *
     * @param cityCategory <code>CityCategoryMaster</code> object in JSON
     * String.
     * @param userid <code>_id</code> value of a document(in hexadecimal string)
     * in <code>ApplicationConstants.USER</code> collection.
     * @return <code>_id</code> value of inserted document ,if insertion is
     * successful
     * @throws Exception if second argument is null..
     */
    public String save(String cityCategory, String userid) throws Exception {

        if (cityCategory == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("cityCategory", cityCategory);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.CITY_CATEGORY_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        CityCategoryMaster cityCategoryobj = new CityCategoryMaster();
        cityCategoryobj.setCityCategory(cityCategory);
        cityCategoryobj.setCreatedBy(userName);
        cityCategoryobj.setUpdatedBy(userName);
        cityCategoryobj.setStatus(ApplicationConstants.ACTIVE);
        cityCategoryobj.setCreateDate(System.currentTimeMillis() + "");
        cityCategoryobj.setUpdateDate(System.currentTimeMillis() + "");
        String cityCategoryjson = new Gson().toJson(cityCategoryobj);

        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.CITY_CATEGORY_TABLE, cityCategoryjson);

        return primaryKey;
    }

    /**
     * lists all documents in
     * <code>ApplicationConstants.CITY_CATEGORY_TABLE</code> collection.Only
     * those document whose <code>ApplicationConstants.STATUS</code> field value
     * is <code>ApplicationConstants.ACTIVE</code> are appear in the list.
     *
     * @return list of documents in JSON String
     * @throws java.lang.Exception
     */
    public String view() throws Exception {
        HashMap<String, String> cityCategoryMap = new HashMap<String, String>();
        cityCategoryMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CITY_CATEGORY_TABLE, cityCategoryMap);
        return result;

    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.CITY_CATEGORY_TABLE</code> collection.If
     * document list already contains <code>cityCategory</code> field value
     * updation will not take place.
     *
     * @param cityCategory <code>CityCategoryMaster</code> object in JSON
     * String.
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userid <code>_id</code> value of a document in the
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is null..
     */
    public String update(String cityCategory, String primaryKey, String userid) throws Exception {

        if (primaryKey == null || userid == null) {
            return ApplicationConstants.FAIL;
        }

        String status;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("cityCategory", cityCategory);

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.CITY_CATEGORY_TABLE, duplicateCheckMap, primaryKey)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_CATEGORY_TABLE, primaryKey);
        List<CityCategoryMaster> list = new Gson().fromJson(jsonStr, new TypeToken<List<CityCategoryMaster>>() {
        }.getType());
        CityCategoryMaster dbObj = list.get(0);

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        dbObj.setCityCategory(cityCategory);
        dbObj.setUpdatedBy(userName);
        dbObj.setStatus(ApplicationConstants.ACTIVE);
        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        String json = new Gson().toJson(dbObj);

        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CITY_CATEGORY_TABLE, primaryKey, json);
        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.CITY_CATEGORY_TABLE</code>
     * collection.If <code>_id</code> value of selected document is already
     * saved in <code>categoryCity</code> filed value of
     * <code>ApplicationConstants.CITY_TABLE</code> collection or
     * <code>cityCategory</code> field value of
     * <code>ApplicationConstants.HEAD_SLAB_TABLE</code> collection,updation
     * will not happen
     *
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userid <code>_id</code> value of a document in the
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is null..
     */
    public String delete(String primaryKey, String userid) throws Exception {

        if (userid == null || primaryKey == null) {
            return ApplicationConstants.FAIL;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_CATEGORY_TABLE, primaryKey);

        List<CityCategoryMaster> list = new Gson().fromJson(existJson, new TypeToken<List<CityCategoryMaster>>() {
        }.getType());

        CityCategoryMaster dbObj = list.get(0);

        dbObj.setDeletedBy(userName);
        dbObj.setStatus(ApplicationConstants.DELETE);
        dbObj.setUpdateDate(System.currentTimeMillis() + "");

        String jsonStr = new Gson().toJson(dbObj);

        String status;

        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.CITY_TABLE, "categoryCity", primaryKey)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.HEAD_SLAB_TABLE, "cityCategory", primaryKey))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CITY_CATEGORY_TABLE, primaryKey, jsonStr);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

}
