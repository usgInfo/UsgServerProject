/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.EmployeePromotion;
import com.accure.hrms.dto.EmployeeSalary;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author upendra
 */
public class EmployeeSalaryManager {

    public String save(EmployeeSalary obj) throws Exception {
        obj.setCreateDate(System.currentTimeMillis() + "");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objJson = new Gson().toJson(obj);
        String objId = DBManager.getDbConnection().insert(ApplicationConstants.EMP_SALARY_TABLE, objJson);
        return objId;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMP_SALARY_TABLE, conditionMap);
        List<EmployeeSalary> empsalarylist = new Gson().fromJson(result1, new TypeToken<List<EmployeeSalary>>() {
        }.getType());
        for (EmployeeSalary cl : empsalarylist) {
            String ddo = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
            List<DDO> ddolist = new Gson().fromJson(ddo, new TypeToken<List<DDO>>() {
            }.getType());
            DDO gal4 = ddolist.get(0);
            cl.setDdo(gal4.getDdoName());
        }
        return new Gson().toJson(empsalarylist);

    }
      public String fetch(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMP_SALARY_TABLE, objId);
        List<EmployeeSalary> objList = new Gson().fromJson(result, new TypeToken<List<EmployeeSalary>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    public boolean delete(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<EmployeeSalary>() {
        }.getType();
        String obj = new EmployeeSalaryManager().fetch(objId);
        if (obj == null || obj.isEmpty()) {
            return false;
        }
        EmployeeSalary objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMP_SALARY_TABLE, objId, new Gson().toJson(objrJson));
        return result;
    }

    public boolean update(EmployeeSalary obj, String objId) throws Exception {
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objrJson = new Gson().toJson(obj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMP_SALARY_TABLE, objId, objrJson);
        return result;
    }
}
