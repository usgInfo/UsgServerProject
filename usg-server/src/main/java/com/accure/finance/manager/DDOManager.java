/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.user.manager.UserManager;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author deepak2310
 */
public class DDOManager {

    public String createDDO(DDO ddo, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        ddo.setCreateDate(System.currentTimeMillis() + "");
        ddo.setUpdateDate(System.currentTimeMillis() + "");
        ddo.setStatus(ApplicationConstants.ACTIVE);
        ddo.setCreatedBy(userName);

        String ddoJson = new Gson().toJson(ddo);

        String ddoId = DBManager.getDbConnection().insert(ApplicationConstants.DDO_TABLE, ddoJson);
        if (ddoId != null) {
            return ddoId;
        }
        return null;
    }

    public HashMap<String, String> fetchAllLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String locationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_TABLE, conditionMap);
        List<Location> locationList = new Gson().fromJson(locationJson, new TypeToken<List<Location>>() {
        }.getType());
        conditionMap.clear();
        for (Location location : locationList) {
            conditionMap.put(((Map<String, String>) location.getId()).get("$oid"), location.getLocationName());
        }
        return conditionMap;
    }

    public List<DDO> fetchAllDdo() throws Exception {
//        HashMap<String, String> locationMap = fetchAllLocation();
//        List<DDO> ddoReturnList = new ArrayList<DDO>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ddoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, conditionMap);
        List<DDO> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DDO>>() {
        }.getType());

//        if (ddoList != null) {
//            for (DDO ddo : ddoList) {
//                if (ddo.getLocation() != null) {
//                    String locationId = ddo.getLocation();
//                    if (locationMap.containsKey(locationId) && locationMap.get(locationId) != null) {
//                        ddo.setLocationName(locationMap.get(locationId));
//                    }
//                }
//                ddoReturnList.add(ddo);
//            }
//        }

        return ddoList;
    }

    public DDO fetch(String ddoId) throws Exception {
        if (ddoId == null || ddoId.isEmpty()) {
            return null;
        }
        String ddoJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddoId);
        if (ddoJson == null || ddoJson.isEmpty()) {
            return null;
        }
        List<DDO> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DDO>>() {
        }.getType());
        if (ddoList == null || ddoList.isEmpty()) {
            return null;
        }
        return ddoList.get(0);

    }

    public boolean updateDDO(DDO ddo, String userId, String ddoId) throws Exception {

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        DDO ddoDb = fetch(ddoId);
        if (ddoDb.getDdoName() != null || !ddoDb.getDdoName().isEmpty()) {
            ddoDb.setDdoName(ddo.getDdoName());
        }
        if (ddoDb.getCode()!= null || !ddoDb.getCode().isEmpty()) {
            ddoDb.setCode(ddo.getCode());
        }
//        if (ddoDb.getLocation() != null || !ddoDb.getLocation().isEmpty()) {
//            ddoDb.setLocation(ddo.getLocation());
//        }
        if (ddoDb.getRemarks() != null || !ddoDb.getRemarks().isEmpty()) {
            ddoDb.setRemarks(ddo.getRemarks());
        }
        ddoDb.setUpdateDate(System.currentTimeMillis() + "");
        ddoDb.setUpdatedBy(userName);
        ddoDb.setStatus(ApplicationConstants.ACTIVE);

        String ddoJson = new Gson().toJson(ddoDb);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.DDO_TABLE, ddoId, ddoJson);
        if (status) {
            return true;
        }
        return false;
    }

    public String deleteDDO(String ddoId, String currentUserLogin) throws Exception {
        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        DDO ddo = fetch(ddoId);
        ddo.setStatus(ApplicationConstants.DELETE);
        ddo.setUpdateDate(System.currentTimeMillis() + "");
        ddo.setUpdatedBy(userName);

        String ddoJson = new Gson().toJson(ddo);
        String status = "";
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.DDO_LOCATION_TABLE, "ddo", ddoId) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "ddo", ddoId)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        }
		else
		{
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DDO_TABLE, ddoId, ddoJson);
            
		 if (flag)
                {
                        status = ApplicationConstants.SUCCESS;
                } 
            else 
                {
                        status = ApplicationConstants.FAIL;
                }
                }return status;
    }
       
        
    }
//    public static void main(String[] args) throws Exception {
//        List<DDO> result=new DDOManager().fetchAllDdo();
//        //System.out.println(result);
//    }

