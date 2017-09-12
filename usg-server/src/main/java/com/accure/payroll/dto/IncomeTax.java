/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class IncomeTax extends Common {

    private String employeeCode;
    private String employeeName;
    private String employeeCodeM;
    private String ddo;
    private String designation;
    private String natureType;
    private String postingCity;
    private String location;
    private String department;
    private String pfType;
    private String fundType;
    private String panNo;
    private double it;
    private double educationcess;
    private double total;
    private int month;
    private int year;
    private String budgetHeadName;
    private String budgetHead;
    private boolean lockStatus;
    private String processStatus;

    
    public double getEducationcess() {
        return educationcess;
    }

    public void setEducationcess(double educationcess) {
        this.educationcess = educationcess;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    


    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }
    

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }
    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }
    
    public String getBudgetCode() {
        return budgetHead;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetHead = budgetCode;
    }

    public boolean isLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String isProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCodeM() {
        return employeeCodeM;
    }

    public void setEmployeeCodeM(String employeeCodeM) {
        this.employeeCodeM = employeeCodeM;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
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

    public double getIt() {
        return it;
    }

    public void setIt(double it) {
        this.it = it;
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

}
