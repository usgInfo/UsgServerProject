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
public class BudgetNature extends Common {

    private String budgetNatureName;
    private String description;

    public String getBudgetNatureName() {
        return budgetNatureName;
    }

    public void setBudgetNatureName(String budgetNatureName) {
        this.budgetNatureName = budgetNatureName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
