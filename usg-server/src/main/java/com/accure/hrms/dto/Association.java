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
public class Association extends Common {

    private String associationName;
    private String isPension;
    private int fees;
    private String remarks;

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public String getIsPension() {
        return isPension;
    }

    public void setIsPension(String isPension) {
        this.isPension = isPension;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
