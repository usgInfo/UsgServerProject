/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class EmployeeSuspension extends Common {

    private String employeeCode;
    private String employeeCodeM;
    private String employeeName;
    private String ddo;
    private String location;
    private String department;
    private String designation;
    private boolean isSuspended;
    private String dated;
    private String suspendDate;
    private String unSuspendDate;
    private String remarks;

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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSuspendDate() {
        return suspendDate;
    }

    public void setSuspendDate(String suspendDate) {
        this.suspendDate = suspendDate;
    }

    public String getUnSuspendDate() {
        return unSuspendDate;
    }

    public void setUnSuspendDate(String unSuspendDate) {
        this.unSuspendDate = unSuspendDate;
    }
}
