/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.accure.db.Query;
import com.accure.finance.dto.ChequeList;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
import com.accure.finance.dto.LedgerCodeMaster;
import com.accure.finance.dto.Location;
import com.accure.finance.dto.ReceiptVoucher;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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
public class ReceiptVoucherManager {

    public String save(ReceiptVoucher receiptvoucher, String loginUserId, String ddoId, String locationId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        DDO name = (new DDOManager().fetch(ddoId));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(locationId);
        String locationName = locName.getLocationName();
        receiptvoucher.setDdoName(ddoName);
        receiptvoucher.setLocationName(locationName);
        receiptvoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(receiptvoucher.getVoucherDate())));
        List<ChequeList> chequelist = receiptvoucher.getChequeList();

        if (chequelist != null) {
            //receiptvoucher.setChequeDateInMilliSecond(Long.parseLong(saveInMilliSecond(chequelist.getChequeDate())));
        }
        receiptvoucher.setVoucherName("ReceiptVoucher");
        receiptvoucher.setEntryStatus("Uncleared Entries");
        receiptvoucher.setCreateDate(System.currentTimeMillis() + "");
        receiptvoucher.setUpdateDate(System.currentTimeMillis() + "");
        receiptvoucher.setStatus(ApplicationConstants.ACTIVE);
        receiptvoucher.setCreatedBy(userName);
        receiptvoucher.setPostingStatus(ApplicationConstants.NEW);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = result.substring(1, result.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        receiptvoucher.setBudgetHeadName(bh);

        String receiptvoucherJson = new Gson().toJson(receiptvoucher);
        String rvId = DBManager.getDbConnection().insert(ApplicationConstants.RECEIPT_VOUCHER_TABLE, receiptvoucherJson);
        if (rvId != null) {
            return rvId;
        }
        return null;
    }

    public String fetch(String rvId) throws Exception {
        if (rvId == null || rvId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.RECEIPT_VOUCHER_TABLE, rvId);
        List<ReceiptVoucher> receiptvoucherList = new Gson().fromJson(result, new TypeToken<List<ReceiptVoucher>>() {
        }.getType());
        if (receiptvoucherList == null || receiptvoucherList.size() < 1) {
            return null;
        }
        return new Gson().toJson(receiptvoucherList.get(0));
    }

    public boolean delete(String rvId, String loginUserId) throws Exception {
        if (rvId == null || rvId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ReceiptVoucher>() {
        }.getType();
        String receiptvoucher = new ReceiptVoucherManager().fetch(rvId);
        if (receiptvoucher == null || receiptvoucher.isEmpty()) {
            return false;
        }
        ReceiptVoucher receiptvoucherrJson = new Gson().fromJson(receiptvoucher, type);

        if (!receiptvoucherrJson.getPostingStatus().equals("Posted")) {
            receiptvoucherrJson.setStatus(ApplicationConstants.INACTIVE);
            receiptvoucherrJson.setUpdatedBy(userName);
            receiptvoucherrJson.setDeletedBy(userName);

            boolean result = DBManager.getDbConnection().update(ApplicationConstants.RECEIPT_VOUCHER_TABLE, rvId, new Gson().toJson(receiptvoucherrJson));
            if (result) {
                return true;
            }
        }

        return false;
    }

    public boolean update(ReceiptVoucher receiptvoucher, String rvId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
          DDO name = (new DDOManager().fetch(receiptvoucher.getDDO()));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(receiptvoucher.getLocation());
        String locationName = locName.getLocationName();
        receiptvoucher.setDdoName(ddoName);
        receiptvoucher.setLocationName(locationName);
        String userName = user.getFname() + " " + user.getLname();
        receiptvoucher.setVoucherName("ReceiptVoucher");
        receiptvoucher.setEntryStatus("Uncleared Entries");
        receiptvoucher.setUpdateDate(System.currentTimeMillis() + "");
        receiptvoucher.setStatus(ApplicationConstants.ACTIVE);
        receiptvoucher.setUpdatedBy(userName);
         HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String budgetresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = budgetresult.substring(1, budgetresult.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        receiptvoucher.setBudgetHeadName(bh);
        
        receiptvoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(receiptvoucher.getVoucherDate())));
        List<ChequeList> chequelist = receiptvoucher.getChequeList();
        if (chequelist != null) {
            //receiptvoucher.setChequeDateInMilliSecond(Long.parseLong(saveInMilliSecond(receiptvoucher.getChequeDate())));
        }
        String receiptvoucherrJson = new Gson().toJson(receiptvoucher);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.RECEIPT_VOUCHER_TABLE, rvId, receiptvoucherrJson);
        if (result) {
            return true;
        }
        return false;
    }

    public String fetchAll(HashMap<String, String> filterMap) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (filterMap != null) {
            Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, null);
            conditionMap.putAll(filterMap);
        }

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.RECEIPT_VOUCHER_TABLE, conditionMap);
        List<ReceiptVoucher> receiptvoucherList = new Gson().fromJson(result1, new TypeToken<List<ReceiptVoucher>>() {
        }.getType());

        return new Gson().toJson(receiptvoucherList);
    }

    public String fetchAllLedgerCategory() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CATEGORY_TABLE, conditionMap);
        return result1;
    }

    public String fetchAllLedgerCategoryForDropdowns() throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.LEDGER_CATEGORY_TABLE);
        DBObject dbobj = new BasicDBObject(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        List catList = collection.distinct("ledgerCategory", dbobj);
        //System.out.println("the final list size is" + catList);
        return new Gson().toJson(catList);

    }

    public String fetchAllDocuments() throws Exception {

        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.RECEIPT_VOUCHER_TABLE);
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
        String result = new ReceiptVoucherManager().fetchAllDocuments();
        List<ReceiptVoucher> paymentVoucherList = new Gson().fromJson(result, new TypeToken<List<ReceiptVoucher>>() {
        }.getType());
        int numb = 0;
        ReceiptVoucher pv = new ReceiptVoucher();
        if (paymentVoucherList != null) {
            for (Iterator<ReceiptVoucher> iterator = paymentVoucherList.iterator(); iterator.hasNext();) {
                ReceiptVoucher next = iterator.next();
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
        String pattern = ddoCode + "/" + locationCode + "/RV-" + Integer.toString(numb);
        pv.setVoucherNo(pattern);
        List<ReceiptVoucher> li = new ArrayList<ReceiptVoucher>();
        li.add(pv);
        return new Gson().toJson(li);
    }

    public void SearchBetweenTheTwoDates() throws ParseException, Exception {
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.STATUS, innerMap);
        //
        String startDateString = "09-07-2016";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = df.parse(startDateString);
        String newDateString = Long.toString(startDate.getTime());
        //System.out.println(newDateString);
        //System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
        //
        Date today = startDate;
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
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
        //System.out.println(toDate);
        //System.out.println("********************************");
        String str1 = saveInMilliSecond("01-01-2010");
        String str2 = saveInMilliSecond("01-01-2040");
        //System.out.println("----------------------------" + str1 + "------------" + str2);
        //System.out.println(new DBManager().getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.RECEIPT_VOUCHER_TABLE, "voucherDate", str1, str2, conditionMap));

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

        String result = DBManager.getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.RECEIPT_VOUCHER_TABLE, "voucherDate", fromdate, ToDate, conditionMap);
        List<ReceiptVoucher> receiptvoucherList = new Gson().fromJson(result, new TypeToken<List<ReceiptVoucher>>() {
        }.getType());
        if (receiptvoucherList != null) {
            for (Iterator<ReceiptVoucher> iterator = receiptvoucherList.iterator(); iterator.hasNext();) {
                ReceiptVoucher next = iterator.next();
                if (next.getVoucherDate() != null) {
                    next.setVoucherDate(new ReceiptVoucherManager().MilliSecondToDDMMYYY(next.getVoucherDate()));
                }
            }
        }
        //System.out.println(new Gson().toJson(receiptvoucherList));

        return new Gson().toJson(receiptvoucherList);

    }

    public String MilliSecondToDDMMYYY(String str) {

        long foo = Long.parseLong(str);
        Date date = new Date(foo);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public String saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);

        return Long.toString(date.getTime());
    }

    public String fetchDataBetweenTwoDates(String fromDate, String toDate, String voucherNo) throws ParseException, Exception {
        List<Query> qlist = new ArrayList<Query>();
        Query query = new Query();
        query.setColumnName("voucherDate");
        fromDate = new ReceiptVoucherManager().saveInMilliSecond(fromDate);
        //System.out.println(fromDate);
        query.setColumnValue(fromDate);
        query.setCondition(ApplicationConstants.LOGICAL_GTE);
        qlist.add(query);

        query = new Query();
        query.setColumnName("voucherDate");
        toDate = new ReceiptVoucherManager().saveInMilliSecond(toDate);
        //System.out.println(toDate);
        query.setColumnValue(toDate);
        query.setCondition(ApplicationConstants.LOGICAL_LTE);
        qlist.add(query);

        query = new Query();
        query.setColumnName("status");
        query.setColumnValue("Active");
        query.setCondition(ApplicationConstants.LOGICAL_IN);
        qlist.add(query);

        String result = new DBManager().getDbConnection().fetchRowsByConditions(ApplicationConstants.RECEIPT_VOUCHER_TABLE, qlist);

        //System.out.println(result);
        return result;

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
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + "/receiptvoucher`";
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
        }

        if (filterMap != null && filterMap.size() > 0) {
            quString = Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, quString);
        }

        String EndPoint = (String) config.getProperty("aql_url");
        String json = aql.getRestData(EndPoint, quString);
        return json;
    }

    public static String getLedgerBasedOnGroup(String group) throws Exception {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("underGroup", group);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
        if (result != null) {
            List<Ledger> ledgerList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
            }.getType());

            for (Ledger li : ledgerList) {

                HashMap<String, String> hmap = new HashMap<String, String>();
                hmap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                hmap.put("governmentLedgerCode", ((LinkedTreeMap<String, String>) li.getId()).get("$oid"));

                String lcStr = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CODE_TABLE, hmap);

                if (lcStr != null) {
                    List<LedgerCodeMaster> lcList = new Gson().fromJson(lcStr, new TypeToken<List<LedgerCodeMaster>>() {
                    }.getType());

                    HashMap<String, String> map = new HashMap<String, String>();
                    
                    map.put("idStr", ((LinkedTreeMap<String, String>) li.getId()).get("$oid"));
                    map.put("ledgerName", li.getLedgerName());
                    map.put("ledgerCode", lcList.get(0).getLedgerCode());
                    
                    list.add(map);

                }

            }

            return new Gson().toJson(list);
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
//        MongoCollection col = getCollection(ApplicationConstants.LEDGER_CATEGORY_TABLE);
//        col.mapReduce(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = new ReceiptVoucherManager().fetchLatestVoucherNumber("582f34960c92ec57796a1e13", "582f34b10c92ec57796a1e1a");
//        //System.out.println("collection size" + col.count());

    }

}
