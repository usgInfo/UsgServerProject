/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author KC
 */
public class StudySessionMaster extends Common{
    
    private int sessionCode;
    private String sessionName;
    private String startDate;
    private String endDate;
    private Long startDateInMilliSecond;
    private Long endDateInMilliSecond;

    public int getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(int sessionCode) {
        this.sessionCode = sessionCode;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getStartDateInMilliSecond() {
        return startDateInMilliSecond;
    }

    public void setStartDateInMilliSecond(Long startDateInMilliSecond) {
        this.startDateInMilliSecond = startDateInMilliSecond;
    }

    public Long getEndDateInMilliSecond() {
        return endDateInMilliSecond;
    }

    public void setEndDateInMilliSecond(Long endDateInMilliSecond) {
        this.endDateInMilliSecond = endDateInMilliSecond;
    }
    
}
