package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gengardraw.R;

import java.util.ArrayList;
import java.util.List;

import Classes.Event;
import Classes.UserProfile;

public class EventImageAdapter extends RecyclerView.Adapter<EventImageAdapter.MyViewHolder> {

    private Context context;
    private List<Event> localEvents;

    public EventImageAdapter(Context context, ArrayList<Event> events) {
        this.context=context;
        localEvents = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.event_image_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = localEvents.get(position);
        if (event.getEventPictureURL() != null) {
            Glide.with(context).load(event.getEventPictureURL()).into(holder.eventPicture);
        } else {
            holder.eventPicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.eventPicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView eventPicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            eventPicture = itemView.findViewById(R.id.event_picture);
            Delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public int getItemCount() {
        return localEvents.size();
    }
}
