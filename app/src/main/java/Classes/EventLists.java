package Classes;

import java.util.List;

public class EventLists {
    private String eventID;
    private Integer maxWinners;
    private Integer maxEntrants;
    private boolean enableGeolocation;
    private List<String> waitingList;
    private List<String> chosenList;
    private List<String> cancelledList;
    private List<String> winnersList;
    private List<String> locationList;

    public EventLists() {}
    public EventLists(String eventID, Integer maxWinners, Integer maxEntrants, boolean enableGeolocation, List<String> waitingList, List<String> chosenList, List<String> cancelledList, List<String> winnersList, List<String> locationList) {
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
    public List<String> getLocationList() {
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
    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }

}
