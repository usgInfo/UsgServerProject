/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Mano
 */
public class EmpAttendance extends Common {

    private String employeeCode;
    private String employeeName;
    private String employeeCodeM;
    private String department;
    private String postingCity;
    private String location;
    private String designation;
    private String salaryType;
    private String pfType;
    private String fundType;
    private String budgetHead;
    private String natureType;
    private int totalDays;
    private int present;
    private int sickLeave;
    private int leaveWithoutPay;
    private int pbsP;
    private int pasP;
    private boolean sd;
    private String remarks;
    private boolean lockStatus;
    private String ddo;
    private int month;
    private int year;
    private String attendaceStatus;
    private boolean onDeputaion;
    private boolean isSuspended;
    private String stopSalary;
    private long stopSalaryDate;
    boolean isattendanceAdjFlag;
    private boolean salaryProcessed = false;
    private boolean payStopSalaryProcessed = false;
    private String gradeId;
    private String gradeName;
    private String pfTypeName;

    public boolean isIsattendanceAdjFlag() {
        return isattendanceAdjFlag;
    }

    public void setIsattendanceAdjFlag(boolean isattendanceAdjFlag) {
        this.isattendanceAdjFlag = isattendanceAdjFlag;
    }

    public long getStopSalaryDate() {
        return stopSalaryDate;
    }

    public void setStopSalaryDate(long stopSalaryDate) {
        this.stopSalaryDate = stopSalaryDate;
    }

    public String getStopSalary() {
        return stopSalary;
    }

    public void setStopSalary(String stopSalary) {
        this.stopSalary = stopSalary;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public boolean isOnDeputaion() {
        return onDeputaion;
    }

    public void setOnDeputaion(boolean onDeputaion) {
        this.onDeputaion = onDeputaion;
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

    public String getEmployeeCodeM() {
        return employeeCodeM;
    }

    public void setEmployeeCodeM(String employeeCodeM) {
        this.employeeCodeM = employeeCodeM;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
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

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(int sickLeave) {
        this.sickLeave = sickLeave;
    }

    public int getLeaveWithoutPay() {
        return leaveWithoutPay;
    }

    public void setLeaveWithoutPay(int leaveWithoutPay) {
        this.leaveWithoutPay = leaveWithoutPay;
    }

    public int getPbsP() {
        return pbsP;
    }

    public void setPbsP(int pbsP) {
        this.pbsP = pbsP;
    }

    public int getPasP() {
        return pasP;
    }

    public void setPasP(int pasP) {
        this.pasP = pasP;
    }

    public boolean getSd() {
        return sd;
    }

    public void setSd(boolean sd) {
        this.sd = sd;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getAttendaceStatus() {
        return attendaceStatus;
    }

    public void setAttendaceStatus(String attendaceStatus) {
        this.attendaceStatus = attendaceStatus;
    }

    public boolean isSalaryProcessed() {
        return salaryProcessed;
    }

    public void setSalaryProcessed(boolean salaryProcessed) {
        this.salaryProcessed = salaryProcessed;
    }

    public boolean isPayStopSalaryProcessed() {
        return payStopSalaryProcessed;
    }

    public void setPayStopSalaryProcessed(boolean payStopSalaryProcessed) {
        this.payStopSalaryProcessed = payStopSalaryProcessed;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getPfTypeName() {
        return pfTypeName;
    }

    public void setPfTypeName(String pfTypeName) {
        this.pfTypeName = pfTypeName;
    }

}
