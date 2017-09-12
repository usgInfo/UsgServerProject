/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class EarningHeadsDetails extends Common {

    private String description;
    private String shortDescription;
    private String mapping;
    private double amount;
    private String effectiveFromDate;
    private SalaryHead descriptionInfo;
    private String salaryHeadId;
    private String isActive;
    private String isBasic;
    private int displayLevel;
    private String headName;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    
    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

   

    public String getEffectiveFromDate() {
        return effectiveFromDate;
    }

    public void setEffectiveFromDate(String effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }

    public SalaryHead getDescriptionInfo() {
        return descriptionInfo;
    }

    public void setDescriptionInfo(SalaryHead descriptionInfo) {
        this.descriptionInfo = descriptionInfo;
    }

    public String getSalaryHeadId() {
        return salaryHeadId;
    }

    public void setSalaryHeadId(String salaryHeadId) {
        this.salaryHeadId = salaryHeadId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsBasic() {
        return isBasic;
    }

    public void setIsBasic(String isBasic) {
        this.isBasic = isBasic;
    }

    public int getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(int displayLevel) {
        this.displayLevel = displayLevel;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
