/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class HeadCode extends Common{
    private String govtBudgetHead;
    private String budgetHead;
    private String budgetHeadDesc;
    private String remarks;
    private String fundType;
    private String budgetHeadDisplay;
    private String underBudgetHead;
    private String activeStatus;

    public String getBudgetHeadDisplay() {
        return budgetHeadDisplay;
    }

    public void setBudgetHeadDisplay(String budgetHeadDisplay) {
        this.budgetHeadDisplay = budgetHeadDisplay;
    }

    public String getUnderBudgetHead() {
        return underBudgetHead;
    }

    public void setUnderBudgetHead(String underBudgetHead) {
        this.underBudgetHead = underBudgetHead;
    }

    
    public String getGovtBudgetHead() {
        return govtBudgetHead;
    }

    public void setGovtBudgetHead(String govtBudgetHead) {
        this.govtBudgetHead = govtBudgetHead;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getBudgetHeadDesc() {
        return budgetHeadDesc;
    }

    public void setBudgetHeadDesc(String budgetHeadDesc) {
        this.budgetHeadDesc = budgetHeadDesc;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

   

    

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }
    
    
    
    
}
