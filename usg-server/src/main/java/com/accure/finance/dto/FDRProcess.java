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
public class FDRProcess extends Common {

    private String fdType;
    private String fdNature;
    private String paymentMode;
    private String bank;
    private String fdDate;
    private String fdNumber;
    private String fdAmount;
    private String fdPeriod;
    private String fdMaturityAmount;
    private String fdMaturityDate;
    private String fdRemarks;
    private String fdEncashmentDate;
     private String fdEncashmentAmount;

//    FOR VIEW
    private String paymentModeName;
    private String bankName;

    private String fdFromDate;
    private String fdToDate;
    private String fdMaturityFromDate;
    private String fdMaturityToDate;

    private String fdDateInMillis;
    private String fdMaturityDateInMillis;
    private String fdEncashmentDateInMillis;

    public String getFdFromDate() {
        return fdFromDate;
    }

    public void setFdFromDate(String fdFromDate) {
        this.fdFromDate = fdFromDate;
    }

    public String getFdToDate() {
        return fdToDate;
    }

    public void setFdToDate(String fdToDate) {
        this.fdToDate = fdToDate;
    }

    public String getFdMaturityFromDate() {
        return fdMaturityFromDate;
    }

    public void setFdMaturityFromDate(String fdMaturityFromDate) {
        this.fdMaturityFromDate = fdMaturityFromDate;
    }

    public String getFdMaturityToDate() {
        return fdMaturityToDate;
    }

    public void setFdMaturityToDate(String fdMaturityToDate) {
        this.fdMaturityToDate = fdMaturityToDate;
    }

    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }

    public String getFdNature() {
        return fdNature;
    }

    public void setFdNature(String fdNature) {
        this.fdNature = fdNature;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getFdDate() {
        return fdDate;
    }

    public void setFdDate(String fdDate) {
        this.fdDate = fdDate;
    }

    public String getFdNumber() {
        return fdNumber;
    }

    public void setFdNumber(String fdNumber) {
        this.fdNumber = fdNumber;
    }

    public String getFdAmount() {
        return fdAmount;
    }

    public void setFdAmount(String fdAmount) {
        this.fdAmount = fdAmount;
    }

    public String getFdPeriod() {
        return fdPeriod;
    }

    public void setFdPeriod(String fdPeriod) {
        this.fdPeriod = fdPeriod;
    }

    public String getFdMaturityAmount() {
        return fdMaturityAmount;
    }

    public void setFdMaturityAmount(String fdMaturityAmount) {
        this.fdMaturityAmount = fdMaturityAmount;
    }
    
    public String getFdEncashmentAmount() {
        return fdEncashmentAmount;
    }

    public void setFdEncashmentAmount(String fdEncashmentAmount) {
        this.fdEncashmentAmount = fdEncashmentAmount;
    }

    public String getFdMaturityDate() {
        return fdMaturityDate;
    }

    public void setFdMaturityDate(String fdMaturityDate) {
        this.fdMaturityDate = fdMaturityDate;
    }

    public String getFdRemarks() {
        return fdRemarks;
    }

    public void setFdRemarks(String fdRemarks) {
        this.fdRemarks = fdRemarks;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getFdDateInMillis() {
        return fdDateInMillis;
    }

    public void setFdDateInMillis(String fdDateInMillis) {
        this.fdDateInMillis = fdDateInMillis;
    }

    public String getFdMaturityDateInMillis() {
        return fdMaturityDateInMillis;
    }

    public void setFdMaturityDateInMillis(String fdMaturityDateInMillis) {
        this.fdMaturityDateInMillis = fdMaturityDateInMillis;
    }

    public String getFdEncashmentDate() {
        return fdEncashmentDate;
    }

    public void setFdEncashmentDate(String fdEncashmentDate) {
        this.fdEncashmentDate = fdEncashmentDate;
    }

    public String getFdEncashmentDateInMillis() {
        return fdEncashmentDateInMillis;
    }

    public void setFdEncashmentDateInMillis(String fdEncashmentDateInMillis) {
        this.fdEncashmentDateInMillis = fdEncashmentDateInMillis;
    }

}
