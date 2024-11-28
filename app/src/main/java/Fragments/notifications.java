package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gengardraw.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Adapters.NotificationAdapter;
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
    private RecyclerView.LayoutManager layoutManager;
    private NotificationAdapter customAdapter;

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
        notifications = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new NotificationAdapter(getContext(), notifications, deviceID, true);

        fetchNotifications(new OnNotificationsLoadedListener() {
            @Override
            public void onNotificationsLoaded(ArrayList<Notification> notifications) {
                //adding notifications to adapter
                notificationsRecyclerView.setLayoutManager(layoutManager);
                notificationsRecyclerView.setAdapter(customAdapter);
            }
        });

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

    public interface OnNotificationsLoadedListener {
        void onNotificationsLoaded(ArrayList<Notification> notifications);
    }

    public void fetchNotifications(OnNotificationsLoadedListener listener){
        // get userprofile
        userProfileManager = new UserProfileManager();
        userProfileManager.getUserProfile(deviceID, new UserProfileManager.OnUserProfileFetchListener() {
            @Override
            public void onUserProfileFetched(UserProfile userProfile) {
                // get notifications list
                List<String> notificationsArray = userProfile.getNotificationsArray();
                for (String notificationString : notificationsArray) {
                    Notification notification = notificationManager.parseNotification(notificationString);
                    notifications.add(notification);
                }
                listener.onNotificationsLoaded(notifications);
            }
            @Override
            public void onUserProfileFetchError(Exception e) {
                // Handle the error
            }
        });

    }
}