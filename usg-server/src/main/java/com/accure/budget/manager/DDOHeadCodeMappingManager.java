/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.budget.dto.FundType;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.budget.dto.ddoHeadCodeMapping;
import com.accure.finance.dto.DDO;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;

import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class DDOHeadCodeMappingManager {

    public String save(ddoHeadCodeMapping ddoheadCode, String userId) throws Exception {

        if (userId == null) {
            return null;
        }
        if (duplicateTest(ddoheadCode)) {
            return ApplicationConstants.DUPLICATE;

        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        ddoheadCode.setStatus(ApplicationConstants.ACTIVE);
        ddoheadCode.setCreateDate(System.currentTimeMillis() + "");
        ddoheadCode.setUpdateDate(System.currentTimeMillis() + "");
        ddoheadCode.setCreatedBy(userName);
        ddoheadCode.setUpdatedBy(userName);

        String result = DBManager.getDbConnection().insert(ApplicationConstants.DDO_HEAD_CODE_TABLE, new Gson().toJson(ddoheadCode));

        return result;
    }

    public boolean duplicateTest(ddoHeadCodeMapping ddoheadCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_HEAD_CODE_TABLE, conditionMap);
        if (result != null) {
            List<ddoHeadCodeMapping> empList = new Gson().fromJson(result, new TypeToken<List<ddoHeadCodeMapping>>() {
            }.getType());
            if (!empList.isEmpty()) {
                for (ddoHeadCodeMapping cl : empList) {
                    if ((cl.getDdo().equals(ddoheadCode.getDdo()) && (cl.getFundType().equals(ddoheadCode.getFundType()))) && (cl.getSector().equals(ddoheadCode.getSector()))) {
                        for (int i = 0; i < cl.getHeadCode().size(); i++) {

                            if (cl.getHeadCode().get(i).equals(ddoheadCode.getHeadCode().get(i))) {
                                return true;

                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_HEAD_CODE_TABLE, conditionMap);
        List<ddoHeadCodeMapping> empList = new Gson().fromJson(result, new TypeToken<List<ddoHeadCodeMapping>>() {
        }.getType());
        for (ddoHeadCodeMapping cl : empList) {
            try {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, cl.getFundType());
                List<FundType> gaList = new Gson().fromJson(gaJson, new TypeToken<List<FundType>>() {
                }.getType());
                FundType gal = gaList.get(0);
                cl.setFundTypeId(cl.getFundType());
                cl.setFundType(gal.getDescription());

            } catch (Exception e) {
            }
            try {
                String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_SECTOR_TABLE, cl.getSector());
                List<BudgetSector> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<BudgetSector>>() {
                }.getType());
                BudgetSector g = gaList1.get(0);
                cl.setSectorId(cl.getSector());
                cl.setSector(g.getDescription());

            } catch (Exception e) {
            }
            try {
                String gaJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                List<DDO> gaList2;
                gaList2 = new Gson().fromJson(gaJson2, new TypeToken<List<DDO>>() {
                }.getType());
                DDO g1;
                g1 = gaList2.get(0);
                cl.setDdo(g1.getDdoName());
            } catch (Exception e) {
            }
            List list1 = new ArrayList();
            List list = new ArrayList();
            list.addAll(cl.getHeadCode());

            for (int i = 0; i < list.size(); i++) {
                try {
                    String gaJson3 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getHeadCode().get(i));

                    List<BudgetHeadMaster> gaList4;
                    gaList4 = new Gson().fromJson(gaJson3, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    BudgetHeadMaster hc;
                    hc = gaList4.get(0);
                    list1.add(hc.getBudgetHead());
                } catch (Exception e) {
                }
            }
            cl.setHeadCodeName(list1);

        }
        return new Gson().toJson(empList);

    }

    public boolean delete(String id, String userId) throws Exception {
        if (id == null || id.isEmpty() || userId == null || userId.isEmpty()) {
            return false;
        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<ddoHeadCodeMapping>() {
        }.getType();
        String dbStr = new DDOHeadCodeMappingManager().fetch(id);
        if (dbStr == null || dbStr.isEmpty()) {
            return false;
        }
        ddoHeadCodeMapping dbObj = new Gson().fromJson(dbStr, type);
        dbObj.setStatus(ApplicationConstants.DELETE);
        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        dbObj.setDeletedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.DDO_HEAD_CODE_TABLE, id, new Gson().toJson(dbObj));

        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_HEAD_CODE_TABLE, Id);
        List<ddoHeadCodeMapping> gisList = new Gson().fromJson(result, new TypeToken<List<ddoHeadCodeMapping>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public String Search(String ddo, String fundtype, String sector) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("fundType", fundtype);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_HEAD_CODE_TABLE, conditionMap);
        List<ddoHeadCodeMapping> searchList = new Gson().fromJson(result, new TypeToken<List<ddoHeadCodeMapping>>() {
        }.getType());
        for (ddoHeadCodeMapping cl : searchList) {
            List list1 = new ArrayList();
            List list = new ArrayList();
            list.addAll(cl.getHeadCode());

            for (int i = 0; i < list.size(); i++) {
                String gaJson3 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getHeadCode().get(i));

                List<BudgetHeadMaster> gaList4;
                gaList4 = new Gson().fromJson(gaJson3, new TypeToken<List<BudgetHeadMaster>>() {
                }.getType());
                BudgetHeadMaster hc;
                hc = gaList4.get(0);
                list1.add(hc.getBudgetHead());

            }
            cl.setHeadCode(list1);

        }

        return new Gson().toJson(searchList);

    }

    public boolean update(ddoHeadCodeMapping headCodeType, String id, String userId) throws Exception {
        boolean result = false;
        if (userId == null || id == null) {
            return false;
        }

        String dbObjectStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_HEAD_CODE_TABLE, id);

        if (dbObjectStr != null) {
            List<ddoHeadCodeMapping> dbObjList = new Gson().fromJson(dbObjectStr, new TypeToken<List<ddoHeadCodeMapping>>() {
            }.getType());

            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            ddoHeadCodeMapping dbObj = dbObjList.get(0);
            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setStatus(ApplicationConstants.ACTIVE);
            dbObj.setBudgetHead(headCodeType.getBudgetHead());
            dbObj.setBudgetHeadDescription(headCodeType.getBudgetHeadDescription());
            dbObj.setDdo(headCodeType.getDdo());
            dbObj.setFundType(headCodeType.getFundType());
            dbObj.setSector(headCodeType.getSector());
            dbObj.setHeadCode(headCodeType.getHeadCode());
            dbObj.setUpdatedBy(userName);

            result = DBManager.getDbConnection().update(ApplicationConstants.DDO_HEAD_CODE_TABLE, id, new Gson().toJson(dbObj));
        }

        return result;
    }

}
