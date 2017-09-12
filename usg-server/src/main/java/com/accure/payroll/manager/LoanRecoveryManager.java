/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.SalaryHead;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.payroll.dto.LoanPayment;
import com.accure.payroll.dto.LoanRecovery;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author upendra
 */
public class LoanRecoveryManager {

    static RestClient aql = new RestClient();
    static String loanPaymentTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LOAN_PAYMENT_TABLE + "`";

    public String saveLoanRecovryData(LoanRecovery bank, LoanPayment loanpymentData) throws Exception {

        String month = bank.getMonth();
        String empCode = bank.getEmpCode();
        String ddo = bank.getDdo();
        String year = bank.getYear();
        String applyNo = bank.getApplyNo();
        String loanType = bank.getLoanType();
        String isLoanAmountPaid;

        HashMap conditionMap = new HashMap();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("month", Integer.parseInt(month));
        conditionMap.put("empCode", empCode);
        conditionMap.put("ddo", ddo);
        conditionMap.put("year", Integer.parseInt(year));
        conditionMap.put("applyNo", applyNo);
        conditionMap.put("loanType", loanType);
        conditionMap.put("paymentMode", "salary");
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap);
//        List<LoanRecovery> empList = new Gson().fromJson(result, new TypeToken<List<LoanRecovery>>() {
//        }.getType());
        if (result != null) {
            return "existed";
        } else {

            double createDateInDouble = loanpymentData.getCreateDateInDouble();

            double balanceAmount = loanpymentData.getBalanceAmount() - loanpymentData.getPaidAmount();
            double balAmount = (double) balanceAmount;
            double instalmentAmount = loanpymentData.getInstallmentAmount();
            double balNoOfInstalments = balanceAmount / instalmentAmount;
            double balNoOfInstalment = (double) Math.ceil(balNoOfInstalments);
            double inetrestAmount = 0.0;
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, loanType);
            if (existrelationJson != null) {
                List<SalaryHead> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<SalaryHead>>() {
                }.getType());
                SalaryHead relation = relationlist.get(0);
                String LoanName = relation.getShortDescription();
                bank.setLoanName(LoanName);
                loanpymentData.setLoanName(LoanName);
            }
            loanpymentData.setBalanceAmount(balAmount);
            loanpymentData.setTotalInstallment(loanpymentData.getTotalInstallment());
            loanpymentData.setBalanceNoOfInstallments(balNoOfInstalment);
            loanpymentData.setCreateDateInDouble(createDateInDouble);
            loanpymentData.setCreated(Long.parseLong(System.currentTimeMillis() + ""));
            loanpymentData.setUpdated(Long.parseLong(System.currentTimeMillis() + ""));
            loanpymentData.setStatus(ApplicationConstants.ACTIVE);
            loanpymentData.setPaymentMode(ApplicationConstants.LOAN_PAYMENT_MODE_LOAN_RECOVERY);
            loanpymentData.setIsLocked(ApplicationConstants.FALSE);
            double interestPercent = loanpymentData.getInterestPercentage();
            if (interestPercent != 0.0) {
                LoanPayment lp = getLatestPayment(empCode, applyNo, loanType);
                loanpymentData.setInterestPaid(0.0);
                // loanpymentData.setInterestInstallmentAmount(lp.getInterestInstallmentAmount());
                inetrestAmount = (balAmount * interestPercent) / 100;
                loanpymentData.setInterestAmount(inetrestAmount);
                loanpymentData.setBalanceInterest(inetrestAmount);
                double intersetInstallmentAmount = inetrestAmount / balNoOfInstalments;
                loanpymentData.setInterestInstallmentAmount(intersetInstallmentAmount);
                loanpymentData.setBalanceNoOfInterestInstallments(balNoOfInstalments);
            } else {

                loanpymentData.setBalanceNoOfInterestInstallments(0.0);
                loanpymentData.setInterestPaid(0.0);
                loanpymentData.setInterestInstallmentAmount(0.0);
                loanpymentData.setInterestAmount(0.0);
                loanpymentData.setBalanceInterest(0.0);
            }
            if (balAmount == 0) {
                isLoanAmountPaid = "True";
                loanpymentData.setIsLoanAmountPaid(isLoanAmountPaid);
                bank.setIsLoanAmountPaid(isLoanAmountPaid);

                boolean status = new LoanApplyManager().setLoanPaidStatus(ddo, empCode, applyNo, isLoanAmountPaid, loanType);
            } else {
                loanpymentData.setIsLoanAmountPaid("False");
                bank.setIsLoanAmountPaid("False");
            }

            bank.setCreateDate(System.currentTimeMillis() + "");
            bank.setUpdateDate(System.currentTimeMillis() + "");
            bank.setStatus(ApplicationConstants.ACTIVE);
            bank.setBalanceAmount(balAmount);
            bank.setBalanceNoOfInstallments(balNoOfInstalment);
            String loanJson = new Gson().toJson(bank);
            String bankid = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_RECOVERY_TABLE, loanJson);

            String loanJson1 = new Gson().toJson(loanpymentData);
            String loanpymentId = DBManager.getDbConnection().insert(ApplicationConstants.LOAN_PAYMENT_TABLE, loanJson1);

            String loanRecoveryJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_RECOVERY_TABLE, bankid);

            List<LoanRecovery> relationlist = new Gson().fromJson(loanRecoveryJson, new TypeToken<List<LoanRecovery>>() {
            }.getType());
            LoanRecovery relation = relationlist.get(0);
            relation.setPaymentTableId(loanpymentId);
            boolean result1 = DBManager.getDbConnection().update(ApplicationConstants.LOAN_RECOVERY_TABLE, bankid, new Gson().toJson(relation));
            return bankid;
        }

    }

    public static LoanPayment getLatestPayment(String empCode, String ApplyNo, String loanType) throws Exception {
        LoanPayment output = null;
        if (ApplyNo == null || empCode == null) {
            return output;
        }

        String loanPaymentQuery = "select * from " + loanPaymentTable
                + " where status=\"" + ApplicationConstants.ACTIVE + "\""
                + " and empCode=\"" + empCode + "\""
                + " and applyNo=\"" + ApplyNo + "\""
                + " and loanType=\"" + loanType + "\""
                + " order by updated desc limit 1";
        //System.out.println(loanPaymentQuery);
        //get data from loanSupress
        String loanPaymentQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, loanPaymentQuery);
        //System.out.println("----" + loanPaymentQueryOutput);
        if (loanPaymentQueryOutput != null && !loanPaymentQueryOutput.isEmpty() && !loanPaymentQueryOutput.equals("[]")) {

            ArrayList<LoanPayment> dbList = new Gson().fromJson(loanPaymentQueryOutput, new TypeToken<ArrayList<LoanPayment>>() {
            }.getType());
            output = dbList.get(0);
        }
//        if (loanSuppressQueryOutput != null && !loanSuppressQueryOutput.isEmpty() && !loanSuppressQueryOutput.equals("[]")) {
//            //if suppress flag is true then dont deduct
//        } else {
//            //need to be implement
//        }

        return output;
    }

    public String viewLoanRecoveryList(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_RECOVERY_TABLE, conditionMap);
        List<LoanRecovery> empList = new Gson().fromJson(result, new TypeToken<List<LoanRecovery>>() {
        }.getType());
        for (LoanRecovery cl : empList) {
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
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(empList);

    }

    public static void main(String[] args) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_RECOVERY_TABLE, "57dff14cd6690615ba7a6f4a");
        List<LoanRecovery> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanRecovery>>() {
        }.getType());
        LoanRecovery relation = relationlist.get(0);
        new LoanRecoveryManager().updateLoanRecovery(relation, "57dff14cd6690615ba7a6f4a");

    }

    public String updateLoanRecovery(LoanRecovery bank, String bankId) throws Exception {
        String isLoanAmountPaid = "";
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_RECOVERY_TABLE, bankId);
        List<LoanRecovery> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanRecovery>>() {
        }.getType());
        LoanRecovery relation = relationlist.get(0);
        HashMap conditionMap = new HashMap();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("month", Integer.parseInt(relation.getMonth()));
        conditionMap.put("empCode", relation.getEmpCode());
        conditionMap.put("ddo", relation.getDdo());
        conditionMap.put("year", Integer.parseInt(relation.getYear()));
        conditionMap.put("applyNo", relation.getApplyNo());
        conditionMap.put("loanType", relation.getLoanType());
        conditionMap.put("paymentMode", "salary");
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap);
        if (result1 != null) {
            return "existed";
        }
        double balanceAmount = bank.getBalanceAmount() - bank.getPaidAmount();
        double balAmount = (double) balanceAmount;
        double instalmentAmount = bank.getInstallmentAmount();
        double balNoOfInstalments = balanceAmount / instalmentAmount;
        double balNoOfInstalment = (double) Math.ceil(balNoOfInstalments);
        double inetrestAmount = 0.0;

        relation.setUpdateDate(System.currentTimeMillis() + "");
        relation.setStatus(ApplicationConstants.ACTIVE);
        relation.setBalanceAmount(balAmount);
        relation.setBalanceNoOfInstallments(balNoOfInstalment);
        relation.setMonth(bank.getMonth());
        relation.setYear(bank.getYear());
        relation.setPaidAmount(bank.getPaidAmount());
        relation.setDate(bank.getDate());
        String paymentId = relation.getPaymentTableId();

        String paymentresult = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_PAYMENT_TABLE, paymentId);
        List<LoanPayment> paymentresultObjs = new Gson().fromJson(paymentresult, new TypeToken<List<LoanPayment>>() {
        }.getType());
        LoanPayment loanPayment = paymentresultObjs.get(0);

        loanPayment.setDated(bank.getDate());
        loanPayment.setUpdateDate(System.currentTimeMillis() + "");
        loanPayment.setSanctionedAmount(bank.getSanctionedAmount());
        loanPayment.setBalanceAmount(balAmount);
        loanPayment.setBalanceNoOfInstallments(balNoOfInstalment);
        loanPayment.setMonth(Integer.parseInt(bank.getMonth()));
        loanPayment.setYear(Integer.parseInt(bank.getYear()));
        loanPayment.setPaidAmount(bank.getPaidAmount());
        loanPayment.setDated(bank.getDate());
        loanPayment.setTotalInstallment(bank.getTotalInstallment());
        double interestPercent = loanPayment.getInterestPercentage();
        double interestAmount = 0.0;
        if (interestPercent != 0.0) {
            LoanPayment lp = getLatestPayment(loanPayment.getEmpCode(), loanPayment.getApplyNo(), loanPayment.getLoanType());

            loanPayment.setInterestPaid(lp.getInterestPaid());
            interestAmount = (balAmount * interestPercent) / 100;
            loanPayment.setInterestAmount(interestAmount);
            loanPayment.setBalanceInterest(interestAmount);
            double intersetInstallmentAmount = interestAmount / balNoOfInstalment;
            loanPayment.setInterestInstallmentAmount(intersetInstallmentAmount);
            loanPayment.setBalanceNoOfInterestInstallments(balNoOfInstalment);
        } else {

            loanPayment.setBalanceNoOfInterestInstallments(0.0);
            loanPayment.setInterestPaid(0.0);
            loanPayment.setInterestInstallmentAmount(0.0);
            loanPayment.setInterestAmount(0.0);
            loanPayment.setBalanceInterest(0.0);
        }
        if (balAmount == 0) {
            isLoanAmountPaid = ApplicationConstants.TRUE;
            loanPayment.setIsLoanAmountPaid(isLoanAmountPaid);
            relation.setIsLoanAmountPaid(isLoanAmountPaid);
            boolean status = new LoanApplyManager().setLoanPaidStatus(bank.getDdo(), bank.getEmpCode(), bank.getLoanType(), bank.getApplyNo(), isLoanAmountPaid);
        } else {
            loanPayment.setIsLoanAmountPaid("False");
            relation.setIsLoanAmountPaid("False");
        }

        String bankJson = new Gson().toJson(relation);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LOAN_RECOVERY_TABLE, bankId, bankJson);
        //String paymentId = relation.getPaymentTableId();
//        String paymentIdJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_PAYMENT_TABLE, paymentId);
//        List<LoanPayment> lpLi = new Gson().fromJson(paymentIdJson, new TypeToken<List<LoanPayment>>() {
//        }.getType());
//        LoanPayment lp = lpLi.get(0);
//        lp.setPaidAmount(relation.getPaidAmount());
//        lp.setBalanceAmount(relation.getBalanceAmount());

        String payId = ((LinkedTreeMap<String, String>) loanPayment.getId()).get("$oid");
        String payJson = new Gson().toJson(loanPayment);
        boolean resultpayJson = DBManager.getDbConnection().update(ApplicationConstants.LOAN_PAYMENT_TABLE, paymentId, payJson);
        if (!resultpayJson) {
            return "Error";
        }
        return "success";
    }

    public boolean deleteloanRecovery(String rid) throws Exception {
        String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_RECOVERY_TABLE, rid);
        List<LoanRecovery> relationlist = new Gson().fromJson(existrelationJson, new TypeToken<List<LoanRecovery>>() {
        }.getType());
        LoanRecovery relation = relationlist.get(0);
        String payId = relation.getPaymentTableId();
//        LoanRecovery relationobje = new LoanRecovery();
//        relationobje.setDdo(relation.getDdo());
//        relationobje.setAccountNo(relation.getAccountNo());
//        relationobje.setBalanceAmount(relation.getBalanceAmount());
//        relationobje.setDate(relation.getDate());
//        relationobje.setDesignation(relation.getDesignation());
//        relationobje.setEmpcode(relation.getEmpcode());
//        relationobje.setEmpname(relation.getEmpname());
//        relationobje.setInsatlmentAmount(relation.getInsatlmentAmount());
//        relationobje.setLoanAmount(relation.getLoanAmount());
//        relationobje.setLoanDate(relation.getLoanDate());
//        relationobje.setLoanNature(relation.getLoanNature());
//        relationobje.setLoantype(relation.getLoantype());
//        relationobje.setMonth(relation.getMonth());
//        relationobje.setOredrno(relation.getOredrno());
//        relationobje.setPaidAmount(relation.getPaidAmount());
//        relationobje.setPreRecovery(relation.getPreRecovery());
//        relationobje.setRemarks(relation.getRemarks());
//        relationobje.setYear(relation.getYear());
//        relationobje.setCreateDate(relation.getCreateDate());
        relation.setStatus(ApplicationConstants.DELETE);
        relation.setUpdateDate(System.currentTimeMillis() + "");
        String relationJson = new Gson().toJson(relation);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOAN_RECOVERY_TABLE, rid, relationJson);
        if (status) {
            String existrPayRelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOAN_PAYMENT_TABLE, payId);
            List<LoanPayment> payrelationlist = new Gson().fromJson(existrPayRelationJson, new TypeToken<List<LoanPayment>>() {
            }.getType());
            LoanPayment payrelation = payrelationlist.get(0);
            payrelation.setStatus(ApplicationConstants.DELETE);
            payrelation.setUpdateDate(System.currentTimeMillis() + "");
            String payrelationJson = new Gson().toJson(payrelation);
            boolean paystatus = DBManager.getDbConnection().update(ApplicationConstants.LOAN_PAYMENT_TABLE, payId, payrelationJson);
        }
        return status;
    }

    public String getEmployeeRecoveryDetails(String ddo, String ecode, String loanType, String applyNo) throws Exception {
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
        String resultAllotmentData = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOAN_PAYMENT_TABLE, conditionMap);
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
        String loannature = empList.get(0).getLoanNature();
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

    public String getBalLoanAmount(String ecode, String loanType, String applyNo) throws Exception {
        PropertiesConfiguration config = getConfig();
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.LOAN_PAYMENT_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        BasicDBObject regexQuery12 = new BasicDBObject();

        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        regexQuery.put("empCode",
                new BasicDBObject("$regex", ecode));
        regexQuery.put("loanType",
                new BasicDBObject("$regex", loanType));
        regexQuery.put("applyNo",
                new BasicDBObject("$regex", applyNo));
        regexQuery12.put("createDateInDouble", -1);
        List<LoanPayment> li = new ArrayList<LoanPayment>();
        LoanPayment em = null;
        DBCursor cursor2 = collection.find(regexQuery).sort(regexQuery12);
        double val = 0;
        double balanceAmount = 0;
        double loanAmount = 0;
        double paidAmount = 0;
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();

            Type type = new TypeToken<LoanPayment>() {
            }.getType();
            em = new Gson().fromJson(ob.toString(), type);
            li.add(em);
        }
        balanceAmount = li.get(0).getBalanceAmount();
        loanAmount = li.get(0).getSanctionedAmount();
        //paidAmount = Integer.parseInt(li.get(0).getPaidAmount());

        for (Iterator<LoanPayment> iterator = li.iterator(); iterator.hasNext();) {
            LoanPayment next = iterator.next();
            if (balanceAmount > next.getBalanceAmount()) {
                balanceAmount = next.getBalanceAmount();
            }
            paidAmount += next.getPaidAmount();
        }
        balanceAmount = loanAmount - paidAmount;
        HashMap<String, Double> map = new HashMap<String, Double>();
        map.put("Value", balanceAmount);

        return new Gson().toJson(map);
    }

}
