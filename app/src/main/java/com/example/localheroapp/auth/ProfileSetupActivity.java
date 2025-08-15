package com.example.localheroapp.auth;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.models.User;
import com.example.localheroapp.models.Ward;
import com.example.localheroapp.utils.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {
    private EditText addressEditText;
    private Spinner municipalitySpinner, wardSpinner;
    private Button useCurrentLocationButton, completeSetupButton;
    private ProgressBar progressBar;
    private TextView locationStatusText;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    
    private List<Ward> availableWards;
    private String selectedMunicipalityId;
    private String selectedWardId;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        // Initialize views
        initViews();
        setupSpinners();
        setupClickListeners();
        
        // Load municipalities and wards
        loadMunicipalities();
    }

    private void initViews() {
        addressEditText = findViewById(R.id.addressEditText);
        municipalitySpinner = findViewById(R.id.municipalitySpinner);
        wardSpinner = findViewById(R.id.wardSpinner);
        useCurrentLocationButton = findViewById(R.id.useCurrentLocationButton);
        completeSetupButton = findViewById(R.id.completeSetupButton);
        progressBar = findViewById(R.id.progressBar);
        locationStatusText = findViewById(R.id.locationStatusText);
    }

    private void setupSpinners() {
        // Municipality spinner
        List<String> municipalities = new ArrayList<>();
        municipalities.add("Select Municipality");
        ArrayAdapter<String> municipalityAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, municipalities);
        municipalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(municipalityAdapter);
        
        // Ward spinner
        List<String> wards = new ArrayList<>();
        wards.add("Select Ward");
        ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, wards);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wardSpinner.setAdapter(wardAdapter);
    }

    private void setupClickListeners() {
        useCurrentLocationButton.setOnClickListener(v -> getCurrentLocation());
        
        completeSetupButton.setOnClickListener(v -> completeProfileSetup());
        
        municipalitySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String selectedMunicipality = parent.getItemAtPosition(position).toString();
                    loadWardsForMunicipality(selectedMunicipality);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void loadMunicipalities() {
        db.collection("municipalities")
                .whereEqualTo("isActive", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> municipalities = new ArrayList<>();
                        municipalities.add("Select Municipality");
                        
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String municipalityName = document.getString("municipalityName");
                            municipalities.add(municipalityName);
                        }
                        
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                                android.R.layout.simple_spinner_item, municipalities);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        municipalitySpinner.setAdapter(adapter);
                    }
                });
    }

    private void loadWardsForMunicipality(String municipalityName) {
        db.collection("municipalities")
                .whereEqualTo("municipalityName", municipalityName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot municipalityDoc = task.getResult().getDocuments().get(0);
                        selectedMunicipalityId = municipalityDoc.getId();
                        
                        // Load wards for this municipality
                        loadWards(selectedMunicipalityId);
                    }
                });
    }

    private void loadWards(String municipalityId) {
        db.collection("wards")
                .whereEqualTo("municipalityId", municipalityId)
                .whereEqualTo("isActive", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        availableWards = new ArrayList<>();
                        List<String> wardNames = new ArrayList<>();
                        wardNames.add("Select Ward");
                        
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ward ward = document.toObject(Ward.class);
                            if (ward != null) {
                                ward.setWardId(document.getId());
                                availableWards.add(ward);
                                wardNames.add(ward.getWardName());
                            }
                        }
                        
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                                android.R.layout.simple_spinner_item, wardNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        wardSpinner.setAdapter(adapter);
                    }
                });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) 
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        
        setLoading(true);
        locationStatusText.setText("Getting your location...");
        
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        currentLocation = task.getResult();
                        getAddressFromLocation(currentLocation);
                    } else {
                        setLoading(false);
                        locationStatusText.setText("Could not get location. Please enter manually.");
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String fullAddress = "";
                
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    fullAddress += address.getAddressLine(i);
                    if (i < address.getMaxAddressLineIndex()) fullAddress += ", ";
                }
                
                addressEditText.setText(fullAddress);
                locationStatusText.setText("Location found: " + fullAddress);
                
                // Try to auto-select municipality and ward based on location
                autoSelectMunicipalityAndWard(location);
            }
        } catch (IOException e) {
            locationStatusText.setText("Could not get address from location");
        }
        
        setLoading(false);
    }

    private void autoSelectMunicipalityAndWard(Location location) {
        // This is a simplified implementation
        // In a real app, you would query the database for municipalities/wards near the coordinates
        locationStatusText.setText("Please manually select your municipality and ward");
    }

    private void completeProfileSetup() {
        String address = addressEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(address)) {
            addressEditText.setError("Address is required");
            addressEditText.requestFocus();
            return;
        }
        
        if (selectedMunicipalityId == null) {
            Toast.makeText(this, "Please select a municipality", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedWardId == null) {
            Toast.makeText(this, "Please select a ward", Toast.LENGTH_SHORT).show();
            return;
        }
        
        setLoading(true);
        
        // Update user profile with ward and municipality information
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUserProfile(currentUser.getUid());
        }
    }

    private void updateUserProfile(String userId) {
        // Find the selected ward
        final Ward selectedWard;
        {
            Ward tempWard = null;
            for (Ward ward : availableWards) {
                if (ward.getWardId().equals(selectedWardId)) {
                    tempWard = ward;
                    break;
                }
            }
            selectedWard = tempWard;
        }
        
        if (selectedWard == null) {
            Toast.makeText(this, "Error: Ward not found", Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;
        }
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("wardId", selectedWardId);
        updates.put("wardName", selectedWard.getWardName());
        updates.put("municipalityId", selectedMunicipalityId);
        updates.put("municipalityName", selectedWard.getMunicipalityName());
        updates.put("updatedAt", System.currentTimeMillis());
        
        db.collection("users").document(userId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        // Profile updated successfully
                        Toast.makeText(this, "Profile setup completed!", Toast.LENGTH_SHORT).show();
                        
                        // Add user to ward group chat
                        addUserToWardGroup(userId, selectedWard);
                        
                        // Go to main activity
                        Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addUserToWardGroup(String userId, Ward ward) {
        // This would add the user to the ward group chat
        // Implementation depends on your chat system
        // For now, we'll just log it
        System.out.println("User " + userId + " should be added to ward group: " + ward.getWardName());
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        completeSetupButton.setEnabled(!loading);
        useCurrentLocationButton.setEnabled(!loading);
        municipalitySpinner.setEnabled(!loading);
        wardSpinner.setEnabled(!loading);
        addressEditText.setEnabled(!loading);
    }
}
