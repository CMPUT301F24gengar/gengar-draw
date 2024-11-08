package com.example.gengardraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import Classes.QRcode;


public class QRcodeTest {

    private QRcode qrCode;

    @BeforeEach
    public void setUp() {
        // Initialize the QRcode object before each test
        qrCode = new QRcode("sampleQRCode123", "event123");
    }

    @Test
    public void testSettersAndGetters() {
        // Test the getter and setter methods for qrcode and eventID
        qrCode.setQRcode("newQRCode456");
        assertEquals("newQRCode456", qrCode.getQRcode());

        qrCode.setEventID("event456");
        assertEquals("event456", qrCode.getEventID());
    }
}