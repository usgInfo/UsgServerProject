/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.budget.dto.FundType;
import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.user.manager.UserManager;
import com.accure.db.in.DAO;
import com.accure.finance.dto.BudgetNature;
import com.accure.finance.dto.Group;
import com.accure.finance.dto.GroupMaster;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerCategory;
import com.accure.finance.dto.ParentLedger;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author deepak2310
 */
public class LedgerManager {

    public String createLedgerCategory(LedgerCategory ledgerCategory, String UserId) throws Exception {
        String result = "";

        if (ledgerCategory.getLedgerCategory() == null || ledgerCategory.getParentLedger() == null || UserId == null) {
            result = null;

        } else if (!ledgerCategory.getLedgerCategory().equalsIgnoreCase(null) && !ledgerCategory.getParentLedger().equalsIgnoreCase(null)) {

            HashMap map = new HashMap();

            map.put("parentLedger", ledgerCategory.getParentLedger());
            map.put("ledgerCategory", ledgerCategory.getLedgerCategory());

            if (Duplicate.hasDuplicateforSave(ApplicationConstants.LEDGER_CATEGORY_TABLE, map)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User UserDB = fetch(UserId);
                String name = UserDB.getFname() + " " + UserDB.getLname();

                ledgerCategory.setCreateDate(System.currentTimeMillis() + "");
                ledgerCategory.setUpdateDate(System.currentTimeMillis() + "");
                ledgerCategory.setStatus(ApplicationConstants.ACTIVE);
                ledgerCategory.setCreatedBy(name);

                Type type = new TypeToken<LedgerCategory>() {
                }.getType();
                String ledgerCategoryJson = new Gson().toJson(ledgerCategory, type);

                String ledgerId = DBManager.getDbConnection().insert(ApplicationConstants.LEDGER_CATEGORY_TABLE, ledgerCategoryJson);

                if (ledgerId != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }

        }
        return result;

    }

    public HashMap<String, String> fetchallParentLedger() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String parentLedgerJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PARENT_LEDGER_TABLE, conditionMap);
        List<ParentLedger> parentLedgerList = new Gson().fromJson(parentLedgerJson, new TypeToken<List<ParentLedger>>() {
        }.getType());
        conditionMap.clear();
        for (ParentLedger parentLedger : parentLedgerList) {
            conditionMap.put(((Map<String, String>) parentLedger.getId()).get("$oid"), parentLedger.getParentLedgerName());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchallFundtype() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fundtypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_TYPE, conditionMap);
        List<FundType> fundtypeList = new Gson().fromJson(fundtypeJson, new TypeToken<List<FundType>>() {
        }.getType());
        conditionMap.clear();
        for (FundType fundledger : fundtypeList) {
            conditionMap.put(((Map<String, String>) fundledger.getId()).get("$oid"), fundledger.getDescription());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchallLadgerName() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ladgernameJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
        List<Ledger> fundtypeList = new Gson().fromJson(ladgernameJson, new TypeToken<List<Ledger>>() {
        }.getType());
        conditionMap.clear();
        for (Ledger fundledger : fundtypeList) {
            conditionMap.put(((Map<String, String>) fundledger.getId()).get("$oid"), fundledger.getLedgerName());
        }
        return conditionMap;
    }

    public static void main(String args[]) throws Exception {
        //System.out.println(new LedgerManager().fetchAllLedger());
    }

    public List<LedgerCategory> fetchAllLedgerCategory() throws Exception {
        HashMap<String, String> parent = fetchallLadgerName();
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ledgerCategoryJson = dao.fetchAllRowsByConditions(ApplicationConstants.LEDGER_CATEGORY_TABLE, conditionMap);
//        dao.close();
        List<LedgerCategory> ledgerCategoryList = new Gson().fromJson(ledgerCategoryJson, new TypeToken<List<LedgerCategory>>() {
        }.getType());
        List<LedgerCategory> ledgerList = new ArrayList<LedgerCategory>();
        if (ledgerCategoryList != null) {
            for (LedgerCategory ledgercategory : ledgerCategoryList) {
                if (ledgercategory.getParentLedger() != null) {
                    String id = ledgercategory.getParentLedger();
                    if (parent.containsKey(id) && parent.get(id) != null) {
                        ledgercategory.setParentLedgerName(parent.get(id));
                    }
                }
                ledgerList.add(ledgercategory);
            }
        }
        return ledgerList;
    }

    public String updateLedgerCategory(LedgerCategory ledgerCategory, String userId, String ledgerCategoryId) throws Exception {
        String result = "";
        if (ledgerCategory.getLedgerCategory() == null || ledgerCategory.getParentLedger() == null || userId == null) {
            result = null;
        } else if (!ledgerCategory.getLedgerCategory().equalsIgnoreCase(null) && !ledgerCategory.getParentLedger().equalsIgnoreCase(null)) {
            HashMap map = new HashMap();

            map.put("ledgerCategory", ledgerCategory.getLedgerCategory());
            map.put("parentLedger", ledgerCategory.getParentLedger());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.LEDGER_CATEGORY_TABLE, map, ledgerCategoryId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User UserDB = fetch(userId);
                String name = UserDB.getFname() + " " + UserDB.getLname();

                LedgerCategory ledgerCategoryDB = fetchLedgerCategory(ledgerCategoryId);

                ledgerCategoryDB.setParentLedger(ledgerCategory.getParentLedger());
                ledgerCategoryDB.setLedgerCategory(ledgerCategory.getLedgerCategory());
                ledgerCategoryDB.setLedgerDetail(ledgerCategory.getLedgerDetail());
                ledgerCategoryDB.setOrderLevel(ledgerCategory.getOrderLevel());

                ledgerCategoryDB.setUpdateDate(System.currentTimeMillis() + "");
                ledgerCategoryDB.setUpdatedBy(name);
                ledgerCategoryDB.setStatus(ApplicationConstants.ACTIVE);

                String ledgerCategoryJson = new Gson().toJson(ledgerCategoryDB);

                boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEDGER_CATEGORY_TABLE, ledgerCategoryId, ledgerCategoryJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public LedgerCategory fetchLedgerCategory(String ledgerCategoryId) throws Exception {
        if (ledgerCategoryId == null || ledgerCategoryId.isEmpty()) {
            return null;
        }
        String ledgerCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_CATEGORY_TABLE, ledgerCategoryId);
        if (ledgerCategoryJson == null || ledgerCategoryJson.isEmpty()) {
            return null;
        }
        List<LedgerCategory> ledgerCategoryList = new Gson().fromJson(ledgerCategoryJson, new TypeToken<List<LedgerCategory>>() {
        }.getType());
        if (ledgerCategoryList == null || ledgerCategoryList.isEmpty()) {
            return null;
        }
        return ledgerCategoryList.get(0);

    }

    public boolean deleteLedgerCategory(String categoryLedgerId, String currentUserLogin) throws Exception {
        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        LedgerCategory ledgerCategory = fetchLedgerCategory(categoryLedgerId);
        ledgerCategory.setStatus(ApplicationConstants.DELETE);
        ledgerCategory.setUpdatedBy(userName);

        String ledgerCategoryJson = new Gson().toJson(ledgerCategory);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEDGER_CATEGORY_TABLE, categoryLedgerId, ledgerCategoryJson);
        if (status) {
            return true;
        }
        return false;
    }

    public String createLedger(Ledger ledger, String UserId) throws Exception {
        String result = "";

        if (ledger.getLedgerName() == null || ledger.getDisplayName() == null || UserId == null) {
            result = null;

        } else if (!ledger.getDisplayName().equalsIgnoreCase(null) && !ledger.getLedgerName().equalsIgnoreCase(null)) {

            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("ledgerName", ledger.getLedgerName());
            map1.put("displayName", ledger.getDisplayName());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.LEDGER_TABLE, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.LEDGER_TABLE, map1)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User UserDB = fetch(UserId);
                String name = UserDB.getFname() + " " + UserDB.getLname();

                ledger.setCreateDate(System.currentTimeMillis() + "");
                ledger.setUpdateDate(System.currentTimeMillis() + "");
                ledger.setStatus(ApplicationConstants.ACTIVE);
                ledger.setCreatedBy(name);

                Type type = new TypeToken<Ledger>() {
                }.getType();
                String ledgerJson = new Gson().toJson(ledger, type);

                String ledgerId = DBManager.getDbConnection().insert(ApplicationConstants.LEDGER_TABLE, ledgerJson);
                if (ledgerId != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }

        }
        return result;

    }

    public User fetch(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            return null;
        }
        String userJson = DBManager.getDbConnection().fetch(ApplicationConstants.USER_TABLE, userId);
        if (userJson == null || userJson.isEmpty()) {
            return null;
        }
        List<User> userList = new Gson().fromJson(userJson, new TypeToken<List<User>>() {
        }.getType());
        if (userList == null || userList.isEmpty()) {
            return null;
        }
        return userList.get(0);

    }

    public Ledger fetchLedger(String ledgerId) throws Exception {
        if (ledgerId == null || ledgerId.isEmpty()) {
            return null;
        }
        String ledgerJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, ledgerId);
        if (ledgerJson == null || ledgerJson.isEmpty()) {
            return null;
        }
        List<Ledger> ledgerList = new Gson().fromJson(ledgerJson, new TypeToken<List<Ledger>>() {
        }.getType());
        if (ledgerList == null || ledgerList.isEmpty()) {
            return null;
        }
        return ledgerList.get(0);

    }

    public HashMap<String, String> fetchallUnderGroup() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String underGroupJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GROUP_MASTER_TABLE, conditionMap);
        List<GroupMaster> underGroupList = new Gson().fromJson(underGroupJson, new TypeToken<List<GroupMaster>>() {
        }.getType());
        conditionMap.clear();
        for (GroupMaster underGroup : underGroupList) {
            conditionMap.put(((Map<String, String>) underGroup.getId()).get("$oid"), underGroup.getGroupName());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchallGroup() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String underGroupJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GROUP_TABLE, conditionMap);
        List<Group> underGroupList = new Gson().fromJson(underGroupJson, new TypeToken<List<Group>>() {
        }.getType());
        conditionMap.clear();
        for (Group underGroup : underGroupList) {
            conditionMap.put(((Map<String, String>) underGroup.getId()).get("$oid"), underGroup.getGroupName());
        }
        return conditionMap;
    }

//    public HashMap<String, String> fetchallBudgetNature() throws Exception {
//        HashMap<String, String> conditionMap = new HashMap<String, String>();
//        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        String budgetNatureJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_NATURE_TABLE, conditionMap);
//        List<BudgetNature> budgetNatureList = new Gson().fromJson(budgetNatureJson, new TypeToken<List<BudgetNature>>() {
//        }.getType());
//        conditionMap.clear();
//        for (BudgetNature budgetNature : budgetNatureList) {
//            conditionMap.put(((Map<String, String>) budgetNature.getId()).get("$oid"), budgetNature.getBudgetNatureName());
//        }
//        return conditionMap;
//    }

    public List<Ledger> fetchAllLedger() throws Exception {
        HashMap<String, String> parentLedger = fetchallFundtype();
        HashMap<String, String> underGroup = fetchallGroup();
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ledgerJson = dao.fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
//        dao.close();
        List<Ledger> ledgerList = new Gson().fromJson(ledgerJson, new TypeToken<List<Ledger>>() {
        }.getType());
        List<Ledger> returnList = new ArrayList<Ledger>();
        if (ledgerList != null) {
            for (Ledger ledger : ledgerList) {
                if (ledger.getFundType() != null) {
                    String parentId = ledger.getFundType();
                    if (parentLedger.containsKey(parentId) && parentLedger.get(parentId) != null) {
                        ledger.setFundTypeName(parentLedger.get(parentId));
                    }
                }
                if (ledger.getUnderGroup() != null) {
                    String groupId = ledger.getUnderGroup();
                    if (underGroup.containsKey(groupId) && underGroup.get(groupId) != null) {
                        ledger.setUnderGroupName(underGroup.get(groupId));
                    }
                }

                returnList.add(ledger);
            }
        }

        return returnList;
    }

    public List<Ledger> fetchAllLedger1() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ddoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
        List<Ledger> ledgerList = new Gson().fromJson(ddoJson, new TypeToken<List<Ledger>>() {
        }.getType());

        return ledgerList;
    }

    public String updateLedgerInfo(Ledger ledger, String userId, String ledgerId) throws Exception {
        String result = "";
        if (ledger.getLedgerName() == null || ledger.getDisplayName() == null || userId == null) {
            result = null;
        } else if (!ledger.getLedgerName().equalsIgnoreCase(null) && !ledger.getDisplayName().equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            HashMap map1 = new HashMap();
            map.put("ledgerName", ledger.getLedgerName());
            map1.put("displayName", ledger.getDisplayName());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.LEDGER_TABLE, map, ledgerId) || Duplicate.isDuplicateforUpdate(ApplicationConstants.LEDGER_TABLE, map1, ledgerId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            } else {
                User UserDB = fetch(userId);
                String name = UserDB.getFname() + " " + UserDB.getLname();

                Ledger ledgerDB = fetchLedger(ledgerId);

                ledgerDB.setLedgerName(ledger.getLedgerName());
                ledgerDB.setDisplayName(ledger.getDisplayName());
                ledgerDB.setUnderGroup(ledger.getUnderGroup());
                ledgerDB.setFundType(ledger.getFundType());
                ledgerDB.setBudgetType(ledger.getBudgetType());
                ledgerDB.setBudgetHeadCode(ledger.getBudgetHeadCode());
                ledgerDB.setRemarks(ledger.getRemarks());
                ledgerDB.setLedgerMapping(ledger.getLedgerMapping());

                ledgerDB.setUpdateDate(System.currentTimeMillis() + "");
                ledgerDB.setUpdatedBy(name);

                boolean status = update(ledgerDB, ledgerId);

                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public boolean update(Ledger ledger, String ledgerId) throws Exception {
        Type type = new TypeToken<Ledger>() {
        }.getType();
        String ledgerJson = new Gson().toJson(ledger, type);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEDGER_TABLE, ledgerId, ledgerJson);
        return status;
    }

    public String deleteLedger(String ledgerId, String loginUserId) throws Exception {
        String result = "";
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.LEDGER_CODE_TABLE, "governmentLedgerCode", ledgerId)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.LEDGER_CATEGORY_TABLE, "parentLedger", ledgerId))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            Ledger ledger = fetchLedger(ledgerId);
            ledger.setStatus(ApplicationConstants.DELETE);
            ledger.setUpdatedBy(userName);

            boolean status = update(ledger, ledgerId);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    //For Parent Ledger Category
    public String createParentLedger(ParentLedger parentLedger, String UserId) throws Exception {

        User UserDB = fetch(UserId);
        String name = UserDB.getFname() + " " + UserDB.getLname();

        parentLedger.setCreateDate(System.currentTimeMillis() + "");
        parentLedger.setUpdateDate(System.currentTimeMillis() + "");
        parentLedger.setStatus(ApplicationConstants.ACTIVE);
        parentLedger.setCreatedBy(name);

        Type type = new TypeToken<ParentLedger>() {
        }.getType();
        String parentLedgerJson = new Gson().toJson(parentLedger, type);

        String ledgerId = DBManager.getDbConnection().insert(ApplicationConstants.PARENT_LEDGER_TABLE, parentLedgerJson);
        return ledgerId;
    }

    public List<ParentLedger> fetchAllParentLedger() throws Exception {
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String parentLedgerJson = dao.fetchAllRowsByConditions(ApplicationConstants.PARENT_LEDGER_TABLE, conditionMap);
//        dao.close();
        List<ParentLedger> parentLedgerList = new Gson().fromJson(parentLedgerJson, new TypeToken<List<ParentLedger>>() {
        }.getType());
        return parentLedgerList;
    }

    public boolean updateParentLedger(ParentLedger parentLedger, String userId, String parentLedgerId) throws Exception {

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        ParentLedger parentLedgerDb = fetchParentLedger(parentLedgerId);
        if (parentLedgerDb.getParentLedgerName() != null || !parentLedgerDb.getParentLedgerName().isEmpty()) {
            parentLedgerDb.setParentLedgerName(parentLedger.getParentLedgerName());
        }
        if (parentLedgerDb.getDescription() != null || !parentLedgerDb.getDescription().isEmpty()) {
            parentLedgerDb.setDescription(parentLedger.getDescription());
        }
        parentLedgerDb.setUpdateDate(System.currentTimeMillis() + "");
        parentLedgerDb.setUpdatedBy(userName);
        parentLedgerDb.setStatus(ApplicationConstants.ACTIVE);

        String parentLedgerJson = new Gson().toJson(parentLedgerDb);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PARENT_LEDGER_TABLE, parentLedgerId, parentLedgerJson);
        if (status) {
            return true;
        }
        return false;
    }

    public ParentLedger fetchParentLedger(String parentLedgerId) throws Exception {
        if (parentLedgerId == null || parentLedgerId.isEmpty()) {
            return null;
        }
        String parentLedgerJson = DBManager.getDbConnection().fetch(ApplicationConstants.PARENT_LEDGER_TABLE, parentLedgerId);
        if (parentLedgerJson == null || parentLedgerJson.isEmpty()) {
            return null;
        }
        List<ParentLedger> parentLedgerList = new Gson().fromJson(parentLedgerJson, new TypeToken<List<ParentLedger>>() {
        }.getType());
        if (parentLedgerList == null || parentLedgerList.isEmpty()) {
            return null;
        }
        return parentLedgerList.get(0);

    }

    public boolean deleteParentLedger(String parentLedgerId, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        ParentLedger parentLedger = fetchParentLedger(parentLedgerId);
        parentLedger.setStatus(ApplicationConstants.DELETE);
        parentLedger.setUpdatedBy(userName);

        String parentLedgerJson = new Gson().toJson(parentLedger);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.PARENT_LEDGER_TABLE, parentLedgerId, parentLedgerJson);
        if (status) {
            return true;
        }
        return false;
    }

}
