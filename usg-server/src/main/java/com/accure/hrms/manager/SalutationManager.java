/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import com.accure.hrms.dto.Salutation;
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
public class SalutationManager {

    public String save(Salutation salutation, String userID) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("salutation", salutation.getSalutation());

        if (userID == null) {
            return null;
        } else if (hasDuplicateforSave(ApplicationConstants.SALUTATION_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userID);
            String userName = user.getFname() + " " + user.getLname();
            salutation.setStatus(ApplicationConstants.ACTIVE);
            salutation.setCreateDate(System.currentTimeMillis() + "");
            salutation.setCreatedBy(userName);
            salutation.setUpdateDate(System.currentTimeMillis() + "");
            salutation.setUpdatedBy(userName);
            String salutationjson = new Gson().toJson(salutation);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.SALUTATION_TABLE, salutationjson);
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
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALUTATION_TABLE, conditionMap);
        return result;

    }

    public String update(Salutation salutation, String rid, String userID) throws Exception {

        String result="";

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("salutation", salutation.getSalutation());

        if (userID == null) {
            result= null;

        } else if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SALUTATION_TABLE, conditionMap, rid)) {
            result = ApplicationConstants.DUPLICATE;

        } else if (rid != null) {
            
            String salutationJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALUTATION_TABLE, rid);
            
            if (salutationJson != null) {
                
                List<Salutation> salutationlist = new Gson().fromJson(salutationJson, new TypeToken<List<Salutation>>() {
                }.getType());
                
                User user = new UserManager().fetch(userID);
                String userName = user.getFname() + " " + user.getLname();
                
                Salutation salutationobj = salutationlist.get(0);
                salutationobj.setSalutation(salutation.getSalutation());
                salutationobj.setSalutationRemarks(salutation.getSalutationRemarks());
                salutationobj.setStatus(ApplicationConstants.ACTIVE);
                salutationobj.setUpdateDate(System.currentTimeMillis() + "");
                salutationobj.setUpdatedBy(userName);
                String salutationfinaljson = new Gson().toJson(salutationobj);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.SALUTATION_TABLE, rid, salutationfinaljson);

                if (status) {

                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }

        return result;
    }

    public String delete(String rid, String userID) throws Exception {
        String existsalutationJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALUTATION_TABLE, rid);
        List<Salutation> salutationlist = new Gson().fromJson(existsalutationJson, new TypeToken<List<Salutation>>() {
        }.getType());
        String status;
        User user = new UserManager().fetch(userID);
        String userName = user.getFname() + " " + user.getLname();
        Salutation salutation = salutationlist.get(0);
        Salutation salutationbje = new Salutation();
        salutationbje.setSalutation(salutation.getSalutation());
        salutationbje.setSalutationRemarks(salutation.getSalutationRemarks());
        salutationbje.setCreateDate(salutation.getCreateDate());
        salutationbje.setCreatedBy(salutation.getCreatedBy());
        salutationbje.setStatus(ApplicationConstants.DELETE);
        salutationbje.setUpdateDate(System.currentTimeMillis() + "");
        salutationbje.setUpdatedBy(salutation.getUpdatedBy());
        salutationbje.setDeletedBy(userName);
        String salutatiJson = new Gson().toJson(salutationbje);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "salutationOption", rid)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SALUTATION_TABLE, rid, salutatiJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }
}
