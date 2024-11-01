package Fragments;

import android.content.Intent;
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

import java.util.List;

import Classes.Event;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link facility_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class facility_profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment facility_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static facility_profile newInstance(String param1, String param2) {
        facility_profile fragment = new facility_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //data
    //private UserProfile user;
    String deviceID;
    private ImageView facilityImage;
    private EditText nameEditText, locationEditText, descriptionEditText;
    private TextView createUpdateBtn, cancelBtn;
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

        facilityImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            Facility facility = new Facility(name, location, description, pictureURL, events, deviceID);
            facilityManager.addUpdateFacility(facility, deviceID);
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