/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class Class extends Common{
    private String name;
    private String gadNonGad;
    private String teaching;
    private String nonTeaching;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGadNonGad() {
        return gadNonGad;
    }

    public void setGadNonGad(String gadNonGad) {
        this.gadNonGad = gadNonGad;
    }

    public String getTeaching() {
        return teaching;
    }

    public void setTeaching(String teaching) {
        this.teaching = teaching;
    }

    public String getNonTeaching() {
        return nonTeaching;
    }

    public void setNonTeaching(String nonTeaching) {
        this.nonTeaching = nonTeaching;
    }
    
    
    
    
    
    
}
