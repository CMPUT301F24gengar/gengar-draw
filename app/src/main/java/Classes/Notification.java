package Classes;

/**
 * Notification
 *
 * Notification class locally stores information relevant to notifications
 *
 * @author Rehan, Rafi
 * @see NotificationManager
 */
public class Notification {
    private boolean notified;
    private String eventTitle;
    private String eventStartDateDay;
    private String eventStartDateMonth;
    private String eventStartDateTime;
    private String eventID;
    private String message;

    /**
     * Empty constructor
     */
    public Notification() {
        // Empty constructor
    }

    /**
     * Constructor with parameters
     * @param eventTitle
     * @param eventStartDateDay
     * @param eventStartDateMonth
     * @param eventStartDateTime
     * @param eventID
     * @param message
     * @param notified
     */
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

    /**
     * Getter for event title
     * @return String event title
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Setter for event title
     * @param eventTitle String event title
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Getter for event start date day
     * @return String event start date day
     */
    public String getEventStartDateDay() {
        return eventStartDateDay;
    }

    /**
     * Setter for event start date day
     * @param eventStartDateDay String event start date day
     */
    public void setEventStartDateDay(String eventStartDateDay) {
        this.eventStartDateDay = eventStartDateDay;
    }

    /**
     * Getter for event start date month
     * @return String event start date month
     */
    public String getEventStartDateMonth() {
        return eventStartDateMonth;
    }

    /**
     * Setter for event start date month
     * @param eventStartDateMonth String event start date month
     */
    public void setEventStartDateMonth(String eventStartDateMonth) {
        this.eventStartDateMonth = eventStartDateMonth;
    }

    /**
     * Getter for event start date time
     * @return String event start date time
     */
    public String getEventStartDateTime() {
        return eventStartDateTime;
    }

    /**
     * Setter for event start date time
     * @param eventStartDateTime String event start date time
     */
    public void setEventStartDateTime(String eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
    }

    /**
     * Getter for event ID
     * @return String event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Setter for event ID
     * @param eventID String event ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Getter for message
     * @return String message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message
     * @param message String message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for notified
     * @return boolean notified
     */
    public boolean getNotified() {
        return notified;
    }

    /**
     * Setter for notified
     * @param notified boolean notified
     */
    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}

