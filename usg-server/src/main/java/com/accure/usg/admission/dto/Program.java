/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.admission.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author accure
 */
public class Program extends Common{
    
    private String university;
    private String institution;
    private String facultyOrDegree;
    private String programCode;
    private String programName;
    private String programAlias;
    private String programType;
    private String period;

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getFacultyOrDegree() {
        return facultyOrDegree;
    }

    public void setFacultyOrDegree(String facultyOrDegree) {
        this.facultyOrDegree = facultyOrDegree;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramAlias() {
        return programAlias;
    }

    public void setProgramAlias(String programAlias) {
        this.programAlias = programAlias;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
    
}
