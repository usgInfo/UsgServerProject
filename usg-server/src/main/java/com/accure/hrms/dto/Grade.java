/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author Asif
 */
public class Grade extends Common{
    private  String gradeName;
    private  String gradePay;
    private  String incrementType;
    private  String remarks;
    private  List<GradeDetails> GradeDetailsList;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

    public String getIncrementType() {
        return incrementType;
    }

    public void setIncrementType(String incrementType) {
        this.incrementType = incrementType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<GradeDetails> getGradeDetailsList() {
        return GradeDetailsList;
    }

    public void setGradeDetailsList(List<GradeDetails> GradeDetailsList) {
        this.GradeDetailsList = GradeDetailsList;
    }
    
}
