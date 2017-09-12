/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.pension.dto.PensionBank;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Shwetha T S
 */
public class PensionBankManager {

    public String save(PensionBank bank, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        boolean status = true;
        String result = "";

        HashMap duplicateCheckMap = new HashMap();

        String ifscCode = bank.getIfscCode();
        String micrCode = bank.getMicrCode();
        if (!(micrCode).equalsIgnoreCase(null) && !(micrCode).equals("")) {
            duplicateCheckMap.put("micrCode", micrCode);
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.PENSION_BANK_TABLE, duplicateCheckMap)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
                status = false;

            }
        }
        if (!(ifscCode).equalsIgnoreCase(null) && !(ifscCode).equals("")) {
            duplicateCheckMap.clear();
            duplicateCheckMap.put("ifscCode", ifscCode);
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.PENSION_BANK_TABLE, duplicateCheckMap)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
                status = false;

            }
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("branchName", bank.getBranchName());
        duplicateCheckMap.put("bankName", bank.getBankName());
        duplicateCheckMap.put("city", bank.getCity());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.PENSION_BANK_TABLE, duplicateCheckMap)) {
            result = ApplicationConstants.DUPLICATE_MESSAGE;
            status = false;

        }
        if (status) {
            bank.setCreatedBy(userName);
            bank.setCreateDate(System.currentTimeMillis() + "");
            bank.setUpdateDate(System.currentTimeMillis() + "");
            bank.setStatus(ApplicationConstants.ACTIVE);

            String bankPensionJson = new Gson().toJson(bank);

            String bankid = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_BANK_TABLE, bankPensionJson);
            if (bankid != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public boolean duplicate(HashMap conditionMap) throws Exception {
        boolean res = false;
        try {
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_BANK_TABLE, conditionMap);
            if (result != null) {
                res = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public boolean duplicateTest(PensionBank bank) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_BANK_TABLE, conditionMap);
            List<PensionBank> empList = new Gson().fromJson(result, new TypeToken<List<PensionBank>>() {
            }.getType());
            if (!empList.isEmpty()) {
                for (PensionBank cl : empList) {
                    if ((cl.getBankName().equalsIgnoreCase(bank.getBankName()) && (cl.getBranchName().equalsIgnoreCase(bank.getBranchName())))) {
                        res = true;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String fetch(String bankId) throws Exception {
        if (bankId == null || bankId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_BANK_TABLE, bankId);
        List<PensionBank> bankList = new Gson().fromJson(result, new TypeToken<List<PensionBank>>() {
        }.getType());
        if (bankList == null || bankList.size() < 1) {
            return null;
        }
        return new Gson().toJson(bankList.get(0));
    }

    public boolean delete(String bankId, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (bankId == null || bankId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionBank>() {
        }.getType();
        String bank = new PensionBankManager().fetch(bankId);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        PensionBank bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.DELETE);
        bankJson.setUpdateDate(System.currentTimeMillis() + "");
        bankJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_BANK_TABLE, bankId, new Gson().toJson(bankJson));

        return result;
    }

    public String update(PensionBank bank, String bankId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        HashMap duplicateCheckMap = new HashMap();
        String result;

        String ifscCode = bank.getIfscCode();
        String micrCode = bank.getMicrCode();
        if (!(micrCode).equalsIgnoreCase(null) && !(micrCode).equals("")) {
            duplicateCheckMap.put("micrCode", micrCode);
            if (hasDuplicateforUpdate(ApplicationConstants.PENSION_BANK_TABLE, duplicateCheckMap, bankId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
                return result;
            }
        }
        if (!(ifscCode).equalsIgnoreCase(null) && !(ifscCode).equals("")) {
            duplicateCheckMap.clear();
            duplicateCheckMap.put("ifscCode", ifscCode);
            if (hasDuplicateforUpdate(ApplicationConstants.PENSION_BANK_TABLE, duplicateCheckMap, bankId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
                return result;
            }
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("branchName", bank.getBranchName());
        duplicateCheckMap.put("bankName", bank.getBankName());
        duplicateCheckMap.put("city", bank.getCity());
        if (hasDuplicateforUpdate(ApplicationConstants.PENSION_BANK_TABLE, duplicateCheckMap, bankId)) {
            result = ApplicationConstants.DUPLICATE_MESSAGE;
            return result;
        } else {

            String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_BANK_TABLE, bankId);
            List<PensionBank> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionBank>>() {
            }.getType());
            PensionBank categoryobje = categorylist.get(0);
            bank.setCreateDate(categoryobje.getCreateDate());
            bank.setCreatedBy(categoryobje.getCreatedBy());
            bank.setUpdatedBy(userName);
            bank.setUpdateDate(System.currentTimeMillis() + "");
            bank.setStatus(ApplicationConstants.ACTIVE);
            String bankJson = new Gson().toJson(bank);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_BANK_TABLE, bankId, bankJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }

    public static boolean hasDuplicateforUpdate(String tableName, HashMap columns, String id) {
        boolean status = false;
        String result = null;
        HashMap duplicatecheckMap = new HashMap();
        try {
            if (!columns.isEmpty()) {
                Iterator entries = columns.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    duplicatecheckMap.put((String) entry.getKey(), Pattern.compile("^" + ((String) entry.getValue()).replace("\\s", "") + "$", (Pattern.CASE_INSENSITIVE | Pattern.COMMENTS)));
                }
                duplicatecheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_BANK_TABLE, duplicatecheckMap);
                if (result1 == null) {
                    status = false;
                    return status;
                } else {
                    List<PensionBank> employeeList = new Gson().fromJson(result1, new TypeToken<List<PensionBank>>() {
                    }.getType());
                    //System.out.println("status" + status);
                    for (PensionBank city : employeeList) {
                        String obid = ((LinkedTreeMap<String, String>) city.getId()).get("$oid");
                        if (!obid.equals(id)) {
                            status = true;
                            break;
                        } else {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            status = false;
        } finally {
            //System.out.println("status" + status);
            return status;
        }
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_BANK_TABLE, conditionMap);
        return result;
    }

}
