/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Asif
 * Fund Category Master comes under Financial Accounting .. 
 */
public class FundCategory extends Common{
   private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
   
    
}
