package com.example.localheroapp.models;

public class IssueUpdate {
    private String updateId;
    private String issueId;
    private String updatedBy;
    private String updatedByName;
    private String updateType;
    private String description;
    private String oldStatus;
    private String newStatus;
    private String assignedDepartmentId;
    private String assignedDepartmentName;
    private String assignedStaffId;
    private String assignedStaffName;
    private long estimatedCompletionDate;
    private String imageUrl;
    private long createdAt;

    public enum UpdateType {
        STATUS_CHANGE,
        ASSIGNMENT,
        PROGRESS_UPDATE,
        COMPLETION,
        REJECTION,
        COMMENT
    }

    // Default constructor for Firestore
    public IssueUpdate() {}

    public IssueUpdate(String updateId, String issueId, String updatedBy, String updatedByName, 
                      String updateType, String description) {
        this.updateId = updateId;
        this.issueId = issueId;
        this.updatedBy = updatedBy;
        this.updatedByName = updatedByName;
        this.updateType = updateType;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getUpdateId() { return updateId; }
    public void setUpdateId(String updateId) { this.updateId = updateId; }

    public String getIssueId() { return issueId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }

    public String getUpdateType() { return updateType; }
    public void setUpdateType(String updateType) { this.updateType = updateType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public String getAssignedDepartmentId() { return assignedDepartmentId; }
    public void setAssignedDepartmentId(String assignedDepartmentId) { this.assignedDepartmentId = assignedDepartmentId; }

    public String getAssignedDepartmentName() { return assignedDepartmentName; }
    public void setAssignedDepartmentName(String assignedDepartmentName) { this.assignedDepartmentName = assignedDepartmentName; }

    public String getAssignedStaffId() { return assignedStaffId; }
    public void setAssignedStaffId(String assignedStaffId) { this.assignedStaffId = assignedStaffId; }

    public String getAssignedStaffName() { return assignedStaffName; }
    public void setAssignedStaffName(String assignedStaffName) { this.assignedStaffName = assignedStaffName; }

    public long getEstimatedCompletionDate() { return estimatedCompletionDate; }
    public void setEstimatedCompletionDate(long estimatedCompletionDate) { this.estimatedCompletionDate = estimatedCompletionDate; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}

