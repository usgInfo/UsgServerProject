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
public class MinorHead extends Common {
    private String minorHead;
    private String minorHeadCode;

    public String getMinorHead() {
        return minorHead;
    }

    public void setMinorHead(String minorHead) {
        this.minorHead = minorHead;
    }

    public String getMinorHeadCode() {
        return minorHeadCode;
    }

    public void setMinorHeadCode(String minorHeadCode) {
        this.minorHeadCode = minorHeadCode;
    }
    
}
