package Classes;

import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Objects;

/**
 * <h1>EventManager</h1>
 * <p>
 *     The <code>EventManager</code> class manages operations related to events within the application.
 *     It includes methods for creating, retrieving, updating, and managing event data stored in Firebase Firestore and Firebase Storage.
 * </p>
 * @author Rehan
 * @see Event
 */
public class EventManager {
    private final FirebaseFirestore db;
    private final CollectionReference eventsRef;
    private final CollectionReference qrCodesRef;
    private final FirebaseStorage storage;
    private QRcodeManager qrcodeManager;
    private EventListsManager eventListsManager;

    /**
     * Initializes EventManager with Firestore and Firebase Storage references.
     */
    public EventManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        qrCodesRef = db.collection("qrcodes");
        storage = FirebaseStorage.getInstance();
        qrcodeManager = new QRcodeManager();
        eventListsManager = new EventListsManager();
    }

    /**
     * Creates an Event object from a Firestore document.
     *
     * @param document The Firestore document representing an event
     * @return An Event object populated with the data from the document
     */
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
        String listReference = document.getString("listReference");
        String locationReference = document.getString("locationReference");
        String QRCode = document.getString("qrcode");

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

    /**
     * Adds a new event to Firestore, uploads its picture if provided, and updates the QR code and event list.
     *
     * @param event            The event to be added
     * @param qrcode           The QR code associated with the event
     * @param eventLists       The event lists object for managing event lists
     * @param imageURI         URI of the event picture to upload
     * @param uploadListener   Listener for handling picture upload success or error
     * @return The document ID of the newly added event
     */
    public String addEvent(Event event, QRcode qrcode, EventLists eventLists, Uri imageURI, OnUploadPictureListener uploadListener) {
        String docID = eventsRef.document().getId();
        eventLists.setEventID(docID);
        qrcode.setEventID(docID);

        String QRCodeID = qrcodeManager.addQRcode(qrcode);
        event.setQRCode(QRCodeID);

        eventListsManager.addEventLists(eventLists);

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

        return docID;
    }

    /**
     * Retrieves an event by ID and executes a callback with the fetched event.
     *
     * @param eventID  The ID of the event to fetch
     * @param callback Callback for handling the event fetch result or error
     */
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

    /**
     * Uploads an event picture to Firebase Storage and updates Firestore with the image URL.
     *
     * @param picUri    The URI of the picture to upload
     * @param docID     The document ID of the event to update
     * @param listener  Callback for upload success or error
     */
    public void uploadEventPicture(Uri picUri, String docID, OnUploadPictureListener listener) {
        StorageReference storageRef = storage.getReference().child("eventPictures/" + docID);

        storageRef.putFile(picUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updateEventPictureInFirestore(docID, downloadUrl, new OnUpdatePictureListener() {
                            @Override
                            public void onSuccess() {
                                listener.onSuccess(uri);
                            }

                            @Override
                            public void onError(Exception e) {
                                listener.onError(e);
                            }
                        });
                    }).addOnFailureListener(listener::onError);
                })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Updates the event picture URL in Firestore for a given event.
     *
     * @param eventID   The ID of the event to update
     * @param picURL    The URL of the new picture to set in Firestore
     * @param listener  Callback for update success or error
     */
    public void updateEventPictureInFirestore(String eventID, String picURL, OnUpdatePictureListener listener) {
        db.collection("events").document(eventID)
                .update("eventPictureURL", picURL)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    /**
     * Callback interface for picture upload events.
     */
    public interface OnUploadPictureListener {
        void onSuccess(Uri downloadUrl);
        void onError(Exception e);
    }

    /**
     * Callback interface for picture update events.
     */
    public interface OnUpdatePictureListener {
        void onSuccess();
        void onError(Exception e);
    }

    /**
     * Callback interface for event fetch results.
     */
    public interface OnEventFetchListener {
        void onEventFetched(Event event);
        void onEventFetchError(Exception e);
    }
}
