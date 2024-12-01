package Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
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

import Classes.CustomDialogClass;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * This is the UserProfileAdapter which is a custom adapter to display all the user profiles.
 */
public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.MyViewHolder> {

//    private static final String[] colors = {"#8078dc", "#1FCBFF", "#FF1E52", "#FF9416", "#FFF947", "#61D771"};
    private List<Integer> colors = new ArrayList<>();

    private Context context;
    private List<UserProfile> localUserProfiles;
    private Boolean showDelete;
    String currentDeviceID;


    /**
     * Constructor for UserProfileAdapter done with context and an arraylist of userProfiles.
     * @param context the context in which the adapter is working.
     * @param userProfiles the list to be displayed in the adapter.
     * @param showDelete the delete button
     */
    public UserProfileAdapter(Context context, ArrayList<UserProfile> userProfiles, Boolean showDelete) {
        this.context=context;
        localUserProfiles = userProfiles;
        this.showDelete = showDelete;

        colors.add(R.color.pfp1);
        colors.add(R.color.pfp2);
        colors.add(R.color.pfp3);
        colors.add(R.color.pfp4);
        colors.add(R.color.pfp5);
        colors.add(R.color.pfp6);
      
        currentDeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }
    /**
     * Called when the RecyclerView needs a new viewHolder.
     * Inflates the layout from user_profile_item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the new viewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.user_profile_item, parent, false);
        return new MyViewHolder(row);
    }
    /**
     * Binds the data from the user at a specified position to the view holder.
     * Sets the name to the user's name at the specified position
     * If there is a picture present, it loads the picture into the profilePicture.
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
                // Show the confirmation dialog
                CustomDialogClass dialog = new CustomDialogClass((Activity) context);
                dialog.setDialogListener(new CustomDialogClass.DialogListener() {
                    @Override
                    public void onProceed() {
                        // Delete the user profile upon confirmation
                        int deletedPosition = holder.getAdapterPosition();
                        UserProfileManager userProfileManager = new UserProfileManager();
                        userProfileManager.deleteUserProfile(localUserProfiles.get(deletedPosition).getDeviceID());
                        Toast.makeText(context, "Deleted " + localUserProfiles.get(deletedPosition).getName(), Toast.LENGTH_SHORT).show();
                        localUserProfiles.remove(deletedPosition);
                        notifyItemRemoved(deletedPosition);
                    }

                    @Override
                    public void onCancel() {
                        // Do nothing, just dismiss the dialog
                    }
                });
                dialog.show();
            }
        });

        UserProfile userProfile = localUserProfiles.get(position);
        holder.Delete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
        holder.name.setText(userProfile.getName());
        if (userProfile.getPictureURL() != null) {
            Glide.with(context).load(userProfile.getPictureURL()).into(holder.profilePicture);
            holder.profilePicture.setVisibility(View.VISIBLE);

            holder.Initials.setVisibility(View.GONE);
        } else {
            holder.profilePicture.setVisibility(View.GONE);

            int nameLength = userProfile.getName().length();
            holder.Initials.setText(userProfile.getInitials());
            holder.Initials.setBackgroundColor(context.getResources().getColor(colors.get(nameLength % 6)));
            holder.Initials.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Used to get references for views of a single item.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profilePicture;
        TextView Initials;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.profile_user_text);
            profilePicture = itemView.findViewById(R.id.profile_user_picture);
            Initials = itemView.findViewById(R.id.profile_user_initials);
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
