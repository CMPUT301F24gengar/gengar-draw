package Classes;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class FacilityManager {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public FacilityManager() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // Check if a facility exists
    public void checkFacilityExists(String deviceID, OnFacilityCheckListener listener) {
        db.collection("facilities")
                .document(deviceID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Facility facility = createFacilityFromDocument(document);
                            listener.onFacilityExists(facility);
                        } else {
                            listener.onFacilityNotExists();
                        }
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    // Create Facility object from Firestore DocumentSnapshot
    private Facility createFacilityFromDocument(DocumentSnapshot document) {
        String name = document.getString("name");
        double latitude = document.getDouble("latitude");
        double longitude = document.getDouble("longitude");
        String location = document.getString("location");
        String description = document.getString("description");
        String pictureURL = document.getString("pictureURL");
        List<String> events = document.contains("events") ? (List<String>) document.get("events") : new ArrayList<>();
        String deviceID = document.getString("deviceID");

        return new Facility(name, latitude, longitude, location, description, pictureURL, events, deviceID);
    }

    // Add new facility to Firestore
    public void addFacility(Facility facility, String deviceID) {
        db.collection("facilities").document(deviceID)
                .set(facility);
    }

    // Update facility details in Firestore
    public void updateFacility(Facility facility, String deviceID) {
        db.collection("facilities").document(deviceID)
                .set(facility);
    }

    // Delete a facility from Firestore
    public void deleteFacility(String deviceID) {
        db.collection("facilities").document(deviceID)
                .delete();
    }

    // Retrieve a facility from Firestore
    public void getFacility(String deviceID, OnFacilityFetchListener listener) {
        db.collection("facilities").document(deviceID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Facility facility = createFacilityFromDocument(document);
                            listener.onFacilityFetched(facility);
                        } else {
                            listener.onFacilityFetched(null); // No facility found
                        }
                    } else {
                        listener.onFacilityFetchError(task.getException());
                    }
                });
    }

    // Upload a facility picture
    public void uploadFacilityPicture(Uri picUri, String deviceID, OnUploadPictureListener listener) {
        StorageReference storageRef = storage.getReference().child("facilityPictures/" + deviceID);
        storageRef.putFile(picUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(listener::onSuccess);
                })
                .addOnFailureListener(listener::onError);
    }

    // Update facility picture URL in Firestore
    public void updateFacilityPictureInFirestore(String deviceID, String picURL, OnUpdateListener listener) {
        db.collection("facilities").document(deviceID)
                .update("pictureURL", picURL)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    // Delete a facility picture
    public void deleteFacilityPicture(String deviceID, OnDeleteListener listener) {
        StorageReference storageRef = storage.getReference().child("facilityPictures/" + deviceID);
        storageRef.delete()
                .addOnSuccessListener(aVoid -> {
                    db.collection("facilities").document(deviceID)
                            .update("pictureURL", null)
                            .addOnSuccessListener(aVoid1 -> listener.onSuccess())
                            .addOnFailureListener(listener::onError);
                })
                .addOnFailureListener(listener::onError);
    }
    // Listener interfaces
    public interface OnFacilityCheckListener {
        void onFacilityExists(Facility facility);
        void onFacilityNotExists();
        void onError(Exception e);
    }

    public interface OnFacilityFetchListener {
        void onFacilityFetched(Facility facility);
        void onFacilityFetchError(Exception e);
    }

    public interface OnUploadPictureListener {
        void onSuccess(Uri downloadUrl);
        void onError(Exception e);
    }

    public interface OnUpdateListener {
        void onSuccess();
        void onError(Exception e);
    }

    public interface OnDeleteListener {
        void onSuccess();
        void onError(Exception e);
    }
}
