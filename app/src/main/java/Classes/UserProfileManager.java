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

/**
 * UserProfileManager
 *
 *     The <code>UserProfileManager</code> class provides methods for managing user profiles, including adding, updating,
 *     deleting, and fetching user profiles in Firebase Firestore. It also provides methods for uploading, updating, and deleting
 *     user profile pictures in Firebase Storage.
 * 
 * @author Rafi, Rehan, Prachetas
 * @see UserProfile
 */
public class UserProfileManager {
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    /**
     * Empty constructor for UserProfileManager
     */
    public UserProfileManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    /**
     * Checks if a user exists in Firestore by their device ID.
     * @param deviceID the device ID used to check for an existing user profile.
     * @param listener the listener to handle the result of the check.
     */
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


    /**
     * Creates a <code>UserProfile</code> object from a Firestore document snapshot.
     * @param document Firestore document snapshot containing user data.
     * @return a new <code>UserProfile</code> object populated with the data from the document.
     */
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


    /**
     * Adds a new user profile to Firestore.
     * @param userProfile the <code>UserProfile</code> object to add to Firestore.
     * @param deviceID the unique device ID for the user.
     */
    public void addUserProfile(UserProfile userProfile, String deviceID) {
        assert db != null;
        db.collection("users").document(deviceID) // Use deviceID as the document ID
                .set(userProfile);
    }

    /**
     * Updates an existing user profile in Firestore.
     * @param userProfile the <code>UserProfile</code> object containing updated data.
     * @param deviceID the unique device ID for the user.
     */
    public void updateUserProfile(UserProfile userProfile, String deviceID) {
        assert db != null;
        db.collection("users").document(deviceID) // Use deviceID as the document ID
                .set(userProfile);
    }

    /**
     * Deletes a user profile from Firestore using the device ID.
     * @param deviceID the unique device ID for the user to delete.
     */
    public void deleteUserProfile(String deviceID) {
        assert db != null;
        EventListsManager eventListsManager = new EventListsManager();
        //delete image from firebase
        StorageReference storageRef = storage.getReference().child("profilePictures/" + deviceID);
        storageRef.delete();

        //delete facility in a user profile
        FacilityManager facilityManager = new FacilityManager();
        facilityManager.deleteFacility(deviceID);

        getUserProfile(deviceID, new UserProfileManager.OnUserProfileFetchListener() {
            @Override
            public void onUserProfileFetched(UserProfile userProfileFetched) {
                for (String eventID : userProfileFetched.getJoinedEvents()){
                    eventListsManager.removeUserFromAllLists(eventID, deviceID);
                }
            }
            @Override
            public void onUserProfileFetchError(Exception e) {
                //Handle the error
            }
        });


    }

    /**
     * Fetches a user profile from Firestore based on the device ID.
     * @param deviceID the unique device ID of the user profile to fetch.
     * @param callback the callback listener for handling the fetched user profile or any errors.
     */
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

    /**
     * Uploads a profile picture to Firebase Storage and updates the Firestore user profile with the image URL.
     * @param picUri the URI of the profile picture to upload.
     * @param deviceID the unique device ID for the user.
     * @param listener the listener for handling the result of the upload operation.
     */
    public void uploadProfilePicture(Uri picUri, String deviceID, OnUploadPictureListener listener) {
        StorageReference storageRef = storage.getReference().child("profilePictures/" + deviceID);

        storageRef.putFile(picUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        updateProfilePictureInFireStore(deviceID, downloadUrl, new OnUpdateListener() {
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
     * Updates the profile picture URL in the Firestore user profile.
     * @param deviceID the unique device ID for the user.
     * @param picURL the URL of the new profile picture.
     * @param listener the listener to handle the result of the update operation.
     */
    public void updateProfilePictureInFireStore(String deviceID, String picURL, OnUpdateListener listener) {
        db.collection("users").document(deviceID)
                .update("pictureURL", picURL)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    /**
     * Deletes a user's profile picture from Firebase Storage and Firestore.
     * @param deviceID the unique device ID for the user.
     * @param listener the listener to handle the result of the deletion operation.
     */
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

    /**
     * Interface for handling the result of user existence check.
     */
    public interface OnUserCheckListener {
        void onUserExists(UserProfile userProfile); // case when user exists
        void onUserNotExists(); //case when user doesn't exist
        void onError(Exception e); // Handle errors
    }

    /**
     * Interface for handling the result of uploading a profile picture.
     */
    public interface OnUploadPictureListener {
        void onSuccess(Uri downloadUrl);
        void onError(Exception e);
    }

    /**
     * Interface for handling the result of updating the user profile or profile picture.
     */
    public interface OnUpdateListener {
        void onSuccess();
        void onError(Exception e);
    }

    /**
     * Interface for handling the result of deleting the user profile or profile picture.
     */
    public interface OnDeleteListener {
        void onSuccess();
        void onError(Exception e);
    }

    /**
     * Interface for handling the result of fetching a user profile.
     */
    public interface OnUserProfileFetchListener {
        void onUserProfileFetched(UserProfile userProfile);
        void onUserProfileFetchError(Exception e);
    }
}