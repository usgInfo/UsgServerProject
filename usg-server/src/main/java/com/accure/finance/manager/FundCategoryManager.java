/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.FundCategory;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shwetha T S
 */
public class FundCategoryManager {

    /**
     * Inserts documents to
     * <code>ApplicationConstants.FUND_CATEGORY_TABLE</code> collection.If
     * documents list already contains <code>category</code> field
     * value,insertion will not take place.
     *
     * @param fundcategory <code>FundCategory</code> object data.
     * @param userId <code>_id</code> value of a document in
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> on successful insertion
     * @throws Exception if second argument is <code>null</code>..
     */
    public String save(FundCategory fundcategory, String userId) throws Exception {

        String result = "";
        HashMap map = new HashMap();
        if (fundcategory == null || userId == null) {
            return null;
        }
        if (!(fundcategory.getCategory().equalsIgnoreCase(null))) {
            map.put("category", fundcategory.getCategory());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.FUND_CATEGORY_TABLE, map)) {
                result = ApplicationConstants.DUPLICATE;
            } else if (!(userId).equalsIgnoreCase(null)) {
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();

                fundcategory.setCreateDate(System.currentTimeMillis() + "");
                fundcategory.setUpdateDate(System.currentTimeMillis() + "");
                fundcategory.setStatus(ApplicationConstants.ACTIVE);
                fundcategory.setCreatedBy(userName);
                fundcategory.setUpdatedBy(userName);

                String objectJson = new Gson().toJson(fundcategory);

                String fResult = DBManager.getDbConnection().insert(ApplicationConstants.FUND_CATEGORY_TABLE, objectJson);

                if (fResult != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }

        return result;

    }

    /**
     * Searches document in the
     * <code>ApplicationConstants.FUND_CATEGORY_TABLE</code> collection.
     *
     * @param fundcategoryId <code>_id</code> value of document
     * @return searched document in JSON String format.
     * @throws Exception if argument is <code>null</code>...
     */
    public String fetch(String fundcategoryId) throws Exception {
        if (fundcategoryId == null || fundcategoryId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_CATEGORY_TABLE, fundcategoryId);
        List<FundCategory> fundcategoryList = new Gson().fromJson(result, new TypeToken<List<FundCategory>>() {
        }.getType());
        if (fundcategoryList == null || fundcategoryList.size() < 1) {
            return null;
        }
        return new Gson().toJson(fundcategoryList.get(0));
    }

    /**
     * Updates <code>status</code> field of selected document in the
     * <code>ApplicationConstants.FUND_CATEGORY_TABLE</code> collection.If
     * <code>_id</code> value of document is saved in <code>fundCategory</code>
     * field value of
     * <code>ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE</code>
     * collection,updation will not take place.
     *
     * @param fundcategoryId <code>_id</code> value of a document.
     * @param userId <code>_id</code> value of document in
     * <code>ApplicationConstants.FUND_CATEGORY_TABLE</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is <code>null</code>...
     */
    public String delete(String fundcategoryId, String userId) throws Exception {
        String result;
        if (fundcategoryId == null || fundcategoryId.isEmpty()) {
            return null;
        } else if ((DeleteDependencyManager.hasDependency(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, "fundCategory", fundcategoryId))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            Type type = new TypeToken<FundCategory>() {
            }.getType();
            String fundcategory = new FundCategoryManager().fetch(fundcategoryId);
            if (fundcategory == null || fundcategory.isEmpty()) {
                return null;
            }
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            FundCategory fundcategoryrJson = new Gson().fromJson(fundcategory, type);
            fundcategoryrJson.setStatus(ApplicationConstants.DELETE);
            fundcategoryrJson.setUpdateDate(System.currentTimeMillis() + "");
            fundcategoryrJson.setUpdatedBy(userName);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.FUND_CATEGORY_TABLE, fundcategoryId, new Gson().toJson(fundcategoryrJson));
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.FUND_CATEGORY_TABLE</code> collection.if
     * documents list already contains <code>category</code> field value,
     * updation will not take place.
     *
     * @param fundcategory <code>FundCategory</code> object data
     * @param fundcategoryId <code>_id</code> value of selected document
     * @param userId <code>_id</code> value of a document in
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> on successful updation.
     * @throws Exception if second or third argument is <code>null</code>..
     */
    public String update(FundCategory fundcategory, String fundcategoryId, String userId) throws Exception {

        String result = "";
        HashMap map = new HashMap();
        if (fundcategory == null || userId == null) {
            return null;
        }
        if (!(fundcategory.getCategory().equalsIgnoreCase(null))) {
            map.put("category", fundcategory.getCategory());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.FUND_CATEGORY_TABLE, map, fundcategoryId)) {
                result = ApplicationConstants.DUPLICATE;
            } else if (!(userId).equalsIgnoreCase(null)) {
                Type type = new TypeToken<FundCategory>() {
                }.getType();
                String dbStr = new FundCategoryManager().fetch(fundcategoryId);
                if (dbStr == null || dbStr.isEmpty()) {
                    return null;
                }
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                FundCategory dbObject = new Gson().fromJson(dbStr, type);

                fundcategory.setCreateDate(dbObject.getCreateDate());
                fundcategory.setUpdateDate(System.currentTimeMillis() + "");
                fundcategory.setStatus(ApplicationConstants.ACTIVE);
                fundcategory.setCreatedBy(dbObject.getCreatedBy());
                fundcategory.setUpdatedBy(userName);

                String objectJson = new Gson().toJson(fundcategory);

                boolean status = DBManager.getDbConnection().update(ApplicationConstants.FUND_CATEGORY_TABLE, fundcategoryId, objectJson);

                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }

        return result;

    }

    /**
     * Displays all the document list in the
     * <code>ApplicationConstants.FUND_CATEGORY_TABLE</code> collection.Those
     * documents, whose <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return List of documents in JSON string.
     * @throws Exception
     */
    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_CATEGORY_TABLE, conditionMap);
        return result;
    }
}
