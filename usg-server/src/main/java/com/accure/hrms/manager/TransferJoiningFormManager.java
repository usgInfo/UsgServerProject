/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;


import com.accure.hrms.dto.EmployeeTransferJoiningForm;
import com.accure.usg.common.manager.DBManager;

import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;

/**
 *
 * @author user
 */
public class TransferJoiningFormManager {

    public String save(EmployeeTransferJoiningForm obj) throws Exception {

        obj.setCreateDate(System.currentTimeMillis() + "");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        String objJson = new Gson().toJson(obj);
        String objId = DBManager.getDbConnection().insert(ApplicationConstants.EMP_TRANSFER_JOINING_TABLE, objJson);
        return objId;
    }
      


}
