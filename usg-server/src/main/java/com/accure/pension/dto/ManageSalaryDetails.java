/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

//import com.accure.usg.server.utils.Common;

import com.accure.usg.common.dto.Common;


/**
 *
 * @author user
 */
public class ManageSalaryDetails extends Common{
    
    private String empName;
    private String empCode;
    private String month;
    private String year;
    private String basic;
    private String da;
    private String gp;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
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

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }
  
    
}
