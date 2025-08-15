package com.example.localheroapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.localheroapp.auth.LoginActivity;
import com.example.localheroapp.fragments.ChatsFragment;
import com.example.localheroapp.fragments.HomeFragment;
import com.example.localheroapp.fragments.IssuesFragment;
import com.example.localheroapp.fragments.ProfileFragment;
import com.example.localheroapp.fragments.ReportIssueFragment;
import com.example.localheroapp.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout fragmentContainer;
    private FloatingActionButton reportIssueFab;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        // Check if user is authenticated
        checkAuthentication();
        
        // Initialize views
        initViews();
        setupBottomNavigation();
        setupFloatingActionButton();
        
        // Load user profile
        loadUserProfile();
        
        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void checkAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User not authenticated, go to login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        reportIssueFab = findViewById(R.id.reportIssueFab);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_issues) {
                fragment = new IssuesFragment();
            } else if (itemId == R.id.nav_chats) {
                fragment = new ChatsFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            
            return false;
        });
    }

    private void setupFloatingActionButton() {
        reportIssueFab.setOnClickListener(v -> {
            // Open report issue fragment
            loadFragment(new ReportIssueFragment());
            
            // Update bottom navigation to show no selection
            bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.nav_issues).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.nav_chats).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(false);
        });


    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Replace the current fragment
        transaction.replace(R.id.fragmentContainer, fragment);
        
        // Add to back stack if it's not the home fragment
        if (!(fragment instanceof HomeFragment)) {
            transaction.addToBackStack(null);
        }
        
        transaction.commit();
    }

    private void loadUserProfile() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            db.collection("users").document(firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot document = task.getResult();
                            currentUser = document.toObject(User.class);
                            if (currentUser != null) {
                                currentUser.setUserId(document.getId());
                                // User profile loaded successfully
                                updateUIForUser();
                            }
                        }
                    });
        }
    }

    private void updateUIForUser() {
        if (currentUser != null) {
            // Update UI based on user role
            if (currentUser.isCouncillor()) {
                // Councillor specific UI updates
                reportIssueFab.setVisibility(View.GONE); // Councillors don't report issues
            } else if (currentUser.isMunicipalityStaff()) {
                // Municipality staff specific UI updates
                reportIssueFab.setVisibility(View.GONE); // Staff don't report issues
            } else {
                // Community member UI updates
                reportIssueFab.setVisibility(View.VISIBLE);
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}