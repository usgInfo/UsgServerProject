/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.MaritalStatus;
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
 * @author upendra
 */
public class MaritalStatusManager {

    public String save(String marital, String loginUserId) throws Exception {
        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("maritalStatus", marital);
        if (loginUserId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.MARITAL_STATUS_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            MaritalStatus maritalobj = new MaritalStatus();
            maritalobj.setMaritalStatus(marital);
            maritalobj.setStatus(ApplicationConstants.ACTIVE);
            maritalobj.setCreateDate(System.currentTimeMillis() + "");
            maritalobj.setUpdateDate(System.currentTimeMillis() + "");
            maritalobj.setCreatedBy(userName);
            maritalobj.setUpdatedBy(userName);
            String maritaljson = new Gson().toJson(maritalobj);
            String id = DBManager.getDbConnection().insert(ApplicationConstants.MARITAL_STATUS_TABLE, maritaljson);
            if (id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    public String view() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MARITAL_STATUS_TABLE, conditionMap);
        return result;
    }


    public String update(String religion, String rid, String loginUserId) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("maritalStatus", religion);
        if (religion == null || rid == null || loginUserId == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.MARITAL_STATUS_TABLE, conditionMap, rid)) {
            result = ApplicationConstants.DUPLICATE;
        } else {

            String maritalJson = DBManager.getDbConnection().fetch(ApplicationConstants.MARITAL_STATUS_TABLE, rid);
            List<MaritalStatus> maritallist = new Gson().fromJson(maritalJson, new TypeToken<List<MaritalStatus>>() {
            }.getType());
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            MaritalStatus marital = maritallist.get(0);
            MaritalStatus maritalobj = new MaritalStatus();
            maritalobj.setMaritalStatus(religion);
            maritalobj.setCreateDate(marital.getCreateDate());
            maritalobj.setCreatedBy(marital.getCreatedBy());
            maritalobj.setStatus(ApplicationConstants.ACTIVE);
            maritalobj.setUpdatedBy(userName);
            maritalobj.setUpdateDate(System.currentTimeMillis() + "");
            String maritaljson = new Gson().toJson(maritalobj);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.MARITAL_STATUS_TABLE, rid, maritaljson);

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }
   

    public String delete(String rid, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        String existmaritalJson = DBManager.getDbConnection().fetch(ApplicationConstants.MARITAL_STATUS_TABLE, rid);
        List<MaritalStatus> maritalStatuslist = new Gson().fromJson(existmaritalJson, new TypeToken<List<MaritalStatus>>() {
        }.getType());
        String status;
        MaritalStatus maritalstatus = maritalStatuslist.get(0);
        MaritalStatus maritalobje = new MaritalStatus();
        maritalobje.setMaritalStatus(maritalstatus.getMaritalStatus());
        maritalobje.setCreateDate(maritalstatus.getCreateDate());
        maritalobje.setCreatedBy(maritalstatus.getCreatedBy());
        maritalobje.setStatus(ApplicationConstants.DELETE);
        maritalobje.setUpdatedBy(maritalstatus.getUpdatedBy());
        maritalobje.setUpdateDate(System.currentTimeMillis() + "");
        maritalobje.setDeletedBy(userName);
        String maritalJson = new Gson().toJson(maritalobje);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "maritalStatus", rid)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.MARITAL_STATUS_TABLE, rid, maritalJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

}
