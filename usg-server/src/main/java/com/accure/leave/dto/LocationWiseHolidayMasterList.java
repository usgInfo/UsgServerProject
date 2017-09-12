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
 * @author accure
 */
public class LocationWiseHolidayMasterList extends Common{
    
    private List<LocationWiseHolidayMaster> locationWiseHolidayFormList;
    private String locationWiseHoliday;
    private String year;
    private String commonHolidayReport;
    private String holidayTypeReport;
    private String locationReport;
    private String yearReport;
    private String fromDateReport;
    private String toDateReport;
    private int yearInNum;
    private Long fromDateInMilliSecondReport;
    private Long toDateInMilliSecondReport;

    public int getYearInNum() {
        return yearInNum;
    }

    public void setYearInNum(int yearInNum) {
        this.yearInNum = yearInNum;
    }
    

    public String getCommonHolidayReport() {
        return commonHolidayReport;
    }

    public void setCommonHolidayReport(String commonHolidayReport) {
        this.commonHolidayReport = commonHolidayReport;
    }

    public String getHolidayTypeReport() {
        return holidayTypeReport;
    }

    public void setHolidayTypeReport(String holidayTypeReport) {
        this.holidayTypeReport = holidayTypeReport;
    }

    public String getLocationReport() {
        return locationReport;
    }

    public void setLocationReport(String locationReport) {
        this.locationReport = locationReport;
    }

    public String getYearReport() {
        return yearReport;
    }

    public void setYearReport(String yearReport) {
        this.yearReport = yearReport;
    }

    public String getFromDateReport() {
        return fromDateReport;
    }

    public void setFromDateReport(String fromDateReport) {
        this.fromDateReport = fromDateReport;
    }

    public String getToDateReport() {
        return toDateReport;
    }

    public void setToDateReport(String toDateReport) {
        this.toDateReport = toDateReport;
    }
    

    public List<LocationWiseHolidayMaster> getLocationWiseHolidayFormList() {
        return locationWiseHolidayFormList;
    }

    public void setLocationWiseHolidayFormList(List<LocationWiseHolidayMaster> locationWiseHolidayFormList) {
        this.locationWiseHolidayFormList = locationWiseHolidayFormList;
    }

    public String getLocationWiseHoliday() {
        return locationWiseHoliday;
    }

    public void setLocationWiseHoliday(String locationWiseHoliday) {
        this.locationWiseHoliday = locationWiseHoliday;
    }
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    public Long getFromDateInMilliSecondReport() {
        return fromDateInMilliSecondReport;
    }

    public void setFromDateInMilliSecondReport(Long fromDateInMilliSecondReport) {
        this.fromDateInMilliSecondReport = fromDateInMilliSecondReport;
    }
    
    public Long getToDateInMilliSecondReport() {
        return toDateInMilliSecondReport;
    }

    public void setToDateInMilliSecondReport(Long toDateInMilliSecondReport) {
        this.toDateInMilliSecondReport = toDateInMilliSecondReport;
    }
    
}
