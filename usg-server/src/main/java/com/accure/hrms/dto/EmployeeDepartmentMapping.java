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
 * @author user
 */
public class EmployeeDepartmentMapping extends Common{
    
    private String employeeCode;
    private String employeeId;
    private String ddo;
    private String location;
    private List<String>department;
    private List<DepartmentDetails>departmentList;

    public List<DepartmentDetails> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentDetails> departmentList) {
        this.departmentList = departmentList;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }
    
}
