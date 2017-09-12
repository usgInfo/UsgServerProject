/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.ConsolidateDepartmentIncome;
import com.accure.hrms.dto.DepartmentDetails;
import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.budget.dto.DeptWiseIncBudgetAllocation;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.EmployeeDepartmentMapping;
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
public class DepartmentWiseIncAlloManager {

    public String getDepartments(String ddo, String location, String empId) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_DEPARTMENT_MAPPING, conditionMap);

        List<EmployeeDepartmentMapping> list = new Gson().fromJson(result, new TypeToken<List<EmployeeDepartmentMapping>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        List<String> list1 = new ArrayList<String>();
        List<DepartmentDetails> list2 = new ArrayList<DepartmentDetails>();
        list1 = list.get(0).getDepartment();
        for (String departmentId : list1) {
            String resultAA = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, departmentId);
            if (!resultAA.isEmpty()) {
                List<Department> deptList = new Gson().fromJson(resultAA, new TypeToken<List<Department>>() {
                }.getType());
                Department em = deptList.get(0);
                DepartmentDetails details = new DepartmentDetails();
                details.setDepartment(departmentId);
                details.setDepartmentName(em.getDepartment());
                list2.add(details);
            }
        }
        list.get(0).setDepartmentList(list2);
        return new Gson().toJson(list.get(0));
    }

    public String searchLedgers(CreateIncomeBudget dtoData) throws Exception {

        CreateIncomeBudget object = new CreateIncomeBudget();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", dtoData.getDdo());
        conditionMap.put("fundType", dtoData.getFundType());
        conditionMap.put("financialYear", dtoData.getFinancialYear());
        if (!dtoData.getBudgetHead().isEmpty() && dtoData.getBudgetHead() != "" && dtoData.getBudgetHead() != null) {
            conditionMap.put("budgetHead", dtoData.getBudgetHead());
        }
        conditionMap.put("sector", dtoData.getSector());
        conditionMap.put("location", dtoData.getLocation());
        conditionMap.put("isSanctioned", ApplicationConstants.FALSE);
        conditionMap.put("department", dtoData.getDepartment());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> ddoList = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());

        for (CreateIncomeBudget cl : ddoList) {
            cl.setHeadCode(cl.getHeadCode());

            String ledgerId = cl.getLedgerId();
            HashMap<String, String> conexpMap = new HashMap<String, String>();
            conexpMap.put("ledgerId", cl.getLedgerId());
            conexpMap.put("status", ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPARTMENT_INCOME, conexpMap);

            List<ConsolidateDepartmentIncome> conexpbudlist = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentIncome>>() {
            }.getType());
            String id = ((LinkedTreeMap<String, String>) cl.getId()).get("$oid");

            for (ConsolidateDepartmentIncome gal1 : conexpbudlist) {
                List<String> li = gal1.getIncomeBudgetIdList();
                String consledgerId = gal1.getLedgerId();
                String isSanctioned = gal1.getIsSanctioned();
                //  if (li.contains(id)) {
                if (ledgerId.equalsIgnoreCase(consledgerId) && isSanctioned.equalsIgnoreCase("true") && li.contains(id)) {
                    try {
                        cl.SanctionedAmount(gal1.getRevisedSanctionedAmount());
                        cl.consolidatedIncomeId(((LinkedTreeMap<String, String>) gal1.getId()).get("$oid"));
                    } catch (Exception e) {
                    }
                    break;
                }
            }
        }
        return new Gson().toJson(ddoList);
    }

    public String search(DeptWiseIncBudgetAllocation emp) throws Exception {

        PropertiesConfiguration config = getConfig();
        BasicDBObject regexQuery = new BasicDBObject();
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.DEPTWISE_INC_BUDGET_ALLOC_TABLE);

        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
        if (emp.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));
        }
        if (emp.getDepartment() != null) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));
        }
        if (emp.getBudgetType() != null) {
            regexQuery.put("budgetType",
                    new BasicDBObject("$regex", emp.getBudgetType()));
        }
        if (emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", emp.getBudgetHead()));
        }
        if (emp.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", emp.getFundType()));
        }
        if (emp.getSector() != null) {
            regexQuery.put("sector",
                    new BasicDBObject("$regex", emp.getSector()));

        }
        if (emp.getFinancialYear() != null) {
            regexQuery.put("financialYear",
                    new BasicDBObject("$regex", emp.getFinancialYear()));

        }

        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        DBCursor cursor2 = collection.find(regexQuery);

        List<DeptWiseIncBudgetAllocation> finalList = new ArrayList<DeptWiseIncBudgetAllocation>();

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<DeptWiseIncBudgetAllocation>() {
            }.getType();
            DeptWiseIncBudgetAllocation em = new Gson().fromJson(ob.toString(), type);
            finalList.add(em);
        }

        return new Gson().toJson(finalList);
    }

    public String save(DeptWiseIncBudgetAllocation next, String loginUserId) {
        try {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            next.setCreateDate(System.currentTimeMillis() + "");
            next.setUpdateDate(System.currentTimeMillis() + "");
            next.setStatus(ApplicationConstants.ACTIVE);
            next.setCreatedBy(userName);
            next.setExtraProvisionAmount("0");
            String id = next.getConsolidatedIncomeId();
            String approvedAmount = next.getApprovedAmount();
            String sanctionAmount = next.getSanctionAmount();
            int appdAmount = Integer.parseInt(approvedAmount);
            int sanctnAmount = Integer.parseInt(sanctionAmount);
            int remAppvdAmount = appdAmount - sanctnAmount;
            String revisedSnctionedAmount = String.valueOf(remAppvdAmount);
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPARTMENT_INCOME, id);
            if (!result.isEmpty()) {
                List<ConsolidateDepartmentIncome> list = new Gson().fromJson(result, new TypeToken<List<ConsolidateDepartmentIncome>>() {
                }.getType());
                ConsolidateDepartmentIncome em = list.get(0);
                em.setRevisedSanctionedAmount(revisedSnctionedAmount);
                String conIncBudgetJson = new Gson().toJson(em);
                boolean Id1 = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPARTMENT_INCOME, id, conIncBudgetJson);
                //System.out.println("Id1" + Id1);
            }
            String icomeBudgetId = next.getBudgetIncomeSanctionId();
            String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, icomeBudgetId);
            if (!result1.isEmpty()) {
                List<CreateIncomeBudget> list1 = new Gson().fromJson(result1, new TypeToken<List<CreateIncomeBudget>>() {
                }.getType());
                CreateIncomeBudget em1 = list1.get(0);
                em1.SanctionedAmount(Integer.toString(sanctnAmount));
                em1.setIsSanctioned(ApplicationConstants.IS_SANCTIONED_TRUE);
                String IncBudgetJson1 = new Gson().toJson(em1);
                boolean Id2 = DBManager.getDbConnection().update(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, icomeBudgetId, IncBudgetJson1);

            }
            String json = new Gson().toJson(next);
            String Id = DBManager.getDbConnection().insert(ApplicationConstants.DEPTWISE_INC_BUDGET_ALLOC_TABLE, json);
            if (Id != null) {
                return Id;
            }

            return null;
        } catch (Exception e) {

        }
        return null;
    }

    public String getDepartmentsForExtraProvision(String budgetType, String finyear,String BudgetHead) throws Exception {
        HashMap<String, String> DeptData = new HashMap<String, String>();
        HashMap<String, String> budgetData = new HashMap<String, String>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, HashMap<String, String>> finalResult = new HashMap<String, HashMap<String, String>>();
        if(!BudgetHead.isEmpty())
        {
          conditionMap.put("budgetHead", BudgetHead);  
        }
        conditionMap.put("budgetType", budgetType);
        conditionMap.put("financialYear", finyear);
        conditionMap.put("isSanctioned", "true");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String dept;
        String budgetHead;
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> ddoList = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());

        for (CreateIncomeBudget cl : ddoList) {
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
