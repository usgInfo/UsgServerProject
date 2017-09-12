/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.hrms.manager;

import com.accure.hrms.dto.EmployeeFileHolder;
import java.util.Comparator;

/**
 *
 * @author Asif
 */
public class GetLatestEmpImageComparator implements Comparator {

    @Override
    public int compare(Object t, Object t1) {
        EmployeeFileHolder e1 = (EmployeeFileHolder) t;
        EmployeeFileHolder e2 = (EmployeeFileHolder) t1;
        if (e1.getUploadDate().before(e2.getUploadDate())) {
            return 1;
        } else if (e1.getUploadDate().after(e2.getUploadDate())) {
            return -1;
        } else {
            return 0;
        }
    }

}
