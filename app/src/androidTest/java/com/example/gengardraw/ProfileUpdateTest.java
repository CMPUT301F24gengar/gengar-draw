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
public class ProfileUpdateTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testProfileUpdate() {
        // Navigate to Profile Screen
        onView(withId(R.id.navbar_profile)).perform(click());

        // Update profile details
        onView(withId(R.id.profile_facility_name)).perform(typeText("Updated Facility Name"));
        onView(withId(R.id.profile_facility_description)).perform(typeText("Updated Description"));

        // Save updates
        onView(withId(R.id.profile_facility_create_btn)).perform(click());
    }
}
