/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.usg.common.manager.DBManager;
import com.accure.pension.dto.PensionEmployee;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
//import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONObject;

/**
 * @author ankur
 */
public class PensionReportBankListManager {
    
    public ByteArrayOutputStream generatePensionBankListPdf(String bankName, String empCode, String month, String year, String path) throws DocumentException, FileNotFoundException, Exception {
        RestClient aql = new RestClient();
        HashMap<String, String> hMap = new HashMap<String, String>();
        List<PensionEmployee> pensionEmployeeList = null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int monthInt = 0;
        int yearInt = 0;
        //System.out.println("month" + month);
        if (month != null && month != "" && !month.isEmpty()) {
            monthInt = Integer.parseInt(month);
        }
        if (year != null && year != "" && !year.isEmpty()) {
            yearInt = Integer.parseInt(year);
        }
        //System.out.println("" + id);
        
        hMap.put(ApplicationConstants.BANK_TABLE, bankName);
         
        String bankjson = new PensionBankManager().fetch(bankName);
        JSONObject json = new JSONObject(bankjson);
        String bName = json.getString("bankName");
        String pensionEmployOutput = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, hMap);
       
        if (pensionEmployOutput != null && !pensionEmployOutput.isEmpty() && !pensionEmployOutput.equals("[]")) {
           pensionEmployeeList = new Gson().fromJson(pensionEmployOutput, new TypeToken<ArrayList<PensionEmployee>>() {
           }.getType());
       }

        if (pensionEmployeeList != null && pensionEmployeeList.size() > 0) {
//            pensionEmployeeList = getDesignation(pensionEmployeeList);
//        }
//
//        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
//            SalarySlipRportPDFGeneration salaryslipreport = new SalarySlipRportPDFGeneration();
            PensionReportBankListPDFGeneration prpb= new PensionReportBankListPDFGeneration();

            result = prpb.generatePensionBankListReport(pensionEmployeeList, path, month, year, bName);

        }
        return result;
     

        //return result;
}
}
