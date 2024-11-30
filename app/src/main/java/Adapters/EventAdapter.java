package Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gengardraw.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Classes.Event;
import Classes.FacilityManager;
import Classes.EventManager;
import Classes.UserProfile;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private List<Event> localEvents;
    private Boolean showDelete;
    private OnEventClickListener listener;
    private String facilityName;
    private String facilityPictureURL;

    public EventAdapter(Context context, ArrayList<Event> events, Boolean showDelete, OnEventClickListener listener) {
        this.context=context;
        localEvents = events;
        this.showDelete = showDelete;
        this.listener = listener;
        this.facilityName = "";
        this.facilityPictureURL = "";
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
        notifyDataSetChanged();
    }

    public void setFacilityPicture(String facilityPictureURL) {
        this.facilityPictureURL = facilityPictureURL;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();
                EventManager eventManager = new EventManager();
                eventManager.deleteEvent(localEvents.get(deletedPosition).getEventID());
                Toast.makeText(view.getContext(), "Deleted " + localEvents.get(deletedPosition).getEventTitle(),Toast.LENGTH_SHORT).show();
                localEvents.remove(deletedPosition);
                notifyItemRemoved(deletedPosition);
            }
        });

        Event event = localEvents.get(position);
        String eventID = localEvents.get(position).getEventID();
        holder.Delete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
        holder.facilityNameTextView.setText(facilityName);
        Log.d("EventAdapter", "onBindViewHolder FacilityPictureURL: " + facilityPictureURL);

        if (!facilityPictureURL.isEmpty()) {
            holder.facilityPicture.setImageTintList(null);
            Glide.with(context).load(facilityPictureURL).into(holder.facilityPicture);
        } else {
            holder.facilityPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.facilityPicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
        holder.eventTitle.setText(event.getEventTitle());
        holder.eventStartDay.setText(String.valueOf(event.getEventStartDate().getDate()));
        holder.eventStartMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(event.getEventStartDate()).toUpperCase());
        holder.eventStartTime.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(event.getEventStartDate()).toUpperCase());
        if (event.getEventPictureURL() != null) {
            holder.eventPicture.setImageTintList(null);
            Glide.with(context).load(event.getEventPictureURL()).into(holder.eventPicture);
        } else {
            holder.eventPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.eventPicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEventClick(localEvents.get(position).getEventID());
                Log.d("EventAdapter", "setOnClickListener position: " + position + " event title: " + event.getEventTitle());
            }
        });

        // Joined Events don't have facility names passed to the adapter
        if (facilityName == "") {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("facilities")
//                    .whereEqualTo("EventID", eventID)
                    .whereArrayContains("events", eventID)
                    .get()

                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            String curr_facilityName = document.getString("name");
                            String curr_facilityPictureURL = document.getString("pictureURL");

                            Log.d("EventAdapter", "Found facility facilityName: " + curr_facilityName + " eventID: " + eventID);

                            holder.facilityNameTextView.setText(curr_facilityName);

                            if (curr_facilityPictureURL != null) {
                                holder.facilityPicture.setImageTintList(null);
                                Glide.with(context).load(curr_facilityPictureURL).into(holder.facilityPicture);
                            } else {
                                holder.facilityPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
                                holder.facilityPicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
                            }
                        } else {
                            Log.e("EventAdapter", "No facility found for EventID: " + eventID);
                        }
                    });
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView facilityNameTextView;
        ImageView facilityPicture;
        TextView eventTitle;
        TextView eventStartDay;
        TextView eventStartMonth;
        TextView eventStartTime;
        ImageView eventPicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            facilityNameTextView = itemView.findViewById(R.id.view_event_facility_name);
            facilityPicture = itemView.findViewById(R.id.view_event_facility_picture);
            eventTitle = itemView.findViewById(R.id.view_event_title);
            eventStartDay = itemView.findViewById(R.id.view_event_day);
            eventStartMonth = itemView.findViewById(R.id.view_event_month);
            eventStartTime = itemView.findViewById(R.id.view_event_time);
            eventPicture = itemView.findViewById(R.id.view_event_picture);
            Delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public int getItemCount() {
        return localEvents.size();
    }

    public interface OnEventClickListener {
        void onEventClick(String eventID);
    }
}
