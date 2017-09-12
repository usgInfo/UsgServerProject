/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class Relation extends Common {
    
    private String relation;
    private String relationRemarks;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationRemarks() {
        return relationRemarks;
    }

    public void setRelationRemarks(String relationRemarks) {
        this.relationRemarks = relationRemarks;
    }
    
    
    
}
