/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.ConsolidateDepartmentIncome;
import com.accure.budget.dto.SanctionUniversityIncomeBudget;
import com.accure.budget.dto.ConsolidateIncomeBudget;
import com.accure.budget.dto.CreateIncomeBudget;
import com.accure.budget.dto.HeadwiseIncomeBudget;
import static com.accure.budget.manager.ConsolidateIncomeBudgetManager.SUBMIT;
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
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author user
 */
public class SearchBudgetHeadManager {

    public String search(HeadwiseIncomeBudget emp) throws Exception {

        PropertiesConfiguration config = getConfig();
        BasicDBObject regexQuery = new BasicDBObject();
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER);

        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }
        if (emp.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));
        }
        if (emp.getBudgetType() != null) {
            regexQuery.put("budgetType",
                    new BasicDBObject("$regex", emp.getBudgetType()));
        }
        if (emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", emp.getBudgetHead()));
        }
        if (emp.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", emp.getFundType()));
        }
        if (emp.getSector() != null) {
            regexQuery.put("sector",
                    new BasicDBObject("$regex", emp.getSector()));

        }
        if (emp.getFinancialYear() != null) {
            regexQuery.put("financialYear",
                    new BasicDBObject("$regex", emp.getFinancialYear()));

        }

        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));
        if (emp.getConsolidateBudgetStatus().isEmpty() == false) {
            regexQuery.put("consolidateBudgetStatus",
                    new BasicDBObject("$regex", emp.getConsolidateBudgetStatus()));

        }
        //Fetching Data from Income Budget
        DBCursor cursor2 = collection.find(regexQuery);

        List<HeadwiseIncomeBudget> finalList = new ArrayList<HeadwiseIncomeBudget>();

        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<HeadwiseIncomeBudget>() {
            }.getType();
            HeadwiseIncomeBudget em = new Gson().fromJson(ob.toString(), type);
            finalList.add(em);
        }

        return new Gson().toJson(finalList);

    }

    public List<CreateIncomeBudget> checkDdoValid(List<CreateIncomeBudget> sanctionList, Map<String, CreateIncomeBudget> map) throws Exception {
        //List sanctionList

        Map<String, String> PFTypeMap = new HashMap<String, String>();
        try {
            String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER);
            List<HeadwiseIncomeBudget> religionList = new Gson().fromJson(result, new TypeToken<List<HeadwiseIncomeBudget>>() {
            }.getType());
            if (religionList.size() > 0) {
                for (Iterator<HeadwiseIncomeBudget> iterator = religionList.iterator(); iterator.hasNext();) {
                    HeadwiseIncomeBudget next = iterator.next();
                    PFTypeMap.put(next.getBudgetIncomeSanctionId(), ((LinkedTreeMap<String, String>) next.getId()).get("$oid"));
                }
            }
        } catch (Exception e) {
        }
        //System.out.println("HEADWISE_INCOME_BUDGET_MASTER");
        List<CreateIncomeBudget> listNew = new ArrayList<CreateIncomeBudget>();
        int count = 0;
        if (PFTypeMap.size() > 0) {
            //System.out.println("---------searchlist---------" + PFTypeMap);
            //Previous
            for (Map.Entry<String, CreateIncomeBudget> map1 : map.entrySet()) {
                count = 0;
                //Present
                for (Map.Entry<String, String> entry : PFTypeMap.entrySet()) {

                    // String value1=(String) sanctionList.get(i).getId();
                    if (entry.getKey().equals(map1.getKey())) {
                        //System.out.println("Inside ");
                        count++;
                    }
                }
                //System.out.println(count);
                if (count == 0) {
                    //System.out.println("****************************");
                    //System.out.println(map1.getValue());
                    listNew.add(map1.getValue());
                }
            }
        }
        if (PFTypeMap.size() < 1) {
            //System.out.println("Size less than 1");
            return sanctionList;
        }
        return listNew;
    }

    public String save(HeadwiseIncomeBudget next, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        next.setCreateDate(System.currentTimeMillis() + "");
        next.setStatus(ApplicationConstants.ACTIVE);
        next.setCreatedBy(userName);
        String finyear = next.getFinancialYear();

        String headwiseIncomeJson = new Gson().toJson(next);
        String Id = DBManager.getDbConnection().insert(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER, headwiseIncomeJson);
        if (Id != null) {
            return Id;
        }
        return null;
    }

    public boolean SubmitData(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }

        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<HeadwiseIncomeBudget>() {
        }.getType();
        String locationwiseBudget = new SearchBudgetHeadManager().fetch(Id);
        if (locationwiseBudget == null || locationwiseBudget.isEmpty()) {
            return false;
        }
        HeadwiseIncomeBudget locationwiseBudgetJson = new Gson().fromJson(locationwiseBudget, type);
        List<String> li = null;
        String id = locationwiseBudgetJson.getConsolidatedIncomeId();
        String approvedAmount = locationwiseBudgetJson.getApprovedAmount();
        String sanctionAmount = locationwiseBudgetJson.getSanctionAmount();
        int appdAmount = Integer.parseInt(approvedAmount);
        int sanctnAmount = Integer.parseInt(sanctionAmount);
        int remAppvdAmount = appdAmount - sanctnAmount;
        String revisedSnctionedAmount = String.valueOf(remAppvdAmount);
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, id);
        if (!result.isEmpty()) {
            List<ConsolidateIncomeBudget> list = new Gson().fromJson(result, new TypeToken<List<ConsolidateIncomeBudget>>() {
            }.getType());
            ConsolidateIncomeBudget em = list.get(0);
            li = em.getIncomeBudgetIdList();
            em.setRevisedSanctionedAmount(revisedSnctionedAmount);
            String conIncBudgetJson = new Gson().toJson(em);
            boolean Id1 = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, id, conIncBudgetJson);
            //System.out.println("Id1" + Id1);
        }
        if (li != null) {
            for (int i = 0; i < li.size(); i++) {
                String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, li.get(i));
                if (!result1.isEmpty()) {
                    List<ConsolidateDepartmentIncome> list1 = new Gson().fromJson(result1, new TypeToken<List<ConsolidateDepartmentIncome>>() {
                    }.getType());
                    ConsolidateDepartmentIncome em1 = list1.get(0);
                    em1.setSanctionedAmount(Integer.toString(sanctnAmount));
                    em1.setRevisedSanctionedAmount(Integer.toString(sanctnAmount));
                    em1.setIsSanctioned(ApplicationConstants.IS_SANCTIONED_TRUE);
                    String IncBudgetJson1 = new Gson().toJson(em1);
                    boolean Id2 = DBManager.getDbConnection().update(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, li.get(i), IncBudgetJson1);
                    //System.out.println("Id1" + Id2);

                }
            }
        }

        locationwiseBudgetJson.setConsolidateBudgetStatus(SUBMIT);
        locationwiseBudgetJson.setUpdateDate(System.currentTimeMillis() + "");

        locationwiseBudgetJson.setUpdatedBy(userName);
        boolean finalresult = DBManager.getDbConnection().update(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER, Id, new Gson().toJson(locationwiseBudgetJson));
        return finalresult;
    }

    private List<SanctionUniversityIncomeBudget> checkDdoValid1(List<SanctionUniversityIncomeBudget> sanctionList, Map<String, SanctionUniversityIncomeBudget> map) throws Exception {
        //List sanctionList

        Map<String, HeadwiseIncomeBudget> pesentMap = new HashMap<String, HeadwiseIncomeBudget>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER);
        List<HeadwiseIncomeBudget> religionList = new Gson().fromJson(result, new TypeToken<List<HeadwiseIncomeBudget>>() {
        }.getType());
        //Urs
        if (religionList.size() > 0) {
            for (Iterator<HeadwiseIncomeBudget> iterator = religionList.iterator(); iterator.hasNext();) {
                HeadwiseIncomeBudget next = iterator.next();
                pesentMap.put(next.getBudgetIncomeSanctionId(), next);
            }
        }

        //Removing Duplicates
        for (Iterator<SanctionUniversityIncomeBudget> iterator = sanctionList.iterator(); iterator.hasNext();) {
            SanctionUniversityIncomeBudget next = iterator.next();
            //System.out.println(next.getId());
            //System.out.println(pesentMap.get(next.getId()));
            HeadwiseIncomeBudget ob1 = pesentMap.get("57a4784ed04d0ea731732239");
            //System.out.println(new Gson().toJson(ob1));
            HeadwiseIncomeBudget ob = pesentMap.get(next.getId());
            if (ob != null) {
                //System.out.println("in if condotion");
                iterator.remove();
            }
            //System.out.println("********");

        }
        return sanctionList;

    }

    public String searchBudgetHeads(CreateIncomeBudget dtoData) throws Exception {

        String result = "no";
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", dtoData.getDdo());
        conditionMap.put("fundType", dtoData.getFundType());
        conditionMap.put("financialYear", dtoData.getFinancialYear());
        if (!dtoData.getBudgetHead().isEmpty() && dtoData.getBudgetHead() != "" && dtoData.getBudgetHead() != null) {
            conditionMap.put("budgetHead", dtoData.getBudgetHead());
        }
        conditionMap.put("sector", dtoData.getSector());
        conditionMap.put("location", dtoData.getLocation());
        conditionMap.put("isSanctioned", "false");
        try {
            result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_DEPT_INCOME, conditionMap);
        } catch (Exception e) {

        }
        List<ConsolidateDepartmentIncome> ddoList = new Gson().fromJson(result, new TypeToken<List<ConsolidateDepartmentIncome>>() {
        }.getType());
        List<String> listId = new ArrayList<String>();
        List<String> ledgerIds = new ArrayList<String>();
        List<ConsolidateIncomeBudget> finallist = new ArrayList<ConsolidateIncomeBudget>();
        for (ConsolidateDepartmentIncome cl : ddoList) {

            HashMap<String, String> conexpMap = new HashMap<String, String>();
            conexpMap.put("ledgerId", cl.getLedgerId());
            conexpMap.put("status", ApplicationConstants.ACTIVE);
            String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.CONSOLIDATE_INCOME_BUDGET, conexpMap);

            List<ConsolidateIncomeBudget> conexpbudlist = new Gson().fromJson(result1, new TypeToken<List<ConsolidateIncomeBudget>>() {
            }.getType());
            String id = ((LinkedTreeMap<String, String>) cl.getId()).get("$oid");
            for (ConsolidateIncomeBudget gal1 : conexpbudlist) {
                List<String> li = gal1.getIncomeBudgetIdList();
                if (li.contains(id) && !listId.contains(id)) {
                    try {
                        gal1.setDepartmentName(cl.getDepartmentName());
                        finallist.add(gal1);
                        listId.addAll(li);
                        ledgerIds.add(cl.getLedgerId());

                    } catch (Exception e) {
                    }
                    break;
                }

            }
        }

        return new Gson().toJson(finallist);
    }

    public String fetch(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER, Id);
        List<HeadwiseIncomeBudget> HeadwiseIncomeBudgetList = new Gson().fromJson(result, new TypeToken<List<HeadwiseIncomeBudget>>() {
        }.getType());
        if (HeadwiseIncomeBudgetList == null || HeadwiseIncomeBudgetList.size() < 1) {
            return null;
        }
        return new Gson().toJson(HeadwiseIncomeBudgetList.get(0));
    }

    public boolean update(HeadwiseIncomeBudget headwiseIncome, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        headwiseIncome.setUpdateDate(System.currentTimeMillis() + "");
        headwiseIncome.setStatus(ApplicationConstants.ACTIVE);
        headwiseIncome.setUpdatedBy(userName);
        String locationwiseJson = new Gson().toJson(headwiseIncome);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER, Id, locationwiseJson);
        return result;
    }

    public boolean delete(String Id, String loginUserId) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();

        Type type = new TypeToken<HeadwiseIncomeBudget>() {
        }.getType();
        String locationwiseBudget = new SearchBudgetHeadManager().fetch(Id);
        if (locationwiseBudget == null || locationwiseBudget.isEmpty()) {
            return false;
        }
        HeadwiseIncomeBudget locationwiseBudgetJson = new Gson().fromJson(locationwiseBudget, type);
        locationwiseBudgetJson.setStatus(ApplicationConstants.INACTIVE);
        locationwiseBudgetJson.setConsolidateBudgetStatus(ApplicationConstants.DELETED);
        locationwiseBudgetJson.setUpdatedBy(userName);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.HEADWISE_INCOME_BUDGET_MASTER, Id, new Gson().toJson(locationwiseBudgetJson));
        return result;
    }
}
