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
public class MajorHead extends Common {
    private String majorHead;
    private String majorHeadCode;

    public String getMajorHead() {
        return majorHead;
    }

    public void setMajorHead(String majorHead) {
        this.majorHead = majorHead;
    }

    public String getMajorHeadCode() {
        return majorHeadCode;
    }

    public void setMajorHeadCode(String majorHeadCode) {
        this.majorHeadCode = majorHeadCode;
    }
    
    
}
