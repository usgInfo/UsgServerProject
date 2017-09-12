/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.ConsolidateDepartmentExpence;
import com.accure.budget.dto.ConsolidateExpenseBudget;
import com.accure.budget.dto.ExpenseBudgetApproval;
import static com.accure.budget.manager.ConsolidateIncomeBudgetManager.SUBMIT;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author upendra
 */
public class ExpenseBudgetApprovalManager {

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EXPENSE_BUDGET_APPROVAL, Id);
        List<ExpenseBudgetApproval> ExpenseBudgetApprovalList = new Gson().fromJson(result, new TypeToken<List<ExpenseBudgetApproval>>() {
        }.getType());
        if (ExpenseBudgetApprovalList == null || ExpenseBudgetApprovalList.size() < 1) {
            return null;
        }
        return new Gson().toJson(ExpenseBudgetApprovalList.get(0));
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ExpenseBudgetApproval>() {
        }.getType();
        String expenseApprovalBudget = new ExpenseBudgetApprovalManager().fetch(Id);
        if (expenseApprovalBudget == null || expenseApprovalBudget.isEmpty()) {
            return false;
        }
        ExpenseBudgetApproval expenseBudgetJson = new Gson().fromJson(expenseApprovalBudget, type);
        List<String> li = null;
        String id = expenseBudgetJson.getConsolidatedExpenseId();
        String approvedAmount = expenseBudgetJson.getApprovedAmount();
        String sanctionedAmount = expenseBudgetJson.getSanctionedAmount();
        int appdAmount = Integer.parseInt(approvedAmount);
        int sanctdAmount = Integer.parseInt(sanctionedAmount);
        int remAppvdAmount = sanctdAmount - appdAmount;
        String revisedSnctionedAmount = String.valueOf(remAppvdAmount);
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, id);
        if (!result.isEmpty()) {
            List<ConsolidateExpenseBudget> list = new Gson().fromJson(result, new TypeToken<List<ConsolidateExpenseBudget>>() {
            }.getType());
            ConsolidateExpenseBudget em = list.get(0);
            li = em.getIncomeBudgetIdList();
            em.setRevisedSanctionedAmount(revisedSnctionedAmount);
            String conExpBudgetJson = new Gson().toJson(em);
            boolean Id1 = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, id, conExpBudgetJson);

        }
        if (li != null) {
            for (int i = 0; i < li.size(); i++) {
                String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, li.get(i));
                if (!result1.isEmpty()) {
                    List<ConsolidateDepartmentExpence> list1 = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentExpence>>() {
                    }.getType());
                    ConsolidateDepartmentExpence em1 = list1.get(0);
                    em1.setSanctionedAmount(Integer.toString(appdAmount));
                    em1.setRevisedSanctionedAmount(Integer.toString(appdAmount));
                    em1.setIsSanctioned(ApplicationConstants.IS_SANCTIONED_TRUE);
                    //     em1.setSentStatus(ApplicationConstants.IS_SANCTIONED_TRUE);
                    String IncBudgetJson1 = new Gson().toJson(em1);
                    boolean Id1 = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, li.get(i), IncBudgetJson1);

                }
            }
        }

        expenseBudgetJson.setConsolidateBudgetStatus(SUBMIT);
        expenseBudgetJson.setUpdateDate(System.currentTimeMillis() + "");

        expenseBudgetJson.setUpdatedBy(userName);
        boolean finalresult = DBManager.getDbConnection().update(ApplicationConstants.EXPENSE_BUDGET_APPROVAL, Id, new Gson().toJson(expenseBudgetJson));
        return finalresult;
    }

    public String save(ExpenseBudgetApproval next, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        next.setCreateDate(System.currentTimeMillis() + "");
        next.setStatus(ApplicationConstants.ACTIVE);
        next.setCreatedBy(userName);
        String expenseBudgetJson = new Gson().toJson(next);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.EXPENSE_BUDGET_APPROVAL, expenseBudgetJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String searchData(ExpenseBudgetApproval emp) throws Exception {

        PropertiesConfiguration config = getConfig();
        BasicDBObject regexQuery = new BasicDBObject();
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EXPENSE_BUDGET_APPROVAL);

        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
        if (emp.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));
        }
        if (emp.getBudgetType() != null) {
            regexQuery.put("budgetType",
                    new BasicDBObject("$regex", emp.getBudgetType()));
        }
        if (emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", emp.getBudgetHead()));
        }
        if (emp.getFundtype() != null) {
            regexQuery.put("fundtype",
                    new BasicDBObject("$regex", emp.getFundtype()));
        }
        if (emp.getSector() != null) {
            regexQuery.put("sector",
                    new BasicDBObject("$regex", emp.getSector()));

        }
        if (emp.getFinYear() != null) {
            regexQuery.put("finYear",
                    new BasicDBObject("$regex", emp.getFinYear()));

        }

        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        if (emp.getConsolidateBudgetStatus().isEmpty() == false) {
            regexQuery.put("consolidateBudgetStatus",
                    new BasicDBObject("$regex", emp.getConsolidateBudgetStatus()));

        }
        //Fetching Data from Income Budget
        DBCursor cursor2 = collection.find(regexQuery);

        List<ExpenseBudgetApproval> finalList = new ArrayList<ExpenseBudgetApproval>();

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<ExpenseBudgetApproval>() {
            }.getType();
            ExpenseBudgetApproval em = new Gson().fromJson(ob.toString(), type);
            finalList.add(em);
        }

        return new Gson().toJson(finalList);
    }

    public String search(String ddo, String fundType, String sector, String finyear, String budgethead, String location) throws Exception {

        String result = "no";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("fundType", fundType);
        conditionMap.put("financialYear", finyear);
        if (budgethead.isEmpty() == false) {
            conditionMap.put("budgetHead", budgethead);
            }
        conditionMap.put("sector", sector);
        conditionMap.put("location", location);
        conditionMap.put("isSanctioned", "false");
        try {
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, conditionMap);
        } catch (Exception e) {

        }
        List<ConsolidateDepartmentExpence> ddoList = new Gson().fromJson(result, new TypeToken<List<ConsolidateDepartmentExpence>>() {
        }.getType());
        List<String> listId = new ArrayList<String>();
        List<ConsolidateExpenseBudget> finallist = new ArrayList<ConsolidateExpenseBudget>();
        for (ConsolidateDepartmentExpence cl : ddoList) {
            cl.setHeadCode(cl.getHeadCode());

            HashMap<String, String> conexpMap = new HashMap<String, String>();
            conexpMap.put("ledgerId", cl.getLedgerId());
            conexpMap.put("status", ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_EXPENSE_TABLE, conexpMap);

            List<ConsolidateExpenseBudget> conexpbudlist = new Gson().fromJson(result1, new TypeToken<List<ConsolidateExpenseBudget>>() {
            }.getType());
            String id = ((LinkedTreeMap<String, String>) cl.getId()).get("$oid");
            for (ConsolidateExpenseBudget gal1 : conexpbudlist) {
                List<String> li = gal1.getIncomeBudgetIdList();
                if (li.contains(id) && !listId.contains(id)) {
                    try {
//                        cl.setSanctionedAmount(gal1.getRevisedSanctionedAmount());
//                        cl.setConsolidatedExpenseId(((LinkedTreeMap<String, String>) gal1.getId()).get("$oid"));
//                        cl.setConsolidateDeptExpenseId(((LinkedTreeMap<String, String>) cl.getId()).get("$oid"));
                        gal1.setDepartmentName(cl.getDepartmentName());
                        finallist.add(gal1);
                        listId.addAll(li);

                    } catch (Exception e) {
                    }
                    break;
                }

            }
        }

        return new Gson().toJson(finallist);
    }
    public boolean update(ExpenseBudgetApproval exenseBudget, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        exenseBudget.setUpdateDate(System.currentTimeMillis() + "");
        exenseBudget.setStatus(ApplicationConstants.ACTIVE);
        exenseBudget.setUpdatedBy(userName);
        String expenseBudgetJson = new Gson().toJson(exenseBudget);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EXPENSE_BUDGET_APPROVAL, Id, expenseBudgetJson);
        return result;
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ExpenseBudgetApproval>() {
        }.getType();
        String expenseBudget = new ExpenseBudgetApprovalManager().fetch(Id);
        if (expenseBudget == null || expenseBudget.isEmpty()) {
            return false;
        }
        ExpenseBudgetApproval expenseBudgetJson = new Gson().fromJson(expenseBudget, type);
        expenseBudgetJson.setStatus(ApplicationConstants.INACTIVE);
        expenseBudgetJson.setConsolidateBudgetStatus(ApplicationConstants.DELETED);
        expenseBudgetJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EXPENSE_BUDGET_APPROVAL, Id, new Gson().toJson(expenseBudgetJson));
        return result;
    }
}
