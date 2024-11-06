package com.example.gengardraw;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserRegistrationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testUserRegistration() {
        // Navigate to the registration screen
        onView(withId(R.id.register)).perform(click());

        // Fill in registration details
        onView(withId(R.id.register_name)).perform(typeText("John Doe"));
        onView(withId(R.id.register_email)).perform(typeText("john.doe@example.com"));
        onView(withId(R.id.register_phone)).perform(typeText("1234567890"));

        // Submit registration
        onView(withId(R.id.register_button)).perform(click());
    }
}
