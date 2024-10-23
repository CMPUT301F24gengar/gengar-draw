package Classes;

import java.time.LocalDateTime;
import java.util.Date;
//import java.util.List;

public class Event {
    private String organizerEmail;
    private String eventTitle;
    private String regOpenDate;
    private String regDeadlineDate;
    private Date eventStartDate;
    private LocalDateTime eventStartTime;
    private Integer maxWinners;
    private Integer maxEntrants;
    private String eventDetails;
    private String eventPictureURL;
    // TODO: save QR code

    public Event() {}

    public Event(String organizerEmail, String eventTitle, String regOpenDate, String regDeadlineDate, Date eventStartDate, LocalDateTime eventStartTime, Integer maxWinners, Integer maxEntrants, String eventDetails, String eventPictureURL){
        this.organizerEmail = organizerEmail;
        this.eventTitle = eventTitle;
        this.regOpenDate = regOpenDate;
        this.regDeadlineDate = regDeadlineDate;
        this.eventStartDate = eventStartDate;
        this.eventStartTime = eventStartTime;
        this.maxWinners = maxWinners;
        this.maxEntrants = maxEntrants;
        this.eventDetails = eventDetails;
        this.eventPictureURL = eventPictureURL;
        // TODO: QR code
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }
    public String getEventTitle() {
        return eventTitle;
    }
    public String getRegOpenDate() {
        return regOpenDate;
    }
    public String getRegDeadlineDate() {
        return regDeadlineDate;
    }
    public Date getEventStartDate() {
        return eventStartDate;
    }
    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }
    public Integer getMaxWinners() {
        return maxWinners;
    }
    public Integer getMaxEntrants() {
        return maxEntrants;
    }
    public String getEventDetails() { return eventDetails; }
    public String getEventPictureURL() { return eventPictureURL; }
    // TODO: get QR code

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    public void setRegOpenDate(String regOpenDate) {
        this.regOpenDate = regOpenDate;
    }
    public void setRegDeadlineDate(String regDeadlineDate) { this.regDeadlineDate = regDeadlineDate; }
    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }
    public void setEventStartTime(LocalDateTime eventStartTime) { this.eventStartTime = eventStartTime; }
    public void setMaxWinners(Integer maxWinners) { this.maxWinners = maxWinners; }
    public void setMaxEntrants(Integer maxEntrants) { this.maxEntrants = maxEntrants; }
    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }
    public void setEventPictureURL(String eventPictureURL) { this.eventPictureURL = eventPictureURL; }
    // TODO: set QR code
}
