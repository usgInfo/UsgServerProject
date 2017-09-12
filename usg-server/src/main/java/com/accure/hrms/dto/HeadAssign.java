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
public class HeadAssign extends Common {

    private String headName;
    private String emplist;
    private String empCode;
    private String empName;
    private String location;
    private String department;
    private String designation;
    private String ddo;
    private String headDesc;
    private String empType;

    public String getHeadDesc() {
        return headDesc;
    }

    public void setHeadDesc(String headDesc) {
        this.headDesc = headDesc;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getEmpCode() {
        return empCode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
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

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getEmplist() {
        return emplist;
    }

    public void setEmplist(String emplist) {
        this.emplist = emplist;
    }

    private String headAssignStatus;

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getHeadAssignStatus() {
        return headAssignStatus;
    }

    public void setHeadAssignStatus(String headAssignStatus) {
        this.headAssignStatus = headAssignStatus;
    }

}
