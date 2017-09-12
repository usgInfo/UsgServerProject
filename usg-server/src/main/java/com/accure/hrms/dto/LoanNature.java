/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class LoanNature extends  Common{
    
    private String loanName;
    
    private String isRefundable;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(String isRefundable) {
        this.isRefundable = isRefundable;
    }
    
    
    
    
}
