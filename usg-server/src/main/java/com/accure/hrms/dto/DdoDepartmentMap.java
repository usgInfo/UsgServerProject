/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;
import java.util.List;

/**
 *
 * @author accure
 */
public class DdoDepartmentMap extends Common {

    private String ddo;
    private List<String> departmentList;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

  
    public List<String> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<String> departmentList) {
        this.departmentList = departmentList;
    }

   

}
