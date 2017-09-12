/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.manager;

import com.accure.budget.dto.FinancialYear;
import com.accure.common.delete.DeleteDependencyManager;
import static com.accure.common.duplicate.Duplicate.hasDuplicateforSave;
import static com.accure.common.duplicate.Duplicate.isDuplicateforUpdate;
import com.accure.payroll.manager.SalarySlipRportPDFGeneration;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class FinancialYearManager {

    /**
     * This method is used to save financial year master data to database.
     *
     * @param financialYear is FinancialYear master data.
     * @param userId is logged in user id.
     * @return String
     * <p>
     * This returns primary key value of the document if data is saved to the
     * database, ApplicationConstants.FAIL if anyone of the parameters in the
     * save method is null or empty, ApplicationConstants.DUPLICATE if user
     * tries to save duplicate year of financial year master.</p>
     * @throws java.lang.Exception on input error.
     */
    public String save(FinancialYear financialYear, String userId) throws Exception {

        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("year", financialYear.getYear());
        saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        if (userId == null) {
            return ApplicationConstants.FAIL;
        }

        if (hasDuplicateforSave(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, saveconditionMap)) {
            return ApplicationConstants.DUPLICATE;
        }

        User user = new UserManager().fetch(userId);
        String fName = user.getFname();
        financialYear.setStatus(ApplicationConstants.ACTIVE);
        financialYear.setActive(ApplicationConstants.NO);
        financialYear.setCreateDate(System.currentTimeMillis() + "");
        financialYear.setUpdateDate(System.currentTimeMillis() + "");
        financialYear.setCreatedBy(fName);
        String jsonStr = new Gson().toJson(financialYear);
        String primaryKey = DBManager.getDbConnection().insert(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, jsonStr);

        return primaryKey;
    }

    /**
     * This method is used to display all saved data of Financial Year master.
     *
     * @return String
     * <p>
     * This returns List of Financial Year master data in JSON String
     * format.</p>
     * @throws java.lang.Exception on input error.
     */
    public String fetchAll() throws Exception {
        HashMap<String, String> hMap = new HashMap<String, String>();
        hMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, hMap);
        return result;
    }

    /**
     * This method is used to update the Financial Year master data.
     *
     * @param financialYear is association master data.
     * @param primaryKey is primary key of the document.
     * @param userid is logged in user id.
     * @return String
     * <p>
     * This returns ApplicationConstants.SUCCESS if record is updated
     * successfully, ApplicationConstants.FAIL if anyone of the parameters in
     * the update method is null or empty, ApplicationConstants.DUPLICATE if
     * record contains duplicate year of financial year master.</p>
     * @throws java.lang.Exception on input error.
     */
    public String update(FinancialYear financialYear, String primaryKey, String userid) throws Exception {
        String result;
        User userna = new UserManager().fetch(userid);
        String fName = userna.getFname();
        HashMap<String, String> saveconditionMap = new HashMap<String, String>();
        saveconditionMap.put("year", financialYear.getYear());
        saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        if (primaryKey == null || userid == null) {
            result = null;
        } else if (isDuplicateforUpdate(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, saveconditionMap, primaryKey)) {
            result = ApplicationConstants.DUPLICATE;
        } else {

            String yearJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, primaryKey);
            List<FinancialYear> yearlist = new Gson().fromJson(yearJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear financialyear = yearlist.get(0);

            financialyear.setYear(financialYear.getYear());
            financialyear.setFromDate(financialYear.getFromDate());
            financialyear.setToDate(financialYear.getToDate());

            financialyear.setStatus(ApplicationConstants.ACTIVE);
            financialyear.setUpdateDate(System.currentTimeMillis() + "");

            financialyear.setUpdatedBy(fName);
            String jsonStr = new Gson().toJson(financialyear);

            boolean fResult = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, primaryKey, jsonStr);
            if (fResult) {
                result = ApplicationConstants.SUCCESS;
            } else {
                result = ApplicationConstants.FAIL;
            }

        }
        return result;
    }

    /**
     * This method is used to update status field of an financial year master.
     *
     * @param primaryKey is primary key of the document.
   
     * @return String<p>
     * This returns ApplicationConstants.SUCCESS if record is updated
     * successfully, ApplicationConstants.FAIL if anyone of the parameters in
     * the delete method is null or empty, ApplicationConstants.DELETE_MESSAGE
     * if record is already being use in further pages.</p>
     * @throws java.lang.Exception on input error.
     */
    public String delete(String primaryKey) throws Exception {
        String status = "";

        if (primaryKey == null || primaryKey.isEmpty()) {
            status = ApplicationConstants.FAIL;
        }
        Type type = new TypeToken<FinancialYear>() {
        }.getType();
        String fy = new FinancialYearManager().fetch(primaryKey);
        if (fy == null || fy.isEmpty()) {
            status = ApplicationConstants.FAIL;
        }
        FinancialYear bankJson = new Gson().fromJson(fy, type);
        String fromYearFormat = bankJson.getFromDate();
        String toYearFormat = bankJson.getToDate();

        DateFormat frominputDF = new SimpleDateFormat("dd/MM/yy");
        Date fromdate = frominputDF.parse(fromYearFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromdate);
        int fromMonth = cal.get(Calendar.MONTH) + 1;
        int fromyear = cal.get(Calendar.YEAR);

        DateFormat toinputDF = new SimpleDateFormat("dd/MM/yy");
        Date todate = toinputDF.parse(toYearFormat);

        Calendar tocal = Calendar.getInstance();
        tocal.setTime(todate);
        int toMonth = tocal.get(Calendar.MONTH) + 1;
        int toyear = tocal.get(Calendar.YEAR);

        if (DeleteDependencyManager.hasDependencyFinancialYear(ApplicationConstants.EMP_ATTENDANCE_TABLE, fromMonth, fromyear, toMonth, toyear)) {
            status = ApplicationConstants.DELETE_MESSAGE;
            return status;
        }
        if (DeleteDependencyManager.hasDependencyFinancialYear(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, fromMonth, fromyear, toMonth, toyear)) {
            status = ApplicationConstants.DELETE_MESSAGE;
            return status;
        }
        if (DeleteDependencyManager.hasDependencyFinancialYear(ApplicationConstants.INCOMETAX_TABLE, fromMonth, fromyear, toMonth, toyear)) {
            status = ApplicationConstants.DELETE_MESSAGE;
            return status;
        }
        if (DeleteDependencyManager.hasDependencyFinancialYear(ApplicationConstants.ARREAR_PROCESS_TABLE, fromMonth, fromyear, toMonth, toyear)) {
            status = ApplicationConstants.DELETE_MESSAGE;
            return status;
        }
        if (DeleteDependencyManager.hasDependencyFinancialYear(ApplicationConstants.LOAN_PAYMENT_TABLE, fromMonth, fromyear, toMonth, toyear)) {
            status = ApplicationConstants.DELETE_MESSAGE;
            return status;
        }
        if (!(status).equalsIgnoreCase(ApplicationConstants.DELETE_MESSAGE)) {
            bankJson.setStatus(ApplicationConstants.DELETE);
            boolean flag = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, primaryKey, new Gson().toJson(bankJson));
            if (flag) {
                status = ApplicationConstants.SUCCESS;
            } else {
                status = ApplicationConstants.FAIL;
            }
        }
        return status;
    }
    
    /**
     * This method is used to search the financial year master data by using
     * primary key value.
     *
     * @param primaryKey is primary key of the record.
     * @return String.
     * <p>
     * This returns  Financial Year master data in the JSON String
     * format.</P>
     * @throws java.lang.Exception on input error.
     */

    public String fetch(String primaryKey) throws Exception {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, primaryKey);
        List<FinancialYear> list = new Gson().fromJson(result, new TypeToken<List<FinancialYear>>() {
        }.getType());
        if (list == null || list.size() < 1) {
            return null;
        }
        return new Gson().toJson(list.get(0));
    }

    public boolean changeFinancialYear(String id) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("active", ApplicationConstants.YES);
        String getdata = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, conditionMap);
        if (getdata != null) {
            List<FinancialYear> activeyeslist = new Gson().fromJson(getdata, new TypeToken<List<FinancialYear>>() {
            }.getType());
            FinancialYear dbObject = activeyeslist.get(0);
            FinancialYear activeYear = new FinancialYear();
            activeYear.setYear(dbObject.getYear());
            activeYear.setFromDate(dbObject.getFromDate());
            activeYear.setToDate(dbObject.getToDate());
            activeYear.setStatus(ApplicationConstants.ACTIVE);
            activeYear.setActive(ApplicationConstants.NO);
            activeYear.setCreateDate(dbObject.getCreateDate());
            activeYear.setUpdateDate(System.currentTimeMillis() + "");

            String id1 = ((Map<String, String>) dbObject.getId()).get("$oid");

            String activejson = new Gson().toJson(activeYear);

            boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, id1, activejson);
        }

        String changeJson = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, id);
        List<FinancialYear> yearlist = new Gson().fromJson(changeJson, new TypeToken<List<FinancialYear>>() {
        }.getType());

        FinancialYear financialyear = yearlist.get(0);
        FinancialYear financialYear = new FinancialYear();
        financialYear.setYear(financialyear.getYear());
        financialYear.setFromDate(financialyear.getFromDate());
        financialYear.setToDate(financialyear.getToDate());
        financialYear.setActive(ApplicationConstants.YES);
        financialYear.setCreateDate(financialyear.getCreateDate());
        financialYear.setStatus(ApplicationConstants.ACTIVE);
        financialYear.setUpdateDate(System.currentTimeMillis() + "");
        String jsonStr = new Gson().toJson(financialYear);

        boolean status = DBManager.getDbConnection().update(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, id, jsonStr);
        return status;
    }

    public static HashMap fetchFinancialYearForReport(String month, String isYear) throws Exception {

        HashMap data = new HashMap();

        ArrayList<String> monthSet = new ArrayList<String>();

        int count = Integer.parseInt(month);
        monthSet.add("<option value='0'>------Select Month----</option>");
        if (isYear.equalsIgnoreCase("fromYear")) {
            while (count <= 12) {
                String monthStr = SalarySlipRportPDFGeneration.getMonthString(count);
                monthSet.add("<option value='" + count + "'>" + monthStr + "</option>");
                count++;
            }
        }
        if (isYear.equalsIgnoreCase("toYear")) {
            int monthcount = 1;
            while (monthcount <= count) {
                String monthStr = SalarySlipRportPDFGeneration.getMonthString(monthcount);
                monthSet.add("<option value='" + monthcount + "'>" + monthStr + "</option>");
                monthcount++;
            }
        }

        if (monthSet != null && monthSet.size() > 0) {
            data.put("month", monthSet);
        } else {
            data.put("year", "no data");
        }

        return data;
    }

    public static FinancialYear getActiveFinancialYear() throws Exception {
        FinancialYear fy = null;
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("active", ApplicationConstants.YES);
        condition.put("status", ApplicationConstants.ACTIVE);

        String changeJson = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.BUDGET_FINACIAL_YEAR_TABLE, condition);
        if (changeJson != null) {
            List<FinancialYear> yearlist = new Gson().fromJson(changeJson, new TypeToken<List<FinancialYear>>() {
            }.getType());
            fy = yearlist.get(0);
        }
        return fy;
    }

}
