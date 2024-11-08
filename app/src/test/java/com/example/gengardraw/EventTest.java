package com.example.gengardraw;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import Classes.Event;

public class EventTest {

    private Event mockEvent() {
        // Create mock event data
        String organizerID = "12345";
        String eventTitle = "Sample Event";
        Date regOpenDate = new Date();  // Current date
        Date regDeadlineDate = new Date(System.currentTimeMillis() + 86400000L); // One day later
        Date eventStartDate = new Date(System.currentTimeMillis() + 172800000L); // Two days later
        Integer maxWinners = 3;
        Integer maxEntrants = 100;
        String eventDetails = "This is a mock event used for testing.";
        String eventPictureURL = "https://example.com/event_picture.jpg";
        boolean enableGeolocation = true;
        String listReference = "list123";
        String locationReference = "location123";
        String QRCode = "sampleQRCode123";

        // Return a new Event object with mock data
        return new Event(organizerID, eventTitle, regOpenDate, regDeadlineDate, eventStartDate,
                maxWinners, maxEntrants, eventDetails, eventPictureURL,
                enableGeolocation, listReference, locationReference, QRCode);
    }

    @Test
    void testGetOrganiserID(){
        Event testEvent = mockEvent();
        String OrganizerID = "12345";
        assertEquals(OrganizerID,testEvent.getOrganizerID());
    }

    @Test
    void testGetEventTitle(){
        Event testEvent = mockEvent();
        String EventTitle = "Sample Event";
        assertEquals(EventTitle,testEvent.getEventTitle());
    }

    @Test
    void testGetRegOpenDate(){
        Event testEvent = mockEvent();
        Date regOpenDate = new Date();  // Current date
        assertEquals(regOpenDate,testEvent.getRegOpenDate());
    }

    @Test
    void testGetRegDeadlineDate(){
        Event testEvent = mockEvent();
        Date regDeadlineDate = new Date(System.currentTimeMillis() + 86400000L); // One day later
        assertEquals(regDeadlineDate,testEvent.getRegDeadlineDate());
    }

    @Test
    void testGetEventStartDate(){
        Event testEvent = mockEvent();
        Date eventStartDate = new Date(System.currentTimeMillis() + 172800000L); // Two days later
        assertEquals(eventStartDate,testEvent.getEventStartDate());
    }

    @Test
    void testGetMaxWinners(){
        Event testEvent = mockEvent();
        Integer maxWinners = 3;
        assertEquals(maxWinners,testEvent.getMaxWinners());
    }

    @Test
    void testGetMaxEntrants(){
        Event testEvent = mockEvent();
        Integer maxEntrants = 100;
        assertEquals(maxEntrants,testEvent.getMaxEntrants());
    }

    @Test
    void testGetEventDetails(){
        Event testEvent = mockEvent();
        String eventDetails = "This is a mock event used for testing.";
        assertEquals(eventDetails,testEvent.getEventDetails());
    }

    @Test
    void testGetEventPictureURL(){
        Event testEvent = mockEvent();
        String eventPictureURL = "https://example.com/event_picture.jpg";
        assertEquals(eventPictureURL,testEvent.getEventPictureURL());
    }

    @Test
    void testGetEnableGeolocation(){
        Event testEvent = mockEvent();
        boolean enableGeolocation = true;
        assertEquals(enableGeolocation,testEvent.getEnableGeolocation());
    }

    @Test
    void testGetListReference(){
        Event testEvent = mockEvent();
        String listReference = "list123";
        assertEquals(listReference,testEvent.getListReference());
    }

    @Test
    void testGetLocationReference(){
        Event testEvent = mockEvent();
        String locationReference = "location123";
        assertEquals(locationReference,testEvent.getLocationReference());
    }

    @Test
    void testGetQRCode(){
        Event testEvent = mockEvent();
        String QRCode = "sampleQRCode123";
        assertEquals(QRCode,testEvent.getQRCode());
    }

    @Test
    public void testSetOrganizerID() {
        Event event = mockEvent();  // Create a mock event
        String newOrganizerID = "67890";  // New field to test
        event.setOrganizerID(newOrganizerID);  // Set new field using the method
        // Assert
        assertEquals(newOrganizerID, event.getOrganizerID());
    }

    @Test
    public void testSetEventTitle() {
        Event event = mockEvent();  // Create a mock event
        String newTitle = "Annual Fundraising Dinner"; // New field to test
        event.setEventTitle(newTitle);  // Set new field using the method
        // Assert
        assertEquals(newTitle, event.getEventTitle());
    }

    @Test
    public void testSetRegOpenDate() {
        Event event = mockEvent();  // Create a mock event
        Date newRegOpenDate = new Date(1672617600000L); // New field to test
        event.setRegOpenDate(newRegOpenDate);  // Set new field using the method
        // Assert
        assertEquals(newRegOpenDate, event.getRegOpenDate());
    }

    @Test
    public void testSetRegDeadlineDate() {
        Event event = mockEvent();  // Create a mock event
        Date newRegDeadlineDate = new Date(1672704000000L); // New registration deadline date
        event.setRegDeadlineDate(newRegDeadlineDate);  // Set new field using the method
        // Assert
        assertEquals(newRegDeadlineDate, event.getRegDeadlineDate());
    }

    @Test
    public void testSetEventStartDate() {
        Event event = mockEvent();  // Create a mock event
        Date newEventStartDate = new Date(1672790400000L); // New event start date
        event.setEventStartDate(newEventStartDate);  // Set new field using the method
        // Assert
        assertEquals(newEventStartDate, event.getEventStartDate());
    }

    @Test
    public void testSetMaxWinners() {
        Event event = mockEvent();  // Create a mock event
        Integer newMaxWinners = 10;
        event.setMaxWinners(newMaxWinners);  // Set new field using the method
        // Assert
        assertEquals(newMaxWinners, event.getMaxWinners());
    }

    @Test
    public void testSetMaxEntrants() {
        Event event = mockEvent();  // Create a mock event
        Integer newMaxEntrants = 250;
        event.setMaxEntrants(newMaxEntrants);  // Set new field using the method
        // Assert
        assertEquals(newMaxEntrants, event.getMaxEntrants());
    }

    @Test
    public void testSetEventDetails() {
        Event event = mockEvent();  // Create a mock event
        String newEventDetails = "An exclusive fundraising dinner with special guests.";
        event.setEventDetails(newEventDetails);  // Set new field using the method
        // Assert
        assertEquals(newEventDetails, event.getEventDetails());
    }

    @Test
    public void testSetEventPictureURL() {
        Event event = mockEvent();  // Create a mock event
        String newPictureURL = "https://example.com/images/fundraiser_dinner.jpg";
        event.setEventPictureURL(newPictureURL);  // Set new field using the method
        // Assert
        assertEquals(newPictureURL, event.getEventPictureURL());
    }

    @Test
    public void testSetEnableGeolocation() {
        Event event = mockEvent();  // Create a mock event
        boolean newGeolocationStatus = false;
        event.setEnableGeolocation(newGeolocationStatus);
        event.setEnableGeolocation(newGeolocationStatus);  // Set new field using the method
        // Assert
        assertEquals(newGeolocationStatus, event.getEnableGeolocation());
    }

    @Test
    public void testSetListReference() {
        Event event = mockEvent();  // Create a mock event
        String newListReference = "event_list_2024";
        event.setListReference(newListReference);  // Set new field using the method
        // Assert
        assertEquals(newListReference, event.getListReference());
    }

    @Test
    public void testSetLocationReference() {
        Event event = mockEvent();  // Create a mock event
        String newLocationReference = "location_67890";
        event.setLocationReference(newLocationReference);  // Set new field using the method
        // Assert
        assertEquals(newLocationReference, event.getLocationReference());
    }

    @Test
    public void testSetQRCode() {
        Event event = mockEvent();  // Create a mock event
        String newQRCode = "newQRCode12345";
        event.setQRCode(newQRCode);  // Set new field using the method
        // Assert
        assertEquals(newQRCode, event.getQRCode());
    }


}
