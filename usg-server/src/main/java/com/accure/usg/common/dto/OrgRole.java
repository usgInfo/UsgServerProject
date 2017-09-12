/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.usg.common.dto;

import java.util.List;

/**
 *
 * @author deepak2310
 */
public class OrgRole extends Common {

    private List location;
    private List<Role> role;

    public List getLocation() {
        return location;
    }

    public void setLocation(List location) {
        this.location = location;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }

}
