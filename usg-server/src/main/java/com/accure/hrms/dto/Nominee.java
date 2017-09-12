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
 * @author upendra
 */
public class Nominee extends Common {

    private String name;
    private String relationShip;
    private String nomineeAge;
    private String nomineeShare;
    private String isMinor;
    private String otherDetails;
    private List<String> nomineeType;
    private String nomineePrimary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    public String getNomineeAge() {
        return nomineeAge;
    }

    public void setNomineeAge(String nomineeAge) {
        this.nomineeAge = nomineeAge;
    }

    public String getNomineeShare() {
        return nomineeShare;
    }

    public void setNomineeShare(String nomineeShare) {
        this.nomineeShare = nomineeShare;
    }

    public String getIsMinor() {
        return isMinor;
    }

    public void setIsMinor(String isMinor) {
        this.isMinor = isMinor;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public List<String> getNomineeType() {
        return nomineeType;
    }

    public void setNomineeType(List<String> nomineeType) {
        this.nomineeType = nomineeType;
    }

    public String getNomineePrimary() {
        return nomineePrimary;
    }

    public void setNomineePrimary(String nomineePrimary) {
        this.nomineePrimary = nomineePrimary;
    }
    
    

}
