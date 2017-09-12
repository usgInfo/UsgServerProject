/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Category;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Quarter;
import com.accure.hrms.dto.QuarterCategory;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asif
 */
public class QuarterManager {

     public String saveQuarter(Quarter quarter, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String quarterid=null;
        String userName = user.getFname() + " " + user.getLname();
        quarter.setCreatedBy(userName);
        if(duplicate(quarter))
        {
             quarterid = ApplicationConstants.DUPLICATE_MESSAGE;
              return quarterid;
        }
        else
        {
        quarter.setCreateDate(System.currentTimeMillis() + "");
        quarter.setStatus(ApplicationConstants.ACTIVE);
        String quarterJson = new Gson().toJson(quarter);
         quarterid = DBManager.getDbConnection().insert(ApplicationConstants.QUARTER_TABLE, quarterJson);
        }
        return quarterid;
    }

    public String fetchQuarter(String qId) throws Exception {
        if (qId == null || qId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_TABLE, qId);
        List<Quarter> quarterList = new Gson().fromJson(result, new TypeToken<List<Quarter>>() {
        }.getType());
        if (quarterList == null || quarterList.size() < 1) {
            return null;
        }
        return new Gson().toJson(quarterList.get(0));
    }

    public String deleteQuarter(String qId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (qId == null || qId.isEmpty()) {
            return null;
        }
        Type type = new TypeToken<Quarter>() {
        }.getType();
        String quarter = new QuarterManager().fetchQuarter(qId);
        if (quarter == null || quarter.isEmpty()) {
            return null;
        }
        Quarter quarterJson = new Gson().fromJson(quarter, type);
        quarterJson.setStatus(ApplicationConstants.DELETE);
        quarterJson.setUpdateDate(System.currentTimeMillis() + "");
        quarterJson.setUpdatedBy(userName);
          String status = "";
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "quarterNo", qId)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.QUARTER_TABLE, qId, new Gson().toJson(quarterJson));
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }
        
      
    public String updateQuarter(Quarter quarter, String qId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        Type type = new TypeToken<Quarter>() {
        }.getType();
        String status = null;
         HashMap duplicateCheckMap = new HashMap();
         duplicateCheckMap.put("quarterNo",quarter.getQuarterNo());
         //duplicateCheckMap.put("category", quarter.getCategory());
        if(Duplicate.isDuplicateforUpdate(ApplicationConstants.QUARTER_TABLE, duplicateCheckMap,qId))
        {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
            else
            {
        String qua = new QuarterManager().fetchQuarter(qId);
        if (qua == null || qua.isEmpty()) {
            status=ApplicationConstants.FAIL;
        }
        Quarter quarterJson = new Gson().fromJson(qua, type);
        quarterJson.setCity(quarter.getCity());
        quarterJson.setCategory(quarter.getCategory());
        quarterJson.setQuarterNo(quarter.getQuarterNo());
        quarterJson.setCarpetArea(quarter.getCarpetArea());
        quarterJson.setCondition(quarter.getCondition());
        quarterJson.setRemarks(quarter.getRemarks());

        quarterJson.setUpdateDate(System.currentTimeMillis() + "");
        quarterJson.setUpdatedBy(userName);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.QUARTER_TABLE, qId, new Gson().toJson(quarterJson));
        if (flag)
                    {
                status = ApplicationConstants.SUCCESS;
            }
        else {
                status = ApplicationConstants.FAIL;
            }       
       
        }
         return status;
    }

    public String fetchAllQuarter() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TABLE, conditionMap);
        //System.out.println(result1);
        List<Quarter> quarterList = new Gson().fromJson(result1, new TypeToken<List<Quarter>>() {
        }.getType());
        quarterList = getCity(quarterList);
        quarterList = getQuarterCategory(quarterList);
        return new Gson().toJson(quarterList);

    }

      public static List<Quarter> getQuarterCategory(List<Quarter> employeeList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.QUARTER_CATEGORY_TABLE);
        List<QuarterCategory> religionList = new Gson().fromJson(result, new TypeToken<List<QuarterCategory>>() {
        }.getType());
        for (Iterator<QuarterCategory> iterator = religionList.iterator(); iterator.hasNext();) {
            QuarterCategory next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCategory());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getCategory())) {
                    employeeList.get(i).setCategory(entry.getValue());
                }
            }
        }
        //System.out.println("the employee List" + employeeList);
        return employeeList;
    }
    
    private List<Quarter> getCity(List<Quarter> list) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CITY_TABLE);
        List<CityMaster> religionList = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());
        for (Iterator<CityMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            CityMaster next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getCity())) {
                    list.get(i).setCityName(entry.getValue());
                }
            }
        }
        return list;
    }
     private List<Quarter> getCategory(List<Quarter> list) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.QUARTER_CATEGORY_TABLE);
        List<QuarterCategory> religionList = new Gson().fromJson(result, new TypeToken<List<QuarterCategory>>() {
        }.getType());
        for (Iterator<QuarterCategory> iterator = religionList.iterator(); iterator.hasNext();) {
            QuarterCategory next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCategory());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getCategory())) {
                    list.get(i).setCategoryName(entry.getValue());
                }
            }
        }
        return list;
    }

    public String fetchAllQuarterByCategoryName(String name) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("category", name);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TABLE, conditionMap);
        return result1;

    }

//    public void deleteQuarteByCategoryName(String name) throws Exception {
//        String AlldateOfRetirement = new QuarterManager().fetchAllQuarterByCategoryName(name);
//        List<Quarter> quarterList = new Gson().fromJson(AlldateOfRetirement, new TypeToken<List<Quarter>>() {
//        }.getType());
//        for (int i = 0; i < quarterList.size(); i++) {
//            Quarter get = quarterList.get(i);
//            String id1 = ((LinkedTreeMap<String, String>) get.getId()).get("$oid");
//            new QuarterManager().deleteQuarter(id1);
//        }
//    }
//
//    public void updateQuarteByCategoryName(String previouscategoryname, String updatedcategoryname) throws Exception {
//        String dateOfRetirementOfQuarterstoBeUpdated = new QuarterManager().fetchAllQuarterByCategoryName(previouscategoryname);
//        List<Quarter> quarterList = new Gson().fromJson(dateOfRetirementOfQuarterstoBeUpdated, new TypeToken<List<Quarter>>() {
//        }.getType());
//        for (int i = 0; i < quarterList.size(); i++) {
//            Quarter get = quarterList.get(i);
//            String id = ((LinkedTreeMap<String, String>) get.getId()).get("$oid");
//            Quarter set = new Quarter();
//            set.setCategory(updatedcategoryname);
//            set.setCarpetArea(get.getCarpetArea());
//            set.setCity(get.getCity());
//            set.setCondition(get.getCondition());
//            set.setCreateDate(get.getCreateDate());
//            set.setMode(get.getMode());
//            set.setQuarterNo(get.getQuarterNo());
//            set.setRemarks(get.getRemarks());
//            set.setStatus(get.getStatus());
//            set.setType(get.getType());
//            set.setUpdateDate(System.currentTimeMillis() + "");
//
//            new QuarterManager().updateQuarter(set, id);
//        }
//    }
     public boolean duplicate(Quarter qtr) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("quarterNo",qtr.getQuarterNo());
           // conditionMap.put("category", qtr.getCategory());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_TABLE, conditionMap);
            if (result != null) {
                
                        res = true;
                    }
                }
            
         catch (Exception ex) 
         {
            ex.printStackTrace();
         }
        return res;
    }
    public static void main(String[] args) throws Exception {
        //System.out.println(new QuarterManager().fetchAllQuarter());
    }
}
