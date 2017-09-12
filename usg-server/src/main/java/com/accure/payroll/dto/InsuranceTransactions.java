/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.hrms.dto.SalaryHead;
import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author user
 */
public class InsuranceTransactions extends Common {

    private String ddo;
    private String empCode;
    private String employeeName;
    private String department;
    private String designation;
    private String dated;
    private String inscName;
    private SalaryHead insInfo;
    private String policyNumber;
    private int installmentAmount;
    private int totalContribution;
    private String lastInscDate;
    private String inscDetails;
    private int totalInstallment;
    private List<SuppressedList> suppressedList;
    private String effectType;
    private String inscRemarks;
    private boolean checked;
    private int month;
    private int year;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getInscName() {
        return inscName;
    }

    public void setInscName(String inscName) {
        this.inscName = inscName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public int getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(int installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public int getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(int totalContribution) {
        this.totalContribution = totalContribution;
    }

    public String getLastInscDate() {
        return lastInscDate;
    }

    public void setLastInscDate(String lastInscDate) {
        this.lastInscDate = lastInscDate;
    }

    public String getInscDetails() {
        return inscDetails;
    }

    public void setInscDetails(String inscDetails) {
        this.inscDetails = inscDetails;
    }

    public int getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(int totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public List<SuppressedList> getSuppressedList() {
        return suppressedList;
    }

    public void setSuppressedList(List<SuppressedList> suppressedList) {
        this.suppressedList = suppressedList;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public String getRemarks() {
        return inscRemarks;
    }

    public void setRemarks(String inscRemarks) {
        this.inscRemarks = inscRemarks;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SalaryHead getInsInfo() {
        return insInfo;
    }

    public void setInsInfo(SalaryHead insInfo) {
        this.insInfo = insInfo;
    }

}
