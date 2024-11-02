package Classes;

import android.net.Uri;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacilityManager {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private CollectionReference facilitiesRef;

    public FacilityManager(){
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        facilitiesRef = db.collection("facilities");
        storage = FirebaseStorage.getInstance();
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
        String userId = document.getString("DeviceID");

        return new Facility(name, location, description, pictureURL, events, userId);
    }

    public void addUpdateFacility(Facility facility, String deviceID) {
        facilitiesRef.document(deviceID).set(facility);
    }

    public void deleteFacility(Facility facility){}//to be implemented

    public void uploadFacilityImage(Uri imgUri, String deviceId, FacilityManager.OnUploadPictureListener listener){
        StorageReference storageRef = storage.getReference().child("facilityImages/" + deviceId);
        storageRef.putFile(imgUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(listener::onSuccess);
                })
                .addOnFailureListener(listener::onError);
    }

    public void updateFacilityImage(String imgUrl, String deviceId, FacilityManager.OnUpdateListener listener){
        db.collection("facilities").document(deviceId)
                .update("pictureURL",imgUrl)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }
    public void deleteFacilityImage(String deviceId, FacilityManager.OnDeleteListener listener){
        StorageReference storageRef = storage.getReference().child("facilityImages/" + deviceId);
        storageRef.delete()
                .addOnSuccessListener(aVoid -> {
                    db.collection("facilities").document(deviceId)
                            .update("pictureURL",null)
                            .addOnSuccessListener(aVoid1 -> listener.onSuccess())
                            .addOnFailureListener(listener::onError);
                })
                .addOnFailureListener(listener::onError);
    }

    // interface to handle facility check result
    public interface OnFacilityCheckListener {
        void onFacilityExists(Facility facility); // case when facility exists
        void onFacilityNotExists(); // case when facility doesn't exist
        void onError(Exception e); // Handle errors
    }

    public interface OnUploadPictureListener{
        void onSuccess(Uri downloadUrl);
        void onError(Exception e);
    }

    public interface OnUpdateListener{
        void onSuccess();
        void onError(Exception e);
    }

    public interface OnDeleteListener {
        void onSuccess();
        void onError(Exception e);
    }
}
