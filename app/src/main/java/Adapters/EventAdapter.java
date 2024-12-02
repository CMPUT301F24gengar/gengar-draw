package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gengardraw.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Locale;

import Classes.CustomDialogClass;
import Classes.Event;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.EventManager;
import Classes.UserProfile;

/**
 * This is the EventAdapter which is a custom adapter to display all the events along with their details.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private Context context;
    private List<Event> localEvents;
    private Boolean showDelete;
    private Boolean showUpdate;
    private OnEventClickListener listener;
    private boolean buttonDebounce = false;


    /**
     * Constructor for event adapter
     * @param context the context in which the adapter is working.
     * @param events the list to be displayed in the adapter.
     * @param showDelete the delete button
     * @param listener the listener for the event details button
     * @param showUpdate the update button
     */
    public EventAdapter(Context context, ArrayList<Event> events, Boolean showDelete, Boolean showUpdate, OnEventClickListener listener) {
        this.context=context;
        localEvents = events;
        this.showDelete = showDelete;
        this.showUpdate = showUpdate;
        this.listener = listener;
    }

    /**
     * Called when the RecyclerView needs a new viewHolder.
     * Inflates the layout from event_item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the new viewHolder
     */

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new MyViewHolder(row);
    }

    /**
     * Binds the data from the event at a specified position to the view holder.
     * Sets the details to the event's details at the specified position
     * If there is a picture present, it loads the picture.
     * Otherwise, it puts in a default image.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClass dialog = new CustomDialogClass((Activity) context);
                dialog.setDialogListener(new CustomDialogClass.DialogListener() {
                    @Override
                    public void onProceed() {
                        int deletedPosition = holder.getAdapterPosition();
                        EventManager eventManager = new EventManager();
                        eventManager.deleteEvent(localEvents.get(deletedPosition).getEventID());
                        Toast.makeText(view.getContext(), "Deleted " + localEvents.get(deletedPosition).getEventTitle(),Toast.LENGTH_SHORT).show();
                        localEvents.remove(deletedPosition);
                        notifyItemRemoved(deletedPosition);
                    }

                    @Override
                    public void onCancel() {
                        //do nothing
                    }
                });
                dialog.show();
            }
        });

        Event event = localEvents.get(position);
        holder.Delete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
        holder.updateEvent.setVisibility(showUpdate ? View.VISIBLE : View.GONE);
        String organizerID = event.getOrganizerID();
        FacilityManager facilityManager = new FacilityManager();
        facilityManager.getFacility(organizerID, new FacilityManager.OnFacilityFetchListener() {
            @Override
            public void onFacilityFetched(Facility facility) {

                if (facility == null){
                    holder.facilityNameTextView.setText("Facility not found.");
                    holder.facilityPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
                } else{
                    //set image
                    if (facility.getPictureURL() == null){
                        holder.facilityPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
                    }
                    else{
                        holder.facilityPicture.setImageTintList(null);
                        Glide.with(context).load(facility.getPictureURL()).into(holder.facilityPicture);
                    }

                    //set text to facility name
                    holder.facilityNameTextView.setText(facility.getName());
                }
            }

            @Override
            public void onFacilityFetchError(Exception e) {
                Log.d("Facility fetch error eventadapter: ", e.toString());
            }
        });

        holder.eventTitle.setText(event.getEventTitle());
        holder.eventStartDay.setText(String.valueOf(event.getEventStartDate().getDate()));
        holder.eventStartMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(event.getEventStartDate()).toUpperCase());
        holder.eventStartTime.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(event.getEventStartDate()).toUpperCase());

        if (event.getEventPictureURL() != null) {
            Glide.with(context).load(event.getEventPictureURL()).into(holder.eventPicture);
            holder.eventPicture.setVisibility(View.VISIBLE);
        } else {
            holder.eventPicture.setVisibility(View.GONE);
        }

        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonDebounce) {
                    return;
                }
                buttonDebounce = true;

                listener.onEventDetailsClick(event.getEventID());
            }
        });

    /**
     * Used to get references for views of a single item.
     */

        holder.updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonDebounce) {
                    return;
                }
                buttonDebounce = true;

                listener.onEventUpdateClick(event.getEventID());
            }
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView facilityNameTextView;
        ImageView facilityPicture;
        TextView eventTitle;
        TextView eventStartDay;
        TextView eventStartMonth;
        TextView eventStartTime;
        ImageView eventPicture;
        FrameLayout updateEvent;
        ImageView viewDetails;
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
            updateEvent = itemView.findViewById(R.id.update_event_btn);
            viewDetails = itemView.findViewById(R.id.view_details_btn);
            Delete = itemView.findViewById(R.id.delete);
        }
    }

    /**
     *
     * @return number of events
     */
    @Override
    public int getItemCount() {
        return localEvents.size();
    }
    public interface OnEventClickListener {
        void onEventDetailsClick(String eventID);
        void onEventUpdateClick(String eventID);
    }
}