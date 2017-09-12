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
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shwetha T S
 */
public class PensionHeadManager {

    public String save(PensionHead pensionhead, String userid) throws Exception {
        String result;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
//        if (duplicateTest(pensionhead)) {
//            result = ApplicationConstants.DUPLICATE;
//
//        } else {
            pensionhead.setStatus(ApplicationConstants.ACTIVE);
            pensionhead.setCreateDate(System.currentTimeMillis() + "");
            pensionhead.setUpdateDate(System.currentTimeMillis() + "");
            pensionhead.setCreatedBy(userName);
            pensionhead.setUpdatedBy(userName);
            String pensionHeadjson = new Gson().toJson(pensionhead);
            //System.out.println("in the manager" + pensionHeadjson);
            result = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_HEAD_TABLE, pensionHeadjson);
      //  }
        return result;
    }

    public boolean duplicateTest(PensionHead pensionhead) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_TABLE, conditionMap);
        List<PensionHead> empList = new Gson().fromJson(result, new TypeToken<List<PensionHead>>() {
        }.getType());
        if (!empList.isEmpty()) {
            for (PensionHead cl : empList) {
                if (cl.getDisplayHeadOrder() == (pensionhead.getDisplayHeadOrder()) || (cl.getPensionHeadName().equalsIgnoreCase(pensionhead.getPensionHeadName())) || (cl.getPensionShortName().equalsIgnoreCase(pensionhead.getPensionShortName()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_TABLE, hMap);
//        List<PensionHead> employeeList = new Gson().fromJson(result, new TypeToken<List<PensionHead>>() {
//        }.getType());
//
//        for (PensionHead cl : employeeList) {
//            if (cl.getHeadFormula() != null) {
//                try {
//                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_FORMULA_TABLE, cl.getHeadFormula());
//                    List<PensionFormula> gaList = new Gson().fromJson(gaJson, new TypeToken<List<PensionFormula>>() {
//                    }.getType());
//                    PensionFormula gal = gaList.get(0);
//                    cl.setHeadFormula(gal.getPensionFormula());
//                } catch (Exception ex) {
//
//                }
//            }
//        }
//        return new Gson().toJson(employeeList);
        return result;
    }

    public static void main() throws Exception {
        String result = new PensionHeadManager().view();
        //System.out.println("final result in view section" + result);
    }

    public boolean delete(String id, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionHead>() {
        }.getType();
        String bank = new PensionHeadManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        PensionHead bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setUpdateDate(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_HEAD_TABLE, id, new Gson().toJson(bankJson));

        return result;

    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_HEAD_TABLE, Id);
        List<PensionHead> gisList = new Gson().fromJson(result, new TypeToken<List<PensionHead>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public boolean update(PensionHead pensionhead, String id, String userid) throws Exception {
        boolean result=false;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
       // if (!duplicateTest(pensionhead)) {
//            result = false;
//        } else {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_HEAD_TABLE, id);
        List<PensionHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<PensionHead>>() {
        }.getType());
        PensionHead relation = relationlist.get(0);
        relation.setDisplayHeadOrder(pensionhead.getOrderLevel());
        relation.setFixedCode(pensionhead.getFixedCode());
        relation.setHeadFormula(pensionhead.getHeadFormula());
        relation.setHeadRemarks(pensionhead.getHeadRemarks());
        relation.setOrderLevel(pensionhead.getOrderLevel());
        relation.setPartOfGross(pensionhead.getPartOfGross());
        relation.setPensionAmount(pensionhead.getPensionAmount());
        relation.setPensionHeadName(pensionhead.getPensionHeadName());
        relation.setPensionShortName(pensionhead.getPensionShortName());
        relation.setPensionheadType(pensionhead.getPensionheadType());
        relation.setCreateDate(pensionhead.getCreateDate());

        relation.setCreatedBy(pensionhead.getCreatedBy());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setUpdatedBy(userName);
        relation.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(relation);
        result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_HEAD_TABLE, id, bankJson);
      //   }
        return result;
    }

}
