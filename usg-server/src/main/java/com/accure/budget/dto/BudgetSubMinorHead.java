/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class BudgetSubMinorHead extends Common {

    private String subMinorHead;
    private String subMinorHeadCode;

    public String getSubMinorHead() {
        return subMinorHead;
    }

    public void setSubMinorHead(String subMinorHead) {
        this.subMinorHead = subMinorHead;
    }

    public String getSubMinorHeadCode() {
        return subMinorHeadCode;
    }

    public void setSubMinorHeadCode(String subMinorHeadCode) {
        this.subMinorHeadCode = subMinorHeadCode;
    }
}
