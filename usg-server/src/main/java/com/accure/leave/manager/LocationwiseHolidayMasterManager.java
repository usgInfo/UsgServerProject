/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.Location;
import com.accure.leave.dto.FinancialYear;
import com.accure.leave.dto.HolidayLocationMaster;
import com.accure.leave.dto.LocationWiseHolidayMaster;
import com.accure.leave.dto.LocationWiseHolidayMasterList;
import com.accure.leave.dto.WeeklyOffMasterList;
import static com.accure.payroll.manager.EmployeeArrearManager.saveInMilliSecond;
import com.accure.user.dto.DdoLocationMap;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class LocationwiseHolidayMasterManager {

    private String LOCATION_WISE_HOLIDAY_MASTER_TABLE = ApplicationConstants.USG_DB1 + ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER + "`";
    private String COMMON_HOLIDAYS_MASTER_TABLE = ApplicationConstants.USG_DB1 + ApplicationConstants.COMMON_HOLIDAYS_MASTER + "`";
    private RestClient aql = new RestClient();

    public String saveLocationwiseHolidayMaster(LocationWiseHolidayMaster lwlm, String userId) throws Exception {

        String result;
        HashMap map = new HashMap();
        map.put("location", lwlm.getLocation());
        map.put("year", lwlm.getYear());
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (userId == null) {
            result = null;
        } else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, map))) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            lwlm.setCreateDate(System.currentTimeMillis() + "");
            lwlm.setCreatedBy(userName);
            lwlm.setUpdateDate(System.currentTimeMillis() + "");
            lwlm.setStatus(ApplicationConstants.ACTIVE);
            String hlmJson = new Gson().toJson(lwlm);
            String cid = DBManager.getDbConnection().insert(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, hlmJson);

            if (cid != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String getAllLocationBasedOnDDO(String ddo) throws Exception {

        List<DdoLocationMap> list = new ArrayList<DdoLocationMap>();

        List<Location> locations = new ArrayList<Location>();
        if (ddo == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
        if (result != null) {
            list = new Gson().fromJson(result, new TypeToken<List<DdoLocationMap>>() {
            }.getType());
        }

        for (String str : list.get(0).getLocation()) {

            String locationStr = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_TABLE, str);

            List<Location> subList = new Gson().fromJson(locationStr, new TypeToken<List<Location>>() {
            }.getType());
            locations.add(subList.get(0));
        }

        return new Gson().toJson(locations);
    }

    public static void main(String[] args) throws Exception {
        String result = new LocationwiseHolidayMasterManager().ViewAllLocationWiseHolidayMaster1("587de3d5a3ad201b1bd9af18");
        System.out.println("final result is" + result);
    }

    public String ViewAllLocationWiseHolidayMaster() throws Exception {
        FinancialYear fy = new FinancialYearManager().fetchActiveLeaveFinancialYear();
        String query = "select a.holidayType, b.commonHoliday \n"
                + "from " + LOCATION_WISE_HOLIDAY_MASTER_TABLE + " as a\n"
                + "join " + COMMON_HOLIDAYS_MASTER_TABLE + " as b\n"
                + "on  (a._id = OID(b.holidayType)) where a.status = \"Active\" and a.year = \"" + ((LinkedTreeMap) fy.getId()).get("$oid") + "\"";
        String result = aql.getRestData(ApplicationConstants.END_POINT, query);

        return result;
    }

    public String ViewAllLocationWiseHolidayMaster1(String ddo) throws Exception {

        List<DdoLocationMap> list = new ArrayList<DdoLocationMap>();
        List<LocationWiseHolidayMasterList> flist = new ArrayList<LocationWiseHolidayMasterList>();

        if (ddo == null) {
            return null;
        }

        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
        if (result != null) {
            list = new Gson().fromJson(result, new TypeToken<List<DdoLocationMap>>() {
            }.getType());
        }

        for (String str : list.get(0).getLocation()) {

            FinancialYear fy = new FinancialYearManager().fetchActiveLeaveFinancialYear();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            map.put("year", ((LinkedTreeMap) fy.getId()).get("$oid") + "");
            map.put("locationWiseHoliday", str);
            String fresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, map);

            if (fresult != null) {
                List<LocationWiseHolidayMasterList> lwhlist = new Gson().fromJson(fresult, new TypeToken<List<LocationWiseHolidayMasterList>>() {
                }.getType());
                flist.add(lwhlist.get(0));
            }

        }

        flist = getYear(flist);
        flist = getLocation(flist);

        return new Gson().toJson(flist);

    }

    private List<LocationWiseHolidayMasterList> getYear(List<LocationWiseHolidayMasterList> LWHList) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_FINACIAL_YEAR_TABLE);
        List<FinancialYear> list = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        for (Iterator<FinancialYear> iterator = list.iterator(); iterator.hasNext();) {
            FinancialYear next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getYear());
        }
        for (int i = 0; i < LWHList.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(LWHList.get(i).getYear())) {
                    LWHList.get(i).setYear(entry.getValue());
                }
            }
        }
        return LWHList;
    }

    private List<LocationWiseHolidayMasterList> getLocation(List<LocationWiseHolidayMasterList> employeeList) throws Exception {
        Map<String, String> PFTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            PFTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getLocationWiseHoliday())) {
                    employeeList.get(i).setLocationWiseHoliday(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public boolean updateLocationWiseHolidaysMaster(LocationWiseHolidayMaster locationwiseholidayMaster, String locationwiseholidayMasterId) throws Exception {
        String existJsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, locationwiseholidayMasterId);
        List<LocationWiseHolidayMaster> list = new Gson().fromJson(existJsonObj, new TypeToken<List<LocationWiseHolidayMaster>>() {
        }.getType());
        LocationWiseHolidayMaster masterData = list.get(0);
        locationwiseholidayMaster.setCreateDate(masterData.getCreateDate());
        locationwiseholidayMaster.setUpdateDate(System.currentTimeMillis() + "");
        locationwiseholidayMaster.setStatus(ApplicationConstants.ACTIVE);
        locationwiseholidayMaster.setId(null);
        String locationwiseholidayMasterJson = new Gson().toJson(locationwiseholidayMaster);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, locationwiseholidayMasterId, locationwiseholidayMasterJson);
        return result;
    }

    public String updateLocationWiseHolidaysMaster1(LocationWiseHolidayMasterList obj, String id, String location, String year, String userID) throws Exception {

        String result;
        HashMap map = new HashMap();

        map.put("locationWiseHoliday", location);
        map.put("year", year);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (userID == null || id == null) {
            result = ApplicationConstants.SUCCESS;
        } else if ((Duplicate.isDuplicateforUpdate(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, map, id))) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            User user = new UserManager().fetch(userID);
            String userName = user.getFname() + " " + user.getLname();
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, id);
            List<LocationWiseHolidayMasterList> lwhmList = new Gson().fromJson(existrelationJson, new TypeToken<List<LocationWiseHolidayMasterList>>() {
            }.getType());
            LocationWiseHolidayMasterList lwhm = lwhmList.get(0);
            List<LocationWiseHolidayMaster> lwhmFinalList = new ArrayList<LocationWiseHolidayMaster>();
            List<LocationWiseHolidayMaster> lwhmFinalList1 = new ArrayList<LocationWiseHolidayMaster>();
            for (int i = 0; i < obj.getLocationWiseHolidayFormList().size(); i++) {
                obj.getLocationWiseHolidayFormList().get(i).setFromDateInMilliSecond(saveInMillis(obj.getLocationWiseHolidayFormList().get(i).getFromDate()));
                lwhmFinalList.add(obj.getLocationWiseHolidayFormList().get(i));
                Collections.sort(lwhmFinalList, new LocationWiseHolidaysSortBy());
            }
//            System.out.println("Listttttttttttttttttttt" + lwhmFinalList);
            for (int i = 0; i < lwhmFinalList.size(); i++) {
                LocationWiseHolidayMaster data = lwhmFinalList.get(i);
                lwhmFinalList1.add(data);
            }
            obj.setLocationWiseHolidayFormList(lwhmFinalList1);
            obj.setCreateDate(lwhm.getCreateDate());
            obj.setLocationWiseHoliday(location);
            obj.setYear(year);
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setUpdatedBy(userName);
            obj.setStatus(ApplicationConstants.ACTIVE);
            String locationWiseHolidayMaster = new Gson().toJson(obj);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, id, locationWiseHolidayMaster);

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public boolean deleteLocationwiseHolidayMaster(String id) throws Exception {
//        User user = new UserManager().fetch(userId);
//        String userName = user.getFname() + " " + user.getLname();
        String locationwiseHolidayJson = DBManager.getDbConnection().fetch(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, id);
        List<LocationWiseHolidayMasterList> LocationwiseHolidaylist = new Gson().fromJson(locationwiseHolidayJson, new TypeToken<List<LocationWiseHolidayMasterList>>() {
        }.getType());
        LocationWiseHolidayMasterList locationwiseHolidayMasterlist = LocationwiseHolidaylist.get(0);
        LocationWiseHolidayMasterList locationwiseHolidayMaster = new LocationWiseHolidayMasterList();
        locationwiseHolidayMaster.setLocationWiseHoliday(locationwiseHolidayMasterlist.getLocationWiseHoliday());
        locationwiseHolidayMaster.setYear(locationwiseHolidayMasterlist.getYear());
        locationwiseHolidayMaster.setCreateDate(locationwiseHolidayMasterlist.getCreateDate());
        locationwiseHolidayMaster.setStatus(ApplicationConstants.DELETE);
        locationwiseHolidayMaster.setUpdateDate(System.currentTimeMillis() + "");
        String locationwiseHolidayMasterJson = new Gson().toJson(locationwiseHolidayMaster);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, id, locationwiseHolidayMasterJson);
        return status;
    }

//    public String viewLocationWiseHolidayMaster(String holidayLocation, Integer year) throws Exception {
//
//        String attQuery = "select *,_id from " + LOCATION_WISE_HOLIDAY_MASTER_TABLE + " where location=\"" + holidayLocation + "\""
//                + " and year=" + year;
//        String result = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
//        return result;
//
//    }
    public String viewLocationWiseHolidayMaster(String holidayLocation, String year) throws Exception {

//          String lwhmQuery = "select chm.*,lhm.*,lhm._id from " + COMMON_HOLIDAYS_MASTER_TABLE  + " as chm left join " + LOCATION_WISE_HOLIDAY_MASTER_TABLE + " as lhm on (chm._id = OID (lhm.locationWiseHolidayFormList.commonHolidayId)) ";
        //+ "where lhm.location=\"" + holidayLocation + "\""
        //+ " and chm.year=\"" + year + "\""; + "and status= \"Active\""
        String lwhmQuery = "select *,_id from " + LOCATION_WISE_HOLIDAY_MASTER_TABLE + " where locationWiseHoliday=\"" + holidayLocation + "\""
                + " and year=\"" + year + "\"" + "and status= \"Active\"";
        String result = aql.getRestData(ApplicationConstants.END_POINT, lwhmQuery);
        return result;

    }

    public String save(LocationWiseHolidayMasterList obj, String locationWiseHoliday, String year, String userId) throws Exception {
        System.out.println("location value for location wise holiday" + locationWiseHoliday);
        String result;
        HashMap map = new HashMap();

        map.put("locationWiseHoliday", locationWiseHoliday);
        map.put("year", year);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (userId == null) {
            result = null;
        } else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, map))) {
            result = ApplicationConstants.DUPLICATE;
        } else {
            List<LocationWiseHolidayMaster> lwhmFinalList = new ArrayList<LocationWiseHolidayMaster>();
            List<LocationWiseHolidayMaster> lwhmFinalList1 = new ArrayList<LocationWiseHolidayMaster>();
            for (int i = 0; i < obj.getLocationWiseHolidayFormList().size(); i++) {
                obj.getLocationWiseHolidayFormList().get(i).setFromDateInMilliSecond(saveInMillis(obj.getLocationWiseHolidayFormList().get(i).getFromDate()));
                lwhmFinalList.add(obj.getLocationWiseHolidayFormList().get(i));
                Collections.sort(lwhmFinalList, new LocationWiseHolidaysSortBy());
            }
//            System.out.println("Listttttttttttttttttttt" + lwhmFinalList);
            for (int i = 0; i < lwhmFinalList.size(); i++) {
                LocationWiseHolidayMaster data = lwhmFinalList.get(i);
                lwhmFinalList1.add(data);
            }
            obj.setLocationWiseHolidayFormList(lwhmFinalList1);
//                List<LocationWiseHolidayMaster> merged = new ArrayList(lwhmFinalList);
//                if (merged != null) {
//                     Collections.sort(lwhmFinalList, new LocationWiseHolidaysSortBy());
//                }
            obj.setLocationWiseHoliday(locationWiseHoliday);
            obj.setYear(year);
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setUpdateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
//            obj.getLocationWiseHolidayFormList().set(0, element)
//            obj.setFromDateInMilliSecondReport(Long.parseLong(saveInMilliSecond(obj.getFromDateReport())));
            String objJson = new Gson().toJson(obj);
            String objId = DBManager.getDbConnection().insert(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, objJson);
//            String objJson1 = new Gson().toJson(merged);
//            String objId1 = DBManager.getDbConnection().insert(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, objJson1);
            if (objId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    public String saveInMilliSeconds(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);

        return Long.toString(date.getTime());
    }

    public Long saveInMillis(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);

        return date.getTime();
    }
}
