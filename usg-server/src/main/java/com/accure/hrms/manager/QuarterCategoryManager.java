/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.QuarterCategory;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class QuarterCategoryManager {

    public String saveQuarterCategory(QuarterCategory quartercategory, String userId) throws Exception {
        
      String bankid=null;
        if(duplicate(quartercategory))
        {
           bankid = ApplicationConstants.DUPLICATE_MESSAGE;  
           return bankid;
        }
        else
        {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        quartercategory.setCreateDate(System.currentTimeMillis() + "");
        quartercategory.setUpdateDate(System.currentTimeMillis() + "");
        quartercategory.setUpdatedBy(userName);
        quartercategory.setCreatedBy(userName);
        quartercategory.setStatus(ApplicationConstants.ACTIVE);

        String quartercategoryJson = new Gson().toJson(quartercategory);

         bankid = DBManager.getDbConnection().insert(ApplicationConstants.QUARTER_CATEGORY_TABLE, quartercategoryJson);
        }
        return bankid;
    }

    public String fetchQuarterCategory(String qid) throws Exception {
        if (qid == null || qid.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.QUARTER_CATEGORY_TABLE, qid);
        List<QuarterCategory> quartercategoryList = new Gson().fromJson(result, new TypeToken<List<QuarterCategory>>() {
        }.getType());
        if (quartercategoryList == null || quartercategoryList.size() < 1) {
            return null;
        }
        return new Gson().toJson(quartercategoryList.get(0));
    }

    public String deleteQuarterCategory(String qId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (qId == null || qId.isEmpty()) {
            return null;
        }
        Type type = new TypeToken<QuarterCategory>() {
        }.getType();
        String qc = new QuarterCategoryManager().fetchQuarterCategory(qId);
        if (qc == null || qc.isEmpty()) {
            return null;
        }
        QuarterCategory quartercategory = new Gson().fromJson(qc, type);
        quartercategory.setStatus(ApplicationConstants.DELETE);
        quartercategory.setUpdateDate(System.currentTimeMillis() + "");
        quartercategory.setUpdatedBy(userName);
        String status="";
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "quarterNo", qId)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.QUARTER_CATEGORY_TABLE, qId, new Gson().toJson(quartercategory));
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;

    }

    public String updateQuarterCategory(QuarterCategory quartercategory, String qId) throws Exception {
        String previousqc = new QuarterCategoryManager().fetchQuarterCategory(qId);
        Type type = new TypeToken<QuarterCategory>() {
        }.getType();
        QuarterCategory previousquatercategory = new Gson().fromJson(previousqc, type);
         String status = null;
         HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("category",quartercategory.getCategory());
        if(Duplicate.isDuplicateforUpdate(ApplicationConstants.QUARTER_CATEGORY_TABLE, duplicateCheckMap,qId))
        {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
            else
            {
        String previouscategoryName = previousquatercategory.getCategory();
        String updatedcategoryname = quartercategory.getCategory();
        quartercategory.setUpdateDate(System.currentTimeMillis() + "");
        quartercategory.setStatus(ApplicationConstants.ACTIVE);
        String quartercategoryJson = new Gson().toJson(quartercategory);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.QUARTER_CATEGORY_TABLE, qId, quartercategoryJson);
//        if (result) {
//            QuarterManager quartermanager = new QuarterManager();
//            try {
//                quartermanager.updateQuarteByCategoryName(previouscategoryName, updatedcategoryname);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
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
     public boolean duplicate(QuarterCategory cuarterCategory) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("category",cuarterCategory.getCategory());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_CATEGORY_TABLE, conditionMap);
            if (result != null) {
//                List<GISGroup> list = new Gson().fromJson(result, new TypeToken<List<GISGroup>>() {
//                }.getType());
//
//                for (GISGroup li : list) {
//                    if (.equalsIgnoreCase(li.getGroupName())) {
                        res = true;
//                    }
//                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
    public String fetchAllQuarterCategory() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.QUARTER_CATEGORY_TABLE, conditionMap);
        return result1;

    }

}
