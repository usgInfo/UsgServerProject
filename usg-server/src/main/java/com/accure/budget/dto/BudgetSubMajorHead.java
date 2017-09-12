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
public class BudgetSubMajorHead extends Common {

    private String subMajorHead;
    private String subMajorHeadCode;

    public String getSubMajorHead() {
        return subMajorHead;
    }

    public void setSubMajorHead(String subMajorHead) {
        this.subMajorHead = subMajorHead;
    }

    public String getSubMajorHeadCode() {
        return subMajorHeadCode;
    }

    public void setSubMajorHeadCode(String subMajorHeadCode) {
        this.subMajorHeadCode = subMajorHeadCode;
    }
}
