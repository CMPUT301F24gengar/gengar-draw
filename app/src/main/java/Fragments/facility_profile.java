package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationRequest;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Classes.Event;
import Classes.Facility;
import Classes.FacilityManager;
import Classes.UserProfile;
import Classes.UserProfileManager;


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

        // temp for testing
//        latitude = 53.5232;
//        longitude = -113.5263;
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
                // temp for testing
//                latitude = 53.5232;
//                longitude = -113.5263;
                location = getLocationDetails(latitude, longitude);
                locationEditText.setText(location);
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
                facilityProfile = new Facility(name, latitude, longitude, location, description, pictureURL, new ArrayList<>(), deviceID);
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
                        facilityManager.updateFacilityPictureInFirestore(deviceID, ImageURI.toString(), new FacilityManager.OnUpdateListener() {
                            @Override
                            public void onSuccess() {

                            }
                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("facility_profile", "Error uploading facility picture", e);
                    }
                });
            }
        });

        cancelButton.setOnClickListener(v -> {
            closeFragment();
        });

        return view;
    }

    public void setDetails() {
        nameEditText.setText(facilityProfile.getName());
        locationEditText.setText(facilityProfile.getLocation());
        descriptionEditText.setText(facilityProfile.getDescription());
        if(facilityProfile.getPictureURL()!=null){
            Glide.with(this).load(facilityProfile.getPictureURL()).into(facilityImage);
            facilityImage.setImageTintList(null);
        }
    }

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

    // Go back to the home screen
    private void closeFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.showHomeFragment();
        } else {
            // Handle error
        }
    }

    private void openUserProfileFragment() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new user_profile()).commit();
        } else {
            // Handle error
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                // temp for testing
//                                latitude = 53.5232;
//                                longitude = -113.5263;

                                Log.d("facility_profile", "getLastLocation " + location + " " + latitude + " " + longitude);
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

    private void getLocation(LocationRequest locationRequest) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

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