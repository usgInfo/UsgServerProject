/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.ConsolidateIncomeBudget;
import com.accure.budget.dto.SanctionUniversityIncomeBudget;
import static com.accure.budget.manager.SanctionUniversityExpenseBudgetManager.SUBMIT;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.FinancialYear;
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
 * *
 *
 * @author user
 */
public class SanctionUniversityIncomeBudgetManager {

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<SanctionUniversityIncomeBudget>() {
        }.getType();
        String SactionUniversityExpense = new SanctionUniversityIncomeBudgetManager().fetch(Id);
        if (SactionUniversityExpense == null || SactionUniversityExpense.isEmpty()) {
            return false;
        }
        SanctionUniversityIncomeBudget BudgetSanctionIncomerJson = new Gson().fromJson(SactionUniversityExpense, type);
        BudgetSanctionIncomerJson.setSanctionIncomeStatus(SUBMIT);
        BudgetSanctionIncomerJson.setUpdateDate(System.currentTimeMillis() + "");

        BudgetSanctionIncomerJson.setUpdatedBy(userName);
        boolean id = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, Id, new Gson().toJson(BudgetSanctionIncomerJson));
        if (id) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, BudgetSanctionIncomerJson.getConsolidateExpenseBudgetId());
            //System.out.println(BudgetSanctionIncomerJson.getConsolidateExpenseBudgetId() + "fdfsgffd" + result);
            List<ConsolidateIncomeBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<ConsolidateIncomeBudget>>() {
            }.getType());
            //System.out.println(BudgetSanctionIncomerJson.getApprovedAmount());
            ConsolidateIncomeBudget cseb = relationlist.get(0);
            cseb.setCreateDate(cseb.getCreateDate());
            cseb.setUpdateDate(System.currentTimeMillis() + "");
            cseb.setStatus(ApplicationConstants.ACTIVE);
            cseb.setSanctionedAmount(BudgetSanctionIncomerJson.getApprovedAmount());
            cseb.setRevisedSanctionedAmount(BudgetSanctionIncomerJson.getApprovedAmount());//upendra
            cseb.IsSanctioned(ApplicationConstants.INCOME_BUDGET_SACTION_STATUS_MEAASGE);
            String incomeJson = new Gson().toJson(cseb);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, BudgetSanctionIncomerJson.getConsolidateExpenseBudgetId(), incomeJson);
            if (resultstaus) {
                return id;
            }

        }
        return false;
    }

    public String search(SanctionUniversityIncomeBudget obj) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("consolidateBudgetStatus", "Submit");
        conditionMap.put("isSanctioned", "false");

        if (obj.getBudgetType().isEmpty() == false) {

            conditionMap.put("budgetType", obj.getBudgetType());
        }

        if (obj.getBudgetType().isEmpty() == false) {

            conditionMap.put("budgetType", obj.getBudgetType());
        }

        if (obj.getFundType().isEmpty() == false) {
            conditionMap.put("fundType", obj.getFundType());
        }
        if (obj.getSector().isEmpty() == false) {
            conditionMap.put("sector", obj.getSector());
        }

        if (obj.getFinancialYear().isEmpty() == false) {
            conditionMap.put("financialYear", obj.getFinancialYear());
        }
        if (obj.getBudgetHead().isEmpty() == false) {
            conditionMap.put("budgetHead", obj.getBudgetHead());
        }
        if (obj.getDdo().isEmpty() == false) {
            conditionMap.put("ddo", obj.getDdo());
        }
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
        List<ConsolidateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateIncomeBudget>>() {
        }.getType());
        //System.out.println(result1 + "result1");

        return new Gson().toJson(list);

    }

    public static List<SanctionUniversityIncomeBudget> getfinancialYear(List<SanctionUniversityIncomeBudget> financialYearList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE);
        List<FinancialYear> financialList = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        for (Iterator<FinancialYear> iterator = financialList.iterator(); iterator.hasNext();) {
            FinancialYear next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getFromDate() + "-" + next.getToDate());
        }
        for (int i = 0; i < financialYearList.size(); i++) {
            String financialyearId = financialYearList.get(i).getFinancialYear();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(financialyearId)) {
                    financialYearList.get(i).setFinancialYear(value);
                }
            }
        }
        return financialYearList;
    }

    public static List<SanctionUniversityIncomeBudget> getBudgetTypeList(List<SanctionUniversityIncomeBudget> budgetTypeList) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_TYPE_TABLE);
        List<BudgetType> budgetTypeListData = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        for (Iterator<BudgetType> iterator = budgetTypeListData.iterator(); iterator.hasNext();) {
            BudgetType next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < budgetTypeList.size(); i++) {
            String budgetTypeId = budgetTypeList.get(i).getBudgetType();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(budgetTypeId)) {
                    budgetTypeList.get(i).setBudgetType(value);
                }
            }
        }
        return budgetTypeList;
    }

    public static List<SanctionUniversityIncomeBudget> getSectorList(List<SanctionUniversityIncomeBudget> sectorList) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_SECTOR_TABLE);
        List<BudgetSector> sectorListData = new Gson().fromJson(result, new TypeToken<List<BudgetSector>>() {
        }.getType());
        for (Iterator<BudgetSector> iterator = sectorListData.iterator(); iterator.hasNext();) {
            BudgetSector next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < sectorList.size(); i++) {
            String budgetTypeId = sectorList.get(i).getSector();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(budgetTypeId)) {
                    sectorList.get(i).setSector(value);
                }
            }
        }
        return sectorList;
    }

    public String searchforBudgetSanctionIncome(SanctionUniversityIncomeBudget obj) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj.getBudgetType().isEmpty() == false && obj.getBudgetType() != null) {

            conditionMap.put("budgetType", obj.getBudgetType());
        }
        if (obj.getFundType() != null) {
            conditionMap.put("fundType", obj.getFundType());
        }
        if (obj.getSector() != null) {
            conditionMap.put("sector", obj.getSector());
        }
        if (obj.getFinancialYear() != null) {
            conditionMap.put("financialYear", obj.getFinancialYear());
        }
        if (obj.getDdo() != null) {
            conditionMap.put("ddo", obj.getDdo());
        }
        if (obj.getSanctionIncomeStatus() != null) {
            conditionMap.put("sanctionIncomeStatus", obj.getSanctionIncomeStatus());
        }
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, conditionMap);
        List<SanctionUniversityIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<SanctionUniversityIncomeBudget>>() {
        }.getType());
        //System.out.println("result1" + result1);
        try {
            list = getfinancialYear(list);
        } catch (Exception e) {
        }
        try {
            list = getBudgetTypeList(list);
        } catch (Exception e) {

        }
        try {
            list = getSectorList(list);
        } catch (Exception e) {

        }

        return new Gson().toJson(list);

    }

    public boolean delete(String id) throws Exception {

        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<SanctionUniversityIncomeBudget>() {
        }.getType();
        String bank = new SanctionUniversityIncomeBudgetManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        SanctionUniversityIncomeBudget bankJson = new Gson().fromJson(bank, type);
        new SanctionUniversityIncomeBudgetManager().updateIsSanctionFlagOfFalseConsolidateIncome(bankJson.getConsolidateExpenseBudgetId());
        bankJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, id, new Gson().toJson(bankJson));
        //System.out.println("deleteResult" + result);
        return result;
    }

    public boolean updateIsSanctionFlagOfFalseConsolidateIncome(String id) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, id);
        List<ConsolidateIncomeBudget> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<ConsolidateIncomeBudget>>() {
        }.getType());
        ConsolidateIncomeBudget obj = incomeBudgetList.get(0);
        obj.IsSanctioned("false");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, id, new Gson().toJson(obj));
        return result;
    }

    public boolean update(SanctionUniversityIncomeBudget sanctionIncomeUpdate, String id, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        sanctionIncomeUpdate.setUpdateDate(System.currentTimeMillis() + "");
        sanctionIncomeUpdate.setStatus(ApplicationConstants.ACTIVE);
        sanctionIncomeUpdate.setUpdatedBy(userName);
        String SanctionIncomeBudgetrJson = new Gson().toJson(sanctionIncomeUpdate);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, id, SanctionIncomeBudgetrJson);

        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, Id);
        List<SanctionUniversityIncomeBudget> gisList = new Gson().fromJson(result, new TypeToken<List<SanctionUniversityIncomeBudget>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public String save(SanctionUniversityIncomeBudget sanctionincome, String loginUserId, String srNo) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        sanctionincome.setCreateDate(System.currentTimeMillis() + "");
        sanctionincome.setUpdateDate(System.currentTimeMillis() + "");
        sanctionincome.setStatus(ApplicationConstants.ACTIVE);
        sanctionincome.setCreatedBy(userName);
        String budgetTypeName = "";
        String finyear = sanctionincome.getFinancialYear();
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, sanctionincome.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());

            sanctionincome.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgetTypeName = BudgetTypeList.get(0).getDescription();
            budgetTypeName = budgetTypeName.substring(0, 3);
            budgetTypeName = budgetTypeName.toUpperCase();

        }
        String srNo1 = finyear + "-" + budgetTypeName + "-" + srNo;
        sanctionincome.setSrNo(srNo1);
        String sanctionIncomeJson = new Gson().toJson(sanctionincome);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, sanctionIncomeJson);
        //System.out.println("id" + Id);
        if (Id != null) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, sanctionincome.getConsolidateExpenseBudgetId());
            //System.out.println(sanctionincome.getConsolidateExpenseBudgetId() + "fdfsgffd" + result);
            List<ConsolidateIncomeBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<ConsolidateIncomeBudget>>() {
            }.getType());
            //System.out.println(sanctionincome.getApprovedAmount());
            ConsolidateIncomeBudget consolidateincomebudget = relationlist.get(0);
            consolidateincomebudget.setCreateDate(consolidateincomebudget.getCreateDate());
            consolidateincomebudget.setUpdateDate(System.currentTimeMillis() + "");
            consolidateincomebudget.setStatus(ApplicationConstants.ACTIVE);
            consolidateincomebudget.setSanctionedAmount(sanctionincome.getApprovedAmount());
            consolidateincomebudget.setRevisedSanctionedAmount(sanctionincome.getApprovedAmount());//upendra
            consolidateincomebudget.IsSanctioned(ApplicationConstants.INCOME_BUDGET_SACTION_STATUS_MEAASGE);
            String incomeJson = new Gson().toJson(consolidateincomebudget);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, sanctionincome.getConsolidateExpenseBudgetId(), incomeJson);
            if (resultstaus) {
                return Id;
            }
        }
        return null;
    }

    public String submit(SanctionUniversityIncomeBudget sanctionincome, String loginUserId, String srNo) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        String budgettypeName = "";
        sanctionincome.setCreateDate(System.currentTimeMillis() + "");
        sanctionincome.setUpdateDate(System.currentTimeMillis() + "");
        String finyear = sanctionincome.getFinancialYear();
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, sanctionincome.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());

            sanctionincome.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgettypeName = BudgetTypeList.get(0).getDescription();
            budgettypeName = budgettypeName.substring(0, 3);
            budgettypeName = budgettypeName.toUpperCase();

        }
        String srNo1 = finyear + "-" + budgettypeName + "-" + srNo;
        sanctionincome.setSrNo(srNo1);
        sanctionincome.setStatus(ApplicationConstants.ACTIVE);
        sanctionincome.setCreatedBy(userName);
        String sanctionIncomeJson = new Gson().toJson(sanctionincome);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, sanctionIncomeJson);
        if (Id != null) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, sanctionincome.getConsolidateExpenseBudgetId());
            //System.out.println(sanctionincome.getConsolidateExpenseBudgetId() + "fdfsgffd" + result);
            List<ConsolidateIncomeBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<ConsolidateIncomeBudget>>() {
            }.getType());
            //System.out.println(sanctionincome.getApprovedAmount());
            ConsolidateIncomeBudget consolidateincomebudget = relationlist.get(0);
            consolidateincomebudget.setCreateDate(consolidateincomebudget.getCreateDate());
            consolidateincomebudget.setUpdateDate(System.currentTimeMillis() + "");
            consolidateincomebudget.setStatus(ApplicationConstants.ACTIVE);
            consolidateincomebudget.setSanctionedAmount(sanctionincome.getApprovedAmount());
            consolidateincomebudget.setRevisedSanctionedAmount(sanctionincome.getApprovedAmount());//upendra
            consolidateincomebudget.IsSanctioned(ApplicationConstants.INCOME_BUDGET_SACTION_STATUS_MEAASGE);
            String incomeJson = new Gson().toJson(consolidateincomebudget);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, sanctionincome.getConsolidateExpenseBudgetId(), incomeJson);
            if (resultstaus) {
                return Id;
            }

        }
        return null;
    }

    public String getsrNos(SanctionUniversityIncomeBudget sanctionUniversityIncomeBudget) throws Exception {
        String finYear = sanctionUniversityIncomeBudget.getFinancialYear();
        String budgetType = sanctionUniversityIncomeBudget.getBudgetType();
        String fundType = sanctionUniversityIncomeBudget.getFundType();
        String budgetTypeName = "";
        if (budgetType != null) {
            String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, budgetType);
            if (budgetTypeJson != null) {
                List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
                }.getType());
                budgetTypeName = BudgetTypeList.get(0).getDescription();
            }
        }
        if (budgetTypeName.equals("Revised")) {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            HashMap<String, SanctionUniversityIncomeBudget> map = new HashMap<String, SanctionUniversityIncomeBudget>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("financialYear", finYear);
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
            //System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<SanctionUniversityIncomeBudget> incomeBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<SanctionUniversityIncomeBudget>>() {
                }.getType());
                for (SanctionUniversityIncomeBudget income : incomeBudgetList) {
                    map.put(income.getSrNo(), income);
                }
                incomeBudgetList.clear();
                incomeBudgetList.addAll(map.values());
                return new Gson().toJson(incomeBudgetList);
            }
        }
        return "";
    }

    public String fetchAllBasedOnFinancialYear(String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }

    public String fetchAllBasedOnFinancialYear(String year, String fundType, String sector, String budgetType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put("budgetType", budgetType);
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }

    public String checkDuplicate(SanctionUniversityIncomeBudget obj) {
        HashMap<String, String> duplicateConditionMap = new HashMap<String, String>();
        duplicateConditionMap.put("ddo", obj.getDdo());
        duplicateConditionMap.put("fundType", obj.getFundType());
        duplicateConditionMap.put("sector", obj.getSector());
        duplicateConditionMap.put("financialYear", obj.getFinancialYear());
        duplicateConditionMap.put("budgetType", obj.getBudgetType());
        duplicateConditionMap.put("ledgerId", obj.getLedgerId());
        duplicateConditionMap.put("consolidateExpenseBudgetId", obj.getConsolidateExpenseBudgetId());
        duplicateConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, duplicateConditionMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }
        return "proceed";
    }

    public String getSlNumber(String year, String fundType, String sector, String budgetType) throws Exception {

        String result = new SanctionUniversityIncomeBudgetManager().fetchAllBasedOnFinancialYear(year, fundType, sector, budgetType);
        List<SanctionUniversityIncomeBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<SanctionUniversityIncomeBudget>>() {
        }.getType());
        int numb = 0;
        if (loanApplyList != null) {
            SanctionUniversityIncomeBudget pv = new SanctionUniversityIncomeBudget();
            String len = pv.getSrNo();
            if (len != null && len != "") {
                String[] arr = len.split("-");
                String sNo = arr[2];
                int value = Integer.parseInt(sNo);
                return String.valueOf(value);
            }
        } else {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("financialYear", year);
            map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, map);
            if (result1 != null) {
                List<SanctionUniversityIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<SanctionUniversityIncomeBudget>>() {
                }.getType());
                for (Iterator<SanctionUniversityIncomeBudget> iterator = list.iterator(); iterator.hasNext();) {
                    SanctionUniversityIncomeBudget next = iterator.next();
                    String len = next.getSrNo();
                    if (len != null && len != "") {
                        String[] arr = len.split("-");
                        String sNo = arr[2];
                        int value = Integer.parseInt(sNo);

                        if (numb < value) {
                            numb = value;
                        }
                    }
                }
            }
        }
        numb++;

        return String.valueOf(numb);
    }

    public String getSlNumber1(String year) throws Exception {
        String result = new SanctionUniversityIncomeBudgetManager().fetchAllBasedOnFinancialYear(year);
        List<SanctionUniversityIncomeBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<SanctionUniversityIncomeBudget>>() {
        }.getType());
        int numb = 0;
        SanctionUniversityIncomeBudget pv = new SanctionUniversityIncomeBudget();
        if (loanApplyList != null) {
            for (Iterator<SanctionUniversityIncomeBudget> iterator = loanApplyList.iterator(); iterator.hasNext();) {
                SanctionUniversityIncomeBudget next = iterator.next();
                String len = next.getSrNo();
                if (len != null && len != "") {
                    String[] arr = len.split("-");
                    String sNo = arr[2];
                    int value = Integer.parseInt(sNo);

                    if (numb < value) {
                        numb = value;
                    }
                }
            }
        }
        numb++;

        return String.valueOf(numb);
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_SACTION_INCOME, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }
}
