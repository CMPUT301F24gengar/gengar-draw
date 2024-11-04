package Classes;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

    public interface OnEventListsFetchListener {
        void onEventListsFetched(EventLists eventLists);
        void onEventListsFetchError(Exception e);
    }

}
