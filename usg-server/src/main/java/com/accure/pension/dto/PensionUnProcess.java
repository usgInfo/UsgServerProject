/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author user
 */
public class PensionUnProcess extends Common {
    private String department;
    private String month;
    private String year;
    private String employeecode;
    private String employeeName;
    private String designation;
    private String grade;
    private String sortBy;
    private String billNo;
    private String dated;
    private List<PensionEmployee> pensionList;
    private String location;
    private String locationName;
    private String departmentName;
    private String designationName;
    private String salaryType;
    private String salaryTypeName;
    private String DDO;
    private String ddoName;
    private String natureType;
    private String postingCity;
    private String pfType;
    private String fundType;
    private String fundTypeName;
    private String pensionStatus;
    private String finYear;
    private String budgetHead;
    private String budgetHeadName;
    private String arrearStatus;
    private boolean lockStatus;
    private Long pension;

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getPensionStatus() {
        return pensionStatus;
    }

    public void setPensionStatus(String pensionStatus) {
        this.pensionStatus = pensionStatus;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeecode = employeecode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getSalaryTypeName() {
        return salaryTypeName;
    }

    public void setSalaryTypeName(String salaryTypeName) {
        this.salaryTypeName = salaryTypeName;
    }

    public String getDDO() {
        return DDO;
    }

    public void setDDO(String DDO) {
        this.DDO = DDO;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

    public String getArrearStatus() {
        return arrearStatus;
    }

    public void setArrearStatus(String arrearStatus) {
        this.arrearStatus = arrearStatus;
    }

    public boolean isLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }
    

    public Long getPension() {
        return pension;
    }

    public void setPension(Long pension) {
        this.pension = pension;
    }

    public Long getFamilyPension() {
        return familyPension;
    }

    public void setFamilyPension(Long familyPension) {
        this.familyPension = familyPension;
    }
   private Long familyPension;
    
     public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<PensionEmployee> getPensionList() {
        return pensionList;
    }

    public void setPensionList(List<PensionEmployee> pensionList) {
        this.pensionList = pensionList;
    }
}
