/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.Period;
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
public class PeriodManager {

    /**
     * Inserts documents into <code>ApplicationConstants.PERIOD_TABLE</code>
     * collection.If document list already contains <code>periodName</code>
     * field value document will not save
     *
     * @param loginUserId <code>_id</code> value of User object in hexadecimal
     * string.
     * @param period <code>Period</code> object data.
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if first argument is null...
     */
    public String savePeriod(String loginUserId, Period period) throws Exception {

        if (loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        HashMap map = new HashMap();
        map.put("periodName", period.getPeriodName());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.PERIOD_TABLE, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        period.setStatus(ApplicationConstants.ACTIVE);
        period.setCreateDate(System.currentTimeMillis() + "");
        period.setCreatedBy(userName);
        period.setUpdateDate(System.currentTimeMillis() + "");
        period.setUpdatedBy(userName);

        String result = DBManager.getDbConnection().insert(ApplicationConstants.PERIOD_TABLE, new Gson().toJson(period));

        return result;
    }

    /**
     * Get all documents list in the
     * <code>ApplicationConstants.PERIOD_TABLE</code> collection.Only those
     * documents <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.Active</code> are appear in the list
     *
     * @return list of documents in JSON String
     * @throws Exception
     */
    public String fetchAllPeriod() throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PERIOD_TABLE, map);
        return result;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.PERIOD_TABLE</code> collection.If document
     * list already contains <code>periodName</code> value document will not
     * update
     *
     * @param loginUserId _id value User object in hexadecimal string
     * @param primaryKey _id value of selected document in hexadecimal string
     * @param period <code>Period</code> object data
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if first or second argument is null or empty
     */
    public String updatePeriod(String loginUserId, String primaryKey, Period period) throws Exception {
        String result = "";
        if (loginUserId == null || loginUserId.isEmpty() || primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("periodName", period.getPeriodName());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.PERIOD_TABLE, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        String periodStr = DBManager.getDbConnection().fetch(ApplicationConstants.PERIOD_TABLE, primaryKey);

        if (periodStr == null) {
            return null;
        }

        List<Period> list = new Gson().fromJson(periodStr, new TypeToken<List<Period>>() {
        }.getType());

        Period dbPeriodData = list.get(0);

        period.setStatus(ApplicationConstants.ACTIVE);
        period.setUpdateDate(System.currentTimeMillis() + "");
        period.setUpdatedBy(userName);
        period.setCreateDate(dbPeriodData.getCreateDate());
        period.setCreatedBy(dbPeriodData.getCreatedBy());

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PERIOD_TABLE, primaryKey, new Gson().toJson(period));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.PERIOD_TABLE</code>
     * collection.
     *
     * @param loginUserId _id value User object in hexadecimal string
     * @param primaryKey _id value of selected document in hexadecimal string
     *
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is null or empty
     */
    public String deletePeriod(String primaryKey, String loginUserId) throws Exception {

        if (primaryKey == null || primaryKey.isEmpty() || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        String periodStr = DBManager.getDbConnection().fetch(ApplicationConstants.PERIOD_TABLE, primaryKey);

        if (periodStr == null) {
            return null;
        }

        List<Period> list = new Gson().fromJson(periodStr, new TypeToken<List<Period>>() {
        }.getType());

        Period dbPeriodData = list.get(0);

        dbPeriodData.setStatus(ApplicationConstants.DELETE);
        dbPeriodData.setUpdateDate(System.currentTimeMillis() + "");
        dbPeriodData.setUpdatedBy(userName);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PERIOD_TABLE, primaryKey, new Gson().toJson(dbPeriodData));

        String result = "";

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

}
