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
public class PensionClassification extends Common{
    private String pensionClassification;

    public String getPensionClassification() {
        return pensionClassification;
    }

    public void setPensionClassification(String pensionClassification) {
        this.pensionClassification = pensionClassification;
    }
    
}
