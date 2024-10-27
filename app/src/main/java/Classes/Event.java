package Classes;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Event {
    //private String organizerEmail;
    private String organizerID;
    private String eventTitle;
    private Date regOpenDate;
    private Date regDeadlineDate;
    private Date eventStartDate;
    private Integer maxWinners;
    private Integer maxEntrants;
    private String eventDetails;
    private String eventPictureURL;
    private boolean enableGeolocation;

    private List<String> waitingList;
    private List<String> chosenEntrantsList;
    private List<String> CancelledEntrantsList;
    private List<String> finalList;

    private String QRCode;

    public Event() {}

    public Event(String organizerID, String eventTitle, Date regOpenDate, Date regDeadlineDate, Date eventStartDate, Integer maxWinners, Integer maxEntrants, String eventDetails, String eventPictureURL, boolean enableGeolocation, List<String> waitingList, List<String> chosenEntrantsList, List<String> CancelledEntrantsList, List<String> finalList, String QRCode){
        this.organizerID = organizerID;
        this.eventTitle = eventTitle;
        this.regOpenDate = regOpenDate;
        this.regDeadlineDate = regDeadlineDate;
        this.eventStartDate = eventStartDate;
        this.maxWinners = maxWinners;
        this.maxEntrants = maxEntrants;
        this.eventDetails = eventDetails;
        this.eventPictureURL = eventPictureURL;
        this.enableGeolocation = enableGeolocation;
        this.waitingList = waitingList;
        this.chosenEntrantsList = chosenEntrantsList;
        this.CancelledEntrantsList = CancelledEntrantsList;
        this.finalList = finalList;
        this.QRCode = QRCode;
    }

    public String getOrganizerID() {
        return organizerID;
    }
    public String getEventTitle() {
        return eventTitle;
    }
    public Date getRegOpenDate() {
        return regOpenDate;
    }
    public Date getRegDeadlineDate() {
        return regDeadlineDate;
    }
    public Date getEventStartDate() {
        return eventStartDate;
    }
    public Integer getMaxWinners() {
        return maxWinners;
    }
    public Integer getMaxEntrants() {
        return maxEntrants;
    }
    public String getEventDetails() { return eventDetails; }
    public String getEventPictureURL() { return eventPictureURL; }
    public boolean getEnableGeolocation() { return enableGeolocation; }
    public List<String> getWaitingList() { return waitingList; }
    public List<String> getChosenEntrantsList() { return chosenEntrantsList; }
    public List<String> getCancelledEntrantsList() { return CancelledEntrantsList; }
    public List<String> getFinalList() { return finalList; }
    public String getQRCode() { return QRCode; }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public void setRegOpenDate(Date regOpenDate) {
        this.regOpenDate = regOpenDate;
    }
    public void setRegDeadlineDate(Date regDeadlineDate) { this.regDeadlineDate = regDeadlineDate; }
    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }
    public void setMaxWinners(Integer maxWinners) { this.maxWinners = maxWinners; }
    public void setMaxEntrants(Integer maxEntrants) { this.maxEntrants = maxEntrants; }
    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }
    public void setEventPictureURL(String eventPictureURL) { this.eventPictureURL = eventPictureURL; }
    public void setEnableGeolocation(boolean enableGeolocation) { this.enableGeolocation = enableGeolocation; }
    public void setWaitingList(List<String> waitingList) { this.waitingList = waitingList; }
    public void setChosenEntrantsList(List<String> chosenEntrantsList) { this.chosenEntrantsList = chosenEntrantsList; }
    public void setCancelledEntrantsList(List<String> CancelledEntrantsList) { this.CancelledEntrantsList = CancelledEntrantsList; }
    public void setFinalList(List<String> finalList) { this.finalList = finalList; }
    public void setQRCode(String QRCode) { this.QRCode = QRCode; }
}
