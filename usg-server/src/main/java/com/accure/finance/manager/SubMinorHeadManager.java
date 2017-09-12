/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.SubMinorHead;
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
 * @author upendra/Shwetha T S
 */
public class SubMinorHeadManager {

    /**
     * Inserts document into
     * <code>ApplicationConstants.SUB_MINORHEAD_TABLE</code> collection.If
     * document list already contains <code>subMinorHead</code> or
     * <code>subMinorHeadCode</code> field values, insertion will not take place
     *
     * @param subMinorHeadName sub minor head name
     * @param subMinorHeadCode sub minor head code
     * @param userId <code>_id</code> value of <code>User</code> object in
     * Hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String saveSubMinorHead(String subMinorHeadName, String subMinorHeadCode, String userId) throws Exception {
        String result = "";

        if (subMinorHeadName == null || subMinorHeadCode == null || userId == null) {
            result = null;

        } else if (!subMinorHeadName.equalsIgnoreCase(null) && !subMinorHeadCode.equalsIgnoreCase(null)) {

            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("subMinorHead", subMinorHeadName);
            map1.put("subMinorHeadCode", subMinorHeadCode);
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.SUB_MINORHEAD_TABLE, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.SUB_MINORHEAD_TABLE, map1)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                SubMinorHead object = new SubMinorHead();
                object.setSubMinorHead(subMinorHeadName);
                object.setSubMinorHeadCode(subMinorHeadCode);
                object.setStatus(ApplicationConstants.ACTIVE);
                object.setCreateDate(System.currentTimeMillis() + "");
                object.setUpdateDate(System.currentTimeMillis() + "");
                object.setCreatedBy(userName);
                object.setUpdatedBy(userName);
                String objectJson = new Gson().toJson(object);
                String id = DBManager.getDbConnection().insert(ApplicationConstants.SUB_MINORHEAD_TABLE, objectJson);
                if (id != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }

        return result;
    }

    /**
     * Displays list of documents in the
     * <code>ApplicationConstants.SUB_MINORHEAD_TABLE</code> collection.Only
     * those document whose <code>status</code> field is <code>Active</code>
     * will appear in the list.
     *
     * @return list of documents in the JSON string.
     * @throws Exception
     */
    public String viewSubMinorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SUB_MINORHEAD_TABLE, conditionMap);
        return result;

    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.SUB_MINORHEAD_TABLE</code> collection. If
     * document list already contains <code>subMinorHead</code> or
     * <code>subMinorHeadCode</code> field values, updation will not take place
     *
     * @param subMinorHeadName sub minor head name
     * @param subMinorHeadCode sub minor head code
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userId <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection in hexadecimal string
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String updateSubMinorHead(String subMinorHeadName, String subMinorHeadCode, String primaryKey, String userId) throws Exception {

        String result = "";
        if (subMinorHeadName == null || subMinorHeadCode == null || userId == null) {
            result = null;
        } else if (!subMinorHeadName.equalsIgnoreCase(null) && !subMinorHeadCode.equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("subMinorHead", subMinorHeadName);
            map1.put("subMinorHeadCode", subMinorHeadCode);
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SUB_MINORHEAD_TABLE, map, primaryKey) || Duplicate.isDuplicateforUpdate(ApplicationConstants.SUB_MINORHEAD_TABLE, map1, primaryKey)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MINORHEAD_TABLE, primaryKey);
                List<SubMinorHead> list = new Gson().fromJson(dbStr, new TypeToken<List<SubMinorHead>>() {
                }.getType());
                SubMinorHead dbObject = list.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setSubMinorHead(subMinorHeadName);
                dbObject.setSubMinorHeadCode(subMinorHeadCode);
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.SUB_MINORHEAD_TABLE, primaryKey, dbObjectJson);
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
     * <code>ApplicationConstants.SUB_MINORHEAD_TABLE</code> collection.If
     * <code>_id</code> value (in hexadecimal string) of document is already
     * saved in <code>subMinorHead</code> field of
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code>
     * collection,document will not get update.
     *
     * @param primaryKey <code>_id</code> value of document in the
     * <code>ApplicationConstants.SUB_MINORHEAD_TABLE</code> collection in
     * hexadecimal String
     * @param userId  <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection in hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if either of document is null...
     */
    public String deleteSubMinorHead(String primaryKey, String userId) throws Exception {

        String status;
        if (primaryKey == null || userId == null) {
            return null;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, "subMinorHead", primaryKey))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MINORHEAD_TABLE, primaryKey);
            List<SubMinorHead> list = new Gson().fromJson(existJson, new TypeToken<List<SubMinorHead>>() {
            }.getType());
            SubMinorHead dbObject = list.get(0);
            dbObject.setStatus(ApplicationConstants.DELETE);
            dbObject.setUpdateDate(System.currentTimeMillis() + "");
            dbObject.setDeletedBy(userName);
            String dbObjectJson = new Gson().toJson(dbObject);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SUB_MINORHEAD_TABLE, primaryKey, dbObjectJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;

    }

}
