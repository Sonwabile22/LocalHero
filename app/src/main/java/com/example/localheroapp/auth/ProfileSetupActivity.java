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
                } else {
                    selectedMunicipalityId = null;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
        
        wardSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && availableWards != null && position <= availableWards.size()) {
                    Ward selectedWard = availableWards.get(position - 1);
                    selectedWardId = selectedWard.getWardId();
                } else {
                    selectedWardId = null;
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
        // Create or find the ward group chat
        String wardGroupId = "ward_group_" + ward.getWardId();
        
        // First, check if ward group exists
        db.collection("ward_groups").document(wardGroupId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            // Group exists, add user to it
                            addUserToExistingWardGroup(userId, wardGroupId, ward);
                        } else {
                            // Group doesn't exist, create it
                            createWardGroup(userId, wardGroupId, ward);
                        }
                    }
                });
    }
    
    private void createWardGroup(String userId, String wardGroupId, Ward ward) {
        Map<String, Object> wardGroup = new HashMap<>();
        wardGroup.put("groupId", wardGroupId);
        wardGroup.put("groupName", ward.getWardName() + " Community Group");
        wardGroup.put("wardId", ward.getWardId());
        wardGroup.put("wardName", ward.getWardName());
        wardGroup.put("municipalityId", selectedMunicipalityId);
        wardGroup.put("municipalityName", ward.getMunicipalityName());
        wardGroup.put("groupType", "WARD_GROUP");
        wardGroup.put("createdAt", System.currentTimeMillis());
        wardGroup.put("updatedAt", System.currentTimeMillis());
        
        // Initialize member lists
        List<String> members = new ArrayList<>();
        List<String> communityMembers = new ArrayList<>();
        List<String> councillors = new ArrayList<>();
        
        members.add(userId);
        communityMembers.add(userId); // Assuming new user is community member by default
        
        wardGroup.put("members", members);
        wardGroup.put("communityMembers", communityMembers);
        wardGroup.put("councillors", councillors);
        
        // Create the ward group
        db.collection("ward_groups").document(wardGroupId)
                .set(wardGroup)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Ward group created and user added: " + ward.getWardName());
                    // Create corresponding chat document
                    createWardGroupChat(wardGroupId, ward, members);
                    // Also find and add any councillors for this ward
                    findAndAddCouncillorsToGroup(wardGroupId, ward.getWardId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Failed to create ward group: " + e.getMessage());
                });
    }
    
    private void addUserToExistingWardGroup(String userId, String wardGroupId, Ward ward) {
        // Add user to existing ward group
        db.collection("ward_groups").document(wardGroupId)
                .update(
                    "members", com.google.firebase.firestore.FieldValue.arrayUnion(userId),
                    "communityMembers", com.google.firebase.firestore.FieldValue.arrayUnion(userId),
                    "updatedAt", System.currentTimeMillis()
                )
                .addOnSuccessListener(aVoid -> {
                    System.out.println("User added to existing ward group: " + ward.getWardName());
                    // Also add to chat participants
                    addUserToExistingWardGroupChat(userId, wardGroupId);
                })
                .addOnFailureListener(e -> {
                    System.err.println("Failed to add user to ward group: " + e.getMessage());
                });
    }
    
    private void addUserToExistingWardGroupChat(String userId, String wardGroupId) {
        // Add user to the corresponding chat document
        db.collection("chats").document(wardGroupId)
                .update(
                    "participantIds", com.google.firebase.firestore.FieldValue.arrayUnion(userId),
                    "updatedAt", System.currentTimeMillis()
                )
                .addOnSuccessListener(aVoid -> {
                    System.out.println("User added to existing ward group chat");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Failed to add user to ward group chat: " + e.getMessage());
                });
    }
    
    private void createWardGroupChat(String wardGroupId, Ward ward, List<String> members) {
        // Create a corresponding chat document for the ward group
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("chatId", wardGroupId);
        chatData.put("chatType", "WARD_GROUP");
        chatData.put("title", ward.getWardName() + " Community Group");
        chatData.put("description", "Community discussion for " + ward.getWardName());
        chatData.put("wardId", ward.getWardId());
        chatData.put("wardName", ward.getWardName());
        chatData.put("municipalityId", selectedMunicipalityId);
        chatData.put("municipalityName", ward.getMunicipalityName());
        chatData.put("participantIds", members);
        chatData.put("isActive", true);
        chatData.put("createdAt", System.currentTimeMillis());
        chatData.put("updatedAt", System.currentTimeMillis());
        
        // Create chat document
        db.collection("chats").document(wardGroupId)
                .set(chatData)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Ward group chat created: " + ward.getWardName());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Failed to create ward group chat: " + e.getMessage());
                });
    }
    
    private void findAndAddCouncillorsToGroup(String wardGroupId, String wardId) {
        // Find councillors for this ward and add them to the group
        db.collection("users")
                .whereEqualTo("wardId", wardId)
                .whereEqualTo("userRole", "COUNCILLOR")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> councillorIds = new ArrayList<>();
                    for (com.google.firebase.firestore.QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        councillorIds.add(doc.getId());
                    }
                    
                    if (!councillorIds.isEmpty()) {
                        // Add councillors to the ward group
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("members", com.google.firebase.firestore.FieldValue.arrayUnion(councillorIds.toArray()));
                        updates.put("councillors", com.google.firebase.firestore.FieldValue.arrayUnion(councillorIds.toArray()));
                        updates.put("updatedAt", System.currentTimeMillis());
                        
                        db.collection("ward_groups").document(wardGroupId)
                                .update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    System.out.println("Councillors added to ward group: " + councillorIds.size());
                                })
                                .addOnFailureListener(e -> {
                                    System.err.println("Failed to add councillors to group: " + e.getMessage());
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Failed to find councillors: " + e.getMessage());
                });
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
