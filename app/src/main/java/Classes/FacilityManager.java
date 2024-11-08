package Classes;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Facility Manager
 *
 *     Facility Manager class handles interactions between the local instance(s) of a given facility with the firestore database
 *     data:<ul> <li>Firebase Firestore Instance</li> <li>Firebase Storage Reference</li></ul>
 *     methods:<ul> <li>Constructor</li> <li>checkFacilityExists</li> <li>createFacilityFromDocument</li> <li>addFacility</li> <li>updateFacility</li> <li>deleteFacility</li> <li>getFacility</li> <li>uploadFacilityPicture</li> <li>updateFacilityPictureInFirestore</li> <li>deleteFacilityPicture</li></ul>
 *     interfaces:<ul> <li>OnFacilityCheckListener</li> <li>OnFacilityFetchListener</li> <li>OnUploadPictureListener</li> <li>OnUpdateListener</li> <li>OnDeleteListener</li></ul>
 *
 * @author Meghan, Rheanna
 * @see Facility
 */
public class FacilityManager {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    /**
     * Facility Manager constructor
     */
    public FacilityManager() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    /**
     * Checks if a given facility exists in the database
     * @param deviceID  unique id pertaining to user's device
     * @param listener  references OnFacilityCheckListener interface
     * @see OnFacilityCheckListener
     */
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

    /**
     * Creates a Facility object from Firestore DocumentSnapshot
     * @param document  reference to a facility in the database
     * @return  Facility
     * @see Facility
     */
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

    /**
     * Adds a new facility to the Firestore
     * @param facility  Facility object to be added
     * @param deviceID  unique id pertaining to user's device
     */
    public void addFacility(Facility facility, String deviceID) {
        db.collection("facilities").document(deviceID)
                .set(facility);
    }

    /**
     * Updates a given facility's details in the Firestore
     * @param facility  Facility object to be updated
     * @param deviceID unique id pertaining to user's device
     */
    public void updateFacility(Facility facility, String deviceID) {
        db.collection("facilities").document(deviceID)
                .set(facility);
    }

    /**
     * Deletes a given facility from the Firestore
     * @param deviceID unique id pertaining to user's device
     */
    public void deleteFacility(String deviceID) {
        db.collection("facilities").document(deviceID)
                .delete();
    }

    /**
     * Retrieves a specific facility from the Firestore
     * @param deviceID unique id pertaining to user's device
     * @param listener references OnFacilityFetchListener interface
     * @see OnFacilityFetchListener
     */
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

    /**
     * Uploads a facility picture to the Firestore
     * @param picUri    Uri instance of picture to be uploaded
     * @param deviceID  unique id pertaining to user's device
     * @param listener  references OnUploadPictureListener interface
     * @see OnUploadPictureListener
     */
    public void uploadFacilityPicture(Uri picUri, String deviceID, OnUploadPictureListener listener) {
        StorageReference storageRef = storage.getReference().child("facilityPictures/" + deviceID);

        storageRef.putFile(picUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updateFacilityPictureInFirestore(deviceID, downloadUrl, new OnUpdateListener() {
                            @Override
                            public void onSuccess() {
                                listener.onSuccess(uri);
                            }

                            @Override
                            public void onError(Exception e) {
                                listener.onError(e);
                            }
                        });
                    }).addOnFailureListener(listener::onError);
                })
                .addOnFailureListener(listener::onError);
    }

    /**
     * Updates a facility picture URL in the Firestore
     * @param deviceID  unique id pertaining to user's device
     * @param picURL    String URL representation of image to be updated
     * @param listener  references OnUpdateListener interface
     * @see OnUpdateListener
     */
    public void updateFacilityPictureInFirestore(String deviceID, String picURL, OnUpdateListener listener) {
        db.collection("facilities").document(deviceID)
                .update("pictureURL", picURL)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    /**
     * Deletes a facility picture
     * @param deviceID  unique id pertaining to user's device
     * @param listener  references OnDeleteListener interface
     * @see OnDeleteListener
     */
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

    /**
     * Interface for handling the result of facility existence check.
     */
    public interface OnFacilityCheckListener {
        void onFacilityExists(Facility facility);
        void onFacilityNotExists();
        void onError(Exception e);
    }

    /**
     * Interface for handling the result of fetching the facility.
     */
    public interface OnFacilityFetchListener {
        void onFacilityFetched(Facility facility);
        void onFacilityFetchError(Exception e);
    }

    /**
     * Interface for handling the result of uploading the facility picture.
     */
    public interface OnUploadPictureListener {
        void onSuccess(Uri downloadUrl);
        void onError(Exception e);
    }

    /**
     * Interface for handling the result of updating the facility picture.
     */
    public interface OnUpdateListener {
        void onSuccess();
        void onError(Exception e);
    }

    /**
     * Interface for handling the result of deleting the facility picture.
     */
    public interface OnDeleteListener {
        void onSuccess();
        void onError(Exception e);
    }
}
