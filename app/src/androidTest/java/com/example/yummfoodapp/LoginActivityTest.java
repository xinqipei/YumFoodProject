package com.example.yummfoodapp;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {
   // intended(hasComponent(YourActivity.class.getName()));
   @Rule
   public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void signInTestWithEmptyEmail() {
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.editText)).check(matches(hasErrorText("please enter your email")));
    }
    @Test
    public void signInTestWithEmptyPassword() {
        onView(withId(R.id.editText)).perform(typeText("li@gmill.com"),closeSoftKeyboard());
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.editText2)).check(matches(hasErrorText("please enter your password")));
    }
    @Test
    public void signInTest() {
        onView(withId(R.id.editText)).perform(typeText("alii@gmail.com"));
        onView(withId(R.id.editText2)).perform(typeText("mine99"),closeSoftKeyboard());
        onView(withId(R.id.button2)).perform(click());
        Intents.release();

    }

}