/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import java.util.List;

/**
 *
 * @author accure
 */
public class DesignationFundTypeMapping extends CategoryPosts {

    private String ddo;
    private String designation;
    private String grade;
    private String fundType;
    private String budgetHead;
    private String natureType;
    private String desciplineName;
    private String totalPosts;
    private List<CategoryPosts> categoryposts;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
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

    public void setGrade(String grad) {
        this.grade = grade;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getBudgetHead() {
        return budgetHead;
    }

    public void setBudgetHead(String budgetHead) {
        this.budgetHead = budgetHead;
    }

    public String getNatureType() {
        return natureType;
    }

    public void setNatureType(String natureType) {
        this.natureType = natureType;
    }

    public String getDesciplineName() {
        return desciplineName;
    }

    public void setDesciplineName(String desciplineName) {
        this.desciplineName = desciplineName;
    }

    public String getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(String totalPosts) {
        this.totalPosts = totalPosts;
    }

    public List<CategoryPosts> getCategoryposts() {
        return categoryposts;
    }

    public void setCategoryposts(List<CategoryPosts> categoryposts) {
        this.categoryposts = categoryposts;
    }

}
