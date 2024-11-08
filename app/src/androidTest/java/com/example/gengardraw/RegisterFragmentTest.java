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
import Fragments.register;

@RunWith(AndroidJUnit4.class)
public class RegisterFragmentTest {

    @Before
    public void setUp() {
        // Launch the RegisterFragment in a container with the app's theme
        FragmentScenario<register> scenario = FragmentScenario.launchInContainer(
                register.class,
                (Bundle) null,
                R.style.Theme_GengarDraw,
                (FragmentFactory) null
        );
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    /**
     * Tests the UI components' visibility in the RegisterFragment.
     */
    @Test
    public void testUIVisibility() {
        // Check if all essential UI components are displayed
        Espresso.onView(ViewMatchers.withId(R.id.register_user_picture))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.register_name))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.register_email))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.register_phone))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.register_button))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Simulates user interaction for filling out the registration form.
     */
    @Test
    public void testUserInteraction() {
        // Type into the EditText fields
        Espresso.onView(ViewMatchers.withId(R.id.register_name))
                .perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.register_email))
                .perform(ViewActions.typeText("john.doe@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.register_phone))
                .perform(ViewActions.typeText("1234567890"), ViewActions.closeSoftKeyboard());

        // Click the register button
        Espresso.onView(ViewMatchers.withId(R.id.register_button))
                .perform(ViewActions.click());
    }
}