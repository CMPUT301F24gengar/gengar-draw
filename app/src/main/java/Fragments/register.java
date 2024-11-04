package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
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
import android.widget.Toast;

import com.example.gengardraw.R;

import java.util.ArrayList;

import Classes.UserProfile;
import Classes.UserProfileManager;

public class register extends Fragment {

    String deviceID;

    private ImageView userPictureImageView;
    private EditText nameEditText, emailEditText, phoneEditText;
    private String profile_image_uri = null; //by default, uri is null until you upload a profile picture
    private Uri ImageURI=null;

    public interface OnRegisterSuccessListener {
        void loadMainContentFragment(UserProfile userProfile);
    }

    private OnRegisterSuccessListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        userPictureImageView = view.findViewById(R.id.register_user_picture);
        nameEditText = view.findViewById(R.id.register_name);
        emailEditText = view.findViewById(R.id.register_email);
        phoneEditText = view.findViewById(R.id.register_phone);
        FrameLayout registerButton = view.findViewById(R.id.register_button);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        UserProfileManager userProfileManager = new UserProfileManager();

        //Selecting a image for the register user picture
        userPictureImageView.setOnClickListener( v ->{
            Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent,1000); //request code is 1000 for this activity.

        });

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
                    profile_image_uri,
                    null, // Default URL
                    true, // Default allowNotifications
                    new ArrayList<>(), // Default empty notificationsArray ,
                    new ArrayList<>(), // Default empty joinedEvents
                    false, // Default not an organizer
                    false  // Default not an admin
            );
            userProfileManager.addUserProfile(userProfile, deviceID);
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


            if (mListener != null) {
                mListener.loadMainContentFragment(userProfile);
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

    // setting register_user_picture to image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                ImageURI = data.getData();
                profile_image_uri = ImageURI.toString(); // converting to string to upload to firebase in the register button function
                userPictureImageView.setImageURI(ImageURI);
                userPictureImageView.setImageTintList(null);
                //uploading to firebase is done in register button.
            }
        }
    }

}