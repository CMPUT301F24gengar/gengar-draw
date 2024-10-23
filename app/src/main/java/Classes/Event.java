package Classes;

import java.time.LocalDateTime;
import java.util.Date;
//import java.util.List;

public class Event {
    //private String organizerEmail;
    private String organizerID;
    private String eventTitle;
    private Date regOpenDate;
    private Date regDeadlineDate;
    private Date eventStartDate;
    //private LocalDateTime eventStartTime;
    private Integer maxWinners;
    private Integer maxEntrants;
    private String eventDetails;
    private String eventPictureURL;
    private boolean enableGeolocation;
    // TODO: save QR code

    public Event() {}

    public Event(String organizerID, String eventTitle, Date regOpenDate, Date regDeadlineDate, Date eventStartDate, Integer maxWinners, Integer maxEntrants, String eventDetails, String eventPictureURL, boolean enableGeolocation){
        //this.organizerEmail = organizerEmail;
        this.organizerID = organizerID;
        this.eventTitle = eventTitle;
        this.regOpenDate = regOpenDate;
        this.regDeadlineDate = regDeadlineDate;
        this.eventStartDate = eventStartDate;
        //this.eventStartTime = eventStartTime;
        this.maxWinners = maxWinners;
        this.maxEntrants = maxEntrants;
        this.eventDetails = eventDetails;
        this.eventPictureURL = eventPictureURL;
        this.enableGeolocation = enableGeolocation;
        // TODO: QR code
    }

    //public String getOrganizerEmail() {return organizerEmail;}
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
    //public LocalDateTime getEventStartTime() { return eventStartTime; }
    public Integer getMaxWinners() {
        return maxWinners;
    }
    public Integer getMaxEntrants() {
        return maxEntrants;
    }
    public String getEventDetails() { return eventDetails; }
    public String getEventPictureURL() { return eventPictureURL; }
    public boolean getEnableGeolocation() { return enableGeolocation; }
    // TODO: get QR code

    //public void setOrganizerEmail(String organizerEmail) {this.organizerEmail = organizerEmail;}
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
    //public void setEventStartTime(LocalDateTime eventStartTime) { this.eventStartTime = eventStartTime; }
    public void setMaxWinners(Integer maxWinners) { this.maxWinners = maxWinners; }
    public void setMaxEntrants(Integer maxEntrants) { this.maxEntrants = maxEntrants; }
    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }
    public void setEventPictureURL(String eventPictureURL) { this.eventPictureURL = eventPictureURL; }
    public void setEnableGeolocation(boolean enableGeolocation) { this.enableGeolocation = enableGeolocation; }
    // TODO: set QR code
}
