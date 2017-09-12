package com.accure.payroll.manager;

import com.accure.budget.manager.FundTypeManager;
import com.accure.db.in.DAO;
import com.accure.finance.manager.DDOManager;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeeSalary;
import com.accure.hrms.manager.BudgetHeadMappingManager;
import com.accure.hrms.manager.DepartmentManager;
import com.accure.hrms.manager.DesignationManager;
import com.accure.hrms.manager.EmployeeManager;
import com.accure.hrms.manager.GradeManager;
import com.accure.hrms.manager.PFTypeManager;
import com.accure.leave.manager.LeaveEncashmentManager;
import com.accure.payroll.dto.ArrearProcess;
import com.accure.payroll.dto.AttendanceAdj;
import com.accure.payroll.dto.AttendanceAdjListDto;
import com.accure.payroll.dto.AutoSalaryProcess;
import com.accure.payroll.dto.Deductions;
import com.accure.payroll.dto.Earnings;
import com.accure.payroll.dto.EmpAttendance;
import com.accure.payroll.dto.InsuranceTransactions;
import com.accure.payroll.dto.LoanPayment;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.common.manager.RestClient;
import com.accure.usg.server.utils.ApplicationConstants;
import static com.accure.usg.server.utils.Common.getConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author upendra/chaitu
 */
public class AutoSalaryProcessManager {

    final static RestClient aql = new RestClient();
    final static String autoSalaryTable = ApplicationConstants.USG_DB1 + ApplicationConstants.AUTO_SALARY_PROCESS_TABLE + "`";
    final static String attendanceTable = ApplicationConstants.USG_DB1 + ApplicationConstants.EMP_ATTENDANCE_TABLE + "`";

    public String insert(AutoSalaryProcess autosalaryDTO) throws Exception {
        if (autosalaryDTO == null) {
            return null;
        }
        autosalaryDTO.setCreateDate(System.currentTimeMillis() + "");
        autosalaryDTO.setUpdateDate(System.currentTimeMillis() + "");
        autosalaryDTO.setStatus(ApplicationConstants.ACTIVE);
        DAO dao = DBManager.getDbConnection();
        String autosalaryJsonresult = dao.insert(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, new Gson().toJson(autosalaryDTO));
        return autosalaryJsonresult;
    }

    /**
     * @author chaitu
     * @description update() method will update the AutoSalaryProcess record
     * @param AutoSalaryProcess asp
     * @param String primaryKey
     * @return boolean
     * @throws Exception
     */
    public static boolean update(AutoSalaryProcess asp, String primaryKey) throws Exception {
        boolean status = false;
        if (primaryKey == null) {
            return status;
        }

        asp.setId(null);
        asp.setIdStr(null);
        asp.setUpdateDate(Long.toString(System.currentTimeMillis()));
        status = DBManager.getDbConnection().update(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, primaryKey, new Gson().toJson(asp));
        return status;
    }

    /**
     * @description fetch() method will expect input String (primaryKey) then it
     * will returns the AutoSalaryProcess object or null .
     *
     * Ex:-fetch("primaryKey")
     *
     * @author chaitu
     * @param String
     * @return AutoSalaryProcess
     * @throws java.lang.Exception
     */
    public static AutoSalaryProcess fetch(String primaryKey) throws Exception {
        AutoSalaryProcess asp = null;
        if (primaryKey == null) {
            return asp;
        }
        String dbOutput = DBManager.getDbConnection().fetch(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, primaryKey);
        if (dbOutput != null) {
            ArrayList<AutoSalaryProcess> resultList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<AutoSalaryProcess>>() {
            }.getType());
            asp = resultList.get(0);
        }
        return asp;
    }

    /**
     * @description fetch() method will expect input as HashMap then it returns
     * the AutoSalaryProcess object or null .
     *
     * @author chaitu
     * @param HashMap<String, String>
     * @return AutoSalaryProcess
     * @throws java.lang.Exception
     */
    public static AutoSalaryProcess fetch(HashMap<String, String> columnCondition) throws Exception {
        AutoSalaryProcess asp = null;
        if (columnCondition == null || columnCondition.size() == 0) {
            return asp;
        }

        String dbOutput = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, columnCondition);
        if (dbOutput != null) {
            ArrayList<AutoSalaryProcess> resultList = new Gson().fromJson(dbOutput, new TypeToken<ArrayList<AutoSalaryProcess>>() {
            }.getType());
            asp = resultList.get(0);
        }
        return asp;
    }

    public String search(String ddo, String Month, String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("ddo", ddo);
        conditionMap.put("month", Month);
        conditionMap.put("year", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, conditionMap);
        return result;
    }

    public List<AutoSalaryProcess> searchforExport(String ddo, String Month, String year, Employee emp) throws Exception {
       DB db = DBManager.getDB();
        DBCollection collection = db.getCollection(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE);
        BasicDBObject regexQuery = new BasicDBObject();
        if (emp.getDdo() != null) {
            regexQuery.put("ddo",
                    new BasicDBObject("$regex", ddo));
        }

        if (!emp.getDepartment().equals("0")) {
            regexQuery.put("salaryList.department",
                    new BasicDBObject("$regex", emp.getDepartment()));

        }
        if (!emp.getDesignation().equals("0")) {
            regexQuery.put("salaryList.designation",
                    new BasicDBObject("$regex", emp.getDesignation()));
        }
        if (!emp.getNatureType().equals("0")) {
            regexQuery.put("nature",
                    new BasicDBObject("$regex", emp.getNatureType()));
        }

        if (!emp.getFundType().equals("0")) {
            regexQuery.put("fundType",
                    new BasicDBObject("$regex", emp.getFundType()));
        }

        if (!emp.getBudgetHead().equals("0") || emp.getBudgetHead() != null) {
            regexQuery.put("budgetHead",
                    new BasicDBObject("$regex", emp.getBudgetHead()));
        }
        if (emp.getEmployeeName() != null || !emp.getEmployeeName().isEmpty()) {
            regexQuery.put("salaryList.employeeName",
                    new BasicDBObject("$regex", emp.getEmployeeName()));
        }
        if (emp.getEmployeeCodeM() != null && !emp.getEmployeeCode().isEmpty()) {
            regexQuery.put("salaryList.employeeCodeM",
                    new BasicDBObject("$regex", emp.getEmployeeCodeM()));
        }
        regexQuery.put("status",
                new BasicDBObject("$regex", "Active"));

        regexQuery.put("month",
                new BasicDBObject("$regex", Month));
        regexQuery.put("year",
                new BasicDBObject("$regex", year));

        DBCursor cursor = collection.find(regexQuery);
        ArrayList<AutoSalaryProcess> list = new ArrayList<AutoSalaryProcess>();

        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            Type type = new TypeToken<AutoSalaryProcess>() {
            }.getType();
            AutoSalaryProcess em = new Gson().fromJson(obj.toString(), type);
            list.add(em);
        }

        return list;

    }

    public String searchsalaryAttendance(String code, String Month, String year) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("month", Month);
        conditionMap.put("year", year);
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);

        String result = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMP_ATTENDANCE_ADJ_TABLE, conditionMap);
        List<AttendanceAdj> salResult = new Gson().fromJson(result, new TypeToken<List<AttendanceAdj>>() {
        }.getType());
        String fresult = null;
        String i = "";
        for (AttendanceAdj cl : salResult) {
            for (AttendanceAdjListDto dl : cl.getAttendanceList()) {
                i = dl.getEmpCodeM();
                if (i.equals(code)) {
                    fresult = dl.getSl();
                }
            }
        }

        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("employeeCode", code);
        String salresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMP_SALARY_TABLE, condition);
        List<EmployeeSalary> atResult = new Gson().fromJson(salresult, new TypeToken<List<EmployeeSalary>>() {
        }.getType());

        String totalearn = null;
        String totaldedu = null;
        int totaldays = 30;
        for (EmployeeSalary empsa : atResult) {
            totalearn = empsa.getTotalEarnings();
            totaldedu = empsa.getTotalDeductions();
            int dedcon = Integer.parseInt(totaldedu);
            int salray = Integer.parseInt(totalearn);
            int perearn = salray / totaldays;
            int lwp = Integer.parseInt(fresult);
            int totaldet = perearn * lwp;
            salray = salray - totaldet;
            int finaldedu = dedcon + totaldet;
            String findedconv = Integer.toString(finaldedu);
            String totearn = Integer.toString(salray);
            empsa.setTotalEarnings(totearn);
            empsa.setTotalDeductions(findedconv);
        }

        return new Gson().toJson(atResult);

    }

    /**
     * @description getSalarySearchResult() method will do search criteria based
     * on UI inputs. It will return the "processed","notprocessed" employees
     * details in HashMap format.
     *
     * @author chaitu
     * @param String json
     * @param boolean lockStatus
     * @return HashMap
     * @throws Exception
     */
    private ArrayList<EmpAttendance> getSalarySearchResult(AutoSalaryProcess asp, boolean lockStatus, String salaryProcessType) throws Exception {
        if (asp == null) {
            return null;
        }
        String attendanceQuery = "select atten._id as idStr,atten.employeeCode,atten.employeeName,atten.location,atten.department,atten.designation,atten.salaryType"
                + " from " + attendanceTable + " as atten where"
                + " atten.year=" + asp.getYear()
                + " and atten.month=" + asp.getMonth()
                + " and atten.status=\"" + ApplicationConstants.ACTIVE + "\"";

        if (salaryProcessType != null && salaryProcessType.equalsIgnoreCase(ApplicationConstants.PAY_STOP_SALARY)) {
            attendanceQuery = attendanceQuery + " and atten.stopSalary=\"Yes\"";
            attendanceQuery = attendanceQuery + " and atten.lockStatus=true";
            attendanceQuery = attendanceQuery + " and atten.payStopSalaryProcessed=" + lockStatus;
        } else {
            attendanceQuery = attendanceQuery + " and atten.lockStatus=" + lockStatus;
        }
        if (asp.getEmployeeCode() != null && !asp.getEmployeeCode().isEmpty()) {
            attendanceQuery = attendanceQuery + " and atten.employeeCode=\"" + asp.getEmployeeCode() + "\"";
        }
        if (asp.getDdo() != null && !asp.getDdo().isEmpty() && !asp.getDdo().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.ddo=\"" + asp.getDdo() + "\"";
        }
        if (asp.getLocation() != null && !asp.getLocation().isEmpty() && !asp.getLocation().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.location=\"" + asp.getLocation() + "\"";
        }
        if (asp.getDepartment() != null && !asp.getDepartment().isEmpty() && !asp.getDepartment().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.department=\"" + asp.getDepartment() + "\"";
        }
        if (asp.getDesignation() != null && !asp.getDesignation().isEmpty() && !asp.getDesignation().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.designation=\"" + asp.getDesignation() + "\"";
        }
        if (asp.getNatureType() != null && !asp.getNatureType().isEmpty() && !asp.getNatureType().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.natureType=\"" + asp.getNatureType() + "\"";
        }
        if (asp.getPostingCity() != null && !asp.getPostingCity().isEmpty() && !asp.getPostingCity().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.postingCity=\"" + asp.getPostingCity() + "\"";
        }
        if (asp.getPfType() != null && !asp.getPfType().isEmpty() && !asp.getPfType().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.pfType=\"" + asp.getPfType() + "\"";
        }
        if (asp.getFundType() != null && !asp.getFundType().isEmpty() && !asp.getFundType().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.fundType=\"" + asp.getFundType() + "\"";
        }
        if (asp.getBudgetHead() != null && !asp.getBudgetHead().isEmpty() && !asp.getBudgetHead().equals("0")) {
            attendanceQuery = attendanceQuery + " and atten.budgetHead=\"" + asp.getBudgetHead() + "\"";
        }

        String attendanceQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, attendanceQuery);
        ArrayList<EmpAttendance> attenList = null;
        if (attendanceQueryOutput != null && !attendanceQueryOutput.isEmpty() && !attendanceQueryOutput.equals("[]")) {
            attenList = new Gson().fromJson(attendanceQueryOutput, new TypeToken<ArrayList<EmpAttendance>>() {
            }.getType());

            for (EmpAttendance attn : attenList) {
//                attn.setDdo(new DDOManager().fetch(attn.getDdo()).getDdoName());
                attn.setLocation(new DDOManager().fetchAllLocation().get(attn.getLocation()));
                attn.setDepartment(new DepartmentManager().fetch(attn.getDepartment()).getDepartment());
                attn.setDesignation(new DesignationManager().get(attn.getDesignation()).getDesignation());
            }
        }
        return attenList;
    }

    /**
     * @author chaitu
     * @param AutoSalaryProcess
     * @return ArrayList<AutoSalaryProcess>
     * @throws Exception
     */
    private ArrayList<AutoSalaryProcess> getSalaryProcessed(AutoSalaryProcess asp, String salaryProcessType) throws Exception {
        ArrayList<AutoSalaryProcess> processedListFromSalary = null;

        //get data from attendance table with the condition of lockStatus=true
        String processedEmpCodes = "";
        ArrayList<EmpAttendance> processedList = getSalarySearchResult(asp, true, salaryProcessType);
        if (processedList != null && processedList.size() > 0) {
            for (EmpAttendance ea : processedList) {
                processedEmpCodes = processedEmpCodes + "\"" + ea.getEmployeeCode() + "\",";
            }
            if (processedEmpCodes != null && !processedEmpCodes.isEmpty()) {
                processedEmpCodes = "(" + processedEmpCodes.substring(0, processedEmpCodes.length() - 1) + ")";
            }

            //get data from already processed salary table with condition status=Processed
            String salaryProcessedQuery = "select _id as idStr,employeeCode,employeeName,locationName,departmentName,designationName,salaryType,status"
                    + " from " + autoSalaryTable
                    + " where isLocked=false"
                    + " and autoGeneratedBillNumber=null"
                    + " and month=" + asp.getMonth()
                    + " and year=" + asp.getYear()
                    + " and status=\"" + ApplicationConstants.PROCESSED + "\""
                    + " and salaryProcessType=\"" + salaryProcessType + "\""
                    + " and employeeCode in " + processedEmpCodes;

            String salaryProcessedOutput = aql.getRestData(ApplicationConstants.END_POINT, salaryProcessedQuery);
            if (salaryProcessedOutput != null && !salaryProcessedOutput.isEmpty() && !salaryProcessedOutput.equals("[]")) {
                processedListFromSalary = new Gson().fromJson(salaryProcessedOutput, new TypeToken<ArrayList<AutoSalaryProcess>>() {
                }.getType());
            }
        }
        return processedListFromSalary;
    }

    /**
     * @author chaitu
     * @param AutoSalaryProcess asp
     * @return HashMap
     * @throws Exception
     */
    private ArrayList<AutoSalaryProcess> getSalaryLocked(AutoSalaryProcess asp, String salaryProcessType) throws Exception {
        if (asp == null) {
            return null;
        }

        String autoSalaryQuery = "select autos._id as idStr,autos.employeeCode,autos.employeeName,autos.locationName,autos.departmentName,autos.designationName,autos.salaryType"
                + " from " + autoSalaryTable + " as autos where"
                + " autos.isLocked=true"
                + " and autos.year=" + asp.getYear()
                + " and autos.month=" + asp.getMonth()
                + " and autos.salaryProcessType=\"" + salaryProcessType + "\"";

        if (asp.getEmployeeCode() != null && !asp.getEmployeeCode().isEmpty()) {
            autoSalaryQuery = autoSalaryQuery + " and autos.employeeCode=\"" + asp.getEmployeeCode() + "\"";
        }
        if (asp.getDdo() != null && !asp.getDdo().isEmpty() && !asp.getDdo().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.ddo=\"" + asp.getDdo() + "\"";
        }
        if (asp.getLocation() != null && !asp.getLocation().isEmpty() && !asp.getLocation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.location=\"" + asp.getLocation() + "\"";
        }
        if (asp.getDepartment() != null && !asp.getDepartment().isEmpty() && !asp.getDepartment().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.department=\"" + asp.getDepartment() + "\"";
        }
        if (asp.getDesignation() != null && !asp.getDesignation().isEmpty() && !asp.getDesignation().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.designation=\"" + asp.getDesignation() + "\"";
        }
        if (asp.getNatureType() != null && !asp.getNatureType().isEmpty() && !asp.getNatureType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.natureType=\"" + asp.getNatureType() + "\"";
        }
        if (asp.getPostingCity() != null && !asp.getPostingCity().isEmpty() && !asp.getPostingCity().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.postingCity=\"" + asp.getPostingCity() + "\"";
        }
        if (asp.getPfType() != null && !asp.getPfType().isEmpty() && !asp.getPfType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.pfType=\"" + asp.getPfType() + "\"";
        }
        if (asp.getFundType() != null && !asp.getFundType().isEmpty() && !asp.getFundType().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.fundType=\"" + asp.getFundType() + "\"";
        }
        if (asp.getBudgetHead() != null && !asp.getBudgetHead().isEmpty() && !asp.getBudgetHead().equals("0")) {
            autoSalaryQuery = autoSalaryQuery + " and autos.budgetHead=\"" + asp.getBudgetHead() + "\"";
        }

        String autoSalaryQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, autoSalaryQuery);
        ArrayList<AutoSalaryProcess> attenList = null;
        if (autoSalaryQueryOutput != null && !autoSalaryQueryOutput.isEmpty() && !autoSalaryQueryOutput.equals("[]")) {
            attenList = new Gson().fromJson(autoSalaryQueryOutput, new TypeToken<ArrayList<AutoSalaryProcess>>() {
            }.getType());
        }
        return attenList;
    }

    /**
     * @description viewSalary() method will do search criteria based on UI
     * inputs. It will return the "processed","notprocessed" employees details
     * in HashMap format.
     * @author chaitu
     * @param String json
     * @return HashMap
     * @throws Exception
     */
    public HashMap viewSalary(String salaryViewJson, String salaryProcessType) throws Exception {
        if (salaryViewJson == null) {
            return null;
        }
        HashMap outputMap = new HashMap();
        //Json from UI
        AutoSalaryProcess asp = new Gson().fromJson(salaryViewJson, new TypeToken<AutoSalaryProcess>() {
        }.getType());

        //get not processed salary list from attendance table with the condition of lockStatus=false
        ArrayList<EmpAttendance> notProcessedList = getSalarySearchResult(asp, false, salaryProcessType);
        if (notProcessedList != null && notProcessedList.size() > 0) {
            outputMap.put("notprocessed", notProcessedList);
        } else {
            outputMap.put("notprocessed", ApplicationConstants.NO_DATA_FOUND);
        }

        //get processed salary list from attendance & autosalary table
        ArrayList<AutoSalaryProcess> processedList = getSalaryProcessed(asp, salaryProcessType);
        if (processedList != null && processedList.size() > 0) {
            outputMap.put("processed", processedList);
        } else {
            outputMap.put("processed", ApplicationConstants.NO_DATA_FOUND);
        }

        //get locked salary list from salary processed table
        ArrayList<AutoSalaryProcess> lockedList = getSalaryLocked(asp, salaryProcessType);
        if (lockedList != null && lockedList.size() > 0) {
            outputMap.put("lockedlist", lockedList);
        } else {
            outputMap.put("lockedlist", ApplicationConstants.NO_DATA_FOUND);
        }
        return outputMap;
    }

    /**
     * @description processSalary() method will process employee salary
     * @author chaitu
     * @param String[] empCodes
     * @param String month
     * @param String year
     * @param String processedBy
     * @return HashMap
     * @throws Exception
     */
    public static HashMap processSalary(String[] empCodes, String monthStr, String yearStr, String processedBy, String salaryProcessType, String payMonth, String payYear) throws Exception {
        if (empCodes == null || monthStr == null || yearStr == null || processedBy == null || salaryProcessType == null || salaryProcessType.isEmpty()) {
            return null;
        }

        HashMap result = new HashMap();
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        //fetching employees
        ArrayList<Employee> empList = EmployeeManager.fetchEmployees(empCodes);
        if (empList != null && empList.size() > 0) {
            //processing each employee salary
            for (Employee emp : empList) {
                String empCode = emp.getEmployeeCode();
                result.put(empCode, ApplicationConstants.HTTP_STATUS_FAIL);

                //if employee don't have earning heads & deduction heads don't process salary
                if (emp.getEarningHeads() == null || emp.getEarningHeads().size() == 0) {
                    continue;
                }

                AutoSalaryProcess asp = new AutoSalaryProcess();
                //get employee attendance based on month & year & set
                EmpAttendance attendance = EmpAttendanceManager.getEmployeeAttendance(empCode, month, year);
                String stopSalary = attendance.getStopSalary();
                if (salaryProcessType.equalsIgnoreCase(ApplicationConstants.PAY_STOP_SALARY)) {
                    stopSalary = "No";
                }
                asp.setAttendance(attendance);

                //get earnings & set
                Earnings earnsInfo = AutoSalaryEarningsManager.getEmployeeEarnings(emp, attendance, month, year, stopSalary);
                asp.setEarningsInfo(earnsInfo);

                //get deductions & set
                Deductions dedsInfo = AutoSalaryDeductionsManager.getEmployeeDeductions(emp, attendance, month, year, stopSalary);
                asp.setDeductionsInfo(dedsInfo);

                //get arrear & set 
                ArrearProcess arrear = AutoSalaryOtherManager.getEmployeeArrearProcess(empCode, month, year, stopSalary);
                if (arrear != null) {
                    asp.setIsArrear(true);
                    asp.setArrear(arrear);
                } else {
                    asp.setIsArrear(false);
                }

//                //update flag in arrear table
//                if (asp.isIsArrear() && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
//                    ArrearProcessManager.update(arrear.getIdStr(), true);
//                }
                //update the flag in incometaxprocess table
                if (dedsInfo.isIsIncomeTax() && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                    IncomeTaxManager.update(dedsInfo.getIncomeTax().getIdStr(), true);
                }

                //update the flag in empattendance table
                if (attendance.getIdStr() != null && !attendance.getIdStr().isEmpty()) {
                    EmpAttendanceManager.update(attendance.getIdStr(), true, salaryProcessType);
                }

                //update the flag in leave encashment
                if (earnsInfo.isIsLeaveEncashment() && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                    LeaveEncashmentManager.update(earnsInfo.getLeaveEncashment().getIdStr(), true);
                }

                //get other meta data info & set
                asp.setMonth(month);
                asp.setYear(year);
                if (payMonth != null) {
                    asp.setPayMonth(Integer.parseInt(payMonth));
                }
                if (payYear != null) {
                    asp.setPayYear(Integer.parseInt(payYear));
                }
                asp.setCreatedBy(processedBy);
                asp.setEmployeePKey(((Map<String, String>) emp.getId()).get("$oid"));
                asp.setEmployeeCodeM(emp.getEmployeeCodeM());
                asp.setEmployeeCode(emp.getEmployeeCode());
                asp.setEmployeeName(emp.getEmployeeName());
                asp.setEmail(emp.getEmail());
                asp.setLocation(emp.getLocation());
                asp.setLocationName(new DDOManager().fetchAllLocation().get(emp.getLocation()));
                asp.setDepartment(emp.getDepartment());
                asp.setDepartmentName(new DepartmentManager().fetch(emp.getDepartment()).getDepartment());
                asp.setDesignation(emp.getDesignation());
                asp.setDesignationName(new DesignationManager().get(emp.getDesignation()).getDesignation());
                asp.setSalaryType(emp.getSalaryType());
                asp.setSalaryPostStatus(emp.getSalaryPostStatus());
                asp.setDdo(emp.getDdo());
                asp.setDdoName(new DDOManager().fetch(emp.getDdo()).getDdoName());
                asp.setNatureType(emp.getNatureType());
                asp.setPostingCity(emp.getPostingCity());
                asp.setPfType(emp.getPfType());
                asp.setPfTypeName(new PFTypeManager().get(emp.getPfType()).getPFType());
                asp.setFundType(emp.getFundType());
                asp.setFundTypeName(new FundTypeManager().fetchAllFundType().get(emp.getFundType()));
                asp.setBudgetHead(emp.getBudgetHead());
                asp.setBudgetHeadName(new BudgetHeadMappingManager().fetch(emp.getBudgetHead()).getBudgetHead());
                asp.setGradeId(emp.getGrade());
                asp.setGradeName(new GradeManager().get(emp.getGrade()).getGradeName());
                asp.setPfNumber(emp.getPfNumber());
                asp.setPanNo(emp.getPanNo());
                asp.setPayMode(emp.getPayMode());
                asp.setAcnumber(emp.getAcnumber());

                //total earnings & deductions & final amount & verify the stop salary flag
                double finalEarnings = AutoSalaryEarningsManager.getTotalEarnings(asp);
                double finalDeductions = AutoSalaryDeductionsManager.getTotalDeductions(asp);
                if (stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.YES)) {
                    finalEarnings = 0.0;
                    finalDeductions = 0.0;
                }
                asp.setEarnings(finalEarnings);
                asp.setDeductions(finalDeductions);
                asp.setFinalPayment((finalEarnings - finalDeductions));

                //tracking info
                asp.setCreatedDate(System.currentTimeMillis());
                asp.setUpdatedDate(System.currentTimeMillis());
                asp.setStatus(ApplicationConstants.PROCESSED);
                asp.setCreatedBy(processedBy);
                asp.setSalaryProcessType(salaryProcessType);

                //verify before salary processing if already processed or not
                AutoSalaryProcess alreadyProcessed = isSalaryProcessed(empCode, month, year, salaryProcessType);
                if (alreadyProcessed == null) {
                    //insert record in salary processed table
                    DAO dao = DBManager.getDbConnection();
                    String insertPKey = dao.insert(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, new Gson().toJson(asp));
                    if (insertPKey != null) {
                        //lock the salary already processed for this employee previous to previous month
                        changeProcessedSalaryLockStatus(empCode, month, year, salaryProcessType, "process");
                        result.put(empCode, ApplicationConstants.HTTP_STATUS_SUCCESS);
                    } else {
                        result.put(empCode, ApplicationConstants.HTTP_STATUS_FAIL);
                    }
                } else {
                    result.put(empCode, ApplicationConstants.SALARY_ALREADY_PROCESSED);
                }
                asp = null;
            }
        }
        return result;
    }

    /**
     * @description unprocessSalary() method will expect inputs then it will
     * unprocess the processed salary & moved to unprocessed salary table.
     * @conditions 1.)fetch processed record using primary key 2.)if insurance
     * history available delete 3.)if loan history is available delete 4.)set
     * the lock status flag false in income tax table 5.)set the lock status
     * flag false in attendance tax table 6.)stored the processed record in
     * unprocess table and delete it.
     *
     * @author chaitu
     * @param String[]
     * @param String
     * @return HashMap
     * @throws java.lang.Exception
     */
    public HashMap unprocessSalary(String[] processedIds, String monthStr, String yearStr, String processedBy, String salaryProcessType, String payMonth, String payYear) throws Exception {
        if (processedIds == null || processedIds.length == 0 || processedBy == null || processedBy.isEmpty() || salaryProcessType == null || salaryProcessType.isEmpty()) {
            return null;
        }
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        HashMap result = new HashMap();
        for (String pid : processedIds) {
            result.put(pid, ApplicationConstants.HTTP_STATUS_FAIL);

            //first fetch the object from already salary processed table
            AutoSalaryProcess asp = fetch(pid);
            if (asp != null) {
                //verify once again if locked flag is false & autoGeneratedBillNumber=null then only we can unprocess
                if (!asp.isIsLocked() && asp.getAutoGeneratedBillNumber() == null) {
                    DAO dao = DBManager.getDbConnection();
                    EmpAttendance attendance = asp.getAttendance();
                    String stopSalary = attendance.getStopSalary();
                    if (salaryProcessType.equalsIgnoreCase(ApplicationConstants.PAY_STOP_SALARY)) {
                        stopSalary = "No";
                    }
                    Deductions deds = asp.getDeductionsInfo();
                    Earnings earns = asp.getEarningsInfo();

                    //delete the insurance history record in insuranceTransactionHistory table
                    if (deds.isIsInsurance() && deds.getInsurance() != null && deds.getInsurance().size() > 0 && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                        //update the total contributions
                        for (InsuranceTransactions its : deds.getInsurance()) {
                            if (its.getTotalContribution() > 0) {
                                InsuranceTransactionManager.updateTotalContributions((String) its.getId(), -1);
                            }
                            dao.delete(ApplicationConstants.INSURANCE_TRANSACTIONS_HISTORY_TABLE, its.getIdStr());
                        }
                    }

                    //delete the loan records in loanpayment table
                    if (deds.isIsLoan() && deds.getLoan() != null && deds.getLoan().size() > 0 && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                        for (LoanPayment lp : deds.getLoan()) {
                            dao.delete(ApplicationConstants.LOAN_PAYMENT_TABLE, lp.getIdStr());
                        }
                    }

//                    //unlock arrear flag in arrearprocess table
//                    if (asp.isIsArrear() && asp.getArrear() != null && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
//                        ArrearProcessManager.update(asp.getArrear().getIdStr(), false);
//                    }
                    //unlock the flag in incometaxprocess table
                    if (deds.isIsIncomeTax() && deds.getIncomeTax() != null && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                        IncomeTaxManager.update(deds.getIncomeTax().getIdStr(), false);
                    }

                    //unlock the flag in empattendance table
                    if (asp.getAttendance() != null && stopSalary != null) {
                        EmpAttendanceManager.update(asp.getAttendance().getIdStr(), false, salaryProcessType);
                    }

                    //update the flag in leave encashment
                    if (earns.isIsLeaveEncashment() && stopSalary != null && stopSalary.equalsIgnoreCase(ApplicationConstants.NO)) {
                        LeaveEncashmentManager.update(earns.getLeaveEncashment().getIdStr(), false);
                    }

                    //other meta data
                    asp.setStatus(ApplicationConstants.UNPROCESSED);
                    asp.setUpdatedBy(processedBy);
                    asp.setUpdatedDate(System.currentTimeMillis());
                    asp.setId(null);
                    asp.setIdStr(null);

                    //insert into unprocessed table
                    String primaryKey = dao.insert(ApplicationConstants.AUTO_SALARY_UNPROCESS_TABLE, new Gson().toJson(asp));
                    if (primaryKey != null) {
                        //unlock the previous to previous month salary
                        changeProcessedSalaryLockStatus(asp.getEmployeeCode(), month, year, salaryProcessType, "unprocess");
                        //delete from processed table
                        boolean isDeleted = dao.delete(ApplicationConstants.AUTO_SALARY_PROCESS_TABLE, pid);
                        if (isDeleted) {
                            result.put(pid, ApplicationConstants.HTTP_STATUS_SUCCESS);
                        } else {
                            result.put(pid, ApplicationConstants.HTTP_STATUS_FAIL);
                        }
                    } else {
                        result.put(pid, ApplicationConstants.HTTP_STATUS_FAIL);
                    }
                } else {
                    result.put(pid, ApplicationConstants.HTTP_STATUS_NOT_ALLOWED);
                }
            } else {
                result.put(pid, ApplicationConstants.SALARY_ALREADY_UNPROCESSED);
            }
        }
        return result;
    }

    public static AutoSalaryProcess isSalaryProcessed(String empCode, int month, int year, String salaryProcessType) throws Exception {
        AutoSalaryProcess processed = null;
        if (empCode == null || month == 0 || year == 0 || salaryProcessType == null) {
            return processed;
        }

        //get data from salary processed table with condition status=Processed
        String salaryProcessedQuery = "select _id as idStr,employeeCode,employeeName,locationName,departmentName,designationName,salaryType,status"
                + " from " + autoSalaryTable
                + " where isLocked=false"
                + " and month=" + month
                + " and year=" + year
                + " and employeeCode=\"" + empCode + "\""
                + " and salaryProcessType=\"" + salaryProcessType + "\""
                + " and status=\"" + ApplicationConstants.PROCESSED + "\"";

        String salaryProcessedOutput = aql.getRestData(ApplicationConstants.END_POINT, salaryProcessedQuery);
        if (salaryProcessedOutput != null && !salaryProcessedOutput.isEmpty() && !salaryProcessedOutput.equals("[]")) {
            processed = new AutoSalaryProcess();
        }
        return processed;
    }

    public static void changeProcessedSalaryLockStatus(String empCode, int month, int year, String salaryProcessType, String operationType) throws Exception {
        if (empCode != null && month != 0 && year != 0 && salaryProcessType != null) {
            String salaryQuery = null;
            //getting previous to previous month & year
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, (month - 3));
            year = calendar.get(Calendar.YEAR);
            month = (calendar.get(Calendar.MONTH) + 1);

            salaryQuery = "select _id as idStr from " + autoSalaryTable
                    + " where month=" + month
                    + " and year=" + year
                    + " and employeeCode=\"" + empCode + "\""
                    + " and salaryProcessType=\"" + salaryProcessType + "\""
                    + " and status=\"" + ApplicationConstants.PROCESSED + "\"";

            if (operationType != null && operationType.equalsIgnoreCase("process")) {
                salaryQuery = salaryQuery + " and isLocked=false";
            } else if (operationType != null && operationType.equalsIgnoreCase("unprocess")) {
                salaryQuery = salaryQuery + " and isLocked=true";
                salaryQuery = salaryQuery + " and autoGeneratedBillNumber=null";
            }

            //update the lock flag
            String salaryQueryOutput = aql.getRestData(ApplicationConstants.END_POINT, salaryQuery);
            if (salaryQueryOutput != null && !salaryQueryOutput.isEmpty() && !salaryQueryOutput.equals("[]")) {
                ArrayList<HashMap> tempList = new Gson().fromJson(salaryQueryOutput, new TypeToken<ArrayList<HashMap>>() {
                }.getType());
                HashMap map = tempList.get(0);
                if (map.containsKey("idStr") && map.get("idStr") != null) {
                    AutoSalaryProcess asp = fetch((String) map.get("idStr"));
                    if (operationType != null && operationType.equalsIgnoreCase("process")) {
                        asp.setIsLocked(true);
                    } else if (operationType != null && operationType.equalsIgnoreCase("unprocess")) {
                        asp.setIsLocked(false);
                    }
                    update(asp, (String) map.get("idStr"));
                }
            }

//            if (salaryQueryOutput != null && !salaryQueryOutput.isEmpty() && !salaryQueryOutput.equals("[]")) {
//                ArrayList<HashMap> tempList = new Gson().fromJson(salaryQueryOutput, new TypeToken<ArrayList<HashMap>>() {
//                }.getType());
//                HashMap map = tempList.get(0);
//                if (map.containsKey(ApplicationConstants.VALUE) && map.get(ApplicationConstants.VALUE) != null) {
//                    AutoSalaryProcess asp = new Gson().fromJson(new Gson().toJson(map.get(ApplicationConstants.VALUE)), new TypeToken<AutoSalaryProcess>() {
//                    }.getType());
//                    if (operationType != null && operationType.equalsIgnoreCase("process")) {
//                        asp.setIsLocked(true);
//                    } else if (operationType != null && operationType.equalsIgnoreCase("unprocess")) {
//                        asp.setIsLocked(false);
//                    }
//                    update(asp, asp.getIdStr());
//                }
//            }
        }
    }

    public static void main(String[] args) {
        try {
//            String[] ids = {"CAO/CAO-9"};
//            new AutoSalaryProcessManager().processSalary(ids, "7", "2016", "chaitu", "salary", "07", "2016");
//            String[] ids = {"58009aab4e4ef80a8a1b7a4e"};
//            new AutoSalaryProcessManager().unprocessSalary(ids, "chaitu");
        } catch (Exception ex) {
            StringWriter stack = new StringWriter();
            ex.printStackTrace(new PrintWriter(stack));
            //System.out.println(stack.toString());
        }
    }
}
