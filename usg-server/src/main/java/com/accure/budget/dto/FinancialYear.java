/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class FinancialYear extends Common{
    private String year;
    private String fromDate;
    private String toDate;
    private String active;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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
    
    
}
