/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.GISGroup;
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
 * @author
 */
public class GISGroupMasterManager {

    /**
     * Inserts documents into <code>ApplicationConstants.GIS_GROUP_TABLE</code>
     * collection.If document list already contains <code>groupName</code> field
     * value,insertion will not happen
     *
     * @param gisGroup <code>GISGroup</code> object data
     * @param userid <code>_id</code> value of document (in hexadecimal string)
     * in <code>ApplicationConstants.USER</code> collection
     * @return <code>_id</code> value of inserted document (in hexadecimal
     * string) if insertion is successful.
     * @throws Exception if second argument is null.
     */
    public String save(GISGroup gisGroup, String userid) throws Exception {

        if (userid == null) {
            return ApplicationConstants.FAIL;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("groupName", gisGroup.getGroupName());
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.GIS_GROUP_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        gisGroup.setCreateDate(System.currentTimeMillis() + "");
        gisGroup.setUpdateDate(System.currentTimeMillis() + "");
        gisGroup.setCreatedBy(userName);
        gisGroup.setUpdatedBy(userName);
        gisGroup.setStatus(ApplicationConstants.ACTIVE);
        String Json = new Gson().toJson(gisGroup);
        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.GIS_GROUP_TABLE, Json);

        return primaryKey;

    }

    /**
     * lists all documents in the
     * <code>ApplicationConstants.GIS_GROUP_TABLE</code> collection.Only those
     * documents whose <code>ApplicationConstants.STATUS</code> field
     *
     * @return List of documents in JSON String format.
     * @throws java.lang.Exception
     */
    public String viewGISGroupMaster() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GIS_GROUP_TABLE, hMap);
        if (result == null) {
            result = ApplicationConstants.NO_DATA_FOUND;
        }
        return result;

    }

    /**
     * @param gisGroup
     * @param primaryKey
     * @param userid
     * @return
     * @throws Exception
     */
    public String update(GISGroup gisGroup, String primaryKey, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String dbObject = new GISGroupMasterManager().fetch(primaryKey);
        GISGroup dbGis = new Gson().fromJson(dbObject, new TypeToken<GISGroup>() {
        }.getType());
        String status;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("groupName", gisGroup.getGroupName());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.GIS_GROUP_TABLE, duplicateCheckMap, primaryKey)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        } else {
            dbGis.setUpdateDate(System.currentTimeMillis() + "");
            dbGis.setUpdatedBy(userName);

            dbGis.setGroupName(gisGroup.getGroupName());
            dbGis.setGradePayFrom(gisGroup.getGradePayFrom());
            dbGis.setGradePayTo(gisGroup.getGradePayTo());
            dbGis.setInsCoverage(gisGroup.getInsCoverage());
            dbGis.setRateOfSub(gisGroup.getRateOfSub());
            dbGis.setRemarks(gisGroup.getRemarks());
            dbGis.setStatus(ApplicationConstants.ACTIVE);

            String jsonStr = new Gson().toJson(dbGis);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.GIS_GROUP_TABLE, primaryKey, jsonStr);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }

        return status;

    }

    /**
     * This method is to update status field of GISGroup Object.
     *
     * @param primaryKey is <code>_id</code> value of <code>GISGroup</code>
     * Object in hexadecimal string.
     * @param userid is <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string.
     * @return
     * <p>
     * <code>True</code>on successful updation of <code>GISGroup</code>
     * Object</p>
     * @throws java.lang.Exception if either of argument is <code>null</code>...
     */
    public boolean delete(String primaryKey, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (primaryKey == null || primaryKey.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<GISGroup>() {
        }.getType();
        String gis = new GISGroupMasterManager().fetch(primaryKey);

        if (gis == null || gis.isEmpty()) {
            return false;
        }

        GISGroup jsonStr = new Gson().fromJson(gis, type);
        jsonStr.setStatus(ApplicationConstants.DELETE);

        jsonStr.setUpdateDate(System.currentTimeMillis() + "");
        jsonStr.setDeletedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.GIS_GROUP_TABLE, primaryKey, new Gson().toJson(jsonStr));

        return result;
    }

    /**
     * This method is to search the <code>GISGroup</code> Object data by using
     * <code>_id</code> value.
     *
     * @param primaryKey is <code>_id</code> value of the <code>GISGroup</code>
     * Object in hexadecimal string.
     * @return
     * <p>
     * <code>GISGroup</code> Object data in the JSON String format.</P>
     * @throws java.lang.Exception if either of argument is <code>null</code>..
     */
    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.GIS_GROUP_TABLE, primaryKey);
        List<GISGroup> gisList = new Gson().fromJson(result, new TypeToken<List<GISGroup>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

}
