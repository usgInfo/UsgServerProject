package com.accure.payroll.manager;

import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ankur
 * Dear debugger if you are changing any logic,type or condition
 * then please first go through the doc before messing the code :) 
 */
public class SalaryBillCreationManager {

    String attTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
    RestClient aql = new RestClient();
    SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
    Calendar cal = Calendar.getInstance();
    String date = d.format(cal.getTime());

    /**@author ankur
     * This method get all employees from DB, as per scenario of
     * sendToAccounts and BillNumber(billNo)
     * @param ddo input is string type to get list of employee for that DDO only
     * @param month is integer input to get list of employee for that month only
     * @param year integer input to get list of employee for that year only
     * @return String, Lists of employee , where :-
     * 1)Key="SalaryProcessedList",Value= "List of employee where
     * sendToaccounts=false & billNo=0" 2)Key="NotTranferredToAccounts", 
     * Value= "List of employee where sendToaccounts=false & billNo>0" 
     * 3)Key="TranferredToAccounts" , Value= "List of employee 
     * where sendToaccounts=true & billNo>0"
     * @throws Exception
     */
    public String getListOfEmployees(String ddo, int month, int year) throws Exception {

        //This list consist of all Emp based on this DDO, Month and Year
        ArrayList<AutoSalaryProcess> list = new ArrayList<AutoSalaryProcess>();

        ////System.out.println(attTable);
        String attQuery = "select *,_id as idStr from " + attTable + " where ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year;
        ////System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        //System.out.println(dbOutput);
        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
        }.getType());
        for (HashMap map : tempList) {
            AutoSalaryProcess asp = new Gson().fromJson(new Gson().toJson(map.get("value")), new TypeToken<AutoSalaryProcess>() {
            }.getType());
            list.add(asp);
        }

        //1) empList1 has list of employee where sendToAccounts=false & billNo=0
        //2) empList2 has list of employee where sendToAccounts=false & biilNo>0
        //3) empList3 has list of employee where sendToAccounts=true & billNo>0
        ArrayList<AutoSalaryProcess> empList1 = new ArrayList<AutoSalaryProcess>();
        ArrayList<AutoSalaryProcess> empList2 = new ArrayList<AutoSalaryProcess>();
        ArrayList<AutoSalaryProcess> empList3 = new ArrayList<AutoSalaryProcess>();

        for (AutoSalaryProcess asp : list) {
            if (asp.isSendToAccounts() == false && asp.getBillNo()==0) {
                empList1.add(asp);
            } else if (asp.isSendToAccounts() == false && asp.getBillNo()>0) {
                empList2.add(asp);
            } else if (asp.isSendToAccounts() == true && asp.getBillNo()>0) {
                empList3.add(asp);
            }
        }
        
        //1) Key = "SalaryProcessedList" , Value= "List of emp where sendToaccounts=false & billNo=0"
        //2) Key = "NotTranferredToAccounts" , Value= "List of emp where sendToaccounts=false & billNo>0"
        //3) Key = "TranferredToAccounts" , Value= "List of emp where sendToaccounts=true & billNo>0"
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("SalaryProcessedList", new Gson().toJson(empList1));
        map.put("NotTranferredToAccounts", new Gson().toJson(empList2));
        map.put("TranferredToAccounts", new Gson().toJson(empList3));

        return new Gson().toJson(map);
    }

    /**@author ankur
     * This method gives the Salary Bill Number from DB if it is saved earlier
     * @conditons 1)get billNum only based on following parameters
     * @param bh  String type budget Head
     * @param ft  String type Fund Type
     * @param ddo  String type DDO
     * @param year  integer type year
     * @param month integer type month
     * @return Bill Number long type, (if saved earlier in DB)
     * @throws Exception
     */
    public Long getBillNumBasedOnBudgetHead_FundType(String bh, String ft, String ddo, int month, int year) throws Exception{
        
        ArrayList<AutoSalaryProcess> list = new ArrayList<AutoSalaryProcess>();
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
            AutoSalaryProcess asp = new Gson().fromJson(new Gson().toJson(map.get("value")), new TypeToken<AutoSalaryProcess>() {
            }.getType());
            list.add(asp);
        }
        long billNum = 0;
        for (AutoSalaryProcess asp : list) {
            billNum = asp.getBillNo();
        }

        return billNum;
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
        
        String attQuery = "select max(billNo) from " + attTable + "where ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year;
        ////System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        //System.out.println(dbOutput);
        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
        }.getType());
        
        long billNum = 0;

        for (HashMap map : tempList) {
             billNum = Long.parseLong(String.format("%.0f", Double.parseDouble(map.get("0")+"")));        
        }

        return billNum;
    }
    
    /**@author ankur
     * This Method gives List of distinct combination of BudgetHead and FundType 
     * @param ddo String type DDO id
     * @param month integer type month
     * @param year integer type year
     * @return ArrayList<HashMap>having Unique Combination of BHEAD & FUNDTYPE
     * where Key="BUDGET HEAD", Value="FUND TYPE"
     * @throws Exception 
     */
    public ArrayList<HashMap> getDistinctPairOfBudgetHead_FundType(String ddo, int month, int year)throws Exception{
    
        String attQuery = "select DISTINCT budgetHead,fundType from " + attTable + "where ddo=\"" + ddo + "\""
                + " and month=" + month
                + " and year=" + year;
        ////System.out.println(attQuery);
        String dbOutput = aql.getRestData(ApplicationConstants.END_POINT, attQuery);
        //System.out.println(dbOutput);
        ArrayList<HashMap> tempList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<HashMap>>() {
        }.getType());
               
        return tempList;
    }
    
    /**@author ankur 
     * This method set the BILL NUMBERS as per BUDGET HEADS and FUND TYPE
     * @conditions 1)Get the max value of Bill Number from DB
     * 2)Check if there is already stored bill number for combination of this 
     * BudgetHead & FundType,DDO,Month & Year * 3)If not start from max value 
     * of bill and keep increasing 
     * @param empObjId is String type array of MongoDb Object Id's
     * @return true, if the Bill Numbers are added as per BudgetHead & Fund Type
     * @throws Exception
     */
    public boolean setBillNumber(String empObjId[]) throws Exception {
        String ddo = "";
        String ddoName = "";
        int month = 0;
        int year = 0;
        //Get the DDO,Month & Year 
        for (String id : empObjId) {
            AutoSalaryProcess asp = fetch(id);
            if (asp.isSendToAccounts()==false) {
                ddo = asp.getDdo();
                ddoName = asp.getDdoName();
                month = asp.getMonth();
                year = asp.getYear();
                break;
            }
        }
        //Get the max value of BillNum in DB based on ddo, Month & Year
        long billNum = getMaxBillNum(ddo, month, year)+1;
        
        //this map is used to set bill number as per budgetHead and FundType, 
        //Key="BudgetHead&FundType", and Value="BillNumber"
        HashMap<HashMap<String, String>, Long> mapBhFt = new HashMap<HashMap<String, String>, Long>();
        
        //This List has distinct pair of BudhetHead & Fund Type in DB
        ArrayList<HashMap> tempList  = getDistinctPairOfBudgetHead_FundType(ddo, month, year);
        //Get pair one by one and save in mapBhFT as key
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
            HashMap<String,String> tempMap = new HashMap<String,String>();
            tempMap.put(keyBh,valFt);
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
            AutoSalaryProcess asp = fetch(id);
            //get budgetHead & FundType of this asp object
            String bh = asp.getBudgetHead();
            String ft = asp.getFundType();
            HashMap<String,String> map2 = new HashMap<String,String>();
            map2.put(bh,ft);
           //getting billnumber for this BudgetHead and FundType from Map
            long bill = mapBhFt.get(map2);
            String budget = asp.getBudgetHeadName();
            String fund = asp.getFundTypeName();
            String autoGenBillNum = ""+ddoName+"-"+month+"-"+year+"-["+budget
                                     +"]-("+fund+")-"+bill;
            asp.setAutoGeneratedBillNumber(autoGenBillNum);
            //Setting BillNumber
            asp.setBillNo(bill);
            asp.setBillDate(date);
            String updatedBillJson = new Gson().toJson(asp);
            //Updating DB with this billNumber
            status = DBManager.getDbConnection().update(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, id, updatedBillJson);
        }

        return status;
    }
    
    /**This method deletes the billNumber, Auto Generated Bill Number, 
     * and update sendToAccounts="false", billDate=""
     * @param empObjId is String type array of MongoDb object Id's
     * @return true, if deletion is successful
     * @throws Exception 
     */
    public boolean delete(String empObjId, String userId)throws Exception{
        boolean status = false;
      
            User user = new UserManager().fetch(userId);
            String userName = user.getFname() + " " + user.getLname();
            AutoSalaryProcess asp = fetch(empObjId);
            asp.setBillNo(0);
            asp.setSendToAccounts(false);
            asp.setAutoGeneratedBillNumber("");
            asp.setManualBillNumber("");
            asp.setBillDate("");
            asp.setUpdatedBy(userName);
            asp.setDeletedBy(userName);
            String updatedJson = new Gson().toJson(asp);
            //Updating DB 
            status = DBManager.getDbConnection().update(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, empObjId, updatedJson);
        
        return status;
    }

    /**@author ankur 
     * This method fetches all query as per objectId and returns
     * an AutoSalaryProcess object
     * @param empObjId is the primary key to get employee details from database
     * @return AutoSalaryProcess object of employee details
     * @throws Exception
     */
    public static AutoSalaryProcess fetch(String empObjId) throws Exception {

        String salaryProcessJson = DBManager.getDbConnection().fetch(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, empObjId);

        if (salaryProcessJson == null || salaryProcessJson.isEmpty()) {
            return null;
        }
        List<AutoSalaryProcess> salaryProcessList = new Gson().fromJson(salaryProcessJson, new TypeToken<List<AutoSalaryProcess>>() {
        }.getType());
        if (salaryProcessList == null || salaryProcessList.isEmpty()) {
            return null;
        }

        return salaryProcessList.get(0);

    }

    /**
     * @author ankur This method will update the List of employees IS-LOCKED
     * status
     * @param empId[] is String Array of Object Id's , whose isLocked status
     * will be set to true
     * @return true if the update is done on isLocked status otherwise false
     * @throws Exception
     */
    /*
    public boolean updateIsLockedTrue(String empId[]) throws Exception {

        //get list of employees and set sendToAccounts = true for all of these employees
        boolean status = false;
        for (String id : empId) {
            AutoSalaryProcess asp = fetch(id);

            if (asp.isIsLocked() == false) {
                asp.setIsLocked(true);
            }

            String salaryProcessJson = new Gson().toJson(asp);
            status = DBManager.getDbConnection().update(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, id, salaryProcessJson);
        }

        return status;
    }*/
    

    /**
     * @author ankur This method will update the List of employees
     * SEND-TO-ACCOUNTS status
     * @param empId[] is String Array of Object Id's , whose sendToAccounts
     * status will be set true
     * @return true if the update is done on sendToAccounts status otherwise
     * false
     * @throws Exception
     */
    public boolean updateSendToAccountsTrue(String empId[]) throws Exception {
        boolean status = false;
        for (String id : empId) {
            AutoSalaryProcess asp = fetch(id);

            if (asp.isSendToAccounts() == false ) {
                asp.setSendToAccounts(true);
            }

            String salaryProcessJson = new Gson().toJson(asp);
            status = DBManager.getDbConnection().update(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, id, salaryProcessJson);
        }

        return status;
    }
    
        public boolean update(AutoSalaryProcess aspList, String Id, String loginUserId) throws Exception {
        User user = new UserManager().fetch(loginUserId);
        String userName = user.getFname() + " " + user.getLname();
        aspList.setUpdateDate(System.currentTimeMillis() + "");
        aspList.setUpdatedBy(userName);
        String aspJson = new Gson().toJson(aspList);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, Id, aspJson);
        return result;
    }
    
        public String fetchData(String Id) throws Exception {
        if (Id == null || Id.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, Id);
        List<AutoSalaryProcess> aspList = new Gson().fromJson(result, new TypeToken<List<AutoSalaryProcess>>() {
        }.getType());
        if (aspList == null || aspList.size() < 1) {
            return null;
        }
        return new Gson().toJson(aspList.get(0));
    }

     public static void main(String[] args) throws Exception {
     SalaryBillCreationManager sb = new SalaryBillCreationManager();
     try{
     //String map = sb.getListOfEmployees("58133a964e4ef815bc437c01", 1, 2016);
       //  //System.out.println(map);
       // //System.out.println("Hellooooo");
       // String ar[] = {"5811f913a3ad206cde291cba"};
       //  //System.out.println(sb.setBillNumber(ar));
      //   //System.out.println(sb.getBillNumBasedOnBudgetHead_FundType
     //  ("57f2029cf5baf949030bafb5","57f51e32e4b00d0c9cf05975", "57fc691ae4b0c3798402d14c",10 ,2016 ));

        // //System.out.println(sb.getDistinctPairOfBudgetHead_FundType("57fc691ae4b0c3798402d14c", 10, 2016));
     } 
    
     catch (Exception e) {
     e.printStackTrace();
     }
     }
}
