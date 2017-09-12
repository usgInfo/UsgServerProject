/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.leave.manager;

import com.accure.leave.dto.LeaveTransaction;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class LeaveTransactionSortByEmpNameManager implements Comparator {

    @Override
    public int compare(Object t, Object t1) {
        LeaveTransaction obj1 = (LeaveTransaction) t;
        LeaveTransaction obj2 = (LeaveTransaction) t1;
        return obj1.getEmployeeName().compareTo(obj2.getEmployeeName());
    }
}
