/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.leave.dto.CommonHolidayMaster;
import com.accure.leave.dto.HolidayLocationMaster;

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
public class HolidayLocationMasterManager {
    
    public String saveCommonHolidayLocationMaster(HolidayLocationMaster hlm) throws Exception {

        hlm.setCreateDate(System.currentTimeMillis() + "");
        hlm.setUpdateDate(System.currentTimeMillis() + "");
        hlm.setStatus(ApplicationConstants.ACTIVE);
        String hlmJson = new Gson().toJson(hlm);
        String cid = DBManager.getDbConnection().insert(ApplicationConstants.HOLIDAYS_LOCATION_MASTER,hlmJson);
        return cid;

    }
      public String ViewAllHolidayLocationMaster() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HOLIDAYS_LOCATION_MASTER, conditionMap);
        return result;
      }
      
       public boolean updateHolidayLocationMaster(HolidayLocationMaster holidaylocationMaster, String locationholidayMasterId) throws Exception {
           String existJsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.HOLIDAYS_LOCATION_MASTER, locationholidayMasterId);
        List<HolidayLocationMaster> list = new Gson().fromJson(existJsonObj, new TypeToken<List<HolidayLocationMaster>>() {
        }.getType());
        HolidayLocationMaster masterData = list.get(0);
        holidaylocationMaster.setCreateDate(masterData.getCreateDate());
        holidaylocationMaster.setUpdateDate(System.currentTimeMillis() + "");
        holidaylocationMaster.setStatus(ApplicationConstants.ACTIVE);
        String locationholidayMasterJson = new Gson().toJson(holidaylocationMaster);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HOLIDAYS_LOCATION_MASTER,locationholidayMasterId, locationholidayMasterJson);
        return result;
    }
       
       public boolean deleteHolidayLocationMaster(String id) throws Exception {
        String holidayLocationJson = DBManager.getDbConnection().fetch(ApplicationConstants.HOLIDAYS_LOCATION_MASTER, id);
        List<HolidayLocationMaster> commonHolidaylist = new Gson().fromJson(holidayLocationJson, new TypeToken<List<HolidayLocationMaster>>() {
        }.getType());
        HolidayLocationMaster holidayLocationMasterlist = commonHolidaylist.get(0);
        HolidayLocationMaster holidayLocationMaster = new HolidayLocationMaster();
        holidayLocationMaster.setHolidayLocation(holidayLocationMasterlist.getHolidayLocation());
        holidayLocationMaster.setDisplayOrder(holidayLocationMasterlist.getDisplayOrder());
        holidayLocationMaster.setRemarks(holidayLocationMasterlist.getRemarks());
        holidayLocationMaster.setCreateDate(holidayLocationMasterlist.getCreateDate());
        holidayLocationMaster.setStatus(ApplicationConstants.INACTIVE);
        holidayLocationMaster.setUpdateDate(System.currentTimeMillis() + "");
        String holidayLocationMasterJson = new Gson().toJson(holidayLocationMaster);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.HOLIDAYS_LOCATION_MASTER, id, holidayLocationMasterJson);
        return status;
    }
    
}
