package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gengardraw.R;

import java.util.ArrayList;

import Classes.UserProfile;
import Classes.UserProfileManager;

public class register extends Fragment {

    String deviceID;

    private ImageView userPictureImageView;
    private EditText nameEditText, emailEditText, phoneEditText;

    public interface OnRegisterSuccessListener {
        void loadMainContentFragment();
    }

    private OnRegisterSuccessListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        nameEditText = view.findViewById(R.id.register_name);
        emailEditText = view.findViewById(R.id.register_email);
        phoneEditText = view.findViewById(R.id.register_phone);
        FrameLayout registerButton = view.findViewById(R.id.register_button);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        UserProfileManager userProfileManager = new UserProfileManager();

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();

            // Validate input
            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "Please enter name and email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the UserProfile object and add to Firestore
            UserProfile userProfile = new UserProfile(
                    deviceID,
                    name,
                    email,
                    phone,
                    "default_picture_url", // Placeholder image url for now
                    "default_facility_url", // Placeholder facility url for now
                    true, // Default allowNotifications
                    new ArrayList<>(), // Default empty notificationsArray ,
                    new ArrayList<>(), // Default empty joinedEvents
                    false, // Default not an organizer
                    false  // Default not an admin
            );
             userProfileManager.addUserProfile(userProfile, deviceID);

            if (mListener != null) {
                mListener.loadMainContentFragment();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRegisterSuccessListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRegisterSuccessListener");
        }
    }
}