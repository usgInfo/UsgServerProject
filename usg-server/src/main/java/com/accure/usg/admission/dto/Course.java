/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author khurshid
 * 
 */
public class Course  extends Common{
    private String ddo;
    private String location;
    private String courseCode;
    private String courseName;
    private String isMidExamApplicable;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

  
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    

    public String getIsMidExamApplicable() {
        return isMidExamApplicable;
    }

    public void setIsMidExamApplicable(String isMidExamApplicable) {
        this.isMidExamApplicable = isMidExamApplicable;
    }

   
    
}
