
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.FundType;
import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
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
 * @author user
 */
public class FundTypeManager {

    /**
     * Inserts document into <code>ApplicationConstants.FUND_TYPE_TABLE</code>
     * collection. If document list already contains  <code>description</code> or
     * <code>order</code> fields value,insertion will not take place.
     *
     * @param fundTypeStr <code>FundType</code> object data in JSON String.
     * @param userId <code>_id</code>
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful.
     * @throws Exception if second argument is <code>null</code>..
     */
    public String save(String fundTypeStr, String userId) throws Exception {
        String result;
        Type type = new TypeToken<FundType>() {
        }.getType();
        FundType fundType = new Gson().fromJson(fundTypeStr, type);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("description", fundType.getDescription());

        HashMap map = new HashMap<String, Integer>();
        map.put("order", fundType.getOrder());

        if (userId == null) {
            result = null;
        } else if ((hasDuplicateforSave(ApplicationConstants.FUND_TYPE_TABLE, map)) || (hasDuplicateforSave(ApplicationConstants.FUND_TYPE_TABLE, conditionMap))) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        } else {

            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            fundType.setCreateDate(System.currentTimeMillis() + "");
            fundType.setUpdateDate(System.currentTimeMillis() + "");
            fundType.setStatus(ApplicationConstants.ACTIVE);
            fundType.setCreatedBy(userName);
            fundType.setUpdatedBy(userName);
            String fundTypeJson = new Gson().toJson(fundType);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.FUND_TYPE_TABLE, fundTypeJson);
            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    public HashMap<String, String> fetchAllFundType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fundTypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_TYPE_TABLE, conditionMap);
        if (fundTypeJson != null) {
            List<FundType> fundTypelist = new Gson().fromJson(fundTypeJson, new TypeToken<List<FundType>>() {
            }.getType());
            conditionMap.clear();
            for (FundType fundType : fundTypelist) {
                conditionMap.put(((Map<String, String>) fundType.getId()).get("$oid"), fundType.getDescription());
            }

        }
        return conditionMap;
    }

    public List<FundType> view() throws Exception {
        List<FundType> resultList = new ArrayList<FundType>();
        HashMap<String, String> parent = fetchAllFundType();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_TYPE_TABLE, conditionMap);
        List<FundType> fundTypeList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        if (result != null) {
            for (FundType fundType : fundTypeList) {
                if (fundType.getUnderFundType() != null) {
                    String id = fundType.getUnderFundType();
                    if (parent.containsKey(id) && parent.get(id) != null) {
                        fundType.setFundTypeName(parent.get(id));
                    }
                }
                resultList.add(fundType);
            }
        }
        return resultList;

    }

    public FundType fetch(String fundtypeId) throws Exception {
        if (fundtypeId == null || fundtypeId.isEmpty()) {
            return null;
        }
        String fundTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, fundtypeId);
        if (fundTypeJson == null || fundTypeJson.isEmpty()) {
            return null;
        }
        List<FundType> fundTypeLIst = new Gson().fromJson(fundTypeJson, new TypeToken<List<FundType>>() {
        }.getType());
        if (fundTypeLIst == null || fundTypeLIst.isEmpty()) {
            return null;
        }
        return fundTypeLIst.get(0);

    }

    /**
     * Updates <code>status</code> field of selected document in the
     * <code>ApplicationConstants.FUND_TYPE_TABLE</code> collection.If
     * <code>_id</code> value (in hexadecimal string) of document is already
     * saved in <code>fundType</code> field of
     * <code>ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE</code> or
     * <code>ApplicationConstants.FUND_HEAD_MAPPING_TABLE</code> or
     * <code>ApplicationConstants.EMPLOYEE_TABLE</code> or
     * <code>ApplicationConstants.EMP_ATTENDANCE_TABLE</code> or
     * <code>ApplicationConstants.EMPLOYEE_PROMOTION_TABLE</code>
     * collection,document will not get update.
     *
     * @param fundtypeId <code>_id</code> value of document in
     * <code>ApplicationConstants.FUND_TYPE_TABLE</code> collection
     * @param userId <code>_id</code> value of document in
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>pplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of document is <code>null</code>..
     */
    public String deleteFundType(String fundtypeId, String userId) throws Exception {

        FundType fundType = fetch(fundtypeId);
        fundType.setStatus(ApplicationConstants.DELETE);
        fundType.setUpdateDate(System.currentTimeMillis() + "");
        fundType.setUpdatedBy(userId);
        String fundTypeJson = new Gson().toJson(fundType);
        String status;

        if (DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, "fundType", fundtypeId) || DeleteDependencyManager.hasDependency(ApplicationConstants.FUND_HEAD_MAPPING_TABLE, "fundType", fundtypeId) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "fundType", fundtypeId) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMP_ATTENDANCE_TABLE, "fundType", fundtypeId) || DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, "fundType", fundtypeId)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.FUND_TYPE_TABLE, fundtypeId, fundTypeJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    /**
     * Updates selected document of
     * <code>ApplicationConstants.FUND_TYPE_TABLE</code> collection.If document
     * list already contains  <code>description</code> or <code>order</code>
     * fields value,insertion will not take place.
     *
     * @param object <code>FundType</code> object data
     * @param id <code>_id</code> value of document in
     * <code>ApplicationConstants.FUND_TYPE_TABLE</code> collection
     * @param userId <code>_id</code> value of document in
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is <code>null</code>..
     */
    public String update(FundType object, String id, String userId) throws Exception {

        String result;
        HashMap conditionMap = new HashMap();
        conditionMap.put("description", object.getDescription());

        HashMap map = new HashMap();
        map.put("order", object.getOrder());

        if (userId == null) {
            result = null;
        } else if ((isDuplicateforUpdate(ApplicationConstants.FUND_TYPE_TABLE, map, id)) || (isDuplicateforUpdate(ApplicationConstants.FUND_TYPE_TABLE, conditionMap, id))) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        } else {

            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            FundType fundType = new FundTypeManager().fetch(id);
            fundType.setDescription(object.getDescription());
            fundType.setUnderFundType(object.getUnderFundType());
            fundType.setOrder(object.getOrder());
            fundType.setUpdateDate(System.currentTimeMillis() + "");
            fundType.setStatus(ApplicationConstants.ACTIVE);
            fundType.setUpdatedBy(userName);
            String json = new Gson().toJson(fundType);
            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.FUND_TYPE_TABLE, id, json);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;

    }

}
