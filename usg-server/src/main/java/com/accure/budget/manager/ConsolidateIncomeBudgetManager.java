/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.ConsolidateDepartmentIncome;
import com.accure.budget.dto.ConsolidateIncomeBudget;
import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.budget.dto.FinancialYear;
import com.accure.budget.dto.FundType;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.budget.dto.Sector;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
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
public class ConsolidateIncomeBudgetManager {

    public static String SUBMIT = "Submit";

    public String save(ConsolidateIncomeBudget consolidateExpenseBudget, String loginUserId, String srNo) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        consolidateExpenseBudget.setCreateDate(System.currentTimeMillis() + "");
        consolidateExpenseBudget.setStatus(ApplicationConstants.ACTIVE);
        String budgetTypeName = "";
        consolidateExpenseBudget.setCreatedBy(userName);
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
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, consolidateExpenseBudgetJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String getsrNos(ConsolidateIncomeBudget consolidateIncomeBudget) throws Exception {
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
            HashMap<String, ConsolidateIncomeBudget> map = new HashMap<String, ConsolidateIncomeBudget>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("financialYear", finYear);
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
            //System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<ConsolidateIncomeBudget> incomeBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<ConsolidateIncomeBudget>>() {
                }.getType());
                for (ConsolidateIncomeBudget income : incomeBudgetList) {
                    map.put(income.getSrNo(), income);
                }
                incomeBudgetList.clear();
                incomeBudgetList.addAll(map.values());
                return new Gson().toJson(incomeBudgetList);
            }
        }
        return "";
    }

    public boolean updateIsConsolidateFlagOfIncomeBudget(String id, String userName) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, id);
        List<ConsolidateDepartmentIncome> incomeBudgetList = new Gson().fromJson(existrelationJson, new TypeToken<List<ConsolidateDepartmentIncome>>() {
        }.getType());
        ConsolidateDepartmentIncome obj = incomeBudgetList.get(0);
        obj.setIsConsolidated(ApplicationConstants.IS_CONSOLIDATED_TRUE);
//        obj.setConsolidatedTime(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, id, new Gson().toJson(obj));
        return result;
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, Id);
        List<ConsolidateIncomeBudget> consolidateExpenseBudgetList = new Gson().fromJson(result, new TypeToken<List<ConsolidateIncomeBudget>>() {
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

        Type type = new TypeToken<ConsolidateIncomeBudget>() {
        }.getType();
        String consolidateExpenseBudget = new ConsolidateIncomeBudgetManager().fetch(Id);
        if (consolidateExpenseBudget == null || consolidateExpenseBudget.isEmpty()) {
            return false;
        }
        ConsolidateIncomeBudget consolidateExpenseBudgetrJson = new Gson().fromJson(consolidateExpenseBudget, type);
        consolidateExpenseBudgetrJson.setStatus(ApplicationConstants.INACTIVE);
        consolidateExpenseBudgetrJson.setConsolidateBudgetStatus(ApplicationConstants.DELETED);
        consolidateExpenseBudgetrJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, Id, new Gson().toJson(consolidateExpenseBudgetrJson));
        return result;
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ConsolidateIncomeBudget>() {
        }.getType();
        String consolidateExpenseBudget = new ConsolidateIncomeBudgetManager().fetch(Id);
        if (consolidateExpenseBudget == null || consolidateExpenseBudget.isEmpty()) {
            return false;
        }
        ConsolidateIncomeBudget consolidateExpenseBudgetrJson = new Gson().fromJson(consolidateExpenseBudget, type);
        consolidateExpenseBudgetrJson.setConsolidateBudgetStatus(SUBMIT);
        consolidateExpenseBudgetrJson.setUpdateDate(System.currentTimeMillis() + "");

        consolidateExpenseBudgetrJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, Id, new Gson().toJson(consolidateExpenseBudgetrJson));
        return result;
    }

    public boolean update(ConsolidateIncomeBudget consolidateExpenseBudget, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        consolidateExpenseBudget.setUpdateDate(System.currentTimeMillis() + "");
        consolidateExpenseBudget.setStatus(ApplicationConstants.ACTIVE);
        consolidateExpenseBudget.setUpdatedBy(userName);
        String consolidateExpenseBudgetrJson = new Gson().toJson(consolidateExpenseBudget);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, Id, consolidateExpenseBudgetrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }

    public String search(ConsolidateIncomeBudget obj, String condition) throws Exception {
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
            if (obj.getConsolidateBudgetStatus().isEmpty() == false) {
                conditionMap.put("consolidateBudgetStatus", obj.getConsolidateBudgetStatus());
            }
            //System.out.println("First Method");
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
            List<ConsolidateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateIncomeBudget>>() {
            }.getType());
            //System.out.println(new Gson().toJson(list));
            try {
                list = getDDO(list);
            } catch (Exception e) {
            }
            try {
                list = getFinancialYear(list);
            } catch (Exception e) {
            }
            try {
                list = getFundType(list);
            } catch (Exception e) {
            }
            try {
                list = getSector(list);
            } catch (Exception e) {
            }
            try {
                list = getBudgetHead(list);
            } catch (Exception e) {
            }
            //System.out.println(new Gson().toJson(list));
            return new Gson().toJson(list);
        } else if (condition.equalsIgnoreCase("IncomeBudget")) {
            //System.out.println("************************************************Second Method**************************************");
            //      conditionMap.put(ApplicationConstants.INCOME_BUDGET_STATUS, ApplicationConstants.INCOME_BUDGET_STATUS_MEAASGE);
            conditionMap.put("consolidateBudgetStatus", "Submit");
            conditionMap.put("isConsolidated", "false");
            if (obj.getSector().isEmpty() == false) {
                conditionMap.put("sector", obj.getSector());
            }
            if (obj.getBudgetHead().isEmpty() == false) {
                conditionMap.put("budgetHead", obj.getBudgetHead());
            }

            //System.out.println(new Gson().toJson(conditionMap));
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, conditionMap);
            //System.out.println(result1);
            List<ConsolidateDepartmentIncome> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentIncome>>() {
            }.getType());
            //map used to sum up the requested amount based on budget head code
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            HashMap<String, String> budgetmap = new HashMap<String, String>();
            //BudgetHeadIncomeBudgetIdmap is used to store the all income budget id based on budget head code 
            HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap = new HashMap<String, ArrayList<String>>();
            // Consolidated List based on budget head code
            List<ConsolidateIncomeBudget> incomebudgetHeadList = new ArrayList<ConsolidateIncomeBudget>();
            for (Iterator<ConsolidateDepartmentIncome> iterator = list.iterator(); iterator.hasNext();) {
                ConsolidateDepartmentIncome next = iterator.next();
                String flag = "false";
                try {
                    flag = next.getIsConsolidated();
                } catch (Exception e) {
                    flag = "false";
                }
                //System.out.println("Before If Loop" + flag);
                if (flag == null || flag.equalsIgnoreCase("false")) {
                    //System.out.println("After If Loop");
                    if (map.get(next.getLedgerId()) == null) {
                        map.put(next.getLedgerId(), Integer.parseInt(next.getAskedForAmount()));
                        budgetmap.put(next.getLedgerId(), next.getBudgetHead());
                        try {
                            BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
                        } catch (Exception e) {
                            //System.out.println("Exception" + e);
                        }
                    } else {
                        int total = map.get(next.getLedgerId());
                        total = total + Integer.parseInt(next.getAskedForAmount());
                        try {
                            BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
                        } catch (Exception e) {
                            //System.out.println("Exception" + e);
                        }
                        map.put(next.getLedgerId(), total);
                    }
                } else if (flag.equalsIgnoreCase("true")) {
                    //System.out.println("Fals is true");
                }
            }
            //System.out.println("Map Size " + map.size());
            if (map.size() > 0) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    ConsolidateIncomeBudget ob = new ConsolidateIncomeBudget();
                    ob.setIncomeBudgetIdList(BudgetHeadIncomeBudgetIdmap.get(key));
                    ob.setDdo(obj.getDdo());
//                    ob.setBudgetHead(obj.getBudgetHead());
//                    ob.setBudgetHeadName(obj.getBudgetHead());
                    //      ob.setHeadCode(key);
                    ob.setBudgetHead(budgetmap.get(key));
                    ob.setBudgetHeadName(budgetmap.get(key));
                    ob.setLedger(key);
                    ob.setLedgerId(key);
                    ob.setRequestedAmount(Integer.toString(value));
                    ob.setFundType(obj.getFundType());
                    ob.setSector(obj.getSector());
                    ob.setFinancialYear(obj.getFinancialYear());
                    ob.setBudgetType(obj.getBudgetType());
                    incomebudgetHeadList.add(ob);

                }
                try {
                    incomebudgetHeadList = getBudgetHeadAndDescriptionForIncome(incomebudgetHeadList);
                } catch (Exception e) {
                }
            }
            //System.out.println(new Gson().toJson(incomebudgetHeadList));
            return new Gson().toJson(incomebudgetHeadList);
        } else {
            //System.out.println("Third Method");
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, conditionMap);

            List<ConsolidateDepartmentIncome> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentIncome>>() {
            }.getType());
            //System.out.println(new Gson().toJson(list));
            try {
                list = getFinancialYearForIncome(list);
            } catch (Exception e) {
            }
            try {
                list = getFundTypeForIncome(list);
            } catch (Exception e) {
            }
            try {
                list = getSectorForIncome(list);
            } catch (Exception e) {
            }
            try {
                list = getBudgetHeadForIncome(list);
            } catch (Exception e) {
            }
            //System.out.println(new Gson().toJson(list));
            return new Gson().toJson(list);
        }

    }

    public HashMap<String, ArrayList<String>> addBudgetCodeIncomeBudgetId(HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap, ConsolidateDepartmentIncome obj) {
        Map<String, String> idMap = new HashMap<String, String>();
        idMap.put(((LinkedTreeMap<String, String>) obj.getId()).get("$oid"), "");
        ArrayList<String> li = new ArrayList<String>();
        li = (ArrayList<String>) BudgetHeadIncomeBudgetIdmap.get(obj.getLedgerId());
        if (li != null) {
            if (li.size() > 0) {
                //System.out.println("Inside Li size > o");
                ArrayList<String> lii = new ArrayList<String>();
                for (Map.Entry<String, String> entry : idMap.entrySet()) {
                    String key = entry.getKey();
                    //System.out.println(key);
                    lii.addAll(li);
                    lii.add(key);
                }
                BudgetHeadIncomeBudgetIdmap.put(obj.getLedgerId(), lii);
            }
        } else {
            //System.out.println("Null");
            ArrayList<String> lii = new ArrayList<String>();
            for (Map.Entry<String, String> entry : idMap.entrySet()) {
                String key = entry.getKey();
                //System.out.println(key);
                lii.add(key);
            }
            BudgetHeadIncomeBudgetIdmap.put(obj.getLedgerId(), lii);
        }
        return BudgetHeadIncomeBudgetIdmap;
    }

    public String searchFromIncomeBudget(ConsolidateIncomeBudget obj, String condition) throws Exception {
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
        result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());

        return new Gson().toJson(list);

    }

    public String getIncomeBudgetId() throws Exception {
        String result1 = DBManager.getDbConnection().fetchDistinctList(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, "incomeBudgetId");
        List<CreateIncomeBudget> list = new ArrayList<CreateIncomeBudget>();
        Type type = new TypeToken<String[]>() {
        }.getType();
        String incomebudgetId[] = new Gson().fromJson(result1, type);
        for (int i = 0; i < incomebudgetId.length; i++) {
            CreateIncomeBudget ob = new CreateIncomeBudget();
            ob.setId(incomebudgetId[i]);
            list.add(ob);
        }
        return new Gson().toJson(list);
    }

    public List<ConsolidateIncomeBudget> removeDuplicates() throws Exception {
        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET);
        List<ConsolidateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateIncomeBudget>>() {
        }.getType());
        return list;
    }

    private List<ConsolidateIncomeBudget> getDDO(List<ConsolidateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getDdo())) {
                    list.get(i).setDdo((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateIncomeBudget> getFinancialYear(List<ConsolidateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE);
        List<FinancialYear> religionList = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        for (Iterator<FinancialYear> iterator = religionList.iterator(); iterator.hasNext();) {
            FinancialYear next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getFromDate() + "-" + next.getToDate());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFinancialYear())) {
                    list.get(i).setFinancialYear((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateIncomeBudget> getFundType(List<ConsolidateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFundType())) {
                    list.get(i).setFundType((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateIncomeBudget> getBudgetType(List<ConsolidateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_TYPE_TABLE);
        List<BudgetType> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        for (Iterator<BudgetType> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetType())) {
                    list.get(i).setBudgetType((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateIncomeBudget> getSector(List<ConsolidateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_SECTOR_TABLE);
        List<Sector> religionList = new Gson().fromJson(result, new TypeToken<List<Sector>>() {
        }.getType());
        for (Iterator<Sector> iterator = religionList.iterator(); iterator.hasNext();) {
            Sector next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getSectorName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getSector())) {
                    list.get(i).setSector((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateIncomeBudget> getBudgetHead(List<ConsolidateIncomeBudget> list) throws Exception {
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

    private List<CreateIncomeBudget> getDDOForIncome(List<CreateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getDdo())) {
                    list.get(i).setDdoName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateDepartmentIncome> getFinancialYearForIncome(List<ConsolidateDepartmentIncome> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE);
        List<FinancialYear> religionList = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        for (Iterator<FinancialYear> iterator = religionList.iterator(); iterator.hasNext();) {
            FinancialYear next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getFromDate() + "-" + next.getToDate());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFinancialYear())) {
                    list.get(i).setFinancialYearName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateDepartmentIncome> getFundTypeForIncome(List<ConsolidateDepartmentIncome> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFundType())) {
                    list.get(i).setFundTypeName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateDepartmentIncome> getBudgetTypeForIncome(List<ConsolidateDepartmentIncome> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_TYPE_TABLE);
        List<BudgetType> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        for (Iterator<BudgetType> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetType())) {
                    list.get(i).setBudgetTypeName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateDepartmentIncome> getSectorForIncome(List<ConsolidateDepartmentIncome> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_SECTOR_TABLE);
        List<Sector> religionList = new Gson().fromJson(result, new TypeToken<List<Sector>>() {
        }.getType());
        for (Iterator<Sector> iterator = religionList.iterator(); iterator.hasNext();) {
            Sector next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getSectorName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getSector())) {
                    list.get(i).setSectorName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ConsolidateDepartmentIncome> getBudgetHeadForIncome(List<ConsolidateDepartmentIncome> list) throws Exception {
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
                    list.get(i).setBudgetHeadName((entry.getValue()));
                }
            }
        }
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHeadDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetHead())) {
                    list.get(i).setHeadDescriptionName(entry.getValue());
                }
            }
        }
        return list;
    }

    private List<ConsolidateIncomeBudget> getBudgetHeadAndDescriptionForIncome(List<ConsolidateIncomeBudget> incomebudgetHeadList) throws Exception {
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
                    incomebudgetHeadList.get(i).setLedger((entry.getValue()));
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

    public String fetchAllBasedOnFinancialYear(String year, String fundType, String sector, String budgetHead) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put("budgetHead", budgetHead);
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
//        List<ConsolidateExpenseBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
//        }.getType());
//        return new Gson().toJson(list);
        return result1;
    }

    public String getSlNumber(String year, String fundType, String sector, String budgetHead) throws Exception {
        String result = new ConsolidateIncomeBudgetManager().fetchAllBasedOnFinancialYear(year, fundType, sector, budgetHead);
        List<ConsolidateIncomeBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<ConsolidateIncomeBudget>>() {
        }.getType());

        int numb = 0;
        if (loanApplyList != null) {
            ConsolidateIncomeBudget pv = loanApplyList.get(0);
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
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, map);
            if (result1 != null) {
                List<ConsolidateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateIncomeBudget>>() {
                }.getType());
                for (Iterator<ConsolidateIncomeBudget> iterator = list.iterator(); iterator.hasNext();) {
                    ConsolidateIncomeBudget next = iterator.next();
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

//    public String search(ConsolidateIncomeBudget obj, String condition) throws Exception {
//        HashMap<String, String> conditionMap = new HashMap<String, String>();
//        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        if (obj.getBudgetType().isEmpty() == false) {
//            conditionMap.put("budgetType", obj.getBudgetType());
//        }
//        if (obj.getFundType().isEmpty() == false) {
//            conditionMap.put("fundType", obj.getFundType());
//        }
//        if (obj.getFinancialYear().isEmpty() == false) {
//            conditionMap.put("financialYear", obj.getFinancialYear());
//        }
//
//        String result1 = "";
//
//        if (condition.equalsIgnoreCase("SearchInConsolidateTable")) {
//            if (obj.getConsolidateBudgetStatus().isEmpty() == false) {
//                conditionMap.put("consolidateBudgetStatus", obj.getConsolidateBudgetStatus());
//            }
//            //System.out.println("First Method");
//            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conditionMap);
//            List<ConsolidateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<ConsolidateIncomeBudget>>() {
//            }.getType());
//            //System.out.println(new Gson().toJson(list));
//            try {
//                list = getDDO(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getFinancialYear(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getFundType(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getSector(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getBudgetHead(list);
//            } catch (Exception e) {
//            }
//            //System.out.println(new Gson().toJson(list));
//            return new Gson().toJson(list);
//        } else if (condition.equalsIgnoreCase("IncomeBudget")) {
//            //System.out.println("************************************************Second Method**************************************");
//            conditionMap.put(ApplicationConstants.INCOME_BUDGET_STATUS, ApplicationConstants.INCOME_BUDGET_STATUS_MEAASGE);
//            if (obj.getSector().isEmpty() == false) {
//                conditionMap.put("sector", obj.getSector());
//            }
//            Map<String, String> innerMap = new HashMap<String, String>();
//            innerMap.put(ApplicationConstants.EQUAL, ApplicationConstants.IS_CONSOLIDATED_TRUE);
//            conditionMap.put("isConsolidated", innerMap);
//            //System.out.println(new Gson().toJson(conditionMap));
//            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
//            //System.out.println(result1);
//            List<CreateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<CreateIncomeBudget>>() {
//            }.getType());
//            //map used to sum up the requested amount based on budget head code
//            HashMap<String, Integer> map = new HashMap<String, Integer>();
//            //BudgetHeadIncomeBudgetIdmap is used to store the all income budget id based on budget head code 
//            HashMap<String, ArrayList<String>> BudgetHeadIncomeBudgetIdmap = new HashMap<String, ArrayList<String>>();
//            // Consolidated List based on budget head code
//            List<ConsolidateIncomeBudget> incomebudgetHeadList = new ArrayList<ConsolidateIncomeBudget>();
//            for (Iterator<CreateIncomeBudget> iterator = list.iterator(); iterator.hasNext();) {
//                CreateIncomeBudget next = iterator.next();
//                if (map.get(next.getBudgetHead()) == null) {
//                    map.put(next.getBudgetHead(), Integer.parseInt(next.getRequestedAmount()));
//                    try {
//                        BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
//                    } catch (Exception e) {
//                        //System.out.println("Exception" + e);
//                    }
//                } else {
//                    int total = map.get(next.getBudgetHead());
//                    total = total + Integer.parseInt(next.getRequestedAmount());
//                    try {
//                        BudgetHeadIncomeBudgetIdmap = addBudgetCodeIncomeBudgetId(BudgetHeadIncomeBudgetIdmap, next);
//                    } catch (Exception e) {
//                        //System.out.println("Exception" + e);
//                    }
//                    map.put(next.getBudgetHead(), total);
//                }
//            }
//            //System.out.println("Map Size " + map.size());
//            if (map.size() > 0) {
//                for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                    String key = entry.getKey();
//                    Integer value = entry.getValue();
//                    ConsolidateIncomeBudget ob = new ConsolidateIncomeBudget();
//                    ob.setIncomeBudgetIdList(BudgetHeadIncomeBudgetIdmap.get(key));
//                    ob.setBudgetHead(key);
//                    ob.setHeadDescription(key);
//                    ob.setRequestedAmount(Integer.toString(value));
//                    ob.setFundType(obj.getFundType());
//                    ob.setSector(obj.getSector());
//                    ob.setFinancialYear(obj.getFinancialYear());
//                    ob.setBudgetType(obj.getBudgetType());
//                    incomebudgetHeadList.add(ob);
//
//                }
//            }
//            //System.out.println(new Gson().toJson(incomebudgetHeadList));
//            return new Gson().toJson(incomebudgetHeadList);
//        } else {
//            //System.out.println("Third Method");
//            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
//
//            List<CreateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<CreateIncomeBudget>>() {
//            }.getType());
//            //System.out.println(new Gson().toJson(list));
//            try {
//                list = getFinancialYearForIncome(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getFundTypeForIncome(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getSectorForIncome(list);
//            } catch (Exception e) {
//            }
//            try {
//                list = getBudgetHeadForIncome(list);
//            } catch (Exception e) {
//            }
//            //System.out.println(new Gson().toJson(list));
//            return new Gson().toJson(list);
//        }
//
//    }
