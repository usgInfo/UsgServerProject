/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.ConsolidateDepartmentExpence;
import com.accure.budget.dto.CreateBudgetExpense;
import com.accure.budget.dto.DeptWiseExpBudgetAllocation;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Department;
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
 * @author user
 */
public class DepartmentWiseExpAlloManager {

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DEPTWISE_EXP_BUDGET_ALLOC_TABLE, Id);
        List<DeptWiseExpBudgetAllocation> expenseBudgetApprovalList = new Gson().fromJson(result, new TypeToken<List<DeptWiseExpBudgetAllocation>>() {
        }.getType());
        if (expenseBudgetApprovalList == null || expenseBudgetApprovalList.size() < 1) {
            return null;
        }
        return new Gson().toJson(expenseBudgetApprovalList);
    }
    public String save(DeptWiseExpBudgetAllocation expbudgetDTO, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        expbudgetDTO.setCreateDate(System.currentTimeMillis() + "");
        expbudgetDTO.setUpdateDate(System.currentTimeMillis() + "");
        expbudgetDTO.setHeadStatus(ApplicationConstants.IS_SANCTIONED_TRUE);
        expbudgetDTO.setStatus(ApplicationConstants.ACTIVE);
        expbudgetDTO.setCreatedBy(userName);
        expbudgetDTO.setExtraProvisionAmount("0");
        String id = expbudgetDTO.getConsolidatedExpenseId();
        String approvedAmount = expbudgetDTO.getApprovedAmount();
        String sanctionedAmount = expbudgetDTO.getSanctionedAmount();
        int appdAmount = Integer.parseInt(approvedAmount);
        int sanctdAmount = Integer.parseInt(sanctionedAmount);
        int remAppvdAmount = sanctdAmount - appdAmount;
        String revisedSnctionedAmount = String.valueOf(remAppvdAmount);
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, id);
        if (!result.isEmpty()) {
            List<ConsolidateDepartmentExpence> list = new Gson().fromJson(result, new TypeToken<List<ConsolidateDepartmentExpence>>() {
            }.getType());
            ConsolidateDepartmentExpence em = list.get(0);
            em.setRevisedSanctionedAmount(revisedSnctionedAmount);
            String conExpBudgetJson = new Gson().toJson(em);
            boolean Id = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, id, conExpBudgetJson);

        }
        String expBudgetId = expbudgetDTO.getCreateExpenseId();
        String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, expBudgetId);
        if (!result1.isEmpty()) {
            List<CreateBudgetExpense> list1 = new Gson().fromJson(result1, new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
            CreateBudgetExpense em1 = list1.get(0);
            em1.setSanctionedAmount(Integer.toString(appdAmount));
            em1.setIsSanctioned(ApplicationConstants.IS_SANCTIONED_TRUE);
            // em1.setSentStatus(ApplicationConstants.IS_SANCTIONED_TRUE);
            String IncBudgetJson1 = new Gson().toJson(em1);
            boolean Id1 = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, expBudgetId, IncBudgetJson1);
        }

        String expbudgetJson = new Gson().toJson(expbudgetDTO);

        String expbudgetJsonresult = DBManager.getDbConnection().insert(ApplicationConstants.DEPTWISE_EXP_BUDGET_ALLOC_TABLE, expbudgetJson);
        return expbudgetJsonresult;

    }

    public String searchData(DeptWiseExpBudgetAllocation emp) throws Exception {

        PropertiesConfiguration config = getConfig();
        BasicDBObject regexQuery = new BasicDBObject();
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.DEPTWISE_EXP_BUDGET_ALLOC_TABLE);

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

        //Fetching Data from Income Budget
        DBCursor cursor2 = collection.find(regexQuery);

        List<DeptWiseExpBudgetAllocation> finalList = new ArrayList<DeptWiseExpBudgetAllocation>();

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<DeptWiseExpBudgetAllocation>() {
            }.getType();
            DeptWiseExpBudgetAllocation em = new Gson().fromJson(ob.toString(), type);
            finalList.add(em);
        }

        return new Gson().toJson(finalList);
    }

    public String search(String ddo, String fundType, String sector, String finyear, String budgethead, String location, String department) throws Exception {
        CreateBudgetExpense object = new CreateBudgetExpense();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("fundType", fundType);
        conditionMap.put("financialYear", finyear);
        if (!budgethead.isEmpty()) {
            conditionMap.put("budgetHead", budgethead);
        }

        conditionMap.put("sector", sector);
        conditionMap.put("location", location);
        conditionMap.put("isSanctioned", ApplicationConstants.FALSE);
        conditionMap.put("department", department);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> ddoList = new Gson().fromJson(result, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());

        for (CreateBudgetExpense cl : ddoList) {
            cl.setHeadCodeId(cl.getHeadCode());
//            try {
//                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getHeadCode());
//                List<BudgetHeadMaster> gaList = new Gson().fromJson(gaJson, new TypeToken<List<BudgetHeadMaster>>() {
//                }.getType());
//                BudgetHeadMaster gal = gaList.get(0);
//                cl.setHeadCode(gal.getBudgetHead());
//
//            } catch (Exception e) {
//            }
            String ledgerId = cl.getLedgerId();
            HashMap<String, String> conexpMap = new HashMap<String, String>();
            conexpMap.put("ledgerId", cl.getLedgerId());
            conexpMap.put("status", ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_EXPENSE, conexpMap);

            List<ConsolidateDepartmentExpence> conexpbudlist = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentExpence>>() {
            }.getType());
            String id = ((LinkedTreeMap<String, String>) cl.getId()).get("$oid");

            for (ConsolidateDepartmentExpence gal1 : conexpbudlist) {
                List<String> li = gal1.getIncomeBudgetIdList();
                String consledgerId = gal1.getLedgerId();
                String isSanctioned = gal1.getIsSanctioned();
                //  if (li.contains(id)) {
                if (ledgerId.equalsIgnoreCase(consledgerId) && isSanctioned.equalsIgnoreCase("true") && li.contains(id)) {
                    try {
                        cl.setSanctionedAmount(gal1.getRevisedSanctionedAmount());
                        cl.setConsolidatedExpenseId(((LinkedTreeMap<String, String>) gal1.getId()).get("$oid"));
                        cl.setCreateExpenseId(((LinkedTreeMap<String, String>) cl.getId()).get("$oid"));
                    } catch (Exception e) {
                    }
                    break;
                }

                //  cl.setSanctionedAmount(gal1.getSanctionedAmount());
            }
        }

        return new Gson().toJson(ddoList);
    }

    public String getDepartmentsForExtraProvision(String budgetType, String finyear, String BudgetHead) throws Exception {
        HashMap<String, String> DeptData = new HashMap<String, String>();
        HashMap<String, String> budgetData = new HashMap<String, String>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, HashMap<String, String>> finalResult = new HashMap<String, HashMap<String, String>>();
        if (!BudgetHead.isEmpty()) {
            conditionMap.put("budgetHead", BudgetHead);
        }
        conditionMap.put("budgetType", budgetType);
        conditionMap.put("financialYear", finyear);
        conditionMap.put("isSanctioned", "true");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String dept;
        String budgetHead;
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> ddoList = new Gson().fromJson(result, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());

        for (CreateBudgetExpense cl : ddoList) {
            if (cl.getDepartment() != null) {

                String deptlist = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
                if (deptlist != null) {
                    List<Department> gaList1 = new Gson().fromJson(deptlist, new TypeToken<List<Department>>() {
                    }.getType());
                    Department gal1 = gaList1.get(0);
                    dept = gal1.getDepartment();
                    DeptData.put(cl.getDepartment(), dept);
                }

            }
            if (cl.getBudgetHead() != null) {

                String budgetlist = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getBudgetHead());
                if (budgetlist != null) {
                    List<BudgetHeadMaster> gaList1 = new Gson().fromJson(budgetlist, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    BudgetHeadMaster gal1 = gaList1.get(0);
                    budgetHead = gal1.getBudgetHead();
                    budgetData.put(cl.getBudgetHead(), budgetHead);
                }

            }
        }

        finalResult.put("FinalResult", DeptData);
        finalResult.put("BudgetResult", budgetData);
        return new Gson().toJson(finalResult);
    }

}
