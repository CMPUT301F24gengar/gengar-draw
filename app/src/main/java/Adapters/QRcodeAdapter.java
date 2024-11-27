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

import Classes.QRcode;
import Classes.QRcodeManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * This is the QRcodeAdapter which is a custom adapter to display all the qrcodes.
 */
public class QRcodeAdapter extends RecyclerView.Adapter<QRcodeAdapter.MyViewHolder> {

    private Context context;
    private List<String> localQRCodes;
    private Boolean showDelete;

    /**
     * Constructor for QRcodeAdapter done with context and an arraylist of qrCodes.
     * @param context the context in which the adapter is working.
     * @param qrCodes the list to be displayed in the adapter.
     * @param showDelete the delete button
     */
    public QRcodeAdapter(Context context, ArrayList<String> qrCodes, Boolean showDelete) {
        this.context=context;
        localQRCodes = qrCodes;
        this.showDelete = showDelete;
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
     * Binds the data from the qrCode at a specified position to the view holder.
     * Sets the name to the qrCode's text at the specified position
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String qrCode = localQRCodes.get(position);
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();
                QRcodeManager qrCodeManager = new QRcodeManager();
                qrCodeManager.deleteQRcode(localQRCodes.get(deletedPosition));
                Toast.makeText(view.getContext(), "Deleted " + localQRCodes.get(deletedPosition),Toast.LENGTH_SHORT).show();
                localQRCodes.remove(deletedPosition);
                notifyItemRemoved(deletedPosition);
            }
        });

        if (qrCode == null) {
            Log.d("qrcode","null");
        } else {
            Log.d("qrcode adapter",qrCode);
        }
        holder.name.setText(qrCode);
    }
    /**
     * Used to get references for views of a single item.
     */
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
     * @return Gets the total count of localQRCodes.
     */
    @Override
    public int getItemCount() {
        return localQRCodes.size();
    }
}
