/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class BudgetHeadMaster extends Common{

    
    private String govtBudgetHead;
    private String fundType;
    private String fundCategory;
    private String budgetHead;
    private String budgetHeadDescription;
    private String underBudgetHead;
    private String remarks;
    private String isActive;

    public String getGovtBudgetHead() {
        return govtBudgetHead;
    }

    public void setGovtBudgetHead(String govtBudgetHead) {
        this.govtBudgetHead = govtBudgetHead;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundCategory() {
        return fundCategory;
    }

    public void setFundCategory(String fundCategory) {
        this.fundCategory = fundCategory;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
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
