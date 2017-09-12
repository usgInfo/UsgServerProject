/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;



/**
 *
 * @author user
 */
public class FundCategory extends Common{
    private String fundCategory;
    private String remarks;

    public String getFundCategory() {
        return fundCategory;
    }

    public void setFundCategory(String fundCategory) {
        this.fundCategory = fundCategory;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
    
}
