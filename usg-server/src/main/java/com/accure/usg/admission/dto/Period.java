/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Shwetha T S
 */
public class Period extends Common{

    private String periodName;
    private int totalPeriodDuration;
    private String durationType;
    private int totalNumberOfExams;

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public int getTotalPeriodDuration() {
        return totalPeriodDuration;
    }

    public void setTotalPeriodDuration(int totalPeriodDuration) {
        this.totalPeriodDuration = totalPeriodDuration;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public int getTotalNumberOfExams() {
        return totalNumberOfExams;
    }

    public void setTotalNumberOfExams(int totalNumberOfExams) {
        this.totalNumberOfExams = totalNumberOfExams;
    }

}
