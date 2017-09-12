/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Class;
import com.accure.hrms.dto.Grade;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class DesignationManager {

    public String save(Designation designation,String userid) throws Exception {
         User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        String designationId=null;
        if(duplicate(designation))
        {
            designationId = ApplicationConstants.DUPLICATE_MESSAGE;
            return designationId; 
        }
        else
        {
        designation.setCreatedBy(userName);
        designation.setCreateDate(System.currentTimeMillis() + "");
        designation.setUpdateDate(System.currentTimeMillis() + "");
        designation.setStatus(ApplicationConstants.ACTIVE);
        String designationJson = new Gson().toJson(designation);
         designationId = DBManager.getDbConnection().insert(ApplicationConstants.DESIGNATION_TABLE, designationJson);
        }
        return designationId;
    }

    public String fetch(String designationId) throws Exception {
        if (designationId == null || designationId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, designationId);
        List<Designation> designationList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        if (designationList == null || designationList.size() < 1) {
            return null;
        }
        return new Gson().toJson(designationList.get(0));
    }

    public Designation get(String designationId) throws Exception {
        Designation des = null;
        if (designationId == null || designationId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, designationId);
        List<Designation> designationList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        if (designationList == null || designationList.size() < 1) {
            return null;
        }
        des = designationList.get(0);
        return des;
    }

    public String delete(String designationId,String userid) throws Exception {
          String status = "";
           User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
    
        if (designationId == null || designationId.isEmpty()) {
            status = ApplicationConstants.FAIL;
        }
        Type type = new TypeToken<Designation>() {
        }.getType();
        String designation = new DesignationManager().fetch(designationId);
        if (designation == null || designation.isEmpty()) {
            status = ApplicationConstants.FAIL;
        }
      
    if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "designation", designationId)|| DeleteDependencyManager.hasDependency(ApplicationConstants.LOAN_ORDER_TABLE, "sanctionedBy", designationId) || DeleteDependencyManager.hasDependency(ApplicationConstants.LOAN_ORDER_TABLE, "comptroller", designationId))
    {
                status = ApplicationConstants.DELETE_MESSAGE;
    }
    else
    {
        Designation designationrJson = new Gson().fromJson(designation, type);
        designationrJson.setStatus(ApplicationConstants.INACTIVE);
        designationrJson.setDeletedBy(userName);
        designationrJson.setUpdateDate(System.currentTimeMillis()+"");
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DESIGNATION_TABLE, designationId, new Gson().toJson(designationrJson));
         if (flag)
                {
                        status = ApplicationConstants.SUCCESS;
                } 
            else 
                {
                        status = ApplicationConstants.FAIL;
                }
       
    }
     return status;
    }

    public String update(Designation designation, String designationId,String userid) throws Exception {
         String result = null;
          User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
            HashMap duplicateCheckMap = new HashMap();
            duplicateCheckMap.put("designation", designation.getDesignation());
           // duplicateCheckMap.put("designationCategory", designation.getDesignationCategory());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.DESIGNATION_TABLE, duplicateCheckMap,designationId))
        {
            result = ApplicationConstants.DUPLICATE_MESSAGE;
            return result;
        }
            else
            {
                designation.setUpdatedBy(userName);
                designation.setUpdateDate(System.currentTimeMillis() + "");
                designation.setStatus(ApplicationConstants.ACTIVE);
                String designationrJson = new Gson().toJson(designation);
                boolean flag = DBManager.getDbConnection().update(ApplicationConstants.DESIGNATION_TABLE, designationId, designationrJson);
                if (flag) 
                {
                result = ApplicationConstants.SUCCESS;
                } else
                {
                result = ApplicationConstants.FAIL;
                }
           }
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_TABLE, conditionMap);
        List<Designation> designationList = new Gson().fromJson(result1, new TypeToken<List<Designation>>() {
        }.getType());
        try {
            designationList = getClass(designationList);
             designationList = getGrade(designationList);
        } catch (Exception e) {
        }
        return new Gson().toJson(designationList);

    }

    public static String getRetirementDate(String designation, String dob) throws Exception {

        String designList = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, designation);
        List<Designation> designationList = new Gson().fromJson(designList, new TypeToken<List<Designation>>() {
        }.getType());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(dob);

        for (Designation desig : designationList) {
            int age = Integer.parseInt(desig.getRetirementAge());
            int getyear = date.getYear();
            int newyear = age + getyear;
            date.setYear(newyear);

        }
        HashMap<String, String> resultList = new HashMap<String, String>();
        resultList.put("retirementdate", new Gson().toJson(formatter.format(date)));

        return new Gson().toJson(resultList);
    }

    public static void main(String[] args) throws Exception {
        String result = new DesignationManager().getRetirementDate("57dcd3bafedb88e2f1f6f968", "12/10/1992");
        //System.out.println(result);
    }

    private List<Designation> getClass(List<Designation> desigList) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CLASS_TABLE);
        List<Class> religionList = new Gson().fromJson(result, new TypeToken<List<Class>>() {
        }.getType());
        for (Iterator<Class> iterator = religionList.iterator(); iterator.hasNext();) {
            Class next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getName());
        }
        for (int i = 0; i < desigList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(desigList.get(i).getClas())) {
                    desigList.get(i).setClas(entry.getValue());
                }
            }
        }
        return desigList;
    }

//    public static void main(String args[]) throws Exception {
//        String res = new DesignationManager().getRetirementDate("574ec44044ae764b46a13557", "22/08/1992");
//        //System.out.println("Retirementage"+res);
//    }
    
    public String getGradePayValue(String Id)throws Exception
    {
          String result = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, Id);
          if(result != "")
          {
            List<Grade> gradeList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {}.getType());
             return new Gson().toJson(gradeList.get(0));
          }
           return "";
    }

    private List<Designation> getGrade(List<Designation> designationList) throws Exception
    {
        Map<String, String> gradeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GRADE_TABLE);
        List<Grade> gradeList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
        }.getType());
        for (Iterator<Grade> iterator = gradeList.iterator(); iterator.hasNext();) {
            Grade next = iterator.next();
            gradeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getGradeName());
        }
        for (int i = 0; i < gradeList.size(); i++)
        {
            for (Map.Entry<String, String> entry : gradeMap.entrySet()) {
                if (entry.getKey().equals(designationList.get(i).getGrade())) {
                    designationList.get(i).setGradeName(entry.getValue());
                }
            }
        }
        return designationList;
    }
     public boolean duplicate(Designation desi) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("designation", desi.getDesignation());
            //conditionMap.put("designationCategory", desi.getDesignationCategory());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_TABLE, conditionMap);
            if (result != null) {
//                List<DAMaster> list = new Gson().fromJson(result, new TypeToken<List<DAMaster>>() {
//                }.getType());
//
//                for (DAMaster li : list) {
//                    if (damaster.getDefinedRate().equals(li.getDefinedRate()) && damaster.getPaidRate() == li.getPaidRate()) {
//                        res = true;
//                    }
//                }
                     res = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}
