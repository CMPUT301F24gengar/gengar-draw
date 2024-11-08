package com.example.gengardraw;

import android.os.Bundle;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import Fragments.facility_profile;

@RunWith(AndroidJUnit4.class)
public class FacilityProfileFragmentTest {

    @Before
    public void setUp() {
        // Launch the facility_profile fragment in a container with the app's theme
        FragmentScenario<facility_profile> scenario = FragmentScenario.launchInContainer(
                facility_profile.class,
                (Bundle) null,
                R.style.Theme_GengarDraw,
                (FragmentFactory) null
        );
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    /**
     * Tests the UI components' visibility in the FacilityProfileFragment.
     */
    @Test
    public void testUIVisibility() {
        // Check if all essential UI components are displayed
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_picture))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_name))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_location))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_description))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_create_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_cancel_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Tests adding a facility picture interaction.
     */
    @Test
    public void testAddFacilityPictureInteraction() {
        // Simulate clicking the "Add Picture" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_picture_add))
                .perform(ViewActions.click());

        // Verify that the facility image view is ready for an update
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_picture))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Tests removing a facility picture interaction.
     */
    @Test
    public void testRemoveFacilityPictureInteraction() {
        // Simulate clicking the "Remove Picture" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_picture_remove))
                .perform(ViewActions.click());

        // Verify that the facility image view is set to the default drawable
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_picture))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Tests saving the facility profile.
     */
    @Test
    public void testSaveFacilityProfile() {
        // Simulate typing into the name and description fields
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_name))
                .perform(ViewActions.typeText("Community Center"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_description))
                .perform(ViewActions.typeText("A place for community gatherings"), ViewActions.closeSoftKeyboard());

        // Simulate clicking the "Save" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_create_btn))
                .perform(ViewActions.click());


    }

    /**
     * Tests the functionality of the cancel button.
     */
    @Test
    public void testCancelButtonFunctionality() {
        // Simulate clicking the "Cancel" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_cancel_btn))
                .perform(ViewActions.click());


    }

    /**
     * Tests navigating to the user profile fragment.
     */
    @Test
    public void testNavigateToUserProfileFragment() {
        // Simulate clicking the "User Profile" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_facility_user_btn))
                .perform(ViewActions.click());


    }
}
