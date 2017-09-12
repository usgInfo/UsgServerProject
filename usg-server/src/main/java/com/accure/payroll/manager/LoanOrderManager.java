/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.LoanNature;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.LoanOrder;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author upendra
 */
public class LoanOrderManager {

    public String saveLoanOrderData(LoanOrder bank, String ddo, String loanType, String orderNo) throws Exception {

        bank.setCreateDate(System.currentTimeMillis() + "");
        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);

        String loanJson = new Gson().toJson(bank);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
          HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("loanType", loanType);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, conditionMap);
        if (result == null)
        {
            
            conditionMap1.put("orderNo", orderNo);
            conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String resul2 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, conditionMap1);
             if (resul2 == null)
            {
            String bankid = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_ORDER_TABLE, loanJson);
            return bankid;
            }
            else
            {
                return ApplicationConstants.EXISTED;
            }
        }
        else
        {
            return ApplicationConstants.EXISTED;
        }
    }

    public String viewLoanOrderList(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo",ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, conditionMap);
        List<LoanOrder> empList = new Gson().fromJson(result, new TypeToken<List<LoanOrder>>() {
        }.getType());
        for (LoanOrder cl : empList) {
            if (cl.getLoanType() != null) {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_NATURE_TABLE, cl.getLoanType());
                if (gaJson != null) {
                    List<LoanNature> gaList = new Gson().fromJson(gaJson, new TypeToken<List<LoanNature>>() {
                    }.getType());
                    LoanNature gal = gaList.get(0);
                    cl.setLoanType(gal.getLoanName());
                }
            }
            if (cl.getLoanType()!= null) {
                String gaJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, cl.getLoanType());
                if (gaJson2 != null) {
                    List<SalaryHead> gaList2 = new Gson().fromJson(gaJson2, new TypeToken<List<SalaryHead>>() {
                    }.getType());
                    SalaryHead gal2 = gaList2.get(0);
                    cl.setLoanType(gal2.getShortDescription());
                }
            }
            if (cl.getDdo() != null) {
                cl.setDdoId(cl.getDdo());
                String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                if (gaJson1 != null) {
                    List<DDO> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO gal1 = gaList1.get(0);
                    cl.setDdo(gal1.getDdoName());
                }
            }
        }
        return new Gson().toJson(empList);

    }

    public String updateLoanOrder(LoanOrder bank, String bankId) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_ORDER_TABLE, bankId);
        List<LoanOrder> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanOrder>>() {
        }.getType());
        LoanOrder relation = relationlist.get(0);
        String orderNo=bank.getOrderNo();
        String ddo=bank.getDdo();
        String loanType=bank.getLoanType();
                
         HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        //conditionMap.put("ddo", ddo);
        conditionMap.put("orderNo", orderNo);
        //conditionMap.put("loanType", loanType);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, conditionMap);
        if(result1 ==null)
        {
        bank.setCreateDate(relation.getCreateDate());
        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(bank);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LOAN_ORDER_TABLE, bankId, bankJson);
        if(result) 
        {
            return bankId;
        }
        }
         else
        {
             return ApplicationConstants.EXISTED;
        }
         return bankId;
    }
    public String deleteLoanOrder(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_ORDER_TABLE, rid);
        List<LoanOrder> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanOrder>>() {
        }.getType());
        LoanOrder relation = relationlist.get(0);
        LoanOrder relationobje = new LoanOrder();
        relationobje.setComptroller(relation.getComptroller());
        relationobje.setDdo(relation.getDdo());
        relationobje.setLoanType(relation.getLoanType());
        relationobje.setOrderNo(relation.getOrderNo());
        relationobje.setRemarks(relation.getRemarks());
        relationobje.setSanctionedBy(relation.getSanctionedBy());
        relationobje.setCreateDate(relation.getCreateDate());
        relationobje.setStatus(ApplicationConstants.DELETE);
        relationobje.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relationobje);
          String status = "";
        if (DeleteDependencyManager.hasDependency(ApplicationConstants.LOAN_ALLOTMENT_TABLE, "orderNo", relation.getOrderNo())) {
            status = ApplicationConstants.DELETE_MESSAGE;
        }else
        {
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.LOAN_ORDER_TABLE, rid, relationJson);
         if (flag)
                {
                        status = ApplicationConstants.SUCCESS;
                } 
            else 
                {
                        status = ApplicationConstants.FAIL;
                }
        }
        return status;
    }

    public String getLoanTypeNoBasedOnDdo(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String resultList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, conditionMap);

        if (resultList != "") {
            List<LoanOrder> list = new Gson().fromJson(resultList, new TypeToken<List<LoanOrder>>() {
            }.getType());
            return resultList;
        }
        return new Gson().toJson(resultList);
    }
     public String getSanctionedByDesignationid() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("designation", "Finance Officer");
        String resultList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION, conditionMap);
         //System.out.println(resultList);

        if (resultList != "") {
            List<Designation> list = new Gson().fromJson(resultList, new TypeToken<List<Designation>>() {
            }.getType());
            Designation list1=list.get(0);
            String id=((LinkedTreeMap<String, String>) list1.getId()).get("$oid");
            return id.toString();
        }
        return new Gson().toJson(resultList);
    }
     
     public static void main(String[] args) throws Exception {
        new LoanOrderManager().getSanctionedByDesignationid();
    }
     
     public String getSanctionedEmployees(String ddo) throws Exception {
         String designationId=new LoanOrderManager().getSanctionedByDesignationid();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("designation", designationId);
        String resultList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        if (resultList != "") {
            List<Employee> list = new Gson().fromJson(resultList, new TypeToken<List<Employee>>() {
            }.getType());
            return resultList;
        }
        return new Gson().toJson(resultList);
    }
     public String getComptrollerDesignationid() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("designation", "Comptroller");
        String resultList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION, conditionMap);
         //System.out.println(resultList);

        if (resultList != "") {
            List<Designation> list = new Gson().fromJson(resultList, new TypeToken<List<Designation>>() {
            }.getType());
            Designation list1=list.get(0);
            String id=((LinkedTreeMap<String, String>) list1.getId()).get("$oid");
            return id.toString();
        }
        return new Gson().toJson(resultList);
    }  

          public String getComptrollerEmployees(String ddo) throws Exception {
         String designationId=new LoanOrderManager().getComptrollerDesignationid();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("designation", designationId);
        String resultList = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        if (resultList != "") {
            List<Employee> list = new Gson().fromJson(resultList, new TypeToken<List<Employee>>() {
            }.getType());
            return resultList;
        }
        return new Gson().toJson(resultList);
    }
}
