package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;

/**
 * "My Events" fragment handler
 */
public class my_events extends Fragment {
    //activity views
    private TextView hostedEventsBtn;
    //data
    public my_events() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        //get views
        hostedEventsBtn = (TextView) view.findViewById(R.id.my_events_hosted_btn);

        //onclick listeners
        hostedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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