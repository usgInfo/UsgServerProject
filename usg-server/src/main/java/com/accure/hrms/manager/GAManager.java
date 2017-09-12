/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.Gazetted;
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
 * @author accure
 */
public class GAManager {

    /**
     * Inserts document into
     * <code>ApplicationConstants.GAZETTED_NATURE_TABLE</code> collection.If
     * document list already contains <code>gazitted</code> field value
     * insertion will not happen
     *
     * @param value gazitted field value
     * @param loginUserId <code>_id</code> value of document(in hexadecimal
     * String) in the <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if second argument in null..
     */
    public String saveGa(String value, String loginUserId) throws Exception {

        String result;

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("gazitted", value);

        if (loginUserId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.GAZETTED_NATURE_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {

            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            Gazetted ga = new Gazetted();
            ga.setGazitted(value);
            ga.setStatus(ApplicationConstants.ACTIVE);
            ga.setCreateDate(System.currentTimeMillis() + "");
            ga.setCreatedBy(userName);
            ga.setUpdateDate(System.currentTimeMillis() + "");
            ga.setUpdatedBy(userName);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.GAZETTED_NATURE_TABLE, new Gson().toJson(ga));

            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    /**
     * Displays all the document in the
     * <code>ApplicationConstants.GAZETTED_NATURE_TABLE</code> collection.Only
     * those documents, whose <code>ApplicationConstants.STATUS</code> field
     * value is <code>ApplicationConstants.ACTIVE</code> are appear in the list.
     *
     * @return list of documents in JSON String
     * @throws Exception
     */
    public String fetchAll() throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String fundJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GAZETTED_NATURE_TABLE, conditionMap);
        return fundJson;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.GAZETTED_NATURE_TABLE</code> collection.If
     * document list already contains <code>gazitted</code> field value,
     * updation will not happen
     *
     * @param id <code>_id</code> value selected document in hexadecimal string
     * @param gazitted gazitted a value of <code>Gazetted</code> object
     * @param loginUserId  <code>_id</code> value of document(in hexadecimal
     * String) in the <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> on successful updation
     * @throws Exception if first or third argument is null..
     */
    public String update(String id, String gazitted, String loginUserId) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("gazitted", gazitted);

        if (loginUserId == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.GAZETTED_NATURE_TABLE, conditionMap, id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.GAZETTED_NATURE_TABLE, id);
            List<Gazetted> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Gazetted>>() {
            }.getType());
            Gazetted gaObj = gaList.get(0);
            gaObj.setGazitted(gazitted);
            gaObj.setStatus(ApplicationConstants.ACTIVE);
            gaObj.setUpdateDate(System.currentTimeMillis() + "");
            gaObj.setUpdatedBy(userName);
            String jsonStr = new Gson().toJson(gaObj);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.GAZETTED_NATURE_TABLE, id, jsonStr);
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
     * document.If <code>_id</code> value of selected document is already saved
     * in <code>gadNonGad</code> field value of document in
     * <code>ApplicationConstants.CLASS_TABLE</code> collection, updation will
     * not happen
     *
     * @param id <code>_id</code> value of selected document in hexadecimal
     * String
     * @param loginUserId <code>_id</code> value of document(in hexadecimal
     * String) in <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of document is null..
     */
    public String delete(String id, String loginUserId) throws Exception {

        String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.GAZETTED_NATURE_TABLE, id);
        List<Gazetted> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Gazetted>>() {
        }.getType());
        String status;
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Gazetted dbObj = gaList.get(0);
        Gazetted gaObj = new Gazetted();
        gaObj.setGazitted(dbObj.getGazitted());
        gaObj.setCreateDate(dbObj.getCreateDate());
        gaObj.setCreatedBy(dbObj.getCreatedBy());
        gaObj.setStatus(ApplicationConstants.DELETE);
        gaObj.setUpdatedBy(dbObj.getUpdatedBy());
        gaObj.setDeletedBy(userName);
        gaObj.setUpdateDate(System.currentTimeMillis() + "");
        String jsonStr = new Gson().toJson(gaObj);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.CLASS_TABLE, "gadNonGad", id)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {

            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.GAZETTED_NATURE_TABLE, id, jsonStr);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

}
