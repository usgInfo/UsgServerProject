/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.Nature;
import com.accure.hrms.dto.Formula;
import com.accure.hrms.dto.CityMaster;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.BasedOn;
import com.accure.hrms.dto.CityCategoryMaster;
import com.accure.hrms.dto.HeadSlab;
import com.accure.hrms.dto.SalaryHead;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
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
 * @author Asif
 */
public class HeadSlabManager {

    public String save(HeadSlab headslab, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        headslab.setCreateDate(System.currentTimeMillis() + "");
        headslab.setCreatedBy(userName);
        headslab.setStatus(ApplicationConstants.ACTIVE);
        if (headslab.getAmount() != null && !headslab.getAmount().isEmpty()) {
            headslab.setAmountLong(Long.parseLong(headslab.getAmount()));
        }
        if (headslab.getFromBasic() != null && !headslab.getFromBasic().isEmpty()) {
            headslab.setFromBasicLong(Long.parseLong(headslab.getFromBasic()));
        }
        if (headslab.getToBasic() != null && !headslab.getToBasic().isEmpty()) {
            headslab.setToBasicLong(Long.parseLong(headslab.getToBasic()));
        }

        if (headslab.getFromGP() != null && !headslab.getFromGP().isEmpty()) {
            headslab.setFromGPLong(Long.parseLong(headslab.getFromGP()));
        }
        if (headslab.getToGP() != null && !headslab.getToGP().isEmpty()) {
            headslab.setToGPLong(Long.parseLong(headslab.getToGP()));
        }
        if (headslab.getMaximumAmount() != null && !headslab.getMaximumAmount().isEmpty()) {
            headslab.setMaximumAmountLong(Long.parseLong(headslab.getMaximumAmount()));
        }
        if (headslab.getMinimumAmount() != null && !headslab.getMinimumAmount().isEmpty()) {
            headslab.setMinimumAmountLong(Long.parseLong(headslab.getMinimumAmount()));
        }

        String headslabJson = new Gson().toJson(headslab);
        String headslabId = DBManager.getDbConnection().insert(ApplicationConstants.HEAD_SLAB_TABLE, headslabJson);
        return headslabId;
    }

    public String fetch(String headslabId) throws Exception {
        if (headslabId == null || headslabId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.HEAD_SLAB_TABLE, headslabId);
        List<HeadSlab> headslabList = new Gson().fromJson(result, new TypeToken<List<HeadSlab>>() {
        }.getType());
        if (headslabList == null || headslabList.size() < 1) {
            return null;
        }
        return new Gson().toJson(headslabList.get(0));
    }

    public boolean delete(String headslabId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        if (headslabId == null || headslabId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<HeadSlab>() {
        }.getType();
        String headslab = new HeadSlabManager().fetch(headslabId);
        if (headslab == null || headslab.isEmpty()) {
            return false;
        }
        HeadSlab headslabrJson = new Gson().fromJson(headslab, type);
        headslabrJson.setStatus(ApplicationConstants.INACTIVE);
        headslabrJson.setUpdatedBy(userName);
        headslabrJson.setUpdateDate(System.currentTimeMillis() + "");
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HEAD_SLAB_TABLE, headslabId, new Gson().toJson(headslabrJson));
        return result;
    }

    public boolean update(HeadSlab headslab, String headslabId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String previosHeadslab = new HeadSlabManager().fetch(headslabId);
        if (previosHeadslab == null || previosHeadslab.isEmpty()) {
            return false;
        }
        HeadSlab previuosObj = new Gson().fromJson(previosHeadslab, new TypeToken<HeadSlab>() {
        }.getType());
        previuosObj.setAmount(headslab.getAmount());
        if (headslab.getAmount() != null && !headslab.getAmount().isEmpty()) {
            previuosObj.setAmountLong(Long.parseLong(headslab.getAmount()));
        }
        previuosObj.setBasedOn(headslab.getBasedOn());
        previuosObj.setCity(headslab.getCity());
        previuosObj.setCityCategory(headslab.getCityCategory());
        previuosObj.setClas(headslab.getClas());
        previuosObj.setDdo(headslab.getDdo());
        previuosObj.setFormulaOne(headslab.getFormulaOne());
        previuosObj.setFormulaTwo(headslab.getFormulaTwo());
        previuosObj.setFromBasic(headslab.getFromBasic());
        if (headslab.getFromBasic() != null && !headslab.getFromBasic().isEmpty()) {
            previuosObj.setFromBasicLong(Long.parseLong(headslab.getFromBasic()));
        } else {
            previuosObj.setFromBasicLong(0);
        }
        previuosObj.setFromGP(headslab.getFromGP());
        if (headslab.getFromGP() != null && !headslab.getFromGP().isEmpty()) {
            previuosObj.setFromGPLong(Long.parseLong(headslab.getFromGP()));
        } else {
            previuosObj.setFromGPLong(0);
        }
        previuosObj.setHeadName(headslab.getHeadName());
        previuosObj.setMaximumAmount(headslab.getMaximumAmount());
        if (headslab.getMaximumAmount() != null && !headslab.getMaximumAmount().isEmpty()) {
            previuosObj.setMaximumAmountLong(Long.parseLong(headslab.getMaximumAmount()));
        } else {
            previuosObj.setMaximumAmountLong(0);
        }
        previuosObj.setMinimumAmount(headslab.getMinimumAmount());
        if (headslab.getMinimumAmount() != null && !headslab.getMinimumAmount().isEmpty()) {
            previuosObj.setMinimumAmountLong(Long.parseLong(headslab.getMinimumAmount()));
        } else {
            previuosObj.setMinimumAmountLong(0);
        }
        previuosObj.setNatureType(headslab.getNatureType());
        previuosObj.setSalaryType(headslab.getSalaryType());
        previuosObj.setToBasic(headslab.getToBasic());
        if (headslab.getToBasic() != null && !headslab.getToBasic().isEmpty()) {
            previuosObj.setToBasicLong(Long.parseLong(headslab.getToBasic()));
        } else {
            previuosObj.setToBasicLong(0);
        }
        previuosObj.setToGP(headslab.getToGP());
        if (headslab.getToGP() != null && !headslab.getToGP().isEmpty()) {
            previuosObj.setToGPLong(Long.parseLong(headslab.getToGP()));
        } else {
            previuosObj.setToGPLong(0);
        }

        previuosObj.setTypeOne(headslab.getTypeOne());
        previuosObj.setTypeTwo(headslab.getTypeTwo());
        previuosObj.setUpdateDate(System.currentTimeMillis() + "");
        previuosObj.setUpdatedBy(userName);
        String headslabrJson = new Gson().toJson(previuosObj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HEAD_SLAB_TABLE, headslabId, headslabrJson);
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_SLAB_TABLE, conditionMap);
        List<HeadSlab> HeadSlabList = new Gson().fromJson(result1, new TypeToken<List<HeadSlab>>() {
        }.getType());
        try {
            HeadSlabList = getDDO(HeadSlabList);
        } catch (Exception e) {
        }
        try {
            HeadSlabList = getSalaryType(HeadSlabList);
        } catch (Exception e) {
        }
        try {
            HeadSlabList = getCityCategory(HeadSlabList);
        } catch (Exception e) {
        }
        for (Iterator<HeadSlab> iterator = HeadSlabList.iterator(); iterator.hasNext();) {
            HeadSlab next = iterator.next();
            //System.out.println(next.getCityCategory());
        }
        try {
            HeadSlabList = getCity(HeadSlabList);

        } catch (Exception e) {
        }
        try {
            HeadSlabList = getClas(HeadSlabList);

        } catch (Exception e) {
        }
        try {
            HeadSlabList = getNature(HeadSlabList);

        } catch (Exception e) {
        }
        try {
            HeadSlabList = getFormulaOne(HeadSlabList);

        } catch (Exception e) {
        }
        try {
            HeadSlabList = getFormulaTwo(HeadSlabList);

        } catch (Exception e) {
        }
        try {
            HeadSlabList = getHeadName(HeadSlabList);

        } catch (Exception e) {
        }
        try {
            HeadSlabList = getBasedOn(HeadSlabList);
        } catch (Exception e) {
        }

        String finalresult = new Gson().toJson(HeadSlabList);

        return finalresult;

    }
//   

    private List<HeadSlab> getDDO(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getDdo())) {
                    HeadSlabList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getSalaryType(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> SalaryTypeMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> list = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = list.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            SalaryTypeMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : SalaryTypeMap.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getSalaryType())) {
                    HeadSlabList.get(i).setSalaryType(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getCityCategory(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> CityCategoryMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CITY_CATEGORY_TABLE);
        List<CityCategoryMaster> list = new Gson().fromJson(result, new TypeToken<List<CityCategoryMaster>>() {
        }.getType());
        for (Iterator<CityCategoryMaster> iterator = list.iterator(); iterator.hasNext();) {
            CityCategoryMaster next = iterator.next();
            CityCategoryMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityCategory());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : CityCategoryMap.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getCityCategory())) {
                    HeadSlabList.get(i).setCityCategory(entry.getValue());
                    HeadSlabList.get(i).setCityCategoryId(entry.getKey());
                }
            }
        }

        return HeadSlabList;
    }

    private List<HeadSlab> getCity(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> CityMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CITY_TABLE);
        List<CityMaster> List = new Gson().fromJson(result, new TypeToken<List<CityMaster>>() {
        }.getType());
        for (Iterator<CityMaster> iterator = List.iterator(); iterator.hasNext();) {
            CityMaster next = iterator.next();
            CityMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getCityName());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : CityMap.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getCity())) {
                    HeadSlabList.get(i).setCity(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getClas(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.CLASS_TABLE);
        List<com.accure.hrms.dto.Class> List = new Gson().fromJson(result, new TypeToken<List<com.accure.hrms.dto.Class>>() {
        }.getType());
        for (Iterator<com.accure.hrms.dto.Class> iterator = List.iterator(); iterator.hasNext();) {
            com.accure.hrms.dto.Class next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getName());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getClas())) {
                    HeadSlabList.get(i).setClas(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getNature(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.NATURE_TABLE);
        List<Nature> List = new Gson().fromJson(result, new TypeToken<List<Nature>>() {
        }.getType());
        for (Iterator<Nature> iterator = List.iterator(); iterator.hasNext();) {
            Nature next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getNatureName());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getNatureType())) {
                    HeadSlabList.get(i).setNatureType(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getFormulaOne(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FORMULA_TABLE);
        List<Formula> List = new Gson().fromJson(result, new TypeToken<List<Formula>>() {
        }.getType());
        for (Iterator<Formula> iterator = List.iterator(); iterator.hasNext();) {
            Formula next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getFormulaOne())) {
                    HeadSlabList.get(i).setFormulaOne(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getFormulaTwo(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FORMULA_TABLE);
        List<Formula> List = new Gson().fromJson(result, new TypeToken<List<Formula>>() {
        }.getType());
        for (Iterator<Formula> iterator = List.iterator(); iterator.hasNext();) {
            Formula next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getFormulaTwo())) {
                    HeadSlabList.get(i).setFormulaTwo(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getHeadName(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.SALARY_HEAD_TABLE);
        List<SalaryHead> List = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        for (Iterator<SalaryHead> iterator = List.iterator(); iterator.hasNext();) {
            SalaryHead next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getHeadName())) {
                    HeadSlabList.get(i).setHeadId(entry.getKey());
                    HeadSlabList.get(i).setHeadName(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    private List<HeadSlab> getBasedOn(List<HeadSlab> HeadSlabList) throws Exception {
        Map<String, String> mp = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BASED_ON_MASTER);
        List<BasedOn> List = new Gson().fromJson(result, new TypeToken<List<BasedOn>>() {
        }.getType());
        for (Iterator<BasedOn> iterator = List.iterator(); iterator.hasNext();) {
            BasedOn next = iterator.next();
            mp.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBasedOn());
        }
        for (int i = 0; i < HeadSlabList.size(); i++) {
            for (Map.Entry<String, String> entry : mp.entrySet()) {
                if (entry.getKey().equals(HeadSlabList.get(i).getBasedOn())) {
                    HeadSlabList.get(i).setBasedOn(entry.getValue());
                }
            }
        }
        return HeadSlabList;
    }

    public String disableFormulaOrAmount(String headId) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.HEAD_SLAB_HEAD_NAME, headId);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.HEAD_SLAB_TABLE, conditionMap);
        List<HeadSlab> HeadSlabList = new Gson().fromJson(result1, new TypeToken<List<HeadSlab>>() {
        }.getType());
        if (HeadSlabList != null) {
            HeadSlab headSlab = HeadSlabList.get(0);
            String isFormulaOrAmount = headSlab.getTypeTwo();
            if (isFormulaOrAmount.equalsIgnoreCase(ApplicationConstants.IS_FORMULA)) {
                resultMap.put("result", ApplicationConstants.IS_FORMULA);
            } else if (isFormulaOrAmount.equalsIgnoreCase(ApplicationConstants.IS_AMOUNT)) {
                resultMap.put("result", ApplicationConstants.IS_AMOUNT);
            }
        } else {
            resultMap.put("result", ApplicationConstants.NO_DATA_FOUND);
        }
        return new Gson().toJson(resultMap);
    }
}
