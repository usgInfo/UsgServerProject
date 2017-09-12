/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.pension.dto.PensionArrear;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
/**
 *
 * @author accure
 */
public class PensionArrearManager {
    public String save(String pensionArrear) throws Exception {
    
        Type type = new TypeToken<PensionArrear>() {
        }.getType();
        PensionArrear arrearDTO = new Gson().fromJson(pensionArrear, type);
        arrearDTO.setCreateDate(System.currentTimeMillis() + "");
        arrearDTO.setUpdateDate(System.currentTimeMillis() + "");
        arrearDTO.setStatus(ApplicationConstants.ACTIVE);

        String arrearJson = new Gson().toJson(arrearDTO);
 
        String arrearResult = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_ARREAR_TABLE, arrearJson);
        return arrearResult;

    }

    public String search(String payMonth, String payYear, String fromDate, String toDate) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("payMonth", payMonth);
        conditionMap.put("payYear",payYear);
        conditionMap.put("fromDate", fromDate);
        conditionMap.put("toDate", toDate);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_ARREAR_TABLE, conditionMap);
        return result;
        
    }
    public String save1(String pensionArrear) throws Exception {
    
        Type type = new TypeToken<PensionArrear>() {
        }.getType();
        PensionArrear arrearDTO = new Gson().fromJson(pensionArrear, type);
        arrearDTO.setCreateDate(System.currentTimeMillis() + "");
        arrearDTO.setUpdateDate(System.currentTimeMillis() + "");
        arrearDTO.setStatus(ApplicationConstants.INACTIVE);

        String arrearJson1 = new Gson().toJson(arrearDTO);
 
        String arrearResult1 = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_ARREAR_NOT_PROCESS_TABLE, arrearJson1);
        return arrearResult1;

    }
//    public static void main(String[] args) throws Exception {
//        String result=new PensionArrearManager().search("","","","");
//        //System.out.println(result);
//        
//    }
    
}

