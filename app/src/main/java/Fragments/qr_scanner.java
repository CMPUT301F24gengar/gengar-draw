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

/**
 * <h1>QR Scanner Fragment</h1>
 * <p>
 *     Handles interactions with the QR scanner fragment including scanning QR codes and opening
 *     a fragment with the event details.
 * </p>
 * @author Rafi, Rehan
 * @see QRcode
 * @see QRcodeManager
 * @see Event
 * @see EventManager
 */
public class qr_scanner extends Fragment {

    private String eventID;
    private EventManager eventManager;
    private QRcode qrcode;;

    /**
     * Construct the qr_scanner fragment view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to. The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Constructed View
     * @see Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        initQRCodeScanner();
        return view;
    }

    /**
     * Initializes the QR code scanner
     */
    private void initQRCodeScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan a QR code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    /**
     * Handles the result of the QR code scan
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     */
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


    /**
     * Handles the scanned QR code data and navigates to the event details fragment
     * @param QRcode The scanned QR code data
     */
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
