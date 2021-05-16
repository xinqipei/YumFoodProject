package com.example.yummfoodapp;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class InfoActivityTest {
    @Test
    public void testCalculatePriceWithExtraLargeOnly() {

        InfoActivity infoActivity = new InfoActivity();
        int price = 40, quantity = 3;
        boolean addExtraLarge = true , addFine =false;
        int total = infoActivity.CalculatePrice(price,quantity,addExtraLarge,addFine);
        assertEquals(total,123);
    }
    @Test
    public void testCalculatePriceWithFineOnly() {

        InfoActivity infoActivity = new InfoActivity();
        int price = 20, quantity = 3;
        boolean addExtraLarge = false, addFine = true;
        int total = infoActivity.CalculatePrice(price, quantity, addExtraLarge, addFine);
        assertEquals(total, 63);
    }
    @Test
    public void testCalculatePriceWithFineAndExtraLarge() {

        InfoActivity infoActivity = new InfoActivity();
        int price = 20, quantity = 3;
        boolean addExtraLarge = true, addFine = true;
        int total = infoActivity.CalculatePrice(price, quantity, addExtraLarge, addFine);
        assertEquals(total, 66);
    }
    
}