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
public class Formula extends Common {

    private String description;
    private String formula;
    private String hiddenformula;
    private int order;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getHiddenformula() {
        return hiddenformula;
    }

    public void setHiddenformula(String hiddenformula) {
        this.hiddenformula = hiddenformula;
    }

}
