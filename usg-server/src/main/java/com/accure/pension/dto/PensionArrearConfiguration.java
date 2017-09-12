/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class PensionArrearConfiguration extends Common{
    private String headName;
    private int arrearOrder;

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public int getArrearOrder() {
        return arrearOrder;
    }

    public void setArrearOrder(int arrearOrder) {
        this.arrearOrder = arrearOrder;
    }
    
    
}
