/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.ConsolidateExpenseBudget;
import com.accure.budget.dto.SanctionUniversityExpenseBudget;
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
 *
 * @author user
 */
public class SanctionUniversityExpenseBudgetManager {

    public static String SUBMIT = "Submit";

    public String search(SanctionUniversityExpenseBudget obj) throws Exception {
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
        if (obj.getDdo().isEmpty() == false) {
            conditionMap.put("ddo", obj.getDdo());
        }
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, conditionMap);
        List<SanctionUniversityExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
        }.getType());

        try {
            list = getBudgetHead(list);
        } catch (Exception e) {
        }
        try {
            list = getBudgetHeadDes(list);
        } catch (Exception e) {
        }
        return new Gson().toJson(list);

    }

    public boolean delete(String id) throws Exception {

        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<SanctionUniversityExpenseBudget>() {
        }.getType();
        //System.out.println("id" + id);
        String bank = new SanctionUniversityExpenseBudgetManager().fetch(id);
        //System.out.println(bank + "hdfgyd");
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        SanctionUniversityExpenseBudget bankJson = new Gson().fromJson(bank, type);
        new SanctionUniversityExpenseBudgetManager().updateIsSanctionFlagOfFalseConsolidateExpense(bankJson.getConsolidateExpenseBudgetId());
        bankJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, id, new Gson().toJson(bankJson));
        //System.out.println("deleteResult" + result);
        return result;
    }

    public boolean updateIsSanctionFlagOfFalseConsolidateExpense(String id) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, id);
        List<ConsolidateExpenseBudget> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<ConsolidateExpenseBudget>>() {
        }.getType());
        ConsolidateExpenseBudget obj = incomeBudgetList.get(0);
        obj.IsSanctioned("false");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, id, new Gson().toJson(obj));
        return result;
    }

    public boolean update(SanctionUniversityExpenseBudget sac, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        sac.setUpdateDate(System.currentTimeMillis() + "");
        sac.setStatus(ApplicationConstants.ACTIVE);
        sac.setUpdatedBy(userName);
        String consolidateExpenseBudgetrJson = new Gson().toJson(sac);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, Id, consolidateExpenseBudgetrJson);
        return result;
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<SanctionUniversityExpenseBudget>() {
        }.getType();
        String SactionUniversityExpense = new SanctionUniversityExpenseBudgetManager().fetch(Id);
        if (SactionUniversityExpense == null || SactionUniversityExpense.isEmpty()) {
            return false;
        }
        SanctionUniversityExpenseBudget SanctionUniversityExpenserJson = new Gson().fromJson(SactionUniversityExpense, type);
        SanctionUniversityExpenserJson.setSanctionIncomeStatus(SUBMIT);
        SanctionUniversityExpenserJson.setUpdateDate(System.currentTimeMillis() + "");

        SanctionUniversityExpenserJson.setUpdatedBy(userName);
        boolean id = DBManager.getDbConnection().update(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, Id, new Gson().toJson(SanctionUniversityExpenserJson));
        if (id) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, SanctionUniversityExpenserJson.getConsolidateExpenseBudgetId());
            //System.out.println(SanctionUniversityExpenserJson.getConsolidateExpenseBudgetId() + "fdfsgffd" + result);
            List<ConsolidateExpenseBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
            }.getType());
            //System.out.println(SanctionUniversityExpenserJson.getApprovedAmount());
            ConsolidateExpenseBudget cseb = relationlist.get(0);
            cseb.setCreateDate(cseb.getCreateDate());
            cseb.setUpdateDate(System.currentTimeMillis() + "");
            cseb.setStatus(ApplicationConstants.ACTIVE);
            cseb.SanctionedAmount(SanctionUniversityExpenserJson.getApprovedAmount());
            cseb.setRevisedSanctionedAmount(SanctionUniversityExpenserJson.getApprovedAmount());
            cseb.IsSanctioned(ApplicationConstants.INCOME_BUDGET_SACTION_STATUS_MEAASGE);
            String incomeJson = new Gson().toJson(cseb);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, SanctionUniversityExpenserJson.getConsolidateExpenseBudgetId(), incomeJson);
            if (resultstaus) {
                return id;
            }

        }
        return false;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, Id);
        List<SanctionUniversityExpenseBudget> gisList = new Gson().fromJson(result, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    private List<SanctionUniversityExpenseBudget> getBudgetHead(List<SanctionUniversityExpenseBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD);
        List<BudgetType> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        for (Iterator<BudgetType> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetHead())) {
                    list.get(i).setBudgetHead((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<SanctionUniversityExpenseBudget> getBudgetHeadDes(List<SanctionUniversityExpenseBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD);
        List<BudgetType> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        for (Iterator<BudgetType> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getHeadDescription())) {
                    list.get(i).setBudgetHead((entry.getValue()));
                }
            }
        }
        return list;
    }

    public String save(SanctionUniversityExpenseBudget sanctionexpense, String loginUserId, String srNo) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        sanctionexpense.setCreateDate(System.currentTimeMillis() + "");
        sanctionexpense.setUpdateDate(System.currentTimeMillis() + "");
        sanctionexpense.setStatus(ApplicationConstants.ACTIVE);
        sanctionexpense.setCreatedBy(userName);
        String budgetTypeName = "";
        String finyear = sanctionexpense.getFinancialYear();
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, sanctionexpense.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());

            sanctionexpense.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgetTypeName = BudgetTypeList.get(0).getDescription();
            budgetTypeName = budgetTypeName.substring(0, 3);
            budgetTypeName = budgetTypeName.toUpperCase();

        }
        String srNo1 = finyear + "-" + budgetTypeName + "-" + srNo;
        sanctionexpense.setSrNo(srNo1);
        String sanctionIncomeJson = new Gson().toJson(sanctionexpense);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, sanctionIncomeJson);
        if (Id != null) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, sanctionexpense.getConsolidateExpenseBudgetId());
            //System.out.println(sanctionincome.getConsolidateExpenseBudgetId() + "fdfsgffd" + result);
            List<ConsolidateExpenseBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
            }.getType());
            //System.out.println(sanctionincome.getApprovedAmount());
            ConsolidateExpenseBudget consolidateexpensebudget = relationlist.get(0);
            consolidateexpensebudget.setCreateDate(consolidateexpensebudget.getCreateDate());
            consolidateexpensebudget.setUpdateDate(System.currentTimeMillis() + "");
            consolidateexpensebudget.setStatus(ApplicationConstants.ACTIVE);
            consolidateexpensebudget.SanctionedAmount(sanctionexpense.getApprovedAmount());
            consolidateexpensebudget.setRevisedSanctionedAmount(sanctionexpense.getApprovedAmount());
            consolidateexpensebudget.IsSanctioned(ApplicationConstants.INCOME_BUDGET_SACTION_STATUS_MEAASGE);
            String incomeJson = new Gson().toJson(consolidateexpensebudget);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, sanctionexpense.getConsolidateExpenseBudgetId(), incomeJson);
            if (resultstaus) {
                return Id;
            }
        }
        return null;
    }

    public String submit(SanctionUniversityExpenseBudget sanctionexpense, String loginUserId, String srNo) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        sanctionexpense.setCreateDate(System.currentTimeMillis() + "");
        sanctionexpense.setUpdateDate(System.currentTimeMillis() + "");
        sanctionexpense.setStatus(ApplicationConstants.ACTIVE);
        sanctionexpense.setCreatedBy(userName);
        String finyear = sanctionexpense.getFinancialYear();
        String budgetTypeName = "";
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, sanctionexpense.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());

            sanctionexpense.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgetTypeName = BudgetTypeList.get(0).getDescription();
            budgetTypeName = budgetTypeName.substring(0, 3);
            budgetTypeName = budgetTypeName.toUpperCase();

        }
        String srNo1 = finyear + "-" + budgetTypeName + "-" + srNo;
        sanctionexpense.setSrNo(srNo1);
        String sanctionIncomeJson = new Gson().toJson(sanctionexpense);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, sanctionIncomeJson);
        if (Id != null) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, sanctionexpense.getConsolidateExpenseBudgetId());
            //System.out.println(sanctionincome.getConsolidateExpenseBudgetId() + "fdfsgffd" + result);
            List<ConsolidateExpenseBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
            }.getType());
            //System.out.println(sanctionincome.getApprovedAmount());
            ConsolidateExpenseBudget consolidateexpensebudget = relationlist.get(0);
            consolidateexpensebudget.setCreateDate(consolidateexpensebudget.getCreateDate());
            consolidateexpensebudget.setUpdateDate(System.currentTimeMillis() + "");
            consolidateexpensebudget.setStatus(ApplicationConstants.ACTIVE);
            consolidateexpensebudget.SanctionedAmount(sanctionexpense.getApprovedAmount());
            consolidateexpensebudget.setRevisedSanctionedAmount(sanctionexpense.getApprovedAmount());
            consolidateexpensebudget.IsSanctioned(ApplicationConstants.INCOME_BUDGET_SACTION_STATUS_MEAASGE);
            String incomeJson = new Gson().toJson(consolidateexpensebudget);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, sanctionexpense.getConsolidateExpenseBudgetId(), incomeJson);
            if (resultstaus) {
                return Id;
            }

        }
        return null;
    }

    public String searchforBudgetSanctionExpense(SanctionUniversityExpenseBudget obj) throws Exception {
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
        if (obj.getSanctionIncomeStatus() != null) {
            conditionMap.put("sanctionIncomeStatus", obj.getSanctionIncomeStatus());
        }
        if (obj.getDdo() != null) {
            conditionMap.put("ddo", obj.getDdo());
        }
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, conditionMap);
        List<SanctionUniversityExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
        }.getType());

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

    public static List<SanctionUniversityExpenseBudget> getBudgetTypeList(List<SanctionUniversityExpenseBudget> budgetTypeList) throws Exception {

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

    public static List<SanctionUniversityExpenseBudget> getSectorList(List<SanctionUniversityExpenseBudget> sectorList) throws Exception {

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

    public static List<SanctionUniversityExpenseBudget> getfinancialYear(List<SanctionUniversityExpenseBudget> financialYearList) throws Exception {

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

    public String getsrNos(SanctionUniversityExpenseBudget consolidateIncomeBudget) throws Exception {
        String finYear = consolidateIncomeBudget.getFinancialYear();
        String budgetType = consolidateIncomeBudget.getBudgetType();
        String fundType = consolidateIncomeBudget.getFundType();
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
            HashMap<String, SanctionUniversityExpenseBudget> map = new HashMap<String, SanctionUniversityExpenseBudget>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("financialYear", finYear);
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, conditionMap);
            //System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<SanctionUniversityExpenseBudget> expenseBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
                }.getType());
                for (SanctionUniversityExpenseBudget expense : expenseBudgetList) {
                    map.put(expense.getSrNo(), expense);
                }
                expenseBudgetList.clear();
                expenseBudgetList.addAll(map.values());
                return new Gson().toJson(expenseBudgetList);
            }
        }
        return "";
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }

    public String fetchAllBasedOnFinancilaYear(String year, String fundType, String sector, String budgetType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put("budgetType", budgetType);
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }

    public String checkDuplicate(SanctionUniversityExpenseBudget obj) {
        HashMap<String, String> duplicateConditionMap = new HashMap<String, String>();
        duplicateConditionMap.put("ddo", obj.getDdo());
        duplicateConditionMap.put("fundType", obj.getFundType());
        duplicateConditionMap.put("sector", obj.getSector());
        duplicateConditionMap.put("financialYear", obj.getFinancialYear());
        duplicateConditionMap.put("budgetType", obj.getBudgetType());
        duplicateConditionMap.put("ledgerId", obj.getLedgerId());
        duplicateConditionMap.put("consolidateExpenseBudgetId", obj.getConsolidateExpenseBudgetId());
        duplicateConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, duplicateConditionMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }
        return "proceed";
    }

    public String getSlNumber(String year, String fundType, String sector, String budgetType) throws Exception {
        String result = new SanctionUniversityExpenseBudgetManager().fetchAllBasedOnFinancilaYear(year, fundType, sector, budgetType);
        List<SanctionUniversityExpenseBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
        }.getType());
        int numb = 0;
        if (loanApplyList != null) {
            SanctionUniversityExpenseBudget pv = new SanctionUniversityExpenseBudget();
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
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SACTION_UNIVERSITY_EXPENSE, map);
            if (result1 != null) {
                List<SanctionUniversityExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
                }.getType());
                for (Iterator<SanctionUniversityExpenseBudget> iterator = list.iterator(); iterator.hasNext();) {
                    SanctionUniversityExpenseBudget next = iterator.next();
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
//    public String getSlNumber1(String year) throws Exception {
//        String result = new SanctionUniversityExpenseBudgetManager().fetchAllBasedOnFinancilaYear(year);
//        List<SanctionUniversityExpenseBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<SanctionUniversityExpenseBudget>>() {
//        }.getType());
//        int numb = 0;
//        SanctionUniversityExpenseBudget pv = new SanctionUniversityExpenseBudget();
//        if (loanApplyList != null) {
//            for (Iterator<SanctionUniversityExpenseBudget> iterator = loanApplyList.iterator(); iterator.hasNext();) {
//                SanctionUniversityExpenseBudget next = iterator.next();
//                String len = next.getSrNo();
//                if (len != null && len != "") {
//                    String[] arr = len.split("-");
//                    String sNo = arr[2];
//                    int value = Integer.parseInt(sNo);
//
//                    if (numb < value) {
//                        numb = value;
//                    }
//                }
//            }
//        }
//        numb++;
//
//        return String.valueOf(numb);
//    }
}
