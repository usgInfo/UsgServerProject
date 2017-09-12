/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class EmployeePriviousJobDetails extends Common {
    private String employeeDDO;
    private String employeeCode;
    private String employeeName;
    private String department;
    private String previousDepartment;
    private String designation;
    private String previousDesignation;
    private String previousLocation;
    private String priviousGrade;
    private int basicPay;
    private int employeeDP;
    private int employeeDA;
    private String fromDate;
    private String toDate;
    private String station;
    private boolean inService;
    private String reasonForLeaving;

    public String getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(String previousLocation) {
        this.previousLocation = previousLocation;
    }

    
    public String getEmployeeDDO() {
        return employeeDDO;
    }

    public void setEmployeeDDO(String employeeDDO) {
        this.employeeDDO = employeeDDO;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPreviousDepartment() {
        return previousDepartment;
    }

    public void setPreviousDepartment(String previousDepartment) {
        this.previousDepartment = previousDepartment;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPreviousDesignation() {
        return previousDesignation;
    }

    public void setPreviousDesignation(String previousDesignation) {
        this.previousDesignation = previousDesignation;
    }

    public String getPriviousGrade() {
        return priviousGrade;
    }

    public void setPriviousGrade(String priviousGrade) {
        this.priviousGrade = priviousGrade;
    }

    public int getBasicPay() {
        return basicPay;
    }

    public void setBasicPay(int basicPay) {
        this.basicPay = basicPay;
    }

    public int getEmployeeDP() {
        return employeeDP;
    }

    public void setEmployeeDP(int employeeDP) {
        this.employeeDP = employeeDP;
    }

    public int getEmployeeDA() {
        return employeeDA;
    }

    public void setEmployeeDA(int employeeDA) {
        this.employeeDA = employeeDA;
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

    public String getStation() {
        return station;
    }

    public void setStaringtion(String station) {
        this.station = station;
    }

    public boolean getInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    public String getReasonForLeaving() {
        return reasonForLeaving;
    }

    public void setReasonForLeaving(String reasonForLeaving) {
        this.reasonForLeaving = reasonForLeaving;
    }
  
}
