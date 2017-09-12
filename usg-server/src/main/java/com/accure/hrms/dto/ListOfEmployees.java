/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

/**
 *
 * @author user
 */
public class ListOfEmployees  extends  Employee{
    
private String fromDOB;    
private String toDOB;
private String fromDOJ;
private String toDOJ;
private String fromDOR;
private String toDOR;

    public String getFromDOB() {
        return fromDOB;
    }

    public void setFromDOB(String fromDOB) {
        this.fromDOB = fromDOB;
    }

    public String getToDOB() {
        return toDOB;
    }

    public void setToDOB(String toDOB) {
        this.toDOB = toDOB;
    }

    public String getFromDOJ() {
        return fromDOJ;
    }

    public void setFromDOJ(String fromDOJ) {
        this.fromDOJ = fromDOJ;
    }

    public String getToDOJ() {
        return toDOJ;
    }

    public void setToDOJ(String toDOJ) {
        this.toDOJ = toDOJ;
    }

    public String getFromDOR() {
        return fromDOR;
    }

    public void setFromDOR(String fromDOR) {
        this.fromDOR = fromDOR;
    }

    public String getToDOR() {
        return toDOR;
    }

    public void setToDOR(String toDOR) {
        this.toDOR = toDOR;
    }

}
