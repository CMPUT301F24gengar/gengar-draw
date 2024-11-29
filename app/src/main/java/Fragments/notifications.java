package Fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gengardraw.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import Adapters.NotificationAdapter;
import Classes.Notification;
import Classes.UserProfile;
import Classes.UserProfileManager;

public class notifications extends Fragment {

    private String channelId = "notification_channel";

    private String deviceID;
    private boolean buttonDebounce = false;

    private ImageView bellButton;

    private FirebaseFirestore db;
    private RecyclerView notificationsRecyclerView;
    private Classes.NotificationManager notificationManager;
    private NotificationManagerCompat notificationManagerCompat;
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
        notificationManager = new Classes.NotificationManager();
        notifications = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new NotificationAdapter(getContext(), notifications, deviceID, true);

        bellButton = view.findViewById(R.id.notification_button);
        bellButton.setOnClickListener(new View.OnClickListener() {
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
                                bellButton.setImageResource(R.drawable.bell_off);
                            } else {
                                userProfileFetched.setAllowNotifications(true);
                                bellButton.setImageResource(R.drawable.bell);
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

        notificationManagerCompat = NotificationManagerCompat.from(requireContext());
        createNotificationChannel();

        fetchNotifications(new OnNotificationsLoadedListener() {
            @Override
            public void onNotificationsLoaded(ArrayList<Notification> notifications) {
                //adding notifications to adapter
                notificationsRecyclerView.setLayoutManager(layoutManager);
                notificationsRecyclerView.setAdapter(customAdapter);

                // check app notification permissions
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 123);
                }

                for (Notification notification : notifications) {
                    if (!notification.getNotified()) {
                        String contentTitle = notification.getEventTitle() + "[" + notification.getEventStartDateDay() + " " + notification.getEventStartDateMonth() + notification.getEventStartDateTime() + "]";
                        String contentText = notification.getMessage();

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                                .setSmallIcon(R.drawable.bell)
                                .setContentTitle(contentTitle)
                                .setContentText(contentText)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);

                        int notificationId = (int) System.currentTimeMillis();
                        notificationManagerCompat.notify(notificationId, builder.build());
                    }
                }

                notificationManager.updateNotified(deviceID, new Classes.NotificationManager.OnNotificationUpdateListener() {
                    @Override
                    public void onSuccess(String message) {
                        // Handle success
                    }
                    @Override
                    public void onError(Exception e) {
                        // Handle error
                    }
                });
            }
        });

        return view;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification_channel";
            String description = "notification_channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notifManager = requireContext().getSystemService(android.app.NotificationManager.class);
            notifManager.createNotificationChannel(channel);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            // Handle the result of the permission request
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                for (Notification notification : notifications) {
                    if (!notification.getNotified()) {
                        String contentTitle = notification.getEventTitle() + "[" + notification.getEventStartDateDay() + " " + notification.getEventStartDateMonth() + notification.getEventStartDateTime() + "]";
                        String contentText = notification.getMessage();

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                                .setSmallIcon(R.drawable.bell)
                                .setContentTitle(contentTitle)
                                .setContentText(contentText)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);

                        int notificationId = (int) System.currentTimeMillis();
                        notificationManagerCompat.notify(notificationId, builder.build());
                    }
                }

                notificationManager.updateNotified(deviceID, new Classes.NotificationManager.OnNotificationUpdateListener() {
                    @Override
                    public void onSuccess(String message) {
                        // Handle success
                    }
                    @Override
                    public void onError(Exception e) {
                        // Handle error
                    }
                });
            }
        }
    }
}