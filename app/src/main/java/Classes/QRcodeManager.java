package Classes;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * QRcodeManager
 *
 *     The <code>QRcodeManager</code> class manages QR code operations within Firebase Firestore,
 *     including adding, retrieving, and searching for QR codes by event ID.
 *
 * @author Rehan
 * @see QRcode
 */
public class QRcodeManager {
    private final FirebaseFirestore db;
    private final CollectionReference qrCodesRef;

    /**
     * Initializes a new instance of <code>QRcodeManager</code> and sets up the Firestore collection reference.
     */
    public QRcodeManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        qrCodesRef = db.collection("qrcodes");
    }


    /**
     * Creates a <code>QRcode</code> object from a Firestore document snapshot.
     * @param document Firestore document snapshot containing QR code data.
     * @return a new <code>QRcode</code> object with data from the document.
     */
    public QRcode createQRcodeFromDocument(DocumentSnapshot document) {
        String eventID = document.getString("eventID");
        String qrcode = document.getString("qrcode");
        return new QRcode(qrcode, eventID);
    }

    /**
     * Adds a new QR code to the Firestore collection.
     * @param qrcode the <code>QRcode</code> object to add.
     * @return the generated unique ID for the added QR code.
     */
    public String addQRcode(QRcode qrcode) {
        String QRCodeID = qrCodesRef.document().getId();
        qrcode.setQRcode(QRCodeID);
        qrCodesRef.document(QRCodeID).set(qrcode);

        return QRCodeID;
    }

    /**
     * Fetches a QR code by its ID and executes a callback upon completion.
     * @param qrcodeID the unique ID of the QR code to fetch.
     * @param callback the callback listener for handling the result.
     */
    public void getQRcode(String qrcodeID, onQRcodeFetchListener callback) {
        db.collection("qrcodes").document(qrcodeID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        callback.onQRcodeFetched(createQRcodeFromDocument(task.getResult()));
                    } else {
                        callback.onQRcodeFetched(null);
                    }
                })
                .addOnFailureListener(callback::onQRcodeFetchError);
    }

    /**
     * retrieves the String QRcode given the eventID of the specific event
     * @param value String
     * @param listener OnQRcodeSearchListener
     * @see OnQRcodeSearchListener
     */
    public void searchQRcode(String value, OnQRcodeSearchListener listener) {
        // Log the search action
        Log.d("QRcodeManager", "Searching for QR code: " + value);

        qrCodesRef.whereEqualTo("qrcode", value) // Use "qrcode" field
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        listener.onQRcodeFound(createQRcodeFromDocument(document));
                    } else {
                        Log.d("QRcodeManager", "No document found for QR code: " + value);
                        listener.onQRcodeNotFound(); // No document found
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("QRcodeManager", "Error searching QR code: ", e);
                    listener.onError(e);
                });
    }

    /**
     * Deletes a qrcode object from the database.
     * @param qrcode The ID of the qrcode object to be deleted
     */
    public void deleteQRcode(String qrcode){
        db.collection("qrcodes")
                .document(qrcode)
                .delete();
    }

    /**
     * Interface for handling QR code search results.
     */
    public interface OnQRcodeSearchListener {
        void onQRcodeFound(QRcode qrcode);
        void onQRcodeNotFound();
        void onError(Exception e);
    }

    /**
     * Interface for handling QR code fetch results.
     */
    public interface onQRcodeFetchListener {
        void onQRcodeFetched(QRcode qrcode);
        void onQRcodeFetchError(Exception e);
    }
}
