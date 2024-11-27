package Classes;

import java.util.Date;

/**
 * Event
 *
 *     The <code>Event</code> class represents an event organized within the application.
 *     It stores various attributes related to the event, such as dates, maximum participants,
 *     geolocation settings, and references to associated resources.
 *
 * @author Rheanna, Rehan
 * @see EventManager
 */
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
    private String eventID;

    /**
     * Empty Event Constructor
     */
    public Event() {}

    /**
     * Complete Event Constructor
     * @param organizerID    Unique ID of the event organizer
     * @param eventTitle     Title of the event
     * @param regOpenDate    Date when registration opens
     * @param regDeadlineDate Date when registration closes
     * @param eventStartDate Date when the event starts
     * @param maxWinners     Maximum number of winners for the event
     * @param maxEntrants    Maximum number of participants allowed
     * @param eventDetails   Description and details of the event
     * @param eventPictureURL URL for the event's image
     * @param enableGeolocation Indicates if geolocation is enabled for the event
     * @param listReference  Reference ID of the associated event list document
     * @param LocationReference  Reference ID of the associated location document
     * @param QRCode         QR code for event identification
     */
    public Event(String organizerID, String eventTitle, Date regOpenDate, Date regDeadlineDate, Date eventStartDate, Integer maxWinners, Integer maxEntrants, String eventDetails, String eventPictureURL, boolean enableGeolocation, String listReference, String LocationReference, String QRCode, String eventID){
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
        this.eventID = eventID;
    }

    /**
     * Gets the organizer ID
     * @return String representing the organizer's unique ID
     */
    public String getOrganizerID() {
        return organizerID;
    }

    /**
     * Gets the event title
     * @return String representing the event's title
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Gets the registration open date
     * @return Date when registration opens
     */
    public Date getRegOpenDate() {
        return regOpenDate;
    }

    /**
     * Gets the registration deadline date
     * @return Date when registration closes
     */
    public Date getRegDeadlineDate() {
        return regDeadlineDate;
    }

    /**
     * Gets the event start date
     * @return Date when the event starts
     */
    public Date getEventStartDate() {
        return eventStartDate;
    }

    /**
     * Gets the maximum number of winners
     * @return Integer representing the max number of winners
     */
    public Integer getMaxWinners() {
        return maxWinners;
    }

    /**
     * Gets the maximum number of entrants
     * @return Integer representing the max number of entrants
     */
    public Integer getMaxEntrants() {
        return maxEntrants;
    }

    /**
     * Gets event details
     * @return String describing the event details
     */
    public String getEventDetails() {
        return eventDetails;
    }

    /**
     * Gets the event picture URL
     * @return String URL for the event's picture
     */
    public String getEventPictureURL() {
        return eventPictureURL;
    }

    /**
     * Checks if geolocation is enabled
     * @return boolean indicating if geolocation is enabled
     */
    public boolean getEnableGeolocation() {
        return enableGeolocation;
    }

    /**
     * Gets the list reference ID
     * @return String ID for the associated event list
     */
    public String getListReference() {
        return ListReference;
    }

    /**
     * Gets the location reference ID
     * @return String ID for the associated location
     */
    public String getLocationReference() {
        return LocationReference;
    }

    /**
     * Gets the QR code
     * @return String representing the event's QR code
     */
    public String getQRCode() {
        return QRCode;
    }

    /**
     * Gets the event ID
     * @return String representing the event's unique ID
     */
    public String getEventID() { return eventID; }

    /**
     * Sets the organizer ID
     * @param organizerID String representing the organizer's unique ID
     */
    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    /**
     * Sets the event title
     * @param eventTitle String representing the event's title
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Sets the registration open date
     * @param regOpenDate Date when registration opens
     */
    public void setRegOpenDate(Date regOpenDate) {
        this.regOpenDate = regOpenDate;
    }

    /**
     * Sets the registration deadline date
     * @param regDeadlineDate Date when registration closes
     */
    public void setRegDeadlineDate(Date regDeadlineDate) { this.regDeadlineDate = regDeadlineDate; }

    /**
     * Sets the event start date
     * @param eventStartDate Date when the event starts
     */
    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    /**
     * Sets the maximum number of winners
     * @param maxWinners Integer representing the max number of winners
     */
    public void setMaxWinners(Integer maxWinners) { this.maxWinners = maxWinners; }

    /**
     * Sets the maximum number of entrants
     * @param maxEntrants Integer representing the max number of entrants
     */
    public void setMaxEntrants(Integer maxEntrants) { this.maxEntrants = maxEntrants; }

    /**
     * Sets event details
     * @param eventDetails String describing the event details
     */
    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    /**
     * Sets the event picture URL
     * @param eventPictureURL String URL for the event's picture
     */
    public void setEventPictureURL(String eventPictureURL) { this.eventPictureURL = eventPictureURL; }

    /**
     * Sets if geolocation is enabled
     * @param enableGeolocation boolean indicating if geolocation is enabled
     */
    public void setEnableGeolocation(boolean enableGeolocation) { this.enableGeolocation = enableGeolocation; }

    /**
     * Sets the list reference ID
     * @param listReference String ID for the associated event list
     */
    public void setListReference(String listReference) { this.ListReference = listReference; }

    /**
     * Sets the location reference ID
     * @param locationReference String ID for the associated location
     */
    public void setLocationReference(String locationReference) { this.LocationReference = locationReference; }

    /**
     * Sets the QR code
     * @param QRCode String representing the event's QR code
     */
    public void setQRCode(String QRCode) { this.QRCode = QRCode; }

    /**
     * Sets the event ID
     * @param eventID String representing the event's unique ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
