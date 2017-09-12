/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.PartyMaster;
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
public class PartyMasterManager {

    /**
     * Inserts documents into <code>ApplicationConstants.PARTY_MASTER</code>
     * collection.If document list already contains <code>partyName</code> field
     * value insertion will not happen
     *
     * @param partyMaster <code>PartyMaster</code> object data
     * @param userId <code>_id</code> value of a document (in hexadecimal
     * String) in <code>ApplicationConstants.USER</code> collection.
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful.
     * @throws Exception if second argument is null
     */
    public String save(PartyMaster partyMaster, String userId) throws Exception {

        String result;

        if (userId == null) {
            return null;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("partyName", partyMaster.getPartyName());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.PARTY_MASTER, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        partyMaster.setStatus(ApplicationConstants.ACTIVE);
        partyMaster.setCreateDate(System.currentTimeMillis() + "");
        partyMaster.setUpdateDate(System.currentTimeMillis() + "");
        partyMaster.setCreatedBy(userName);
        partyMaster.setUpdatedBy(userName);

        String jsonStr = new Gson().toJson(partyMaster);

        result = DBManager.getDbConnection().insert(ApplicationConstants.PARTY_MASTER, jsonStr);

        if (result != null) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

    /**
     * Displays all document in <code>ApplicationConstants.PARTY_MASTER</code>
     * collection.Only those document whose
     * <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list.
     *
     * @return list of document in JSOn String.
     * @throws Exception
     */
    public String view() throws Exception {

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PARTY_MASTER, hMap);
        return result;
    }

    public String update(PartyMaster partyMaster, String userId, String id) throws Exception {
        String result;
        if (userId == null || id == null) {
            return null;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("partyName", partyMaster.getPartyName());

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.PARTY_MASTER, map, id)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + "" + user.getLname();

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.PARTY_MASTER, id);

        if (dbStr == null) {
            return null;
        }
        List<PartyMaster> list = new Gson().fromJson(dbStr, new TypeToken<List<PartyMaster>>() {
        }.getType());
        PartyMaster dbObj = list.get(0);

        partyMaster.setStatus(ApplicationConstants.ACTIVE);
        partyMaster.setCreateDate(dbObj.getCreateDate());
        partyMaster.setCreatedBy(dbObj.getCreatedBy());
        partyMaster.setUpdateDate(System.currentTimeMillis() + "");
        partyMaster.setUpdatedBy(userName);

        String json = new Gson().toJson(partyMaster);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PARTY_MASTER, id, json);
        if (status) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.PARTY_MASTER</code>
     * collection.
     *
     * @param id <code>_id</code> value of selected document in hexadecimal
     * String.
     * @param userid <code>_id</code> value of a document (in hexadecimal
     * String) in <code>ApplicationConstants.USER</code> collection.
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is null..
     */
    public String delete(String id, String userid) throws Exception {

        if (id == null || userid == null) {
            return null;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String partyJson = DBManager.getDbConnection().fetch(ApplicationConstants.PARTY_MASTER, id);
        if (partyJson == null) {
            return null;
        }
        List<PartyMaster> list = new Gson().fromJson(partyJson, new TypeToken<List<PartyMaster>>() {
        }.getType());
        PartyMaster partyMaster = list.get(0);
        partyMaster.setStatus(ApplicationConstants.DELETE);
        partyMaster.setUpdateDate(System.currentTimeMillis() + "");
        partyMaster.setUpdatedBy(userName);
        String status;

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PARTY_MASTER, id, new Gson().toJson(partyMaster));
        if (result) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;

    }

}
