/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.MinorHead;
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
 * @author upendra/shwetha
 */
public class MinorHeadManager {

    /**
     * Inserts document into <code>ApplicationConstants.MINORHEAD_TABLE</code>
     * collection.If document list already contains <code>minorHead</code> or
     * <code>minorHeadCode</code> field values, insertion will not take place
     *
     * @param minorHeadName minor head name
     * @param minorHeadCode minor head code
     * @param userId <code>_id</code> value of <code>User</code> object in
     * Hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String saveMinorHead(String minorHeadName, String minorHeadCode, String userId) throws Exception {

        String result;
        if (minorHeadName == null || minorHeadCode == null || userId == null) {
            return null;
        }

        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("minorHead", minorHeadName);
        map1.put("minorHeadCode", minorHeadCode);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.MINORHEAD_TABLE, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.MINORHEAD_TABLE, map1)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        MinorHead object = new MinorHead();
        object.setMinorHead(minorHeadName);
        object.setMinorHeadCode(minorHeadCode);
        object.setStatus(ApplicationConstants.ACTIVE);
        object.setCreateDate(System.currentTimeMillis() + "");
        object.setUpdateDate(System.currentTimeMillis() + "");
        object.setCreatedBy(userName);
        object.setUpdatedBy(userName);
        String objectJson = new Gson().toJson(object);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.MINORHEAD_TABLE, objectJson);
        if (id != null) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

    /**
     * Displays list of documents in the
     * <code>ApplicationConstants.MINORHEAD_TABLE</code> collection.Only those
     * document whose <code>status</code> field is <code>Active</code> will
     * appear in the list.
     *
     * @return list of documents in the JSON string.
     * @throws Exception
     */
    public String viewMinorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MINORHEAD_TABLE, conditionMap);
        return result;

    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.MINORHEAD_TABLE</code> collection. If document
     * list already contains <code>minorHead</code> or
     * <code>minorHeadCode</code> field values, updation will not take place
     *
     * @param minorHeadName minor head name
     * @param minorHeadCode minor head code
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userId <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection in
     * hexadecimal string
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String updateMinorHead(String minorHeadName, String minorHeadCode, String primaryKey, String userId) throws Exception {

        String result = "";
        if (minorHeadName == null || minorHeadCode == null || userId == null) {
            result = null;
        } else if (!minorHeadName.equalsIgnoreCase(null) && !minorHeadCode.equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("minorHead", minorHeadName);
            map1.put("minorHeadCode", minorHeadCode);
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.MINORHEAD_TABLE, map, primaryKey) || Duplicate.isDuplicateforUpdate(ApplicationConstants.MINORHEAD_TABLE, map1, primaryKey)) {
                return ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                String minorHeadJson = DBManager.getDbConnection().fetch(ApplicationConstants.MINORHEAD_TABLE, primaryKey);
                List<MinorHead> minorHeadlist = new Gson().fromJson(minorHeadJson, new TypeToken<List<MinorHead>>() {
                }.getType());
                MinorHead dbObject = minorHeadlist.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setMinorHead(minorHeadName);
                dbObject.setMinorHeadCode(minorHeadCode);
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.MINORHEAD_TABLE, primaryKey, dbObjectJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    /**
     * Updates status field of selected document in the
     * <code>ApplicationConstants.MINORHEAD_TABLE</code> collection.If
     * <code>_id</code> value (in hexadecimal string) of document is already
     * saved in <code>minorHead</code> field of
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code>
     * collection,document will not get update.
     *
     * @param primaryKey <code>_id</code> value of document in the
     * <code>ApplicationConstants.MINORHEAD_TABLE</code> collection in
     * hexadecimal String
     * @param userId  <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection in hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if either of document is null...
     */
    public String deleteMinorHead(String primaryKey, String userId) throws Exception {

        String status;
        if (primaryKey == null || userId == null) {
            return null;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, "minorHead", primaryKey))) {
            return ApplicationConstants.DELETE_MESSAGE;
        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.MINORHEAD_TABLE, primaryKey);
        List<MinorHead> minorHeadlist = new Gson().fromJson(existJson, new TypeToken<List<MinorHead>>() {
        }.getType());
        MinorHead dbObject = minorHeadlist.get(0);
        dbObject.setStatus(ApplicationConstants.DELETE);
        dbObject.setUpdateDate(System.currentTimeMillis() + "");
        dbObject.setDeletedBy(userName);
        String dbObjectJson = new Gson().toJson(dbObject);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.MINORHEAD_TABLE, primaryKey, dbObjectJson);
        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;

    }
}
