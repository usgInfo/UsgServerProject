/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.DAMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shwetha T S
 */
public class DAManager {

    public String saveDAMaster(DAMaster da, String userid) throws Exception {
        String result;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        if (duplicate(da)) {
            result = ApplicationConstants.DUPLICATE_MESSAGE;
            //System.out.println("--------result" + result);
            return result;
        } else if (validateDate(da)) {
            result = ApplicationConstants.VALIDATE_DATES;
        } else {
            da.setCreateDate(System.currentTimeMillis() + "");
            da.setUpdateDate(System.currentTimeMillis() + "");
            da.setCreatedBy(userName);
            da.setUpdatedBy(userName);
            da.setStatus(ApplicationConstants.ACTIVE);
            String claJson = new Gson().toJson(da);
            result = DBManager.getDbConnection().insert(ApplicationConstants.DA_TABLE, claJson);
        }
        return result;
    }

    public boolean validateDate(DAMaster da) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String fromDate = da.getEffFromDate();
        String toDate = da.getEffToDate();
        boolean result = false;
        Date fDate = formatter.parse(fromDate);
        Date tDate = formatter.parse(toDate);
        if (tDate.compareTo(fDate) < 0) {
            result = true;
        }
        return result;
    }

    public boolean duplicate(DAMaster damaster) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("definedRate", damaster.getDefinedRate());
            conditionMap.put("effFromDate", damaster.getEffFromDate());
            conditionMap.put("effToDate", damaster.getEffToDate());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DA_TABLE, conditionMap);
            if (result != null) {
//                List<DAMaster> list = new Gson().fromJson(result, new TypeToken<List<DAMaster>>() {
//                }.getType());
//
//                for (DAMaster li : list) {
//                    if (damaster.getDefinedRate().equals(li.getDefinedRate()) && damaster.getPaidRate() == li.getPaidRate()) {
//                        res = true;
//                    }
//                }
                res = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String viewDAMaster() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DA_TABLE, hMap);
        return result;
    }

    public String updateDA(DAMaster da, String id, String userid) throws Exception {

        String result = null;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<DAMaster>() {
        }.getType();
        String dbObj = new DAManager().fetchDA(id);
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("definedRate", da.getDefinedRate());
        duplicateCheckMap.put("effFromDate", da.getEffFromDate());
        duplicateCheckMap.put("effToDate", da.getEffToDate());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.DA_TABLE, duplicateCheckMap, id)) {
            result = ApplicationConstants.DUPLICATE_MESSAGE;
            return result;
        } else if (dbObj == null || id == null || userid == null) {
            return null;
        } else if (validateDate(da)) {
            result = ApplicationConstants.VALIDATE_DATES;
        } else {
            DAMaster dbObjJson = new Gson().fromJson(dbObj, type);
            dbObjJson.setUpdatedBy(userName);
            dbObjJson.setUpdateDate(System.currentTimeMillis() + "");
            dbObjJson.setActualRate(da.getActualRate());
            dbObjJson.setDefinedRate(da.getDefinedRate());
            dbObjJson.setEffFromDate(da.getEffFromDate());
            dbObjJson.setEffToDate(da.getEffToDate());
            dbObjJson.setIsPensionUsed(da.getIsPensionUsed());

            String assJson = new Gson().toJson(dbObjJson);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.DA_TABLE, id, assJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        //System.out.println("aaaaaaaaaa" + result);
        return result;
    }

    public boolean deleteDAMaster(String id, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<DAMaster>() {
        }.getType();
        String bank = new DAManager().fetchDA(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        DAMaster bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setUpdateDate(System.currentTimeMillis() + "");
        bankJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.DA_TABLE, id, new Gson().toJson(bankJson));
        return result;
    }

    public String fetchDA(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DA_TABLE, Id);
        List<DAMaster> gisList = new Gson().fromJson(result, new TypeToken<List<DAMaster>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

//    public static boolean hasDuplicateforUpdate(String tableName, HashMap columns, String id) {
//        boolean status = false;
//        String result = null;
//        HashMap duplicatecheckMap = new HashMap();
//        try {
//            if (!columns.isEmpty()) {
//                Iterator entries = columns.entrySet().iterator();
//                while (entries.hasNext()) {
//                    Map.Entry entry = (Map.Entry) entries.next();
//                    duplicatecheckMap.put(entry.getKey(), entry.getValue());
//                }
//                duplicatecheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//                String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(tableName, duplicatecheckMap);
//                if (result1 == null) {
//                    status = false;
//                    return status;
//                } else {
//                    List<Map> employeeList = new Gson().fromJson(result1, new TypeToken<List<Map>>() {
//                    }.getType());
//                    //System.out.println("status" + status);
//                    for (Map city : employeeList) {
//                        String obid = ((Map<String, String>) city.get("_id")).get("$oid");
//
////                        String obid1 = obid.toString();
////                        //System.out.println("------" + obid);
////                        //System.out.println("------" + obid1.substring(6));
////                        String obid2 = obid1.substring(6, obid1.length() - 1);
////                        //System.out.println("------" + obid2);
//                        if (!id.equals(obid)) {
//                            status = true;
//                            break;
//                        } else {
//                            continue;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            status = false;
//        } finally {
//            //System.out.println("status" + status);
//            return status;
//        }
//    }
}
