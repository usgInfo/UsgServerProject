/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class PensionAssociation extends Common {

    private String pensionAssociationName;
    private double pensionFees;
    private String pensionRemarks;

    public String getPensionAssociationName() {
        return pensionAssociationName;
    }

    public void setPensionAssociationName(String pensionAssociationName) {
        this.pensionAssociationName = pensionAssociationName;
    }

    public String getPensionRemarks() {
        return pensionRemarks;
    }

    public void setPensionRemarks(String pensionRemarks) {
        this.pensionRemarks = pensionRemarks;
    }

    public double getPensionFees() {
        return pensionFees;
    }

    public void setPensionFees(double pensionFees) {
        this.pensionFees = pensionFees;
    }

}
