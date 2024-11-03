package Classes;

import java.util.Date;

public class Event {
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
    private String ListReference; // Stores the docID of the EventLists
    private String LocationReference; // Stores the docID of the Locations;
    private String QRCode;

    public Event() {}

    public Event(String organizerID, String eventTitle, Date regOpenDate, Date regDeadlineDate, Date eventStartDate, Integer maxWinners, Integer maxEntrants, String eventDetails, String eventPictureURL, boolean enableGeolocation, String listReference, String LocationReference, String QRCode){
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
        this.ListReference = listReference;
        this.LocationReference = LocationReference;
        this.QRCode = QRCode;
    }

    public String getOrganizerID() {
        return organizerID;
    }
    public String getEventTitle() {
        return eventTitle;
    }
    public CharSequence getRegOpenDate() {
        return (CharSequence) regOpenDate;
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
    public String getListReference() { return ListReference; }
    public String getLocationReference() { return LocationReference; }
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
    public void setListReference(String listReference) { this.ListReference = listReference; }
    public void setLocationReference(String locationReference) { this.LocationReference = locationReference; }
    public void setQRCode(String QRCode) { this.QRCode = QRCode; }
}
