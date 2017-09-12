/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;


import com.accure.pension.dto.PensionRevision;
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
public class PensionRevisionManager {

    public String save(PensionRevision pensionrevision,String userid) throws Exception {
         String result;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        pensionrevision.setStatus(ApplicationConstants.ACTIVE);
        pensionrevision.setCreateDate(System.currentTimeMillis() + "");
        pensionrevision.setUpdateDate(System.currentTimeMillis() + "");
        String GISjson = new Gson().toJson(pensionrevision);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_REVISION_TABLE, GISjson);
        //System.out.println(id);
        return id;

    }
     public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_REVISION_TABLE, Id);
        List<PensionRevision> bankList = new Gson().fromJson(result, new TypeToken<List<PensionRevision>>() {
        }.getType());
        if (bankList == null || bankList.size() < 1) {
            return null;
        }
        return new Gson().toJson(bankList.get(0));
    }

    public boolean delete(String bankId,String userid) throws Exception {
        
        
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        
        if (bankId == null || bankId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionRevision>() {
        }.getType();
        String bank = new PensionRevisionManager().fetch(bankId);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        PensionRevision bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_REVISION_TABLE, bankId, new Gson().toJson(bankJson));
        return result;
    }

    public boolean update(PensionRevision obj, String id,String userid) throws Exception {
        
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(obj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_REVISION_TABLE, id, bankJson);
        return result;
    }
    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_REVISION_TABLE, conditionMap);
        return result;
    }
     

}
