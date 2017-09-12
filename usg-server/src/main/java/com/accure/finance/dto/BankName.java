/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class BankName extends Common {

    private String bankName;
    private String branchName;
    private String city;
    private String state;
    private String ifscCode;
    private String micrCode;
    private String primaryContactName;
    private String secondaryContactName;
    private String primaryContactNo;
    private String secondaryContactNo;
    private String accNumber;
    private String address;
    private String remarks;
    private boolean usedInLedger;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfsccode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getMicrCode() {
        return micrCode;
    }

    public void setMicrCode(String micrCode) {
        this.micrCode = micrCode;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getSecondaryContactName() {
        return secondaryContactName;
    }

    public void setSecondaryContactName(String secondaryContactName) {
        this.secondaryContactName = secondaryContactName;
    }

    public String getPrimaryContactNo() {
        return primaryContactNo;
    }

    public void setPrimaryContactNo(String primaryContactNo) {
        this.primaryContactNo = primaryContactNo;
    }

    public String getSecondaryContactNo() {
        return secondaryContactNo;
    }

    public void setSecondaryContactNo(String secondaryContactNo) {
        this.secondaryContactNo = secondaryContactNo;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isUsedInLedger() {
        return usedInLedger;
    }

    public void setUsedInLedger(boolean usedInLedger) {
        this.usedInLedger = usedInLedger;
    }

   

}
