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
public class Designation extends Common{
    
    private  String designation;
    private  String designationCategory;
    private  String grade;
    private  String gradeName;
    private  String seniorityLevel;
    private  String retirementAge;
    private  String qualification;
    private  String gradePay;
    private  String remarks;
    private  String clas;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDesignationCategory() {
        return designationCategory;
    }

    public void setDesignationCategory(String designationCategory) {
        this.designationCategory = designationCategory;
    }

    public String getSeniorityLevel() {
        return seniorityLevel;
    }

    public void setSeniorityLevel(String seniorityLevel) {
        this.seniorityLevel = seniorityLevel;
    }

    public String getRetirementAge() {
        return retirementAge;
    }

    public void setRetirementAge(String retirementAge) {
        this.retirementAge = retirementAge;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }
    
    
    
    
}
