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
public class FacultyDegreeMapping extends Common{
    
    private String location;
    private String course;
    private String faculty;
    private String isGraceApplicable;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getIsGraceApplicable() {
        return isGraceApplicable;
    }

    public void setIsGraceApplicable(String isGraceApplicable) {
        this.isGraceApplicable = isGraceApplicable;
    }
    
}
