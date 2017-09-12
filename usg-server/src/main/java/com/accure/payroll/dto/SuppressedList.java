/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author Shwetha T S
 */
public class SuppressedList extends Common{
    private boolean isSuppressed;
    private long suppressedDate;
    private long unSuppressedDate;

    public boolean getIsSuppressed() {
        return isSuppressed;
    }

    public void setIsSuppressed(boolean isSuppressed) {
        this.isSuppressed = isSuppressed;
    }

    public long getSuppressedDate() {
        return suppressedDate;
    }

    public void setSuppressedDate(long suppressedDate) {
        this.suppressedDate = suppressedDate;
    }

    public long getUnSuppressedDate() {
        return unSuppressedDate;
    }
    public void setUnSuppressedDate(long unSuppressedDate) {
        this.unSuppressedDate = unSuppressedDate;
    }

  
}
