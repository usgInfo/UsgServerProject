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
public class TransfeJoiningForm extends Common{

    private String sno;
    private String empCode;
    private String empName;
    private String fromDDO;
    private String designation;
    private String workDetails;
    private String transferDate;
    private String hasJoined;
    private String date;
    private String remarks;

    public String getHasJoined() {
        return hasJoined;
    }

    public void setHasJoined(String hasJoined) {
        this.hasJoined = hasJoined;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getEmpCode() {
        return empCode;
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

    public String getFromDDO() {
        return fromDDO;
    }

    public void setFromDDO(String fromDDO) {
        this.fromDDO = fromDDO;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorkDetails() {
        return workDetails;
    }

    public void setWorkDetails(String workDetails) {
        this.workDetails = workDetails;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
   

}
