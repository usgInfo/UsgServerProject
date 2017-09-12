/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;


/**
 *
 * @author accure
 */
public class PensionArrear extends Common{
    
    private String employeeCode;
    private String manualEmployeeCode;
    private String employeeName;
    private String department;
    private String designation;
    private String payMonth;
    private String payYear;
    private String pensionOrderNumber;
    private String billNumber;
    private String billDate;
    private String fromDate;
    private String toDate;
    private String payMode;
    private String remarks;
    private String isPensionArrear;
    private List<PensionEmployee> pensionArrearList;
    
    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }
    public String getManualEmployeeCode() {
        return manualEmployeeCode;
    }

    public void setManualEmployeeCode(String manualEmployeeCode) {
        this.manualEmployeeCode = manualEmployeeCode;
    }
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
    public String getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(String payMonth) {
        this.payMonth = payMonth;
    }

    public String getPayYear() {
        return payYear;
    }
}

