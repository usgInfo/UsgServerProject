/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author deepak2310
 */
public class LedgerCategory extends Common {

    private String parentLedger;
    private String parentLedgerName;
    private String ledgerCategory;
    private String orderLevel;
    private String ledgerDetail;

    public String getParentLedger() {
        return parentLedger;
    }

    public void setParentLedger(String parentLedger) {
        this.parentLedger = parentLedger;
    }

    public String getLedgerCategory() {
        return ledgerCategory;
    }

    public void setLedgerCategory(String ledgerCategory) {
        this.ledgerCategory = ledgerCategory;
    }

    public String getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(String orderLevel) {
        this.orderLevel = orderLevel;
    }

    public String getLedgerDetail() {
        return ledgerDetail;
    }

    public void setLedgerDetail(String ledgerDetail) {
        this.ledgerDetail = ledgerDetail;
    }

    public String getParentLedgerName() {
        return parentLedgerName;
    }

    public void setParentLedgerName(String parentLedgerName) {
        this.parentLedgerName = parentLedgerName;
    }

}
