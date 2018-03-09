/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author KC
 */
public class SpecialClaimMaster extends Common{

    private String specialClaimName;
    
    public String getSpecialClaimName() {
        return specialClaimName;
    }

    public void setSpecialClaimName(String specialClaimName) {
        this.specialClaimName = specialClaimName;
    }
    
    
}
