/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.user.manager.UserManager;
import com.accure.finance.dto.Group;
import com.accure.finance.dto.GroupMaster;
import com.accure.user.dto.User;
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
 * @author Asif/Shwetha T S
 */
public class GroupManager {

    public String save(Group group, String userId) throws Exception {

        String result = "";
        boolean status_one = false, status_two = false;
        HashMap map = new HashMap();
        if (group == null || userId == null) {
            result = null;
        }
        if (!(group.getGroupAlias().equalsIgnoreCase(null)) && !(group.getGroupName().equalsIgnoreCase(null))) {
            map.put("groupName", group.getGroupName());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.GROUP_TABLE, map)) {
                status_one = true;
                return ApplicationConstants.DUPLICATE;

            }
        }

        if (!(group.getGroupAlias().equalsIgnoreCase(null))) {
            map.clear();
            map.put("groupAlias", group.getGroupAlias());
            if (Duplicate.hasDuplicateforSave(ApplicationConstants.GROUP_TABLE, map)) {
                status_two = true;
                return ApplicationConstants.DUPLICATE;

            }
        }
        if ((status_one && status_two) == false) {
            if (!(userId).equalsIgnoreCase(null)) {
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();

                group.setCreateDate(System.currentTimeMillis() + "");
                group.setUpdateDate(System.currentTimeMillis() + "");
                group.setStatus(ApplicationConstants.ACTIVE);
                group.setCreatedBy(userName);
                group.setUpdatedBy(userName);

                String objectJson = new Gson().toJson(group);

                String fResult = DBManager.getDbConnection().insert(ApplicationConstants.GROUP_TABLE, objectJson);

                if (fResult != null) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public String fetch(String gId) throws Exception {
        if (gId == null || gId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.GROUP_TABLE, gId);
        List<Group> groupList = new Gson().fromJson(result, new TypeToken<List<Group>>() {
        }.getType());
        if (groupList == null || groupList.size() < 1) {
            return null;
        }
        return new Gson().toJson(groupList.get(0));
    }

    public String delete(String gId, String userId) throws Exception {
        String result = "";
        if (gId == null || gId.isEmpty()) {
            return null;
        }
        Type type = new TypeToken<Group>() {
        }.getType();
        String group = new GroupManager().fetch(gId);
        if (group == null || group.isEmpty()) {
            return null;
        }
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.LEDGER_TABLE, "underGroup", gId))) {
            result = ApplicationConstants.DELETE_MESSAGE;
        } else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            Group grouprJson = new Gson().fromJson(group, type);
            grouprJson.setStatus(ApplicationConstants.DELETE);
            grouprJson.setUpdatedBy(userName);
            boolean status = DBManager.getDbConnection().update(ApplicationConstants.GROUP_TABLE, gId, new Gson().toJson(grouprJson));

            if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }

        return result;
    }

    public String update(Group group, String gId, String userId) throws Exception {

        String result = "";
        boolean status_one = false, status_two = false;
        HashMap map = new HashMap();
        if (group == null || userId == null) {
            result = null;
        }
        if (!(group.getGroupAlias().equalsIgnoreCase(null)) && !(group.getGroupName().equalsIgnoreCase(null))) {
            map.put("groupName", group.getGroupName());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.GROUP_TABLE, map, gId)) {
                status_one = true;
                return ApplicationConstants.DUPLICATE;

            }
        }

        if (!(group.getGroupAlias().equalsIgnoreCase(null))) {
            map.clear();
            map.put("groupAlias", group.getGroupAlias());
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.GROUP_TABLE, map, gId)) {
                status_two = true;
                return ApplicationConstants.DUPLICATE;

            }
        }
        if ((status_one && status_two) == false) {
            if (!(userId).equalsIgnoreCase(null)) {

                group.setUpdateDate(System.currentTimeMillis() + "");
                group.setStatus(ApplicationConstants.ACTIVE);
                String grouprJson = new Gson().toJson(group);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.GROUP_TABLE, gId, grouprJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GROUP_TABLE, conditionMap);
        List<Group> list = new Gson().fromJson(result, new TypeToken<List<Group>>() {
        }.getType());
        for(Group group:list)
        {
             String underGroupId="";
             underGroupId=group.getUnderGroup();
            if(!underGroupId.equals(""))
            {
            String groupJson = DBManager.getDbConnection().fetch(ApplicationConstants.GROUP_TABLE, underGroupId);
             if (groupJson == null || groupJson.isEmpty())
             {
           
            }
             else
             {
                 List<Group> groupList = new Gson().fromJson(groupJson, new TypeToken<List<Group>>() {}.getType());
                 Group groupObj=groupList.get(0);
                    String underGroupName=groupObj.getGroupName();
                    group.setUnderGroupName(underGroupName);
             }
            }
            else
            {
            group.setUnderGroupName("");
        }
        }
       // list = getUnderGroup(list);
        return new Gson().toJson(list);
    }

    public String createGroup(GroupMaster group, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        group.setCreateDate(System.currentTimeMillis() + "");
        group.setUpdateDate(System.currentTimeMillis() + "");
        group.setStatus(ApplicationConstants.ACTIVE);
        group.setCreatedBy(userName);

        String groupJson = new Gson().toJson(group);

        String groupId = DBManager.getDbConnection().insert(ApplicationConstants.GROUP_MASTER_TABLE, groupJson);
        if (groupId != null) {
            return groupId;
        }
        return null;
    }

    public List<Group> fetchAllGroup() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String groupJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GROUP_TABLE, conditionMap);
        List<Group> groupList = new Gson().fromJson(groupJson, new TypeToken<List<Group>>() {
        }.getType());
        return groupList;
    }
     public String getNatutureForUnderGroup(String underGroupId)throws Exception
     {
         String natureType=null;
         String groupJson = DBManager.getDbConnection().fetch(ApplicationConstants.GROUP_TABLE, underGroupId);
        if (groupJson == null || groupJson.isEmpty()) {
            return null;
        }
        else
        {
          List<Group> groupList = new Gson().fromJson(groupJson, new TypeToken<List<Group>>() {
        }.getType());
          Group groupObj=groupList.get(0);
          natureType=groupObj.getNature();
        }
         return natureType;
     }
    public GroupMaster fetchGroup(String groupId) throws Exception {
        if (groupId == null || groupId.isEmpty()) {
            return null;
        }
        String groupJson = DBManager.getDbConnection().fetch(ApplicationConstants.GROUP_MASTER_TABLE, groupId);
        if (groupJson == null || groupJson.isEmpty()) {
            return null;
        }
        List<GroupMaster> groupList = new Gson().fromJson(groupJson, new TypeToken<List<GroupMaster>>() {
        }.getType());
        if (groupList == null || groupList.isEmpty()) {
            return null;
        }
        return groupList.get(0);

    }

    public boolean deleteGroupMaster(String groupId, String currentUserLogin) throws Exception {
        User user = new UserManager().fetch(currentUserLogin);
        String userName = user.getFname() + " " + user.getLname();

        GroupMaster group = fetchGroup(groupId);
        group.setStatus(ApplicationConstants.DELETE);
        group.setUpdateDate(System.currentTimeMillis() + "");
        group.setUpdatedBy(userName);

        String groupJson = new Gson().toJson(group);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.GROUP_MASTER_TABLE, groupId, groupJson);
        if (status) {
            return true;
        }
        return false;
    }

    public boolean updateGroupMaster(GroupMaster group, String userId, String groupId) throws Exception {
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        GroupMaster groupMaster = fetchGroup(groupId);
        if (groupMaster.getGroupName() != null || !groupMaster.getGroupName().isEmpty()) {
            groupMaster.setGroupName(group.getGroupName());
        }
        if (groupMaster.getRemarks() != null || !groupMaster.getRemarks().isEmpty()) {
            groupMaster.setRemarks(group.getRemarks());
        }
        groupMaster.setUpdateDate(System.currentTimeMillis() + "");
        groupMaster.setUpdatedBy(userName);
        groupMaster.setStatus(ApplicationConstants.ACTIVE);

        String groupJson = new Gson().toJson(groupMaster);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.GROUP_MASTER_TABLE, groupId, groupJson);
        if (status) {
            return true;
        }
        return false;
    }

    private List<Group> getUnderGroup(List<Group> list) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.GROUP_MASTER_TABLE, conditionMap);
        List<GroupMaster> underGroupList = new Gson().fromJson(result, new TypeToken<List<GroupMaster>>() {
        }.getType());
        conditionMap.clear();
        for (GroupMaster underGroup : underGroupList) {
            conditionMap.put(((Map<String, String>) underGroup.getId()).get("$oid"), underGroup.getGroupName());
        }
        for (int i = 0; i < list.size(); i++) {
            String val = conditionMap.get(list.get(i).getUnderGroup());
            if (val != null) {
                list.get(i).setUnderGroup(val);
            }
        }
        return list;
    }
}
