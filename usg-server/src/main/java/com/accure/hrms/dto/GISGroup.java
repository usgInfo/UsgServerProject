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
public class GISGroup extends Common {
    
    private String groupName;
    private String insCoverage;
    private String rateOfSub;
    private String gradePayTo;
    private String gradePayFrom;
    private String remarks;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInsCoverage() {
        return insCoverage;
    }

    public void setInsCoverage(String insCoverage) {
        this.insCoverage = insCoverage;
    }

    public String getRateOfSub() {
        return rateOfSub;
    }

    public void setRateOfSub(String rateOfSub) {
        this.rateOfSub = rateOfSub;
    }

    public String getGradePayTo() {
        return gradePayTo;
    }

    public void setGradePayTo(String gradePayTo) {
        this.gradePayTo = gradePayTo;
    }

    public String getGradePayFrom() {
        return gradePayFrom;
    }

    public void setGradePayFrom(String gradePayFrom) {
        this.gradePayFrom = gradePayFrom;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "GISGroup{" + "groupName=" + groupName + ", insCoverage=" + insCoverage + ", rateOfSub=" + rateOfSub + ", gradePayTo=" + gradePayTo + ", gradePayFrom=" + gradePayFrom + ", remarks=" + remarks + '}';
    }

    public void setGISGroup(String gisGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
