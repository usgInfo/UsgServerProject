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
public class CommonHolidayMaster extends Common{
    
    private String holidayType;
    private String commonHoliday;
    private long displayOrder;
    private String remarks;

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public String getCommonHoliday() {
        return commonHoliday;
    }

    public void setCommonHoliday(String commonHoliday) {
        this.commonHoliday = commonHoliday;
    }
    public long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
