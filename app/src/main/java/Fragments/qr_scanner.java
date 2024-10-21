package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.gengardraw.R;

public class qr_scanner extends Fragment {

    private FrameLayout viewBtn;
    private FrameLayout signUpBtn;

    private boolean signup;
    private String eventID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);

        viewBtn = view.findViewById(R.id.qr_scanner_view_btn);
        signUpBtn = view.findViewById(R.id.qr_scanner_signup_btn);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup = false;
                eventID = scanQRcode();
                Log.d("qr_scanner", "eventID: " + eventID + " view");
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup = true;
                eventID = scanQRcode();
                Log.d("qr_scanner", "eventID: " + eventID + " signup");
            }
        });
        return view;
    }

    private String scanQRcode(){
        return "";
    }
}