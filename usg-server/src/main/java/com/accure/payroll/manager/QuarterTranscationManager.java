/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Quarter;
import com.accure.hrms.dto.QuarterCategory;
import com.accure.payroll.dto.QuaterTransaction;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class QuarterTranscationManager {

    /**
     * This is to insert document into
     * <code>ApplicationConstants.QUARTER_TRANSACTION_TABLE</code> collection.If
     * document list already contains combination of
     * <code>ddo</code>,<code>quarterNumber</code> and
     * <code>quarterCategory</code> insertion will not happen.
     *
     * @param qt is <code>QuaterTransaction</code> Object data
     * @param userId <code>_id</code> value of <code>User</code> object in
     * hexadecimal String.
     * @return <code>_id</code> value of <code>QuaterTransaction</code> object
     * if insertion is successful
     * @throws Exception if second argument is <code>null</code>...
     */
    public String saveQuaterTransactionMaster(QuaterTransaction qt, String userId) throws Exception {
        String result;
        if (qt == null) {
            return null;
        } else if (quarterNumberValidation(qt) != 0) {
            result = ApplicationConstants.VALIDATE_QUARTER_NUMBER;
        } else if (validateDate(qt)) {
            result = ApplicationConstants.VALIDATE_DATES;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            qt.setStatus(ApplicationConstants.ACTIVE);
            qt.setCreateDate(System.currentTimeMillis() + "");
            qt.setUpdateDate(System.currentTimeMillis() + "");
            qt.setCreatedBy(userName);
            qt.setUpdatedBy(userName);
            String json = new Gson().toJson(qt);
            result = DBManager.getDbConnection().insert(ApplicationConstants.QUARTER_TRANSACTION_TABLE, json);

        }
        return result;
    }

    /**
     * This is to get list of document in the
     * <code>ApplicationConstants.QUARTER_TRANSACTION_TABLE</code> table.only
     * those documents which are in selected <code>ddo</code> field of document
     * will display in the list
     *
     * @param ddo <code>_id</code> value of <code>DDO</code> object in
     * hexadecimal string.
     * @return list of <code>QuaterTransaction</code> in JSON String format.
     * @throws Exception if argument is <code>null</code>...
     */
    public String viewQuaterTranscationMaster(String ddo) throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put("ddo", ddo);
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TRANSACTION_TABLE, hMap);

        if (result == null) {
            return null;
        }
        List<QuaterTransaction> list = new Gson().fromJson(result, new TypeToken<List<QuaterTransaction>>() {
        }.getType());

        list = getDDO(list);

        list = getQuarterNo(list);

        String finalresult = new Gson().toJson(list);
        return finalresult;
    }

    /**
     *
     * @param quaterList
     * @return
     * @throws Exception
     */
    private List<QuaterTransaction> getQuarterNo(List<QuaterTransaction> quaterList) throws Exception {
        Map<String, String> QuarterMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.QUARTER_TABLE);
        List<Quarter> qList = new Gson().fromJson(result, new TypeToken<List<Quarter>>() {
        }.getType());
        for (Iterator<Quarter> iterator = qList.iterator(); iterator.hasNext();) {
            Quarter next = iterator.next();
            QuarterMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getQuarterNo());
        }
        for (int i = 0; i < quaterList.size(); i++) {
            for (Map.Entry<String, String> entry : QuarterMap.entrySet()) {
                if (entry.getKey().equals(quaterList.get(i).getQuaterNumber())) {
                    quaterList.get(i).setQuaterNumber(entry.getValue());
                }
            }
        }
        return quaterList;
    }

    /**
     *
     * @param quaterList
     * @return
     * @throws Exception
     */
    public static List<QuaterTransaction> getDDO(List<QuaterTransaction> quaterList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> ddoList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = ddoList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < quaterList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(quaterList.get(i).getDdo())) {
                    quaterList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return quaterList;
    }

    /**
     * Updates selected document in
     * <code>ApplicationConstants.QUARTER_TRANSACTION_TABLE</code> collection.
     *
     * @param qt <code>QuaterTransaction</code> object data
     * @param id <code>_id</code> value of <code>QuaterTransaction</code> object
     * in hexadecimal String
     * @param userId <code>_id</code> value of <code>User</code> object in
     * hexadecimal String
     * @return <code>ApplicationConstants.SUCCESS</code> if updation is
     * successful
     * @throws Exception
     */
    public String updateQuarterTransactionMaster(QuaterTransaction qt, String id, String userId) throws Exception {

        String result;
        if (qt == null) {
            return null;
        } else if (quarterNumberValidation(qt) != 1) {
            result = ApplicationConstants.VALIDATE_QUARTER_NUMBER;
        } else if (validateDate(qt)) {
            result = ApplicationConstants.VALIDATE_DATES;
        } else {
            String existJson = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_TRANSACTION_TABLE, id);
            List<QuaterTransaction> list = new Gson().fromJson(existJson, new TypeToken<List<QuaterTransaction>>() {
            }.getType());
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            QuaterTransaction dbObj = list.get(0);
            dbObj.setAllowcationDate(qt.getAllowcationDate());
            dbObj.setEmployeeCode(qt.getEmployeeCode());
            dbObj.setEmployeeName(qt.getEmployeeName());
            dbObj.setCity(qt.getCity());
            dbObj.setDdo(qt.getDdo());
            dbObj.setDepartment(qt.getDepartment());
            dbObj.setDesignation(qt.getDesignation());
            dbObj.setLeaveDate(qt.getLeaveDate());
            dbObj.setQuaterCategory(qt.getQuaterCategory());
            dbObj.setQuaterNumber(qt.getQuaterNumber());
            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setUpdatedBy(userName);
            dbObj.setStatus(ApplicationConstants.ACTIVE);
            String jsonStr = new Gson().toJson(dbObj);
            boolean res = DBManager.getDbConnection().update(ApplicationConstants.QUARTER_TRANSACTION_TABLE, id, jsonStr);
            if (res) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;

    }

    /**
     * Updates <code>status</code> field of selected document in
     * <code>ApplicationConstants.QUARTER_TRANSACTION_TABLE</code> collection
     *
     * @param id primary key value of a document in
     * <code>ApplicationConstants.QUARTER_TRANSACTION_TABLE</code> collection.
     * @param userId primary key value of document in
     * <code>ApplicationConstants.USER_TABLE</code> collection
     * @return <code>True</code> if updation is successful
     * @throws Exception
     */
    public boolean deleteQuaterTransactionMaster(String id, String userId) throws Exception {

        String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_TRANSACTION_TABLE, id);
        List<QuaterTransaction> list = new Gson().fromJson(existCategoryJson, new TypeToken<List<QuaterTransaction>>() {
        }.getType());
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        QuaterTransaction qt = list.get(0);
        qt.setStatus(ApplicationConstants.DELETE);
        qt.setUpdateDate(System.currentTimeMillis() + "");
        qt.setUpdatedBy(userName);
        String jsonStr = new Gson().toJson(qt);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.QUARTER_TRANSACTION_TABLE, id, jsonStr);
        return status;

    }

    /**
     * This is to fetch the list of documents from
     * <code>ApplicationConstants.EMPLOYEE_TABLE</code> collection .Only those
     * documents whose <code>stopSalary</code> field is <code>No</code> and
     * <code>EmployeeLeftStatus</code> field is <code>No</code> are appear in
     * the list
     *
     * @return List of documents in
     * <code>ApplicationConstants.EMPLOYEE_TABLE</code> collection in JSON
     * String format
     * @throws Exception
     */
    public String fetchQuarterEmployee() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("stopSalary", "No");
        conditionMap.put("EmployeeLeftStatus", "NO");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        return result;
    }

    /**
     * @param city primary key value of a document in the
     * <code>ApplicationConstants.CITY_TABLE</code> collection
     * @return list of documents in JSON string.
     * <code>ApplicationConstants.QUARTER_TABLE</code> collection in JSON string
     * format.
     * @throws Exception if argument is <code>null</code>...
     */
    public String fetchQuarterCategory(String city) throws Exception {

        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.QUARTER_TABLE);
        BasicDBObject dbobj = new BasicDBObject();
        dbobj.append(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        dbobj.append("city", city);
        List<String> catList = collection.distinct("category", dbobj);

        List<QuarterCategory> qclist = new ArrayList<QuarterCategory>();
        if (catList != null) {
            for (String li : catList) {
                String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_CATEGORY_TABLE, li);
                if (existCategoryJson != null) {
                    List<QuarterCategory> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<QuarterCategory>>() {
                    }.getType());
                    QuarterCategory qc = categorylist.get(0);
                    qclist.add(qc);
                }
            }
        }

        return new Gson().toJson(qclist);
    }

    /**
     *
     * @param city
     * @param category
     * @return
     * @throws Exception
     */
    public String fetchQuarterNumber(String city, String category) throws Exception {
        if (city == null || category == null) {
            return null;
        }
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("city", city);
        conditionMap.put("category", category);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TABLE, conditionMap);
        return result;
    }

    /**
     *
     * @param qt
     * @return
     * @throws Exception
     */
    public boolean validateDate(QuaterTransaction qt) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
        String allDate = qt.getAllowcationDate();
        String leaveDate = "";
        if (!qt.getLeaveDate().equals("") || qt.getLeaveDate() != null || !qt.getLeaveDate().isEmpty()) {
            leaveDate = qt.getLeaveDate();
        }
        boolean result = false;

        if (!leaveDate.equals("")) {
            Date alldate = formatter.parse(allDate);
            Date lDate = formatter.parse(leaveDate);
            if (alldate.compareTo(lDate) > 0) {
                result = true;
            }
        }

        return result;
    }

    /**
     *
     * @param qt
     * @return
     * @throws Exception
     */
    public int quarterNumberValidation(QuaterTransaction qt) throws Exception {

        int count = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");

        String uiallDate = qt.getAllowcationDate();
        Date alldateui = formatter.parse(uiallDate);

        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        hMap.put("ddo", qt.getDdo());
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TRANSACTION_TABLE, hMap);

        if (result != null) {
            List<QuaterTransaction> categorylist = new Gson().fromJson(result, new TypeToken<List<QuaterTransaction>>() {
            }.getType());
            for (QuaterTransaction emp : categorylist) {
                String dballDate = emp.getAllowcationDate();
                String dbleaveDate = emp.getLeaveDate();

                if ((emp.getDdo().equalsIgnoreCase(qt.getDdo())) && (emp.getCity().equalsIgnoreCase(qt.getCity()))
                        && (emp.getQuaterCategory().equalsIgnoreCase(qt.getQuaterCategory()))
                        && (emp.getQuaterNumber().equalsIgnoreCase(qt.getQuaterNumber()))
                        && ((emp.getAllowcationDate().equals(qt.getAllowcationDate())) || (emp.getLeaveDate().equals("")))) {
                    count = count + 1;

                }

                if (!dbleaveDate.equals("")) {
                    Date alldatedb = formatter.parse(dballDate);
                    Date lDatedb = formatter.parse(dbleaveDate);
                    if ((emp.getDdo().equalsIgnoreCase(qt.getDdo())) && (emp.getCity().equalsIgnoreCase(qt.getCity()))
                            && (emp.getQuaterCategory().equalsIgnoreCase(qt.getQuaterCategory()))
                            && (emp.getQuaterNumber().equalsIgnoreCase(qt.getQuaterNumber()))
                            && ((!alldatedb.equals(alldateui)) && ((alldateui.compareTo(lDatedb)) > 0))) {
                        count = count + 1;
                    }
                }

            }
        }
        return count;
    }

}
