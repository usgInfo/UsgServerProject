/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

/**
 * @author ankur
 */
public class ChequeList {
    private String chequeNo;
    private String chequeDate;
    private String chequeBankName;
    private String chequeAmount;
    private String inFavorOf;
    private String chequeRemarks;
    private Long ChequeDateInMilliSecond;

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeBankName() {
        return chequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        this.chequeBankName = chequeBankName;
    }

    public String getChequeAmount() {
        return chequeAmount;
    }

    public void setChequeAmount(String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }

    public String getInFavorOf() {
        return inFavorOf;
    }

    public void setInFavorOf(String inFavorOf) {
        this.inFavorOf = inFavorOf;
    }

    public String getChequeRemarks() {
        return chequeRemarks;
    }

    public void setChequeRemarks(String chequeRemarks) {
        this.chequeRemarks = chequeRemarks;
    }

    public Long getChequeDateInMilliSecond() {
        return ChequeDateInMilliSecond;
    }

    public void setChequeDateInMilliSecond(Long ChequeDateInMilliSecond) {
        this.ChequeDateInMilliSecond = ChequeDateInMilliSecond;
    }
    
}