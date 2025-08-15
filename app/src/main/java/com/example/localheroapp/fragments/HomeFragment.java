package com.example.localheroapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.models.User;

public class HomeFragment extends Fragment {
    private TextView welcomeText;
    private TextView wardInfoText;
    private RecyclerView recentIssuesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    
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
        loadHomeData();
    }

    private void initViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        wardInfoText = view.findViewById(R.id.wardInfoText);
        recentIssuesRecyclerView = view.findViewById(R.id.recentIssuesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
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
}



