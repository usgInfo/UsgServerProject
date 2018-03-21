/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.budget.dto.DeptWiseIncBudgetAllocation;
import static com.accure.budget.manager.SanctionUniversityExpenseBudgetManager.SUBMIT;
import com.accure.common.duplicate.Duplicate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class ExtraProvisionIncomeManager {

    public String save(CreateIncomeBudget extraProvsion, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        extraProvsion.setCreateDate(System.currentTimeMillis() + "");
        extraProvsion.setUpdateDate(System.currentTimeMillis() + "");
        extraProvsion.setStatus(ApplicationConstants.ACTIVE);
        extraProvsion.setSentStatus("Save");
        extraProvsion.setCreatedBy(userName);
        String extraProvsionJson = new Gson().toJson(extraProvsion);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.EXTRA_PROVISION_INCOME, extraProvsionJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public String checkDuplicateCon(CreateIncomeBudget obj) {
        HashMap<String, String> duplicateConditionMap = new HashMap<String, String>();
        duplicateConditionMap.put("fundType", obj.getFundType());
        duplicateConditionMap.put("sector", obj.getSector());
        duplicateConditionMap.put("financialYear", obj.getFinancialYear());
        duplicateConditionMap.put("budgetType", obj.getBudgetType());
        duplicateConditionMap.put("department", obj.getDepartment());
        duplicateConditionMap.put("ledgerId", obj.getLedgerId());
        duplicateConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.EXTRA_PROVISION_INCOME, duplicateConditionMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }
        return "proceed";
    }

    public String SearchData(CreateIncomeBudget obj) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (obj.getBudgetType().isEmpty() == false) {

            conditionMap.put("budgetType", obj.getBudgetType());
        }
        if (obj.getSentStatus().isEmpty() == false) {

            conditionMap.put("sentStatus", obj.getSentStatus());
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
        if (obj.getDepartment().isEmpty() == false) {
            conditionMap.put("department", obj.getDepartment());
        }
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EXTRA_PROVISION_INCOME, conditionMap);
        List<CreateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        return new Gson().toJson(list);

    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EXTRA_PROVISION_INCOME, Id);
        List<CreateIncomeBudget> gisList = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public boolean update(CreateIncomeBudget extraIncomeUpdate, String id, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        extraIncomeUpdate.setUpdateDate(System.currentTimeMillis() + "");
        extraIncomeUpdate.setStatus(ApplicationConstants.ACTIVE);
        extraIncomeUpdate.setUpdatedBy(userName);
        String SanctionIncomeBudgetrJson = new Gson().toJson(extraIncomeUpdate);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EXTRA_PROVISION_INCOME, id, SanctionIncomeBudgetrJson);

        return result;
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<CreateIncomeBudget>() {
        }.getType();
        String SactionUniversityExpense = new ExtraProvisionIncomeManager().fetch(Id);
        if (SactionUniversityExpense == null || SactionUniversityExpense.isEmpty()) {
            return false;
        }
        CreateIncomeBudget BudgetSanctionIncomerJson = new Gson().fromJson(SactionUniversityExpense, type);
        BudgetSanctionIncomerJson.setSentStatus(SUBMIT);
        BudgetSanctionIncomerJson.setUpdateDate(System.currentTimeMillis() + "");

        BudgetSanctionIncomerJson.setUpdatedBy(userName);
        boolean id = DBManager.getDbConnection().update(ApplicationConstants.EXTRA_PROVISION_INCOME, Id, new Gson().toJson(BudgetSanctionIncomerJson));
        if (id) {
            String result = DBManager.getDbConnection().fetch(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, BudgetSanctionIncomerJson.getIncomeBudgetId());
            List<CreateIncomeBudget> relationlist = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
            }.getType());
            CreateIncomeBudget cseb = relationlist.get(0);
            cseb.setCreateDate(cseb.getCreateDate());
            cseb.setUpdateDate(System.currentTimeMillis() + "");
            cseb.setStatus(ApplicationConstants.ACTIVE);
            cseb.SanctionedAmount(BudgetSanctionIncomerJson.getSanctionedAmount());
            cseb.setExtraProvisionAmount(BudgetSanctionIncomerJson.getExtraProvisionAmount());//upendra
            cseb.setTotalAmount(BudgetSanctionIncomerJson.getTotalAmount());
            cseb.setIsExtraProvisioned("true");
            String incomeJson = new Gson().toJson(cseb);
            boolean resultstaus = DBManager.getDbConnection().update(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, BudgetSanctionIncomerJson.getIncomeBudgetId(), incomeJson);
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("budgetIncomeSanctionId", BudgetSanctionIncomerJson.getIncomeBudgetId());
            condition.put("status", ApplicationConstants.ACTIVE);

            String departwiseAllocresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DEPTWISE_INC_BUDGET_ALLOC_TABLE, condition);
            List<DeptWiseIncBudgetAllocation> departwiseAlloclist = new Gson().fromJson(departwiseAllocresult, new TypeToken<List<DeptWiseIncBudgetAllocation>>() {
            }.getType());
            DeptWiseIncBudgetAllocation aloocationData = departwiseAlloclist.get(0);
            aloocationData.setExtraProvisionAmount(BudgetSanctionIncomerJson.getExtraProvisionAmount());
            String allocationJson = new Gson().toJson(aloocationData);
            DBManager.getDbConnection().update(ApplicationConstants.DEPTWISE_INC_BUDGET_ALLOC_TABLE, ((Map<String, String>) aloocationData.getId()).get("$oid"), allocationJson);
            if (resultstaus) {
                return id;
            }

        }
        return false;
    }

    public boolean delete(String id) throws Exception {

        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<CreateIncomeBudget>() {
        }.getType();
        String bank = new ExtraProvisionIncomeManager().fetch(id);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        CreateIncomeBudget bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EXTRA_PROVISION_INCOME, id, new Gson().toJson(bankJson));
        //System.out.println("deleteResult" + result);
        return result;
    }
}
