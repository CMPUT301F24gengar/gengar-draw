package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.qrcode.encoder.QRCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Classes.Event;
import Classes.EventManager;
import Classes.Facility;

/**
 * Update Event Fragment
 *     Handles updates to the update event page fragment
 *     data:<ul> <li>fragment views</li> <li>event title</li> <li>event registration opens and deadline dates</li> <li>event max winners</li> <li>event details</li></ul>
 *     methods:<ul> <li>onCreate</li> <li>onCreateView</li> <li>getEventFromDatabase</li> <li>updateEventDisplayed</li> <li>getFacilityFromDatabase</li> <li>updateFacilityDisplayed</li> <li>formatDate</li></ul>
 *
 * @author Rheanna
 * @see Event
 */
public class update_event extends Fragment {
    private FirebaseFirestore db;
    private String eventID;
    private String facilityID;
    private EditText detailsEditText;
    private Uri imageURI;
    private Event event;
    private TextView eventTitle;
    private TextView eventRegistrationOpens;
    private TextView eventRegistrationDeadline;
    private TextView eventMaxWinners;
    private TextView eventDetails;
    private ImageView eventPicture;
    private TextView updateEventSaveBtn;
    private TextView updateEventCancelBtn;
    private Facility facility;
    private TextView facilityName;
    private ImageView facilityPicture;
    private CheckBox geolocationToggle;
    private Boolean editGeolocationToggle;
    private ImageView eventPicture;

    public update_event() {
        // Required empty public constructor
    }

    public static update_event newInstance(String param1, String param2) {
        Log.d("update_event", "newInstance" + param1 + " " + param2);
        update_event fragment = new update_event();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Construct the update_event fragment
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @see Fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventID = getArguments().getString("eventID");
            facilityID = getArguments().getString("facilityID");
            Log.d("update_event", "onCreate eventID: " + eventID + " facilityID: " + facilityID );
        }
    }

    /**
     * Construct the update_event fragment view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Constructed View
     *
     * @see Fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_event, container, false);

        db = FirebaseFirestore.getInstance();

        updateEventSaveBtn = view.findViewById(R.id.update_event_save_btn);
        updateEventCancelBtn = view.findViewById(R.id.update_event_cancel_btn);
        detailsEditText = view.findViewById(R.id.update_event_details);
        geolocationToggle = view.findViewById(R.id.update_event_checkbox);

        eventTitle = view.findViewById(R.id.view_event_title);
        eventRegistrationOpens = view.findViewById(R.id.view_event_registration_opens);
        eventRegistrationDeadline = view.findViewById(R.id.view_event_registration_deadline);
        eventMaxWinners = view.findViewById(R.id.view_event_max_winners);

        facilityName = view.findViewById(R.id.view_event_facility_name);

        if (getArguments() != null) {
            eventID = getArguments().getString("eventID");
            facilityID = getArguments().getString("facilityID");
            Log.d("update_event", "onCreateView eventID: " + eventID + " facilityID: " + facilityID);
            getEventFromDatabase(eventID);
            getFacilityFromDatabase(facilityID);
        }

        eventPicture = view.findViewById(R.id.view_event_picture);
        facilityPicture = view.findViewById(R.id.view_event_facility_picture);

        geolocationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editGeolocationToggle = !editGeolocationToggle;
            }
        });

        eventPicture.setOnClickListener(view1 -> {
            Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent, 1000);
        });

        updateEventSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String details = detailsEditText.getText().toString().trim();
                    if (details.isEmpty()) {
                        Toast.makeText(getContext(), "Event details cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }


                db.collection("events").document(eventID)
                        .update(
                                "eventDetails", details,
                                "enableGeolocation", editGeolocationToggle
                        )
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Event updated successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "error updating event details: ", e);
                        });
                closeFragment();
            }
        });
        //Go back to my_events view
        updateEventCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
        return view;
    }

    /**
     * gets the event record from the firestore database
     * @param eventID String event ID
     * @throws Exception Exception if error while getting event
     */
    private void getEventFromDatabase(String eventID) {
        db = FirebaseFirestore.getInstance();

        // Fetch specific event document by its event ID
        db.collection("events").document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        event = documentSnapshot.toObject(Event.class);

                        if (event != null) {
                            updateEventDisplayed(event);
                        }
                    } else {
                        Log.d("firestore get", "documentSnapshot.NOT exists eventID " + eventID);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting event: ", e);
                });
    }

    /**
     * sets the title, registration opens and deadline dates, maximum winners, details for the event
     * @param event Event object
     */
    private void updateEventDisplayed(Event event) {
        eventTitle.setText(event.getEventTitle());
        eventRegistrationOpens.setText(formatDate(event.getEventStartDate()));
        eventRegistrationDeadline.setText(formatDate(event.getRegDeadlineDate()));
        eventMaxWinners.setText(String.valueOf(event.getMaxWinners()));
        detailsEditText.setText(event.getEventDetails());
        editGeolocationToggle = event.getEnableGeolocation();
        geolocationToggle.setChecked(editGeolocationToggle);

        // Load image
        Glide.with(getView().getContext())
                .load(event.getEventPictureURL())
                .into((ImageView) getView().findViewById(R.id.view_event_picture));
    }

    /**
     * gets the facility record from the firestore database
     * @param facilityID String facility ID
     * @throws Exception Exception if error while getting facility
     */
    private void getFacilityFromDatabase(String facilityID) {
        db = FirebaseFirestore.getInstance();

        // Fetch specific event document by its event ID
        db.collection("facilities").document(facilityID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        facility = documentSnapshot.toObject(Facility.class);

                        if (facility != null) {
                            updateFacilityDisplayed(facility);
                        }
                    } else {
                        Log.d("firestore get", "documentSnapshot.NOT exists facilityID " + facilityID);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting facility: ", e);
                });
    }

    /**
     * sets the name and image for the facility
     * @param facility Facility object
     */
    private void updateFacilityDisplayed(Facility facility) {
        facilityName.setText(facility.getName());

        // Load image
        Glide.with(getView().getContext())
                .load(facility.getPictureURL())
                .into((ImageView) getView().findViewById(R.id.view_event_facility_picture));
    }

    /**
     * formats the dates for the event
     * @param date Dates for the event
     * @return String Dates for the event
     */
    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            return formatter.format(date).toUpperCase();  // Convert to uppercase to match the format
        }
        return "";  // Return empty string for null dates
    }

    /**
     * returns user to the my_events
     * @throws Exception activity not instance of MainActivity
     * @see my_events
     */
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new my_events()).commit();
        } else {
            // Handle error
        }
    }

    /**
     * sets image if activity result indicates success
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     * @see Fragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageURI = data.getData();
                eventPicture.setImageURI(imageURI);
            }
        }
    }
}