package Adapters;

import android.content.Context;
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

import Classes.Facility;
/**
 * This is the FacilityAdapter which is a custom adapter to display all the facilities along with their profile picture.
 */

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.MyViewHolder> {

    private Context context;
    private List<Facility> localFacilities;
    /**
     * Constructor for FacilityAdapter done with context and an arraylist of facilties.
     * @param context the context in which the adapter is working.
     * @param facilities the list to be displayed in the adapter.
     */
    public FacilityAdapter(Context context, ArrayList<Facility> facilities) {
        this.context=context;
        localFacilities = facilities;
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
        View row = LayoutInflater.from(context).inflate(R.layout.user_profile_item, parent, false); //change to facility_item
        return new MyViewHolder(row);
    }
    /**
     * Binds the data from the facility at a specified position to the view holder.
     * Sets the name to the facility's name at the specified position
     * If there is a picture present, it loads the picture into the profilePicture.
     * Otherwise, it puts in a default image.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Facility facility = localFacilities.get(position);
        holder.name.setText(facility.getName());
        if (facility.getPictureURL() != null) {
            Glide.with(context).load(facility.getPictureURL()).into(holder.profilePicture);
        } else {
            holder.profilePicture.setImageDrawable(context.getResources().getDrawable(R.drawable.user));
            holder.profilePicture.setImageTintList(context.getResources().getColorStateList(R.color.green));
        }
    }
    /**
     * Used to get references for views of a single item.
     */
    // can reuse user_profile_item.xml for facility
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
    /**
     * @return Gets the total count of localFacilities.
     */
    @Override
    public int getItemCount() {
        return localFacilities.size();
    }
}
