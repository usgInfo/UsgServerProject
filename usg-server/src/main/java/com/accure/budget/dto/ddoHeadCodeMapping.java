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
public class ddoHeadCodeMapping extends Common {

    private String ddo;
    private String sector;
    private String sectorId;
    private String budgetHead;
    private String budgetHeadDescription;
    private String fundType;
    private String fundTypeId;
    private List<String> headCode;
    private List<String> headCodeName;

    public String getBudgetHead() {
        return budgetHead;
    }

    public List<String> getHeadCodeName() {
        return headCodeName;
    }

    public void setHeadCodeName(List<String> headCodeName) {
        this.headCodeName = headCodeName;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getBudgetHeadDescription() {
        return budgetHeadDescription;
    }

    public void setBudgetHeadDescription(String budgetHeadDescription) {
        this.budgetHeadDescription = budgetHeadDescription;
    }

    public List<String> getHeadCode() {
        return headCode;
    }

    public void setHeadCode(List<String> headCode) {
        this.headCode = headCode;
    }

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    public String getFundTypeId() {
        return fundTypeId;
    }

    public void setFundTypeId(String fundTypeId) {
        this.fundTypeId = fundTypeId;
    }

}
