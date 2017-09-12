/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.pension.dto.PensionEmployee;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author user
 */
public class ManagePensionArrearAdjustmentManager {

    public String fetchPensionHeadByHeadName(String month, String Year,String obj) throws Exception {
        
     HashMap<String, String> conditionMap = new HashMap<String, String>();
        if (month.equalsIgnoreCase("Jan")) {
           month="01";
        }
        else if(month.equalsIgnoreCase("Feb")){
             month="02";
        }
         else if(month.equalsIgnoreCase("Mar")){
             month="03";
        }
         else if(month.equalsIgnoreCase("Apr")){
             month="04";
        }
         else if(month.equalsIgnoreCase("May")){
             month="05";
        }
         else if(month.equalsIgnoreCase("June")){
             month="06";
        }
         else if(month.equalsIgnoreCase("July")){
             month="07";
        }
         else if(month.equalsIgnoreCase("Aug")){
             month="08";
        }
         else if(month.equalsIgnoreCase("Sep")){
             month="09";
        }
         else if(month.equalsIgnoreCase("Oct")){
             month="10";
        }
         else if(month.equalsIgnoreCase("Nov")){
             month="11";
        }
         else if(month.equalsIgnoreCase("Dec")){
             month="12";
        }
         conditionMap.put("QSYear", Year);
        conditionMap.put("QSMonth", month);
        conditionMap.put("employeecode", obj);
        //System.out.println(conditionMap);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);

        List<PensionEmployee> empList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());

        return result;
    }
    
    public String fetchPensionHeadByHeadName(String month, String Year) throws Exception {
        
     HashMap<String, String> conditionMap = new HashMap<String, String>();
        if (month.equalsIgnoreCase("Jan")) {
           month="01";
        }
        else if(month.equalsIgnoreCase("Feb")){
             month="02";
        }
         else if(month.equalsIgnoreCase("Mar")){
             month="03";
        }
         else if(month.equalsIgnoreCase("Apr")){
             month="04";
        }
         else if(month.equalsIgnoreCase("May")){
             month="05";
        }
         else if(month.equalsIgnoreCase("June")){
             month="06";
        }
         else if(month.equalsIgnoreCase("July")){
             month="07";
        }
         else if(month.equalsIgnoreCase("Aug")){
             month="08";
        }
         else if(month.equalsIgnoreCase("Sep")){
             month="09";
        }
         else if(month.equalsIgnoreCase("Oct")){
             month="10";
        }
         else if(month.equalsIgnoreCase("Nov")){
             month="11";
        }
         else if(month.equalsIgnoreCase("Dec")){
             month="12";
        }
         conditionMap.put("QSYear", Year);
        conditionMap.put("QSMonth", month);
       // conditionMap.put("employeecode", obj);
        //System.out.println(conditionMap);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_EMPLOYEE_TABLE, conditionMap);

        List<PensionEmployee> empList = new Gson().fromJson(result, new TypeToken<List<PensionEmployee>>() {
        }.getType());

        return result;
    }
    public static void main(String[] args) throws Exception{
     String m=new ManagePensionArrearAdjustmentManager().fetchPensionHeadByHeadName("Dec","2015","1234");
    System.out.print(m);
    }
}
