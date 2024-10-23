package Classes;

import java.util.List;

public class Facility {
    //data
    private String name;
    private String location;//location to be updated to match project's geolocation implementation
    private String description;

    private String pictureURL;
    private List<String> events; //to be replaced with List<Event>

    private String usrId;
    //constructors
    public Facility(){};
    public Facility(String name, String location, String description, String pictureURL, List<String> events, String usrId){
        this.name = name;
        this.location = location;
        this.description = description;
        this.pictureURL = pictureURL;
        this.events = events;
        this.usrId = usrId;
    }
    //getters
    public String getName(){return this.name;}
    public String getLocation(){return this.location;}
    public String getDescription(){return this.description;}
    public String getPictureURL(){return this.pictureURL;}
    public List<String> getEvents(){return this.events;}
    public String getUsrId(){return this.usrId;}
    //setters
    public void setName(String name){this.name = name;}
    public void setLocation(String location){this.location = location;}
    public void setDescription(String description){this.description = description;}
    public void setPictureURL(String pictureURL){this.pictureURL = pictureURL;}
    public void setEvents(List<String> events){this.events = events;}
    public void setUsrId(String usrId){this.usrId = usrId;}
}
