/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.LoanNature;
import com.accure.hrms.dto.LoanType;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.LoanApply;
import com.accure.payroll.dto.LoanPayment;
import com.accure.payroll.dto.LoanTransaction;
import static com.accure.payroll.manager.LoanRecoveryManager.getLatestPayment;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author upendra
 */
public class LoanTransactionManager {

    public String saveLoanTransactionData(String Json) throws Exception {

        Type type = new TypeToken<LoanTransaction>() {
        }.getType();
        LoanTransaction bank = new Gson().fromJson(Json, type);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", bank.getDdo());
        conditionMap.put("empCode", bank.getEmpCode());
        conditionMap.put("loanType", bank.getLoanType());
        conditionMap.put("isSuppressed", "Yes");
        conditionMap.put("applyNo", bank.getApplyNo());
        //conditionMap.put("suppressMonth",bank.getSuppressMonth());
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_TRANSACTION_TABLE, conditionMap);
        if (result != null) {
            List<LoanTransaction> categolist = new Gson().fromJson(result, new TypeToken<List<LoanTransaction>>() {
            }.getType());
            for (int i = 0; i < categolist.size(); i++) {
                if (categolist.get(i).getEmpCode() == bank.getEmpCode() || categolist.get(i).getEmpCode().equals(bank.getEmpCode())) {
                    return ApplicationConstants.EXISTED;
                }
            }
        }
//        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
//        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        conditionMap1.put("ddo", bank.getDdo());
//        conditionMap1.put("empcode",bank.getEmpcode());
//        conditionMap1.put("loantype",bank.getLoantype());
//        conditionMap1.put("financialYear",bank.getFinancialYear());
//        conditionMap1.put("suppressMonth",bank.getSuppressMonth());
//        conditionMap1.put("paymentMode","SALARY");
//        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap1);
//        if (result1 != null) 
//        {
//            List<LoanTransaction> categolist = new Gson().fromJson(result, new TypeToken<List<LoanTransaction>>() {
//            }.getType());
//         
//                    return "existed";
//               
//            
//        }
        bank.setCreateDate(System.currentTimeMillis() + "");
        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);

        String loanJson = new Gson().toJson(bank);

        String bankid = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_TRANSACTION_TABLE, loanJson);
        return bankid;
    }

    public String viewLoanTransList(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_TRANSACTION_TABLE, conditionMap);
        List<LoanTransaction> empList = new Gson().fromJson(result, new TypeToken<List<LoanTransaction>>() {
        }.getType());

        for (LoanTransaction cl : empList) {
            try {
                if (cl.getDdo() != null) {
                    String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                    if (gaJson1 != null) {
                        List<DDO> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal1 = gaList1.get(0);
                        cl.setDdo(gal1.getDdoName());
                    }

                }
                if (cl.getCity() != null) {
                    String gaJson2 = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, cl.getCity());
                    if (gaJson2 != null) {
                        List<CityMaster> gaList2 = new Gson().fromJson(gaJson2, new TypeToken<List<CityMaster>>() {
                        }.getType());
                        CityMaster gal2 = gaList2.get(0);
                        cl.setCity(gal2.getCityName());
                    }

                }
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
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList);

    }

    public boolean updateLoanTransaction(LoanTransaction bank, String bankId) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_TRANSACTION_TABLE, bankId);
        List<LoanTransaction> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanTransaction>>() {
        }.getType());
        LoanTransaction relation = relationlist.get(0);
        relation.setCity(bank.getCity());
        relation.setRemarks(bank.getRemarks());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setDated(bank.getDated());
        relation.setSuppressMonth(bank.getSuppressMonth());
        relation.setFinancialYear(bank.getFinancialYear());
        relation.setIsSuppressed(bank.getIsSuppressed());
        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LOAN_TRANSACTION_TABLE, bankId, bankJson);

        return result;
    }

    public boolean deleteloanTransaction(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_TRANSACTION_TABLE, rid);
        List<LoanTransaction> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanTransaction>>() {
        }.getType());
        LoanTransaction relation = relationlist.get(0);
//        LoanTransaction relationobje = new LoanTransaction();
//        relationobje.setDdo(relation.getDdo());
//        relationobje.setAccountNo(relation.getAccountNo());
//        relationobje.setCity(relation.getCity());
//        relationobje.setRemarks(relation.getRemarks());
//        relationobje.setDated(relation.getDated());
//        relationobje.setDepartment(relation.getDepartment());
//        relationobje.setDesignation(relation.getDesignation());
//        relationobje.setEmpcode(relation.getEmpcode());
//        relationobje.setEmpname(relation.getEmpname());
//        relationobje.setLoannature(relation.getLoannature());
//        relationobje.setLoantype(relation.getLoantype());
//        relationobje.setOredro(relation.getOredro());
//        relationobje.setCreateDate(relation.getCreateDate());
        relation.setStatus(ApplicationConstants.DELETE);
        relation.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relation);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOAN_TRANSACTION_TABLE, rid, relationJson);
        return status;
    }

    public String getEmployeeTransDetails(String ddo, String ecode, String loanType, String applyNo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("empCode", ecode);
        conditionMap.put("loanType", loanType);
        conditionMap.put("applyNo", applyNo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap);
        List<LoanPayment> empList = new Gson().fromJson(result, new TypeToken<List<LoanPayment>>() {
        }.getType());
        String allotedDate = "";

        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap1.put("ddo", ddo);
        conditionMap1.put("empCode", ecode);
        conditionMap1.put("loanType", loanType);
        conditionMap1.put("applyNo", applyNo);
        conditionMap1.put("paymentMode", "loanAllotment");
        String resultAllotmentData = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap1);
        List<LoanPayment> resultAllotList = new Gson().fromJson(resultAllotmentData, new TypeToken<List<LoanPayment>>() {
        }.getType());
        if (resultAllotList.size() != 0) {
            allotedDate = resultAllotList.get(0).getDated();
        }
        double val = 0;
        double balanceAmount = 0;
        double balanceInterest = 0;
        double loanAmount = 0;
        double paidAmount = 0;
        double interestpaid = 0;
        conditionMap.put("applyNo", applyNo);
        balanceAmount = empList.get(0).getBalanceAmount();
        loanAmount = empList.get(0).getLoanAmount();
        double instalmentAmount = empList.get(0).getInstallmentAmount();
        double totalNoOfInstal = empList.get(0).getTotalInstallment();
        double interestPercent = empList.get(0).getInterestPercentage();
        double interestAmount = empList.get(0).getInterestAmount();
        String loannature = resultAllotList.get(0).getLoanNature();
        String orderNo = empList.get(0).getOrderNo();
        for (Iterator<LoanPayment> iterator = empList.iterator(); iterator.hasNext();) {
            LoanPayment next = iterator.next();
            if (balanceAmount > next.getBalanceAmount()) {
                balanceAmount = next.getBalanceAmount();
            }
            paidAmount += next.getPaidAmount();
            interestpaid += next.getInterestPaid();
        }
        balanceAmount = loanAmount - paidAmount;
        balanceInterest = interestAmount - interestpaid;

        HashMap<String, String> map = new HashMap<String, String>();
        double balNoOfInstalments = balanceAmount / instalmentAmount;
        int balNoOfInstalment = (int) Math.ceil(balNoOfInstalments);
        String DdoName = "";
        String loanTypeName = "";
        String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddo);
        if (gaJson1 != null) {
            List<DDO> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<DDO>>() {
            }.getType());
            DDO gal1 = gaList1.get(0);
            DdoName = gal1.getDdoName();
        }

        String loanTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, loanType);
        if (loanTypeJson != null) {
            List<SalaryHead> gaList1 = new Gson().fromJson(loanTypeJson, new TypeToken<List<SalaryHead>>() {
            }.getType());
            SalaryHead gal1 = gaList1.get(0);
            loanTypeName = gal1.getShortDescription();
        }
        map.put("ddo", DdoName);
        map.put("empcode", ecode);
        map.put("loantype", loanTypeName);
        map.put("balanceAmount", String.valueOf(balanceAmount));
        map.put("paidAmount", String.valueOf(paidAmount));
        map.put("loanAmount", String.valueOf(loanAmount));
        map.put("totalNoOfInstalments", String.valueOf(totalNoOfInstal));
        map.put("balNoOfInstalments", String.valueOf(balNoOfInstalment));
        map.put("balanceInterest", String.valueOf(balanceInterest));
        map.put("interestAmount", String.valueOf(interestAmount));
        map.put("interestPercent", String.valueOf(interestPercent));
        map.put("interestpaid", String.valueOf(interestpaid));
        map.put("instalmentAmount", String.valueOf(instalmentAmount));
        map.put("loannature", loannature);
        map.put("orderNo", orderNo);
        map.put("allotedDate", allotedDate);
        map.put("applyNo", applyNo);

        return new Gson().toJson(map);
    }

    public String getEmployeeLoanDetails(String ddo, String empCode, String loanType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("empCode", empCode);
        conditionMap.put("loanType", loanType);
        conditionMap.put("isLoanAmountPaid", ApplicationConstants.FALSE);
        conditionMap.put("isAllotted", ApplicationConstants.TRUE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMap);
        if (result != null) {
            List<LoanApply> gaList1 = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
            }.getType());
            LoanApply gal1 = gaList1.get(0);
            return new Gson().toJson(gal1);
        }
        return null;

    }

    public String getEmployeeLoanTransDetails(String ddo, String empCode, String loanType, String financialYear) throws Exception {
        HashMap<String, String> conditionMaps = new HashMap<String, String>();
        conditionMaps.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMaps.put("ddo", ddo);
        conditionMaps.put("empCode", empCode);
        conditionMaps.put("loanType", loanType);
        conditionMaps.put("financialYear", financialYear);
        conditionMaps.put("isAllotted", ApplicationConstants.TRUE);
        ArrayList list = new ArrayList();
        HashMap<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, conditionMaps);
        if (result != null) {
            List<LoanApply> gaList1 = new Gson().fromJson(result, new TypeToken<List<LoanApply>>() {
            }.getType());
            LoanApply gal1 = gaList1.get(0);
            //  for(LoanApply l1:gaList1)
            //{

            String applyNo = gal1.getApplyNo();
            String isAmountPaid = gal1.getIsLoanAmountPaid();
            map.put("applyNo", applyNo);
            map.put("isAmountPaid", isAmountPaid);
            if (!applyNo.equals("")) {
                HashMap<String, String> conditionMap = new HashMap<String, String>();
                conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                conditionMap.put("ddo", ddo);
                conditionMap.put("empCode", empCode);
                conditionMap.put("loanType", loanType);
                conditionMap.put("applyNo", applyNo);
                String paymentresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap);
                List<LoanPayment> empList = new Gson().fromJson(paymentresult, new TypeToken<List<LoanPayment>>() {
                }.getType());
                String allotedDate = "";

                HashMap<String, String> conditionMap1 = new HashMap<String, String>();
                conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                conditionMap1.put("ddo", ddo);
                conditionMap1.put("empCode", empCode);
                conditionMap1.put("loanType", loanType);
                conditionMap1.put("applyNo", applyNo);
                conditionMap1.put("paymentMode", "loanAllotment");
                String resultAllotmentData = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap);
                List<LoanPayment> resultAllotList = new Gson().fromJson(resultAllotmentData, new TypeToken<List<LoanPayment>>() {
                }.getType());
                if (resultAllotList.size() != 0) {
                    allotedDate = resultAllotList.get(0).getDated();
                }
                double val = 0;
                double balanceAmount = 0.0;
                double balanceInterest = 0.0;
                double loanAmount = 0.0;
                double paidAmount = 0.0;
                double cashRecoveryAmount = 0.0;
                double interestpaid = 0.0;
                double interestAmount = 0.0;
                conditionMap.put("applyNo", applyNo);
                LoanPayment lp = getLatestPayment(empCode, applyNo, loanType);
                balanceAmount = empList.get(0).getBalanceAmount();
                loanAmount = empList.get(0).getLoanAmount();
                double instalmentAmount = empList.get(0).getInstallmentAmount();
                double totalNoOfInstal = empList.get(0).getTotalInstallment();
                double interestPercent = empList.get(0).getInterestPercentage();
                interestAmount = empList.get(0).getInterestAmount();
                String loannature = empList.get(0).getLoanNature();
                String orderNo = empList.get(0).getOrderNo();

                if (lp != null) {
                    interestAmount = lp.getInterestAmount();
                    balanceInterest = lp.getBalanceInterest();
                    interestpaid = lp.getInterestAmount() - lp.getBalanceInterest();
                }
                for (Iterator<LoanPayment> iterator = empList.iterator(); iterator.hasNext();) {
                    LoanPayment next = iterator.next();
                    if (balanceAmount > next.getBalanceAmount()) {
                        balanceAmount = next.getBalanceAmount();
                    }
                    paidAmount += next.getPaidAmount();

                }
                HashMap<String, String> conditionMp = new HashMap<String, String>();
                conditionMp.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                conditionMp.put("ddo", ddo);
                conditionMp.put("empCode", empCode);
                conditionMp.put("loanType", loanType);
                conditionMp.put("applyNo", applyNo);
                conditionMp.put("paymentMode", "LoanRecovery");
                String resultDataaa = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMp);
                List<LoanPayment> resultPayRecList = new Gson().fromJson(resultDataaa, new TypeToken<List<LoanPayment>>() {
                }.getType());
                if (resultDataaa != null) {
                    for (LoanPayment loanPayment : resultPayRecList) {

                        cashRecoveryAmount += loanPayment.getPaidAmount();
                    }
                }
                //System.out.println("-------" + cashRecoveryAmount);
                double balNoOfInstalments = 0.0;
                double balNoOfInstalment = 0.0;
                if (loannature.equals("Refundable")) {
                    balNoOfInstalments = balanceAmount / instalmentAmount;
                    balNoOfInstalment = (double) Math.ceil(balNoOfInstalments);
                    balanceAmount = loanAmount - paidAmount;
                } else {
                    balanceAmount = 0.0;
                }

                //HashMap<String, String> map = new HashMap<String, String>();
                String DdoName = "";
                String loanTypeName = "";
                String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddo);
                if (gaJson1 != null) {
                    List<DDO> gaList2 = new Gson().fromJson(gaJson1, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO gal2 = gaList2.get(0);
                    DdoName = gal2.getDdoName();
                }

                String loanTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, loanType);
                if (loanTypeJson != null) {
                    List<SalaryHead> gaList3 = new Gson().fromJson(loanTypeJson, new TypeToken<List<SalaryHead>>() {
                    }.getType());
                    SalaryHead gal3 = gaList3.get(0);
                    loanTypeName = gal3.getShortDescription();
                }
                map.put("ddo", DdoName);
                map.put("empcode", empCode);
                map.put("loantype", loanTypeName);
                map.put("balanceAmount", String.valueOf(balanceAmount));
                map.put("paidAmount", String.valueOf(paidAmount));
                map.put("loanAmount", String.valueOf(loanAmount));
                map.put("totalNoOfInstalments", String.valueOf(totalNoOfInstal));
                map.put("balNoOfInstalments", String.valueOf(balNoOfInstalment));
                map.put("balanceInterest", String.valueOf(balanceInterest));
                map.put("interestAmount", String.valueOf(interestAmount));
                map.put("interestPercent", String.valueOf(interestPercent));
                map.put("interestpaid", String.valueOf(interestpaid));
                map.put("instalmentAmount", String.valueOf(instalmentAmount));
                map.put("loannature", loannature);
                map.put("orderNo", orderNo);
                map.put("allotedDate", allotedDate);
                map.put("cashRecoveryAmount", String.valueOf(cashRecoveryAmount));
                map.put("applyNo", applyNo);
            }
            // list.add(map);

            //}
            return new Gson().toJson(map);
        }
        return null;

    }
}
