/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class LedgerCodeMaster extends Common{
    
    private String governmentLedgerCode;
    private String ledgerCode;
    private String description;
    private String budgetHead;
    private long orderLevel;

    public String getGovernmentLedgerCode() {
        return governmentLedgerCode;
    }

    public void setGovernmentLedgerCode(String governmentLedgerCode) {
        this.governmentLedgerCode = governmentLedgerCode;
    }

    public void setLedgerCode(String ledgerCode) {
        this.ledgerCode = ledgerCode;
    }

    public String getLedgerCode() {
        return ledgerCode;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public long getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(long orderLevel) {
        this.orderLevel = orderLevel;
    }
    
    
}
