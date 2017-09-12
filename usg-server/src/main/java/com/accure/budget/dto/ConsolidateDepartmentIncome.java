/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author upendra
 */
public class ConsolidateDepartmentIncome extends Common {

    private String ddo;
    private String location;
    private String financialYear;
    private String financialYearName;
    private String fundType;
    private String fundTypeName;
    private String budgetType;
    private String budgetTypeName;
    private String sector;
    private String sectorName;
    private String budgetHead;
    private String department;
    private String departmentName;
    private String budgetHeadName;
    private String ledger;
    private String ledgerId;
    private String requestedAmount;
    private String askedForAmount;
    private String consolidateBudgetStatus;
    private String consolidateBudgetDate;
    private String sanctionedAmount;
    private String revisedSanctionedAmount;
    private String isSanctioned;
    private String isConsolidated;
    private List<String> incomeBudgetIdList;
    private String srNo;
    private String incomeBudgetId;
    private String headDescriptionName;

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

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getBudgetTypeName() {
        return budgetTypeName;
    }

    public void setBudgetTypeName(String budgetTypeName) {
        this.budgetTypeName = budgetTypeName;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
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

    public String getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public String getAskedForAmount() {
        return askedForAmount;
    }

    public void setAskedForAmount(String askedForAmount) {
        this.askedForAmount = askedForAmount;
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

    public String getSanctionedAmount() {
        return sanctionedAmount;
    }

    public void setSanctionedAmount(String sanctionedAmount) {
        this.sanctionedAmount = sanctionedAmount;
    }

    public String getRevisedSanctionedAmount() {
        return revisedSanctionedAmount;
    }

    public void setRevisedSanctionedAmount(String revisedSanctionedAmount) {
        this.revisedSanctionedAmount = revisedSanctionedAmount;
    }

    public String getIsSanctioned() {
        return isSanctioned;
    }

    public void setIsSanctioned(String isSanctioned) {
        this.isSanctioned = isSanctioned;
    }

    public List<String> getIncomeBudgetIdList() {
        return incomeBudgetIdList;
    }

    public void setIncomeBudgetIdList(List<String> incomeBudgetIdList) {
        this.incomeBudgetIdList = incomeBudgetIdList;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIncomeBudgetId() {
        return incomeBudgetId;
    }

    public void setIncomeBudgetId(String incomeBudgetId) {
        this.incomeBudgetId = incomeBudgetId;
    }

    public String getIsConsolidated() {
        return isConsolidated;
    }

    public void setIsConsolidated(String isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public String getFinancialYearName() {
        return financialYearName;
    }

    public void setFinancialYearName(String financialYearName) {
        this.financialYearName = financialYearName;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getHeadDescriptionName() {
        return headDescriptionName;
    }

    public void setHeadDescriptionName(String headDescriptionName) {
        this.headDescriptionName = headDescriptionName;
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
}
