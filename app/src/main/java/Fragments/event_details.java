package Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Classes.Event;
import Classes.EventManager;


public class event_details extends Fragment {

    private String eventID; // Variable to hold the event ID
    private EventManager eventManager;
    ImageView eventPicture;
    TextView viewEventQRCode;
    TextView joinButton;
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
        joinButton = view.findViewById(R.id.view_event_join_leave);
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


        // Load event details using eventManager
        loadEventDetails(eventID, view);

        // Set Click Listeners
        viewEventQRCode.setOnClickListener(v -> {
            // Handle View QR Code action
            // Show QR code logic
            qrCodeContainer.setVisibility(View.VISIBLE);
        });

        qrCodeBack.setOnClickListener(v -> {
            qrCodeContainer.setVisibility(View.GONE);
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

                }
            }

            @Override
            public void onEventFetchError(Exception e) {
                // Handle the error while fetching the event
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
