/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.dto;

import com.accure.usg.common.dto.Common;





/**
 *
 * @author user
 */
public class HolidayTypeMaster extends Common{
    private String holidayType=null;
    private String userID=null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    

    public String getHolidayType() {
        return holidayType;
    }

  
    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }
   
    
}
