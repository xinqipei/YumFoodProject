package com.example.yummfoodapp;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class ShopActivityTest {
    @Rule
    public ActivityScenarioRule<ShopActivity> mActivityTestRule = new ActivityScenarioRule<ShopActivity>(ShopActivity.class);

    @Test
    public void incrementQuantityTest() {
        onView(withId(R.id.addProductsBtn)).perform(click());
        Intents.release();
    }

}