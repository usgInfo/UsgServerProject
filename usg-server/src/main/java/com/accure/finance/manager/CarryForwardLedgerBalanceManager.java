/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.CarryForwardLedgerBalance;
import com.accure.finance.dto.ManageOpeningBalance;
import com.accure.finance.dto.TrialBalance;
import static com.accure.finance.manager.BankBookReportManager.roundTwoDecimals;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author accure
 */
public class CarryForwardLedgerBalanceManager {

    public Map<String, Object> search(String ledgerId, String ddoId, String locationId, String fromDate, String toDate) throws Exception {
        Map<String, Object> resultmap = new HashMap<String, Object>();
        double closingBalance;
        double openingBalance = 0.00;
        double transactionCrAmount = 0.00;
        double transactionDrAmount = 0.00;
        String openingBal = "0.00";
        List<ManageOpeningBalance> manageOpeningList = new ArrayList<ManageOpeningBalance>();
        String amountType = "";
        String ledgerName = "";
        String str = "no";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ledgerId", ledgerId);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String manageOpeningBalanceJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap);
        if (manageOpeningBalanceJson != null) {
            manageOpeningList = new Gson().fromJson(manageOpeningBalanceJson, new TypeToken<List<ManageOpeningBalance>>() {
            }.getType());

            for (ManageOpeningBalance manageOpeningBalance : manageOpeningList) {
                amountType = manageOpeningBalance.getAmountType();
                ledgerName = manageOpeningBalance.getLedgerName();
                openingBal = manageOpeningBalance.getAmount();
            }
            resultmap.put("manageOpening", manageOpeningList.get(0));

            TreeMap<String, Object> trialBalanceResult = new TrialBalanceManager().getTrialBalanceReocrds(
                    ddoId, locationId, "",
                    "", "", fromDate, toDate);

            Set<String> sets = trialBalanceResult.keySet();
            for (String key : sets) {
                if (key.equals("result")) {
                    ArrayList<TrialBalance> resultList = (ArrayList) trialBalanceResult
                            .get("result");
                    String ledgerNameStr;
                    String closingCrAmount = "0.00";
                    String closingDrAmount = "0.00";
//                    if (resultList.isEmpty()) {
//                        closingCrAmount = "0.00";
//                        closingDrAmount = "0.00";
////                        closingBalance = openingBalance;
//                    }
                    for (TrialBalance groupEntries : resultList) {
                        ledgerNameStr = groupEntries.getLedgerName();
                        String tempBankName = null;
//                        if (ledgerName.contains("(")) {
//                            tempBankName = ledgerName.substring(0, ledgerName.indexOf("("));
//                        }
//                        if (tempBankName.equalsIgnoreCase(ledgerNameStr)) {
                            if (ledgerName.equalsIgnoreCase(ledgerNameStr)) {
                               str = "yes"; 
                            transactionCrAmount = Double.parseDouble(groupEntries.getTransactionCrAmount());
                            transactionDrAmount = Double.parseDouble(groupEntries.getTransactionDrAmount());
                            if (amountType.equalsIgnoreCase("Cr")) {
                                closingCrAmount = groupEntries.getTransactionCrAmount();
                                openingBalance = Double.parseDouble(groupEntries.getOpeningBalanceCrAmount());
                                closingBalance = openingBalance + transactionCrAmount - transactionDrAmount;
                                String closingBalance1 = roundTwoDecimalPoints(closingBalance);
                                String closingBalance2 = closingBalance1 + " Cr";
                                resultmap.put("closingCrAmount", roundTwoDecimalPoints(Double.parseDouble(closingCrAmount)));
                                resultmap.put("closingDrAmount", roundTwoDecimalPoints(transactionDrAmount));
//                                resultmap.put("closingAmount", roundTwoDecimalPoints(closingBalance));
                                resultmap.put("closingAmount", closingBalance2);
                            } else if (amountType.equalsIgnoreCase("Dr")) {
//                                closingDrAmount = groupEntries.getClosingBalanceDrAmount();
                                closingDrAmount = groupEntries.getTransactionDrAmount();
                                openingBalance = roundTwoDecimals(Double.parseDouble(groupEntries.getOpeningBalanceDrAmount()));
                                closingBalance = openingBalance + transactionDrAmount - transactionCrAmount;
                                String closingBalance1 = roundTwoDecimalPoints(closingBalance);
                                String closingBalance2 = closingBalance1 + " Dr";
                                resultmap.put("closingCrAmount", roundTwoDecimalPoints(transactionCrAmount));
                                resultmap.put("closingDrAmount", roundTwoDecimalPoints(Double.parseDouble(closingDrAmount)));
//                                resultmap.put("closingAmount", roundTwoDecimalPoints(closingBalance));
                                resultmap.put("closingAmount", closingBalance2);
                            }
                        }
                            else{
                                if(str.equalsIgnoreCase("no")){
                                transactionCrAmount = Double.parseDouble(closingCrAmount);
                                transactionDrAmount = Double.parseDouble(closingDrAmount);
                                closingBalance = Double.parseDouble(openingBal) + transactionCrAmount + transactionDrAmount;
                                if(amountType != null){
                                    String closingBalance1 = roundTwoDecimalPoints(closingBalance);
                                    String closingBalance2 = closingBalance1 + " " +amountType;
                                    resultmap.put("closingCrAmount", closingCrAmount);
                                    resultmap.put("closingDrAmount", closingDrAmount);
                                    resultmap.put("closingAmount", closingBalance2);
                                }
                                else{
                                resultmap.put("closingCrAmount", closingCrAmount);
                                resultmap.put("closingDrAmount", closingDrAmount);
                                resultmap.put("closingAmount", roundTwoDecimalPoints(closingBalance));
                                }
                                }
                            }
                    }
                    if (resultList.isEmpty()) {
                                transactionCrAmount = Double.parseDouble(closingCrAmount);
                                transactionDrAmount = Double.parseDouble(closingDrAmount);
                                closingBalance = Double.parseDouble(openingBal) + transactionCrAmount + transactionDrAmount;
                                if(amountType != null){
                                    String closingBalance1 = roundTwoDecimalPoints(closingBalance);
                                    String closingBalance2 = closingBalance1 + " " +amountType;
                                    resultmap.put("closingCrAmount", closingCrAmount);
                                    resultmap.put("closingDrAmount", closingDrAmount);
                                    resultmap.put("closingAmount", closingBalance2);
                                }
                                else{
                                resultmap.put("closingCrAmount", closingCrAmount);
                                resultmap.put("closingDrAmount", closingDrAmount);
                                resultmap.put("closingAmount", roundTwoDecimalPoints(closingBalance));
                                }
                    }

                }

            }
        }

        return resultmap;

    }

//    public static void main(String args[]) throws Exception {
//        CarryForwardLedgerBalanceManager cb = new CarryForwardLedgerBalanceManager();
////        mb.fetchLedgerFromVouchers("");
//        System.out.println("result-----------------" + new Gson().toJson(cb.search("584509fdd0bd0c24361f1bc1", "582f34960c92ec57796a1e13", "582f34a70c92ec57796a1e17", "", "")));
//    }
//    public String updateLedgerBalance(CarryForwardLedgerBalance closingBalance, String userId) throws Exception {
//        double closeBalance = 0.0;
//        if (closingBalance.getClosingBalance() != null) {
//            closeBalance = Double.parseDouble(closingBalance.getClosingBalance());
//        }
//        ManageOpeningBalance manageBalance = new ManageOpeningBalance();
//        String returnId = "";
//        String colCarryForwardFlag = closingBalance.getCollapseOrCarryForward();
//
//        if (colCarryForwardFlag.equalsIgnoreCase("Carry Forward")) {
//            if (closeBalance > 0) {
//                manageBalance.setAmountType("Dr");
//                if (closingBalance.getClosingBalance() != null) {
//                    manageBalance.setAmount(closingBalance.getClosingBalance());
//                }
//                manageBalance.setLedgerId(closingBalance.getLedgerId());
//                manageBalance.setLedgerName(closingBalance.getLedgerName());
//                manageBalance.setFinancialYear(closingBalance.getYearId());
//                manageBalance.setFinancialYearId(closingBalance.getYear());
//                manageBalance.setTransferStatus(ApplicationConstants.CARRY_FORWARD);
//                manageBalance.setCreateDate(System.currentTimeMillis() + "");
//                manageBalance.setCreatedBy(userId);
//                manageBalance.setStatus(ApplicationConstants.ACTIVE);
//                String manageOpeningId = new Gson().toJson(manageBalance);
//                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
//            } else {
//                manageBalance.setAmountType("Cr");
//                if (closingBalance.getClosingBalance() != null) {
//                    manageBalance.setAmount(closingBalance.getClosingBalance().substring(1, closingBalance.getClosingBalance().length()));
//                } else {
//
//                }
//                manageBalance.setLedgerId(closingBalance.getLedgerId());
//                manageBalance.setLedgerName(closingBalance.getLedgerName());
//                manageBalance.setFinancialYear(closingBalance.getYearId());
//                manageBalance.setFinancialYearId(closingBalance.getYear());
//                manageBalance.setTransferStatus(ApplicationConstants.CARRY_FORWARD);
//                manageBalance.setCreateDate(System.currentTimeMillis() + "");
//                manageBalance.setCreatedBy(userId);
//                manageBalance.setStatus(ApplicationConstants.ACTIVE);
//                String manageOpeningId = new Gson().toJson(manageBalance);
//                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
//            }
//
//        } else if (colCarryForwardFlag.equalsIgnoreCase("Lapse")) {
//            if (closeBalance > 0) {
//                manageBalance.setAmountType("Dr");
//                manageBalance.setAmount("");
//                manageBalance.setLedgerId(closingBalance.getLedgerId());
//                manageBalance.setLedgerName(closingBalance.getLedgerName());
//                manageBalance.setFinancialYear(closingBalance.getYearId());
//                manageBalance.setTransferStatus(ApplicationConstants.LAPSE);
//                manageBalance.setCreateDate(System.currentTimeMillis() + "");
//                manageBalance.setCreatedBy(userId);
//                manageBalance.setStatus(ApplicationConstants.ACTIVE);
//                String manageOpeningId = new Gson().toJson(manageBalance);
//                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
//            } else {
//                manageBalance.setAmountType("Cr");
//                manageBalance.setAmount("");
//                manageBalance.setLedgerId(closingBalance.getLedgerId());
//                manageBalance.setLedgerName(closingBalance.getLedgerName());
//                manageBalance.setFinancialYear(closingBalance.getYearId());
//                manageBalance.setTransferStatus(ApplicationConstants.LAPSE);
//                manageBalance.setCreateDate(System.currentTimeMillis() + "");
//                manageBalance.setCreatedBy(userId);
//                manageBalance.setStatus(ApplicationConstants.ACTIVE);
//                String manageOpeningId = new Gson().toJson(manageBalance);
//                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
//            }
//        }
//        return returnId;
//    }
    public String updateLedgerBalance(CarryForwardLedgerBalance closingBalance, String userId) throws Exception {
        double closeBalance = 0.0;
        if (closingBalance.getClosingBalance() != null) {
            
//            closeBalance = Double.parseDouble(closingBalance.getClosingBalance());
            String clBalAmt = null;
                        if (closingBalance.getClosingBalance().contains(" Dr")) {
                            clBalAmt = closingBalance.getClosingBalance().substring(0, closingBalance.getClosingBalance().indexOf("Dr"));
                            closeBalance = Double.parseDouble(clBalAmt);
                        }
                        else if (closingBalance.getClosingBalance().contains(" Cr")) {
                            clBalAmt = closingBalance.getClosingBalance().substring(0, closingBalance.getClosingBalance().indexOf("Cr"));
                            closeBalance = Double.parseDouble(clBalAmt);
                        }
                        else
                        {
                            closeBalance = Double.parseDouble(closingBalance.getClosingBalance());
                        }
        }
        ManageOpeningBalance manageBalance = new ManageOpeningBalance();
        String returnId = "";
        String colCarryForwardFlag = closingBalance.getCollapseOrCarryForward();
        String openingBal = closingBalance.getOpeningBal();
        String ledgerId = closingBalance.getLedgerId();
        String finYear = closingBalance.getYear();
        int finYearNum = Integer.parseInt(finYear);
        int finYearNum1 = finYearNum - 1;
        String finYear1 = Integer.toString(finYearNum1);
        String finYearId1 = closingBalance.getYearId();
         HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put("ledgerId", ledgerId);
        conditionMap1.put("financialYearId", finYear1);
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String openingBalJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap1);
        if(openingBalJson != null)
        {
            List<ManageOpeningBalance> openingBalanceList = new Gson().fromJson(openingBalJson, new TypeToken<List<ManageOpeningBalance>>() {
            }.getType());
            ManageOpeningBalance openingBalanceObj = openingBalanceList.get(0);

            openingBal = openingBalanceObj.getAmount();
        }
        double openingBalNum = Double.parseDouble(openingBal);

        if (colCarryForwardFlag.equalsIgnoreCase("Carry Forward")) {
            //  if (closeBalance > 0) {
            if (closingBalance.getAmountType().equalsIgnoreCase("Dr")) {
                if (closingBalance.getDrAmount() == null || closingBalance.getDrAmount() == "") {
                    closingBalance.setDrAmount("0.0");
                }
                if (closingBalance.getCrAmount() == null || closingBalance.getCrAmount() == "") {
                    closingBalance.setCrAmount("0.0");
                }
                Double amount = openingBalNum + Double.parseDouble(closingBalance.getDrAmount()) - Double.parseDouble(closingBalance.getCrAmount());
                String finalAmount = String.valueOf(amount);
                manageBalance.setLedgerId(closingBalance.getLedgerId());
                manageBalance.setAmount(roundTwoDecimalPoints(Double.parseDouble(finalAmount)));
                manageBalance.setLedgerName(closingBalance.getLedgerName());
                manageBalance.setFinancialYear(closingBalance.getYearId());
                manageBalance.setAmountType(closingBalance.getAmountType());
                manageBalance.setFinancialYearId(closingBalance.getYear());
                manageBalance.setTransferStatus(ApplicationConstants.CARRY_FORWARD);
                manageBalance.setCreateDate(System.currentTimeMillis() + "");
                manageBalance.setCreatedBy(userId);
                manageBalance.setStatus(ApplicationConstants.ACTIVE);
                String manageOpeningId = new Gson().toJson(manageBalance);
                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
            } else {
                if (closingBalance.getDrAmount() == null || closingBalance.getDrAmount() == "") {
                    closingBalance.setDrAmount("0.0");
                }
                if (closingBalance.getCrAmount() == null || closingBalance.getCrAmount() == "") {
                    closingBalance.setCrAmount("0.0");
                }
                Double amount = Double.parseDouble(closingBalance.getOpeningBal()) + Double.parseDouble(closingBalance.getCrAmount()) - Double.parseDouble(closingBalance.getDrAmount());
                String finalAmount = String.valueOf(amount);
//                manageBalance.setAmount(finalAmount);
                manageBalance.setAmount(roundTwoDecimalPoints(Double.parseDouble(finalAmount)));
                manageBalance.setLedgerId(closingBalance.getLedgerId());
                manageBalance.setLedgerName(closingBalance.getLedgerName());
                manageBalance.setFinancialYear(closingBalance.getYearId());
                manageBalance.setAmountType(closingBalance.getAmountType());
                manageBalance.setFinancialYearId(closingBalance.getYear());
                manageBalance.setTransferStatus(ApplicationConstants.CARRY_FORWARD);
                manageBalance.setCreateDate(System.currentTimeMillis() + "");
                manageBalance.setCreatedBy(userId);
                manageBalance.setStatus(ApplicationConstants.ACTIVE);
                String manageOpeningId = new Gson().toJson(manageBalance);
                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
            }

        } else if (colCarryForwardFlag.equalsIgnoreCase("Lapse")) {
            if (closeBalance > 0) {
                manageBalance.setAmountType("Dr");
                manageBalance.setAmount("");
                manageBalance.setLedgerId(closingBalance.getLedgerId());
                manageBalance.setLedgerName(closingBalance.getLedgerName());
                manageBalance.setFinancialYear(closingBalance.getYearId());
                manageBalance.setTransferStatus(ApplicationConstants.LAPSE);
                manageBalance.setCreateDate(System.currentTimeMillis() + "");
                manageBalance.setCreatedBy(userId);
                manageBalance.setStatus(ApplicationConstants.ACTIVE);
                String manageOpeningId = new Gson().toJson(manageBalance);
                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
            } else {
                manageBalance.setAmountType("Cr");
                manageBalance.setAmount("");
                manageBalance.setLedgerId(closingBalance.getLedgerId());
                manageBalance.setLedgerName(closingBalance.getLedgerName());
                manageBalance.setFinancialYear(closingBalance.getYearId());
                manageBalance.setTransferStatus(ApplicationConstants.LAPSE);
                manageBalance.setCreateDate(System.currentTimeMillis() + "");
                manageBalance.setCreatedBy(userId);
                manageBalance.setStatus(ApplicationConstants.ACTIVE);
                String manageOpeningId = new Gson().toJson(manageBalance);
                returnId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningId);
            }
        }
        return returnId;
    }

//    public static void main(String... args){
//    String str="-900";
//        System.out.println(str.substring(1,str.length()));
//    }
public static double roundTwoDecimals(double amount) {
        if (amount == 0 || amount == -1) {
            return amount;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double output = Double.parseDouble(df.format(amount) + "");
        return output;
    }

public static String roundTwoDecimalPoints(double amount) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String output = nf.format(amount).replaceAll(",", "");
        return output;
    }
}
