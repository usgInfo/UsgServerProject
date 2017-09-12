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
public class Bank extends Common {

    private String bankname;
    private String branchname;
    private String city;
    private String state;
    private String ifsccode;
    private String micrcode;
    private String primarycontactname;
    private String secondarycontactname;
    private String primarycontactno;
    private String secondarycontactno;
    private String acnumber;
    private String address;
    private String remarks;

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIfsccode() {
        return ifsccode;
    }

    public void setIfsccode(String ifsccode) {
        this.ifsccode = ifsccode;
    }

    public String getMicrcode() {
        return micrcode;
    }

    public void setMicrcode(String micrcode) {
        this.micrcode = micrcode;
    }

    public String getPrimarycontactname() {
        return primarycontactname;
    }

    public void setPrimarycontactname(String primarycontactname) {
        this.primarycontactname = primarycontactname;
    }

    public String getSecondarycontactname() {
        return secondarycontactname;
    }

    public void setSecondarycontactname(String secondarycontactname) {
        this.secondarycontactname = secondarycontactname;
    }

    public String getPrimarycontactno() {
        return primarycontactno;
    }

    public void setPrimarycontactno(String primarycontactno) {
        this.primarycontactno = primarycontactno;
    }

    public String getSecondarycontactno() {
        return secondarycontactno;
    }

    public void setSecondarycontactno(String secondarycontactno) {
        this.secondarycontactno = secondarycontactno;
    }

    public String getAcnumber() {
        return acnumber;
    }

    public void setAcnumber(String acnumber) {
        this.acnumber = acnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
