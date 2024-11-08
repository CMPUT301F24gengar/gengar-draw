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
import Adapters.UserProfileAdapter;
import Classes.Event;
import Classes.EventListsManager;
import Classes.EventManager;

public class admin_events extends Fragment{

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private EventManager eventManager;
    private ArrayList<Event> events;
    private ArrayList<Event> searchEvents;
    private RecyclerView.LayoutManager layoutManager;
    private EventAdapter customAdapter;
    private EventAdapter searchCustomAdapter;
    private ImageView searchButton;
    private EditText searchBarText;
    private String searchQuery;

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
        customAdapter = new EventAdapter(getContext(),events, true);
        searchCustomAdapter = new EventAdapter(getContext(),searchEvents, true);


        fetchEvents(new OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(ArrayList<Event> events) {
                //adding userProfiles to adapter
                recyclerView.setLayoutManager(layoutManager); //arranges recyclerView in linear form
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

    public interface OnEventsLoadedListener {
        void onEventsLoaded(ArrayList<Event> events);
    }

    //creates listener since firebase's get() is asynchronous in nature,
    //so it notifies when all profiles have been loaded.
    public void fetchEvents(OnEventsLoadedListener listener) {
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("event", document.getId() + " => " + document.getData());

                                String eventID = document.getId();

                                eventManager.getEvent(eventID, new EventManager.OnEventFetchListener() {
                                    @Override
                                    public void onEventFetched(Event event) {
                                        events.add(event);
                                        // Check if all documents have been processed
                                        if (events.size() == task.getResult().size()) {
                                            listener.onEventsLoaded(events); // Notify when done
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