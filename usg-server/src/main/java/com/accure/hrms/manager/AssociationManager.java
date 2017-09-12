/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Association;
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
public class AssociationManager {

    /**
     * Inserts document into <code>ApplicationConstants.ASSOCIATION_TABLE</code>
     * collection.if document list already contains <code>associationName</code>
     * value insertion will not happen
     *
     * @param association is <code>Association</code> Object data
     * @param userid <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string
     * @return
     * <p>
     * <code>_id</code> value of inserted document in hexadecimal string if
     * insertion is successful
     *
     * @throws java.lang.Exception if second argument is <code>null</code>...
     */
    public String save(Association association, String userid) throws Exception {

        if (userid == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("associationName", association.getAssociationName());
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.ASSOCIATION_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        association.setCreatedBy(userName);
        association.setStatus(ApplicationConstants.ACTIVE);
        association.setCreateDate(System.currentTimeMillis() + "");
        association.setUpdateDate(System.currentTimeMillis() + "");
        String associationJson = new Gson().toJson(association);
        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.ASSOCIATION_TABLE, associationJson);

        return primaryKey;
    }

    /**
     * Displays all the documents in the
     * <code>ApplicationConstants.ASSOCIATION_TABLE</code> collection.Only those
     * document whose <code>ApplicationConstants.STATUS</code> value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return List of document in JSON String .
     * @throws java.lang.Exception
     */
    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ASSOCIATION_TABLE, hMap);

        return result;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.ASSOCIATION_TABLE</code> collection.If
     * document list already contains <code>associationName</code> field value
     * updation will not happen
     *
     * @param association <code>Association</code> object data.
     * @param primaryKey <code>_id</code> value of selected document in the
     * hexadecimal string
     * @param userid <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string
     * @return <code> ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is null
     */
    public String update(Association association, String primaryKey, String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<Association>() {
        }.getType();

        String dbObj = new AssociationManager().fetch(primaryKey);
        if (dbObj == null) {
            return ApplicationConstants.FAIL;
        }
        String result;
        Association dbObjJson = new Gson().fromJson(dbObj, type);
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("associationName", association.getAssociationName());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.ASSOCIATION_TABLE, duplicateCheckMap, primaryKey)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        } else {
            dbObjJson.setUpdatedBy(userName);
            dbObjJson.setUpdateDate(System.currentTimeMillis() + "");
            dbObjJson.setAssociationName(association.getAssociationName());
            dbObjJson.setFees(association.getFees());
            dbObjJson.setIsPension(association.getIsPension());
            dbObjJson.setRemarks(association.getRemarks());
            String assJson = new Gson().toJson(dbObjJson);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.ASSOCIATION_TABLE, primaryKey, assJson);

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;

    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.ASSOCIATION_TABLE</code>
     * collection.If <code>_id</code> value of selected document already saved
     * in <code>association</code> field of
     * <code>ApplicationConstants.EMPLOYEE_TABLE</code> or
     * <code>ApplicationConstants.EMPLOYEE_PROMOTION_TABLE</code>
     * collection,updation will not happen
     *
     * @param primaryKey <code>_id</code> value of selected document in the
     * hexadecimal string
     * @param userid <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string
     * @return <code> ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is null
     */
    public String delete(String primaryKey, String userid) throws Exception {
        String status;
        if (userid == null || userid.isEmpty()) {
            return ApplicationConstants.FAIL;
        }
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        if (primaryKey == null || primaryKey.isEmpty()) {
            return ApplicationConstants.FAIL;
        }
        Type type = new TypeToken<Association>() {
        }.getType();
        String ass = new AssociationManager().fetch(primaryKey);
        if (ass == null || ass.isEmpty()) {
            return ApplicationConstants.FAIL;
        }
        Association assJson = new Gson().fromJson(ass, type);
        assJson.setDeletedBy(userName);
        assJson.setStatus(ApplicationConstants.DELETE);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "association", primaryKey) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, "association", primaryKey)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.ASSOCIATION_TABLE, primaryKey, new Gson().toJson(assJson));
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;

    }

    /**
     * Search the document in the
     * <code>ApplicationConstants.ASSOCIATION_TABLE</code> collection using
     * <code>_id</code> value of document
     *
     * @param primaryKey <code>_id</code> value of document in hexadecimal
     * String
     * @return searched document in the JSOn String
     * @throws Exception if argument is null
     */
    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.ASSOCIATION_TABLE, primaryKey);
        List<Association> list = new Gson().fromJson(result, new TypeToken<List<Association>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }

}
