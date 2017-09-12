/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class LoanAllotment extends Common {

    private String ddo;
    private String loanType;
    private String orderNo;
    private String loanNature;
    private String empCode;
    private String empName;
    private String department;
    private String designation;
    private String loan;
    private String dated;
    private double requestAmount;
    private double sanctionedAmount;
   
    private double adjustAmount;
    private double allotAmount;
    private double installmentAmount;
    private double totalInstallment;
    private String remarks;
    private String interestPercentage;
    private String interestPaid;
    private String balanceInterest;
    private String applyNo;
    private String financialYear;
     private String isLoanAmountPaid;
     private String isAllotted;
    private String loanTypeId;
    private String loanName;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(String loanTypeId) {
        this.loanTypeId = loanTypeId;
    }

    
    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getOrdErNo() {
        return orderNo;
    }

    public void setOrdErNo(String ordErNo) {
        this.orderNo = ordErNo;
    }

    public String getLoanNature() {
        return loanNature;
    }

    public void setLoanNature(String loanNature) {
        this.loanNature = loanNature;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public double getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(double requestAmount) {
        this.requestAmount = requestAmount;
    }

    public double getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(double sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    public double getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(double adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public double getAllotAmount() {
        return allotAmount;
    }

    public void setAllotAmount(double allotAmount) {
        this.allotAmount = allotAmount;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public double getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(double totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(String interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public String getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(String interestPaid) {
        this.interestPaid = interestPaid;
    }

    public String getBalanceInterest() {
        return balanceInterest;
    }

    public void setBalanceInterest(String balanceInterest) {
        this.balanceInterest = balanceInterest;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getIsLoanAmountPaid() {
        return isLoanAmountPaid;
    }

    public void setIsLoanAmountPaid(String isLoanAmountPaid) {
        this.isLoanAmountPaid = isLoanAmountPaid;
    }

    public String getIsAllotted() {
        return isAllotted;
    }

    public void setIsAllotted(String isAllotted) {
        this.isAllotted = isAllotted;
   
    }

}
