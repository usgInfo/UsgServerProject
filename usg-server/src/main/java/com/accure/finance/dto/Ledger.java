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
public class Ledger extends Common {

    private String ledgerName;
    private String ledgerCode;
    private String displayName;
    private String underGroup;
    private String FundType;
    private String budgetType;
    private String budgetHeadCode;
     private String budgetHeadName;
    private String remarks;
    private String ledgerMapping;
    private String prevReqAmount;

    public String getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }
    private String requestAmount;

//    for only table purpose
    private String underGroupName;
    private String fundTypeName;

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public String getPrevReqAmount() {
        return prevReqAmount;
    }

    public void setPrevReqAmount(String prevReqAmount) {
        this.prevReqAmount = prevReqAmount;
    }
    
    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }
    
    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUnderGroup() {
        return underGroup;
    }

    public void setUnderGroup(String underGroup) {
        this.underGroup = underGroup;
    }
    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }
    public String getBudgetHeadCode() {
        return budgetHeadCode;
    }

    public void setBudgetHeadCode(String budgetHeadCode) {
        this.budgetHeadCode = budgetHeadCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLedgerMapping() {
        return ledgerMapping;
    }

    public void setLedgerMapping(String ledgerMapping) {
        this.ledgerMapping = ledgerMapping;
    }

    public String getUnderGroupName() {
        return underGroupName;
    }

    public void setUnderGroupName(String underGroupName) {
        this.underGroupName = underGroupName;
    }
    public String getLedgerCode() {
        return ledgerCode;
    }

    public void setLedgerCode(String ledgerCode) {
        this.ledgerCode = ledgerCode;
    }

    public String getFundType() {
        return FundType;
    }

    public void setFundType(String FundType) {
        this.FundType = FundType;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

}
