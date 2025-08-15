package com.example.localheroapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.localheroapp.R;
import com.example.localheroapp.MainActivity;
import com.example.localheroapp.auth.LoginActivity;
import com.example.localheroapp.auth.ProfileSetupActivity;
import com.example.localheroapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    private TextView fullNameText, emailText, phoneText, wardText, municipalityText, roleText;
    private MaterialButton editProfileButton, profileSetupButton, logoutButton;
    
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get current user from MainActivity
        if (getActivity() instanceof MainActivity) {
            currentUser = ((MainActivity) getActivity()).getCurrentUser();
        }
        
        initViews(view);
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews(View view) {
        fullNameText = view.findViewById(R.id.fullNameText);
        emailText = view.findViewById(R.id.emailText);
        phoneText = view.findViewById(R.id.phoneText);
        wardText = view.findViewById(R.id.wardText);
        municipalityText = view.findViewById(R.id.municipalityText);
        roleText = view.findViewById(R.id.roleText);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        profileSetupButton = view.findViewById(R.id.profileSetupButton);
        logoutButton = view.findViewById(R.id.logoutButton);
    }

    private void setupClickListeners() {
        editProfileButton.setOnClickListener(v -> editProfile());
        profileSetupButton.setOnClickListener(v -> openProfileSetup());
        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            fullNameText.setText(currentUser.getFullName());
            emailText.setText(currentUser.getEmail());
            phoneText.setText(currentUser.getPhoneNumber());
            wardText.setText(currentUser.getWardName() != null ? currentUser.getWardName() : "Not set");
            municipalityText.setText(currentUser.getMunicipalityName() != null ? currentUser.getMunicipalityName() : "Not set");
            
            String roleDisplay = getRoleDisplayName(currentUser.getUserRole());
            roleText.setText(roleDisplay);
        }
    }

    private String getRoleDisplayName(User.UserRole role) {
        if (role == null) return "Unknown";
        
        switch (role) {
            case COMMUNITY_MEMBER:
                return "Community Member";
            case COUNCILLOR:
                return "Councillor";
            case MUNICIPALITY_STAFF:
                return "Municipality Staff";
            case ADMIN:
                return "Administrator";
            default:
                return "Unknown";
        }
    }

    private void editProfile() {
        // Show a simple edit dialog for now
        // In a full app, this would open a dedicated edit activity
        Toast.makeText(getContext(), "Profile editing feature coming soon!", Toast.LENGTH_SHORT).show();
        
        // For now, let's show what fields can be edited
        String message = "You can edit:\n" +
                        "• Full Name\n" +
                        "• Phone Number\n" +
                        "• Profile Picture\n" +
                        "• Address (requires re-ward assignment)";
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Profile")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
    
    private void openProfileSetup() {
        Intent intent = new Intent(getActivity(), ProfileSetupActivity.class);
        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
