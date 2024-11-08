package com.example.gengardraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.EventLists;

public class EventListsTest {


    private EventLists eventLists;
    private List<String> waitingList;
    private List<String> chosenList;
    private List<String> cancelledList;
    private List<String> winnersList;
    private Map<String, Object> locationList;

    @BeforeEach
    public void setUp() {
        waitingList = new ArrayList<>();
        chosenList = new ArrayList<>();
        cancelledList = new ArrayList<>();
        winnersList = new ArrayList<>();
        locationList = new HashMap<>();

        eventLists = new EventLists(
                "event123",
                3, // max winners
                5, // max entrants
                true, // enable geolocation
                waitingList,
                chosenList,
                cancelledList,
                winnersList,
                locationList
        );
    }

    @Test
    public void testAddToWaitingList() {
        eventLists.addToWaitingList("user1");
        eventLists.addToWaitingList("user2");

        assertEquals(2, waitingList.size());
        assertTrue(waitingList.contains("user1"));
        assertTrue(waitingList.contains("user2"));
    }

    @Test
    public void testAddToChosenList() {
        eventLists.addToChosenList("user1");
        eventLists.addToChosenList("user2");

        assertEquals(2, chosenList.size());
        assertTrue(chosenList.contains("user1"));
        assertTrue(chosenList.contains("user2"));
    }

    @Test
    public void testWaitingListFull() {
        eventLists.addToWaitingList("user1");
        eventLists.addToWaitingList("user2");
        eventLists.addToWaitingList("user3");
        eventLists.addToWaitingList("user4");
        eventLists.addToWaitingList("user5");

        assertTrue(eventLists.isWaitingListFull());
    }

    @Test
    public void testChosenListFull() {
        eventLists.addToChosenList("user1");
        eventLists.addToChosenList("user2");
        eventLists.addToChosenList("user3");

        assertTrue(eventLists.isChosenListFull());
    }

    @Test
    public void testAddToCancelledList() {
        eventLists.addToCancelledList("user1");

        assertEquals(1, cancelledList.size());
        assertTrue(cancelledList.contains("user1"));
    }

    @Test
    public void testAddToWinnersList() {
        eventLists.addToWinnersList("user1");

        assertEquals(1, winnersList.size());
        assertTrue(winnersList.contains("user1"));
    }

    @Test
    public void testRemoveFromWaitingList() {
        eventLists.addToWaitingList("user1");
        eventLists.removeFromWaitingList("user1");

        assertEquals(0, waitingList.size());
        assertFalse(waitingList.contains("user1"));
    }

    @Test
    public void testRemoveFromChosenList() {
        eventLists.addToChosenList("user1");
        eventLists.removeFromChosenList("user1");

        assertEquals(0, chosenList.size());
        assertFalse(chosenList.contains("user1"));
    }

    @Test
    public void testRemoveFromCancelledList() {
        eventLists.addToCancelledList("user1");
        eventLists.removeFromCancelledList("user1");

        assertEquals(0, cancelledList.size());
        assertFalse(cancelledList.contains("user1"));
    }

    @Test
    public void testRemoveFromWinnersList() {
        eventLists.addToWinnersList("user1");
        eventLists.removeFromWinnersList("user1");

        assertEquals(0, winnersList.size());
        assertFalse(winnersList.contains("user1"));
    }

    @Test
    public void testRemoveFromLocationList() {
        eventLists.getLocationList().put("user1", "location1");
        eventLists.removeFromLocationHashmap("user1");

        assertNull(eventLists.getLocationList().get("user1"));
    }

    @Test
    public void testSettersAndGetters() {
        eventLists.setEventID("event456");
        assertEquals("event456", eventLists.getEventID());

        Integer maxWinners = 10;
        eventLists.setMaxWinners(maxWinners);
        assertEquals(maxWinners, eventLists.getMaxWinners());

        Integer maxEntrants = 20;
        eventLists.setMaxEntrants(maxEntrants);
        assertEquals(maxEntrants, eventLists.getMaxEntrants());

        eventLists.setEnableGeolocation(false);
        assertFalse(eventLists.getEnableGeolocation());
    }
}