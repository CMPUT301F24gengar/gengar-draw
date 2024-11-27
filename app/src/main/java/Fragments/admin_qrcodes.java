package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.gengardraw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Adapters.QRcodeAdapter;
import Adapters.UserProfileAdapter;
import Classes.QRcode;
import Classes.QRcodeManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * @author Dion
 * admin_qrcodes
 * This fragment displays a list of qr codes currently stored in firebase.
 */

public class admin_qrcodes extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private QRcodeManager qrCodeManager;
    private ArrayList<QRcode> qrCodes;
    private ArrayList<String> qrCodesId;
    private ArrayList<QRcode> searchQRCodes;
    private ArrayList<String> searchQRCodesId;
    private RecyclerView.LayoutManager layoutManager;
    private QRcodeAdapter customAdapter;
    private QRcodeAdapter searchCustomAdapter;
    private ImageView searchButton;
    private EditText searchBarText;
    private String searchQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_qrcodes, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.admin_qrcodes_list);
        searchButton = view.findViewById(R.id.admin_qrcode_search_button);
        searchBarText = view.findViewById(R.id.admin_qrcode_search_bar);
        qrCodeManager = new QRcodeManager();
        qrCodes = new ArrayList<>();
        qrCodesId = new ArrayList<>();
        searchQRCodes = new ArrayList<>();
        searchQRCodesId = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new QRcodeAdapter(getContext(),qrCodesId, true);
        searchCustomAdapter = new QRcodeAdapter(getContext(),searchQRCodesId, true);


        fetchQRcodes(new OnQRCodeLoadedListener() {
            @Override
            public void onQRCodeLoaded(ArrayList<String> qrCodes) {
                //adding qrCodes to adapter
                Log.d("size in : ","size: " + qrCodes.size());
                recyclerView.setLayoutManager(layoutManager); //arranges recyclerView in linear form
                recyclerView.setAdapter(customAdapter);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchQRCodesId.clear(); //in case it is filled from a search before.
                searchQuery = searchBarText.getText().toString(); //get text from searchbar edittext
                //query with the text
                for (int i=0; i<qrCodesId.size();i++){
                    if(qrCodesId.get(i).toLowerCase().contains(searchQuery.toLowerCase())){
                        searchQRCodesId.add(qrCodesId.get(i));
                    }
                }
                searchCustomAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(searchCustomAdapter);
            }
        });

        return view;
    }

    /**
     * interface for listener to check if all the QRCodes have been loaded.
     */
    public interface OnQRCodeLoadedListener {
        void onQRCodeLoaded(ArrayList<String> qrCodes);
    }

    /**
     * This method fetches all the qr codes from firebase currently stored
     * and adds it to a list of qr codes.
     * @param listener to check if all the qr codes have been loaded.
     */
    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchQRcodes(OnQRCodeLoadedListener listener) {
        db.collection("qrcodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                listener.onQRCodeLoaded(qrCodesId);
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("qrcode", document.getId() + " => " + document.getData());

                                String qrCodeID = document.getId();
                                Log.d("qrcodeID","QRCODEID: "+qrCodeID);
                                qrCodesId.add(qrCodeID);
                                if(qrCodesId.size() == task.getResult().size()){
                                    listener.onQRCodeLoaded(qrCodesId);
                                    Log.d("qrcode loaded","loaded");
                                }
                            }
                        } else {
                            Log.d("qrcode doc", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}