/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.Group;
import com.accure.finance.dto.JournalVoucher;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerCodeMaster;
import com.accure.finance.dto.LedgerList;
import com.accure.finance.dto.Location;
import com.accure.finance.dto.ManageOpeningBalance;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.finance.dto.TrialBalance;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author user
 */
public class TrialBalanceManager {

    private TreeMap<String, TreeMap> groupMap;
    private List<ManageOpeningBalance> manageOpeningBalanceList;
    private String asOnDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public String getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(String asOnDate) {
        this.asOnDate = asOnDate;
    }

    public enum VoucherType {
        payment, receipt, contra, journal
    }

    public enum TrialBalanceFields {
        openingbalance, transaction, closingbalance, Dr, Cr, voucherDate
    }

    public TreeMap<String, TreeMap> getGroupMap() {
        return groupMap;
    }

    public void setGroupMap(TreeMap<String, TreeMap> groupMap) {
        this.groupMap = groupMap;
    }

    public String getLedgerIdAndCode(String ledgerName) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        map.put("ledgerName", ledgerName);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, map);

        List<LedgerCodeMaster> ledgerCodeList = new Gson().fromJson(result, new TypeToken<List<LedgerCodeMaster>>() {
        }.getType());
        LedgerCodeMaster ledgerCode = ledgerCodeList.get(0);

        String lName = new CashBookReportManager().getLedgerCodeAndLedgerName(((LinkedTreeMap<String, String>) ledgerCode.getId()).get("$oid"), ledgerName);

        return lName;
    }

   
    public TreeMap<String, Object> getTrialBalanceReocrds(String ddo, String location,
            String fundType, String budgetHead, String suppressZero,
            String fromDate, String toDate) {

        TreeMap<String, Object> result = new TreeMap();

        try {

            setAsOnDate(toDate);

            List<String> queryDocument = new ArrayList();

            if (ddo != null && ddo.length() > 0) {
                queryDocument.add("DDO=\"" + ddo + "\"");
            }

            if (location != null && location.length() > 0) {
                queryDocument.add("location=\"" + location + "\"");
            }

            if (fundType != null && fundType.length() > 0) {
                queryDocument.add("fundType=\"" + fundType + "\"");
            }

            if (budgetHead != null && budgetHead.length() > 0) {
                queryDocument.add("budgetHead=\"" + budgetHead + "\"");
            }

            if (toDate != null && fromDate != null && fromDate.length() > 0
                    && toDate.length() > 0) {
                queryDocument.add("voucherDateInMilliSecond<=" + dateFormat.parse(toDate).getTime());
                queryDocument.add("voucherDateInMilliSecond>=" + dateFormat.parse(fromDate).getTime());
            }

            queryDocument.add("status=\"Active\"");
            queryDocument.add("postingStatus=\"Posted\"");

            ArrayList<PaymentVoucher> paymentVoucherList = getVoucherList(
                    VoucherType.payment.toString(), queryDocument);
            ArrayList<ReceiptVoucher> receiptVoucherList = getVoucherList(
                    VoucherType.receipt.toString(), queryDocument);
            ArrayList<ContraVoucher> contraVoucherList = getVoucherList(
                    VoucherType.contra.toString(), queryDocument);
            ArrayList<ContraVoucher> journalVoucherList = getVoucherList(
                    VoucherType.journal.toString(), queryDocument);

            this.groupMap = new TreeMap();

            manageOpeningBalanceList = new ManageOpeningBalanceManager().
                    manageOpeningBalanceList();
            boolean doSuppression = Boolean.parseBoolean(suppressZero);
            populateGroupMap(paymentVoucherList, doSuppression);
            populateGroupMap(receiptVoucherList, doSuppression);
            populateGroupMap(contraVoucherList, doSuppression);
            populateGroupMap(journalVoucherList, doSuppression);

            ArrayList<TrialBalance> trialBalanceList = new ArrayList();

            Set<Map.Entry<String, TreeMap>> groupSet = this.groupMap.entrySet();
            for (Map.Entry<String, TreeMap> groupEntry : groupSet) {
                Set<Map.Entry<String, TreeMap>> ledgerSet = groupEntry.getValue().entrySet();
                for (Map.Entry<String, TreeMap> ledgerEntry : ledgerSet) {
                    TrialBalance trialBalance = new TrialBalance();
                    trialBalance.setGroupName(groupEntry.getKey());
                    String ledgerNames = new TrialBalanceManager().getLedgerIdAndCode(ledgerEntry.getKey());
                    trialBalance.setLedgerName(ledgerNames);

                    TreeMap<String, Object> transactionMap = (TreeMap) ledgerEntry.getValue().get(
                            TrialBalanceFields.transaction.toString());

                    trialBalance.setTransactionDrAmount(transactionMap.get("Dr") + "");
                    trialBalance.setTransactionCrAmount(transactionMap.get("Cr") + "");

                    TreeMap<String, Object> openingBalanceMap = (TreeMap) ledgerEntry.getValue().get(
                            TrialBalanceFields.openingbalance.toString());

                    trialBalance.setOpeningBalanceDrAmount(openingBalanceMap.get("Dr") + "");
                    trialBalance.setOpeningBalanceCrAmount(openingBalanceMap.get("Cr") + "");

                    TreeMap<String, Object> closingBalanceMap = (TreeMap) ledgerEntry.getValue().get(
                            TrialBalanceFields.closingbalance.toString());

                    trialBalance.setClosingBalanceDrAmount(closingBalanceMap.get("Dr") + "");
                    trialBalance.setClosingBalanceCrAmount(closingBalanceMap.get("Cr") + "");

                    String voucherDate = (String) ledgerEntry.getValue().get(
                            TrialBalanceFields.voucherDate.toString());

                    trialBalance.setVoucherDate(voucherDate);
                    trialBalanceList.add(trialBalance);

                }
            }

            TrialBalance.setSortBy(TrialBalance.SortBy.descending);
            Collections.sort(trialBalanceList);

            result.put("result", trialBalanceList);

            Location locationManager = new LocationManager().fetch(location);

            result.put("location", locationManager.getLocationName());
            result.put("asOnDate", getAsOnDate());

        } catch (Exception e) {
        }

        return result;
    }

    public TreeMap<String, Object> getTrialBalanceReocrds1(String ddo, String location,
            String fundType, String budgetHead, String suppressZero,
            String fromDate, String toDate) {

        TreeMap<String, Object> result = new TreeMap();

        try {

            setAsOnDate(toDate);

            List<String> queryDocument = new ArrayList();

            if (ddo != null && ddo.length() > 0) {
                queryDocument.add("DDO=\"" + ddo + "\"");
            }

            if (location != null && location.length() > 0) {
                queryDocument.add("location=\"" + location + "\"");
            }

            if (fundType != null && fundType.length() > 0) {
                queryDocument.add("fundType=\"" + fundType + "\"");
            }

            if (budgetHead != null && budgetHead.length() > 0) {
                queryDocument.add("budgetHead=\"" + budgetHead + "\"");
            }

            if (toDate != null && fromDate != null && fromDate.length() > 0
                    && toDate.length() > 0) {
                queryDocument.add("voucherDateInMilliSecond<" + dateFormat.parse(toDate).getTime());
                queryDocument.add("voucherDateInMilliSecond>=" + dateFormat.parse(fromDate).getTime());
            }

            queryDocument.add("status=\"Active\"");
            queryDocument.add("postingStatus=\"Posted\"");

            ArrayList<PaymentVoucher> paymentVoucherList = getVoucherList(
                    VoucherType.payment.toString(), queryDocument);
            ArrayList<ReceiptVoucher> receiptVoucherList = getVoucherList(
                    VoucherType.receipt.toString(), queryDocument);
            ArrayList<ContraVoucher> contraVoucherList = getVoucherList(
                    VoucherType.contra.toString(), queryDocument);
            ArrayList<ContraVoucher> journalVoucherList = getVoucherList(
                    VoucherType.journal.toString(), queryDocument);

            this.groupMap = new TreeMap();

            manageOpeningBalanceList = new ManageOpeningBalanceManager().
                    manageOpeningBalanceList();
            boolean doSuppression = Boolean.parseBoolean(suppressZero);
            populateGroupMap(paymentVoucherList, doSuppression);
            populateGroupMap(receiptVoucherList, doSuppression);
            populateGroupMap(contraVoucherList, doSuppression);
            populateGroupMap(journalVoucherList, doSuppression);

            ArrayList<TrialBalance> trialBalanceList = new ArrayList();

            Set<Map.Entry<String, TreeMap>> groupSet = this.groupMap.entrySet();
            for (Map.Entry<String, TreeMap> groupEntry : groupSet) {
                Set<Map.Entry<String, TreeMap>> ledgerSet = groupEntry.getValue().entrySet();
                for (Map.Entry<String, TreeMap> ledgerEntry : ledgerSet) {
                    TrialBalance trialBalance = new TrialBalance();
                    trialBalance.setGroupName(groupEntry.getKey());
                    trialBalance.setLedgerName(ledgerEntry.getKey());

                    TreeMap<String, Object> transactionMap = (TreeMap) ledgerEntry.getValue().get(
                            TrialBalanceFields.transaction.toString());

                    trialBalance.setTransactionDrAmount(transactionMap.get("Dr") + "");
                    trialBalance.setTransactionCrAmount(transactionMap.get("Cr") + "");

                    TreeMap<String, Object> openingBalanceMap = (TreeMap) ledgerEntry.getValue().get(
                            TrialBalanceFields.openingbalance.toString());

                    trialBalance.setOpeningBalanceDrAmount(openingBalanceMap.get("Dr") + "");
                    trialBalance.setOpeningBalanceCrAmount(openingBalanceMap.get("Cr") + "");

                    TreeMap<String, Object> closingBalanceMap = (TreeMap) ledgerEntry.getValue().get(
                            TrialBalanceFields.closingbalance.toString());

                    trialBalance.setClosingBalanceDrAmount(closingBalanceMap.get("Dr") + "");
                    trialBalance.setClosingBalanceCrAmount(closingBalanceMap.get("Cr") + "");

                    String voucherDate = (String) ledgerEntry.getValue().get(
                            TrialBalanceFields.voucherDate.toString());

                    trialBalance.setVoucherDate(voucherDate);
                    trialBalanceList.add(trialBalance);

                }
            }

            TrialBalance.setSortBy(TrialBalance.SortBy.descending);
            Collections.sort(trialBalanceList);

            result.put("result", trialBalanceList);

            Location locationManager = new LocationManager().fetch(location);

            result.put("location", locationManager.getLocationName());
            result.put("asOnDate", getAsOnDate());

        } catch (Exception e) {
        }

        return result;
    }

    private void populateOpeningBalance(String group, String ledger) {
        if (group != null && ledger != null) {
            if (this.groupMap.containsKey(group)) {
                TreeMap<String, TreeMap> ledgerMap = this.groupMap.get(group);
                if (ledgerMap.containsKey(ledger)) {
                    TreeMap<String, TreeMap> trialBalanceMap = ledgerMap.get(ledger);
                    if (!trialBalanceMap.containsKey(TrialBalanceFields.openingbalance.toString())) {
                        TreeMap<String, Object> openingBalance = new TreeMap();
                        for (ManageOpeningBalance manageOpeningBalance : this.manageOpeningBalanceList) {
//                            if (manageOpeningBalance.getLedgerName() != null
//                                    && manageOpeningBalance.getLedgerName().equalsIgnoreCase(ledger)) {
                            String tempBankName = null;
                            if (manageOpeningBalance.getLedgerName().contains("(")) {
                                tempBankName = manageOpeningBalance.getLedgerName().substring(0, manageOpeningBalance.getLedgerName().indexOf("("));
                            }
                            if (tempBankName != null
                                    && tempBankName.equalsIgnoreCase(ledger)) {
                                openingBalance.put(manageOpeningBalance.getAmountType(), Double.parseDouble(
                                        manageOpeningBalance.getAmount()));
                                break;
                            }
                        }
                        if (!openingBalance.containsKey("Dr")) {
                            openingBalance.put("Dr", 0d);
                        }
                        if (!openingBalance.containsKey("Cr")) {
                            openingBalance.put("Cr", 0d);
                        }
                        trialBalanceMap.put(TrialBalanceFields.openingbalance.toString(), openingBalance);
                    }
                }
            }
        }
    }

    private void populateClosingBalance(String group, String ledger, boolean doSuppression) {
        if (group != null && ledger != null) {
            TreeMap<String, TreeMap> ledgerMap = this.groupMap.get(group);
            if (ledgerMap != null) {
                TreeMap<String, TreeMap> trialBalanceMap = ledgerMap.get(ledger);
                if (trialBalanceMap != null) {
                    TreeMap<String, Object> openingBalance = trialBalanceMap.get(
                            TrialBalanceFields.openingbalance.toString());
                    TreeMap<String, Object> transaction = trialBalanceMap.get(
                            TrialBalanceFields.transaction.toString());
                    Set<Map.Entry<String, Object>> entrySet = openingBalance.entrySet();
                    TreeMap<String, Object> closingBalance = new TreeMap();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        Double transactionValue = Double.parseDouble(transaction.get(entry.getKey()) == null ? "0"
                                : transaction.get(entry.getKey()) + "");
                        Double openingBalanceValue = (Double.parseDouble(entry.getValue() == null ? "0"
                                : entry.getValue() + ""));

                        closingBalance.put(entry.getKey(), (openingBalanceValue + transactionValue));
                    }

                    if (!closingBalance.containsKey("Dr")) {
                        closingBalance.put("Dr", 0d);
                    }
                    if (!closingBalance.containsKey("Cr")) {
                        closingBalance.put("Cr", 0d);
                    }
                    if (((Double) closingBalance.get("Dr")) == 0 && ((Double) closingBalance.get("Cr")) == 0
                            && doSuppression) {
                        ledgerMap.remove(ledger);
                    }
                    trialBalanceMap.put(TrialBalanceFields.closingbalance.toString(), closingBalance);
                }
            }
        }
    }

    private void populateGroupMap(ArrayList voucherList, boolean doSuppression) throws Exception {
        LedgerManager ledgerManager = new LedgerManager();
        GroupManager groupManager = new GroupManager();
        List<LedgerList> ledgerList = null;
        String voucherDate = null;

        for (Object object : voucherList) {

            if (object instanceof PaymentVoucher) {
                voucherDate = ((PaymentVoucher) object).getVoucherDate();
                ledgerList = ((PaymentVoucher) object).getLedgerList();
            } else if (object instanceof ReceiptVoucher) {
                voucherDate = ((ReceiptVoucher) object).getVoucherDate();
                ledgerList = ((ReceiptVoucher) object).getLedgerList();
            } else if (object instanceof ContraVoucher) {
                voucherDate = ((ContraVoucher) object).getVoucherDate();
                ledgerList = ((ContraVoucher) object).getLedgerList();
            } else if (object instanceof JournalVoucher) {
                voucherDate = ((JournalVoucher) object).getVoucherDate();
                ledgerList = ((JournalVoucher) object).getLedgerList();
            }
            if (ledgerList != null) {
                for (LedgerList pvLedger : ledgerList) {

                    String groupString = groupManager.fetch(pvLedger.getGroup());

                    Ledger ledger = ledgerManager.fetchLedger(pvLedger.getLedger());
                    if (groupString != null && ledger != null) {
                        Group group = new Gson().fromJson(groupString, new TypeToken<Group>() {
                        }.getType());
                        if (this.groupMap.containsKey(group.getGroupName())) {
                            TreeMap<String, TreeMap> ledgerMap = this.groupMap.get(group.getGroupName());

                            if (ledgerMap.containsKey(ledger.getLedgerName())) {

                                TreeMap<String, TreeMap> trialBalanceFieldsMap
                                        = ledgerMap.get(ledger.getLedgerName());

                                if (trialBalanceFieldsMap.containsKey(TrialBalanceFields.transaction.toString())) {
                                    populateTransaction(trialBalanceFieldsMap, pvLedger);
                                    populateClosingBalance(group.getGroupName(), ledger.getLedgerName(), doSuppression);

                                } else {
                                    TreeMap<String, Object> transaction = new TreeMap();
                                    if (pvLedger.getDrCr() != null && pvLedger.getDrCr().equalsIgnoreCase("Dr")) {
                                        transaction.put(pvLedger.getDrCr(), pvLedger.getDrAmount());
                                    } else {
                                        transaction.put(pvLedger.getDrCr(), pvLedger.getCrAmount());
                                    }
                                    ledgerMap.put(ledger.getLedgerName(), transaction);
                                    populateClosingBalance(group.getGroupName(), ledger.getLedgerName(), doSuppression);
                                }
                            } else {
                                populateLedger(pvLedger, ledger, group, voucherDate);
                                populateOpeningBalance(group.getGroupName(),
                                        ledger.getLedgerName());
                                populateClosingBalance(group.getGroupName(), ledger.getLedgerName(), doSuppression);
                            }
                        } else {
                            populateLedger(pvLedger, ledger, group, voucherDate);
                            populateOpeningBalance(group.getGroupName(),
                                    ledger.getLedgerName());
                            populateClosingBalance(group.getGroupName(), ledger.getLedgerName(), doSuppression);
                        }
                    }
                }
            }
        }
    }

    private void populateTransaction(TreeMap<String, TreeMap> trialBalanceFieldsMap, LedgerList pvLedger) {
        TreeMap<String, Object> transaction
                = trialBalanceFieldsMap.get(TrialBalanceFields.transaction.toString());
        if (transaction.containsKey(pvLedger.getDrCr())) {
            Double value = Double.parseDouble(transaction.get(pvLedger.getDrCr()) + "");
            if (pvLedger.getDrCr() != null && pvLedger.getDrCr().equalsIgnoreCase("Dr")) {
                value += Double.parseDouble(pvLedger.getDrAmount() + "");
            } else if (pvLedger.getDrCr() != null && pvLedger.getDrCr().equalsIgnoreCase("Cr")) {
                value += Double.parseDouble(pvLedger.getCrAmount() + "");
            }
            transaction.put(pvLedger.getDrCr(), value);
        } else if (pvLedger.getDrCr() != null && pvLedger.getDrCr().equalsIgnoreCase("Dr")) {
            transaction.put(pvLedger.getDrCr(), pvLedger.getDrAmount());
        } else {
            transaction.put(pvLedger.getDrCr(), pvLedger.getCrAmount());
        }
    }

    private void populateLedger(LedgerList pvLedger, Ledger ledger, Group group, String voucherDate) {

        TreeMap<String, Object> trialBalanceFieldsMap = new TreeMap();
        TreeMap<String, Object> transaction = new TreeMap();
        if (pvLedger.getDrCr() != null && pvLedger.getDrCr().equalsIgnoreCase("Dr")) {
            transaction.put(pvLedger.getDrCr(), pvLedger.getDrAmount());
        } else {
            transaction.put(pvLedger.getDrCr(), pvLedger.getCrAmount());
        }

        if (!transaction.containsKey("Dr")) {
            transaction.put("Dr", 0d);
        }
        if (!transaction.containsKey("Cr")) {
            transaction.put("Cr", 0d);
        }

        trialBalanceFieldsMap.put(TrialBalanceFields.transaction.toString(), transaction);
        trialBalanceFieldsMap.put(TrialBalanceFields.voucherDate.toString(), voucherDate);

        TreeMap<String, TreeMap> ledgerMap = this.groupMap.get(group.getGroupName());

        if (ledgerMap == null) {
            ledgerMap = new TreeMap();
        }

        ledgerMap.put(ledger.getLedgerName(), trialBalanceFieldsMap);
        this.groupMap.put(group.getGroupName(), ledgerMap);

    }

    public ArrayList getVoucherList(String voucherType, List<String> queryDocument) throws Exception {

        String voucherTable = null;

        if (voucherType == null) {
            return null;
        }
        Type type = null;
        if (voucherType.equalsIgnoreCase(VoucherType.payment.toString())) {
            voucherTable = ApplicationConstants.PAYMENT_VOUCHER_TABLE;
            type = new TypeToken<List<PaymentVoucher>>() {
            }.getType();
        } else if (voucherType.equalsIgnoreCase(VoucherType.receipt.toString())) {
            voucherTable = ApplicationConstants.RECEIPT_VOUCHER_TABLE;
            type = new TypeToken<List<ReceiptVoucher>>() {
            }.getType();
        } else if (voucherType.equalsIgnoreCase(VoucherType.contra.toString())) {
            voucherTable = ApplicationConstants.CONTRA_VOUCHER_TABLE;
            type = new TypeToken<List<ContraVoucher>>() {
            }.getType();
        } else if (voucherType.equalsIgnoreCase(VoucherType.journal.toString())) {
            voucherTable = ApplicationConstants.JOURNAL_VOUCHER_TABLE;
            type = new TypeToken<List<JournalVoucher>>() {
            }.getType();
        }

        String voucherTableForQuasar = ApplicationConstants.USG_DB1
                + voucherTable + "`";

        String query = "select * from " + voucherTableForQuasar;

        if (queryDocument != null) {
            int i = 0;
            for (String queryFilter : queryDocument) {
                if (i++ == 0) {
                    query += " where " + queryFilter;
                } else {
                    query += " and " + queryFilter;
                }
            }
        }
        query += " Order by voucherDate";

        RestClient rc = new RestClient();
        String resultData = rc.getRestData(ApplicationConstants.END_POINT, query);

        ArrayList resultList = new ArrayList();

        if (voucherType.equalsIgnoreCase(VoucherType.payment.toString())) {
            List<PaymentVoucher> pv = new Gson().fromJson(resultData, type);
            resultList.addAll(pv);
        }
        if (voucherType.equalsIgnoreCase(VoucherType.receipt.toString())) {
            List<ReceiptVoucher> pv = new Gson().fromJson(resultData, type);
            resultList.addAll(pv);
        }
        if (voucherType.equalsIgnoreCase(VoucherType.contra.toString())) {
            List<ContraVoucher> pv = new Gson().fromJson(resultData, type);
            resultList.addAll(pv);
        }
        if (voucherType.equalsIgnoreCase(VoucherType.journal.toString())) {
            List<JournalVoucher> pv = new Gson().fromJson(resultData, type);
            resultList.addAll(pv);
        }

        return resultList;
    }

}
