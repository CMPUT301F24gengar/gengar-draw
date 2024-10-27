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

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

            // TODO : Check if registration opens is before registration deadline etc
            boolean check = (title.isEmpty() || registrationOpens == null || registrationDeadline == null || eventStarts == null || maxWinners.isEmpty() || Integer.parseInt(maxWinners) <= 0 || details.isEmpty());

            if (check) {
                return;
            }

            Integer maxWinnersInt = Integer.parseInt(maxWinners);
            Integer maxEntrantsInt = null;
            if (!maxEntrants.isEmpty()) {
                maxEntrantsInt = Integer.parseInt(maxEntrants);
            }


            Event event = new Event(deviceID, title, registrationOpens, registrationDeadline, eventStarts, maxWinnersInt, maxEntrantsInt, details, null, enableGeolocation, null, null, null, null, null);
            eventManager.addEvent(event);

            // TODO : Add event to facility's event list

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
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH) + 1,
                        date.get(Calendar.DAY_OF_MONTH),
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
            return format.parse(dateString);  // Parses the text into a Date object
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