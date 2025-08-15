package com.example.localheroapp.models;

import java.util.List;

public class Ward {
    private String wardId;
    private String wardName;
    private String municipalityId;
    private String municipalityName;
    private String councillorId;
    private String councillorName;
    private String councillorPhone;
    private String councillorEmail;
    private String description;
    private List<String> departmentIds;
    private List<String> memberIds;
    private int memberCount;
    private double centerLatitude;
    private double centerLongitude;
    private String boundaryData; // GeoJSON or similar format
    private boolean isActive;
    private long createdAt;
    private long updatedAt;

    // Default constructor for Firestore
    public Ward() {}

    public Ward(String wardId, String wardName, String municipalityId, String municipalityName, 
                String councillorId, String councillorName, String councillorPhone, String councillorEmail) {
        this.wardId = wardId;
        this.wardName = wardName;
        this.municipalityId = municipalityId;
        this.municipalityName = municipalityName;
        this.councillorId = councillorId;
        this.councillorName = councillorName;
        this.councillorPhone = councillorPhone;
        this.councillorEmail = councillorEmail;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getWardId() { return wardId; }
    public void setWardId(String wardId) { this.wardId = wardId; }

    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }

    public String getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(String municipalityId) { this.municipalityId = municipalityId; }

    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }

    public String getCouncillorId() { return councillorId; }
    public void setCouncillorId(String councillorId) { this.councillorId = councillorId; }

    public String getCouncillorName() { return councillorName; }
    public void setCouncillorName(String councillorName) { this.councillorName = councillorName; }

    public String getCouncillorPhone() { return councillorPhone; }
    public void setCouncillorPhone(String councillorPhone) { this.councillorPhone = councillorPhone; }

    public String getCouncillorEmail() { return councillorEmail; }
    public void setCouncillorEmail(String councillorEmail) { this.councillorEmail = councillorEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getDepartmentIds() { return departmentIds; }
    public void setDepartmentIds(List<String> departmentIds) { this.departmentIds = departmentIds; }

    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }

    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

    public double getCenterLatitude() { return centerLatitude; }
    public void setCenterLatitude(double centerLatitude) { this.centerLatitude = centerLatitude; }

    public double getCenterLongitude() { return centerLongitude; }
    public void setCenterLongitude(double centerLongitude) { this.centerLongitude = centerLongitude; }

    public String getBoundaryData() { return boundaryData; }
    public void setBoundaryData(String boundaryData) { this.boundaryData = boundaryData; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}

