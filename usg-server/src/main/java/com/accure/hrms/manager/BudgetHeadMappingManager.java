/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.budget.dto.FundType;
import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.finance.dto.FundCategory;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.finance.dto.GovtBudgetHead;
import com.accure.finance.manager.GovtBudgetHeadManager;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class BudgetHeadMappingManager {

    /**
     * Inserts document into
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code> collection.If
     * document list already contains <code>budgetHeadDescription</code> or
     * <code>budgetHead</code> field values,insertion will not happen
     *
     * @param bhm <code>BudgetHeadMaster</code> object data
     * @param userid  <code>_id</code> value of document (in hexadecimal string)
     * in <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> on successful
     * insertion.
     * @throws Exception if second argument is null..
     */
    public String save(BudgetHeadMaster bhm, String userid) throws Exception {

        String result;
        HashMap conditionMap = new HashMap<String, String>();
        conditionMap.put("budgetHeadDescription", bhm.getBudgetHeadDescription());

        if (userid == null) {
            result = null;
            return result;
        }
        if (hasDuplicateforSave(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        }
        conditionMap.clear();
        conditionMap.put("budgetHead", bhm.getBudgetHead());
        if (hasDuplicateforSave(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        }
        User user = new UserManager().fetch(userid);
        String fName = user.getFname();
        bhm.setCreateDate(System.currentTimeMillis() + "");
        bhm.setCreatedBy(fName);
        bhm.setUpdateDate(System.currentTimeMillis() + "");
        bhm.setUpdatedBy(fName);
        bhm.setStatus(ApplicationConstants.ACTIVE);
        String bhmJson = new Gson().toJson(bhm);
        String bankid = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, bhmJson);
        if (bankid != null) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

    /**
     * Displays all the documents in the
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code>
     * collection.Only those documents, whose
     * <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.ACTIVE</code> and
     * <code>ApplicationConstants.ISACTIVE</code> field is
     * <code>ApplicationConstants.YES</code> are appear in the list
     *
     * @return list of documents inJSON String
     * @throws Exception
     */
    public String fetch() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> BudgetHeadList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());

        BudgetHeadList = getGovtBudgetHeadListForBudgetHeadMapping(BudgetHeadList);

        for (BudgetHeadMaster li : BudgetHeadList) {

            if (li.getFundType() != null) {
                try {
                    String ftStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, li.getFundType());

                    List<FundType> ftList = new Gson().fromJson(ftStr, new TypeToken<List<FundType>>() {
                    }.getType());
                    FundType ft = ftList.get(0);
                    li.setFundType(ft.getDescription());
                } catch (Exception e) {
                }
            }
            if (li.getFundCategory() != null) {
                try {
                    String fcStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_CATEGORY_TABLE, li.getFundCategory());

                    List<FundCategory> fcList = new Gson().fromJson(fcStr, new TypeToken<List<FundCategory>>() {
                    }.getType());
                    FundCategory fc = fcList.get(0);
                    li.setFundCategory(fc.getCategory());
                } catch (Exception e) {
                }
            }
        }
        return new Gson().toJson(BudgetHeadList);

    }

    /**
     * Displays all the documents in the
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code>
     * collection.Only those documents, whose
     * <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return list of documents inJSON String
     * @throws Exception
     */
    public String fetchAllBudgetHeads() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> BudgetHeadList = new Gson().fromJson(result1, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());

        BudgetHeadList = getGovtBudgetHeadListForBudgetHeadMapping(BudgetHeadList);

        for (BudgetHeadMaster li : BudgetHeadList) {

            if (li.getFundType() != null) {
                try {
                    String ftStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, li.getFundType());

                    List<FundType> ftList = new Gson().fromJson(ftStr, new TypeToken<List<FundType>>() {
                    }.getType());
                    FundType ft = ftList.get(0);
                    li.setFundType(ft.getDescription());
                } catch (Exception e) {
                }
            }
            if (li.getFundCategory() != null) {
                try {
                    String fcStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_CATEGORY_TABLE, li.getFundCategory());

                    List<FundCategory> fcList = new Gson().fromJson(fcStr, new TypeToken<List<FundCategory>>() {
                    }.getType());
                    FundCategory fc = fcList.get(0);
                    li.setFundCategory(fc.getCategory());
                } catch (Exception e) {
                }
            }
        }
        return new Gson().toJson(BudgetHeadList);

    }

    /**
     * Searches document in the
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code> collection
     * using <code>_id</code> value of document.
     *
     * @param primaryKey <code>_id</code> value of document in hexadecimal
     * string.
     * @return <code>BudgetHeadMaster</code> Object data
     * @throws Exception if argument is null..
     */
    public BudgetHeadMaster fetch(String primaryKey) throws Exception {
        BudgetHeadMaster bhm = null;
        if (primaryKey == null) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, primaryKey);
        if (result != null) {
            List<BudgetHeadMaster> budgetHeadList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
            }.getType());
            bhm = budgetHeadList.get(0);
        }
        return bhm;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code> collection.If
     * document list already contains <code>budgetHeadDescription</code> or
     * <code>budgetHead</code> fields value, updation will not happen
     *
     * @param bhm <code>BudgetHeadMaster</code> object data
     * @param bhmId <code>_id</code> value of selected document in hexadecimal
     * string
     * @param userid <code>_id</code> value of document (in hexadecimal String)
     * in the <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> on successful updation
     * @throws Exception if second or third argument is null..
     */
    public String update(BudgetHeadMaster bhm, String bhmId, String userid) throws Exception {

        String result;

        HashMap conditionMap = new HashMap<String, String>();
        conditionMap.put("budgetHeadDescription", bhm.getBudgetHeadDescription());

        if (userid == null) {
            result = null;
            return result;
        }
        if (isDuplicateforUpdate(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap, bhmId)) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        }
        conditionMap.clear();
        conditionMap.put("budgetHead", bhm.getBudgetHead());
        if (isDuplicateforUpdate(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap, bhmId)) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        }
        User user = new UserManager().fetch(userid);
        String fName = user.getFname();
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, bhmId);
        List<BudgetHeadMaster> list = new Gson().fromJson(existrelationJson, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        BudgetHeadMaster bhmObj = list.get(0);
        bhmObj.setBudgetHead(bhm.getBudgetHead());
        bhmObj.setBudgetHeadDescription(bhm.getBudgetHeadDescription());
        bhmObj.setFundCategory(bhm.getFundCategory());
        bhmObj.setFundType(bhm.getFundType());
        bhmObj.setGovtBudgetHead(bhm.getGovtBudgetHead());
        bhmObj.setIsActive(bhm.getIsActive());
        bhmObj.setRemarks(bhm.getRemarks());
        bhmObj.setUnderBudgetHead(bhm.getUnderBudgetHead());
        bhmObj.setUpdateDate(System.currentTimeMillis() + "");
        bhmObj.setUpdatedBy(fName);
        bhmObj.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(bhmObj);
        boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, bhmId, bankJson);
        if (fResult) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }
        return result;
    }

    /**
     * Updates status field of selected document in the
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code> collection.If
     * <code>_id</code> value of selected document is already saved in
     * <code>budgetHead</code> field in
     * <code>ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE</code>
     * collection,updation will not happen
     *
     * @param id <code>_id</code> value of selected document in hexadecimal
     * string
     * @param loginUserId <code>_id</code> value of a document in
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is null..
     */
    public String deleteBudgetHead(String id, String loginUserId) throws Exception {
        String existStr = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, id);
        List<BudgetHeadMaster> existList = new Gson().fromJson(existStr, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        BudgetHeadMaster bhm = existList.get(0);

        bhm.setStatus(ApplicationConstants.DELETE);
        bhm.setUpdateDate(System.currentTimeMillis() + "");
        bhm.setDeletedBy(loginUserId);
        String relationJson = new Gson().toJson(bhm);
        String result;
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, "budgetHead", id))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, id, relationJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String Search(String head, String fundtype, String cat) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("govtBudgetHead", head);
        conditionMap.put("fundType", fundtype);
        conditionMap.put("fundCategory", cat);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> BudgetHeadList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());

        BudgetHeadList = getGovtBudgetHeadListForBudgetHeadMapping(BudgetHeadList);

        for (BudgetHeadMaster li : BudgetHeadList) {
            try {
                if (li.getFundType() != null) {
                    String ftStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE, li.getFundType());

                    List<FundType> ftList = new Gson().fromJson(ftStr, new TypeToken<List<FundType>>() {
                    }.getType());
                    FundType ft = ftList.get(0);
                    li.setFundType(ft.getDescription());
                }
            } catch (Exception e) {
            }
            try {
                if (li.getFundCategory() != null) {
                    String fcStr = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_CATEGORY_TABLE, li.getFundCategory());

                    List<FundCategory> fcList = new Gson().fromJson(fcStr, new TypeToken<List<FundCategory>>() {
                    }.getType());
                    FundCategory fc = fcList.get(0);
                    li.setFundCategory(fc.getCategory());
                }
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(BudgetHeadList);

    }

    private List<BudgetHeadMaster> getFundCategoryForBudgetHeadMapping(List<BudgetHeadMaster> li) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_CATEGORY_TABLE);
        List<FundCategory> religionList = new Gson().fromJson(result, new TypeToken<List<FundCategory>>() {
        }.getType());
        for (Iterator<FundCategory> iterator = religionList.iterator(); iterator.hasNext();) {
            FundCategory next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCategory());
        }
        for (int i = 0; i < li.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(li.get(i).getFundCategory())) {
                    li.get(i).setFundCategory(entry.getValue());
                }
            }
        }
        return li;
    }

    private List<BudgetHeadMaster> getFundTypeForBudgetHeadMapping(List<BudgetHeadMaster> li) throws Exception {

        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE);
        List<FundType> ftList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = ftList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < li.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(li.get(i).getFundType())) {
                    li.get(i).setFundType(entry.getValue());
                }
            }
        }
        return li;
    }

    public List<BudgetHeadMaster> fetchAllBudget() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        List<BudgetHeadMaster> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        return ledgerList;

    }

    private List<BudgetHeadMaster> getGovtBudgetHeadListForBudgetHeadMapping(List<BudgetHeadMaster> li) throws Exception {

        Map<String, String> getGovtBudgetHeadMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE);
        List<GovtBudgetHead> list = new Gson().fromJson(new GovtBudgetHeadManager().viewGovtBudgetHeadList(), new TypeToken<List<GovtBudgetHead>>() {
        }.getType());
        for (Iterator<GovtBudgetHead> iterator = list.iterator(); iterator.hasNext();) {
            GovtBudgetHead next = iterator.next();
            getGovtBudgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getMajorHead() + "-" + next.getSubMajorHead() + "-" + next.getMinorHead() + "-" + next.getSubMinorHead() + "-" + next.getOrder());
        }
        for (int i = 0; i < li.size(); i++) {
            String gId = li.get(i).getGovtBudgetHead();
            for (Map.Entry<String, String> entry : getGovtBudgetHeadMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(gId)) {
                    li.get(i).setGovtBudgetHead(value);
                }

            }
        }
        return li;
    }

    public String SearchBasedOnfields(BudgetHeadMaster budgetHead) throws Exception {
        String finalResult = null;
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        if (budgetHead.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", budgetHead.getBudgetHead()));
        }
        if (budgetHead.getBudgetHeadDescription() != null) {
            regexQuery.put("budgetHeadDescription",
                    new BasicDBObject("$regex", budgetHead.getBudgetHeadDescription()));
        }
        if (budgetHead.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", budgetHead.getFundType()));
        }
        if (budgetHead.getFundCategory() != null) {
            regexQuery.put("fundCategory",
                    new BasicDBObject("$regex", budgetHead.getFundCategory()));
        }
        if (budgetHead.getGovtBudgetHead() != null) {
            regexQuery.put("govtBudgetHead",
                    new BasicDBObject("$regex", budgetHead.getGovtBudgetHead()));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));

        DBCursor cursor = collection.find(regexQuery);
        List<BudgetHeadMaster> list = new ArrayList<BudgetHeadMaster>();
        while (cursor.hasNext()) {
            DBObject ob = cursor.next();

            Type type = new TypeToken<BudgetHeadMaster>() {
            }.getType();
            BudgetHeadMaster bhm = new Gson().fromJson(ob.toString(), type);
            list.add(bhm);
        }
        try {
            list = getGovtBudgetHeadListForBudgetHeadMapping(list);
        } catch (Exception ex) {

        }
        try {
            list = getFundCategoryForBudgetHeadMapping(list);
        } catch (Exception ex) {

        }
        try {
            list = getFundTypeForBudgetHeadMapping(list);
        } catch (Exception ex) {

        }
        if (list.size() > 0) {

            finalResult = new Gson().toJson(list);
        }

        return finalResult;
    }

}
