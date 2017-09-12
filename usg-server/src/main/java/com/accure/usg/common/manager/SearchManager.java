/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.common.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.hrms.dto.Employee;
import com.accure.user.dto.User;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author deepak2310
 */
public class SearchManager {

    public HashMap<String, String> fetchallDDO() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ddoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, conditionMap);
        List<DDO> ddoList = new Gson().fromJson(ddoJson, new TypeToken<List<DDO>>() {
        }.getType());
        conditionMap.clear();
        for (DDO ddo : ddoList) {
            conditionMap.put(((Map<String, String>) ddo.getId()).get("$oid"), ddo.getDdoName());
        }
        return conditionMap;
    }

    public List<Employee> get(String searchKey, String input_sindex, String input_limit) throws Exception {
        // check input parameter
        HashMap<String, String> ddo = fetchallDDO();
        if (searchKey == null || searchKey.isEmpty() || input_sindex == null || input_sindex.isEmpty()
                || input_limit == null || input_limit.isEmpty()) {
            return null;
        }
        List<Employee> result = null;
        Map<String, String> smap = new HashMap<String, String>();
        int sindex = 0;
        int limit = 0;
        if (input_sindex != null && input_limit != null) {
            sindex = Integer.parseInt(input_sindex);
            limit = Integer.parseInt(input_limit);
        }

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
            smap.put(ApplicationConstants.NAME, searchKey);
            smap.put(ApplicationConstants.SORTKEY, ApplicationConstants.NAME);
            smap.put(ApplicationConstants.SORTORDER, ApplicationConstants.SORTORDER_TYPE);

            String dbOutput = null;
            if (limit == 0 && sindex == 0) {
                limit = 10;
                // fetch employee from data base
                dbOutput = DBManager.getDbConnection().fetchRowsLike(ApplicationConstants.EMPLOYEE_TABLE, smap, ApplicationConstants.LOGICAL_AND, sindex, limit);
            } else {
                // fetch employee from data base
                dbOutput = DBManager.getDbConnection().fetchRowsLike(ApplicationConstants.EMPLOYEE_TABLE, smap, ApplicationConstants.LOGICAL_AND, sindex, limit);
            }
            if (dbOutput != null) {
                Type type = new TypeToken<List<Employee>>() {
                }.getType();
                result = new Gson().fromJson(dbOutput, type);
                List<Employee> employeeList = new ArrayList<Employee>();
                if (result != null) {
                    for (Employee employee : result) {
                        if (employee.getDdo() != null) {
                            String id = employee.getDdo();
                            if (ddo.containsKey(id) && ddo.get(id) != null) {
                                employee.setDdoName(ddo.get(id));
                            }
                        }
                        employeeList.add(employee);
                    }
                }
            }
        }
        return result;
    }

    public HashMap<String, String> fetchallLocation() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String ddoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LOCATION, conditionMap);
        List<Location> locationList = new Gson().fromJson(ddoJson, new TypeToken<List<Location>>() {
        }.getType());
        conditionMap.clear();
        for (Location locationsList : locationList) {
            conditionMap.put(((Map<String, String>) locationsList.getId()).get("$oid"), locationsList.getLocationName());
        }
        return conditionMap;
    }

    public List<Employee> get(String searchKey, String input_sindex, String input_limit, String ddoId, String locationId) throws Exception {
        // check input parameter
        List<Employee> employeeList = new ArrayList<Employee>();
        HashMap<String, String> ddo = fetchallDDO();
        if (searchKey == null || searchKey.isEmpty() || input_sindex == null || input_sindex.isEmpty()
                || input_limit == null || input_limit.isEmpty()) {
            return null;
        }
        List<Employee> result = null;
        Map<String, String> smap = new HashMap<String, String>();
        int sindex = 0;
        int limit = 0;
        if (input_sindex != null && input_limit != null) {
            sindex = Integer.parseInt(input_sindex);
            limit = Integer.parseInt(input_limit);
        }

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
            smap.put(ApplicationConstants.NAME, searchKey);
            if (ddoId != null && !ddoId.isEmpty() && !ddoId.equals("")) {
                smap.put(ApplicationConstants.DDO, ddoId);
            }
            if (locationId != null && !locationId.isEmpty() && !locationId.equals("")) {
                smap.put(ApplicationConstants.LOCATION, locationId);
            }
            smap.put(ApplicationConstants.SORTKEY, ApplicationConstants.NAME);
            smap.put(ApplicationConstants.SORTORDER, ApplicationConstants.SORTORDER_TYPE);

            String dbOutput = null;
            if (limit == 0 && sindex == 0) {
                limit = 10;
                // fetch employee from data base
                dbOutput = DBManager.getDbConnection().fetchRowsLike(ApplicationConstants.EMPLOYEE_TABLE, smap, ApplicationConstants.LOGICAL_AND, sindex, limit);
            } else {
                // fetch employee from data base
                dbOutput = DBManager.getDbConnection().fetchRowsLike(ApplicationConstants.EMPLOYEE_TABLE, smap, ApplicationConstants.LOGICAL_AND, sindex, limit);
            }
            if (dbOutput != null) {
                Type type = new TypeToken<List<Employee>>() {
                }.getType();
                result = new Gson().fromJson(dbOutput, type);
            }

            smap.clear();
            smap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String presentUserListJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.USER, smap);
            List<User> presentUserList = new Gson().fromJson(presentUserListJson, new TypeToken<List<User>>() {
            }.getType());
            Set<String> presentUserEmployeeId = new HashSet<String>();
            if (presentUserList != null) {
                for (Iterator<User> iterator = presentUserList.iterator(); iterator.hasNext();) {
                    User next = iterator.next();
                    if (next.getEmployeeId() != null && !next.getEmployeeId().isEmpty() && !next.getEmployeeId().equals("")) {
                        presentUserEmployeeId.add(next.getEmployeeId());
                    }
                }
            }
            //System.out.println("****************************************presentUserEmployeeId*********************************************************\n\n");
            //System.out.println(new Gson().toJson(presentUserEmployeeId));
            //System.out.println("*************************************************************************************************\n\n");
            if (presentUserEmployeeId != null && presentUserEmployeeId.size() > 0) {
                for (Iterator<Employee> iterator = result.iterator(); iterator.hasNext();) {
                    Employee next = iterator.next();
                    String employeeId = ((LinkedTreeMap<String, String>) next.getId()).get("$oid");
                    if (presentUserEmployeeId.contains(employeeId)) {
                        iterator.remove();
                    }
                }
            }
             
             if (result != null) {
                    for (Employee employee : result) {
                        if (employee.getDdo() != null) {
                            String id = employee.getDdo();
                            if (ddo.containsKey(id) && ddo.get(id) != null) {
                                employee.setDdoName(ddo.get(id));
                            }
                        }
                        employeeList.add(employee);
                    }
                }
        }
        return employeeList;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(new SearchManager().get("Payroll", "0", "1000", "", ""));
    }
}
