/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.dto;

import com.accure.usg.common.dto.Common;
import com.accure.usg.common.dto.OrgRole;
import java.util.List;

/**
 *
 * @author deepak2310
 */
public class User extends Common {

    private String fname;
    private String mname;
    private String lname;
    private String gender;
    private String maritialstatus;
    private String dob;
    private String mobile;
    private String email;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String loginid;
    private String password;
    private String ddoId;
    private List<OrgRole> orgRole;
    private long createdDate;
    private long updatedDate;
    private String employeeId;
    //Only for Ui
    private List<String> locationNames;

//    for Display purpose
    private String ddoName;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritialstatus() {
        return maritialstatus;
    }

    public void setMaritialstatus(String maritialstatus) {
        this.maritialstatus = maritialstatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<OrgRole> getOrgRole() {
        return orgRole;
    }

    public void setOrgRole(List<OrgRole> orgRole) {
        this.orgRole = orgRole;
    }

    public String getDdoId() {
        return ddoId;
    }

    public void setDdoId(String ddoId) {
        this.ddoId = ddoId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public List<String> getLocationNames() {
        return locationNames;
    }

    public void setLocationNames(List<String> locationNames) {
        this.locationNames = locationNames;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

}
