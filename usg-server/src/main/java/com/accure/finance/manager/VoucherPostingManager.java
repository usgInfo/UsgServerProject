/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.JournalVoucher;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asif/ankur
 */
public class VoucherPostingManager {

    public String Search(String fromdate, String ToDate, String voucherNo) throws ParseException, Exception {
        fromdate = saveInMilliSecond(fromdate);
        ToDate = saveInMilliSecond(ToDate);
        voucherNo = voucherNo;
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        innerMap.put("voucherNo", voucherNo);
        conditionMap.put(ApplicationConstants.STATUS, innerMap);
        String result1 = DBManager.getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.PAYMENT_VOUCHER_TABLE, "voucherDate", fromdate, ToDate, conditionMap);

        List<ContraVoucher> receiptvoucherList = new Gson().fromJson(result1, new TypeToken<List<ContraVoucher>>() {
        }.getType());
        if (receiptvoucherList != null) {
            for (Iterator<ContraVoucher> iterator = receiptvoucherList.iterator(); iterator.hasNext();) {
                ContraVoucher next = iterator.next();
                if (next.getVoucherDate() != null) {
                    next.setVoucherDate(new ContraVoucherManager().MilliSecondToDDMMYYY(next.getVoucherDate()));
                }
            }
        }

        return new Gson().toJson(receiptvoucherList);
    }

    public String Search(String voucherType) throws ParseException, Exception {

        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.STATUS, innerMap);
        conditionMap.put("postingStatus", innerMap);
        String result1 = DBManager.getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.CONTRA_VOUCHER_TABLE, "voucherDate", "", "", conditionMap);

        List<ContraVoucher> receiptvoucherList = new Gson().fromJson(result1, new TypeToken<List<ContraVoucher>>() {
        }.getType());
        if (receiptvoucherList != null) {
            for (Iterator<ContraVoucher> iterator = receiptvoucherList.iterator(); iterator.hasNext();) {
                ContraVoucher next = iterator.next();
                if (next.getVoucherDate() != null) {
                    next.setVoucherDate(new ContraVoucherManager().MilliSecondToDDMMYYY(next.getVoucherDate()));
                }
            }
        }

        return new Gson().toJson(receiptvoucherList);
    }

    public String MilliSecondToDDMMYYY(String str) {

        long foo = Long.parseLong(str);
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public String saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return Long.toString(date.getTime());
    }

    public String Search(String voucherType, String fromDate, String toDate, String DDO, String location, String fundType, String budgetHead, String manualVoucherNo, String voucherAmount) throws Exception {
        //System.out.println(voucherType + "," + fromDate + "," + toDate + "," + DDO + "," + location + "," + fundType + "," + budgetHead + "," + manualVoucherNo + "," + voucherAmount);
        String result = "";
        if (voucherType.equalsIgnoreCase("Payment-Voucher")) {
            result = new VoucherPostingManager().PaymentVoucherSearch(fromDate, toDate, DDO, location, budgetHead, manualVoucherNo, voucherAmount, fundType);
        } else if (voucherType.equalsIgnoreCase("Receipt-Voucher")) {
            result = new VoucherPostingManager().ReceiptVoucherSearch(fromDate, toDate, DDO, location, budgetHead, manualVoucherNo, voucherAmount, fundType);
        } else if (voucherType.equalsIgnoreCase("Contra-Voucher")) {
            result = new VoucherPostingManager().ContraVoucherSearch(fromDate, toDate, DDO, location, budgetHead, manualVoucherNo, voucherAmount, fundType);
        } else if (voucherType.equalsIgnoreCase("Journal-Voucher")) {
            result = new VoucherPostingManager().JournalVoucherSearch(fromDate, toDate, DDO, location, budgetHead, manualVoucherNo, voucherAmount, fundType);
        }
        return result;
    }

    private String JournalVoucherSearch(String fromDate, String toDate, String DDO, String location, String budgetHead, String voucherNo, String voucherAmount, String fundType) throws ParseException {
        RestClient aql = new RestClient();
        String mongoCollectionName = ApplicationConstants.USG_DB1 + ApplicationConstants.JOURNAL_VOUCHER_TABLE + "`";
        String Active = "\"Active\"";
        StringBuilder quString1 = new StringBuilder();
        fromDate = saveInMilliSecond(fromDate);
        toDate = saveInMilliSecond(toDate);
        quString1.append("select *,_id from " + mongoCollectionName + " where voucherDateInMilliSecond between " + fromDate + " and " + toDate);

        if (voucherNo.isEmpty() == false) {
            voucherNo = "\"%" + voucherNo + "%\"";
            quString1.append(" and voucherNo like" + voucherNo);
        }
        if (DDO.isEmpty() == false) {
            DDO = "\"" + DDO + "\"";
            quString1.append(" and DDO =" + DDO);
        }
        if (location.isEmpty() == false) {
            location = "\"" + location + "\"";
            quString1.append(" and location =" + location);
        }
        if (fundType.isEmpty() == false) {
            fundType = "\"" + fundType + "\"";
            quString1.append(" and fundType =" + fundType);
        }
        if (budgetHead.isEmpty() == false) {
            budgetHead = "\"" + budgetHead + "\"";
            quString1.append("  and budgetHead =" + budgetHead);
        }
//        if (voucherAmount.isEmpty() == false) {
//            voucherAmount = "\"" + voucherAmount + "\"";
//            quString1.append("  and totalDrAmount >" + voucherAmount);
//        }
        quString1.append(" and status =" + Active);
        quString1.append(" and postingStatus = " + "\"" + ApplicationConstants.NEW + "\"");
        //System.out.println(quString1);
        String json = aql.getRestData(ApplicationConstants.END_POINT, quString1.toString());
        return json;
    }

    private String PaymentVoucherSearch(String fromDate, String toDate, String DDO, String location, String budgetHead, String voucherNo, String voucherAmount, String fundType) throws ParseException {
        RestClient aql = new RestClient();
        String mongoCollectionName = ApplicationConstants.USG_DB1 + ApplicationConstants.PAYMENT_VOUCHER_TABLE + "`";
        String Active = "\"Active\"";
        StringBuilder quString1 = new StringBuilder();
        fromDate = saveInMilliSecond(fromDate);
        toDate = saveInMilliSecond(toDate);
        quString1.append("select *,_id from " + mongoCollectionName + " where voucherDateInMilliSecond between " + fromDate + " and " + toDate);

        if (voucherNo.isEmpty() == false) {
            voucherNo = "\"%" + voucherNo + "%\"";
            quString1.append(" and voucherNo like" + voucherNo);
        }
        if (fundType.isEmpty() == false) {
            fundType = "\"" + fundType + "\"";
            quString1.append(" and fundType =" + fundType);
        }
        if (budgetHead.isEmpty() == false) {
            budgetHead = "\"" + budgetHead + "\"";
            quString1.append("  and budgetHead =" + budgetHead);
        }
        quString1.append(" and status =" + Active);
        quString1.append(" and postingStatus = " + "\"" + ApplicationConstants.NEW + "\"");
        String json = aql.getRestData(ApplicationConstants.END_POINT, quString1.toString());
        return json;
    }

    private String ReceiptVoucherSearch(String fromDate, String toDate, String DDO, String location, String budgetHead, String voucherNo, String voucherAmount, String fundType) throws ParseException {
        RestClient aql = new RestClient();
        String mongoCollectionName = ApplicationConstants.USG_DB1 + ApplicationConstants.RECEIPT_VOUCHER_TABLE + "`";
        String Active = "\"Active\"";
        StringBuilder quString1 = new StringBuilder();
        fromDate = saveInMilliSecond(fromDate);
        toDate = saveInMilliSecond(toDate);
        quString1.append("select *,_id from " + mongoCollectionName + " where voucherDateInMilliSecond between " + fromDate + " and " + toDate);

        if (voucherNo.isEmpty() == false) {
            voucherNo = "\"%" + voucherNo + "%\"";
            quString1.append(" and voucherNo like" + voucherNo);
        }
        if (fundType.isEmpty() == false) {
            fundType = "\"" + fundType + "\"";
            quString1.append(" and fundType =" + fundType);
        }
        if (budgetHead.isEmpty() == false) {
            budgetHead = "\"" + budgetHead + "\"";
            quString1.append("  and budgetHead =" + budgetHead);
        }
        quString1.append(" and status =" + Active);
        quString1.append(" and postingStatus = " + "\"" + ApplicationConstants.NEW + "\"");
        String json = aql.getRestData(ApplicationConstants.END_POINT, quString1.toString());
        return json;
    }

    private String ContraVoucherSearch(String fromDate, String toDate, String DDO, String location, String budgetHead, String voucherNo, String voucherAmount, String fundType) throws ParseException {
        RestClient aql = new RestClient();
        String mongoCollectionName = ApplicationConstants.USG_DB1 + ApplicationConstants.CONTRA_VOUCHER_TABLE + "`";
        String Active = "\"Active\"";
        StringBuilder quString1 = new StringBuilder();
        fromDate = saveInMilliSecond(fromDate);
        toDate = saveInMilliSecond(toDate);
        quString1.append("select *,_id from " + mongoCollectionName + " where voucherDateInMilliSecond between " + fromDate + " and " + toDate);

        if (voucherNo.isEmpty() == false) {
            voucherNo = "\"%" + voucherNo + "%\"";
            quString1.append(" and voucherNo like" + voucherNo);
        }
        if (DDO.isEmpty() == false) {
            DDO = "\"" + DDO + "\"";
            quString1.append(" and DDO =" + DDO);
        }
        if (location.isEmpty() == false) {
            location = "\"" + location + "\"";
            quString1.append(" and location =" + location);
        }
        quString1.append(" and status =" + Active);
        quString1.append(" and postingStatus = " + "\"" + ApplicationConstants.NEW + "\"");
        String json = aql.getRestData(ApplicationConstants.END_POINT, quString1.toString());
        return json;
    }

    /**@author ankur
     * This method update the Posting Status of Vouchers
     * @param pvId is String type Object Id of MongoDB
     * @param voucherName is String type Voucher Name, For Ex: "ReceiptVoucher"
     * @return true, if the Posting Status is updated to "Posted" , else false
     * @throws Exception 
     */
    public boolean updatePostingStatus(String pvId, String voucherName, String postingDate) throws Exception {
        boolean status = false;
        if (voucherName.equals("ReceiptVoucher")) {
            String voucherJson = DBManager.getDbConnection().fetch(ApplicationConstants.RECEIPT_VOUCHER_TABLE, pvId);
            List<ReceiptVoucher> voucherList = new Gson().fromJson(voucherJson, new TypeToken<List<ReceiptVoucher>>() {
            }.getType());
            ReceiptVoucher receiptVoucher = voucherList.get(0);
            receiptVoucher.setPostingStatus(ApplicationConstants.POSTED);
            receiptVoucher.setPostingDate(postingDate);
            String updatedJson = new Gson().toJson(receiptVoucher);
            status = DBManager.getDbConnection().update(ApplicationConstants.RECEIPT_VOUCHER_TABLE, pvId, updatedJson);
        } else if (voucherName.equals("ContraVoucher")) {
            String voucherJson = DBManager.getDbConnection().fetch(ApplicationConstants.CONTRA_VOUCHER_TABLE, pvId);
            List<ContraVoucher> voucherList = new Gson().fromJson(voucherJson, new TypeToken<List<ContraVoucher>>() {
            }.getType());
            ContraVoucher contraVoucher = voucherList.get(0);
            contraVoucher.setPostingStatus(ApplicationConstants.POSTED);
            contraVoucher.setPostingDate(postingDate);
            String updatedJson = new Gson().toJson(contraVoucher);
            status = DBManager.getDbConnection().update(ApplicationConstants.CONTRA_VOUCHER_TABLE, pvId, updatedJson);
        } else if (voucherName.equals("JournalVoucher")) {
            String voucherJson = DBManager.getDbConnection().fetch(ApplicationConstants.JOURNAL_VOUCHER_TABLE, pvId);
            List<JournalVoucher> voucherList = new Gson().fromJson(voucherJson, new TypeToken<List<JournalVoucher>>() {
            }.getType());
            JournalVoucher journalVoucher = voucherList.get(0);
            journalVoucher.setPostingStatus(ApplicationConstants.POSTED);
            journalVoucher.setPostingDate(postingDate);
            String updatedJson = new Gson().toJson(journalVoucher);
            status = DBManager.getDbConnection().update(ApplicationConstants.JOURNAL_VOUCHER_TABLE, pvId, updatedJson);
        } else if (voucherName.equals("PaymentVoucher")) {
            String voucherJson = DBManager.getDbConnection().fetch(ApplicationConstants.PAYMENT_VOUCHER_TABLE, pvId);
            List<PaymentVoucher> voucherList = new Gson().fromJson(voucherJson, new TypeToken<List<PaymentVoucher>>() {
            }.getType());
            PaymentVoucher paymentVoucher = voucherList.get(0);
            paymentVoucher.setPostingStatus(ApplicationConstants.POSTED);
            paymentVoucher.setPostingDate(postingDate);
            String updatedJson = new Gson().toJson(paymentVoucher);
            status = DBManager.getDbConnection().update(ApplicationConstants.PAYMENT_VOUCHER_TABLE, pvId, updatedJson);
        }

        return status;
    }

//    public static void main(String[] args) {
//      try {
//           new VoucherPostingManager().updatePostingStatus("581996f9ff91a403a85ef173", "ReceiptVoucher");
//       } catch (Exception e) {
//           e.printStackTrace();
//        }
//    }
}
