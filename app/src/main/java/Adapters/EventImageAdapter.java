package Adapters;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import Classes.Event;
import Classes.EventManager;
import Classes.UserProfile;

/**
 * This is the EventImageAdapter which is a custom adapter to display all the images of the events.
 */

public class EventImageAdapter extends RecyclerView.Adapter<EventImageAdapter.MyViewHolder> {

    private Context context;
    private List<Event> localEvents;

    /**
     * Constructor for EventImageAdapter done with context and an arraylist of events.
     * @param context the context in which the adapter is working.
     * @param events the list to be displayed in the adapter.
     */
    public EventImageAdapter(Context context, ArrayList<Event> events) {
        this.context=context;
        localEvents = events;
    }

    /**
     * Called when the RecyclerView needs a new viewHolder.
     * Inflates the layout from event_image_item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the new viewholder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.event_image_item, parent, false);
        return new MyViewHolder(row);
    }

    /**
     * Binds the data from the event at a specified position to the view holder.
     * If there is a picture present, it loads the picture into the eventPicture.
     * Otherwise, it puts in a default image.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = localEvents.get(position);
        EventManager eventManager = new EventManager();

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();
                eventManager.deleteEventPicture(localEvents.get(deletedPosition).getEventID(), new EventManager.OnDeleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context,"Deleted event picture of " + localEvents.get(deletedPosition).getEventTitle(),Toast.LENGTH_SHORT).show();
                        localEvents.remove(deletedPosition);
                        notifyItemRemoved(deletedPosition);
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("Event picture deletion error: ",e.toString());
                    }
                });

            }
        });

        if (event.getEventPictureURL() != null) {
            Glide.with(context).load(event.getEventPictureURL()).into(holder.eventPicture);
            holder.eventPicture.setVisibility(View.VISIBLE);
        } else {
            holder.eventPicture.setVisibility(View.GONE);
        }
    }

    /**
     * Used to get references for views of a single item.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView eventPicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            eventPicture = itemView.findViewById(R.id.event_picture);
            Delete = itemView.findViewById(R.id.delete);
        }
    }

    /**
     * @return Gets the total count of localEvents.
     */
    @Override
    public int getItemCount() {
        return localEvents.size();
    }
}
