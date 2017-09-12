/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.finance.manager;

import com.accure.finance.dto.LedgerList;
import java.util.Comparator;

/**
 *
 * @author accure
 */
public class VoucherDatesSortBy implements Comparator<LedgerList> {
 
//    @Override
//    public int compare(Object t, Object t1) {
//        LedgerList obj1 = (LedgerList) t;
//        LedgerList obj2 = (LedgerList) t1;
//        if (obj1.getVoucherDateInMilliSecondReport() == obj2.getVoucherDateInMilliSecondReport()) {
//            return 0;
//        } else if (obj1.getVoucherDateInMilliSecondReport() > obj2.getVoucherDateInMilliSecondReport()) {
//            return 1;
//        } else {
//            return -1;
//        }
//    }
    
//    @Override
//    public int compare(Object t, Object t1)
//{
//    double delta = t.getV - p2.getY();
//    if(delta > 0.00001) return 1;
//    if(delta < -0.00001) return -1;
//    return 0;
//}
    public int compare(LedgerList o1, LedgerList o2) {
        return o1.getVoucherDateInMilliSecondReport().compareTo(o2.getVoucherDateInMilliSecondReport());
    }
}
