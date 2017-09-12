/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.ConsolidateExpenseBudget;
import com.accure.budget.dto.CreateBudgetExpense;
import com.accure.budget.dto.ExpenseBudgetApproval;
import com.accure.budget.dto.DeptWiseExpBudgetAllocation;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.finance.dto.DDO;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class BudgetReAppropriationManager {

    
       public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EXPENSE_BUDGET_APPROVAL, Id);
        List<ExpenseBudgetApproval> expenseBudgetApprovalList = new Gson().fromJson(result, new TypeToken<List<ExpenseBudgetApproval>>() {
        }.getType());
        if (expenseBudgetApprovalList == null || expenseBudgetApprovalList.size() < 1) {
            return null;
        }
        return new Gson().toJson(expenseBudgetApprovalList);
    }
    public String search(CreateBudgetExpense obj) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj.getBudgetType().isEmpty() == false) {
            conditionMap.put("budgetType", obj.getBudgetType());
        }
        if (obj.getFundType().isEmpty() == false) {
            conditionMap.put("fundtype", obj.getFundType());
        }
        if (obj.getFinancialYear().isEmpty() == false) {
            conditionMap.put("finYear", obj.getFinancialYear());
        }
        if (obj.getSector().isEmpty() == false) {
            conditionMap.put("sector", obj.getSector());
        }
        if (obj.getDdo().isEmpty() == false) {
            conditionMap.put("ddo", obj.getDdo());
        }
        if (obj.getBudgetHead().isEmpty() == false) 
         {
            conditionMap.put("budgetHead", obj.getBudgetHead());
         }
        if (obj.getLocation().isEmpty() == false) 
         {
            conditionMap.put("location", obj.getLocation());
         }
         if (obj.getDepartment().isEmpty() == false) 
         {
            conditionMap.put("department", obj.getDepartment());
         }
        String result1 = "";
       // conditionMap.put("isSanctioned", "true");
        //System.out.println("-------conditionMap----"+conditionMap.toString());
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DEPTWISE_EXP_BUDGET_ALLOC_TABLE, conditionMap);
        if(result!=null)
        {
        List<DeptWiseExpBudgetAllocation> list = new Gson().fromJson(result, new TypeToken<List<DeptWiseExpBudgetAllocation>>() {
        }.getType());
        
        //list = getBudgetHeadAndDescription(list);
        return new Gson().toJson(list);
        }
        return null;
    }

    public boolean updateAppropriationAmount(DeptWiseExpBudgetAllocation deptWiseExpBudgetAllocation, String userName) throws Exception {
        String ID = deptWiseExpBudgetAllocation.getCreateExpenseId();
         //System.out.println("----approvId--before---");
        String deptWiseExpBudAllotionId=(String) deptWiseExpBudgetAllocation.getIdStr();
         //System.out.println("----approvId--after---"+ID);
        //System.out.println("----ID-----"+ID);
        String expenseBudget = new BudgetExpenseManager().Fetch(ID);
        CreateBudgetExpense obj = new Gson().fromJson(expenseBudget, new TypeToken<CreateBudgetExpense>() {
        }.getType());
        obj.setAppropriationValue(deptWiseExpBudgetAllocation.getAppropriationValue());
        obj.setSanctionedAmount(Integer.toString(Integer.parseInt(obj.getSanctionedAmount())+Integer.parseInt(deptWiseExpBudgetAllocation.getAppropriationValue())));
         String expenseBudgetApproval=new DepartmentWiseExpAlloManager().fetch(deptWiseExpBudAllotionId);
        List<DeptWiseExpBudgetAllocation> list = new Gson().fromJson(expenseBudgetApproval, new TypeToken<List<DeptWiseExpBudgetAllocation>>() {}.getType());
        DeptWiseExpBudgetAllocation expenseBudgetApprovalObj=list.get(0);
        expenseBudgetApprovalObj.setApprovedAmount(Integer.toString(Integer.parseInt(expenseBudgetApprovalObj.getApprovedAmount())+Integer.parseInt(deptWiseExpBudgetAllocation.getAppropriationValue())));
        expenseBudgetApprovalObj.setAppropriationValue(deptWiseExpBudgetAllocation.getAppropriationValue());
        boolean res1 = DBManager.getDbConnection().update(ApplicationConstants.DEPTWISE_EXP_BUDGET_ALLOC_TABLE, deptWiseExpBudAllotionId, new Gson().toJson(expenseBudgetApprovalObj));
        boolean res = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, ID, new Gson().toJson(obj));
        return res;
    }

    private List<CreateBudgetExpense> getBudgetHeadAndDescription(List<CreateBudgetExpense> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getHeadCode())) {
                    list.get(i).setBudgetHead((entry.getValue()));
                }
            }
        }
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHeadDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getHeadCode())) {
                    list.get(i).setHeadDescription((entry.getValue()));
                }
            }
        }

        return list;
    }

}
