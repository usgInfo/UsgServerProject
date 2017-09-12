/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.common.delete.DeleteDependencyManager;
import com.accure.hrms.dto.Bank;
import com.accure.hrms.dto.CityMaster;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author Asif
 */
public class BankManager {

    public String saveBank(Bank bank,String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        
          HashMap duplicateCheckMap = new HashMap();
         String bankid=null;
         String micrCode=null;
           String ifscCode=null;
          ifscCode=bank.getIfsccode();
          micrCode=bank.getMicrcode();
          if(!(micrCode).equals(null) && !(micrCode).equals("") )
          {
            duplicateCheckMap.put("micrcode",micrCode );
            if(duplicate(duplicateCheckMap))
            {
                bankid = ApplicationConstants.DUPLICATE_MESSAGE;
                return bankid;
            }
          }
          if(!(ifscCode).equals(null) && !(ifscCode).equals(""))
          {
            duplicateCheckMap.clear();
            duplicateCheckMap.put("ifsccode", ifscCode);
           if(duplicate(duplicateCheckMap))
            {
                bankid = ApplicationConstants.DUPLICATE_MESSAGE;
                 return bankid;
            }
          }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("branchname", bank.getBranchname());
        duplicateCheckMap.put("city", bank.getCity());
        
        if(duplicate(duplicateCheckMap))
        {
            bankid = ApplicationConstants.DUPLICATE_MESSAGE;
            return bankid;
        }
        
        bank.setCreatedBy(userName);
        bank.setCreateDate(System.currentTimeMillis() + "");
        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);
        

        String bankJson = new Gson().toJson(bank);

         bankid = DBManager.getDbConnection().insert(ApplicationConstants.BANK_TABLE, bankJson);
        return bankid;
    }

    public String fetchBank(String bankId) throws Exception {
        if (bankId == null || bankId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_TABLE, bankId);
        List<Bank> bankList = new Gson().fromJson(result, new TypeToken<List<Bank>>() {
        }.getType());
        if (bankList == null || bankList.size() < 1) {
            return null;
        }
        return new Gson().toJson(bankList.get(0));
    }

    /*public boolean deleteBank(String bankId) throws Exception {

        if (bankId == null || bankId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<Bank>() {
        }.getType();
        String bank = new BankManager().fetchBank(bankId);
        if (bank == null || bank.isEmpty()) {
            return false;
        }
        Bank bankJson = new Gson().fromJson(bank, type);
        bankJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.BANK_TABLE, bankId, new Gson().toJson(bankJson));
        return result;
    }*/
    
    public String deleteBank(String bankId,String loginUserId) throws Exception {

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        Bank bank = fetchBankDetails(bankId);

        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.DELETE);
        bank.setDeletedBy(userName);
         String bankJson = new Gson().toJson(bank);
        String status = "";
        if ((DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_TABLE, "bank", bankId)) || (DeleteDependencyManager.hasDependency(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, "bank", bankId))) {
            status = ApplicationConstants.DELETE_MESSAGE;
        } else {
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.BANK_TABLE, bankId, bankJson);
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }

    public Bank fetchBankDetails(String rid) throws Exception {
        if (rid == null) {
            return null;
        }
        String bankJson = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_TABLE, rid);
        List<Bank> banklist = new Gson().fromJson(bankJson, new TypeToken<List<Bank>>() {
        }.getType());
        Bank bank = banklist.get(0);
        return bank;
    }
    public String updateBank(Bank bank, String bankId,String loginUserId) throws Exception
    {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
           HashMap duplicateCheckMap = new HashMap();
           String result = null;
           String micrCode=null;
           String ifscCode=null;
          ifscCode=bank.getIfsccode();
          micrCode=bank.getMicrcode();
          if(!(micrCode).equals(null) && !(micrCode).equals("") )
          {
            duplicateCheckMap.put("micrcode",micrCode );
            if (hasDuplicateforUpdate(ApplicationConstants.BANK_TABLE, duplicateCheckMap,bankId))
            {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
                return result;
            }
          }
          if(!(ifscCode).equals(null) && !(ifscCode).equals(""))
          {
            duplicateCheckMap.clear();
            duplicateCheckMap.put("ifsccode", ifscCode);
            if(hasDuplicateforUpdate(ApplicationConstants.BANK_TABLE, duplicateCheckMap,bankId))
            {
                result = ApplicationConstants.DUPLICATE_MESSAGE;
                 return result;
            }
          }
        duplicateCheckMap.clear();
        duplicateCheckMap.put("branchname", bank.getBranchname());
        duplicateCheckMap.put("city", bank.getCity());
        if(hasDuplicateforUpdate(ApplicationConstants.BANK_TABLE, duplicateCheckMap,bankId))
        {
            result = ApplicationConstants.DUPLICATE_MESSAGE;
            return result;
        }
            else
            {
        bank.setUpdatedBy(userName);
        bank.setUpdateDate(System.currentTimeMillis() + "");
        bank.setStatus(ApplicationConstants.ACTIVE);
        String bankJson = new Gson().toJson(bank);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BANK_TABLE, bankId, bankJson);
         if (status) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }
            }
        return result;
    }

    public String fetchAllBank() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_TABLE, conditionMap);
        return result;

    }
      public boolean duplicate(HashMap conditionMap) throws Exception {
        boolean res = false;
        try {
            //HashMap<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
           // conditionMap.put("bankname", bank.getBankname());
//            conditionMap.put("branchname", bank.getBranchname());
//            conditionMap.put("city", bank.getCity());
            //conditionMap.put("state", bank.getState());
            String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_TABLE, conditionMap);
            if (result != null) {
//                List<DAMaster> list = new Gson().fromJson(result, new TypeToken<List<DAMaster>>() {
//                }.getType());
//
//                for (DAMaster li : list) {
//                    if (damaster.getDefinedRate().equals(li.getDefinedRate()) && damaster.getPaidRate() == li.getPaidRate()) {
//                        res = true;
//                    }
//                }
                     res = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }
      public static boolean hasDuplicateforUpdate(String tableName, HashMap columns, String id) {
        boolean status = false;
        String result = null;
        HashMap duplicatecheckMap = new HashMap();
        try {
            if (!columns.isEmpty()) {
                Iterator entries = columns.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                        duplicatecheckMap.put(entry.getKey(),entry.getValue());
                }
                duplicatecheckMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                 String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BANK_TABLE, duplicatecheckMap);
                 if(result1 ==null)
                 {
                     status=false;
                     return status;
                 }
                 else
                 {
                 List<Bank> employeeList = new Gson().fromJson(result1, new TypeToken<List<Bank>>() { }.getType());
                //System.out.println("status" + status);
                 for(Bank city:employeeList)
                 {
                    String obid=((LinkedTreeMap<String, String>) city.getId()).get("$oid");
                    if(!obid.equals(id))
                    {
                        status=true;
                        break;
                    }
                    else
                    {
                        continue;
                    }
                 }
                 }
            }
        } catch (Exception e) {
            status = false;
        } finally {
            //System.out.println("status" + status);
            return status;
        }
    }
}
