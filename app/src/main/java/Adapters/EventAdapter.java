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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Classes.Event;
import Classes.UserProfile;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private List<Event> localEvents;
    private Boolean showDelete;

    public EventAdapter(Context context, ArrayList<Event> events, Boolean showDelete) {
        this.context=context;
        localEvents = events;
        this.showDelete = showDelete;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Event event = localEvents.get(position);
        holder.Delete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
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
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        TextView eventStartDay;
        TextView eventStartMonth;
        TextView eventStartTime;
        ImageView eventPicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
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
}
