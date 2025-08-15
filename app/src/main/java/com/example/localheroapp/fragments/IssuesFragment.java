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

public class IssuesFragment extends Fragment {
    private RecyclerView issuesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noIssuesText;
    
    private MainActivity mainActivity;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_issues, container, false);
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
        loadIssues();
    }

    private void initViews(View view) {
        issuesRecyclerView = view.findViewById(R.id.issuesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        noIssuesText = view.findViewById(R.id.noIssuesText);
    }

    private void setupRecyclerView() {
        issuesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: Set adapter for issues
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
    }

    private void loadIssues() {
        if (currentUser != null) {
            // TODO: Load issues based on user role and ward
            // This would involve querying Firestore for issues
            showNoIssuesMessage();
        }
    }

    private void showNoIssuesMessage() {
        noIssuesText.setVisibility(View.VISIBLE);
        issuesRecyclerView.setVisibility(View.GONE);
    }

    private void showIssuesList() {
        noIssuesText.setVisibility(View.GONE);
        issuesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void refreshData() {
        loadIssues();
        swipeRefreshLayout.setRefreshing(false);
    }
}



