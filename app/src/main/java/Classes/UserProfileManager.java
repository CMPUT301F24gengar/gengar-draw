package Classes;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserProfileManager {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public UserProfileManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // Method to check if user exists by device ID
    public void checkUserExists(String deviceID, OnUserCheckListener listener) {
        db.collection("users")
                .document(deviceID) // Use the deviceID directly as the document ID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // User exists, create UserProfile
                            UserProfile userProfile = createUserProfileFromDocument(document);
                            listener.onUserExists(userProfile);
                        } else {
                            // User does not exist
                            listener.onUserNotExists();
                        }
                    } else {
                        // Handle any error from the task
                        listener.onError(task.getException());
                    }
                });
    }


    //method to create UserProfile from Firestore document
    private UserProfile createUserProfileFromDocument(DocumentSnapshot document) {
        String deviceID = document.getString("deviceID");
        String name = document.getString("name");
        String email = document.getString("email");
        String phoneNumber = document.getString("phoneNumber");
        String pictureURL = document.getString("pictureURL");
        String facilityURL = document.getString("facilityURL");
        Boolean allowNotifications = document.getBoolean("allowNotifications");

        List<String> notificationsArray = document.contains("notificationsArray") ? (List<String>) document.get("notificationsArray") : new ArrayList<>();
        List<String> joinedEvents = document.contains("joinedEvents") ? (List<String>) document.get("joinedEvents") : new ArrayList<>();

        Boolean isOrganizer = document.getBoolean("organizer");
        Boolean isAdmin = document.getBoolean("admin");

        // Create and return the UserProfile object
        return new UserProfile(
                deviceID,
                name,
                email,
                phoneNumber,
                pictureURL,
                facilityURL,
                Boolean.TRUE.equals(allowNotifications),
                notificationsArray,
                joinedEvents,
                Boolean.TRUE.equals(isOrganizer),
                Boolean.TRUE.equals(isAdmin)
        );
    }


    // Method to add a user profile to Firestore
    public void addUserProfile(UserProfile userProfile, String deviceID) {
        assert db != null;
        db.collection("users").document(deviceID) // Use deviceID as the document ID
                .set(userProfile);
    }

    // Method to update a user profile in Firestore
    public void updateUserProfile(UserProfile userProfile, String deviceID) {
        assert db != null;
        db.collection("users").document(deviceID) // Use deviceID as the document ID
                .set(userProfile);
    }

    // Method to delete a user profile from Firestore
    public void deleteUserProfile(String deviceID) {
        assert db != null;
        db.collection("users").document(deviceID) // Use deviceID as the document ID
                .delete();
    }

    // Method to return a user profile from Firestore
    public void getUserProfile(String deviceID, final OnUserProfileFetchListener callback) {
        assert db != null;
        db.collection("users").document(deviceID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserProfile userProfile = createUserProfileFromDocument(document);
                            callback.onUserProfileFetched(userProfile);
                        } else {
                            callback.onUserProfileFetched(null);  // No user profile found
                        }
                    } else {
                        callback.onUserProfileFetchError(task.getException());
                    }
                });
    }



    public void uploadProfilePicture(Uri picUri, String deviceID, OnUploadPictureListener listener) {
        StorageReference storageRef = storage.getReference().child("profilePictures/" + deviceID);
        StorageReference imageFilePath = storageRef.child(Objects.requireNonNull(picUri.getLastPathSegment()));

        imageFilePath.putFile(picUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        listener.onSuccess(uri);
                        updateProfilePictureInFireStore(deviceID, downloadUrl, new OnUpdateListener() {
                            @Override
                            public void onSuccess() {
                                // Handle success if needed
                            }

                            @Override
                            public void onError(Exception e) {
                                // Handle error if needed
                            }
                        });
                    });
                })
                .addOnFailureListener(listener::onError);
    }

    public void updateProfilePictureInFireStore(String deviceID, String picURL, OnUpdateListener listener) {
        db.collection("users").document(deviceID)
                .update("pictureURL", picURL)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }



    public void deleteProfilePicture(String deviceID, OnDeleteListener listener) {
        StorageReference storageRef = storage.getReference().child("profilePictures/" + deviceID);
        storageRef.delete()
                .addOnSuccessListener(aVoid -> {
                    db.collection("users").document(deviceID)
                            .update("pictureURL",null)
                            .addOnSuccessListener(aVoid1 -> listener.onSuccess())
                            .addOnFailureListener(listener::onError);
                })
                .addOnFailureListener(listener::onError);
    }

    // interface to handle user check result
    public interface OnUserCheckListener {
        void onUserExists(UserProfile userProfile); // case when user exists
        void onUserNotExists(); //case when user doesn't exist
        void onError(Exception e); // Handle errors
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

    public interface OnUserProfileFetchListener {
        void onUserProfileFetched(UserProfile userProfile);
        void onUserProfileFetchError(Exception e);
    }
}