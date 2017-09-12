/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author user
 */
public class Group extends Common{
    private String groupName;
    private String groupAlias;
    private String underGroup;
    private String underGroupName;
    private String nature;
    private String isActive;

    public String getUnderGroupName() {
        return underGroupName;
    }

    public void setUnderGroupName(String underGroupName) {
        this.underGroupName = underGroupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAlias() {
        return groupAlias;
    }

    public void setGroupAlias(String groupAlias) {
        this.groupAlias = groupAlias;
    }

    public String getUnderGroup() {
        return underGroup;
    }

    public void setUnderGroup(String underGroup) {
        this.underGroup = underGroup;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    
}
