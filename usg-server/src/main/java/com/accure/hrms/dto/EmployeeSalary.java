/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import java.util.List;

/**
 *
 * @author upendra
 */
public class EmployeeSalary extends EarningHeadsDetails {

    private String employeeCode;
    private String employeeName;
    private String ddo;
    private String location;
    private String department;
    private String designation;
    private String grade;
    private String nature;
    private String postingCity;
    private String fundType;
    private String budgetHead;
    private String discipline;
    private String association;
    private String bank;
    private String acnumber;
    private String gradePay;
    private List<EarningHeadsDetails> earningHeads;
    private List<EarningHeadsDetails> deductionHeads;
    private String totalEarnings;
    private String totalDeductions;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAcnumber() {
        return acnumber;
    }

    public void setAcnumber(String acnumber) {
        this.acnumber = acnumber;
    }

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

    public List<EarningHeadsDetails> getEarningHeads() {
        return earningHeads;
    }

    public void setEarningHeads(List<EarningHeadsDetails> earningHeads) {
        this.earningHeads = earningHeads;
    }

    public List<EarningHeadsDetails> getDeductionHeads() {
        return deductionHeads;
    }

    public void setDeductionHeads(List<EarningHeadsDetails> deductionHeads) {
        this.deductionHeads = deductionHeads;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(String totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public String getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(String totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

}
