package Classes;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NotificationManager
 *
 * NotificationManager class handles interactions between the local instance(s) of a given facility with the firestore database
 *
 * @author Rehan, Rafi
 * @see Notification
 */
public class NotificationManager {

    private final FirebaseFirestore db;

    /**
     * Empty constructor
     */
    public NotificationManager() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Parses a notification string into a Notification object
     * @param notificationString contains notification information
     * @return Notification object
     */
    public Notification parseNotification(String notificationString) {
        String[] parts = notificationString.split("\\$");

        if (parts.length != 7) {
//            throw new IllegalArgumentException("Invalid format");
            return null;
        }


        String message = parts[0];
        String day = parts[1];
        String month = parts[2];
        String time = parts[3];
        String eventID = parts[4];
        String title = parts[5];
        boolean notified = parts[6].equals("1");

        return new Notification(title, day, month, time, eventID, message, notified);
    }

    /**
     * Unparses a notification object into a string
     * @param notification Notification object
     * @return String containing notification information
     */
    public String unparseNotification(Notification notification) {
        String message = notification.getMessage() + "$" +
                notification.getEventStartDateDay() + "$" +
                notification.getEventStartDateMonth() + "$" +
                notification.getEventStartDateTime() + "$" +
                notification.getEventID() + "$" +
                notification.getEventTitle() + "$" +
                (notification.getNotified() ? "1" : "0");
        return message;
    }

    /**
     * Adds a notification to the user's notifications
     * @param userID the user's ID
     * @param notification the notification to add
     * @param listener the listener to call when the notification is added
     */
    public void addNotification(String userID, String notification, OnNotificationUpdateListener listener) {
        db.runTransaction(transaction -> {
            // Get the user document
            DocumentSnapshot snapshot = transaction.get(db.collection("users").document(userID));

            // If the user document doesn't exist, return
            if (!snapshot.exists()) {
                return null;
            }

            // If the users notification enabled is false, return
            Boolean allowNotifications = snapshot.getBoolean("allowNotifications");
            if (!allowNotifications) {
                return null;
            }

            // Get current notifications
            List<String> notificationsArray = (List<String>) snapshot.get("notificationsArray");

            // Add the new notification
            notificationsArray.add(notification);

            // Update the database
            transaction.update(db.collection("users").document(userID), "notificationsArray", notificationsArray);
            return null;
        }).addOnSuccessListener(aVoid -> {
            listener.onSuccess("Notification added successfully");
        }).addOnFailureListener(listener::onError);
    }

    /**
     * Updates the notified field of a notification
     * @param userID the user's ID
     * @param listener the listener to call when the notification is updated
     */
    public void updateNotified(String userID, OnNotificationUpdateListener listener) {
        db.runTransaction(transaction -> {
            // Get the user document
            DocumentSnapshot snapshot = transaction.get(db.collection("users").document(userID));

            // Get current notifications
            List<String> notificationsArray = (List<String>) snapshot.get("notificationsArray");

            // Change all strings last character from 0 to 1
            for (int i = 0; i < notificationsArray.size(); i++) {
                String notification = notificationsArray.get(i);
                String newNotification = notification.substring(0, notification.length() - 1) + "1";
                notificationsArray.set(i, newNotification);
            }

            // Update the database
            transaction.update(db.collection("users").document(userID), "notificationsArray", notificationsArray);
            return null;
        }).addOnSuccessListener(aVoid -> {
            listener.onSuccess("Notification removed successfully");
        }).addOnFailureListener(listener::onError);
    }

    /**
     * Removes a notification from the user's notifications
     * @param userID the user's ID
     * @param notification the notification to remove
     * @param listener the listener to call when the notification is removed
     */
    public void removeNotification(String userID, String notification, OnNotificationUpdateListener listener) {
        db.runTransaction(transaction -> {
            // Get the user document
            DocumentSnapshot snapshot = transaction.get(db.collection("users").document(userID));

            // Get current notifications
            List<String> notificationsArray = (List<String>) snapshot.get("notificationsArray");

            // Remove the notification
            if (notificationsArray.contains(notification)) {
                notificationsArray.remove(notification);
            }
            // Update the database
            transaction.update(db.collection("users").document(userID), "notificationsArray", notificationsArray);
            return null;
        }).addOnSuccessListener(aVoid -> {
            listener.onSuccess("Notification removed successfully");
        }).addOnFailureListener(listener::onError);
    }

    /**
     * Interface for handling the result of adding a notification.
     */
    public interface OnNotificationUpdateListener {
        void onSuccess(String message);
        void onError(Exception e);
    }

}

