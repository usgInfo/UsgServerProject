/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

/**
 * @author ankur
 */
public class BankPaymentList {
    private String transferMode;
    private String bankAccountNo;
    private String bankName;
    private String bankAmount;
    private String bankRemarks;

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(String bankAmount) {
        this.bankAmount = bankAmount;
    }

    public String getBankRemarks() {
        return bankRemarks;
    }

    public void setBankRemarks(String bankRemarks) {
        this.bankRemarks = bankRemarks;
    }
    
}
