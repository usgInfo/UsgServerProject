/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.payroll.manager;

import com.accure.payroll.dto.PayStopedSalary;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 *
 * @author upendra
 */
public class PayStopedSalaryManager {
      public String save(String salaryData) throws Exception {
        Type type = new TypeToken<PayStopedSalary>() {
        }.getType();
        PayStopedSalary paystopedDTO = new Gson().fromJson(salaryData, type);
        paystopedDTO.setCreateDate(System.currentTimeMillis() + "");
        paystopedDTO.setUpdateDate(System.currentTimeMillis() + "");
        paystopedDTO.setStatus(ApplicationConstants.ACTIVE);

        String paystopedJson = new Gson().toJson(paystopedDTO);

        String paystopedJsonresult = DBManager.getDbConnection().insert(ApplicationConstants.PAY_STOPED_SALARY_TABLE, paystopedJson);
        return paystopedJsonresult;

    }
}
