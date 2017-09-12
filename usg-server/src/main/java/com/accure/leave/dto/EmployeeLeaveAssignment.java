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
public class EmployeeLeaveAssignment extends Common {

    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String employeeCodeM;
    private String ddo;
    private String designation;
    private String natureType;
    private String employeeCategory;
    private String leaveType;
    private String year;
    private String instalment;
    private String noOfDays;
    private String currentYearLeaves;
    private String totalEarnedLeaves;
    //Filed is used in Leave transaction Page
    private String leaveTypeName;
    private String ddoId;
    private String designationId;
    private String natureTypeId;
    private String rowId;

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getInstalment() {
        return instalment;
    }

    public void setInstalment(String instalment) {
        this.instalment = instalment;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
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

    public String getCurrentYearLeaves() {
        return currentYearLeaves;
    }

    public void setCurrentYearLeaves(String currentYearLeaves) {
        this.currentYearLeaves = currentYearLeaves;
    }

    public String getTotalEarnedLeaves() {
        return totalEarnedLeaves;
    }

    public void setTotalEarnedLeaves(String totalEarnedLeaves) {
        this.totalEarnedLeaves = totalEarnedLeaves;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getDdoId() {
        return ddoId;
    }

    public void setDdoId(String ddoId) {
        this.ddoId = ddoId;
    }

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getNatureTypeId() {
        return natureTypeId;
    }

    public void setNatureTypeId(String natureTypeId) {
        this.natureTypeId = natureTypeId;
    }

}
