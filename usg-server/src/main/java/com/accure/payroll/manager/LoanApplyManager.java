/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.manager.ChangeFinancialYearManager;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.FinancialYear;
import com.accure.payroll.dto.LoanApply;
import com.accure.hrms.dto.LoanType;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.LoanAllotment;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author upendra
 * @author Misha
 */
public class LoanApplyManager {

    public String saveLoanApplyData(LoanApply loanJson, String currentFinancialYear) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", loanJson.getDdo());
        conditionMap.put("empCode", loanJson.getEmpCode());
        conditionMap.put("loanType", loanJson.getLoanType());
        conditionMap.put("isLoanAmountPaid", ApplicationConstants.FALSE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
        if (result != null) {
            return "existed";
        } else {
            String dates[] = currentFinancialYear.split("~");
            String fromDate = dates[0];
            String toDate = dates[1];
            String financialYearJson = new ChangeFinancialYearManager().fetchFinancialYear(fromDate, toDate);
            List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear fyObj = fyList.get(0);
            String fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
            //System.out.println("----------------fyId-------" + fyId);
            if (!isValidForLoanApply(loanJson, fyId)) {
                loanJson.setCreateDate(System.currentTimeMillis() + "");
                loanJson.setUpdateDate(System.currentTimeMillis() + "");
                loanJson.setStatus(ApplicationConstants.ACTIVE);
                loanJson.setFinancialYear(fyId);
                loanJson.setIsLoanAmountPaid(ApplicationConstants.FALSE);
                loanJson.setIsAllotted(ApplicationConstants.FALSE);
                String loanType = loanJson.getLoanType();
                String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, loanType);
                if (existrelationJson != null) {
                    List<SalaryHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<SalaryHead>>() {
                    }.getType());
                    SalaryHead relation = relationlist.get(0);
                    String LoanName = relation.getShortDescription();
                    loanJson.setLoanName(LoanName);
                }
                String loanfinalJson = new Gson().toJson(loanJson);
                String res = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_APPLY_TABLE, loanfinalJson);
                return res;
            } else {
                return null;
            }
        }
    }

    public static boolean isValidForLoanApply(LoanApply loanApply, String fyId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", loanApply.getDdo());
        conditionMap.put("empCode", loanApply.getEmpCode());
        conditionMap.put("loanType", loanApply.getLoanType());
        conditionMap.put("financialYear", fyId);
        String resString = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
        if (resString == null) {
            return false;
        } else {
            return true;
        }
    }

    public String viewLoanApplyList(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
         conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
        List<LoanApply> loanApplyResult = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
        }.getType());
        for (LoanApply cl : loanApplyResult) {

            if (cl.getLoanType() != null) {
                try {
                    cl.setLoanTypeId(cl.getLoanType());
                    String loanTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, cl.getLoanType());
                    if (loanTypeJson != null) {
                        List<SalaryHead> gaList1 = new Gson().fromJson(loanTypeJson, new TypeToken<List<SalaryHead>>() {
                        }.getType());
                        SalaryHead gal1 = gaList1.get(0);
                        cl.setLoanType(gal1.getShortDescription());
                    }
                } catch (Exception e) {
                }
            }
            if (cl.getDdo() != null) {
                try {
                    String getDDOList = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                    if (getDDOList != null) {
                        List<DDO> getDDO = new Gson().fromJson(getDDOList, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal1 = getDDO.get(0);
                        cl.setDdo(gal1.getDdoName());
                    }
                } catch (Exception e) {
                }
            }
        }
        return new Gson().toJson(loanApplyResult);

    }

    public boolean updateLoanApply(LoanApply bank, String bankId) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_APPLY_TABLE, bankId);
        List<LoanApply> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanApply>>() {
        }.getType());
        LoanApply relation = relationlist.get(0);
        relation.setAmount(bank.getAmount());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setDated(bank.getDated());
        relation.setRemarks(bank.getRemarks());
        relation.setLoanType(bank.getLoanType());
        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LOAN_APPLY_TABLE, bankId, bankJson);

        return result;
    }

    public boolean deleteLoanApply(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_APPLY_TABLE, rid);
        List<LoanApply> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanApply>>() {
        }.getType());
        LoanApply relation = relationlist.get(0);
        //LoanApply relationobje = new LoanApply();
//        relationobje.setAmount(relation.getAmount());
//       // relationobje.setDdo(relation.getDdo());
//        relationobje.setLoanType(relation.getLoanType());
//        //relationobje.setApplyNo(relation.getApplyNo());
//        relationobje.setRemarks(relation.getRemarks());
//        relationobje.setDated(relation.getDated());
//       // relationobje.setDepartment(relation.getDepartment());
//       // relationobje.setDesignation(relation.getDesignation());
//      //  relationobje.setEmpCode(relation.getEmpCode());
//        //relationobje.setEmpName(relation.getEmpName());
//       // relationobje.setPfType(relation.getPfType());
//       // relationobje.setCreateDate(relation.getCreateDate());
        relation.setStatus(ApplicationConstants.DELETE);
        relation.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relation);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOAN_APPLY_TABLE, rid, relationJson);
        return status;
    }

    public String getEmpCodes(String ddo, String loantype, String scrId, String currentFinancialYear) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("loanType", loantype);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        ArrayList l1st = new ArrayList();
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("ddo", ddo);
        map1.put("loanType", loantype);
        // map1.put("isLoanAmountPaid", ApplicationConstants.FALSE);
        map1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = "";
        String fyId = "";

//         String dates[]=currentFinancialYear.split("~");
//            String fromDate=dates[0];
//             String toDate=dates[1];
//              String financialYearJson = new ChangeFinancialYearManager().fetchFinancialYear(fromDate,toDate);
//            List<FinancialYear> fyList = new Gson().fromJson(financialYearJson, new TypeToken<List<FinancialYear>>() {
//            }.getType());
//            FinancialYear fyObj = fyList.get(0);
//             fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
        if (scrId.equals("allotment")) {
            conditionMap.put("isAllotted", ApplicationConstants.FALSE);
            if (fyId != "" && fyId != null) {
                conditionMap.put("financialYear", currentFinancialYear);
            }
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
            if (result != "" || result != null) {

                List<LoanApply> empList = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
                }.getType());
                if (empList.size() > 0) {
                    for (LoanApply cl : empList) {
                        String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                        List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal = gaList.get(0);
                        cl.setDdo(gal.getDdoName());
                    }
                    return new Gson().toJson(empList);
                }
            }
        } else if (scrId.equals("recovery")) {
            map1.put("isLoanAmountPaid", ApplicationConstants.FALSE);

            if (fyId != "" && fyId != null) {
                map1.put("financialYear", fyId);
            }
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, map1);
            if ((!result.equals("")) || (!result.equals(null))) {
                List<LoanAllotment> empList = new Gson().fromJson(result, new TypeToken<List<LoanAllotment>>() {
                }.getType());
                if (empList.size() > 0) {
                    for (LoanAllotment cl : empList) {
                        String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                        List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal = gaList.get(0);
                        cl.setDdo(gal.getDdoName());
                    }
                    return new Gson().toJson(empList);
                }
            }

        } else if (scrId.equals("transactions")) {
            if (fyId != "" && fyId != null) {
                map1.put("financialYear", fyId);
            }
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, map1);
            //System.out.println("-----------" + result);
            if ((!result.equals("")) || (!result.equals(null))) {
                List<LoanAllotment> empList = new Gson().fromJson(result, new TypeToken<List<LoanAllotment>>() {
                }.getType());
                HashMap<String, LoanAllotment> map2 = new HashMap<String, LoanAllotment>();
                if (empList.size() > 0) {
                    for (LoanAllotment cl : empList) {

                        String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                        List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal = gaList.get(0);
                        cl.setDdo(gal.getDdoName());
                        map2.put(cl.getEmpCode(), cl);

                        // map2.put(cl.getEmpname(),cl.getEmpname());
                    }
                    l1st.addAll(map2.values());
//                  HashMap <String,String> map2=new HashMap<String, String>();
//                if (empList.size() > 0) {
//                    for (LoanAllotment cl : empList) {
//                          
//                        String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
//                        List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
//                        }.getType());
//                        DDO gal = gaList.get(0);
//                        cl.setDdo(gal.getDdoName());
//                      map2.put(cl.getEmpcode(),cl.getEmpname());
//                      // map2.put(cl.getEmpname(),cl.getEmpname());
//                       l1.add(map2);
//                    }

                    return new Gson().toJson(l1st);
                }
            }

        }
//        List<LoanApply> empList = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
//        }.getType());
//        for (LoanApply cl : empList) {
//            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
//            List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
//            }.getType());
//            DDO gal = gaList.get(0);
//            cl.setDdo(gal.getDdoName());
//        }

        return "";
    }

    public String getEmpCodesForRecovryServce(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);

        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
        List<LoanApply> empList = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
        }.getType());
        for (LoanApply cl : empList) {
            String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
            List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
            }.getType());
            DDO gal = gaList.get(0);
            cl.setDdo(gal.getDdoName());
        }
        return new Gson().toJson(empList);

    }

    public String fetchAllDocuments() throws Exception {

        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOAN_APPLY_TABLE);
        return result1;
    }

    public String getApplyNumber() throws Exception {
        String result = new LoanApplyManager().fetchAllDocuments();
        List<LoanApply> loanApplyList = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
        }.getType());
        int numb = 0;
        LoanApply pv = new LoanApply();
        if (loanApplyList != null) {
            for (Iterator<LoanApply> iterator = loanApplyList.iterator(); iterator.hasNext();) {
                LoanApply next = iterator.next();
                int len = next.getApplyNo().length();
                int value = Integer.parseInt(next.getApplyNo().substring(9, len));

                if (numb < value) {
                    numb = value;
                }
            }
        }
        numb++;
        String pattern = "LO-Y1-Y2-" + Integer.toString(numb);
        pv.setApplyNo(pattern);
        List<LoanApply> li = new ArrayList<LoanApply>();
        li.add(pv);
        return new Gson().toJson(li);
    }

    boolean setLoanPaidStatus(String ddo, String empCode, String applyNo, String isLoanAmountPaid, String loanType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        conditionMap.put("empCode", empCode);
        conditionMap.put("ddo", ddo);
        conditionMap.put("applyNo", applyNo);
        conditionMap.put("loanType", loanType);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
        String payresult1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, conditionMap);

        List<LoanApply> relationlist = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
        }.getType());
        List<LoanAllotment> allotRelationlist = new Gson().fromJson(payresult1, new TypeToken<List<LoanAllotment>>() {
        }.getType());

        LoanApply relation = relationlist.get(0);
        LoanAllotment allotRelation = allotRelationlist.get(0);

        String id = ((LinkedTreeMap<String, String>) relation.getId()).get("$oid");
        String allotid = ((LinkedTreeMap<String, String>) allotRelation.getId()).get("$oid");
        relation.setIsLoanAmountPaid(isLoanAmountPaid);
        allotRelation.setIsLoanAmountPaid(isLoanAmountPaid);
        String bankJson = new Gson().toJson(relation);
        String allotJson = new Gson().toJson(allotRelation);
        boolean result1 = DBManager.getDbConnection().update(ApplicationConstants.LOAN_APPLY_TABLE, id, bankJson);
        boolean result2 = DBManager.getDbConnection().update(ApplicationConstants.LOAN_ALLOTMENT_TABLE, id, allotJson);

        return result1;

    }

    public String getEmpCodesForLoan(String ddo, String toFinYear) throws Exception {
        String output = null;
        if (ddo == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("EmployeeLeftStatus", ApplicationConstants.NO);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
        if (result != null) {
            ArrayList<Employee> empList = new Gson().fromJson(result, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            ArrayList<Employee> newEmpList = new ArrayList<Employee>();
            SimpleDateFormat sdf = new SimpleDateFormat(ApplicationConstants.DATE_FORMAT);
            Date date1 = sdf.parse(toFinYear);
            for (Employee cl : empList) {
                String dateOfJoin = cl.getDateOfJoining();
                if ((!dateOfJoin.equals(null)) && (!dateOfJoin.equals(""))) {
                    Date date2 = sdf.parse(dateOfJoin);
                    if (date2.compareTo(date1) > 0) {
                        //System.out.println("Date2 is after Date2");
                        // empList.remove(cl);
                    } else {
                        newEmpList.add(cl);
                    }
                }
            }
            for (Employee cl : newEmpList) {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                if (gaJson != null) {
                    List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO gal = gaList.get(0);
                    cl.setDdo(gal.getDdoName());
                }
            }
            output = new Gson().toJson(newEmpList);
        }
        return output;
    }
}
