/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.budget.dto.FinancialYear;
import com.accure.budget.dto.PreviousBudgetAmountDetails;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.budget.dto.ddoHeadCodeMapping;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
import com.accure.hrms.manager.SortByDisplayLeave;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class BudgetIncomeManager {

    public String getBudgetHeads(String ddo, String fundType, String sector) throws Exception {
        ddoHeadCodeMapping object = new ddoHeadCodeMapping();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("fundType", fundType);
        conditionMap.put("sector", sector);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DDO_HEAD_CODE_TABLE, conditionMap);
        List<ddoHeadCodeMapping> ddoList = new Gson().fromJson(result, new TypeToken<List<ddoHeadCodeMapping>>() {
        }.getType());
        List<BudgetHeadMaster> headlist = new ArrayList<BudgetHeadMaster>();
        for (ddoHeadCodeMapping cl : ddoList) {
            for (int i = 0; i < cl.getHeadCode().size(); i++) {
                String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getHeadCode().get(i));
                if (result1 != null) {
                    List<BudgetHeadMaster> headCodeList = new Gson().fromJson(result1, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    BudgetHeadMaster gal = headCodeList.get(0);
                    if (gal.getIsActive().equalsIgnoreCase(ApplicationConstants.YES)) {
                        headlist.add(gal);
                    }
                }
            }
        }
        return new Gson().toJson(headlist);
    }

    private List<CreateIncomeBudget> getSector(List<CreateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_SECTOR_TABLE);
        List<BudgetSector> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetSector>>() {
        }.getType());
        for (Iterator<BudgetSector> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetSector next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getSector())) {
                    list.get(i).setSectorName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<CreateIncomeBudget> getFinancialYearForIncome(List<CreateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE);
        List<FinancialYear> religionList = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        for (Iterator<FinancialYear> iterator = religionList.iterator(); iterator.hasNext();) {
            FinancialYear next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getFromDate() + "-" + next.getToDate());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getFinancialYear())) {
                    list.get(i).setFinancialYear((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<CreateIncomeBudget> getBudgetTypeForIncome(List<CreateIncomeBudget> list) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_TYPE_TABLE);
        List<BudgetType> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetType>>() {
        }.getType());
        for (Iterator<BudgetType> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetType next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetType())) {
                    list.get(i).setBudgetTypeName((entry.getValue()));
                }
            }
        }
        return list;
    }

    private List<ddoHeadCodeMapping> getBudgetHeadForBudgteIncome(List<ddoHeadCodeMapping> list) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetHead())) {
                    list.get(i).setBudgetHead((entry.getValue()));
                }
            }
        }
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            map.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHeadDescription());
        }
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals(list.get(i).getBudgetHead())) {
                    list.get(i).setBudgetHeadDescription(entry.getValue());
                }
            }
        }
        return list;
    }

    public String Save(CreateIncomeBudget obj, String newSlno) throws Exception {
        String budgetTypeName = "";
        String budgetDdoCode = "";
        obj.setCreateDate(System.currentTimeMillis() + "");
        obj.setUpdateDate(System.currentTimeMillis() + "");
        obj.setStatus(ApplicationConstants.ACTIVE);
        obj.setSentStatus(ApplicationConstants.FALSE);
        obj.setIsSanctioned(ApplicationConstants.FALSE);
        obj.SanctionedAmount("0");
        obj.setIsConsolidated(ApplicationConstants.FALSE);
//        String ddo=obj.getDdo();
//        ddo=ddo.substring(3);
        String budgetType = obj.getBudgetType();
        String finYear = obj.getFinancialYear();
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("fromDate", finYear.trim());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        //System.out.println(conditionMap.toString());
        String currentYearJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, conditionMap);
        if (currentYearJson == null) {

        } else {
            List<com.accure.hrms.dto.FinancialYear> fyList = new Gson().fromJson(currentYearJson, new TypeToken<List<com.accure.hrms.dto.FinancialYear>>() {
            }.getType());
            com.accure.hrms.dto.FinancialYear fyObj = fyList.get(0);
            String fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
            //System.out.println("----------------fyId-------" + fyId);
            obj.setFinancialYearId(fyId);
        }
        //System.out.println("-------" + currentYearJson);
        String result2 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, obj.getDdo());
        List<DDO> ddoList = new Gson().fromJson(result2, new TypeToken<List<DDO>>() {
        }.getType());
        if (result2 != null) {
            List<DDO> DDOList = new Gson().fromJson(result2, new TypeToken<List<DDO>>() {
            }.getType());
            obj.setDdoName(DDOList.get(0).getDdoName());
            budgetDdoCode = DDOList.get(0).getCode();
            budgetDdoCode = budgetDdoCode.toUpperCase();
        }
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, obj.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());
            obj.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgetTypeName = BudgetTypeList.get(0).getDescription();
            budgetTypeName = budgetTypeName.substring(0, 3);
            budgetTypeName = budgetTypeName.toUpperCase();

        }
        String serNo = budgetDdoCode + "-" + budgetTypeName + "-" + finYear + "-" + newSlno;
        obj.setSrNo(serNo);
        obj.setExtraProvisionAmount("0");
        obj.setIsExtraProvisioned("false");

        String result = DBManager.getDbConnection().insert(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, new Gson().toJson(obj));
        return result;
    }

    public String getSlNumber(String year, String ddo, String location, String budgetType, CreateIncomeBudget obj) throws Exception {
        HashMap<String, String> duplicateConditionMap = new HashMap<String, String>();
        duplicateConditionMap.put("ddo", obj.getDdo());
        duplicateConditionMap.put("location", obj.getLocation());
        duplicateConditionMap.put("fundType", obj.getFundType());
        duplicateConditionMap.put("sector", obj.getSector());
        duplicateConditionMap.put("financialYear", obj.getFinancialYear());
        duplicateConditionMap.put("department", obj.getDepartment());
        duplicateConditionMap.put("budgetType", obj.getBudgetType());
        duplicateConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, duplicateConditionMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }
        String result = new BudgetIncomeManager().fetchAllBasedOnYear(year, ddo, location, budgetType);
        List<CreateIncomeBudget> loanApplyList = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        int numb = 0;
        CreateIncomeBudget pv = new CreateIncomeBudget();
        if (loanApplyList != null) {
            for (Iterator<CreateIncomeBudget> iterator = loanApplyList.iterator(); iterator.hasNext();) {
                CreateIncomeBudget next = iterator.next();
                String len = next.getSrNo();
                String fY = next.getFinancialYear();

                if (len != null && len != "") {
                    String[] arr = len.split("-");
                    String sNo = arr[3];
                    int value = Integer.parseInt(sNo);

                    if (numb < value) {
                        numb = value;
                    }
                }
            }
        }
        numb++;

        return String.valueOf(numb);
    }

    public String fetchAllIncomHeadWise(String ddo, String location, String finYear, String depart) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        String[] arr = finYear.split("/");
        HashMap<String, CreateIncomeBudget> objmap = new HashMap<String, CreateIncomeBudget>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        conditionMap.put("department", depart);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("financialYear", arr[2].trim());
        //System.out.println("--conditionMap--" + new Gson().toJson(conditionMap));
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        int extraProvisionamount = 0;
        for (CreateIncomeBudget incomeBudget : ledgerList) {
            String budgetHead = incomeBudget.getBudgetHead();
            String finanYear = incomeBudget.getFinancialYear();
            String budgetTtypeName = incomeBudget.getBudgetTypeName();
            String budgetddo = incomeBudget.getDdo();
            String dept = incomeBudget.getDepartment();
            String srno = incomeBudget.getSrNo();
            int amount = Integer.parseInt(incomeBudget.getRequestedAmount());
            if (Integer.parseInt(incomeBudget.getSanctionedAmount()) != 0) {
                amount = Integer.parseInt(incomeBudget.getSanctionedAmount());
                incomeBudget.setRequestedAmount(String.valueOf(amount));
//                if (Integer.parseInt(incomeBudget.getExtraProvisionAmount()) != 0) {
//                    amount = amount + Integer.parseInt(incomeBudget.getExtraProvisionAmount());
//                }

            }

            try {
                extraProvisionamount = Integer.parseInt(incomeBudget.getExtraProvisionAmount());
            } catch (Exception e) {

            }
            // String key = budgetHead + budgetTtypeName + budgetddo + dept;
            String key = budgetTtypeName + budgetddo + dept + finanYear;
            if (objmap.containsKey(key)) {
                CreateIncomeBudget mapObj = (CreateIncomeBudget) objmap.get(key);
                int headWiseAmount = amount + Integer.parseInt(mapObj.getRequestedAmount());
                int headWiseextraAmount = extraProvisionamount + Integer.parseInt(mapObj.getExtraProvisionAmount());
                mapObj.setRequestedAmount(String.valueOf(headWiseAmount));
                mapObj.setExtraProvisionAmount(String.valueOf(headWiseextraAmount));
                objmap.put(key, mapObj);
            } else {
                objmap.put(key, incomeBudget);
            }
        }
        ledgerList.clear();
        ledgerList.addAll(objmap.values());
        try {
            ledgerList = getSector(ledgerList);
        } catch (Exception e) {
        }
        try {
            ledgerList = getBudgetTypeForIncome(ledgerList);
        } catch (Exception e) {
        }
        try {
            ledgerList = getFinancialYearForIncome(ledgerList);
        } catch (Exception e) {
        }

        String finalresult = new Gson().toJson(ledgerList);
        return finalresult;
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, CreateIncomeBudget> objmap = new HashMap<String, CreateIncomeBudget>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        //conditionMap.put("sentStatus", ApplicationConstants.FALSE);
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        try {
            ledgerList = getSector(ledgerList);
        } catch (Exception e) {
        }
        try {
            ledgerList = getBudgetTypeForIncome(ledgerList);
        } catch (Exception e) {
        }
        try {
            ledgerList = getFinancialYearForIncome(ledgerList);
        } catch (Exception e) {
        }

        String finalresult = new Gson().toJson(ledgerList);
        return finalresult;
    }

    public String fetchAllBasedOnYear(String year, String ddo, String location, String budgetType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, CreateIncomeBudget> objmap = new HashMap<String, CreateIncomeBudget>();
        conditionMap.put("financialYear", year);
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        conditionMap.put("budgetType", budgetType);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        try {
            ledgerList = getSector(ledgerList);
        } catch (Exception e) {
        }
        try {
            ledgerList = getBudgetTypeForIncome(ledgerList);
        } catch (Exception e) {
        }
        try {
            ledgerList = getFinancialYearForIncome(ledgerList);
        } catch (Exception e) {
        }

        String finalresult = new Gson().toJson(ledgerList);
        return finalresult;
    }

    public boolean delete(String id) throws Exception {
        boolean result = false;
        int count = 0;
//        JSONObject obj = new JSONObject(id);
//        JSONArray array = obj.getJSONArray("id");
//        for (int i = 0; i < array.length(); i++) {
        Type type = new TypeToken<CreateIncomeBudget>() {
        }.getType();
        String assStr = new BudgetIncomeManager().Fetch(id);
        if (assStr == null || assStr.isEmpty()) {
            return false;
        }
        CreateIncomeBudget assJson = new Gson().fromJson(assStr, type);
        HashMap<String, String> conditionMap2 = new HashMap<String, String>();

        conditionMap2.put("ddo", assJson.getDdo());
        conditionMap2.put("fundType", assJson.getFundType());
//        conditionMap2.put("budgetHead", assJson.getBudgetHead());
        conditionMap2.put("budgetTypeName", assJson.getBudgetTypeName());
        conditionMap2.put("location", assJson.getLocation());
        conditionMap2.put("financialYear", assJson.getFinancialYear());
        conditionMap2.put("sector", assJson.getSector());
        conditionMap2.put("srNo", assJson.getSrNo());
        conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap2);
        //System.out.println("--budgetJson--" + budgetJson1);
        if (budgetJson1 != null) {
            List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson1, new TypeToken<List<CreateIncomeBudget>>() {
            }.getType());
            for (CreateIncomeBudget createIncomeBudget : ledgerList) {
                String objId = ((LinkedTreeMap<String, String>) createIncomeBudget.getId()).get("$oid");
                createIncomeBudget.setStatus(ApplicationConstants.DELETE);
                result = DBManager.getDbConnection().update(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, objId, new Gson().toJson(createIncomeBudget));
            }
        }
        if (result) {
            count++;
        }
        //}
        return result;
    }

    public boolean update(String id, String object) throws Exception {
        String categoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, id);
        List<CreateIncomeBudget> sectorlist = new Gson().fromJson(categoryJson, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        CreateIncomeBudget sectorobj = sectorlist.get(0);
        sectorobj.setRequestedAmount(object);
        sectorobj.setCreateDate(sectorobj.getCreateDate());
        sectorobj.setStatus(ApplicationConstants.ACTIVE);
        sectorobj.setUpdateDate(System.currentTimeMillis() + "");
        String sectorjson = new Gson().toJson(sectorobj);
        boolean status = DBManager.getDbConnection().update(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, id, sectorjson);
        return status;
    }

    public boolean Send(String id) throws Exception {
        boolean result = false;
        int count = 0;

        JSONObject obj = new JSONObject(id);
        //System.out.println(obj);
        JSONArray array = obj.getJSONArray("id");

        for (int i = 0; i < array.length(); i++) {

            Type type = new TypeToken<CreateIncomeBudget>() {
            }.getType();
            String assStr = new BudgetIncomeManager().Fetch((String) array.get(i));

            //System.out.println("in the search criteria" + assStr);
            if (assStr == null || assStr.isEmpty()) {
                return false;
            }

            CreateIncomeBudget assJson = new Gson().fromJson(assStr, type);
            HashMap<String, String> conditionMap2 = new HashMap<String, String>();

            conditionMap2.put("ddo", assJson.getDdo());
            conditionMap2.put("fundType", assJson.getFundType());
            //  conditionMap2.put("budgetHead", assJson.getBudgetHead());
            conditionMap2.put("budgetTypeName", assJson.getBudgetTypeName());
            //     conditionMap2.put("location", assJson.getLocation());
            conditionMap2.put("financialYear", assJson.getFinancialYear());
            conditionMap2.put("department", assJson.getDepartment());
            conditionMap2.put("sector", assJson.getSector());
            conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap2);
            //System.out.println("--budgetJson--" + budgetJson1);
            if (budgetJson1 != null) {
                List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson1, new TypeToken<List<CreateIncomeBudget>>() {
                }.getType());
                for (CreateIncomeBudget createIncomeBudget : ledgerList) {
                    String objId = ((LinkedTreeMap<String, String>) createIncomeBudget.getId()).get("$oid");
                    createIncomeBudget.setSentStatus("true");
                    result = DBManager.getDbConnection().update(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, objId, new Gson().toJson(createIncomeBudget));
                }
            }

            if (result) {
                count++;
            }

        }
        return result;
    }

    public String Fetch(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return null;
        }
        String idValue = id.toString().trim();
        //System.out.println("idValue -------------- " + idValue);
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, idValue);
        List<CreateIncomeBudget> gisList = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));

    }

    public String FetchAllCheckedRow(String id) throws Exception {
        String result = null;
        int count = 0;

        JSONObject obj = new JSONObject(id);
        //System.out.println(obj);
        JSONArray array = obj.getJSONArray("id");
        List<CreateIncomeBudget> gisList = new Gson().fromJson(result, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        for (int i = 0; i < array.length(); i++) {

            Type type = new TypeToken<CreateIncomeBudget>() {
            }.getType();
            result = new BudgetIncomeManager().Fetch((String) array.get(i));

            //System.out.println("in the search criteria" + result);
            if (result == null || result.isEmpty()) {
                return null;
            }

        }

        return new Gson().toJson(gisList.get(0));
        //  return null;
    }

    public List<CreateIncomeBudget> searchItFinally(CreateIncomeBudget emp) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE);

        BasicDBObject regexQuery = new BasicDBObject();

        if (emp.getFinancialYear() != null) {
            regexQuery.put("financialYear",
                    new BasicDBObject("$regex", emp.getFinancialYear()));
        }
        if (emp.getBudgetType() != null) {
            regexQuery.put("budgetType",
                    new BasicDBObject("$regex", emp.getBudgetType()));
        }
        if (emp.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", emp.getFundType()));
        }
        if (emp.getSector() != null) {
            regexQuery.put("sector",
                    new BasicDBObject("$regex", emp.getSector()));
        }

        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        //System.out.println(regexQuery);

        DBCursor cursor2 = collection.find(regexQuery);

        List<CreateIncomeBudget> employeeList = new ArrayList<CreateIncomeBudget>();
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<CreateIncomeBudget>() {
            }.getType();
            CreateIncomeBudget em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }
        //System.out.println(employeeList);
        return employeeList;
    }

    public static void main(String[] args) throws Exception {
        //  String result = new BudgetIncomeManager().fetchAllIncomHeadWise("582f34960c92ec57796a1e13", "582f34a70c92ec57796a1e17", "01/09/2017");
        //System.out.println(result);
//        String result1 = new BudgetIncomeManager().fetchAll();
//        //System.out.println(result);
    }

    public String getLedgersBasedOnHeads(String fundType, String budgetHead, String budgetType, String location, String finacialYear, String ddo, String dept) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, String> conditionMap2 = new HashMap<String, String>();
        int finYear = Integer.parseInt(finacialYear) - 1;
        String prevFinYear = Integer.toString(finYear);
        conditionMap.put("FundType", fundType);
        conditionMap.put("budgetType", "Yes");
        if (budgetHead != "" && budgetHead != null) {
            conditionMap.put("budgetHeadCode", budgetHead);
        }
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgettypeName = "";
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_TABLE, conditionMap);
        List<Ledger> ddoList = new Gson().fromJson(result, new TypeToken<List<Ledger>>() {
        }.getType());
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, budgetType);
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());
            budgettypeName = BudgetTypeList.get(0).getDescription();
        }
        List<Ledger> headlist = new ArrayList<Ledger>();
        String prrviousBudgetCon = "notRequired";
// condition actual previous budget
        if (budgettypeName.equals("Estimated") || budgettypeName.equals("Revised")) {
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("ddo", ddo);
            map.put("location", location);
            map.put("financialYear", prevFinYear);
            map.put("fundType", fundType);
            map.put("budgetNature", "Income");
//            map.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
            map.put("department", dept);
            map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

            String prviousActualbudget = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map);
            if (prviousActualbudget != null) {
                prrviousBudgetCon = "Required";
            }
        }
        if (ddoList != null) {
            conditionMap.clear();
            for (Ledger ledger : ddoList) {
                String revStatus = "required";
                conditionMap2.put("ddo", ddo);
                conditionMap2.put("fundType", fundType);
                conditionMap2.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                conditionMap2.put("budgetTypeName", budgettypeName);
                conditionMap2.put("location", location);
                conditionMap2.put("department", dept);
                conditionMap2.put("financialYear", finacialYear);
                conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap2);
                if (budgetJson1 != null) {
                    continue;
                }
                if (budgettypeName.equals("Revised")) {
                    conditionMap.put("ddo", ddo);
                    conditionMap.put("fundType", fundType);
                    if (budgetHead != "" && budgetHead != null) {
                        conditionMap.put("budgetHead", budgetHead);
                    }
                    conditionMap.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                    conditionMap.put("budgetTypeName", "Estimated");
                    conditionMap.put("location", location);
                    conditionMap.put("financialYear", finacialYear);
                    conditionMap.put("department", dept);
                    conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    //System.out.println("--conditionMap--" + conditionMap);
                    String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
                    //System.out.println("--budgetJson--" + budgetJson);
                    if (budgetJson != null) {
                        List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateIncomeBudget>>() {
                        }.getType());
                        String actualamount = "0";
                        //  int reqAmountint = Integer.parseInt(ledgerList.get(0).getSanctionedAmount()) + Integer.parseInt(ledgerList.get(0).getExtraProvisionAmount());

//                        String reqAmount = Integer.toString(reqAmountint);
                        String reqAmount = ledgerList.get(0).getRequestedAmount();
                        if (actualamount == null) {
                            actualamount = "0";
                        }
                        if (reqAmount.equalsIgnoreCase("0")) {
                            revStatus = "notRequired";
                        }

                        ledger.setPrevReqAmount(actualamount);
                        ledger.setRequestAmount("0");
//                        prrviousBudgetCon = "notRequired";
                    } else {
                        continue;
                    }
                } else if (budgettypeName.equals("Actual")) {
                    conditionMap.put("fundType", fundType);
                    if (budgetHead != "" && budgetHead != null) {
                        conditionMap.put("budgetHead", budgetHead);
                    }
                    conditionMap.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                    conditionMap.put("budgetTypeName", "Revised");
                    conditionMap.put("location", location);
                    conditionMap.put("financialYear", finacialYear);
                    conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    //System.out.println("--conditionMap--" + conditionMap);
                    String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
                    //System.out.println("--budgetJson--" + budgetJson);
                    if (budgetJson != null) {
                        List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateIncomeBudget>>() {
                        }.getType());
                        String requestedAmount = ledgerList.get(0).getRequestedAmount();
                        String requestAmount = ledgerList.get(0).getRequestedAmount();
                        if (requestedAmount == null) {
                            requestedAmount = "0";
                        }
                        ledger.setRequestAmount(requestAmount);
                        ledger.setPrevReqAmount(requestedAmount);
                    } else {
                        continue;
                    }
                }
                String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, ledger.getBudgetHeadCode());
                if (result1 != null) {
                    List<BudgetHeadMaster> gisList = new Gson().fromJson(result1, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    ledger.setBudgetHeadName(gisList.get(0).getBudgetHead());
                }
                //get actual amount
                if (prrviousBudgetCon.equalsIgnoreCase("Required")) {
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("ddo", ddo);
                    map.put("location", location);
                    map.put("financialYear", prevFinYear);
                    map.put("fundType", fundType);
                    map.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                    map.put("department", dept);
                    map.put("budgetNature", "Income");
                    map.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

                    String prviousActualbudget = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.PREVIOUS_BUDGET_AMOUNT_DETAILS, map);
                    if (prviousActualbudget != null) {
                        List<PreviousBudgetAmountDetails> ledgerList = new Gson().fromJson(prviousActualbudget, new TypeToken<List<PreviousBudgetAmountDetails>>() {
                        }.getType());
                        String actualAmt = ledgerList.get(0).getActualAmount();
                        ledger.setPrevReqAmount(actualAmt);
                    }
                }
                HashMap<String, String> conditionMap1 = new HashMap<String, String>();
                conditionMap1.put("parentLedger", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                conditionMap1.put("ledgerCategory", "Income");
                conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                String budgetJson11 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.LEDGER_CATEGORY_TABLE, conditionMap1);
                if (budgetJson11 != null && revStatus.equalsIgnoreCase("required")) {
                    headlist.add(ledger);
                }
            }
            if (headlist.size() == 0) {
                return new Gson().toJson(ApplicationConstants.NO_DATA_FOUND);
            } else {
                return new Gson().toJson(headlist);
            }

        }
        return new Gson().toJson(ApplicationConstants.NO_DATA_FOUND);
    }

    public String getLedgersForEditBasedOnHeads(String fundType, String budgetHead, String budgetType, String location, String finacialYear, String ddo, String department, String srno) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, String> conditionMap2 = new HashMap<String, String>();
        String budgettypeName = "";
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, budgetType);
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());
            budgettypeName = BudgetTypeList.get(0).getDescription();
        }
        List<Ledger> headlist = new ArrayList<Ledger>();
        conditionMap.clear();
        conditionMap2.put("ddo", ddo);
        conditionMap2.put("fundType", fundType);
//        if (budgetHead != "" && budgetHead != null) {
//            conditionMap2.put("budgetHead", budgetHead);
//        }
        conditionMap2.put("budgetTypeName", budgettypeName);
        conditionMap2.put("location", location);
        conditionMap2.put("financialYear", finacialYear);
        conditionMap2.put("budgetType", budgetType);
        conditionMap2.put("department", department);
        conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap2);
        if (budgetJson1 != null) {
            List<CreateIncomeBudget> ledgerList = new Gson().fromJson(budgetJson1, new TypeToken<List<CreateIncomeBudget>>() {
            }.getType());
            Collections.sort(ledgerList, new SortByLedgers());
            return new Gson().toJson(ledgerList);
        }
        return new Gson().toJson(ApplicationConstants.NO_DATA_FOUND);
    }

    public String getsrNos(CreateIncomeBudget createIncomeBudgetObj) throws Exception {
        String finYear = createIncomeBudgetObj.getFinancialYear();
        String budgetType = createIncomeBudgetObj.getBudgetType();
        String ddo = createIncomeBudgetObj.getDdo();
        String location = createIncomeBudgetObj.getLocation();
        String fundType = createIncomeBudgetObj.getFundType();
        String sector = createIncomeBudgetObj.getSector();
        String department = createIncomeBudgetObj.getDepartment();
        String budgetTypeName = "";
        if (budgetType != null) {
            String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, budgetType);
            if (budgetTypeJson != null) {
                List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
                }.getType());
                budgetTypeName = BudgetTypeList.get(0).getDescription();
            }
        }
        if (budgetTypeName.equals("Revised")) {
            HashMap<String, String> conditionMap = new HashMap<String, String>();
            HashMap<String, CreateIncomeBudget> map = new HashMap<String, CreateIncomeBudget>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("location", location);
            conditionMap.put("ddo", ddo);
            conditionMap.put("financialYear", finYear);
            conditionMap.put("sector", sector);
            if (department != "" && department != null) {
                conditionMap.put("department", department);
            }
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
            //System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<CreateIncomeBudget> incomeBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateIncomeBudget>>() {
                }.getType());
                for (CreateIncomeBudget income : incomeBudgetList) {
                    map.put(income.getSrNo(), income);
                }
                incomeBudgetList.clear();
                incomeBudgetList.addAll(map.values());
                return new Gson().toJson(incomeBudgetList);
            }
        }
        return "";
    }

    public String getDataExtraProvision(CreateIncomeBudget obj) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("isSanctioned", "true");
        conditionMap.put("isExtraProvisioned", "false");
        if (obj.getBudgetType().isEmpty() == false) {

            conditionMap.put("budgetType", obj.getBudgetType());
        }

        if (obj.getFundType().isEmpty() == false) {
            conditionMap.put("fundType", obj.getFundType());
        }
        if (obj.getSector().isEmpty() == false) {
            conditionMap.put("sector", obj.getSector());
        }

        if (obj.getFinancialYear().isEmpty() == false) {
            conditionMap.put("financialYear", obj.getFinancialYear());
        }
        if (obj.getBudgetHead().isEmpty() == false) {
            conditionMap.put("budgetHead", obj.getBudgetHead());
        }
        if (obj.getDepartment().isEmpty() == false) {
            conditionMap.put("department", obj.getDepartment());
        }
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CREATE_INCOME_BUDGET_TABLE, conditionMap);
        List<CreateIncomeBudget> list = new Gson().fromJson(result1, new TypeToken<List<CreateIncomeBudget>>() {
        }.getType());
        List<CreateIncomeBudget> finallist = new ArrayList<CreateIncomeBudget>();
        for (CreateIncomeBudget incomeBudget : list) {
            HashMap<String, String> conditionMap1 = new HashMap<String, String>();
            conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap1.put("incomeBudgetId", ((LinkedTreeMap<String, String>) incomeBudget.getId()).get("$oid"));
            String resultextra = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EXTRA_PROVISION_INCOME, conditionMap1);
            List<CreateIncomeBudget> listExtra = new Gson().fromJson(resultextra, new TypeToken<List<CreateIncomeBudget>>() {
            }.getType());
            if (listExtra == null || listExtra.size() < 1) {
                finallist.add(incomeBudget);
            }
        }
        return new Gson().toJson(finallist);

    }
}
