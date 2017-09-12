/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.ConsolidateDepartmentExpence;
import com.accure.budget.dto.ConsolidateExpenseBudget;
import com.accure.budget.dto.CreateBudgetExpense;
import com.accure.finance.dto.Ledger;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asif
 */
public class ConsolidateExpenseBudgetManager {

    public static String SUBMIT = "Submit";

    public String save(ConsolidateExpenseBudget consolidateExpenseBudget, String loginUserId, String srNo) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        consolidateExpenseBudget.setCreateDate(System.currentTimeMillis() + "");
        consolidateExpenseBudget.setStatus(ApplicationConstants.ACTIVE);
        consolidateExpenseBudget.setCreatedBy(userName);
        String budgetTypeName = "";
        String finyear = consolidateExpenseBudget.getFinancialYear();
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, consolidateExpenseBudget.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());

            consolidateExpenseBudget.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgetTypeName = BudgetTypeList.get(0).getDescription();
            budgetTypeName = budgetTypeName.substring(0, 3);
            budgetTypeName = budgetTypeName.toUpperCase();

        }
        String srNo1 = finyear + "-" + budgetTypeName + "-" + srNo;
        consolidateExpenseBudget.setSrNo(srNo1);
        String consolidateExpenseBudgetJson = new Gson().toJson(consolidateExpenseBudget);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, consolidateExpenseBudgetJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public boolean updateIsConsolidateFlagOfExpenseBudget(String id, String userName) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, id);
        List<ConsolidateDepartmentExpence> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<ConsolidateDepartmentExpence>>() {
        }.getType());
        ConsolidateDepartmentExpence obj = incomeBudgetList.get(0);
        obj.setIsConsolidated(ApplicationConstants.IS_CONSOLIDATED_TRUE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, id, new Gson().toJson(obj));
        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, Id);
        List<ConsolidateExpenseBudget> consolidateExpenseBudgetList = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
        }.getType());
        if (consolidateExpenseBudgetList == null || consolidateExpenseBudgetList.size() < 1) {
            return null;
        }
        return new Gson().toJson(consolidateExpenseBudgetList.get(0));
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<ConsolidateExpenseBudget>() {
        }.getType();
        String consolidateExpenseBudget = new ConsolidateExpenseBudgetManager().fetch(Id);
        if (consolidateExpenseBudget == null || consolidateExpenseBudget.isEmpty()) {
            return false;
        }
        ConsolidateExpenseBudget consolidateExpenseBudgetrJson = new Gson().fromJson(consolidateExpenseBudget, type);
        consolidateExpenseBudgetrJson.setStatus(ApplicationConstants.INACTIVE);
        consolidateExpenseBudgetrJson.setConsolidateBudgetStatus(ApplicationConstants.DELETED);
        consolidateExpenseBudgetrJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, Id, new Gson().toJson(consolidateExpenseBudgetrJson));
        return result;
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<ConsolidateExpenseBudget>() {
        }.getType();
        String consolidateExpenseBudget = new ConsolidateExpenseBudgetManager().fetch(Id);
        if (consolidateExpenseBudget == null || consolidateExpenseBudget.isEmpty()) {
            return false;
        }
        ConsolidateExpenseBudget consolidateExpenseBudgetrJson = new Gson().fromJson(consolidateExpenseBudget, type);
        consolidateExpenseBudgetrJson.setConsolidateBudgetStatus(SUBMIT);
        consolidateExpenseBudgetrJson.setUpdateDate(System.currentTimeMillis() + "");
        consolidateExpenseBudgetrJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, Id, new Gson().toJson(consolidateExpenseBudgetrJson));
        return result;
    }

    public boolean update(ConsolidateExpenseBudget consolidateExpenseBudget, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        consolidateExpenseBudget.setUpdateDate(System.currentTimeMillis() + "");
        consolidateExpenseBudget.setStatus(ApplicationConstants.ACTIVE);
        consolidateExpenseBudget.setUpdatedBy(userName);
        String consolidateExpenseBudgetrJson = new Gson().toJson(consolidateExpenseBudget);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, Id, consolidateExpenseBudgetrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, conditionMap);
        return result1;
    }

    public String search(ConsolidateExpenseBudget obj, String condition) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj.getBudgetType().isEmpty() == false) {
            conditionMap.put("budgetType", obj.getBudgetType());
        }
        if (obj.getFundType().isEmpty() == false) {
            conditionMap.put("fundType", obj.getFundType());
        }
        if (obj.getFinancialYear().isEmpty() == false) {
            conditionMap.put("financialYear", obj.getFinancialYear());
        }
        if (obj.getDdo().isEmpty() == false) {
            conditionMap.put("ddo", obj.getDdo());
        }
        String result1 = "";
        if (condition.equalsIgnoreCase("SearchInConsolidateTable")) {
            if (obj.getSector().isEmpty() == false) {
                conditionMap.put("sector", obj.getSector());
            }
            if (obj.getConsolidateBudgetStatus().isEmpty() == false) {
                conditionMap.put("consolidateBudgetStatus", obj.getConsolidateBudgetStatus());
            }
            //System.out.println("************************************First Method************************************");
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, conditionMap);
            List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
            }.getType());

            try {
                list = getBudgetHeadAndDescForExpense(list);
            } catch (Exception e) {
            }
            return new Gson().toJson(list);
        } else if (condition.equalsIgnoreCase("IncomeBudget")) {
            //System.out.println("************************************************Second Method**************************************");
            //  conditionMap.put(ApplicationConstants.INCOME_BUDGET_STATUS, ApplicationConstants.IS_CONSOLIDATED_TRUE);
            conditionMap.put("consolidateBudgetStatus", "Submit");
            conditionMap.put("isConsolidated", "false");
            if (obj.getSector().isEmpty() == false) {
                conditionMap.put("sector", obj.getSector());
            }
            if (obj.getBudgetHead().isEmpty() == false) {
                conditionMap.put("budgetHead", obj.getBudgetHead());
            }
            //System.out.println(new Gson().toJson(conditionMap));
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, conditionMap);
            //System.out.println(result1);
            List<ConsolidateDepartmentExpence> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentExpence>>() {
            }.getType());
            //map used to sum up the requested amount based on budget head code
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            HashMap<String, String> budgetmap = new HashMap<String, String>();
            //BudgetHeadIncomeBudgetIdmap is used to store the all income budget id based on budget head code 
            HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap = new HashMap<String, ArrayList<String>>();
            // Consolidated List based on budget head code
            List<ConsolidateExpenseBudget> incomebudgetHeadList = new ArrayList<ConsolidateExpenseBudget>();
            for (Iterator<ConsolidateDepartmentExpence> iterator = list.iterator(); iterator.hasNext();) {
                ConsolidateDepartmentExpence next = iterator.next();
                String flag = "false";
                try {
                    flag = next.getIsConsolidated();
                } catch (Exception e) {
                    flag = "false";
                }
                if (flag == null || flag.equalsIgnoreCase("false")) {
                    if (map.get(next.getLedgerId()) == null) {
                        map.put(next.getLedgerId(), Integer.parseInt(next.getAskedForAmount()));
                        budgetmap.put(next.getLedgerId(), next.getBudgetHead());
                        try {
                            BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
                        } catch (Exception e) {
                        }
                    } else {
                        int total = map.get(next.getLedgerId());
                        total = total + Integer.parseInt(next.getAskedForAmount());
                        try {
                            BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
                        } catch (Exception e) {
                        }
                        map.put(next.getLedgerId(), total);
                    }
                } else if (flag.equalsIgnoreCase("true")) {
                }
            }
            if (map.size() > 0) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    ConsolidateExpenseBudget ob = new ConsolidateExpenseBudget();
                    ob.setIncomeBudgetIdList(BudgetHeadIncomeBudgetIdmap.get(key));
                    ob.setBudgetHead(budgetmap.get(key));
                    ob.setBudgetHeadName(budgetmap.get(key));
                    ob.setLedgerName(key);
                    ob.setLedgerId(key);
                    ob.setDdo(obj.getDdo());
                    ob.setHeadDescription(key);
                    ob.setRequestedAmount(Integer.toString(value));
                    ob.setFundType(obj.getFundType());
                    ob.setSector(obj.getSector());
                    ob.setFinancialYear(obj.getFinancialYear());
                    ob.setBudgetType(obj.getBudgetType());
                    incomebudgetHeadList.add(ob);
                }
                try {
                    incomebudgetHeadList = getBudgetHeadAndDescriptionForExpense(incomebudgetHeadList);
                } catch (Exception e) {
                }
            }
            //System.out.println(new Gson().toJson(incomebudgetHeadList));
            return new Gson().toJson(incomebudgetHeadList);
        } else {
            //System.out.println("Third Method");
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
            List<CreateBudgetExpense> list = new Gson().fromJson(result1, new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
            return new Gson().toJson(list);
        }
    }

    public HashMap<String, ArrayList<String>> addBudgetCodeIncomeBudgetId(HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap, ConsolidateDepartmentExpence obj) {
        Map<String, String> idMap = new HashMap<String, String>();
        idMap.put(((LinkedTreeMap<String, String>) obj.getId()).get("$oid"), "");
        ArrayList<String> li = new ArrayList<String>();
        li = (ArrayList<String>) BudgetHeadIncomeBudgetIdmap.get(obj.getLedgerId());
        if (li != null) {
            if (li.size() > 0) {
                ArrayList<String> lii = new ArrayList<String>();
                for (Map.Entry<String, String> entry : idMap.entrySet()) {
                    String key = entry.getKey();
                    lii.addAll(li);
                    lii.add(key);
                }
                BudgetHeadIncomeBudgetIdmap.put(obj.getLedgerId(), lii);
            }
        } else {
            ArrayList<String> lii = new ArrayList<String>();
            for (Map.Entry<String, String> entry : idMap.entrySet()) {
                String key = entry.getKey();
                lii.add(key);
            }
            BudgetHeadIncomeBudgetIdmap.put(obj.getLedgerId(), lii);
        }
        return BudgetHeadIncomeBudgetIdmap;
    }

    public String searchFromIncomeBudget(ConsolidateExpenseBudget obj, String condition) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj.getFinancialYear().isEmpty() == false) {
            conditionMap.put("financialYear", obj.getFinancialYear());
        }
        if (obj.getFundType().isEmpty() == false) {
            conditionMap.put("fundType", obj.getFundType());
        }
        if (obj.getSector().isEmpty() == false) {
            conditionMap.put("sector", obj.getSector());
        }
        if (obj.getBudgetType().isEmpty() == false) {
            conditionMap.put("budgetType", obj.getBudgetType());
        }
        String result1 = "";
        result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> list = new Gson().fromJson(result1, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());

        return new Gson().toJson(list);

    }

    public String getIncomeBudgetId() throws Exception {
        String result1 = DBManager.getDbConnection().fetchDistinctList(ApplicationConstants.BUDGET_EXPENSE_TABLE, "incomeBudgetId");
        List<CreateBudgetExpense> list = new ArrayList<CreateBudgetExpense>();
        Type type = new TypeToken<String[]>() {
        }.getType();
        String incomebudgetId[] = new Gson().fromJson(result1, type);
        for (int i = 0; i < incomebudgetId.length; i++) {
            CreateBudgetExpense ob = new CreateBudgetExpense();
            ob.setId(incomebudgetId[i]);
            list.add(ob);
        }
        return new Gson().toJson(list);
    }

    public List<ConsolidateExpenseBudget> removeDuplicates() throws Exception {
        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_EXPENSE_TABLE);
        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
        }.getType());
        return list;
    }

    private List<ConsolidateExpenseBudget> getBudgetHeadAndDescriptionForExpense(List<ConsolidateExpenseBudget> incomebudgetHeadList) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        Map<String, String> budgetMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEDGER_TABLE);
        List<Ledger> ledgerList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
        }.getType());
        for (Iterator<Ledger> iterator = ledgerList.iterator(); iterator.hasNext();) {
            Ledger next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLedgerName());
        }
        for (int i = 0; i < incomebudgetHeadList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(incomebudgetHeadList.get(i).getLedgerId())) {
                    incomebudgetHeadList.get(i).setLedgerName((entry.getValue()));
                }
            }
        }
        String budgetresult1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> headCodeList = new Gson().fromJson(budgetresult1, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = headCodeList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster budgetnext = iterator.next();
            budgetMap.put(((LinkedTreeMap<String, String>) budgetnext.getId()).get("$oid"), budgetnext.getBudgetHead());
        }
        for (int j = 0; j < incomebudgetHeadList.size(); j++) {
            for (Map.Entry<String, String> entry : budgetMap.entrySet()) {
                if (entry.getKey().equals(incomebudgetHeadList.get(j).getBudgetHeadName())) {
                    incomebudgetHeadList.get(j).setBudgetHeadName((entry.getValue()));
                }
            }
        }
        return incomebudgetHeadList;
    }

    private List<ConsolidateExpenseBudget> getBudgetHeadAndDescForExpense(List<ConsolidateExpenseBudget> consolidateBudgetHeadList) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> headCodeList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = headCodeList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < consolidateBudgetHeadList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(consolidateBudgetHeadList.get(i).getHeadCode())) {
                    consolidateBudgetHeadList.get(i).setBudgetHead((entry.getValue()));
                }
            }
        }
        for (Iterator<BudgetHeadMaster> iterator = headCodeList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHeadDescription());
        }
        for (int i = 0; i < consolidateBudgetHeadList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(consolidateBudgetHeadList.get(i).getHeadCode())) {
                    consolidateBudgetHeadList.get(i).setHeadDescription((entry.getValue()));
                }
            }
        }
        return consolidateBudgetHeadList;
    }

    public String getsrNos(ConsolidateExpenseBudget consolidateIncomeBudget) throws Exception {
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
            HashMap<String, ConsolidateExpenseBudget> map = new HashMap<String, ConsolidateExpenseBudget>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("financialYear", finYear);
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, conditionMap);
            //System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<ConsolidateExpenseBudget> expenseBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<ConsolidateExpenseBudget>>() {
                }.getType());
                for (ConsolidateExpenseBudget expense : expenseBudgetList) {
                    map.put(expense.getSrNo(), expense);
                }
                expenseBudgetList.clear();
                expenseBudgetList.addAll(map.values());
                return new Gson().toJson(expenseBudgetList);
            }
        }
        return "";
    }

    public String fetchAllBasedOnFinancialYear(String year, String fundType, String sector, String budgetHead) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put("budgetHead", budgetHead);
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, conditionMap);
        return result1;
    }

//    public String getSlNumber1(String year,String ddo,String location) throws Exception {
//        String result = new ConsolidateExpenseBudgetManager().fetchAllBasedOnFinancialYear(year,ddo,location);
//        List<ConsolidateExpenseBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        int numb = 0;
//        ConsolidateExpenseBudget pv = new ConsolidateExpenseBudget();
//        if (loanApplyList != null) {
//            for (Iterator<ConsolidateExpenseBudget> iterator = loanApplyList.iterator(); iterator.hasNext();) {
//                ConsolidateExpenseBudget next = iterator.next();
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
    public String getSlNumber(String year, String fundType, String sector, String budgetHead) throws Exception {
        String result = new ConsolidateExpenseBudgetManager().fetchAllBasedOnFinancialYear(year, fundType, sector, budgetHead);
        List<ConsolidateExpenseBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
        }.getType());

        int numb = 0;
        if (loanApplyList != null) {
            ConsolidateExpenseBudget pv = loanApplyList.get(0);
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
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, map);
            if (result1 != null) {
                List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
                }.getType());
                for (Iterator<ConsolidateExpenseBudget> iterator = list.iterator(); iterator.hasNext();) {
                    ConsolidateExpenseBudget next = iterator.next();
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
}
