/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.Class;
import com.accure.leave.dto.EmployeeLeaveAssignment;
import com.accure.leave.dto.LeaveTypeDetails;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public class LeaveTypeManager {

    public String save(LeaveTypeMaster leaveType, String loginUserId) throws Exception {

        String result = "";
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("leaveType", leaveType.getLeaveType());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        map1.put("shortDescription", leaveType.getShortDescription());
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (leaveType == null || loginUserId == null) {
            result = null;
        } else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LEAVE_TYPE_MASTER, map)) || (Duplicate.hasDuplicateforSave(ApplicationConstants.LEAVE_TYPE_MASTER, map1))) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            leaveType.setCreateDate(System.currentTimeMillis() + "");
            leaveType.setUpdateDate(System.currentTimeMillis() + "");
            leaveType.setStatus(ApplicationConstants.ACTIVE);
            leaveType.setCreatedBy(userName);
            String leaveTypeJson = new Gson().toJson(leaveType);
            String Id = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_TYPE_MASTER, leaveTypeJson);
            if (Id != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String fetchAllCashable(String empCode) throws Exception {
        List<LeaveTypeMaster> list1 = new ArrayList<LeaveTypeMaster>();
        HashMap condMap = new HashMap();
        HashMap condMap1 = new HashMap();
        condMap.put("employeeCode", empCode);
        com.accure.leave.dto.FinancialYear financialYear = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String activeFinancialyearId = ((LinkedTreeMap) financialYear.getId()).get("$oid") + "";
        condMap.put("year", activeFinancialyearId);
        condMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, condMap);
        List<EmployeeLeaveAssignment> leaveAssignmentListStr = new Gson().fromJson(result, new TypeToken<List<EmployeeLeaveAssignment>>() {
        }.getType());
        for (EmployeeLeaveAssignment empleaveAssignment : leaveAssignmentListStr) {
            String leaveType = empleaveAssignment.getLeaveType();
            String natureType = empleaveAssignment.getNatureType();
//        HashMap<String, String> conditionMap = new HashMap<String, String>();
//        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TYPE_MASTER, leaveType);
            List<LeaveTypeMaster> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTypeMaster>>() {
            }.getType());
            LeaveTypeMaster LeaveType = list.get(0);
//        for(LeaveTypeMaster LeaveType:list)
//        {
            List<LeaveTypeDetails> leaveTypeDetails = LeaveType.getLeaveTypeDetails();
            for (LeaveTypeDetails leaveTypeDetail : leaveTypeDetails) {
                String nature = leaveTypeDetail.getNatureType();
                if (natureType.equals(nature)) {
                    String isCashable = leaveTypeDetail.getCashable();
                    if (isCashable.equals("Yes")) {
                        list1.add(LeaveType);
                        condMap1.put(leaveType, LeaveType);
                        break;
                    }
                }
            }
        }
        list1.clear();
        list1.addAll(condMap1.values());
        if (list1.size() != 0) {
            list1 = getNatureType(list1);
            list1 = getEmployeeCategory(list1);
        }
        return new Gson().toJson(list1);
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TYPE_MASTER, Id);
        List<LeaveTypeMaster> leaveTypeList = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        if (leaveTypeList == null || leaveTypeList.size() < 1) {
            return null;
        }
        leaveTypeList = getNatureType(leaveTypeList);
        leaveTypeList = getEmployeeCategory(leaveTypeList);
        return new Gson().toJson(leaveTypeList.get(0));
    }

    public String fetchId(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TYPE_MASTER, Id);
        List<LeaveTypeMaster> leaveTypeList = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        if (leaveTypeList == null || leaveTypeList.size() < 1) {
            return null;
        }
        // leaveTypeList = getNatureType(leaveTypeList);
        //leaveTypeList = getEmployeeCategory(leaveTypeList);
        return new Gson().toJson(leaveTypeList.get(0));
    }

    public String fetchById(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TYPE_MASTER, Id);
        List<LeaveTypeMaster> leaveTypeList = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        if (leaveTypeList == null || leaveTypeList.size() < 1) {
            return null;
        }
        return new Gson().toJson(leaveTypeList.get(0));
    }

    public String delete(String Id, String loginUserId) throws Exception {
        String result = "";
        if (Id == null || Id.isEmpty()) {
            return null;
        } else if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, "leaveType", Id))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();

            Type type = new TypeToken<LeaveTypeMaster>() {
            }.getType();
            String leaveType = new LeaveTypeManager().fetch(Id);
            if (leaveType == null || leaveType.isEmpty()) {
                return null;
            }
            LeaveTypeMaster leaveTyperJson = new Gson().fromJson(leaveType, type);
            leaveTyperJson.setStatus(ApplicationConstants.DELETE);
            leaveTyperJson.setUpdatedBy(userName);

            boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TYPE_MASTER, Id, new Gson().toJson(leaveTyperJson));

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.SUCCESS;
            }
        }

        return result;
    }

    public String update(LeaveTypeMaster leaveType, String Id, String loginUserId) throws Exception {
        String result;
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("leaveType", leaveType.getLeaveType());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        map1.put("shortDescription", leaveType.getShortDescription());
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String dbStr = new LeaveTypeManager().fetch(Id);
        if (Id == null || dbStr.isEmpty() || loginUserId == null) {
            return null;
        } else if ((Duplicate.isDuplicateforUpdate(ApplicationConstants.LEAVE_TYPE_MASTER, map, Id)) || (Duplicate.isDuplicateforUpdate(ApplicationConstants.LEAVE_TYPE_MASTER, map1, Id))) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            Type type = new TypeToken<LeaveTypeMaster>() {
            }.getType();
            LeaveTypeMaster dbStrObj = new Gson().fromJson(dbStr, type);
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            leaveType.setCreateDate(dbStrObj.getCreateDate());
            leaveType.setCreatedBy(dbStrObj.getCreatedBy());
            leaveType.setUpdateDate(System.currentTimeMillis() + "");
            leaveType.setStatus(ApplicationConstants.ACTIVE);
            leaveType.setUpdatedBy(userName);
            String leaveTyperJson = new Gson().toJson(leaveType);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_TYPE_MASTER, Id, leaveTyperJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TYPE_MASTER, conditionMap);
        List<LeaveTypeMaster> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        //list = getNatureType(list);
        // list = getEmployeeCategory(list);
        return new Gson().toJson(list);
    }

    public String fetchAllCashable() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        List<LeaveTypeMaster> list1 = new ArrayList<LeaveTypeMaster>();
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TYPE_MASTER, conditionMap);
        List<LeaveTypeMaster> list = new Gson().fromJson(result1, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        for (LeaveTypeMaster LeaveType : list) {
            List<LeaveTypeDetails> leaveTypeDetails = LeaveType.getLeaveTypeDetails();
            for (LeaveTypeDetails leaveTypeDetail : leaveTypeDetails) {
                String isCashable = leaveTypeDetail.getCashable();
                if (isCashable.equals("Yes")) {
                    list1.add(LeaveType);
                    break;
                }
            }
        }
        list1 = getNatureType(list1);
        list1 = getEmployeeCategory(list1);
        return new Gson().toJson(list1);
    }

    public String getLeaveTypes(String empCategory, String empNature) throws Exception {
        String leaveType = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_TYPE_MASTER + "`";
        String Query = "select lt._id as idStr,lt.leaveType from " + leaveType + ""
                + " as lt where (lt.leaveTypeDetails.natureType= \"" + empNature + "\" ) and (lt.leaveTypeDetails.employeeCategory= \"" + empCategory + "\") and lt.status=\"Active\"";
        RestClient aql = new RestClient();
        String fOutput = aql.getRestData(ApplicationConstants.END_POINT, Query);
        return fOutput;

    }

    public String getLeaveTypes(String empCategory, String empNature, String gender) throws Exception {
        String leaveType = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_TYPE_MASTER + "`";
        String Query;
        if (gender.equalsIgnoreCase(ApplicationConstants.GENDER_BOTH)) {
            Query = "select lt._id as idStr,lt.leaveType from " + leaveType + ""
                    + " as lt where (lt.leaveTypeDetails.natureType= \"" + empNature + "\" ) and (lt.leaveTypeDetails.employeeCategory= \"" + empCategory + "\") and lt.status=\"Active\"";
        } else {
            Query = "select lt._id as idStr,lt.leaveType from " + leaveType + ""
                    + " as lt where (lt.leaveTypeDetails.natureType= \"" + empNature + "\" ) and (lt.leaveTypeDetails.employeeCategory= \"" + empCategory + "\") and lt.status=\"Active\" and (lt.gender=\"" + gender + "\" or lt.gender=\"" + ApplicationConstants.GENDER_BOTH + "\")";
        }
        RestClient aql = new RestClient();
        String fOutput = aql.getRestData(ApplicationConstants.END_POINT, Query);
        return fOutput;

    }

    private List<LeaveTypeMaster> getNatureType(List<LeaveTypeMaster> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.NATURE_TABLE);
        List<Nature> natureList = new Gson().fromJson(result, new TypeToken<List<Nature>>() {
        }.getType());
        for (Iterator<Nature> iterator = natureList.iterator(); iterator.hasNext();) {
            Nature next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getNatureName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                List<LeaveTypeDetails> detailsList = list.get(i).getLeaveTypeDetails();
                for (Iterator<LeaveTypeDetails> iterator = detailsList.iterator(); iterator.hasNext();) {
                    LeaveTypeDetails next = iterator.next();
                    if (entry.getKey().equals(next.getNatureType())) {
                        next.setNatureType((entry.getValue()));
                    }
                }
            }
        }
        return list;
    }

    private List<LeaveTypeMaster> getEmployeeCategory(List<LeaveTypeMaster> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CLASS_TABLE);
        List<Class> natureList = new Gson().fromJson(result, new TypeToken<List<Class>>() {
        }.getType());
        for (Iterator<Class> iterator = natureList.iterator(); iterator.hasNext();) {
            Class next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                List<LeaveTypeDetails> detailsList = list.get(i).getLeaveTypeDetails();
                for (Iterator<LeaveTypeDetails> iterator = detailsList.iterator(); iterator.hasNext();) {
                    LeaveTypeDetails next = iterator.next();
                    if (entry.getKey().equals(next.getEmployeeCategory())) {
                        next.setEmployeeCategory((entry.getValue()));
                        next.setEmployeeCategoryName((entry.getKey()));
                    }
                }
            }
        }
        return list;
    }

    public List<Document> getLeaveType(List<HashMap> filterList) throws Exception {

        Document filterDocument = Common.getFilterDocumentForFilterList(filterList);
        FindIterable<Document> iterable = Common.getCollection(ApplicationConstants.LEAVE_TYPE_MASTER).
                find(filterDocument);
        List<Document> resultList = new ArrayList();
        if (iterable != null) {
            MongoCursor<Document> cursor = iterable.iterator();
            if (cursor != null) {
                while (cursor.hasNext()) {
                    Document documentResult = cursor.next();
                    documentResult.put("_id", ((ObjectId) documentResult.get("_id")).toString());
                    resultList.add(documentResult);
                }
            }
        }

        return resultList;
    }

    public LeaveTypeMaster fetchObject(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TYPE_MASTER, Id);
        List<LeaveTypeMaster> leaveTypeList = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        if (leaveTypeList == null || leaveTypeList.size() < 1) {
            return null;
        }
        leaveTypeList = getNatureType(leaveTypeList);
        leaveTypeList = getEmployeeCategory(leaveTypeList);
        return leaveTypeList.get(0);
    }

    public LeaveTypeMaster fetchRawObject(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_TYPE_MASTER, Id);
        List<LeaveTypeMaster> leaveTypeList = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        if (leaveTypeList == null || leaveTypeList.size() < 1) {
            return null;
        }
        return leaveTypeList.get(0);
    }

    //method used only for leave carry forward
  public String getLeaveTypeForLeaveCarryForward(String natureType, String employeeCategory) {
        String leaveType = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_TYPE_MASTER + "`";
        String Query = "select lt._id as idStr,lt.leaveType ,leaveTypeDetails from " + leaveType + ""
                + " as lt where (lt.leaveTypeDetails.natureType= \"" + natureType + "\" ) and (lt.leaveTypeDetails.employeeCategory= \"" + employeeCategory + "\") and lt.status=\"Active\" and (lt.leaveTypeDetails.carryForward=\"Yes\")";
        RestClient aql = new RestClient();
        String fOutput = aql.getRestData(ApplicationConstants.END_POINT, Query);
        List<LeaveTypeMaster> leaveTypeList = new Gson().fromJson(fOutput, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        System.out.println(leaveTypeList.size());
        List<LeaveTypeMaster> leaveTypeListFinal = new ArrayList<LeaveTypeMaster>();
        HashMap<String, LeaveTypeMaster> resultmap = new HashMap<String, LeaveTypeMaster>();
        if (leaveTypeList != null && leaveTypeList.size() > 0) {
            for (Iterator<LeaveTypeMaster> it = leaveTypeList.iterator(); it.hasNext();) {
                LeaveTypeMaster leaveTypeMaster = it.next();
                boolean flag = false;
                List<LeaveTypeDetails> leavetypelistList = leaveTypeMaster.getLeaveTypeDetails();
                if (leavetypelistList != null && leavetypelistList.size() > 0) {
                    for (Iterator<LeaveTypeDetails> it1 = leavetypelistList.iterator(); it1.hasNext();) {
                        LeaveTypeDetails leaveTypeDetails = it1.next();
                        if (leaveTypeDetails.getNatureType().equalsIgnoreCase(natureType) && leaveTypeDetails.getEmployeeCategory().equalsIgnoreCase(employeeCategory) && leaveTypeDetails.getCarryForward().equalsIgnoreCase(ApplicationConstants.YES)) {
                            resultmap.put(leaveTypeMaster.getIdStr(), leaveTypeMaster);
                        }
                    }
                }
            }
        }
        return new Gson().toJson(resultmap.values());
    }

    public boolean checkDuplicateForLeaveTypeDetails(String employeeCategory, String natureType) {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("leaveTypeDetails.natureType", natureType);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        map1.put("leaveTypeDetails.employeeCategory", employeeCategory);
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (employeeCategory != null && natureType != null) {
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.LEAVE_TYPE_MASTER, map) || Duplicate.hasDuplicateforSave(ApplicationConstants.LEAVE_TYPE_MASTER, map1)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDeleteMapForLeaveTypeDetails(String employeeCategory, String natureType, String id) {
        if (id != null && employeeCategory != null && natureType != null) {
            if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, "leaveType", id))
                    && (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, "natureType", natureType)) && (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, "employeeCategory", employeeCategory))) {
                return true;
            }
        }
        return false;
    }

    public LeaveTypeMaster fetchLWPId() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("shortDescription", "LWP");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String getdata = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TYPE_MASTER, conditionMap);
        List<LeaveTypeMaster> gisList = new Gson().fromJson(getdata, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return gisList.get(0);
    }

    public static void main(String... args) {
        System.out.println(new LeaveTypeManager().getLeaveTypeForLeaveCarryForward("582f31590c92ec57796a1dba", "583f02b20c92ec40ee5eb33e"));
    }

}
