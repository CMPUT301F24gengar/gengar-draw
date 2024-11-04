package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Toast;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Classes.Event;
import Classes.EventLists;
import Classes.EventManager;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.QRcode;

public class create_event extends Fragment {

    private FrameLayout blackFrame;
    private LinearLayout facilityCreateLayout;
    private FrameLayout facilityProceedBtn;
    private FrameLayout facilityCancelBtn;

    private ImageView eventImage;
    private EditText titleEditText;
    private EditText registrationOpensEditText, registrationDeadlineEditText, eventStartsEditText;
    private EditText maxWinnersEditText, maxEntrantsEditText;
    private EditText detailsEditText;
    private CheckBox checkboxCheckBox;
    private TextView createBtn;
    private TextView cancelBtn;

    private Uri imageURI;
    private Facility facilityProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        String deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initializing views
        blackFrame = view.findViewById(R.id.black_frame);
        facilityCreateLayout = view.findViewById(R.id.facility_create_proceed_layout);
        facilityProceedBtn = view.findViewById(R.id.facility_create_proceed_btn);
        facilityCancelBtn = view.findViewById(R.id.facility_create_cancel_btn);

        eventImage = view.findViewById(R.id.create_event_picture);
        titleEditText = view.findViewById(R.id.create_event_title);
        registrationOpensEditText = view.findViewById(R.id.create_event_registration_opens);
        registrationDeadlineEditText = view.findViewById(R.id.create_event_registration_deadline);
        eventStartsEditText = view.findViewById(R.id.create_event_event_starts);
        maxWinnersEditText = view.findViewById(R.id.create_event_max_winners);
        maxEntrantsEditText = view.findViewById(R.id.create_event_max_entrants);
        detailsEditText = view.findViewById(R.id.create_event_details);
        checkboxCheckBox = view.findViewById(R.id.create_event_checkbox);
        createBtn = view.findViewById(R.id.create_event_create_btn);
        cancelBtn = view.findViewById(R.id.create_event_cancel_btn);

        FacilityManager facilityManager = new FacilityManager();
        EventManager eventManager = new EventManager();

        facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
            @Override
            public void onFacilityExists(Facility facility) {
                facilityProfile = facility;
                blackFrame.setVisibility(View.GONE);
            }

            @Override
            public void onFacilityNotExists() {
                facilityCreateLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                Log.e("facility_profile", "Error checking facility", e);
            }
        });

        facilityProceedBtn.setOnClickListener(v -> {
            openFacilityFragment();
        });
        facilityCancelBtn.setOnClickListener(v -> {
            closeFragment();
        });

        registrationOpensEditText.setOnClickListener(v -> showDateTimePicker(registrationOpensEditText));
        registrationDeadlineEditText.setOnClickListener(v -> showDateTimePicker(registrationDeadlineEditText));
        eventStartsEditText.setOnClickListener(v -> showDateTimePicker(eventStartsEditText));

        eventImage.setOnClickListener(view1 -> {
            Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent, 1000);
        });

        createBtn.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            Date registrationOpens = getDateFromEditText(registrationOpensEditText);
            Date registrationDeadline = getDateFromEditText(registrationDeadlineEditText);
            Date eventStarts = getDateFromEditText(eventStartsEditText);
            String maxWinners = maxWinnersEditText.getText().toString();
            String maxEntrants = maxEntrantsEditText.getText().toString();
            String details = detailsEditText.getText().toString();
            boolean enableGeolocation = checkboxCheckBox.isChecked();

            if (imageURI == null) {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }
            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (registrationOpens == null) {
                Toast.makeText(getContext(), "Please set the registration open date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (registrationDeadline == null) {
                Toast.makeText(getContext(), "Please set the registration deadline date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (eventStarts == null) {
                Toast.makeText(getContext(), "Please set the event start date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (registrationDeadline.before(registrationOpens)) {
                Toast.makeText(getContext(), "Registration deadline must be after registration opens", Toast.LENGTH_SHORT).show();
                return;
            }
            if (eventStarts.before(registrationDeadline)) {
                Toast.makeText(getContext(), "Event start date must be after registration deadline", Toast.LENGTH_SHORT).show();
                return;
            }
            if (maxWinners.isEmpty() || Integer.parseInt(maxWinners) <= 0) {
                Toast.makeText(getContext(), "Please enter a valid number for max winners", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!maxEntrants.isEmpty() && Integer.parseInt(maxEntrants) < Integer.parseInt(maxWinners) ) {
                Toast.makeText(getContext(), "Please enter a valid number for max entrants", Toast.LENGTH_SHORT).show();
                return;
            }
            if (details.isEmpty()) {
                Toast.makeText(getContext(), "Event details cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer maxWinnersInt = Integer.parseInt(maxWinners);
            Integer maxEntrantsInt = maxEntrants.isEmpty() ? null : Integer.parseInt(maxEntrants);

            Event event = new Event(deviceID, title, registrationOpens, registrationDeadline, eventStarts, maxWinnersInt, maxEntrantsInt, details, null, enableGeolocation, null, null, null);
            QRcode qrcode = new QRcode();

            EventLists eventLists = new EventLists(null, maxWinnersInt, maxEntrantsInt, enableGeolocation, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>());

            String docID;
            docID = eventManager.addEvent(event, qrcode, eventLists, imageURI, new EventManager.OnUploadPictureListener() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    Toast.makeText(getContext(), "Event created successfully", Toast.LENGTH_SHORT).show();
                    closeFragment();
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), "Failed to create event", Toast.LENGTH_SHORT).show();
                }
            });

            facilityProfile.addEvent(docID);
            
            facilityManager.updateFacility(facilityProfile, deviceID);

        });

        cancelBtn.setOnClickListener(v -> closeFragment());
        return view;
    }

    private Date getDateFromEditText(EditText editText) {
        String dateText = editText.getText().toString();
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateText);
        } catch (ParseException e) {
            return null;
        }
    }

    private void showDateTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            new TimePickerDialog(getContext(), (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                editText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.getTime()));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageURI = data.getData();
                eventImage.setImageURI(imageURI);
            }
        }
    }

    private void openFacilityFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new facility_profile()).commit();
        } else {
            // Handle error
        }
    }
}