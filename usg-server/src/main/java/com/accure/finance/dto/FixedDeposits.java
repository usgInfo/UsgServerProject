/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author deepak2310
 */
public class FixedDeposits extends Common {

    private String fundType;
    private String voucherNo;
    private String bank;
    private String date;
    private String fdrNumber;
    private String amount;
    private String periods;
    private String rate;
    private String fdrStatus;
    private String remarks;
    private String RefToFdr;
    private String interestAmount;
    private String maturityAmount;
    private String maturityDate;
    
//    for only view or list
    private String fundTypeName;
    private String bankName;
    

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFdrNumber() {
        return fdrNumber;
    }

    public void setFdrNumber(String fdrNumber) {
        this.fdrNumber = fdrNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRefToFdr() {
        return RefToFdr;
    }

    public void setRefToFdr(String RefToFdr) {
        this.RefToFdr = RefToFdr;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(String maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getFdrStatus() {
        return fdrStatus;
    }

    public void setFdrStatus(String fdrStatus) {
        this.fdrStatus = fdrStatus;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

}
