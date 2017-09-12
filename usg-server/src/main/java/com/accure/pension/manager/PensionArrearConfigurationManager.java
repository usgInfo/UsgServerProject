/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforUpdate;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.pension.dto.PensionArrearConfiguration;
import com.accure.pension.dto.PensionHead;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shwetha T S
 */
public class PensionArrearConfigurationManager {

    public String save(PensionArrearConfiguration penArrConfig, String userid) throws Exception {

        String result = "";

        boolean cstatus = true;
        if (userid == null) {
            result = null;
        }
//getArrearOrder()
        HashMap map = new HashMap();
        map.put("arrearOrder", penArrConfig.getArrearOrder());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        HashMap map1 = new HashMap();
        map1.put("headName", penArrConfig.getHeadName());
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (hasDuplicateforSave(ApplicationConstants.PENSION_ARREAR_TABLE, map)) {
            result = ApplicationConstants.DUPLICATE;
            cstatus = false;
        }

        if (hasDuplicateforSave(ApplicationConstants.PENSION_ARREAR_TABLE, map1)) {
            result = ApplicationConstants.DUPLICATE;
            cstatus = false;
        }

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        String id;
        if (duplicate(penArrConfig)) {
            id = ApplicationConstants.DUPLICATE;
        } else {
            penArrConfig.setStatus(ApplicationConstants.ACTIVE);
            penArrConfig.setCreateDate(System.currentTimeMillis() + "");
            penArrConfig.setUpdateDate(System.currentTimeMillis() + "");
            penArrConfig.setCreatedBy(userName);
            penArrConfig.setUpdatedBy(userName);
            String GISjson = new Gson().toJson(penArrConfig);
            id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_ARREAR_TABLE, GISjson);
        }
        return id;
    }

    public boolean duplicate(PensionArrearConfiguration penArrConfig) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ARREAR_TABLE, conditionMap);
            if (result != null) {
                List<PensionArrearConfiguration> list = new Gson().fromJson(result, new TypeToken<List<PensionArrearConfiguration>>() {
                }.getType());

                for (PensionArrearConfiguration li : list) {

                    if (penArrConfig.getHeadName().equals(li.getHeadName()) || penArrConfig.getArrearOrder() == (li.getArrearOrder())) {
                        //System.out.println("true");
                        res = true;
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String getPensionHead() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("pensionheadType", "Earning");
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_TABLE, hMap);
        return result;
    }

    public static void main(String[] args) throws Exception {
        String res = new PensionArrearConfigurationManager().getPensionHead();
        //System.out.println("final result is" + res);
    }

    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ARREAR_TABLE, hMap);
        List<PensionArrearConfiguration> headList = new Gson().fromJson(result, new TypeToken<List<PensionArrearConfiguration>>() {
        }.getType());
        try {
            headList = getHeadList(headList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String finalresult = new Gson().toJson(headList);
        return finalresult;

    }

    public boolean delete(String bankId, String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (bankId == null || bankId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionArrearConfiguration>() {
        }.getType();
        String bank = new PensionArrearConfigurationManager().fetch(bankId);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        PensionArrearConfiguration bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setUpdatedBy(userName);
        bankJson.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_ARREAR_TABLE, bankId, new Gson().toJson(bankJson));

        return result;
    }

    public String update(PensionArrearConfiguration penArrConfig, String id, String userid) throws Exception {
        String result = "";

        boolean cstatus = true;
        if (id == null || userid == null) {
            result = null;
        }
        HashMap map = new HashMap();
        map.put("arrearOrder", penArrConfig.getArrearOrder());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        HashMap map1 = new HashMap();
        map1.put("headName", penArrConfig.getHeadName());
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ARREAR_TABLE, map);
        if (result1 != null) {
            result = ApplicationConstants.DUPLICATE;
            cstatus = false;
        }

        if (isDuplicateforUpdate(ApplicationConstants.PENSION_ARREAR_TABLE, map1, id)) {
            result = ApplicationConstants.DUPLICATE;
            cstatus = false;
        }

        if (cstatus) {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + " " + user.getLname();
            Type type = new TypeToken<PensionArrearConfiguration>() {
            }.getType();
            String dbObject = new PensionArrearConfigurationManager().fetch(id);
            if (dbObject == null || dbObject.isEmpty()) {
                return null;
            }
            PensionArrearConfiguration dbJson = new Gson().fromJson(dbObject, type);
            dbJson.setArrearOrder(penArrConfig.getArrearOrder());
            dbJson.setHeadName(penArrConfig.getHeadName());
            dbJson.setUpdateDate(System.currentTimeMillis() + "");
            dbJson.setUpdatedBy(userName);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_ARREAR_TABLE, id, new Gson().toJson(dbJson));

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_ARREAR_TABLE, Id);
        List<PensionArrearConfiguration> gisList = new Gson().fromJson(result, new TypeToken<List<PensionArrearConfiguration>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    private List<PensionArrearConfiguration> getHeadList(List<PensionArrearConfiguration> headList) throws Exception {
        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.PENSION_HEAD_TABLE);
        List<PensionHead> religionList = new Gson().fromJson(result, new TypeToken<List<PensionHead>>() {
        }.getType());
        for (Iterator<PensionHead> iterator = religionList.iterator(); iterator.hasNext();) {
            PensionHead next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getPensionHeadName());
            //System.out.println("map" + ReligionMap);
        }
        for (int i = 0; i < headList.size(); i++) {

            String religionId = headList.get(i).getHeadName();

            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(religionId)) {
                    headList.get(i).setHeadName(value);
                }
            }
        }
        return headList;
    }
}
