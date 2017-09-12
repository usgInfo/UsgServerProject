/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.budget.dto.FinancialYear;
import com.accure.budget.dto.FundType;
import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.Group;
import com.accure.finance.dto.JournalVoucher;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerList;
import com.accure.finance.dto.ManageOpeningBalance;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author accure
 */
public class ManageOpeningBalanceManager {

    public String createOpeningBalance(ManageOpeningBalance manageBalance, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        
        String finYearId = manageBalance.getFinancialYear();
        String FYJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, finYearId);
        if (FYJson != null) {

                        List<FinancialYear> finYearList = new Gson().fromJson(FYJson, new TypeToken<List<FinancialYear>>() {
                        }.getType());

                        FinancialYear yearObj = finYearList.get(0);
                        finYearId=yearObj.getYear();
                       
                    }
        String opBal = manageBalance.getAmount();
        manageBalance.setAmount(roundTwoDecimalPoints(Double.parseDouble(opBal)));
        manageBalance.setCreateDate(System.currentTimeMillis() + "");
        manageBalance.setUpdateDate(System.currentTimeMillis() + "");
        manageBalance.setStatus(ApplicationConstants.ACTIVE);
        manageBalance.setCreatedBy(userName);
        manageBalance.setFinancialYearId(finYearId);

        String manageOpeningBalance = new Gson().toJson(manageBalance);

        String manageOpeningId = DBManager.getDbConnection().insert(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, manageOpeningBalance);
        if (manageOpeningId != null) {
            return manageOpeningId;
        }
        return null;
    }

    public String updateOpeningBalance(ManageOpeningBalance manageBalance, String userId, String Id) throws Exception {
        String result = "";
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        ManageOpeningBalance openingBalDB = fetch(Id);
        openingBalDB.setAmount(manageBalance.getAmount());
        openingBalDB.setCreateDate(System.currentTimeMillis() + "");
        openingBalDB.setUpdateDate(System.currentTimeMillis() + "");
        openingBalDB.setStatus(ApplicationConstants.ACTIVE);
        openingBalDB.setUpdatedBy(userName);
        openingBalDB.setAmountType(manageBalance.getAmountType());

        String manageOpeningBalance = new Gson().toJson(openingBalDB);

//        <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        String ledgerId = manageBalance.getLedgerId();
        String finYear =  manageBalance.getFinancialYear();
       
        HashMap<String, String> conditionMap2 = new HashMap<String, String>();
        conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap2.put("_id", finYear);

//        String FYJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap2);
        String FYJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, manageBalance.getFinancialYear());
        
        if (FYJson != null) {

                        List<FinancialYear> finYearList = new Gson().fromJson(FYJson, new TypeToken<List<FinancialYear>>() {
                        }.getType());

                        FinancialYear yearObj = finYearList.get(0);
                        finYear=yearObj.getYear();
                       
                    }
        int finYearInt = Integer.parseInt(finYear);
        finYearInt = finYearInt + 1;
        String finYear1 = Integer.toString(finYearInt);
        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put("ledgerId", ledgerId);
        conditionMap1.put("financialYearId", finYear1);
        conditionMap1.put("transferStatus", "CarryForward");
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String ledgerJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap1);

        
        boolean status = false;
//<<<<<<<<<<<<<<<<<<<<<<<<
        
        if(ledgerJson == null || ledgerJson.isEmpty()){
        status = DBManager.getDbConnection().update(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, Id, manageOpeningBalance);
        }
        else
        {
            result = ApplicationConstants.CARRY_FORWARD_LAPSE;
        }
        if (status) {
            result = ApplicationConstants.SUCCESS;
        } else {
//            if(ledgerJson != null)
//            {
//                result = ApplicationConstants.CARRY_FORWARD_LAPSE;
//            }else{
            result = ApplicationConstants.FAIL;
//            }
        }
        return result;
    }

    public List<ManageOpeningBalance> manageOpeningBalanceList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String manageOpeningBalanceJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap);
        List<ManageOpeningBalance> manageOpeningList = new Gson().fromJson(manageOpeningBalanceJson, new TypeToken<List<ManageOpeningBalance>>() {
        }.getType());
        return manageOpeningList;
    }

    public ManageOpeningBalance fetch(String openingBalanceId) throws Exception {
        if (openingBalanceId == null || openingBalanceId.isEmpty()) {
            return null;
        }
        String openingBalanceJson = DBManager.getDbConnection().fetch(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, openingBalanceId);
        if (openingBalanceJson == null || openingBalanceJson.isEmpty()) {
            return null;
        }
        List<ManageOpeningBalance> manageOpeningBalanceList = new Gson().fromJson(openingBalanceJson, new TypeToken<List<ManageOpeningBalance>>() {
        }.getType());
        if (manageOpeningBalanceList == null || manageOpeningBalanceList.isEmpty()) {
            return null;
        }
        return manageOpeningBalanceList.get(0);

    }

    public boolean deleteManageOpeningBalance(String openingBalanceId, String currentUserLogin) throws Exception {

        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        ManageOpeningBalance manageOpeningBalance = fetch(openingBalanceId);
        manageOpeningBalance.setStatus(ApplicationConstants.DELETE);
        manageOpeningBalance.setUpdateDate(System.currentTimeMillis() + "");
        manageOpeningBalance.setUpdatedBy(userName);

        String openingBalanceJson = new Gson().toJson(manageOpeningBalance);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, openingBalanceId, openingBalanceJson);
        if (status) {
            return true;
        }
        return false;
    }

    public String getFundandGroup(String ledger) throws Exception {

        String result = DBManager.getDbConnection().fetch(ApplicationConstants.LEDGER_TABLE, ledger);

        List<Ledger> ledgerList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
        }.getType());
        for (Ledger cl : ledgerList) {
            if ((cl.getFundType() != null)) {
                try {
                    String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE, cl.getFundType());
                    if (gaJson1 != null) {
                        List<FundType> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<FundType>>() {
                        }.getType());
                        FundType gal1 = gaList1.get(0);
                        cl.setFundTypeName(gal1.getDescription());
                    }
                } catch (Exception e) {
                }
            }
            if ((cl.getUnderGroup() != null)) {
                try {
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.GROUP_TABLE, cl.getUnderGroup());
                    if (gaJson != null) {
                        List<Group> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Group>>() {
                        }.getType());
                        Group gal = gaList.get(0);
                        cl.setUnderGroupName(gal.getGroupName());
                    }
                } catch (Exception e) {
                }
            }
        }
        return new Gson().toJson(ledgerList);
    }

    public List<ManageOpeningBalance> search(String ledger, String financialYear) throws Exception {

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ledgerId", ledger);
        conditionMap.put("financialYear", financialYear);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String manageOpeningBalanceJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap);
        List<ManageOpeningBalance> manageOpeningList = new Gson().fromJson(manageOpeningBalanceJson, new TypeToken<List<ManageOpeningBalance>>() {
        }.getType());

        return manageOpeningList;
    }

    public boolean fetchLedgerFromVouchers(String ledgerId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String recieptVoucherJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.RECEIPT_VOUCHER_TABLE, conditionMap);
        List<ReceiptVoucher> receiptVoucherList = new Gson().fromJson(recieptVoucherJson, new TypeToken<List<ReceiptVoucher>>() {
        }.getType());
        for (ReceiptVoucher receiptVoucher : receiptVoucherList) {
            List<LedgerList> ledgerList = new ArrayList<LedgerList>();
            ledgerList = receiptVoucher.getLedgerList();
            for (Iterator<LedgerList> it = ledgerList.iterator(); it.hasNext();) {
                LedgerList ledgerList1 = it.next();
                if (ledgerList1.getLedger().equals(ledgerId)) {
                    return true;
                }

            }
        }
        String paymentVoucherJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, conditionMap);
        List<PaymentVoucher> paymentVoucherList = new Gson().fromJson(paymentVoucherJson, new TypeToken<List<PaymentVoucher>>() {
        }.getType());
        for (PaymentVoucher paymentVoucher : paymentVoucherList) {
            List<LedgerList> ledgerList = new ArrayList<LedgerList>();
            ledgerList = paymentVoucher.getLedgerList();
            for (Iterator<LedgerList> it = ledgerList.iterator(); it.hasNext();) {
                LedgerList ledgerList1 = it.next();
                if (ledgerList1.getLedger().equals(ledgerId)) {
                    return true;
                }

            }
        }
        String contraVoucherJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONTRA_VOUCHER_TABLE, conditionMap);
        List<ContraVoucher> contraVoucherList = new Gson().fromJson(contraVoucherJson, new TypeToken<List<ContraVoucher>>() {
        }.getType());
        for (ContraVoucher contraVoucher : contraVoucherList) {
            List<LedgerList> ledgerList = new ArrayList<LedgerList>();
            ledgerList = contraVoucher.getLedgerList();
            for (Iterator<LedgerList> it = ledgerList.iterator(); it.hasNext();) {
                LedgerList ledgerList1 = it.next();
                if (ledgerList1.getLedger().equals(ledgerId)) {
                    return true;
                }

            }
        }
        String journalVoucherJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.JOURNAL_VOUCHER_TABLE, conditionMap);
        List<JournalVoucher> journalVoucherList = new Gson().fromJson(journalVoucherJson, new TypeToken<List<JournalVoucher>>() {
        }.getType());
        for (JournalVoucher journalVoucher : journalVoucherList) {
            List<LedgerList> ledgerList = new ArrayList<LedgerList>();
            ledgerList = journalVoucher.getLedgerList();
            for (Iterator<LedgerList> it = ledgerList.iterator(); it.hasNext();) {
                LedgerList ledgerList1 = it.next();
                if (ledgerList1.getLedger().equals(ledgerId)) {
                    return true;
                }

            }
        }
        return false;

    }
    
    public boolean fetchLedgerForCarryForward(String ledgerId, String finYearId) throws Exception {
        String ledgerId1 = ledgerId;
        String yearId1 =  finYearId;
       
       

//        String FYJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, conditionMap2);
        String FYJson = DBManager.getDbConnection().fetch(ApplicationConstants.FINANCIAL_ACCOUNTING_FINANCIAL_YEAR_TABLE, yearId1);
        
        if (FYJson != null) {

                        List<FinancialYear> finYearList = new Gson().fromJson(FYJson, new TypeToken<List<FinancialYear>>() {
                        }.getType());

                        FinancialYear yearObj = finYearList.get(0);
                        yearId1=yearObj.getYear();
                       
                    }
        int finYearInt = Integer.parseInt(yearId1);
        finYearInt = finYearInt + 1;
        String finYear1 = Integer.toString(finYearInt);
        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put("ledgerId", ledgerId1);
        conditionMap1.put("financialYearId", finYear1);
        conditionMap1.put("transferStatus", "CarryForward");
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String ledgerJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.MANAGE_OPENING_BALANCE_TABLE, conditionMap1);
        if(ledgerJson != null)
        {
            return true;
        }
        
        return false;

    }
    
    public static String roundTwoDecimalPoints(double amount) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String output = nf.format(amount).replaceAll(",", "");
        return output;
    }

    public static void main(String args[]) throws Exception {
//        ManageOpeningBalanceManager mb = new ManageOpeningBalanceManager();
////        mb.fetchLedgerFromVouchers("");
////        System.out.println("result-----------------" + mb.fetchLedgerFromVouchers("584804b7d0bd0c2655da226c"));
    }

}
