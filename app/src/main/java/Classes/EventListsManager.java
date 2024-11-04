package Classes;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListsManager {
    private FirebaseFirestore db;

    private EventLists createEventListsFromDocument(DocumentSnapshot document) {
        String eventID = document.getString("eventID");
        Integer maxWinners = document.getLong("maxWinners").intValue();
        Integer maxEntrants = document.getLong("maxEntrants").intValue();
        boolean enableGeolocation = document.getBoolean("enableGeolocation");
        List<String> waitingList = document.contains("waitingList") ? (List<String>) document.get("waitingList") : new ArrayList<>();
        List<String> chosenList = document.contains("chosenList") ? (List<String>) document.get("chosenList") : new ArrayList<>();
        List<String> cancelledList = document.contains("cancelledList") ? (List<String>) document.get("cancelledList") : new ArrayList<>();
        List<String> winnersList = document.contains("winnersList") ? (List<String>) document.get("winnersList") : new ArrayList<>();
        Map<String, Object> locationList = document.contains("locationList") ? (Map<String, Object>) document.get("locationList") : new HashMap<>();

        return new EventLists(eventID, maxWinners, maxEntrants, enableGeolocation, waitingList, chosenList, cancelledList, winnersList, locationList);
    }

    public EventListsManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void addEventLists(EventLists eventLists) {
        db.collection("event-lists")
                .document(eventLists.getEventID())
                .set(eventLists);
    }

    public void updateEventLists(EventLists eventLists) {
        db.collection("event-lists")
                .document(eventLists.getEventID())
                .set(eventLists);
    }

    public void deleteEventLists(String eventID) {
        db.collection("event-lists")
                .document(eventID)
                .delete();
    }

    public void getEventLists(String eventID, OnEventListsFetchListener listener) {
        db.collection("event-lists").document(eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            EventLists eventLists = createEventListsFromDocument(document);
                            listener.onEventListsFetched(eventLists);
                        } else {
                            listener.onEventListsFetched(null); // No facility found
                        }
                    } else {
                        listener.onEventListsFetchError(task.getException());
                    }
                });
    }

    public void addUserToWaitingList(String eventID, String userID, OnEventListsUpdateListener listener, Double latitude, Double longitude) {
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    Integer maxEntrants = eventLists.getMaxEntrants();
                    List<String> waitingList = eventLists.getWaitingList();

                    if ((maxEntrants == null || waitingList.size() < maxEntrants) && !waitingList.contains(userID)) {
                        eventLists.addToWaitingList(userID);

                        // If latitude and longitude are provided [enabled geolocation], add them to the location list
                        if (latitude != null && longitude != null) {
                            Map<String, Object> location = eventLists.getLocationList();
                            Map<String, Double> userLocation = new HashMap<>();
                            userLocation.put("latitude", latitude);
                            userLocation.put("longitude", longitude);
                            location.put(userID, userLocation);

                            eventLists.setLocationList(location);
                        }

                        transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    }
                    return null;
                })
                .addOnSuccessListener(aVoid -> {listener.onSuccess();})
                .addOnFailureListener(listener::onError);
    }

    public void removeUserFromWaitingList(String eventID, String userID, OnEventListsUpdateListener listener) {
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    List<String> waitingList = eventLists.getWaitingList();

                    if (waitingList.contains(userID)) {
                        eventLists.getWaitingList().remove(userID);

                        // Remove user from locationList if present
//                        Map<String, Object> locationList = eventLists.getLocationList();
//                        if (locationList.containsKey(userID)) {
//                            locationList.remove(userID);
//                            eventLists.setLocationList(locationList);
//                        }

                        transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    }
                    return null;
                })
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    public interface OnEventListsFetchListener {
        void onEventListsFetched(EventLists eventLists);
        void onEventListsFetchError(Exception e);
    }

    public interface OnEventListsUpdateListener {
        void onSuccess();
        void onError(Exception e);
    }

}
