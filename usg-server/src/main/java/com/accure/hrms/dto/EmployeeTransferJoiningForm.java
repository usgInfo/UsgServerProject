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
 * @author user
 */
public class EmployeeTransferJoiningForm extends Common{
    private List<TransfeJoiningForm> employeeTransferJoiningFormList;

    public List<TransfeJoiningForm> getEmployeeTransferJoiningFormList() {
        return employeeTransferJoiningFormList;
    }

    public void setEmployeeTransferJoiningFormList(List<TransfeJoiningForm> employeeTransferJoiningFormList) {
        this.employeeTransferJoiningFormList = employeeTransferJoiningFormList;
    }

  
    
    
    
    
}
