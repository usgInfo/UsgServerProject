/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.budget.dto.FundType;
import com.accure.db.in.DAO;
import com.accure.finance.dto.BankName;
import com.accure.finance.dto.FixedDeposits;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author deepak2310
 */
public class FixedDepositsManager {

    public String createFixedDeposit(FixedDeposits fixedDeposits, String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        fixedDeposits.setCreateDate(System.currentTimeMillis() + "");
        fixedDeposits.setUpdateDate(System.currentTimeMillis() + "");
        fixedDeposits.setStatus(ApplicationConstants.ACTIVE);
        fixedDeposits.setCreatedBy(userName);

        String fdrJson = new Gson().toJson(fixedDeposits);

        String fdrId = DBManager.getDbConnection().insert(ApplicationConstants.FIXED_DEPOSIT_TABLE, fdrJson);
        if (fdrId != null) {
            return fdrId;
        }
        return null;
    }

    public HashMap<String, String> fetchallBudgetNature() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fundTypeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.FUND_TYPE_TABLE, conditionMap);
        List<FundType> fundTypeList = new Gson().fromJson(fundTypeJson, new TypeToken<List<FundType>>() {
        }.getType());
        conditionMap.clear();
        for (FundType fundType : fundTypeList) {
            conditionMap.put(((Map<String, String>) fundType.getId()).get("$oid"), fundType.getDescription());
        }
        return conditionMap;
    }

    public HashMap<String, String> fetchAllBank() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String bankNameJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_NAME_TABLE, conditionMap);
        List<BankName> bankNameList = new Gson().fromJson(bankNameJson, new TypeToken<List<BankName>>() {
        }.getType());
        conditionMap.clear();
        for (BankName bankName : bankNameList) {
            conditionMap.put(((Map<String, String>) bankName.getId()).get("$oid"), bankName.getBankName());
        }
        return conditionMap;
    }

    public List<FixedDeposits> fetchAllFixedDeposits() throws Exception {
        HashMap<String, String> fundType = fetchallBudgetNature();
        HashMap<String, String> bankName = fetchAllBank();
        DAO dao = DBManager.getDbConnection();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String fixedDepositJson = dao.fetchAllRowsByConditions(ApplicationConstants.FIXED_DEPOSIT_TABLE, conditionMap);
//        dao.close();
        List<FixedDeposits> fixedDepositList = new Gson().fromJson(fixedDepositJson, new TypeToken<List<FixedDeposits>>() {
        }.getType());
        List<FixedDeposits> returnList = new ArrayList<FixedDeposits>();
        if (fixedDepositList != null) {
            for (FixedDeposits fixedDeposits : fixedDepositList) {
                if (fixedDeposits.getFundType() != null) {
                    String fundTypeId = fixedDeposits.getFundType();
                    if (fundType.containsKey(fundTypeId) && fundType.get(fundTypeId) != null) {
                        fixedDeposits.setFundTypeName(fundType.get(fundTypeId));
                    }
                }
                if (fixedDeposits.getBank() != null) {
                    String bankId = fixedDeposits.getBank();
                    if (bankName.containsKey(bankId) && bankName.get(bankId) != null) {
                        fixedDeposits.setBankName(bankName.get(bankId));
                    }
                }
                returnList.add(fixedDeposits);
            }
        }

        return returnList;
    }

    public FixedDeposits fetch(String fixedDepositId) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();

        String fixedDepositJson = DBManager.getDbConnection().fetch(ApplicationConstants.FIXED_DEPOSIT_TABLE, fixedDepositId);

        if (fixedDepositJson == null || fixedDepositJson.isEmpty()) {
            return null;
        }
        List<FixedDeposits> fixedDepositList = new Gson().fromJson(fixedDepositJson, new TypeToken<List<FixedDeposits>>() {
        }.getType());
        if (fixedDepositList == null || fixedDepositList.isEmpty()) {
            return null;
        }
        return fixedDepositList.get(0);

    }

    public boolean updateFixedDeposits(FixedDeposits fixedDeposits, String userId, String fixedDepositId) throws Exception {

        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();

        FixedDeposits fixedDepositsDB = fetch(fixedDepositId);
        if (fixedDepositsDB.getFundType() != null) {
            fixedDepositsDB.setFundType(fixedDeposits.getFundType());
        }
        if (fixedDepositsDB.getVoucherNo() != null) {
            fixedDepositsDB.setVoucherNo(fixedDeposits.getVoucherNo());
        }
        if (fixedDepositsDB.getBank() != null) {
            fixedDepositsDB.setBank(fixedDeposits.getBank());
        }
        if (fixedDepositsDB.getDate() != null) {
            fixedDepositsDB.setDate(fixedDeposits.getDate());
        }
        if (fixedDepositsDB.getFdrNumber() != null) {
            fixedDepositsDB.setFdrNumber(fixedDeposits.getFdrNumber());
        }
        if (fixedDepositsDB.getAmount() != null) {
            fixedDepositsDB.setAmount(fixedDeposits.getAmount());
        }
        if (fixedDepositsDB.getPeriods() != null) {
            fixedDepositsDB.setPeriods(fixedDeposits.getPeriods());
        }
        if (fixedDepositsDB.getRate() != null) {
            fixedDepositsDB.setRate(fixedDeposits.getRate());
        }
        if (fixedDepositsDB.getStatus() != null) {
            fixedDepositsDB.setStatus(fixedDeposits.getStatus());
        }
        if (fixedDepositsDB.getRemarks() != null) {
            fixedDepositsDB.setRemarks(fixedDeposits.getRemarks());
        }
        if (fixedDepositsDB.getRefToFdr() != null) {
            fixedDepositsDB.setRefToFdr(fixedDeposits.getRefToFdr());
        }
        if (fixedDepositsDB.getInterestAmount() != null) {
            fixedDepositsDB.setInterestAmount(fixedDeposits.getInterestAmount());
        }
        if (fixedDepositsDB.getMaturityAmount() != null) {
            fixedDepositsDB.setMaturityAmount(fixedDeposits.getMaturityAmount());
        }
        if (fixedDepositsDB.getMaturityDate() != null) {
            fixedDepositsDB.setMaturityDate(fixedDeposits.getMaturityDate());
        }

        fixedDepositsDB.setUpdateDate(System.currentTimeMillis() + "");
        fixedDepositsDB.setStatus(ApplicationConstants.ACTIVE);
        fixedDepositsDB.setUpdatedBy(userName);

        String fixedDepositJson = new Gson().toJson(fixedDepositsDB);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FIXED_DEPOSIT_TABLE, fixedDepositId, fixedDepositJson);
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteFixedDeposits(String fixedDepositId, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        FixedDeposits fixedDeposits = fetch(fixedDepositId);
        fixedDeposits.setStatus(ApplicationConstants.DELETE);
        fixedDeposits.setUpdatedBy(userName);

        String fixedDeposit = new Gson().toJson(fixedDeposits);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.FIXED_DEPOSIT_TABLE, fixedDepositId, fixedDeposit);
        if (status) {
            return true;
        }
        return false;
    }

}
