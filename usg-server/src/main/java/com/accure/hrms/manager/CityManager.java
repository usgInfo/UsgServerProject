/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.CityCategoryMaster;
import com.accure.hrms.dto.CityMaster;
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
public class CityManager {

    /**
     * Inserts documents into <code>ApplicationConstants.CITY_TABLE</code>
     * collection.If document list already contains <code>cityName</code> field
     * value, document will not save into collection
     *
     * @param city <code>CityMaster</code> object data
     * @param userid <code>_id</code> value of a document(in hexadecimal string)
     * in <code>ApplicationConstants.USER</code> collection.
     * @return <code>_id</code> value of inserted document ,if insertion is
     * successful
     * @throws Exception if second argument is null..
     */
    public String save(CityMaster city, String userid) throws Exception {

        if (city == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("cityName", city.getCityName());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.CITY_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        city.setStatus(ApplicationConstants.ACTIVE);
        city.setCreateDate(System.currentTimeMillis() + "");
        city.setUpdateDate(System.currentTimeMillis() + "");
        city.setCreatedBy(userName);
        city.setUpdatedBy(userName);
        String jsonStr = new Gson().toJson(city);

        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.CITY_TABLE, jsonStr);

        return primaryKey;
    }

    /**
     * lists all documents in <code>ApplicationConstants.CITY_TABLE</code>
     * collection.Only those document whose
     * <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list.
     *
     * @return list of documents in JSON String
     * @throws java.lang.Exception
     */
    public String view() throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CITY_TABLE, conditionMap);
        List<CityMaster> list = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());

        for (CityMaster li : list) {
            if (li.getCityName() != null) {

                String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_CATEGORY_TABLE, li.getCityCategory());
                List<CityCategoryMaster> dbList = new Gson().fromJson(dbStr, new TypeToken<List<CityCategoryMaster>>() {
                }.getType());
                CityCategoryMaster dbObj = dbList.get(0);
                li.setCategoryCity(dbObj.getCityCategory());
                li.setCategoryCityName(dbObj.getCityCategory());

            }
        }

        return new Gson().toJson(list);
    }

    /**
     * lists all documents in <code>ApplicationConstants.CITY_TABLE</code>
     * collection.Only those document whose
     * <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> and <code>categoryCity</code>
     * field value is same as argument value are appear in the list.
     *
     * @param cityCategory <code>categoryCity</code> field value of a document
     * in <code>ApplicationConstants.CITY_TABLE</code> collection.
     * @return list of documents in JSON String
     * @throws java.lang.Exception
     */
    public String listOfCityView(String cityCategory) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("categoryCity", cityCategory);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CITY_TABLE, conditionMap);
        List<CityMaster> list = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());

        return new Gson().toJson(list);
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.CITY_TABLE</code> collection.If document list
     * already contains <code>cityName</code> field value, updation will not
     * happen
     *
     * @param city <code>CityMaster</code> object data in JSON String
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userid <code>_id</code> value of document in
     * <code>ApplicationConstants.CITY_TABLE</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is null..
     */
    public String update(String city, String primaryKey, String userid) throws Exception {

        if (primaryKey == null || userid == null) {
            return null;
        }

        CityMaster cityObj = new Gson().fromJson(city, new TypeToken<CityMaster>() {
        }.getType());

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("cityName", cityObj.getCityName());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.CITY_TABLE, duplicateCheckMap, primaryKey)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        String status;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, primaryKey);

        List<CityMaster> list = new Gson().fromJson(jsonStr, new TypeToken<List<CityMaster>>() {
        }.getType());

        CityMaster dbObj = list.get(0);

        dbObj.setCategoryCity(cityObj.getCityCategory());
        dbObj.setAddressOne(cityObj.getAddressOne());
        dbObj.setAddressTwo(cityObj.getAddressTwo());
        dbObj.setCityName(cityObj.getCityName());
        dbObj.setIsMetro(cityObj.getIsMetro());
        dbObj.setIsOther(cityObj.getIsOther());
        dbObj.setCreateDate(cityObj.getCreateDate());
        dbObj.setCreatedBy(cityObj.getCreatedBy());
        dbObj.setStatus(ApplicationConstants.ACTIVE);
        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        dbObj.setUpdatedBy(userName);

        String json = new Gson().toJson(dbObj);

        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CITY_TABLE, primaryKey, json);

        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.CITY_TABLE</code>
     * collection.If <code>_id</code> value of selected document is already
     * saved in <code>city</code> field value in
     * <code>ApplicationConstants.QUARTER_TRANSACTION_TABLE</code> or
     * <code>ApplicationConstants.QUARTER_TABLE</code> or
     * <code>ApplicationConstants.HEAD_SLAB_TABLE</code> collection or
     * <code>postingCity</code> field of
     * <code>ApplicationConstants.EMPLOYEE_TABLE</code> collection updation will
     * not happen.
     *
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userid <code>_id</code> value of document in
     * <code>ApplicationConstants.CITY_TABLE</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws java.lang.Exception if either of argument is <code>null</code>...
     */
    public String delete(String primaryKey, String userid) throws Exception {
        String status;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        if (primaryKey == null || primaryKey.isEmpty()) {
            return ApplicationConstants.FAIL;
        }
        Type type = new TypeToken<CityMaster>() {
        }.getType();
        String city = new CityManager().fetch(primaryKey);
        if (city == null || city.isEmpty()) {
            return ApplicationConstants.FAIL;
        }
        CityMaster cityJson = new Gson().fromJson(city, type);
        cityJson.setStatus(ApplicationConstants.DELETE);
        cityJson.setUpdateDate(System.currentTimeMillis() + "");
        cityJson.setDeletedBy(userName);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.QUARTER_TRANSACTION_TABLE, "city", primaryKey) || DeleteDependencyManager.hasDependency(ApplicationConstants.QUARTER_TABLE, "city", primaryKey) || DeleteDependencyManager.hasDependency(ApplicationConstants.HEAD_SLAB_TABLE, "city", primaryKey) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "postingCity", primaryKey)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CITY_TABLE, primaryKey, new Gson().toJson(cityJson));
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;

    }

    /**
     * Searches the particular document in the
     * <code>ApplicationConstants.CITY_TABLE</code> collection using _id value.
     *
     * @param primaryKey primary key value
     * @return searched document in JSON String
     * @throws java.lang.Exception if argument is <code>null</code>....
     */
    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, primaryKey);
        List<CityMaster> list = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }

}
