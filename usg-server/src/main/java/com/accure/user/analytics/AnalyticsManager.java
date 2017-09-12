/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.analytics;

import com.accure.finance.dto.DDO;
import com.accure.finance.dto.FinancialAccounting_FinancialYear;
import com.accure.hrms.dto.Category;
import com.accure.hrms.dto.CategoryPosts;
import com.accure.hrms.dto.DesignationFundTypeMapping;
import com.accure.hrms.dto.Employee;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author accure
 */
public class AnalyticsManager {

    public String getEmployeeCountByFY(String fy, String ddo) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }
        HashMap<Integer, Long> result = new HashMap<Integer, Long>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        HashMap<Integer, HashMap<Long, Long>> fyMap = new HashMap<Integer, HashMap<Long, Long>>();
        // get all available FY from fy collection
        String strFy = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FINANCIAL_YEAR_TABLE, conditionMap);
        if (null != strFy && !strFy.isEmpty()) {
            List<FinancialAccounting_FinancialYear> financialYear = new Gson().fromJson(strFy, new TypeToken<List<FinancialAccounting_FinancialYear>>() {
            }.getType());
            for (FinancialAccounting_FinancialYear fYear : financialYear) {
                HashMap<Long, Long> map = new HashMap<Long, Long>();
                map.put(convertDataToMillsec(fYear.getFromDate()), convertDataToMillsec(fYear.getToDate()));
                fyMap.put(Integer.parseInt(fYear.getYear()), map);
                result.put(Integer.parseInt(fYear.getYear()), 0l);
            }

            HashMap<String, String> cond = new HashMap<String, String>();
//            cond.put("ddoName", "Gujarat");
//            String strDdoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, cond);
//            if (null != strDdoJson && !strDdoJson.isEmpty()) {
//                List<DDO> ddoList = new Gson().fromJson(strDdoJson, new TypeToken<List<DDO>>() {
//                }.getType());
            //  cond = new HashMap<String, String>();
            cond.put("ddo", ddo);
            // get all employee collection
            String strEmp = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, cond);
            if (null != strEmp && !strEmp.isEmpty()) {
                List<Employee> employee = new Gson().fromJson(strEmp, new TypeToken<List<Employee>>() {
                }.getType());
                for (Employee emp : employee) {
                    if (null != emp.getDateOfJoining() && !emp.getDateOfJoining().isEmpty()) {
                        // //System.out.println(emp.getDateOfJoining());
                        Long dateOfJoining = convertDataToMillsec(emp.getDateOfJoining());
                        int year = Integer.parseInt(emp.getDateOfJoining().substring(emp.getDateOfJoining().length() - 4, emp.getDateOfJoining().length()));
                        HashMap<Long, Long> value = fyMap.get(year);
                        try {
                            for (Map.Entry<Long, Long> m : value.entrySet()) {
                                Long fyrFromdate = m.getKey();
                                Long fyrTodate = m.getValue();
                                if (fyrFromdate <= dateOfJoining || dateOfJoining <= fyrTodate) {
                                    long count = result.get(year);
                                    result.put(year, ++count);
                                } else if (dateOfJoining < fyrFromdate) {
                                    long count = result.get(year);
                                    result.put((year - 1), ++count);
                                } else if (dateOfJoining > fyrTodate) {
                                    long count = result.get(year);
                                    result.put((year + 1), ++count);
                                }
                            }
                        } catch (Exception ee) {
                        }
                    }
                }
            }
            //  }
        }
        //System.out.println(new Gson().toJson(result));
        return new Gson().toJson(result);
    }

    public String getEmployeeCountByDDO(String fy, String getddo) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }
        HashMap<String, Long> result = new HashMap<String, Long>();
        HashMap<String, String> ddoMap = new HashMap<String, String>();
        // get all available ddo from ddo collection
        HashMap<String, String> statusMap = new HashMap<String, String>();
        statusMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String strDdo = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, statusMap);
        if (null != strDdo && !strDdo.isEmpty()) {
            List<DDO> ddoList = new Gson().fromJson(strDdo, new TypeToken<List<DDO>>() {
            }.getType());
            for (DDO ddo : ddoList) {
                ddoMap.put(((Map<String, String>) ddo.getId()).get("$oid"), ddo.getDdoName());
                result.put(ddo.getDdoName(), 0l);
            }
            // get all employee collection
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String strEmp = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);
            if (null != strEmp && !strEmp.isEmpty()) {
                List<Employee> employee = new Gson().fromJson(strEmp, new TypeToken<List<Employee>>() {
                }.getType());
                for (Employee emp : employee) {
                    if (null != emp.getDdo() && !emp.getDdo().isEmpty()) {
                        long count = result.get(ddoMap.get(emp.getDdo()));
                        result.put(ddoMap.get(emp.getDdo()), ++count);
                    }
                }
            }
        }
        //System.out.println(new Gson().toJson(result));
        return new Gson().toJson(result);
    }

    public String getVacancyStatus(String fy, String ddo) throws Exception {
        if (null == fy || fy.isEmpty()) {
            return null;
        }
        String categoryName;
        HashMap<String, HashMap<String, Long>> result = new HashMap<String, HashMap<String, Long>>();

        HashMap<String, String> designationMap = new HashMap<String, String>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        // get all available ddo from ddo collection
        String strDesignation = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        if (null != strDesignation && !strDesignation.isEmpty()) {
            List<DesignationFundTypeMapping> designationList = new Gson().fromJson(strDesignation, new TypeToken<List<DesignationFundTypeMapping>>() {
            }.getType());
            for (DesignationFundTypeMapping designation : designationList) {
                if (null != designation.getCategoryposts() && !designation.getCategoryposts().isEmpty()) {
                    for (CategoryPosts desig : designation.getCategoryposts()) {
                        //  for(int i=0;i<designation.getCategoryposts().size();i++)  

                        try {
                            categoryName = fetch(desig.getCategoory());
                            desig.setCategooryName(categoryName);
                            designationMap.put(desig.getCategoory(), desig.getCategooryName());
                            if (result.containsKey(desig.getCategooryName())) {
                                HashMap<String, Long> value = result.get(desig.getCategooryName());
                                long total = (null == desig.getPosts() || desig.getPosts().isEmpty()) ? 0l : Long.parseLong(desig.getPosts());
                                if (value.containsKey("total")) {
                                    total = total + value.get("total");
                                    value.put("total", total);
                                } else {
                                    value.put("total", total);
                                }
                                result.put(desig.getCategooryName(), value);
                            } else {
                                HashMap<String, Long> value = new HashMap<String, Long>();
                                long total = (null == desig.getPosts() || desig.getPosts().isEmpty()) ? 0l : Long.parseLong(desig.getPosts());
                                value.put("total", total);
                                result.put(desig.getCategooryName(), value);
                            }
                            //  employeeList = getSalutation(employeeList);
                        } catch (Exception e) {
                        }

                        //  categoryName = fetch(desig.getCategoory());
                    }
                }
            }
            HashMap<String, String> cond = new HashMap<String, String>();
//            cond.put("ddoName", "Gujarat");
//            String strDdoJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_TABLE, cond);
//          //  if (null != strDdoJson && !strDdoJson.isEmpty()) {
//                List<DDO> ddoList = new Gson().fromJson(strDdoJson, new TypeToken<List<DDO>>() {
//                }.getType());
//                cond = new HashMap<String, String>();
            cond.put("ddo", ddo);
            // get all employee collection
            String strEmp = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, cond);
            if (null != strEmp && !strEmp.isEmpty()) {
                List<Employee> employee = new Gson().fromJson(strEmp, new TypeToken<List<Employee>>() {
                }.getType());
                for (Employee emp : employee) {
                    if (null != emp.getCategory() && !emp.getCategory().isEmpty()) {
                        HashMap<String, Long> value = result.get(designationMap.get(emp.getCategory()));
                        if (value.containsKey("filled")) {
                            Long filled = value.get("filled") + 1;
                            value.put("filled", filled);
                        } else {
                            value.put("filled", 1l);
                        }
                        result.put(designationMap.get(emp.getCategory()), value);
                    }
                }
            }
            //    }
        }
        //System.out.println(new Gson().toJson(result));
        return new Gson().toJson(result);
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, Id);
        List<Category> objList = new ArrayList<Category>();
        if (result != null) {
            objList = new Gson().fromJson(result, new TypeToken<List<Category>>() {
            }.getType());
        }

        if (objList == null || objList.size() < 1) {
            return null;
        }

        return objList.get(0).getCategoryy();
    }

    public static Long convertDataToMillsec(String stdate) throws ParseException {
        if (null == stdate || stdate.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(stdate);
        return date.getTime();
    }

    public static void main(String args[]) throws Exception {
//        new AnalyticsManager().getEmployeeCountByFY("2015");
//        new AnalyticsManager().getEmployeeCountByDDO("2015");
//        new AnalyticsManager().getVacancyStatus("2015");
    }
}
