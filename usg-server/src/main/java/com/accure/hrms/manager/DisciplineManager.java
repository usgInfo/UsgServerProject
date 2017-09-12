/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Discipline;
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
 * @author Shwetha T S
 */
public class DisciplineManager {

    /**
     * This method is to Insert <code>Discipline</code> Object data to
     * <code>ApplicationConstants.DISCIPLINE_TABLE</code>.
     *
     * @param discipline is <code>disciplineName</code> field value of
     * <code>Discipline</code> Object
     * @param userid is <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string.
     * @return
     * <p>
     * <code><code>_id</code>value of the <code>Discipline</code> Object</code>
     * in hexadecimal string if insertion of Discipline Object is successful
     * or</p>
     * <p>
     * <code>ApplicationConstants.FAIL</code> if any one of the parameters in
     * the save method is null or empty or</p>
     * <p>
     * <code> ApplicationConstants.DUPLICATE_MESSAGE </code>if
     * <code>Discipline</code> Object list already contains
     * <code>disciplineName</code> field value. Object</p>
     * @throws java.lang.Exception If either of argument is <code>null</code>..
     */
    public String save(String discipline, String userid) throws Exception {

        if (discipline == null) {
            return ApplicationConstants.FAIL;
        }
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("disciplineName", discipline);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.DISCIPLINE_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }
        Discipline disciplineobj = new Discipline();
        disciplineobj.setDisciplineName(discipline);
        disciplineobj.setStatus(ApplicationConstants.ACTIVE);
        disciplineobj.setCreateDate(System.currentTimeMillis() + "");
        disciplineobj.setUpdateDate(System.currentTimeMillis() + "");
        disciplineobj.setCreatedBy(userName);
        disciplineobj.setUpdatedBy(userName);
        String disciplinejson = new Gson().toJson(disciplineobj);
        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.DISCIPLINE_TABLE, disciplinejson);

        return primaryKey;
    }

    /**
     * This method is to get List of <code>Discipline</code> Object
     *
     *
     * @return
     * <p>
     * List of <code>Discipline</code> Object in JSON String format.</p>
     * @throws java.lang.Exception
     */
    public String view() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DISCIPLINE_TABLE, conditionMap);
        return result;

    }

    /**
     * This method is to update the <code>Discipline</code> Object by using
     * <code>_id</code> value.
     *
     * @param discipline is <code>disciplineName</code> field value of
     * <code>Discipline</code> Object.
     * @param primaryKey is <code>_id</code> value of the
     * <code>Discipline</code> Object in hexadecimal string.
     * @param userid is <code>_id</code> value of the <code>User</code> Object
     * in hexadecimal string.
     * @return String
     * <p>
     * <code>ApplicationConstants.SUCCESS</code> if record is updated
     * successfully</p><p>
     * <code> ApplicationConstants.FAIL</code> if anyone of the parameters in
     * the update method is null or empty</p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE_MESSAGE</code> if Discipline list
     * already contains <code>disciplineName</code> value</p>
     * @throws java.lang.Exception if either of argument is
     * <code>null</code>....
     */
    public String update(String discipline, String primaryKey, String userid) throws Exception {
        String status;
        if (primaryKey == null || userid == null) {
            return ApplicationConstants.FAIL;
        }
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("disciplineName", discipline);
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.DISCIPLINE_TABLE, duplicateCheckMap, primaryKey)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        } else {
            String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, primaryKey);
            List<Discipline> list = new Gson().fromJson(jsonStr, new TypeToken<List<Discipline>>() {
            }.getType());
            Discipline dbObj = list.get(0);
            Discipline disc = new Discipline();
            disc.setDisciplineName(discipline);
            disc.setCreateDate(dbObj.getCreateDate());
            disc.setCreatedBy(dbObj.getCreatedBy());
            disc.setStatus(ApplicationConstants.ACTIVE);
            disc.setUpdateDate(System.currentTimeMillis() + "");
            disc.setUpdatedBy(userName);
            String json = new Gson().toJson(disc);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DISCIPLINE_TABLE, primaryKey, json);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    /**
     * This method is used to update status field of an <code>Discipline</code>
     * Object.
     *
     * @param primaryKey is <code>_id</code> value of <code>Discipline</code>
     * Object in hexadecimal string.
     * @param userid is <code>_id</code>value of <code>User</code> Object in
     * hexadecimal string.
     * @return
     * <p>
     * <code>ApplicationConstants.SUCCESS</code> on successful updation of
     * <code>Discipline</code> Object or</p><p>
     * <code> ApplicationConstants.FAIL</code> if anyone of the parameters in
     * the delete method is null or empty or</p><p>
     * <code> ApplicationConstants.DELETE_MESSAGE</code> if primary key value of
     * <code>Discipline</code> Object is saved in <code>disciplineName</code>
     * field of <code>ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE</code> or
     * <code>discipline</code> field of
     * <code>ApplicationConstants.EMPLOYEE_PROMOTION_TABLE</code></p>
     * @throws java.lang.Exception if either of the argument is
     * <code>null</code>..
     */
    public String delete(String primaryKey, String userid) throws Exception {

        if (primaryKey == null || userid == null) {
            return null;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, primaryKey);
        List<Discipline> list = new Gson().fromJson(existJson, new TypeToken<List<Discipline>>() {
        }.getType());
        Discipline disc = list.get(0);

        disc.setStatus(ApplicationConstants.DELETE);
        disc.setUpdateDate(System.currentTimeMillis() + "");
        disc.setDeletedBy(userName);
        String status;
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, "disciplineName", primaryKey) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, "discipline", primaryKey)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            String jsonStr = new Gson().toJson(disc);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DISCIPLINE_TABLE, primaryKey, jsonStr);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    /**
     * This method is used to search the Discipline Object data by using
     * <code>_id</code> value.
     *
     * @param primaryKey is <code>_id</code> value of the
     * <code>Discipline</code> Object in hexadecimal string.
     * @return
     * <p>
     * <code>Discipline</code> Object data in the JSON String format.</P>
     * @throws java.lang.Exception if either of the argument is
     * <code>null</code>.
     */
    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, primaryKey);
        List<Discipline> disciplineList = new Gson().fromJson(result, new TypeToken<List<Discipline>>() {
        }.getType());
        if (disciplineList == null || disciplineList.size() < 1) {
            return null;
        }
        return new Gson().toJson(disciplineList.get(0));
    }

}
