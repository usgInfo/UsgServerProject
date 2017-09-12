/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Asif
 */
public class SalaryHead extends Common {

    private String description;
    private String InterestCalculated;
    private String shortDescription;
    private String interestPercentage;
    private String headType;
    private String parentHead;
    private String deductionType;
    private String rounding;
    private String mapping;
    private String effectType;
    private String formula;
    private String isBasic;
    private String amount;
    private String partOfGross;
    private int displayLevel;
    private String presentDependent;
    private int orderLevel;
    private String halfOnSuspended;
    private String taxable;
    private String showOnRegister;
    private String partiallyTaxableLimit;
    private String showOnSalarySlip;
    private String fixedHead;
    private String showOnArrearReport;
    private String chapterType;
    private String calculateOnIncrement;
    private String sectionPart;
    private String sectionId;
    private String blockSummation;
    private String active;
    private String remarks;
    private String forNominee;
    private double calculatedAmount;
    private String isRefundable;
    //private String useInPension;
    //private String importDesc;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShowOnSalarySlip() {
        return showOnSalarySlip;
    }

    public void setShowOnSalarySlip(String showOnSalarySlip) {
        this.showOnSalarySlip = showOnSalarySlip;
    }

    public String getFixedHead() {
        return fixedHead;
    }

    public void setFixedHead(String fixedHead) {
        this.fixedHead = fixedHead;
    }

    public String getInterestCalculated() {
        return InterestCalculated;
    }

    public void setInterestCalculated(String InterestCalculated) {
        this.InterestCalculated = InterestCalculated;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(String interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public String getHeadType() {
        return headType;
    }

    public void setHeadType(String headType) {
        this.headType = headType;
    }

    public String getParentHead() {
        return parentHead;
    }

    public void setParentHead(String parentHead) {
        this.parentHead = parentHead;
    }

    public String getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(String deductionType) {
        this.deductionType = deductionType;
    }

    public String getRounding() {
        return rounding;
    }

    public void setRounding(String rounding) {
        this.rounding = rounding;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getIsBasic() {
        return isBasic;
    }

    public void setIsBasic(String isBasic) {
        this.isBasic = isBasic;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPartOfGross() {
        return partOfGross;
    }

    public void setPartOfGross(String partOfGross) {
        this.partOfGross = partOfGross;
    }

    public int getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(int displayLevel) {
        this.displayLevel = displayLevel;
    }

    public String getPresentDependent() {
        return presentDependent;
    }

    public void setPresentDependent(String presentDependent) {
        this.presentDependent = presentDependent;
    }

    public int getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(int orderLevel) {
        this.orderLevel = orderLevel;
    }

    public String getHalfOnSuspended() {
        return halfOnSuspended;
    }

    public void setHalfOnSuspended(String halfOnSuspended) {
        this.halfOnSuspended = halfOnSuspended;
    }

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }

    public String getShowOnRegister() {
        return showOnRegister;
    }

    public void setShowOnRegister(String showOnRegister) {
        this.showOnRegister = showOnRegister;
    }

    public String getPartiallyTaxableLimit() {
        return partiallyTaxableLimit;
    }

    public void setPartiallyTaxableLimit(String partiallyTaxableLimit) {
        this.partiallyTaxableLimit = partiallyTaxableLimit;
    }

    public String getShowOnArrearReport() {
        return showOnArrearReport;
    }

    public void setShowOnArrearReport(String showOnArrearReport) {
        this.showOnArrearReport = showOnArrearReport;
    }

    public String getChapterType() {
        return chapterType;
    }

    public void setChapterType(String chapterType) {
        this.chapterType = chapterType;
    }

    public String getCalculateOnIncrement() {
        return calculateOnIncrement;
    }

    public void setCalculateOnIncrement(String calculateOnIncrement) {
        this.calculateOnIncrement = calculateOnIncrement;
    }

    public String getSectionPart() {
        return sectionPart;
    }

    public void setSectionPart(String sectionPart) {
        this.sectionPart = sectionPart;
    }

//    public String getUseInPension() {
//        return useInPension;
//    }
//
//    public void setUseInPension(String useInPension) {
//        this.useInPension = useInPension;
//    }
//
//    public String getImportDesc() {
//        return importDesc;
//    }
//
//    public void setImportDesc(String importDesc) {
//        this.importDesc = importDesc;
//    }
    public String getBlockSummation() {
        return blockSummation;
    }

    public void setBlockSummation(String blockSummation) {
        this.blockSummation = blockSummation;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getForNominee() {
        return forNominee;
    }

    public void setForNominee(String forNominee) {
        this.forNominee = forNominee;
    }

    public double getCalculatedAmount() {
        return calculatedAmount;
    }

    public void setCalculatedAmount(double calculatedAmount) {
        this.calculatedAmount = calculatedAmount;
    }

    public String getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(String isRefundable) {
        this.isRefundable = isRefundable;
    }

}
