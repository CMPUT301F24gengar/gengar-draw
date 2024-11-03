package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;

import Classes.Facility;
import Classes.FacilityManager;

/**
 * "My Events" fragment handler
 */
public class my_events extends Fragment {
    //activity views
    private TextView hostedEventsBtn;
    //data
    private String deviceID;
    public my_events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        //get views
        hostedEventsBtn = (TextView) view.findViewById(R.id.my_events_hosted_btn);

        //onclick listeners
        hostedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decide which fragment to open
                FacilityManager facilityManager = new FacilityManager();
                facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
                    @Override
                    public void onFacilityExists(Facility facility) {

                    }

                    @Override
                    public void onFacilityNotExists() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });

        return view;
    }

    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

    // replace current fragment with HostedEvents fragment
    private void openHostedEventsFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new hosted_events()).commit();
        } else {
            // Handle error
        }
    }

    // replace current fragment with MissingFacility fragment
    private void openMissingFacilityFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new facility_profile()).commit();
        } else {
            // Handle error
        }
    }
}