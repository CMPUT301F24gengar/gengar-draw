package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gengardraw.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Classes.Notification;
import Classes.NotificationManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

public class notifications extends Fragment {

    private String deviceID;
    private boolean buttonDebounce;

    private ImageView bell;

    private FirebaseFirestore db;
    private RecyclerView notificationsRecyclerView;
    private NotificationManager notificationManager;
    private ArrayList<Notification> notifications;

    private UserProfileManager userProfileManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();
        notificationsRecyclerView = view.findViewById(R.id.notifications_list);
        notificationManager = new NotificationManager();

        bell = view.findViewById(R.id.notification_button);
        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonDebounce) {
                    buttonDebounce = true;
                    // set users allownotifications to false if true and true if false
                    userProfileManager = new UserProfileManager();
                    userProfileManager.getUserProfile(deviceID, new UserProfileManager.OnUserProfileFetchListener() {
                        @Override
                        public void onUserProfileFetched(UserProfile userProfileFetched) {
                            if (userProfileFetched.isAllowNotifications()) {
                                userProfileFetched.setAllowNotifications(false);
                                bell.setImageResource(R.drawable.bell_off);
                            } else {
                                userProfileFetched.setAllowNotifications(true);
                                bell.setImageResource(R.drawable.bell);
                            }
                            userProfileManager.updateUserProfile(userProfileFetched, deviceID);
                            buttonDebounce = false;
                        }
                        @Override
                        public void onUserProfileFetchError(Exception e) {
                            //Handle the error
                            buttonDebounce = false;
                        }
                    });
                }
            }
        });

        return view;
    }
}