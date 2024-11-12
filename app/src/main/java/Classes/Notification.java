package Classes;

import java.util.Date;

public class Notification {
    private String eventTitle;
    private Date eventStartDate;
    private String eventID;
    private String message;

    public Notification() {
        //Empty constructor
    }

    public Notification(String eventTitle, Date eventStartDate, String eventID, String message){
        this.eventTitle = eventTitle;
        this.eventStartDate = eventStartDate;
        this.eventID = eventID;
        this.message = message;
    }

    //Setters and Getters

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
