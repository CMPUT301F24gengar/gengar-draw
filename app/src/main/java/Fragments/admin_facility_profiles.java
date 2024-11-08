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

import com.example.gengardraw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Adapters.FacilityAdapter;
import Adapters.UserProfileAdapter;
import Classes.Facility;
import Classes.FacilityManager;

/**
 * @author Dion
 * admin_facility_profiles
 * This fragment displays the list of facility profiles that an admin can browse through.
 */

public class admin_facility_profiles extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FacilityManager facilityManager;
    private ArrayList<Facility> facilities;
    private ArrayList<Facility> searchFacilities;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView searchButton;
    private EditText searchBarText;
    private String searchQuery;
    private FacilityAdapter customAdapter;
    private FacilityAdapter searchCustomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_facility_profiles, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.admin_facility_profiles_list);
        searchButton = view.findViewById(R.id.admin_facility_profiles_search_button);
        searchBarText = view.findViewById(R.id.admin_facility_profiles_search_bar);
        facilityManager= new FacilityManager();
        facilities = new ArrayList<>();
        searchFacilities = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new FacilityAdapter(getContext(),facilities);
        searchCustomAdapter = new FacilityAdapter(getContext(),searchFacilities);

        fetchFacilities(new OnProfilesLoadedListener() {
            @Override
            public void onProfilesLoaded(ArrayList<Facility> facilities) {

                //adding facilities to adapter
                recyclerView.setLayoutManager(layoutManager); //arranges recyclerView in linear form
                recyclerView.setAdapter(customAdapter);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFacilities.clear(); //in case it is filled from a search before.
                searchQuery = searchBarText.getText().toString(); //get text from searchbar edittext
                //query with the text
                for (int i=0; i<facilities.size();i++){
                    if(facilities.get(i).getName().toLowerCase().contains(searchQuery.toLowerCase())){
                        searchFacilities.add(facilities.get(i));
                    }
                }
                searchCustomAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(searchCustomAdapter);
            }
        });

        return view;

    }

    /**
     * interface for the listener that checks if all the facilities have been loaded.
     */
    public interface OnProfilesLoadedListener {
        void onProfilesLoaded(ArrayList<Facility> facilities);
    }

    /**
     * fetchFacilities
     * @param listener
     * method to get all the facilities in firebase currently and add it to the facility array list.
     */
    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchFacilities(OnProfilesLoadedListener listener) {
        db.collection("facilities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                listener.onProfilesLoaded(facilities);
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("facility", document.getId() + " => " + document.getData());

                                String currentDeviceID = document.getId();

                                facilityManager.getFacility(currentDeviceID, new FacilityManager.OnFacilityFetchListener() {
                                    @Override
                                    public void onFacilityFetched(Facility facility) {

                                        facilities.add(facility);
                                        // Check if all documents have been processed
                                        if (facilities.size() == task.getResult().size()) {
                                            listener.onProfilesLoaded(facilities); // Notify when done
                                        }
                                    }
                                    @Override
                                    public void onFacilityFetchError(Exception e) {
                                        Log.d("facilities fetch ERROR", e.toString());
                                    }
                                });
                            }
                        } else {
                            Log.d("facility doc", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}