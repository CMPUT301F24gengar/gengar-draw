package Classes;

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

    public interface onQRcodeFetchListener {
        void onQRcodeFetched(QRcode qrcode);
        void onQRcodeFetchError(Exception e);
    }
}
