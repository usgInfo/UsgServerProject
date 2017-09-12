/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

/**
 *
 * @author accure
 */
public class CarryForwardLedgerBalance {

    private String closingBalance;
    private String collapseOrCarryForward;
    private String ledgerId;
    private String ledgerName;
    private String year;
    private String yearId;
    private String openingBal;
    private String amountType;
    private String drAmount;
    private String crAmount;

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getCollapseOrCarryForward() {
        return collapseOrCarryForward;
    }

    public void setCollapseOrCarryForward(String collapseOrCarryForward) {
        this.collapseOrCarryForward = collapseOrCarryForward;
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearId() {
        return yearId;
    }

    public void setYearId(String yearId) {
        this.yearId = yearId;
    }

    public String getOpeningBal() {
        return openingBal;
    }

    public void setOpeningBal(String openingBal) {
        this.openingBal = openingBal;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getDrAmount() {
        return drAmount;
    }

    public void setDrAmount(String drAmount) {
        this.drAmount = drAmount;
    }

    public String getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(String crAmount) {
        this.crAmount = crAmount;
    }

}
