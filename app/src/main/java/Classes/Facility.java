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
    public String getName(){return this.name;}
    public double getLatitude(){return this.latitude;}
    public double getLongitude(){return this.longitude;}
    public String getLocation(){return this.location;}
    public String getDescription(){return this.description;}
    public String getPictureURL(){return this.pictureURL;}
    public List<String> getEvents(){return this.events;}
    public String getDeviceID(){return this.deviceID;}
    //setters
    public void setName(String name){this.name = name;}
    public void setLatitude(double latitude){this.latitude = latitude;}
    public void setLongitude(double longitude){this.longitude = longitude;}
    public void setLocation(String location){this.location = location;}
    public void setDescription(String description){this.description = description;}
    public void setPictureURL(String pictureURL){this.pictureURL = pictureURL;}
    public void setEvents(List<String> events){this.events = events;}
    public void setDeviceID(String deviceID){this.deviceID = deviceID;}

    /**
     * Adds an event to this facility's list of events
     * @param eventID   String id representing Event
     */
    public void addEvent(String eventID){
        this.events.add(eventID);
    }
}
