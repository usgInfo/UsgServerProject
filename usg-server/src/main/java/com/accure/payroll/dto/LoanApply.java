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
public class LoanApply extends Common {

    private String empCode;
    private String ddo;
    private String empName;
    private String department;
    private String designation;
    private String applyNo;
    private String pfType;
    private String loanType;
    private String dated;
    private double amount;
    private String remarks;
    private String loanTypeId;
    
    private String isAllotted;
    private String isLoanClosed;
    private String isLoanAmountPaid;
    private String financialYear;
    private String loanName;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
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

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsLoanClosed() {
        return isLoanClosed;
    }

    public void setIsLoanClosed(String isLoanClosed) {
        this.isLoanClosed = isLoanClosed;
    }
    
    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }
}
