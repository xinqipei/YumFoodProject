package com.example.yummfoodapp;

import com.example.yummfoodapp.Model.Order;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {
    @Test
    public void testOrderClass()
    {
        Order testingOrder = new Order("TestDrinkName","90","2","No","yes");
        assertEquals("TestDrinkName", testingOrder.getNameofdrink());
        assertEquals("90", testingOrder.getPricesofdrink());
        testingOrder.setPricesofdrink("80");
        assertEquals("80", testingOrder.getPricesofdrink());
    }
}