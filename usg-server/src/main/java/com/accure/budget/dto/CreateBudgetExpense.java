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
public class CreateBudgetExpense extends Common {

    private String ddo;
    private String ddoName;
    private String location;
    private String financialYear;
    private String financialYearId;
    private String fundType;
    private String fundTypeName;
    private String sector;
    private String sectorName;
    private String budgetType;
    private String budgetTypeName;
    private String budgetCode;
    private String budgetDate;
    private String previousBudget;
    private String remarks;
    private String headCode;
    private String headCodeId;
    private String consolidatedExpenseId;
    private String createExpenseId;
    private String isSanctioned;
    private String sanctionedAmount;
    private String requestedAmount;
    private String sentStatus;
    private String appropriationValue;
    private String budgetId;
    private String budgetHead;
    private String budgetHeadName;
    private String headDescription;
    private String approvedAmount;
    private String isConsolidate;
    private String ledgerId;
    private String ledgerName;
    private String srNo;
    private String department;
    private String isConsolidated;
    private String consolidatedTime;
    private String extraProvisionAmount;
    private String totalAmount;
    private String incomeBudgetId;
    private String extraProvisionId;
    private String isExtraProvisioned;

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(String financialYearId) {
        this.financialYearId = financialYearId;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public String getBudgetTypeName() {
        return budgetTypeName;
    }

    public void setBudgetTypeName(String budgetTypeName) {
        this.budgetTypeName = budgetTypeName;
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getIsConsolidate() {
        return isConsolidate;
    }

    public void setIsConsolidate(String isConsolidate) {
        this.isConsolidate = isConsolidate;
    }

    public String getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(String sentStatus) {
        this.sentStatus = sentStatus;
    }

    public String getHeadCode() {
        return headCode;
    }

    public void setHeadCode(String headCode) {
        this.headCode = headCode;
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

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getBudgetDate() {
        return budgetDate;
    }

    public void setBudgetDate(String budgetDate) {
        this.budgetDate = budgetDate;
    }

    public String getPreviousBudget() {
        return previousBudget;
    }

    public void setPreviousBudget(String previousBudget) {
        this.previousBudget = previousBudget;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getHeadCodeId() {
        return headCodeId;
    }

    public void setHeadCodeId(String headCodeId) {
        this.headCodeId = headCodeId;
    }

    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    public String getConsolidatedExpenseId() {
        return consolidatedExpenseId;
    }

    public void setConsolidatedExpenseId(String consolidatedExpenseId) {
        this.consolidatedExpenseId = consolidatedExpenseId;
    }

    public String getIsSanctioned() {
        return isSanctioned;
    }

    public void setIsSanctioned(String isSanctioned) {
        this.isSanctioned = isSanctioned;
    }

    public String getCreateExpenseId() {
        return createExpenseId;
    }

    public void setCreateExpenseId(String createExpenseId) {
        this.createExpenseId = createExpenseId;
    }

    public String getAppropriationValue() {
        return appropriationValue;
    }

    public void setAppropriationValue(String appropriationValue) {
        this.appropriationValue = appropriationValue;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIsConsolidated() {
        return isConsolidated;
    }

    public void setIsConsolidated(String isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public String getConsolidatedTime() {
        return consolidatedTime;
    }

    public void setConsolidatedTime(String consolidatedTime) {
        this.consolidatedTime = consolidatedTime;
    }

    public String getExtraProvisionAmount() {
        return extraProvisionAmount;
    }

    public void setExtraProvisionAmount(String extraProvisionAmount) {
        this.extraProvisionAmount = extraProvisionAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIncomeBudgetId() {
        return incomeBudgetId;
    }

    public void setIncomeBudgetId(String incomeBudgetId) {
        this.incomeBudgetId = incomeBudgetId;
    }

    public String getExtraProvisionId() {
        return extraProvisionId;
    }

    public void setExtraProvisionId(String extraProvisionId) {
        this.extraProvisionId = extraProvisionId;
    }

    public String getIsExtraProvisioned() {
        return isExtraProvisioned;
    }

    public void setIsExtraProvisioned(String isExtraProvisioned) {
        this.isExtraProvisioned = isExtraProvisioned;
    }
}
