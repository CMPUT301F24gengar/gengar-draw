package com.example.gengardraw;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FacilityCreationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testCreateFacility() {
        // Navigate to Facility Profile
        onView(withId(R.id.profile_facility_user_btn)).perform(click());

        // Enter facility details
        onView(withId(R.id.profile_facility_name)).perform(typeText("Community Center"));
        onView(withId(R.id.profile_facility_location)).perform(typeText("123 Main Street"));
        onView(withId(R.id.profile_facility_description)).perform(typeText("A facility for community events"));

        // Submit the creation
        onView(withId(R.id.profile_facility_create_btn)).perform(click());
    }
}
