package Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Adapters.UserProfileAdapter;
import Classes.Event;
import Classes.EventLists;
import Classes.EventListsManager;
import Classes.EventManager;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;


/**
 * Event Details Fragment
 *
 *     Handles interactions with the event details page fragment
 *     data:<ul> <li>fragment views</li> <li>event image URI</li> <li>event QR code</li> <li>event title</li> <li>event registration open and deadline and start dates</li> <li>event max winners and entrants</li> <li>event description</li> <li>local Event object</li></ul>
 *     methods:<ul> <li>onCreateView</li> <li>loadEventDetails</li> <li>setupButtons</li> <li>generateQRCode</li>  <li>formatDate</li> <li>setJoinLeaveButtonText</li> <li>fetchUserProfiles</li> <li>interface OnProfilesLoadedListener</li></ul>
 *
 * @author Rafi, Rehan
 * @see Event
 * @see EventManager
 */
public class event_details extends Fragment {

    private Boolean buttonDebounce = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;

    private String deviceID;
    private Date currentDate;

    private Boolean inWaitingList;
    private Boolean inChosenList;
    private Boolean inCancelledList;
    private Boolean inWinnersList;

    private Date regOpenDate;
    private Date regDeadlineDate;
    private Date eventStartDate;

    private EventListsManager eventListsManager = new EventListsManager();

    private String eventID; // Variable to hold the event ID
    private EventManager eventManager;
    ImageView eventPicture;
    TextView viewEventQRCode;
    LinearLayout qrCodeContainer;
    ImageView qrCodeImage;
    TextView qrCodeBack;

    FrameLayout blackFrame;
    LinearLayout geoLocationWarning;
    FrameLayout geoLocationWarningProceed;
    FrameLayout geoLocationWarningCancel;

    TextView viewEventTitle;
    TextView viewEventStartDay;
    TextView viewEventStartMonth;
    TextView viewEventStartTime;
    TextView viewEventRegistrationOpens;
    TextView viewEventRegistrationDeadline;
    TextView viewEventMaxWinners;
    TextView viewEventMaxEntrantsTitle;
    TextView viewEventMaxEntrants;
    TextView viewEventDescription;

    TextView join_leaveButton;
    TextView chooseEntrantsButton;

    LinearLayout accept_declineLayout;
    TextView acceptButton;
    TextView declineButton;

    TextView waitingListButton;
    TextView mapButton;
    TextView chosenEntrantsButton;
    TextView cancelledEntrantsButton;
    TextView winnersListButton;

    TextView statusText;

    TextView listBack;
    TextView listTitle;
    LinearLayout listContainer;
    RecyclerView recyclerView;
    UserProfileManager userProfileManager;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UserProfile> userProfiles;
    UserProfileAdapter customAdapter;

    UserProfile userProfile;

    /**
     * Construct the event_details fragment view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Constructed View
     * @see Fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Retrieve the eventID from the bundle
        if (getArguments() != null) {
            eventID = getArguments().getString("eventID");
        }

        // Initialize EventManager
        eventManager = new EventManager();

        // Initialize Views
        eventPicture = view.findViewById(R.id.view_event_picture);
        viewEventQRCode = view.findViewById(R.id.view_qr_code);
        qrCodeContainer = view.findViewById(R.id.view_qr_code_container);
        qrCodeImage = view.findViewById(R.id.qr_code_image);
        qrCodeBack = view.findViewById(R.id.qr_code_back);

        blackFrame = view.findViewById(R.id.black_frame);
        geoLocationWarning = view.findViewById(R.id.geolocation_warning_layout);
        geoLocationWarningProceed = view.findViewById(R.id.geolocation_proceed_btn);
        geoLocationWarningCancel = view.findViewById(R.id.geolocation_cancel_btn);

        viewEventTitle = view.findViewById(R.id.view_event_title);
        viewEventStartDay = view.findViewById(R.id.view_event_day);
        viewEventStartMonth = view.findViewById(R.id.view_event_month);
        viewEventStartTime = view.findViewById(R.id.view_event_time);
        viewEventRegistrationOpens = view.findViewById(R.id.view_event_registration_opens);
        viewEventRegistrationDeadline = view.findViewById(R.id.view_event_registration_deadline);
        viewEventMaxWinners = view.findViewById(R.id.view_event_max_winners);
        viewEventMaxEntrantsTitle = view.findViewById(R.id.view_event_max_entrants_1);
        viewEventMaxEntrants = view.findViewById(R.id.view_event_max_entrants_2);
        viewEventDescription = view.findViewById(R.id.view_event_description);

        join_leaveButton = view.findViewById(R.id.view_event_join_leave);
        chooseEntrantsButton = view.findViewById(R.id.view_event_choose_entrants);

        accept_declineLayout = view.findViewById(R.id.view_event_accept_decline);
        acceptButton = view.findViewById(R.id.view_event_accept);
        declineButton = view.findViewById(R.id.view_event_decline);

        waitingListButton = view.findViewById(R.id.view_event_waiting_list);
        mapButton = view.findViewById(R.id.view_event_map);
        chosenEntrantsButton = view.findViewById(R.id.view_event_chosen_entrants);
        cancelledEntrantsButton = view.findViewById(R.id.view_event_cancelled_entrants);
        winnersListButton = view.findViewById(R.id.view_event_winners);

        statusText = view.findViewById(R.id.status_message);

        listBack = view.findViewById(R.id.view_list_back);
        listTitle = view.findViewById(R.id.view_list_title);
        listContainer = view.findViewById(R.id.user_profile_list_container);
        recyclerView = view.findViewById(R.id.recycler_view);
        userProfileManager = new UserProfileManager();
        userProfiles = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        customAdapter = new UserProfileAdapter(getContext(), userProfiles, false);
        recyclerView.setAdapter(customAdapter);


        viewEventQRCode.setOnClickListener(v -> {
            qrCodeContainer.setVisibility(View.VISIBLE);
        });
        qrCodeBack.setOnClickListener(v -> {
            qrCodeContainer.setVisibility(View.GONE);
        });
        listBack.setOnClickListener(v -> {
            listContainer.setVisibility(View.GONE);
        });

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        currentDate = new Date();

        // get user profile from deviceID
        userProfileManager.getUserProfile(deviceID, new UserProfileManager.OnUserProfileFetchListener() {
            @Override
            public void onUserProfileFetched(UserProfile userProfileFetched) {
                userProfile=userProfileFetched;
                loadEventDetails(eventID, view);
            }
            @Override
            public void onUserProfileFetchError(Exception e) {
                //Handle the error
            }
        });

        return view;
    }

    /**
     * sets the title, various dates, maximum winners and entrants, picture url and QR code for the event
     * @param eventID String event ID
     * @param view View for the event
     * @throws Exception Exception if error while fetching event
     */
    private void loadEventDetails(String eventID, View view) {
        // Use eventManager to fetch event details by eventID
        eventManager.getEvent(eventID, new EventManager.OnEventFetchListener() {
            @Override
            public void onEventFetched(Event event) {
                if (event != null) {
                    viewEventTitle.setText(event.getEventTitle());

                    viewEventStartDay.setText(String.valueOf(event.getEventStartDate().getDate()));
                    viewEventStartMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(event.getEventStartDate()).toUpperCase());
                    viewEventStartTime.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(event.getEventStartDate()).toUpperCase());

                    viewEventRegistrationOpens.setText(formatDate(event.getRegOpenDate()));
                    viewEventRegistrationDeadline.setText(formatDate(event.getRegDeadlineDate()));
                    viewEventMaxWinners.setText(String.valueOf(event.getMaxWinners()));
                    viewEventDescription.setText(event.getEventDetails());
                    if (event.getMaxEntrants() != null) {
                        viewEventMaxEntrantsTitle.setVisibility(View.VISIBLE);
                        viewEventMaxEntrants.setText(String.valueOf(event.getMaxEntrants()));
                        viewEventMaxEntrants.setVisibility(View.VISIBLE);
                    }
                    Glide.with(view.getContext())
                            .load(event.getEventPictureURL())
                            .into((ImageView) view.findViewById(R.id.view_event_picture));

                    generateQRCode(event.getQRCode());

                    setupButtons(event, eventID);

                }
            }

            @Override
            public void onEventFetchError(Exception e) {
                // Handle the error while fetching the event
            }
        });
    }

    /**
     * sets up the various buttons for the event
     * @param event Event object that buttons apply to
     * @throws Exception Exception if error while clicking button
     */
    private void setupButtons(Event event, String eventID) {

        // check location permissions
        if (event.getEnableGeolocation()) {
            geoLocationWarning.setVisibility(View.VISIBLE);
            geoLocationWarningProceed.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    getLastLocation();
                    Toast.makeText(getContext(), "Location permissions granted "+latitude+1+" "+longitude, Toast.LENGTH_SHORT).show();
                }
                blackFrame.setVisibility(View.GONE);
            });
            geoLocationWarningCancel.setOnClickListener(v -> {
                closeFragment();
            });

        } else {
            blackFrame.setVisibility(View.GONE);
        }

        join_leaveButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            if (!inWaitingList) {

                if (event.getEnableGeolocation()) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Please enable location services and restart app", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        getLastLocation();
                    }
                }

                eventListsManager.addUserToWaitingList(eventID, deviceID, new EventListsManager.OnEventListsUpdateListener() {
                            @Override
                            public void onSuccess(String message, boolean boolValue) {
                                inWaitingList = boolValue;
                                // if in waiting list, add eventID to users joined events
                                if (inWaitingList) {
                                    userProfile.getJoinedEvents().add(eventID);
                                    userProfileManager.updateUserProfile(userProfile, deviceID);
                                }
                                setJoinLeaveButtonText(inWaitingList);
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                buttonDebounce = false;
                            }
                            @Override
                            public void onError(Exception e) {
                                buttonDebounce = false;
                            }
                        }
                        ,latitude, longitude);
            } else {
                eventListsManager.removeUserFromWaitingList(eventID, deviceID, new EventListsManager.OnEventListsUpdateListener() {
                    @Override
                    public void onSuccess(String message, boolean boolValue) {
                        inWaitingList = !boolValue;
                        // if not in waiting list, remove eventID from users joined events
                        if (!inWaitingList) {
                            userProfile.getJoinedEvents().remove(eventID);
                            userProfileManager.updateUserProfile(userProfile, deviceID);
                            if (currentDate.after(regDeadlineDate)) {
                                join_leaveButton.setVisibility(View.GONE);
                            }
                        }
                        setJoinLeaveButtonText(inWaitingList);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        buttonDebounce = false;
                    }
                    @Override
                    public void onError(Exception e) {
                        buttonDebounce = false;
                    }
                });
            }
        });

        acceptButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.addUserToWinnersList(eventID, deviceID, new EventListsManager.OnEventListsUpdateListener() {
                @Override
                public void onSuccess(String message, boolean boolValue) {
                    accept_declineLayout.setVisibility(View.GONE);
                    statusText.setTextColor(getResources().getColor(R.color.green));
                    statusText.setText("!!! WINNER !!!");
                    statusText.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    buttonDebounce = false;
                }
                @Override
                public void onError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        declineButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.removeUserFromChosenList(eventID, deviceID, new EventListsManager.OnEventListsUpdateListener() {
                @Override
                public void onSuccess(String message, boolean boolValue) {
                    accept_declineLayout.setVisibility(View.GONE);
                    statusText.setTextColor(getResources().getColor(R.color.red));
                    statusText.setText("!!! DECLINED !!!");
                    statusText.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    buttonDebounce = false;
                }
                @Override
                public void onError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        waitingListButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {
                @Override
                public void onEventListsFetched(EventLists eventLists) {
                    List<String> waitingList = eventLists.getWaitingList();
                    userProfiles.clear();

                    if (waitingList.isEmpty()) {
                        customAdapter.notifyDataSetChanged();
                        listTitle.setText("WAITING LIST");
                        listContainer.setVisibility(View.VISIBLE);
                        buttonDebounce = false;
                        return;
                    }

                    fetchUserProfiles(waitingList, new OnProfilesLoadedListener(){
                        @Override
                        public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                            customAdapter.notifyDataSetChanged();
                            listTitle.setText("WAITING LIST");
                            listContainer.setVisibility(View.VISIBLE);
                            buttonDebounce = false;
                        }
                    });
                }
                @Override
                public void onEventListsFetchError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        chosenEntrantsButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {
                @Override
                public void onEventListsFetched(EventLists eventLists) {
                    List<String> chosenList = eventLists.getChosenList();
                    userProfiles.clear();

                    if (chosenList.isEmpty()) {
                        customAdapter.notifyDataSetChanged();
                        listTitle.setText("CHOSEN ENTRANTS LIST");
                        listContainer.setVisibility(View.VISIBLE);
                        buttonDebounce = false;
                        return;
                    }

                    fetchUserProfiles(chosenList, new OnProfilesLoadedListener(){
                        @Override
                        public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                            customAdapter.notifyDataSetChanged();
                            listTitle.setText("CHOSEN ENTRANTS LIST");
                            listContainer.setVisibility(View.VISIBLE);
                            buttonDebounce = false;
                        }
                    });
                }
                @Override
                public void onEventListsFetchError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        cancelledEntrantsButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {
                @Override
                public void onEventListsFetched(EventLists eventLists) {
                    List<String> cancelledList = eventLists.getCancelledList();
                    userProfiles.clear();

                    if (cancelledList.isEmpty()) {
                        customAdapter.notifyDataSetChanged();
                        listTitle.setText("CANCELLED ENTRANTS LIST");
                        listContainer.setVisibility(View.VISIBLE);
                        buttonDebounce = false;
                        return;
                    }

                    fetchUserProfiles(cancelledList, new OnProfilesLoadedListener(){
                        @Override
                        public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                            customAdapter.notifyDataSetChanged();
                            listTitle.setText("CANCELLED ENTRANTS LIST");
                            listContainer.setVisibility(View.VISIBLE);
                            buttonDebounce = false;
                        }
                    });
                }
                @Override
                public void onEventListsFetchError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        winnersListButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {
                @Override
                public void onEventListsFetched(EventLists eventLists) {
                    List<String> winnersList = eventLists.getWinnersList();
                    userProfiles.clear();

                    if (winnersList.isEmpty()) {
                        customAdapter.notifyDataSetChanged();
                        listTitle.setText("WINNERS LIST");
                        listContainer.setVisibility(View.VISIBLE);
                        buttonDebounce = false;
                        return;
                    }

                    fetchUserProfiles(winnersList, new OnProfilesLoadedListener(){
                        @Override
                        public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                            customAdapter.notifyDataSetChanged();
                            listTitle.setText("WINNERS LIST");
                            listContainer.setVisibility(View.VISIBLE);
                            buttonDebounce = false;
                        }
                    });
                }
                @Override
                public void onEventListsFetchError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        chooseEntrantsButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.chooseWinners(eventID, new EventListsManager.OnEventListsUpdateListener() {
                @Override
                public void onSuccess(String message, boolean boolValue) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    buttonDebounce = false;
                }
                @Override
                public void onError(Exception e) {
                    buttonDebounce = false;
                }
            });
        });

        // Initialize conditions and buttons
        eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {

            String organizerID = event.getOrganizerID();

            @Override
            public void onEventListsFetched(EventLists eventLists) {
                if (eventLists != null) {
                    regOpenDate = event.getRegOpenDate();
                    regDeadlineDate = event.getRegDeadlineDate();
                    eventStartDate = event.getEventStartDate();

                    inWaitingList = eventLists.getWaitingList().contains(deviceID);
                    inChosenList = eventLists.getChosenList().contains(deviceID);
                    inCancelledList = eventLists.getCancelledList().contains(deviceID);
                    inWinnersList = eventLists.getWinnersList().contains(deviceID);

                    if (!Objects.equals(deviceID, organizerID)) { // ENTRANT
                        if (currentDate.before(regOpenDate)) {
                            // do nothing
                        } else if (currentDate.before(regDeadlineDate)) {
                            join_leaveButton.setVisibility(View.VISIBLE);
                            setJoinLeaveButtonText(inWaitingList);
                        } else if (currentDate.before(eventStartDate)) {
                            if (inWaitingList) {
                                join_leaveButton.setVisibility(View.VISIBLE);
                                setJoinLeaveButtonText(inWaitingList);
                            } else if (inChosenList) {
                                // show accept/decline
                                accept_declineLayout.setVisibility(View.VISIBLE);
                            } else if (inCancelledList) {
                                statusText.setTextColor(getResources().getColor(R.color.red));
                                statusText.setText("!!! CANCELLED !!!");
                                statusText.setVisibility(View.VISIBLE);
                            } else if (inWinnersList) {
                                statusText.setTextColor(getResources().getColor(R.color.green));
                                statusText.setText("!!! WINNER !!!");
                                statusText.setVisibility(View.VISIBLE);
                            }
                        }
                    } else { // ORGANIZER
                        if (currentDate.after(regOpenDate)) {
                            waitingListButton.setVisibility(View.VISIBLE);
                        }
                        if (currentDate.after(regDeadlineDate)) {
                            chooseEntrantsButton.setVisibility(View.VISIBLE);
                            chosenEntrantsButton.setVisibility(View.VISIBLE);
                            cancelledEntrantsButton.setVisibility(View.VISIBLE);
                            winnersListButton.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
            @Override
            public void onEventListsFetchError(Exception e) {
                // Handle the error while fetching the event lists
            }
        });

    }

    /**
     * generates the QR code for the event
     * @param QRcode String QR Code
     * @throws Exception WriterException when QR code generation fails
     */
    private void generateQRCode(String QRcode){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(QRcode, BarcodeFormat.QR_CODE, 300, 300);
            qrCodeImage.setImageBitmap(bitmap); // Set the generated QR code to ImageView
        } catch (WriterException e) {
            Toast.makeText(getContext(), "Failed to generate QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * formats the dates for the event
     * @param date Dates for the event
     * @return String Dates for the event
     */
    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            return formatter.format(date).toUpperCase(); // Convert to uppercase to match the format
        }
        return ""; // Return empty string for null dates
    }

    /**
     * sets the Join or Leave text for the event button
     * @param inWaitingList Boolean for if they are in the waiting list for event
     */
    public void setJoinLeaveButtonText(Boolean inWaitingList) {
        if (inWaitingList) {
            join_leaveButton.setText("LEAVE");
            join_leaveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        } else {
            join_leaveButton.setText("JOIN");
            join_leaveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        }
    }

    /**
     * fetches the user profile
     * @param userIDList list of user IDs
     * @param listener listener for when user profiles are loaded
     */
    public void fetchUserProfiles(List<String> userIDList, OnProfilesLoadedListener listener) {
        for (String userID : userIDList) {
            userProfileManager.getUserProfile(userID, new UserProfileManager.OnUserProfileFetchListener() {
                @Override
                public void onUserProfileFetched(UserProfile userProfile) {
                    userProfiles.add(userProfile);
                    if (userProfiles.size() == userIDList.size()) {
                        listener.onProfilesLoaded(userProfiles); // Notify when done
                    }
                }

                @Override
                public void onUserProfileFetchError(Exception e) {
                }
            });
        }
    }

    /**
     * retrieves user's device latitude and longitude and converts it to location
     * @throws Exception null location
     */
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location2 = task.getResult();
                                latitude = location2.getLatitude();
                                longitude = location2.getLongitude();
                            } else {
                                // Handle the case where Location is null
                            }
                        }
                    });
        } else {
            // Handle the case where permission is not granted
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * returns the user to the homepage.
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
     * interface for the listener that checks if all the user profiles have been loaded.
     */
    public interface OnProfilesLoadedListener {
        void onProfilesLoaded(ArrayList<UserProfile> userProfiles);
    }
}