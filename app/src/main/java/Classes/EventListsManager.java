package Classes;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * EventListsManager
 *
 *     The <code>EventListsManager</code> class manages event lists by interacting with Firebase Firestore. Supports adding, updating,
 *     and removing entries in various event-related lists.
 *
 * @author Rehan
 * @see EventLists
 */
public class EventListsManager {
    private FirebaseFirestore db;

    /**
     * Creates an EventLists object from a Firestore DocumentSnapshot.
     * @param document The Firestore DocumentSnapshot representing the event lists
     * @return An EventLists object populated with the data from the document
     */
    private EventLists createEventListsFromDocument(DocumentSnapshot document) {
        String eventID = document.getString("eventID");
        Integer maxWinners = document.getLong("maxWinners") != null ? document.getLong("maxWinners").intValue() : null;
        Integer maxEntrants = document.getLong("maxEntrants") != null ? document.getLong("maxEntrants").intValue() : null;
        boolean enableGeolocation = document.getBoolean("enableGeolocation");
        List<String> waitingList = document.contains("waitingList") ? (List<String>) document.get("waitingList") : new ArrayList<>();
        List<String> chosenList = document.contains("chosenList") ? (List<String>) document.get("chosenList") : new ArrayList<>();
        List<String> cancelledList = document.contains("cancelledList") ? (List<String>) document.get("cancelledList") : new ArrayList<>();
        List<String> winnersList = document.contains("winnersList") ? (List<String>) document.get("winnersList") : new ArrayList<>();
        Map<String, Object> locationList = document.contains("locationList") ? (Map<String, Object>) document.get("locationList") : new HashMap<>();

        return new EventLists(eventID, maxWinners, maxEntrants, enableGeolocation, waitingList, chosenList, cancelledList, winnersList, locationList);
    }

    /**
     * Default constructor for creating an EventListsManager object.
     */
    public EventListsManager() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Adds an EventLists object to the database.
     * @param eventLists The EventLists object to be added
     */
    public void addEventLists(EventLists eventLists) {
        db.collection("event-lists")
                .document(eventLists.getEventID())
                .set(eventLists);
    }

    /**
     * Updates an existing EventLists object in the database.
     * @param eventLists The updated EventLists object
     */
    public void updateEventLists(EventLists eventLists) {
        db.collection("event-lists")
                .document(eventLists.getEventID())
                .set(eventLists);
    }

    /**
     * Deletes an EventLists object from the database.
     * @param eventID The ID of the EventLists object to be deleted
     */
    public void deleteEventLists(String eventID) {
        db.collection("event-lists")
                .document(eventID)
                .delete();
    }

    /**
     * Retrieves an EventLists object by its ID.
     * @param eventID The ID of the EventLists object to retrieve
     * @param listener The callback for handling the retrieval result
     */
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

    /**
     * Adds a user to the waiting list for an event.
     *
     * @param eventID the ID of the event
     * @param userID the ID of the user to add
     * @param listener a listener to handle success or error
     * @param latitude the latitude for the user's location (optional)
     * @param longitude the longitude for the user's location (optional)
     */
    public void addUserToWaitingList(String eventID, String userID, OnEventListsUpdateListener listener, Double latitude, Double longitude) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean added = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    Integer maxEntrants = eventLists.getMaxEntrants();
                    List<String> waitingList = eventLists.getWaitingList();

                    if (eventLists.getEnableGeolocation() && (latitude == null || longitude == null)) {
                        message.set("No location provided");
                        added.set(false);
                        return null;
                    }

                    if ((maxEntrants == null || waitingList.size() < maxEntrants) && !waitingList.contains(userID)) {
                        eventLists.addToWaitingList(userID);
                        message.set("Joined the waiting list");
                        added.set(true);

//                         If latitude and longitude are provided [enabled geolocation], add them to the location list
                        if (latitude != null && longitude != null) {
                            Map<String, Object> location = eventLists.getLocationList();
                            Map<String, Double> userLocation = new HashMap<>();
                            userLocation.put("latitude", latitude);
                            userLocation.put("longitude", longitude);
                            location.put(userID, userLocation);

                            eventLists.setLocationList(location);
                        }

                        transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    } else {
                        message.set("Waiting list is full");
                        added.set(false);
                    }
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), added.get()); })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Removes a user from the waiting list of an event.
     *
     * @param eventID the ID of the event
     * @param userID the ID of the user to remove
     * @param listener a listener to handle success or error
     */
    public void removeUserFromWaitingList(String eventID, String userID, OnEventListsUpdateListener listener) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean removed = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    List<String> waitingList = eventLists.getWaitingList();

                    if (waitingList.contains(userID)) {
                        eventLists.removeFromWaitingList(userID);
                        message.set("Left the waiting list");
                        removed.set(true);

//                         Remove user from locationList if present
                        Map<String, Object> locationList = eventLists.getLocationList();
                        if (locationList.containsKey(userID)) {
                            locationList.remove(userID);
                            eventLists.setLocationList(locationList);
                        }

                        transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    } else {
                        message.set("Not in the waiting list");
                        removed.set(false);
                    }
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), removed.get()); })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Randomly selects winners from the waiting list and moves them to the chosen list.
     *
     * @param eventID the ID of the event
     * @param listener a listener to handle success or error
     */
    public void chooseWinners(String eventID, OnEventListsUpdateListener listener) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean chosen = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    List<String> waitingList = eventLists.getWaitingList();
                    List<String> chosenList = eventLists.getChosenList();
                    Integer maxWinners = eventLists.getMaxWinners();

                    int slotsLeft = maxWinners - chosenList.size();

                    if (slotsLeft >= waitingList.size()) {
                        if (slotsLeft > 0 && waitingList.isEmpty()) {
                            message.set("Waiting list is empty");
                            chosen.set(false);
                            return null;
                        }
                        slotsLeft = waitingList.size();
                    }

                    if (slotsLeft > 0) {
                        Collections.shuffle(waitingList);
                        List<String> winners = waitingList.subList(0, slotsLeft);

                        chosenList.addAll(winners);
                        waitingList.removeAll(winners); // Remove the winners from the waitingList

                        eventLists.setChosenList(chosenList);
                        eventLists.setWaitingList(waitingList);

                        message.set("Winners chosen");
                        chosen.set(true);

                        transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    } else {
                        message.set("Winners list is full");
                        chosen.set(false);
                    }
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), chosen.get()); })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Adds a user to the cancelled list of an event and removes them from the chosen list.
     *
     * @param eventID the ID of the event
     * @param userID the ID of the user to add to the cancelled list
     * @param listener a listener to handle success or error
     */
    public void addUserToCancelledList(String eventID, String userID, OnEventListsUpdateListener listener) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean cancelled = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    eventLists.removeFromChosenList(userID);
                    eventLists.addToCancelledList(userID);

//                    Remove user from locationList if present
                    Map<String, Object> locationList = eventLists.getLocationList();
                    if (locationList.containsKey(userID)) {
                        locationList.remove(userID);
                        eventLists.setLocationList(locationList);
                    }

                    message.set("Cancelled");
                    cancelled.set(true);

                    transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), cancelled.get()); })
                .addOnFailureListener(listener::onError);
    }

    public void addUsersToCancelledList(String eventID, OnEventListsUpdateListener listener) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean cancelled = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    // remove all users from chosen list and add to cancelled list
                    List<String> chosenList = eventLists.getChosenList();
                    List<String> cancelledList = eventLists.getCancelledList();

                    cancelledList.addAll(chosenList);

//                    Remove user from locationList if present
                    Map<String, Object> locationList = eventLists.getLocationList();
                    for (String userID : chosenList) {
                        if (locationList.containsKey(userID)) {
                            locationList.remove(userID);
                        }
                    }
                    eventLists.setLocationList(locationList);
                    chosenList.clear();

                    message.set("Cancelled");
                    cancelled.set(true);

                    transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), cancelled.get()); })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Adds a user to the winners list of an event and removes them from the chosen list.
     *
     * @param eventID the ID of the event
     * @param userID the ID of the user to add to the winners list
     * @param listener a listener to handle success or error
     */
    public void addUserToWinnersList(String eventID, String userID, OnEventListsUpdateListener listener) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean added = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    eventLists.removeFromChosenList(userID);
                    eventLists.addToWinnersList(userID);

                    message.set("Accepted");
                    added.set(true);

                    transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), added.get()); })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Removes a user from the chosen list of an event.
     *
     * @param eventID the ID of the event
     * @param userID the ID of the user to remove from the chosen list
     * @param listener a listener to handle success or error
     */
    public void removeUserFromChosenList(String eventID, String userID, OnEventListsUpdateListener listener) {
        AtomicReference<String> message = new AtomicReference<>();
        AtomicBoolean added = new AtomicBoolean(false);
        db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("event-lists").document(eventID));
                    EventLists eventLists = createEventListsFromDocument(snapshot);

                    eventLists.removeFromChosenList(userID);

//                    Remove user from locationList if present
                    Map<String, Object> locationList = eventLists.getLocationList();
                    if (locationList.containsKey(userID)) {
                        locationList.remove(userID);
                        eventLists.setLocationList(locationList);
                    }

                    message.set("Declined");
                    added.set(false);

                    transaction.set(db.collection("event-lists").document(eventID), eventLists);
                    return null;
                })
                .addOnSuccessListener(aVoid -> { listener.onSuccess(message.get(), added.get()); })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Listener interface for fetching event data.
     */
    public interface OnEventListsFetchListener {
        void onEventListsFetched(EventLists eventLists);
        void onEventListsFetchError(Exception e);
    }

    /**
     * Listener interface for updating event data.
     */
    public interface OnEventListsUpdateListener {
        void onSuccess(String message, boolean boolValue);
        void onError(Exception e);
    }

}