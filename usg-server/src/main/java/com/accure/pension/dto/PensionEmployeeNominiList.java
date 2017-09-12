/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

/**
 *
 * @author user
 */
public class PensionEmployeeNominiList {
    
    private String memberName;
    private  String DateOfBirth;
    private String remarks;
    private String relation;
    private String nonominee;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getNonominee() {
        return nonominee;
    }

    public void setNonominee(String nonominee) {
        this.nonominee = nonominee;
    }
    
    
}
