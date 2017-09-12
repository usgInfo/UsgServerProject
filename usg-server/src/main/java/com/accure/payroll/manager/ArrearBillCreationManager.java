/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.budget.dto.FundType;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Designation;
import com.accure.payroll.dto.ArrearProcess;
import static com.accure.payroll.manager.SalaryBillCreationManager.fetch;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author MISHA THOMAS
 */
public class ArrearBillCreationManager {

     String attTable = ApplicationConstants.USG_DB1 + ApplicationConstants.ARREAR_PROCESS_TABLE + "`";
       RestClient aql = new RestClient();
   public String getListOfEmployees(String ddo, int month, int year,String orderBy) throws Exception {

        ArrayList<ArrearProcess> list = new ArrayList<ArrearProcess>();
      
       
        ////System.out.println(attTable);

        String attQuery = "select *,_id as idStr from " + attTable + " where ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year
                + " order by " + orderBy;
        //System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        //System.out.println(dbOutput);
        if(dbOutput==null)
        {
            return ApplicationConstants.FAIL;
        }
        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
        }.getType());
        for (HashMap map : tempList) {
            ArrearProcess asp = new Gson().fromJson(new Gson().toJson(map.get("value")), new TypeToken<ArrearProcess>() {
            }.getType());
            list.add(asp);
        }
//        list = new Gson().fromJson(dbOutput, new TypeToken< ArrayList<ArrearProcess>>() {
//        }.getType());

        //1) empList1 has list of employee where isLocked & sendToAccounts both are false
        //2) empList2 has list of employee where isLocked=true & sendToAccounts=false
        //3) empList3 has list of employee where isLocked and sendToAccounts both are true
        ArrayList<ArrearProcess> empList1 = new ArrayList<ArrearProcess>();
        ArrayList<ArrearProcess> empList2 = new ArrayList<ArrearProcess>();
        ArrayList<ArrearProcess> empList3 = new ArrayList<ArrearProcess>();

        for (ArrearProcess asp : list) {
            if (asp.getLockStatus()== false && asp.isSendToAccountStatus()== false) {
                empList1.add(asp);         
            } else if (asp.getLockStatus() == true && asp.isSendToAccountStatus() == false) {
                empList2.add(asp);
            } else if (asp.getLockStatus() == true && asp.isSendToAccountStatus() == true) {
                empList3.add(asp);
            }
        }
//        try 
////        {
////            empList1 = getFundTypeArrearprocess(empList1);
////            empList2 = getFundTypeArrearprocess(empList2);
////            empList3 = getFundTypeArrearprocess(empList3);
//         
//        } catch (Exception e) {
//        }
//         try 
//        {
////            empList1 = getBudgetHeadName(empList1);
////            empList2 = getBudgetHeadName(empList2);
////            empList3 = getBudgetHeadName(empList3);
//         
//        } catch (Exception e) {
//        }
        
        //1) Key = "SalaryProcessedList" , Value= "List of emp where isLocked=false & sendToaccounts=false"
        //2) Key = "NotTranferredToAccounts" , Value= "List of emp where isLocked=true & sendToaccounts=false"
        //3) Key = "TranferredToAccounts" , Value= "List of emp where isLocked=true & sendToaccounts=true"
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ArrearProcessedList", new Gson().toJson(empList1));
        map.put("NotTranferredToAccounts", new Gson().toJson(empList2));
        map.put("TranferredToAccounts", new Gson().toJson(empList3));

        return new Gson().toJson(map);
    }
     public Long getBillNumBasedOnBudgetHead_FundType(String bh, String ft, String ddo, int month, int year) throws Exception{
        
        ArrayList<ArrearProcess> list = new ArrayList<ArrearProcess>();
        String attQuery = "select *,_id from " + attTable + " where budgetHead=\"" + bh + "\""
                + " and fundType=\"" + ft + "\"" + " and ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year;
        ////System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        //System.out.println(dbOutput);
        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
        }.getType());
        for (HashMap map : tempList) {
            ArrearProcess asp = new Gson().fromJson(new Gson().toJson(map.get("value")), new TypeToken<ArrearProcess>() {
            }.getType());
            list.add(asp);
        }
        long billNum1 = 0;
        for (ArrearProcess asp : list) {
            long billNum = asp.getBillSlNo();
            if(billNum >billNum1)
            {
                billNum1=billNum;
            }
        }

        return billNum1;
    }

    /**@author ankur
     * This method gives the maximum value of BILL-NUMBER i.e already stored 
     * @param ddo String type DDO id
     * @param month integer type month 
     * @param year integer type year
     * @return long type max value of BILL NUMBER in that Collection
     * @throws Exception 
     */
    public Long getMaxBillNum(String ddo, int month, int year)throws Exception {
        
        String attQuery = "select MAX(billSlNo) as billSlNo from " + attTable + "where ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year;
        ////System.out.println(attQuery);
//        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
//        //System.out.println(dbOutput);
//        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
//        }.getType());
//        
//        long billNum = 0;
//
//        for (HashMap map : tempList) {
//             billNum = Long.parseLong(String.format("%.0f", Double.parseDouble(map.get("0")+"")));        
//        }
                long slNo=0;
                //System.out.println(attQuery);
                String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
                //System.out.println(dbOutput);
                 List<Map<String,Long>> list = new Gson().fromJson(dbOutput, new TypeToken<List<Map<String,Long>>>() { }.getType());
                 if(dbOutput !="")
                 {
                Map slNooutput=list.get(0);
                 slNo=(Long)slNooutput.get("billSlNo");
                 }
        return slNo;
    }
    
    /**@author ankur
     * This Method gives map of distinct combination of BudgetHead and FundType 
     * @param ddo String type DDO id
     * @param month integer type month
     * @param year integer type year
     * @return HashMap of Unique Combination of BUDGET HEAD & FUND TYPE
     * where Key="BUDGET HEAD", Value="FUND TYPE"
     * @throws Exception 
     */
    public HashMap<String,String> getDistinctPairOfBudgetHead_FundType(String ddo, int month, int year)throws Exception{
    
        String attQuery = "select DISTINCT budgetHead,fundType from " + attTable + "where ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year;
        ////System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        //System.out.println(dbOutput);
        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
        }.getType());
        
       // ArrayList<HashMap> list = new ArrayList<HashMap>();
        HashMap<String,String> map1 = new HashMap<String,String>();
        for (HashMap map : tempList) {
            Set s = map.entrySet();
            Iterator i = s.iterator();
             String keyBh ="";
             String valFt = "";
             int p = 0;
            while(p<2){
            Map.Entry entry =(Map.Entry)i.next();
              if(p==0)
             valFt =(String)entry.getValue();
              if(p==1)
             keyBh = (String) entry.getValue();
              p++;
            }
            map1.put(keyBh+"-"+valFt,keyBh+"-"+valFt);
        }
               
        return map1;
    }
    
    public boolean setBillNumber(String empObjId[]) throws Exception {
        String ddo = "";
        int month = 0;
        int year = 0;
        //Get the DDO,Month & Year 
        for (String id : empObjId) {
            ArrearProcess asp = fetchArrearProcessedEmployee(id);
            if (asp.getLockStatus()== false) {
                ddo = asp.getDdo();
                month = asp.getMonth();
                year = asp.getYear();
            }
        }
        
        //Get the max value of BillNum in DB based on ddo, Month & Year
        long billNum = getMaxBillNum(ddo, month, year)+1;
        
        //this map is used to set bill number as per budgetHead and FundType, 
        //Key="BudgetHead&FundType", and Value="BillNumber"
        HashMap<HashMap<String, String>, Long> mapBhFt = new HashMap<HashMap<String, String>, Long>();
        
        //This map has distinct pair of BudhetHead & Fund Type in DB
        HashMap<String,String> map1 = getDistinctPairOfBudgetHead_FundType(ddo, month, year);
        
        Set set = map1.entrySet();
        Iterator i = set.iterator();
        //Now set the value of bill Number in Map, First Check if bill number  
        //is available in Db, if not then generate from last max value
        while(i.hasNext()){
            Map.Entry entry = (Map.Entry)i.next();
            String key11 = (String)entry.getKey();
            //String valFt = (String)entry.getValue();
            String[] keyval=key11.split("-");
            String keyBh = keyval[0];
            String valFt = keyval[1];
            HashMap<String,String> tempMap = new HashMap<String, String>();
            tempMap.put(keyBh,valFt);
            //This method will get billNum if it exists in DB
            long dbBillNum = getBillNumBasedOnBudgetHead_FundType(keyBh, valFt, ddo, month, year);
            //If value is > 0, that means BillNumber already generated
            if(dbBillNum>0)
                mapBhFt.put(tempMap,dbBillNum);
            else{
                mapBhFt.put(tempMap, billNum);
                billNum++;
            }
        }

        boolean status = false;

        //Now update billNum in DB as per budgetHead & FundType of Employee
        for (String id : empObjId) {
            ArrearProcess asp = fetchArrearProcessedEmployee(id);
            //get budgetHead & FundType of this asp object
            String bh = asp.getBudgetHead();
            String ft = asp.getFundType();
             String budgetheadname=asp.getBudgetHeadName();
               String fundtypename=asp.getFundTypeName();
            HashMap<String,String> map2 = new HashMap<String,String>();
            map2.put(bh,ft);
           //getting billnumber for this BudgetHead and FundType from Map
            long bill = mapBhFt.get(map2);
            //Setting BillNumber
            asp.setBillSlNo(bill);
            asp.setAutoGeneratedBillNo(month+"-"+year+"-["+fundtypename+"]--("+budgetheadname+")-"+bill);
            String updatedBillJson = new Gson().toJson(asp);
            //Updating DB with this billNumber
            status = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, id, updatedBillJson);
        }

        return status;
    }
//    public boolean setBillNumber(String empObjId[]) throws Exception {
//
//        String budgetHeads[] = new String[empObjId.length];
//
//        //this i variable is used to increase the index in loop 
//        int i = 0;
//        //Make lists of String[] as per budgetheads and empCode
//         HashMap<String, Integer> map = new HashMap<String, Integer>();
//        for (String id : empObjId) 
//        {
//            ArrearProcess asp = fetchArrearProcessedEmployee(id);
//            if (asp.getLockStatus()== false) 
//            {
//               String budgetHead = asp.getBudgetHead();
//               String funtType = asp.getFundType();
//              
//               
//                String key=String.valueOf(budgetHead)+"-"+String.valueOf(funtType);
//                 if(!map.containsKey(key))
//                 {
//                    i++;
//                    map.put(key,i);
//                }
//            }
//        }
//        //this set is used to remove duplicate budgetHead in list of bh array 
////        HashSet<String> set = new HashSet<String>();
////        for (int j = 0; j < budgetHeads.length; j++) {
////            set.add(budgetHeads[j]);
////        }
//
//        //Start billnumber from 1
//        int billNum = 0;
//
//        //this map is used to set bill number as per budgetHead, 
//        //BudhetHead is key, and billNumber is value
//       
////        for (String budgetHead : set) {
////            map.put(budgetHead, billNum);
////           // billNum++;
////        }
//         RestClient aql = new RestClient();
//        boolean status = false;
//              for (String id1 : empObjId) 
//              {
//                   ArrearProcess asp1 = fetchArrearProcessedEmployee(id1);
//                     int month=asp1.getMonth();
//                     int year=asp1.getYear();
//                     String ddo=asp1.getDdo();
//                     String budgetHead=asp1.getBudgetHead();
//                     String fundType=asp1.getFundType();
//        ////System.out.println(attTable);
//
//        String attQuery = "select MAX(BillSlNo) as billSlNo from " + attTable + " where ddo=\"" + ddo + "\""
//                + " and month=" + month
//                + " and year=" + year
//                + " and budgetHead=\""+ budgetHead+ "\""
//                + " and fundType=\""+ fundType+ "\"";
//                //System.out.println(attQuery);
//                String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
//                //System.out.println(dbOutput);
//                 List<Map<String,Integer>> list = new Gson().fromJson(dbOutput, new TypeToken<List<Map<String,Integer>>>() {
//                }.getType());
//                 if(dbOutput !="")
//                 {
//                Map slNooutput=list.get(0);
//                int slNo=(Integer)slNooutput.get("billSlNo");
//                String key1=String.valueOf(budgetHead)+"-"+String.valueOf(fundType);
//                if(map.containsKey(key1))
//                {
////                   int slNo1=map.get(key1);
//                   if(slNo!=0)
//                    {
//                      map.put(key1,slNo);
//                  }
////                   else
////                    {
////                         map.put(key1,slNo); 
////                    }
//                }
//            }
//              }
//        //Now update billNum in Database as per budgetHead of employees
//        for (String id : empObjId) {
//            ArrearProcess asp = fetchArrearProcessedEmployee(id);
//            //get budgetHead of this asp object
//            String bh = asp.getBudgetHead();
//             String ft = asp.getFundType();
//            String month=String.valueOf(asp.getMonth());
//            String year=String.valueOf(asp.getYear());
//             String budgetheadname=asp.getBudgetHeadName();
//               String fundtypename=asp.getFundTypeName();
//            //getting billnumber for this budgetHead from map
//            int bill = map.get(bh+"-"+ft);
//            
//            //If the billnumber is 0, or not created, we start from 1
//            
//                
//                asp.setBillSlNo(bill);
//                asp.setAutoGeneratedBillNo(month+"-"+year+"-["+fundtypename+"]--("+budgetheadname+")-"+bill);
//                //map.put(bh,bill);
//            
////            //else if billno is there, we will increase val of billno by one 
////            else{
////              long val =   asp.getBillNo();
////              asp.setBillNo(val++);
////            }
//
//
//            String updatedBillJson = new Gson().toJson(asp);
//
//            status = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, id, updatedBillJson);
//
//        }
//
//        return status;
//    }
   
    public boolean updateIsLockedTrue(String empId[]) throws Exception {

        //get list of employees and set sendToAccounts = true for all of these employees
        boolean status = false;
        for (String id : empId) {
            ArrearProcess asp = fetchArrearProcessedEmployee(id);

            if (asp.getLockStatus()== false) {
                asp.setLockStatus(true);
            }

            String arrearProcessJson = new Gson().toJson(asp);
            status = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, id, arrearProcessJson);
        }

        return status;
    }
    
     public boolean updateSendToAccountsTrue(String empListJson) throws Exception {
        boolean status = false;
        List<Map> list = new Gson().fromJson(empListJson, new TypeToken<List<Map>>() {
                }.getType());
                String empObjId[] = new String[list.size()];
               //This i is used to increment the index of empObjId
                int i = 0;
                //we will get one by one Object Id's & then Store in empObjId Array
                for (Map li : list)
                {
                    String str = (String) li.get("emprowid");
                    String manualBillNo = (String) li.get("manualBillNo");
                    String billDate = (String) li.get("billDate");
                ArrearProcess asp = fetchArrearProcessedEmployee(str);
            if (asp.isSendToAccountStatus()== false && asp.getLockStatus()== true)
            {
                asp.setSendToAccountStatus(true);
                asp.setManualBillNo(manualBillNo);
                asp.setBillDate(billDate);
            }
            String arrearProcessJson = new Gson().toJson(asp);
            status = DBManager.getDbConnection().update(ApplicationConstants.ARREAR_PROCESS_TABLE, str, arrearProcessJson);
        }

        return status;
    }
        public static ArrearProcess fetchArrearProcessedEmployee(String empObjId) throws Exception {

        String arrearProcessJson = DBManager.getDbConnection().fetch(ApplicationConstants.ARREAR_PROCESS_TABLE, empObjId);

        if (arrearProcessJson == null || arrearProcessJson.isEmpty()) {
            return null;
        }
        List<ArrearProcess> arrearProcessList = new Gson().fromJson(arrearProcessJson, new TypeToken<List<ArrearProcess>>() {
        }.getType());
        if (arrearProcessList == null || arrearProcessList.isEmpty()) {
            return null;
        }

        return arrearProcessList.get(0);

    }
    public static ArrayList<ArrearProcess> getFundTypeArrearprocess(ArrayList<ArrearProcess> employeeList) throws Exception {
        Map<String, String> DesignationMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.FUND_TYPE_TABLE);
        List<FundType> religionList = new Gson().fromJson(result, new TypeToken<List<FundType>>() {
        }.getType());
        for (Iterator<FundType> iterator = religionList.iterator(); iterator.hasNext();) {
            FundType next = iterator.next();
            DesignationMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDescription());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DesignationMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getFundType())) {
                    employeeList.get(i).setFundTypeName(entry.getValue());
                }
            }
        }
        return employeeList;
    }
     public static ArrayList<ArrearProcess> getBudgetHeadName(ArrayList<ArrearProcess> employeeList) throws Exception {
        Map<String, String> budgetHeadMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE);
        List<BudgetHeadMaster> religionList = new Gson().fromJson(result, new TypeToken<List<BudgetHeadMaster>>() {
        }.getType());
        for (Iterator<BudgetHeadMaster> iterator = religionList.iterator(); iterator.hasNext();) {
            BudgetHeadMaster next = iterator.next();
            budgetHeadMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getBudgetHead());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : budgetHeadMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getBudgetHead())) {
                    employeeList.get(i).setBudgetHeadName(entry.getValue());
                }
            }
        }
        return employeeList;
    }
}
