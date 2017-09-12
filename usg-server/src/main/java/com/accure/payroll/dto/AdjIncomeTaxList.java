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
public class AdjIncomeTaxList extends Common {
     private String employeeCodeM;
     private String employeeName;
     private String location;
     private String department;
     private String it;
     private String surCharge;
     private String cessCharge;
     private String itStatus;

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

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getSurCharge() {
        return surCharge;
    }

    public void setSurCharge(String surCharge) {
        this.surCharge = surCharge;
    }

    public String getCessCharge() {
        return cessCharge;
    }

    public void setCessCharge(String cessCharge) {
        this.cessCharge = cessCharge;
    }

    public String getItStatus() {
        return itStatus;
    }

    public void setItStatus(String itStatus) {
        this.itStatus = itStatus;
    }
}
