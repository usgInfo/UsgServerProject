/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.leave.dto.LeaveTransaction;
import com.accure.payroll.dto.ArrearProcess;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class ArrearSortManager implements Comparator {

    @Override
    public int compare(Object t, Object t1) {
        ArrearProcess obj1 = (ArrearProcess) t;
        ArrearProcess obj2 = (ArrearProcess) t1;
        return obj1.getEmployeeCode().compareTo(obj2.getEmployeeCode());
    }
    
}
