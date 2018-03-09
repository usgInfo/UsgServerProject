/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.finance.manager.ReceiptVoucherManager;
import com.accure.usg.admission.dto.ApplicationForm;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

/**
 * @author ankur
 */
public class ApplicationFormManager {

       public String save(ApplicationForm applicationForm, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        //applicationForm.setDobInMilliSecond(Long.parseLong(new ReceiptVoucherManager().saveInMilliSecond(applicationForm.getDob())));

        applicationForm.setCreateDate(System.currentTimeMillis() + "");
        applicationForm.setUpdateDate(System.currentTimeMillis() + "");
        applicationForm.setStatus(ApplicationConstants.ACTIVE);
        applicationForm.setCreatedBy(userName);

        String formJson = new Gson().toJson(applicationForm);
        String result = DBManager.getDbConnection().insert(ApplicationConstants.APPLICATION_FORM, formJson);
        if (result != null) {
            return result;
        }
        return null;
    }
    
    public boolean update(ApplicationForm applicationForm, String candidateId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        applicationForm.setUpdateDate(System.currentTimeMillis() + "");
        applicationForm.setStatus(ApplicationConstants.ACTIVE);
        applicationForm.setUpdatedBy(userName);
       
        String applicationFormJson = new Gson().toJson(applicationForm);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.RECEIPT_VOUCHER_TABLE, candidateId, applicationFormJson);
        if (result) {
            return true;
        }
        return false;
    }

    public String fetch(String candidateId) throws Exception {
        if (candidateId == null || candidateId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.APPLICATION_FORM, candidateId);
        List<ApplicationForm> applicationFormList = new Gson().fromJson(result, new TypeToken<List<ApplicationForm>>() {
        }.getType());
        if (applicationFormList == null || applicationFormList.size() < 1) {
            return null;
        }
        return new Gson().toJson(applicationFormList.get(0));
    }
    
     public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        return DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.APPLICATION_FORM, conditionMap);
    }
     
     public String fetchCandidateToUpdate(String candidateId) throws Exception {
        if (candidateId == null || candidateId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.APPLICATION_FORM, candidateId);
        List<ApplicationForm> formList = new Gson().fromJson(result, new TypeToken<List<ApplicationForm>>() {
        }.getType());
        if (formList == null || formList.size() < 1) {
            return null;
        }
        ApplicationForm emp = formList.get(0);
        
        return new Gson().toJson(emp);
    }
     
     public boolean delete(String candidateId, String loginUserId) throws Exception {
        if (candidateId == null || candidateId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ApplicationForm>() {
        }.getType();
        String form = new ApplicationFormManager().fetch(candidateId);
        if (form == null || form.isEmpty()) {
            return false;
        }
        ApplicationForm applicationFormJson = new Gson().fromJson(form, type);

            applicationFormJson.setStatus(ApplicationConstants.INACTIVE);
            applicationFormJson.setUpdatedBy(userName);
            applicationFormJson.setDeletedBy(userName);

            boolean result = DBManager.getDbConnection().update(ApplicationConstants.APPLICATION_FORM, candidateId, new Gson().toJson(applicationFormJson));
            if (result) {
                return true;
        }

        return false;
    }
     
     public static void main(String[] args){
        
        // new ApplicationFormManager().save();
     }
}
