package Classes;

import java.util.List;

/**
 * UserProfile
 *
 *     UserProfile class locally stores information about users
 *     data:<ul> <li>deviceID</li> <li>name</li> <li>email</li> <li>phoneNumber</li> <li>pictureURL</li> <li>facilityURL</li> <li>allowNotifications</li> <li>notificationsArray</li> <li>joinedEvents</li> <li>isOrganizer</li> <li>isAdmin</li></ul>
 *     methods include:<ul> <li>constructors</li> <li>getters</li> <li>setters</li> <li>getInitials</li></ul>
 *
 * @author Rafi, Rehan
 * @see UserProfileManager
 */

public class UserProfile {
    private String deviceID;
    private String name;
    private String email;
    private String phoneNumber;

    private String pictureURL;
    private String facilityURL;

    private boolean allowNotifications;
    private List<String> notificationsArray;

    private List<String> joinedEvents;

    private boolean isOrganizer;
    private boolean isAdmin;

    /**
     * Empty UserProfile Constructor
     */
    public UserProfile() {}

    /**
     * Complete UserProfile Constructor
     * @param deviceID  Unique id of user's device
     * @param name String name of the user
     * @param email String email of the user
     * @param phoneNumber  String representation of user's phone number
     * @param pictureURL  String URL representation holds user's image information
     * @param facilityURL   String URL representation holds facility image information
     * @param allowNotifications  Boolean keeping track of whether the user allows notifications
     * @param notificationsArray List keeping track of user's notifications
     * @param joinedEvents List of events the user has joined, events represented by their String id
     * @param isOrganizer Boolean keeping track whether ths user is an organizer
     * @param isAdmin Boolean keeping track whether the user is an admin
     */
    public UserProfile(String deviceID, String name, String email, String phoneNumber, String pictureURL, String facilityURL, boolean allowNotifications, List<String> notificationsArray, List<String> joinedEvents, boolean isOrganizer, boolean isAdmin){;
        this.deviceID = deviceID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pictureURL = pictureURL;
        this.facilityURL = facilityURL;
        this.allowNotifications = allowNotifications;
        this.notificationsArray = notificationsArray;
        this.joinedEvents = joinedEvents;
        this.isOrganizer = isOrganizer;
        this.isAdmin = isAdmin;
    }

    /**
     * gets user's DeviceID
     * @return String DeviceID
     */
    public String getDeviceID() {
        return deviceID;
    }

    /**
     * gets user's name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * gets user's email
     * @return String email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets user's phone number
     * @return String phone number
     */
    public String getPhoneNumber() {return phoneNumber; }

    /**
     * gets String representation of the URL of user's profile picture
     * @return String pictureURL
     */
    public String getPictureURL() {
        return pictureURL;
    }

    /**
     * gets String representation of the URL of facility
     * @return String DeviceID
     */
    public String getFacilityURL() {
        return facilityURL;
    }

    /**
     * gets user's consent on receiving notifications
     * @return boolean isAllowNotifications
     */
    public boolean isAllowNotifications() {
        return allowNotifications;
    }

    /**
     * gets the list of user's notifications
     * @return List of boolean values
     */
    public List<String> getNotificationsArray() {
        return notificationsArray;
    }

    /**
     * gets a list of String IDs of the events the user has joined
     * @return List of EventIDs
     */
    public List<String> getJoinedEvents() {
        return joinedEvents;
    }

    /**
     * gets information as to whether the user is an organizer
     * @return String isOrganizer
     */
    public boolean isOrganizer() {
        return isOrganizer;
    }

    /**
     * gets information as to whether the user is an admin
     * @return boolean isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * gets the initials of user's name
     * @return String initials
     */
    public String getInitials(){
        StringBuilder initials = new StringBuilder();
        String[] newString = name.trim().split("\\s+");
        for (String letter : newString){
            initials.append(letter.charAt(0));
        }
        return initials.toString().toUpperCase();
    }

    /**
     * sets user's deviceID
     * @param deviceID String
     */
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * sets user's name
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets user's email
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * sets user's phone numbers
     * @param phoneNumber String
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * sets user's profile picture URL
     * @param pictureURL String
     */
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    /**
     * sets facility URL
     * @param facilityURL String
     */
    public void setFacilityURL(String facilityURL) {
        this.facilityURL = facilityURL;
    }

    /**
     * sets whether the user allows notifications
     * @param allowNotifications boolean
     */
    public void setAllowNotifications(boolean allowNotifications) {
        this.allowNotifications = allowNotifications;
    }

    /**
     * sets the list of notifications of the user
     * @param notificationsArray List
     */
    public void setNotificationsArray(List<String> notificationsArray) {
        this.notificationsArray = notificationsArray;
    }

    /**
     * sets the list of eventID's of the events joined by the user
     * @param joinedEvents List
     */
    public void setJoinedEvents(List<String> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    /**
     * sets whether the user is an organizer
     * @param isOrganizer boolean
     */
    public void setOrganizer(boolean isOrganizer) {
        this.isOrganizer = isOrganizer;
    }

    /**
     * sets whether the user is an admin
     * @param isAdmin boolean
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}
