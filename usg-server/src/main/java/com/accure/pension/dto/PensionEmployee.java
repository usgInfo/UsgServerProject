/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class PensionEmployee extends Common {

    private String employeecode;
    private String pensionType;
    private String pensionClassification;
    private String manualCode;
    private String payMode;
    private String employeeName;
    private String bank;
    private String fatherName;
    private String motherName;
    private String accountNumber;
    private String dateofBirth;
    private String DDO;
    private String appointmentDate;
    private String location;
    private String retDate;
    private String pensionStartDate;
    private String department;
    private String designation;
    private String gender;
    private String comMatDate;
    private String incDate;
    private String height;
    private String empStatus;
    private String deathDate;
    private String religion;
    private String ageOnNextDob;
    private String casteCategory;
    private String association;
    private String corrAddress;
    private String permanentAddress;
    private String contactNOC;
    private String contactNOP;
    private String date;
    private String pensionOrderNum;
    private String pensionLeftStatus;
    private String leftDate;
    private String indentationMark;
    private String remarks;
    private String stopPension;
    private String grade;
    private String gradePay;
    private String pension;
    private String familyPension;
    private String correspondingPayScale;
    private String formula;
    
    
    //these are not applicable

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

    public String getCorrespondingPayScale() {
        return correspondingPayScale;
    }

    public void setCorrespondingPayScale(String correspondingPayScale) {
        this.correspondingPayScale = correspondingPayScale;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    
    private String qualifyingServiceYear;
    private String qualifyingServiceYearR;
    private String nonQualifyingServiceYearR;
    private String bankName;
    private String lastDrawnPayWithDA;
    private String commutedPension;
    private String commutedAmount;
    private String lastDrawnPayWithoutDA;
    private String monthlyCommutedAmount;
    private String commFact;
    private String residualPension;
    private String AverageEmoluments;
    private String gratuity;
    private String deathGratuity;
    private String lessAmountFromGratuity;
    private String famPensionAfterYear;
    private String it;
    private String familyPensionYearly;
    private String otherDeduction;
    private String locationName;
    private String departmentName; 
    private String designationName;
    private String DDOName;
    private String salaryTypeName;
    private String salaryType;

    public String getSalaryTypeName() {
        return salaryTypeName;
    }

    public void setSalaryTypeName(String salaryTypeName) {
        this.salaryTypeName = salaryTypeName;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getDDOName() {
        return DDOName;
    }

    public void setDDOName(String DDOName) {
        this.DDOName = DDOName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }
      

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
 
    private String processStatus;
    private String commutedPercentage;

    private List<PensionEmployeeNominiList> pensionEmployeenomineeList;

    public String getCommutedPercentage() {
        return commutedPercentage;
    }

    public void setCommutedPercentage(String commutedPercentage) {
        this.commutedPercentage = commutedPercentage;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    public String getPensionStartDate() {
        return pensionStartDate;
    }

    public void setPensionStartDate(String pensionStartDate) {
        this.pensionStartDate = pensionStartDate;
    }

    public String getQualifyingServiceYear() {
        return qualifyingServiceYear;
    }

    public void setQualifyingServiceYear(String qualifyingServiceYear) {
        this.qualifyingServiceYear = qualifyingServiceYear;
    }

    public String getQualifyingServiceYearR() {
        return qualifyingServiceYearR;
    }

    public void setQualifyingServiceYearR(String qualifyingServiceYearR) {
        this.qualifyingServiceYearR = qualifyingServiceYearR;
    }

    public String getNonQualifyingServiceYearR() {
        return nonQualifyingServiceYearR;
    }

    public void setNonQualifyingServiceYearR(String nonQualifyingServiceYearR) {
        this.nonQualifyingServiceYearR = nonQualifyingServiceYearR;
    }

    public String getRetDate() {
        return retDate;
    }

    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    public String getPensionType() {
        return pensionType;
    }

    public void setPensionType(String pensionType) {
        this.pensionType = pensionType;
    }

    public String getPensionClassification() {
        return pensionClassification;
    }

    public void setPensionClassification(String pensionClassification) {
        this.pensionClassification = pensionClassification;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getComMatDate() {
        return comMatDate;
    }

    public void setComMatDate(String comMatDate) {
        this.comMatDate = comMatDate;
    }

    public String getIncDate() {
        return incDate;
    }

    public void setIncDate(String incDate) {
        this.incDate = incDate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getAgeOnNextDob() {
        return ageOnNextDob;
    }

    public void setAgeOnNextDob(String ageOnNextDob) {
        this.ageOnNextDob = ageOnNextDob;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public String getCorrAddress() {
        return corrAddress;
    }

    public void setCorrAddress(String corrAddress) {
        this.corrAddress = corrAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getContactNOC() {
        return contactNOC;
    }

    public void setContactNOC(String contactNOC) {
        this.contactNOC = contactNOC;
    }

    public String getContactNOP() {
        return contactNOP;
    }

    public void setContactNOP(String contactNOP) {
        this.contactNOP = contactNOP;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPensionOrderNum() {
        return pensionOrderNum;
    }

    public void setPensionOrderNum(String pensionOrderNum) {
        this.pensionOrderNum = pensionOrderNum;
    }

    public String getPensionLeftStatus() {
        return pensionLeftStatus;
    }

    public void setPensionLeftStatus(String pensionLeftStatus) {
        this.pensionLeftStatus = pensionLeftStatus;
    }

    public String getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(String leftDate) {
        this.leftDate = leftDate;
    }

    public String getIndentationMark() {
        return indentationMark;
    }

    public void setIndentationMark(String indentationMark) {
        this.indentationMark = indentationMark;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStopPension() {
        return stopPension;
    }

    public void setStopPension(String stopPension) {
        this.stopPension = stopPension;
    }

    public String getLastDrawnPayWithDA() {
        return lastDrawnPayWithDA;
    }

    public void setLastDrawnPayWithDA(String lastDrawnPayWithDA) {
        this.lastDrawnPayWithDA = lastDrawnPayWithDA;
    }

    public String getCommutedPension() {
        return commutedPension;
    }

    public void setCommutedPension(String commutedPension) {
        this.commutedPension = commutedPension;
    }

    public String getCommutedAmount() {
        return commutedAmount;
    }

    public void setCommutedAmount(String commutedAmount) {
        this.commutedAmount = commutedAmount;
    }

    public String getLastDrawnPayWithoutDA() {
        return lastDrawnPayWithoutDA;
    }

    public void setLastDrawnPayWithoutDA(String lastDrawnPayWithoutDA) {
        this.lastDrawnPayWithoutDA = lastDrawnPayWithoutDA;
    }

    public String getMonthlyCommutedAmount() {
        return monthlyCommutedAmount;
    }

    public void setMonthlyCommutedAmount(String monthlyCommutedAmount) {
        this.monthlyCommutedAmount = monthlyCommutedAmount;
    }

    public String getCommFact() {
        return commFact;
    }

    public void setCommFact(String commFact) {
        this.commFact = commFact;
    }

    public String getResidualPension() {
        return residualPension;
    }

    public void setResidualPension(String residualPension) {
        this.residualPension = residualPension;
    }

    public String getAverageEmoluments() {
        return AverageEmoluments;
    }

    public void setAverageEmoluments(String AverageEmoluments) {
        this.AverageEmoluments = AverageEmoluments;
    }

    public String getGratuity() {
        return gratuity;
    }

    public void setGratuity(String gratuity) {
        this.gratuity = gratuity;
    }

    public String getPension() {
        return pension;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getDeathGratuity() {
        return deathGratuity;
    }

    public void setDeathGratuity(String deathGratuity) {
        this.deathGratuity = deathGratuity;
    }

    public String getFamilyPension() {
        return familyPension;
    }

    public void setFamilyPension(String familyPension) {
        this.familyPension = familyPension;
    }

    public String getLessAmountFromGratuity() {
        return lessAmountFromGratuity;
    }

    public void setLessAmountFromGratuity(String lessAmountFromGratuity) {
        this.lessAmountFromGratuity = lessAmountFromGratuity;
    }

    public String getFamPensionAfterYear() {
        return famPensionAfterYear;
    }

    public void setFamPensionAfterYear(String famPensionAfterYear) {
        this.famPensionAfterYear = famPensionAfterYear;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getFamilyPensionYearly() {
        return familyPensionYearly;
    }

    public void setFamilyPensionYearly(String familyPensionYearly) {
        this.familyPensionYearly = familyPensionYearly;
    }

    public String getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(String otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public void setDateofBirth(String dateofBirth) {
        this.dateofBirth = dateofBirth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCasteCategory() {
        return casteCategory;
    }

    public void setCasteCategory(String casteCategory) {
        this.casteCategory = casteCategory;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDDO() {
        return DDO;
    }

    public void setDDO(String DDO) {
        this.DDO = DDO;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<PensionEmployeeNominiList> getPensionEmployeenomineeList() {
        return pensionEmployeenomineeList;
    }

    public void setPensionEmployeenomineeList(List<PensionEmployeeNominiList> pensionEmployeenomineeList) {
        this.pensionEmployeenomineeList = pensionEmployeenomineeList;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

}
