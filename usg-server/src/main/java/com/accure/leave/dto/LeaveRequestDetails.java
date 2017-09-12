/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

/**
 *
 * @author Asif
 */
public class LeaveRequestDetails {

    private String employeeId;
    private String employeeCode;
    private String employeeName;
    private String leaveTypeId;
    private String leavetypeName;
    private String maxLeavePerYear;
    private String totalAppliedLeaves;
    private String totalApprovedLeaves;
    private String balanceLeaves;
    private String adjustedLeavesRequested;
    private String adjustedLeavesApproved;
    private String appliedBalanceLeaves;

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeavetypeName() {
        return leavetypeName;
    }

    public void setLeavetypeName(String leavetypeName) {
        this.leavetypeName = leavetypeName;
    }

    public String getMaxLeavePerYear() {
        return maxLeavePerYear;
    }

    public void setMaxLeavePerYear(String maxLeavePerYear) {
        this.maxLeavePerYear = maxLeavePerYear;
    }

    public String getTotalAppliedLeaves() {
        return totalAppliedLeaves;
    }

    public void setTotalAppliedLeaves(String totalAppliedLeaves) {
        this.totalAppliedLeaves = totalAppliedLeaves;
    }

    public String getTotalApprovedLeaves() {
        return totalApprovedLeaves;
    }

    public void setTotalApprovedLeaves(String totalApprovedLeaves) {
        this.totalApprovedLeaves = totalApprovedLeaves;
    }

    public String getBalanceLeaves() {
        return balanceLeaves;
    }

    public void setBalanceLeaves(String balanceLeaves) {
        this.balanceLeaves = balanceLeaves;
    }

   
    public String getAppliedBalanceLeaves() {
        return appliedBalanceLeaves;
    }

    public void setAppliedBalanceLeaves(String appliedBalanceLeaves) {
        this.appliedBalanceLeaves = appliedBalanceLeaves;
    }

    public String getAdjustedLeavesRequested() {
        return adjustedLeavesRequested;
    }

    public void setAdjustedLeavesRequested(String adjustedLeavesRequested) {
        this.adjustedLeavesRequested = adjustedLeavesRequested;
    }

    public String getAdjustedLeavesApproved() {
        return adjustedLeavesApproved;
    }

    public void setAdjustedLeavesApproved(String adjustedLeavesApproved) {
        this.adjustedLeavesApproved = adjustedLeavesApproved;
    }
    

}
