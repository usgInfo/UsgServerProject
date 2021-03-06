/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.BudgetSector;
import com.accure.budget.dto.BudgetType;
import com.accure.budget.dto.CreateBudgetExpense;
import com.accure.budget.dto.FinancialYear;
import com.accure.budget.dto.PreviousBudgetAmountDetails;
import com.accure.common.duplicate.Duplicate;
import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Ledger;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class BudgetExpenseManager {

    public String getLedgersBasedOnHeads(String fundType, String budgetHead, String budgetType, String location, String finacialYear, String ddo, String department) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, String> conditionMap2 = new HashMap<String, String>();
        int finYear = Integer.parseInt(finacialYear) - 1;
        String prevFinYear = Integer.toString(finYear);
        conditionMap.put("FundType", fundType);
        if (budgetHead != "" && budgetHead != null) {
            conditionMap.put("budgetHeadCode", budgetHead);
        }
        conditionMap.put("budgetType", "Yes");
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
        if (budgettypeName.equals("Estimated") || budgettypeName.equals("Revised")) {
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("ddo", ddo);
            map.put("location", location);
            map.put("financialYear", prevFinYear);
            map.put("fundType", fundType);
//            map.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
            map.put("department", department);
            map.put("budgetNature", "Expense");
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
//                        if(budgettypeName.equals("Estimated"))
//                        {
                conditionMap2.put("ddo", ddo);
                conditionMap2.put("fundType", fundType);
                conditionMap2.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                conditionMap2.put("budgetTypeName", budgettypeName);
                conditionMap2.put("location", location);
                conditionMap2.put("financialYear", finacialYear);
                conditionMap2.put("department", department);
                conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap2);
                ////System.out.println("--budgetJson--" + budgetJson1);
                if (budgetJson1 != null) {
                    continue;
                }
//                        }
                if (budgettypeName.equals("Revised")) {
                    conditionMap.put("ddo", ddo);
                    conditionMap.put("fundType", fundType);
                    if (budgetHead != "" && budgetHead != null) {
                        conditionMap.put("budgetHead", budgetHead);
                    }
                    conditionMap.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                    conditionMap.put("budgetTypeName", "Estimated");
                    conditionMap.put("location", location);
                    conditionMap.put("department", department);
                    conditionMap.put("financialYear", finacialYear);
//                    BasicDBObject reqCon = new BasicDBObject();
//                    reqCon.put("$ne", 0);
//                    conditionMap.put("requestedAmount", reqCon);
                    conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
                    ////System.out.println("--conditionMap--" + conditionMap);
                    String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
                    ////System.out.println("--budgetJson--" + budgetJson);
                    if (budgetJson != null) {
                        List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateBudgetExpense>>() {
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
                    ////System.out.println("--conditionMap--" + conditionMap);
                    String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
                    ////System.out.println("--budgetJson--" + budgetJson);
                    if (budgetJson != null) {
                        List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateBudgetExpense>>() {
                        }.getType());
                        String requestedAmount = ledgerList.get(0).getRequestedAmount();
                        String requestAmount = ledgerList.get(0).getRequestedAmount();
                        if (requestedAmount == null) {
                            requestedAmount = "0";
                        }
                        ledger.setPrevReqAmount(requestedAmount);
                        ledger.setRequestAmount(requestAmount);
                    } else {
                        continue;
                    }
                }
                if (ledger.getBudgetHeadCode() != null) {
                    String result1 = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, ledger.getBudgetHeadCode());
                    if (result1 != null) {
                        List<BudgetHeadMaster> gisList = new Gson().fromJson(result1, new TypeToken<List<BudgetHeadMaster>>() {
                        }.getType());
                        ledger.setBudgetHeadName(gisList.get(0).getBudgetHead());
                    }
                }
                //get actual amount
                if (prrviousBudgetCon.equalsIgnoreCase("Required")) {
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("ddo", ddo);
                    map.put("location", location);
                    map.put("financialYear", prevFinYear);
                    map.put("fundType", fundType);
                    map.put("ledgerId", ((LinkedTreeMap<String, String>) ledger.getId()).get("$oid"));
                    map.put("department", department);
                    map.put("budgetNature", "Expense");
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
                conditionMap1.put("ledgerCategory", "Expense");
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

    public String save(CreateBudgetExpense expbudgetDTO, String newSlno) throws Exception {
        String budgetTypeName = "";
        String budgetDdoCode = "";
        expbudgetDTO.setCreateDate(System.currentTimeMillis() + "");
        expbudgetDTO.setUpdateDate(System.currentTimeMillis() + "");
        expbudgetDTO.setStatus(ApplicationConstants.ACTIVE);
        expbudgetDTO.setIsConsolidate(ApplicationConstants.FALSE);
        expbudgetDTO.setIsSanctioned(ApplicationConstants.FALSE);
        expbudgetDTO.setSentStatus(ApplicationConstants.FALSE);
        String ddo = expbudgetDTO.getDdo();
        ddo = ddo.substring(3);
        String budgetType = expbudgetDTO.getBudgetType();
        String finYear = expbudgetDTO.getFinancialYear();
        String result2 = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, expbudgetDTO.getDdo());
        List<DDO> ddoList = new Gson().fromJson(result2, new TypeToken<List<DDO>>() {
        }.getType());
        if (result2 != null) {
            List<DDO> DDOList = new Gson().fromJson(result2, new TypeToken<List<DDO>>() {
            }.getType());
            expbudgetDTO.setDdoName(DDOList.get(0).getDdoName());
            budgetDdoCode = DDOList.get(0).getCode();
            budgetDdoCode = budgetDdoCode.toUpperCase();
        }
        String budgetTypeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_TYPE_TABLE, expbudgetDTO.getBudgetType());
        if (budgetTypeJson != null) {
            List<BudgetType> BudgetTypeList = new Gson().fromJson(budgetTypeJson, new TypeToken<List<BudgetType>>() {
            }.getType());
            expbudgetDTO.setBudgetTypeName(BudgetTypeList.get(0).getDescription());
            budgetTypeName = BudgetTypeList.get(0).getDescription();
            budgetTypeName = budgetTypeName.substring(0, 3);
            budgetTypeName = budgetTypeName.toUpperCase();

        }
        String serNo = budgetDdoCode + "-" + budgetTypeName + "-" + finYear + "-" + newSlno;
        expbudgetDTO.setSrNo(serNo);
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("year", finYear.trim());
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        ////System.out.println(conditionMap.toString());
        String currentYearJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, conditionMap);
        if (currentYearJson == null) {

        } else {
            List<com.accure.hrms.dto.FinancialYear> fyList = new Gson().fromJson(currentYearJson, new TypeToken<List<com.accure.hrms.dto.FinancialYear>>() {
            }.getType());
            com.accure.hrms.dto.FinancialYear fyObj = fyList.get(0);
            String fyId = ((LinkedTreeMap<String, String>) fyObj.getId()).get("$oid");
            ////System.out.println("----------------fyId-------" + fyId);
            expbudgetDTO.setFinancialYearId(fyId);
        }
        expbudgetDTO.setAppropriationValue("0");
        expbudgetDTO.setExtraProvisionAmount("0");
        expbudgetDTO.setSanctionedAmount("0");
        expbudgetDTO.setIsExtraProvisioned("false");
        String expbudgetJson = new Gson().toJson(expbudgetDTO);
        String expbudgetJsonresult = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_EXPENSE_TABLE, expbudgetJson);
        return expbudgetJsonresult;
    }

    public String fetchPreviousAmount(String year) throws Exception {

        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, year);
        List<FinancialYear> empList = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        List<FinancialYear> employeeList = new ArrayList<FinancialYear>();
        employeeList.add(empList.get(0));

        for (FinancialYear fy : employeeList) {
            String fr = fy.getYear();
        }
        return new Gson().toJson(employeeList);
    }

    public String fetchAll() throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateBudgetExpense>>() {
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

    public String fetchAllIncomHeadWise(String ddo, String location, String finYear, String depart) throws Exception {
        String[] arr = finYear.split("/");
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        HashMap<String, CreateBudgetExpense> objmap = new HashMap<String, CreateBudgetExpense>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        conditionMap.put("department", depart);
        conditionMap.put("financialYear", arr[2].trim());
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        int extraProvisionamount = 0;
        int sanctionAmount = 0;
        for (CreateBudgetExpense incomeBudget : ledgerList) {
            String budgetHead = incomeBudget.getBudgetHead();
            String budgetTtypeName = incomeBudget.getBudgetTypeName();
            String budgetddo = incomeBudget.getDdo();
            String finanYear = incomeBudget.getFinancialYear();
            String dept = incomeBudget.getDepartment();
            int amount = Integer.parseInt(incomeBudget.getRequestedAmount());
            if (Integer.parseInt(incomeBudget.getSanctionedAmount()) != 0) {
                amount = Integer.parseInt(incomeBudget.getSanctionedAmount());
                incomeBudget.setRequestedAmount(String.valueOf(amount));
//                if (Integer.parseInt(incomeBudget.getExtraProvisionAmount()) != 0) {
//                    amount = amount + Integer.parseInt(incomeBudget.getExtraProvisionAmount());
//                }

            }
            String srno = incomeBudget.getSrNo();
            try {
                extraProvisionamount = Integer.parseInt(incomeBudget.getExtraProvisionAmount());
            } catch (Exception e) {

            }
            try {
                sanctionAmount = Integer.parseInt(incomeBudget.getSanctionedAmount());
            } catch (Exception ex) {
            }
            String key = budgetTtypeName + budgetddo + dept + finanYear;
            if (objmap.containsKey(key)) {
                CreateBudgetExpense mapObj = (CreateBudgetExpense) objmap.get(key);
                int headWiseAmount = amount + Integer.parseInt(mapObj.getRequestedAmount());
                int headWiseextraAmount = extraProvisionamount + Integer.parseInt(mapObj.getExtraProvisionAmount());
                try {
                    int headWiseSanctAmount = sanctionAmount + Integer.parseInt(mapObj.getSanctionedAmount());
                    mapObj.setSanctionedAmount(String.valueOf(headWiseSanctAmount));
                } catch (Exception ex) {
                }
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
        conditionMap2.put("budgetTypeName", budgettypeName);
        conditionMap2.put("location", location);
        conditionMap2.put("financialYear", finacialYear);
        conditionMap2.put("budgetType", budgetType);
        conditionMap2.put("department", department);
        conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap2);
        ////System.out.println("--budgetJson--" + budgetJson1);
        if (budgetJson1 != null) {
            List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson1, new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
            Collections.sort(ledgerList, new SortByLedgersInExpense());
            return new Gson().toJson(ledgerList);
        }
        return new Gson().toJson(ApplicationConstants.NO_DATA_FOUND);
    }

    private List<CreateBudgetExpense> getSector(List<CreateBudgetExpense> list) throws Exception {
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

    private List<CreateBudgetExpense> getFinancialYearForIncome(List<CreateBudgetExpense> list) throws Exception {
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

    private List<CreateBudgetExpense> getBudgetTypeForIncome(List<CreateBudgetExpense> list) throws Exception {
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

    public boolean delete(String id) throws Exception {
        boolean result = false;
        int count = 0;
//        JSONObject obj = new JSONObject(id);
//        ////System.out.println(obj);
//        JSONArray array = obj.getJSONArray("id");
//        for (int i = 0; i < array.length(); i++) {
        Type type = new TypeToken<CreateBudgetExpense>() {
        }.getType();
        String assStr = new BudgetExpenseManager().Fetch((String) id);
        if (assStr == null || assStr.isEmpty()) {
            return false;
        }
        CreateBudgetExpense assJson = new Gson().fromJson(assStr, type);
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
        String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap2);
        ////System.out.println("--budgetJson--" + budgetJson1);
        if (budgetJson1 != null) {
            List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson1, new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
            for (CreateBudgetExpense createBudgetExpense : ledgerList) {
                String objId = ((LinkedTreeMap<String, String>) createBudgetExpense.getId()).get("$oid");
                createBudgetExpense.setStatus(ApplicationConstants.DELETE);
                createBudgetExpense.setStatus(ApplicationConstants.DELETE);
                result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, (String) objId, new Gson().toJson(createBudgetExpense));
            }
        }
        if (result) {
            count++;
        }
        //}
        return result;
    }

    public String Fetch(String id) throws Exception {
        if (id == null || id.isEmpty()) {
            return null;
        }

        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, id);
        List<CreateBudgetExpense> gisList = new Gson().fromJson(result, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        if (gisList == null || gisList.size() < 1) {
            return null;
        }
        return new Gson().toJson(gisList.get(0));
    }

    public boolean update(String id, String object) throws Exception {
        ////System.out.println(object);
        String categoryJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_EXPENSE_TABLE, id);
        List<CreateBudgetExpense> sectorlist = new Gson().fromJson(categoryJson, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        CreateBudgetExpense sectorobj = sectorlist.get(0);
        sectorobj.setRequestedAmount(object);
        sectorobj.setCreateDate(sectorobj.getCreateDate());
        sectorobj.setStatus(ApplicationConstants.ACTIVE);
        sectorobj.setUpdateDate(System.currentTimeMillis() + "");
        String sectorjson = new Gson().toJson(sectorobj);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, id, sectorjson);
        return status;
    }

    public boolean Send(String id) throws Exception {
        boolean result = false;
        int count = 0;
        JSONObject obj = new JSONObject(id);
        ////System.out.println(obj);
        JSONArray array = obj.getJSONArray("id");

        for (int i = 0; i < array.length(); i++) {
            Type type = new TypeToken<CreateBudgetExpense>() {
            }.getType();
            String assStr = new BudgetExpenseManager().Fetch((String) array.get(i));
            if (assStr == null || assStr.isEmpty()) {
                return false;
            }
            CreateBudgetExpense assJson = new Gson().fromJson(assStr, type);
            HashMap<String, String> conditionMap2 = new HashMap<String, String>();
            conditionMap2.put("ddo", assJson.getDdo());
            conditionMap2.put("fundType", assJson.getFundType());
//            conditionMap2.put("budgetHead", assJson.getBudgetHead());
            conditionMap2.put("budgetTypeName", assJson.getBudgetTypeName());
            //     conditionMap2.put("location", assJson.getLocation());
            conditionMap2.put("financialYear", assJson.getFinancialYear());
            conditionMap2.put("department", assJson.getDepartment());
            conditionMap2.put("sector", assJson.getSector());
            conditionMap2.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String budgetJson1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap2);
            ////System.out.println("--budgetJson--" + budgetJson1);
            if (budgetJson1 != null) {
                List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson1, new TypeToken<List<CreateBudgetExpense>>() {
                }.getType());
                for (CreateBudgetExpense createBudgetExpense : ledgerList) {
                    String objId = ((LinkedTreeMap<String, String>) createBudgetExpense.getId()).get("$oid");
                    createBudgetExpense.setSentStatus("true");
                    result = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_EXPENSE_TABLE, objId, new Gson().toJson(createBudgetExpense));
                }
            }
            if (result) {
                count++;
            }
        }
        return result;
    }

    public String fetchAllBasedOnFinancialYear(String year, String ddo, String location, String budgetType) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("financialYear", year);
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        conditionMap.put("budgetType", budgetType);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> ledgerList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateBudgetExpense>>() {
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

    public String getSlNumber(String year, String ddo, String location, String budgetType, CreateBudgetExpense obj) throws Exception {

        HashMap<String, String> duplicateConditionMap = new HashMap<String, String>();
        duplicateConditionMap.put("ddo", obj.getDdo());
        duplicateConditionMap.put("location", obj.getLocation());
        duplicateConditionMap.put("fundType", obj.getFundType());
        duplicateConditionMap.put("sector", obj.getSector());
        duplicateConditionMap.put("financialYear", obj.getFinancialYear());
        duplicateConditionMap.put("department", obj.getDepartment());
        duplicateConditionMap.put("budgetType", obj.getBudgetType());
        duplicateConditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (Duplicate.hasDuplicateforSave(ApplicationConstants.BUDGET_EXPENSE_TABLE, duplicateConditionMap)) {
            return ApplicationConstants.DUPLICATE_MESSAGE;

        }

        String result = new BudgetExpenseManager().fetchAllBasedOnFinancialYear(year, ddo, location, budgetType);
        List<CreateBudgetExpense> loanApplyList = new Gson().fromJson(result, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        int numb = 0;
        CreateBudgetExpense pv = new CreateBudgetExpense();
        if (loanApplyList != null) {
            for (Iterator<CreateBudgetExpense> iterator = loanApplyList.iterator(); iterator.hasNext();) {
                CreateBudgetExpense next = iterator.next();
                String len = next.getSrNo();
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

    public String getsrNos(CreateBudgetExpense createIncomeBudgetObj) throws Exception {
        String finYear = createIncomeBudgetObj.getFinancialYear();
        String budgetType = createIncomeBudgetObj.getBudgetType();
        String ddo = createIncomeBudgetObj.getDdo();
        String location = createIncomeBudgetObj.getLocation();
        String fundType = createIncomeBudgetObj.getFundType();
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
            HashMap<String, CreateBudgetExpense> map = new HashMap<String, CreateBudgetExpense>();
            conditionMap.put("fundType", fundType);
            conditionMap.put("budgetTypeName", "Estimated");
            conditionMap.put("location", location);
            conditionMap.put("financialYear", finYear);
            conditionMap.put("ddo", ddo);
            if (department != null && department != "") {
                conditionMap.put("department", department);
            }
            String budgetJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
            ////System.out.println("--budgetJson--" + budgetJson);
            if (budgetJson != null) {
                List<CreateBudgetExpense> incomeBudgetList = new Gson().fromJson(budgetJson, new TypeToken<List<CreateBudgetExpense>>() {
                }.getType());
                for (CreateBudgetExpense income : incomeBudgetList) {
                    map.put(income.getSrNo(), income);
                }
                incomeBudgetList.clear();
                incomeBudgetList.addAll(map.values());
                return new Gson().toJson(incomeBudgetList);
            }
        }
        return "";
    }

    public String getDataExtraProvision(CreateBudgetExpense obj) throws Exception {
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
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_EXPENSE_TABLE, conditionMap);
        List<CreateBudgetExpense> list = new Gson().fromJson(result1, new TypeToken<List<CreateBudgetExpense>>() {
        }.getType());
        List<CreateBudgetExpense> finallist = new ArrayList<CreateBudgetExpense>();
        for (CreateBudgetExpense expenseBudget : list) {
            HashMap<String, String> conditionMap1 = new HashMap<String, String>();
            conditionMap1.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            conditionMap1.put("incomeBudgetId", ((LinkedTreeMap<String, String>) expenseBudget.getId()).get("$oid"));
            String resultextra = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EXTRA_PROVISION_EXPENSE, conditionMap1);
            List<CreateBudgetExpense> listExtra = new Gson().fromJson(resultextra, new TypeToken<List<CreateBudgetExpense>>() {
            }.getType());
            if (listExtra == null || listExtra.size() < 1) {
                finallist.add(expenseBudget);
            }
        }
        return new Gson().toJson(finallist);

    }
}
