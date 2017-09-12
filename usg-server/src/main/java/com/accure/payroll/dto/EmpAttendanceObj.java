/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import java.util.List;

/**
 *
 * @author Mano
 */
public class EmpAttendanceObj extends EmpAttendance{
    private String ddo;
    //private String month;
    //private String year;
    private List<EmpAttendance> attendanceList;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

//    public String getMonth() {
//        return month;
//    }
//
//    public void setMonth(String month) {
//        this.month = month;
//    }
//
//    public String getYear() {
//        return year;
//    }
//
//    public void setYear(String year) {
//        this.year = year;
//    }

    public List<EmpAttendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<EmpAttendance> attendanceList) {
        this.attendanceList = attendanceList;
    }
    
    
}
