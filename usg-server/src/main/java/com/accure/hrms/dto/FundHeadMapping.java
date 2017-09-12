/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class FundHeadMapping extends Common {

    private String fundType;
    private String sector;
    private String headCode;

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

    public String getHeadCode() {
        return headCode;
    }

    public void setHeadCode(String headCode) {
        this.headCode = headCode;
    }

}
