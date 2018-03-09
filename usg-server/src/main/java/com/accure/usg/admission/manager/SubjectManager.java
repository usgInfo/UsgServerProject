/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.Subject;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shwetha T S
 */
public class SubjectManager {

    /**
     * Inserts documents into <code>ApplicationConstants.SUBJECT_TABLE</code>
     * collection.if document list already contains <code>subjectCode</code> or
     * <code>subjectName</code> values document will not save.
     *
     * @param subject <code>Subject</code> object data
     * @param loginUserId <code>_id</code> value of <code>User</code> object in
     * hexadecimal string
     * @return <code>_id</code> value of inserted document if insertion is successful
     * @throws Exception
     */
    public String saveSubject(Subject subject, String loginUserId) throws Exception {

        if (loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        HashMap map = new HashMap();
        map.put("subjectCode", subject.getSubjectCode());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.SUBJECT_TABLE, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        map.clear();

        map.put("subjectName", subject.getSubjectName());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.SUBJECT_TABLE, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        subject.setStatus(ApplicationConstants.ACTIVE);
        subject.setCreatedBy(userName);
        subject.setUpdatedBy(userName);
        subject.setCreateDate(System.currentTimeMillis() + "");
        subject.setUpdateDate(System.currentTimeMillis() + "");

        String result = DBManager.getDbConnection().insert(ApplicationConstants.SUBJECT_TABLE, new Gson().toJson(subject));

        return result;
    }

    public String fetchAllSubject(String ddo, String location) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("ddo", ddo);
        map.put("location", location);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SUBJECT_TABLE, map);

        List<Subject> subjectList = new Gson().fromJson(result, new TypeToken<List<Subject>>() {
        }.getType());

        try {
            subjectList = getLocationName(subjectList);
            subjectList = getDDOName(subjectList);
        } catch (Exception ex) {

        }

        return new Gson().toJson(subjectList);
    }

    public static List<Subject> getLocationName(List<Subject> subjectList) throws Exception {

        Map<String, String> locationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> locationList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = locationList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();

            locationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < subjectList.size(); i++) {
            String locationId = subjectList.get(i).getLocation();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(locationId)) {
                    subjectList.get(i).setLocation(value);
                }
            }
        }

        return subjectList;
    }

    public static List<Subject> getDDOName(List<Subject> subjectList) throws Exception {

        Map<String, String> locationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> ddoList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = ddoList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();

            locationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < subjectList.size(); i++) {
            String ddoId = subjectList.get(i).getDdo();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(ddoId)) {
                    subjectList.get(i).setDdo(value);
                }
            }
        }

        return subjectList;
    }

    public String updateSubject(String primaryKey, String loginUserId, Subject subject) throws Exception {

        String result = "";

        if (primaryKey == null || primaryKey.isEmpty() || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        HashMap map = new HashMap();
        map.put("subjectCode", subject.getSubjectCode());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SUBJECT_TABLE, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        map.clear();

        map.put("subjectName", subject.getSubjectName());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SUBJECT_TABLE, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        String subjectJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUBJECT_TABLE, primaryKey);

        if (subjectJson == null) {
            return null;
        }

        List<Subject> subjectList = new Gson().fromJson(subjectJson, new TypeToken<List<Subject>>() {
        }.getType());

        Subject dbSubjectData = subjectList.get(0);

        subject.setCreateDate(dbSubjectData.getCreateDate());
        subject.setCreatedBy(dbSubjectData.getCreatedBy());
        subject.setUpdateDate(System.currentTimeMillis() + "");
        subject.setUpdatedBy(userName);
        subject.setStatus(ApplicationConstants.ACTIVE);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.SUBJECT_TABLE, primaryKey, new Gson().toJson(subject));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

    public String deleteSubject(String primaryKey, String loginUserId) throws Exception {
        String result = "";
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        String subjectJson = DBManager.getDbConnection().fetch(ApplicationConstants.SUBJECT_TABLE, primaryKey);

        if (subjectJson == null) {
            return null;
        }

        List<Subject> subjectList = new Gson().fromJson(subjectJson, new TypeToken<List<Subject>>() {
        }.getType());

        Subject dbSubjectData = subjectList.get(0);

        dbSubjectData.setStatus(ApplicationConstants.DELETE);
        dbSubjectData.setUpdatedBy(userName);
        dbSubjectData.setUpdateDate(System.currentTimeMillis() + "");

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.SUBJECT_TABLE, primaryKey, new Gson().toJson(dbSubjectData));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

}
