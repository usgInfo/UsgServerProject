/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class AttendanceAdjListDto extends Common {
     private String empCodeM;
     private String empName;
     private String location;
     private String designation;
     private String tdays;
     private String present;
     private String sl;
     private String lwp;
     private String pdays;
     private String pbs;
     private String pas;
     private String sd;
     private String remarks;

    public String getEmpCodeM() {
        return empCodeM;
    }

    public void setEmpCodeM(String empCodeM) {
        this.empCodeM = empCodeM;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTdays() {
        return tdays;
    }

    public void setTdays(String tdays) {
        this.tdays = tdays;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getLwp() {
        return lwp;
    }

    public void setLwp(String lwp) {
        this.lwp = lwp;
    }

    public String getPdays() {
        return pdays;
    }

    public void setPdays(String pdays) {
        this.pdays = pdays;
    }

    public String getPbs() {
        return pbs;
    }

    public void setPbs(String pbs) {
        this.pbs = pbs;
    }

    public String getPas() {
        return pas;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
     
}
