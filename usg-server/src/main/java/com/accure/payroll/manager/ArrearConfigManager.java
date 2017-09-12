/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.ArrearConfig;
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
public class ArrearConfigManager {

    public String save(String ddo, String head, String order) throws Exception {
        ArrearConfig arrearconfigDTO = new ArrearConfig();
        arrearconfigDTO.setDdo(ddo);
        arrearconfigDTO.setHead(head);
        arrearconfigDTO.setOrder(order);
        arrearconfigDTO.setStatus(ApplicationConstants.ACTIVE);
        arrearconfigDTO.setCreateDate(System.currentTimeMillis() + "");
        arrearconfigDTO.setUpdateDate(System.currentTimeMillis() + "");
        String arrearconfigjson = new Gson().toJson(arrearconfigDTO);

        String id = DBManager.getDbConnection().insert(ApplicationConstants.ARREAR_CONFIG_TABLE, arrearconfigjson);
        return id;
    }

    public String viewArrearConfigList(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo",ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.ARREAR_CONFIG_TABLE, conditionMap);
        List<ArrearConfig> ArrearConfigResult = new Gson().fromJson(result, new TypeToken<List<ArrearConfig>>() {
        }.getType());
        for (ArrearConfig cl : ArrearConfigResult) {

            String getDDOList = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());

            List<DDO> getDDO = new Gson().fromJson(getDDOList, new TypeToken<List<DDO>>() {
            }.getType());
            DDO gal1 = getDDO.get(0);
            cl.setDdo(gal1.getDdoName());
            String getHeadList = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, cl.getHead());

            List<SalaryHead> getDDO1 = new Gson().fromJson(getHeadList, new TypeToken<List<SalaryHead>>() {
            }.getType());
            SalaryHead gal = getDDO1.get(0);
            cl.setHead(gal.getShortDescription());

        }
        return new Gson().toJson(ArrearConfigResult);

    }

    public boolean update(String ddo, String rid, String head, String order) throws Exception {
        String ArrearConfigJson = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_CONFIG_TABLE, rid);
        List<ArrearConfig> ArrearConfiglist = new Gson().fromJson(ArrearConfigJson, new TypeToken<List<ArrearConfig>>() {
        }.getType());
        ArrearConfig arreardata = ArrearConfiglist.get(0);
        ArrearConfig ArrearConfigobj = new ArrearConfig();
        ArrearConfigobj.setDdo(ddo);
        ArrearConfigobj.setHead(head);
        ArrearConfigobj.setOrder(order);
        ArrearConfigobj.setCreateDate(arreardata.getCreateDate());
        ArrearConfigobj.setStatus(ApplicationConstants.ACTIVE);
        ArrearConfigobj.setUpdateDate(System.currentTimeMillis() + "");
        String ArrearConfigjson = new Gson().toJson(ArrearConfigobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_CONFIG_TABLE, rid, ArrearConfigjson);
        return status;
    }

    public boolean deleteArrearconfig(String rid) throws Exception {
        String existreligionJson = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_CONFIG_TABLE, rid);
        List<ArrearConfig> ArrearConfiglist = new Gson().fromJson(existreligionJson, new TypeToken<List<ArrearConfig>>() {
        }.getType());
        ArrearConfig arrearconfig = ArrearConfiglist.get(0);
        ArrearConfig ArrearConfigobje = new ArrearConfig();
        ArrearConfigobje.setDdo(arrearconfig.getDdo());
        ArrearConfigobje.setHead(arrearconfig.getHead());
        ArrearConfigobje.setOrder(arrearconfig.getOrder());
        ArrearConfigobje.setCreateDate(arrearconfig.getCreateDate());
        ArrearConfigobje.setStatus(ApplicationConstants.DELETE);
        ArrearConfigobje.setUpdateDate(System.currentTimeMillis() + "");
        String ArrearConfigJson = new Gson().toJson(ArrearConfigobje);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_CONFIG_TABLE, rid, ArrearConfigJson);
        return status;
    }

}
