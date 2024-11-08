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
 * My Events Activity
 *
 *     handles interactions with the 'my events' fragment
 *     data:<ul> <li>activity views</li> <li>device Id</li> <li>highlightedButton</li></ul>
 *     methods:<ul> <li>constructor</li> <li>onCreateView</li> <li>closeFragment</li> <li>setHighlightedButton</li> <li>openFacilityFragment</li></ul>
 *
 * @author Meghan
 * @see Fragment
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

    /**
     * Required empty public constructor
     */
    public my_events() {
        // Required empty public constructor
    }

    /**
     * Construct the my_events fragment view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return constructed View
     * @see Fragment
     */
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

    /**
     * returns user to the homepage
     * @throws Exception activity not instance of MainActivity
     * @see MainActivity
     */
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

    /**
     * switches which button is highlighted
     * @param button    TextView representing a button
     */
    private void setHighlightedButton(TextView button) {
        highlightedButton.setTextColor(getResources().getColor(R.color.grey));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black2)));
        highlightedButton = button;
        highlightedButton.setTextColor(getResources().getColor(R.color.black1));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
    }

    /**
     * replaces the current fragment with the facility fragment
     * @throws Exception activity not instance of MainActivity
     * @see facility_profile
     */
    private void openFacilityFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new facility_profile()).commit();
        } else {
            // Handle error
        }
    }
}