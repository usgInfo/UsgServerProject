/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.accure.db.Query;
import com.accure.common.duplicate.Duplicate;
import com.accure.hrms.dto.ChapterVIType;
import com.accure.hrms.dto.DeductionType;
import com.accure.hrms.dto.FixedHead;
import com.accure.hrms.dto.Formula;
import com.accure.hrms.dto.ParentHead;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.dto.Section;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.accure.common.dto.Query;

/**
 *
 * @author Asif
 */
public class SalaryHeadManager {

    public String save(SalaryHead salaryhead, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String salaryheadId = null;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.clear();
        duplicateCheckMap.put("description", salaryhead.getDescription());
        if (duplicate(duplicateCheckMap)) {
            salaryheadId = ApplicationConstants.DUPLICATE_MESSAGE;
            return salaryheadId;
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("shortDescription", salaryhead.getShortDescription());
        if (duplicate(duplicateCheckMap)) {
            salaryheadId = ApplicationConstants.DUPLICATE_MESSAGE;
            return salaryheadId;
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("displayLevel", salaryhead.getDisplayLevel());
        if (duplicate(duplicateCheckMap)) {
            salaryheadId = ApplicationConstants.DUPLICATE_MESSAGE;
            return salaryheadId;
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("orderLevel", salaryhead.getOrderLevel());
        if (duplicate(duplicateCheckMap)) {
            salaryheadId = ApplicationConstants.DUPLICATE_MESSAGE;
            return salaryheadId;
        }
//        if(duplicate(salaryhead))
//        {
//             salaryheadId = ApplicationConstants.DUPLICATE_MESSAGE;  
//           return salaryheadId;
//        }
//        else
//        {
        String userName = user.getFname() + " " + user.getLname();
        salaryhead.setCreateDate(System.currentTimeMillis() + "");
        salaryhead.setStatus(ApplicationConstants.ACTIVE);
        salaryhead.setCreatedBy(userName);
        String salaryheadJson = new Gson().toJson(salaryhead);
        salaryheadId = DBManager.getDbConnection().insert(ApplicationConstants.SALARY_HEAD_TABLE, salaryheadJson);
        //}
        return salaryheadId;
    }

    public String fetch(String salaryheadId) throws Exception {
        if (salaryheadId == null || salaryheadId.isEmpty()) {
            return null;
        }

        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, salaryheadId);
        List<SalaryHead> list = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        Object idMap = list.get(0).getId();
        try {
            list = getParentHead(list);

        } catch (Exception e) {
        }
        try {
            list = getDeductionType(list);

        } catch (Exception e) {
        }
        try {
            list = getFormula(list);

        } catch (Exception e) {
        }
        try {
            list = getFixedHead(list);

        } catch (Exception e) {
        }
        try {
            list = getChapterVIType(list);

        } catch (Exception e) {
        }
        try {
            list = getSectionPart(list);
        } catch (Exception e) {
        }

        if (list == null || list.size() < 1) {
            return null;
        }
        list.get(0).setId(idMap);
        return new Gson().toJson(list.get(0));
    }

    public static SalaryHead get(String primaryKey) throws Exception {
        SalaryHead salaryHead = null;
        if (primaryKey == null || primaryKey.isEmpty()) {
            return salaryHead;
        }
        String descriptionDetails = new SalaryHeadManager().fetch(primaryKey);
        if (descriptionDetails != null && !descriptionDetails.isEmpty()) {
            salaryHead = new Gson().fromJson(descriptionDetails, new TypeToken<SalaryHead>() {
            }.getType());
        }
        return salaryHead;
    }

    public static SalaryHead get(HashMap<String, String> condition) throws Exception {
        SalaryHead salaryHead = null;
        if (condition == null) {
            return salaryHead;
        }
        String descriptionDetails = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, condition);
        if (descriptionDetails != null && !descriptionDetails.isEmpty()) {
            List<SalaryHead> salaryHeadList = new Gson().fromJson(descriptionDetails, new TypeToken<List<SalaryHead>>() {
            }.getType());
            salaryHeadList = getFormula(salaryHeadList);
            salaryHead = salaryHeadList.get(0);
        }
        return salaryHead;
    }

    public String fetchRawData(String salaryheadId) throws Exception {
        if (salaryheadId == null || salaryheadId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, salaryheadId);
        List<SalaryHead> list = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return new Gson().toJson(list.get(0));
    }

    public boolean delete(String salaryheadId, String userId) throws Exception {
        boolean result;
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (salaryheadId == null || salaryheadId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<SalaryHead>() {
        }.getType();
        String salaryhead = new SalaryHeadManager().fetch(salaryheadId);
        if (salaryhead == null || salaryhead.isEmpty()) {
            return false;
        }
        SalaryHead sala = new Gson().fromJson(salaryhead, new TypeToken<SalaryHead>() {
        }.getType());

        if (EmployeeManager.checkSalaryHeadInEmployee(sala.getShortDescription())) {
            return false;
        } else {
            SalaryHead salaryheadrJson = new Gson().fromJson(salaryhead, type);
            salaryheadrJson.setStatus(ApplicationConstants.INACTIVE);
            salaryheadrJson.setUpdateDate(System.currentTimeMillis() + "");
            salaryheadrJson.setUpdatedBy(userName);
            result = DBManager.getDbConnection().update(ApplicationConstants.SALARY_HEAD_TABLE, salaryheadId, new Gson().toJson(salaryheadrJson));

        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        //System.out.println("result" + new SalaryHeadManager().delete("5821d17b88b96122832906f3", "57e6bafa44ae76ee18b34293"));
    }

    public String update(SalaryHead salaryhead, String salaryheadId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String status = null;
        HashMap duplicateCheckMap = new HashMap();
        duplicateCheckMap.clear();
        duplicateCheckMap.put("description", salaryhead.getDescription());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SALARY_HEAD_TABLE, duplicateCheckMap, salaryheadId)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
            return status;
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("shortDescription", salaryhead.getShortDescription());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SALARY_HEAD_TABLE, duplicateCheckMap, salaryheadId)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
            return status;
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("displayLevel", salaryhead.getDisplayLevel());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SALARY_HEAD_TABLE, duplicateCheckMap, salaryheadId)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
            return status;
        }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("orderLevel", salaryhead.getOrderLevel());
        if (Duplicate.isDuplicateforUpdate(ApplicationConstants.SALARY_HEAD_TABLE, duplicateCheckMap, salaryheadId)) {
            status = ApplicationConstants.DUPLICATE_MESSAGE;
            return status;
        }

        if (salaryheadId == null || salaryheadId.isEmpty()) {
            status = ApplicationConstants.FAIL;
        }
        Type type = new TypeToken<SalaryHead>() {
        }.getType();
        String previosJson = new SalaryHeadManager().fetch(salaryheadId);
        if (previosJson == null || previosJson.isEmpty()) {
            status = ApplicationConstants.FAIL;
        }
        SalaryHead previousObj = new Gson().fromJson(previosJson, type);
        previousObj.setActive(salaryhead.getActive());
        previousObj.setAmount(salaryhead.getAmount());
        previousObj.setBlockSummation(salaryhead.getBlockSummation());
        previousObj.setCalculateOnIncrement(salaryhead.getCalculateOnIncrement());
        previousObj.setChapterType(salaryhead.getChapterType());
        previousObj.setDeductionType(salaryhead.getDeductionType());
        previousObj.setDescription(salaryhead.getDescription());
        previousObj.setDisplayLevel(salaryhead.getDisplayLevel());
        previousObj.setEffectType(salaryhead.getEffectType());
        previousObj.setFixedHead(salaryhead.getFixedHead());
        previousObj.setForNominee(salaryhead.getForNominee());
        previousObj.setFormula(salaryhead.getFormula());
        previousObj.setHalfOnSuspended(salaryhead.getHalfOnSuspended());
        previousObj.setHeadType(salaryhead.getHeadType());
        previousObj.setInterestCalculated(salaryhead.getInterestCalculated());
        previousObj.setInterestPercentage(salaryhead.getInterestPercentage());
        previousObj.setIsBasic(salaryhead.getIsBasic());
        previousObj.setMapping(salaryhead.getMapping());
        previousObj.setOrderLevel(salaryhead.getOrderLevel());
        previousObj.setParentHead(salaryhead.getParentHead());
        previousObj.setPartOfGross(salaryhead.getPartOfGross());
        previousObj.setPartiallyTaxableLimit(salaryhead.getPartiallyTaxableLimit());
        previousObj.setPresentDependent(salaryhead.getPresentDependent());
        previousObj.setRemarks(salaryhead.getRemarks());
        previousObj.setRounding(salaryhead.getRounding());
        previousObj.setSectionPart(salaryhead.getSectionPart());
        previousObj.setShortDescription(salaryhead.getShortDescription());
        previousObj.setShowOnArrearReport(salaryhead.getShowOnArrearReport());
        previousObj.setShowOnRegister(salaryhead.getShowOnRegister());
        previousObj.setShowOnSalarySlip(salaryhead.getShowOnSalarySlip());
        previousObj.setTaxable(salaryhead.getTaxable());
        previousObj.setUpdateDate(System.currentTimeMillis() + "");
        previousObj.setUpdatedBy(userName);
        if (salaryhead.getHeadType().equalsIgnoreCase(ApplicationConstants.DEDUCTION_HEADS)) {
            if (salaryhead.getDeductionType().equalsIgnoreCase(ApplicationConstants.DEDUCTION_TYPE_LOAN)) {
                previousObj.setIsRefundable(salaryhead.getIsRefundable());
            } else {
                previousObj.setIsRefundable(ApplicationConstants.NO);
            }
        } else {
            previousObj.setIsRefundable(ApplicationConstants.NO);
        }
        previousObj.setIsRefundable(salaryhead.getIsRefundable());// For Loan Module
        String salaryheadrJson = new Gson().toJson(previousObj);
        boolean flag = DBManager.getDbConnection().update(ApplicationConstants.SALARY_HEAD_TABLE, salaryheadId, salaryheadrJson);
        if (flag) {
            status = ApplicationConstants.SUCCESS;
        } else {
            status = ApplicationConstants.FAIL;
        }

        return status;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        try {
            list = getParentHead(list);

        } catch (Exception e) {
        }
        try {
            list = getDeductionType(list);

        } catch (Exception e) {
        }
//        try {
//            list = getFormula(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getFixedHead(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getChapterVIType(list);
//
//        } catch (Exception e) {
//        }
//        try {
//            list = getSectionPart(list);
//        } catch (Exception e) {
//        }
        Collections.sort(list, new Comparator<SalaryHead>() {
            public int compare(SalaryHead s1, SalaryHead s2) {
                return s1.getOrderLevel() - s2.getOrderLevel();
            }
        });

        return new Gson().toJson(list);
    }

    public String fetchAllSalaryHeadsBasedonDeductiontype() throws Exception {

        List<Query> qlist = new ArrayList<Query>();
        Query query = new Query();
        Query query1 = new Query();
        Query query2 = new Query();
        String deductionType[] = {"Loan", "Advance"};

//        query1 = new Query();
//        query1.setColumnName(ApplicationConstants.STATUS);
//        query1.setColumnValue(ApplicationConstants.ACTIVE);
//        query1.setColumnName("active");
//        query1.setColumnValue("Yes");
//        query1.setCondition(ApplicationConstants.EQUAL);
//        qlist.add(query1);
//        query = new Query();
//        query.setColumnName("deductionType");
//        query.setMatchValues(deductionType);
//
//        query.setCondition(ApplicationConstants.LOGICAL_IN);
//        qlist.add(query);
        query1 = new Query();
        query1.setColumnName(ApplicationConstants.STATUS);
        query1.setColumnValue(ApplicationConstants.ACTIVE);
        query1.setCondition(ApplicationConstants.EQUAL);
        qlist.add(query1);

        query2 = new Query();
        query2.setColumnName("active");
        query2.setColumnValue("Yes");
        query2.setCondition(ApplicationConstants.EQUAL);
        qlist.add(query2);

        query = new Query();
        query.setColumnName("deductionType");
        query.setMatchValues(deductionType);
        query.setCondition(ApplicationConstants.LOGICAL_IN);
        qlist.add(query);

        String result = DBManager.getDbConnection().fetchRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, qlist);
        List<SalaryHead> list = new ArrayList<SalaryHead>();
        if (null != result && !result.isEmpty()) {
            list = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
            }.getType());
        }
        ////
        return (null == list || list.isEmpty()) ? null : new Gson().toJson(list);
    }

    public String fetchAllArrearHead() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        // conditionMap.put("headType", "Earnings");
        conditionMap.put("active", "Yes");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (SalaryHead cl : list) {
            if (cl.getFormula() != null) {
                try {
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, cl.getFormula());
                    List<Formula> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Formula>>() {
                    }.getType());
                    Formula gal = gaList.get(0);
                    cl.setFormula(gal.getDescription());
                } catch (Exception e) {
                }

            }
        }
        return new Gson().toJson(list);
    }

    public String fetchAllByHeadType(String headType, String value) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(headType, value);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (SalaryHead cl : list) {
            if (cl.getFormula() != null) {
                try {
                    String gaJson = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, cl.getFormula());
                    List<Formula> gaList = new Gson().fromJson(gaJson, new TypeToken<List<Formula>>() {
                    }.getType());
                    Formula gal = gaList.get(0);
                    cl.setFormula(gal.getDescription());
                } catch (Exception e) {
                }

            }
        }
        return new Gson().toJson(list);
    }
//Replacing _Id of Parent Head to Present Value

    public static List<SalaryHead> getParentHead(List<SalaryHead> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.PARENT_HEAD_MASTER);
        List<ParentHead> resultListist = new Gson().fromJson(result, new TypeToken<List<ParentHead>>() {
        }.getType());
        for (Iterator<ParentHead> iterator = resultListist.iterator(); iterator.hasNext();) {
            ParentHead next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getParentHead());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getParentHead())) {
                    list.get(i).setParentHead(entry.getValue());
                }
            }
        }
        return list;
    }
//Replacing _Id of Parent Head to Deduction Type

    public static List<SalaryHead> getDeductionType(List<SalaryHead> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DEDUCTION_TYPE_MASTER);
        List<DeductionType> resultListist = new Gson().fromJson(result, new TypeToken<List<DeductionType>>() {
        }.getType());
        for (Iterator<DeductionType> iterator = resultListist.iterator(); iterator.hasNext();) {
            DeductionType next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDeductionType());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getDeductionType())) {
                    list.get(i).setDeductionType(entry.getValue());
                }
            }
        }
        return list;
    }
//Replacing _Id of ChapterVI Type to Present Value

    public static List<SalaryHead> getChapterVIType(List<SalaryHead> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CHAPTERVI_TYPE_MASTER);
        List<ChapterVIType> resultListist = new Gson().fromJson(result, new TypeToken<List<ChapterVIType>>() {
        }.getType());
        for (Iterator<ChapterVIType> iterator = resultListist.iterator(); iterator.hasNext();) {
            ChapterVIType next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getChapterVIType());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getChapterType())) {
                    list.get(i).setChapterType(entry.getValue());
                }
            }
        }
        return list;
    }
//Replacing _Id of Fixed Head to Present Value

    public static List<SalaryHead> getFixedHead(List<SalaryHead> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FIXED_HEAD_MASTER);
        List<FixedHead> resultListist = new Gson().fromJson(result, new TypeToken<List<FixedHead>>() {
        }.getType());
        for (Iterator<FixedHead> iterator = resultListist.iterator(); iterator.hasNext();) {
            FixedHead next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getFixedHead());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFixedHead())) {
                    list.get(i).setFixedHead(entry.getValue());
                }
            }
        }
        return list;
    }
//Replacing _Id of Section Part to Present Value

    public static List<SalaryHead> getSectionPart(List<SalaryHead> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SECTION_TABLE);
        List<Section> resultListist = new Gson().fromJson(result, new TypeToken<List<Section>>() {
        }.getType());
        for (Iterator<Section> iterator = resultListist.iterator(); iterator.hasNext();) {
            Section next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getSectionPart())) {
                    list.get(i).setSectionId(list.get(i).getSectionPart());
                    list.get(i).setSectionPart(entry.getValue());

                }
            }
        }
        return list;
    }
//Replacing _Id of Formula to Present Value

    public static List<SalaryHead> getFormula(List<SalaryHead> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FORMULA_TABLE);
        List<Formula> resultListist = new Gson().fromJson(result, new TypeToken<List<Formula>>() {
        }.getType());
        for (Iterator<Formula> iterator = resultListist.iterator(); iterator.hasNext();) {
            Formula next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFormula())) {
                    list.get(i).setFormula(entry.getValue());
                }
            }
        }
        return list;
    }

    public String fetchHeadsHeadslab() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("headType", "Earnings");
        conditionMap.put("mapping", "Yes");
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        List<SalaryHead> list = new Gson().fromJson(result1, new TypeToken<List<SalaryHead>>() {
        }.getType());
        return new Gson().toJson(list);
    }

    public static String fetchAllEarningHeads() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("headType", "Earnings");
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, conditionMap);
        return result1;
    }

    public boolean checkConditionInEmp(String salaryheadId) throws Exception {

        if (salaryheadId == null || salaryheadId.isEmpty()) {
            return false;
        }
        Map<String, Map<String, String>> conditionMap = new HashMap<String, Map<String, String>>();
        Map<String, String> innerMap = new HashMap<String, String>();
        innerMap.put("description", salaryheadId);
        conditionMap.put("earningHeads", innerMap);
        conditionMap.put("deductionHeads", innerMap);
        boolean result = false;
        String data = null;
        data = new DBManager().getDbConnection().fetchRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, conditionMap);

        if (data != null) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean duplicate(HashMap duplicateCheckMap) throws Exception {
        boolean res = false;
        try {
//            HashMap duplicateCheckMap = new HashMap();
//            duplicateCheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
//            duplicateCheckMap.put("description",salaryhead.getShortDescription());
//            duplicateCheckMap.put("shortDescription",salaryhead.getDescription());
//            duplicateCheckMap.put("displayLevel",salaryhead.getDisplayLevel());
//             duplicateCheckMap.put("orderLevel",salaryhead.getOrderLevel());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.SALARY_HEAD_TABLE, duplicateCheckMap);
            if (result != null) {
//                List<GISGroup> list = new Gson().fromJson(result, new TypeToken<List<GISGroup>>() {
//                }.getType());
//
//                for (GISGroup li : list) {
//                    if (.equalsIgnoreCase(li.getGroupName())) {
                res = true;
//                    }
//                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
}
