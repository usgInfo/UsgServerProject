/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.leave.dto.LeaveWorkFlowMaster;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author User
 */
public class LeaveWorkFlowMasterManager {
    public String saveLeaveWorkFlowMaster(LeaveWorkFlowMaster lwfm) throws Exception {

        lwfm.setCreateDate(System.currentTimeMillis() + "");
        lwfm.setUpdateDate(System.currentTimeMillis() + "");
        lwfm.setStatus(ApplicationConstants.ACTIVE);
        String hlmJson1 = new Gson().toJson(lwfm);
        String cid1 = DBManager.getDbConnection().insert(ApplicationConstants.LEAVE_WORK_FLOW_MASTER,hlmJson1);
        return cid1;
   }
    
    public String ViewAllLeaveWorkFlowMaster() throws Exception {
        HashMap<String, String> conditionMap1 = new HashMap<String, String>();
        conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEAVE_WORK_FLOW_MASTER, conditionMap1);
        return result1;
      }
    
     public boolean updateLeaveWorkFlowMaster(LeaveWorkFlowMaster leaveWorkFlowMaster, String leaveWorkFlowMasterId) throws Exception {
        leaveWorkFlowMaster.setUpdateDate(System.currentTimeMillis() + "");
        leaveWorkFlowMaster.setStatus(ApplicationConstants.ACTIVE);
        String leaveWorkFlowMasterJson = new Gson().toJson(leaveWorkFlowMaster);
        boolean result1 = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_WORK_FLOW_MASTER, leaveWorkFlowMasterId, leaveWorkFlowMasterJson);
        return result1;
    }
     
     public boolean deleteLeaveWorkFlowMaster(String id) throws Exception {
        String leaveWorkFlowJson = DBManager.getDbConnection().fetch(ApplicationConstants.LEAVE_WORK_FLOW_MASTER, id);
        List<LeaveWorkFlowMaster> leaveWorkFlowlist = new Gson().fromJson(leaveWorkFlowJson, new TypeToken<List<LeaveWorkFlowMaster>>() {
        }.getType());
        LeaveWorkFlowMaster leaveWorkFlowMasterlist = leaveWorkFlowlist.get(0);
        LeaveWorkFlowMaster leaveWorkFlowMaster = new LeaveWorkFlowMaster();
        leaveWorkFlowMaster.setDdo(leaveWorkFlowMasterlist.getDdo());
        leaveWorkFlowMaster.setClass1(leaveWorkFlowMasterlist.getClass1());
        leaveWorkFlowMaster.setWorkFlowName(leaveWorkFlowMasterlist.getWorkFlowName());
        leaveWorkFlowMaster.setDescription(leaveWorkFlowMasterlist.getDescription());
        leaveWorkFlowMaster.setCreateDate(leaveWorkFlowMasterlist.getCreateDate());
        leaveWorkFlowMaster.setStatus(ApplicationConstants.INACTIVE);
        leaveWorkFlowMaster.setUpdateDate(System.currentTimeMillis() + "");
        String leaveWorkFlowMasterJson = new Gson().toJson(leaveWorkFlowMaster);
        boolean status1 = DBManager.getDbConnection().update(ApplicationConstants.LEAVE_WORK_FLOW_MASTER, id, leaveWorkFlowMasterJson);
        return status1;
    }
    
}
