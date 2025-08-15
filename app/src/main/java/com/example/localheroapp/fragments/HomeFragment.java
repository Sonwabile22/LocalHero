package com.example.localheroapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
<<<<<<< HEAD
=======
import androidx.fragment.app.FragmentTransaction;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.models.User;
<<<<<<< HEAD
=======
import com.google.android.material.button.MaterialButton;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b

public class HomeFragment extends Fragment {
    private TextView welcomeText;
    private TextView wardInfoText;
    private RecyclerView recentIssuesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
<<<<<<< HEAD
=======
    private MaterialButton reportIssueButton;
    private MaterialButton viewMapButton;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
    
    private MainActivity mainActivity;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            currentUser = mainActivity.getCurrentUser();
        }
        
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
<<<<<<< HEAD
=======
        setupButtonClickListeners();
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
        loadHomeData();
    }

    private void initViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        wardInfoText = view.findViewById(R.id.wardInfoText);
        recentIssuesRecyclerView = view.findViewById(R.id.recentIssuesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
<<<<<<< HEAD
=======
        reportIssueButton = view.findViewById(R.id.reportIssueButton);
        viewMapButton = view.findViewById(R.id.viewMapButton);
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
    }

    private void setupRecyclerView() {
        recentIssuesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: Set adapter for recent issues
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
    }

    private void loadHomeData() {
        if (currentUser != null) {
            updateWelcomeMessage();
            updateWardInfo();
            loadRecentIssues();
        }
    }

    private void updateWelcomeMessage() {
        if (currentUser != null) {
            String welcomeMessage = "Welcome back, " + currentUser.getFullName() + "!";
            welcomeText.setText(welcomeMessage);
        }
    }

    private void updateWardInfo() {
        if (currentUser != null && currentUser.getWardName() != null) {
            String wardInfo = "Ward: " + currentUser.getWardName() + 
                             "\nMunicipality: " + currentUser.getMunicipalityName();
            wardInfoText.setText(wardInfo);
        }
    }

    private void loadRecentIssues() {
        // TODO: Load recent issues from database
        // This would typically involve querying Firestore for issues in the user's ward
    }

    private void refreshData() {
        loadHomeData();
        swipeRefreshLayout.setRefreshing(false);
    }
<<<<<<< HEAD
=======
    
    private void setupButtonClickListeners() {
        // View Map Button
        viewMapButton.setOnClickListener(v -> {
            navigateToMapFragment();
        });
        
        // Report Issue Button
        reportIssueButton.setOnClickListener(v -> {
            handleReportIssueClick();
        });
    }
    
    private void navigateToMapFragment() {
        MapFragment mapFragment = new MapFragment();
        
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        
        Toast.makeText(getContext(), "Opening Map View", Toast.LENGTH_SHORT).show();
    }
    
    private void handleReportIssueClick() {
        // Check user role before allowing issue reporting
        if (currentUser != null && (currentUser.isCouncillor() || currentUser.isMunicipalityStaff())) {
            // Councillors and municipality staff cannot report issues
            Toast.makeText(getContext(), "Only community members can report issues", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Navigate to Issues fragment (Issues page)
        IssuesFragment issuesFragment = new IssuesFragment();
        
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, issuesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        
        Toast.makeText(getContext(), "Opening Issues Page", Toast.LENGTH_SHORT).show();
    }
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
}



