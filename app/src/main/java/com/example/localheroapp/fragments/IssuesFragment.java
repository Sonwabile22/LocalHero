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
<<<<<<< HEAD
import com.example.localheroapp.models.User;
=======
import com.example.localheroapp.adapters.IssuesAdapter;
import com.example.localheroapp.models.Issue;
import com.example.localheroapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

// Import LoadingStateManager for performance optimization
import com.example.localheroapp.utils.LoadingStateManager;

import java.util.ArrayList;
import java.util.List;
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b

public class IssuesFragment extends Fragment {
    private RecyclerView issuesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noIssuesText;
    
    private MainActivity mainActivity;
    private User currentUser;
<<<<<<< HEAD
=======
    private FirebaseFirestore db;
    private IssuesAdapter issuesAdapter;
    private LoadingStateManager loadingManager;
    private long lastRefreshTime = 0;
    private static final long REFRESH_COOLDOWN_MS = 2000; // 2 second cooldown
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b

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
        
<<<<<<< HEAD
=======
        // Initialize Firestore and LoadingStateManager
        db = FirebaseFirestore.getInstance();
        loadingManager = LoadingStateManager.getInstance();
        
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
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
<<<<<<< HEAD
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
=======
        issuesAdapter = new IssuesAdapter(currentUser);
        
        // Set up click listeners
        issuesAdapter.setOnIssueClickListener(issue -> {
            // Handle issue click - could open detail view
            android.widget.Toast.makeText(getContext(), "Issue clicked: " + issue.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
        });
        
        issuesAdapter.setOnStatusChangeListener((issue, newStatus) -> {
            updateIssueStatus(issue, newStatus);
        });
        
        issuesRecyclerView.setAdapter(issuesAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Implement debouncing to prevent excessive refresh requests
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRefreshTime < REFRESH_COOLDOWN_MS) {
                swipeRefreshLayout.setRefreshing(false);
                android.widget.Toast.makeText(getContext(), "Please wait before refreshing again", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            lastRefreshTime = currentTime;
            
            loadingManager.handleSwipeRefresh("issues_refresh", swipeRefreshLayout, this::refreshData);
        });
        
        // Set refresh colors for better UX
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );
    }

    private void loadIssues() {
        if (currentUser == null) {
            showNoIssuesMessage();
            return;
        }

        // Load issues based on user role
        if (currentUser.isCommunityMember()) {
            // Community members see issues in their ward
            loadIssuesByWard(currentUser.getWardId());
        } else if (currentUser.isCouncillor()) {
            // Councillors see issues in their ward
            loadIssuesByWard(currentUser.getWardId());
        } else if (currentUser.isMunicipalityStaff()) {
            // Municipality staff see all issues in their municipality
            loadIssuesByMunicipality(currentUser.getMunicipalityId());
        } else {
            // Default: load all issues
            loadAllIssues();
        }
    }
    
    private void loadIssuesByWard(String wardId) {
        if (wardId == null) {
            loadAllIssues();
            return;
        }
        
        db.collection("issues")
                .whereEqualTo("wardId", wardId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Issue> issues = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Issue issue = convertDocumentToIssue(doc);
                        if (issue != null) {
                            issues.add(issue);
                        }
                    }
                    updateUI(issues);
                    // Stop refresh loading if active
                    loadingManager.stopRefresh("issues_refresh", swipeRefreshLayout);
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(getContext(), "Error loading issues: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    showNoIssuesMessage();
                    loadingManager.stopRefresh("issues_refresh", swipeRefreshLayout);
                });
    }
    
    private void loadIssuesByMunicipality(String municipalityId) {
        if (municipalityId == null) {
            loadAllIssues();
            return;
        }
        
        db.collection("issues")
                .whereEqualTo("municipalityId", municipalityId)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Issue> issues = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Issue issue = convertDocumentToIssue(doc);
                        if (issue != null) {
                            issues.add(issue);
                        }
                    }
                    updateUI(issues);
                    // Stop refresh loading if active
                    loadingManager.stopRefresh("issues_refresh", swipeRefreshLayout);
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(getContext(), "Error loading issues: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    showNoIssuesMessage();
                    loadingManager.stopRefresh("issues_refresh", swipeRefreshLayout);
                });
    }
    
    private void loadAllIssues() {
        db.collection("issues")
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Issue> issues = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Issue issue = convertDocumentToIssue(doc);
                        if (issue != null) {
                            issues.add(issue);
                        }
                    }
                    updateUI(issues);
                    // Stop refresh loading if active
                    loadingManager.stopRefresh("issues_refresh", swipeRefreshLayout);
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(getContext(), "Error loading issues: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    showNoIssuesMessage();
                    loadingManager.stopRefresh("issues_refresh", swipeRefreshLayout);
                });
    }
    
    private Issue convertDocumentToIssue(QueryDocumentSnapshot doc) {
        try {
            Issue issue = new Issue();
            issue.setIssueId(doc.getString("issueId"));
            issue.setTitle(doc.getString("title"));
            issue.setDescription(doc.getString("description"));
            issue.setReporterId(doc.getString("reporterId"));
            issue.setReporterName(doc.getString("reporterName"));
            issue.setWardId(doc.getString("wardId"));
            issue.setWardName(doc.getString("wardName"));
            issue.setMunicipalityId(doc.getString("municipalityId"));
            issue.setMunicipalityName(doc.getString("municipalityName"));
            
            Double latitude = doc.getDouble("latitude");
            Double longitude = doc.getDouble("longitude");
            if (latitude != null && longitude != null) {
                issue.setLatitude(latitude);
                issue.setLongitude(longitude);
            }
            
            issue.setAddress(doc.getString("address"));
            
            // Convert string values to enums
            String categoryStr = doc.getString("category");
            if (categoryStr != null) {
                try {
                    issue.setCategory(Issue.IssueCategory.valueOf(categoryStr));
                } catch (IllegalArgumentException e) {
                    issue.setCategory(Issue.IssueCategory.OTHER);
                }
            }
            
            String priorityStr = doc.getString("priority");
            if (priorityStr != null) {
                try {
                    issue.setPriority(Issue.IssuePriority.valueOf(priorityStr));
                } catch (IllegalArgumentException e) {
                    issue.setPriority(Issue.IssuePriority.MEDIUM);
                }
            }
            
            String statusStr = doc.getString("status");
            if (statusStr != null) {
                try {
                    issue.setStatus(Issue.IssueStatus.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    issue.setStatus(Issue.IssueStatus.REPORTED);
                }
            }
            
            Long createdAt = doc.getLong("createdAt");
            if (createdAt != null) {
                issue.setCreatedAt(createdAt);
            }
            
            Long updatedAt = doc.getLong("updatedAt");
            if (updatedAt != null) {
                issue.setUpdatedAt(updatedAt);
            }
            
            return issue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void updateIssueStatus(Issue issue, Issue.IssueStatus newStatus) {
        // Update issue status in Firestore
        db.collection("issues").document(issue.getIssueId())
                .update("status", newStatus.name(), "updatedAt", System.currentTimeMillis())
                .addOnSuccessListener(aVoid -> {
                    android.widget.Toast.makeText(getContext(), "Issue status updated successfully", android.widget.Toast.LENGTH_SHORT).show();
                    // Refresh the data
                    loadIssues();
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(getContext(), "Failed to update status: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                });
    }
    
    private void updateUI(List<Issue> issues) {
        if (issues.isEmpty()) {
            showNoIssuesMessage();
        } else {
            showIssuesList();
            issuesAdapter.updateIssues(issues);
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
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
<<<<<<< HEAD
        loadIssues();
        swipeRefreshLayout.setRefreshing(false);
=======
        // Only refresh if not already loading
        if (!loadingManager.isLoading("issues_refresh")) {
            loadIssues();
        }
        // Note: swipeRefreshLayout.setRefreshing(false) is now handled by LoadingStateManager
>>>>>>> 128b1afa5f0dc11ba4f41a1f80f23565a984143b
    }
}



