/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;



/**
 *
 * @author user
 */
public class HolidayLocationMaster extends Common{
    private String holidayLocation;
    private String displayOrder;
    private String remarks;
    

    public String getHolidayLocation() {
        return holidayLocation;
    }

    public void setHolidayLocation(String holidayLocation) {
        this.holidayLocation = holidayLocation;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
 }
