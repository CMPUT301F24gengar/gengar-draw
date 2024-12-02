package com.example.gengardraw;

import android.content.Intent;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Fragments.qr_scanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class QRScannerFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Launch the QR Scanner Fragment
        activityRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, new qr_scanner())
                .commit();
    }

    @Test
    public void testQRScannerHandlesInjectedQRCode() {
        // Simulate the scanning process with a mocked QR code string
        String mockedQRCode = "eriS7rD5NLCDetMVYvJS";

        // Inject the QR code string into the fragment's scanner
        qr_scanner fragment = (qr_scanner) activityRule.getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.main_content);
        if (fragment != null) {
            fragment.handleScannedData(mockedQRCode);
        }

    }

}
