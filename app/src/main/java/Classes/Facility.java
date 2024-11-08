package Classes;

import java.util.List;

/**
 * <h1>Facility</h1>
 * <p>
 *     Facility class locally stores information relevant to specific facilities
 *     <ul>data: <li>name</li> <li>location</li> <li>description</li> <li>local instance of facility photo</li> <li>list of event ids</li></ul>
 *     <ul>methods include: <li>constructors</li> <li>getters</li> <li>setters</li></ul>
 * </p>
 * @author Meghan, Rheanna
 * @see FacilityManager
 */
public class Facility {
    //data
    private String name;
    private double latitude;
    private double longitude;
    private String location;//location to be updated to match project's geolocation implementation
    private String description;

    private String pictureURL;
    private List<String> events;

    private String deviceID;
    //constructors

    /**
     * Empty Facility Constructor
     */
    public Facility(){}

    /**
     * Complete Facility Constructor
     * @param name  String name of facility
     * @param latitude
     * @param longitude
     * @param location  String representation of facility location
     * @param description   String
     * @param pictureURL    String URL representation holds facility image information
     * @param events    List of events belonging to facility, events represented by their String id
     * @param deviceID  Unique id of user's device
     */
    public Facility(String name, double latitude, double longitude, String location, String description, String pictureURL, List<String> events, String deviceID){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.description = description;
        this.pictureURL = pictureURL;
        this.events = events;
        this.deviceID = deviceID;
    }

    //getters

    /**
     * gets facility name
     * @return String facility name
     */
    public String getName(){return this.name;}

    /**
     * gets facility location latitude
     * @return double latitude
     */
    public double getLatitude(){return this.latitude;}

    /**
     * gets facility location longitude
     * @return double longitude
     */
    public double getLongitude(){return this.longitude;}

    /**
     * gets facility location
     * @return String representation of location
     */
    public String getLocation(){return this.location;}

    /**
     * gets facility description
     * @return String description
     */
    public String getDescription(){return this.description;}

    /**
     * gets facility image URL
     * @return String URL representation of image
     */
    public String getPictureURL(){return this.pictureURL;}

    /**
     * gets list of events at this facility
     * @return List of String ids representing events
     */
    public List<String> getEvents(){return this.events;}

    /**
     * gets device id attributed to this facility
     * @return unique string device id
     */
    public String getDeviceID(){return this.deviceID;}

    //setters

    /**
     * sets facility name
     * @param name String name
     */
    public void setName(String name){this.name = name;}

    /**
     * sets facility's location latitude
     * @param latitude double
     */
    public void setLatitude(double latitude){this.latitude = latitude;}

    /**
     * sets facility's location longitude
     * @param longitude double
     */
    public void setLongitude(double longitude){this.longitude = longitude;}

    /**
     * sets facility's location
     * @param location string representation of location
     */
    public void setLocation(String location){this.location = location;}

    /**
     * sets facility's description
     * @param description String description
     */
    public void setDescription(String description){this.description = description;}

    /**
     * sets facility image's URL
     * @param pictureURL    String URL representation of image
     */
    public void setPictureURL(String pictureURL){this.pictureURL = pictureURL;}

    /**
     * sets entire list of events held at this facility
     * @param events list of string ids representing events
     */
    public void setEvents(List<String> events){this.events = events;}

    /**
     * sets the device id attributed to this facility
     * @param deviceID  unique string id
     */
    public void setDeviceID(String deviceID){this.deviceID = deviceID;}

    /**
     * Adds an event to this facility's list of events
     * @param eventID   String id representing Event
     */
    public void addEvent(String eventID){
        this.events.add(eventID);
    }
}
