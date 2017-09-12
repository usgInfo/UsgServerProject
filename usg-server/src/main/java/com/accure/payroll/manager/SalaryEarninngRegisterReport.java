/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.hrms.dto.Designation;
import com.accure.payroll.dto.SalarySlipRegisterReport;
import static com.accure.payroll.manager.SalarySlipRegisterReportManager.getDesignation;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;


/**
 *
 * @author user
 */
public class SalaryEarninngRegisterReport {
    
     public ByteArrayOutputStream generatePdfSalaryEarningRegister(String id, String ddo, String month, String year, String path) throws DocumentException, FileNotFoundException, Exception {
        RestClient aql = new RestClient();
        List<SalaryEarninngRegisterReport> salarySlipRegisterList = null;
        ByteArrayOutputStream result = null;
        String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
        int monthInt = 0;
        int yearInt = 0;
        //System.out.println("month" + month);
        if (month != null && month != "" && !month.isEmpty()) {
            monthInt = Integer.parseInt(month);
        }
        if (year != null && year != "" && !year.isEmpty()) {
            yearInt = Integer.parseInt(year);
        }
        //System.out.println(""+id);

        String autoSalaryProcessQuery = "select autosalary._id as idStr,autosalary.departmentName,autosalary.ddoName,autosalary.designationName,autosalary.panNo,autosalary.pfNumber,autosalary.month,autosalary.attendance,autosalary.acnumber,autosalary.year,autosalary.employeeCode,autosalary.employeeCodeM,autosalary.employeeName,autosalary.salaryType,autosalary.designation,autosalary.earningsInfo,autosalary.deductionsInfo,autosalary.attendance,autosalary.earnings,autosalary.deductions,autosalary.isArrear,autosalary.arrear from " + autoSalaryTable + ""
                + " as autosalary where autosalary._id= OID(\"" +id+"\") and autosalary.ddo=\"" + ddo + "\" and autosalary.month= " + monthInt + "  and  autosalary.status=\"Processed\"  and autosalary.year=" + yearInt + "";

        //System.out.println("autoSalaryProcessQuery" + autoSalaryProcessQuery);
        String autosalaryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryProcessQuery);
        //System.out.println("autosalaryOutput" + autosalaryOutput);

        if (autosalaryOutput != null && !autosalaryOutput.isEmpty() && !autosalaryOutput.equals("[]")) {
            salarySlipRegisterList = new Gson().fromJson(autosalaryOutput, new TypeToken<ArrayList<SalarySlipRegisterReport>>() {
            }.getType());
        }

        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
           // salarySlipRegisterList = getDesignation(salarySlipRegisterList);
        }

        if (salarySlipRegisterList != null && salarySlipRegisterList.size() > 0) {
            SalaryEarningRegisterPDFGeneration salaryslipreport = new SalaryEarningRegisterPDFGeneration();

           // result = salaryslipreport.generateSalaryEarningReport(id,ddo,month,year,path);

        }
        return result;
    }
     
   
}
