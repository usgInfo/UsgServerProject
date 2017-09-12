/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class PartyMaster extends Common{
    
    private String partyName;
    private String partyContactNumber;
    private String firstContactPersonName;
    private String secondContactPersonName;
    private String firstContactAddress;
    private String secondContactAddress;
    private String remarks;

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyContactNumber() {
        return partyContactNumber;
    }

    public void setPartyContactNumber(String partyContactNumber) {
        this.partyContactNumber = partyContactNumber;
    }

    public String getFirstContactPersonName() {
        return firstContactPersonName;
    }

    public void setFirstContactPersonName(String firstContactPersonName) {
        this.firstContactPersonName = firstContactPersonName;
    }

    public String getSecondContactPersonName() {
        return secondContactPersonName;
    }

    public void setSecondContactPersonName(String secondContactPersonName) {
        this.secondContactPersonName = secondContactPersonName;
    }

    public String getFirstContactAddress() {
        return firstContactAddress;
    }

    public void setFirstContactAddress(String firstContactAddress) {
        this.firstContactAddress = firstContactAddress;
    }

    public String getSecondContactAddress() {
        return secondContactAddress;
    }

    public void setSecondContactAddress(String secondContactAddress) {
        this.secondContactAddress = secondContactAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
