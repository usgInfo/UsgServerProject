package com.accure.payroll.dto;

import com.accure.hrms.dto.SalaryHead;
import com.accure.usg.common.dto.Common;

/**
 *
 * @author misha
 */
public class LoanPayment extends Common {

    private String ddo;
    private String empCode;
    private String empName;
    private String designation;
    private String loanType;
    private SalaryHead loanInfo;
    private String loanDate;
    private String orderNo;
    private String accountNo;
    private String loanNature;
    private int month;
    private int year;
    private String preRecovery;
    private String dated;
    private String applyNo;
    private String remarks;
    private String paymentMode;
     private String loanTypeId;
     private String loanName;
     private String financialYear;

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

    //amount related fields
    private double loanAmount;
    private double sanctionedAmount;
    private double installmentAmount;
    private double paidAmount;
    private double balanceAmount;
    private double balanceNoOfInstallments;
    private double totalInstallment;

    //interest related fields
    private double interestPercentage;
    private double interestAmount;
    private double interestInstallmentAmount;
    private double balanceInterest;
    private double balanceNoOfInterestInstallments;
    private double interestPaid;

    //meta data
    private String isLocked;
    private String isLoanAmountPaid;
    private double createDateInDouble;
    private long created;
    private long updated;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getPreRecovery() {
        return preRecovery;
    }

    public void setPreRecovery(String preRecovery) {
        this.preRecovery = preRecovery;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(double sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getBalanceNoOfInstallments() {
        return balanceNoOfInstallments;
    }

    public void setBalanceNoOfInstallments(double balanceNoOfInstallments) {
        this.balanceNoOfInstallments = balanceNoOfInstallments;
    }

    public double getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(double totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public double getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(double interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getInterestInstallmentAmount() {
        return interestInstallmentAmount;
    }

    public void setInterestInstallmentAmount(double interestInstallmentAmount) {
        this.interestInstallmentAmount = interestInstallmentAmount;
    }

    public double getBalanceInterest() {
        return balanceInterest;
    }

    public void setBalanceInterest(double balanceInterest) {
        this.balanceInterest = balanceInterest;
    }

    public double getBalanceNoOfInterestInstallments() {
        return balanceNoOfInterestInstallments;
    }

    public void setBalanceNoOfInterestInstallments(double balanceNoOfInterestInstallments) {
        this.balanceNoOfInterestInstallments = balanceNoOfInterestInstallments;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsLoanAmountPaid() {
        return isLoanAmountPaid;
    }

    public void setIsLoanAmountPaid(String isLoanAmountPaid) {
        this.isLoanAmountPaid = isLoanAmountPaid;
    }

    public double getCreateDateInDouble() {
        return createDateInDouble;
    }

    public void setCreateDateInDouble(double createDateInDouble) {
        this.createDateInDouble = createDateInDouble;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public SalaryHead getLoanInfo() {
        return loanInfo;
    }

    public void setLoanInfo(SalaryHead loanInfo) {
        this.loanInfo = loanInfo;
    }

}
