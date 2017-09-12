/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Relation;
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
 * @author upendra
 */
public class RelationManager {

    public String save(String relation, String relationRemaks, String loginUserId) throws Exception {

        String status = null;
        if (relation == null) {
            return null;
        }
        User user = new UserManager().fetch(loginUserId);
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("relation", relation);
        String userName = user.getFname() + " " + user.getLname();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.RELATION_TABLE, duplicateCheckMap)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
        if (status == null || status.isEmpty()) {
            Relation relationobj = new Relation();
            relationobj.setRelation(relation);
            relationobj.setRelationRemarks(relationRemaks);
            relationobj.setStatus(ApplicationConstants.ACTIVE);
            relationobj.setCreateDate(System.currentTimeMillis() + "");
            relationobj.setCreatedBy(userName);
            String relationjson = new Gson().toJson(relationobj);
            status = DBManager.getDbConnection().insert(ApplicationConstants.RELATION_TABLE, relationjson);
        }
        return status;

    }

    public String viewList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.RELATION_TABLE, conditionMap);
        return result;

    }

    public Relation fetch(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String relationJson = DBManager.getDbConnection().fetch(ApplicationConstants.RELATION_TABLE, rid);
        List<Relation> relationlist = new Gson().fromJson(relationJson, new TypeToken<List<Relation>>() {
        }.getType());
        Relation relation = relationlist.get(0);
        return relation;
    }

    public boolean updateRelation(Relation dbRelObj, String pKey) throws Exception {
        String relationjson = new Gson().toJson(dbRelObj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.RELATION_TABLE, pKey, relationjson);
        return status;
    }

    public String update(String religion, String remarks, String rid, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);

        String status = null;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("relation", religion);
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.RELATION_TABLE, duplicateCheckMap, rid)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
        if (status == null || status.isEmpty()) {
            String relationJson = DBManager.getDbConnection().fetch(ApplicationConstants.RELATION_TABLE, rid);
            List<Relation> relationlist = new Gson().fromJson(relationJson, new TypeToken<List<Relation>>() {
            }.getType());
            Relation relationdata = relationlist.get(0);
            String userName = user.getFname() + " " + user.getLname();
            Relation dbRelObj = fetch(rid);
            dbRelObj.setRelation(religion.replaceAll(rid, religion));
            dbRelObj.setRelationRemarks(remarks);
            dbRelObj.setCreatedBy(relationdata.getCreatedBy());
            dbRelObj.setCreateDate(relationdata.getCreateDate());
            dbRelObj.setUpdateDate(System.currentTimeMillis() + "");
            dbRelObj.setUpdatedBy(userName);
            boolean flag = updateRelation(dbRelObj, rid);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public String delete(String rid, String loginUserId) throws Exception {

        String existsalutationJson = DBManager.getDbConnection().fetch(ApplicationConstants.RELATION_TABLE, rid);
        List<Relation> relationlist = new Gson().fromJson(existsalutationJson, new TypeToken<List<Relation>>() {
        }.getType());
        String status;
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Relation relation = relationlist.get(0);
        Relation dbRelObj = fetch(rid);
        dbRelObj.setStatus(ApplicationConstants.DELETE);
        dbRelObj.setCreatedBy(relation.getCreatedBy());
        dbRelObj.setCreateDate(relation.getCreateDate());
        dbRelObj.setUpdateDate(System.currentTimeMillis() + "");
        dbRelObj.setUpdatedBy(relation.getUpdatedBy());
        dbRelObj.setDeletedBy(userName);
        String relationjson = new Gson().toJson(dbRelObj);
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.NOMINEE_TABLE, "nomineeList.relationShip", rid)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.RELATION_TABLE, rid, relationjson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

}
