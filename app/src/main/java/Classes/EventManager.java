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




public class EventManager {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    public EventManager(){
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
    }
    //public boolean checkEventExists(String deviceID){return Boolean.TRUE;}
    public Event createEventFromDocument(DocumentSnapshot document){
        /**
         * create an event object from information stored in firebase
         */
        String organizerID = document.getString("organizerID");
        String eventTitle = document.getString("eventTitle");
        Date regOpenDate = document.getDate("regOpenDate");
        Date regDeadlineDate = document.getDate("regDeadlineDate");
        Date eventStartDate = document.getDate("eventStartDate");
        //Integer maxWinners = document.getInteger("maxWinners");
        //Integer maxEntrants = document.getInteger("maxEntrants");

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
        //List<String> events = document.contains("events") ? (List<String>) document.get("events") : new ArrayList<>(); //Facility has list of events

        // Create and return the Event object
        return new Event(organizerID,
                eventTitle,
                regOpenDate,
                regDeadlineDate,
                eventStartDate,
                maxWinners,
                maxEntrants,
                eventDetails,
                eventPictureURL,
                enableGeolocation);
    }

    public void addEvent(Event event){
        /**
         * adds event to firebase
         */
        if (!checkEventExists(event.getOrganizerID())){
            //only add if event doesn't already exist
            eventsRef.document(event.getOrganizerID()).set(event);
        }
    }

    public void updateEvent(Event event, String organizerID){
        /**
         * updates the firebase storage with the information from the given event object
         */
        if (checkEventExists(event.getOrganizerID())){
            //only update if event exists
            eventsRef.document(organizerID).set(event);
        }else{
            addEvent(event);
        }
    }

    public void deleteEvent(Event event){}//to be implemented

    public boolean checkEventExists(String organizerID){
        return true;
    }
}