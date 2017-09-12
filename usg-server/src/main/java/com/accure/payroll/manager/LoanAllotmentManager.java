/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.payroll.dto.LoanAllotment;
import com.accure.hrms.dto.LoanNature;
import com.accure.hrms.dto.SalaryHead;
import com.accure.payroll.dto.LoanApply;
import com.accure.payroll.dto.LoanOrder;
import com.accure.payroll.dto.LoanPayment;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author upendra
 */
public class LoanAllotmentManager {

    public String saveLoanAllotData(String json) throws Exception {
        Type type = new TypeToken<LoanAllotment>() {
        }.getType();
        Type type1 = new TypeToken<LoanPayment>() {
        }.getType();
        LoanAllotment bank = new Gson().fromJson(json, type);
        LoanPayment loanpymentData = new Gson().fromJson(json, type1);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", bank.getDdo());
        conditionMap.put("empCode", bank.getEmpCode());
        conditionMap.put("loanType", bank.getLoanType());
        conditionMap.put("isAllotted", ApplicationConstants.TRUE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, conditionMap);
        if (result != null) {
            List<LoanAllotment> categolist = new Gson().fromJson(result, new TypeToken<List<LoanAllotment>>() {
            }.getType());
            for (int i = 0; i < categolist.size(); i++) {
                if (categolist.get(i).getEmpCode() == bank.getEmpCode() || categolist.get(i).getEmpCode().equals(bank.getEmpCode())) {
                    return "existed";
                }
            }
        }

        bank.setCreateDate(System.currentTimeMillis() + "");
        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);
        bank.setIsAllotted("True");
        bank.setIsLoanAmountPaid("False");
        double doubleval = 0.0;
         String loanType=bank.getLoanType();
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, loanType);
           if(existrelationJson!=null)
           {
               List<SalaryHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<SalaryHead>>() {}.getType());
               SalaryHead relation = relationlist.get(0);
               String LoanName=relation.getShortDescription();
               bank.setLoanName(LoanName);
               loanpymentData.setLoanName(LoanName);
           }
        String loanJson = new Gson().toJson(bank);
        String bankid = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_ALLOTMENT_TABLE, loanJson);
        String dated = loanpymentData.getDated();
        double interestPercent = 0.0;
        String month = dated.substring(3, 5);
        String year = dated.substring(6, 10);
        loanpymentData.setCreated(Long.parseLong(System.currentTimeMillis() + ""));
        loanpymentData.setUpdated(Long.parseLong(System.currentTimeMillis() + ""));
        loanpymentData.setStatus(ApplicationConstants.ACTIVE);
        loanpymentData.setPaymentMode(ApplicationConstants.LOAN_ALLOTMENT_MODE);
        loanpymentData.setPaidAmount(0.0);
        loanpymentData.setLoanDate(dated);
        loanpymentData.setIsLoanAmountPaid("False");
        loanpymentData.setBalanceAmount(loanpymentData.getSanctionedAmount());
        loanpymentData.setIsLocked("false");
        loanpymentData.setMonth(Integer.parseInt(month));
        loanpymentData.setYear(Integer.parseInt(year));
//        double createDateInDouble = loanpymentData.getCreateDateInDouble();
        //loanpymentData.setCreateDateInDouble(loanpymentData.getto);
        //loanpymentData.setTotalNoOfInstallments(loanJson);
        //loanpymentData.setBalanceAmount(loanpymentData.getBalanceAmount() - loanpymentData.getPaidAmount());
        double sanctionedAmnt = loanpymentData.getSanctionedAmount();
        loanpymentData.setLoanAmount(loanpymentData.getSanctionedAmount());
        loanpymentData.setBalanceNoOfInstallments(loanpymentData.getTotalInstallment());
        interestPercent = loanpymentData.getInterestPercentage();
        String loanNature=loanpymentData.getLoanNature();
        if(loanNature.equals("Refundable"))
        {
        if (interestPercent != doubleval) {

            double interestPercentage = interestPercent;
            double inetrestAmount = (sanctionedAmnt * interestPercentage) / 100;
            loanpymentData.setInterestAmount(inetrestAmount);
            loanpymentData.setInterestPaid(0.0);
            loanpymentData.setBalanceInterest(inetrestAmount);
            double totalInstalments = loanpymentData.getTotalInstallment();
            double intersetInstallmentAmount = inetrestAmount / totalInstalments;
            loanpymentData.setInterestInstallmentAmount(intersetInstallmentAmount);
            loanpymentData.setBalanceNoOfInterestInstallments(totalInstalments);
        } else {
            loanpymentData.setInterestAmount(doubleval);
            loanpymentData.setInterestPaid(doubleval);
            loanpymentData.setBalanceInterest(doubleval);
        }
        }else
        {
            loanpymentData.setInterestAmount(doubleval);
            loanpymentData.setInterestPaid(doubleval);
            loanpymentData.setBalanceInterest(doubleval);   
            loanpymentData.setInterestInstallmentAmount(doubleval);
            loanpymentData.setBalanceNoOfInterestInstallments(doubleval);
        }
        
        if ((!bankid.equals(null)) || (!bankid.equals(""))) {
            String loanJson1 = new Gson().toJson(loanpymentData);
            String bankid1 = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_PAYMENT_TABLE, loanJson1);

            HashMap<String, String> loanconditionMap = new HashMap<String, String>();
            loanconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            loanconditionMap.put("ddo", bank.getDdo());
            loanconditionMap.put("empCode", bank.getEmpCode());
            loanconditionMap.put("loanType", bank.getLoanType());
            loanconditionMap.put("applyNo", bank.getApplyNo());
            String loanresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, loanconditionMap);
            if (loanresult != null) {
                List<LoanApply> categolist = new Gson().fromJson(loanresult, new TypeToken<List<LoanApply>>() {
                }.getType());
                LoanApply gal = categolist.get(0);
                String id = ((LinkedTreeMap<String, String>) gal.getId()).get("$oid");
                gal.setIsAllotted("True");
                gal.setIsLoanAmountPaid("False");
                String galJson = new Gson().toJson(gal);
                boolean result22 = DBManager.getDbConnection().update(ApplicationConstants.LOAN_APPLY_TABLE, id, galJson);

            }
        }
        return bankid;
    }

    public static void main(String[] args) throws ParseException {
        String dated = "04/03/2016";
        //System.out.println(dated.substring(3, 5));
        //System.out.println(dated.substring(6, 10));
        String str[] = dated.split("/");

        DateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
        Calendar cal = Calendar.getInstance();
        Date dateStart = (Date) formatter.parse(dated);
        cal.setTime(dateStart);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        //System.out.println(year + "-" + month);
    }

    public String viewLoanAllotList(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
         conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, conditionMap);
        List<LoanAllotment> empList = new Gson().fromJson(result, new TypeToken<List<LoanAllotment>>() {
        }.getType());

        for (LoanAllotment cl : empList) {
            if ((cl.getLoanNature() != null)) {
                try {
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_NATURE_TABLE, cl.getLoanNature());
                    if (gaJson != null) {
                        List<LoanNature> gaList = new Gson().fromJson(gaJson, new TypeToken<List<LoanNature>>() {
                        }.getType());
                        LoanNature gal = gaList.get(0);
                        cl.setLoanNature(gal.getLoanName());
                    }
                } catch (Exception e) {
                }
            }
            if (cl.getDdo() != null) {
                try {
                    String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                    if (gaJson1 != null) {
                        List<DDO> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<DDO>>() {
                        }.getType());
                        DDO gal1 = gaList1.get(0);
                        cl.setDdo(gal1.getDdoName());

                    }
                } catch (Exception e) {
                }
            }
        }

        return new Gson()
                .toJson(empList);

    }

    public boolean updateLoanAllotment(LoanAllotment bank, String bankId) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_ALLOTMENT_TABLE, bankId);
        List<LoanAllotment> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanAllotment>>() {
        }.getType());
        LoanAllotment relation = relationlist.get(0);
        String dated = bank.getDated();
        String month = dated.substring(3, 5);
        String year = dated.substring(6, 10);
        relation.setDated(bank.getDated());
        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setSanctionedAmount(bank.getSanctionedAmount());
        relation.setAdjustAmount(bank.getAdjustAmount());
        relation.setAllotAmount(bank.getAllotAmount());
        relation.setInstallmentAmount(bank.getInstallmentAmount());
        relation.setTotalInstallment(bank.getTotalInstallment());
        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LOAN_ALLOTMENT_TABLE, bankId, bankJson);

        HashMap<String, String> loanconditionMap = new HashMap<String, String>();
        loanconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        loanconditionMap.put("ddo", bank.getDdo());
        loanconditionMap.put("empCode", bank.getEmpCode());
        loanconditionMap.put("loanType", bank.getLoanType());
        loanconditionMap.put("applyNo", bank.getApplyNo());
        loanconditionMap.put("paymentMode", "loanAllotment");
        String paymentresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, loanconditionMap);
        List<LoanPayment> paymentresultObjs = new Gson().fromJson(paymentresult, new TypeToken<List<LoanPayment>>() {
        }.getType());
        LoanPayment loanPayment = paymentresultObjs.get(0);
        loanPayment.setDated(bank.getDated());
        loanPayment.setUpdateDate(System.currentTimeMillis() + "");
        loanPayment.setSanctionedAmount(bank.getSanctionedAmount());
        //loanPayment.setAdjustamount(String.valueOf(bank.getAdjustamount()));
        // loanPayment.setAllotamount(bank.getAllotamount());
        loanPayment.setInstallmentAmount(bank.getInstallmentAmount());
        loanPayment.setBalanceAmount(bank.getSanctionedAmount());
        loanPayment.setBalanceAmount(loanPayment.getBalanceAmount() - loanPayment.getPaidAmount());
        double sanctionedAmnt = loanPayment.getSanctionedAmount();
        loanPayment.setLoanAmount(bank.getSanctionedAmount());
        loanPayment.setTotalInstallment(bank.getTotalInstallment());
        loanPayment.setBalanceNoOfInstallments(bank.getTotalInstallment());
        Double interestPercent = loanPayment.getInterestPercentage();
        if (interestPercent != 0.0) {

            double interestPercentage = interestPercent;
            double inetrestAmount = (sanctionedAmnt * interestPercentage) / 100;
            loanPayment.setInterestAmount(inetrestAmount);
            loanPayment.setInterestPaid(0.0);
            loanPayment.setBalanceInterest(inetrestAmount);
            double totalInstalments = bank.getTotalInstallment();
            double intersetInstallmentAmount = inetrestAmount / totalInstalments;
            loanPayment.setInterestInstallmentAmount(intersetInstallmentAmount);
            loanPayment.setBalanceNoOfInterestInstallments(totalInstalments);
        } else {
            loanPayment.setInterestAmount(0.0);
            loanPayment.setInterestPaid(0.0);
            loanPayment.setBalanceInterest(0.0);
        }
        String payId = ((LinkedTreeMap<String, String>) loanPayment.getId()).get("$oid");
        String payJson = new Gson().toJson(loanPayment);
        boolean resultpayJson = DBManager.getDbConnection().update(ApplicationConstants.LOAN_PAYMENT_TABLE, payId, payJson);
        return result;
    }

    public boolean deleteloanAllot(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_ALLOTMENT_TABLE, rid);
        List<LoanAllotment> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanAllotment>>() {
        }.getType());
        LoanAllotment relation = relationlist.get(0);
        // LoanAllotment relationobje = new LoanAllotment();

        relation.setStatus(ApplicationConstants.DELETE);
        relation.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relation);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOAN_ALLOTMENT_TABLE, rid, relationJson);
        HashMap<String, String> loanconditionMap = new HashMap<String, String>();
        loanconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        loanconditionMap.put("ddo", relation.getDdo());
        loanconditionMap.put("empCode", relation.getEmpCode());
        loanconditionMap.put("loanType", relation.getLoanType());
        loanconditionMap.put("applyNo", relation.getApplyNo());
        String loanresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_APPLY_TABLE, loanconditionMap);
        if (loanresult != null) {
            List<LoanApply> categolist = new Gson().fromJson(loanresult, new TypeToken<List<LoanApply>>() {
            }.getType());
            LoanApply gal = categolist.get(0);
            String id = ((LinkedTreeMap<String, String>) gal.getId()).get("$oid");
            gal.setIsAllotted("False");
            String galJson = new Gson().toJson(gal);
            boolean result22 = DBManager.getDbConnection().update(ApplicationConstants.LOAN_APPLY_TABLE, id, galJson);
        }
        return status;

    }

    public String getEmpCodes(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ALLOTMENT_TABLE, conditionMap);
        List<LoanAllotment> empList = new Gson().fromJson(result, new TypeToken<List<LoanAllotment>>() {
        }.getType());
        for (LoanAllotment cl : empList) {
            try {
                String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, cl.getDdo());
                List<DDO> gaList = new Gson().fromJson(gaJson, new TypeToken<List<DDO>>() {
                }.getType());
                DDO gal = gaList.get(0);
                cl.setDdo(gal.getDdoName());
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList);

    }

    public Object getOrderNoBasedOnDdoAndLoanType(String ddo, String loanType) {
        try {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put("ddo", ddo);
            conditionMap.put("loanType", loanType);
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_ORDER_TABLE, conditionMap);
            if (result != null) {
                List<LoanOrder> list = new Gson().fromJson(result, new TypeToken<List<LoanOrder>>() {
                }.getType());
                List<LoanOrder> employeeList = new ArrayList<LoanOrder>();
                LoanOrder gal = list.get(0);
                employeeList.add(gal);
                return new Gson().toJson(employeeList);
            }
        } catch (Exception e) {
        }

        return "";

    }

}
