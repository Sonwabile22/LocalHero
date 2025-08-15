package com.example.localheroapp.models;

import java.util.List;
import java.util.Map;

public class Issue {
    private String issueId;
    private String title;
    private String description;
    private String reporterId;
    private String reporterName;
    private String wardId;
    private String wardName;
    private String municipalityId;
    private String municipalityName;
    private double latitude;
    private double longitude;
    private String address;
    private IssueCategory category;
    private IssuePriority priority;
    private IssueStatus status;
    private List<String> imageUrls;
    private List<String> offlineImagePaths; // For offline storage
    private String assignedDepartmentId;
    private String assignedDepartmentName;
    private String assignedStaffId;
    private String assignedStaffName;
    private long estimatedCompletionDate;
    private long actualCompletionDate;
    private List<IssueUpdate> updates;
    private Map<String, Object> metadata; // Additional flexible data
    private boolean isOffline;
    private long createdAt;
    private long updatedAt;

    public enum IssueCategory {
        POTHOLES,
        WATER_LEAKS,
        STREETLIGHT_OUTAGES,
        GARBAGE_COLLECTION,
        ROAD_MAINTENANCE,
        SEWER_ISSUES,
        TRAFFIC_SIGNS,
        PARKS_AND_RECREATION,
        OTHER
    }

    public enum IssuePriority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    public enum IssueStatus {
        REPORTED,
        UNDER_REVIEW,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CLOSED,
        REJECTED
    }

    // Default constructor for Firestore
    public Issue() {}

    public Issue(String issueId, String title, String description, String reporterId, 
                 String reporterName, String wardId, String wardName, String municipalityId, 
                 String municipalityName, double latitude, double longitude, String address, 
                 IssueCategory category, IssuePriority priority) {
        this.issueId = issueId;
        this.title = title;
        this.description = description;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.municipalityId = municipalityId;
        this.municipalityName = municipalityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.category = category;
        this.priority = priority;
        this.status = IssueStatus.REPORTED;
        this.isOffline = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getIssueId() { return issueId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getWardId() { return wardId; }
    public void setWardId(String wardId) { this.wardId = wardId; }

    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }

    public String getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(String municipalityId) { this.municipalityId = municipalityId; }

    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public IssueCategory getCategory() { return category; }
    public void setCategory(IssueCategory category) { this.category = category; }

    public IssuePriority getPriority() { return priority; }
    public void setPriority(IssuePriority priority) { this.priority = priority; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public List<String> getOfflineImagePaths() { return offlineImagePaths; }
    public void setOfflineImagePaths(List<String> offlineImagePaths) { this.offlineImagePaths = offlineImagePaths; }

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

    public long getActualCompletionDate() { return actualCompletionDate; }
    public void setActualCompletionDate(long actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }

    public List<IssueUpdate> getUpdates() { return updates; }
    public void setUpdates(List<IssueUpdate> updates) { this.updates = updates; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public boolean isOffline() { return isOffline; }
    public void setOffline(boolean offline) { isOffline = offline; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isCompleted() {
        return status == IssueStatus.COMPLETED || status == IssueStatus.CLOSED;
    }

    public boolean isInProgress() {
        return status == IssueStatus.ASSIGNED || status == IssueStatus.IN_PROGRESS;
    }
}

