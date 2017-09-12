/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.payroll.manager;

import com.accure.payroll.dto.AdjIncomeTax;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 *
 * @author upendra
 */
public class AdjIncomeTaxManager {
    public String save(String incometaxData) throws Exception {
        Type type = new TypeToken<AdjIncomeTax>() {
        }.getType();
        AdjIncomeTax adjincometaxDTO = new Gson().fromJson(incometaxData, type);
        adjincometaxDTO.setCreateDate(System.currentTimeMillis() + "");
        adjincometaxDTO.setUpdateDate(System.currentTimeMillis() + "");
        adjincometaxDTO.setStatus(ApplicationConstants.ACTIVE);

        String adjincometaxJson = new Gson().toJson(adjincometaxDTO);

        String adjincometaxJsonresult = DBManager.getDbConnection().insert(ApplicationConstants.ADJ_INCOMETAX_TABLE, adjincometaxJson);
        return adjincometaxJsonresult;

    } 
     public String search(String ddo, String Month, String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("month", Month);
        conditionMap.put("year", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ADJ_INCOMETAX_TABLE, conditionMap);
        return result;

    }
}
