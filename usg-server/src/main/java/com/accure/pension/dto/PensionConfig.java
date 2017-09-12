/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Shwetha T S
 */
public class PensionConfig extends Common{
    private double maxPurYear;
    private double maxGratuity;
    private double maxComAmt;
    private double durOfCompYear;
    private double CompPer;
    private double CompFac;
    private double detGraDiv;
    private double minPensionAmountForUgc;
    private double minPensionAmountForNonUgc ;
    private double pensionInJob;
    private double pensionAfterPer;
    private double pensionUptoPer;
    private double pensionAfterJob;
    private double retGraDiv;

    public double getMaxPurYear() {
        return maxPurYear;
    }

    public void setMaxPurYear(double maxPurYear) {
        this.maxPurYear = maxPurYear;
    }

    public double getMaxGratuity() {
        return maxGratuity;
    }

    public void setMaxGratuity(double maxGratuity) {
        this.maxGratuity = maxGratuity;
    }

    public double getMaxComAmt() {
        return maxComAmt;
    }

    public void setMaxComAmt(double maxComAmt) {
        this.maxComAmt = maxComAmt;
    }

    public double getDurOfCompYear() {
        return durOfCompYear;
    }

    public void setDurOfCompYear(double durOfCompYear) {
        this.durOfCompYear = durOfCompYear;
    }

    public double getCompPer() {
        return CompPer;
    }

    public void setCompPer(double CompPer) {
        this.CompPer = CompPer;
    }

    public double getCompFac() {
        return CompFac;
    }

    public void setCompFac(double CompFac) {
        this.CompFac = CompFac;
    }

    public double getDetGraDiv() {
        return detGraDiv;
    }

    public void setDetGraDiv(double detGraDiv) {
        this.detGraDiv = detGraDiv;
    }

    public double getMinPensionAmountForUgc() {
        return minPensionAmountForUgc;
    }

    public void setMinPensionAmountForUgc(double minPensionAmountForUgc) {
        this.minPensionAmountForUgc = minPensionAmountForUgc;
    }

    public double getMinPensionAmountForNonUgc() {
        return minPensionAmountForNonUgc;
    }

    public void setMinPensionAmountForNonUgc(double minPensionAmountForNonUgc) {
        this.minPensionAmountForNonUgc = minPensionAmountForNonUgc;
    }

    public double getPensionInJob() {
        return pensionInJob;
    }

    public void setPensionInJob(double pensionInJob) {
        this.pensionInJob = pensionInJob;
    }

    public double getPensionAfterPer() {
        return pensionAfterPer;
    }

    public void setPensionAfterPer(double pensionAfterPer) {
        this.pensionAfterPer = pensionAfterPer;
    }

    public double getPensionUptoPer() {
        return pensionUptoPer;
    }

    public void setPensionUptoPer(double pensionUptoPer) {
        this.pensionUptoPer = pensionUptoPer;
    }

    public double getPensionAfterJob() {
        return pensionAfterJob;
    }

    public void setPensionAfterJob(double pensionAfterJob) {
        this.pensionAfterJob = pensionAfterJob;
    }

    public double getRetGraDiv() {
        return retGraDiv;
    }

    public void setRetGraDiv(double retGraDiv) {
        this.retGraDiv = retGraDiv;
    }

 
    
    
}