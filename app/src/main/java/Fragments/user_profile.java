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

import Classes.UserProfile;
import Classes.UserProfileManager;


public class user_profile extends Fragment {

    String deviceId;

    private TextView facilityButton;
    private ImageView profileImage;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private TextView removeProfilePicture;
    private TextView addProfilePicture;
    private TextView cancelButton;
    private String profile_image_uri = null; //by default, uri is null until you add a profile picture
    private Uri ImageURI=null;
    private FrameLayout saveButton;
    UserProfile userProfile = new UserProfile();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        facilityButton = view.findViewById(R.id.profile_user_facility_btn);
        facilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacilityFragment();
            }
        });

        profileImage = view.findViewById(R.id.profile_user_picture);
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
                userProfile=userProfileFetched;

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
                profileImage.setImageDrawable(getResources().getDrawable(R.drawable.user));
                profileImage.setImageTintList(getResources().getColorStateList(R.color.grey));

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
                if(ImageURI==null){
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
                            userProfileManager.updateProfilePictureInFireStore(deviceID, ImageURI.toString(), new UserProfileManager.OnUpdateListener() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

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

    public void setDetails(UserProfile userProfile) {
        nameEditText.setText(userProfile.getName());
        emailEditText.setText(userProfile.getEmail());
        phoneNumberEditText.setText(userProfile.getPhoneNumber());
        if(userProfile.getPictureURL()!=null){
            Glide.with(this).load(userProfile.getPictureURL()).into(profileImage);
            profileImage.setImageTintList(null);
        }

        Log.d("userprof","userprof: "+ userProfile.getPictureURL());

    }

    // setting user_picture to image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                ImageURI = data.getData();
                profile_image_uri = ImageURI.toString(); // converting to string to upload to firebase
                profileImage.setImageURI(ImageURI);
                profileImage.setImageTintList(null);
            }
        }
    }

    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

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