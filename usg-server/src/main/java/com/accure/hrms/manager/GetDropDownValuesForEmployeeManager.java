/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.finance.manager.LocationManager;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.DesignationFundTypeMapping;
import com.accure.hrms.dto.Discipline;
import com.accure.budget.dto.FundType;
import com.accure.hrms.dto.Grade;
import com.accure.hrms.dto.Nature;
import com.accure.user.dto.DdoLocationMap;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Asif
 */
public class GetDropDownValuesForEmployeeManager {

    public static String getAllValues() {
        return null;
    }

    public String primaryMethod(String ddo, String designation, String fundType, String budgetHead, String natureType, String condition) throws Exception {
        String resultString = null;
        if (condition.equalsIgnoreCase(ApplicationConstants.GET_DESIGNATION_BASED_ON_DDO)) {
            resultString = getDesignationBasedOnDDO(ddo);
        } else if (condition.equalsIgnoreCase(ApplicationConstants.GET_FUND_TYPE_GRADE_N_CLASS_BASED_ON_DDO_DESI)) {
            resultString = getFundTypeNGradeBasedOnDDODESIGNATION(ddo, designation);
        } else if (condition.equalsIgnoreCase(ApplicationConstants.GET_BH_BASED_ON_DDO_DESI_FT)) {
            resultString = getBudgetHeadBasedOnDDODESIGNATION_FT(ddo, designation, fundType);
        } else if (condition.equalsIgnoreCase(ApplicationConstants.GET_NATURE_BASED_ON_DDO_DESI_FT_BH)) {
            resultString = getNatureBasedOnDDODESIGNATION_FT_BH(ddo, designation, fundType, budgetHead);
        } else if (condition.equalsIgnoreCase(ApplicationConstants.GET_DEPARTMENT_BASED_ON_DDO)) {
            resultString = getDesciplineBasedOnDDODESIGNATION_FT_BH_Nature(ddo, designation, fundType, budgetHead, natureType);
        } else if (condition.equalsIgnoreCase(ApplicationConstants.GET_DESCIPLINE_BASED_ON_DDO_DESI_FT_BH_NATURE)) {
            resultString = getDesciplineBasedOnDDODESIGNATION_FT_BH_Nature(ddo, designation, fundType, budgetHead, natureType);
        } else if (condition.equalsIgnoreCase(ApplicationConstants.GET_LOCATION_BASED_ON_DDO)) {
            resultString = getLocationBasedOnDDO(ddo);
        }
        return resultString;
    }

    public static String getDesignationBasedOnDDO(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        List<DesignationFundTypeMapping> desigFundHeadTypelist = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        Set<String> designationIdSet = new HashSet<String>();
        for (Iterator<DesignationFundTypeMapping> iterator = desigFundHeadTypelist.iterator(); iterator.hasNext();) {
            DesignationFundTypeMapping next = iterator.next();
            designationIdSet.add(next.getDesignation());
        }
        List<Designation> li = new ArrayList<Designation>();
        for (Iterator<String> iterator = designationIdSet.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            String res = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION, next);
            List<Designation> desList = new Gson().fromJson(res, new TypeToken<List<Designation>>() {
            }.getType());
            if (desList != null) {
                li.add(desList.get(0));
            }
        }
        return new Gson().toJson(li);
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(new GetDropDownValuesForEmployeeManager().getFundTypeNGradeBasedOnDDODESIGNATION("57ea0649e4b0f97866d150b0", "57eba25ae4b09c5cec7cbca6"));
    }

    public static String getLocationBasedOnDDO(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_LOCATION_TABLE, conditionMap);
        if (result != null) {
            List<DdoLocationMap> ddoList = new Gson().fromJson(result, new TypeToken<List<DdoLocationMap>>() {
            }.getType());
            HashSet<String> locationIdSet = new HashSet<String>();
            for (Iterator<DdoLocationMap> iterator = ddoList.iterator(); iterator.hasNext();) {
                DdoLocationMap next = iterator.next();
                List<String> li = next.getLocation();
                for (Iterator<String> iterator1 = li.iterator(); iterator1.hasNext();) {
                    String next1 = iterator1.next();
                    locationIdSet.add(next1);
                }
            }
            List<Location> locationList = new ArrayList<Location>();
            for (Iterator<String> iterator = locationIdSet.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                locationList.add(new LocationManager().fetch(next));
            }
            return new Gson().toJson(locationList);
        } else {
            return null;
        }
    }

    public static String getFundTypeNGradeBasedOnDDODESIGNATION(String ddo, String designation) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("designation", designation);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        List<DesignationFundTypeMapping> desigFundHeadTypelist = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        Set<String> fundTypeId = new HashSet<String>();
        for (Iterator<DesignationFundTypeMapping> iterator = desigFundHeadTypelist.iterator(); iterator.hasNext();) {
            DesignationFundTypeMapping next = iterator.next();
            fundTypeId.add(next.getFundType());
        }
        List<FundType> li = new ArrayList<FundType>();
        for (Iterator<String> iterator = fundTypeId.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            String res = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, next);
            List<FundType> desList = new Gson().fromJson(res, new TypeToken<List<FundType>>() {
            }.getType());
            if (desList != null) {
                li.add(desList.get(0));
            }
        }
        String gradeClassResult = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION, designation);
        //System.out.println(gradeClassResult);
        List<Designation> desig = new Gson().fromJson(gradeClassResult, new TypeToken<List<Designation>>() {
        }.getType());
        String gradeObj = null;
        String classObj = null;
        if (desig != null && desig.size() > 0 && desig.get(0) != null) {
            // gradeObj = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE, desig.get(0).getGrade());
            classObj = DBManager.getDbConnection().fetch(ApplicationConstants.CLASS_TABLE, desig.get(0).getClas());
        }
        //Get Grade

        List<Grade> desigdropgradelist = new ArrayList<Grade>();
        String graderesult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        List<DesignationFundTypeMapping> desiggradelist = new Gson().fromJson(graderesult, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        for (DesignationFundTypeMapping desigfunlist : desiggradelist) {
            if ((desigfunlist.getGrade() != null)) {
                try {
                    String gaJson1 = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, desigfunlist.getGrade());
                    if (gaJson1 != null) {
                        List<Grade> gaList1 = new Gson().fromJson(gaJson1, new TypeToken<List<Grade>>() {
                        }.getType());
                        Grade gal1 = gaList1.get(0);
                        desigdropgradelist.add(gal1);
                    }
                } catch (Exception e) {
                }
            }
        }

        HashMap<String, String> resultMap = new HashMap<String, String>();
        List<com.accure.hrms.dto.Class> clasList = new Gson().fromJson(classObj, new TypeToken<List<com.accure.hrms.dto.Class>>() {
        }.getType());

        resultMap.put("fundType", new Gson().toJson(li));
        resultMap.put("gradeDeatils", new Gson().toJson(desigdropgradelist));
        if (classObj != null) {
            //System.out.println(new Gson().toJson(clasList));
            //System.out.println("*******************************************************");
            resultMap.put("classDeatils", new Gson().toJson(clasList.get(0)));
        }

        return new Gson().toJson(resultMap);
    }

//    private static List<Grade> getDropdownGrade(List<DesignationFundTypeMapping> designationList) throws Exception {
//        Map<String, String> GradeMap = new HashMap<String, String>();
//        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.GRADE_TABLE);
//        List<Grade> religionList = new Gson().fromJson(result, new TypeToken<List<Grade>>() {
//        }.getType());
//        for (Iterator<Grade> iterator = religionList.iterator(); iterator.hasNext();) {
//            Grade next = iterator.next();
//            GradeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getGradeName());
//        }
//        for (int i = 0; i < designationList.size(); i++) {
//            for (Map.Entry<String, String> entry : GradeMap.entrySet()) {
//                if (entry.getKey().equals(designationList.get(i).getGrade())) {
//                    designationList.get(i).setGrade(entry.getValue());
//                }
//            }
//        }
//        return designationList;
//    }
    public static String getBudgetHeadBasedOnDDODESIGNATION_FT(String ddo, String designation, String fundType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("designation", designation);
        conditionMap.put("fundType", fundType);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        List<DesignationFundTypeMapping> desigFundHeadTypelist = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        Set<String> fundTypeId = new HashSet<String>();
        for (Iterator<DesignationFundTypeMapping> iterator = desigFundHeadTypelist.iterator(); iterator.hasNext();) {
            DesignationFundTypeMapping next = iterator.next();
            fundTypeId.add(next.getBudgetHead());
        }
        List<BudgetHeadMaster> li = new ArrayList<BudgetHeadMaster>();
        for (Iterator<String> iterator = fundTypeId.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            String res = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, next);
            List<BudgetHeadMaster> desList = new Gson().fromJson(res, new TypeToken<List<BudgetHeadMaster>>() {
            }.getType());
            if (desList != null) {
                li.add(desList.get(0));
            }
        }
        return new Gson().toJson(li);
    }

    public static String getNatureBasedOnDDODESIGNATION_FT_BH(String ddo, String designation, String fundType, String budgetHead) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("designation", designation);
        conditionMap.put("fundType", fundType);
        conditionMap.put("budgetHead", budgetHead);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        List<DesignationFundTypeMapping> desigFundHeadTypelist = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        Set<String> fundTypeId = new HashSet<String>();
        for (Iterator<DesignationFundTypeMapping> iterator = desigFundHeadTypelist.iterator(); iterator.hasNext();) {
            DesignationFundTypeMapping next = iterator.next();
            fundTypeId.add(next.getNatureType());
        }
        List<Nature> li = new ArrayList<Nature>();
        for (Iterator<String> iterator = fundTypeId.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            String res = DBManager.getDbConnection().fetch(ApplicationConstants.NATURE_TABLE, next);
            List<Nature> desList = new Gson().fromJson(res, new TypeToken<List<Nature>>() {
            }.getType());
            if (desList != null) {
                li.add(desList.get(0));
            }
        }
        return new Gson().toJson(li);
    }

    public static String getDesciplineBasedOnDDODESIGNATION_FT_BH_Nature(String ddo, String designation, String fundType, String budgetHead, String natureType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("designation", designation);
        conditionMap.put("fundType", fundType);
        conditionMap.put("budgetHead", budgetHead);
        conditionMap.put("natureType", natureType);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, conditionMap);
        List<DesignationFundTypeMapping> desigFundHeadTypelist = new Gson().fromJson(result, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        Set<String> fundTypeId = new HashSet<String>();
        for (Iterator<DesignationFundTypeMapping> iterator = desigFundHeadTypelist.iterator(); iterator.hasNext();) {
            DesignationFundTypeMapping next = iterator.next();
            fundTypeId.add(next.getDesciplineName());
        }
        List<Discipline> li = new ArrayList<Discipline>();
        for (Iterator<String> iterator = fundTypeId.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            String res = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, next);
            List<Discipline> desList = new Gson().fromJson(res, new TypeToken<List<Discipline>>() {
            }.getType());
            if (desList != null) {
                li.add(desList.get(0));
            }
        }
        return new Gson().toJson(li);
    }
//////////////////////////////////////////////////Coverting ID With Present Name/////////////////////////////////////////////////////////////////////////
//    public static String getDepartmentBasedOnDDO(String ddo) throws Exception {
//        HashMap<String, String> conditionMap = new HashMap<String, String>();
//        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//        conditionMap.put("ddo", ddo);
//        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_DEPARTMENT_TABLE, conditionMap);
//        List<DdoDepartmentMap> ddoList = new Gson().fromJson(result, new TypeToken<List<DdoDepartmentMap>>() {
//        }.getType());
//        HashSet<String> deaprtmentIdSet = new HashSet<String>();
//        for (Iterator<DdoDepartmentMap> iterator = ddoList.iterator(); iterator.hasNext();) {
//            DdoDepartmentMap next = iterator.next();
//            List<String> li = next.getDeptList();
//            for (Iterator<String> iterator1 = li.iterator(); iterator1.hasNext();) {
//                String next1 = iterator1.next();
//                deaprtmentIdSet.add(next1);
//            }
//        }
//        List<Department> departmentList = new ArrayList<Department>();
//        for (Iterator<String> iterator = deaprtmentIdSet.iterator(); iterator.hasNext();) {
//            String next = iterator.next();
//            departmentList.add(new DepartmentManager().fetch(next));
//        }
//        return new Gson().toJson(departmentList);
//    }

}
