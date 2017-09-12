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
public class FundType extends Common{
    private String description;
    private String underFundType;
    private String order;
    
//    for display purpose
   private String fundTypeName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnderFundType() {
        return underFundType;
    }

    public void setUnderFundType(String underFundType) {
        this.underFundType = underFundType;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFundTypeName() {
        return fundTypeName;
    }

    public void setFundTypeName(String fundTypeName) {
        this.fundTypeName = fundTypeName;
    }
    
}
