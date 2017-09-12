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
public class PensionHead extends Common{
    private String pensionHeadName;
    private String pensionShortName;
    private String pensionheadType;
    private String fixedCode;
    private int pensionAmount;
    private String headFormula;
    private int displayHeadOrder;
    private int orderLevel;
    private String headRemarks;
    private String partOfGross;

    public String getPensionHeadName() {
        return pensionHeadName;
    }

    public void setPensionHeadName(String pensionHeadName) {
        this.pensionHeadName = pensionHeadName;
    }

    public String getPensionShortName() {
        return pensionShortName;
    }

    public void setPensionShortName(String pensionShortName) {
        this.pensionShortName = pensionShortName;
    }

    public String getPensionheadType() {
        return pensionheadType;
    }

    public void setPensionheadType(String pensionheadType) {
        this.pensionheadType = pensionheadType;
    }

    public String getFixedCode() {
        return fixedCode;
    }

    public void setFixedCode(String fixedCode) {
        this.fixedCode = fixedCode;
    }

    public int getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(int pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public String getHeadFormula() {
        return headFormula;
    }

    public void setHeadFormula(String headFormula) {
        this.headFormula = headFormula;
    }

    public int getDisplayHeadOrder() {
        return displayHeadOrder;
    }

    public void setDisplayHeadOrder(int displayHeadOrder) {
        this.displayHeadOrder = displayHeadOrder;
    }

    public int getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(int orderLevel) {
        this.orderLevel = orderLevel;
    }

    public String getHeadRemarks() {
        return headRemarks;
    }

    public void setHeadRemarks(String headRemarks) {
        this.headRemarks = headRemarks;
    }

    public String getPartOfGross() {
        return partOfGross;
    }

    public void setPartOfGross(String partOfGross) {
        this.partOfGross = partOfGross;
    }
    
    
}
