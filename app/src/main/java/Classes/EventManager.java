package Classes;
import static java.security.AccessController.getContext;

import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/** CHANGE COMMENTS !?!?!? **/

public class EventManager {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    public EventManager(){
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
    }

    public Event createEventFromDocument(DocumentSnapshot document){
        String organizerID = document.getString("organizerID");
        String eventTitle = document.getString("eventTitle");
        Date regOpenDate = document.getDate("regOpenDate");
        Date regDeadlineDate = document.getDate("regDeadlineDate");
        Date eventStartDate = document.getDate("eventStartDate");

        String maxWinnersString = document.getString("maxWinners");
        Integer maxWinners = Integer.parseInt(maxWinnersString);

        Integer maxEntrants = null;
        String maxEntrantsString = document.getString("maxEntrants");
        if (maxEntrantsString != null) {
            maxEntrants = Integer.parseInt(maxEntrantsString);
        }

        String eventDetails = document.getString("eventDetails");
        String eventPictureURL = document.getString("eventPictureURL");
        boolean enableGeolocation = document.getBoolean("enableGeolocation");
        List<String> waitingList = (List<String>) document.get("waitingList");
        List<String> chosenEntrantsList = (List<String>) document.get("chosenEntrantsList");
        List<String> CancelledEntrantsList = (List<String>) document.get("CancelledEntrantsList");
        List<String> finalList = (List<String>) document.get("finalList");

        // Create and return the Event object
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
                waitingList,
                chosenEntrantsList,
                CancelledEntrantsList,
                finalList,
                null
        );
    }

    public void addEvent(Event event){
        String docID = eventsRef.document().getId();
        event.setQRCode(docID);
        eventsRef.document(docID).set(event);
    }

    // TODO: implement updateEvent()
    public void updateEvent(Event event, String organizerID){
    }

    public void deleteEvent(Event event){
        eventsRef.document(event.getOrganizerID()).delete();
    }

    public void getEvent(String eventID, final OnEventFetchListener callback){
        assert db != null;
        db.collection("events").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Event event = createEventFromDocument(document);
                            callback.onEventFetched(event);
                        } else {
                            callback.onEventFetched(null);  // No user profile found
                        }
                    } else {
                        callback.onEventFetchError(task.getException());
                    }
                });
    }

    public interface OnEventFetchListener {
        void onEventFetched(Event event);
        void onEventFetchError(Exception e);
    }

}