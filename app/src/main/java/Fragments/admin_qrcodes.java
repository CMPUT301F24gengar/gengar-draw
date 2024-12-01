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

import Adapters.EventAdapter;
import Adapters.QRcodeAdapter;
import Adapters.UserProfileAdapter;
import Classes.Event;
import Classes.EventManager;
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
    private EventManager eventManager;
    private ArrayList<Event> events;
    private ArrayList<Event> searchEvents;
    private RecyclerView.LayoutManager layoutManager;
    private QRcodeAdapter customAdapter;
    private QRcodeAdapter searchCustomAdapter;
    private ImageView searchButton;
    private EditText searchBarText;
    private String searchQuery;
    private EventAdapter.OnEventClickListener eventsListener;
    private int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_user_profiles, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.admin_user_profiles_list);
        searchButton = view.findViewById(R.id.admin_user_profiles_search_button);
        searchBarText = view.findViewById(R.id.admin_user_profiles_search_bar);
        eventManager= new EventManager();
        events = new ArrayList<>();
        searchEvents = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        customAdapter = new QRcodeAdapter(getContext(),events, true);
        searchCustomAdapter = new QRcodeAdapter(getContext(),searchEvents, true);
        count = 0;


        fetchEvents(new OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(ArrayList<Event> events) {
                //adding userProfiles to adapter
                recyclerView.setLayoutManager(layoutManager);  // arranges recyclerView in linear form
                recyclerView.setAdapter(customAdapter);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEvents.clear(); //in case it is filled from a search before.
                searchQuery = searchBarText.getText().toString(); //get text from searchbar edittext
                //query with the text
                for (int i=0; i<events.size();i++){
                    if(events.get(i).getEventTitle().toLowerCase().contains(searchQuery.toLowerCase())){
                        searchEvents.add(events.get(i));
                    }
                }
                searchCustomAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(searchCustomAdapter);
            }
        });

        return view;
    }

    /**
     * interface for the listener that checks if all the events have been loaded.
     */

    public interface OnEventsLoadedListener {
        void onEventsLoaded(ArrayList<Event> events);
    }

    /**
     * fetchEvents
     * @param listener
     * method to get all the events in firebase currently and add it to the event array list.
     */
    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchEvents(OnEventsLoadedListener listener) {
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                listener.onEventsLoaded(events);
                                return;
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("event", document.getId() + " => " + document.getData());

                                String eventID = document.getId();

                                eventManager.getEvent(eventID, new EventManager.OnEventFetchListener() {
                                    @Override
                                    public void onEventFetched(Event event) {
                                        if(event.getQRCode() != null){
                                            events.add(event);
                                        }
                                        count++;
                                        // Check if all documents have been processed
                                        if (count == task.getResult().size()) {
                                            Log.d("qrcode events","peepee");
                                            listener.onEventsLoaded(events); // Notify when done
                                        }
                                        else{
                                            Log.d("12345","event size: "+events.size());
                                            Log.d("12345","event size: "+task.getResult().size());
                                        }
                                    }

                                    @Override
                                    public void onEventFetchError(Exception e) {
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