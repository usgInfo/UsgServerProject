/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.budget.manager.FinancialYearManager;
import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.LedgerList;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.manager.TrialBalanceManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class BudgetAtAGlance {

    private static Logger LOGGER = Logger.getLogger(BudgetAtAGlance.class);

    private String budgetHead;

    private String budgetHeadName;

    private final NumberFormat numberFormat = NumberFormat.getInstance();

    private final List<String> headerList = new ArrayList();

    public List<String> getHeaderList() {
        return headerList;
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

    private enum BudgetHeaders {
        Budget_Head("BUDGET HEAD"),
        Actual_For("ACTUAL FOR"),
        Budget_Estimate("BUDGET ESTIMATE FOR"),
        Revised_Estimate("REVISED ESTIMATES FOR"),
        Future_Budget_Extimate("BUDGET ESTIMATE FOR"),
        Ledger_Surplus_Label("SURPLUS / DEFICIT");

        private String name;

        private BudgetHeaders(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum LedgerRowKeys {
        text, actualFor, budgetEstimate, revisedEstimate, futureBudgetEstimate,
        surplusDeficit("SURPLUS / DEFICIT");

        private String name;

        LedgerRowKeys() {
            name = "";
        }

        LedgerRowKeys(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public List<LedgerWiseEstimate> getLedgerWiseEstimate() {
        return ledgerWiseEstimate;
    }

    public void setLedgerWiseEstimate(List<LedgerWiseEstimate> ledgerWiseEstimate) {
        this.ledgerWiseEstimate = ledgerWiseEstimate;
    }

    public class LedgerWiseEstimate implements Comparable<LedgerWiseEstimate> {

        private String ledgerId;
        private String ledgerName;
        private String acutalFor;
        private String budgetEstimate;
        private String revisedEstimate;
        private String futureBudgetEstimate;

        private final LinkedHashMap<String, Object> rowData = new LinkedHashMap();

        public String getLedgerName() {
            return ledgerName;
        }

        public void setLedgerName(String ledgerName) {
            this.ledgerName = ledgerName;
            this.rowData.put(LedgerRowKeys.text.toString(), ledgerName);

        }

        public void setLedgerKey(LedgerRowKeys ledgerRowKeys) {
            this.rowData.put(LedgerRowKeys.surplusDeficit.toString(),
                    LedgerRowKeys.surplusDeficit.getName());
        }

        public String getAcutalFor() {
            return acutalFor;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + (this.ledgerName != null ? this.ledgerName.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LedgerWiseEstimate other = (LedgerWiseEstimate) obj;
            if ((this.ledgerName == null) ? (other.ledgerName != null) : !this.ledgerName.equals(other.ledgerName)) {
                return false;
            }
            return true;
        }

        public void setAcutalFor(String acutalFor) {
            this.acutalFor = acutalFor;
            this.rowData.put(LedgerRowKeys.actualFor.toString(), acutalFor);
        }

        public String getBudgetEstimate() {
            return budgetEstimate;
        }

        public void setBudgetEstimate(String budgetEstimate) {
            this.budgetEstimate = budgetEstimate;
            this.rowData.put(LedgerRowKeys.budgetEstimate.toString(), budgetEstimate);
        }

        public String getRevisedEstimate() {
            return revisedEstimate;
        }

        public void setRevisedEstimate(String revisedEstimate) {
            this.revisedEstimate = revisedEstimate;
            this.rowData.put(LedgerRowKeys.revisedEstimate.toString(), revisedEstimate);
        }

        public String getFutureBudgetEstimate() {
            return futureBudgetEstimate;
        }

        public void setFutureBudgetEstimate(String futureBudgetEstimate) {
            this.futureBudgetEstimate = futureBudgetEstimate;
            this.rowData.put(LedgerRowKeys.futureBudgetEstimate.toString(), futureBudgetEstimate);
        }

        @Override
        public int compareTo(LedgerWiseEstimate o) {
            if (this.ledgerName != null && o.getLedgerName() != null) {
                return this.ledgerName.compareTo(o.getLedgerName());
            } else {
                return 0;
            }
        }

        public LinkedHashMap<String, Object> getRowData() {
            return rowData;
        }

        public String getLedgerId() {
            return ledgerId;
        }

        public void setLedgerId(String ledgerId) {
            this.ledgerId = ledgerId;
        }

    }

    private List<LedgerWiseEstimate> ledgerWiseEstimate;

    public HashMap<String, Object> getBudgetAtGlanceRecords(String createBudgetExpense) {

        List<BudgetAtAGlance> budgetAtGlanceList = null;
        HashMap<String, Object> finalResult = new HashMap();

        try {

            List<String> queryDocument = new ArrayList();

            queryDocument.add("status=\"Active\"");
            queryDocument.add("postingStatus=\"Posted\"");

            HashMap<String, Object> queryFilterMap = new Gson().fromJson(createBudgetExpense,
                    new TypeToken<HashMap<String, Object>>() {
            }.getType());

            String financialYearSelected = new FinancialYearManager().fetch(
                    queryFilterMap.get("financialYearId") + "");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String year = null;

            if (financialYearSelected != null && financialYearSelected.length() > 0) {
                HashMap<String, Object> financialYearMap = new Gson().fromJson(financialYearSelected,
                        new TypeToken<HashMap<String, Object>>() {
                }.getType());

                year = financialYearMap.get("year") + "";
                String fromDate = financialYearMap.get("fromDate") + "";
                String toDate = financialYearMap.get("toDate") + "";

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                cal.setTime(dateFormat.parse(fromDate));
                cal.add(Calendar.YEAR, -1);
                long fromDateLong = cal.getTimeInMillis();

                cal.setTime(dateFormat.parse(toDate));
                cal.add(Calendar.YEAR, -1);
                long toDateLong = cal.getTimeInMillis();

                queryDocument.add("voucherDateInMilliSecond>=" + fromDateLong);
                queryDocument.add("voucherDateInMilliSecond<=" + toDateLong);

            }

            this.headerList.add(BudgetHeaders.Budget_Head.getName());

            TrialBalanceManager trialBalanceManager = new TrialBalanceManager();
            ArrayList paymentVoucherList = trialBalanceManager.getVoucherList(
                    TrialBalanceManager.VoucherType.payment.toString(), queryDocument);
            ArrayList contraVoucherList = trialBalanceManager.getVoucherList(
                    TrialBalanceManager.VoucherType.contra.toString(), queryDocument);

            HashMap<String, HashMap<String, HashMap>> budgetAtAGlanceMap = new HashMap();
            getActualBudgetForVouchers(paymentVoucherList, budgetAtAGlanceMap,
                    TrialBalanceManager.VoucherType.payment.toString());
            getActualBudgetForVouchers(contraVoucherList, budgetAtAGlanceMap,
                    TrialBalanceManager.VoucherType.contra.toString());

            queryFilterMap.put("financialYearId", null);
            queryFilterMap.put("ddo", null);

            Set<Map.Entry<String, Object>> set = queryFilterMap.entrySet();

            Iterator<Map.Entry<String, Object>> setIterator = set.iterator();
            while (setIterator.hasNext()) {
                Map.Entry<String, Object> entry = setIterator.next();
                if (entry.getValue() == null || (entry.getValue() + "").equalsIgnoreCase("")) {
                    setIterator.remove();
                }
            }

            List<CreateBudgetExpense> budgetExpenseList = fetchBudgetExpenseTableRecords(queryFilterMap, year);
            budgetAtGlanceList = addBudgetExpenseRecordsIntoBudgetHeadMap(
                    budgetAtAGlanceMap, budgetExpenseList, financialYearSelected);

            finalResult.put("result", budgetAtGlanceList);
            finalResult.put("header", this.headerList);
            finalResult.put("financialYear", financialYearSelected);

        } catch (Exception exception) {
            LOGGER.error("getBudgetAtGlanceRecords", exception);
        }
        return finalResult;
    }

    public HashMap<String, Object> getBudgetAtGlanceRecords_v1(String createBudgetExpense) {

        List<BudgetAtAGlance> budgetAtGlanceList = null;
        HashMap<String, Object> finalResult = new HashMap();

        try {

            List<String> queryDocument = new ArrayList();

            queryDocument.add("status=\"Active\"");
            queryDocument.add("postingStatus=\"Posted\"");

            HashMap<String, Object> queryFilterMap = new Gson().fromJson(createBudgetExpense,
                    new TypeToken<HashMap<String, Object>>() {
            }.getType());

            String financialYearSelected = new FinancialYearManager().fetch(
                    queryFilterMap.get("financialYearId") + "");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String year = null;

            if (financialYearSelected != null && financialYearSelected.length() > 0) {
                HashMap<String, Object> financialYearMap = new Gson().fromJson(financialYearSelected,
                        new TypeToken<HashMap<String, Object>>() {
                }.getType());

                year = financialYearMap.get("year") + "";
                String fromDate = financialYearMap.get("fromDate") + "";
                String toDate = financialYearMap.get("toDate") + "";

                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                cal.setTime(dateFormat.parse(fromDate));
                cal.add(Calendar.YEAR, -1);
                long fromDateLong = cal.getTimeInMillis();

                cal.setTime(dateFormat.parse(toDate));
                cal.add(Calendar.YEAR, -1);
                long toDateLong = cal.getTimeInMillis();

                queryDocument.add("voucherDateInMilliSecond>=" + fromDateLong);
                queryDocument.add("voucherDateInMilliSecond<=" + toDateLong);

            }

            this.headerList.add(BudgetHeaders.Budget_Head.getName());

            HashMap<String, HashMap<String, HashMap>> budgetAtAGlanceMap = new HashMap();

            PreviousBudgetAmountDetailsManager prevBudAmtDetails = new PreviousBudgetAmountDetailsManager();

            HashMap<String, Object> queryFilterMap_1 = new HashMap(queryFilterMap);
            Iterator<Map.Entry<String, Object>> mapItr = queryFilterMap_1.entrySet().iterator();
            while (mapItr.hasNext()) {
                Map.Entry<String, Object> entry = mapItr.next();
                if (entry.getValue() == null || (entry.getValue() + "").isEmpty()) {
                    mapItr.remove();
                }
            }

            queryFilterMap_1.remove("financialYearId");
            if (year != null) {
                queryFilterMap_1.put("financialYear", year);
            }

            List<PreviousBudgetAmountDetails> prevBudAmtList = prevBudAmtDetails.
                    getPreviousBudgetAmountDetails(queryFilterMap_1, year);
            if (prevBudAmtList != null) {

                putPrevBudgetAmountsIntoLedgers(prevBudAmtList, budgetAtAGlanceMap);

            } else {

                TrialBalanceManager trialBalanceManager = new TrialBalanceManager();
                ArrayList paymentVoucherList = trialBalanceManager.getVoucherList(
                        TrialBalanceManager.VoucherType.payment.toString(), queryDocument);
                ArrayList contraVoucherList = trialBalanceManager.getVoucherList(
                        TrialBalanceManager.VoucherType.contra.toString(), queryDocument);

                getActualBudgetForVouchers(paymentVoucherList, budgetAtAGlanceMap,
                        TrialBalanceManager.VoucherType.payment.toString());
                getActualBudgetForVouchers(contraVoucherList, budgetAtAGlanceMap,
                        TrialBalanceManager.VoucherType.contra.toString());

            }

            queryFilterMap.put("financialYearId", null);
            queryFilterMap.put("ddo", null);

            Set<Map.Entry<String, Object>> set = queryFilterMap.entrySet();

            Iterator<Map.Entry<String, Object>> setIterator = set.iterator();
            while (setIterator.hasNext()) {
                Map.Entry<String, Object> entry = setIterator.next();
                if (entry.getValue() == null || (entry.getValue() + "").equalsIgnoreCase("")) {
                    setIterator.remove();
                }
            }

            List<CreateBudgetExpense> budgetExpenseList = fetchBudgetExpenseTableRecords(queryFilterMap, year);
            budgetAtGlanceList = addBudgetExpenseRecordsIntoBudgetHeadMap(
                    budgetAtAGlanceMap, budgetExpenseList, financialYearSelected);

            finalResult.put("result", budgetAtGlanceList);
            finalResult.put("header", this.headerList);
            finalResult.put("financialYear", financialYearSelected);

        } catch (Exception exception) {
            LOGGER.error("getBudgetAtGlanceRecords", exception);
        }
        return finalResult;
    }

    public void putPrevBudgetAmountsIntoLedgers(List<PreviousBudgetAmountDetails> prevBudAmtDetails,
            HashMap<String, HashMap<String, HashMap>> budgetHeadMap) {

        if (prevBudAmtDetails != null && budgetHeadMap != null) {
            try {
                NumberFormat nFormat = NumberFormat.getInstance();
                for (PreviousBudgetAmountDetails prevBudAmt : prevBudAmtDetails) {

                    if (budgetHeadMap.containsKey(prevBudAmt.getBudgetHead())) {
                        HashMap<String, HashMap> ledgerMap = budgetHeadMap.get(prevBudAmt.getBudgetHead());
                        if (ledgerMap.containsKey(prevBudAmt.getLedgerId())) {
                            HashMap<String, Object> ledger = ledgerMap.get(prevBudAmt.getLedgerId());
                            if (ledger.containsKey("amount")) {
                                double amount = nFormat.parse(ledger.get("amount") + "").doubleValue();
                                amount += nFormat.parse(prevBudAmt.getActualAmount()).doubleValue();
                                ledger.put("amount", amount);
                            }
                        } else {
                            HashMap<String, Object> amountMap = new HashMap();
                            amountMap.put("amount", prevBudAmt.getActualAmount());
                            ledgerMap.put(prevBudAmt.getLedgerId(), amountMap);
                        }

                    } else {
                        HashMap<String, HashMap> ledgerMap = new HashMap();
                        HashMap<String, Object> amountMap = new HashMap();
                        amountMap.put("amount", prevBudAmt.getActualAmount());
                        ledgerMap.put(prevBudAmt.getLedgerId(), amountMap);
                        budgetHeadMap.put(prevBudAmt.getBudgetHead(), ledgerMap);
                    }
                }
            } catch (ParseException parseException) {
                LOGGER.error("putPrevBudgetAmountsIntoLedgers", parseException);
            }
        }

    }

    private void getActualBudgetForVouchers(ArrayList voucherList,
            HashMap<String, HashMap<String, HashMap>> budgetAtAGlanceMap, String ledgerType) {

        PaymentVoucher paymentVoucher = null;
        ContraVoucher contraVoucher = null;

        for (Object voucher : voucherList) {
            if (voucher instanceof PaymentVoucher) {
                paymentVoucher = (PaymentVoucher) voucher;
                if (budgetAtAGlanceMap.containsKey(paymentVoucher.getBudgetHead())) {
                    HashMap<String, HashMap> ledgerMap = budgetAtAGlanceMap.get(paymentVoucher.getBudgetHead());
                    List<LedgerList> ledgerList = paymentVoucher.getLedgerList();
                    putLedgerMapIntoBudgetHeadMap(ledgerMap, ledgerList, ledgerType);
                } else {
                    HashMap<String, HashMap> ledgerMap = new HashMap();
                    List<LedgerList> ledgerList = paymentVoucher.getLedgerList();
                    for (LedgerList ledger : ledgerList) {
                        if (ledger.getGroupName() != null && (ledger.getGroupName().equalsIgnoreCase("cash group")
                                || ledger.getGroupName().equalsIgnoreCase("bank group"))
                                && ledgerType != null) {
                            Double amount = 0d;
                            if (ledgerType.equalsIgnoreCase(TrialBalanceManager.VoucherType.contra.toString())) {
                                amount = Double.parseDouble(ledger.getCrAmount() + "");
                            } else {
                                amount = Double.parseDouble(ledger.getDrAmount() + "")
                                        + Double.parseDouble(ledger.getCrAmount() + "");
                            }
                            HashMap budgetLedgerMap = new HashMap();
                            budgetLedgerMap.put("amount", String.format("%.2f", amount));
                            ledgerMap.put(ledger.getLedger(), budgetLedgerMap);
                        }
                    }
                    budgetAtAGlanceMap.put(paymentVoucher.getBudgetHead(), ledgerMap);
                }
            } else if (voucher instanceof ContraVoucher) {
                contraVoucher = (ContraVoucher) voucher;
                if (budgetAtAGlanceMap.containsKey(contraVoucher.getBudgetHead())) {
                    HashMap<String, HashMap> ledgerMap = budgetAtAGlanceMap.get(contraVoucher.getBudgetHead());
                    List<LedgerList> ledgerList = contraVoucher.getLedgerList();
                    putLedgerMapIntoBudgetHeadMap(ledgerMap, ledgerList, ledgerType);
                } else {
                    HashMap<String, HashMap> ledgerMap = new HashMap();
                    List<LedgerList> ledgerList = contraVoucher.getLedgerList();
                    for (LedgerList ledger : ledgerList) {
                        if (ledger.getGroupName() != null && (ledger.getGroupName().equalsIgnoreCase("cash group")
                                || ledger.getGroupName().equalsIgnoreCase("bank group"))) {
                            Double amount = 0d;
                            if (ledgerType.equalsIgnoreCase(TrialBalanceManager.VoucherType.contra.toString())) {
                                amount = Double.parseDouble(ledger.getCrAmount() + "");
                            } else {
                                amount = Double.parseDouble(ledger.getDrAmount() + "")
                                        + Double.parseDouble(ledger.getCrAmount() + "");
                            }
                            HashMap budgetLedgerMap = new HashMap();
                            budgetLedgerMap.put("amount", String.format("%.2f", amount));
                            ledgerMap.put(ledger.getLedger(), budgetLedgerMap);
                        }
                    }
                    budgetAtAGlanceMap.put(contraVoucher.getBudgetHead(), ledgerMap);
                }
            }
        }
    }

    private void putLedgerMapIntoBudgetHeadMap(HashMap<String, HashMap> ledgerMap, List<LedgerList> ledgerList,
            String ledgerType) {
        for (LedgerList ledger : ledgerList) {
            if (ledger.getGroupName() != null && (ledger.getGroupName().equalsIgnoreCase("bank group")
                    || ledger.getGroupName().equalsIgnoreCase("cash group")) && ledgerType != null) {

                if (ledgerMap.containsKey(ledger.getLedger())) {
                    HashMap budgetLedgerMap = ledgerMap.get(ledger.getLedger());
                    Double existingValue = Double.parseDouble(budgetLedgerMap.get("amount") + "")
                            + Double.parseDouble(budgetLedgerMap.get("amount") + "");
                    if (ledgerType.equalsIgnoreCase(TrialBalanceManager.VoucherType.contra.toString())) {
                        existingValue += Double.parseDouble(ledger.getCrAmount());
                    } else {
                        existingValue += Double.parseDouble(ledger.getCrAmount())
                                + Double.parseDouble(ledger.getDrAmount());
                    }
                    budgetLedgerMap.put("amount", String.format("%.2f", existingValue));
                    ledgerMap.put(ledger.getLedger(), budgetLedgerMap);

                } else {
                    HashMap budgetLedgerMap = new HashMap();
                    Double amount = 0d;
                    if (ledgerType.equalsIgnoreCase(TrialBalanceManager.VoucherType.contra.toString())) {
                        amount = Double.parseDouble(ledger.getCrAmount());
                    } else {
                        amount = Double.parseDouble(ledger.getCrAmount())
                                + Double.parseDouble(ledger.getDrAmount());
                    }
                    budgetLedgerMap.put("amount", String.format("%.2f", amount));
                    ledgerMap.put(ledger.getLedger(), budgetLedgerMap);

                }
            }
        }

    }

    private List<BudgetAtAGlance> addBudgetExpenseRecordsIntoBudgetHeadMap(
            HashMap<String, HashMap<String, HashMap>> budgetAtAGlanceMap,
            List<CreateBudgetExpense> budgetExpenseList, String selectedFinancialYear) throws Exception {

        ArrayList<BudgetAtAGlance> budgetAtGlanceList = new ArrayList();
        TreeSet budgetHeadSet = new TreeSet();

        FinancialYear financialYear = null;
        if (selectedFinancialYear != null) {
            financialYear = ((FinancialYear) new Gson().fromJson(selectedFinancialYear,
                    new TypeToken<FinancialYear>() {
            }.getType()));
        }

        addHeader(BudgetHeaders.Actual_For, financialYear);
        addHeader(BudgetHeaders.Budget_Estimate, financialYear);
        addHeader(BudgetHeaders.Revised_Estimate, financialYear);
        addHeader(BudgetHeaders.Future_Budget_Extimate, financialYear);

        if (budgetExpenseList.size() > 0) {

            for (CreateBudgetExpense budgetExpense : budgetExpenseList) {
                String budgetHeadNameStr = budgetExpense.getBudgetHeadName();
                String budgetHeadStr = budgetExpense.getBudgetHead();
                BudgetAtAGlance budgetAtGlance = null;
                List<LedgerWiseEstimate> ledgerWiseEstimateList = null;

                if (budgetHeadSet.contains(budgetHeadStr)) {
                    Iterator<BudgetAtAGlance> budgetAtGlanceListIterator = budgetAtGlanceList.iterator();
                    while (budgetAtGlanceListIterator.hasNext()) {
                        BudgetAtAGlance tempbudgetAtGlance = budgetAtGlanceListIterator.next();
                        if (tempbudgetAtGlance.getBudgetHead() != null
                                && tempbudgetAtGlance.getBudgetHead().equalsIgnoreCase(budgetHeadStr)) {
                            budgetAtGlance = tempbudgetAtGlance;
                            ledgerWiseEstimateList = budgetAtGlance.getLedgerWiseEstimate();
                            break;
                        }
                    }
                } else {
                    budgetHeadSet.add(budgetHeadStr);
                    budgetAtGlance = new BudgetAtAGlance();
                    budgetAtGlance.setBudgetHead(budgetHeadStr);
                    budgetAtGlance.setBudgetHeadName(budgetHeadNameStr);
                    ledgerWiseEstimateList = new ArrayList();
                    budgetAtGlance.setLedgerWiseEstimate(ledgerWiseEstimateList);
                    budgetAtGlanceList.add(budgetAtGlance);
                }

                HashMap<String, HashMap> ledgerMap = null;

                if (budgetAtAGlanceMap.containsKey(budgetHeadStr)) {
                    ledgerMap = budgetAtAGlanceMap.get(budgetHeadStr);
                }

                if (ledgerMap == null) {
                    ledgerMap = new HashMap();
                }

                addbudgetExpenseToBudgetAtGlance(budgetExpense, ledgerMap, financialYear,
                        ledgerWiseEstimateList);

            }

//            calculateSurplusDeficit(budgetAtGlanceList);
        }
        return budgetAtGlanceList;
    }

    private List<BudgetAtAGlance> addPreviousYearActualBudgetAmountIntoBudgetHeadMap(
            HashMap<String, HashMap<String, HashMap>> budgetAtAGlanceMap,
            List<PreviousBudgetAmountDetails> budgetAmountDetailsList, String selectedFinancialYear) throws Exception {

        ArrayList<BudgetAtAGlance> budgetAtGlanceList = new ArrayList();
        TreeSet budgetHeadSet = new TreeSet();

        FinancialYear financialYear = null;
        if (selectedFinancialYear != null) {
            financialYear = ((FinancialYear) new Gson().fromJson(selectedFinancialYear,
                    new TypeToken<FinancialYear>() {
            }.getType()));
        }

        addHeader(BudgetHeaders.Actual_For, financialYear);
        addHeader(BudgetHeaders.Budget_Estimate, financialYear);
        addHeader(BudgetHeaders.Revised_Estimate, financialYear);
        addHeader(BudgetHeaders.Future_Budget_Extimate, financialYear);

        if (budgetAmountDetailsList.size() > 0) {

            for (PreviousBudgetAmountDetails budgetAmount : budgetAmountDetailsList) {
                String budgetHeadNameStr = budgetAmount.getBudgetHeadName();
                String budgetHeadStr = budgetAmount.getBudgetHead();
                BudgetAtAGlance budgetAtGlance = null;
                List<LedgerWiseEstimate> ledgerWiseEstimateList = null;

                if (budgetHeadSet.contains(budgetHeadStr)) {
                    Iterator<BudgetAtAGlance> budgetAtGlanceListIterator = budgetAtGlanceList.iterator();
                    while (budgetAtGlanceListIterator.hasNext()) {
                        BudgetAtAGlance tempbudgetAtGlance = budgetAtGlanceListIterator.next();
                        if (tempbudgetAtGlance.getBudgetHead() != null
                                && tempbudgetAtGlance.getBudgetHead().equalsIgnoreCase(budgetHeadStr)) {
                            budgetAtGlance = tempbudgetAtGlance;
                            ledgerWiseEstimateList = budgetAtGlance.getLedgerWiseEstimate();
                            break;
                        }
                    }
                } else {
                    budgetHeadSet.add(budgetHeadStr);
                    budgetAtGlance = new BudgetAtAGlance();
                    budgetAtGlance.setBudgetHead(budgetHeadStr);
                    budgetAtGlance.setBudgetHeadName(budgetHeadNameStr);
                    ledgerWiseEstimateList = new ArrayList();
                    budgetAtGlance.setLedgerWiseEstimate(ledgerWiseEstimateList);
                    budgetAtGlanceList.add(budgetAtGlance);
                }

                HashMap<String, HashMap> ledgerMap = null;

                if (budgetAtAGlanceMap.containsKey(budgetHeadStr)) {
                    ledgerMap = budgetAtAGlanceMap.get(budgetHeadStr);
                }

                if (ledgerMap == null) {
                    ledgerMap = new HashMap();
                }

//                addbudgetExpenseToBudgetAtGlance(budgetExpense, ledgerMap, financialYear,
//                        ledgerWiseEstimateList);
//                addbudgetExpenseToBudgetAtGlance(budgetExpense, ledgerMap, financialYear,
//                        ledgerWiseEstimateList);
            }

            calculateSurplusDeficit(budgetAtGlanceList);

        }
        return budgetAtGlanceList;
    }

    private void addbudgetExpenseToBudgetAtGlance(CreateBudgetExpense budgetExpense,
            HashMap<String, HashMap> ledgerMap, FinancialYear financialYear,
            List<LedgerWiseEstimate> ledgerWiseEstimateList) throws Exception {

        String ledgerNameStr = budgetExpense.getLedgerName();
        String ledgerId = budgetExpense.getLedgerId();
        LedgerWiseEstimate ledgerWiseEstimate = null;

        Iterator<LedgerWiseEstimate> ledgerWiseEstimateIterator = ledgerWiseEstimateList.iterator();
        while (ledgerWiseEstimateIterator.hasNext()) {
            LedgerWiseEstimate tempLedgerWiseEstimate = ledgerWiseEstimateIterator.next();
            if (tempLedgerWiseEstimate.getLedgerId() != null && ledgerId != null
                    && tempLedgerWiseEstimate.getLedgerId().equals(ledgerId)) {
                ledgerWiseEstimate = tempLedgerWiseEstimate;
                break;
            }
        }
        if (ledgerWiseEstimate == null) {
            ledgerWiseEstimate = new LedgerWiseEstimate();
            ledgerWiseEstimateList.add(ledgerWiseEstimate);
        }

        ledgerWiseEstimate.setLedgerName(ledgerNameStr);
        ledgerWiseEstimate.setLedgerId(ledgerId);
        if (ledgerMap.containsKey(ledgerId)) {
            HashMap<String, Object> amountMap = ledgerMap.get(ledgerId);
            if (amountMap.containsKey("amount")) {
                if (amountMap.get("amount") != null) {

                    double amount = 0d;
                    amount += numberFormat.parse(amountMap.get("amount") + "").doubleValue();

                    if (ledgerWiseEstimate.getAcutalFor() != null
                            && ledgerWiseEstimate.getAcutalFor().length() > 0) {

                        amount += numberFormat.parse(ledgerWiseEstimate.getAcutalFor()).doubleValue();
                        ledgerWiseEstimate.setAcutalFor(String.format("%.2f", amount));
                    } else {
                        ledgerWiseEstimate.setAcutalFor(String.format("%.2f", amount));
                    }
                }

            }
        }

        String sanctionedAmount = budgetExpense.getSanctionedAmount() == null ? "0.00"
                : budgetExpense.getSanctionedAmount();
        String appropriationAmount = budgetExpense.getApprovedAmount() == null ? "0.00"
                : budgetExpense.getApprovedAmount();
        String extraProvisionAmount = budgetExpense.getExtraProvisionAmount() == null ? "0.00"
                : budgetExpense.getExtraProvisionAmount();

        double sanctionedamount = numberFormat.parse(sanctionedAmount).doubleValue();
        double appropriationamount = numberFormat.parse(appropriationAmount).doubleValue();
        double extraProvisionamount = numberFormat.parse(extraProvisionAmount).doubleValue();

        sanctionedamount += appropriationamount + extraProvisionamount;

        if (budgetExpense.getFinancialYearId() != null && financialYear != null
                && financialYear.getId() != null && (((LinkedTreeMap) financialYear.getId()).get("$oid") + "").
                equalsIgnoreCase(budgetExpense.getFinancialYearId())) {
            if (budgetExpense.getBudgetTypeName().equalsIgnoreCase("Estimated")) {

                if (ledgerWiseEstimate.getBudgetEstimate() != null) {
                    sanctionedamount += numberFormat.parse(ledgerWiseEstimate.getBudgetEstimate()).doubleValue();
                }
                ledgerWiseEstimate.setBudgetEstimate(String.format("%.2f", sanctionedamount));
            } else if (budgetExpense.getBudgetTypeName().equalsIgnoreCase("Revised")) {

                if (ledgerWiseEstimate.getRevisedEstimate() != null) {
                    sanctionedamount += numberFormat.parse(ledgerWiseEstimate.getRevisedEstimate()).doubleValue();
                }
                ledgerWiseEstimate.setRevisedEstimate(String.format("%.2f", sanctionedamount));
            }
        } else {

            if (ledgerWiseEstimate.getFutureBudgetEstimate() != null) {
                sanctionedamount += numberFormat.parse(ledgerWiseEstimate.getFutureBudgetEstimate()).doubleValue();
            }
            ledgerWiseEstimate.setFutureBudgetEstimate(String.format("%.2f", sanctionedamount));
        }

        if (ledgerWiseEstimate.getAcutalFor() == null || ledgerWiseEstimate.
                getAcutalFor().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setAcutalFor("0.00");
        }

        if (ledgerWiseEstimate.getBudgetEstimate() == null || ledgerWiseEstimate.
                getBudgetEstimate().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setBudgetEstimate("0.00");
        }
        if (ledgerWiseEstimate.getRevisedEstimate() == null || ledgerWiseEstimate.
                getRevisedEstimate().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setRevisedEstimate("0.00");
        }
        if (ledgerWiseEstimate.getFutureBudgetEstimate() == null || ledgerWiseEstimate.
                getFutureBudgetEstimate().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setFutureBudgetEstimate("0.00");
        }

    }

    private void addPreviousYearActualBudgetAmountToBudgetAtGlance(PreviousBudgetAmountDetails budgetAmount,
            HashMap<String, HashMap> ledgerMap, FinancialYear financialYear,
            List<LedgerWiseEstimate> ledgerWiseEstimateList) throws Exception {

        String ledgerNameStr = budgetAmount.getLedger();
        String ledgerId = budgetAmount.getLedgerId();
        LedgerWiseEstimate ledgerWiseEstimate = null;

        Iterator<LedgerWiseEstimate> ledgerWiseEstimateIterator = ledgerWiseEstimateList.iterator();
        while (ledgerWiseEstimateIterator.hasNext()) {
            LedgerWiseEstimate tempLedgerWiseEstimate = ledgerWiseEstimateIterator.next();
            if (tempLedgerWiseEstimate.getLedgerId() != null && ledgerId != null
                    && tempLedgerWiseEstimate.getLedgerId().equals(ledgerId)) {
                ledgerWiseEstimate = tempLedgerWiseEstimate;
                break;
            }
        }
        if (ledgerWiseEstimate == null) {
            ledgerWiseEstimate = new LedgerWiseEstimate();
            ledgerWiseEstimateList.add(ledgerWiseEstimate);
        }

        ledgerWiseEstimate.setLedgerName(ledgerNameStr);
        ledgerWiseEstimate.setLedgerId(ledgerId);
        if (ledgerMap.containsKey(ledgerId)) {
            HashMap<String, Object> amountMap = ledgerMap.get(ledgerId);
            if (amountMap.containsKey("amount")) {
                if (amountMap.get("amount") != null) {

                    double amount = 0d;
                    amount += numberFormat.parse(amountMap.get("amount") + "").doubleValue();

                    if (ledgerWiseEstimate.getAcutalFor() != null
                            && ledgerWiseEstimate.getAcutalFor().length() > 0) {

                        amount += numberFormat.parse(ledgerWiseEstimate.getAcutalFor()).doubleValue();
                        ledgerWiseEstimate.setAcutalFor(String.format("%.2f", amount));
                    } else {
                        ledgerWiseEstimate.setAcutalFor(String.format("%.2f", amount));
                    }
                }

            }
        }

        String sanctionedAmount = budgetAmount.getActualAmount() == null ? "0.00"
                : budgetAmount.getActualAmount();
        double amount = numberFormat.parse(sanctionedAmount).doubleValue();

        if (budgetAmount.getFinancialYear() != null && financialYear != null
                && financialYear.getId() != null && (financialYear.getYear().
                equalsIgnoreCase(budgetAmount.getFinancialYear()))) {
//            if (budgetAmount.getBudgetTypeName().equalsIgnoreCase("Estimated")) {
//
//                if (ledgerWiseEstimate.getBudgetEstimate() != null) {
//                    amount += numberFormat.parse(ledgerWiseEstimate.getBudgetEstimate()).doubleValue();
//                }
//                ledgerWiseEstimate.setBudgetEstimate(String.format("%.2f", amount));
//            } else if (budgetExpense.getBudgetTypeName().equalsIgnoreCase("Revised")) {
//
//                if (ledgerWiseEstimate.getRevisedEstimate() != null) {
//                    amount += numberFormat.parse(ledgerWiseEstimate.getRevisedEstimate()).doubleValue();
//                }
//                ledgerWiseEstimate.setRevisedEstimate(String.format("%.2f", amount));
//            }
        } //else {
//
//            if (ledgerWiseEstimate.getFutureBudgetEstimate() != null) {
//                amount += numberFormat.parse(ledgerWiseEstimate.getFutureBudgetEstimate()).doubleValue();
//            }
//            ledgerWiseEstimate.setFutureBudgetEstimate(String.format("%.2f", amount));
//        }

        if (ledgerWiseEstimate.getAcutalFor() == null || ledgerWiseEstimate.
                getAcutalFor().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setAcutalFor("0.00");
        }

        if (ledgerWiseEstimate.getBudgetEstimate() == null || ledgerWiseEstimate.
                getBudgetEstimate().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setBudgetEstimate("0.00");
        }
        if (ledgerWiseEstimate.getRevisedEstimate() == null || ledgerWiseEstimate.
                getRevisedEstimate().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setRevisedEstimate("0.00");
        }
        if (ledgerWiseEstimate.getFutureBudgetEstimate() == null || ledgerWiseEstimate.
                getFutureBudgetEstimate().equalsIgnoreCase("null")) {
            ledgerWiseEstimate.setFutureBudgetEstimate("0.00");
        }

    }

    private void addHeader(BudgetHeaders header, FinancialYear year) {

        if (null != header) {
            if (this.headerList.contains(header.getName())) {
                return;
            }

            switch (header) {
                case Actual_For:
                    this.headerList.add(header.getName() + " " + +(Integer.parseInt(year.getFromDate().
                            split("/")[2]) - 1) + "-"
                            + (Integer.parseInt(year.getToDate().split("/")[2]) - 1) + " (IN LACS)");
                    break;
                case Budget_Estimate:
                case Revised_Estimate:
                    this.headerList.add(header.getName() + " " + year.getFromDate().
                            split("/")[2] + "-"
                            + year.getToDate().split("/")[2] + " (IN LACS)");
                    break;
                case Future_Budget_Extimate:
                    this.headerList.add(header.getName() + " " + (Integer.parseInt(year.getFromDate().
                            split("/")[2]) + 1) + "-"
                            + (Integer.parseInt(year.getToDate().split("/")[2]) + 1) + " (IN LACS)");
                    break;
                default:
                    break;
            }
        }

    }

    private void calculateSurplusDeficit(ArrayList<BudgetAtAGlance> budgetAtGlanceList) throws Exception {

        NumberFormat numberFormat = NumberFormat.getInstance();

        for (BudgetAtAGlance budgetAtGlance : budgetAtGlanceList) {
            List<LedgerWiseEstimate> ledgerWiseEstimateList = budgetAtGlance.getLedgerWiseEstimate();
            Double surplusDeficitActualFor = 0d;
            Double surplusDeficitBudgetEstimate = 0d;
            Double surplusDeficitBudgetRevised = 0d;
            Double surplusDeficitFutureBudget = 0d;
            for (LedgerWiseEstimate ledgerWiseEstimate : ledgerWiseEstimateList) {
                LinkedHashMap<String, Object> rowData = ledgerWiseEstimate.getRowData();
                surplusDeficitActualFor += numberFormat.parse(rowData.get(
                        LedgerRowKeys.actualFor.toString()) + "").doubleValue();
                surplusDeficitBudgetEstimate += numberFormat.parse(rowData.get(
                        LedgerRowKeys.budgetEstimate.toString()) + "").doubleValue();
                surplusDeficitBudgetRevised += numberFormat.parse(rowData.get(
                        LedgerRowKeys.revisedEstimate.toString()) + "").doubleValue();
                surplusDeficitFutureBudget += numberFormat.parse(rowData.get(
                        LedgerRowKeys.futureBudgetEstimate.toString()) + "").doubleValue();
            }
            LedgerWiseEstimate ledgerWiseEstimate = new LedgerWiseEstimate();
            ledgerWiseEstimate.setLedgerKey(LedgerRowKeys.surplusDeficit);
            ledgerWiseEstimate.setLedgerName(LedgerRowKeys.surplusDeficit.getName());
            ledgerWiseEstimate.setAcutalFor(String.format("%.2f", surplusDeficitActualFor));
            ledgerWiseEstimate.setBudgetEstimate(String.format("%.2f", surplusDeficitBudgetEstimate));
            ledgerWiseEstimate.setRevisedEstimate(String.format("%.2f", surplusDeficitBudgetRevised));
            ledgerWiseEstimate.setFutureBudgetEstimate(String.format("%.2f", surplusDeficitFutureBudget));
            ledgerWiseEstimateList.add(ledgerWiseEstimate);
        }

    }

    private List<CreateBudgetExpense> fetchBudgetExpenseTableRecords(HashMap<String, Object> queryFilterMap,
            String selectedYear) throws Exception {

        String bet = ApplicationConstants.BUDGET_EXPENSE_TABLE;

        RestClient aql = new RestClient();
        String budgetExpenseTable = ApplicationConstants.USG_DB1
                + bet + "`";

        String budgetExpenseQuery = "select " + bet + ".budgetHead, " + bet + ".ddoName, "
                + bet + ".sanctionedAmount, " + bet + ".appropriationValue, "
                + bet + ".extraProvisionAmount, " + bet + ".budgetTypeName, "
                + bet + ".ledgerName, " + bet + ".budgetHeadName, " + bet
                + ".ledgerName, " + bet + ".financialYearId, " + bet
                + ".budgetDate, " + bet + ".ledgerId from " + budgetExpenseTable + " as " + bet;

        Set<Map.Entry<String, Object>> querySet = queryFilterMap.entrySet();
        int i = 0;
        for (Map.Entry<String, Object> queryEntry : querySet) {
            if (i++ == 0) {
                budgetExpenseQuery += " where " + queryEntry.getKey() + "=\"" + queryEntry.getValue() + "\"";
            } else {
                budgetExpenseQuery += " and " + queryEntry.getKey() + "=\"" + queryEntry.getValue() + "\"";
            }
        }

        String nextFinancialYear = numberFormat.parse(selectedYear).intValue() + 1 + "";

        if (budgetExpenseQuery.contains("where")) {
            budgetExpenseQuery += " and " + bet + ".financialYear >=\"" + selectedYear
                    + "\" and " + bet + ".financialYear<=\"" + nextFinancialYear + "\"";
        } else {
            budgetExpenseQuery += " where " + bet + ".financialYear>=\"" + selectedYear
                    + "\" and " + bet + ".financialYear<=\"" + nextFinancialYear + "\"";
        }

        String resultString = aql.getRestData(ApplicationConstants.END_POINT, budgetExpenseQuery);

        List<CreateBudgetExpense> budgetExpenseList = new ArrayList();

        if (resultString != null) {
            budgetExpenseList = new Gson().fromJson(resultString,
                    new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
        }

        return budgetExpenseList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.budgetHead != null ? this.budgetHead.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BudgetAtAGlance other = (BudgetAtAGlance) obj;
        if ((this.budgetHead == null) ? (other.budgetHead != null) : !this.budgetHead.equalsIgnoreCase(other.budgetHead)) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        BudgetAtAGlance baag = new BudgetAtAGlance();
        baag.getBudgetAtGlanceRecords(null);

    }

}
