/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author user
 */
public class FinancialFundHeadMapping extends Common{
       private String fundType;
     private String sector;
     private List<String> headCodeList;

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public List<String> getHeadCodeList() {
        return headCodeList;
    }

    public void setHeadCodeList(List<String> headCodeList) {
        this.headCodeList = headCodeList;
    }
    
}
