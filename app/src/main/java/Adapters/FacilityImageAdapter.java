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

import Classes.Facility;
import Classes.FacilityManager;

/**
 * This is the FacilityImageAdapter which is a custom adapter to display all the images of the facilities.
 */
public class FacilityImageAdapter extends RecyclerView.Adapter<FacilityImageAdapter.MyViewHolder> {

    private Context context;
    private List<Facility> localFacilities;
    /**
     * Constructor for FacilityImageAdapter done with context and an arraylist of facilities.
     * @param context the context in which the adapter is working.
     * @param facilities the list to be displayed in the adapter.
     */
    public FacilityImageAdapter(Context context, ArrayList<Facility> facilities) {
        this.context=context;
        localFacilities = facilities;
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
     * Binds the data from the facility at a specified position to the view holder.
     * If there is a picture present, it loads the picture into the profilePicture.
     * Otherwise, it puts in a default image.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Facility facility = localFacilities.get(position);
        FacilityManager facilityManager = new FacilityManager();
        //deleting
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();
                facilityManager.deleteFacilityPicture(localFacilities.get(deletedPosition).getDeviceID(), new FacilityManager.OnDeleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context,"Deleted facility picture of " + localFacilities.get(deletedPosition).getName(),Toast.LENGTH_SHORT).show();
                        localFacilities.remove(deletedPosition);
                        notifyItemRemoved(deletedPosition);
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("Facility picture deletion error",e.toString());
                    }
                });
            }
        });


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
     * @return Gets the total count of localFacilties.
     */
    @Override
    public int getItemCount() {
        return localFacilities.size();
    }
}
