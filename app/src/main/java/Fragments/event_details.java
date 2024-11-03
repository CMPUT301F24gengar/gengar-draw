package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gengardraw.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Classes.Event;
import Classes.EventManager;


public class event_details extends Fragment {

    private String eventID; // Variable to hold the event ID
    private EventManager eventManager;

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
        ImageView eventPicture = view.findViewById(R.id.view_event_picture);
        TextView viewEventQRCode = view.findViewById(R.id.view_qr_code);
        FrameLayout joinButton = view.findViewById(R.id.view_event_btn);

        // Load event details using eventManager
        loadEventDetails(eventID, view);

        // Set Click Listeners
        viewEventQRCode.setOnClickListener(v -> {
            // Handle View QR Code action
            // Show QR code logic
        });

        joinButton.setOnClickListener(v -> {
            // Handle Join action
            joinEvent(eventID); // Join the event
        });

        return view;
    }

    private void loadEventDetails(String eventID, View view) {
        // Use eventManager to fetch event details by eventID
        eventManager.getEvent(eventID, new EventManager.OnEventFetchListener() {
            @Override
            public void onEventFetched(Event event) {
                if (event != null) {
                    // Populate your views with event data
                    ((TextView) view.findViewById(R.id.view_event_title)).setText(event.getEventTitle());
                    ((TextView) view.findViewById(R.id.view_event_registration_opens)).setText(formatDate(event.getEventStartDate()));
                    ((TextView) view.findViewById(R.id.view_event_registration_deadline)).setText(formatDate(event.getRegDeadlineDate()));
                    ((TextView) view.findViewById(R.id.view_event_max_winners)).setText(String.valueOf(event.getMaxWinners()));
                    ((TextView) view.findViewById(R.id.view_event_description)).setText(event.getEventDetails());

                    // Load the image using Glide
                    Glide.with(view.getContext())
                            .load(event.getEventPictureURL())
                            .into((ImageView) view.findViewById(R.id.view_event_picture));
                }
            }

            @Override
            public void onEventFetchError(Exception e) {
                // Handle the error while fetching the event
            }
        });
    }

    private void joinEvent(String eventID) {
        // Implement joining event logic
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            return formatter.format(date).toUpperCase(); // Convert to uppercase to match the format
        }
        return ""; // Return empty string for null dates
    }
}
