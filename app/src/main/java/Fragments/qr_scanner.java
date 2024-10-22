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
import com.example.gengardraw.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class qr_scanner extends Fragment {

    private boolean signup;
    private String eventID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);

        FrameLayout viewBtn = view.findViewById(R.id.qr_scanner_view_btn);
        FrameLayout signUpBtn = view.findViewById(R.id.qr_scanner_signup_btn);

        viewBtn.setOnClickListener(v -> {
            signup = false;
            initQRCodeScanner();
        });

        signUpBtn.setOnClickListener(v -> {
            signup = true;
            initQRCodeScanner();
        });

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

    // Handles the scanned data
    private void handleScannedData(String eventID) {
        if (signup) {
            Log.d("QRScanner", "Sign up with scanned data: " + eventID);
            // TODO : Add logic for signing up here
        } else {
            Log.d("QRScanner", "View details with scanned data: " + eventID);
            // TODO : Add logic for viewing details here
        }
    }
}
