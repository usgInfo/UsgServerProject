/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Misha Thomas
 */
public class PensionEmployeeDetails extends Common {

    private String employeeCode;
    private String employeeCodeM;
    private String salutationOption;
    private String employeeName;
    private String fatherName;
    private String gender;
    private String religion;
    private String religionName;
    private String maritalStatus;
    private String dob;
    private String email;
    private String panNo;
    private String remarks;
    //private String category;
    //Bank details
    private String ddo;
    //for display ddoName is created
    private String ddoName;
    private String location;
    private String locationName;
    private String department;
    private String departmentName;
    private String discipline;
    private String disciplineName;

    private String natureType;
    private String natureTypeName;
    private String fundType;
    private String fundTypeName;
    private String budgetHead;
    private String budgetHeadName;
    private String grade;
    private String gradePay;
    private String leavingReason;

    public String getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(String leavingReason) {
        this.leavingReason = leavingReason;
    }
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    //
    private String dateOfAppointment;
    private String dateOfJoining;
    private String lastAppointmentDate;
    private String lastJoiningDate;
    private String dateOfRetirement;
    private String LeavingDate;
    private String LeavingRemarks;

    public String getLeavingRemarks() {
        return LeavingRemarks;
    }

    public void setLeavingRemarks(String LeavingRemarks) {
        this.LeavingRemarks = LeavingRemarks;
    }
    //salary sturcture
    private String designation;
    private String designationName;
//    private String basic;

    private long dobInMillisecond;
    private long dateOfJoiningInMillisecond;
    private long dateOfRetirementInMillisecond;
    private long dateOfAppointmentInMillisecond;
    private long dateOfUnDeputationInMillisecond;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getNatureTypeName() {
        return natureTypeName;
    }

    public void setNatureTypeName(String natureTypeName) {
        this.natureTypeName = natureTypeName;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }

    public String getBudgetHeadName() {
        return budgetHeadName;
    }

    public void setBudgetHeadName(String budgetHeadName) {
        this.budgetHeadName = budgetHeadName;
    }

    public boolean isImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(boolean imageAvailable) {
        this.imageAvailable = imageAvailable;
    }
    private boolean imageAvailable;

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

    public String getSalutationOption() {
        return salutationOption;
    }

    public void setSalutationOption(String salutationOption) {
        this.salutationOption = salutationOption;
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

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
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

    public String getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(String dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public String getLeavingDate() {
        return LeavingDate;
    }

    public void setLeavingDate(String LeavingDate) {
        this.LeavingDate = LeavingDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public long getDateOfUnDeputationInMillisecond() {
        return dateOfUnDeputationInMillisecond;
    }

    public void setDateOfUnDeputationInMillisecond(long dateOfUnDeputationInMillisecond) {
        this.dateOfUnDeputationInMillisecond = dateOfUnDeputationInMillisecond;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

}
