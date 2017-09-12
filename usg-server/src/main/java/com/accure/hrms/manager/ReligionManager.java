/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.Religion;
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
public class ReligionManager {

    public String save(String religion, String userID) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("religion", religion);
        if (religion == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.RELIGION_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userID);
            String userName = user.getFname() + " " + user.getLname();
            Religion religionobj = new Religion();
            religionobj.setReligion(religion);
            religionobj.setStatus(ApplicationConstants.ACTIVE);
            religionobj.setCreatedBy(userName);
            religionobj.setCreateDate(System.currentTimeMillis() + "");
            String religionjson = new Gson().toJson(religionobj);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.RELIGION_TABLE, religionjson);
            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;

    }

    public String viewList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.RELIGION_TABLE, conditionMap);
        return result;

    }

    public String update(String religion, String rid, String userID) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("religion", religion);

        if (rid == null || userID == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.RELIGION_TABLE, conditionMap,rid)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userID);
            String userName = user.getFname() + " " + user.getLname();
            String religionJson = DBManager.getDbConnection().fetch(ApplicationConstants.RELIGION_TABLE, rid);
            List<Religion> religionlist = new Gson().fromJson(religionJson, new TypeToken<List<Religion>>() {
            }.getType());
            Religion religionobj = religionlist.get(0);
            religionobj.setReligion(religion);
            religionobj.setStatus(ApplicationConstants.ACTIVE);
            religionobj.setUpdatedBy(userName);
            religionobj.setUpdateDate(System.currentTimeMillis() + "");
            String religionjson = new Gson().toJson(religionobj);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.RELIGION_TABLE, rid, religionjson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String delete(String rid, String userID) throws Exception {
        String existreligionJson = DBManager.getDbConnection().fetch(ApplicationConstants.RELIGION_TABLE, rid);
        List<Religion> religionlist = new Gson().fromJson(existreligionJson, new TypeToken<List<Religion>>() {
        }.getType());
        String status;
        User user = new UserManager().fetch(userID);
        String userName = user.getFname() + " " + user.getLname();
        Religion religion = religionlist.get(0);
        Religion religionobje = new Religion();
        religionobje.setReligion(religion.getReligion());
        religionobje.setCreateDate(religion.getCreateDate());
        religionobje.setCreatedBy(religion.getCreatedBy());
        religionobje.setStatus(ApplicationConstants.DELETE);
        religionobje.setCreateDate(religion.getCreateDate());
        religionobje.setUpdateDate(System.currentTimeMillis() + "");
        religionobje.setUpdatedBy(religion.getUpdatedBy());
        religionobje.setDeletedBy(userName);
        String religionJson = new Gson().toJson(religionobje);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "religion", rid)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.RELIGION_TABLE, rid, religionJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

}
