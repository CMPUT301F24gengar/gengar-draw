package Classes;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;

public class NotificationManager {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public NotificationManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void addNotification(String eventTitle, Date eventStartDate, String eventID, String message) {
        Notification notification = new Notification(eventTitle, eventStartDate, eventID, message);

        db.collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference ->
                        Log.d("Firestore", "Notification added with ID: " + documentReference.getId())
                )
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Error adding notification", e)
                );
    }


}
