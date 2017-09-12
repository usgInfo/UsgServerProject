/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class PensionRevision extends Common {

    private String employeeCode;
    private String employeeName;
    private String revisionstatus;
    private String retirementDate;
    private String deathdate;
    private double lastPayDrawWithDA;
    private double newLastPayDrawWithDA;
    private double lastPayDrawWithoutDA;
    private double newlastPayDrawWithoutDA;
    private int dueMonth;
    private int dueYear;
    private String dated;
    private String remarks;
    private String revisionCode;

    private String qualifyingService;
  
    private String qualifyingServiceR;
    private String nonqualifyingServiceR;
    private double commutedPercentage;
    private double commFactor;
    private double residualPension;
    private double newPaylastDrawnWithDA;
    private double newPaylastDrawnWithoutDA;
    private double monthlyCommutedAmount;
    private double averageEmoluments;
    private double gratuity;
    private double deathGratuity;
    private double LessAmountFromGratuity;
    private double familyPension;
    private double familyPensionYear;
    private double familyPensionAfterYear;
    private double pension;
    private double it;
    private double otherDeduction;
    private double commutedAmount;

    public double getNewPaylastDrawnWithoutDA() {
        return newPaylastDrawnWithoutDA;
    }

    public void setNewPaylastDrawnWithoutDA(double newPaylastDrawnWithoutDA) {
        this.newPaylastDrawnWithoutDA = newPaylastDrawnWithoutDA;
    }

    public double getCommutedAmount() {
        return commutedAmount;
    }

    public void setCommutedAmount(double commutedAmount) {
        this.commutedAmount = commutedAmount;
    }

    public String getQualifyingService() {
        return qualifyingService;
    }

    public void setQualifyingService(String qualifyingService) {
        this.qualifyingService = qualifyingService;
    }

    

    public String getQualifyingServiceR() {
        return qualifyingServiceR;
    }

    public void setQualifyingServiceR(String qualifyingServiceR) {
        this.qualifyingServiceR = qualifyingServiceR;
    }

    public String getNonqualifyingServiceR() {
        return nonqualifyingServiceR;
    }

    public void setNonqualifyingServiceR(String nonqualifyingServiceR) {
        this.nonqualifyingServiceR = nonqualifyingServiceR;
    }

    public double getCommutedPercentage() {
        return commutedPercentage;
    }

    public void setCommutedPercentage(double commutedPercentage) {
        this.commutedPercentage = commutedPercentage;
    }

    public double getCommFactor() {
        return commFactor;
    }

    public void setCommFactor(double commFactor) {
        this.commFactor = commFactor;
    }

    public double getResidualPension() {
        return residualPension;
    }

    public void setResidualPension(double residualPension) {
        this.residualPension = residualPension;
    }

    public double getNewPaylastDrawnWithDA() {
        return newPaylastDrawnWithDA;
    }

    public void setNewPaylastDrawnWithDA(double newPaylastDrawnWithDA) {
        this.newPaylastDrawnWithDA = newPaylastDrawnWithDA;
    }

    public double getMonthlyCommutedAmount() {
        return monthlyCommutedAmount;
    }

    public void setMonthlyCommutedAmount(double monthlyCommutedAmount) {
        this.monthlyCommutedAmount = monthlyCommutedAmount;
    }

    public double getAverageEmoluments() {
        return averageEmoluments;
    }

    public void setAverageEmoluments(double averageEmoluments) {
        this.averageEmoluments = averageEmoluments;
    }

    public double getGratuity() {
        return gratuity;
    }

    public void setGratuity(double gratuity) {
        this.gratuity = gratuity;
    }

    public double getDeathGratuity() {
        return deathGratuity;
    }

    public void setDeathGratuity(double deathGratuity) {
        this.deathGratuity = deathGratuity;
    }

    public double getLessAmountFromGratuity() {
        return LessAmountFromGratuity;
    }

    public void setLessAmountFromGratuity(double LessAmountFromGratuity) {
        this.LessAmountFromGratuity = LessAmountFromGratuity;
    }

    public double getFamilyPension() {
        return familyPension;
    }

    public void setFamilyPension(double familyPension) {
        this.familyPension = familyPension;
    }

    public double getFamilyPensionYear() {
        return familyPensionYear;
    }

    public void setFamilyPensionYear(double familyPensionYear) {
        this.familyPensionYear = familyPensionYear;
    }

    public double getFamilyPensionAfterYear() {
        return familyPensionAfterYear;
    }

    public void setFamilyPensionAfterYear(double familyPensionAfterYear) {
        this.familyPensionAfterYear = familyPensionAfterYear;
    }

    public double getPension() {
        return pension;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }

    public double getIt() {
        return it;
    }

    public void setIt(double it) {
        this.it = it;
    }

    public double getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(double otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public String getRevisionstatus() {
        return revisionstatus;
    }

    public void setRevisionstatus(String revisionstatus) {
        this.revisionstatus = revisionstatus;
    }

    public String getRevisionCode() {
        return revisionCode;
    }

    public void setRevisionCode(String revisionCode) {
        this.revisionCode = revisionCode;
    }

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

    public String getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(String retirementDate) {
        this.retirementDate = retirementDate;
    }

    public String getDeathdate() {
        return deathdate;
    }

    public void setDeathdate(String deathdate) {
        this.deathdate = deathdate;
    }

    public double getLastPayDrawWithDA() {
        return lastPayDrawWithDA;
    }

    public void setLastPayDrawWithDA(double lastPayDrawWithDA) {
        this.lastPayDrawWithDA = lastPayDrawWithDA;
    }

    public double getNewLastPayDrawWithDA() {
        return newLastPayDrawWithDA;
    }

    public void setNewLastPayDrawWithDA(double newLastPayDrawWithDA) {
        this.newLastPayDrawWithDA = newLastPayDrawWithDA;
    }

    public double getLastPayDrawWithoutDA() {
        return lastPayDrawWithoutDA;
    }

    public void setLastPayDrawWithoutDA(double lastPayDrawWithoutDA) {
        this.lastPayDrawWithoutDA = lastPayDrawWithoutDA;
    }

    public double getNewlastPayDrawWithoutDA() {
        return newlastPayDrawWithoutDA;
    }

    public void setNewlastPayDrawWithoutDA(double newlastPayDrawWithoutDA) {
        this.newlastPayDrawWithoutDA = newlastPayDrawWithoutDA;
    }

    public int getDueMonth() {
        return dueMonth;
    }

    public void setDueMonth(int dueMonth) {
        this.dueMonth = dueMonth;
    }

    public int getDueYear() {
        return dueYear;
    }

    public void setDueYear(int dueYear) {
        this.dueYear = dueYear;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
