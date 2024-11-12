package Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodSession;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.transform.Result;

import Adapters.EventAdapter;
import Classes.Event;
import Classes.EventLists;
import Classes.EventManager;
import Classes.Facility;
import Classes.FacilityManager;
//import kotlinx.coroutines.EventLoop;
import Adapters.UserProfileAdapter;

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
    private List<String> eventIDs;

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private EventManager eventManager;
    private ArrayList<Event> events;
    private ArrayList<Event> events2;
    private RecyclerView.LayoutManager layoutManager;
    EventAdapter customAdapter;

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

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.my_events_list);
        eventManager = new EventManager();
        events = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new EventAdapter(getContext(), events, false);

        recyclerView.setLayoutManager(layoutManager); //arranges recyclerView in linear form
        recyclerView.setAdapter(customAdapter);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("onCreateView ", "Events size: " + events.size());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                // Empty out the events ArrayList to hold the Event objects
                events.clear();
                customAdapter.notifyDataSetChanged();
                Log.d("fetchEvents", "events.clear size: " + events.size());

                setHighlightedButton(hostedEventsBtn);
                eventListView.setVisibility(View.VISIBLE);
                missingFacilityFrame.setVisibility(View.INVISIBLE);
                checkForFacility();
            }
        });
        joinedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Empty out the events ArrayList to hold the Event objects
                events.clear();
                customAdapter.notifyDataSetChanged();
                Log.d("fetchEvents", "events.clear size: " + events.size());

                //switch views
                setHighlightedButton(joinedEventsBtn);
                missingFacilityFrame.setVisibility(View.GONE);
                //eventListView.setVisibility(View.INVISIBLE);  // TEMPORARY: TO SEE "HOSTED LIST"!!!
                eventListView.setVisibility(View.VISIBLE); // TEMPORARY: TO SEE "HOSTED LIST"!!!

                Log.d("JOIN FacilityExists", "eventListView VISIBLE");
                Log.d("JOIN VisibilityCheck", "RecyclerView visibility: " + (eventListView.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
                Log.d("JOIN RecyclerView", "Adapter set to RecyclerView: " + (recyclerView.getAdapter() != null));
                Log.d("JOIN RecyclerView", "LayoutManager set: " + (recyclerView.getLayoutManager() != null));
            }
        });
        createFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacilityFragment();
            }
        });
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
public interface OnEventsLoadedListener {
    void onEventsLoaded(ArrayList<Event> events);
}

public void checkForFacility() {
    //decide which fragment to open
    FacilityManager facilityManager = new FacilityManager();
    facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
        @Override
        public void onFacilityExists(Facility facility) {
            missingFacilityFrame.setVisibility(View.GONE);
            eventIDs = facility.getEvents();

            fetchEvents(eventIDs, new FetchEventsCallback() {
                @Override
                public void onSuccess(ArrayList<Event> curr_events) {
                    Log.d("RecyclerView ", "AFTER fetchEvents curr_events size: " + curr_events.size());
                    Log.d("HOST FacilityExists", "eventListView VISIBLE");
                    Log.d("HOST VisibilityCheck", "RecyclerView visibility: " + (eventListView.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
                    Log.d("HOST RecyclerView", "Adapter set to RecyclerView: " + (recyclerView.getAdapter() != null));
                    Log.d("HOST RecyclerView", "LayoutManager set: " + (recyclerView.getLayoutManager() != null));
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("checkForFacility", "Error in fetching events", e);
                }
            });
        }


        @Override
        public void onFacilityNotExists() {
            //missing facility layout
            missingFacilityFrame.setVisibility(View.VISIBLE);
            eventListView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(Exception e) {
            Log.e("my events", "Error checking facility exists", e);
        }
    });
}


// Creates listener since firebase's get() is asynchronous in nature,
// so it notifies when all events have been loaded.
public void fetchEvents(List<String> eventIDs, final FetchEventsCallback callback) {

    // Empty out the events ArrayList to hold the Event objects
//    events.clear();
//    customAdapter.notifyDataSetChanged();
////        customAdapter.updateAdapter(events);
//    Log.d("fetchEvents", "events.clear size: " + events.size());

    // Track how many events have been fetched
    int totalEvents = eventIDs.size();
    Log.d("fetchEvents", "totalEvents: " + totalEvents);
    // Thread-safe  way to increment an integer
    AtomicInteger completedTasks = new AtomicInteger(0);

    // Loop through each eventID in the eventIDs list
    for (String eventID : eventIDs) {

        // Fetch the specific event document by its ID
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        // If document exists, fetch event using event manager
                        DocumentSnapshot document = task.getResult();

                        if (document != null && document.exists()) {
                            String currentDocumentID = document.getId();

                            eventManager.getEvent(currentDocumentID, new EventManager.OnEventFetchListener() {
                                @Override
                                public void onEventFetched(Event curr_event) {
                                    Log.d("onEventFetched", "Event title : " + curr_event.getEventTitle());
                                    events.add(curr_event);   // Add the retrieved Event object to the List

                                    // Notify adapter that event was added
                                    customAdapter.notifyItemInserted(events.size() - 1);

                                    if (completedTasks.incrementAndGet() == totalEvents) { // All events have been fetched
                                        callback.onSuccess(events); // Pass list to the callback
                                    }
                                }

                                @Override
                                public void onEventFetchError(Exception e) {
                                    Log.e("fetchEvents", "Failed to fetch Event for ID: " + currentDocumentID + e);

                                    if (completedTasks.incrementAndGet() == totalEvents) {
                                        callback.onSuccess(events); // Pass whatever was retrieved so far
                                    }
                                }
                            });
                        } else {
                            Log.e("fetchEvents", "Document does not exist for eventID: " + eventID);

                            if (completedTasks.incrementAndGet() == totalEvents) {
                                callback.onSuccess(events); // Pass whatever was retrieved so far
                            }
                        }
                    } else {

                        Log.e("fetchEvents", "Failed to fetch document for eventID: " + eventID, task.getException());

                        if (completedTasks.incrementAndGet() == totalEvents) {
                            callback.onSuccess(events); // Pass whatever was retrieved so far
                        }
                    }
                });
    }
}


    public interface FetchEventsCallback {
        void onSuccess(ArrayList<Event> events);
        void onFailure(Exception e);
    }
}