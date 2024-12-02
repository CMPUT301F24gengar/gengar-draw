package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * @author Dion
 * user_profile
 * this fragment displays the user profile details to the user.
 * The user can update details such as name, email id, phone number.
 * The user can also update the profile picture or remove the profile picture.
 */

public class user_profile extends Fragment {

    private List<Integer> colors = new ArrayList<>();

    String deviceId;

    private TextView facilityButton;
    private ImageView profileImage;
    private TextView profileInitials;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private TextView removeProfilePicture;
    private TextView addProfilePicture;
    private TextView cancelButton;
    private Uri ImageURI=null;
    private String pictureURL;
    private FrameLayout saveButton;
    UserProfile userProfile = new UserProfile();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        colors.add(R.color.pfp1);
        colors.add(R.color.pfp2);
        colors.add(R.color.pfp3);
        colors.add(R.color.pfp4);
        colors.add(R.color.pfp5);
        colors.add(R.color.pfp6);

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        facilityButton = view.findViewById(R.id.profile_user_facility_btn);
        facilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacilityFragment();
            }
        });

        profileImage = view.findViewById(R.id.profile_user_picture);
        profileInitials = view.findViewById(R.id.profile_user_initials);
        nameEditText = view.findViewById(R.id.profile_user_name);
        emailEditText = view.findViewById(R.id.profile_user_email);
        phoneNumberEditText = view.findViewById(R.id.profile_user_phone);
        removeProfilePicture = view.findViewById(R.id.profile_user_picture_remove);
        addProfilePicture = view.findViewById(R.id.profile_user_picture_add);
        saveButton = view.findViewById(R.id.profile_user_save_btn);
        cancelButton = view.findViewById(R.id.profile_user_cancel_btn);

        String deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        UserProfileManager userProfileManager = new UserProfileManager();
        userProfileManager.getUserProfile(deviceID, new UserProfileManager.OnUserProfileFetchListener() {
            @Override
            public void onUserProfileFetched(UserProfile userProfileFetched) {
                setDetails(userProfileFetched);
                userProfile = userProfileFetched;
                pictureURL = userProfileFetched.getPictureURL();
            }
            @Override
            public void onUserProfileFetchError(Exception e) {
                //Handle the error
            }
        });

        removeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageURI=null;
                pictureURL = null;

                profileImage.setVisibility(View.GONE);

                int nameLength = userProfile.getName().length();
                profileInitials.setText(userProfile.getInitials());
                profileInitials.setBackgroundColor(getContext().getResources().getColor(colors.get(nameLength % 6)));
                profileInitials.setVisibility(View.VISIBLE);
            }
        });

        addProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGalleryIntent,1000); //request code is 1000 for this activity.;

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checks
                if (nameEditText.getText().toString().isEmpty() || emailEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter name and email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //updating name,email and phone number in firestore
                userProfile.setName(nameEditText.getText().toString());
                userProfile.setEmail(emailEditText.getText().toString());
                userProfile.setPhoneNumber(phoneNumberEditText.getText().toString());
                userProfileManager.updateUserProfile(userProfile,deviceID);
                //deleting profile pic if remove is pressed
                if(ImageURI==null && pictureURL == null){
                    userProfileManager.deleteProfilePicture(deviceID, new UserProfileManager.OnDeleteListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }


                //updating profile picture in firestore and storage
                if (ImageURI!=null){
                    userProfileManager.uploadProfilePicture(ImageURI, deviceID, new UserProfileManager.OnUploadPictureListener() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        return view;
    }

    /**
     * Set details such as name, email ID and phone number to that of the user's.
     * If the profile picture is set up, then the user's profile picture will also be loaded.
     * @param userProfile is used to get the details of the user.
     */
    public void setDetails(UserProfile userProfile) {
        nameEditText.setText(userProfile.getName());
        emailEditText.setText(userProfile.getEmail());
        phoneNumberEditText.setText(userProfile.getPhoneNumber());
        if(userProfile.getPictureURL()!=null){
            profileImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(userProfile.getPictureURL()).into(profileImage);

            profileInitials.setVisibility(View.GONE);
        } else {
            profileImage.setVisibility(View.GONE);

            int nameLength = userProfile.getName().length();
            profileInitials.setText(userProfile.getInitials());
            profileInitials.setBackgroundColor(getContext().getResources().getColor(colors.get(nameLength % 6)));
            profileInitials.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates the profile picture as chosen by the user.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    // setting user_picture to image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                ImageURI = data.getData();
                profileImage.setImageURI(ImageURI);

                profileImage.setVisibility(View.VISIBLE);
                profileInitials.setVisibility(View.GONE);
            }
        }
    }

    /**
     * returns the user to the homepage.
     */
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

    /**
     * Takes user to facility page
     * @throws Exception activity not instance of MainActivity
     */
    // replace current fragment with facility fragment
    private void openFacilityFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new facility_profile()).commit();
        } else {
            // Handle error
        }
    }

}