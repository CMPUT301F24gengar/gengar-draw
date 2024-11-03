package Classes;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class QRcodeManager {
    private final FirebaseFirestore db;
    private final CollectionReference qrCodesRef;

    public QRcodeManager() {
        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        qrCodesRef = db.collection("qrcodes");
    }

    public QRcode createQRcodeFromDocument(DocumentSnapshot document) {
        String eventID = document.getString("eventID");
        String qrcode = document.getString("QRCode");
        return new QRcode(qrcode, eventID);
    }

    public String addQRcode(QRcode qrcode) {
        String QRCodeID = qrCodesRef.document().getId();
        qrcode.setQRcode(QRCodeID);
        qrCodesRef.document(QRCodeID).set(qrcode);

        return QRCodeID;
    }

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

    public interface OnQRcodeSearchListener {
        void onQRcodeFound(QRcode qrcode);
        void onQRcodeNotFound();
        void onError(Exception e);
    }


    public interface onQRcodeFetchListener {
        void onQRcodeFetched(QRcode qrcode);
        void onQRcodeFetchError(Exception e);
    }
}
