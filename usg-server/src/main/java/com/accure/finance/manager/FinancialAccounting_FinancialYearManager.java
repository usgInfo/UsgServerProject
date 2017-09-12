/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.FinancialAccounting_FinancialYear;
import com.accure.hrms.dto.Relation;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author upendra
 */
public class FinancialAccounting_FinancialYearManager {
    
    public String saveFinancialAccounting_FinancialYear(String year, String fromdate, String todate) throws Exception {
        
        if (year == null || fromdate == null || todate == null) {
            return null;
        }
        //  SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        //  Date fromdt = (Date) formatter.parse(fromdate);
//        DateFormat parser = new SimpleDateFormat("MM/dd/yyyy");
//        Date date = (Date) parser.parse(fromdate);
//        // Date fromdt = new Date(fromdate);
//        long mills = date.getTime();
//        String covfromdate = Long.toString(mills);
//
//        // Date todt = (Date) formatter.parse(todate);
//        Date todt = (Date) parser.parse(todate);
//        long mills1 = todt.getTime();
//        String covtodate = Long.toString(mills1);
        FinancialAccounting_FinancialYear financialobj = new FinancialAccounting_FinancialYear();
        financialobj.setYear(year);
        financialobj.setFromDate(fromdate);
        financialobj.setToDate(todate);
        financialobj.setStatus(ApplicationConstants.ACTIVE);
        financialobj.setActive(ApplicationConstants.NO);
        financialobj.setCreateDate(System.currentTimeMillis() + "");
        financialobj.setUpdateDate(System.currentTimeMillis() + "");
        String financialjson = new Gson().toJson(financialobj);
        String id = DBManager.getDbConnection().insert(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, financialjson);
        return id;
    }
    
    public String viewFinancialAccounting_FinancialYearList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap);
        return result;
        
    }
    
    public boolean updateFinancialAccounting_FinancialYearRelation(String fdate, String tdate, String rid, String year) throws Exception {
        String yearJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rid);
        List<FinancialAccounting_FinancialYear> yearlist = new Gson().fromJson(yearJson, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
        }.getType());

//        DateFormat parser = new SimpleDateFormat("MM/dd/yyyy");
//        Date updatefromdt = (Date) parser.parse(fdate);
//        long mills = updatefromdt.getTime();
//        String covupdatefromdate = Long.toString(mills);
//
//        Date updatetodt = (Date) parser.parse(tdate);
//        long millstodt = updatetodt.getTime();
//        String covupdatetodate = Long.toString(millstodt);
//
        FinancialAccounting_FinancialYear financialyear = yearlist.get(0);
        FinancialAccounting_FinancialYear fyearobj = new FinancialAccounting_FinancialYear();
        fyearobj.setYear(financialyear.getYear());
        fyearobj.setFromDate(fdate);
        fyearobj.setToDate(tdate);
        fyearobj.setYear(year);
        
        fyearobj.setActive(financialyear.getActive());
        fyearobj.setCreateDate(financialyear.getCreateDate());
        fyearobj.setStatus(ApplicationConstants.ACTIVE);
        fyearobj.setUpdateDate(System.currentTimeMillis() + "");
        String fyearjson = new Gson().toJson(fyearobj);
        
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rid, fyearjson);
        return status;
    }
    
    public boolean deleteFinancialyear(String rid) throws Exception {
        String existyearJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rid);
        List<FinancialAccounting_FinancialYear> fyearlist = new Gson().fromJson(existyearJson, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
        }.getType());
        FinancialAccounting_FinancialYear fyear = fyearlist.get(0);
        FinancialAccounting_FinancialYear fyearobje = new FinancialAccounting_FinancialYear();
        fyearobje.setYear(fyear.getYear());
        fyearobje.setFromDate(fyear.getFromDate());
        fyearobje.setToDate(fyear.getToDate());
        fyearobje.setActive(fyear.getActive());
        fyearobje.setCreateDate(fyear.getCreateDate());
        fyearobje.setStatus(ApplicationConstants.DELETE);
        fyearobje.setUpdateDate(System.currentTimeMillis() + "");
        String fyearJson = new Gson().toJson(fyearobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rid, fyearJson);
        return status;
    }
    
    public boolean changeFinancialAccounting_FinancialYear(String rid) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("active", ApplicationConstants.YES);
        String getdata = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap);
        if (getdata != null) {
            List<FinancialAccounting_FinancialYear> activeyeslist = new Gson().fromJson(getdata, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType());
            FinancialAccounting_FinancialYear activedata = activeyeslist.get(0);
            FinancialAccounting_FinancialYear activeobj = new FinancialAccounting_FinancialYear();
            activeobj.setYear(activedata.getYear());
            activeobj.setFromDate(activedata.getFromDate());
            activeobj.setToDate(activedata.getToDate());
            activeobj.setStatus(ApplicationConstants.ACTIVE);
            activeobj.setActive(ApplicationConstants.NO);
            activeobj.setCreateDate(activedata.getCreateDate());
            activeobj.setUpdateDate(System.currentTimeMillis() + "");
            
            String id1 = ((Map<String, String>) activedata.getId()).get("$oid");

            //  String id = (String) activedata.getId().get("$oid");
            String activejson = new Gson().toJson(activeobj);
            
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, id1, activejson);
        }
        
        String changeJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rid);
        List<FinancialAccounting_FinancialYear> yearlist = new Gson().fromJson(changeJson, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
        }.getType());
        
        FinancialAccounting_FinancialYear financialyear = yearlist.get(0);
        FinancialAccounting_FinancialYear fyearobj = new FinancialAccounting_FinancialYear();
        fyearobj.setYear(financialyear.getYear());
        fyearobj.setFromDate(financialyear.getFromDate());
        fyearobj.setToDate(financialyear.getToDate());
        fyearobj.setActive(ApplicationConstants.YES);
        fyearobj.setCreateDate(financialyear.getCreateDate());
        fyearobj.setStatus(ApplicationConstants.ACTIVE);
        fyearobj.setUpdateDate(System.currentTimeMillis() + "");
        String fyearjson = new Gson().toJson(fyearobj);
        
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, rid, fyearjson);
        return status;
    }
    
}
