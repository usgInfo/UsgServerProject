/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.hrms.dto.Employee;
import java.util.List;

/**
 *
 * @author upendra
 */
public class PayStopedSalary extends Employee {

    private String nature;
    private String month;
    private String year;
    private List<Employee> salaryList;
    private String payMonth;
    private String payYear;
    private String headDescription;
    private String sortBy;

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<Employee> getSalaryList() {
        return salaryList;
    }

    public void setSalaryList(List<Employee> salaryList) {
        this.salaryList = salaryList;
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

    public void setPayYear(String payYear) {
        this.payYear = payYear;
    }

    public String getHeadDescription() {
        return headDescription;
    }

    public void setHeadDescription(String headDescription) {
        this.headDescription = headDescription;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
