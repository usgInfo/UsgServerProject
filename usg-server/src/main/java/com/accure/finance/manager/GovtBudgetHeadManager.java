/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.GovtBudgetHead;
import com.accure.finance.dto.MajorHead;
import com.accure.finance.dto.MinorHead;
import com.accure.finance.dto.SubMajorHead;
import com.accure.finance.dto.SubMinorHead;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author upendra/Shwetha T S
 */
public class GovtBudgetHeadManager {

    /**
     * Inserts document into
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code> collection.If
     * documents in the collection already contains same combination of
     * <code>order</code>,<code>majorHead</code>,<code>subMajorHead</code>,<code>minorHead</code>
     * and <code>subMinorHead</code> data insertion will not take place.
     *
     * @param govtBudgetHead <code>GovtBudgetHead</code> object data
     * @param userId <code>_id</code> value of <code>User</code> object in
     * hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if second argument is <code>null</code>..
     */
    public String savegovtbudegthead(GovtBudgetHead govtBudgetHead, String userId) throws Exception {
        String result;
        if (govtBudgetHead == null || userId == null) {
            return null;
        }
        HashMap map1 = new HashMap();
        HashMap map = new HashMap();
        map1.put("order", govtBudgetHead.getOrder());
        map.put("majorHead", govtBudgetHead.getMajorHead());
        map.put("subMajorHead", govtBudgetHead.getSubMajorHead());
        map.put("minorHead", govtBudgetHead.getMinorHead());
        map.put("subMinorHead", govtBudgetHead.getSubMinorHead());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, map1)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        govtBudgetHead.setCreateDate(System.currentTimeMillis() + "");
        govtBudgetHead.setUpdateDate(System.currentTimeMillis() + "");
        govtBudgetHead.setStatus(ApplicationConstants.ACTIVE);
        govtBudgetHead.setCreatedBy(userName);
        govtBudgetHead.setUpdatedBy(userName);

        String govtBudgetHeadJson = new Gson().toJson(govtBudgetHead);

        String fResult = DBManager.getDbConnection().insert(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, govtBudgetHeadJson);

        if (fResult != null) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;

    }

    /**
     * Displays list of documents in the
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code> collection.Only
     * those documents whose <code>ApplicationConstants.STATUS</code> field is
     * <code> ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return list of documents in the
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code> collection in
     * JSON String
     * @throws Exception
     */
    public String viewGovtBudgetHeadList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, conditionMap);

        List<GovtBudgetHead> gbhList = new Gson().fromJson(result, new TypeToken<List<GovtBudgetHead>>() {
        }.getType());
        if (gbhList != null) {
            for (GovtBudgetHead li : gbhList) {
                String majorHeadJson = DBManager.getDbConnection().fetch(ApplicationConstants.MAJORHEAD_TABLE, li.getMajorHead());
                List<MajorHead> majHeadList = new Gson().fromJson(majorHeadJson, new TypeToken<List<MajorHead>>() {
                }.getType());
                MajorHead majHead = majHeadList.get(0);
                li.setMajorHead(majHead.getMajorHead());
                String minorid = li.getMinorHead();
                String minorjson = DBManager.getDbConnection().fetch(ApplicationConstants.MINORHEAD_TABLE, minorid);
                List<MinorHead> minorHeadList = new Gson().fromJson(minorjson, new TypeToken<List<MinorHead>>() {
                }.getType());
                MinorHead minorHead = minorHeadList.get(0);
                li.setMinorHead(minorHead.getMinorHead());
                String subMajorHeadJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MAJORHEAD_TABLE, li.getSubMajorHead());
                List<SubMajorHead> subMajHeadList = new Gson().fromJson(subMajorHeadJson, new TypeToken<List<SubMajorHead>>() {
                }.getType());
                SubMajorHead subMajHead = subMajHeadList.get(0);
                li.setSubMajorHead(subMajHead.getSubMajorHead());
                String subMinorHeadJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUB_MINORHEAD_TABLE, li.getSubMinorHead());
                List<SubMinorHead> subMinorList = new Gson().fromJson(subMinorHeadJson, new TypeToken<List<SubMinorHead>>() {
                }.getType());
                SubMinorHead subMinor = subMinorList.get(0);
                li.setSubMinorHead(subMinor.getSubMinorHead());
            }
        }
        return new Gson().toJson(gbhList);

    }

    /**
     * Updates the  <code>status</code> field of a document in the
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code> collection.If
     * <code>_id</code> value of document is already save in
     * <code>govtBudgetHead</code> field of
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code> collection
     * updation will not take place.
     *
     * @param primaryKey <code>_id</code> value of document in the
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code> collection in
     * hexadecimal string
     *
     * @param userId <code>_id</code> value of <code>User</code> object in
     * hexadecimal string
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String deleteGBH(String primaryKey, String userId) throws Exception {

        String status;
        if (primaryKey == null || userId == null) {
            return null;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, "govtBudgetHead", primaryKey))) {
            return ApplicationConstants.DELETE_MESSAGE;
        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, primaryKey);
        List<GovtBudgetHead> list = new Gson().fromJson(existrelationJson, new TypeToken<List<GovtBudgetHead>>() {
        }.getType());
        GovtBudgetHead dbObject = list.get(0);
        dbObject.setStatus(ApplicationConstants.DELETE);
        dbObject.setUpdateDate(System.currentTimeMillis() + "");
        dbObject.setDeletedBy(userName);
        String dbObjectJson = new Gson().toJson(dbObject);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, primaryKey, dbObjectJson);
        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;

    }

    /**
     * Updates a document in the
     * <code>ApplicationConstants.GOVT_BUDGET_HEAD_TABLE</code> collection.If
     * documents in the collection already contains same combination of
     * <code>order</code>,<code>majorHead</code>,<code>subMajorHead</code>,<code>minorHead</code>
     * and <code>subMinorHead</code> fields data updation will not take place.
     *
     * @param govtBudgetHead <code>GovtBudgetHead</code> object data
     * @param id <code>_id</code> value of <code>GovtBudgetHead</code> object in
     * hexadecimal String
     * @param userId <code>_id</code> value of <code>User</code> object in
     * hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if second or third argument is <code>null</code>..
     */
    public String updateGovtBudgetHead(GovtBudgetHead govtBudgetHead, String id, String userId) throws Exception {
        String result;
        if (govtBudgetHead == null || userId == null) {
            return null;
        }
        HashMap map1 = new HashMap();
        HashMap map = new HashMap();
        map1.put("order", govtBudgetHead.getOrder());
        map.put("majorHead", govtBudgetHead.getMajorHead());
        map.put("subMajorHead", govtBudgetHead.getSubMajorHead());
        map.put("minorHead", govtBudgetHead.getMinorHead());
        map.put("subMinorHead", govtBudgetHead.getSubMinorHead());

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, map, id) || Duplicate.isDuplicateforUpdate(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, map1, id)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, id);
        List<GovtBudgetHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<GovtBudgetHead>>() {
        }.getType());
        GovtBudgetHead dbObject = relationlist.get(0);

        dbObject.setMajorHead(govtBudgetHead.getMajorHead());
        dbObject.setSubMajorHead(dbObject.getSubMajorHead());
        dbObject.setSubMinorHead(govtBudgetHead.getSubMinorHead());
        dbObject.setMinorHead(govtBudgetHead.getMinorHead());
        dbObject.setOrder(govtBudgetHead.getOrder());
        dbObject.setRemarks(govtBudgetHead.getRemarks());
        dbObject.setUpdatedBy(userName);
        dbObject.setUpdateDate(System.currentTimeMillis() + "");

        String govtBudgetHeadJson = new Gson().toJson(dbObject);

        boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, id, govtBudgetHeadJson);

        if (fResult) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;
    }

}
