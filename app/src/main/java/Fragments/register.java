package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import Classes.Event;
import Classes.EventManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * Register Fragment
 *
 *     Registers a new user and handles interactions with the register page fragment
 *
 * @author Rehan, Dion
 * @see UserProfile
 * @see UserProfileManager
 */
public class register extends Fragment {

    String deviceID;

    private ImageView userPictureImageView;
    private EditText nameEditText, emailEditText, phoneEditText;
    private String profile_image_uri = null; // by default, uri is null until you upload a profile picture
    private Uri ImageURI = null;

    /**
     * Interface for handling the registration success
     */
    public interface OnRegisterSuccessListener {
        void loadMainContentFragment(UserProfile userProfile);
    }

    private OnRegisterSuccessListener mListener;

    /**
     * Construct the register fragment view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Constructed View
     * @see Fragment
     */
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

        // Selecting an image for the register user picture
        userPictureImageView.setOnClickListener(v -> {
            Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent, 1000); // request code is 1000 for this activity.
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
                    new ArrayList<>(), // Default empty notificationsArray
                    new ArrayList<>(), // Default empty joinedEvents
                    false, // Default not an organizer
                    false  // Default not an admin
            );
            userProfileManager.addUserProfile(userProfile, deviceID);
            if (ImageURI != null) {
                userProfileManager.uploadProfilePicture(ImageURI, deviceID, new UserProfileManager.OnUploadPictureListener() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }

            // Safely call the listener if it's not null
            if (mListener != null) {
                mListener.loadMainContentFragment(userProfile);
            }
        });

        return view;
    }

    /**
     * Called when a fragment is first attached to its context.
     * @param context
     * @throws ClassCastException
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterSuccessListener) {
            mListener = (OnRegisterSuccessListener) context;
        } else {
            // Log a warning and handle the scenario gracefully for testing
            Log.w("RegisterFragment", "Parent activity does not implement OnRegisterSuccessListener. This is expected during testing.");
            mListener = null;
        }
    }

    /**
     * Called when a user chooses an image from the gallery.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                ImageURI = data.getData();
                profile_image_uri = ImageURI.toString(); // Converting to string to upload to firebase in the register button function
                userPictureImageView.setImageURI(ImageURI);
                userPictureImageView.setImageTintList(null);
                // Uploading to firebase is done in register button.
            }
        }
    }
}