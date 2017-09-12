/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.pension.dto.PensionFormula;
import com.accure.pension.dto.PensionHead;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shwetha T S
 */
public class PensionFormulaManager {

    public String save(PensionFormula association, String userid) throws Exception {
        String result;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        if (duplicateTest(association)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            association.setStatus(ApplicationConstants.ACTIVE);
            association.setCreateDate(System.currentTimeMillis() + "");
            association.setUpdateDate(System.currentTimeMillis() + "");
            association.setCreatedBy(userName);
            association.setUpdatedBy(userName);
            String GISjson = new Gson().toJson(association);
            result = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_FORMULA_TABLE, GISjson);
        }
        return result;
    }

    public String getPensionSortedHead() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("pensionheadType", "Earning");
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_TABLE, hMap);
        List<PensionHead> list = new Gson().fromJson(result, new TypeToken<List<PensionHead>>() {
        }.getType());
        Collections.sort(list, new Comparator<PensionHead>() {
            public int compare(PensionHead s1, PensionHead s2) {
                return s2.getDisplayHeadOrder() - s1.getDisplayHeadOrder();
            }
        });
        return new Gson().toJson(list);
    }

    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_FORMULA_TABLE, hMap);
        return result;
    }

    public boolean duplicateTest(PensionFormula formula) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_FORMULA_TABLE, conditionMap);
            List<PensionFormula> empList = new Gson().fromJson(result, new TypeToken<List<PensionFormula>>() {
            }.getType());
            if (!empList.isEmpty()) {
                for (PensionFormula cl : empList) {
                    if (formula.getPensionOrder()==(cl.getPensionOrder())||formula.getPensionFormula().equals(cl.getPensionFormula())||formula.getPensionDescription().equals(cl.getPensionDescription())) {
                        res = true;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public boolean update(PensionFormula formula, String id, String userid) throws Exception {
        boolean result;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<PensionFormula>() {
        }.getType();
        String dbObject = new PensionFormulaManager().fetch(id);
        if (dbObject == null || dbObject.isEmpty()) {
            return false;
        } else {
            PensionFormula dbJson = new Gson().fromJson(dbObject, type);
            dbJson.setPensionDescription(formula.getPensionDescription());
            dbJson.setPensionFormula(formula.getPensionFormula());
            dbJson.setPensionOrder(formula.getPensionOrder());
            dbJson.setUpdateDate(System.currentTimeMillis() + "");
            dbJson.setUpdatedBy(userName);
            result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_FORMULA_TABLE, id, new Gson().toJson(dbJson));
        }
        return result;

    }

    public boolean delete(String id, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionFormula>() {
        }.getType();
        String bank = new PensionFormulaManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        PensionFormula bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setUpdateDate(System.currentTimeMillis() + "");
        bankJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_FORMULA_TABLE, id, new Gson().toJson(bankJson));

        return result;

    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_FORMULA_TABLE, Id);
        List<PensionFormula> gisList = new Gson().fromJson(result, new TypeToken<List<PensionFormula>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

}
