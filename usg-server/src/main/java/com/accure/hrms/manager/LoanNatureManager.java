/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.LoanNature;
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
 * @author accure
 */
public class LoanNatureManager {

    /**
     * Inserts document into <code>ApplicationConstants.LOAN_NATURE_TABLE</code>
     * collection.If document list already contains <code>loanName</code> field
     * value insertion will not happen.
     *
     * @param loanNature <code>LoanNature</code> object data
     * @param userid <code>_id</code> value of document in the
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if second argument is null..
     */
    public String save(LoanNature loanNature, String userid) throws Exception {

        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("loanName", loanNature.getLoanName());

        if (userid == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.LOAN_NATURE_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + " " + user.getLname();
            loanNature.setStatus(ApplicationConstants.ACTIVE);
            loanNature.setCreateDate(System.currentTimeMillis() + "");
            loanNature.setUpdateDate(System.currentTimeMillis() + "");
            loanNature.setCreatedBy(userName);
            loanNature.setUpdatedBy(userName);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_NATURE_TABLE, new Gson().toJson(loanNature));
            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    /**
     * Displays all document in the
     * <code>ApplicationConstants.LOAN_NATURE_TABLE</code> collection.Only those
     * documents,whose <code>ApplicationConstants.STATUS</code> field value is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return list of documents in JSOn string.
     * @throws Exception
     */
    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fundJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_NATURE_TABLE, conditionMap);
        return fundJson;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.LOAN_NATURE_TABLE</code> collection.If
     * document list already contains <code>loanName</code> field
     * value,insertion will not take place.
     *
     * @param loanNature <code>LoanNature</code> object data
     * @param id <code>_id</code> value of selected document in hexadecimal
     * string
     * @param userId <code>_id</code> value of document(in hexadecimal String)
     * in the <code>ApplicationConstants.USER</code> collection,
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second and third argument is null..
     */
    public String update(LoanNature loanNature, String id, String userId) throws Exception {
        String result = "";

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("loanName", loanNature.getLoanName());

        if (userId == null) {
            return null;
        } else if (isDuplicateforUpdate(ApplicationConstants.LOAN_NATURE_TABLE, conditionMap, id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();

            String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_NATURE_TABLE, id);
            if (dbStr != null) {

                List<LoanNature> list = new Gson().fromJson(dbStr, new TypeToken<List<LoanNature>>() {
                }.getType());
                LoanNature dbObj = list.get(0);

                dbObj.setIsRefundable(loanNature.getIsRefundable());
                dbObj.setLoanName(loanNature.getLoanName());
                dbObj.setUpdatedBy(userName);
                dbObj.setUpdateDate(System.currentTimeMillis() + "");
                dbObj.setStatus(ApplicationConstants.ACTIVE);
                boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.LOAN_NATURE_TABLE, id, new Gson().toJson(dbObj));

                if (fResult) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }

        return result;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in <code>(ApplicationConstants.LOAN_NATURE_TABLE</code>
     * collection.
     *
     * @param id <code>_id</code> value of selected document in hexadecimal
     * String
     * @param userId <code>_id</code> value of a document (in hexadecimal
     * string) in <code>(ApplicationConstants.USER</code> collection
     * @return <code>True</code> if updation is successful
     * @throws Exception if either of argument is null..
     */
    public boolean delete(String id, String userId) throws Exception {

        if (id == null || userId == null) {
            return false;
        }

        String dbStr = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_NATURE_TABLE, id);

        if (dbStr == null) {
            return false;
        }

        List<LoanNature> list = new Gson().fromJson(dbStr, new TypeToken<List<LoanNature>>() {
        }.getType());

        LoanNature dbObj = list.get(0);

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        dbObj.setUpdatedBy(userName);
        dbObj.setStatus(ApplicationConstants.DELETE);

        boolean rs = DBManager.getDbConnection().update(ApplicationConstants.LOAN_NATURE_TABLE, id, new Gson().toJson(dbObj));

        return rs;
    }

    public String fetchLoanNatureBasedInId(String id) throws Exception {

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("_id", id);

        String fundJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_NATURE_TABLE, id);
        List<LoanNature> list = new Gson().fromJson(fundJson, new TypeToken<List<LoanNature>>() {
        }.getType());
        LoanNature loanNature = list.get(0);
        String isrefundable = loanNature.getIsRefundable();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("isrefundable", isrefundable);

        return new Gson().toJson(map);

    }
}
