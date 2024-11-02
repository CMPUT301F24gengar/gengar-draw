package com.example.gengardraw;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import Classes.UserProfile;
import Classes.UserProfileManager;
import Fragments.create_event;
import Fragments.facility_profile;
import Fragments.notifications;
import Fragments.qr_scanner;
import Fragments.register;
import Fragments.user_profile;

public class MainActivity extends AppCompatActivity implements register.OnRegisterSuccessListener {
    String deviceID;

    FrameLayout mainContentFrame;
    FrameLayout registerFrame;

    ImageView highlightedButton;
    ImageView navbarNotificationsbutton;
    ImageView navbarMyEventsbutton;
    ImageView navbarScannerbutton;
    ImageView navbarCreateEventbutton;
    ImageView navbarProfilebutton;

    ImageView highlightedAdminButton;
    boolean isExpanded = false;
    FrameLayout adminLayout;
    ImageView adminButton;
    LinearLayout adminButtonsExpanded;
    ImageView adminProfileButton;
    ImageView adminEventsButton;
    ImageView adminImagesButton;
    ImageView adminFacilitiesButton;

    UserProfileManager userProfileManager;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContentFrame = findViewById(R.id.main_content);
        registerFrame = findViewById(R.id.register);

        navbarNotificationsbutton = findViewById(R.id.navbar_notifications);
        navbarMyEventsbutton = findViewById(R.id.navbar_my_events);
        navbarScannerbutton = findViewById(R.id.navbar_scanner);
        navbarCreateEventbutton = findViewById(R.id.navbar_create_event);
        navbarProfilebutton = findViewById(R.id.navbar_profile);
        highlightedButton = navbarNotificationsbutton;

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        userProfileManager = new UserProfileManager();
        userProfileManager.checkUserExists(deviceID, new UserProfileManager.OnUserCheckListener() {
            @Override
            public void onUserExists(UserProfile userProfile) {
                loadMainContentFragment(userProfile);
            }

            @Override
            public void onUserNotExists() {
                loadRegisterFragment();
            }

            @Override
            public void onError(Exception e) {
                Log.e("MainActivity", "Error checking user", e);
            }
        });

        navbarNotificationsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHomeFragment();
            }
        });
        navbarMyEventsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarMyEventsbutton);
            }
        });
        navbarScannerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarScannerbutton);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new qr_scanner()).commit();
            }
        });
        navbarCreateEventbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarCreateEventbutton);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new create_event()).commit();
            }
        });
        navbarProfilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarProfilebutton);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new user_profile()).commit();
            }
        });

        adminLayout = findViewById(R.id.admin_layout);
        adminButton = findViewById(R.id.admin_button);
        adminButtonsExpanded = findViewById(R.id.admin_buttons_expanded);
        adminProfileButton = findViewById(R.id.admin_profile_button);
        adminEventsButton = findViewById(R.id.admin_events_button);
        adminImagesButton = findViewById(R.id.admin_images_button);
        adminFacilitiesButton = findViewById(R.id.admin_facilities_button);

        highlightedAdminButton = adminButton;

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) { // then expand the buttons panel
                    setHighlightedAdminButton(adminButton, isExpanded);
                    adminButtonsExpanded.setVisibility(View.VISIBLE);
                    isExpanded = true;
                } else {
                    setHighlightedAdminButton(adminButton, isExpanded);
                    adminButtonsExpanded.setVisibility(View.GONE);
                    isExpanded = false;
                }
            }
        });

        adminProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminProfileButton, isExpanded);
            }
        });
        adminEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminEventsButton, isExpanded);
            }
        });
        adminImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminImagesButton, isExpanded);
            }
        });
        adminFacilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminFacilitiesButton, isExpanded);
            }
        });

    }

    private void setHighlightedButton(ImageView button) {
        highlightedButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
        highlightedButton = button;
        highlightedButton.setColorFilter(ContextCompat.getColor(this, R.color.black2));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
    }

    private void setHighlightedAdminButton(ImageView button, boolean isExpanded) {
        if (!isExpanded) { // make admin button unhighlighted
            highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
            highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
            highlightedAdminButton = button;
            highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
            highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
        } else {
            highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
            highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
            highlightedAdminButton = button;
            highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.black2));
            highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
        }
    }

    private void loadRegisterFragment() {
        // Replace the register FrameLayout with the register fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.register, new register()).commit();
    }

    @Override
    public void loadMainContentFragment(UserProfile userProfile) {
        // TODO : Replace the main_content FrameLayout with the main content and set the register FrameLayout visibility to false
        registerFrame.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new notifications()).commit();
        this.userProfile = userProfile;
        if (userProfile.isAdmin()) {
            adminLayout.setVisibility(View.VISIBLE);
        }
    }

    public void showHomeFragment() {
        setHighlightedButton(navbarNotificationsbutton);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new notifications()).commit();
    }



}