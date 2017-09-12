/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.pension.dto.PensionType;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class PensionTypeManager {
     public String save(String cityCategory) throws Exception {
        
        if (cityCategory == null) {
            return null;
        }

        PensionType cityCategoryobj = new PensionType();
        cityCategoryobj.setPensionType(cityCategory);
        cityCategoryobj.setStatus(ApplicationConstants.ACTIVE);
        cityCategoryobj.setCreateDate(System.currentTimeMillis() + "");
        cityCategoryobj.setUpdateDate(System.currentTimeMillis() + "");
        String cityCategoryjson = new Gson().toJson(cityCategoryobj);

        String id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_TYPE_TABLE,cityCategoryjson);
        return id;
    }
    
    
    
    public String view() throws Exception {
        HashMap<String, String> cityCategoryMap = new HashMap<String, String>();
        cityCategoryMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_TYPE_TABLE, cityCategoryMap);
        return result;
        
    }
    
      public boolean update(String cityCategory, String cCid) throws Exception {
       String categoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_TYPE_TABLE, cCid);
        List<PensionType> categolist = new Gson().fromJson(categoryJson, new TypeToken<List<PensionType>>() {
        }.getType());
        PensionType categor = categolist.get(0);
        PensionType categoryobj = new PensionType();
        categoryobj.setPensionType(cityCategory);
        categoryobj.setCreateDate(categor.getCreateDate());
        categoryobj.setStatus(ApplicationConstants.ACTIVE);
        categoryobj.setUpdateDate(System.currentTimeMillis() + "");
        String categojson = new Gson().toJson(categoryobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_TYPE_TABLE, cCid, categojson);
        return status;
    }
       public boolean delete(String cCId) throws Exception {
         String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_TYPE_TABLE, cCId);
        List<PensionType> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionType>>() {
        }.getType());
        PensionType category = categorylist.get(0);
        PensionType categoryobje = new PensionType();
        categoryobje.setPensionType(category.getPensionType());
        categoryobje.setCreateDate(category.getCreateDate());
        categoryobje.setStatus(ApplicationConstants.DELETE);
        categoryobje.setUpdateDate(System.currentTimeMillis() + "");
        String categoryJson = new Gson().toJson(categoryobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_TYPE_TABLE, cCId, categoryJson);
        return status;
    }

   
    
}
