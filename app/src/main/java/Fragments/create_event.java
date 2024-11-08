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


/**
 * <h1>Create Event Activity</h1>
 * <p>
 *     Handles interactions with the create event page fragment
 *     <ul>data: <li>fragment views</li> <li>event image URI</li> <li>event title</li> <li>event registration open and deadline and start dates</li> <li>event max winners and entrants</li> <li>geolocation checkbox</li> <li>local Facility object</li></ul>
 *     <ul>methods: <li>onCreateView</li> <li>getDateFromEditText</li> <li>showDateTimePicker</li> <li>showToast</li>  <li>closeFragment</li> <li>onActivityResult</li> <li>openFacilityFragment</li></ul>
 * </p>
 * @author TO-DO
 * @see Fragment
 * @see Facility
 * @see FacilityManager
 * @see <a href="https://www.geeksforgeeks.org/how-to-get-current-location-inside-android-fragment/"</a>
 * @see <a href="https://stackoverflow.com/questions/72038038/how-to-call-getcurrentlocation-method-of-fusedlocationproviderclient-in-kotlin"</a>
 */
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

    private boolean buttonClicked = false;

    /**
     * Construct the create_event fragment view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Constructed View
     * @throws Exception error checking facility
     * @see Fragment
     */
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
            if (buttonClicked) {
                return;
            }

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

            buttonClicked = true;

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

    /**
     * gets the date for the event
     * @param editText EditText for date
     * @return Date for the event
     * @throws Exception ParseException
     */
    private Date getDateFromEditText(EditText editText) {
        String dateText = editText.getText().toString();
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateText);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * sets the date and time for the various event dates
     * @param editText The editText event date that will be broken into its components.
     */
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

    /**
     * show a pop up toast message
     * @param message The String message that will be displayed as a toast message.
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns user back to the home screen
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
                eventImage.setImageURI(imageURI);
            }
        }
    }

    /**
     * Opens create event screen
     * @throws Exception activity not instance of MainActivity
     * @see MainActivity
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