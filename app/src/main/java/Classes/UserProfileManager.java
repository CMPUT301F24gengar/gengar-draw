package Classes;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfileManager {
    private FirebaseFirestore db;

    public UserProfileManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
    }

    // Method to check if user exists by device ID
    public void checkUserExists(String deviceID, OnUserCheckListener listener) {
        db.collection("users") // Assuming collection 'users'
                .whereEqualTo("deviceID", deviceID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            // User exists
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            UserProfile userProfile = createUserProfileFromDocument(document);
                            listener.onUserExists(userProfile);
                        } else {
                            // User does not exist
                            listener.onUserNotExists();
                        }
                    } else {
                        // Handle error
                        listener.onError(task.getException());
                    }
                });
    }

    //method to create UserProfile from Firestore document
    private UserProfile createUserProfileFromDocument(DocumentSnapshot document) {
        return new UserProfile(
                document.getString("deviceID"),
                document.getString("name"),
                document.getString("email"),
                document.getString("phoneNumber"),
                document.getString("pictureURL"),
                document.getString("facilityURL"),
                document.getBoolean("allowNotifications"),
                (String[]) document.get("notificationsArray"),
                (String[]) document.get("joinedEvents"),
                document.getBoolean("isOrganizer"),
                document.getBoolean("isAdmin")
        );
    }

    // interface to handle user check result
    public interface OnUserCheckListener {
        void onUserExists(UserProfile userProfile); // case when user exists
        void onUserNotExists(); //case when user doesn't exist
        void onError(Exception e); // Handle errors
    }
}