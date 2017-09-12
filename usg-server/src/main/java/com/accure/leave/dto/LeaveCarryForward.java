/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Asif
 */
public class LeaveCarryForward extends Common {

    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String employeeCodeM;
    private String ddo;
    private String designation;
    private String department;
    private String natureType;
    private String employeeCategory;
    private String leaveType;
    private String fromYear;
    private String toYear;
    private String currentYearLeaves;
    private String leaveAvailed;
    private String totalEarnedLeaves;
    private String postingCity;
    private String location;
    private double carryForwardingLeaves;
    //Used only for ui purpose not for save
    private String designationName;
    private String idString;
    private String assignmentId;
    private String carryForwardDone;//this is used for keeping the status , weather carryForward done before assignment or after 
    private String transactionId;//while carry forward , simulatniously inserting document in transction to do transaction of for that employee to track balance leaves,  if the carry forward get unprocessed this ID is helpfull to delete record 

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

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(String employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getCurrentYearLeaves() {
        return currentYearLeaves;
    }

    public void setCurrentYearLeaves(String currentYearLeaves) {
        this.currentYearLeaves = currentYearLeaves;
    }

    public String getLeaveAvailed() {
        return leaveAvailed;
    }

    public void setLeaveAvailed(String leaveAvailed) {
        this.leaveAvailed = leaveAvailed;
    }

    public String getTotalEarnedLeaves() {
        return totalEarnedLeaves;
    }

    public void setTotalEarnedLeaves(String totalEarnedLeaves) {
        this.totalEarnedLeaves = totalEarnedLeaves;
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

    public double getCarryForwardingLeaves() {
        return carryForwardingLeaves;
    }

    public void setCarryForwardingLeaves(double carryForwardingLeaves) {
        this.carryForwardingLeaves = carryForwardingLeaves;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getCarryForwardDone() {
        return carryForwardDone;
    }

    public void setCarryForwardDone(String carryForwardDone) {
        this.carryForwardDone = carryForwardDone;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}