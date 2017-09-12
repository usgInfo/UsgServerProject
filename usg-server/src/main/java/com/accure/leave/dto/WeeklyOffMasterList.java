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
 * @author user
 */
public class WeeklyOffMasterList extends Common {

    private List<WeeklyOffMaster> weeklyoffFormList;
    private String weeklyOffLocation;

    public String getWeeklyOffLocation() {
        return weeklyOffLocation;
    }

    public void setWeeklyOffLocation(String weeklyOffLocation) {
        this.weeklyOffLocation = weeklyOffLocation;
    }

    public List<WeeklyOffMaster> getWeeklyoffFormList() {
        return weeklyoffFormList;
    }

    public void setWeeklyoffFormList(List<WeeklyOffMaster> weeklyoffFormList) {
        this.weeklyoffFormList = weeklyoffFormList;
    }

}
