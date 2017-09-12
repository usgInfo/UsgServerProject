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
public class PensionFormula extends Common{

    private String pensionDescription;
    private String pensionFormula;
    private int pensionOrder;

    public String getPensionDescription() {
        return pensionDescription;
    }

    public void setPensionDescription(String pensionDescription) {
        this.pensionDescription = pensionDescription;
    }

    public String getPensionFormula() {
        return pensionFormula;
    }

    public void setPensionFormula(String pensionFormula) {
        this.pensionFormula = pensionFormula;
    }

    public int getPensionOrder() {
        return pensionOrder;
    }

    public void setPensionOrder(int pensionOrder) {
        this.pensionOrder = pensionOrder;
    }
    
    

}
