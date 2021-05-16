package com.example.yummfoodapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class InfoActivityIntegrationTest {
    @Rule
    public ActivityScenarioRule<InfoActivity> mActivityTestRule = new ActivityScenarioRule<InfoActivity>(InfoActivity.class);

    @Test
    public void incrementQuantityTest() {
        onView(withId(R.id.addquantity)).perform(click());
        onView(withId(R.id.quantity)).check(matches(withText("1")));
        onView(withId(R.id.addquantity)).perform(click());
        onView(withId(R.id.quantity)).check(matches(withText("2")));
    }
    @Test
    public void decrementQuantityTest() {
        onView(withId(R.id.subquantity)).perform(click());
        onView(withId(R.id.quantity)).check(matches(withText("0")));
        onView(withId(R.id.addquantity)).perform(click());
        onView(withId(R.id.addquantity)).perform(click());
        onView(withId(R.id.quantity)).check(matches(withText("2")));
        onView(withId(R.id.subquantity)).perform(click());
        onView(withId(R.id.quantity)).check(matches(withText("1")));
    }

}