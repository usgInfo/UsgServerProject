/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author Asif
 */
public class LeaveTypeMaster extends Common {

    private String leaveType;
    private String shortDescription;
    private String leaveNature;
    private String gender;
    private String fixedLeaveType;
    private String remarks;
    private List<LeaveTypeDetails> leaveTypeDetails;

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLeaveNature() {
        return leaveNature;
    }

    public void setLeaveNature(String leaveNature) {
        this.leaveNature = leaveNature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFixedLeaveType() {
        return fixedLeaveType;
    }

    public void setFixedLeaveType(String fixedLeaveType) {
        this.fixedLeaveType = fixedLeaveType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<LeaveTypeDetails> getLeaveTypeDetails() {
        return leaveTypeDetails;
    }

    public void setLeaveTypeDetails(List<LeaveTypeDetails> leaveTypeDetails) {
        this.leaveTypeDetails = leaveTypeDetails;
    }

}
