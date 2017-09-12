/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.SubMajorHead;
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
 * @author upendra/shwetha t s
 */
public class SubMajorHeadManager {

    /**
     * Inserts document into
     * <code>ApplicationConstants.SUB_MAJORHEAD_TABLE</code> collection.If
     * document list already contains <code>subMajorHead</code> or
     * <code>subMajorHeadCode</code> field values, insertion will not take place
     *
     * @param subMajorHeadName sub major head name
     * @param subMajorHeadCode sub major head code
     * @param userId <code>_id</code> value of <code>User</code> object in
     * Hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String saveSubMajorHead(String subMajorHeadName, String subMajorHeadCode, String userId) throws Exception {

        String result = "";

        if (subMajorHeadName == null || subMajorHeadCode == null || userId == null) {
            return null;

        }
        if (!subMajorHeadName.equalsIgnoreCase(null) && !subMajorHeadCode.equalsIgnoreCase(null)) {

            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("subMajorHead", subMajorHeadName);
            map1.put("subMajorHeadCode", subMajorHeadCode);
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.SUB_MAJORHEAD_TABLE, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.SUB_MAJORHEAD_TABLE, map1)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                SubMajorHead object = new SubMajorHead();
                object.setSubMajorHead(subMajorHeadName);
                object.setSubMajorHeadCode(subMajorHeadCode);
                object.setStatus(ApplicationConstants.ACTIVE);
                object.setCreateDate(System.currentTimeMillis() + "");
                object.setUpdateDate(System.currentTimeMillis() + "");
                object.setCreatedBy(userName);
                object.setUpdatedBy(userName);
                String objectJson = new Gson().toJson(object);
                String id = DBManager.getDbConnection().insert(ApplicationConstants.SUB_MAJORHEAD_TABLE, objectJson);
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
     * <code>ApplicationConstants.SUB_MAJORHEAD_TABLE</code> collection.Only
     * those document whose <code>status</code> field is <code>Active</code>
     * will appear in the list.
     *
     * @return list of documents in the JSON string.
     * @throws Exception
     */
    public String viewSubMajorHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SUB_MAJORHEAD_TABLE, conditionMap);
        return result;

    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.SUB_MAJORHEAD_TABLE</code> collection. If
     * document list already contains <code>subMajorHead</code> or
     * <code>subMajorHeadCode</code> field values, updation will not take place
     *
     * @param subMajorHeadName sub major head name
     * @param subMajorHeadCode sub major head code
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal string
     * @param userId <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection in hexadecimal string
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String updateSubMajorHead(String subMajorHeadName, String subMajorHeadCode, String primaryKey, String userId) throws Exception {
        String result = "";
        if (subMajorHeadName == null || subMajorHeadCode == null || userId == null) {
            result = null;
        } else if (!subMajorHeadName.equalsIgnoreCase(null) && !subMajorHeadCode.equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("subMajorHead", subMajorHeadName);
            map1.put("subMajorHeadCode", subMajorHeadCode);
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SUB_MAJORHEAD_TABLE, map, primaryKey) || Duplicate.isDuplicateforUpdate(ApplicationConstants.SUB_MAJORHEAD_TABLE, map1, primaryKey)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                String dbJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MAJORHEAD_TABLE, primaryKey);
                List<SubMajorHead> list = new Gson().fromJson(dbJson, new TypeToken<List<SubMajorHead>>() {
                }.getType());
                SubMajorHead dbObject = list.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setSubMajorHead(subMajorHeadName);
                dbObject.setSubMajorHeadCode(subMajorHeadCode);
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.SUB_MAJORHEAD_TABLE, primaryKey, dbObjectJson);
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
     * <code>ApplicationConstants.SUB_MAJORHEAD_TABLE</code> collection.If
     * <code>_id</code> value (in hexadecimal string) of document is already
     * saved in <code>subMajorHead</code> field of
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code>
     * collection,document will not get update.
     *
     * @param primaryKey <code>_id</code> value of document in the
     * <code>ApplicationConstants.MAJORHEAD_TABLE</code> collection in
     * hexadecimal String
     * @param userId  <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection in hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if either of document is null...
     */
    public String deleteSubMajorHead(String primaryKey, String userId) throws Exception {

        String status;
        if (primaryKey == null || userId == null) {
            return null;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, "subMajorHead", primaryKey))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MAJORHEAD_TABLE, primaryKey);
            List<SubMajorHead> list = new Gson().fromJson(existJson, new TypeToken<List<SubMajorHead>>() {
            }.getType());
            SubMajorHead dbObject = list.get(0);
            dbObject.setStatus(ApplicationConstants.DELETE);
            dbObject.setUpdateDate(System.currentTimeMillis() + "");
            dbObject.setDeletedBy(userName);
            String dbObjectJson = new Gson().toJson(dbObject);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SUB_MAJORHEAD_TABLE, primaryKey, dbObjectJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;

    }

}
