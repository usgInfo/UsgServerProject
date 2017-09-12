/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.user.manager.UserManager;
import com.accure.finance.dto.BankReconcilation;
import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.LedgerCodeMaster;

import com.accure.finance.dto.PaymentVoucher;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.user.dto.User;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author deepak2310
 */
public class BankReconcilationManager {

    public String createReconcilation(BankReconcilation bankReconcilation, String UserId) throws Exception {
        PropertiesConfiguration config = getConfig();
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.PAYMENT_VOUCHER_TABLE);
        DBCollection contracollection = db.getCollection(ApplicationConstants.CONTRA_VOUCHER_TABLE);
        DBCollection receiptcollection = db.getCollection(ApplicationConstants.RECEIPT_VOUCHER_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        BasicDBObject contraregexQuery = new BasicDBObject();
        BasicDBObject receiptregexQuery = new BasicDBObject();
        if (bankReconcilation.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", bankReconcilation.getLocation()));
            contraregexQuery.put("location",
                    new BasicDBObject("$regex", bankReconcilation.getLocation()));
            receiptregexQuery.put("location",
                    new BasicDBObject("$regex", bankReconcilation.getLocation()));

        }

        if (bankReconcilation.getReconcilationStatus().equalsIgnoreCase("Uncleared Entries") || bankReconcilation.getReconcilationStatus().equalsIgnoreCase("Cleared Entries")) {
            regexQuery.put("entryStatus",
                    new BasicDBObject("$regex", bankReconcilation.getReconcilationStatus()));
            contraregexQuery.put("entryStatus",
                    new BasicDBObject("$regex", bankReconcilation.getReconcilationStatus()));
            receiptregexQuery.put("entryStatus",
                    new BasicDBObject("$regex", bankReconcilation.getReconcilationStatus()));
        }
        if (bankReconcilation.getFromDate() != null && bankReconcilation.getToDate() != null) {
            String fromDate = bankReconcilation.getFromDate();
            String toDate = bankReconcilation.getToDate();
            long fromDateMilliSec = saveInMilliSecond(fromDate);
            long toDateMilliSec = saveInMilliSecond(toDate);
            regexQuery.put("voucherDateInMilliSecond", new BasicDBObject("$gte", fromDateMilliSec).append("$lte", toDateMilliSec));
            contraregexQuery.put("voucherDateInMilliSecond", new BasicDBObject("$gte", fromDateMilliSec).append("$lte", toDateMilliSec));
            receiptregexQuery.put("voucherDateInMilliSecond", new BasicDBObject("$gte", fromDateMilliSec).append("$lte", toDateMilliSec));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        contraregexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        receiptregexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        DBCursor cursor2 = collection.find(regexQuery);
        DBCursor contracursor = contracollection.find(contraregexQuery);
        DBCursor receiptcursor = receiptcollection.find(receiptregexQuery);
        //System.out.println(cursor2);
        List<BankReconcilation> voucherList = new ArrayList<BankReconcilation>();
        String payvouCon = "No";
        String convouCon = "No";
        String recvouCon = "No";
        while (cursor2.hasNext()) {
            BankReconcilation finallist = new BankReconcilation();
            DBObject ob = cursor2.next();
            Type type = new TypeToken<PaymentVoucher>() {
            }.getType();
            PaymentVoucher em = new Gson().fromJson(ob.toString(), type);
            if (em.getPaymentMode().equalsIgnoreCase("Cheque")) {
                for (int i = 0; i < em.getLedgerList().size(); i++) {
                    if (em.getLedgerList().get(i).getDrCr().equalsIgnoreCase("Cr") && em.getLedgerList().get(i).getGroupName().equalsIgnoreCase("Bank Group") && em.getLedgerList().get(i).getLedger().equalsIgnoreCase(bankReconcilation.getLedger())) {
                        payvouCon = "Yes";
                    }

                }
                if (payvouCon.equalsIgnoreCase("Yes")) {
                    finallist.setEntryStatus(em.getEntryStatus());
                    finallist.setVoucherId(((LinkedTreeMap<String, String>) em.getId()).get("$oid"));
                    finallist.setVoucherName(em.getVoucherName());
                    finallist.setVoucherNo(em.getVoucherNo());
                    finallist.setVoucherDate(em.getVoucherDate());
                    finallist.setFundType(em.getFundType());
                    voucherList.add(finallist);
                }
            }

        }
        //System.out.println(new Gson().toJson(voucherList));
        //System.out.println("-------------------------------------------------------------------------------");
        while (contracursor.hasNext()) {
            BankReconcilation finallist = new BankReconcilation();
            DBObject contraob = contracursor.next();
            Type type = new TypeToken<ContraVoucher>() {
            }.getType();
            ContraVoucher em = new Gson().fromJson(contraob.toString(), type);
            if (em.getPaymentMode().equalsIgnoreCase("Cheque")) {
                for (int i = 0; i < em.getLedgerList().size(); i++) {
                    if (em.getLedgerList().get(i).getGroupName().equalsIgnoreCase("Bank Group") && em.getLedgerList().get(i).getLedger().equalsIgnoreCase(bankReconcilation.getLedger())) {
                        convouCon = "Yes";
                    }

                }
                if (convouCon.equalsIgnoreCase("Yes")) {
                    finallist.setEntryStatus(em.getEntryStatus());
                    finallist.setVoucherId(((LinkedTreeMap<String, String>) em.getId()).get("$oid"));
                    finallist.setVoucherName(em.getVoucherName());
                    finallist.setVoucherNo(em.getVoucherNo());
                    finallist.setVoucherDate(em.getVoucherDate());
                    finallist.setFundType(em.getFundType());
                    voucherList.add(finallist);
                }
            }

        }
        while (receiptcursor.hasNext()) {
            BankReconcilation finallist = new BankReconcilation();
            DBObject receiptob = receiptcursor.next();
            Type type = new TypeToken<ReceiptVoucher>() {
            }.getType();
            ReceiptVoucher em = new Gson().fromJson(receiptob.toString(), type);
            if (em.getPaymentMode().equalsIgnoreCase("Cheque")) {
                for (int i = 0; i < em.getLedgerList().size(); i++) {
                    if (em.getLedgerList().get(i).getGroupName().equalsIgnoreCase("Bank Group") && em.getLedgerList().get(i).getLedger().equalsIgnoreCase(bankReconcilation.getLedger())) {
                        recvouCon = "Yes";
                    }

                }
                if (recvouCon.equalsIgnoreCase("Yes")) {
                    finallist.setEntryStatus(em.getEntryStatus());
                    finallist.setVoucherId(((LinkedTreeMap<String, String>) em.getId()).get("$oid"));
                    finallist.setVoucherName(em.getVoucherName());
                    finallist.setVoucherNo(em.getVoucherNo());
                    finallist.setVoucherDate(em.getVoucherDate());
                    finallist.setFundType(em.getFundType());
                    voucherList.add(finallist);
                }
            }

        }
        //System.out.println(new Gson().toJson(voucherList));

        HashMap<String, String> resultList = new HashMap<String, String>();
        resultList.put("voucherList", new Gson().toJson(voucherList));

        return new Gson().toJson(resultList);

    }

    public long saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    public String getLedgerDropdowns() throws Exception {

        String groupTable = ApplicationConstants.USG_DB1 + ApplicationConstants.GROUP_TABLE + "`";

        String ledgerTable = ApplicationConstants.USG_DB1 + ApplicationConstants.LEDGER_TABLE + "`";
       
        RestClient aql = new RestClient();

        String ledQuery = "select led._id as idStr,led.ledgerName"
                + " from " + ledgerTable + " as led "
                + " inner join " + groupTable + " as grp "
                + "on  (grp._id = OID(led.underGroup)) where led.status = \"Active\" "
                + "and grp.groupName=\"Bank Group\" ";

        String ledgerOutput = aql.getRestData(ApplicationConstants.END_POINT, ledQuery);

        List<Map<String, String>> list = new Gson().fromJson(ledgerOutput, new TypeToken<List<Map<String, String>>>() {
        }.getType());

        List<Map<String, String>> outlist = new ArrayList<Map<String, String>>();

        for (Map<String, String> li : list) {


            HashMap<String, String> hmap = new HashMap<String, String>();

            hmap.put("governmentLedgerCode", li.get("idStr"));
            hmap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            String lcStr = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CODE_TABLE, hmap);

            if (lcStr != null) {
                List<LedgerCodeMaster> lcmList = new Gson().fromJson(lcStr, new TypeToken<List<LedgerCodeMaster>>() {
                }.getType());
                String ledgerCode = lcmList.get(0).getLedgerCode();

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("idStr", li.get("idStr"));
                map.put("ledgerCode", ledgerCode);
                map.put("ledgerName", li.get("ledgerName"));
                
                outlist.add(map);

            }

        }

        return new Gson().toJson(outlist);

    }

    public static void main(String[] args) throws Exception {
        String result = new BankReconcilationManager().getLedgerDropdowns();
        System.out.println("final result is" + result);
    }

    public BankReconcilation fetch(String bankReconcilationId) throws Exception {
        if (bankReconcilationId == null || bankReconcilationId.isEmpty()) {
            return null;
        }
        String bankReconcilationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_RECONCILATION_TABLE, bankReconcilationId);
        if (bankReconcilationJson == null || bankReconcilationJson.isEmpty()) {
            return null;
        }
        List<BankReconcilation> bankReconcilationList = new Gson().fromJson(bankReconcilationJson, new TypeToken<List<BankReconcilation>>() {
        }.getType());
        if (bankReconcilationList == null || bankReconcilationList.isEmpty()) {
            return null;
        }
        return bankReconcilationList.get(0);

    }

    public List<BankReconcilation> fetchAllBankReconcilation() throws Exception {
        HashMap<String, String> parentLedger = new LedgerManager().fetchallParentLedger();
        HashMap<String, String> allLocation = new HeadCodeLocationManager().fetchallLocation();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String bankReconcilationJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_RECONCILATION_TABLE, conditionMap);
        List<BankReconcilation> bankReconcilationList = new Gson().fromJson(bankReconcilationJson, new TypeToken<List<BankReconcilation>>() {
        }.getType());
        List<BankReconcilation> returnList = new ArrayList<BankReconcilation>();
        if (bankReconcilationList != null) {
            for (BankReconcilation bankReconcilation : bankReconcilationList) {
                if (bankReconcilation.getLedger() != null) {
                    String ledgerId = bankReconcilation.getLedger();
                    if (parentLedger.containsKey(ledgerId) && parentLedger.get(ledgerId) != null) {
                        bankReconcilation.setLedgerName(parentLedger.get(ledgerId));
                    }
                }
                if (bankReconcilation.getLocation() != null) {
                    String locationId = bankReconcilation.getLocation();
                    if (allLocation.containsKey(locationId) && allLocation.get(locationId) != null) {
                        bankReconcilation.setLocationName(allLocation.get(locationId));
                    }
                }
                returnList.add(bankReconcilation);
            }
        }
        return returnList;
    }

    public boolean updateBankReconcilation(PaymentVoucher bankReconcilation, String userId) throws Exception {
        if (bankReconcilation == null || userId == null || userId.isEmpty()) {
            return false;
        }
        boolean bankrestatus = false;
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (bankReconcilation.getVoucherName().equalsIgnoreCase("PaymentVoucher")) {
            PaymentVoucher bankReconcilationDb = new PaymentVoucherManager().fetchData(bankReconcilation.getVoucherId());
//  
            bankReconcilationDb.setEntryStatus(bankReconcilation.getEntryStatus());
            bankReconcilationDb.setUpdateDate(System.currentTimeMillis() + "");
            bankReconcilationDb.setUpdatedBy(userName);

            String bankReconcilationJson = new Gson().toJson(bankReconcilationDb);
            bankrestatus = DBManager.getDbConnection().update(ApplicationConstants.PAYMENT_VOUCHER_TABLE, bankReconcilation.getVoucherId(), bankReconcilationJson);
        } else if (bankReconcilation.getVoucherName().equalsIgnoreCase("ContraVoucher")) {
            String ContraDb = new ContraVoucherManager().fetch(bankReconcilation.getVoucherId());
            Type type = new TypeToken<ContraVoucher>() {
            }.getType();
            ContraVoucher contraVoucherrJson = new Gson().fromJson(ContraDb, type);
            contraVoucherrJson.setEntryStatus(bankReconcilation.getEntryStatus());
            contraVoucherrJson.setUpdateDate(System.currentTimeMillis() + "");
            contraVoucherrJson.setUpdatedBy(userName);

            String bankcontraJson = new Gson().toJson(contraVoucherrJson);
            bankrestatus = DBManager.getDbConnection().update(ApplicationConstants.CONTRA_VOUCHER_TABLE, bankReconcilation.getVoucherId(), bankcontraJson);
        }
        if (bankrestatus) {
            return true;
        }
        return false;
    }

    public boolean deleteBankReconcilation(String bankReconcilationId, String currentUserLogin) throws Exception {
        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        BankReconcilation DbData = fetch(bankReconcilationId);
        DbData.setStatus(ApplicationConstants.DELETE);
        DbData.setUpdateDate(System.currentTimeMillis() + "");
        DbData.setUpdatedBy(userName);

        String bankReconcilationJson = new Gson().toJson(DbData);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BANK_RECONCILATION_TABLE, bankReconcilationId, bankReconcilationJson);
        if (status) {
            return true;
        }
        return false;
    }

}
