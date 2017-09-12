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
public class BudgetSector extends Common{
    private String sectorCode;
    private String description;
    private String underSector;
    private String isIncome;
    private String isExpense;
    
//    only for View
    private String underSectorName;

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnderSector() {
        return underSector;
    }

    public void setUnderSector(String underSector) {
        this.underSector = underSector;
    }

    public String getIsIncome() {
        return isIncome;
    }

    public void setIsIncome(String isIncome) {
        this.isIncome = isIncome;
    }

    public String getIsExpense() {
        return isExpense;
    }

    public void setIsExpense(String isExpense) {
        this.isExpense = isExpense;
    }

    public String getUnderSectorName() {
        return underSectorName;
    }

    public void setUnderSectorName(String underSectorName) {
        this.underSectorName = underSectorName;
    }
    
    
    
}
