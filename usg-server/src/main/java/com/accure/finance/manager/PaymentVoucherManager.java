/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.accure.db.Query;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.Location;
import com.accure.finance.dto.PaymentVoucher;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONObject;

/**
 *
 * @author asif/ankur
 */
public class PaymentVoucherManager {

    public String save(PaymentVoucher paymentvoucher, String loginUserId, String ddoId, String locationId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        DDO name = (new DDOManager().fetch(ddoId));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(locationId);
        String locationName = locName.getLocationName();
        paymentvoucher.setDdoName(ddoName);
        paymentvoucher.setLocationName(locationName);
        String voucherDate = paymentvoucher.getVoucherDate();
        paymentvoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(voucherDate)));
        paymentvoucher.setVoucherName("PaymentVoucher");
        paymentvoucher.setEntryStatus("Uncleared Entries");
        paymentvoucher.setCreateDate(System.currentTimeMillis() + "");
        paymentvoucher.setUpdateDate(System.currentTimeMillis() + "");
        paymentvoucher.setStatus(ApplicationConstants.ACTIVE);
        paymentvoucher.setCreatedBy(userName);
        paymentvoucher.setPostingStatus(ApplicationConstants.NEW);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = result.substring(1, result.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        paymentvoucher.setBudgetHeadName(bh);

        String paymentvoucherJson = new Gson().toJson(paymentvoucher);
        String rvId = DBManager.getDbConnection().insert(ApplicationConstants.PAYMENT_VOUCHER_TABLE, paymentvoucherJson);
        if (rvId != null) {
            return rvId;
        }
        return null;
    }

    public String fetch(String rvId) throws Exception {
        if (rvId == null || rvId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PAYMENT_VOUCHER_TABLE, rvId);
        List<PaymentVoucher> paymentvoucherList = new Gson().fromJson(result, new TypeToken<List<PaymentVoucher>>() {
        }.getType());
        if (paymentvoucherList == null || paymentvoucherList.size() < 1) {
            return null;
        }
        return new Gson().toJson(paymentvoucherList.get(0));
    }

    public boolean delete(String rvId, String loginUserId) throws Exception {
        if (rvId == null || rvId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<PaymentVoucher>() {
        }.getType();
        String paymentvoucher = new PaymentVoucherManager().fetch(rvId);
        if (paymentvoucher == null || paymentvoucher.isEmpty()) {
            return false;
        }
        PaymentVoucher paymentvoucherrJson = new Gson().fromJson(paymentvoucher, type);

        if (!paymentvoucherrJson.getPostingStatus().equals("Posted")) {
            paymentvoucherrJson.setStatus(ApplicationConstants.INACTIVE);
            paymentvoucherrJson.setUpdatedBy(userName);
            paymentvoucherrJson.setDeletedBy(userName);
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.PAYMENT_VOUCHER_TABLE, rvId, new Gson().toJson(paymentvoucherrJson));
            if (result) {
                return true;
            }
        }
        return false;
    }

    public boolean update(PaymentVoucher paymentvoucher, String rvId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        PaymentVoucher oldVoucherData = fetchData(rvId);
        oldVoucherData.setChequeList(paymentvoucher.getChequeList());
//        oldVoucherData.setBankAccountNo(paymentvoucher.getBankAccountNo());
//        oldVoucherData.setBankAmount(paymentvoucher.getBankAmount());
//        oldVoucherData.setBankName(paymentvoucher.getBankName());
//        oldVoucherData.setBankRemarks(paymentvoucher.getBankRemarks());
        oldVoucherData.setBudgetHead(paymentvoucher.getBudgetHead());
        oldVoucherData.setBankPaymentList(paymentvoucher.getBankPaymentList());
//        oldVoucherData.setChequeAmount(paymentvoucher.getChequeAmount());
//        oldVoucherData.setChequeBankName(paymentvoucher.getChequeBankName());
//        oldVoucherData.setChequeDate(paymentvoucher.getChequeDate());
//        oldVoucherData.setChequeNo(paymentvoucher.getChequeNo());
//        oldVoucherData.setChequeRemarks(paymentvoucher.getChequeRemarks());
//        oldVoucherData.setInFavorOf(paymentvoucher.getInFavorOf());

        oldVoucherData.setDDO(paymentvoucher.getDDO());
        oldVoucherData.setFundType(paymentvoucher.getFundType());

        oldVoucherData.setLedgerCategory(paymentvoucher.getLedgerCategory());
        oldVoucherData.setLedgerList(paymentvoucher.getLedgerList());
        oldVoucherData.setLocation(paymentvoucher.getLocation());
        oldVoucherData.setManualVoucher(paymentvoucher.getManualVoucher());
        oldVoucherData.setNarration(paymentvoucher.getNarration());
        oldVoucherData.setNarrationType(paymentvoucher.getNarrationType());

        oldVoucherData.setPaymentMode(paymentvoucher.getPaymentMode());
        oldVoucherData.setPostingDate(paymentvoucher.getPostingDate());
        oldVoucherData.setPostingStatus(paymentvoucher.getPostingStatus());
        oldVoucherData.setTotalCrAmount(paymentvoucher.getTotalCrAmount());
        oldVoucherData.setTotalDrAmount(paymentvoucher.getTotalDrAmount());
        oldVoucherData.setVoucherDate(paymentvoucher.getVoucherDate());
        oldVoucherData.setVoucherNo(paymentvoucher.getVoucherNo());

        oldVoucherData.setVoucherName("PaymentVoucher");
        oldVoucherData.setUpdateDate(System.currentTimeMillis() + "");
        oldVoucherData.setStatus(ApplicationConstants.ACTIVE);
        oldVoucherData.setUpdatedBy(userName);
        oldVoucherData.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(paymentvoucher.getVoucherDate())));

//        if (paymentvoucher.getChequeDate() != null) {
//            oldVoucherData.setChequeDateInMilliSecond(Long.parseLong(saveInMilliSecond(paymentvoucher.getChequeDate())));
//        }
        String paymentvoucherrJson = new Gson().toJson(oldVoucherData);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PAYMENT_VOUCHER_TABLE, rvId, paymentvoucherrJson);
        if (result) {
            return true;
        }
        return false;
    }

    public PaymentVoucher fetchData(String headCodeId) throws Exception {
        if (headCodeId == null || headCodeId.isEmpty()) {
            return null;
        }
        String headCodeJson = DBManager.getDbConnection().fetch(ApplicationConstants.PAYMENT_VOUCHER_TABLE, headCodeId);
        if (headCodeJson == null || headCodeJson.isEmpty()) {
            return null;
        }
        List<PaymentVoucher> headCodeList = new Gson().fromJson(headCodeJson, new TypeToken<List<PaymentVoucher>>() {
        }.getType());
        if (headCodeList == null || headCodeList.isEmpty()) {
            return null;
        }
        return headCodeList.get(0);

    }

    public String fetchAll(HashMap<String, String> filterMap) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (filterMap != null) {
            Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, null);
            conditionMap.putAll(filterMap);
        }

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, conditionMap);
        List<PaymentVoucher> paymentvoucherList = new Gson().fromJson(result1, new TypeToken<List<PaymentVoucher>>() {
        }.getType());

        return new Gson().toJson(paymentvoucherList);
    }

    /**
     * Search the list of documents from
     * <code>ApplicationConstants.NARATION_TABLE</code> collection.Only those
     * documents whose <code>narationType</code> is <code>Common</code> or
     * <code>Payment Voucher</code> will appear in the list.
     *
     * @return List of <code>ApplicationConstants.NARATION_TABLE</code>
     * collection documents in JSON string.
     * @throws java.lang.Exception
     */
    public String getNarrationTypeForPaymentVoucher() throws Exception {

        String narrationTable = ApplicationConstants.USG_DB1 + ApplicationConstants.NARATION_TABLE + "`";
        String narrationQuery = "select nr._id as idStr,nr.narationDetail from " + narrationTable + ""
                + " as nr where nr.narationType= \"Payment Voucher\" or nr.narationType=\"Common\"  and nr.status=\"Active\"";
        RestClient aql = new RestClient();
        String narrationOutput = aql.getRestData(ApplicationConstants.END_POINT, narrationQuery);
        return narrationOutput;
    }

    /**
     * Search the list of documents from
     * <code>ApplicationConstants.NARATION_TABLE</code> collection.Only those
     * documents whose <code>narationType</code> is <code>Common</code> or
     * <code>Receipt Voucher</code> will appear in the list.
     *
     * @return List of <code>ApplicationConstants.NARATION_TABLE</code>
     * collection documents in JSON string.
     * @throws java.lang.Exception
     */
    public String getNarrationTypeForReceiptVoucher() throws Exception {

        String narrationTable = ApplicationConstants.USG_DB1 + ApplicationConstants.NARATION_TABLE + "`";
        String narrationQuery = "select nr._id as idStr,nr.narationDetail from " + narrationTable + ""
                + " as nr where nr.narationType= \"Receipt Voucher\" or nr.narationType= \"Common\"  and nr.status=\"Active\"";
        RestClient aql = new RestClient();
        String narrationOutput = aql.getRestData(ApplicationConstants.END_POINT, narrationQuery);
        return narrationOutput;
    }

    /**
     * Search the list of documents from
     * <code>ApplicationConstants.NARATION_TABLE</code> collection.Only those
     * documents whose <code>narationType</code> is <code>Common</code> or
     * <code>Contra Voucher</code> will appear in the list.
     *
     * @return List of <code>ApplicationConstants.NARATION_TABLE</code>
     * collection documents in JSON string.
     * @throws java.lang.Exception
     */
    public String getNarrationTypeForContraVoucher() throws Exception {

        String narrationTable = ApplicationConstants.USG_DB1 + ApplicationConstants.NARATION_TABLE + "`";
        String narrationQuery = "select nr._id as idStr,nr.narationDetail from " + narrationTable + ""
                + " as nr where nr.narationType=\"Contra Voucher\" or nr.narationType= \"Common\" and nr.status=\"Active\"";
        RestClient aql = new RestClient();
        String narrationOutput = aql.getRestData(ApplicationConstants.END_POINT, narrationQuery);
        return narrationOutput;
    }

    /**
     * Search the list of documents from
     * <code>ApplicationConstants.NARATION_TABLE</code> collection.Only those
     * documents whose <code>narationType</code> is <code>Common</code> or
     * <code>Journal Voucher</code> will appear in the list.
     *
     * @return List of <code>ApplicationConstants.NARATION_TABLE</code>
     * collection documents in JSON string.
     * @throws java.lang.Exception
     */
    public String getNarrationTypeForJournalVoucher() throws Exception {

        String narrationTable = ApplicationConstants.USG_DB1 + ApplicationConstants.NARATION_TABLE + "`";
        String narrationQuery = "select nr._id as idStr,nr.narationDetail from " + narrationTable + ""
                + " as nr where nr.narationType= \"Journal Voucher\" or nr.narationType= \"Common\" and nr.status=\"Active\"";
        RestClient aql = new RestClient();
        String narrationOutput = aql.getRestData(ApplicationConstants.END_POINT, narrationQuery);
        return narrationOutput;
    }

    public String fetchAllLedgerCategory() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CATEGORY_TABLE, conditionMap);
        return result1;
    }

    public String fetchAllDocuments() throws Exception {

        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.PAYMENT_VOUCHER_TABLE);
        return result1;
    }

    public String fetchLatestVoucherNumber(String ddoId, String locationId) throws Exception {

        String ddo = DBManager.getDbConnection().fetch(ApplicationConstants.DDO, ddoId);
        String location = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION, locationId);
        //Type casting the List of DDO and location to DDO & Location Objects 
        List<DDO> ddoList = new Gson().fromJson(ddo, new TypeToken<List<DDO>>() {
        }.getType());
        List<Location> locationList = new Gson().fromJson(location, new TypeToken<List<Location>>() {
        }.getType());
        //Selectiong the DDO and Location Objects
        DDO ddoObj = ddoList.get(0);
        Location locationObj = locationList.get(0);

        String ddoCode = ddoObj.getCode();
        String locationCode = locationObj.getLocationCode();

        String result = new PaymentVoucherManager().fetchAllDocuments();
        List<PaymentVoucher> paymentVoucherList = new Gson().fromJson(result, new TypeToken<List<PaymentVoucher>>() {
        }.getType());
        int numb = 0;
        PaymentVoucher pv = new PaymentVoucher();
        if (paymentVoucherList != null) {
            for (Iterator<PaymentVoucher> iterator = paymentVoucherList.iterator(); iterator.hasNext();) {
                PaymentVoucher next = iterator.next();
                if (next.getVoucherNo().length() >= 10) {
                    String getVousub = ddoCode + "/" + locationCode;
                    String voucSub = next.getVoucherNo().substring(0, getVousub.length());

                    if (voucSub.equalsIgnoreCase(getVousub)) {
                        int value = Integer.parseInt(next.getVoucherNo().substring(getVousub.length() + 4, next.getVoucherNo().length()));

                        if (numb < value) {
                            numb = value;
                        }
                    }
                }
            }
        }
        numb++;
        String pattern = ddoCode + "/" + locationCode + "/PV-" + Integer.toString(numb);
        pv.setVoucherNo(pattern);
        List<PaymentVoucher> li = new ArrayList<PaymentVoucher>();
        li.add(pv);
        return new Gson().toJson(li);
    }

    public void SearchBetweenTheTwoDates() throws ParseException, Exception {
//        Long lg = new PaymentVoucherManager().saveInMilliSecond("01/01/2000");
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.STATUS, innerMap);
        //
        String startDateString = "09-07-2016";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = df.parse(startDateString);
        String newDateString = Long.toString(startDate.getTime());
        //System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
        //System.out.println(newDateString);
        //System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
        //
        Date today = startDate;
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
//        cal.add(Calendar.DAY_OF_MONTH, -30);
        String fromDate = Long.toString(cal.getTimeInMillis());
        String toDate = Long.toString(System.currentTimeMillis());
        //System.out.println("From Date" + fromDate);
        //System.out.println("To Date" + toDate);
        //
        List<Query> qlist = new ArrayList<Query>();
        Query query = new Query();
        query.setColumnName("createDate");
        query.setColumnValue("946665000000");
        query.setCondition(ApplicationConstants.LOGICAL_GTE);
        qlist.add(query);

        query = new Query();
        query.setColumnName("createDate");
        query.setColumnValue("1577817000000");
        query.setCondition(ApplicationConstants.LOGICAL_LTE);
        qlist.add(query);
        query = new Query();
        query.setColumnName("status");
        query.setColumnValue("Active");
        qlist.add(query);
        //System.out.println("********************************");
//        //System.out.println(fromDate + "-" + + "=");
        //System.out.println(toDate);
        //System.out.println("********************************");
        String str1 = saveInMilliSecond("01-01-2010");
        String str2 = saveInMilliSecond("01-01-2040");
        //System.out.println("----------------------------" + str1 + "------------" + str2);
//        //System.out.println(new DBManager().getDbConnection().fetchRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, qlist));
        //System.out.println(new DBManager().getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.PAYMENT_VOUCHER_TABLE, "voucherDate", str1, str2, conditionMap));

    }
    public static final String LOGICAL_LTE = "lte";

    public static final String LOGICAL_GTE = "gte";

    public String Search(String fromdate, String ToDate, String voucherNo) throws ParseException, Exception {
        fromdate = saveInMilliSecond(fromdate);
        ToDate = saveInMilliSecond(ToDate);
        voucherNo = voucherNo;
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.EQUAL, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.STATUS, innerMap);

//        conditionMap.put("voucherNo", new Gson().toJson(map));
//        conditionMap.put("voucherNo", innerMap);
        String result = DBManager.getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.PAYMENT_VOUCHER_TABLE, "voucherDate", fromdate, ToDate, conditionMap);
        List<PaymentVoucher> paymentvoucherList = new Gson().fromJson(result, new TypeToken<List<PaymentVoucher>>() {
        }.getType());
        if (paymentvoucherList != null) {
            for (Iterator<PaymentVoucher> iterator = paymentvoucherList.iterator(); iterator.hasNext();) {
                PaymentVoucher next = iterator.next();
                if (next.getVoucherDate() != null) {
                    next.setVoucherDate(new PaymentVoucherManager().MilliSecondToDDMMYYY(next.getVoucherDate()));
                }
            }
        }
        //System.out.println(new Gson().toJson(paymentvoucherList));

        return new Gson().toJson(paymentvoucherList);

    }

    public String MilliSecondToDDMMYYY(String str) {

        long foo = Long.parseLong(str);
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public String saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
//        //System.out.println("Date - Time in milliseconds : " + date.getTime());
//        //System.out.println(new Date(str).getTime());
        return Long.toString(date.getTime());
    }

    public String fetchDataBetweenTwoDates(String fromDate, String toDate, String voucherNo) throws ParseException, Exception {
        List<Query> qlist = new ArrayList<Query>();
        Query query = new Query();
        query.setColumnName("voucherDate");
        fromDate = new PaymentVoucherManager().saveInMilliSecond(fromDate);
        //System.out.println(fromDate);
        query.setColumnValue(fromDate);
        query.setCondition(ApplicationConstants.LOGICAL_GTE);
        qlist.add(query);

        query = new Query();
        query.setColumnName("voucherDate");
        toDate = new PaymentVoucherManager().saveInMilliSecond(toDate);
        //System.out.println(toDate);
        query.setColumnValue(toDate);
        query.setCondition(ApplicationConstants.LOGICAL_LTE);
        qlist.add(query);

        query = new Query();
        query.setColumnName("status");
        query.setColumnValue("Active");
        query.setCondition(ApplicationConstants.LOGICAL_IN);
        qlist.add(query);

//        query = new Query();
//        query.setColumnName("voucherNo");
////        voucherNo = new PaymentVoucherManager().getRegexString(voucherNo);
//        query.setColumnValue(voucherNo);
//        query.setCondition(ApplicationConstants.EQUAL);
//        qlist.add(query);
//        //System.out.println(new Gson().toJson(qlist));
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("$regex", voucherNo);
//        map.put("$options", "i");
//
//        String regexString = new Gson().toJson(map);
//        //System.out.println(regexString);
//        query.setColumnValue(regexString);
//        query.setCondition(ApplicationConstants.LOGICAL_AND);
//        qlist.add(query);
        String result = new DBManager().getDbConnection().fetchRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, qlist);
//        Map<String, String> mp = new HashMap<String, String>();
//        mp.put("", result)
//        
//        String rs = new DBManager().getDbConnection().fetchRowsLike(ApplicationConstants.PAYMENT_VOUCHER_TABLE, conditions, result, 0, 0)
        //System.out.println(result);
        return result;
//        voucherNo = "{$regex:"+new PaymentVoucherManager().getRegexString(voucherNo)+"}";
//        voucherNo = "{$regex:"+new PaymentVoucherManager().getRegexString(voucherNo)+"}";
//        voucherNo = "$regex"+new PaymentVoucherManager().getRegexString(voucherNo);
    }

    public String getRegexString(String searchKey) {

        if ((searchKey != null || !searchKey.equals("")) && searchKey.length() >= 0) {
            if (searchKey.contains(" ")) {
                String searchStr[] = searchKey.split(" ");
                if (searchStr.length > 1) {
                    searchKey = "(" + searchStr[0] + ")" + "(.*?)" + "(" + searchStr[searchStr.length - 1] + ")";
                } else {
                    searchKey = "(^" + searchKey + "|\\W" + searchKey + ")";
                }
            } else {
                searchKey = "(^" + searchKey + "|\\W" + searchKey + ")";
            }
        }
        return searchKey;

    }

    public String SearchUsingAQL(String fromDate, String toDate, String voucherNo,
            HashMap<String, Object> filterMap) throws ParseException {
        RestClient aql = new RestClient();
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + "/paymentvoucher`";
        String voucherNum = "\"%" + voucherNo + "%\"";
        String Active = "\"Active\"";
        String quString = "";
        if ((!fromDate.isEmpty()) && (!toDate.isEmpty()) && (!voucherNo.isEmpty())) {
            //System.out.println("in first method");
            fromDate = saveInMilliSecond(fromDate);
            toDate = saveInMilliSecond(toDate);
            quString = "select  *,_id from  " + mongoCollectionName + " where  voucherDateInMilliSecond between " + fromDate + " and " + toDate + " and voucherNo like " + voucherNum + " and  status = " + Active;
        } else if ((!fromDate.isEmpty()) && (!toDate.isEmpty()) && (voucherNo.isEmpty())) {
            //System.out.println("in second method");
            fromDate = saveInMilliSecond(fromDate);
            toDate = saveInMilliSecond(toDate);
            quString = "select  *,_id from  " + mongoCollectionName + " where  voucherDateInMilliSecond between " + fromDate + " and " + toDate + " and status =" + Active;
        } else if ((fromDate.isEmpty()) && (toDate.isEmpty()) && (!voucherNo.isEmpty())) {
            //System.out.println("in third method");
            quString = "select  * from  " + mongoCollectionName;
//            quString = "select  *,_id from  " + mongoCollectionName + " where  voucherNo like " + voucherNum + "and status =" + Active;
        }

        quString = Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, quString);

        String EndPoint = (String) config.getProperty("aql_url");
        String json = aql.getRestData(EndPoint, quString);
        return json;
    }

    public static String getLedgerBasedOnGroup(String group) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("underGroup", group);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
        if (result != null) {
            List<Ledger> ledgerList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
            }.getType());

            return new Gson().toJson(ledgerList);
        } else {
            return null;
        }
    }

}
