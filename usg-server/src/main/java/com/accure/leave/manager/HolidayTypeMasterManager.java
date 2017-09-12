/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.leave.dto.HolidayTypeMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class HolidayTypeMasterManager {

    public String saveHolidayTypeMaster(HolidayTypeMaster holidaytype, String userId) throws Exception {

        String result;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("holidayType", holidaytype.getHolidayType());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (userId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.HOLIDAY_TYPE_MASTER, map)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            holidaytype.setCreateDate(System.currentTimeMillis() + "");
            holidaytype.setUpdateDate(System.currentTimeMillis() + "");
            holidaytype.setStatus(ApplicationConstants.ACTIVE);
            holidaytype.setCreatedBy(userName);
            String holidayTypeJson = new Gson().toJson(holidaytype);
            String id = DBManager.getDbConnection().insert(ApplicationConstants.HOLIDAY_TYPE_MASTER, holidayTypeJson);
            if (id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String ViewAllHolidayTypeMaster() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HOLIDAY_TYPE_MASTER, conditionMap);
        return result;

    }

    public String updateHolidaysTypeMaster(HolidayTypeMaster holidayMaster, String holidayMasterId, String userId) throws Exception {
        String result;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("holidayType", holidayMaster.getHolidayType());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (holidayMasterId == null || userId == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.HOLIDAY_TYPE_MASTER, map, holidayMasterId)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.HOLIDAY_TYPE_MASTER, holidayMasterId);
            List<HolidayTypeMaster> list = new Gson().fromJson(existrelationJson, new TypeToken<List<HolidayTypeMaster>>() {
            }.getType());
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            HolidayTypeMaster masterData = list.get(0);
            holidayMaster.setCreateDate(masterData.getCreateDate());
            holidayMaster.setUpdateDate(System.currentTimeMillis() + "");
            holidayMaster.setStatus(ApplicationConstants.ACTIVE);
            holidayMaster.setUpdatedBy(userName);
            String holidayMasterJson = new Gson().toJson(holidayMaster);
            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.HOLIDAY_TYPE_MASTER, holidayMasterId, holidayMasterJson);

            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    public String deleteHolidayTypeMaster(String id, String userId) throws Exception {
        String result;
        if (id == null || userId == null) {
            result = null;
        } 
        else  if ((DeleteDependencyManager.hasDependency(ApplicationConstants.COMMON_HOLIDAYS_MASTER, "holidayType", id))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } 
        else {
            String holidayTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.HOLIDAY_TYPE_MASTER, id);
            List<HolidayTypeMaster> commonHolidaylist = new Gson().fromJson(holidayTypeJson, new TypeToken<List<HolidayTypeMaster>>() {
            }.getType());
            HolidayTypeMaster holidayTypeMasterlist = commonHolidaylist.get(0);
            HolidayTypeMaster holidayTypeMaster = new HolidayTypeMaster();
            holidayTypeMaster.setHolidayType(holidayTypeMasterlist.getHolidayType());
            holidayTypeMaster.setUserID(holidayTypeMasterlist.getUserID());
            holidayTypeMaster.setCreateDate(holidayTypeMasterlist.getCreateDate());
            holidayTypeMaster.setStatus(ApplicationConstants.INACTIVE);
            holidayTypeMaster.setUpdateDate(System.currentTimeMillis() + "");
            String commonHolidayMasterJson = new Gson().toJson(holidayTypeMaster);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.HOLIDAY_TYPE_MASTER, id, commonHolidayMasterJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }
}
