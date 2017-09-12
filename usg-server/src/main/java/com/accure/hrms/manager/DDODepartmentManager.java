/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.DdoDepartmentMap;
import com.accure.hrms.dto.Department;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author accure
 */
public class DDODepartmentManager {

    /**
     * Inserts document to
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code> collection.If
     * document list already contains  <code>ddo</code> field value insertion
     * will not happen
     *
     * @param ddoDept <code>DdoDepartmentMap</code> object data.
     * @param userid <code>_id</code> value of document (in hexadecimal string)
     * in the <code>ApplicationConstants.USER</code> collection.
     * @return <code>ApplicationConstants.SUCCESS</code> if insertion is
     * successful.
     * @throws Exception if second argument is <code>null</code>..
     */
    public String save(DdoDepartmentMap ddoDept, String userid) throws Exception {
        String result;
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddoDept.getDdo());
        if (userid == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.DDO_DEPARTMENT_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + " " + user.getLname();
            ddoDept.setCreateDate(System.currentTimeMillis() + "");
            ddoDept.setUpdateDate(System.currentTimeMillis() + "");
            ddoDept.setStatus(ApplicationConstants.ACTIVE);
            ddoDept.setCreatedBy(userName);
            String jsonStr = new Gson().toJson(ddoDept);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.DDO_DEPARTMENT_TABLE, jsonStr);
            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;

    }

    /**
     * Displays all documents in the
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code> collection.Only
     * those documents, whose <code>ApplicationConstants.STATUS</code> field
     * value is <code>ApplicationConstants.ACTIVE</code> and <code>ddo</code>
     * field value is same as method argument value are appear in the list.
     *
     * @param ddo <code>_id</code> value of document in the
     * <code>ApplicationConstants.DDO_TABLE</code> collection
     * @return list of documents in JSON String
     * @throws Exception if argument is <code>null</code>..
     */
    public String viewAll(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_DEPARTMENT_TABLE, conditionMap);
        if (result == null) {
            return null;
        }
        List<DdoDepartmentMap> list = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
        }.getType());
        for (DdoDepartmentMap ddm : list) {
            if ((ddm.getDdo() != null)) {

                String ddoStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddm.getDdo());
                if (ddoStr != null) {
                    List<DDO> ddoList = new Gson().fromJson(ddoStr, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO ddoObj = ddoList.get(0);
                    ddm.setDdo(ddoObj.getDdoName());
                }

            }
            if ((ddm.getDepartmentList() != null)) {
                List<String> departments = ddm.getDepartmentList();
                for (int dept = 0; dept < departments.size(); dept++) {
                    String next = departments.get(dept);

                    String deptStr = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, next);
                    if (deptStr != null) {
                        List<Department> deptList = new Gson().fromJson(deptStr, new TypeToken<List<Department>>() {
                        }.getType());
                        Department deptObj = deptList.get(0);
                        next = deptObj.getDepartment();
                        departments.set(dept, next);
                    }

                }

            }
        }
        return new Gson()
                .toJson(list);

    }

    /**
     * Displays all documents in the
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code> collection.Only
     * those documents, whose <code>ApplicationConstants.STATUS</code> field
     * value is <code>ApplicationConstants.ACTIVE</code> are appear in the list.
     *
     * @return list of documents in JSON String
     * @throws Exception if argument is <code>null</code>..
     */
    public String fetch() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_DEPARTMENT_TABLE, conditionMap);
        List<DdoDepartmentMap> list = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
        }.getType());
        for (DdoDepartmentMap ddoDeptMap : list) {
            if ((ddoDeptMap.getDdo() != null)) {

                String ddoStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddoDeptMap.getDdo());
                if (ddoStr != null) {
                    List<DDO> ddoList = new Gson().fromJson(ddoStr, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO ddo = ddoList.get(0);
                    ddoDeptMap.setDdo(ddo.getDdoName());
                }

            }
            if ((ddoDeptMap.getDepartmentList() != null)) {

                List<String> deptList = ddoDeptMap.getDepartmentList();
                for (int dept = 0; dept < deptList.size(); dept++) {
                    String next = deptList.get(dept);

                    String deptStr = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, next);
                    if (deptStr != null) {
                        List<Department> deptlist = new Gson().fromJson(deptStr, new TypeToken<List<Department>>() {
                        }.getType());
                        Department deptObj = deptlist.get(0);
                        next = deptObj.getDepartment();
                        deptList.set(dept, next);
                    }

                }

            }
        }
        return new Gson()
                .toJson(list);
    }

    /**
     * Updates status field of selected document in the
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code> collection.If
     * <code>ddo</code> field value and <code>department</code> field value of
     * document is saved in <code>ddo</code> and <code>department</code> field
     * values in <code>ApplicationConstants.EMPLOYEE_TABLE</code> collection,
     * updation will not happen
     *
     * @param id <code>_id</code> value of document in hexadecimal string.
     * @param userid <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection.
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if either of argument is <code>null</code>..
     */
    public String delete(String id, String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();

        String ddoJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_DEPARTMENT_TABLE, id);
        List<DdoDepartmentMap> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DdoDepartmentMap>>() {
        }.getType());
        DdoDepartmentMap ddo = ddoList.get(0);

        ddo.setStatus(ApplicationConstants.DELETE);
        ddo.setUpdateDate(System.currentTimeMillis() + "");
        ddo.setUpdatedBy(userName);

        String status = "";
        List<String> list = ddo.getDepartmentList();

        for (int dept = 0; dept < list.size(); dept++) {
            String next = list.get(dept);
            if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "ddo", ddo.getDdo())) && (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "department", next))) {
                status = ApplicationConstants.DELETE_MESSAGE;
                break;
            } else {
                boolean result = DBManager.getDbConnection().update(ApplicationConstants.DDO_DEPARTMENT_TABLE, id, new Gson().toJson(ddo));
                if (result) {
                    status = ApplicationConstants.SUCCESS;
                } else {
                    status = ApplicationConstants.FAIL;
                }
            }
        }
        return status;

    }

    /**
     * Updates selected document in the
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code> collection.If
     * document list already contains <code>ddo</code> field value ,updation
     * will not happen
     *
     * @param dept <code>DdoDepartmentMap</code> object data
     * @param id <code>_id</code> value of selected document in hexadecimal
     * string.
     * @param userid <code>_id</code> value of document in the
     * <code>ApplicationConstants.USER</code> collection.
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception if second or third argument is <code>null</code>..
     */
    public String update(DdoDepartmentMap dept, String id, String userid) throws Exception {

        String result;

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("ddo", dept.getDdo());

        if (id == null || userid == null) {
            result = null;
        } else if (Duplicate.isDuplicateforUpdate(ApplicationConstants.DDO_DEPARTMENT_TABLE, hMap, id)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + " " + user.getLname();
            String jsonStr = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_DEPARTMENT_TABLE, id);

            List<DdoDepartmentMap> list = new Gson().fromJson(jsonStr, new TypeToken<List<DdoDepartmentMap>>() {
            }.getType());
            DdoDepartmentMap dbObj = list.get(0);
            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setUpdatedBy(userName);
            dbObj.setDdo(dept.getDdo());
            dbObj.setDepartmentList(dept.getDepartmentList());
            dbObj.setStatus(ApplicationConstants.ACTIVE);
            String json = new Gson().toJson(dbObj);
            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.DDO_DEPARTMENT_TABLE, id, json);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    /**
     * Searches particular document in the
     * <code>ApplicationConstants.DDO_DEPARTMENT_TABLE</code> collection using
     * <code>_id</code> value
     *
     * @param id <code>_id<code> value of document in hexadecimal String
     * @return search document in JSON string format.
     * @throws Exception if argument is null..
     */
    String fetchById(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_DEPARTMENT_TABLE, id);
        List<DdoDepartmentMap> ddoList = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
        }.getType());
        if (ddoList == null || ddoList.size() < 1) {
            return null;
        }
        return new Gson().toJson(ddoList.get(0));
    }

    public String search(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_DEPARTMENT_TABLE, conditionMap);

        List<DdoDepartmentMap> ddodeptlist = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
        }.getType());
        List<Department> deptList = new ArrayList<Department>();

        for (DdoDepartmentMap dfm : ddodeptlist) {
            List<String> deptLit = dfm.getDepartmentList();
            for (int i = 0; i < deptLit.size(); i++) {
                try {
                    String gaJson5 = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, deptLit.get(i));

                    List<Department> designa = new Gson().fromJson(gaJson5, new TypeToken<List<Department>>() {
                    }.getType());
                    deptList.add(designa.get(0));

                } catch (Exception e) {
                }
            }
        }
        HashMap<String, String> resultList = new HashMap<String, String>();
        resultList.put("deptList", new Gson().toJson(deptList));

        return new Gson().toJson(resultList);
    }

    public String fetchAll() throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.DDO_DEPARTMENT_TABLE);

        DBObject groupFields = new BasicDBObject("_id", "$ddo");

        DBObject group = new BasicDBObject("$group", groupFields);

        AggregationOutput output = collection.aggregate(group);
        List<DdoDepartmentMap> ddoDepartmentList = new ArrayList<DdoDepartmentMap>();
        for (DBObject doc : output.results()) {
            DdoDepartmentMap ddmdata = new DdoDepartmentMap();
            HashMap<String, String> saveconditionMap = new HashMap<String, String>();
            saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            saveconditionMap.put("ddo", doc.get("_id").toString());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_DEPARTMENT_TABLE, saveconditionMap);
            ddmdata.setDdo(doc.get("_id").toString());

            List<DdoDepartmentMap> ddoDeptMapList = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
            }.getType());
            List<String> deaprt;
            for (DdoDepartmentMap li : ddoDeptMapList) {
                deaprt = li.getDepartmentList();
            }
            ddoDepartmentList.add(ddmdata);
        }
        return new Gson().toJson(ddoDepartmentList);
    }

}
