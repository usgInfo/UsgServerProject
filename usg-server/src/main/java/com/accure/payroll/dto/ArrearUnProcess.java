/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.hrms.dto.EarningHeadsDetails;
import java.util.List;

/**
 *
 * @author user
 */
public class ArrearUnProcess {
     private String sortBy;
    private int payMonth;
    private String arrearType;
    private int payYear;
    private String fromDate;
    private String toDate;
    private String orderno;
     private int month;
    private int year;
    
     private String employeeCode;

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(int payMonth) {
        this.payMonth = payMonth;
    }

    public String getArrearType() {
        return arrearType;
    }

    public void setArrearType(String arrearType) {
        this.arrearType = arrearType;
    }

    public int getPayYear() {
        return payYear;
    }

    public void setPayYear(int payYear) {
        this.payYear = payYear;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
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

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public long getEarnings() {
        return earnings;
    }

    public void setEarnings(long earnings) {
        this.earnings = earnings;
    }

    public long getDeductions() {
        return deductions;
    }

    public void setDeductions(long deductions) {
        this.deductions = deductions;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
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

    public List<EarningHeadsDetails> getEarningHeads() {
        return earningHeads;
    }

    public void setEarningHeads(List<EarningHeadsDetails> earningHeads) {
        this.earningHeads = earningHeads;
    }
    private String employeeCodeM;
    private String employeeName;
    private String location;
    private String department;
    private String designation;
    private String salaryType;
    private String ddo;
    private String ddoName;
    private String natureType;
    private String postingCity;
    private String pfType;
    private String fundType;
    private String budgetHead;
    private long earnings;
    private long deductions;
    private int fromYear;
     private int fromMonth;
    private int toMonth;
    private int toYear;
    private String arrearStatus;
    private boolean lockStatus;
    private List<EarningHeadsDetails> earningHeads;
    
}
