/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/*
 * @author Asif/ankur
 */
public class ReceiptVoucher extends Common {

    private String manualVoucher;
    private String narrationType;
    private String DDO;
    private String ddoName;
    private String location;
    private String locationName;
    private String voucherNo;
    private String voucherDate;
    private Long voucherDateInMilliSecond;
    private String fundType;
    private String budgetHead;
    private String budgetHeadName;
    private String ledgerCategory;
    private String isGrant;
    private List<LedgerList> ledgerList;
    private double totalDrAmount;
    private double totalCrAmount;
    private String narration;
    private String paymentMode;
    //Cheque Details
    private List<ChequeList> chequeList;
    //Bank Details
    private List<BankPaymentList> bankPaymentList;
    private String postingStatus;
    private String postingDate;
    private String unPostingDate;
    private String entryStatus;
    private String voucherId;
    
    private String fromDate;
    private String toDate;

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    private String voucherName;

    public String getPostingStatus() {
        return postingStatus;
    }

    public void setPostingStatus(String postingStatus) {
        this.postingStatus = postingStatus;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getLedgerCategory() {
        return ledgerCategory;
    }

    public void setLedgerCategory(String ledgerCategory) {
        this.ledgerCategory = ledgerCategory;
    }

    public String getIsGrant() {
        return isGrant;
    }

    public void setIsGrant(String isGrant) {
        this.isGrant = isGrant;
    }

    public List<LedgerList> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<LedgerList> ledgerList) {
        this.ledgerList = ledgerList;
    }

    public double getTotalDrAmount() {
        return totalDrAmount;
    }

    public void setTotalDrAmount(double totalDrAmount) {
        this.totalDrAmount = totalDrAmount;
    }

    public double getTotalCrAmount() {
        return totalCrAmount;
    }

    public void setTotalCrAmount(double totalCrAmount) {
        this.totalCrAmount = totalCrAmount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getVoucherDateInMilliSecond() {
        return voucherDateInMilliSecond;
    }

    public void setVoucherDateInMilliSecond(Long voucherDateInMilliSecond) {
        this.voucherDateInMilliSecond = voucherDateInMilliSecond;
    }

    public String getDDO() {
        return DDO;
    }

    public void setDDO(String DDO) {
        this.DDO = DDO;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getManualVoucher() {
        return manualVoucher;
    }

    public void setManualVoucher(String manualVoucher) {
        this.manualVoucher = manualVoucher;
    }

    public String getNarrationType() {
        return narrationType;
    }

    public void setNarrationType(String narrationType) {
        this.narrationType = narrationType;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getUnPostingDate() {
        return unPostingDate;
    }

    public void setUnPostingDate(String unPostingDate) {
        this.unPostingDate = unPostingDate;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    
//    public Long getChequeDateInMilliSecond() {
//        return ChequeDateInMilliSecond;
//    }
//
//    public void setChequeDateInMilliSecond(Long ChequeDateInMilliSecond) {
//        this.ChequeDateInMilliSecond = ChequeDateInMilliSecond;
//    }
//    
    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
    }
    
    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public List<ChequeList> getChequeList() {
        return chequeList;
    }

    public void setChequeList(List<ChequeList> chequeList) {
        this.chequeList = chequeList;
    }

    public List<BankPaymentList> getBankPaymentList() {
        return bankPaymentList;
    }

    public void setBankPaymentList(List<BankPaymentList> bankPaymentList) {
        this.bankPaymentList = bankPaymentList;
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

}
