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
public class GovernmentLedgerCode extends Common{
    
    private String governmentLedgerCode;
    private String description;
    private String budgetHead;
    private int orderLevel;

//    public boolean getBudgetHead() {
//        return budgetHead;
//    }
//
//    public void setBudgetHead(boolean budgetHead) {
//        this.budgetHead = budgetHead;
//    }
    public void setGovernmentLedgerCode(String governmentLedgerCode) {
        this.governmentLedgerCode = governmentLedgerCode;
    }

    public String getGovernmentLedgerCode() {
        return governmentLedgerCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }
    
    public int getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(int orderLevel) {
        this.orderLevel = orderLevel;
    }
    
}
