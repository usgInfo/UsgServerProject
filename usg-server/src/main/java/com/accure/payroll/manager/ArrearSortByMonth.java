/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.payroll.manager;

import com.accure.payroll.dto.ArrearProcess;
import java.util.Comparator;

/**
 *
 * @author user
 */
class ArrearSortByMonth implements Comparator {

   

    @Override
    public int compare(Object t, Object t1) {
        ArrearProcess obj1 = (ArrearProcess) t;
        ArrearProcess obj2 = (ArrearProcess) t1;
       if(obj1.getMonth()>obj2.getMonth())
       {
          return 1; 
       }
       else if(obj1.getMonth()<obj2.getMonth())
       {
          return -1;  
       }
        else 
       {
          return 0;  
       }
       
    }
}
