/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.hrms.dto.SalaryHead;
import com.accure.usg.common.dto.Common;

/**
 *
 * @author Asif
 */
public class LeaveEncashment extends Common {

    private String employeeId;
    private String employeeCode;
    private String employeeCodeM;
    private String employeeName;
    private String ddo;
    private String ddoId;
    private String locationId;
    private String designation;
    private String department;
    private String location;
    private String natureType;
    private String postingCity;
    private String leftStatus;
    private String leaveType;
    private String leaveTypeDesc;
    private LeaveTypeMaster leaveTypeInfo;
    private String encashmentDate;
    private int month;
    private int year;
    private double leaveBalance;
    private double cashableLeaves;
    private double leaveToBeEncashed;
    private double totalAmount;
    private boolean isLocked = false;
    private String remarks;
    private String transactionId;
    private SalaryHead leInfo;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDdoId() {
        return ddoId;
    }

    public void setDdoId(String ddoId) {
        this.ddoId = ddoId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    //added on 13-12-2016
    private String leaveTypeName;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getPostingCity() {
        return postingCity;
    }

    public void setPostingCity(String postingCity) {
        this.postingCity = postingCity;
    }

    public String getLeftStatus() {
        return leftStatus;
    }

    public void setLeftStatus(String leftStatus) {
        this.leftStatus = leftStatus;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveTypeMaster getLeaveTypeInfo() {
        return leaveTypeInfo;
    }

    public void setLeaveTypeInfo(LeaveTypeMaster leaveTypeInfo) {
        this.leaveTypeInfo = leaveTypeInfo;
    }

    public String getEncashmentDate() {
        return encashmentDate;
    }

    public void setEncashmentDate(String encashmentDate) {
        this.encashmentDate = encashmentDate;
    }

    public double getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(double leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public double getCashableLeaves() {
        return cashableLeaves;
    }

    public void setCashableLeaves(double cashableLeaves) {
        this.cashableLeaves = cashableLeaves;
    }

    public double getLeaveToBeEncashed() {
        return leaveToBeEncashed;
    }

    public void setLeaveToBeEncashed(double leaveToBeEncashed) {
        this.leaveToBeEncashed = leaveToBeEncashed;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLeaveTypeDesc() {
        return leaveTypeDesc;
    }

    public void setLeaveTypeDesc(String leaveTypeDesc) {
        this.leaveTypeDesc = leaveTypeDesc;
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

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public SalaryHead getLeInfo() {
        return leInfo;
    }

    public void setLeInfo(SalaryHead leInfo) {
        this.leInfo = leInfo;
    }

}
