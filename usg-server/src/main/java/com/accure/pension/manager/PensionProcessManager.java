/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.pension.dto.PensionEmployee;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class PensionProcessManager {

    public String viewPensionProcessEmployeeData(String startDate, String billNo, String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("pensionLeftStatus", "Yes");
//        conditionMap.put("employeecode", "1234");
//       conditionMap.put("pensionStartDate","07/15/2016");
//        conditionMap.put("pensionOrderNum", "123");
        //conditionMap.put("pensionStartDate", startDate);
     //   conditionMap.put("pensionOrderNum", billNo);
        conditionMap.put("employeecode", employeeCode);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);
        return result;
    }

    public String viewPensionNotProcessEmployeeData(String startDate, String billNo, String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("pensionLeftStatus", "No");
        conditionMap.put("pensionStartDate", startDate);
        conditionMap.put("pensionOrderNum", billNo);
        conditionMap.put("employeecode", employeeCode);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);
        return result;
    }

    public String viewPensionLockedEmployeeData(String startDate, String billNo, String employeeCode) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("stopPension", "Yes");
        conditionMap.put("employeecode", employeeCode);
        conditionMap.put("pensionStartDate", startDate);
        conditionMap.put("pensionOrderNum", billNo);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);
        List<PensionEmployee> empList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());
        //System.out.println(empList.size());
        return result;

    }

    public boolean PensionProcessEmployeeData(String id) throws Exception {
        boolean result = false;
        int count = 0;

        JSONObject obj = new JSONObject(id);

        JSONArray array = obj.getJSONArray("arrearprocessedList");

       

        return result;

    }

    public String fetchPensionEmployee(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String idValue = Id.toString().trim();
        //System.out.println("idValue -------------- " + idValue);
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_EMPLOYEE_TABLE, idValue);
        List<PensionEmployee> gisList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public boolean PensionNotProcessEmployeeData(String id) throws Exception {

        boolean result = false;
        int count = 0;

        JSONObject obj = new JSONObject(id);

       

        return result;

    }

    public static void main(String args[]) throws Exception {

        //boolean a = new PensionProcessManager().PensionProcessEmployeeData("577b69d15c9f228bf7f468d7","");
        ////System.out.println(a);
    }

}
