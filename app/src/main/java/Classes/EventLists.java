package Classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EventLists
 *
 *     The <code>EventLists</code> class is responsible for managing lists related to event participants,
 *     including waiting lists, chosen lists, cancelled lists, and winners lists. This class also tracks
 *     maximum limits for entrants and winners, as well as optional geolocation information for participants.
 * 
 * @author Rehan
 * @see EventListsManager
 */
public class EventLists {
    private String eventID;
    private Integer maxWinners;
    private Integer maxEntrants;
    private boolean enableGeolocation;
    private List<String> waitingList;
    private List<String> chosenList;
    private List<String> cancelledList;
    private List<String> winnersList;
    Map<String, Object> locationList;

    /**
     * Default constructor for creating an empty EventLists object.
     */
    public EventLists() {}

    /**
     * Constructs an EventLists object with specified parameters.
     *
     * @param eventID           Unique ID of the event
     * @param maxWinners        Maximum number of winners allowed
     * @param maxEntrants       Maximum number of entrants allowed
     * @param enableGeolocation Boolean flag to enable or disable geolocation for participants
     * @param waitingList       List of users currently in the waiting list
     * @param chosenList        List of users chosen for the event
     * @param cancelledList     List of users who have cancelled their participation
     * @param winnersList       List of users who are winners of the event
     * @param locationList      Map containing location data for each user
     */
    public EventLists(String eventID, Integer maxWinners, Integer maxEntrants, boolean enableGeolocation, List<String> waitingList, List<String> chosenList, List<String> cancelledList, List<String> winnersList, Map<String, Object> locationList) {
        this.eventID = eventID;
        this.maxWinners = maxWinners;
        this.maxEntrants = maxEntrants;
        this.enableGeolocation = enableGeolocation;
        this.waitingList = waitingList;
        this.chosenList = chosenList;
        this.cancelledList = cancelledList;
        this.winnersList = winnersList;
        this.locationList = locationList;
    }

    /** @return The event ID */
    public String getEventID() { return eventID; }

    /** @return Maximum number of winners allowed */
    public Integer getMaxWinners() { return maxWinners; }

    /** @return Maximum number of entrants allowed */
    public Integer getMaxEntrants() { return maxEntrants; }

    /** @return True if geolocation is enabled, false otherwise */
    public boolean getEnableGeolocation() { return enableGeolocation; }

    /** @return List of users currently on the waiting list */
    public List<String> getWaitingList() { return waitingList; }

    /** @return List of users chosen for the event */
    public List<String> getChosenList() { return chosenList; }

    /** @return List of users who have cancelled their participation */
    public List<String> getCancelledList() { return cancelledList; }

    /** @return List of users who are winners of the event */
    public List<String> getWinnersList() { return winnersList; }

    /** @return Map containing location data for each user */
    public Map<String, Object> getLocationList() { return locationList; }

    /** Sets the event ID */
    public void setEventID(String eventID) { this.eventID = eventID; }

    /** Sets the maximum number of winners */
    public void setMaxWinners(Integer maxWinners) { this.maxWinners = maxWinners; }

    /** Sets the maximum number of entrants */
    public void setMaxEntrants(Integer maxEntrants) { this.maxEntrants = maxEntrants; }

    /** Enables or disables geolocation */
    public void setEnableGeolocation(boolean enableGeolocation) { this.enableGeolocation = enableGeolocation; }

    /** Sets the waiting list */
    public void setWaitingList(List<String> waitingList) { this.waitingList = waitingList; }

    /** Sets the chosen list */
    public void setChosenList(List<String> chosenList) { this.chosenList = chosenList; }

    /** Sets the cancelled list */
    public void setCancelledList(List<String> cancelledList) { this.cancelledList = cancelledList; }

    /** Sets the winners list */
    public void setWinnersList(List<String> winnersList) { this.winnersList = winnersList; }

    /** Sets the location list */
    public void setLocationList(Map<String, Object> locationList) { this.locationList = locationList; }

    /**
     * Adds a user to the waiting list if they are not already on it and the list is not full.
     *
     * @param userID The ID of the user to add to the waiting list
     */
    public void addToWaitingList(String userID) {
        if (!waitingList.contains(userID) && !isWaitingListFull() ) {
            waitingList.add(userID);
        }
    }

    /**
     * Adds a user to the chosen list if they are not already on it and the list is not full.
     *
     * @param userID The ID of the user to add to the chosen list
     */
    public void addToChosenList(String userID) {
        if (!chosenList.contains(userID) && !isChosenListFull() ) {
            chosenList.add(userID);
        }
    }

    /**
     * Adds a user to the cancelled list if they are not already on it.
     *
     * @param userID The ID of the user to add to the cancelled list
     */
    public void addToCancelledList(String userID) {
        if (!cancelledList.contains(userID)) {
            cancelledList.add(userID);
        }
    }

    /**
     * Adds a user to the winners list if they are not already on it.
     *
     * @param userID The ID of the user to add to the winners list
     */
    public void addToWinnersList(String userID) {
        if (!winnersList.contains(userID)) {
            winnersList.add(userID);
        }
    }

    /**
     * Removes a user from the waiting list.
     *
     * @param userID The ID of the user to remove from the waiting list
     */
    public void removeFromWaitingList(String userID) {
        waitingList.remove(userID);
    }

    /**
     * Removes a user from the chosen list.
     *
     * @param userID The ID of the user to remove from the chosen list
     */
    public void removeFromChosenList(String userID) {
        chosenList.remove(userID);
    }

    /**
     * Removes a user from the cancelled list.
     *
     * @param userID The ID of the user to remove from the cancelled list
     */
    public void removeFromCancelledList(String userID) {
        cancelledList.remove(userID);
    }

    /**
     * Removes a user from the winners list.
     *
     * @param userID The ID of the user to remove from the winners list
     */
    public void removeFromWinnersList(String userID) {
        winnersList.remove(userID);
    }


    /**
     * Removes a user from the location list.
     *
     * @param userID The ID of the user to remove from the location list
     */
    public void removeFromLocationHashmap(String userID) {
        locationList.remove(userID);
    }

    /**
     * Checks if the waiting list has reached the maximum entrant limit.
     *
     * @return True if the waiting list is full, false otherwise
     */
    public boolean isWaitingListFull() {
        if (maxEntrants == null) {
            return false;
        }
        return waitingList.size() >= maxEntrants;
    }

    /**
     * Checks if the chosen list has reached the maximum winners limit.
     *
     * @return True if the chosen list is full, false otherwise
     */
    public boolean isChosenListFull() {
        return chosenList.size() >= maxWinners;
    }

}
