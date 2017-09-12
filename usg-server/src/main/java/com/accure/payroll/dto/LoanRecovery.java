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
public class LoanRecovery extends Common {

    private String ddo;
    private String empCode;
    private String empName;
    private String designation;
    private String loanType;
    private String loanDate;
    private String orderNo;
    private String accountNo;
    private String loanNature;
    private String month;
    private String preRecovery;
    private String year;
    private String date;
    private String loanTypeId;
    private String financialYear;
    
    private double paidAmount;
    private double loanAmount;
    private double balanceAmount;
    private double installmentAmount;
    private double totalInstallment;
    private double sanctionedAmount;
    private double interestPercentage;
    private double interestAmount;
    private double interestPaid;
    private double balanceInterest;
    private double balanceNoOfInstallments;
    
    private String remarks;
    private String applyNo;
    private String isLoanAmountPaid;
    private String paymentTableId;
    private String loanName;

    public String getLoanName() {
        return loanName;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(String loanTypeId) {
        this.loanTypeId = loanTypeId;
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

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(double totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public double getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(double sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    public String getIsLoanAmountPaid() {
        return isLoanAmountPaid;
    }

    public void setIsLoanAmountPaid(String isLoanAmountPaid) {
        this.isLoanAmountPaid = isLoanAmountPaid;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public double getBalanceNoOfInstallments() {
        return balanceNoOfInstallments;
    }

    public void setBalanceNoOfInstallments(double balanceNoOfInstallments) {
        this.balanceNoOfInstallments = balanceNoOfInstallments;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    public double getBalanceInterest() {
        return balanceInterest;
    }

    public void setBalanceInterest(double balanceInterest) {
        this.balanceInterest = balanceInterest;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(double interestPercentage) {
        this.interestPercentage = interestPercentage;
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

    
    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

   

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getLoanNature() {
        return loanNature;
    }

    public void setLoanNature(String loanNature) {
        this.loanNature = loanNature;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getInsatlmentAmount() {
        return installmentAmount;
    }

    public void setInsatlmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPreRecovery() {
        return preRecovery;
    }

    public void setPreRecovery(String preRecovery) {
        this.preRecovery = preRecovery;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaymentTableId() {
        return paymentTableId;
    }

    public void setPaymentTableId(String paymentTableId) {
        this.paymentTableId = paymentTableId;
    }

}
