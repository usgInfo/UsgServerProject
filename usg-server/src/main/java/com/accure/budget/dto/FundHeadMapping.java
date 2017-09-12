/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author user
 */
public class FundHeadMapping extends Common{
    private String sector;
    private String fundType;
     private List<String> headCode;

    public List<String> getHeadCode() {
        return headCode;
    }

    public void setHeadCode(List<String> headCode) {
        this.headCode = headCode;
    }

   
    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }
    
}
