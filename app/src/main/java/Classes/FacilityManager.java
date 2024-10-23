package Classes;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacilityManager {
    private FirebaseFirestore db;
    private CollectionReference facilitiesRef;

    public FacilityManager(){
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        facilitiesRef = db.collection("facilities");
    }

    public boolean checkFacilityExists(String deviceId){return Boolean.TRUE;}
    public Facility createFacilityFromDocument(DocumentSnapshot document){
        /**
         * create a facility object from information stored in firebase
         */
        String name = document.getString("name");
        String location = document.getString("location");
        String description = document.getString("location");
        String pictureURL = document.getString("pictureURL");
        List<String> events = document.contains("events") ? (List<String>) document.get("events") : new ArrayList<>();
        String userId = document.getString("DeviceID");

        return new Facility(name, location, description, pictureURL, events, userId);

    }
    public void addFacility(Facility facility){
        /**
         * adds facility to firebase by corresponding user ID
         */
        if (!checkFacilityExists(facility.getUsrId())){
            //only add if facility doesn't already exist
            facilitiesRef.document(facility.getUsrId()).set(facility);
        }
    }
    public void updateFacility(Facility facility, String deviceId){
        /**
         * updates the firebase storage with the information from the given facility object
         * specifically, gives the facility owned by deviceId the attributes of given facility
         */
        if (checkFacilityExists(deviceId)){
            //only update if facility exists
            facilitiesRef.document(deviceId).set(facility);

        }else{
            addFacility(facility);
        }
    }
    public void deleteFacility(Facility facility){}//to be implemented
}
