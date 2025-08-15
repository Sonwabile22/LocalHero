package com.example.localheroapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import com.example.localheroapp.R;
import com.example.localheroapp.models.Issue;
import com.example.localheroapp.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssueViewHolder> {
    
    private List<Issue> issues = new ArrayList<>();
    private User currentUser;
    private OnIssueClickListener onIssueClickListener;
    private OnStatusChangeListener onStatusChangeListener;
    
    public interface OnIssueClickListener {
        void onIssueClick(Issue issue);
    }
    
    public interface OnStatusChangeListener {
        void onStatusChange(Issue issue, Issue.IssueStatus newStatus);
    }
    
    public IssuesAdapter(User currentUser) {
        this.currentUser = currentUser;
    }
    
    public void setOnIssueClickListener(OnIssueClickListener listener) {
        this.onIssueClickListener = listener;
    }
    
    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.onStatusChangeListener = listener;
    }
    
    public void updateIssues(List<Issue> newIssues) {
        this.issues.clear();
        this.issues.addAll(newIssues);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_issue, parent, false);
        return new IssueViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull IssueViewHolder holder, int position) {
        Issue issue = issues.get(position);
        holder.bind(issue);
    }
    
    @Override
    public int getItemCount() {
        return issues.size();
    }
    
    class IssueViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView titleText;
        private TextView descriptionText;
        private TextView reporterText;
        private TextView addressText;
        private TextView dateText;
        private Chip statusChip;
        private Chip priorityChip;
        private Chip categoryChip;
        private Button changeStatusButton;
        private Spinner statusSpinner;
        
        public IssueViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            reporterText = itemView.findViewById(R.id.reporterText);
            addressText = itemView.findViewById(R.id.addressText);
            dateText = itemView.findViewById(R.id.dateText);
            statusChip = itemView.findViewById(R.id.statusChip);
            priorityChip = itemView.findViewById(R.id.priorityChip);
            categoryChip = itemView.findViewById(R.id.categoryChip);
            changeStatusButton = itemView.findViewById(R.id.changeStatusButton);
            statusSpinner = itemView.findViewById(R.id.statusSpinner);
        }
        
        public void bind(Issue issue) {
            titleText.setText(issue.getTitle());
            descriptionText.setText(issue.getDescription());
            reporterText.setText("Reported by: " + issue.getReporterName());
            addressText.setText(issue.getAddress());
            
            // Format date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(new Date(issue.getCreatedAt()));
            dateText.setText(formattedDate);
            
            // Set status chip
            statusChip.setText(getStatusDisplayName(issue.getStatus()));
            statusChip.setChipBackgroundColorResource(getStatusColor(issue.getStatus()));
            
            // Set priority chip
            priorityChip.setText(issue.getPriority().name());
            priorityChip.setChipBackgroundColorResource(getPriorityColor(issue.getPriority()));
            
            // Set category chip
            categoryChip.setText(issue.getCategory().name().replace("_", " "));
            
            // Handle role-based permissions
            setupRoleBasedUI(issue);
            
            // Set click listener
            cardView.setOnClickListener(v -> {
                if (onIssueClickListener != null) {
                    onIssueClickListener.onIssueClick(issue);
                }
            });
        }
        
        private void setupRoleBasedUI(Issue issue) {
            if (currentUser != null && currentUser.isMunicipalityStaff()) {
                // Municipality workers can change status
                changeStatusButton.setVisibility(View.VISIBLE);
                statusSpinner.setVisibility(View.VISIBLE);
                
                // Setup status spinner
                setupStatusSpinner(issue);
                
                changeStatusButton.setOnClickListener(v -> {
                    int selectedPosition = statusSpinner.getSelectedItemPosition();
                    Issue.IssueStatus newStatus = getStatusFromPosition(selectedPosition);
                    
                    if (newStatus != issue.getStatus() && onStatusChangeListener != null) {
                        onStatusChangeListener.onStatusChange(issue, newStatus);
                    }
                });
            } else {
                // Other users can only view
                changeStatusButton.setVisibility(View.GONE);
                statusSpinner.setVisibility(View.GONE);
            }
        }
        
        private void setupStatusSpinner(Issue issue) {
            List<String> statusOptions = new ArrayList<>();
            statusOptions.add("Reported");
            statusOptions.add("In Progress");
            statusOptions.add("Completed");
            statusOptions.add("Closed");
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, statusOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            statusSpinner.setAdapter(adapter);
            
            // Set current status as selected
            int currentPosition = getStatusPosition(issue.getStatus());
            statusSpinner.setSelection(currentPosition);
        }
        
        private int getStatusPosition(Issue.IssueStatus status) {
            switch (status) {
                case REPORTED: return 0;
                case IN_PROGRESS: return 1;
                case COMPLETED: return 2;
                case CLOSED: return 3;
                default: return 0;
            }
        }
        
        private Issue.IssueStatus getStatusFromPosition(int position) {
            switch (position) {
                case 0: return Issue.IssueStatus.REPORTED;
                case 1: return Issue.IssueStatus.IN_PROGRESS;
                case 2: return Issue.IssueStatus.COMPLETED;
                case 3: return Issue.IssueStatus.CLOSED;
                default: return Issue.IssueStatus.REPORTED;
            }
        }
        
        private String getStatusDisplayName(Issue.IssueStatus status) {
            switch (status) {
                case REPORTED: return "Reported";
                case IN_PROGRESS: return "In Progress";
                case COMPLETED: return "Completed";
                case CLOSED: return "Closed";
                default: return "Unknown";
            }
        }
        
        private int getStatusColor(Issue.IssueStatus status) {
            switch (status) {
                case REPORTED: return R.color.error;
                case IN_PROGRESS: return R.color.warning;
                case COMPLETED: return R.color.success;
                case CLOSED: return R.color.text_secondary;
                default: return R.color.text_secondary;
            }
        }
        
        private int getPriorityColor(Issue.IssuePriority priority) {
            switch (priority) {
                case HIGH: return R.color.error;
                case MEDIUM: return R.color.warning;
                case LOW: return R.color.success;
                default: return R.color.text_secondary;
            }
        }
    }
}
