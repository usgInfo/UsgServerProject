/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.Nature;
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
public class NatureManager {

    /**
     * Inserts documents into <code>ApplicationConstants.NATURE_TABLE</code>
     * collection.If document list already contains  <code>natureName</code>
     * field value ,insertion will not happen
     *
     * @param natureName <code>natureName</code> field value of
     * <code>Nature</code> object
     * @param loginUserId <code>_id</code> value of document (in hexadecimal
     * String) in the <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception If second argument is null..
     */
    public String save(String natureName, String loginUserId) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("natureName", natureName);
        if (loginUserId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.NATURE_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            if (natureName == null || natureName.isEmpty()) {
                return null;
            }
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            Nature nature = new Nature();
            nature.setNatureName(natureName);
            nature.setStatus(ApplicationConstants.ACTIVE);
            nature.setCreateDate(System.currentTimeMillis() + "");
            nature.setCreatedBy(userName);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.NATURE_TABLE, new Gson().toJson(nature));

            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    /**
     * Displays all the documents in the
     * <code>ApplicationConstants.NATURE_TABLE</code> collection.Only those
     * documents,whose <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return list of document in the JSOn String .
     * @throws Exception
     */
    public String fetchAll() throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String natureResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NATURE_TABLE, conditionMap);

        return natureResult;
    }

    /**
     * Updates selected document in
     * <code>ApplicationConstants.NATURE_TABLE</code> collection.If document
     * list already contains  <code>natureName</code> field value ,updation will
     * not happen
     *
     * @param id <code>_id</code> value of selected document in hexadecimal
     * string.
     * @param name <code>natureName</code> field value of <code>Nature</code>
     * object
     * @param loginUserId <code>_id</code> value of document (in hexadecimal
     * String) in the <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception If first or third argument is null..
     */
    public String update(String id, String name, String loginUserId) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("natureName", name);
        if (loginUserId == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.NATURE_TABLE, conditionMap, id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            String natureJson = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, id);
            List<Nature> naturelist = new Gson().fromJson(natureJson, new TypeToken<List<Nature>>() {
            }.getType());
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            Nature nature = naturelist.get(0);
            nature.setNatureName(name);
            nature.setUpdatedBy(userName);
            nature.setStatus(ApplicationConstants.ACTIVE);
            nature.setUpdateDate(System.currentTimeMillis() + "");
            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.NATURE_TABLE, id, new Gson().toJson(nature));
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    /**
     *
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.NATURE_TABLE</code>
     * collection.If <code>_id</code> value of selected document already saved
     * in <code>natureType</code> field value of
     * <code>ApplicationConstants.EMPLOYEE_TABLE</code> or
     * <code>ApplicationConstants.HEAD_SLAB_TABLE</code> or
     * <code>ApplicationConstants.AUTO_SALARY_PROCESS_TABLE</code> or
     * <code>ApplicationConstants.EMP_ATTENDANCE_TABLE</code> or
     * <code>ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE</code> or
     * <code>ApplicationConstants.LEAVE_TYPE_MASTER</code> collection ,updation
     * will not happen
     *
     * @param id <code>_id</code> value of selected document in hexadecimal
     * string.
     *
     * @param loginUserId <code>_id</code> value of document (in hexadecimal
     * String) in the <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception If either of argument is null..
     */
    public String delete(String id, String loginUserId) throws Exception {

        String natureJson = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, id);
        List<Nature> naturelist = new Gson().fromJson(natureJson, new TypeToken<List<Nature>>() {
        }.getType());
        String status;
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Nature dbObj = naturelist.get(0);
        Nature nature = new Nature();
        nature.setNatureName(dbObj.getNatureName());
        nature.setCreateDate(dbObj.getCreateDate());
        nature.setCreatedBy(dbObj.getCreatedBy());
        nature.setUpdatedBy(dbObj.getUpdatedBy());
        nature.setDeletedBy(userName);
        nature.setStatus(ApplicationConstants.DELETE);
        nature.setUpdateDate(System.currentTimeMillis() + "");
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "natureType", id)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.EMP_ATTENDANCE_TABLE, "natureType", id))
                || (DeleteDependencyManager.hasDependency(ApplicationConstants.HEAD_SLAB_TABLE, "natureType", id)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.INCOMETAX_TABLE, "natureType", id))
                || (DeleteDependencyManager.hasDependency(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, "natureType", id)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, "natureType", id)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.LEAVE_TYPE_MASTER, "leaveTypeDetails.natureType", id))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.NATURE_TABLE, id, new Gson().toJson(nature));
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

}
