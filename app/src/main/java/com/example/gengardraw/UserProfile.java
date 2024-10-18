package com.example.gengardraw;


public class UserProfile {
    private String deviceID;
    private String name;
    private String email;
    private String phoneNumber;
    private String pictureURL;
    private String location;
    private boolean isAdmin;
    private boolean isOrganizer;
    private boolean isEntrant;

    public UserProfile(String deviceID, String name, String phoneNumber, String pictureURL, String email, String location, boolean isAdmin, boolean isOrganizer, boolean isEntrant){
        this.deviceID = deviceID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pictureURL = pictureURL;
        this.email = email;
        this.location = location;
        this.isAdmin = isAdmin;
        this.isOrganizer = isOrganizer;
        this.isEntrant = isEntrant;
    }


    public String getDeviceID(){
        return deviceID;
    }

    public void setDeviceID(String deviceID){
        this.deviceID = deviceID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPictureURL(){
        return pictureURL;
    }

    public void setPictureURL(String pictureURL){
        this.pictureURL = pictureURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getAdminBool() {
        return isAdmin;
    }

    public void setAdminBool(boolean admin){
        isAdmin = admin;
    }


    public boolean getOrganizerBool() {
        return isOrganizer;
    }

    public void setOrganizerBool(boolean organizer) {
        isOrganizer = organizer;
    }

    public boolean getEntrantBool() {
        return isEntrant;
    }

    public void setEntrantBool(boolean entrant) {
        isEntrant = entrant;
    }

}
