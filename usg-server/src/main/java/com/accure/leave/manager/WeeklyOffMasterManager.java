/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.Employee;
import com.accure.leave.dto.WeeklyOffMasterList;
import com.accure.user.dto.DdoLocationMap;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class WeeklyOffMasterManager {

    private String WEEKLY_OFF_MASTER_TABLE = ApplicationConstants.USG_DB1 + ApplicationConstants.WEEKLY_OFF_MASTER + "`";
    private RestClient aql = new RestClient();

    public String save(WeeklyOffMasterList obj, String weeklyOffLocation, String userId) throws Exception {

        String result = "";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("weeklyOffLocation", weeklyOffLocation);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj == null || weeklyOffLocation == null || userId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.WEEKLY_OFF_MASTER, map)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            obj.setWeeklyOffLocation(weeklyOffLocation);
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreatedBy(userName);
            obj.setUpdatedBy(userName);
            String objJson = new Gson().toJson(obj);
            String objId = DBManager.getDbConnection().insert(ApplicationConstants.WEEKLY_OFF_MASTER, objJson);

            if (objId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String ViewAllWeekOffMaster(String ddo) throws Exception {

        List<DdoLocationMap> list = new ArrayList<DdoLocationMap>();
        List<WeeklyOffMasterList> flist = new ArrayList<WeeklyOffMasterList>();

        if (ddo == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
        if (result != null) {
            list = new Gson().fromJson(result, new TypeToken<List<DdoLocationMap>>() {
            }.getType());
        }

        for (String str : list.get(0).getLocation()) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            map.put("weeklyOffLocation", str);
            String fresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.WEEKLY_OFF_MASTER, map);

            if (fresult != null) {
                List<WeeklyOffMasterList> lwhlist = new Gson().fromJson(fresult, new TypeToken<List<WeeklyOffMasterList>>() {
                }.getType());
                flist.add(lwhlist.get(0));
            }

        }

        flist = getLocation(flist);

        return new Gson().toJson(flist);

    }

    private List<WeeklyOffMasterList> getLocation(List<WeeklyOffMasterList> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getWeeklyOffLocation())) {
                    employeeList.get(i).setWeeklyOffLocation(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public String fetchDetails(String id) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.WEEKLY_OFF_MASTER, id);
        return result;
    }

    public String updateweeklyoffMaster(WeeklyOffMasterList obj, String weeklyOffLocation, String id, String userId) throws Exception {
        String result;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("weeklyOffLocation", weeklyOffLocation);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj == null || weeklyOffLocation == null || id == null || userId == null) {
            result = null;
        } else if (Duplicate.isDuplicateforUpdate(ApplicationConstants.WEEKLY_OFF_MASTER, map, id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.WEEKLY_OFF_MASTER, id);
            List<WeeklyOffMasterList> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<WeeklyOffMasterList>>() {
            }.getType());
            WeeklyOffMasterList weeklymaster = relationlist.get(0);
            obj.setCreateDate(weeklymaster.getCreateDate());
            obj.setWeeklyOffLocation(weeklyOffLocation);
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setUpdatedBy(userName);
            String weeklyoffmaster = new Gson().toJson(obj);

            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.WEEKLY_OFF_MASTER, id, weeklyoffmaster);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String deleteWeekOffMaster(String id, String userId) throws Exception {

        String result;
        if (id == null || userId == null) {
            result = null;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String holidayLocationJson = DBManager.getDbConnection().fetch(ApplicationConstants.WEEKLY_OFF_MASTER, id);
        List<WeeklyOffMasterList> commonHolidaylist = new Gson().fromJson(holidayLocationJson, new TypeToken<List<WeeklyOffMasterList>>() {
        }.getType());
        WeeklyOffMasterList WeeklyOffMasterlist = commonHolidaylist.get(0);

        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.LEAVE_TRANSACTION, "location", WeeklyOffMasterlist.getWeeklyOffLocation()))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            WeeklyOffMasterList weeklyoffMaster = new WeeklyOffMasterList();
            weeklyoffMaster.setWeeklyoffFormList(WeeklyOffMasterlist.getWeeklyoffFormList());
            weeklyoffMaster.setWeeklyOffLocation(WeeklyOffMasterlist.getWeeklyOffLocation());
            weeklyoffMaster.setCreateDate(WeeklyOffMasterlist.getCreateDate());
            weeklyoffMaster.setStatus(ApplicationConstants.DELETE);
            weeklyoffMaster.setUpdateDate(System.currentTimeMillis() + "");
            weeklyoffMaster.setUpdatedBy(userName);
            String weeklyoffMasterJson = new Gson().toJson(weeklyoffMaster);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.WEEKLY_OFF_MASTER, id, weeklyoffMasterJson);

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String viewWeeklyOffMaster(String weeklyOffLocation) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("weeklyOffLocation", weeklyOffLocation);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.WEEKLY_OFF_MASTER, conditionMap);

        return new Gson().toJson(result);

    }
}
