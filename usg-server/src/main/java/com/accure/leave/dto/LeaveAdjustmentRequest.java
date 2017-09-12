/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author User
 */
public class LeaveAdjustmentRequest extends Common{

    private String reportingTo;
    private String leaveRequest;
    private String remarks;
    private String leaveType;
    private String totalLeave;
    private String availedLeave;
    private String adjustedLeave;
    private String balanceLeave;
    private String appliedLeave;
    private String appliedBalanceLeave;
    private String adjustedRequestedDate;
    private String fromDate;
    private String toDate;
    private String totalLeaveDays;
    private String leaveStatus;
    private String employeeName;
    
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public String getReportingTo() {
        return reportingTo;
    }

    public void setReportingTo(String reportingTo) {
        this.reportingTo = reportingTo;
    }
    public String getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(String leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }
    public String getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(String totalLeave) {
        this.totalLeave = totalLeave;
    }
    public String getAvailedLeave() {
        return availedLeave;
    }

    public void setAvailedLeave(String availedLeave) {
        this.availedLeave = availedLeave;
    }
    public String getAdjustedLeave() {
        return adjustedLeave;
    }

    public void setAdjustedLeave(String adjustedLeave) {
        this.adjustedLeave = adjustedLeave;
    }
    public String getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(String balanceLeave) {
        this.balanceLeave = balanceLeave;
    }
    public String getAppliedLeave() {
        return appliedLeave;
    }

    public void setAppliedLeave(String appliedLeave) {
        this.appliedLeave = appliedLeave;
    }
    public String getAppliedBalanceLeave() {
        return appliedBalanceLeave;
    }

    public void setAppliedBalanceLeave(String appliedBalanceLeave) {
        this.appliedBalanceLeave = appliedBalanceLeave;
    }
    public String getAdjustedRequestedDate() {
        return adjustedRequestedDate;
    }

    public void setAdjustedRequestedDate(String adjustedRequestedDate) {
        this.adjustedRequestedDate = adjustedRequestedDate;
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
    public String getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(String totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }
    public String getleaveStatus() {
        return leaveStatus;
    }

    public void setleaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
    
}
