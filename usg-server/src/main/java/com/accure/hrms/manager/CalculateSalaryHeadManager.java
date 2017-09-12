/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.Formula;
import com.accure.hrms.dto.SalaryHead;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author accure
 */
public class CalculateSalaryHeadManager {

   
    public String getHeadAmount(String headId, String basicId, String basicAmount) throws Exception {
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, headId);
        List<SalaryHead> salryheadList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        SalaryHead fo = salryheadList.get(0);
        String forid = fo.getFormula();
        String forml = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, forid);
        List<Formula> formuList = new Gson().fromJson(forml, new TypeToken<List<Formula>>() {
        }.getType());
        Formula forobj = formuList.get(0);
        String formu = forobj.getHiddenformula();

        List<Integer> liIndexes = new ArrayList<Integer>();
        List<String> idlist = new ArrayList<String>();
        boolean condition = true;
        String str = "";
        //System.out.println(formu);

        while (condition) {
            //System.out.println("********************************************************");
            condition = false;
            char[] chrArray = formu.toCharArray();
            int j = 0;
            for (int i = 0; i < chrArray.length; i++) {
                char c = chrArray[i];
                if (c == '#') {
                    liIndexes.add(i);
                }
            }
            for (Iterator<Integer> it = liIndexes.iterator(); it.hasNext();) {
                Integer integer = it.next();
            }
            for (int k = 0; k < liIndexes.size(); k = k + 2) {
                idlist.add(formu.substring(liIndexes.get(k) + 1, liIndexes.get(k + 1)));
            }
            for (Iterator<String> iterator = idlist.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                if (next.equalsIgnoreCase(basicId)) {
                    //System.out.println(basicId);
                    formu = formu.replaceAll("#" + basicId + "#", basicAmount);
                } else {
                    SalaryHead sal = new Gson().fromJson(new SalaryHeadManager().fetchRawData(next), new TypeToken<SalaryHead>() {
                    }.getType());
                    //System.out.println(sal.getFormula());
                    formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(sal.getFormula()).getHiddenformula());
//                    formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(next).getHiddenformula());
                }
                condition = false;
                char[] chrArrayForCheck = formu.toCharArray();
                for (int i = 0; i < chrArrayForCheck.length; i++) {
                    char c = chrArrayForCheck[i];
                    if (c == '#') {
                        condition = true;
                        break;
                    }
                }
            }
            //System.out.println(formu);
//        int k = ExpressionCalculatorManager.evaluate(str);
        }

        double k = ExpressionCalculatorManager.calculateTheValue(formu);
        //System.out.println(k);
        HashMap<String, Double> headAmount = new HashMap<String, Double>();
        headAmount.put("amount", k);
        return new Gson().toJson(headAmount);
    }

    public static void main(String args[]) throws Exception {
        //System.out.println(new CalculateSalaryHeadManager().getHeadAmount("57ce6e393b04131b3a9db0a6", "57ce69063b04a5094bf1779c", "80000"));
        //System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(new CalculateSalaryHeadManager().getHeadAmount("57ce9e653b047c8e89a771b9", "57ce69063b04a5094bf1779c", "80000"));

    }

}
