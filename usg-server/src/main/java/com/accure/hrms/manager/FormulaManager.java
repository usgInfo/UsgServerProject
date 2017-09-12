/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.hrms.dto.Formula;
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
 * @author upendra
 */
public class FormulaManager {

    public String saveFormula(Formula formula, String userid) throws Exception {

        String result;

        HashMap conditionMap = new HashMap();
        conditionMap.put("description", formula.getDescription());

        HashMap map = new HashMap();
        map.put("order", formula.getOrder());

        if (userid == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.FORMULA_TABLE, map) || hasDuplicateforSave(ApplicationConstants.FORMULA_TABLE, conditionMap)) {
            result = ApplicationConstants.DUPLICATE;
            return result;
        } else {
            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + " " + user.getLname();
            formula.setStatus(ApplicationConstants.ACTIVE);
            formula.setCreateDate(System.currentTimeMillis() + "");
            formula.setUpdateDate(System.currentTimeMillis() + "");
            formula.setCreatedBy(userName);
            formula.setUpdatedBy(userName);
            String formulajson = new Gson().toJson(formula);
            String fResult = DBManager.getDbConnection().insert(ApplicationConstants.FORMULA_TABLE, formulajson);
            if (fResult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String viewFormulaList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FORMULA_TABLE, conditionMap);
        return result;

    }

    public String updateFormula(Formula formula, String rid, String userid) throws Exception {

        String result;

        HashMap conditionMap = new HashMap();
        conditionMap.put("description", formula.getDescription());

        HashMap map = new HashMap();
        map.put("order", formula.getOrder());

        if (userid == null) {
            result = null;
        } else if ((isDuplicateforUpdate(ApplicationConstants.FORMULA_TABLE, map,rid)) || (isDuplicateforUpdate(ApplicationConstants.FORMULA_TABLE, conditionMap, rid))) {
            result = ApplicationConstants.DUPLICATE;
             return result;
        } else {

            User user = new UserManager().fetch(userid);
            String userName = user.getFname() + " " + user.getLname();

            String formulJson = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, rid);
            List<Formula> formulalist = new Gson().fromJson(formulJson, new TypeToken<List<Formula>>() {
            }.getType());
            Formula dbObj = formulalist.get(0);
            dbObj.setDescription(formula.getDescription());
            dbObj.setFormula(formula.getFormula());
            dbObj.setHiddenformula(formula.getHiddenformula());
            dbObj.setOrder(formula.getOrder());
            dbObj.setStatus(ApplicationConstants.ACTIVE);
            dbObj.setUpdateDate(System.currentTimeMillis() + "");
            dbObj.setUpdatedBy(userName);

            String formulajson = new Gson().toJson(dbObj);

            boolean status = DBManager.getDbConnection().update(ApplicationConstants.FORMULA_TABLE, rid, formulajson);

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;
    }

    public String deleteFormula(String rid, String userId) throws Exception {

        if (rid == null || rid.isEmpty()) {
            return null;
        }
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        String existformulaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, rid);
        List<Formula> formulalist = new Gson().fromJson(existformulaJson, new TypeToken<List<Formula>>() {
        }.getType());
        Formula formula = formulalist.get(0);
        Formula formulaobje = new Formula();
        formulaobje.setDescription(formula.getDescription());
        formulaobje.setFormula(formula.getFormula());
        formulaobje.setOrder(formula.getOrder());
        formulaobje.setCreateDate(formula.getCreateDate());
        formulaobje.setStatus(ApplicationConstants.DELETE);
        formulaobje.setUpdateDate(System.currentTimeMillis() + "");
        formulaobje.setDeletedBy(userName);

        String status ;
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.SALARY_HEAD_TABLE, "formula", rid) || DeleteDependencyManager.hasDependency(ApplicationConstants.HEAD_SLAB_TABLE, "formulaTwo", rid)) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.FORMULA_TABLE, rid, new Gson().toJson(formulaobje));
            if (result) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;

    }

    public static Formula fetchFormula(String next) throws Exception {
        String existformulaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, next);
        List<Formula> formulalist = new Gson().fromJson(existformulaJson, new TypeToken<List<Formula>>() {
        }.getType());
        Formula formula = formulalist.get(0);
        return formula;
    }

}
