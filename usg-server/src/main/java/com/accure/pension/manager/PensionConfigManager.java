/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.pension.dto.PensionConfig;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shwetha T S
 */
public class PensionConfigManager {
    
    public String save(PensionConfig association, String userid) throws Exception {
        Type type = new TypeToken<PensionConfig>() {
        }.getType();
        PensionConfig assObj = new PensionConfig();
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        association.setStatus(ApplicationConstants.ACTIVE);
        
        association.setCreateDate(System.currentTimeMillis() + "");
        association.setUpdateDate(System.currentTimeMillis() + "");
        association.setUpdatedBy(userName);
        association.setCreatedBy(userName);
        String penConfigjson = new Gson().toJson(association);
        
        String id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_CONFIG_TABLE, penConfigjson);
        
        return id;
        
    }
    
    public boolean update(PensionConfig penConfig, String id, String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        try {
            String dbobject = new PensionConfigManager().fetch(id);
            PensionConfig gisList = new Gson().fromJson(dbobject, new TypeToken<PensionConfig>() {
            }.getType());
            gisList.setUpdateDate(System.currentTimeMillis() + "");
            gisList.setUpdatedBy(userName);
            gisList.setStatus(ApplicationConstants.ACTIVE);
            gisList.setCompFac(penConfig.getCompFac());
            gisList.setCompPer(penConfig.getCompPer());
            gisList.setDetGraDiv(penConfig.getDetGraDiv());
            gisList.setDurOfCompYear(penConfig.getDurOfCompYear());
            gisList.setMaxComAmt(penConfig.getMaxComAmt());
            gisList.setMaxGratuity(penConfig.getMaxGratuity());
            gisList.setMaxPurYear(penConfig.getMaxPurYear());
            gisList.setMinPensionAmountForNonUgc(penConfig.getMinPensionAmountForNonUgc());
            gisList.setMinPensionAmountForUgc(penConfig.getMinPensionAmountForUgc());
            gisList.setPensionAfterJob(penConfig.getPensionAfterJob());
            gisList.setPensionAfterPer(penConfig.getPensionAfterPer());
            gisList.setPensionInJob(penConfig.getPensionInJob());
            gisList.setPensionUptoPer(penConfig.getPensionUptoPer());
            gisList.setRetGraDiv(penConfig.getRetGraDiv());
            
            String classJson = new Gson().toJson(gisList);
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_CONFIG_TABLE, id, classJson);
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_CONFIG_TABLE, hMap);
        return result;
    }
    
    public boolean delete(String id,String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionConfig>() {
        }.getType();
        String bank = new PensionConfigManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        PensionConfig bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setUpdateDate(System.currentTimeMillis() + "");
        bankJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_CONFIG_TABLE, id, new Gson().toJson(bankJson));
        
        return result;
    }
    
    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_CONFIG_TABLE, Id);
        List<PensionConfig> gisList = new Gson().fromJson(result, new TypeToken<List<PensionConfig>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }
    
}
