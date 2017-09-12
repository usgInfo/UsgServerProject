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
public class GradeDetails extends Common{
    private String gradeOne;
    private String gradeTwo;
    private String gradeThree;
    private String order;

    public String getGradeOne() {
        return gradeOne;
    }

    public void setGradeOne(String gradeOne) {
        this.gradeOne = gradeOne;
    }

    public String getGradeTwo() {
        return gradeTwo;
    }

    public void setGradeTwo(String gradeTwo) {
        this.gradeTwo = gradeTwo;
    }

    public String getGradeThree() {
        return gradeThree;
    }

    public void setGradeThree(String gradeThree) {
        this.gradeThree = gradeThree;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
    
    
    
}
