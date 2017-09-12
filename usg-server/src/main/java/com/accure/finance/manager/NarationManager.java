/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.user.manager.UserManager;
import com.accure.db.in.DAO;
import com.accure.finance.dto.Naration;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author deepak2310
 */
public class NarationManager {

    public String createNaration(Naration naration, String UserId) throws Exception {
        String result = "";

        if (naration.getNarationType() == null || UserId == null) {
            result = null;

        } else {
            User user = new UserManager().fetch(UserId);
            String userName = user.getFname() + " " + user.getLname();

            naration.setCreateDate(System.currentTimeMillis() + "");
            naration.setUpdateDate(System.currentTimeMillis() + "");
            naration.setStatus(ApplicationConstants.ACTIVE);
            naration.setCreatedBy(userName);

            String narationJson = new Gson().toJson(naration);

            String narationId = DBManager.getDbConnection().insert(ApplicationConstants.NARATION_TABLE, narationJson);
            if (narationId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;

    }

    public List<Naration> fetchAllNaration() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String narationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.NARATION_TABLE, conditionMap);
        List<Naration> narationList = new Gson().fromJson(narationJson, new TypeToken<List<Naration>>() {
        }.getType());
        return narationList;
    }

    public Naration fetch(String narationId) throws Exception {
        if (narationId == null || narationId.isEmpty()) {
            return null;
        }
        String narationJson = DBManager.getDbConnection().fetch(ApplicationConstants.NARATION_TABLE, narationId);
        if (narationJson == null || narationJson.isEmpty()) {
            return null;
        }
        List<Naration> narationList = new Gson().fromJson(narationJson, new TypeToken<List<Naration>>() {
        }.getType());
        if (narationList == null || narationList.isEmpty()) {
            return null;
        }
        return narationList.get(0);

    }

    public String updateNaration(Naration naration, String userId, String narationId) throws Exception {

        String result = "";
        if (naration.getNarationType() == null || userId == null) {
            result = null;
        } else {

            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            Naration narationDb = fetch(narationId);
            if (narationDb.getNarationDetail() != null || !narationDb.getNarationDetail().isEmpty()) {
                narationDb.setNarationDetail(naration.getNarationDetail());
            }
            if (narationDb.getNarationType() != null || !narationDb.getNarationType().isEmpty()) {
                narationDb.setNarationType(naration.getNarationType());
            }
            narationDb.setUpdateDate(System.currentTimeMillis() + "");
            narationDb.setUpdatedBy(userName);
            narationDb.setStatus(ApplicationConstants.ACTIVE);

            String narationJson = new Gson().toJson(narationDb);

            boolean status = DBManager.getDbConnection().update(ApplicationConstants.NARATION_TABLE, narationId, narationJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;

    }

    public boolean deleteNaration(String narationId, String currentUserLogin) throws Exception {

        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        Naration naration = fetch(narationId);
        naration.setStatus(ApplicationConstants.DELETE);
        naration.setUpdateDate(System.currentTimeMillis() + "");
        naration.setUpdatedBy(userName);

        String narationJson = new Gson().toJson(naration);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.NARATION_TABLE, narationId, narationJson);
        if (status) {
            return true;
        }
        return false;
    }

}
