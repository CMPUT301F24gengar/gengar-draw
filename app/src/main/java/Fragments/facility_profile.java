package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
//import android.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gengardraw.MainActivity;
import com.example.gengardraw.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.location.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Classes.Event;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;


/* SOURCES
https://www.geeksforgeeks.org/how-to-get-current-location-inside-android-fragment/
https://stackoverflow.com/questions/72038038/how-to-call-getcurrentlocation-method-of-fusedlocationproviderclient-in-kotlin
*/

/**
 * <h1>Facility Activity</h1>
 * <p>
 *     Handles interactions with the facility profile page fragment
 *     <ul>data: <li>device id</li> <li>fragment views</li> <li>facility image URI</li> <li>local Facility object</li> <li>latitude and longitude (device location)</li></ul>
 *     <ul>methods: <li>onCreateView</li> <li>setDetails</li> <li>onActivityResult</li> <li>closeFragment</li> <li>openUserProfileFragment</li> <li>getLastLocation</li> <li>onRequestPermissionsResult</li> <li>getLocationDetails</li> <li>getLocationDetails</li></ul>
 * </p>
 * @author Meghan, Rheanne
 * @see Fragment
 * @see Facility
 * @see FacilityManager
 */
public class facility_profile extends Fragment {

    String deviceID;

    private TextView userProfileButton;
    private ImageView facilityImage;
    private EditText nameEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private TextView removeFacilityPicture;
    private TextView addFacilityPicture;
    private TextView cancelButton;
    private Uri ImageURI=null;
    private FrameLayout saveButton;
    private TextView createUpdateBtn;
    Facility facilityProfile;
    private double latitude;
    private double longitude;
    private String location;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView addressTextView;
    private LocationCallback locationCallback;

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Constructed View
     * @throws Exception error checking facility
     * @see Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_facility_profile, container, false);
        userProfileButton = view.findViewById(R.id.profile_facility_user_btn);
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfileFragment();
            }
        });

        facilityImage = view.findViewById(R.id.profile_facility_picture);
        nameEditText = view.findViewById(R.id.profile_facility_name);
        locationEditText = view.findViewById(R.id.profile_facility_location);
        descriptionEditText = view.findViewById(R.id.profile_facility_description);
        removeFacilityPicture = view.findViewById(R.id.profile_facility_picture_remove);
        addFacilityPicture = view.findViewById(R.id.profile_facility_picture_add);
        saveButton = view.findViewById(R.id.profile_facility_create_frame);
        cancelButton = view.findViewById(R.id.profile_facility_cancel_btn);
        createUpdateBtn = view.findViewById(R.id.profile_facility_create_btn);

        deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }

        location = getLocationDetails(latitude, longitude);
        FacilityManager facilityManager = new FacilityManager();

        facilityManager.checkFacilityExists(deviceID, new FacilityManager.OnFacilityCheckListener() {
            @Override
            public void onFacilityExists(Facility facility) {
                facilityProfile = facility;
                createUpdateBtn.setText("UPDATE");

                setDetails();
            }

            @Override
            public void onFacilityNotExists() {
                createUpdateBtn.setText("CREATE");
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    getLastLocation();
                }

//                location = getLocationDetails(latitude, longitude);
//                locationEditText.setText(location);
            }

            @Override
            public void onError(Exception e) {
                Log.e("facility_profile", "Error checking facility", e);
            }
        });

        removeFacilityPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageURI=null;
                facilityImage.setImageDrawable(getResources().getDrawable(R.drawable.facility));
                facilityImage.setImageTintList(getResources().getColorStateList(R.color.grey));
            }
        });

        addFacilityPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGalleryIntent,1001); //request code is 1000 for this activity.;

            }
        });

        createUpdateBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            //String location = locationEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Please enable location services and restart app", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
//            } else if (location.isEmpty()) {
//                Toast.makeText(getContext(), "Please enter a location", Toast.LENGTH_SHORT).show();
//                return;
            } else if (description.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a description", Toast.LENGTH_SHORT).show();
                return;
            } else if (ImageURI == null) {
                Toast.makeText(getContext(), "Please select a picture", Toast.LENGTH_SHORT).show();
                return;
            }

            if (facilityProfile != null) {
                Log.e("facility_profile", "exists");
                facilityProfile.setName(name);
                facilityProfile.setLocation(location);
                facilityProfile.setDescription(description);
                facilityManager.updateFacility(facilityProfile,deviceID);
                Toast.makeText(getContext(), "Facility updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                facilityProfile = new Facility(name, latitude, longitude, location, description, null, new ArrayList<>(), deviceID);
                // update the users facilityURL
                UserProfileManager userProfileManager = new UserProfileManager();
                userProfileManager.getUserProfile(deviceID , new UserProfileManager.OnUserProfileFetchListener() {
                    @Override
                    public void onUserProfileFetched(UserProfile userProfile) {
                        userProfile.setFacilityURL(deviceID);
                        userProfile.setOrganizer(true);
                        userProfileManager.updateUserProfile(userProfile, deviceID);
                    }
                    @Override
                    public void onUserProfileFetchError(Exception e) {
                        Log.e("facility_profile", "Error fetching user profile", e);
                    }
                });

                facilityManager.addFacility(facilityProfile, deviceID);
                Toast.makeText(getContext(), "Facility created successfully", Toast.LENGTH_SHORT).show();
                createUpdateBtn.setText("UPDATE");
            }

            if(ImageURI==null){
                facilityManager.deleteFacilityPicture(deviceID, new FacilityManager.OnDeleteListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            } else {
                facilityManager.uploadFacilityPicture(ImageURI, deviceID, new FacilityManager.OnUploadPictureListener() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
            }
        });

        cancelButton.setOnClickListener(v -> {
            closeFragment();
        });

        return view;
    }

    /**
     * sets the activity views to reflect the local facility
     */
    public void setDetails() {
        nameEditText.setText(facilityProfile.getName());
        locationEditText.setText(facilityProfile.getLocation());
        descriptionEditText.setText(facilityProfile.getDescription());
        if(facilityProfile.getPictureURL()!=null){
            Glide.with(this).load(facilityProfile.getPictureURL()).into(facilityImage);
            facilityImage.setImageTintList(null);
        }
    }

    /**
     * sets image if activity result indicates success
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     * @throws Exception null data
     * @see Fragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1001){
            if (resultCode== Activity.RESULT_OK){
                assert data != null;
                ImageURI = data.getData();
                facilityImage.setImageURI(ImageURI);
                facilityImage.setImageTintList(null);
            }
        }
    }

    /**
     * Returns user back to the home screen
     * @throws Exception activity not instance of MainActivity
     * @see MainActivity
     */
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

    /**
     * Takes user to User Profile Page
     * @throws Exception activity not instance of MainActivity
     * @see user_profile
     */
    private void openUserProfileFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new user_profile()).commit();
        } else {
            // Handle error
        }
    }

    /**
     * retrieves user's device location
     * @throws null location
     */
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location2 = task.getResult();
                                latitude = location2.getLatitude();
                                longitude = location2.getLongitude();
                                location = getLocationDetails(latitude, longitude);
                                locationEditText.setText(location);

                            } else {
                                // Handle the case where Location is null
                            }
                        }
                    });
        } else {
            // Handle the case where permission is not granted
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     *
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     * @throws Exception permission denied
     * @see Fragment
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED) {
                Log.d("facilityProfile", "onRequestPermissionsResult ");
                getLastLocation();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    /**
     * Intreprets latitude and longitude into a String representation of location
     * @param latitude  double, device's physical location latitude
     * @param longitude double, device's physical location longitude
     * @return String representation of device location
     * @throws  Exception null/empty address
     * @throws Exception IOException
     * @see Geocoder
     */
    private String getLocationDetails(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String country = address.getCountryName();
                String city = address.getLocality();
                String addressLine = address.getAddressLine(0);
                return addressLine;
            } else {
                // Handle exception
            }
        } catch (IOException e) {
            // Handle exception
            //throw new RuntimeException(e);
        }
        return "";
    }
}