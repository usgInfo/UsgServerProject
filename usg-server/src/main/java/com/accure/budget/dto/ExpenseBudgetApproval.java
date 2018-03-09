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
public class ExpenseBudgetApproval extends Common {

    private String ddo;
    private String location;
    private String fundtype;
    private String sector;
    private String finYear;
    private String budgetType;
    private String budgetStatus;
    private String approvedAmount;
    private String budgetHead;
    private String previousAmount;
    private String requestAmount;
    private String headStatus;
    private String sanctionedAmount;
    private String consolidatedExpenseId;
    private String createExpenseId;
    private String ledgerId;
    private String ledgerName;
    private String budgetHeadName;
    private String appropriationValue;
    private String departmentName;
    private String consolidateBudgetStatus;
    private String incomeBudgetId;
    private String department;
    private List<String> departments;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAppropriationValue() {
        return appropriationValue;
    }

    public void setAppropriationValue(String appropriationValue) {
        this.appropriationValue = appropriationValue;
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

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getBudgetStatus() {
        return budgetStatus;
    }

    public void setBudgetStatus(String budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

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

    public String getConsolidatedExpenseId() {
        return consolidatedExpenseId;
    }

    public void setConsolidatedExpenseId(String consolidatedExpenseId) {
        this.consolidatedExpenseId = consolidatedExpenseId;
    }

    public String getCreateExpenseId() {
        return createExpenseId;
    }

    public void setCreateExpenseId(String createExpenseId) {
        this.createExpenseId = createExpenseId;
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

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getConsolidateBudgetStatus() {
        return consolidateBudgetStatus;
    }

    public void setConsolidateBudgetStatus(String consolidateBudgetStatus) {
        this.consolidateBudgetStatus = consolidateBudgetStatus;
    }

    public String getIncomeBudgetId() {
        return incomeBudgetId;
    }

    public void setIncomeBudgetId(String incomeBudgetId) {
        this.incomeBudgetId = incomeBudgetId;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }
}
