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
 * @author accure
 */
public class ExpenseBudget extends Common {

    private String ddo;
    private String location;
    private String fundtype;
    private String sector;
    private String finYear;
    private String budgetCode;
    private String budgetType;
    private String date;
    private String preBudget;
    private String remarks;
    private String percentageType;
    private String percentage;
    private String budgetStatus;
   // private List<BudgetList> budgetHeadList;
    private String budgetHead;
    private String previousAmount;
    private String requestAmount;
    private String headStatus;
    private String sanctionedAmount;
    private String approvedAmount;
    private String isConsolidate;

    public String getApprovedAmount() {
        return approvedAmount;
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

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreBudget() {
        return preBudget;
    }

    public void setPreBudget(String preBudget) {
        this.preBudget = preBudget;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPercentageType() {
        return percentageType;
    }

    public void setPercentageType(String percentageType) {
        this.percentageType = percentageType;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

//    public List<BudgetList> getBudgetHeadList() {
//        return budgetHeadList;
//    }
//
//    public void setBudgetHeadList(List<BudgetList> budgetHeadList) {
//        this.budgetHeadList = budgetHeadList;
//    }

    public String getBudgetStatus() {
        return budgetStatus;
    }

    public void setBudgetStatus(String budgetStatus) {
        this.budgetStatus = budgetStatus;
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

}
