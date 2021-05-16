package com.example.yummfoodapp;

import androidx.test.espresso.action.TypeTextAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void signupTestWithEmptyEmail() {
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.editText)).check(matches(hasErrorText("please enter your email")));
    }
    @Test
    public void signupTestWithEmptyPassword() {
        onView(withId(R.id.editText)).perform(typeText("li@gmill.com"),closeSoftKeyboard());
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.editText2)).check(matches(hasErrorText("please enter your password")));
    }


}