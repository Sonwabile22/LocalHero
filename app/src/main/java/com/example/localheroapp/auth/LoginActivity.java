package com.example.localheroapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

<<<<<<< HEAD
=======
// Import the LoadingStateManager
import com.example.localheroapp.utils.LoadingStateManager;

>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ProgressBar progressBar;
    private TextView forgotPasswordText;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
<<<<<<< HEAD
=======
    private LoadingStateManager loadingManager;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        try {
            // Initialize Firebase
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
<<<<<<< HEAD
=======
            loadingManager = LoadingStateManager.getInstance();
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
            
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
        
        // Check if user is already signed in
        checkCurrentUser();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> {
            Toast.makeText(this, "Login button clicked!", Toast.LENGTH_SHORT).show();
            
            // Simple test - just show a message first
            Toast.makeText(this, "Button is working! Testing Firebase connection...", Toast.LENGTH_LONG).show();
            
            // Then try Firebase operation
            attemptLogin();
        });
        
        registerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Register button clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        
        forgotPasswordText.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot password clicked!", Toast.LENGTH_SHORT).show();
            // TODO: Implement forgot password functionality
            Toast.makeText(this, "Forgot password feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, check if profile exists
            checkUserProfile(currentUser.getUid());
        }
    }

    private void checkUserProfile(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        // User profile exists, go to main activity
                        startMainActivity();
                    } else {
                        // User profile doesn't exist, go to profile setup
                        startProfileSetup();
                    }
                });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
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

<<<<<<< HEAD
        // Show progress
        setLoading(true);
=======
        // Show progress with centralized loading manager
        loadingManager.showLoading("login_operation", progressBar, 
            loginButton, registerButton, emailEditText, passwordEditText);
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
        
        // Add debug logging
        Toast.makeText(this, "Attempting login...", Toast.LENGTH_SHORT).show();

        // Attempt login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
<<<<<<< HEAD
                        setLoading(false);
=======
                        loadingManager.hideLoading("login_operation");
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
                        
                        if (task.isSuccessful()) {
                            // Login successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserProfile(user.getUid());
                            }
                        } else {
                            // Login failed
                            String errorMessage = "Authentication failed";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

<<<<<<< HEAD
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!loading);
        registerButton.setEnabled(!loading);
        emailEditText.setEnabled(!loading);
        passwordEditText.setEnabled(!loading);
    }
=======
    // Deprecated: Using LoadingStateManager instead
    // private void setLoading(boolean loading) {
    //     progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    //     loginButton.setEnabled(!loading);
    //     registerButton.setEnabled(!loading);
    //     emailEditText.setEnabled(!loading);
    //     passwordEditText.setEnabled(!loading);
    // }
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void startProfileSetup() {
        Intent intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in when activity starts
        checkCurrentUser();
    }
<<<<<<< HEAD
=======
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any pending loading operations
        if (loadingManager != null) {
            loadingManager.hideLoading("login_operation");
        }
    }
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
}

