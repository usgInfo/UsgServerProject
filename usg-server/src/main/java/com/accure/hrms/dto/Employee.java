/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author Asif
 */
public class Employee extends Common {

    private String pKey;
    private String employeeCode;
    private String employeeCodeM;
    private String salutationOption;
    private String employeeName;
    private String fatherName;
    private String gender;
    private String religion;
    private String maritalStatus;
    private String dob;
    private String email;
    private String panNo;
    private String remarks;
    //Bank details
    private String payMode;
    private String acnumber;
    private String bank;
    //other details
    private String ddo;
    //for display ddoName is created
    private String ddoName;
    private String location;
    private String department;
    private String discipline;
    private String natureType;
    private String fundType;
    private String budgetHead;
    private String reportingTo;
    private String pfBank;
    private String pfNumber;
    private String pfBalance;
    private String siPremNo;
    private String ptApplicable;
    private String stopGPF;
    private String auditNumber;
    private String workDetails;
    private String welfareNo;
    private String isPgTeacher;
    private String pgCode;
    private String postingDDO;

    //
    private String dateOfAppointment;
    private String dateOfJoining;
    private String lastAppointmentDate;
    private String lastJoiningDate;
    private String dateOfRetirement;
    private String EmployeeLeftStatus;
    private String EmployeeLeftReason;
    private String LeavingDate;
    private String LeavingRemarks;
    private String IncrementDueDate;
    private long IncrementDueDateInMilliSecond;
    private String onDeputation;
    private String fromDDO;
    private String association;
    private String isHandicapped;
    private String stopSalary;
    private String isGazetted;
    private String personalFileNo;
    private String salaryBillType;
    //salary sturcture
    private String headSlab;
    private String salaryType;
    private String designation;
    private String pfType;
    private String grade;
//    private String basic;
    private long basic;
    private String postingCity;
    private String postedDesignation;
    private String quarterNo;
    private long gradePay;
    private String incrementPercentage;
    private String salaryPostStatus;
    private String arrearStatus;
    private String isPromoted;
    private List<EarningHeadsDetails> earningHeads;
    private List<EarningHeadsDetails> deductionHeads;
    private double totalEarnings;
    private double totalDeductions;

    private long associationDate;

    private long stopSalaryDate;
    private long stopGPFDate;
    private long ptApplicableDate;
    private String classMaster;
    private String classMasterId;

    //Demographic
    private String currentAddress;
    private String currentContactNo;
    private String permanentAddress;
    private String permanentContactNo;
    private String identificationMarks;
    private String techQualification;
    private String height;
    private String scholarship;
    private String refference;
    private String updateAt;

    //Employee Promotion
    private String promotionCode;
    private String promotionStatus;
    private String promotedDate;

    //Employee Suspension
    private boolean isSuspended;
    private String empSuspendedId;
    private boolean imageAvailable;
    //For Fetching Data Based on dob,doj,dor
    private long dobInMillisecond;
    private long dateOfJoiningInMillisecond;
    private long dateOfRetirementInMillisecond;
    private long dateOfAppointmentInMillisecond;
    private long dateOfUnDeputationInMillisecond;
//Employee Salary Increment
    private String salaryIncId;

    public String getSalaryPostStatus() {
        return salaryPostStatus;
    }

    public void setSalaryPostStatus(String salaryPostStatus) {
        this.salaryPostStatus = salaryPostStatus;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCodeM() {
        return employeeCodeM;
    }

    public void setEmployeeCodeM(String employeeCodeM) {
        this.employeeCodeM = employeeCodeM;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getAcnumber() {
        return acnumber;
    }

    public void setAcnumber(String acnumber) {
        this.acnumber = acnumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
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

    public String getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(String reportingTo) {
        this.reportingTo = reportingTo;
    }

    public String getPfBank() {
        return pfBank;
    }

    public void setPfBank(String pfBank) {
        this.pfBank = pfBank;
    }

    public String getPfNumber() {
        return pfNumber;
    }

    public void setPfNumber(String pfNumber) {
        this.pfNumber = pfNumber;
    }

    public String getPfBalance() {
        return pfBalance;
    }

    public void setPfBalance(String pfBalance) {
        this.pfBalance = pfBalance;
    }

    public String getSiPremNo() {
        return siPremNo;
    }

    public void setSiPremNo(String siPremNo) {
        this.siPremNo = siPremNo;
    }

    public String getPtApplicable() {
        return ptApplicable;
    }

    public void setPtApplicable(String ptApplicable) {
        this.ptApplicable = ptApplicable;
    }

    public String getStopGPF() {
        return stopGPF;
    }

    public void setStopGPF(String stopGPF) {
        this.stopGPF = stopGPF;
    }

    public String getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(String auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getWorkDetails() {
        return workDetails;
    }

    public void setWorkDetails(String workDetails) {
        this.workDetails = workDetails;
    }

    public String getWelfareNo() {
        return welfareNo;
    }

    public void setWelfareNo(String welfareNo) {
        this.welfareNo = welfareNo;
    }

    public String getIsPgTeacher() {
        return isPgTeacher;
    }

    public void setIsPgTeacher(String isPgTeacher) {
        this.isPgTeacher = isPgTeacher;
    }

    public String getPgCode() {
        return pgCode;
    }

    public void setPgCode(String pgCode) {
        this.pgCode = pgCode;
    }

    public String getPostingDDO() {
        return postingDDO;
    }

    public void setPostingDDO(String postingDDO) {
        this.postingDDO = postingDDO;
    }

    public String getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(String dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getLastAppointmentDate() {
        return lastAppointmentDate;
    }

    public void setLastAppointmentDate(String lastAppointmentDate) {
        this.lastAppointmentDate = lastAppointmentDate;
    }

    public String getLastJoiningDate() {
        return lastJoiningDate;
    }

    public void setLastJoiningDate(String lastJoiningDate) {
        this.lastJoiningDate = lastJoiningDate;
    }

    public String getEmployeeLeftStatus() {
        return EmployeeLeftStatus;
    }

    public void setEmployeeLeftStatus(String EmployeeLeftStatus) {
        this.EmployeeLeftStatus = EmployeeLeftStatus;
    }

    public String getEmployeeLeftReason() {
        return EmployeeLeftReason;
    }

    public void setEmployeeLeftReason(String EmployeeLeftReason) {
        this.EmployeeLeftReason = EmployeeLeftReason;
    }

    public String getLeavingDate() {
        return LeavingDate;
    }

    public void setLeavingDate(String LeavingDate) {
        this.LeavingDate = LeavingDate;
    }

    public String getLeavingRemarks() {
        return LeavingRemarks;
    }

    public void setLeavingRemarks(String LeavingRemarks) {
        this.LeavingRemarks = LeavingRemarks;
    }

    public String getIncrementDueDate() {
        return IncrementDueDate;
    }

    public void setIncrementDueDate(String IncrementDueDate) {
        this.IncrementDueDate = IncrementDueDate;
    }

    public String getOnDeputation() {
        return onDeputation;
    }

    public void setOnDeputation(String onDeputation) {
        this.onDeputation = onDeputation;
    }

    public String getFromDDO() {
        return fromDDO;
    }

    public void setFromDDO(String fromDDO) {
        this.fromDDO = fromDDO;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public String getIsHandicapped() {
        return isHandicapped;
    }

    public void setIsHandicapped(String isHandicapped) {
        this.isHandicapped = isHandicapped;
    }

    public String getStopSalary() {
        return stopSalary;
    }

    public void setStopSalary(String stopSalary) {
        this.stopSalary = stopSalary;
    }

    public String getIsGazetted() {
        return isGazetted;
    }

    public void setIsGazetted(String isGazetted) {
        this.isGazetted = isGazetted;
    }

    public String getPersonalFileNo() {
        return personalFileNo;
    }

    public void setPersonalFileNo(String personalFileNo) {
        this.personalFileNo = personalFileNo;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public long getBasic() {
        return basic;
    }

    public void setBasic(long basic) {
        this.basic = basic;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    public String getPostedDesignation() {
        return postedDesignation;
    }

    public void setPostedDesignation(String postedDesignation) {
        this.postedDesignation = postedDesignation;
    }

    public String getQuarterNo() {
        return quarterNo;
    }

    public void setQuarterNo(String quarterNo) {
        this.quarterNo = quarterNo;
    }

    public String getIncrementPercentage() {
        return incrementPercentage;
    }

    public void setIncrementPercentage(String incrementPercentage) {
        this.incrementPercentage = incrementPercentage;
    }

    public String getSalutationOption() {
        return salutationOption;
    }

    public void setSalutationOption(String salutationOption) {
        this.salutationOption = salutationOption;
    }

    public String getArrearStatus() {
        return arrearStatus;
    }

    public void setArrearStatus(String arrearStatus) {
        this.arrearStatus = arrearStatus;
    }

    public String getSalaryBillType() {
        return salaryBillType;
    }

    public void setSalaryBillType(String salaryBillType) {
        this.salaryBillType = salaryBillType;
    }

    public String getHeadSlab() {
        return headSlab;
    }

    public void setHeadSlab(String headSlab) {
        this.headSlab = headSlab;
    }

    public String getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(String isPromoted) {
        this.isPromoted = isPromoted;
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

    public void setTotalDeductions(long totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public String getpKey() {
        return pKey;
    }

    public void setpKey(String pKey) {
        this.pKey = pKey;
    }

    public String getClassMaster() {
        return classMaster;
    }

    public void setClassMaster(String classMaster) {
        this.classMaster = classMaster;
    }

    public long getAssociationDate() {
        return associationDate;
    }

    public void setAssociationDate(long associationDate) {
        this.associationDate = associationDate;
    }

    public long getStopSalaryDate() {
        return stopSalaryDate;
    }

    public void setStopSalaryDate(long stopSalaryDate) {
        this.stopSalaryDate = stopSalaryDate;
    }

    public long getStopGPFDate() {
        return stopGPFDate;
    }

    public void setStopGPFDate(long stopGPFDate) {
        this.stopGPFDate = stopGPFDate;
    }

    public long getPtApplicableDate() {
        return ptApplicableDate;
    }

    public void setPtApplicableDate(long ptApplicableDate) {
        this.ptApplicableDate = ptApplicableDate;
    }

    public long getIncrementDueDateInMilliSecond() {
        return IncrementDueDateInMilliSecond;
    }

    public void setIncrementDueDateInMilliSecond(long IncrementDueDateInMilliSecond) {
        this.IncrementDueDateInMilliSecond = IncrementDueDateInMilliSecond;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentContactNo() {
        return currentContactNo;
    }

    public void setCurrentContactNo(String currentContactNo) {
        this.currentContactNo = currentContactNo;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentContactNo() {
        return permanentContactNo;
    }

    public void setPermanentContactNo(String permanentContactNo) {
        this.permanentContactNo = permanentContactNo;
    }

    public String getIdentificationMarks() {
        return identificationMarks;
    }

    public void setIdentificationMarks(String identificationMarks) {
        this.identificationMarks = identificationMarks;
    }

    public String getTechQualification() {
        return techQualification;
    }

    public void setTechQualification(String techQualification) {
        this.techQualification = techQualification;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getRefference() {
        return refference;
    }

    public void setRefference(String refference) {
        this.refference = refference;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public long getGradePay() {
        return gradePay;
    }

    public void setGradePay(long gradePay) {
        this.gradePay = gradePay;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getPromotionStatus() {
        return promotionStatus;
    }

    public void setPromotionStatus(String promotionStatus) {
        this.promotionStatus = promotionStatus;
    }

    public String getPromotedDate() {
        return promotedDate;
    }

    public void setPromotedDate(String promotedDate) {
        this.promotedDate = promotedDate;
    }

    public boolean getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getEmpSuspendedId() {
        return empSuspendedId;
    }

    public void setEmpSuspendedId(String empSuspendedId) {
        this.empSuspendedId = empSuspendedId;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public boolean isImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(boolean imageAvailable) {
        this.imageAvailable = imageAvailable;
    }

    public String getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(String dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public long getDobInMillisecond() {
        return dobInMillisecond;
    }

    public void setDobInMillisecond(long dobInMillisecond) {
        this.dobInMillisecond = dobInMillisecond;
    }

    public long getDateOfJoiningInMillisecond() {
        return dateOfJoiningInMillisecond;
    }

    public void setDateOfJoiningInMillisecond(long dateOfJoiningInMillisecond) {
        this.dateOfJoiningInMillisecond = dateOfJoiningInMillisecond;
    }

    public long getDateOfRetirementInMillisecond() {
        return dateOfRetirementInMillisecond;
    }

    public void setDateOfRetirementInMillisecond(long dateOfRetirementInMillisecond) {
        this.dateOfRetirementInMillisecond = dateOfRetirementInMillisecond;
    }

    public long getDateOfAppointmentInMillisecond() {
        return dateOfAppointmentInMillisecond;
    }

    public void setDateOfAppointmentInMillisecond(long dateOfAppointmentInMillisecond) {
        this.dateOfAppointmentInMillisecond = dateOfAppointmentInMillisecond;
    }

    public String getSalaryIncId() {
        return salaryIncId;
    }

    public void setSalaryIncId(String salaryIncId) {
        this.salaryIncId = salaryIncId;
    }

    public long getDateOfUnDeputationInMillisecond() {
        return dateOfUnDeputationInMillisecond;
    }

    public void setDateOfUnDeputationInMillisecond(long dateOfUnDeputationInMillisecond) {
        this.dateOfUnDeputationInMillisecond = dateOfUnDeputationInMillisecond;
    }

    public String getClassMasterId() {
        return classMasterId;
    }

    public void setClassMasterId(String classMasterId) {
        this.classMasterId = classMasterId;
    }
}
