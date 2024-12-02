package com.example.gengardraw;

import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;
import android.os.Bundle;

import Fragments.my_events;

@RunWith(AndroidJUnit4.class)
public class myEventsFragmentTest {

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS);

    @Before
    public void setUp() {
        FragmentScenario<my_events> scenario = FragmentScenario.launchInContainer(
                my_events.class,
                (Bundle) null,
                R.style.Theme_GengarDraw,
                (FragmentFactory) null
        );
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    @Test
    public void testHostedEventsButtonHighlight() {
        onView(withId(R.id.my_events_hosted_btn)).perform(click());
        onView(withId(R.id.my_events_hosted_btn))
                .check(ViewAssertions.matches(ViewMatchers.hasTextColor(R.color.black1)));
        onView(withId(R.id.my_events_joined_btn))
                .check(ViewAssertions.matches(ViewMatchers.hasTextColor(R.color.grey)));
    }

    @Test
    public void testJoinedEventsButtonHighlight() {
        onView(withId(R.id.my_events_joined_btn)).perform(click());
        onView(withId(R.id.my_events_joined_btn))
                .check(ViewAssertions.matches(ViewMatchers.hasTextColor(R.color.black1)));
        onView(withId(R.id.my_events_hosted_btn))
                .check(ViewAssertions.matches(ViewMatchers.hasTextColor(R.color.grey)));
    }
}


