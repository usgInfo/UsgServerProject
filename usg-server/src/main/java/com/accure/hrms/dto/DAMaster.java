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
public class DAMaster extends Common {

    String definedRate;
    int paidRate;
    int actualRate;
    String effFromDate;
    String effToDate;
    String isPensionUsed;

    public String getDefinedRate() {
        return definedRate;
    }

    public void setDefinedRate(String definedRate) {
        this.definedRate = definedRate;
    }

    public int getPaidRate() {
        return paidRate;
    }

    public void setPaidRate(int paidRate) {
        this.paidRate = paidRate;
    }

    public int getActualRate() {
        return actualRate;
    }

    public void setActualRate(int actualRate) {
        this.actualRate = actualRate;
    }

    public String getEffFromDate() {
        return effFromDate;
    }

    public void setEffFromDate(String effFromDate) {
        this.effFromDate = effFromDate;
    }

    public String getEffToDate() {
        return effToDate;
    }

    public void setEffToDate(String effToDate) {
        this.effToDate = effToDate;
    }

    public String getIsPensionUsed() {
        return isPensionUsed;
    }

    public void setIsPensionUsed(String isPensionUsed) {
        this.isPensionUsed = isPensionUsed;
    }

}
