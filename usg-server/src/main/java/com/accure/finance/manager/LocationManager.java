/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.Location;
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
 * @author
 */
public class LocationManager {

    public String createLocation(Location location, String loginUserId) throws Exception {

        String result ;
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("locationName", location.getLocationName());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        map1.put("locationCode", location.getLocationCode());
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (loginUserId == null) {
            result = null;
        }
       else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LOCATION_TABLE, map)) || (Duplicate.hasDuplicateforSave(ApplicationConstants.LOCATION_TABLE, map1))) {
            result = ApplicationConstants.DUPLICATE;
        }
        else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            location.setCreateDate(System.currentTimeMillis() + "");
            location.setUpdateDate(System.currentTimeMillis() + "");
            location.setStatus(ApplicationConstants.ACTIVE);
            location.setCreatedBy(userName);

            String locationJson = new Gson().toJson(location);

            String locationId = DBManager.getDbConnection().insert(ApplicationConstants.LOCATION_TABLE, locationJson);
            if (locationId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public List<Location> fetchAllLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String locationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_TABLE, conditionMap);
        List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
        }.getType());
        return locationList;
    }

    public Location fetch(String locationId) throws Exception {
        if (locationId == null || locationId.isEmpty()) {
            return null;
        }
        String locationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, locationId);
        if (locationJson == null || locationJson.isEmpty()) {
            return null;
        }
        List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
        }.getType());
        if (locationList == null || locationList.isEmpty()) {
            return null;
        }
        return locationList.get(0);

    }

    public String updateLocation(Location location, String userId, String locationId) throws Exception {

        String result;
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("locationName", location.getLocationName());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        map1.put("locationCode", location.getLocationCode());
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (userId == null || locationId == null) {
            result = null;
        } 
        else if ((Duplicate.isDuplicateforUpdate(ApplicationConstants.LOCATION_TABLE, map, locationId)) || (Duplicate.isDuplicateforUpdate(ApplicationConstants.LOCATION_TABLE, map1, locationId))) {
            result = ApplicationConstants.DUPLICATE;
        } 
        else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            Location locationDb = fetch(locationId);
            if (locationDb.getLocationName() != null || !locationDb.getLocationName().isEmpty()) {
                locationDb.setLocationName(location.getLocationName());
            }

            locationDb.setLocationCode(location.getLocationCode());

            if (locationDb.getDescription() != null || !locationDb.getDescription().isEmpty()) {
                locationDb.setDescription(location.getDescription());
            }
            locationDb.setUpdateDate(System.currentTimeMillis() + "");
            locationDb.setUpdatedBy(userName);
            locationDb.setStatus(ApplicationConstants.ACTIVE);

            String locationJson = new Gson().toJson(locationDb);

            boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOCATION_TABLE, locationId, locationJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    public String deleteLocation(String locationId, String currentUserLogin) throws Exception {
        String status = "";
        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        Location location = fetch(locationId);
        location.setStatus(ApplicationConstants.DELETE);
        location.setUpdateDate(System.currentTimeMillis() + "");
        location.setUpdatedBy(userName);

        String locationJson = new Gson().toJson(location);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "location", locationId) || DeleteDependencyManager.hasDependency(ApplicationConstants.WEEKLY_OFF_MASTER, "weeklyOffLocation", locationId)
                || DeleteDependencyManager.hasDependency(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, "locationWiseHoliday", locationId)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.LOCATION_TABLE, locationId, locationJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }

        return status;
    }

}
