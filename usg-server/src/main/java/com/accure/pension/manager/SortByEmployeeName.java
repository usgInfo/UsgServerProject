/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.pension.manager;

import com.accure.leave.dto.LeaveTransaction;
import com.accure.pension.dto.PensionEmployee;
import java.util.Comparator;

/**
 *
 * @author user
 */
public class SortByEmployeeName  implements Comparator {

    @Override
    public int compare(Object t, Object t1) 
    {
        PensionEmployee obj1 = (PensionEmployee) t;
        PensionEmployee obj2 = (PensionEmployee) t1;
        return obj1.getEmployeeName().compareTo(obj2.getEmployeeName());
}
}