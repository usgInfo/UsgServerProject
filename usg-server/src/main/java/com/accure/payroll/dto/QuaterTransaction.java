/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class QuaterTransaction extends Common{
    
    private String ddo;
    private String city;
    private String quaterNumber ;
    private String quaterCategory;
    private String employeeName;
    private String employeeCode;
    private String designation;
    private String department;
    private String allowcationDate;
    private String leaveDate;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getQuaterNumber() {
        return quaterNumber;
    }

    public void setQuaterNumber(String quaterNumber) {
        this.quaterNumber = quaterNumber;
    }

    public String getQuaterCategory() {
        return quaterCategory;
    }

    public void setQuaterCategory(String quaterCategory) {
        this.quaterCategory = quaterCategory;
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

    public String getAllowcationDate() {
        return allowcationDate;
    }

    public void setAllowcationDate(String allowcationDate) {
        this.allowcationDate = allowcationDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }
    
     
    
}
