/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class FinancialBudgetHeadCode extends Common {

    private String governmentBudgetHead;
    private String fundType;
    private String budgetHead;
    private String displayBudgetHead;
    private String budgetHeadDescription;
    private String underBudgetHead;
    private String remarks;
    private String isActive;

    public String getGovernmentBudgetHead() {
        return governmentBudgetHead;
    }

    public void setGovernmentBudgetHead(String governmentBudgetHead) {
        this.governmentBudgetHead = governmentBudgetHead;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getDisplayBudgetHead() {
        return displayBudgetHead;
    }

    public void setDisplayBudgetHead(String displayBudgetHead) {
        this.displayBudgetHead = displayBudgetHead;
    }

    public String getBudgetHeadDescription() {
        return budgetHeadDescription;
    }

    public void setBudgetHeadDescription(String budgetHeadDescription) {
        this.budgetHeadDescription = budgetHeadDescription;
    }

    public String getUnderBudgetHead() {
        return underBudgetHead;
    }

    public void setUnderBudgetHead(String underBudgetHead) {
        this.underBudgetHead = underBudgetHead;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    
}
