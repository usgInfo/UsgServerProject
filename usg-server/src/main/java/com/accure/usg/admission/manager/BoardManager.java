/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.common.duplicate.Duplicate;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.admission.dto.BoardMaster;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author KC
 */
public class BoardManager {

    public String createBoard(String board, String loginUserId) throws Exception {
        BoardMaster obj = new BoardMaster();
        String result;
        HashMap map = new HashMap();
        map.put("board", board);
        map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (loginUserId == null) {
            result = null;
        } else if ((Duplicate.hasDuplicateforSave(ApplicationConstants.BOARD_TABLE, map))) {
            result = ApplicationConstants.DATA_EXISTED;
        } else {
            User user = new UserManager().fetch(loginUserId);
            String userName = user.getFname() + " " + user.getLname();
            obj.setBoard(board);
            obj.setCreateDate(System.currentTimeMillis() + "");
            obj.setStatus(ApplicationConstants.ACTIVE);
            obj.setCreatedBy(userName);
            obj.setUpdatedBy(userName);
            obj.setUpdateDate(System.currentTimeMillis() + "");
            String boardJson = new Gson().toJson(obj);
            String boardId = DBManager.getDbConnection().insert(ApplicationConstants.BOARD_TABLE, boardJson);
            if (boardId != null) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
        }
        return result;
    }
    
    public String viewBoardMasterList() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BOARD_TABLE, conditionMap);
        return result;

    }
    
    public String updateBoardMaster(String boardName, String primaryKey, String userId) throws Exception {
        String result = "";
        if (boardName == null || userId == null) {
            return null;
        }
        if (!boardName.equalsIgnoreCase(null)) {
            HashMap map = new HashMap();
            map.put("board", boardName);
            if (Duplicate.isDuplicateforUpdate(ApplicationConstants.BOARD_TABLE, map, primaryKey)) {
                result = ApplicationConstants.DATA_EXISTED;
            } else {
                String boardJson = DBManager.getDbConnection().fetch(ApplicationConstants.BOARD_TABLE, primaryKey);
                List<BoardMaster> list = new Gson().fromJson(boardJson, new TypeToken<List<BoardMaster>>() {
                }.getType());
                BoardMaster dbObject = list.get(0);
                User user = new UserManager().fetch(userId);
                String userName = user.getFname() + " " + user.getLname();
                dbObject.setBoard(boardName);
                dbObject.setStatus(ApplicationConstants.ACTIVE);
                dbObject.setUpdateDate(System.currentTimeMillis() + "");
                dbObject.setUpdatedBy(userName);
                String dbObjectJson = new Gson().toJson(dbObject);
                boolean status = DBManager.getDbConnection().update(ApplicationConstants.BOARD_TABLE, primaryKey, dbObjectJson);
                if (status) {
                    result = ApplicationConstants.SUCCESS;
                } else {
                    result = ApplicationConstants.FAIL;
                }
            }
        }
        return result;

    }

    public String deleteBoardMaster(String primaryKey, String userId) throws Exception {
        String status;
        if (primaryKey == null || userId == null) {
            return null;
        } 
//        else if ((DeleteDependencyManager.hasDependency(ApplicationConstants.GOVT_BUDGET_HEAD_TABLE, "majorHead", primaryKey))) {
//            status = ApplicationConstants.DELETE_MESSAGE;
//        } 
        else {
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            String existrelationJson = DBManager.getDbConnection().fetch(ApplicationConstants.BOARD_TABLE, primaryKey);
            List<BoardMaster> list = new Gson().fromJson(existrelationJson, new TypeToken<List<BoardMaster>>() {
            }.getType());
            BoardMaster dbObject = list.get(0);
            dbObject.setStatus(ApplicationConstants.DELETE);
            dbObject.setUpdateDate(System.currentTimeMillis() + "");
            dbObject.setDeletedBy(userName);
            String dbObjectJson = new Gson().toJson(dbObject);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.BOARD_TABLE, primaryKey, dbObjectJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }

        }
        return status;

    }
}
