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
public class DeptWiseIncBudgetAllocation extends Common {

    private String incomeBudgetId;
    private String ddo;
    private String location;
    private String financialYear;
    private String budgetCode;
    private String fundType;
    private String budgetType;
    private String headDescription;
    private String budgetDate;
    private String sector;
    private String prievioueBudget;
    private String sanctionincomeDate;
    private String approvedAmount;
    private String askedForAmount;
    private String requestedAmount;
    private String consolidateBudgetAmount;
    private String consolidateBudgetStatus;
    private String consolidateBudgetDate;
    private String sanctionAmount;
    private String budgetIncomeSanctionId;
    private String consolidatedIncomeId;
    private String ledger;
    private String ledgerId;
    private String department;
    private String departmentName;
    private String extraProvisionAmount;

    public String getDepartment() {
        return department;
    }

    public void setDepartmentId(String department) {
        this.department = department;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getConsolidatedIncomeId() {
        return consolidatedIncomeId;
    }

    public void consolidatedIncomeId(String consolidatedIncomeId) {
        this.consolidatedIncomeId = consolidatedIncomeId;
    }

    public String getSanctionAmount() {
        return sanctionAmount;
    }

    public void setSanctionAmount(String sanctionAmount) {
        this.sanctionAmount = sanctionAmount;
    }

    public String getHeadDescription() {
        return headDescription;
    }

    public void setHeadDescription(String headDescription) {
        this.headDescription = headDescription;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }
    private String budgetHead;

    public String getSanctionincomeDate() {
        return sanctionincomeDate;
    }

    public void setSanctionincomeDate(String sanctionincomeDate) {
        this.sanctionincomeDate = sanctionincomeDate;
    }

    public String getIncomeBudgetId() {
        return incomeBudgetId;
    }

    public void setIncomeBudgetId(String incomeBudgetId) {
        this.incomeBudgetId = incomeBudgetId;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getBudgetDate() {
        return budgetDate;
    }

    public void setBudgetDate(String budgetDate) {
        this.budgetDate = budgetDate;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPrievioueBudget() {
        return prievioueBudget;
    }

    public void setPrievioueBudget(String prievioueBudget) {
        this.prievioueBudget = prievioueBudget;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getAskedForAmount() {
        return askedForAmount;
    }

    public void setAskedForAmount(String askedForAmount) {
        this.askedForAmount = askedForAmount;
    }

    public String getRequestedAmount() {

        return requestedAmount;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getConsolidateBudgetAmount() {
        return consolidateBudgetAmount;
    }

    public void setConsolidateBudgetAmount(String consolidateBudgetAmount) {
        this.consolidateBudgetAmount = consolidateBudgetAmount;
    }

    public String getConsolidateBudgetStatus() {
        return consolidateBudgetStatus;
    }

    public void setConsolidateBudgetStatus(String consolidateBudgetStatus) {
        this.consolidateBudgetStatus = consolidateBudgetStatus;
    }

    public String getConsolidateBudgetDate() {
        return consolidateBudgetDate;
    }

    public void setConsolidateBudgetDate(String consolidateBudgetDate) {
        this.consolidateBudgetDate = consolidateBudgetDate;
    }

    public String getBudgetIncomeSanctionId() {
        return budgetIncomeSanctionId;
    }

    public void setBudgetIncomeSanctionId(String budgetIncomeSanctionId) {
        this.budgetIncomeSanctionId = budgetIncomeSanctionId;
    }

    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getExtraProvisionAmount() {
        return extraProvisionAmount;
    }

    public void setExtraProvisionAmount(String extraProvisionAmount) {
        this.extraProvisionAmount = extraProvisionAmount;
    }

}
