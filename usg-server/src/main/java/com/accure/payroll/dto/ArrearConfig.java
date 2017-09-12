/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class ArrearConfig extends Common {

    private String ddo;
    private String head;
    private String order;

    public String getDdo() {
        return ddo;
    }

    public void setDdo(String ddo) {
        this.ddo = ddo;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
