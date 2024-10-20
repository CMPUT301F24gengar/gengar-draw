package com.example.gengardraw;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import Classes.UserProfile;
import Classes.UserProfileManager;
import Fragments.notifications;
import Fragments.register;

public class MainActivity extends AppCompatActivity implements register.OnRegisterSuccessListener {
    String deviceID;

    FrameLayout mainContentFrame;
    FrameLayout registerFrame;

    UserProfileManager userProfileManager;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContentFrame = findViewById(R.id.main_content);
        registerFrame = findViewById(R.id.register);

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
}