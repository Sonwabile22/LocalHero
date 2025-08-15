package com.example.localheroapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
<<<<<<< HEAD
=======
import android.widget.RadioButton;
import android.widget.RadioGroup;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localheroapp.R;
<<<<<<< HEAD
=======
import com.example.localheroapp.models.User;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private EditText fullNameEditText, phoneEditText;
    private Button registerButton;
    private ProgressBar progressBar;
    private TextView loginText;
<<<<<<< HEAD
=======
    private RadioGroup userRoleRadioGroup;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        try {
            // Initialize Firebase
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            
            // Test Firebase connection
            if (mAuth == null) {
                Toast.makeText(this, "Error: Firebase Auth not initialized", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Firebase initialized successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Firebase initialization error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        // Initialize views
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        loginText = findViewById(R.id.loginText);
<<<<<<< HEAD
=======
        userRoleRadioGroup = findViewById(R.id.userRoleRadioGroup);
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
    }

    private void setupClickListeners() {
        // Test button click without Firebase
        registerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Register button clicked!", Toast.LENGTH_SHORT).show();
            
            // Simple test - just show a message first
            Toast.makeText(this, "Button is working! Testing Firebase connection...", Toast.LENGTH_LONG).show();
            
            // Then try Firebase operation
            attemptRegistration();
        });
        
        loginText.setOnClickListener(v -> {
            Toast.makeText(this, "Login link clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void attemptRegistration() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.setError("Full name is required");
            fullNameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone number is required");
            phoneEditText.requestFocus();
            return;
        }

        // Show progress
        setLoading(true);
        
        // Add debug logging
        Toast.makeText(this, "Starting registration...", Toast.LENGTH_SHORT).show();

        // Create user account
        Toast.makeText(this, "Creating account...", Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            Toast.makeText(RegisterActivity.this, "Account created! Setting up profile...", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Create user profile
                                createUserProfile(user.getUid(), email, fullName, phone);
                            } else {
                                setLoading(false);
                                Toast.makeText(RegisterActivity.this, "Error: User not found after creation", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Registration failed
                            setLoading(false);
                            String errorMessage = "Registration failed";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createUserProfile(String userId, String email, String fullName, String phone) {
        Toast.makeText(this, "Creating user profile...", Toast.LENGTH_SHORT).show();
        
<<<<<<< HEAD
=======
        // Get selected user role
        String selectedRole = getSelectedUserRole();
        
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userId);
        user.put("email", email);
        user.put("fullName", fullName);
        user.put("phoneNumber", phone);
<<<<<<< HEAD
        user.put("userRole", "COMMUNITY_MEMBER");
=======
        user.put("userRole", selectedRole);
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
        user.put("isVerified", false);
        user.put("createdAt", System.currentTimeMillis());
        user.put("updatedAt", System.currentTimeMillis());

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        // Profile created successfully, go to profile setup
                        Toast.makeText(RegisterActivity.this, "Profile created! Going to setup...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, ProfileSetupActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Profile creation failed
                        String errorMessage = "Failed to create profile";
                        if (task.getException() != null) {
                            errorMessage = task.getException().getMessage();
                        }
                        Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

<<<<<<< HEAD
=======
    private String getSelectedUserRole() {
        int selectedId = userRoleRadioGroup.getCheckedRadioButtonId();
        
        if (selectedId == R.id.communityMemberRadio) {
            return "COMMUNITY_MEMBER";
        } else if (selectedId == R.id.councillorRadio) {
            return "COUNCILLOR";
        } else if (selectedId == R.id.municipalityWorkerRadio) {
            return "MUNICIPALITY_STAFF";
        }
        
        // Default to community member if nothing is selected
        return "COMMUNITY_MEMBER";
    }
    
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        registerButton.setEnabled(!loading);
        emailEditText.setEnabled(!loading);
        passwordEditText.setEnabled(!loading);
        confirmPasswordEditText.setEnabled(!loading);
        fullNameEditText.setEnabled(!loading);
        phoneEditText.setEnabled(!loading);
    }
}
<<<<<<< HEAD

=======
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
