package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import Classes.Event;
import Classes.EventManager;
import Classes.QRcode;
import Classes.QRcodeManager;
import Classes.UserProfile;
import Classes.UserProfileManager;

/**
 * This is the QRcodeAdapter which is a custom adapter to display all the qrcodes.
 */
public class QRcodeAdapter extends RecyclerView.Adapter<QRcodeAdapter.MyViewHolder> {

    private Context context;
    private List<Event> localEvents;
    private Boolean showDelete;

    /**
     * Constructor for QRcodeAdapter done with context and an arraylist of qrCodes.
     * @param context the context in which the adapter is working.
     * @param events the list to be displayed in the adapter.
     * @param showDelete the delete button
     */
    public QRcodeAdapter(Context context, ArrayList<Event> events, Boolean showDelete) {
        this.context=context;
        localEvents = events;
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
        View row = LayoutInflater.from(context).inflate(R.layout.qr_code_item, parent, false);
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
        String qrCode = localEvents.get(position).getQRCode();
        String eventTitle = localEvents.get(position).getEventTitle();
        QRcodeManager qrCodeManager = new QRcodeManager();
        EventManager eventManager = new EventManager();
        String eventId;
        String eventName;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int deletedPosition = holder.getAdapterPosition();
                qrCodeManager.deleteQRcode(qrCode);
                localEvents.get(deletedPosition).setQRCode(null);//set local copy's QR Code to null.
                //set qrcode to null in firebase
                db.collection("events").document(localEvents.get(deletedPosition).getEventID())
                                .update("qrcode",null);
                Toast.makeText(view.getContext(), "Deleted "+localEvents.get(deletedPosition).getEventTitle(),Toast.LENGTH_SHORT).show();
                localEvents.remove(deletedPosition);
                notifyItemRemoved(deletedPosition);
            }
        });

        holder.name.setText(eventTitle);
        generateQRCode(qrCode,holder.qrImage); //generating qr code and setting image to it.

    }
    /**
     * Used to get references for views of a single item.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView qrImage;
        ImageView Delete;

        public MyViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.event_name);
            qrImage = itemView.findViewById(R.id.qr_code);
            Delete = itemView.findViewById(R.id.delete);
        }
    }
    /**
     * @return Gets the total count of localQRCodes.
     */
    @Override
    public int getItemCount() {
        return localEvents.size();
    }

    private void generateQRCode(String qrCode, ImageView qrImage){
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try{
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrCode, BarcodeFormat.QR_CODE,400,400);
            qrImage.setImageBitmap(bitmap); //setting the image to the QRCode.
        }
        catch (WriterException e){
            Log.d("admin qrcode image error",e.toString());
        }
    }

}
