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
import com.accure.usg.admission.dto.Course;
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
 * @author khurshid
 */
public class CourseManager {

    /**
     * Inserts documents into
     *
     * @conditions <code>ApplicationConstants.COURSE_MASTER_TABLE</code>
     * collection.If document list already contains <code>courseName</code>
     * field value, document will not save into collection
     *
     * @param courseJson <code>Course</code> object data
     * @param loginUserId <code>_id</code> value of a document(in hexadecimal
     * string) in <code>ApplicationConstants.USER</code> collection.
     * @return <code>_id</code> value of inserted document ,if insertion is
     * successful
     * @throws Exception if second argument is null..
     */
    public String saveCourseMaster(Course courseJson, String loginUserId) throws Exception {
        if (courseJson == null || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("courseCode", courseJson.getCourseCode());
        duplicateCheckMap.put("courseName", courseJson.getCourseName());

        if (Duplicate.hasDuplicateforSave(ApplicationConstants.COURSE_MASTER_TABLE, duplicateCheckMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        courseJson.setStatus(ApplicationConstants.ACTIVE);
        courseJson.setCreateDate(System.currentTimeMillis() + "");
        courseJson.setUpdateDate(System.currentTimeMillis() + "");
        courseJson.setCreatedBy(userName);
        courseJson.setUpdatedBy(userName);
        String jsonCsr = new Gson().toJson(courseJson);

        String courseKey = DBManager.getDbConnection().insert(ApplicationConstants.COURSE_MASTER_TABLE, jsonCsr);

        return courseKey;
    }

    public String viewCourseMasterList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COURSE_MASTER_TABLE, conditionMap);
        return result;

    }

     /**
     * Updates selected document in the
     * <code>ApplicationConstants.COURSE_MASTER_TABLE</code> collection.If document
     * list already contains <code>courseName</code> value document will not
     * update
     *
     * @param loginUserId _id value User object in hexadecimal string
     * @param primaryKey _id value of selected document in hexadecimal string
     * @param courseJson <code>Course</code> object data
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if first or second argument is null or empty
     */
    
    public String updateCourseMaster(Course courseJson, String primaryKey, String loginUserId) throws Exception {
        if (loginUserId == null || loginUserId.isEmpty() || primaryKey == null || primaryKey.isEmpty() || courseJson == null) {
            return null;
        }
        HashMap map = new HashMap();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("periodName", courseJson.getCourseName());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.COURSE_MASTER_TABLE, map, primaryKey)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        String courseStr = DBManager.getDbConnection().fetch(ApplicationConstants.COURSE_MASTER_TABLE, primaryKey);

        if (courseStr == null) {
            return null;
        }

        List<Course> list = new Gson().fromJson(courseStr, new TypeToken<List<Course>>() {
        }.getType());

        Course dbCursorData = list.get(0);

        courseJson.setStatus(ApplicationConstants.ACTIVE);
        courseJson.setUpdateDate(System.currentTimeMillis() + "");
        courseJson.setUpdatedBy(userName);
        courseJson.setCreateDate(dbCursorData.getCreateDate());
        courseJson.setCreatedBy(dbCursorData.getCreatedBy());

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.COURSE_MASTER_TABLE, primaryKey, new Gson().toJson(courseJson));
        String result="";
        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }
    /**
     * Delete <code>ApplicationConstants.STATUS</code> field of selected
     * document in the <code>ApplicationConstants.COURSE_MASTER_TABLE</code>
     *
     * @param primaryKey <code>_id</code> value of selected document in
     * hexadecimal String
     * @param loginUserId <code>_id</code> value of <code>User</code> Object in
     * hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if deletion is
     * successful.
     *
     * @throws Exception if either of argument is null or empty
     */
    public String deleteCourseMaster(String loginUserId, String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty() || loginUserId == null || loginUserId.isEmpty()) {
            return null;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        String courseStr = DBManager.getDbConnection().fetch(ApplicationConstants.COURSE_MASTER_TABLE, primaryKey);

        if (courseStr == null) {
            return null;
        }

        List<Course> dbList = new Gson().fromJson(courseStr, new TypeToken<List<Course>>() {
        }.getType());

        Course courseM = dbList.get(0);

        courseM.setStatus(ApplicationConstants.DELETE);
        courseM.setUpdateDate(System.currentTimeMillis() + "");
        courseM.setUpdatedBy(userName);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.COURSE_MASTER_TABLE, primaryKey, new Gson().toJson(courseM));
        String result = "";

        if (status) {
            result = ApplicationConstants.SUCCESS;
        }

        return result;
    }

//    To get location name from location id.
    
    public static List<Course> getLocationName(List<Course> instituteList) throws Exception {

        Map<String, String> locationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.COURSE_MASTER_TABLE);
        List<Location> locationList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = locationList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();

            locationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < instituteList.size(); i++) {
            String locationId = instituteList.get(i).getLocation();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(locationId)) {
                    instituteList.get(i).setLocation(value);
                }
            }
        }
        return instituteList;
    }
    //    To get Ddo name from ddo  id.
    public static List<Course> getDDOName(List<Course> universityList) throws Exception {

        Map<String, String> locationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> ddoList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = ddoList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();

            locationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < universityList.size(); i++) {
            String ddoId = universityList.get(i).getDdo();
            for (Map.Entry<String, String> entry : locationMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(ddoId)) {
                    universityList.get(i).setDdo(value);
                }
            }
        }

        return universityList;
    }

    public String fetchAllCourseMaster(String ddo, String location) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();

//        conditionMap.put("ddo", ddo);
//        conditionMap.put("location", location);

        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COURSE_MASTER_TABLE, conditionMap);
        List<Course> courseList = new Gson().fromJson(result, new TypeToken<List<Course>>() {
        }.getType());
        if (courseList == null || courseList.size() < 1) {
            return null;
        }
        courseList = getLocationName(courseList);
        courseList = getDDOName(courseList);

        return new Gson().toJson(courseList);
    }

}
//    public static void main(String[] erargs) throws Exception {
//        Course course = new Course();
//        
//        course.setCourseCode("some degree");
//        course.setCourseName("some name");
//        course.setLocation("institution name");
//        course.setIsMidExamApplicable("less");
//        course.setDdo("location");
//        
//        String result = new CourseManager().updateCourseMaster(course,"587de3d5a3ad201b1bd9af18", "58fdb112a3fb831598b4a9ae");
//        System.out.println("the result is :" + result);
//    }
