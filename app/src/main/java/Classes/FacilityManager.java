package Classes;

import android.widget.Button;

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

    // Method to check if user exists by device ID
    public void checkFacilityExists(String deviceID, OnFacilityCheckListener listener) {
        db.collection("facilities")
                .document(deviceID) // Use the deviceID directly as the document ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // facility exists, create Facility
                            Facility facility = createFacilityFromDocument(document);
                            listener.onFacilityExists(facility);
                        } else {
                            // Facility does not exist
                            listener.onFacilityNotExists();
                        }
                    } else {
                        // Handle any error from the task
                        listener.onError(task.getException());
                    }
                });
    }

    public Facility createFacilityFromDocument(DocumentSnapshot document){
        /**
         * create a facility object from information stored in firebase
         */
        String name = document.getString("name");
        String location = document.getString("location");
        String description = document.getString("description");
        String pictureURL = document.getString("pictureURL");
        List<String> events = document.contains("events") ? (List<String>) document.get("events") : new ArrayList<>();
        String userId = document.getString("deviceID");

        return new Facility(name, location, description, pictureURL, events, userId);
    }

    public void addFacility(Facility facility) {
        facilitiesRef.document(facility.getDeviceID()).set(facility);
    }

    public void updateFacility(Facility facility) {
        facilitiesRef.document(facility.getDeviceID()).set(facility);
    }

    public void deleteFacility(Facility facility){}//to be implemented

    // interface to handle facility check result
    public interface OnFacilityCheckListener {
        void onFacilityExists(Facility facility); // case when facility exists
        void onFacilityNotExists(); // case when facility doesn't exist
        void onError(Exception e); // Handle errors
    }
}
