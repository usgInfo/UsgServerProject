/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Section;
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
public class SectionManager {

    /**
     * This method is to insert <code>Section</code> Object data to
     * <code>ApplicationConstants.SECTION_TABLE</code>.
     *
     * @param section is Section Object data.
     * @param loginUserId is <code>User</code> Object <code>_id</code> value in
     * hexadecimal string
     * @return
     * <p>
     *
     * <code>_id</code> value of the <code>Section</code> Object in hexadecimal
     * string on successful insertion of Section Object data or</p><p>
     * <code> ApplicationConstants.FAIL</code> if anyone of the parameters in
     * the save method is null or empty or</p>
     * <p>
     * <code> ApplicationConstants.DUPLICATE_MESSAGE</code> if Section list
     * already contains <code>description</code> value of Section Object</p>
     * @throws java.lang.Exception if either of the argument is
     * <code>null</code>..
     */
    public String save(Section section, String loginUserId) throws Exception {

        if (loginUserId == null || loginUserId.isEmpty()) {
            return ApplicationConstants.FAIL;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("description", section.getDescription());
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.SECTION_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        section.setStatus(ApplicationConstants.ACTIVE);
        section.setCreateDate(System.currentTimeMillis() + "");
        section.setUpdateDate(System.currentTimeMillis() + "");
        section.setCreatedBy(userName);
        section.setUpdatedBy(userName);
        String disciplinejson = new Gson().toJson(section);

        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.SECTION_TABLE, disciplinejson);

        return primaryKey;
    }

    /**
     * This method is to get List of <code>Section</code> Object data.
     *
     * @return
     * <p>
     * List of Section Object in JSON String format.</p>
     * @throws java.lang.Exception
     */
    public String view() throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SECTION_TABLE, conditionMap);
        return result;

    }

    /**
     * This method is to update the <code>Section</code> Object data.
     *
     * @param description is <code>Section</code> Object
     * <code>description</code> field value.
     * @param primaryKey is <code>_id</code> of <code>Section</code> Object in
     * hexadecimal string.
     * @param userId is <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string.
     * @return
     * <p>
     * <code>ApplicationConstants.SUCCESS</code> on Successful updation of
     * Section Object or</p>
     * <p>
     * <code>ApplicationConstants.FAIL</code> if anyone of the parameters in the
     * update method is null or empty or</p>
     * <p>
     * <code>ApplicationConstants.DUPLICATE_MESSAGE</code> if Section list
     * already contains Section description</p>
     * @throws java.lang.Exception if either of the argument is
     * <code>null</code>..
     */
    public String update(String description, String primaryKey, String userId) throws Exception {

        if (primaryKey == null || userId == null) {
            return ApplicationConstants.FAIL;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("description", description);
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SECTION_TABLE, duplicateCheckMap, primaryKey)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String status;

        String sectionJson = DBManager.getDbConnection().fetch(ApplicationConstants.SECTION_TABLE, primaryKey);
        List<Section> list = new Gson().fromJson(sectionJson, new TypeToken<List<Section>>() {
        }.getType());
        Section sec = list.get(0);
        sec.setDescription(description);
        sec.setStatus(ApplicationConstants.ACTIVE);
        sec.setUpdateDate(System.currentTimeMillis() + "");
        sec.setUpdatedBy(userName);
        String json = new Gson().toJson(sec);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SECTION_TABLE, primaryKey, json);
        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;

    }

    /**
     * This method is to update status field of <code>Section</code> Object.
     *
     * @param primaryKey is <code>_id</code> of <code>Section</code> Object in
     * hexadecimal string.
     * @param userid is <code>_id</code> of <code>User</code> Object in
     * hexadecimal string.
     * @return
     * <p>
     * <code>ApplicationConstants.SUCCESS</code> if Section Object updation is
     * successful or</p><p>
     * <code>ApplicationConstants.FAIL</code> if anyone of the parameters in the
     * delete method is null or empty or</p><p>
     * <code> ApplicationConstants.DELETE_MESSAGE</code> if <code>_id</code>
     * value in hexadecimal string of <code>Section</code> Object is saved in
     * <code>sectionPart</code> field of
     * <code>ApplicationConstants.SALARY_HEAD_TABLE</code></p>
     * @throws java.lang.Exception if either of the argument is
     * <code>null</code>..
     */
    public String delete(String primaryKey, String userid) throws Exception {

        if (primaryKey == null || userid == null) {
            return ApplicationConstants.FAIL;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.SECTION_TABLE, primaryKey);
        List<Section> list = new Gson().fromJson(existJson, new TypeToken<List<Section>>() {
        }.getType());

        Section section = list.get(0);
        Section sectionobje = new Section();
        sectionobje.setDescription(section.getDescription());
        sectionobje.setCreateDate(section.getCreateDate());
        sectionobje.setStatus(ApplicationConstants.DELETE);
        sectionobje.setUpdateDate(System.currentTimeMillis() + "");
        sectionobje.setDeletedBy(userName);
        String sectionJson = new Gson().toJson(sectionobje);
        String status;
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.SALARY_HEAD_TABLE, "sectionPart", primaryKey)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SECTION_TABLE, primaryKey, sectionJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    /**
     * This method is to search the <code>Section</code> Object data by using
     * <code>_id</code> value .
     *
     * @param primaryKey is <code>_id</code> value of <code>Section</code>
     * Object in hexadecimal string.
     * @return
     * <p>
     * <code>Section</code> Object data in the JSON String format.</P>
     * @throws java.lang.Exception if either of the argument is
     * <code>null</code>...
     */
    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SECTION_TABLE, primaryKey);
        List<Section> sectionList = new Gson().fromJson(result, new TypeToken<List<Section>>() {
        }.getType());
        if (sectionList == null || sectionList.size() < 1) {
            return null;
        }
        return new Gson().toJson(sectionList.get(0));
    }

}
