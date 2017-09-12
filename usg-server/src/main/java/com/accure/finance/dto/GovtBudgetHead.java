/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class GovtBudgetHead extends Common {

    private String majorHead;
    private String subMajorHead;
    private String minorHead;
    private String subMinorHead;
    private String order;
    private String remarks;

    public String getMajorHead() {
        return majorHead;
    }

    public void setMajorHead(String majorHead) {
        this.majorHead = majorHead;
    }

    public String getSubMajorHead() {
        return subMajorHead;
    }

    public void setSubMajorHead(String subMajorHead) {
        this.subMajorHead = subMajorHead;
    }

    public String getMinorHead() {
        return minorHead;
    }

    public void setMinorHead(String minorHead) {
        this.minorHead = minorHead;
    }

    public String getSubMinorHead() {
        return subMinorHead;
    }

    public void setSubMinorHead(String subMinorHead) {
        this.subMinorHead = subMinorHead;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
