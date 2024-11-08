package com.example.gengardraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.UserProfile;

public class UserProfileTest {

    private UserProfile userProfile;

    @BeforeEach
    public void setUp() {
        // Initialize the UserProfile object before each test
        List<String> notifications = Arrays.asList("Notification1", "Notification2");
        List<String> events = Arrays.asList("Event1", "Event2");

        userProfile = new UserProfile("device123", "John Doe", "johndoe@example.com", "1234567890",
                "http://example.com/profile.jpg", "http://example.com/facility",
                true, notifications, events, true, false);
    }


    @Test
    public void testGetInitials() {
        // Test if the getInitials method returns the correct initials
        assertEquals("JD", userProfile.getInitials());
    }

    @Test
    public void testSettersAndGetters() {
        // Test the getter and setter methods
        userProfile.setDeviceID("newDevice123");
        userProfile.setName("Jane Doe");
        userProfile.setEmail("janedoe@example.com");
        userProfile.setPhoneNumber("0987654321");
        userProfile.setPictureURL("http://example.com/newProfile.jpg");
        userProfile.setFacilityURL("http://example.com/newFacility");
        userProfile.setAllowNotifications(false);
        userProfile.setNotificationsArray(Arrays.asList("Notification3", "Notification4"));
        userProfile.setJoinedEvents(Arrays.asList("Event3", "Event4"));
        userProfile.setOrganizer(false);
        userProfile.setAdmin(true);

        assertEquals("newDevice123", userProfile.getDeviceID());
        assertEquals("Jane Doe", userProfile.getName());
        assertEquals("janedoe@example.com", userProfile.getEmail());
        assertEquals("0987654321", userProfile.getPhoneNumber());
        assertEquals("http://example.com/newProfile.jpg", userProfile.getPictureURL());
        assertEquals("http://example.com/newFacility", userProfile.getFacilityURL());
        assertFalse(userProfile.isAllowNotifications());
        assertEquals(2, userProfile.getNotificationsArray().size());
        assertEquals(2, userProfile.getJoinedEvents().size());
        assertFalse(userProfile.isOrganizer());
        assertTrue(userProfile.isAdmin());
    }
}