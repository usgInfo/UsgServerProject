/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asif
 */
public class LeaveTransaction extends Common {

    private String employeeId;
    private String employeeCode;
    private String employeeCodeM;
    private String employeeName;
    private String ddo;
    private String designation;
    private String department;
    private String location;
    private String locationName;
    private String natureType;
    private String postingCity;
    private String leaveType;
    private String leaveTypeDescription;
    private String eligibilityLeaves;
    private String earnedLeaves;
    private String totalAppliedLeaves;
    private String totalBalanceLeaves;
    private String fromDate;
    private String toDate;
    private Long fromDateInMilliSecond;
    private Long toDateInMilliSecond;
    private String contactNumber;
    private String reasonForLeave;
    private String totalLeaveDays;
    private String requestingDate;
    private Long requestingDateInMillisecond;
    private int noOfLeaveWithoutPayDays;
    private ArrayList<String> leaveWithoutPayDays;
    private List<Map<String, String>> dateRemarksAndIsHalfDay;
    private String financialYear;
    private String rowId;
    private String lwpId;
    private String dataFrom;
    
    private String leaveTypeName;
    private String natureTypeName;
    private String designationName;
    private Double lwpHalfDays;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(String dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
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

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getEligibilityLeaves() {
        return eligibilityLeaves;
    }

    public void setEligibilityLeaves(String eligibilityLeaves) {
        this.eligibilityLeaves = eligibilityLeaves;
    }

    public String getEarnedLeaves() {
        return earnedLeaves;
    }

    public void setEarnedLeaves(String earnedLeaves) {
        this.earnedLeaves = earnedLeaves;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getReasonForLeave() {
        return reasonForLeave;
    }

    public void setReasonForLeave(String reasonForLeave) {
        this.reasonForLeave = reasonForLeave;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRequestingDate() {
        return requestingDate;
    }

    public void setRequestingDate(String requestingDate) {
        this.requestingDate = requestingDate;
    }

    public String getEmployeeCodeM() {
        return employeeCodeM;
    }

    public void setEmployeeCodeM(String employeeCodeM) {
        this.employeeCodeM = employeeCodeM;
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

    public Long getRequestingDateInMillisecond() {
        return requestingDateInMillisecond;
    }

    public void setRequestingDateInMillisecond(Long requestingDateInMillisecond) {
        this.requestingDateInMillisecond = requestingDateInMillisecond;
    }

    public String getLeaveTypeDescription() {
        return leaveTypeDescription;
    }

    public void setLeaveTypeDescription(String leaveTypeDescription) {
        this.leaveTypeDescription = leaveTypeDescription;
    }

    public String getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(String totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public int getNoOfLeaveWithoutPayDays() {
        return noOfLeaveWithoutPayDays;
    }

    public void setNoOfLeaveWithoutPayDays(int noOfLeaveWithoutPayDays) {
        this.noOfLeaveWithoutPayDays = noOfLeaveWithoutPayDays;
    }

    public ArrayList<String> getLeaveWithoutPayDays() {
        return leaveWithoutPayDays;
    }

    public void setLeaveWithoutPayDays(ArrayList<String> leaveWithoutPayDays) {
        this.leaveWithoutPayDays = leaveWithoutPayDays;
    }

    public List<Map<String, String>> getDateRemarksAndIsHalfDay() {
        return dateRemarksAndIsHalfDay;
    }

    public void setDateRemarksAndIsHalfDay(List<Map<String, String>> dateRemarksAndIsHalfDay) {
        this.dateRemarksAndIsHalfDay = dateRemarksAndIsHalfDay;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getLwpId() {
        return lwpId;
    }

    public void setLwpId(String lwpId) {
        this.lwpId = lwpId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getNatureTypeName() {
        return natureTypeName;
    }

    public void setNatureTypeName(String natureTypeName) {
        this.natureTypeName = natureTypeName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public Double getLwpHalfDays() {
        return lwpHalfDays;
    }

    public void setLwpHalfDays(Double lwpHalfDays) {
        this.lwpHalfDays = lwpHalfDays;
    }

}
