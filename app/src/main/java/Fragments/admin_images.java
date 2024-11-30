package Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gengardraw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Adapters.EventImageAdapter;
import Adapters.FacilityImageAdapter;
import Adapters.UserProfileAdapter;
import Adapters.UserProfileImageAdapter;
import Classes.Event;
import Classes.EventManager;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * @author Dion
 * admin_images
 * This fragment displays the list of facility profiles that an admin can browse through.
 * the images are divided by userprofile images, facility images and event images.
 */

public class admin_images extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private UserProfileManager userProfileManager;
    private ArrayList<UserProfile> userProfiles;
    private FacilityManager facilityManager;
    private ArrayList<Facility> facilities;
    private EventManager eventManager;
    private ArrayList <Event> events;
    private RecyclerView.LayoutManager gridLayoutManager;
    private RecyclerView.LayoutManager linearLayoutManager;
    private UserProfileImageAdapter userProfileImageAdapter;
    private FacilityImageAdapter facilityImageAdapter;
    private EventImageAdapter eventImageAdapter;
    private TextView userProfileImagesButton;
    private TextView facilityImagesButton;
    private TextView eventImagesButton;
    private int count;

    TextView highlightedButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_images, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.admin_user_profile_images_list);
        userProfileManager= new UserProfileManager();
        userProfiles = new ArrayList<>();
        facilityManager = new FacilityManager();
        facilities = new ArrayList<>();
        eventManager = new EventManager();
        events = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(getActivity(),3);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        userProfileImageAdapter = new UserProfileImageAdapter(getContext(),userProfiles);

        userProfileImagesButton = view.findViewById(R.id.admin_user_profile_images_button);
        facilityImagesButton = view.findViewById(R.id.admin_facility_images_button);
        eventImagesButton = view.findViewById(R.id.admin_event_images_button);
        count = 0;
        highlightedButton = userProfileImagesButton;

        userProfileImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlightedButton(userProfileImagesButton);

                userProfiles.clear();
                fetchUserProfiles(new OnProfilesLoadedListener() {
                    @Override
                    public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                        //adding userProfiles to adapter
                        recyclerView.setLayoutManager(gridLayoutManager); //arranges recyclerView in grid form
                        recyclerView.setAdapter(userProfileImageAdapter);
                    }
                });
            }
        });

        facilityImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlightedButton(facilityImagesButton);

                facilities.clear(); //clear if already loaded from previous click.
                fetchFacilities(new OnFacilitiesLoadedListener() {
                    @Override
                    public void onFacilitiesLoaded(ArrayList<Facility> facilities) {
                        //adding facilties to adapter
                        facilityImageAdapter = new FacilityImageAdapter(getActivity(), facilities);
                        recyclerView.setLayoutManager(gridLayoutManager); //arranges recyclerView in grid form
                        recyclerView.setAdapter(facilityImageAdapter);
                    }
                });
            }
        });

        eventImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighlightedButton(eventImagesButton);
                events.clear();
                fetchEvents(new OnEventsLoadedListener() {
                    @Override
                    public void onEventsLoaded(ArrayList<Event> events) {
                        //adding events to adapter
                        eventImageAdapter = new EventImageAdapter(getActivity(),events);
                        recyclerView.setLayoutManager(linearLayoutManager); //arranges recyclerView in grid form
                        recyclerView.setAdapter(eventImageAdapter);

                    }
                });

            }
        });

        //by default, fetch users.
        fetchUserProfiles(new OnProfilesLoadedListener() {
            @Override
            public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                setHighlightedButton(userProfileImagesButton);

                //adding userProfiles to adapter
                recyclerView.setLayoutManager(gridLayoutManager); //arranges recyclerView in grid form
                recyclerView.setAdapter(userProfileImageAdapter);

            }
        });

        return view;

    }

    /**
     * interface for listener to check if all the user profiles have been loaded.
     */
    // Fetching Users
    public interface OnProfilesLoadedListener {
        void onProfilesLoaded(ArrayList<UserProfile> userProfiles);
    }

    /**
     * This method fetches all the user profiles from firebase currently stored
     * and adds it to a list of user profiles.
     * @param listener to check if all the profiles have been loaded.
     */
    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchUserProfiles(OnProfilesLoadedListener listener) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                listener.onProfilesLoaded(userProfiles);
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("userprofile", document.getId() + " => " + document.getData());

                                String currentDeviceID = document.getId();

                                userProfileManager.getUserProfile(currentDeviceID, new UserProfileManager.OnUserProfileFetchListener() {
                                    @Override
                                    public void onUserProfileFetched(UserProfile userProfile) {
                                        if (userProfile.getPictureURL() != null){
                                            userProfiles.add(userProfile);
                                        }
                                        count++;

                                        // Check if all documents have been processed
                                        if (count == task.getResult().size()) {
                                            listener.onProfilesLoaded(userProfiles); // Notify when done
                                            count = 0;
                                        }
                                    }

                                    @Override
                                    public void onUserProfileFetchError(Exception e) {
                                        Log.d("userProfiles fetch ERROR", e.toString());
                                    }
                                });
                            }
                        } else {
                            Log.d("userprofile doc", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * interface for listener to check if all the facilities have been loaded.
     */
    //Fetching Facilties
    public interface OnFacilitiesLoadedListener {
        void onFacilitiesLoaded(ArrayList<Facility> facilities);
    }

    /**
     * This method fetches all the facilities from firebase currently stored
     * and adds it to a list of facilities.
     * @param listener to check if all the facilities have been loaded
     */
    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchFacilities(OnFacilitiesLoadedListener listener) {
        db.collection("facilities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                listener.onFacilitiesLoaded(facilities);
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("facility", document.getId() + " => " + document.getData());

                                String currentDeviceID = document.getId();

                                facilityManager.getFacility(currentDeviceID, new FacilityManager.OnFacilityFetchListener() {
                                    @Override
                                    public void onFacilityFetched(Facility facility) {
                                        if (facility.getPictureURL() != null){
                                            facilities.add(facility);
                                        }
                                        count++;
                                        // Check if all documents have been processed
                                        if (count == task.getResult().size()) {
                                            listener.onFacilitiesLoaded(facilities); // Notify when done
                                            count = 0;
                                        }
                                    }
                                    @Override
                                    public void onFacilityFetchError(Exception e) {
                                        Log.d("facilities fetch ERROR", e.toString());
                                    }
                                });
                            }
                        } else {
                            Log.d("facility doc", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * interface for listener to check if all the events have been loaded.
     */
    //Fetching Events
    public interface OnEventsLoadedListener {
        void onEventsLoaded(ArrayList<Event> events);
    }

    /**
     * This method fetches all the events from firebase currently stored
     * and adds it to a list of events.
     * @param listener to check if all the events have been loaded
     */
    public void fetchEvents(OnEventsLoadedListener listener) {
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                listener.onEventsLoaded(events);
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("event", document.getId() + " => " + document.getData());

                                String currentEventID = document.getId();

                                eventManager.getEvent(currentEventID, new EventManager.OnEventFetchListener() {
                                    @Override
                                    public void onEventFetched(Event event) {
                                        if (event.getEventPictureURL() != null){
                                            events.add(event);
                                        }
                                        count++;
                                        // Check if all documents have been processed
                                        if (count == task.getResult().size()) {
                                            listener.onEventsLoaded(events); // Notify when done
                                            count = 0;
                                        }
                                    }
                                    @Override
                                    public void onEventFetchError(Exception e) {
                                        Log.d("events fetch ERROR", e.toString());
                                    }
                                });
                            }
                        } else {
                            Log.d("event doc", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * method to set a button that is clicked to be highlighted
     * @param button the textview that when clicked becomes highlighted
     * sets the text color of current highlighted button to grey
     * sets the background of current highlighted button to black
     * sets the new button that is clicked as the highlighted button
     * sets the text color of the new highlighted button to black
     * sets the background of the new highlighted button to blue.
     */

    private void setHighlightedButton(TextView button) {
        highlightedButton.setTextColor(getResources().getColor(R.color.grey));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black1)));
        highlightedButton = button;
        highlightedButton.setTextColor(getResources().getColor(R.color.black1));
        highlightedButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
    }

}