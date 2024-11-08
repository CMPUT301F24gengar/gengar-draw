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
import Fragments.user_profile;

@RunWith(AndroidJUnit4.class)
public class UserProfileFragmentTest {

    @Before
    public void setUp() {
        // Launch the user_profile fragment in a container with the app's theme
        FragmentScenario<user_profile> scenario = FragmentScenario.launchInContainer(
                user_profile.class,
                (Bundle) null,
                R.style.Theme_GengarDraw,
                (FragmentFactory) null
        );
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    /**
     * Tests the UI components' visibility in the UserProfileFragment.
     */
    @Test
    public void testUIVisibility() {
        // Check if all essential UI components are displayed
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_picture))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_name))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_email))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_phone))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_facility_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_save_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_cancel_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Tests the interaction for removing a profile picture.
     */
    @Test
    public void testRemoveProfilePictureInteraction() {
        // Simulate clicking the "Remove Profile Picture" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_picture_remove))
                .perform(ViewActions.click());

    }

    /**
     * Tests the functionality of the cancel button.
     */
    @Test
    public void testCancelButtonFunctionality() {
        // Simulate clicking the "Cancel" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_cancel_btn))
                .perform(ViewActions.click());


    }

    /**
     * Tests navigating to the facility fragment.
     */
    @Test
    public void testNavigateToFacilityFragment() {
        // Simulate clicking the "Facility" button
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_facility_btn))
                .perform(ViewActions.click());


    }

    /**
     * Tests the update functionality of the user profile.
     */
    @Test
    public void testUpdateUserProfile() {
        // Simulate entering data into the EditText fields
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_name))
                .perform(ViewActions.clearText(), ViewActions.typeText("John Smith"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_email))
                .perform(ViewActions.clearText(), ViewActions.typeText("john.smith@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_phone))
                .perform(ViewActions.clearText(), ViewActions.typeText("9876543210"), ViewActions.closeSoftKeyboard());

        // Simulate clicking the save/update button
        Espresso.onView(ViewMatchers.withId(R.id.profile_user_save_btn))
                .perform(ViewActions.click());




    }
}
