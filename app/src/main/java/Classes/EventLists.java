package Classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public EventLists() {}

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

    public String getEventID() {
        return eventID;
    }
    public Integer getMaxWinners() {
        return maxWinners;
    }
    public Integer getMaxEntrants() {
        return maxEntrants;
    }
    public boolean getEnableGeolocation() {
        return enableGeolocation;
    }
    public List<String> getWaitingList() {
        return waitingList;
    }
    public List<String> getChosenList() {
        return chosenList;
    }
    public List<String> getCancelledList() {
        return cancelledList;
    }
    public List<String> getWinnersList() {
        return winnersList;
    }
    public Map<String, Object> getLocationList() {
        return locationList;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public void setMaxWinners(Integer maxWinners) {
        this.maxWinners = maxWinners;
    }
    public void setMaxEntrants(Integer maxEntrants) {
        this.maxEntrants = maxEntrants;
    }
    public void setEnableGeolocation(boolean enableGeolocation) {
        this.enableGeolocation = enableGeolocation;
    }
    public void setWaitingList(List<String> waitingList) {
        this.waitingList = waitingList;
    }
    public void setChosenList(List<String> chosenList) {
        this.chosenList = chosenList;
    }
    public void setCancelledList(List<String> cancelledList) {
        this.cancelledList = cancelledList;
    }
    public void setWinnersList(List<String> winnersList) {
        this.winnersList = winnersList;
    }
    public void setLocationList(Map<String, Object> locationList) {
        this.locationList = locationList;
    }

    public void addToWaitingList(String userID) {
        if (!waitingList.contains(userID) && !isWaitingListFull() ) {
            waitingList.add(userID);
        }
    }

    public void addToChosenList(String userID) {
        if (!chosenList.contains(userID) && !isChosenListFull() ) {
            chosenList.add(userID);
        }
    }

    public void addToCancelledList(String userID) {
        if (!cancelledList.contains(userID)) {
            cancelledList.add(userID);
        }
    }

    public void addToWinnersList(String userID) {
        if (!winnersList.contains(userID)) {
            winnersList.add(userID);
        }
    }

    public void removeFromWaitingList(String userID) {
        waitingList.remove(userID);
    }

    public void removeFromChosenList(String userID) {
        chosenList.remove(userID);
    }

    public void removeFromCancelledList(String userID) {
        cancelledList.remove(userID);
    }

    public void removeFromWinnersList(String userID) {
        winnersList.remove(userID);
    }

    public void removeFromLocationHashmap(String userID) {
        locationList.remove(userID);
    }

    public boolean isWaitingListFull() {
        if (maxEntrants == null) {
            return false;
        }
        return waitingList.size() >= maxEntrants;
    }

    public boolean isChosenListFull() {
        return chosenList.size() >= maxWinners;
    }

}
