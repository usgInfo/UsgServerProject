/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.Formula;
import com.accure.hrms.dto.HeadSlab;
import com.accure.hrms.dto.SalaryHead;
import com.accure.hrms.manager.EmployeeManager;
import static com.accure.hrms.manager.EmployeeManager.basedOnQuery;
import static com.accure.hrms.manager.EmployeeManager.getDesignation;
import com.accure.hrms.manager.ExpressionCalculatorManager;
import com.accure.hrms.manager.SalaryHeadManager;
import com.accure.payroll.dto.SalaryIncrementDateWise;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author accure
 */
public class SalaryIncrementDateWiseManager {

    public String searchSalaryIncrement(Employee emp, String fromdate, String ToDate) throws Exception {
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.EMPLOYEE_TABLE);
        DBCollection salarycollection = db.getCollection(ApplicationConstants.SALARY_INCREMENT_DATE_WISE);
        BasicDBObject regexQuery = new BasicDBObject();
        BasicDBObject incrementregexQuery = new BasicDBObject();
//     
        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
            incrementregexQuery.put("ddo",
                    new BasicDBObject("$regex", emp.getDdo()));
        }

        if (emp.getLocation() != null) {
            regexQuery.put("location",
                    new BasicDBObject("$regex", emp.getLocation()));

        }
        if (emp.getDepartment() != null) {
            regexQuery.put("department",
                    new BasicDBObject("$regex", emp.getDepartment()));

        }
        if (emp.getDesignation() != null) {
            regexQuery.put("designation",
                    new BasicDBObject("$regex", emp.getDesignation()));

        }
        if (emp.getNatureType() != null) {
            regexQuery.put("natureType",
                    new BasicDBObject("$regex", emp.getNatureType()));

        }
        if (emp.getPostingCity() != null) {
            regexQuery.put("postingCity",
                    new BasicDBObject("$regex", emp.getPostingCity()));

        }
        if (emp.getFundType() != null) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", emp.getFundType()));

        }
        if (emp.getEmployeeName() != null) {
            regexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
            incrementregexQuery.put("employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (emp.getEmployeeCode() != null) {
            regexQuery.put("employeeCode",
                    new BasicDBObject("$regex", emp.getEmployeeCode()));
            incrementregexQuery.put("employeeCode",
                    new BasicDBObject("$regex", emp.getEmployeeCode()));
        }
        if (fromdate != null && ToDate != null) {
            String fromDate = fromdate;
            String toDate = ToDate;
            long fromDateMilliSec = saveInMilliSecond(fromDate);
            long toDateMilliSec = saveInMilliSecond(toDate);
            regexQuery.put("IncrementDueDateInMilliSecond", new BasicDBObject("$gte", fromDateMilliSec).append("$lte", toDateMilliSec));
            incrementregexQuery.put("incrementDate", new BasicDBObject("$gte", fromDateMilliSec).append("$lte", toDateMilliSec));

        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));

        DBCursor cursor2 = collection.find(regexQuery);
        //System.out.println(cursor2);
        DBCursor saleryinccursor = salarycollection.find(incrementregexQuery);
        //System.out.println(saleryinccursor);
        List<Employee> employeeList = new ArrayList<Employee>();
        List<SalaryIncrementDateWise> salaryincreList = new ArrayList<SalaryIncrementDateWise>();

        while (saleryinccursor.hasNext()) {
            DBObject salaryob = saleryinccursor.next();
            Type salarytype = new TypeToken<SalaryIncrementDateWise>() {
            }.getType();
            SalaryIncrementDateWise salaryem = new Gson().fromJson(salaryob.toString(), salarytype);
            salaryincreList.add(salaryem);
        }
        //System.out.println(employeeList);
        while (cursor2.hasNext()) {
            DBObject ob = cursor2.next();
            Type type = new TypeToken<Employee>() {
            }.getType();
            Employee em = new Gson().fromJson(ob.toString(), type);
            employeeList.add(em);
        }
        if (employeeList.size() > 0) {
            try {
                employeeList = getDesignation(employeeList);

            } catch (Exception e) {
            }
            try {
                employeeList = getSalaryIncDDO(employeeList);

            } catch (Exception e) {
            }
        }
        if (salaryincreList.size() > 0) {
            for (SalaryIncrementDateWise salryinclist : salaryincreList) {
                if (salryinclist.getDdo() != null) {
                    try {
                        String getDDOList = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, salryinclist.getDdo());
                        if (getDDOList != null) {
                            List<DDO> getDDO = new Gson().fromJson(getDDOList, new TypeToken<List<DDO>>() {
                            }.getType());
                            DDO gal1 = getDDO.get(0);
                            salryinclist.setDdo(gal1.getDdoName());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        HashMap<String, String> resultList = new HashMap<String, String>();
        resultList.put("empList", new Gson().toJson(employeeList));
        resultList.put("salaryIncDueList", new Gson().toJson(salaryincreList));

        return new Gson().toJson(resultList);

    }

    public long saveInMilliSecond(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = str;
        Date date = sdf.parse(dateInString);
        return date.getTime();
    }

    private List<Employee> getSalaryIncDDO(List<Employee> employeeList) throws Exception {
        Map<String, String> DdoMap = new HashMap<String, String>();
        String result = DBManager.getDbConnection().fetchAll(ApplicationConstants.DDO_TABLE);
        List<DDO> religionList = new Gson().fromJson(result, new TypeToken<List<DDO>>() {
        }.getType());
        for (Iterator<DDO> iterator = religionList.iterator(); iterator.hasNext();) {
            DDO next = iterator.next();
            DdoMap.put(((LinkedTreeMap<String, String>) next.getId()).get("$oid"), next.getDdoName());
        }
        for (int i = 0; i < employeeList.size(); i++) {
            for (Map.Entry<String, String> entry : DdoMap.entrySet()) {
                if (entry.getKey().equals(employeeList.get(i).getDdo())) {
                    employeeList.get(i).setDdo(entry.getValue());
                }
            }
        }
        return employeeList;
    }

    public String save(SalaryIncrementDateWise salaryIncDTO, String userid) throws Exception {
// increment persentage fetch from frent end
        String ddoresult = null;
        String increpre = salaryIncDTO.getIncrementPercentage();

        //get employee details     
        if (increpre != null && increpre != "") {
            try {
                int increPer = Integer.parseInt(increpre);
                Employee emp = new EmployeeManager().fetchEmployee(salaryIncDTO.getEmprowid());
                long intbasic = emp.getBasic();
                long gradePay = emp.getGradePay();
                // long intbasic = Long.parseLong(basic);
                intbasic = intbasic + (intbasic + gradePay) * increPer / 100;
                String basicSalaryHeadId = null;
                String gradepaySalaryHeadId = null;
                double Totalearnings = 0.0;
                double Totaldeductions = 0.0;

                SalaryHead basicEarningHead = new EmployeeManager().fetchBasicEarningHead();
                SalaryHead gardepayEarningHead = new EmployeeManager().fetchGradepayEarningHead();

                salaryIncDTO.setEarningHeads(emp.getEarningHeads());
                salaryIncDTO.setDeductionHeads(emp.getDeductionHeads());
                salaryIncDTO.setTotalDeductions(emp.getTotalDeductions());
                salaryIncDTO.setTotalEarnings(emp.getTotalEarnings());
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = formatter.parse(salaryIncDTO.getIncrementStrDate());
                long dateInLong = date.getTime();
                User user = new UserManager().fetch(userid);
                String fName = user.getFname();
                salaryIncDTO.setCreatedBy(fName);
                //System.out.println("=+++++++++++++" + intbasic);
                salaryIncDTO.setIncbasic(intbasic);
                salaryIncDTO.setCreateDate(System.currentTimeMillis() + "");
                salaryIncDTO.setIncrementDate(dateInLong);
                salaryIncDTO.setStatus(ApplicationConstants.ACTIVE);

                String salaryIncJson = new Gson().toJson(salaryIncDTO);

                ddoresult = DBManager.getDbConnection().insert(ApplicationConstants.SALARY_INCREMENT_DATE_WISE, salaryIncJson);

                if (basicEarningHead != null) {
                    basicSalaryHeadId = ((LinkedTreeMap<String, String>) basicEarningHead.getId()).get("$oid");
                }
                if (gardepayEarningHead != null) {
                    gradepaySalaryHeadId = ((LinkedTreeMap<String, String>) gardepayEarningHead.getId()).get("$oid");
                }

                List<EarningHeadsDetails> earHeadsList = new ArrayList<EarningHeadsDetails>(emp.getEarningHeads());
                for (EarningHeadsDetails earingHeads : earHeadsList) {
                    if (!earingHeads.getDescription().equalsIgnoreCase(basicSalaryHeadId)) {
                        boolean headcondition = new SalaryIncrementDateWiseManager().checkHeadCondition(earingHeads.getDescription());
                        if (headcondition) {
                            SalaryHead fo = getsalryhead(earingHeads.getDescription());
                            double headamount = getHeadAmount(fo, basicSalaryHeadId, intbasic, gradePay, emp, gradepaySalaryHeadId, increPer, "");
                            earingHeads.setAmount(headamount);
                            Totalearnings = Totalearnings + headamount;
                        } else {
                            Totalearnings = Totalearnings + earingHeads.getAmount();
                        }
                    } else {
                        earingHeads.setAmount(intbasic);
                        Totalearnings = Totalearnings + intbasic;
                    }
                }

                //-------------------Deduction Heads ----------------------------------       
                List<EarningHeadsDetails> dedHeadsList = new ArrayList<EarningHeadsDetails>(emp.getDeductionHeads());
                for (EarningHeadsDetails deducHeads : dedHeadsList) {
                    if (!deducHeads.getDescription().equalsIgnoreCase(basicSalaryHeadId)) {
                        boolean headcondition = new SalaryIncrementDateWiseManager().checkHeadCondition(deducHeads.getDescription());
                        if (headcondition) {
                            SalaryHead fo = getsalryhead(deducHeads.getDescription());
                            double headamount = getHeadAmount(fo, basicSalaryHeadId, intbasic, gradePay, emp, gradepaySalaryHeadId, increPer, "");
                            deducHeads.setAmount(headamount);
                            Totaldeductions = Totaldeductions + headamount;
                        } else {
                            Totaldeductions = Totaldeductions + deducHeads.getAmount();
                        }
                    } else {
                        deducHeads.setAmount(intbasic);
                        Totalearnings = Totalearnings + intbasic;
                    }
                }
                emp.setEarningHeads(earHeadsList);
                emp.setDeductionHeads(dedHeadsList);
                emp.setTotalDeductions(Totaldeductions);
                emp.setTotalEarnings(Totalearnings);
                emp.setBasic(intbasic);
                emp.setUpdateDate(System.currentTimeMillis() + "");

                // Save data into Salary Increment Table
                emp.setSalaryIncId(ddoresult);
                String status = new EmployeeManager().update(emp, salaryIncDTO.getEmprowid(), userid);
            } catch (Exception e) {
            }
        }
        return ddoresult;
    }

    public static double getHeadAmount(SalaryHead fo, String basicId, Long basicAmount, Long gradePay, Employee empGetHeads, String gradePayId, int per, String conditi) throws Exception {
        double result = 0.0;
        String basicAmountInString = "" + basicAmount;
        String gradepayAmountInString = "" + gradePay;
        if (fo.getMapping().equalsIgnoreCase(ApplicationConstants.NO)) {
            //System.out.println("Mapping is no");
            String forid = fo.getFormula();
            String forml = null;
            String amount = fo.getAmount();
            if (amount != null && !amount.isEmpty() && (forid == null || forid.isEmpty())) {
                if (conditi.equalsIgnoreCase("delete")) {
                    result = Double.parseDouble(amount) * 100 / (100 + per);
                } else {
                    result = Double.parseDouble(amount) + Double.parseDouble(amount) * per / 100;
                }

                return result;
            } else if ((forid != null && !forid.isEmpty()) && (amount == null || amount.isEmpty())) {
                //System.out.println("Inside formula");
                forml = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, forid);
                List<Formula> formuList = null;
                if (forml != null) {
                    formuList = new Gson().fromJson(forml, new TypeToken<List<Formula>>() {
                    }.getType());
                } else {
                    return result;
                }
                Formula forobj = formuList.get(0);
                String formu = forobj.getHiddenformula();
                List<Integer> liIndexes = new ArrayList<Integer>();
                List<String> idlist = new ArrayList<String>();
                boolean condition = true;
                while (condition) {
                    liIndexes.clear();
                    idlist.clear();
                    condition = false;
                    char[] chrArray = formu.toCharArray();
                    int j = 0;
                    for (int i = 0; i < chrArray.length; i++) {
                        char c = chrArray[i];
                        if (c == '#') {
                            liIndexes.add(i);
                        }
                    }
                    for (int k = 0; k < liIndexes.size(); k = k + 2) {
                        idlist.add(formu.substring(liIndexes.get(k) + 1, liIndexes.get(k + 1)));
                    }
                    for (Iterator<String> iterator = idlist.iterator(); iterator.hasNext();) {
                        String next = iterator.next();
                        if (next.equalsIgnoreCase(basicId)) {
                            formu = formu.replaceAll("#" + basicId + "#", basicAmountInString);
                        } else if (next.equalsIgnoreCase(gradePayId)) {
                            formu = formu.replaceAll("#" + gradePayId + "#", gradepayAmountInString);
                        } else {
                            SalaryHead sal = new Gson().fromJson(new SalaryHeadManager().fetchRawData(next), new TypeToken<SalaryHead>() {
                            }.getType());
                            formu = formu.replaceAll("#" + next + "#", getHeadAmount(sal, basicId, basicAmount, gradePay, empGetHeads, gradePayId, per, "") + "");
//                            formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(sal.getFormula()).getHiddenformula());
                        }
                        condition = false;
                        char[] chrArrayForCheck = formu.toCharArray();
                        for (int i = 0; i < chrArrayForCheck.length; i++) {
                            char c = chrArrayForCheck[i];
                            if (c == '#') {
                                condition = true;
                                break;
                            }
                        }
                    }
                }
                result = ExpressionCalculatorManager.calculateTheValue(formu);
                return result;
            }
        } else if (fo.getMapping().equalsIgnoreCase(ApplicationConstants.YES)) {
            result = getAmountUsingHeadSlab(fo, basicId, basicAmount, gradePay, empGetHeads, gradePayId, per, conditi);
            return result;
        }
        return result;
    }

    private static double getAmountUsingHeadSlab(SalaryHead fo, String basicId, long basicAmount, long gradePay, Employee empGetHeads, String gradePayId, int pers, String conditi) throws Exception {
        double returnValue = 0;
        DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.HEAD_SLAB_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        String salaryHeadId = ((LinkedTreeMap<String, String>) fo.getId()).get("$oid");
        regexQuery.put(ApplicationConstants.HEAD_SLAB_HEAD_NAME,
                new BasicDBObject(ApplicationConstants.EQUAL_OPERATOR, salaryHeadId));
        regexQuery.put("fromGPLong",
                new BasicDBObject(ApplicationConstants.LTE_OPERATOR, gradePay));
        regexQuery.put("toGPLong",
                new BasicDBObject(ApplicationConstants.GTE_OPERATOR, gradePay));
        regexQuery.put("fromBasicLong",
                new BasicDBObject(ApplicationConstants.LTE_OPERATOR, basicAmount));
        regexQuery.put("toBasicLong",
                new BasicDBObject(ApplicationConstants.GTE_OPERATOR, basicAmount));
        regexQuery.put(ApplicationConstants.STATUS,
                new BasicDBObject(ApplicationConstants.EQUAL_OPERATOR, "Active"));
        DBCursor cursor = collection.find(regexQuery);
        List<HeadSlab> headsSlabList = new ArrayList<HeadSlab>();
        HeadSlab headSlabForIteration = null;
        while (cursor.hasNext()) {
            DBObject ob = cursor.next();
            Type type = new TypeToken<HeadSlab>() {
            }.getType();
            headSlabForIteration = new Gson().fromJson(ob.toString(), type);
            headsSlabList.add(headSlabForIteration);
        }
        if (headsSlabList != null) {
            HeadSlab headSlab = basedOnQuery(headsSlabList, empGetHeads);
            if (headSlab != null) {
                String amountOrFormula = headSlab.getTypeTwo();
                if (amountOrFormula.equalsIgnoreCase(ApplicationConstants.IS_FORMULA)) {
                    String formulaId = headSlab.getFormulaTwo();
                    String formula = DBManager.getDbConnection().fetch(ApplicationConstants.FORMULA_TABLE, formulaId);
                    List<Formula> formuList = null;
                    if (formula != null) {
                        formuList = new Gson().fromJson(formula, new TypeToken<List<Formula>>() {
                        }.getType());
                    } else {
                        return returnValue;
                    }
                    Formula forobj = formuList.get(0);
                    String formu = forobj.getHiddenformula();
                    List<Integer> liIndexes = new ArrayList<Integer>();
                    List<String> idlist = new ArrayList<String>();
                    boolean condition = true;
                    while (condition) {
                        liIndexes.clear();
                        idlist.clear();
                        condition = false;
                        char[] chrArray = formu.toCharArray();
                        int j = 0;
                        for (int i = 0; i < chrArray.length; i++) {
                            char c = chrArray[i];
                            if (c == '#') {
                                liIndexes.add(i);
                            }
                        }
                        for (int k = 0; k < liIndexes.size(); k = k + 2) {
                            idlist.add(formu.substring(liIndexes.get(k) + 1, liIndexes.get(k + 1)));
                        }
                        for (Iterator<String> iterator = idlist.iterator(); iterator.hasNext();) {
                            String next = iterator.next();
                            if (next.equalsIgnoreCase(basicId)) {
                                formu = formu.replaceAll("#" + basicId + "#", basicAmount + "");
                            } else {
                                SalaryHead sal = new Gson().fromJson(new SalaryHeadManager().fetchRawData(next), new TypeToken<SalaryHead>() {
                                }.getType());
                                formu = formu.replaceAll("#" + next + "#", getHeadAmount(sal, basicId, basicAmount, gradePay, empGetHeads, gradePayId, pers, "") + "");
//                            formu = formu.replaceAll("#" + next + "#", new FormulaManager().fetchFormula(sal.getFormula()).getHiddenformula());
                            }
                            condition = false;
                            char[] chrArrayForCheck = formu.toCharArray();
                            for (int i = 0; i < chrArrayForCheck.length; i++) {
                                char c = chrArrayForCheck[i];
                                if (c == '#') {
                                    condition = true;
                                    break;
                                }
                            }
                        }
                    }
                    returnValue = ExpressionCalculatorManager.calculateTheValue(formu);
                    long maximumAmount = headSlab.getMaximumAmountLong();
                    long minimumAmount = headSlab.getMinimumAmountLong();
                    if (returnValue >= minimumAmount && returnValue <= maximumAmount) {
                        return returnValue;
                    } else if (returnValue < minimumAmount) {
                        return minimumAmount;
                    } else if (returnValue > maximumAmount) {
                        return maximumAmount;
                    }
                    return returnValue;
                } else {
                    returnValue = headSlab.getAmountLong();
                    if (conditi.equalsIgnoreCase("delete")) {
                        returnValue = returnValue * 100 / (100 + pers);
                    } else {
                        returnValue = returnValue + returnValue * pers / 100;
                    }

                    return returnValue;
                }
            } else {
                return 0;
            }
        }
        return 0;
    }

    public static SalaryHead getsalryhead(String primaryKey) throws Exception {
        SalaryHead salaryHead = null;
        if (primaryKey == null || primaryKey.isEmpty()) {
            return salaryHead;
        }
        String descriptionDetails = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, primaryKey);

        if (descriptionDetails != null && !descriptionDetails.isEmpty()) {
            List<SalaryHead> list = new Gson().fromJson(descriptionDetails, new TypeToken<List<SalaryHead>>() {
            }.getType());
            salaryHead = list.get(0);
        }
        return salaryHead;
    }

    public static double round(String fo, double num) throws Exception {
        double multipleOf = 0.0;
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, fo);
        List<SalaryHead> list = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        SalaryHead saldata = list.get(0);
        if (saldata.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_NONE)) {
            return num;
        } else if (saldata.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_50_PAISE)) {
            multipleOf = 0.5;
        } else if (saldata.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_1_RUPEE)) {
            multipleOf = 1.0;
        } else if (saldata.getRounding().equalsIgnoreCase(ApplicationConstants.ROUNDING_10_RUPEE)) {
            multipleOf = 10.0;
        } else {
            return num;
        }
        double dd = Math.floor((num + multipleOf / 2) / multipleOf) * multipleOf;
        DecimalFormat df = new DecimalFormat("#.00");
        //System.out.println(df.format(676.0));
        return dd;
    }

    public boolean delete(SalaryIncrementDateWise salaryIncDTO, String userid) throws Exception {

        if (salaryIncDTO.getSalincrowid().equalsIgnoreCase("") || salaryIncDTO.getSalincrowid().equalsIgnoreCase("null")) {
            return false;
        }
        String status = null;
        boolean finalresult = false;
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_INCREMENT_DATE_WISE, salaryIncDTO.getSalincrowid());
        if (result.equalsIgnoreCase("null") || result.equalsIgnoreCase("")) {
            return false;
        }
        List<SalaryIncrementDateWise> salaryinlist = new Gson().fromJson(result, new TypeToken<List<SalaryIncrementDateWise>>() {
        }.getType());
        List<SalaryIncrementDateWise> salaryincfinList = new ArrayList<SalaryIncrementDateWise>();
        salaryincfinList.add(salaryinlist.get(0));
        for (SalaryIncrementDateWise salaryHeadList : salaryincfinList) {
            Employee emp = new EmployeeManager().fetchEmployee(salaryIncDTO.getEmprowid());
            emp.setDeductionHeads(salaryHeadList.getDeductionHeads());
            emp.setEarningHeads(salaryHeadList.getEarningHeads());
            emp.setTotalEarnings(salaryHeadList.getTotalEarnings());
            emp.setTotalDeductions(salaryHeadList.getTotalDeductions());
            emp.setBasic(salaryHeadList.getBasic());
            emp.setSalaryIncId("");
            emp.setUpdateDate(System.currentTimeMillis() + "");
            status = new EmployeeManager().update(emp, salaryIncDTO.getEmprowid(), userid);
        }

//        String increpre = salaryIncDTO.getIncrementPercentage();
////        String increpre = empresult.getIncrementPercentage();
//        //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$" + increpre);
//        //get employee details     
//        if (increpre != null && increpre != "") {
//            try {
//                int increPer = Integer.parseInt(increpre);
//                Employee emp = new EmployeeManager().fetchEmployee(salaryIncDTO.getEmprowid());
//                long intbasic = emp.getBasic();
//                //   int intbasic = Integer.parseInt(basic);
//                intbasic = intbasic * 100 / (100 + increPer);
//                String basicSalaryHeadId = null;
//                String gradepaySalaryHeadId = null;
//                double Totalearnings = 0.0;
//                double Totaldeductions = 0.0;
//                long gradePay = emp.getGradePay();
//                SalaryHead basicEarningHead = new EmployeeManager().fetchBasicEarningHead();
//                SalaryHead gardepayEarningHead = new EmployeeManager().fetchGradepayEarningHead();
//
//                if (basicEarningHead != null) {
//                    basicSalaryHeadId = ((LinkedTreeMap<String, String>) basicEarningHead.getId()).get("$oid");
//                }
//                if (gardepayEarningHead != null) {
//                    gradepaySalaryHeadId = ((LinkedTreeMap<String, String>) gardepayEarningHead.getId()).get("$oid");
//                }
//                List<EarningHeadsDetails> earHeadsList = new ArrayList<EarningHeadsDetails>(emp.getEarningHeads());
//                for (EarningHeadsDetails earingHeads : earHeadsList) {
//                    if (!earingHeads.getDescription().equalsIgnoreCase(basicSalaryHeadId)) {
//                        boolean headcondition = new SalaryIncrementDateWiseManager().checkHeadCondition(earingHeads.getDescription());
//                        if (headcondition) {
//                            SalaryHead fo = getsalryhead(earingHeads.getDescription());
//                            double headamount = getHeadAmount(fo, basicSalaryHeadId, intbasic, gradePay, emp, gradepaySalaryHeadId, increPer, "delete");
//                            earingHeads.setAmount(headamount);
//                            Totalearnings = Totalearnings + headamount;
//                        } else {
//                            Totalearnings = Totalearnings + earingHeads.getAmount();
//                        }
//                    } else {
//                        earingHeads.setAmount(intbasic);
//                        Totalearnings = Totalearnings + intbasic;
//                    }
//
//                }
//                List<EarningHeadsDetails> dedHeadsList = new ArrayList<EarningHeadsDetails>(emp.getDeductionHeads());
//                for (EarningHeadsDetails deducHeads : dedHeadsList) {
//                    boolean headcondition = new SalaryIncrementDateWiseManager().checkHeadCondition(deducHeads.getDescription());
//                    if (headcondition) {
//                        double headamount = deducHeads.getAmount();
//                        headamount = headamount * 100 / (100 + increPer);
//                        headamount = round(deducHeads.getDescription(), headamount);
//                        deducHeads.setAmount(headamount);
//                        Totaldeductions = Totaldeductions + headamount;
//                    } else {
//                        Totaldeductions = Totaldeductions + deducHeads.getAmount();
//                    }
//                }
//                emp.setDeductionHeads(dedHeadsList);
//                emp.setEarningHeads(earHeadsList);
//                emp.setTotalEarnings(Totalearnings);
//                emp.setTotalDeductions(Totaldeductions);
//                emp.setBasic(intbasic);
//                emp.setUpdateDate(System.currentTimeMillis() + "");
//                String status = new EmployeeManager().update(emp, salaryIncDTO.getEmprowid(), userid);
//
//            } catch (Exception e) {
//            }
//        }
        boolean salresult = DBManager.getDbConnection().deleteDocument(ApplicationConstants.SALARY_INCREMENT_DATE_WISE, salaryIncDTO.getSalincrowid());

        if (status != null && salresult == true) {
            finalresult = true;
        } else {
            finalresult = false;
        }
        return finalresult;
    }
//  calculateOnIncrement

    public static void main(String args[]) throws Exception {
        boolean result = new SalaryIncrementDateWiseManager().checkHeadCondition("582f37100c92ec57796a1e3d");
        //System.out.println("final result" + result);
//        //  new SalaryIncrementDateWiseManager().converter("01/01/2016", "31/12/2016", "25/06/2016");
    }

    private boolean checkHeadCondition(String id) throws Exception {
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.SALARY_HEAD_TABLE, id);
        boolean finalresult = false;
        List<SalaryHead> salaryheadList = new Gson().fromJson(result, new TypeToken<List<SalaryHead>>() {
        }.getType());
        List<SalaryHead> salaryheadList1 = new ArrayList<SalaryHead>();
        salaryheadList1.add(salaryheadList.get(0));
        for (SalaryHead salaryHeadList : salaryheadList1) {
            if (salaryHeadList.getCalculateOnIncrement().equalsIgnoreCase("Yes") && !salaryHeadList.getDescription().equalsIgnoreCase(ApplicationConstants.GRADE_PAY)) {
                finalresult = true;
            }
        }
        return finalresult;
    }
}
