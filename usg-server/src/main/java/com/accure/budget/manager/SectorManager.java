/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.Sector;
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
public class SectorManager {
    public String saveSector(String sector) throws Exception {

        if (sector == null) {
            return null;
        }
 
        Sector sectorObj = new Sector();
        sectorObj.setSectorName(sector);
        sectorObj.setStatus(ApplicationConstants.ACTIVE);
        sectorObj.setCreateDate(System.currentTimeMillis() + "");
        sectorObj.setUpdateDate(System.currentTimeMillis() + "");
        String sectorjson = new Gson().toJson(sectorObj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.SECTOR_TABLE, sectorjson);
        return id;
    }
      public String viewSectorList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SECTOR_TABLE, conditionMap);
        return result;

    }

    public boolean updateSector(String section, String id) throws Exception {

     
        String categoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.SECTOR_TABLE, id);
        List<Sector> sectorlist = new Gson().fromJson(categoryJson, new TypeToken<List<Sector>>() {
        }.getType());
        Sector sector = sectorlist.get(0);
        Sector sectorobj = new Sector();
        sectorobj.setSectorName(section);
        sectorobj.setCreateDate(sector.getCreateDate());
        sectorobj.setStatus(ApplicationConstants.ACTIVE);
        sectorobj.setUpdateDate(System.currentTimeMillis() + "");
        String sectorjson = new Gson().toJson(sectorobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.SECTOR_TABLE, id, sectorjson);
        return status;
    }

    public boolean deleteSector(String id) throws Exception {
       String existSectorJson = DBManager.getDbConnection().fetch(ApplicationConstants.SECTOR_TABLE, id);
        List<Sector> sectorlist = new Gson().fromJson(existSectorJson, new TypeToken<List<Sector>>() {
        }.getType());
        Sector sector = sectorlist.get(0);
        Sector sectorobj = new Sector();
        sectorobj.setSectorName(sector.getSectorName());
        sectorobj.setCreateDate(sector.getCreateDate());
        sectorobj.setStatus(ApplicationConstants.DELETE);
        sectorobj.setUpdateDate(System.currentTimeMillis() + "");
        String sectorJson = new Gson().toJson(sectorobj);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.SECTOR_TABLE, id, sectorJson);
        return status;
    }
}
