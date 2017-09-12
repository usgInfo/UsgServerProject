/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class BudgetList extends Common {

    private String budgetHead;
    private String previousAmount;
    private String requestAmount;
    private String headStatus;
    private String sanctionedAmount;

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(String previousAmount) {
        this.previousAmount = previousAmount;
    }

    public String getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getHeadStatus() {
        return headStatus;
    }

    public void setHeadStatus(String headStatus) {
        this.headStatus = headStatus;
    }

    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

}
