package Classes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gengardraw.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<UserProfile> localUserProfiles;
    private Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView profileItemText;
        private final ImageView profileItemImage;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            profileItemText = (TextView) view.findViewById(R.id.profile_user_text);
            profileItemImage = (ImageView) view.findViewById(R.id.profile_user_picture); //is this needed?

        }

        public TextView getProfileItemText() {
            return profileItemText;
        }

        public ImageView getProfileItemImage() {
            return profileItemImage;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param userProfiles ArrayList<UserProfile> userProfiles containing the data to populate views to be used
     * by RecyclerView
     */
    public CustomAdapter(Context context,ArrayList<UserProfile> userProfiles) {
        this.context=context;
        localUserProfiles = userProfiles;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.admin_user_profile_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your UserProfile at this position and replace the
        // contents of the view with that element
        viewHolder.getProfileItemText().setText(localUserProfiles.get(position).getName());
        String profileUriString = localUserProfiles.get(position).getPictureURL();
        Log.d("picture uri","pictureURI: "+ profileUriString);
        if (profileUriString!=null){
//            Glide.with(context).load(profileUriString).centerCrop().thumbnail(0.1f).placeholder(R.drawable.user)
//                    .into(viewHolder.getProfileItemImage());
            //can use .placeholder(R.drawable.loading_icon) to show a loading icon while glide is still retrieving from firebase.

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localUserProfiles.size();
    }
}
