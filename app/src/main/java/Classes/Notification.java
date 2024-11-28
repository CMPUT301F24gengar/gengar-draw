package Classes;


public class Notification {
    private boolean notified;
    private String eventTitle;
    private String eventStartDateDay;
    private String eventStartDateMonth;
    private String eventStartDateTime;
    private String eventID;
    private String message;

    public Notification() {
        // Empty constructor
    }

    public Notification(String eventTitle, String eventStartDateDay, String eventStartDateMonth,
                        String eventStartDateTime, String eventID, String message, boolean notified) {
        this.eventTitle = eventTitle;
        this.eventStartDateDay = eventStartDateDay;
        this.eventStartDateMonth = eventStartDateMonth;
        this.eventStartDateTime = eventStartDateTime;
        this.eventID = eventID;
        this.message = message;
        this.notified = notified;
    }

    // Getters and setters
    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventStartDateDay() {
        return eventStartDateDay;
    }

    public void setEventStartDateDay(String eventStartDateDay) {
        this.eventStartDateDay = eventStartDateDay;
    }

    public String getEventStartDateMonth() {
        return eventStartDateMonth;
    }

    public void setEventStartDateMonth(String eventStartDateMonth) {
        this.eventStartDateMonth = eventStartDateMonth;
    }

    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    public void setEventStartDateTime(String eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
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

    public boolean getNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}

