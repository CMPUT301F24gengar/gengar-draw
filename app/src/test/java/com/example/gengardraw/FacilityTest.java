package com.example.gengardraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.Facility;

/**
 * tests for functionality of all facility classes: Facility, FacilityManager, and facility_activity
 */
public class FacilityTest {


    private Facility facility;
    private List<String> events;

    @BeforeEach
    public void setUp() {
        // Setting up the initial data before each test.
        events = new ArrayList<>();
        facility = new Facility(
                "Test Facility",
                40.7128,  // latitude (for example, New York City)
                -74.0060, // longitude
                "New York",
                "A description of the facility",
                "https://example.com/pic.jpg",
                events,
                "device123"
        );
    }


    @Test
    public void testSettersAndGetters() {
        facility.setName("Updated Facility");
        assertEquals("Updated Facility", facility.getName());

        facility.setLatitude(34.0522); // latitude for Los Angeles
        assertEquals(34.0522, facility.getLatitude());

        facility.setLongitude(-118.2437); // longitude for Los Angeles
        assertEquals(-118.2437, facility.getLongitude());

        facility.setLocation("Los Angeles");
        assertEquals("Los Angeles", facility.getLocation());

        facility.setDescription("Updated description");
        assertEquals("Updated description", facility.getDescription());

        facility.setPictureURL("https://example.com/newpic.jpg");
        assertEquals("https://example.com/newpic.jpg", facility.getPictureURL());

        facility.setDeviceID("device456");
        assertEquals("device456", facility.getDeviceID());
    }

    @Test
    public void testAddEvent() {
        facility.addEvent("event123");
        assertEquals(1, facility.getEvents().size());
        assertTrue(facility.getEvents().contains("event123"));

        // Add another event
        facility.addEvent("event456");
        assertEquals(2, facility.getEvents().size());
        assertTrue(facility.getEvents().contains("event456"));
    }
}