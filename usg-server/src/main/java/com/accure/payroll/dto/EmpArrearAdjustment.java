/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.hrms.dto.Employee;

/**
 *
 * @author upendra
 */
public class EmpArrearAdjustment extends Employee {

    private String financialYear;
    private String fromMonth;
    private String toMonth;
    private String fromYear;
    private String toYear;
    private String openingBal;
    private String arrearDate;

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getOpeningBal() {
        return openingBal;
    }

    public void setOpeningBal(String openingBal) {
        this.openingBal = openingBal;
    }

    public String getArrearDate() {
        return arrearDate;
    }

    public void setArrearDate(String arrearDate) {
        this.arrearDate = arrearDate;
    }

}
