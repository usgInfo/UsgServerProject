/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class LeaveAdjustment extends Common {

    private String location;
    private String ddo;
    private String employeeCode;
    private String employeeName;
    private String natureType;
    private String leaveType;
    private String department;
    private String designation;
    private String fromDate;
    private String toDate;
    private Long fromDateInMilliSecond;
    private Long toDateInMilliSecond;
    private String totalAppliedLeaves;
    private String totalBalanceLeaves;
    private String requestDate;
    private Long requestDateInMillisecond;
    private String loanTransactionRowId;
    private String totalCancelledLeaves;
    private String remarks;
    
    private String natureTypeName;
    private String leaveTypeDescription;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Long getFromDateInMilliSecond() {
        return fromDateInMilliSecond;
    }

    public void setFromDateInMilliSecond(Long fromDateInMilliSecond) {
        this.fromDateInMilliSecond = fromDateInMilliSecond;
    }

    public Long getToDateInMilliSecond() {
        return toDateInMilliSecond;
    }

    public void setToDateInMilliSecond(Long toDateInMilliSecond) {
        this.toDateInMilliSecond = toDateInMilliSecond;
    }

    public String getTotalAppliedLeaves() {
        return totalAppliedLeaves;
    }

    public void setTotalAppliedLeaves(String totalAppliedLeaves) {
        this.totalAppliedLeaves = totalAppliedLeaves;
    }

    public String getTotalBalanceLeaves() {
        return totalBalanceLeaves;
    }

    public void setTotalBalanceLeaves(String totalBalanceLeaves) {
        this.totalBalanceLeaves = totalBalanceLeaves;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public Long getRequestDateInMillisecond() {
        return requestDateInMillisecond;
    }

    public void setRequestDateInMillisecond(Long requestDateInMillisecond) {
        this.requestDateInMillisecond = requestDateInMillisecond;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getNatureTypeName() {
        return natureTypeName;
    }

    public void setNatureTypeName(String natureTypeName) {
        this.natureTypeName = natureTypeName;
    }

    public String getLeaveTypeDescription() {
        return leaveTypeDescription;
    }

    public void setLeaveTypeDescription(String leaveTypeDescription) {
        this.leaveTypeDescription = leaveTypeDescription;
    }

    public String getLoanTransactionRowId() {
        return loanTransactionRowId;
    }

    public void setLoanTransactionRowId(String loanTransactionRowId) {
        this.loanTransactionRowId = loanTransactionRowId;
    }

    public String getTotalCancelledLeaves() {
        return totalCancelledLeaves;
    }

    public void setTotalCancelledLeaves(String totalCancelledLeaves) {
        this.totalCancelledLeaves = totalCancelledLeaves;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
