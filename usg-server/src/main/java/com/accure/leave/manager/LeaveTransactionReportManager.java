/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.leave.dto.LeaveTransaction;
import com.accure.leave.dto.LeaveTypeMaster;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author user
 */
public class LeaveTransactionReportManager {

    private final RestClient aql = new RestClient();
    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

    public String searchEmployeeWithLeave(LeaveTransaction emp, String sortBy) {

        String result = null;

        try {

            Long fromDateInDate = format1.parse(emp.getFromDate()).getTime();
            Long toDateInDate = format1.parse(emp.getToDate()).getTime();

            String query = "select leaveTransaction._id as id, leaveTransaction.employeeCode, "
                    + "leaveTransaction.employeeName, leaveTransaction.employeeId,"
                    + "leaveTransaction.leaveTypeDescription, leaveTransaction.fromDate, leaveTransaction.toDate, \n"
                    + "leaveTransaction.employeeCodeM "
                    + ",leaveTransaction.dateRemarksAndIsHalfDay, location.locationName, "
                    + "departmentTable.department, designation.designation from \n"
                    + "`/usg_db/usg/leaveTransaction` as leaveTransaction join \n"
                    + "`/usg_db/usg/location` as location on (location._id = "
                    + "OID(leaveTransaction.location)) join \n"
                    + "`/usg_db/usg/departmentTable` as departmentTable on "
                    + "(departmentTable._id = OID(leaveTransaction.department)) join \n"
                    + "`/usg_db/usg/designation` as designation on (designation._id = "
                    + "OID(leaveTransaction.designation)) join `/usg_db/usg/ddo` as "
                    + "ddo on (ddo._id = OID(leaveTransaction.ddo)) join \n"
                    + "`/usg_db/usg/cityTable` as cityTable on (cityTable._id = "
                    + "OID(leaveTransaction.postingCity)) join \n"
                    + "`/usg_db/usg/naturemaster` as naturemaster on (naturemaster._id = "
                    + "OID(leaveTransaction.natureType)) where ddo._id=OID(\"" + emp.getDdo() + "\") and"
                    + " leaveTransaction.fromDateInMilliSecond>=" + fromDateInDate + " and "
                    + "leaveTransaction.toDateInMilliSecond<=" + toDateInDate + " \n";

            if (emp.getEmployeeCode() != null && !emp.getEmployeeCode().equalsIgnoreCase("")) {
                query += "and leaveTransaction.employeeCode=\"" + emp.getEmployeeCode() + "\" \n";
            }
            if (emp.getEmployeeCodeM() != null && !emp.getEmployeeCodeM().equalsIgnoreCase("")) {
                query += "and leaveTransaction.employeeCodeM=\"" + emp.getEmployeeCodeM() + "\" \n";
            }
            if (emp.getEmployeeName() != null && !emp.getEmployeeName().equalsIgnoreCase("")) {
                query += "and leaveTransaction.employeeName=\"" + emp.getEmployeeCodeM() + "\" \n";
            }
            if (emp.getLocation() != null && !emp.getLocation().equalsIgnoreCase("")) {
                query += "and location._id=OID(\"" + emp.getLocation() + "\") \n";
            }
            if (emp.getDepartment() != null && !emp.getDepartment().equalsIgnoreCase("")) {
                query += "and departmentTable._id=OID(\"" + emp.getDepartment() + "\") \n";
            }
            if (emp.getDesignation() != null && !emp.getDesignation().equalsIgnoreCase("")) {
                query += "and designation._id=OID(\"" + emp.getDesignation() + "\") \n";
            }
            if (emp.getNatureType() != null && !emp.getNatureType().equalsIgnoreCase("")) {
                query += "and naturemaster._id=OID(\"" + emp.getNatureType() + "\") \n";
            }
            if (emp.getPostingCity() != null && !emp.getPostingCity().equalsIgnoreCase("")) {
                query += "and cityTable._id=OID(\"" + emp.getPostingCity() + "\") \n";
            }

            result = aql.getRestData(ApplicationConstants.END_POINT, query);
        } catch (Exception ex) {

        }
        return result;
    }

    public String searchItFinally(LeaveTransaction emp, String sortBy) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.LEAVE_TRANSACTION);
        BasicDBObject regexQuery = new BasicDBObject();
        BasicDBObject sortQuery = new BasicDBObject();

        if (!emp.getEmployeeName().equals("")) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (!emp.getEmployeeCode().equals("")) {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", emp.getEmployeeCode()));
        }
//        if (emp.getEmployeeCode() != null) {
//            regexQuery.put("employeeCodeM",
//                    new BasicDBObject("$regex", emp.getEmployeeCode()));
//        }
        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
//       
        if (!emp.getDesignation().equals("") ) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", emp.getDesignation()));
        }
        if (!emp.getLocation().equals("") ) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));
        }
        if (!emp.getDepartment().equals("") ) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));
        }
        if (!emp.getNatureType().equals("") ) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", emp.getNatureType()));
        }
        if (!emp.getPostingCity().equals("")) {
            regexQuery.put("postingCity",
                    new BasicDBObject("$regex", emp.getPostingCity()));
        }

//        if (!emp.getLeaveType().equals("") ) {
//            regexQuery.put("leaveType",
//                    new BasicDBObject("$regex", emp.getLeaveType()));
//        }
                    DateFormat df = new SimpleDateFormat("dd-M-yyyy");
                     Date demoDate = new Date();
            Date demoDate1 = new Date();
        if (!(emp.getFromDate().isEmpty())) {
            String fromDate = emp.getFromDate();
            String[] d = fromDate.split("/");
            String newDate=d[0]+"-"+d[1]+"-"+d[2];
            System.out.println("------fromDate-----"+newDate);
            long fromDateMilliSec = saveInMilliSecond(newDate);
            regexQuery.put("fromDateInMilliSecond", new BasicDBObject("$gte", fromDateMilliSec));
        }
        if (!(emp.getToDate().isEmpty())) {
            String toDate = emp.getToDate();
            String[] d1 = toDate.split("/");
             String newDate=d1[0]+"-"+d1[1]+"-"+d1[2];
             System.out.println("------toDate-----"+newDate);
            long toDateMilliSec = saveInMilliSecond(newDate);
            regexQuery.put("toDateInMilliSecond",
                    new BasicDBObject("$lte", toDateMilliSec));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
//        regexQuery.put("$sort",
//                new BasicDBObject("employeeName", -1));
//          regexQuery.put("fromDateinMillisecond",
//                new BasicDBObject("$gt", "716927400000"));

        System.out.println(regexQuery);

        DBCursor cursor2 = collection.find(regexQuery);
        // cursor2.sort();
        List<LeaveTransaction> employeeList = new ArrayList<LeaveTransaction>();

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            //System.out.println(ob.get("employeeName"));

//            String empJson = new Gson().toJson(cursor2.next());
//            //System.out.println(empJson);
            Type type = new TypeToken<LeaveTransaction>() {
            }.getType();
            LeaveTransaction em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }

        if (employeeList.size() > 0) {

            employeeList = getDDO(employeeList);
             employeeList = getLocation(employeeList);
            employeeList = getDepartment(employeeList);
            employeeList = getDesignation(employeeList);
            employeeList = getPostingCity(employeeList);
            employeeList = getLeaveType(employeeList);

        }
        if (sortBy.equals("Department")) {
            Collections.sort(employeeList, new LeaveTransactionSortByDepartmentManager());
        } else if (sortBy.equals("EmployeeCodeM")) {
            Collections.sort(employeeList, new LeaveTransactionSortByEmpCodeMManager());
        } else if (sortBy.equals("EmployeeName")) {
            Collections.sort(employeeList, new LeaveTransactionSortByEmpNameManager());
        }

        String finalresult = new Gson().toJson(employeeList);
        return finalresult;
    }

    private List<LeaveTransaction> getDDO(List<LeaveTransaction> employeeList) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDdo())) {
                    employeeList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<LeaveTransaction> getDesignation(List<LeaveTransaction> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DESIGNATION_TABLE);
        List<Designation> religionList = new Gson().fromJson(result, new TypeToken<List<Designation>>() {
        }.getType());
        for (Iterator<Designation> iterator = religionList.iterator(); iterator.hasNext();) {
            Designation next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDesignation());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDesignation())) {
                    employeeList.get(i).setDesignation(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<LeaveTransaction> getDepartment(List<LeaveTransaction> employeeList) throws Exception {
        Map<String, String> DepartmentMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DEPARTMENT_TABLE);
        List<Department> religionList = new Gson().fromJson(result, new TypeToken<List<Department>>() {
        }.getType());
        for (Iterator<Department> iterator = religionList.iterator(); iterator.hasNext();) {
            Department next = iterator.next();
            DepartmentMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDepartment());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DepartmentMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDepartment())) {
                    employeeList.get(i).setDepartment(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<LeaveTransaction> getPostingCity(List<LeaveTransaction> employeeList) throws Exception {
        Map<String, String> CityMasterMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CITY_TABLE);
        List<CityMaster> religionList = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());
        for (Iterator<CityMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            CityMaster next = iterator.next();
            CityMasterMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMasterMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getPostingCity())) {
                    employeeList.get(i).setPostingCity(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public static List<LeaveTransaction> getLeaveType(List<LeaveTransaction> employeeList) throws Exception {
        Map<String, String> LeaveTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LEAVE_TYPE_MASTER);
        List<LeaveTypeMaster> religionList = new Gson().fromJson(result, new TypeToken<List<LeaveTypeMaster>>() {
        }.getType());
        for (Iterator<LeaveTypeMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            LeaveTypeMaster next = iterator.next();
            LeaveTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLeaveType());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : LeaveTypeMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getLeaveType())) {
                    employeeList.get(i).setLeaveType(entry.getValue());
                }
            }
        }
        return employeeList;
    }
     public static List<LeaveTransaction> getLocation(List<LeaveTransaction> employeeList) throws Exception {

        Map<String, String> ReligionMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.LOCATION_TABLE);
        List<Location> religionList = new Gson().fromJson(result, new TypeToken<List<Location>>() {
        }.getType());
        for (Iterator<Location> iterator = religionList.iterator(); iterator.hasNext();) {
            Location next = iterator.next();
            ReligionMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getLocationName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            String locationId = employeeList.get(i).getLocation();
            for (Map.Entry<String, String> entry : ReligionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(locationId)) {
                    employeeList.get(i).setLocationName(value);
                }
            }
        }
        return employeeList;
    }

    public Long saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_TRANSACTION, conditionMap);
        //System.out.println(result1);
        List<LeaveTransaction> leaveTransactionList = new Gson().fromJson(result1, new TypeToken<List<LeaveTransaction>>() {
        }.getType());

        //System.out.println(leaveTransactionList.size());
        Collections.sort(leaveTransactionList, new LeaveTransactionSortByEmpNameManager());
        //System.out.println(new Gson().toJson(leaveTransactionList));
        return result1;
    }

    public static void main(String[] args) throws Exception, Exception, Exception, Exception, Exception {
//        LeaveTransaction l = new LeaveTransaction();
//        l.setEmployeeName("X");
//        l.setEmployeeCode("A");
//        new LeaveTransactionReportManager().searchItFinally(l, "1");
        new LeaveTransactionReportManager().fetchAll();
    }

}
