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

import Classes.UserProfile;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.MyViewHolder> {

    private Context context;
    private List<UserProfile> localUserProfiles;
    private Boolean showDelete;

    public UserProfileAdapter(Context context, ArrayList<UserProfile> userProfiles, Boolean showDelete) {
        this.context=context;
        localUserProfiles = userProfiles;
        this.showDelete = showDelete;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.user_profile_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserProfile userProfile = localUserProfiles.get(position);
        holder.Delete.setVisibility(showDelete ? View.VISIBLE : View.GONE);
        holder.name.setText(userProfile.getName());
        if (userProfile.getPictureURL() != null) {
            holder.profilePicture.setImageTintList(null);
            Glide.with(context).load(userProfile.getPictureURL()).into(holder.profilePicture);
        } else {
            holder.profilePicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.profilePicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profilePicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.profile_user_text);
            profilePicture = itemView.findViewById(R.id.profile_user_picture);
            Delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public int getItemCount() {
        return localUserProfiles.size();
    }
}
