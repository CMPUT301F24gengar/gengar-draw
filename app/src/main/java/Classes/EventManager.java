package Classes;

import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class EventManager {
    private final FirebaseFirestore db;
    private final CollectionReference eventsRef;
    private final FirebaseStorage storage;

    public EventManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        storage = FirebaseStorage.getInstance();
    }

    public Event createEventFromDocument(DocumentSnapshot document) {
        String organizerID = document.getString("organizerID");
        String eventTitle = document.getString("eventTitle");
        Date regOpenDate = document.getDate("regOpenDate");
        Date regDeadlineDate = document.getDate("regDeadlineDate");
        Date eventStartDate = document.getDate("eventStartDate");

        Integer maxWinners = document.getLong("maxWinners") != null ? document.getLong("maxWinners").intValue() : null;
        Integer maxEntrants = document.getLong("maxEntrants") != null ? document.getLong("maxEntrants").intValue() : null;

        String eventDetails = document.getString("eventDetails");
        String eventPictureURL = document.getString("eventPictureURL");
        boolean enableGeolocation = Boolean.TRUE.equals(document.getBoolean("enableGeolocation"));
        String listReference = document.getString("ListReference");
        String locationReference = document.getString("LocationReference");
        String QRCode = document.getString("QRCode");

        return new Event(
                organizerID,
                eventTitle,
                regOpenDate,
                regDeadlineDate,
                eventStartDate,
                maxWinners,
                maxEntrants,
                eventDetails,
                eventPictureURL,
                enableGeolocation,
                listReference,
                locationReference,
                QRCode
        );
    }

    public void addEvent(Event event, Uri imageURI, OnUploadPictureListener uploadListener) {
        String docID = eventsRef.document().getId();
        event.setQRCode(docID);

        eventsRef.document(docID).set(event).addOnSuccessListener(aVoid -> {
            if (imageURI != null) {
                uploadEventPicture(imageURI, docID, new OnUploadPictureListener() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        updateEventPictureInFirestore(docID, downloadUrl.toString(), new OnUpdatePictureListener() {
                            @Override
                            public void onSuccess() {
                                uploadListener.onSuccess(downloadUrl);
                            }
                            @Override
                            public void onError(Exception e) {
                                uploadListener.onError(e);
                            }
                        });
                    }
                    @Override
                    public void onError(Exception e) {
                        uploadListener.onError(e);
                    }
                });
            }
        });

        // TODO : Add list reference and location reference to event object AND change QR code logic
    }

    public void getEvent(String eventID, OnEventFetchListener callback) {
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        callback.onEventFetched(createEventFromDocument(task.getResult()));
                    } else {
                        callback.onEventFetched(null);
                    }
                })
                .addOnFailureListener(callback::onEventFetchError);
    }

    public void uploadEventPicture(Uri picUri, String docID, OnUploadPictureListener listener) {
        StorageReference storageRef = storage.getReference().child("eventPictures/" + docID);
        storageRef.putFile(picUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(listener::onSuccess))
                .addOnFailureListener(listener::onError);
    }

    public void updateEventPictureInFirestore(String eventID, String picURL, OnUpdatePictureListener listener) {
        db.collection("events").document(eventID)
                .update("eventPictureURL", picURL)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    public interface OnUploadPictureListener {
        void onSuccess(Uri downloadUrl);
        void onError(Exception e);
    }

    public interface OnUpdatePictureListener {
        void onSuccess();
        void onError(Exception e);
    }

    public interface OnEventFetchListener {
        void onEventFetched(Event event);
        void onEventFetchError(Exception e);
    }
}
