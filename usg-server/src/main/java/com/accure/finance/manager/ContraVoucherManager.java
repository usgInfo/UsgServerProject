/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.ContraVoucher;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
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
public class ContraVoucherManager {

    public String save(ContraVoucher contraVoucher, String loginUserId, String ddoId, String locationId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        DDO name = (new DDOManager().fetch(ddoId));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(locationId);
        String locationName = locName.getLocationName();
        contraVoucher.setDdoName(ddoName);
        contraVoucher.setLocationName(locationName);
        contraVoucher.setVoucherName("ContraVoucher");
        contraVoucher.setEntryStatus("Uncleared Entries");
        contraVoucher.setCreateDate(System.currentTimeMillis() + "");
        contraVoucher.setUpdateDate(System.currentTimeMillis() + "");
        contraVoucher.setStatus(ApplicationConstants.ACTIVE);
        contraVoucher.setCreatedBy(userName);
        contraVoucher.setPostingStatus(ApplicationConstants.NEW);
        contraVoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(contraVoucher.getVoucherDate())));

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = result.substring(1, result.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        contraVoucher.setBudgetHeadName(bh);

        String contraVoucherJson = new Gson().toJson(contraVoucher);
        String rvId = DBManager.getDbConnection().insert(ApplicationConstants.CONTRA_VOUCHER_TABLE, contraVoucherJson);
        if (rvId != null) {
            return rvId;
        }
        return null;
    }

    public String fetch(String rvId) throws Exception {
        if (rvId == null || rvId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONTRA_VOUCHER_TABLE, rvId);
        List<ContraVoucher> contraVoucherList = new Gson().fromJson(result, new TypeToken<List<ContraVoucher>>() {
        }.getType());
        if (contraVoucherList == null || contraVoucherList.size() < 1) {
            return null;
        }
        return new Gson().toJson(contraVoucherList.get(0));
    }

    public boolean delete(String rvId, String loginUserId) throws Exception {
        if (rvId == null || rvId.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<ContraVoucher>() {
        }.getType();
        String contraVoucher = new ContraVoucherManager().fetch(rvId);
        if (contraVoucher == null || contraVoucher.isEmpty()) {
            return false;
        }
        ContraVoucher contraVoucherrJson = new Gson().fromJson(contraVoucher, type);
        if (!contraVoucherrJson.getPostingStatus().equals("Posted")) {
            contraVoucherrJson.setStatus(ApplicationConstants.INACTIVE);
            contraVoucherrJson.setUpdatedBy(userName);
            contraVoucherrJson.setDeletedBy(userName);
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONTRA_VOUCHER_TABLE, rvId, new Gson().toJson(contraVoucherrJson));
            if (result) {
                return true;
            }
        }
        return false;
    }

    public boolean update(ContraVoucher contraVoucher, String rvId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        DDO name = (new DDOManager().fetch(contraVoucher.getDDO()));
        String ddoName = name.getDdoName();
        Location locName = new LocationManager().fetch(contraVoucher.getLocation());
        String locationName = locName.getLocationName();
        contraVoucher.setDdoName(ddoName);
        contraVoucher.setLocationName(locationName);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put(ApplicationConstants.ISACTIVE, ApplicationConstants.YES);
        String budgetresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, conditionMap);
        String rs1 = budgetresult.substring(1, budgetresult.length() - 1);
        JSONObject json = new JSONObject(rs1);
        String bh = json.getString("budgetHead");
        contraVoucher.setEntryStatus("Uncleared Entries");
        contraVoucher.setBudgetHeadName(bh);
        contraVoucher.setVoucherName("ContraVoucher");
        contraVoucher.setUpdateDate(System.currentTimeMillis() + "");
        contraVoucher.setStatus(ApplicationConstants.ACTIVE);
        contraVoucher.setUpdatedBy(userName);
        contraVoucher.setVoucherDateInMilliSecond(Long.parseLong(saveInMilliSecond(contraVoucher.getVoucherDate())));
//        if (contraVoucher.getChequeDate() != null) {
//            contraVoucher.setChequeDateInMilliSecond(Long.parseLong(saveInMilliSecond(contraVoucher.getChequeDate())));
//        }

        String contraVoucherrJson = new Gson().toJson(contraVoucher);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.CONTRA_VOUCHER_TABLE, rvId, contraVoucherrJson);
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

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONTRA_VOUCHER_TABLE, conditionMap);
        List<ContraVoucher> list = new Gson().fromJson(result1, new TypeToken<List<ContraVoucher>>() {
        }.getType());

        return new Gson().toJson(list);
    }

    public String fetchAllDocuments() throws Exception {
        String result1 = DBManager.getDbConnection().fetchAll(ApplicationConstants.CONTRA_VOUCHER_TABLE);
        return result1;
    }

    public String getLatestVoucherNumber(String ddoId, String locationId) throws Exception {
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

        String result = new ContraVoucherManager().fetchAllDocuments();
        List<ContraVoucher> paymentVoucherList = new Gson().fromJson(result, new TypeToken<List<ContraVoucher>>() {
        }.getType());
        int numb = 0;
        ContraVoucher pv = new ContraVoucher();
        if (paymentVoucherList != null) {
            for (Iterator<ContraVoucher> iterator = paymentVoucherList.iterator(); iterator.hasNext();) {
                ContraVoucher next = iterator.next();
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
        String pattern = ddoCode + "/" + locationCode + "/CV-" + Integer.toString(numb);
        pv.setVoucherNo(pattern);
        List<ContraVoucher> li = new ArrayList<ContraVoucher>();
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
        String result1 = DBManager.getDbConnection().fetchRowsByDatePeriods(ApplicationConstants.CONTRA_VOUCHER_TABLE, "voucherDate", fromdate, ToDate, conditionMap);

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
            HashMap<String, String> filterMap) throws ParseException {
        RestClient aql = new RestClient();
        PropertiesConfiguration config = getConfig();
        String mongoCollectionName = "`" + (String) config.getProperty("aql_db") + "contravoucher`";
        String voucherNum = "\"%" + voucherNo + "%\"";
        String Active = "\"Active\"";
        String quString = "";
        if ((!fromDate.isEmpty()) && (!toDate.isEmpty()) && (!voucherNo.isEmpty())) {
            //System.out.println("in first method");
            fromDate = saveInMilliSecond(fromDate);
            toDate = saveInMilliSecond(toDate);
            quString = "select  *,_id from  " + mongoCollectionName + "  where  voucherDateInMilliSecond between " + fromDate + " and " + toDate + " and voucherNo like " + voucherNum + " and  status = " + Active;
        } else if ((!fromDate.isEmpty()) && (!toDate.isEmpty()) && (voucherNo.isEmpty())) {
            //System.out.println("in second method");
            fromDate = saveInMilliSecond(fromDate);
            toDate = saveInMilliSecond(toDate);
            quString = "select  *,_id from  " + mongoCollectionName + " where  voucherDateInMilliSecond between " + fromDate + " and " + toDate + " and status =" + Active;
        } else if ((fromDate.isEmpty()) && (toDate.isEmpty()) && (!voucherNo.isEmpty())) {
            //System.out.println("in third method");
            quString = "select  *,_id from  " + mongoCollectionName + "  where  voucherNo like " + voucherNum + "and status =" + Active;
        }

        quString = Common.removeNullAndEmptyValuesInMapAndBuildQuery(filterMap, quString);

        //System.out.println(quString);
        String EndPoint = (String) config.getProperty("aql_url");
        String json = aql.getRestData(EndPoint, quString);
        return json;
    }

    private List<ContraVoucher> getLocation(List<ContraVoucher> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getLocation())) {
                    list.get(i).setLocation(entry.getValue());
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        String result = new ContraVoucherManager().getLatestVoucherNumber("58412160d0bd0c24361f0ec9", "584121a5d0bd0c24361f0ee5");
    }
}
