/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.budget.dto.FundType;
import com.accure.finance.dto.BankName;
import com.accure.finance.dto.DDO;
import com.accure.hrms.dto.Association;
import com.accure.hrms.dto.BudgetHeadMaster;
import com.accure.hrms.dto.Category;
import com.accure.hrms.dto.CategoryPosts;
import com.accure.hrms.dto.CityMaster;
import com.accure.hrms.dto.Department;
import com.accure.hrms.dto.Designation;
import com.accure.hrms.dto.DesignationFundTypeMapping;
import com.accure.hrms.dto.Discipline;
import com.accure.hrms.dto.Employee;
import com.accure.hrms.dto.EmployeePromotion;
import com.accure.hrms.dto.Grade;
import static com.accure.hrms.manager.EmployeeManager.postCheckingMethod;
import com.accure.user.dto.User;
import com.accure.user.manager.UserManager;
import com.accure.usg.common.manager.DBManager;
import com.accure.usg.server.utils.ApplicationConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author user
 */
public class EmployeePromotionManager {

    public String save(EmployeePromotion empPromotionJson, String userId, String empid) throws Exception {
        User user = new UserManager().fetch(userId);
        String fName = user.getFname();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(empPromotionJson.getIncrementDueDate());
        long dateInLong = date.getTime();
        String statusMessage = "success";
        HashMap<String, String> resultList = new HashMap<String, String>();

        if (postCheckingMethodInEmpPromo(empPromotionJson)) {
            Employee emp = new EmployeeManager().fetchEmployee(empid);
            if (empPromotionJson.getPromotionStatus().equalsIgnoreCase("Deputation")) {
                emp.setFromDDO(empPromotionJson.getDdo());
                emp.setOnDeputation("Yes");
            } else {
                emp.setDdo(empPromotionJson.getDdo());
                emp.setLocation(empPromotionJson.getLocation());
            }
            emp.setDepartment(empPromotionJson.getDepartment());
            emp.setDesignation(empPromotionJson.getDesignation());
            emp.setGrade(empPromotionJson.getGrade());
            emp.setFundType(empPromotionJson.getFundType());
            emp.setBudgetHead(empPromotionJson.getBudgetHead());
            emp.setNatureType(empPromotionJson.getNatureType());
            emp.setDiscipline(empPromotionJson.getDiscipline());
            emp.setPostingCity(empPromotionJson.getPostingCity());
            emp.setAssociation(empPromotionJson.getAssociation());
            emp.setBank(empPromotionJson.getBank());
            emp.setAcnumber(empPromotionJson.getAcnumber());
            emp.setBasic(empPromotionJson.getBasic());
            emp.setGradePay(empPromotionJson.getGradePay());
            emp.setIncrementDueDate(empPromotionJson.getIncrementDueDate());
            emp.setIncrementDueDateInMilliSecond(dateInLong);
            emp.setIncrementPercentage(empPromotionJson.getIncrementPercentage());
            emp.setPromotionCode(empPromotionJson.getPromotionCode());
            emp.setPromotionStatus(empPromotionJson.getPromotionStatus());
            emp.setPromotedDate(empPromotionJson.getPromotedDate());
            emp.setEarningHeads(empPromotionJson.getEarningHeads());
            emp.setDeductionHeads(empPromotionJson.getDeductionHeads());
            emp.setTotalEarnings(empPromotionJson.getTotalEarnings());
            emp.setTotalDeductions(empPromotionJson.getTotalDeductions());

            //insert into promotion table
            empPromotionJson.setStatus(ApplicationConstants.ACTIVE);
            empPromotionJson.setCreateDate(System.currentTimeMillis() + "");
            empPromotionJson.setCreatedBy(fName);
            empPromotionJson.setEmpid(empid);
            String promotionjson = new Gson().toJson(empPromotionJson);
            String id = DBManager.getDbConnection().insert(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, promotionjson);
            // String bankJson = new Gson().toJson(emp);
            boolean status = new EmployeePromotionManager().EmpupdateinEmpPromomtion(emp, empid, userId);

            resultList.put("statusMessage", statusMessage);
        } else {
            resultList.put("noposts", ApplicationConstants.NO_POST_AVAILABLE);
        }
        return new Gson().toJson(resultList);
    }

    public String fetch(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return null;
        }
        String result = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, objId);
        List<EmployeePromotion> objList = new Gson().fromJson(result, new TypeToken<List<EmployeePromotion>>() {
        }.getType());
        if (objList == null || objList.size() < 1) {
            return null;
        }
        return new Gson().toJson(objList.get(0));
    }

    public EmployeePromotion fetchdata(String objId) throws Exception {
        if (objId == null) {
            return null;
        }
        String empJson = DBManager.getDbConnection().fetch(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, objId);
        List<EmployeePromotion> empprolist = new Gson().fromJson(empJson, new TypeToken<List<EmployeePromotion>>() {
        }.getType());
        EmployeePromotion emp = empprolist.get(0);
        return emp;
    }

    public boolean EmpupdateinEmpPromomtion(Employee newEmployee, String employeeId, String userId) throws Exception {
        Employee previousEmp = new Gson().fromJson(new EmployeeManager().fetch(employeeId), new TypeToken<Employee>() {
        }.getType());
        User user = new UserManager().fetch(userId);
        String userName = user.getFname() + " " + user.getLname();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(newEmployee.getIncrementDueDate());
        long dateInLong = date.getTime();
        if (newEmployee.getPromotionStatus().equalsIgnoreCase("Deputation")) {
            previousEmp.setFromDDO(newEmployee.getFromDDO());
            previousEmp.setOnDeputation("Yes");
        } else {
            previousEmp.setDdo(newEmployee.getDdo());
            previousEmp.setLocation(newEmployee.getLocation());
        }
        previousEmp.setDepartment(newEmployee.getDepartment());
        previousEmp.setDesignation(newEmployee.getDesignation());
        previousEmp.setGrade(newEmployee.getGrade());
        previousEmp.setFundType(newEmployee.getFundType());
        previousEmp.setBudgetHead(newEmployee.getBudgetHead());
        previousEmp.setNatureType(newEmployee.getNatureType());
        previousEmp.setDiscipline(newEmployee.getDiscipline());
        previousEmp.setPostingCity(newEmployee.getPostingCity());
        previousEmp.setAssociation(newEmployee.getAssociation());
        previousEmp.setBank(newEmployee.getBank());
        previousEmp.setAcnumber(newEmployee.getAcnumber());
        previousEmp.setBasic(newEmployee.getBasic());
        previousEmp.setGradePay(newEmployee.getGradePay());
        previousEmp.setIncrementDueDate(newEmployee.getIncrementDueDate());
        previousEmp.setIncrementDueDateInMilliSecond(dateInLong);
        previousEmp.setIncrementPercentage(newEmployee.getIncrementPercentage());
        previousEmp.setPromotionCode(newEmployee.getPromotionCode());
        previousEmp.setPromotionStatus(newEmployee.getPromotionStatus());
        previousEmp.setPromotedDate(newEmployee.getPromotedDate());
        previousEmp.setEarningHeads(newEmployee.getEarningHeads());
        previousEmp.setDeductionHeads(newEmployee.getDeductionHeads());
        previousEmp.setTotalEarnings(newEmployee.getTotalEarnings());
        previousEmp.setTotalDeductions(newEmployee.getTotalDeductions());
        previousEmp.setPromotedDate(newEmployee.getPromotedDate());
        previousEmp.setPromotionCode(newEmployee.getPromotionCode());
        previousEmp.setPromotionStatus(newEmployee.getPromotionStatus());
        previousEmp.setUpdatedBy(userName);
        previousEmp.setUpdateDate(System.currentTimeMillis() + "");

        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_TABLE, employeeId, new Gson().toJson(previousEmp));
        if (result) {
            new EmployeeManager().employeeHistory(previousEmp, employeeId);
        }
        return result;
    }

    public boolean delete(String objId) throws Exception {
        if (objId == null || objId.isEmpty()) {
            return false;
        }
        Type type = new TypeToken<EmployeePromotion>() {
        }.getType();
        String obj = new EmployeePromotionManager().fetch(objId);
        if (obj == null || obj.isEmpty()) {
            return false;
        }
        EmployeePromotion objrJson = new Gson().fromJson(obj, type);
        objrJson.setStatus(ApplicationConstants.INACTIVE);
        boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, objId, new Gson().toJson(objrJson));
        return result;
    }

    public String update(EmployeePromotion empPromotionJson, String objId, String UserId) throws Exception {

        User user = new UserManager().fetch(UserId);
        String fName = user.getFname();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(empPromotionJson.getIncrementDueDate());
        long dateInLong = date.getTime();

        Employee emp = new EmployeeManager().fetchEmployee(empPromotionJson.getEmpid());
        boolean isAvailable = isPostAreAvailbleWhileEmpProUpdating(emp, empPromotionJson);
        if (isAvailable == true) {
            if (empPromotionJson.getPromotionStatus().equalsIgnoreCase("Deputation")) {
                emp.setFromDDO(empPromotionJson.getDdo());
                emp.setOnDeputation("Yes");
            } else {
                emp.setDdo(empPromotionJson.getDdo());
                emp.setLocation(empPromotionJson.getLocation());
            }

            emp.setDepartment(empPromotionJson.getDepartment());
            emp.setDesignation(empPromotionJson.getDesignation());
            emp.setGrade(empPromotionJson.getGrade());
            emp.setFundType(empPromotionJson.getFundType());
            emp.setBudgetHead(empPromotionJson.getBudgetHead());
            emp.setNatureType(empPromotionJson.getNatureType());
            emp.setDiscipline(empPromotionJson.getDiscipline());
            emp.setPostingCity(empPromotionJson.getPostingCity());
            emp.setAssociation(empPromotionJson.getAssociation());
            emp.setBank(empPromotionJson.getBank());
            emp.setAcnumber(empPromotionJson.getAcnumber());
            emp.setBasic(empPromotionJson.getBasic());
            emp.setGradePay(empPromotionJson.getGradePay());
            emp.setIncrementDueDate(empPromotionJson.getIncrementDueDate());
            emp.setIncrementDueDateInMilliSecond(dateInLong);
            emp.setIncrementPercentage(empPromotionJson.getIncrementPercentage());
            emp.setPromotionCode(empPromotionJson.getPromotionCode());
            emp.setPromotionStatus(empPromotionJson.getPromotionStatus());
            emp.setPromotedDate(empPromotionJson.getPromotedDate());
            emp.setEarningHeads(empPromotionJson.getEarningHeads());
            emp.setDeductionHeads(empPromotionJson.getDeductionHeads());
            emp.setTotalEarnings(empPromotionJson.getTotalEarnings());
            emp.setTotalDeductions(empPromotionJson.getTotalDeductions());

            EmployeePromotion empPromotion = new EmployeePromotionManager().fetchdata(objId);
            empPromotion.setDdo(empPromotionJson.getDdo());
            empPromotion.setPromotionCode(empPromotionJson.getPromotionCode());
            empPromotion.setPromotionStatus(empPromotionJson.getPromotionStatus());
            empPromotion.setLocation(empPromotionJson.getLocation());
            empPromotion.setDepartment(empPromotionJson.getDepartment());
            empPromotion.setDesignation(empPromotionJson.getDesignation());
            empPromotion.setGrade(empPromotionJson.getGrade());
            empPromotion.setFundType(empPromotionJson.getFundType());
            empPromotion.setBudgetHead(empPromotionJson.getBudgetHead());
            empPromotion.setNatureType(empPromotionJson.getNatureType());
            empPromotion.setPostingCity(empPromotionJson.getPostingCity());
            empPromotion.setAssociation(empPromotionJson.getAssociation());
            empPromotion.setBank(empPromotionJson.getBank());
            empPromotion.setAcnumber(empPromotionJson.getAcnumber());
            empPromotion.setBasic(empPromotionJson.getBasic());
            empPromotion.setGradePay(empPromotionJson.getGradePay());
            empPromotion.setIncrementPercentage(empPromotionJson.getIncrementPercentage());
            empPromotion.setIncrementDueDate(empPromotionJson.getIncrementDueDate());
            empPromotion.setPromotedDate(empPromotionJson.getPromotedDate());
            empPromotion.setRemarks(empPromotionJson.getRemarks());
            empPromotion.setEmpid(empPromotionJson.getEmpid());
            empPromotion.setEarningHeads(empPromotionJson.getEarningHeads());
            empPromotion.setDeductionHeads(empPromotionJson.getDeductionHeads());
            empPromotion.setTotalEarnings(empPromotionJson.getTotalEarnings());
            empPromotion.setTotalDeductions(empPromotionJson.getTotalDeductions());
            empPromotion.setUpdateDate(System.currentTimeMillis() + "");
            empPromotion.setUpdatedBy(fName);

            String objrJson = new Gson().toJson(empPromotion);
            boolean result = DBManager.getDbConnection().update(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, objId, objrJson);

            boolean status = new EmployeePromotionManager().EmpupdateinEmpPromomtion(emp, empPromotionJson.getEmpid(), UserId);

            return ApplicationConstants.UPDATED;
        } else {
            return ApplicationConstants.NO_POST_AVAILABLE;
        }
    }

    public String fetchAll(String ddo, String location) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        conditionMap.put("location", location);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, conditionMap);
        List<EmployeePromotion> li = new Gson().fromJson(result1, new TypeToken<List<EmployeePromotion>>() {
        }.getType());
        for (EmployeePromotion cl : li) {
            String jsonObj = "";
            try {
                jsonObj = "";
                if ((cl.getDdo() != (null))) {
                    String ddoId = cl.getDdo();
                    //System.out.println("id" + ddoId);
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddoId);
                    List<DDO> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO gal2 = gaList2.get(0);
                    cl.setDdo(gal2.getDdoName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";

                if (cl.getDepartment() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
                    List<Department> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Department>>() {
                    }.getType());
                    Department gal2 = gaList2.get(0);
                    cl.setDepartment(gal2.getDepartment());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getDesignation() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, cl.getDesignation());
                    List<Designation> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Designation>>() {
                    }.getType());
                    Designation gal2 = gaList2.get(0);
                    cl.setDesignation(gal2.getDesignation());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getGrade() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, cl.getGrade());
                    List<Grade> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Grade>>() {
                    }.getType());
                    Grade gal2 = gaList2.get(0);
                    cl.setGrade(gal2.getGradeName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";

                if (cl.getPostingCity() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, cl.getPostingCity());
                    List<CityMaster> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<CityMaster>>() {
                    }.getType());
                    CityMaster gal2 = gaList2.get(0);
                    cl.setPostingCity(gal2.getCityName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getFundType() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, cl.getFundType());
                    List<FundType> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<FundType>>() {
                    }.getType());
                    FundType gal2 = gaList2.get(0);
                    cl.setFundType(gal2.getDescription());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getBudgetHead() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getBudgetHead());
                    List<BudgetHeadMaster> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    BudgetHeadMaster gal2 = gaList2.get(0);
                    cl.setBudgetHead(gal2.getBudgetHead());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getDiscipline() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, cl.getDiscipline());
                    List<Discipline> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Discipline>>() {
                    }.getType());
                    Discipline gal2 = gaList2.get(0);
                    cl.setDiscipline(gal2.getDisciplineName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getAssociation() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.ASSOCIATION_TABLE, cl.getAssociation());
                    List<Association> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Association>>() {
                    }.getType());
                    Association gal2 = gaList2.get(0);
                    cl.setAssociation(gal2.getAssociationName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getBank() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_NAME_TABLE, cl.getBank());
                    List<BankName> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<BankName>>() {
                    }.getType());
                    BankName gal2 = gaList2.get(0);
                    cl.setBank(gal2.getBankName());
                }
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(li);
//        return result1;
    }

    public String fetchAllForEmployeeTransferform(String ddo) throws Exception {
        HashMap<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        conditionMap.put("ddo", ddo);
        //  conditionMap.put("location",location);
        String result1 = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_PROMOTION_TABLE, conditionMap);
        List<EmployeePromotion> li = new Gson().fromJson(result1, new TypeToken<List<EmployeePromotion>>() {
        }.getType());
        for (EmployeePromotion cl : li) {
            String jsonObj = "";
            try {
                jsonObj = "";
                if ((cl.getDdo() != (null))) {
                    String ddoId = cl.getDdo();
                    //System.out.println("id" + ddoId);
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DDO_TABLE, ddoId);
                    List<DDO> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<DDO>>() {
                    }.getType());
                    DDO gal2 = gaList2.get(0);
                    cl.setDdo(gal2.getDdoName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";

                if (cl.getDepartment() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DEPARTMENT_TABLE, cl.getDepartment());
                    List<Department> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Department>>() {
                    }.getType());
                    Department gal2 = gaList2.get(0);
                    cl.setDepartment(gal2.getDepartment());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getDesignation() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DESIGNATION_TABLE, cl.getDesignation());
                    List<Designation> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Designation>>() {
                    }.getType());
                    Designation gal2 = gaList2.get(0);
                    cl.setDesignation(gal2.getDesignation());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getGrade() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.GRADE_TABLE, cl.getGrade());
                    List<Grade> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Grade>>() {
                    }.getType());
                    Grade gal2 = gaList2.get(0);
                    cl.setGrade(gal2.getGradeName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";

                if (cl.getPostingCity() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.CITY_TABLE, cl.getPostingCity());
                    List<CityMaster> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<CityMaster>>() {
                    }.getType());
                    CityMaster gal2 = gaList2.get(0);
                    cl.setPostingCity(gal2.getCityName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getFundType() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.FUND_TYPE_TABLE, cl.getFundType());
                    List<FundType> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<FundType>>() {
                    }.getType());
                    FundType gal2 = gaList2.get(0);
                    cl.setFundType(gal2.getDescription());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getBudgetHead() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.BUDGET_HEAD_MAPPING_TABLE, cl.getBudgetHead());
                    List<BudgetHeadMaster> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<BudgetHeadMaster>>() {
                    }.getType());
                    BudgetHeadMaster gal2 = gaList2.get(0);
                    cl.setBudgetHead(gal2.getBudgetHead());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getDiscipline() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.DISCIPLINE_TABLE, cl.getDiscipline());
                    List<Discipline> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Discipline>>() {
                    }.getType());
                    Discipline gal2 = gaList2.get(0);
                    cl.setDiscipline(gal2.getDisciplineName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getAssociation() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.ASSOCIATION_TABLE, cl.getAssociation());
                    List<Association> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<Association>>() {
                    }.getType());
                    Association gal2 = gaList2.get(0);
                    cl.setAssociation(gal2.getAssociationName());
                }
            } catch (Exception e) {
            }
            try {
                jsonObj = "";
                if (cl.getBank() != null) {
                    jsonObj = DBManager.getDbConnection().fetch(ApplicationConstants.BANK_NAME_TABLE, cl.getBank());
                    List<BankName> gaList2 = new Gson().fromJson(jsonObj, new TypeToken<List<BankName>>() {
                    }.getType());
                    BankName gal2 = gaList2.get(0);
                    cl.setBank(gal2.getBankName());
                }
            } catch (Exception e) {
            }
        }
        return new Gson().toJson(li);
//        return result1;
    }

    public static boolean postCheckingMethodInEmpPromo(EmployeePromotion employee) throws Exception {
        HashMap<String, String> desigcondiMap = new HashMap<String, String>();
        desigcondiMap.put("ddo", employee.getDdo());
        desigcondiMap.put("designation", employee.getDesignation());
        desigcondiMap.put("grade", employee.getGrade());
        desigcondiMap.put("fundType", employee.getFundType());
        desigcondiMap.put("budgetHead", employee.getBudgetHead());
        desigcondiMap.put("natureType", employee.getNatureType());
        desigcondiMap.put("desciplineName", employee.getDiscipline());
        desigcondiMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String desigresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, desigcondiMap);
        if (desigresult == null) {
            return false;
        }
        List<DesignationFundTypeMapping> desiglist = new Gson().fromJson(desigresult, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        if (desiglist == null || desiglist.size() < 1) {
            return false;
        }
        DesignationFundTypeMapping desigsinglist = desiglist.get(0);
        int postForThisCategory = 0;
        List<CategoryPosts> categoryposts = desigsinglist.getCategoryposts();
        for (Iterator<CategoryPosts> iterator = categoryposts.iterator(); iterator.hasNext();) {
            CategoryPosts next = iterator.next();
            String categoryId = employee.getCategory();
            String categoryData = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, categoryId);

            if (categoryData != null) {
                List<Category> categoryList = new Gson().fromJson(categoryData, new TypeToken<List<Category>>() {
                }.getType());
                Category category = categoryList.get(0);
                String categoryNameId = next.getCategoory();
                String categoryNameData = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, categoryNameId);
                if (categoryNameData != null) {
                    List<Category> categoryNameList = new Gson().fromJson(categoryNameData, new TypeToken<List<Category>>() {
                    }.getType());
                    Category cateNameObj = categoryNameList.get(0);
                    String catName = cateNameObj.getCategoryy();
                    if (catName.equalsIgnoreCase(category.getCategoryy())) {
                        postForThisCategory = Integer.parseInt(next.getPosts());
                    }
                }
            }

        }
        if (postForThisCategory < 1) {
            return false;
        } else {
            HashMap<String, String> saveconditionMap = new HashMap<String, String>();
            saveconditionMap.put("ddo", employee.getDdo());
            saveconditionMap.put("designation", employee.getDesignation());
            saveconditionMap.put("grade", employee.getGrade());
            saveconditionMap.put("fundType", employee.getFundType());
            saveconditionMap.put("budgetHead", employee.getBudgetHead());
            saveconditionMap.put("natureType", employee.getNatureType());
            saveconditionMap.put("discipline", employee.getDiscipline());
            saveconditionMap.put("category", employee.getCategory());
            saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String totalNoOfEmpBelongToThisCategory = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, saveconditionMap);
            List<Employee> emplist = new Gson().fromJson(totalNoOfEmpBelongToThisCategory, new TypeToken<List<Employee>>() {
            }.getType());
            if (emplist == null) {
                return true;
            } else if (emplist.size() > 0) {
                if (emplist.size() < postForThisCategory) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public static boolean postCheckingMethodInEmpPromoUpdate(EmployeePromotion employee) throws Exception {
        HashMap<String, String> desigcondiMap = new HashMap<String, String>();
        desigcondiMap.put("ddo", employee.getDdo());
        desigcondiMap.put("designation", employee.getDesignation());
        desigcondiMap.put("grade", employee.getGrade());
        desigcondiMap.put("fundType", employee.getFundType());
        desigcondiMap.put("budgetHead", employee.getBudgetHead());
        desigcondiMap.put("natureType", employee.getNatureType());
        desigcondiMap.put("desciplineName", employee.getDiscipline());
        desigcondiMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
        String desigresult = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.DESIGNATION_FUND_TYPE_TABLE, desigcondiMap);
        if (desigresult == null) {
            return false;
        }
        List<DesignationFundTypeMapping> desiglist = new Gson().fromJson(desigresult, new TypeToken<List<DesignationFundTypeMapping>>() {
        }.getType());
        if (desiglist == null || desiglist.size() < 1) {
            return false;
        }
        DesignationFundTypeMapping desigsinglist = desiglist.get(0);
        int postForThisCategory = 0;
        List<CategoryPosts> categoryposts = desigsinglist.getCategoryposts();
        for (Iterator<CategoryPosts> iterator = categoryposts.iterator(); iterator.hasNext();) {
            CategoryPosts next = iterator.next();
            String categoryId = employee.getCategory();
            String categoryData = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, categoryId);

            if (categoryData != null) {
                List<Category> categoryList = new Gson().fromJson(categoryData, new TypeToken<List<Category>>() {
                }.getType());
                Category category = categoryList.get(0);
                String categoryNameId = next.getCategoory();
                String categoryNameData = DBManager.getDbConnection().fetch(ApplicationConstants.CATEGORY_TABLE, categoryNameId);
                if (categoryNameData != null) {
                    List<Category> categoryNameList = new Gson().fromJson(categoryNameData, new TypeToken<List<Category>>() {
                    }.getType());
                    Category cateNameObj = categoryNameList.get(0);
                    String catName = cateNameObj.getCategoryy();
                    if (catName.equalsIgnoreCase(category.getCategoryy())) {
                        postForThisCategory = Integer.parseInt(next.getPosts());
                    }
                }
            }

        }
        if (postForThisCategory < 1) {
            return false;
        } else {
            HashMap<String, String> saveconditionMap = new HashMap<String, String>();
            saveconditionMap.put("ddo", employee.getDdo());
            saveconditionMap.put("designation", employee.getDesignation());
            saveconditionMap.put("grade", employee.getGrade());
            saveconditionMap.put("fundType", employee.getFundType());
            saveconditionMap.put("budgetHead", employee.getBudgetHead());
            saveconditionMap.put("natureType", employee.getNatureType());
            saveconditionMap.put("discipline", employee.getDiscipline());
            saveconditionMap.put("category", employee.getCategory());
            saveconditionMap.put(ApplicationConstants.STATUS, ApplicationConstants.ACTIVE);
            String totalNoOfEmpBelongToThisCategory = DBManager.getDbConnection().fetchAllRowsByConditions(ApplicationConstants.EMPLOYEE_TABLE, saveconditionMap);
            List<Employee> emplist = new Gson().fromJson(totalNoOfEmpBelongToThisCategory, new TypeToken<List<Employee>>() {
            }.getType());
            if (emplist == null) {
                return true;
            } else if (emplist.size() > 0) {
                if (emplist.size() <= postForThisCategory) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    private boolean isPostAreAvailbleWhileEmpProUpdating(Employee previousEmp, EmployeePromotion newEmployee) throws Exception {
        //System.out.println(new Gson().toJson(previousEmp));
        //System.out.println("***********************************************");
        //System.out.println(new Gson().toJson(newEmployee));
        if ((previousEmp.getNatureType().equalsIgnoreCase(newEmployee.getNatureType())) && (previousEmp.getCategory().equalsIgnoreCase(newEmployee.getCategory())) && (previousEmp.getDiscipline().equalsIgnoreCase(newEmployee.getDiscipline())) && (previousEmp.getBudgetHead().equalsIgnoreCase(newEmployee.getBudgetHead())) && (previousEmp.getDdo().equalsIgnoreCase(newEmployee.getDdo())) && (previousEmp.getDesignation().equalsIgnoreCase(newEmployee.getDesignation())) && (previousEmp.getGrade().equalsIgnoreCase(newEmployee.getGrade())) && (previousEmp.getFundType().equalsIgnoreCase(newEmployee.getFundType())) && (previousEmp.getPromotionStatus().equalsIgnoreCase(newEmployee.getPromotionStatus())) && (previousEmp.getPromotionCode().equalsIgnoreCase(newEmployee.getPromotionCode())) && (previousEmp.getBasic() == newEmployee.getBasic()) && (previousEmp.getIncrementDueDate().equalsIgnoreCase(newEmployee.getIncrementDueDate())) && (previousEmp.getIncrementPercentage().equalsIgnoreCase(newEmployee.getIncrementPercentage()))) {
            return true;
        } else {
            boolean result = postCheckingMethodInEmpPromoUpdate(newEmployee);
            return result;
        }
    }
}
