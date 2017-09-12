/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.payroll.dto.SalarySlipRegisterReport;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class GenerateSalarySlipMailManager {

    public HashMap employeeSalarySlipMail(String EmployeeCode, String ddo, String month, String year, String path,String financialyearstart,String financialyearEnd) throws Exception {

         HashMap outputMap = new HashMap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<SalarySlipRegisterReport> salarySlipRegisterList = null;
        String[] employeeCode = null;
        JSONArray arr = null;

        if (EmployeeCode != null && !EmployeeCode.isEmpty()) {
            arr = new JSONArray(EmployeeCode);
            employeeCode = new String[arr.length()];
        }
        if (employeeCode.length > 0 && employeeCode != null) {
            for (int i = 0; i < arr.length(); i++) {
              
                JSONObject obj = arr.getJSONObject(i);
                employeeCode[i] = (String) obj.get("employeeCode");
                HashMap conditionMap = new HashMap();
                conditionMap.put("ddo",ddo);
                conditionMap.put("employeeCode", employeeCode[i]);
                conditionMap.put("month", Integer.parseInt(month));
                conditionMap.put("year", Integer.parseInt(year));
//                conditionMap.put("isLocked",true);
                //System.out.println("conditionMap"+conditionMap);

                String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, conditionMap);
                //System.out.println("result" + result);
                if (result != null && result != "" && !result.isEmpty()) {
                    salarySlipRegisterList = new Gson().fromJson(result, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
                    }.getType());
                }

                if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
                    SalarySlipRportPDFGeneration salaryslipreport = new SalarySlipRportPDFGeneration();

                    bos = salaryslipreport.generateSalaryReport(salarySlipRegisterList, path,financialyearstart,financialyearEnd);
                        SendEmail se = new SendEmail();
                       
                        boolean isFlag=se.send(bos, salarySlipRegisterList);
                        if(isFlag){
                              outputMap.put(employeeCode[i], "success");
                               
                        }else{
                              outputMap.put(employeeCode[i], "faliure");
                        }
                        

                    
                }
            }
        }
        return outputMap;

    }
}
