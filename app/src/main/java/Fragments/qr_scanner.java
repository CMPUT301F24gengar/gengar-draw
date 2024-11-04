package Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.encoder.QRCode;

import Classes.Event;
import Classes.EventManager;
import Classes.QRcode;
import Classes.QRcodeManager;

public class qr_scanner extends Fragment {

    private String eventID;
    private EventManager eventManager;
    private QRcode qrcode;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        initQRCodeScanner();
        return view;
    }

    // Opens up the QR code scanner
    private void initQRCodeScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    // Gets the result of the QR code scan
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedData = result.getContents();
                handleScannedData(scannedData);
            }
        }
    }


    private void handleScannedData(String QRcode) {
        Log.d("QRScanner", "scanned data: " + QRcode);

        // Create an instance of QRcodeManager
        QRcodeManager qrCodeManager = new QRcodeManager();
        eventManager = new EventManager();
        // Search for the QR code
        qrCodeManager.searchQRcode(QRcode, new QRcodeManager.OnQRcodeSearchListener() {
            @Override
            public void onQRcodeFound(QRcode qrCodeObject) {
                // Retrieve the eventID from the found QR code object
                String eventID = qrCodeObject.getEventID();
                Log.d("QRScanner", "scanned data: " + QRcode);

                // Fetch the event details using the eventID
                eventManager.getEvent(eventID, new EventManager.OnEventFetchListener() {
                    @Override
                    public void onEventFetched(Event event) {
                        if (event != null) {
                            // Prepare to navigate to the event details fragment
                            Bundle bundle = new Bundle();
                            bundle.putString("eventID", eventID);

                            event_details eventDetailsFragment = new event_details();
                            eventDetailsFragment.setArguments(bundle); // Pass the eventID to the fragment
                            // Navigate to event details fragment

                            if (getActivity() instanceof MainActivity) {
                                MainActivity activity = (MainActivity) getActivity();
                                activity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, eventDetailsFragment) // Replace with your container ID
                                        .addToBackStack(null) // Optional: to add to back stack
                                        .commit();
                            } else {
                                // Handle the error
                                Log.e("QRScanner", "Activity is not an instance of MainActivity");
                            }
                        } else {
                            Toast.makeText(getContext(), "Event not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onEventFetchError(Exception e) {
                        Log.e("QRScanner", "Error fetching event: ", e);
                        Toast.makeText(getContext(), "Error fetching event details.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onQRcodeNotFound() {
                Toast.makeText(getContext(), "QR code not found!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.e("QRScanner", "Error searching QR code: ", e);
                Toast.makeText(getContext(), "Error occurred while searching QR code.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
