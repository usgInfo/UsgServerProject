/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.PreviousBudgetAmountDetails;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Designation;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class PreviousBudgetAmountDetailsManager {

    public String getAllPreviousYears(String fromDate, String toDate) throws Exception {

        String FYTable = ApplicationConstants.USG_DB1 + ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE + "`";
        String FYQuery = "select emp._id as idStr,emp.fromDate,emp.toDate from " + FYTable + ""
                + " as emp where emp.fromDate < \"" + fromDate + "\"   and emp.status=\"Active\"";
        RestClient aql = new RestClient();
        String FYOutput = aql.getRestData(ApplicationConstants.END_POINT, FYQuery);

        return FYOutput;
    }

    public String Save(PreviousBudgetAmountDetails preBudgetAmtDet, String userId) throws Exception {

        String status;
        if (userId == null) {
            return null;
        }

        HashMap map = new HashMap();

        map.put("ddo", preBudgetAmtDet.getDdo());
        map.put("location", preBudgetAmtDet.getLocation());
        map.put("fundType", preBudgetAmtDet.getFundType());
        map.put("sector", preBudgetAmtDet.getSector());
        map.put("financialYear", preBudgetAmtDet.getFinancialYear());
        map.put("budgetHead", preBudgetAmtDet.getBudgetHead());
        map.put("budgetType", preBudgetAmtDet.getBudgetType());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map)) {
            return ApplicationConstants.DUPLICATE;

        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        preBudgetAmtDet.setCreateDate(System.currentTimeMillis() + "");
        preBudgetAmtDet.setCreatedBy(userName);
        preBudgetAmtDet.setUpdateDate(System.currentTimeMillis() + "");
        preBudgetAmtDet.setUpdatedBy(userName);
        preBudgetAmtDet.setStatus(ApplicationConstants.ACTIVE);

        String json = new Gson().toJson(preBudgetAmtDet);

        String result = DBManager.getDbConnection().insert(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, json);

        if (result != null) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;
    }

    public String View(String ddo, String location, String year) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("ddo", ddo);
        map.put("location", location);
        map.put("financialYear", year);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map);

        if (result == null) {
            return null;
        }

        List<PreviousBudgetAmountDetails> list = new Gson().fromJson(result, new TypeToken<List<PreviousBudgetAmountDetails>>() {
        }.getType());

        list = getDDO(list);
        list = getBudgetHead(list);

        return new Gson().toJson(list);
    }

    private List<PreviousBudgetAmountDetails> getDDO(List<PreviousBudgetAmountDetails> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getDdo())) {
                    list.get(i).setDdo(entry.getValue());
                }
            }
        }
        return list;
    }

    private List<PreviousBudgetAmountDetails> getBudgetHead(List<PreviousBudgetAmountDetails> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetHead())) {
                    list.get(i).setBudgetHead(entry.getValue());
                }
            }
        }
        return list;
    }

    public String Update(PreviousBudgetAmountDetails preBudgetAmtDet, String userId, String id) throws Exception {

        String status;
        if (userId == null || id == null) {
            return null;
        }

        HashMap map = new HashMap();

        map.put("ddo", preBudgetAmtDet.getDdo());
        map.put("location", preBudgetAmtDet.getLocation());
        map.put("fundType", preBudgetAmtDet.getFundType());
        map.put("sector", preBudgetAmtDet.getSector());
        map.put("financialYear", preBudgetAmtDet.getFinancialYear());
        map.put("budgetHead", preBudgetAmtDet.getBudgetHead());
        map.put("budgetType", preBudgetAmtDet.getBudgetType());

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map, id)) {
            return ApplicationConstants.DUPLICATE;

        }

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id);

        if (dbStr == null) {
            return null;
        }

        List<PreviousBudgetAmountDetails> list = new Gson().fromJson(dbStr, new TypeToken<List<PreviousBudgetAmountDetails>>() {
        }.getType());

        PreviousBudgetAmountDetails obj = list.get(0);
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        preBudgetAmtDet.setCreateDate(obj.getCreateDate());
        preBudgetAmtDet.setCreatedBy(obj.getCreatedBy());
        preBudgetAmtDet.setUpdateDate(System.currentTimeMillis() + "");
        preBudgetAmtDet.setUpdatedBy(userName);
        preBudgetAmtDet.setStatus(ApplicationConstants.ACTIVE);

        String json = new Gson().toJson(preBudgetAmtDet);

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id, json);

        if (result) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;

    }

    public String Delete(String id, String userId) throws Exception {
        String result;
        if (id == null || userId == null) {
            return null;
        }

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id);

        if (dbStr == null) {
            return null;
        }

        List<PreviousBudgetAmountDetails> list = new Gson().fromJson(dbStr, new TypeToken<List<PreviousBudgetAmountDetails>>() {
        }.getType());

        PreviousBudgetAmountDetails obj = list.get(0);
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        obj.setStatus(ApplicationConstants.DELETE);
        obj.setDeletedBy(userName);
        obj.setUpdateDate(System.currentTimeMillis() + "");

        String json = new Gson().toJson(obj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, id, json);

        if (status) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

}
