package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.List;

import Classes.Event;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * facility profile fragment
 */
public class facility_profile extends Fragment {
    //data
    String deviceID;
    private static final int PICK_IMAGE = 1000;//request code for image gallery
    private Uri ImageURI = null;
    private String ImageURI_string;

    //view references;
    private ImageView facilityImage;
    private EditText nameEditText, locationEditText, descriptionEditText;
    private TextView createUpdateBtn, cancelBtn, addFacilityImage, removeFacilityImage;
    private List<Event> events;
    //private FrameLayout createUpdateFrameLayout;

    public facility_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_facility_profile, container, false);

        facilityImage = (ImageView) view.findViewById(R.id.profile_facility_picture);
        addFacilityImage = (TextView) view.findViewById(R.id.profile_facility_picture_add);
        removeFacilityImage = (TextView) view.findViewById(R.id.profile_facility_picture_remove);
        nameEditText = (EditText) view.findViewById(R.id.profile_facility_name);
        locationEditText = (EditText) view.findViewById(R.id.profile_facility_location);
        descriptionEditText = (EditText) view.findViewById(R.id.profile_facility_description);
        createUpdateBtn = (TextView) view.findViewById(R.id.profile_facility_create_btn);
        cancelBtn = (TextView) view.findViewById(R.id.profile_facility_cancel_btn);


        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        FacilityManager facilityManager = new FacilityManager();

        facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
            @Override
            public void onFacilityExists(Facility facility) {
                createUpdateBtn.setText("UPDATE");
                nameEditText.setText(facility.getName());
                locationEditText.setText(facility.getLocation());
                descriptionEditText.setText(facility.getDescription());
                events = facility.getEvents();  // TODO : test events list is correct
            }

            @Override
            public void onFacilityNotExists() {
                createUpdateBtn.setText("CREATE");
            }

            @Override
            public void onError(Exception e) {
                Log.e("facility_profile", "Error checking facility", e);
            }
        });

        addFacilityImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGalleryIntent, PICK_IMAGE);
            }
        });


        createUpdateBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            boolean checkValidInput = (name.isEmpty() || location.isEmpty() || description.isEmpty());

            if (checkValidInput) {
                return;
            }
            //delete image (if deleted)
            if (ImageURI == null){
                facilityManager.deleteFacilityImage(deviceID, new FacilityManager.OnDeleteListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            // update image to database
            if (ImageURI!=null){
                Facility facility = new Facility(name, location, description, ImageURI_string, events, deviceID);
                facilityManager.uploadFacilityImage(ImageURI, deviceID, new FacilityManager.OnUploadPictureListener() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            closeFragment();
        });

        cancelBtn.setOnClickListener(v -> {
            closeFragment();
        });

        return view;
    }
    // setting user_picture to image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1000){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                ImageURI = data.getData();
                ImageURI_string = ImageURI.toString(); // converting to string to upload to firebase
                facilityImage.setImageURI(ImageURI);
                facilityImage.setImageTintList(null);
            }
        }
    }
    // Go back to the home screen
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }
}