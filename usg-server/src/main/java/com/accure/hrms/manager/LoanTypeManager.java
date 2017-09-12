/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.LoanType;
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
public class LoanTypeManager {

    public String saveLoanType(String head) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_TYPE_TABLE, conditionMap);
        if (result != null) {
            List<LoanType> categolist = new Gson().fromJson(result, new TypeToken<List<LoanType>>() {
            }.getType());
            for (int i = 0; i < categolist.size(); i++) {
                if (categolist.get(i).getLoanType() == head || categolist.get(i).getLoanType().equals(head)) {
                    return "existed";
                }
            }
        }

        LoanType loantypeobj = new LoanType();
        loantypeobj.setLoanType(head);
        loantypeobj.setStatus(ApplicationConstants.ACTIVE);
        loantypeobj.setCreateDate(System.currentTimeMillis() + "");
        loantypeobj.setUpdateDate(System.currentTimeMillis() + "");
        String loantypejson = new Gson().toJson(loantypeobj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_TYPE_TABLE, loantypejson);
        return id;
    }

    public String viewLoanType() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_TYPE_TABLE, conditionMap);
        return result;

    }

    public boolean updateLoanType(String loantype, String rid) throws Exception {
        String relationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_TYPE_TABLE, rid);
        List<LoanType> relationlist = new Gson().fromJson(relationJson, new TypeToken<List<LoanType>>() {
        }.getType());
        LoanType relation = relationlist.get(0);
        LoanType relationobj = new LoanType();
        relationobj.setLoanType(loantype);
        relationobj.setCreateDate(relation.getCreateDate());
        relationobj.setStatus(ApplicationConstants.ACTIVE);
        relationobj.setUpdateDate(System.currentTimeMillis() + "");
        String relationjson = new Gson().toJson(relationobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOAN_TYPE_TABLE, rid, relationjson);
        return status;
    }

    public boolean deleteLoanType(String rid) throws Exception {
        String loanTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_TYPE_TABLE, rid);
        List<LoanType> loanTypelist = new Gson().fromJson(loanTypeJson, new TypeToken<List<LoanType>>() {
        }.getType());
        LoanType loantype = loanTypelist.get(0);
        LoanType loantypeobje = new LoanType();
        loantypeobje.setLoanType(loantype.getLoanType());
        loantypeobje.setCreateDate(loantype.getCreateDate());
        loantypeobje.setStatus(ApplicationConstants.DELETE);
        loantypeobje.setUpdateDate(System.currentTimeMillis() + "");
        String loantypeobjeJson = new Gson().toJson(loantypeobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOAN_TYPE_TABLE, rid, loantypeobjeJson);
        return status;
    }

}
