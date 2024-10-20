package Classes;

import java.util.List;

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

    public UserProfile() {}

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

    public String getDeviceID() {
        return deviceID;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPictureURL() {
        return pictureURL;
    }
    public String getFacilityURL() {
        return facilityURL;
    }
    public boolean isAllowNotifications() {
        return allowNotifications;
    }
    public List<String> getNotificationsArray() {
        return notificationsArray;
    }
    public List<String> getJoinedEvents() {
        return joinedEvents;
    }
    public boolean isOrganizer() {
        return isOrganizer;
    }
    public boolean isAdmin() {
        return isAdmin;
    }


    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
    public void setFacilityURL(String facilityURL) {
        this.facilityURL = facilityURL;
    }
    public void setAllowNotifications(boolean allowNotifications) {
        this.allowNotifications = allowNotifications;
    }
    public void setNotificationsArray(List<String> notificationsArray) {
        this.notificationsArray = notificationsArray;
    }
    public void setJoinedEvents(List<String> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }
    public void setOrganizer(boolean isOrganizer) {
        this.isOrganizer = isOrganizer;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}
