/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

/**
 *
 * @author user
 */
public class TrialBalance implements Comparable<TrialBalance> {

    private String groupName;
    private String ledgerName;
    private String voucherDate;
    private String openingBalanceDrAmount;
    private String openingBalanceCrAmount;
    private String closingBalanceCrAmount;
    private String closingBalanceDrAmount;
    private String transactionDrAmount;

    @Override
    public String toString() {
        return "TrialBalance{" + "groupName=" + groupName + ", ledgerName=" + ledgerName + ", voucherDate=" + voucherDate + ", openingBalanceDrAmount=" + openingBalanceDrAmount + ", openingBalanceCrAmount=" + openingBalanceCrAmount + ", closingBalanceCrAmount=" + closingBalanceCrAmount + ", closingBalanceDrAmount=" + closingBalanceDrAmount + ", transactionDrAmount=" + transactionDrAmount + ", transactionCrAmount=" + transactionCrAmount + '}';
    }
    private String transactionCrAmount;
    private static SortBy sortBy = SortBy.descending;

    public static SortBy getSortBy() {
        return sortBy;
    }

    public static void setSortBy(SortBy sortBy) {
        sortBy = sortBy;
    }

    public enum SortBy {
        ascending, descending
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getOpeningBalanceDrAmount() {
        return openingBalanceDrAmount;
    }

    public void setOpeningBalanceDrAmount(String openingBalanceDrAmount) {
        this.openingBalanceDrAmount = openingBalanceDrAmount;
    }

    public String getOpeningBalanceCrAmount() {
        return openingBalanceCrAmount;
    }

    public void setOpeningBalanceCrAmount(String openingBalanceCrAmount) {
        this.openingBalanceCrAmount = openingBalanceCrAmount;
    }

    public String getClosingBalanceCrAmount() {
        return closingBalanceCrAmount;
    }

    public void setClosingBalanceCrAmount(String closingBalanceCrAmount) {
        this.closingBalanceCrAmount = closingBalanceCrAmount;
    }

    public String getClosingBalanceDrAmount() {
        return closingBalanceDrAmount;
    }

    public void setClosingBalanceDrAmount(String closingBalanceDrAmount) {
        this.closingBalanceDrAmount = closingBalanceDrAmount;
    }

    public String getTransactionDrAmount() {
        return transactionDrAmount;
    }

    public void setTransactionDrAmount(String transactionDrAmount) {
        this.transactionDrAmount = transactionDrAmount;
    }

    public String getTransactionCrAmount() {
        return transactionCrAmount;
    }

    public void setTransactionCrAmount(String transactionCrAmount) {
        this.transactionCrAmount = transactionCrAmount;
    }

    @Override
    public int compareTo(TrialBalance o) {

        if (this.sortBy == SortBy.ascending) {
            return this.voucherDate.compareTo(o.getVoucherDate());
        } else {
            return o.getVoucherDate().compareTo(this.voucherDate);
        }
    }

}
