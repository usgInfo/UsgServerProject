/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.ConsolidateDepartmentExpence;
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
 * @author upendra
 */
public class ConsolidateDeptExpenseManager {

    public static String SUBMIT = "Submit";

    public String search(ConsolidateDepartmentExpence obj, String condition, List deptData) throws Exception {
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
//        if (obj.getDepartment().isEmpty() == false) {
//            conditionMap.put("department", obj.getDepartment());
//        }
        String result1 = "";
        if (condition.equalsIgnoreCase("SearchInConsolidateTable")) {
            List<ConsolidateDepartmentExpence> finallist = new ArrayList<ConsolidateDepartmentExpence>();
            if (obj.getSector().isEmpty() == false) {
                conditionMap.put("sector", obj.getSector());
            }
            if (obj.getConsolidateBudgetStatus().isEmpty() == false) {
                conditionMap.put("consolidateBudgetStatus", obj.getConsolidateBudgetStatus());
            }
            //System.out.println("************************************First Method************************************");
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, conditionMap);
            List<ConsolidateDepartmentExpence> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentExpence>>() {
            }.getType());
            for (ConsolidateDepartmentExpence cl : list) {
                List<String> incomeIds = cl.getIncomeBudgetIdList();
                List<String> deptIds = new ArrayList<String>();
                String process = "stop";
                for (String id : incomeIds) {
                    String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, id);
                    List<CreateBudgetExpense> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<CreateBudgetExpense>>() {
                    }.getType());
                    CreateBudgetExpense obj1 = incomeBudgetList.get(0);
                    deptIds.add(obj1.getDepartment());
                }
                for (int i = 0; i < deptIds.size(); i++) {
                    if (deptData.contains(deptIds.get(i))) {
                        process = "start";
                        break;
                    }
                }
                if (process.equalsIgnoreCase("start")) {
                    finallist.add(cl);
                }

            }
            try {
                finallist = getBudgetHeadAndDescForExpense(finallist);
            } catch (Exception e) {
            }
            return new Gson().toJson(finallist);
        } else if (condition.equalsIgnoreCase("IncomeBudget")) {
            //System.out.println("************************************************Second Method**************************************");
            conditionMap.put(ApplicationConstants.INCOME_BUDGET_STATUS, ApplicationConstants.IS_CONSOLIDATED_TRUE);
            if (obj.getSector().isEmpty() == false) {
                conditionMap.put("sector", obj.getSector());
            }
            if (obj.getDdo().isEmpty() == false) {
                conditionMap.put("ddo", obj.getDdo());
            }
            if (obj.getLocation().isEmpty() == false) {
                conditionMap.put("location", obj.getLocation());
            }
            if (obj.getBudgetHead().isEmpty() == false) {
                conditionMap.put("budgetHead", obj.getBudgetHead());
            }
            //System.out.println(new Gson().toJson(conditionMap));
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
            //System.out.println(result1);
            List<CreateBudgetExpense> list = new Gson().fromJson(result1, new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
            //map used to sum up the requested amount based on budget head code
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            HashMap<String, String> budgetmap = new HashMap<String, String>();
            //BudgetHeadIncomeBudgetIdmap is used to store the all income budget id based on budget head code 
            HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap = new HashMap<String, ArrayList<String>>();
            // Consolidated List based on budget head code
            List<ConsolidateDepartmentExpence> incomebudgetHeadList = new ArrayList<ConsolidateDepartmentExpence>();
            for (Iterator<CreateBudgetExpense> iterator = list.iterator(); iterator.hasNext();) {
                CreateBudgetExpense next = iterator.next();
                if (deptData.contains(next.getDepartment())) {
                    String flag = "false";
                    try {
                        flag = next.getIsConsolidate();
                    } catch (Exception e) {
                        flag = "false";
                    }
                    //System.out.println("Before If Loop" + flag);
                    if (flag == null || flag.equalsIgnoreCase("false")) {
                        if (!next.getRequestedAmount().equalsIgnoreCase("0")) {
                            //System.out.println("After If Loop");
                            if (map.get(next.getLedgerId()) == null) {
                                map.put(next.getLedgerId(), Integer.parseInt(next.getRequestedAmount()));
                                budgetmap.put(next.getLedgerId(), next.getBudgetHead());
                                try {
                                    BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
                                } catch (Exception e) {
                                    //System.out.println("Exception" + e);
                                }
                            } else {
                                int total = map.get(next.getLedgerId());
                                total = total + Integer.parseInt(next.getRequestedAmount());
                                try {
                                    BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
                                } catch (Exception e) {
                                    //System.out.println("Exception" + e);
                                }
                                map.put(next.getLedgerId(), total);
                            }
                        }
                    } else if (flag.equalsIgnoreCase("true")) {
                        //System.out.println("Fals is true");
                    }
                }
            }
            //System.out.println("Map Size " + map.size());
            if (map.size() > 0) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    ConsolidateDepartmentExpence ob = new ConsolidateDepartmentExpence();
                    ob.setIncomeBudgetIdList(BudgetHeadIncomeBudgetIdmap.get(key));
                    ob.setBudgetHead(budgetmap.get(key));
                    ob.setBudgetHeadName(budgetmap.get(key));
                    ob.setLedgerName(key);
                    ob.setLedgerId(key);
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

    private List<ConsolidateDepartmentExpence> getBudgetHeadAndDescForExpense(List<ConsolidateDepartmentExpence> consolidateBudgetHeadList) throws Exception {
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

    public HashMap<String, ArrayList<String>> addBudgetCodeIncomeBudgetId(HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap, CreateBudgetExpense obj) {
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

    private List<ConsolidateDepartmentExpence> getBudgetHeadAndDescriptionForExpense(List<ConsolidateDepartmentExpence> incomebudgetHeadList) throws Exception {
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

    public String save(ConsolidateDepartmentExpence consolidateExpenseBudget, String loginUserId, String srNo) throws Exception {
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
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, consolidateExpenseBudgetJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public boolean updateIsConsolidateFlagOfExpenseBudget(String id, String userName) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, id);
        List<CreateBudgetExpense> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        CreateBudgetExpense obj = incomeBudgetList.get(0);
        obj.setIsConsolidated(ApplicationConstants.IS_CONSOLIDATED_TRUE);
        obj.setIsConsolidate(ApplicationConstants.IS_CONSOLIDATED_TRUE);
        obj.setConsolidatedTime(System.currentTimeMillis() + "");
//        obj.setConsolidatedTime(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, id, new Gson().toJson(obj));
        return result;
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ConsolidateDepartmentExpence>() {
        }.getType();
        String consolidateDeptBudget = new ConsolidateDeptExpenseManager().fetch(Id);
        if (consolidateDeptBudget == null || consolidateDeptBudget.isEmpty()) {
            return false;
        }
        ConsolidateDepartmentExpence consolidateDeptBudgetrJson = new Gson().fromJson(consolidateDeptBudget, type);
        consolidateDeptBudgetrJson.setConsolidateBudgetStatus(SUBMIT);
        consolidateDeptBudgetrJson.setUpdateDate(System.currentTimeMillis() + "");

        consolidateDeptBudgetrJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, Id, new Gson().toJson(consolidateDeptBudgetrJson));
        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, Id);
        List<ConsolidateDepartmentExpence> consolidateExpenseBudgetList = new Gson().fromJson(result, new TypeToken<List<ConsolidateDepartmentExpence>>() {
        }.getType());
        if (consolidateExpenseBudgetList == null || consolidateExpenseBudgetList.size() < 1) {
            return null;
        }
        return new Gson().toJson(consolidateExpenseBudgetList.get(0));

    }

    public boolean update(ConsolidateDepartmentExpence consolidateDeptexpen, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        consolidateDeptexpen.setUpdateDate(System.currentTimeMillis() + "");
        consolidateDeptexpen.setStatus(ApplicationConstants.ACTIVE);
        consolidateDeptexpen.setUpdatedBy(userName);
        String consolidateDeptJson = new Gson().toJson(consolidateDeptexpen);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, Id, consolidateDeptJson);
        return result;
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ConsolidateDepartmentExpence>() {
        }.getType();
        String consolidateDeptExpense = new ConsolidateDeptExpenseManager().fetch(Id);
        if (consolidateDeptExpense == null || consolidateDeptExpense.isEmpty()) {
            return false;
        }
        ConsolidateDepartmentExpence consolidateDeptExpenJson = new Gson().fromJson(consolidateDeptExpense, type);

        ArrayList<String> li = (ArrayList<String>) consolidateDeptExpenJson.getIncomeBudgetIdList();
        for (Iterator<String> iterator1 = li.iterator(); iterator1.hasNext();) {
            String next1 = iterator1.next();
            new ConsolidateDeptExpenseManager().updateIsConsolidateFlagOfFalseExpenseBudget(next1, userName);
        }

        consolidateDeptExpenJson.setStatus(ApplicationConstants.INACTIVE);
        consolidateDeptExpenJson.setConsolidateBudgetStatus(ApplicationConstants.DELETED);
        consolidateDeptExpenJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, Id, new Gson().toJson(consolidateDeptExpenJson));
        return result;
    }

    public boolean updateIsConsolidateFlagOfFalseExpenseBudget(String id, String userName) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, id);
        List<CreateBudgetExpense> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        CreateBudgetExpense obj = incomeBudgetList.get(0);
        obj.setIsConsolidate("False");
        obj.setConsolidatedTime(System.currentTimeMillis() + "");
        obj.setConsolidatedTime(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, id, new Gson().toJson(obj));
        return result;
    }

    public String getsrNos(ConsolidateDepartmentExpence createexpenseBudgetObj, List deptData) throws Exception {
        String finYear = createexpenseBudgetObj.getFinancialYear();
        String budgetType = createexpenseBudgetObj.getBudgetType();
        String ddo = createexpenseBudgetObj.getDdo();
        String location = createexpenseBudgetObj.getLocation();
        String fundType = createexpenseBudgetObj.getFundType();
        String sector = createexpenseBudgetObj.getSector();
        String department = createexpenseBudgetObj.getDepartment();
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
            HashMap<String, ConsolidateDepartmentExpence> map = new HashMap<String, ConsolidateDepartmentExpence>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("location", location);
            conditionMap.put("ddo", ddo);
            conditionMap.put("financialYear", finYear);
            conditionMap.put("sector", sector);
            if (department != "" && department != null) {
                conditionMap.put("department", department);
            }
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, conditionMap);
            //System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<ConsolidateDepartmentExpence> incomeBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<ConsolidateDepartmentExpence>>() {
                }.getType());
                for (ConsolidateDepartmentExpence income : incomeBudgetList) {
                    List<String> incomeIds = income.getIncomeBudgetIdList();
                    List<String> deptIds = new ArrayList<String>();
                    String process = "stop";
                    for (String id : incomeIds) {
                        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, id);
                        List<CreateBudgetExpense> incomeBudgetListData = new Gson().fromJson(existrelationJson, new TypeToken<List<CreateBudgetExpense>>() {
                        }.getType());
                        CreateBudgetExpense obj1 = incomeBudgetListData.get(0);
                        deptIds.add(obj1.getDepartment());
                    }
                    for (int i = 0; i < deptIds.size(); i++) {
                        if (deptData.contains(deptIds.get(i))) {
                            process = "start";
                            break;
                        }
                    }
                    if (process.equalsIgnoreCase("start")) {
                        map.put(income.getSrNo(), income);
                    }

                }
                incomeBudgetList.clear();
                incomeBudgetList.addAll(map.values());
                return new Gson().toJson(incomeBudgetList);
            }
        }
        return "";
    }

    public String fetchAllBasedOnFinancialYear(String year, String fundType, String sector, String budgetType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put("budgetType", budgetType);
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, conditionMap);
        return result1;
    }

    public String getSlNumber(String year, String fundType, String sector, String budgetType) throws Exception {
        String result = new ConsolidateExpenseBudgetManager().fetchAllBasedOnFinancialYear(year, fundType, sector, budgetType);
        List<ConsolidateDepartmentExpence> loanApplyList = new Gson().fromJson(result, new TypeToken<List<ConsolidateDepartmentExpence>>() {
        }.getType());

        int numb = 0;
        if (loanApplyList != null) {
            ConsolidateDepartmentExpence pv = loanApplyList.get(0);
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
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, map);
            if (result1 != null) {
                List<ConsolidateDepartmentExpence> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentExpence>>() {
                }.getType());
                for (Iterator<ConsolidateDepartmentExpence> iterator = list.iterator(); iterator.hasNext();) {
                    ConsolidateDepartmentExpence next = iterator.next();
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
