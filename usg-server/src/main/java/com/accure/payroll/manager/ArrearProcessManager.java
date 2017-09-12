/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.payroll.dto.ArrearProcess;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author accure
 */
public class ArrearProcessManager {

    public String save(String arrearData) throws Exception {
        Type type = new TypeToken<ArrearProcess>() {
        }.getType();
        ArrearProcess arrearprocessDTO = new Gson().fromJson(arrearData, type);
        arrearprocessDTO.setCreateDate(System.currentTimeMillis() + "");
        arrearprocessDTO.setUpdateDate(System.currentTimeMillis() + "");
        arrearprocessDTO.setStatus(ApplicationConstants.ACTIVE);

        String arrearprocessJson = new Gson().toJson(arrearprocessDTO);

        String arrearprocessJsonresult = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_PROCESS_TABLE, arrearprocessJson);
        return arrearprocessJsonresult;

    }

    public String search(String ddo, String Month, String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("payMonth", Month);
        conditionMap.put("payYear", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_PROCESS_TABLE, conditionMap);
        return result;

    }

    public String searchforArrearAdj(String ddo, String Month, String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("payMonth", Month);
        conditionMap.put("payYear", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_PROCESS_TABLE, conditionMap);
        return result;

    }

    /**
     * @author chaitu
     * @description fetch() method will expect input as arrearId(primaryKey)
     * then it will returns the ArrearProcess object or null.
     * @table arrearprocess
     * @param String
     * @return ArrearProcess
     * @throws java.lang.Exception
     */
    public static ArrearProcess fetch(String arrearId) throws Exception {
        ArrearProcess arp = null;
        if (arrearId == null) {
            return arp;
        }
        String dbOutput = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_PROCESS_TABLE, arrearId);
        ArrayList<ArrearProcess> resultList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<ArrearProcess>>() {
        }.getType());
        arp = resultList.get(0);
        return arp;
    }

    /**
     * @author chaitu
     * @description update() method will update the sendToAccountStatus lock
     * status flag
     * @table arrearprocess
     * @param String arrearId
     * @param boolean sendToAccountStatus
     * @return boolean
     * @throws Exception
     */
    public static boolean update(String arrearId, boolean sendToAccountStatus) throws Exception {
        boolean isArrearUpdated = false;
        if (arrearId == null) {
            return isArrearUpdated;
        }

        ArrearProcess arp = fetch(arrearId);
        arp.setSendToAccountStatus(sendToAccountStatus);
        arp.setUpdateDate(Long.toString(System.currentTimeMillis()));
        isArrearUpdated = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, arrearId, new Gson().toJson(arp));
        return isArrearUpdated;
    }
}
