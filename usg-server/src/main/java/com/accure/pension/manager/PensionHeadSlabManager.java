/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.pension.manager;

import com.accure.hrms.manager.HeadSlabManager;
import com.accure.pension.dto.PensionHeadSlab;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author accure
 */
public class PensionHeadSlabManager {
     public String save(PensionHeadSlab headslab, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        headslab.setCreateDate(System.currentTimeMillis() + "");
        headslab.setCreatedBy(userName);
        headslab.setStatus(ApplicationConstants.ACTIVE);
        if (headslab.getAmount() != null && !headslab.getAmount().isEmpty()) {
            headslab.setAmountLong(Long.parseLong(headslab.getAmount()));
        }
        if (headslab.getFromOriginalPension()!= null && !headslab.getFromOriginalPension().isEmpty()) {
            headslab.setFromOriginalPensionLong(Long.parseLong(headslab.getFromOriginalPension()));
        }
        if (headslab.getToOriginalPension()!= null && !headslab.getToOriginalPension().isEmpty()) {
            headslab.setToOriginalPensionLong(Long.parseLong(headslab.getToOriginalPension()));
        }
        if (headslab.getMaxAmount()!= null && !headslab.getMaxAmount().isEmpty()) {
            headslab.setMaximumAmountLong(Long.parseLong(headslab.getMaxAmount()));
        }
        if (headslab.getMinAmount()!= null && !headslab.getMinAmount().isEmpty()) {
            headslab.setMinimumAmountLong(Long.parseLong(headslab.getMinAmount()));
        }

        String headslabJson = new Gson().toJson(headslab);
        String headslabId = DBManager.getDbConnection().insert(ApplicationConstants.PENSION_HEAD_SLAB_TABLE, headslabJson);
        return headslabId;
    }
       public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PENSION_HEAD_SLAB_TABLE, conditionMap);
        List<PensionHeadSlab> HeadSlabList = new Gson().fromJson(result1, new TypeToken<List<PensionHeadSlab>>() {
        }.getType());
//        try {
//            HeadSlabList = getDDO(HeadSlabList);
//        } catch (Exception e) {
//        }
//        try {
//            HeadSlabList = getSalaryType(HeadSlabList);
//        } catch (Exception e) {
//        }
//        try {
//            HeadSlabList = getCityCategory(HeadSlabList);
//        } catch (Exception e) {
//        }
//       
//        try {
//            HeadSlabList = getClas(HeadSlabList);
//
//        } catch (Exception e) {
//        }
       

        String finalresult = new Gson().toJson(HeadSlabList);

        return finalresult;

    }
         public String fetch(String headslabId) throws Exception {
        if (headslabId == null || headslabId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.PENSION_HEAD_SLAB_TABLE, headslabId);
        List<PensionHeadSlab> headslabList = new Gson().fromJson(result, new TypeToken<List<PensionHeadSlab>>() {
        }.getType());
        if (headslabList == null || headslabList.size() < 1) {
            return null;
        }
        return new Gson().toJson(headslabList.get(0));
    }
        public boolean update(PensionHeadSlab headslab, String headslabId, String userId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        String previosHeadslab = new HeadSlabManager().fetch(headslabId);
        if (previosHeadslab == null || previosHeadslab.isEmpty()) {
            return false;
        }
        PensionHeadSlab previuosObj = new Gson().fromJson(previosHeadslab, new TypeToken<PensionHeadSlab>() {
        }.getType());
        previuosObj.setAmount(headslab.getAmount());
        if (headslab.getAmount() != null && !headslab.getAmount().isEmpty()) {
            previuosObj.setAmountLong(Long.parseLong(headslab.getAmount()));
        }
        previuosObj.setPensionBasedOn(headslab.getPensionBasedOn());
        previuosObj.setPensionClass(headslab.getPensionClass());
        previuosObj.setFormulaOne(headslab.getFormulaOne());
        previuosObj.setFormulaTwo(headslab.getFormulaTwo());

        previuosObj.setFromOriginalPension(headslab.getFromOriginalPension());
        if (headslab.getFromOriginalPension()!= null && !headslab.getFromOriginalPension().isEmpty()) {
            previuosObj.setFromOriginalPensionLong(Long.parseLong(headslab.getFromOriginalPension()));
        } else {
            previuosObj.setFromOriginalPensionLong(0);
        }
         previuosObj.setToOriginalPension(headslab.getToOriginalPension());
        if (headslab.getToOriginalPension()!= null && !headslab.getToOriginalPension().isEmpty()) {
            previuosObj.setToOriginalPensionLong(Long.parseLong(headslab.getToOriginalPension()));
        } else {
            previuosObj.setToOriginalPensionLong(0);
        }
        previuosObj.setPensionHeadName(headslab.getPensionHeadName());
        previuosObj.setMaxAmount(headslab.getMaxAmount());
        if (headslab.getMaxAmount()!= null && !headslab.getMaxAmount().isEmpty()) {
            previuosObj.setMaximumAmountLong(Long.parseLong(headslab.getMaxAmount()));
        } else {
            previuosObj.setMaximumAmountLong(0);
        }
        previuosObj.setMinAmount(headslab.getMinAmount());
        if (headslab.getMinAmount() != null && !headslab.getMinAmount().isEmpty()) {
            previuosObj.setMinimumAmountLong(Long.parseLong(headslab.getMinAmount()));
        } else {
            previuosObj.setMinimumAmountLong(0);
        }
        previuosObj.setPensionNatureType(headslab.getPensionNatureType());
        previuosObj.setTypeOne(headslab.getTypeOne());
        previuosObj.setTypeTwo(headslab.getTypeTwo());
        previuosObj.setUpdateDate(System.currentTimeMillis() + "");
        previuosObj.setUpdatedBy(userName);
        String headslabrJson = new Gson().toJson(previuosObj);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.PENSION_HEAD_SLAB_TABLE, headslabId, headslabrJson);
        return result;
    }
}

