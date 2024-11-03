package Classes;

public class QRcode {
    private String qrcode;
    private String eventID;

    public QRcode() {}

    public QRcode(String qrcode, String eventID) {
        this.eventID = eventID;
    }

    public String getQRcode() {
        return qrcode;
    }
    public String getEventID() {
        return eventID;
    }
    public void setQRcode(String qrcode) {
        this.qrcode = qrcode;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
