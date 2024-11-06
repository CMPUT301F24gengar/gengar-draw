package Fragments;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.gengardraw.R;
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
import Classes.UserProfile;
import Classes.UserProfileManager;


public class event_details extends Fragment {

    private Boolean buttonDebounce = false;

    private String deviceID;
    private Date currentDate;

    private Boolean inWaitingList;
    private Boolean inChosenList;
    private Boolean inCancelledList;
    private Boolean inWinnersList;

    private EventListsManager eventListsManager = new EventListsManager();

    private String eventID; // Variable to hold the event ID
    private EventManager eventManager;
    ImageView eventPicture;
    TextView viewEventQRCode;
    LinearLayout qrCodeContainer;
    ImageView qrCodeImage;
    TextView qrCodeBack;

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

    TextView listBack;
    LinearLayout listContainer;
    RecyclerView recyclerView;
    UserProfileManager userProfileManager;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UserProfile> userProfiles;
    UserProfileAdapter customAdapter;

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

        listBack = view.findViewById(R.id.view_list_back);
        listContainer = view.findViewById(R.id.user_profile_list_container);
        recyclerView = view.findViewById(R.id.recycler_view);
        userProfileManager = new UserProfileManager();
        userProfiles = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        customAdapter = new UserProfileAdapter(getContext(), userProfiles);
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
        currentDate = new Date();

        loadEventDetails(eventID, view);

        return view;
    }

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

    private void setupButtons(Event event, String eventID) {
        join_leaveButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            if (!inWaitingList) {
                eventListsManager.addUserToWaitingList(eventID, deviceID, new EventListsManager.OnEventListsUpdateListener() {
                    @Override
                    public void onSuccess(String message, boolean boolValue) {
                        inWaitingList = boolValue;
                        setJoinLeaveButtonText(inWaitingList);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        buttonDebounce = false;
                    }
                    @Override
                    public void onError(Exception e) {
                        buttonDebounce = false;
                    }
                }
                ,null, null);
            } else {
                eventListsManager.removeUserFromWaitingList(eventID, deviceID, new EventListsManager.OnEventListsUpdateListener() {
                    @Override
                    public void onSuccess(String message, boolean boolValue) {
                        inWaitingList = !boolValue;
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

        waitingListButton.setOnClickListener(v -> {
            if (buttonDebounce) return;
            buttonDebounce = true;

            eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {
                @Override
                public void onEventListsFetched(EventLists eventLists) {
                    List<String> waitingList = eventLists.getWaitingList();
                    userProfiles.clear();
                    fetchUserProfiles(waitingList, new OnProfilesLoadedListener(){
                        @Override
                        public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {
                            customAdapter.notifyDataSetChanged();
                            listContainer.setVisibility(View.VISIBLE);
                            buttonDebounce = false;
                        }
                    });
                }
                @Override
                public void onEventListsFetchError(Exception e) {
                    // Handle the error while fetching the event lists
                }
            });
        });

        // Initialize conditions and buttons
        eventListsManager.getEventLists(eventID, new EventListsManager.OnEventListsFetchListener() {

            String organizerID = event.getOrganizerID();

            Date regOpenDate = event.getRegOpenDate();
            Date regDeadlineDate = event.getRegDeadlineDate();
            Date eventStartDate = event.getEventStartDate();

            @Override
            public void onEventListsFetched(EventLists eventLists) {
                if (eventLists != null) {

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
                        }
                    } else { // ORGANIZER
                        if (currentDate.after(regOpenDate)) {
                            waitingListButton.setVisibility(View.VISIBLE);
                        }
                        if (currentDate.after(regDeadlineDate)) {
                            chooseEntrantsButton.setVisibility(View.VISIBLE);
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

    private void generateQRCode(String QRcode){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(QRcode, BarcodeFormat.QR_CODE, 300, 300);
            qrCodeImage.setImageBitmap(bitmap); // Set the generated QR code to ImageView
            Log.e("event_details", "QR code generated successfully");
        } catch (WriterException e) {
            Log.e("event_details", "Error generating QR code: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Failed to generate QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            return formatter.format(date).toUpperCase(); // Convert to uppercase to match the format
        }
        return ""; // Return empty string for null dates
    }

    public void setJoinLeaveButtonText(Boolean inWaitingList) {
        if (inWaitingList) {
            join_leaveButton.setText("LEAVE");
            join_leaveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        } else {
            join_leaveButton.setText("JOIN");
            join_leaveButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        }
    }

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

    public interface OnProfilesLoadedListener {
        void onProfilesLoaded(ArrayList<UserProfile> userProfiles);
    }
}
