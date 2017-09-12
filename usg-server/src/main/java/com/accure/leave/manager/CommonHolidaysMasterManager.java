/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.leave.dto.CommonHolidayMaster;
import com.accure.leave.dto.LeaveTypeMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.accure.usg.server.utils.Common;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class CommonHolidaysMasterManager {

    private final String HOLIDAY_TYPE_MASTER_TABLE = ApplicationConstants.USG_DB1 + ApplicationConstants.HOLIDAY_TYPE_MASTER + "`";
    private final String COMMON_HOLIDAYS_MASTER_TABLE = ApplicationConstants.USG_DB1 + ApplicationConstants.COMMON_HOLIDAYS_MASTER + "`";
    private final RestClient aql = new RestClient();

    public String saveCommonHolidaysMaster(CommonHolidayMaster chm, String userId) throws Exception {
        String result = "";
        boolean status1 = false, status2 = false, status3 = false;
        HashMap map = new HashMap();
        if (chm == null || userId == null) {
            result = null;
        }
        if (chm.getCommonHoliday() != null) {
            map.clear();
            map.put("commonHoliday", chm.getCommonHoliday());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map)) {
                result = ApplicationConstants.DUPLICATE;
                status1 = true;
            }
        }
        if (chm.getDisplayOrder() != 0) {
            map.clear();
            map.put("displayOrder", chm.getDisplayOrder());
            map.put("status", ApplicationConstants.ACTIVE);
             String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map);
            if(result1!=null) {
                result = ApplicationConstants.DUPLICATE;
                status2 = true;
            }

        }

        if (status1 != true && status2 != true ) {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            chm.setCreateDate(System.currentTimeMillis() + "");
            chm.setUpdateDate(System.currentTimeMillis() + "");
            chm.setStatus(ApplicationConstants.ACTIVE);
            chm.setCreatedBy(userName);

            String claJson = new Gson().toJson(chm);

            String cid = DBManager.getDbConnection().insert(ApplicationConstants.COMMON_HOLIDAYS_MASTER, claJson);
            if (cid != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }

        return result;

    }

    public String ViewAllCommonHolidaysMaster() throws Exception {
        String query = "select b._id as id, b.displayOrder, a.holidayType, b.commonHoliday "
                + ", b.remarks "
                + "from " + HOLIDAY_TYPE_MASTER_TABLE + " as a "
                + "join " + COMMON_HOLIDAYS_MASTER_TABLE + " as b "
                + "on  (a._id = OID(b.holidayType)) where a.status = \"Active\" "
                + "and b.status=\"Active\" order by b.displayOrder asc";
        String result = aql.getRestData(ApplicationConstants.END_POINT, query);
        return result;

    }

    public static List<CommonHolidayMaster> getHolidayType(List<CommonHolidayMaster> holidaytypeList) throws Exception {

        Map<String, String> holidayTypeMap = new HashMap<String, String>();

        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.HOLIDAY_TYPE_MASTER);
        List<CommonHolidayMaster> religionList = new Gson().fromJson(result, new TypeToken<List<CommonHolidayMaster>>() {
        }.getType());
        for (Iterator<CommonHolidayMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            CommonHolidayMaster next = iterator.next();
            holidayTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getHolidayType());
        }
        for (int i = 0; i < holidaytypeList.size(); i++) {
            String religionId = holidaytypeList.get(i).getHolidayType();
            for (Map.Entry<String, String> entry : holidayTypeMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(religionId)) {
                    holidaytypeList.get(i).setHolidayType(value);
                }
            }
        }
        return holidaytypeList;
    }

    public String updateCommonHolidaysMaster(CommonHolidayMaster holidayMaster, String holidayMasterId, String userId) throws Exception {
        String result = "";
        String result1 = "";
        String result2 = "";

        if (holidayMasterId == null || userId == null) {
            result = null;
        }

        boolean status1 = false, status2 = false, status3 = false;
        HashMap conditionMap = new HashMap();
        HashMap map = new HashMap();
//        <<<<<<<<<<<<<<
        HashMap conditionMap1 = new HashMap();
        conditionMap1.put("status", ApplicationConstants.ACTIVE);
        
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        
        
         if (holidayMaster.getCommonHoliday() != null) {
             result2 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COMMON_HOLIDAYS_MASTER, conditionMap1);

        if (result2 != null) {
            List<CommonHolidayMaster> commonHolidaylist = new Gson().fromJson(result2, new TypeToken<List<CommonHolidayMaster>>() {
            }.getType());
            for (int i = 0; i < commonHolidaylist.size(); i++) {
                String str = commonHolidaylist.get(i).getCommonHoliday();
                String str1 = holidayMaster.getCommonHoliday();
                str = str.replaceAll(" ", "");
                str1 = str1.replaceAll(" ", "");
                if (str.equalsIgnoreCase(str1)) {
                    result = ApplicationConstants.DUPLICATE;
                    status1 = true;
                }
            }
        }
            /*conditionMap.clear();
//            String str = holidayMaster.getCommonHoliday();
//            boolean str1 = str.equalsIgnoreCase(str);
//        HashMap<String, String> holidayCondi = new HashMap<String, String>();
//        holidayCondi.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        String holidayResult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayCondi);
            
            conditionMap.put("commonHoliday", holidayMaster.getCommonHoliday());
            conditionMap.put("status", ApplicationConstants.ACTIVE);
//            if (isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, conditionMap, holidayMasterId)) {
                result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COMMON_HOLIDAYS_MASTER, conditionMap);
                if(result!=null) {
                result = ApplicationConstants.DUPLICATE;
                status1 = true;
                }*/
                
//            }
        }
        if (holidayMaster.getDisplayOrder() != 0) {
            map.clear();
            map.put("displayOrder", holidayMaster.getDisplayOrder());
            map.put("status", ApplicationConstants.ACTIVE);
//           if (isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map, holidayMasterId)) {
            result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map);
            if(result1!=null) {
                result = ApplicationConstants.DUPLICATE;
                status2 = true;
            }
//            }

        }

//        if (holidayMaster.getCommonHoliday() != null) {
//
//            map.clear();
//            map.put("commonHoliday", holidayMaster.getCommonHoliday());
//            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map, holidayMasterId)) {
//                result = ApplicationConstants.DUPLICATE;
//                status1 = true;
//            }
//
//        }
//        if (holidayMaster.getDisplayOrder() != 0) {
//            map.clear();
//            map.put("displayOrder", holidayMaster.getDisplayOrder());
//            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map, holidayMasterId)) {
//                result = ApplicationConstants.DUPLICATE;
//                status2 = true;
//            }
//
//        }


        if (status1 != true && status2 != true) {
            String existJsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId);
            List<CommonHolidayMaster> list = new Gson().fromJson(existJsonObj, new TypeToken<List<CommonHolidayMaster>>() {
            }.getType());
            CommonHolidayMaster masterData = list.get(0);
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            holidayMaster.setHolidayType(holidayMaster.getHolidayType());
            holidayMaster.setCommonHoliday(holidayMaster.getCommonHoliday());
            holidayMaster.setDisplayOrder(holidayMaster.getDisplayOrder());
            holidayMaster.setRemarks(holidayMaster.getRemarks());
            holidayMaster.setCreateDate(masterData.getCreateDate());
            holidayMaster.setUpdateDate(System.currentTimeMillis() + "");
            holidayMaster.setStatus(ApplicationConstants.ACTIVE);
            holidayMaster.setCreatedBy(userName);
            String holidayMasterJson = new Gson().toJson(holidayMaster);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId, holidayMasterJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        if (status1 == true && status2 != true) {
            String existJsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId);
            List<CommonHolidayMaster> list = new Gson().fromJson(existJsonObj, new TypeToken<List<CommonHolidayMaster>>() {
            }.getType());
            CommonHolidayMaster masterData = list.get(0);
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            holidayMaster.setHolidayType(holidayMaster.getHolidayType());
//            holidayMaster.setCommonHoliday(holidayMaster.getCommonHoliday());
            holidayMaster.setDisplayOrder(holidayMaster.getDisplayOrder());
            holidayMaster.setRemarks(holidayMaster.getRemarks());
            holidayMaster.setCreateDate(masterData.getCreateDate());
            holidayMaster.setUpdateDate(System.currentTimeMillis() + "");
            holidayMaster.setStatus(ApplicationConstants.ACTIVE);
            holidayMaster.setCreatedBy(userName);
            String holidayMasterJson = new Gson().toJson(holidayMaster);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId, holidayMasterJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        if (status1 != true && status2 == true) {
            String existJsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId);
            List<CommonHolidayMaster> list = new Gson().fromJson(existJsonObj, new TypeToken<List<CommonHolidayMaster>>() {
            }.getType());
            CommonHolidayMaster masterData = list.get(0);
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            holidayMaster.setHolidayType(holidayMaster.getHolidayType());
            holidayMaster.setCommonHoliday(holidayMaster.getCommonHoliday());
//            holidayMaster.setDisplayOrder(holidayMaster.getDisplayOrder());
            holidayMaster.setRemarks(holidayMaster.getRemarks());
            holidayMaster.setCreateDate(masterData.getCreateDate());
            holidayMaster.setUpdateDate(System.currentTimeMillis() + "");
            holidayMaster.setStatus(ApplicationConstants.ACTIVE);
            holidayMaster.setCreatedBy(userName);
            String holidayMasterJson = new Gson().toJson(holidayMaster);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId, holidayMasterJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
        
//        String result = "";
//        HashMap conditionMap = new HashMap();
//        conditionMap.put("commonHoliday", holidayMaster.getCommonHoliday());
//
//        HashMap map = new HashMap();
//        map.put("displayOrder", holidayMaster.getDisplayOrder());
//
//        if (userId == null) {
//            result = null;
//        } else if ((isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map,holidayMasterId)) || (isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, conditionMap,holidayMasterId))) {
//            result = ApplicationConstants.DUPLICATE;
//            return result;
//        } else {
//
//            User user = new UserManager().fetch(userId);
//            String userName = user.getFname() + " " + user.getLname();
//
//            CommonHolidayMaster commonHoliday = new CommonHolidaysMasterManager().fetch(holidayMasterId);
//            commonHoliday.setHolidayType(holidayMaster.getHolidayType());
//            commonHoliday.setCommonHoliday(holidayMaster.getCommonHoliday());
//            commonHoliday.setDisplayOrder(holidayMaster.getDisplayOrder());
//            commonHoliday.setRemarks(holidayMaster.getRemarks());
//            commonHoliday.setUpdateDate(System.currentTimeMillis() + "");
//            commonHoliday.setStatus(ApplicationConstants.ACTIVE);
//            commonHoliday.setUpdatedBy(userName);
//            String json = new Gson().toJson(commonHoliday);
//            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId, json);
//            if (fResult) {
//                result = ApplicationConstants.SUCCESS;
//            } else {
//                result = ApplicationConstants.FAIL;
//            }
//
//        }
//        return result;
        
//        <<<<<<<<<<<<<<<<<<<<<<<<<,
//          String result = "";
//        HashMap conditionMap = new HashMap();
//        conditionMap.put("commonHoliday", holidayMaster.getCommonHoliday());
//
//        HashMap map = new HashMap();
//        map.put("displayOrder", holidayMaster.getDisplayOrder());
//
//        if (userId == null) {
//            result = null;
//        } else if ((isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, conditionMap,holidayMasterId)) || (isDuplicateforUpdate(ApplicationConstants.COMMON_HOLIDAYS_MASTER, map,holidayMasterId))) {
//            result = ApplicationConstants.DUPLICATE;
//            return result;
//        } else {
//
//            User user = new UserManager().fetch(userId);
//            String userName = user.getFname() + " " + user.getLname();
//
//            CommonHolidayMaster commonHoliday = new CommonHolidaysMasterManager().fetch(holidayMasterId);
//            commonHoliday.setHolidayType(holidayMaster.getHolidayType());
//            commonHoliday.setCommonHoliday(holidayMaster.getCommonHoliday());
//            commonHoliday.setDisplayOrder(holidayMaster.getDisplayOrder());
//            commonHoliday.setRemarks(holidayMaster.getRemarks());
//            commonHoliday.setUpdateDate(System.currentTimeMillis() + "");
//            commonHoliday.setStatus(ApplicationConstants.ACTIVE);
//            commonHoliday.setUpdatedBy(userName);
//            String json = new Gson().toJson(commonHoliday);
//            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.COMMON_HOLIDAYS_MASTER, holidayMasterId, json);
//            if (fResult) {
//                result = ApplicationConstants.SUCCESS;
//            } else {
//                result = ApplicationConstants.FAIL;
//            }
//
//        }
//        return result;


//<<<<<<<<<<<<<<<<<
    }
    
    public CommonHolidayMaster fetch(String commonHolidayId) throws Exception {
        if (commonHolidayId == null || commonHolidayId.isEmpty()) {
            return null;
        }
        String commonHolidayJson = DBManager.getDbConnection().fetch(ApplicationConstants.COMMON_HOLIDAYS_MASTER, commonHolidayId);
        if (commonHolidayJson == null || commonHolidayJson.isEmpty()) {
            return null;
        }
        List<CommonHolidayMaster> commonHolidayLIst = new Gson().fromJson(commonHolidayJson, new TypeToken<List<CommonHolidayMaster>>() {
        }.getType());
        if (commonHolidayLIst == null || commonHolidayLIst.isEmpty()) {
            return null;
        }
        return commonHolidayLIst.get(0);

    }

    public String deleteCommonHolidayMaster(String id, String userId) throws Exception {
        String result = "";

        if (id == null || userId == null) {
            result = ApplicationConstants.SUCCESS;
        }
        else if ((DeleteDependencyManager.hasDependency(ApplicationConstants.LEAVE_TYPE_MASTER, "fixedLeaveType", id))||(DeleteDependencyManager.hasDependency(ApplicationConstants.LOCATION_WISE_HOLIDAY_MASTER, "locationWiseHolidayFormList.commonHolidayId", id))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        }
        else {
            String commonHolidayJson = DBManager.getDbConnection().fetch(ApplicationConstants.COMMON_HOLIDAYS_MASTER, id);
            List<CommonHolidayMaster> commonHolidaylist = new Gson().fromJson(commonHolidayJson, new TypeToken<List<CommonHolidayMaster>>() {
            }.getType());
            CommonHolidayMaster commonHolidayMasterlist = commonHolidaylist.get(0);
            CommonHolidayMaster commonHolidayMaster = new CommonHolidayMaster();
            commonHolidayMaster.setHolidayType(commonHolidayMasterlist.getHolidayType());
            commonHolidayMaster.setCommonHoliday(commonHolidayMasterlist.getCommonHoliday());
            commonHolidayMaster.setDisplayOrder(commonHolidayMasterlist.getDisplayOrder());
            commonHolidayMaster.setRemarks(commonHolidayMasterlist.getRemarks());
            commonHolidayMaster.setCreateDate(commonHolidayMasterlist.getCreateDate());
            commonHolidayMaster.setStatus(ApplicationConstants.INACTIVE);
            commonHolidayMaster.setUpdateDate(System.currentTimeMillis() + "");
            String commonHolidayMasterJson = new Gson().toJson(commonHolidayMaster);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.COMMON_HOLIDAYS_MASTER, id, commonHolidayMasterJson);
            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }
}
