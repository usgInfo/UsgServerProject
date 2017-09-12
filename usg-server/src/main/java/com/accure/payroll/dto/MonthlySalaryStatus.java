/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.payroll.dto;

/**
 *
 * @author upendra
 */
public class MonthlySalaryStatus {
    private String ddo;
    private String empCount;
    private String totalAmount;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getEmpCount() {
        return empCount;
    }

    public void setEmpCount(String empCount) {
        this.empCount = empCount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    
}
