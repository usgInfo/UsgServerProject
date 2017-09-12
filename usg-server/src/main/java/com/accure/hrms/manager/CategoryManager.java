/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.common.delete.DeleteDependencyManager;
import com.accure.hrms.dto.Category;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author accure
 */
public class CategoryManager {

    public String saveCategory(String category, String loginUserId) throws Exception {

        String status = null;
        if (category == null) {
            return null;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("categoryy", category);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.CATEGORY_TABLE, duplicateCheckMap)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
        if (status == null) {
            Category categoryobj = new Category();
            categoryobj.setCategoryy(category);
            categoryobj.setStatus(ApplicationConstants.ACTIVE);
            categoryobj.setCreateDate(System.currentTimeMillis() + "");
            categoryobj.setCreatedBy(userName);
            String categoryjson = new Gson().toJson(categoryobj);
            status = DBManager.getDbConnection().insert(ApplicationConstants.CATEGORY_TABLE, categoryjson);
        }
        return status;
    }

    public String viewCategoryList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CATEGORY_TABLE, conditionMap);
        return result;

    }

    public String viewCategoryListforDesginationMapping() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CATEGORY_TABLE, conditionMap);
        return result;

    }

    public String updateCategory(String category, String rid, String loginUserId) throws Exception {

        String categoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, rid);
        List<Category> categorylist = new Gson().fromJson(categoryJson, new TypeToken<List<Category>>() {
        }.getType());
        String status = null;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("categoryy", category);
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.CATEGORY_TABLE, duplicateCheckMap, rid)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        }
        if (status == null || status.isEmpty()) {
            Category dbObj = categorylist.get(0);
            Category categoryobj = new Category();
            categoryobj.setCategoryy(category);
            categoryobj.setCreateDate(dbObj.getCreateDate());
            categoryobj.setCreatedBy(dbObj.getCreatedBy());
            categoryobj.setUpdatedBy(userName);
            categoryobj.setStatus(ApplicationConstants.ACTIVE);
            categoryobj.setUpdateDate(System.currentTimeMillis() + "");
            String categojson = new Gson().toJson(categoryobj);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CATEGORY_TABLE, rid, categojson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public String deleteCategory(String rid, String loginUserId) throws Exception {
        String existCategoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, rid);
        List<Category> categorylist = new Gson().fromJson(existCategoryJson, new TypeToken<List<Category>>() {
        }.getType());
        String status = "";
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Category category = categorylist.get(0);
        Category categoryobje = new Category();
        categoryobje.setCategoryy(category.getCategoryy());
        categoryobje.setCreateDate(category.getCreateDate());
        categoryobje.setCreatedBy(category.getCreatedBy());
        categoryobje.setUpdatedBy(category.getUpdatedBy());
        categoryobje.setDeletedBy(userName);
        categoryobje.setStatus(ApplicationConstants.DELETE);
        categoryobje.setUpdateDate(System.currentTimeMillis() + "");
        String categoryJson = new Gson().toJson(categoryobje);
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "category", rid))) {
            status = ApplicationConstants.DELETE_MESSAGE;
            return status;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, "categoryposts.categoory", rid))) {
            status = ApplicationConstants.DELETE_MESSAGE;
            
            return status;
        }
        if (!(status).equalsIgnoreCase(ApplicationConstants.DELETE_MESSAGE)) {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.CATEGORY_TABLE, rid, categoryJson);
            
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;

    }

}
