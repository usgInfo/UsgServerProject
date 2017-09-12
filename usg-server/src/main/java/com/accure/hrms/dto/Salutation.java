/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class Salutation extends Common {
    
    
    private String salutation;
    private String salutationRemarks;

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getSalutationRemarks() {
        return salutationRemarks;
    }

    public void setSalutationRemarks(String salutationRemarks) {
        this.salutationRemarks = salutationRemarks;
    }
    
    
}
