/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.user.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author accure
 */
public class DdoLocationMap extends Common{
    
    private String ddo;

    private List<String> location;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

   

    
}
