/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import com.accure.pension.dto.PensionAssociation;
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
public class PensionAssociationManager {

    public String save(PensionAssociation association, String userid) throws Exception {
        String result;

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pensionAssociationName", association.getPensionAssociationName());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (hasDuplicateforSave(ApplicationConstants.PENSION_ASSOCIATION_TABLE, map)) {
            result = ApplicationConstants.DUPLICATE;

        } else {
            association.setStatus(ApplicationConstants.ACTIVE);
            association.setCreateDate(System.currentTimeMillis() + "");
            association.setUpdateDate(System.currentTimeMillis() + "");
            association.setUpdatedBy(userName);
            association.setCreatedBy(userName);
            String GISjson = new Gson().toJson(association);
            String id = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_ASSOCIATION_TABLE, GISjson);

            if (id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    public String view() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ASSOCIATION_TABLE, hMap);
        return result;
    }

    public boolean duplicate(PensionAssociation association) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ASSOCIATION_TABLE, conditionMap);
            if (result != null) {
                List<PensionAssociation> list = new Gson().fromJson(result, new TypeToken<List<PensionAssociation>>() {
                }.getType());
                for (PensionAssociation li : list) {
                    if (association.getPensionAssociationName().equalsIgnoreCase(li.getPensionAssociationName())) {
                        res = true;
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String update(PensionAssociation association, String associationId, String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        HashMap duplicateCheckMap = new HashMap();
        String result = "";

        String associationName = association.getPensionAssociationName();
        if (!(associationName).equalsIgnoreCase(null) && !(associationName).equals("")) {
            duplicateCheckMap.put("pensionAssociationName", associationName);
            if (hasDuplicate(ApplicationConstants.PENSION_ASSOCIATION_TABLE, duplicateCheckMap, associationId)) {
                result = ApplicationConstants.DUPLICATE_MESSAGE;

            } else {

                String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_ASSOCIATION_TABLE, associationId);
                List<PensionAssociation> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<PensionAssociation>>() {
                }.getType());
                PensionAssociation categoryobje = categorylist.get(0);
                association.setUpdatedBy(userName);
                association.setCreateDate(categoryobje.getCreateDate());
                association.setCreatedBy(categoryobje.getCreatedBy());
                association.setUpdateDate(System.currentTimeMillis() + "");
                association.setStatus(ApplicationConstants.ACTIVE);
                String associationJson = new Gson().toJson(association);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.PENSION_ASSOCIATION_TABLE, associationId, associationJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public static boolean hasDuplicate(String tableName, HashMap columns, String id) {
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
                String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ASSOCIATION_TABLE, duplicatecheckMap);
                if (result1 == null) {
                    status = false;
                    return status;
                } else {
                    List<PensionAssociation> associationList = new Gson().fromJson(result1, new TypeToken<List<PensionAssociation>>() {
                    }.getType());
                    //System.out.println("status" + status);
                    for (PensionAssociation pensionAssociationName : associationList) {
                        String obid = ((LinkedTreeMap<String, String>) pensionAssociationName.getId()).get("$oid");
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

    public boolean delete(String id, String userid) throws Exception {

        boolean result;
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        if (id == null || id.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<PensionAssociation>() {
        }.getType();
        String assStr = new PensionAssociationManager().fetch(id);
        if (assStr == null || assStr.isEmpty()) {
            return false;
        }
        PensionAssociation assJson = new Gson().fromJson(assStr, type);
        assJson.setStatus(ApplicationConstants.DELETE);
        assJson.setUpdatedBy(userName);
        assJson.setUpdateDate(System.currentTimeMillis() + "");
        result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_ASSOCIATION_TABLE, id, new Gson().toJson(assJson));
        return result;

    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_ASSOCIATION_TABLE, Id);
        List<PensionAssociation> gisList = new Gson().fromJson(result, new TypeToken<List<PensionAssociation>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

}
