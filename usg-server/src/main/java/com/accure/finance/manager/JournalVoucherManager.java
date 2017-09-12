/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.accure.db.Query;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.JournalVoucher;
import com.accure.finance.dto.Location;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONObject;

/**
 *
 * @author Asif/ankur
 */
public class JournalVoucherManager {

    public String save(JournalVoucher journalVoucher, String loginUserId, String ddoId, String locationId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        DDO name = (new DDOManager().fetch(ddoId));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(locationId);
        String locationName = locName.getLocationName();
        journalVoucher.setDdoName(ddoName);
        journalVoucher.setLocationName(locationName);
        journalVoucher.setVoucherName("JournalVoucher");
        journalVoucher.setCreateDate(System.currentTimeMillis() + "");
        journalVoucher.setUpdateDate(System.currentTimeMillis() + "");
        journalVoucher.setStatus(ApplicationConstants.ACTIVE);
        journalVoucher.setPostingStatus(ApplicationConstants.NEW);
        journalVoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(journalVoucher.getVoucherDate())));
        journalVoucher.setCreatedBy(userName);

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = result.substring(1, result.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        journalVoucher.setBudgetHeadName(bh);

        String journalVoucherJson = new Gson().toJson(journalVoucher);
        String jvId = DBManager.getDbConnection().insert(ApplicationConstants.JOURNAL_VOUCHER_TABLE, journalVoucherJson);
        if (jvId != null) {
            return jvId;
        }
        return null;
    }

    public String fetch(String jvId) throws Exception {
        if (jvId == null || jvId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.JOURNAL_VOUCHER_TABLE, jvId);
        List<JournalVoucher> journalVoucherList = new Gson().fromJson(result, new TypeToken<List<JournalVoucher>>() {
        }.getType());
        if (journalVoucherList == null || journalVoucherList.size() < 1) {
            return null;
        }
        return new Gson().toJson(journalVoucherList.get(0));
    }

    public boolean delete(String jvId, String loginUserId) throws Exception {
        if (jvId == null || jvId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<JournalVoucher>() {
        }.getType();
        String journalVoucher = new JournalVoucherManager().fetch(jvId);
        if (journalVoucher == null || journalVoucher.isEmpty()) {
            return false;
        }
        JournalVoucher journalVoucherrJson = new Gson().fromJson(journalVoucher, type);

        if (!journalVoucherrJson.getPostingStatus().equals("Posted")) {
            journalVoucherrJson.setStatus(ApplicationConstants.INACTIVE);
            journalVoucherrJson.setUpdatedBy(userName);
            journalVoucherrJson.setDeletedBy(userName);
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.JOURNAL_VOUCHER_TABLE, jvId, new Gson().toJson(journalVoucherrJson));
            if (result) {
                return true;
            }
        }
        return false;
    }

    public boolean update(JournalVoucher journalVoucher, String jvId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        DDO name = (new DDOManager().fetch(journalVoucher.getDDO()));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(journalVoucher.getLocation());
        String locationName = locName.getLocationName();
        journalVoucher.setDdoName(ddoName);
        journalVoucher.setLocationName(locationName);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String budgetresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = budgetresult.substring(1, budgetresult.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        journalVoucher.setBudgetHeadName(bh);
        journalVoucher.setVoucherName("JournalVoucher");
        journalVoucher.setUpdateDate(System.currentTimeMillis() + "");
        journalVoucher.setStatus(ApplicationConstants.ACTIVE);
        journalVoucher.setUpdatedBy(userName);
        journalVoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(journalVoucher.getVoucherDate())));
        String journalVoucherrJson = new Gson().toJson(journalVoucher);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.JOURNAL_VOUCHER_TABLE, jvId, journalVoucherrJson);
        return result;
    }

    public String fetchAll(HashMap<String, String> filterMap) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, null);
        conditionMap.putAll(filterMap);

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.JOURNAL_VOUCHER_TABLE, conditionMap);
        return result1;
    }

    public String getAllDocuments() throws Exception {
        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.JOURNAL_VOUCHER_TABLE);
        return result1;
    }

    public String GetLatestVoucherByGettingAllRows(String ddoId, String locationId) throws Exception {
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

        String result = new JournalVoucherManager().getAllDocuments();
        List<JournalVoucher> paymentVoucherList = new Gson().fromJson(result, new TypeToken<List<JournalVoucher>>() {
        }.getType());
        int numb = 0;
        JournalVoucher pv = new JournalVoucher();
        if (paymentVoucherList != null) {
            for (Iterator<JournalVoucher> iterator = paymentVoucherList.iterator(); iterator.hasNext();) {
                JournalVoucher next = iterator.next();
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
        String pattern = ddoCode + "/" + locationCode + "/JV-" + Integer.toString(numb);
        pv.setVoucherNo(pattern);
        List<JournalVoucher> li = new ArrayList<JournalVoucher>();
        li.add(pv);
        return new Gson().toJson(li);
    }

    public String Search(String fromdate, String ToDate, String voucherNo) throws ParseException, Exception {
        fromdate = saveInMilliSecond(fromdate);
        ToDate = saveInMilliSecond(ToDate);
        voucherNo = voucherNo;
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put(ApplicationConstants.EQUAL, ApplicationConstants.ACTIVE);
        innerMap.put("voucherNo", voucherNo);
        conditionMap.put(ApplicationConstants.STATUS, innerMap);
        String result1 = DBManager.getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.JOURNAL_VOUCHER_TABLE, "voucherDate", fromdate, ToDate, conditionMap);
        List<JournalVoucher> receiptvoucherList = new Gson().fromJson(result1, new TypeToken<List<JournalVoucher>>() {
        }.getType());
        if (receiptvoucherList != null) {
            for (Iterator<JournalVoucher> iterator = receiptvoucherList.iterator(); iterator.hasNext();) {
                JournalVoucher next = iterator.next();
                if (next.getVoucherDate() != null) {
                    next.setVoucherDate(new JournalVoucherManager().MilliSecondToDDMMYYY(next.getVoucherDate()));
                }
            }
        }
        return new Gson().toJson(receiptvoucherList);
    }

    public String fetchLatestWithList() throws Exception {
        List<Query> mainQuery = new ArrayList<Query>();
        Query qu = new Query();
        qu.setColumnName("SORTKEY");
        qu.setColumnValue("createDate");
        mainQuery.add(qu);

        qu = new Query();
        qu.setColumnName("SORTORDER");
        qu.setColumnValue("-1");
        mainQuery.add(qu);

        qu = new Query();
        qu.setColumnName("status");
        qu.setColumnValue(ApplicationConstants.ACTIVE);
        mainQuery.add(qu);

        String result = DBManager.getDbConnection().fetchRowsByConditions(ApplicationConstants.PAYMENT_VOUCHER_TABLE, mainQuery);
        return result;
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

    public String SearchUsingAQL(String fromDate, String toDate, String voucherNo,
            HashMap<String, Object> filterMap) throws ParseException {
        RestClient aql = new RestClient();
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + "/journalvoucher`";
        String voucherNum = "\"%" + voucherNo + "%\"";
        String Active = "\"Active\"";
        String quString = "";
        if ((!fromDate.isEmpty()) && (!toDate.isEmpty()) && (!voucherNo.isEmpty())) {
            fromDate = saveInMilliSecond(fromDate);
            toDate = saveInMilliSecond(toDate);
            quString = "select  *,_id from  " + mongoCollectionName + "  where  voucherDateInMilliSecond between " + fromDate + " and " + toDate + " and voucherNo like " + voucherNum + " and  status = " + Active;
        } else if ((!fromDate.isEmpty()) && (!toDate.isEmpty()) && (voucherNo.isEmpty())) {
            fromDate = saveInMilliSecond(fromDate);
            toDate = saveInMilliSecond(toDate);
            quString = "select  *,_id from  " + mongoCollectionName + " where  voucherDateInMilliSecond between " + fromDate + " and " + toDate + " and status =" + Active;
        } else if ((fromDate.isEmpty()) && (toDate.isEmpty()) && (!voucherNo.isEmpty())) {
            quString = "select  *,_id from  " + mongoCollectionName + "  where  voucherNo like " + voucherNum + "and status =" + Active;
        }

        quString = Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, quString);

        String EndPoint = (String) config.getProperty("aql_url");
        String json = aql.getRestData(EndPoint, quString);
        return json;
    }
}
