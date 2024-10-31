package Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;

import Classes.Event;
import Classes.EventManager;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

public class create_event extends Fragment {

    String deviceID;
    Facility facility;

    private ImageView eventImage;
    private EditText titleEditText;
    private EditText registrationOpensEditText, registrationDeadlineEditText, eventStartsEditText;
    private EditText maxWinnersEditText, maxEntrantsEditText;
    private EditText detailsEditText;
    private CheckBox checkboxCheckBox;
    private TextView createBtn;
    private TextView cancelBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
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

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        FacilityManager facilityManager = new FacilityManager();
        EventManager eventManager = new EventManager();

        registrationOpensEditText.setOnClickListener(v -> showDateTimePicker(registrationOpensEditText));
        registrationDeadlineEditText.setOnClickListener(v -> showDateTimePicker(registrationDeadlineEditText));
        eventStartsEditText.setOnClickListener(v -> showDateTimePicker(eventStartsEditText));

        createBtn.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();

            Date registrationOpens = getDateFromEditText(registrationOpensEditText);
            Date registrationDeadline = getDateFromEditText(registrationDeadlineEditText);
            Date eventStarts = getDateFromEditText(eventStartsEditText);

            String maxWinners = maxWinnersEditText.getText().toString();
            String maxEntrants = maxEntrantsEditText.getText().toString();

            String details = detailsEditText.getText().toString();
            boolean enableGeolocation = checkboxCheckBox.isChecked();

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
            eventManager.addEvent(event);

            // TODO : Add event to facility's event list


            Toast.makeText(getContext(), "Event created successfully", Toast.LENGTH_SHORT).show();
            closeFragment();
        });

        cancelBtn.setOnClickListener(v -> {
            closeFragment();
        });


        return view;
    }

    private void showDateTimePicker(final EditText editText) {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);

            new TimePickerDialog(requireContext(), (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);

                editText.setText(String.format(Locale.getDefault(), "%02d/%02d/%d %02d:%02d",
                        date.get(Calendar.MONTH) + 1,
                        date.get(Calendar.DAY_OF_MONTH),
                        date.get(Calendar.YEAR),
                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE)));
            },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false).show();

        },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DATE)).show();
    }

    private Date getDateFromEditText(EditText editText) {
        String dateString = editText.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());

        try {
            Date date = format.parse(dateString);
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.MILLISECOND, 0);  // Zero out milliseconds
                return cal.getTime();
            }
            return null;
        } catch (ParseException e) {
            return null;  // Return null if parsing fails
        }
    }

    // Go back to the home screen
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

}