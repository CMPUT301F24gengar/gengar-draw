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
import Fragments.admin_events;
import Fragments.admin_facility_profiles;
import Fragments.admin_images;
import Fragments.admin_qrcodes;
import Fragments.admin_user_profiles;
import Fragments.create_event;
import Fragments.facility_profile;
import Fragments.my_events;
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
    ImageView adminUserProfileButton;
    ImageView adminEventsButton;
    ImageView adminImagesButton;
    ImageView adminFacilitiesButton;
    ImageView adminQRcodesButton;

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
                setHighlightedButton(navbarMyEventsbutton, false);
                setHighlightedAdminButton(adminButton, isExpanded, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new my_events()).commit();
            }
        });
        navbarScannerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarScannerbutton, false);
                setHighlightedAdminButton(adminButton, isExpanded, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new qr_scanner()).commit();
            }
        });
        navbarCreateEventbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarCreateEventbutton,false);
                setHighlightedAdminButton(adminButton, isExpanded, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new create_event()).commit();
            }
        });
        navbarProfilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedButton(navbarProfilebutton,false);
                setHighlightedAdminButton(adminButton, isExpanded, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new user_profile()).commit();
            }
        });

        adminLayout = findViewById(R.id.admin_layout);
        adminButton = findViewById(R.id.admin_button);
        adminButtonsExpanded = findViewById(R.id.admin_buttons_expanded);
        adminUserProfileButton = findViewById(R.id.admin_profile_button);
        adminEventsButton = findViewById(R.id.admin_events_button);
        adminImagesButton = findViewById(R.id.admin_images_button);
        adminFacilitiesButton = findViewById(R.id.admin_facilities_button);
        adminQRcodesButton = findViewById(R.id.admin_qrcode_button);

        highlightedAdminButton = adminButton;

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) { // then expand the buttons panel
                    setHighlightedAdminButton(adminButton, isExpanded, false);
                    adminButtonsExpanded.setVisibility(View.VISIBLE);
                    isExpanded = true;
                } else {
                    setHighlightedAdminButton(adminButton, isExpanded, false);
                    adminButtonsExpanded.setVisibility(View.GONE);
                    isExpanded = false;
                }
            }
        });

        adminUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminUserProfileButton, isExpanded, false);
                setHighlightedButton(highlightedButton, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new admin_user_profiles()).commit();
            }
        });
        adminEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminEventsButton, isExpanded, false);
                setHighlightedButton(highlightedButton, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new admin_events()).commit();
            }
        });
        adminImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminImagesButton, isExpanded, false);
                setHighlightedButton(highlightedButton, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new admin_images()).commit();
            }
        });
        adminFacilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminFacilitiesButton, isExpanded, false);
                setHighlightedButton(highlightedButton, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new admin_facility_profiles()).commit();
            }
        });
        adminQRcodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHighlightedAdminButton(adminQRcodesButton, isExpanded, false);
                setHighlightedButton(highlightedButton, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new admin_qrcodes()).commit();
            }
        });
    }

    private void setHighlightedButton(ImageView button, boolean unhighlight) {
        highlightedButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
        if (!unhighlight) { // set the button to highlighted
            highlightedButton = button;
            highlightedButton.setColorFilter(ContextCompat.getColor(this, R.color.black2));
            highlightedButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
        }
    }

    private void setHighlightedAdminButton(ImageView button, boolean isExpanded, boolean unhighlight) {
        highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
        highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
        highlightedAdminButton = button;
        if (!isExpanded) { // make admin button unhighlighted
            if (unhighlight) {
                highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.black2));
                highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                return;
            }
            highlightedAdminButton.setColorFilter(ContextCompat.getColor(this, R.color.grey));
            highlightedAdminButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black2)));
        } else {
            if (unhighlight) {
                return;
            }
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
        registerFrame.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new notifications()).commit();
        this.userProfile = userProfile;
        if (userProfile.isAdmin()) {
            adminLayout.setVisibility(View.VISIBLE);
        }
    }

    public void showHomeFragment() {
        setHighlightedButton(navbarNotificationsbutton, false);
        setHighlightedAdminButton(adminButton, isExpanded, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new notifications()).commit();
    }



}