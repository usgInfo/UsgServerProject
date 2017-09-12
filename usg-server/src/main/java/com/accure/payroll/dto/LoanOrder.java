/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class LoanOrder extends Common {

    private String ddo;
    private String ddoId;

    public String getDdoId() {
        return ddoId;
    }

    public void setDdoId(String ddoId) {
        this.ddoId = ddoId;
    }
    private String orderNo;
    private String sanctionedBy;
    private String remarks;
    private String loanType;
    private String comptroller;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSanctionedBy() {
        return sanctionedBy;
    }

    public void setSanctionedBy(String sanctionedBy) {
        this.sanctionedBy = sanctionedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getComptroller() {
        return comptroller;
    }

    public void setComptroller(String comptroller) {
        this.comptroller = comptroller;
    }
}
