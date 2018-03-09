/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.Location;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.Faculty;
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
public class FacultyManager {

    /**
     * Insets documents into <code>ApplicationConstants.FACULTY_TABLE</code>
     * collection.If document list already contains <code>facultyCode</code> or
     * <code>facultyName</code> document will not save to database
     *
     * @param loginUserId <code>_id</code> value of User object in hexadecimal
     * string.
     * @param faculty <code>Faculty</code> object data.
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful
     * @throws Exception if first argument is null...
     */
    public String saveFaculty(String loginUserId, Faculty faculty) throws Exception {

        if (loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("facultyCode", faculty.getFacultyCode());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.FACULTY_TABLE, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        map.clear();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("facultyName", faculty.getFacultyName());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.FACULTY_TABLE, map)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        faculty.setStatus(ApplicationConstants.ACTIVE);
        faculty.setCreateDate(System.currentTimeMillis() + "");
        faculty.setUpdateDate(System.currentTimeMillis() + "");
        faculty.setCreatedBy(userName);
        faculty.setUpdatedBy(userName);

        String result = DBManager.getDbConnection().insert(ApplicationConstants.FACULTY_TABLE, new Gson().toJson(faculty));

        return result;

    }

    /**
     * Get all documents list in the
     * <code>ApplicationConstants.FACULTY_TABLE</code> collection.Only those
     * documents <code>ApplicationConstants.STATUS</code> field is
     * <code>ApplicationConstants.Active</code> are appear in the list
     *
     * @param ddo ddo name
     * @param location location name
     * @return list of documents in JSON String
     * @throws Exception if either of argument is null
     */
    public String fetchAllFaculty(String ddo, String location) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("ddo", ddo);
        map.put("location", location);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FACULTY_TABLE, map);

        List<Faculty> facultyList = new Gson().fromJson(result, new TypeToken<List<Faculty>>() {
        }.getType());

        facultyList = getLocationName(facultyList);

        return new Gson().toJson(facultyList);

    }
//To get location name from location id.

    public static List<Faculty> getLocationName(List<Faculty> facultyList) throws Exception {

        Map<String, String> locationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> locationList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = locationList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();

            locationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < facultyList.size(); i++) {
            String locationId = facultyList.get(i).getLocation();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(locationId)) {
                    facultyList.get(i).setLocation(value);
                }
            }
        }

        return facultyList;
    }

    /**
     * Updates <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.FACULTY_TABLE</code>
     * collection.
     *
     * @param loginUserId _id value User object in hexadecimal string
     * @param primaryKey _id value of selected document in hexadecimal string
     *
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is null or empty
     */
    public String deleteFaculty(String primaryKey, String loginUserId) throws Exception {

        String result = "";
        if (primaryKey == null || primaryKey.isEmpty() || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        String facultyStr = DBManager.getDbConnection().fetch(ApplicationConstants.FACULTY_TABLE, primaryKey);

        if (facultyStr == null) {
            return null;
        }

        List<Faculty> facultyList = new Gson().fromJson(facultyStr, new TypeToken<List<Faculty>>() {
        }.getType());

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Faculty faculty = facultyList.get(0);

        faculty.setStatus(ApplicationConstants.DELETE);
        faculty.setUpdatedBy(userName);
        faculty.setUpdateDate(System.currentTimeMillis() + "");

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FACULTY_TABLE, primaryKey, new Gson().toJson(faculty));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.Faculty_TABLE</code> collection.If document
     * list already contains <code>facultyName</code> or
     * <code>facultyCode</code> value document will not update
     *
     * @param loginUserId _id value User object in hexadecimal string
     * @param primaryKey _id value of selected document in hexadecimal string
     * @param faculty <code>Faculty</code> object data
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if first or second argument is null or empty
     */
    public String updateFaculty(String primaryKey, String loginUserId, Faculty faculty) throws Exception {

        String result = "";
        if (primaryKey == null || primaryKey.isEmpty() || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("facultyCode", faculty.getFacultyCode());

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.FACULTY_TABLE, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        map.clear();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("facultyName", faculty.getFacultyName());

        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.FACULTY_TABLE, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        String facultyStr = DBManager.getDbConnection().fetch(ApplicationConstants.FACULTY_TABLE, primaryKey);

        if (facultyStr == null) {
            return null;
        }

        List<Faculty> facultyList = new Gson().fromJson(facultyStr, new TypeToken<List<Faculty>>() {
        }.getType());

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Faculty dbFaculty = facultyList.get(0);

        faculty.setStatus(ApplicationConstants.ACTIVE);
        faculty.setCreateDate(dbFaculty.getCreateDate());
        faculty.setCreatedBy(dbFaculty.getCreatedBy());
        faculty.setUpdateDate(System.currentTimeMillis() + "");
        faculty.setUpdatedBy(userName);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FACULTY_TABLE, primaryKey, new Gson().toJson(faculty));

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

}
