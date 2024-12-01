package com.example.gengardraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import Classes.Notification;

public class NotificationsTest {
    private Notification notification;

    @BeforeEach
    public void setUp() {
        // Initialize a Notification object before each test
        notification = new Notification(
                "Event Title",
                "15",
                "DEC",
                "10:00 AM",
                "12345",
                "This is a notification message.",
                true
        );
    }

    @Test
    public void testGetEventTitle() {
        assertEquals("Event Title", notification.getEventTitle());
    }

    @Test
    public void testSetEventTitle() {
        notification.setEventTitle("New Event Title");
        assertEquals("New Event Title", notification.getEventTitle());
    }

    @Test
    public void testGetEventStartDateDay() {
        assertEquals("15", notification.getEventStartDateDay());
    }

    @Test
    public void testSetEventStartDateDay() {
        notification.setEventStartDateDay("16");
        assertEquals("16", notification.getEventStartDateDay());
    }

    @Test
    public void testGetEventStartDateMonth() {
        assertEquals("DEC", notification.getEventStartDateMonth());
    }

    @Test
    public void testSetEventStartDateMonth() {
        notification.setEventStartDateMonth("November");
        assertEquals("November", notification.getEventStartDateMonth());
    }

    @Test
    public void testGetEventStartDateTime() {
        assertEquals("10:00 AM", notification.getEventStartDateTime());
    }

    @Test
    public void testSetEventStartDateTime() {
        notification.setEventStartDateTime("11:00 AM");
        assertEquals("11:00 AM", notification.getEventStartDateTime());
    }

    @Test
    public void testGetEventID() {
        assertEquals("12345", notification.getEventID());
    }

    @Test
    public void testSetEventID() {
        notification.setEventID("67890");
        assertEquals("67890", notification.getEventID());
    }

    @Test
    public void testGetMessage() {
        assertEquals("This is a notification message.", notification.getMessage());
    }

    @Test
    public void testSetMessage() {
        notification.setMessage("Updated message.");
        assertEquals("Updated message.", notification.getMessage());
    }

    @Test
    public void testGetNotified() {
        assertTrue(notification.getNotified());
    }

    @Test
    public void testSetNotified() {
        notification.setNotified(false);
        assertFalse(notification.getNotified());
    }
}
