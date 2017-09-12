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
public class LoanTransaction extends Common {

    private String ddo;
    private String orderNo;
    private String empCode;
    private String empName;
    private String department;
    private String designation;
    private String loanType;
    private String loanTypeId;
    private String loanNature;
    private String accountNo;
    private String dated;
    private String city;
    private String remarks;
     private String suppressyear;
    private String suppressMonth;
    private String isSuppressed;
     private String isLocked;
      private String financialYear;
       private String applyNo;
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

    public String getLoanNature() {
        return loanNature;
    }

    public void setLoanNature(String loanNature) {
        this.loanNature = loanNature;
    }
       
      public String getApplyNo() 
      {
        return applyNo;
    }

    public void setApplyNo(String applyNo) 
    {
        this.applyNo = applyNo;
    }
    
    public String getFinancialYear()
    {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

     
    public String getLoanTypeId() {
        return loanTypeId;
    }

    public void setLoanTypeId(String loanTypeId) {
        this.loanTypeId = loanTypeId;
    }
    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getSuppressyear() {
        return suppressyear;
    }

    public void setSuppressyear(String suppressyear) {
        this.suppressyear = suppressyear;
    }

    public String getSuppressMonth() {
        return suppressMonth;
    }

    public void setSuppressMonth(String suppressMonth) {
        this.suppressMonth = suppressMonth;
    }

    public String getIsSuppressed() {
        return isSuppressed;
    }

    public void setIsSuppressed(String isSuppressed) {
        this.isSuppressed = isSuppressed;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
