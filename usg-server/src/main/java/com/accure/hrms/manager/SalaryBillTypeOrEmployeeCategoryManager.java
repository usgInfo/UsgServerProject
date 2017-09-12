/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.SalaryBillTypeOREmployeeCategory;
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
 * @author
 */
public class SalaryBillTypeOrEmployeeCategoryManager {

    /**
     * Inserts documents into the
     * <code>ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER</code>
     * collection.If document list already contains <code>description</code>
     * value,insertion will not happen.
     *
     * @param salaryBillType <code>SalaryBillTypeOREmployeeCategory</code>
     * object data.
     * @param loginUserId <code>_id</code> value of a document in the
     * <code>ApplicationConstants.USER</code> collection.
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if second argument is <code>null</code>..
     */
    public String save(SalaryBillTypeOREmployeeCategory salaryBillType, String loginUserId) throws Exception {

        String result;
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("description", salaryBillType.getDescription());

        if (loginUserId == null) {

            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, hMap)) {

            result = ApplicationConstants.DUPLICATE;

        } else {

            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            salaryBillType.setCreateDate(System.currentTimeMillis() + "");
            salaryBillType.setUpdateDate(System.currentTimeMillis() + "");
            salaryBillType.setStatus(ApplicationConstants.ACTIVE);
            salaryBillType.setCreatedBy(userName);
            salaryBillType.setUpdatedBy(userName);
            String jsonStr = new Gson().toJson(salaryBillType);
            result = DBManager.getDbConnection().insert(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, jsonStr);
            if (result != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    /**
     * Searches the document in the
     * <code>ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER</code>
     * collection.
     *
     * @param Id <code>_id</code> value of document in JSON String.
     * @return document in the JSON String
     * @throws Exception if argument is <code>null</code>
     */
    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, Id);

        if (result == null) {
            return null;
        }

        List<SalaryBillTypeOREmployeeCategory> list = new Gson().fromJson(result, new TypeToken<List<SalaryBillTypeOREmployeeCategory>>() {
        }.getType());

        return new Gson().toJson(list.get(0));
    }

    /**
     * Updates status field of selected document in the
     * <code>ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER</code>
     *
     * @param Id <code>_id</code> value of selected document.
     * @param loginUserId <code>_id</code> value of a document in the
     * <code>ApplicationConstants.USER</code> collection
     * @return <code>True</code> if updation is successful
     * @throws Exception if either of document is <code>null</code>..
     */
    public boolean delete(String Id, String loginUserId) throws Exception {

        if (Id == null || Id.isEmpty()) {
            return false;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<SalaryBillTypeOREmployeeCategory>() {
        }.getType();

        String dbStr = new SalaryBillTypeOrEmployeeCategoryManager().fetch(Id);

        if (dbStr == null || dbStr.isEmpty()) {
            return false;
        }
        SalaryBillTypeOREmployeeCategory dbObj = new Gson().fromJson(dbStr, type);
        dbObj.setStatus(ApplicationConstants.DELETE);
        dbObj.setDeletedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, Id, new Gson().toJson(dbObj));
        return result;
    }

    /**
     * Updates selected document of
     * <code>ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER</code>
     * collection.If document list already contains <code>description</code>
     * field value updation will not happen.
     *
     * @param salaryBillType <code>SalaryBillTypeOREmployeeCategory</code>
     * object data.
     * @param Id <code>_id</code> value of selected document.
     * @param loginUserId <code>_id</code> value of a document in the
     * <code>ApplicationConstants.USER</code> collection
     * @return
     * @throws Exception
     */
    public String update(SalaryBillTypeOREmployeeCategory salaryBillType, String Id, String loginUserId) throws Exception {

        String result;

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("description", salaryBillType.getDescription());

        if (Id == null || loginUserId == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, conditionMap, Id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            Type type = new TypeToken<SalaryBillTypeOREmployeeCategory>() {
            }.getType();
            String dbStr = new SalaryBillTypeOrEmployeeCategoryManager().fetch(Id);
            if (dbStr == null || dbStr.isEmpty()) {
                return null;
            }
            SalaryBillTypeOREmployeeCategory dbObj = new Gson().fromJson(dbStr, type);

            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setStatus(ApplicationConstants.ACTIVE);
            dbObj.setDescription(salaryBillType.getDescription());
            dbObj.setUpdatedBy(userName);

            String jsonStr = new Gson().toJson(dbObj);

            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, Id, jsonStr);

            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    /**
     * Displays all the documents in the
     * <code>ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER</code>
     * collection.Those documents, whose
     * <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.ACTIVE</code> are appear in list
     *
     * @return List of documents in the
     * <code>ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER</code>
     * collection,in JSON String format.
     * @throws Exception
     */
    public String fetchAll() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_BILL_TYPE_OR_EMP_CATEGORY_MASTER, hMap);

        return result;
    }

}
