/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.user.manager.UserManager;
import com.accure.finance.dto.FinancialAccounting_FinancialYear;
import com.accure.hrms.dto.FinancialYear;
import com.accure.user.dto.User;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author upendra
 */
public class FinancialYearManager {

    public String saveFinancialYear(FinancialYear financialobj, String name) throws Exception {
        
        String result = "";
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("year", financialobj.getYear());
        //saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (financialobj == null || name == null) {
            result = null;
        } else if (hasDuplicateforSave(ApplicationConstants.FINANCIAL_YEAR_TABLE, saveconditionMap)) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(name);
            String fName = user.getFname();
            financialobj.setStatus(ApplicationConstants.ACTIVE);
            financialobj.setActive(ApplicationConstants.NO);
            financialobj.setCreateDate(System.currentTimeMillis() + "");
            financialobj.setUpdateDate(System.currentTimeMillis() + "");
            financialobj.setCreatedBy(fName);
            String financialjson = new Gson().toJson(financialobj);
            String fresult = DBManager.getDbConnection().insert(ApplicationConstants.FINANCIAL_YEAR_TABLE, financialjson);
            if (fresult != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String viewFinancialYearList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_YEAR_TABLE, conditionMap);
        return result;

    }

    public String updateFinancialYearRelation(FinancialYear financialobj,String rid, String user) throws Exception {

        String result="";
        User userna = new UserManager().fetch(user);
        String fName = userna.getFname();
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("year", financialobj.getYear());
        //saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
       if(financialobj==null||rid==null||user==null){
           result=null;
       }else if(isDuplicateforUpdate(ApplicationConstants.FINANCIAL_YEAR_TABLE, saveconditionMap, rid)){
           result=ApplicationConstants.DUPLICATE;
       }
       else {

            String yearJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_YEAR_TABLE, rid);
            List<FinancialYear> yearlist = new Gson().fromJson(yearJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear financialyear = yearlist.get(0);
       
            financialyear.setYear(financialobj.getYear());
            financialyear.setFromDate(financialobj.getFromDate());
            financialyear.setToDate(financialobj.getToDate());
  
            financialyear.setStatus(ApplicationConstants.ACTIVE);
            financialyear.setUpdateDate(System.currentTimeMillis() + "");

            financialyear.setUpdatedBy(fName);
            String fyearjson = new Gson().toJson(financialyear);

           boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_YEAR_TABLE, rid, fyearjson);
           if(fResult){
                 result=ApplicationConstants.SUCCESS;
           }else{
              result=ApplicationConstants.FAIL; 
           }

        }
        return result;
    }

    public boolean deleteFinancialyear(String rid) throws Exception {
        String existyearJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_YEAR_TABLE, rid);
        List<FinancialYear> fyearlist = new Gson().fromJson(existyearJson, new TypeToken<List<FinancialYear>>() {
        }.getType());
        FinancialYear fyear = fyearlist.get(0);
        FinancialYear fyearobje = new FinancialYear();
        fyearobje.setYear(fyear.getYear());
        fyearobje.setFromDate(fyear.getFromDate());
        fyearobje.setToDate(fyear.getToDate());
        fyearobje.setActive(fyear.getActive());
        fyearobje.setCreateDate(fyear.getCreateDate());
        fyearobje.setStatus(ApplicationConstants.DELETE);
        fyearobje.setUpdateDate(System.currentTimeMillis() + "");
        String fyearJson = new Gson().toJson(fyearobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_YEAR_TABLE, rid, fyearJson);
        return status;
    }

    public boolean changeFinancialYear(String rid) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("active", ApplicationConstants.YES);
        String getdata = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_YEAR_TABLE, conditionMap);
        if (getdata != null) {
            List<FinancialYear> activeyeslist = new Gson().fromJson(getdata, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear activedata = activeyeslist.get(0);
            FinancialYear activeobj = new FinancialYear();
            activeobj.setYear(activedata.getYear());
            activeobj.setFromDate(activedata.getFromDate());
            activeobj.setToDate(activedata.getToDate());
            activeobj.setStatus(ApplicationConstants.ACTIVE);
            activeobj.setActive(ApplicationConstants.NO);
            activeobj.setCreateDate(activedata.getCreateDate());
            activeobj.setUpdateDate(System.currentTimeMillis() + "");

            String id1 = ((Map<String, String>) activedata.getId()).get("$oid");

            String activejson = new Gson().toJson(activeobj);

            boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_YEAR_TABLE, id1, activejson);
        }

        String changeJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_YEAR_TABLE, rid);
        List<FinancialYear> yearlist = new Gson().fromJson(changeJson, new TypeToken<List<FinancialYear>>() {
        }.getType());

        FinancialYear financialyear = yearlist.get(0);
        FinancialYear fyearobj = new FinancialYear();
        fyearobj.setYear(financialyear.getYear());
        fyearobj.setFromDate(financialyear.getFromDate());
        fyearobj.setToDate(financialyear.getToDate());
        fyearobj.setActive(ApplicationConstants.YES);
        fyearobj.setCreateDate(financialyear.getCreateDate());
        fyearobj.setStatus(ApplicationConstants.ACTIVE);
        fyearobj.setUpdateDate(System.currentTimeMillis() + "");
        String fyearjson = new Gson().toJson(fyearobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_YEAR_TABLE, rid, fyearjson);
        return status;
    }

    //Added by deepak , for change financial year in financial masters
    public boolean changeYear(String fromDate, String toDate, String userId, String rowId) throws Exception {
        String recentYearJson = "";

        User user = new UserManager().fetch(userId);
        String fName = user.getFname();

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.FINANCIAL_ACTIVE, ApplicationConstants.YES);
        String data = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap);
        if (data != null) {
            Type type = new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType();
            List<FinancialAccounting_FinancialYear> financialYears = new Gson().fromJson(data, type);
            FinancialAccounting_FinancialYear financialYear = financialYears.get(0);
            financialYear.setActive(ApplicationConstants.NO);
            financialYear.setUpdateDate(System.currentTimeMillis() + "");
            financialYear.setUpdatedBy(fName);
            String yearJson = new Gson().toJson(financialYear);
            String id = ((Map<String, String>) financialYear.getId()).get("$oid");
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, id, yearJson);
        }
        HashMap<String, String> dateConditionMap = new HashMap<String, String>();
        dateConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        dateConditionMap.put("fromDate", fromDate);
        dateConditionMap.put("toDate", toDate);
        String recentData = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, dateConditionMap);
        if (recentData != null) {
            Type type = new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType();
            List<FinancialAccounting_FinancialYear> newFinancialYears = new Gson().fromJson(recentData, type);
            FinancialAccounting_FinancialYear newFinancialYear = newFinancialYears.get(0);
            newFinancialYear.setActive(ApplicationConstants.YES);
            newFinancialYear.setStatus(ApplicationConstants.ACTIVE);
            newFinancialYear.setUpdateDate(System.currentTimeMillis() + "");
            newFinancialYear.setUpdatedBy(fName);
            recentYearJson = new Gson().toJson(newFinancialYear);
        }
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rowId, recentYearJson);
        if (status) {
            return true;
        }
        return false;
    }

}
