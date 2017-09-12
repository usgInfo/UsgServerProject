/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.user.dto.DdoLocationMap;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author accure
 */
public class DDOLocationManager {

    public String save(DdoLocationMap ddoLocation, String userid) throws Exception {
        String ddoresult = "";
        User user = new UserManager().fetch(userid);
        String fName = user.getFname();
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("ddo", ddoLocation.getDdo());
        saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String saveresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, saveconditionMap);

        if (saveresult != null) {
            ddoresult = "existed";
        } else {
            ddoLocation.setCreateDate(System.currentTimeMillis() + "");
            ddoLocation.setUpdateDate(System.currentTimeMillis() + "");
            ddoLocation.setStatus(ApplicationConstants.ACTIVE);
            ddoLocation.setCreatedBy(fName);

            String ddoJson = new Gson().toJson(ddoLocation);
            ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.DDO_LOCATION_TABLE, ddoJson);
        }

        return ddoresult;

    }

    public String fetch() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
        List<DdoLocationMap> ddoLocationList = new Gson().fromJson(result1, new TypeToken<List<DdoLocationMap>>() {
        }.getType());
        for (DdoLocationMap dlm : ddoLocationList) {
            if ((dlm.getDdo() != null)) {
                try {
                    String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, dlm.getDdo());
                    if (gaJson1 != null) {
                        List<DDO> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal1 = gaList1.get(0);
                        dlm.setDdo(gal1.getDdoName());
                    }
                } catch (Exception e) {
                }
            }
            if ((dlm.getLocation() != null)) {
                List<String> li = dlm.getLocation();
                for (int i = 0; i < li.size(); i++) {
                    String next = li.get(i);
                    try {
                        String locationlist = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, next);
                        if (locationlist != null) {
                            List<Location> gaList1 = new Gson().fromJson(locationlist, new TypeToken<List<Location>>() {
                            }.getType());
                            Location gal1 = gaList1.get(0);
                            next = gal1.getLocationName();
                            li.set(i, next);
                        }
                    } catch (Exception e) {
                    }
                }
                {
                    for (Iterator<String> iterator = li.iterator(); iterator.hasNext();) {
                        String next = iterator.next();
                        try {
                            String locationlist = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, next);
                            if (locationlist != null) {
                                List<Location> gaList1 = new Gson().fromJson(locationlist, new TypeToken<List<Location>>() {
                                }.getType());
                                Location gal1 = gaList1.get(0);
                                next = gal1.getLocationName();
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }

        }

        return new Gson()
                .toJson(ddoLocationList);
    }

    public String deleteDDOLocation(String id) throws Exception {


        String ddoJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_LOCATION_TABLE, id);
        List<DdoLocationMap> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DdoLocationMap>>() {
        }.getType());
        DdoLocationMap ddo = ddoList.get(0);
        DdoLocationMap d = new DdoLocationMap();

        d.setDdo(ddo.getDdo());
        d.setStatus(ApplicationConstants.DELETE);
        d.setUpdateDate(System.currentTimeMillis() + "");
        String status = "";
        List<String> li = ddo.getLocation();
        for (int i = 0; i < li.size(); i++) {
            String next = li.get(i);
            if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "ddo", ddo.getDdo())) {
                status = ApplicationConstants.DELETE_MESSAGE;
                break;
            } else {
                boolean result = DBManager.getDbConnection().update(ApplicationConstants.DDO_LOCATION_TABLE, id, new Gson().toJson(d));
                if (result) {
                    status = ApplicationConstants.SUCCESS;
                } else {
                    status = ApplicationConstants.FAIL;
                }
            }
        }
        return status;

    }

    public boolean update(DdoLocationMap locationType, String id) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_LOCATION_TABLE, id);
        List<DdoLocationMap> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<DdoLocationMap>>() {
        }.getType());
        DdoLocationMap relation = relationlist.get(0);
        relation.setCreateDate(relation.getCreateDate());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setDdo(locationType.getDdo());
        relation.setLocation(locationType.getLocation());
        relation.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.DDO_LOCATION_TABLE, id, bankJson);

        return result;
    }

    String fetchById(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_LOCATION_TABLE, id);
        List<DdoLocationMap> ddoList = new Gson().fromJson(result, new TypeToken<List<DdoLocationMap>>() {
        }.getType());
        if (ddoList == null || ddoList.size() < 1) {
            return null;
        }
        return new Gson().toJson(ddoList.get(0));
    }

    public List<Location> fetchAllLocation(String ddoId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.DDO, ddoId);
        String locationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
        List<DdoLocationMap> locationList = new Gson().fromJson(locationJson, new TypeToken<List<DdoLocationMap>>() {
        }.getType());
        List<Location> returnLocationList = new ArrayList<Location>();
        for (DdoLocationMap dlm : locationList) {
            if ((dlm.getLocation() != null)) {
                List<String> li = dlm.getLocation();
                for (int i = 0; i < li.size(); i++) {
                    String next = li.get(i);
                    try {
                        String locationlist = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, next);
                        if (locationlist != null) {
                            List<Location> gaList1 = new Gson().fromJson(locationlist, new TypeToken<List<Location>>() {
                            }.getType());
                            Location gal1 = gaList1.get(0);
                            returnLocationList.add(gal1);
                            li.set(i, next);
                        }
                    } catch (Exception e) {
                    }
                }

            }
        }
        return returnLocationList;
    }

}
