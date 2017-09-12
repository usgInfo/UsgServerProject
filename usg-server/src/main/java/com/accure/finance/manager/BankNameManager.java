/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.BankName;
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
 * @author Shwetha T S
 */
public class BankNameManager {

    /**
     * inserts document into <code>ApplicationConstants.BANK_NAME_TABLE</code>
     * collection.if document list in the collection already contains
     * <code>accNumber</code>,<code>micrCode</code>,<code>ifscCode</code> and
     * combination of <code>branchName</code>,<code>bankName</code> and
     * <code>city</code> then insertion will not take place.
     *
     * @param bankName <code>BankName</code> object data.
     * @param loginUserId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal string
     * @return <code>ApplicationConstants.SUCCESS</code> on successful
     * insertion.
     * @throws Exception if second argument is <code>null</code>...
     */
    public String saveBankName(BankName bankName, String loginUserId) throws Exception {
        String result = "";

        HashMap map = new HashMap();
        if (loginUserId == null || bankName == null) {
            return null;
        }
        if (bankName.getAccNumber() != null && !bankName.getAccNumber().isEmpty()) {
            map.clear();
            map.put("accNumber", bankName.getAccNumber());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.BANK_NAME_TABLE, map)) {
                return ApplicationConstants.ACCOUNT_NUMBER_EXISTED;
            }
        } else {

            return ApplicationConstants.INVALID_ACCOUNT_NUMBER;
        }

        if (bankName.getMicrCode() != null && !(bankName.getMicrCode().isEmpty())) {
            map.clear();
            map.put("micrCode", bankName.getMicrCode());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.BANK_NAME_TABLE, map)) {
                return ApplicationConstants.MICR_CODE_EXISTED;
            }
        }
        if (bankName.getIfscCode() != null && !bankName.getIfscCode().isEmpty()) {
            map.clear();
            map.put("ifscCode", bankName.getIfscCode());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.BANK_NAME_TABLE, map)) {
                return ApplicationConstants.IFSC_CODE_EXISTED;
            }
        }
        if (!(bankName.getCity()).equalsIgnoreCase("") || !(bankName.getCity()).equalsIgnoreCase(null)
                || !(bankName.getBranchName()).equalsIgnoreCase("") || !(bankName.getBranchName()).equalsIgnoreCase(null) || !(bankName.getBankName()).equalsIgnoreCase("") || !(bankName.getBankName()).equalsIgnoreCase(null)) {
            map.clear();
            map.put("branchName", bankName.getBranchName());
            map.put("bankName", bankName.getBankName());
            map.put("city", bankName.getCity());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.BANK_NAME_TABLE, map)) {
                return ApplicationConstants.DUPLICATE_MESSAGE;
            }
        }

        if (!(loginUserId).equalsIgnoreCase(null)) {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            bankName.setCreateDate(System.currentTimeMillis() + "");
            bankName.setUpdateDate(System.currentTimeMillis() + "");
            bankName.setStatus(ApplicationConstants.ACTIVE);
            bankName.setCreatedBy(userName);
            bankName.setUpdatedBy(userName);
            String objJson = new Gson().toJson(bankName);
            String Id = DBManager.getDbConnection().insert(ApplicationConstants.BANK_NAME_TABLE, objJson);
            if (Id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    /**
     * Fetch the list of documents from
     * <code>ApplicationConstants.BANK_NAME_TABLE</code> collection.only those
     * documents whose <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.ACTIVE</code> are appear in the list
     *
     * @return document list of
     * <code>ApplicationConstants.BANK_NAME_TABLE</code> collection in JSON
     * String
     * @throws Exception
     */
    public String viewBankNameList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_NAME_TABLE, conditionMap);
        return result;

    }

    /**
     * Updates a document of <code>ApplicationConstants.BANK_NAME_TABLE</code>
     * collection.if document list in the collection already contains
     * <code>accNumber</code>,<code>micrCode</code>,<code>ifscCode</code> and
     * combination of <code>branchName</code>,<code>bankName</code> and
     * <code>city</code> then updation will not take place.
     *
     * @param bankName <code>BankName</code> object data.
     * @param id <code>_id</code> value of <code>BankName</code> object.
     * @param loginUserId <code>_id</code> value of <code>User</code> object.
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if second or third argument is <code>null</code>..
     */
    public String updateBankName(BankName bankName, String id, String loginUserId) throws Exception {
        String result = "";

        HashMap map = new HashMap();
        if (loginUserId == null || bankName == null) {
            return null;
        }

        if (bankName.getAccNumber() != null && !bankName.getAccNumber().isEmpty()) {
            map.clear();
            map.put("accNumber", bankName.getAccNumber());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BANK_NAME_TABLE, map, id)) {

                return ApplicationConstants.ACCOUNT_NUMBER_EXISTED;
            }
        } else {

            return ApplicationConstants.INVALID_ACCOUNT_NUMBER;
        }
        if (bankName.getMicrCode() != null && !(bankName.getMicrCode().isEmpty())) {
            map.clear();
            map.put("micrCode", bankName.getMicrCode());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BANK_NAME_TABLE, map, id)) {

                return ApplicationConstants.DUPLICATE_MESSAGE;
            }
        }
        if (bankName.getIfscCode() != null && !bankName.getIfscCode().isEmpty()) {
            map.clear();
            map.put("ifscCode", bankName.getIfscCode());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BANK_NAME_TABLE, map, id)) {
                return ApplicationConstants.DUPLICATE_MESSAGE;
            }
        }
        if (!(bankName.getCity()).equalsIgnoreCase("") || !(bankName.getCity()).equalsIgnoreCase(null)
                || !(bankName.getBranchName()).equalsIgnoreCase("") || !(bankName.getBranchName()).equalsIgnoreCase(null) || !(bankName.getBankName()).equalsIgnoreCase("") || !(bankName.getBankName()).equalsIgnoreCase(null)) {
            map.clear();
            map.put("branchName", bankName.getBranchName());
            map.put("bankName", bankName.getBankName());
            map.put("city", bankName.getCity());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BANK_NAME_TABLE, map, id)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
            }
        }

        if (!(loginUserId).equalsIgnoreCase(null)) {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            BankName bank = fetch(id);
            bankName.setCreateDate(bank.getCreateDate());
            bankName.setUpdateDate(System.currentTimeMillis() + "");
            bankName.setStatus(ApplicationConstants.ACTIVE);
            bankName.setCreatedBy(bank.getCreatedBy());
            bankName.setUpdatedBy(userName);
            String objJson = new Gson().toJson(bankName);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.BANK_NAME_TABLE, id, objJson);
            if (flag) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    /**
     * Updates <code>status</code> field of a document in the
     * <code>ApplicationConstants.BANK_NAME_TABLE</code> collection.If
     * <code>_id</code> value of a document is already being save in
     * <code>bank</code> field of document in
     * <code>ApplicationConstants.FDR_PROCESS_TABLE</code> collection or
     * <code>ApplicationConstants.FIXED_DEPOSIT_TABLE</code> collection or
     * <code>ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE</code>
     * collection updation will not takes place.
     *
     * @param primaryKey <code>_id</code> value of a document in the
     * <code>ApplicationConstants.BANK_NAME_TABLE</code> collection
     * @param loginUserId  <code>_id</code> value of <code>User</code> object.
     *
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful.
     * @throws Exception if either of argument is <code>null</code>...
     */
    public String deleteBankName(String primaryKey, String loginUserId) throws Exception {

        String status;
        if (primaryKey == null || loginUserId == null) {
            return null;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.FDR_PROCESS_TABLE, "bank", primaryKey)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.FIXED_DEPOSIT_TABLE, "bank", primaryKey) || (DeleteDependencyManager.hasDependency(ApplicationConstants.BANK_CHEQUE_CONFIGURATION_TABLE, "bank", primaryKey)))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            BankName bank = fetch(primaryKey);
            bank.setUpdateDate(System.currentTimeMillis() + "");
            bank.setDeletedBy(userName);
            bank.setStatus(ApplicationConstants.DELETE);
            String bankJson = new Gson().toJson(bank);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.BANK_NAME_TABLE, primaryKey, bankJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;
    }

    /**
     * Search the <code>BankName</code> object data.
     *
     * @param primaryKey <code>_id</code> value of <code>BankName</code> object
     * data
     * @return <code>BankName</code> object
     * @throws Exception if argument is null..
     */
    public BankName fetch(String primaryKey) throws Exception {
        if (primaryKey == null) {
            return null;
        }
        String bankJson = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_NAME_TABLE, primaryKey);
        List<BankName> banklist = new Gson().fromJson(bankJson, new TypeToken<List<BankName>>() {
        }.getType());
        BankName bank = banklist.get(0);
        return bank;
    }

}
