/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.finance.dto.DDO;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/**
 *
 * @author Asif
 */
public class GetLoggedInUserManager {

    public String getLoggedInDDOInformation(String userId, String ddoId) throws Exception {
        if (ddoId != null) {
            String ddoJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO, ddoId);
            List<DDO> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DDO>>() {
            }.getType());
            DDO ddo = ddoList.get(0);
            return new Gson().toJson(ddo);
        }
        return null;
    }

}
