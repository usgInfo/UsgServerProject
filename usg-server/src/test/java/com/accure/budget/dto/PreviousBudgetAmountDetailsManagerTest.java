/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accure.budget.dto;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class PreviousBudgetAmountDetailsManagerTest {

    public PreviousBudgetAmountDetailsManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPreviousBudgetAmountDetails method, of class
     * PreviousBudgetAmountDetailsManager.
     */
//    @Test
    public void testGetPreviousBudgetAmountDetails_HashMap_String() {
        System.out.println("getPreviousBudgetAmountDetails");
        HashMap<String, Object> queryFilterMap = new HashMap();
        String selectedYear = "2016";
        PreviousBudgetAmountDetailsManager instance = new PreviousBudgetAmountDetailsManager();
        List<PreviousBudgetAmountDetails> result = instance.getPreviousBudgetAmountDetails(queryFilterMap, selectedYear);
        assertNotNull(result);
        System.out.println("Result:"+new Gson().toJson(result));
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getPreviousBudgetAmountDetails method, of class
     * PreviousBudgetAmountDetailsManager.
     */
//    @Test
    public void testGetPreviousBudgetAmountDetails_0args() {
        System.out.println("getPreviousBudgetAmountDetails");
        PreviousBudgetAmountDetailsManager instance = new PreviousBudgetAmountDetailsManager();
        PreviousBudgetAmountDetails expResult = null;
        PreviousBudgetAmountDetails result = instance.getPreviousBudgetAmountDetails();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setPreviousBudgetAmountDetails method, of class
     * PreviousBudgetAmountDetailsManager.
     */
//    @Test
    public void testSetPreviousBudgetAmountDetails() {
        System.out.println("setPreviousBudgetAmountDetails");
        PreviousBudgetAmountDetails previousBudgetAmountDetails = null;
        PreviousBudgetAmountDetailsManager instance = new PreviousBudgetAmountDetailsManager();
        instance.setPreviousBudgetAmountDetails(previousBudgetAmountDetails);
        // TODO review the generated test code and remove the default call to fail.
    }

}
