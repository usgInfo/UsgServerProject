/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.leave.dto.LocationWiseHolidayMaster;
import com.accure.leave.dto.LocationWiseHolidayMasterList;
import java.util.Comparator;

/**
 *
 * @author accure
 */
public class LocationWiseHolidaysSortBy implements Comparator<LocationWiseHolidayMaster>{
    
//    public int compare(LocationWiseHolidayMasterList o1, LocationWiseHolidayMasterList o2) {
//        return o1.getFromDateInMilliSecondReport().compareTo(o2.getFromDateInMilliSecondReport());
//    }

//    @Override
//    public int compare(LocationWiseHolidayMaster o1, LocationWiseHolidayMaster o2) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
//    @Override
    public int compare(LocationWiseHolidayMaster o1, LocationWiseHolidayMaster o2) {
        return o1.getFromDateInMilliSecond().compareTo(o2.getFromDateInMilliSecond());
    }
    
}
