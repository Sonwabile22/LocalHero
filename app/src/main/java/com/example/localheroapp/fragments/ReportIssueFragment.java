package com.example.localheroapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.models.Issue;
import com.example.localheroapp.models.User;
import com.example.localheroapp.utils.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportIssueFragment extends Fragment implements OnMapReadyCallback {
    private EditText titleEditText, descriptionEditText, addressEditText;
    private AutoCompleteTextView categorySpinner, prioritySpinner;
    private MaterialButton useLocationButton, addPhotoButton, submitButton;
    private ImageView photoPreview;
    private TextView locationStatusText;
    
    private MainActivity mainActivity;
    private User currentUser;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private GoogleMap mMap;
    
    private Location currentLocation;
    private Uri selectedImageUri;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_issue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            currentUser = mainActivity.getCurrentUser();
        }
        
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        
        initViews(view);
        setupSpinners();
        setupClickListeners();
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        prioritySpinner = view.findViewById(R.id.prioritySpinner);
        useLocationButton = view.findViewById(R.id.useLocationButton);
        addPhotoButton = view.findViewById(R.id.addPhotoButton);
        submitButton = view.findViewById(R.id.submitButton);
        photoPreview = view.findViewById(R.id.photoPreview);
        locationStatusText = view.findViewById(R.id.locationStatusText);
    }

    private void setupSpinners() {
        // Category spinner
        List<String> categories = new ArrayList<>();
        for (Issue.IssueCategory category : Issue.IssueCategory.values()) {
            categories.add(category.name().replace("_", " "));
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_dropdown_item_1line, categories);
        categorySpinner.setAdapter(categoryAdapter);
        
        // Priority spinner
        List<String> priorities = new ArrayList<>();
        for (Issue.IssuePriority priority : Issue.IssuePriority.values()) {
            priorities.add(priority.name());
        }
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_dropdown_item_1line, priorities);
        prioritySpinner.setAdapter(priorityAdapter);
    }

    private void setupClickListeners() {
        useLocationButton.setOnClickListener(v -> getCurrentLocation());
        addPhotoButton.setOnClickListener(v -> openCamera());
        submitButton.setOnClickListener(v -> submitIssue());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        
        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        
        // Set default location (you can set this to a default city)
        LatLng defaultLocation = new LatLng(-26.2041, 28.0473); // Johannesburg
        mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
        
        // Add click listener to map for manual location selection
        mMap.setOnMapClickListener(latLng -> {
            currentLocation = new Location("manual");
            currentLocation.setLatitude(latLng.latitude);
            currentLocation.setLongitude(latLng.longitude);
            
            // Clear previous markers
            mMap.clear();
            
            // Add marker at selected location
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
                    .snippet("Tap to confirm this location"));
            
            locationStatusText.setText("Location selected on map");
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), 
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        
        locationStatusText.setText("Getting your location...");
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        currentLocation = location;
                        locationStatusText.setText("Location captured successfully");
                        
                        // Update map with current location
                        if (mMap != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Your Location")
                                    .snippet("GPS location captured"));
                            mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }
                        
                        // TODO: Reverse geocode to get address
                    } else {
                        locationStatusText.setText("Could not get location. Please enter manually.");
                    }
                });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                // Handle captured image
                // In a real app, you'd save the image and get its URI
                Toast.makeText(getContext(), "Photo captured successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitIssue() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        
        if (title.isEmpty()) {
            titleEditText.setError("Title is required");
            titleEditText.requestFocus();
            return;
        }
        
        if (description.isEmpty()) {
            descriptionEditText.setError("Description is required");
            descriptionEditText.requestFocus();
            return;
        }
        
        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            addressEditText.requestFocus();
            return;
        }
        
        if (currentLocation == null) {
            Toast.makeText(getContext(), "Please capture your location", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create issue object
        String issueId = UUID.randomUUID().toString();
        
        // Get selected category and priority from AutoCompleteTextView
        String categoryText = categorySpinner.getText().toString();
        String priorityText = prioritySpinner.getText().toString();
        
        Issue.IssueCategory category = null;
        Issue.IssuePriority priority = null;
        
        // Find category by display name
        for (Issue.IssueCategory cat : Issue.IssueCategory.values()) {
            if (cat.name().replace("_", " ").equals(categoryText)) {
                category = cat;
                break;
            }
        }
        
        // Find priority by display name
        for (Issue.IssuePriority pri : Issue.IssuePriority.values()) {
            if (pri.name().equals(priorityText)) {
                priority = pri;
                break;
            }
        }
        
        if (category == null || priority == null) {
            Toast.makeText(getContext(), "Please select both category and priority", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Issue issue = new Issue(issueId, title, description, currentUser.getUserId(), 
                               currentUser.getFullName(), currentUser.getWardId(), currentUser.getWardName(),
                               currentUser.getMunicipalityId(), currentUser.getMunicipalityName(),
                               currentLocation.getLatitude(), currentLocation.getLongitude(), address,
                               category, priority);
        
        // Save issue to Firestore
        saveIssue(issue);
    }

    private void saveIssue(Issue issue) {
        db.collection("issues").document(issue.getIssueId())
                .set(issue)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Issue reported successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate back to home
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).onBackPressed();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to report issue: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

