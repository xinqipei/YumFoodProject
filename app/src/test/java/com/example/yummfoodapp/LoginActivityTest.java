package com.example.yummfoodapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    public void testIsEmailInValid() {
        String input = "adas!//]";
        boolean output;
        boolean expected = false;
        LoginActivity login = new LoginActivity();
        output = login.isEmailValid(input);
        assertEquals(expected,output);
    }
    @Test
    public void testIsEmailValid() {
        String input = "adas@gmail.com";
        boolean output;
        boolean expected = true;
        LoginActivity login = new LoginActivity();
        output = login.isEmailValid(input);
        assertEquals(expected,output);
    }

}