package Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;

import org.w3c.dom.Text;

import Classes.Facility;
import Classes.FacilityManager;

/**
 * <h1>My Events Activity</h1>
 * <p>
 *     handles interactions with the 'my events' fragment
 *     <ul>data: <li>activity views</li> <li>device Id</li> <li>highlightedButton</li></ul>
 *     <ul>methods: <li>constructor</li> <li>onCreateView</li> <li>closeFragment</li> <li>setHighlightedButton</li> <li>openFacilityFragment</li></ul>
 * </p>
 * @author Meghan
 */
public class my_events extends Fragment {
    //activity views
    private TextView hostedEventsBtn;
    private TextView joinedEventsBtn;
    private TextView createFacilityBtn;
    private FrameLayout missingFacilityFrame;
    private View eventListView;
    //data
    private String deviceID;
    TextView highlightedButton;

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
        joinedEventsBtn = (TextView) view.findViewById(R.id.my_events_joined_btn);
        createFacilityBtn = (TextView) view.findViewById(R.id.my_events_facility_create_btn);
        missingFacilityFrame = (FrameLayout) view.findViewById(R.id.missing_facility);
        eventListView = view.findViewById(R.id.my_events_list);

        //set highlighted button
        highlightedButton = joinedEventsBtn;

        //onclick listeners
        hostedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //decide which fragment to open
                FacilityManager facilityManager = new FacilityManager();
                facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
                    @Override
                    public void onFacilityExists(Facility facility) {
                        //list layout
                        setHighlightedButton(hostedEventsBtn);
                    }

                    @Override
                    public void onFacilityNotExists() {
                        //missing facility layout
                        missingFacilityFrame.setVisibility(View.VISIBLE);
                        eventListView.setVisibility(View.INVISIBLE);
                        setHighlightedButton(hostedEventsBtn);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("my events","Error checking facility exists", e);
                    }
                });
            }
        });
        joinedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch views
                setHighlightedButton(joinedEventsBtn);
                missingFacilityFrame.setVisibility(View.GONE);
                eventListView.setVisibility(View.VISIBLE);
            }
        });
        createFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacilityFragment();
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

    private void setHighlightedButton(TextView button) {
        highlightedButton.setTextColor(getResources().getColor(R.color.grey));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black2)));
        highlightedButton = button;
        highlightedButton.setTextColor(getResources().getColor(R.color.black1));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
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