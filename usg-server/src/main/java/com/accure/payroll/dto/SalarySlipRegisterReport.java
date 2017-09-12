/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author mano
 */
public class SalarySlipRegisterReport extends Common {

    private String employeePKey;
    private String employeeCode;
    private String employeeCodeM;
    private String employeeName;
    private String email;
    private String ddo;
    private String ddoName;
    private String location;
    private String locationName;
    private String department;
    private String departmentName;
    private String designation;
    private String designationName;
    private String salaryType;
    private String salaryPostStatus;
    private String natureType;
    private String natureTypeName;
    private String postingCity;
    private String postingCityName;
    private String pfType;
    private String pfTypeName;
    private String fundType;
    private String fundTypeName;
    private String budgetHead;
    private String budgetHeadName;
    private double earnings;
    private double deductions;
    private int month;
    private int year;
    private Earnings earningsInfo;
    private Deductions deductionsInfo;
    private EmpAttendance attendance;
    private boolean isArrear;
    private ArrearProcess arrear;
    private String pfNumber;
    private String panNo;
    private String payMode;
    private String acnumber;
    private long createdDate;
    private long updatedDate;
    private String salaryProcessType;
    private String gradeId;
    private String gradeName;

    public String getSalaryProcessType() {
        return salaryProcessType;
    }

    public void setSalaryProcessType(String salaryProcessType) {
        this.salaryProcessType = salaryProcessType;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public boolean isIsArrear() {
        return isArrear;
    }

    public void setIsArrear(boolean isArrear) {
        this.isArrear = isArrear;
    }

    public ArrearProcess getArrear() {
        return arrear;
    }

    public void setArrear(ArrearProcess arrear) {
        this.arrear = arrear;
    }

    public EmpAttendance getAttendance() {
        return attendance;
    }

    public void setAttendance(EmpAttendance attendance) {
        this.attendance = attendance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPfNumber() {
        return pfNumber;
    }

    public void setPfNumber(String pfNumber) {
        this.pfNumber = pfNumber;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getAcnumber() {
        return acnumber;
    }

    public void setAcnumber(String acnumber) {
        this.acnumber = acnumber;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEmployeePKey() {
        return employeePKey;
    }

    public void setEmployeePKey(String employeePKey) {
        this.employeePKey = employeePKey;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCodeM() {
        return employeeCodeM;
    }

    public void setEmployeeCodeM(String employeeCodeM) {
        this.employeeCodeM = employeeCodeM;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getSalaryPostStatus() {
        return salaryPostStatus;
    }

    public void setSalaryPostStatus(String salaryPostStatus) {
        this.salaryPostStatus = salaryPostStatus;
    }

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getNatureTypeName() {
        return natureTypeName;
    }

    public void setNatureTypeName(String natureTypeName) {
        this.natureTypeName = natureTypeName;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    public String getPostingCityName() {
        return postingCityName;
    }

    public void setPostingCityName(String postingCityName) {
        this.postingCityName = postingCityName;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public String getPfTypeName() {
        return pfTypeName;
    }

    public void setPfTypeName(String pfTypeName) {
        this.pfTypeName = pfTypeName;
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

    public double getEarnings() {
        return earnings;
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Earnings getEarningsInfo() {
        return earningsInfo;
    }

    public void setEarningsInfo(Earnings earningsInfo) {
        this.earningsInfo = earningsInfo;
    }

    public Deductions getDeductionsInfo() {
        return deductionsInfo;
    }

    public void setDeductionsInfo(Deductions deductionsInfo) {
        this.deductionsInfo = deductionsInfo;
    }

}
