package com.example.gengardraw;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
                Log.d("MainActivity", "User exists : " + userProfile.getName());
                loadMainContentFragment();
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
                // for testing facility
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new facility_profile()).commit();
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

    private void loadRegisterFragment() {
        // Replace the register FrameLayout with the register fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.register, new register()).commit();
    }

    @Override
    public void loadMainContentFragment() {
        // TODO : Replace the main_content FrameLayout with the main content and set the register FrameLayout visibility to false
        registerFrame.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new notifications()).commit();
    }

    public void showHomeFragment() {
        setHighlightedButton(navbarNotificationsbutton);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new notifications()).commit();
    }



}