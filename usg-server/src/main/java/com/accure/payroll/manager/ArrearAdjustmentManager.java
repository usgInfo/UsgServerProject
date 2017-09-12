/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.LoanNature;
import com.accure.payroll.dto.ArrearConfig;
import com.accure.payroll.dto.EmpArrearAdjustment;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author accure
 */
public class ArrearAdjustmentManager {

    public String save(String arrearAdjData) throws Exception {
        Type type = new TypeToken<EmpArrearAdjustment>() {
        }.getType();
        EmpArrearAdjustment arrearadjDTO = new Gson().fromJson(arrearAdjData, type);
        arrearadjDTO.setCreateDate(System.currentTimeMillis() + "");
        arrearadjDTO.setUpdateDate(System.currentTimeMillis() + "");
        arrearadjDTO.setStatus(ApplicationConstants.ACTIVE);

        String arrearadjJson = new Gson().toJson(arrearadjDTO);

        String arrearadjJsonresult = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_ADJUSTMENT_TABLE, arrearadjJson);
        return arrearadjJsonresult;
    }

    public String viewArrearAdjList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_ADJUSTMENT_TABLE, conditionMap);
        List<EmpArrearAdjustment> ArrearAdjResult = new Gson().fromJson(result, new TypeToken<List<EmpArrearAdjustment>>() {
        }.getType());
        for (EmpArrearAdjustment cl : ArrearAdjResult) {
            if ((cl.getDdo() != null)) {
                String getDDOList = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                if (getDDOList != null) {
                    List<DDO> getDDO = new Gson().fromJson(getDDOList, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO gal1 = getDDO.get(0);
                    cl.setDdo(gal1.getDdoName());

                }
            }

        }
        return new Gson().toJson(ArrearAdjResult);

    }
}
