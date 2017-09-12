/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.PFType;
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
public class PFTypeManager {

    /**
     * Inserts document to <code>ApplicationConstants.PF_TYPE_MASTER</code>
     * collection.If document list already contains <code>PFType</code> field
     * value ,insertion will not happen
     *
     * @param pfType
     * @param loginUserId <code>_id</code> value of document(in JSON string) in
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if second argument is <code>null</code>..
     */
    public String save(PFType pfType, String loginUserId) throws Exception {
        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("PFType", pfType.getPFType());
        if (loginUserId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.PF_TYPE_MASTER, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            pfType.setCreateDate(System.currentTimeMillis() + "");
            pfType.setUpdateDate(System.currentTimeMillis() + "");
            pfType.setStatus(ApplicationConstants.ACTIVE);
            pfType.setCreatedBy(userName);
            String objJson = new Gson().toJson(pfType);
            String Id = DBManager.getDbConnection().insert(ApplicationConstants.PF_TYPE_MASTER, objJson);
            if (Id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    /**
     * Search the particular document in the
     * <code>ApplicationConstants.PF_TYPE_MASTER</code> using <code>_id</code>
     * value
     *
     * @param Id <code>_id</code> value of document(in hexadecimal String>
     * @return document in the <code>ApplicationConstants.PF_TYPE_MASTER</code>
     * collection in JSON String .
     * @throws Exception if argument is <code>null</code>..
     */
    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PF_TYPE_MASTER, Id);
        List<PFType> objList = new Gson().fromJson(result, new TypeToken<List<PFType>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    /**
     * Search the particular document in the
     * <code>ApplicationConstants.PF_TYPE_MASTER</code> using <code>_id</code>
     * value
     *
     * @param Id <code>_id</code> value of document(in hexadecimal String>
     * @return document in the <code>ApplicationConstants.PF_TYPE_MASTER</code>
     * collection
     * @throws Exception if argument is <code>null</code>..
     */
    public PFType get(String Id) throws Exception {

        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PF_TYPE_MASTER, Id);

        if (result == null) {
            return null;
        }
        List<PFType> list = new Gson().fromJson(result, new TypeToken<List<PFType>>() {
        }.getType());

        return list.get(0);
    }

    /**
     * Updates status field of selected document in the
     * <code>ApplicationConstants.PF_TYPE_MASTER</code> collection
     *
     * @param Id <code>_id</code> value of selected document in hexadecimal
     * String
     * @param loginUserId <code>_id</code> value of a document(in hexadecimal
     * String) in the <code>ApplicationConstants.USER</code> collection
     * @return <code>True</code> if updation is successful
     * @throws Exception if either of argument is <code>null</code> ..
     */
    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<PFType>() {
        }.getType();
        String obj = new PFTypeManager().fetch(Id);
        if (obj == null || obj.isEmpty()) {
            return false;
        }
        PFType pfType = new Gson().fromJson(obj, type);
        pfType.setStatus(ApplicationConstants.DELETE);
        pfType.setDeletedBy(userName);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PF_TYPE_MASTER, Id, new Gson().toJson(pfType));
        return result;
    }

    /**
     * Updates selected document of
     * <code>ApplicationConstants.PF_TYPE_MASTER</code> collection.If document
     * list already contains <code>PFType</code> field value, updation will not
     * happen.
     *
     * @param pfType <code>PFType</code> object data.
     * @param Id <code>_id</code> value of selected document in hexadecimal
     * String.
     * @param loginUserId <code>_id</code> value of a document (in JSON String)
     * in the <code>ApplicationConstants.PF_TYPE_MASTER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is <code>null</code>..
     */
    public String update(PFType pfType, String Id, String loginUserId) throws Exception {
        String result;

        HashMap<String, String> conditionMap = new HashMap<String, String>();

        conditionMap.put("PFType", pfType.getPFType());

        if (Id == null || loginUserId == null) {

            result = null;

        } else if (isDuplicateforUpdate(ApplicationConstants.PF_TYPE_MASTER, conditionMap, Id)) {

            result = ApplicationConstants.DUPLICATE;

        } else {

            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            Type type = new TypeToken<PFType>() {
            }.getType();

            String dbObjStr = new PFTypeManager().fetch(Id);

            if (dbObjStr == null || dbObjStr.isEmpty()) {
                return null;
            }

            PFType dbObj = new Gson().fromJson(dbObjStr, type);
            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setStatus(ApplicationConstants.ACTIVE);
            dbObj.setUpdatedBy(userName);
            dbObj.setPFType(pfType.getPFType());
            String jsonStr = new Gson().toJson(dbObj);

            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.PF_TYPE_MASTER, Id, jsonStr);

            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    /**
     * Displays all the documents in the
     * <code>ApplicationConstants.PF_TYPE_MASTER</code> collection.Only those
     * documents, whose <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return list of documents in
     * <code>ApplicationConstants.PF_TYPE_MASTER</code> collection in JSON
     * String format
     * @throws Exception
     */
    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PF_TYPE_MASTER, conditionMap);
        return result;
    }

}
