/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author upendra
 */
public class EmployeeNominee extends Common {
     private String employeeCode;
     private String ddo;
     private String designation;
     private String department;
     private String employeeName;
     private String remarks;
    private List<Nominee> nomineeList;

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
    
    
    public List<Nominee> getNomineeList() {
        return nomineeList;
    }

    public void setNomineeList(List<Nominee> nomineeList) {
        this.nomineeList = nomineeList;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    
    
}
