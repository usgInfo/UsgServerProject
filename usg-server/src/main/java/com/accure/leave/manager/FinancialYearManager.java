/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import com.accure.leave.dto.FinancialYear;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class FinancialYearManager {

    public String Save(FinancialYear financial, String userId) throws Exception {
        String result = "";
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("year", financial.getYear());
        saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (financial == null || userId == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, saveconditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userId);
            String fName = user.getFname() + " " + user.getLname();
            financial.setStatus(ApplicationConstants.ACTIVE);
            financial.setIsActive("No");
            financial.setCreateDate(System.currentTimeMillis() + "");
            financial.setUpdateDate(System.currentTimeMillis() + "");
            financial.setCreatedBy(fName);
            String financialYearJson = new Gson().toJson(financial);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, financialYearJson);
            if (fResult != null) {
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
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, conditionMap);
        return result;
    }

    public String fetchAllForDropDowns() throws Exception {

        String leaveFinancialYearTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE + "`";
        String leaveFinancialYearQuery = "select fy._id as idStr,fy.year from " + leaveFinancialYearTable + ""
                   + " as fy where  fy.status=\"Active\"  or fy.status=\"Delete\"  ";

        RestClient aql = new RestClient();
        String EmpAttendanceOutput = aql.getRestData(ApplicationConstants.END_POINT, leaveFinancialYearQuery);
        return EmpAttendanceOutput;
    }

    public String delete(String id, String userId) throws Exception {
        String result;
        if (id == null || userId == null) {
            return null;
        } else if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_LEAVE_ASSIGNMENT, "year", id)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, "year", id))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User username = new UserManager().fetch(userId);
            String fName = username.getFname();
            String dbStr = new FinancialYearManager().fetch(id);
            FinancialYear dbObj = new Gson().fromJson(dbStr, new TypeToken<FinancialYear>() {
            }.getType());
            dbObj.setStatus(ApplicationConstants.DELETE);
            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setDeletedBy(fName);
            String dbObjJson = new Gson().toJson(dbObj);

            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, id, dbObjJson);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;

    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, Id);
        List<FinancialYear> gisList = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public String active(String id, String userId) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("isActive", ApplicationConstants.YES);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String getdata = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, conditionMap);
        if (getdata != null) {
            List<FinancialYear> activeyeslist = new Gson().fromJson(getdata, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear activedata = activeyeslist.get(0);
            activedata.setIsActive("No");
            String activejson = new Gson().toJson(activedata);
            String id1 = ((Map<String, String>) activedata.getId()).get("$oid");
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, id1, activejson);
        }

        String result = "";
        if (id == null || userId == null) {
            return null;
        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String dbStr = new FinancialYearManager().fetch(id);
        FinancialYear dbObj = new Gson().fromJson(dbStr, new TypeToken<FinancialYear>() {
        }.getType());
        dbObj.setIsActive("Yes");
        dbObj.setUpdateDate(System.currentTimeMillis() + "");
        dbObj.setUpdatedBy(userName);
        String dbObjJson = new Gson().toJson(dbObj);

        boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, id, dbObjJson);
        if (fResult) {
            result = ApplicationConstants.SUCCESS;
        } else {
            result = ApplicationConstants.FAIL;
        }

        return result;

    }

    public FinancialYear fetchActiveLeaveFinancialYear() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("isActive", ApplicationConstants.YES);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String getdata = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE, conditionMap);
        List<FinancialYear> gisList = new Gson().fromJson(getdata, new TypeToken<List<FinancialYear>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return gisList.get(0);
    }
    
    
    
    
    public static void main(String[] args) throws Exception {
        String result=new FinancialYearManager().fetchAllForDropDowns();
        System.out.println("final result is"+result);
    }
}
