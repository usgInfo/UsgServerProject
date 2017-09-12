/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import java.util.List;

/**
 *
 * @author upendra
 */
public class AttendanceAdj extends AttendanceAdjListDto {

    private String ddo;
    private String month;
    private String year;
    private List<AttendanceAdjListDto> attendanceList;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<AttendanceAdjListDto> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<AttendanceAdjListDto> attendanceList) {
        this.attendanceList = attendanceList;
    }
}
