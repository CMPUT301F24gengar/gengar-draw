package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Classes.Event;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;


public class facility_profile extends Fragment {

    String deviceID;

    private ImageView facilityImage;
    private EditText nameEditText, locationEditText, descriptionEditText;
    private TextView createUpdateBtn;
    private TextView cancelBtn;
    Facility facilityProfile;

    public facility_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_facility_profile, container, false);

        facilityImage = view.findViewById(R.id.profile_facility_picture);
        nameEditText = view.findViewById(R.id.profile_facility_name);
        locationEditText = view.findViewById(R.id.profile_facility_location);
        descriptionEditText = view.findViewById(R.id.profile_facility_description);
        createUpdateBtn = view.findViewById(R.id.profile_facility_create_btn);
        cancelBtn = view.findViewById(R.id.profile_facility_cancel_btn);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        FacilityManager facilityManager = new FacilityManager();

        facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
            @Override
            public void onFacilityExists(Facility facility) {
                facilityProfile = facility;
                createUpdateBtn.setText("UPDATE");
                nameEditText.setText(facilityProfile.getName());
                locationEditText.setText(facilityProfile.getLocation());
                descriptionEditText.setText(facilityProfile.getDescription());
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


        createUpdateBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            boolean checkValidInput = (name.isEmpty() || location.isEmpty() || description.isEmpty());

            if (checkValidInput) {
                return;
            }

            // TODO : get actual picture urls
            String pictureURL = "picUrlTest";

            if (facilityProfile != null) {
                Log.e("facility_profile", "exists");
                facilityProfile.setName(name);
                facilityProfile.setLocation(location);
                facilityProfile.setDescription(description);
                facilityProfile.setPictureURL(pictureURL);
                Log.e("facility_profile", "fac:" + facilityProfile.toString());

                facilityManager.updateFacility(facilityProfile);
                Toast.makeText(getContext(), "Facility updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                facilityProfile = new Facility(name, location, description, pictureURL, new ArrayList<>(), deviceID);
                // update the users facilityURL
                UserProfileManager userProfileManager = new UserProfileManager();
                userProfileManager.getUserProfile(deviceID , new UserProfileManager.OnUserProfileFetchListener() {
                    @Override
                    public void onUserProfileFetched(UserProfile userProfile) {
                        userProfile.setFacilityURL(deviceID);
                        userProfile.setOrganizer(true);
                        userProfileManager.updateUserProfile(userProfile, deviceID);
                        }
                    @Override
                    public void onUserProfileFetchError(Exception e) {
                        Log.e("facility_profile", "Error fetching user profile", e);
                    }
                });

                facilityManager.addFacility(facilityProfile);
                Toast.makeText(getContext(), "Facility created successfully", Toast.LENGTH_SHORT).show();
            }
            closeFragment();
        });

        cancelBtn.setOnClickListener(v -> {
            closeFragment();
        });

        return view;
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