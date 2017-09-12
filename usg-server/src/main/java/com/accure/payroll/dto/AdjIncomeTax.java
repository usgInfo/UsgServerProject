/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import java.util.List;

/**
 *
 * @author upendra
 */
public class AdjIncomeTax extends AdjIncomeTaxList {

    private String ddo;
    private String month;
    private String year;
    private String empcode;
    private String designation;
    private String nature;
    private String postingCity;
    private String fundType;
    private String budgetHead;
    private String pfType;
    private String sortBy;
    private List<AdjIncomeTaxList> incometaxList;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<AdjIncomeTaxList> getIncometaxList() {
        return incometaxList;
    }

    public void setIncometaxList(List<AdjIncomeTaxList> incometaxList) {
        this.incometaxList = incometaxList;
    }
}
