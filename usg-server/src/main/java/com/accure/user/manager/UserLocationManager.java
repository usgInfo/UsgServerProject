/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.manager;

import com.accure.finance.dto.DDO;
import com.accure.finance.dto.Location;
import com.accure.finance.manager.DDOManager;
import com.accure.finance.manager.LocationManager;
import com.accure.usg.common.dto.OrgRole;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Asif
 */
public class UserLocationManager {
    
    public String getLocationOfUser(List<OrgRole> orgRole) throws Exception {
        List<String> locationList = new ArrayList<String>();
        if (orgRole != null) {
            for (Iterator<OrgRole> iterator = orgRole.iterator(); iterator.hasNext();) {
                OrgRole next = iterator.next();
                List<String> listOfLocationIds = next.getLocation();
                if (listOfLocationIds != null && !listOfLocationIds.isEmpty()) {
                    for (Iterator<String> iterator1 = listOfLocationIds.iterator(); iterator1.hasNext();) {
                        String next1 = iterator1.next();
                        if (next1 != null && !next1.isEmpty()) {
                            locationList.add(next1);
                        }
                    }
                }
            }
        }
        List<String> returnOptions = new ArrayList<String>();
        if (locationList != null && locationList.size() > 1) {
            for (Iterator<String> iterator = locationList.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                Location loc = new LocationManager().fetch(next);
                if (loc != null) {
                    String pre = "<option value=" + next + ">" + loc.getLocationName() + "</option>";
                    returnOptions.add(pre);
                }
            }
        } else if (locationList != null && locationList.size() == 1) {
            returnOptions.add(locationList.get(0));
        }
        return new Gson().toJson(returnOptions);
    }
    
    public String getloggedInDdoLocation(String ddoId, String locationId) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        DDO ddoObj = null;
        Location locationObj = null;
        if (ddoId != null && !ddoId.isEmpty() && !ddoId.equals("")) {
            ddoObj = new DDOManager().fetch(ddoId);
        }
        if (locationId != null && !locationId.isEmpty() && !locationId.equals("")) {
            locationObj = new LocationManager().fetch(locationId);
        }
        resultMap.put("ddoObj", new Gson().toJson(ddoObj));
        resultMap.put("locationObj", new Gson().toJson(locationObj));
        
        return new Gson().toJson(resultMap);
    }
    
}
