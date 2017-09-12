/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author user
 */
public class LocationWiseHolidayMaster extends Common {

//    private String location ;
//    private int year;
//    private String fromDate;
//    private String toDate;
//    private String holidayType;
//    private String commonHoliday;
//    private String sno;
    private String commonHolidayId;
    private String sno;
    private String commonHoliday;
    private String holidayType;
    private String location;
    private int year;
    private String fromDate;
    private String toDate;
    private Long fromDateInMilliSecond;
    private List<LocationWiseHolidayMasterList> locWiseHolidayMasterList;

    public List<LocationWiseHolidayMasterList> getLocWiseHolidayMasterList() {
        return locWiseHolidayMasterList;
    }

    public void setLocWiseHolidayMasterList(List<LocationWiseHolidayMasterList> locWiseHolidayMasterList) {
        this.locWiseHolidayMasterList = locWiseHolidayMasterList;
    }
    
    public String getCommonHolidayId() {
        return commonHolidayId;
    }

    public void setCommonHolidayId(String commonHolidayId) {
        this.commonHolidayId = commonHolidayId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
    
    public Long getFromDateInMilliSecond() {
        return fromDateInMilliSecond;
    }

    public void setFromDateInMilliSecond(Long fromDateInMilliSecond) {
        this.fromDateInMilliSecond = fromDateInMilliSecond;
    }
    
}
