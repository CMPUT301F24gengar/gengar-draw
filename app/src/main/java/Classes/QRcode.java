package Classes;

/**
 * QRcode
 *
 *     The <code>QRcode</code> class represents a QR code associated with an event.
 *     It stores a QR code string and an event ID that links the QR code to a specific event.
 *
 * @author Rehan
 * @see QRcodeManager
 */
public class QRcode {
    private String qrcode;
    private String eventID;

    /**
     * Default constructor for QRcode
     */
    public QRcode() {}

    /**
     * Complete constructor for QRcode
     * @param qrcode String representing the QR code
     * @param eventID String ID of the associated event
     */
    public QRcode(String qrcode, String eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the QR code
     * @return String representing the QR code
     */
    public String getQRcode() {
        return qrcode;
    }

    /**
     * Gets the event ID associated with this QR code
     * @return String ID of the event
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the QR code
     * @param qrcode String representing the QR code
     */
    public void setQRcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * Sets the event ID associated with this QR code
     * @param eventID String ID of the event
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
