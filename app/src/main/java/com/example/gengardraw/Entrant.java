package com.example.gengardraw;

public class Entrant extends UserProfile{
    public Entrant(String deviceID, String name, String phoneNumber, String pictureURL, String email, String location, boolean isAdmin, boolean isOrganizer, boolean isEntrant){
        super(deviceID, name, phoneNumber, pictureURL, email, location, false, false, true);

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Your name cannot be null or empty.");
        }
        else if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Your phone number cannot be null or empty");
        }
    }

}
