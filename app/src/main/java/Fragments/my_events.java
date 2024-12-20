package Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Toast;

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
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * My Events Activity
 *
 *     handles interactions with the 'my events' fragment
 *     data:<ul> <li>activity views</li> <li>device Id</li> <li>highlightedButton</li></ul>
 *     methods:<ul> <li>constructor</li> <li>onCreateView</li> <li>closeFragment</li> <li>setHighlightedButton</li> <li>openFacilityFragment</li> <li>checkForFacility</li> <li>fetchEvents</li> <li>fetchJoinedEvents</li> <li>createJoinedEventsList</li> <li>fetchJoinedEventIDs</li></ul>
 *
 * @author Meghan, Rheanna, Rehan
 * @see Fragment
 * @see EventManager
 * @see Event
 * @see Facility
 * @see FacilityManager
 * @see <a href="https://www.geeksforgeeks.org/atomicinteger-incrementandget-method-in-java-with-examples/">https://www.geeksforgeeks.org/atomicinteger-incrementandget-method-in-java-with-examples/</a>
 */
public class my_events extends Fragment implements EventAdapter.OnEventClickListener {
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
    EventAdapter customAdapterHosted;
    EventAdapter customAdapterJoined;
    /**
     * Required empty public constructor
     */
    public my_events() {
        // Required empty public constructor
    }

    /**
     * Construct the my_events fragment view
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
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
        eventIDs = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity());
        customAdapterHosted = new EventAdapter(getContext(), events, false, true, this);
        customAdapterJoined = new EventAdapter(getContext(), events, false, false, this);

        recyclerView.setLayoutManager(layoutManager); //arranges recyclerView in linear form
        recyclerView.setAdapter(customAdapterJoined);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

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
        createJoinedEventsList();

        //onclick listeners
        hostedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Empty out the events ArrayList to hold the Event objects
                events.clear();
                customAdapterHosted.notifyDataSetChanged();
                recyclerView.setAdapter(customAdapterHosted);

                setHighlightedButton(hostedEventsBtn);
                eventListView.setVisibility(View.VISIBLE);
                missingFacilityFrame.setVisibility(View.INVISIBLE);
                checkForFacility();
            }
        });
        joinedEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createJoinedEventsList();
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
     * Handles the event details button click
     * @param EventID The event ID of the clicked event
     */
    @Override
    public void onEventDetailsClick(String EventID) {
        Bundle bundle = new Bundle();
        bundle.putString("eventID", EventID);

        event_details eventDetailsFragment = new event_details();
        eventDetailsFragment.setArguments(bundle); // Pass the eventID to the fragment
        // Navigate to event details fragment

        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, eventDetailsFragment) // Replace with your container ID
                    .addToBackStack(null) // Optional: to add to back stack
                    .commit();
        } else {
            // Handle the error
        }
    }

    /**
     * Handles the event update button click
     * @param EventID The event ID of the clicked event
     */
    @Override
    public void onEventUpdateClick(String EventID) {
        String selectedEventID = EventID;

        Bundle bundle = new Bundle();
        bundle.putString("eventID", selectedEventID);
        bundle.putString("facilityID", deviceID);

        update_event updateEvent = new update_event(); // call update_event to display event details
        updateEvent.setArguments(bundle); // pass eventID to update_event

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, updateEvent);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * returns user to the homepage
     *
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
     *
     * @param button TextView representing a button
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
     *
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

    /**
     * checks if a facility exists for hosted events list
     */
    public void checkForFacility() {
        //decide which fragment to open
        FacilityManager facilityManager = new FacilityManager();
        facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
            @Override
            public void onFacilityExists(Facility facility) {
                missingFacilityFrame.setVisibility(View.GONE);
                eventIDs = facility.getEvents();

                fetchEvents(eventIDs, true, new FetchEventsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Event> curr_events) {
                        Log.d("RecyclerView ", "AFTER fetchEvents curr_events size: " + curr_events.size());
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

    /**
     * fetches events for the list of event IDs.
     * Creates listener since firebase's get() is asynchronous in nature,
     * so it notifies when all events have been loaded.
     *
     * @param eventIDs String List of event IDs
     * @param callback callback to let the calling method know when all the events have been loaded.
     *
     */
    public void fetchEvents(List<String> eventIDs, boolean isHosted, final FetchEventsCallback callback) {
        Log.d("fetchEvents", "Event IDs : " + eventIDs);

        int totalEvents = eventIDs.size();  // total number of events to fetch

        AtomicInteger completedTasks = new AtomicInteger(0);  // needed for incrementAndGet()

        for (String eventID : eventIDs) {
            db.collection("events").document(eventID)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document != null && document.exists()) {
                                String currentDocumentID = document.getId();

                                eventManager.getEvent(currentDocumentID, new EventManager.OnEventFetchListener() {
                                    @Override
                                    public void onEventFetched(Event current_event) {
                                        Log.d("onEventFetched", "Event title : " + current_event.getEventTitle());
                                        Event curr_event = document.toObject(Event.class);
                                        curr_event.setEventID(document.getId()); // This Event object has eventID field added
                                        events.add(curr_event);  // Add retrieved Event object to the List

                                        // Notify adapter that event was added
                                        if (isHosted) {
                                            customAdapterHosted.notifyItemInserted(events.size() - 1);
                                        } else {
                                            customAdapterJoined.notifyItemInserted(events.size() - 1);
                                        }

                                        if (completedTasks.incrementAndGet() == totalEvents) {  // All events have been fetched
                                            callback.onSuccess(events);  // Pass retrieved events list
                                        }
                                    }

                                    @Override
                                    public void onEventFetchError(Exception e) {
                                        Log.e("fetchEvents", "Failed to fetch Event for ID: " + currentDocumentID + e);

                                        if (completedTasks.incrementAndGet() == totalEvents) {
                                            callback.onSuccess(events);  // Pass events which were retrieved
                                        }
                                    }
                                });
                            } else {
                                Log.d("fetchEvents", "Document does not exist for eventID: " + eventID);  // Skip deleted event records???

                                if (completedTasks.incrementAndGet() == totalEvents) {
                                    callback.onSuccess(events);  // Pass events which were retrieved
                                }
                            }
                        } else {

                            Log.e("fetchEvents", "Failed to fetch document for eventID: " + eventID, task.getException());

                            if (completedTasks.incrementAndGet() == totalEvents) {
                                callback.onSuccess(events);  // Pass events which were retrieved
                            }
                        }
                    });
        }
    }

    public interface FetchEventsCallback {
        void onSuccess(ArrayList<Event> events);

        void onFailure(Exception e);
    }

    /**
     * This method fetches all the events the user joined from firebase currently stored
     * and adds it to a list of events.
     * @param deviceID the device ID of the user.
     */
    public void fetchJoinedEvents(String deviceID) {
        fetchJoinedEventIDs(deviceID, new FetchJoinedEventIDsCallback() {
            @Override
            public void onJoinedEventIDsFetched(List<String> eventIDs) {
                Log.d("fetchJoinedEvents", "Before call fetchEvents: Joined Event IDs: " + eventIDs);
                fetchEvents(eventIDs, false, new FetchEventsCallback() {
                    @Override
                    public void onSuccess(ArrayList<Event> curr_events) {
                        Log.d("RecyclerView ", "AFTER fetchEvents curr_events size: " + curr_events.size());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("fetchJoinedEvents", "Error in fetching events", e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("fetchJoinedEvents", "Error");
            }
        });
    }

    /**
     * This method creates the joined events list for the user.
     */
    public void createJoinedEventsList() {
        eventIDs.clear();
        events.clear();
        recyclerView.setAdapter(customAdapterJoined);

        //switch views
        setHighlightedButton(joinedEventsBtn);
        missingFacilityFrame.setVisibility(View.GONE);
        eventListView.setVisibility(View.VISIBLE);
        fetchJoinedEvents(deviceID);
    }

    /**
     * This method fetches all the event IDs from firebase currently stored
     * and adds it to a list of event IDs.
     * @param deviceID the device ID of the user.
     * @param callback to check if all the event IDs have been loaded.
     */
    public void fetchJoinedEventIDs(String deviceID, final FetchJoinedEventIDsCallback callback) {
        db.collection("users").document(deviceID)
                .get()

                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document != null && document.exists()) {
                            eventIDs = (List<String>) document.get("joinedEvents");
                            if (eventIDs != null) {
                                Log.d("fetchJoinedEventIDs", "Joined Event IDs: " + eventIDs);
                                callback.onJoinedEventIDsFetched(eventIDs);
                            } else {
                                Log.d("fetchJoinedEventIDs", "No Joined Events found");
                                callback.onJoinedEventIDsFetched(eventIDs);
                            }
                        } else {
                            Log.e("fetchJoinedEventIDs", "No User document for device ID:  " + deviceID);
                            callback.onError(new Exception("No User document found for deviceID:" + deviceID));
                        }
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    /**
     * This interface is used to check if all the event IDs have been loaded.
     */
    public interface FetchJoinedEventIDsCallback {
        void onJoinedEventIDsFetched(List<String> eventIDs);
        void onError(Exception e);
    }
}