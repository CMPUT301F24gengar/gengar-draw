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

import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * This is the UserProfileImageAdapter which is a custom adapter to display all the images of the user profiles.
 */
public class UserProfileImageAdapter extends RecyclerView.Adapter<UserProfileImageAdapter.MyViewHolder> {

    private Context context;
    private List<UserProfile> localUserProfiles;
    /**
     * Constructor for UserProfileImageAdapter done with context and an arraylist of userProfiles.
     * @param context the context in which the adapter is working.
     * @param userProfiles the list to be displayed in the adapter.
     */
    public UserProfileImageAdapter(Context context, ArrayList<UserProfile> userProfiles) {
        this.context=context;
        localUserProfiles = userProfiles;
    }
    /**
     * Called when the RecyclerView needs a new viewHolder.
     * Inflates the layout from profile_image_item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the new viewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.profile_image_item, parent, false);
        return new MyViewHolder(row);
    }
    /**
     * Binds the data from the userProfileImageAdapter at a specified position to the view holder.
     * If there is a picture present, it loads the picture into the profilePicture.
     * Otherwise, it puts in a default image.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserProfile userProfile = localUserProfiles.get(position);
        UserProfileManager userProfileManager = new UserProfileManager();

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();
                userProfileManager.deleteProfilePicture(localUserProfiles.get(deletedPosition).getDeviceID(), new UserProfileManager.OnDeleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context,"Deleted profile picture of "+ localUserProfiles.get(deletedPosition).getName(),Toast.LENGTH_SHORT).show();
                        localUserProfiles.remove(deletedPosition);
                        notifyItemRemoved(deletedPosition);
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("Error profile picture deletion: ", e.toString());
                    }
                });
            }
        });

        if (userProfile.getPictureURL() != null) {
            Glide.with(context).load(userProfile.getPictureURL()).into(holder.profilePicture);
        } else {
            holder.profilePicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.profilePicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
    }
    /**
     * Used to get references for views of a single item.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            Delete = itemView.findViewById(R.id.delete);
        }
    }
    /**
     * @return Gets the total count of localUserProfiles.
     */
    @Override
    public int getItemCount() {
        return localUserProfiles.size();
    }
}
