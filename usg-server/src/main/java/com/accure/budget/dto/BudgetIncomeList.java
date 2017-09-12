/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

/**
 *
 * @author user
 */
public class BudgetIncomeList {
   
    private String ddoList;
    private String budgetHead;
    private String headDescription;
    private String requestedAmount;

    public String getDdoList() {
        return ddoList;
    }

    public void setDdoList(String ddoList) {
        this.ddoList = ddoList;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getHeadDescription() {
        return headDescription;
    }

    public void setHeadDescription(String headDescription) {
        this.headDescription = headDescription;
    }

    public String getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
    
    
    
}
