/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.SalaryHead;
import java.util.Comparator;

/**
 *
 * @author Asif
 */
public class SortByDisplayLeave implements Comparator {

    @Override
    public int compare(Object t, Object t1) {
        SalaryHead obj1 = (SalaryHead) t;
        SalaryHead obj2 = (SalaryHead) t1;
        if (obj1.getDisplayLevel() == obj2.getDisplayLevel()) {
            return 0;
        } else if (obj1.getDisplayLevel() > obj2.getDisplayLevel()) {
            return 1;
        } else {
            return -1;
        }
    }

}
