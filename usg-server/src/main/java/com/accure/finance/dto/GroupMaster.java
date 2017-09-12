/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author deepak2310
 */
public class GroupMaster extends Common {
    
    private String groupName;
    private String remarks;
    

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
