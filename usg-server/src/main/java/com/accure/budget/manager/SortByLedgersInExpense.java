/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.budget.manager;

import com.accure.budget.dto.CreateBudgetExpense;
import java.util.Comparator;

/**
 *
 * @author accure
 */
public class SortByLedgersInExpense implements Comparator {
    @Override
    public int compare(Object t, Object t1) {
        CreateBudgetExpense obj1 = (CreateBudgetExpense) t;
        CreateBudgetExpense obj2 = (CreateBudgetExpense) t1;
        if (Integer.parseInt(obj1.getRequestedAmount()) == Integer.parseInt(obj2.getRequestedAmount())) {
            return 0;
        } else if (Integer.parseInt(obj1.getRequestedAmount()) > Integer.parseInt(obj2.getRequestedAmount())) {
            return 1;
        } else {
            return -1;
        }
    }
}
