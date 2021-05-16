package com.example.yummfoodapp;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class mydemotest {

    @Test
    public void testIsEmailInValid() {
        String input = "adas!//]";
        boolean output;
        boolean expected = false;
        LoginActivity login = new LoginActivity();
        output = login.isEmailValid(input);
        assertEquals(expected,output);
    }
}
//i wanna know how this junit thin works i dont understand the code that what i mean sorry
//unit test is writen for testing the validation apply on code.
//support if we write a method who will return 5 we will pass 2 +3  then he will return 5 and test will be pass but if method return
//6 or something else by passing 3,2  it means test faild right? yes, watch a demo video on youtube you will understand easily
// whats teh diferent with different unit test frameworks then ? like JUnit, Mockito, JMock, JMeter.
//junit is for checking for wirting backend code. mockito is used for front end related work. and don't know about others. okey i got you, so junit  is it the default in the android studio or u have to configure it yes
// we write for different activites and for model classes.
