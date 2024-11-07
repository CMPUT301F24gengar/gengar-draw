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

public class UserProfileImageAdapter extends RecyclerView.Adapter<UserProfileImageAdapter.MyViewHolder> {

    private Context context;
    private List<UserProfile> localUserProfiles;

    public UserProfileImageAdapter(Context context, ArrayList<UserProfile> userProfiles) {
        this.context=context;
        localUserProfiles = userProfiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.profile_image_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserProfile userProfile = localUserProfiles.get(position);
        if (userProfile.getPictureURL() != null) {
            Glide.with(context).load(userProfile.getPictureURL()).into(holder.profilePicture);
        } else {
            holder.profilePicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.profilePicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            Delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public int getItemCount() {
        return localUserProfiles.size();
    }
}
