package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gengardraw.R;

import java.util.ArrayList;
import java.util.List;

import Classes.Notification;
import Classes.NotificationManager;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<Notification> localNotifications;
    private String userID;
    private Boolean showDelete;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications, String userID, Boolean showDelete) {
        this.context=context;
        this.localNotifications = notifications;
        this.userID = userID;
        this.showDelete = showDelete;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();

                NotificationManager notificationManager = new NotificationManager();
                String notificationString = notificationManager.unparseNotification(localNotifications.get(deletedPosition));
                notificationManager.removeNotification(userID, notificationString, new NotificationManager.OnNotificationUpdateListener() {
                    @Override
                    public void onSuccess(String message) {
                        localNotifications.remove(deletedPosition);
                        notifyItemRemoved(deletedPosition);
                    }
                    @Override
                    public void onError(Exception e) {
                        // Handle error
                    }
                } );
            }
        });

        Notification notification = localNotifications.get(position);
        holder.message.setText(notification.getMessage());
        holder.day.setText(notification.getEventStartDateDay());
        holder.month.setText(notification.getEventStartDateMonth());
        holder.time.setText(notification.getEventStartDateTime());
        holder.title.setText(notification.getEventTitle());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView day;
        TextView month;
        TextView time;
        TextView title;
        ImageView delete;

        public MyViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.notification_message);
            delete = itemView.findViewById(R.id.delete);
            day = itemView.findViewById(R.id.view_event_day);
            month = itemView.findViewById(R.id.view_event_month);
            time = itemView.findViewById(R.id.view_event_time);
            title = itemView.findViewById(R.id.view_event_title);
        }
    }

    @Override
    public int getItemCount() {
        return localNotifications.size();
    }
}
