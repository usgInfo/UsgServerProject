/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.ProgramType;
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
public class ProgramTypeManager {

    /**
     * Inserts documents into
     * <code>ApplicationConstants.PROGRAM_TYPE_TABLE</code> collection.If
     * document list already contains <code>programType</code> field value
     * document will not save.
     *
     * @param programType program type name
     * @param loginUserId  <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String
     * @return  <code>_id</code> value of inserted document (in hexadecimal
     * String) if insertion is successful
     * @throws Exception if second argument is <code>null</code>..
     */
    public String saveProgramType(String programType, String loginUserId) throws Exception {

        if (loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        if (programType != null) {

            HashMap map = new HashMap();
            map.put("programType", programType);
            map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            if (Duplicate.hasDuplicateforSave(ApplicationConstants.PROGRAM_TYPE_TABLE, map)) {
                return ApplicationConstants.DUPLICATE_MESSAGE;
            }

        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        ProgramType programTypeObject = new ProgramType();
        programTypeObject.setCreateDate(System.currentTimeMillis() + "");
        programTypeObject.setUpdateDate(System.currentTimeMillis() + "");
        programTypeObject.setStatus(ApplicationConstants.ACTIVE);
        programTypeObject.setCreatedBy(userName);
        programTypeObject.setUpdatedBy(userName);
        programTypeObject.setProgramType(programType);

        String result = DBManager.getDbConnection().insert(ApplicationConstants.PROGRAM_TYPE_TABLE, new Gson().toJson(programTypeObject));

        return result;

    }

    /**
     * To get list of documents in the
     * <code>ApplicationConstants.PROGRAM_TYPE_TABLE</code> collection.Only
     * those documents <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list.
     *
     * @return list of documents in the
     * <code>ApplicationConstants.PROGRAM_TYPE_TABLE</code> collection in JSON
     * String.
     * @throws Exception
     */
    public String fetchAllProgramType() throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PROGRAM_TYPE_TABLE, map);
        return result;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.PROGRAM_TYPE_TABLE</code> collection.If
     * document list already contains <code>programType</code> field value
     * document will not update.
     *
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal String
     * @param loginUserId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String
     * @param programType <code>ProgramType</code> object data
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if either of argument is null or empty
     */
    public String updateProgramType(String primaryKey, String loginUserId, String programType) throws Exception {

        String result = "";

        if (primaryKey == null || primaryKey.isEmpty() || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        if (programType != null || !programType.isEmpty()) {

            HashMap map = new HashMap();
            map.put("programType", programType);
            map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.PROGRAM_TYPE_TABLE, map, primaryKey)) {
                return ApplicationConstants.DUPLICATE_MESSAGE;
            }

        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        String dbProgramTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.PROGRAM_TYPE_TABLE, primaryKey);

        if (dbProgramTypeJson == null) {
            return null;
        }

        List<ProgramType> programTypeList = new Gson().fromJson(dbProgramTypeJson, new TypeToken<List<ProgramType>>() {
        }.getType());

        ProgramType dbProgramType = programTypeList.get(0);

        dbProgramType.setUpdateDate(System.currentTimeMillis() + "");
        dbProgramType.setUpdatedBy(userName);
        dbProgramType.setStatus(ApplicationConstants.ACTIVE);
        dbProgramType.setProgramType(programType);

        Boolean status = DBManager.getDbConnection().update(ApplicationConstants.PROGRAM_TYPE_TABLE, primaryKey, new Gson().toJson(dbProgramType));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.PROGRAM_TYPE_TABLE</code>
     * collection.If _id value of selected document is already saved in
     * <code>programType</code> field of
     * <code>ApplicationConstants.PROGRAM_TABLE</code> collection updation will
     * not happen.
     *
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal String
     * @param loginUserId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     *
     * @throws Exception if either of argument is null or empty
     */
    public String deleteProgramType(String loginUserId, String primaryKey) throws Exception {
        String result = "";
        if (loginUserId == null || loginUserId.isEmpty() || primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }

        if (DeleteDependencyManager.hasDependency(ApplicationConstants.PROGRAM_TABLE, "programType", primaryKey)) {
            return ApplicationConstants.DELETE_MESSAGE;
        }
        User user = new UserManager().fetch(loginUserId);

        String userName = user.getFname() + "" + user.getLname();

        String programTypeStr = DBManager.getDbConnection().fetch(ApplicationConstants.PROGRAM_TYPE_TABLE, primaryKey);

        if (programTypeStr == null) {
            return null;
        }

        List<ProgramType> programTypeList = new Gson().fromJson(programTypeStr, new TypeToken<List<ProgramType>>() {
        }.getType());

        ProgramType programType = programTypeList.get(0);

        programType.setStatus(ApplicationConstants.DELETE);
        programType.setUpdateDate(System.currentTimeMillis() + "");
        programType.setUpdatedBy(userName);

        Boolean status = DBManager.getDbConnection().update(ApplicationConstants.PROGRAM_TYPE_TABLE, primaryKey, new Gson().toJson(programType));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

}
