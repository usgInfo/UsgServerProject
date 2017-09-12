/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.Grade;
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
 * @author user
 */
public class GradeManager {

    public String save(Grade grade,String userid) throws Exception {
        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        grade.setCreatedBy(userName);
        grade.setCreateDate(System.currentTimeMillis() + "");
        grade.setUpdateDate(System.currentTimeMillis() + "");
        grade.setStatus(ApplicationConstants.ACTIVE);
        String gradeid = null;
        if (duplicate(grade)) {
            gradeid = ApplicationConstants.DUPLICATE_MESSAGE;
            return gradeid;
        } else {
            String gradeJson = new Gson().toJson(grade);
            gradeid = DBManager.getDbConnection().insert(ApplicationConstants.GRADE_TABLE, gradeJson);
        }
        return gradeid;
    }

    public String fetch(String gradeId) throws Exception {
        if (gradeId == null || gradeId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, gradeId);
        List<Grade> gradeList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
        }.getType());
        if (gradeList == null || gradeList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gradeList.get(0));
    }

    public Grade get(String gradeId) throws Exception {
        if (gradeId == null || gradeId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, gradeId);
        List<Grade> gradeList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
        }.getType());
        if (gradeList == null || gradeList.size() < 1) {
            return null;
        }
        return gradeList.get(0);
    }

    /*public boolean delete(String gradeId) throws Exception {
        if (gradeId == null || gradeId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<Grade>() {
        }.getType();
        String grade = new GradeManager().fetch(gradeId);
        if (grade == null || grade.isEmpty()) {
            return false;
        }
        Grade graderJson = new Gson().fromJson(grade, type);
        graderJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.GRADE_TABLE, gradeId, new Gson().toJson(graderJson));
        return result;
    }*/
    public String deleteGrade(String gradeId,String userid) throws Exception {

        User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        Grade bank = fetchGradeDetails(gradeId);

        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.DELETE);
        bank.setDeletedBy(userName);
        String bankJson = new Gson().toJson(bank);
        String status = "";
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "grade", gradeId)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, "grade", gradeId)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.DESIGNATION_TABLE, "grade", gradeId)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, "grade", gradeId))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.GRADE_TABLE, gradeId, bankJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public Grade fetchGradeDetails(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String bankJson = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE, rid);
        List<Grade> gradeList = new Gson().fromJson(bankJson, new TypeToken<List<Grade>>() {
        }.getType());
        Grade grade = gradeList.get(0);
        return grade;
    }

    public String update(Grade grade, String gradeId,String userid) throws Exception {
         User user = new UserManager().fetch(userid);
        String userName = user.getFname() + " " + user.getLname();
        grade.setUpdatedBy(userName);
        grade.setUpdateDate(System.currentTimeMillis() + "");
        grade.setStatus(ApplicationConstants.ACTIVE);
        String status = null;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.put("gradeName", grade.getGradeName());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.GRADE_TABLE, duplicateCheckMap, gradeId)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
        } else {
            String graderJson = new Gson().toJson(grade);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.GRADE_TABLE, gradeId, graderJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public boolean duplicate(Grade grade) throws Exception {
        boolean res = false;
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap.put("gradeName", grade.getGradeName());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GRADE_TABLE, conditionMap);
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

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GRADE_TABLE, conditionMap);
        return result1;

    }

}
