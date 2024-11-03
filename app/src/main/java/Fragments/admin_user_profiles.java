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

import com.example.gengardraw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Adapters.UserProfileAdapter;
import Classes.UserProfile;
import Classes.UserProfileManager;

public class admin_user_profiles extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private UserProfileManager userProfileManager;
    private ArrayList<UserProfile> userProfiles;
    private RecyclerView.LayoutManager layoutManager;
    UserProfileAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_user_profiles, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.admin_user_profiles_list);
        userProfileManager= new UserProfileManager();
        userProfiles = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new UserProfileAdapter(getContext(),userProfiles);


        fetchUserProfiles(new OnProfilesLoadedListener() {
            @Override
            public void onProfilesLoaded(ArrayList<UserProfile> userProfiles) {

                //adding userProfiles to adapter
                recyclerView.setLayoutManager(layoutManager); //arranges recyclerView in linear form
                recyclerView.setAdapter(customAdapter);

            }
        });

        return view;

    }

    public interface OnProfilesLoadedListener {
        void onProfilesLoaded(ArrayList<UserProfile> userProfiles);
    }

    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchUserProfiles(OnProfilesLoadedListener listener) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("userprofile", document.getId() + " => " + document.getData());

                                String currentDeviceID = document.getId();

                                userProfileManager.getUserProfile(currentDeviceID, new UserProfileManager.OnUserProfileFetchListener() {
                                    @Override
                                    public void onUserProfileFetched(UserProfile userProfile) {
                                        userProfiles.add(userProfile);
                                        // Check if all documents have been processed
                                        if (userProfiles.size() == task.getResult().size()) {
                                            listener.onProfilesLoaded(userProfiles); // Notify when done
                                        }
                                    }

                                    @Override
                                    public void onUserProfileFetchError(Exception e) {
                                        Log.d("userProfiles fetch ERROR", e.toString());
                                    }
                                });
                            }
                        } else {
                            Log.d("userprofile doc", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}