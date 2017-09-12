/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.hrms.dto.EarningHeadsDetails;
import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author upendra
 */
public class SalaryIncrementDateWise extends Common {

    private String employeeCode;
    private String employeeCodeM;
    private String employeeName;
    private String ddo;
    private String location;
    private String department;
    private String designation;
    private String natureType;
    private String postingCity;
    private String transactionDate;
    private String fromDate;
    private String toDate;
    private String fundType;
    private String pfType;
    private String empType;
    private long basic;
    private long Incbasic;
    private String incrementPercentage;
    private long incrementDate;
    private String incrementStrDate;
    private String emprowid;
    private String salincrowid;
    private List<EarningHeadsDetails> earningHeads;
    private List<EarningHeadsDetails> deductionHeads;
    private double totalEarnings;
    private double totalDeductions;

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

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
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

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getBasic() {
        return basic;
    }

    public void setBasic(long basic) {
        this.basic = basic;
    }

    public String getIncrementPercentage() {
        return incrementPercentage;
    }

    public void setIncrementPercentage(String incrementPercentage) {
        this.incrementPercentage = incrementPercentage;
    }

    public long getIncrementDate() {
        return incrementDate;
    }

    public void setIncrementDate(long incrementDate) {
        this.incrementDate = incrementDate;
    }

    public String getEmprowid() {
        return emprowid;
    }

    public void setEmprowid(String emprowid) {
        this.emprowid = emprowid;
    }

    public String getIncrementStrDate() {
        return incrementStrDate;
    }

    public void setIncrementStrDate(String incrementStrDate) {
        this.incrementStrDate = incrementStrDate;
    }

    public String getSalincrowid() {
        return salincrowid;
    }

    public void setSalincrowid(String salincrowid) {
        this.salincrowid = salincrowid;
    }

    public List<EarningHeadsDetails> getEarningHeads() {
        return earningHeads;
    }

    public void setEarningHeads(List<EarningHeadsDetails> earningHeads) {
        this.earningHeads = earningHeads;
    }

    public List<EarningHeadsDetails> getDeductionHeads() {
        return deductionHeads;
    }

    public void setDeductionHeads(List<EarningHeadsDetails> deductionHeads) {
        this.deductionHeads = deductionHeads;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public long getIncbasic() {
        return Incbasic;
    }

    public void setIncbasic(long Incbasic) {
        this.Incbasic = Incbasic;
    }
}
