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
public class QuarterCategory extends Common{
    private String carpetFrom;
    private String carpetTo;
    private String quarters;
    private String payFrom;
    private String payTo;
    private String rentAmount;
    private String waterAmount;
    private String remarks;

    public String getCarpetFrom() {
        return carpetFrom;
    }

    public void setCarpetFrom(String carpetFrom) {
        this.carpetFrom = carpetFrom;
    }

    public String getCarpetTo() {
        return carpetTo;
    }

    public void setCarpetTo(String carpetTo) {
        this.carpetTo = carpetTo;
    }

    public String getQuarters() {
        return quarters;
    }

    public void setQuarters(String quarters) {
        this.quarters = quarters;
    }

    public String getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(String payFrom) {
        this.payFrom = payFrom;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(String PayTo) {
        this.payTo = PayTo;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(String rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(String waterAmount) {
        this.waterAmount = waterAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
    
    
}
