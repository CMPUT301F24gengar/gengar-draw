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

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private boolean facilityExists;
    private FrameLayout createUpdateFrameLayout;
    private TextView createUpdateText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //data
    private UserProfile user;

    private ImageView facilityImage;
    private EditText nameEditText, locationEditText, descriptionEditText;

    public facility_profile() {
        // Required empty public constructor
    }

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacilityManager facilityManager = new FacilityManager();

        // TODO: pass actual device ID as parameter
        facilityManager.checkFacilityExists("deviceIdTest", new FacilityManager.OnFacilityCheckListener() {
            @Override
            public void onFacilityExists(Facility facility) {
                facilityExists = true;
            }

            @Override
            public void onFacilityNotExists() {
                facilityExists = false;
            }

            @Override
            public void onError(Exception e) {
                Log.e("facility_profile", "Error checking facility", e);
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_facility_profile, container, false);

        // change button text based on if facility exists
        createUpdateFrameLayout = view.findViewById(R.id.profile_facility_save_btn);
        createUpdateText = (TextView) createUpdateFrameLayout.getChildAt(0);
        if (facilityExists) {
            createUpdateText.setText("UPDATE");
        } else {
            createUpdateText.setText("CREATE");
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FacilityManager facilityManager = new FacilityManager();
        // TODO: get actual facility info
        Facility facility = new Facility("nameTest", "locationTest", "descriptionTest", "URLTest", "idTest");
        createUpdateFrameLayout.setOnClickListener(v -> {
            facilityManager.addUpdateFacility(facility, "idTest");
        });
    }

}