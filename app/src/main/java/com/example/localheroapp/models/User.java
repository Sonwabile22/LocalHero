package com.example.localheroapp.models;

import java.util.List;

public class User {
    private String userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String wardId;
    private String wardName;
    private String municipalityId;
    private String municipalityName;
    private UserRole userRole;
    private String profileImageUrl;
    private boolean isOnline;
    private long lastSeen;
    private List<String> departmentIds; // For municipality staff
    private boolean isVerified;
    private long createdAt;
    private long updatedAt;

    public enum UserRole {
        COMMUNITY_MEMBER,
        COUNCILLOR,
        MUNICIPALITY_STAFF,
        ADMIN
    }

    // Default constructor for Firestore
    public User() {}

    public User(String userId, String email, String fullName, String phoneNumber, 
                String wardId, String wardName, String municipalityId, String municipalityName, 
                UserRole userRole) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.wardId = wardId;
        this.wardName = wardName;
        this.municipalityId = municipalityId;
        this.municipalityName = municipalityName;
        this.userRole = userRole;
        this.isOnline = false;
        this.lastSeen = System.currentTimeMillis();
        this.isVerified = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getWardId() { return wardId; }
    public void setWardId(String wardId) { this.wardId = wardId; }

    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }

    public String getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(String municipalityId) { this.municipalityId = municipalityId; }

    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }

    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }

    public List<String> getDepartmentIds() { return departmentIds; }
    public void setDepartmentIds(List<String> departmentIds) { this.departmentIds = departmentIds; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isCouncillor() {
        return userRole == UserRole.COUNCILLOR;
    }

    public boolean isMunicipalityStaff() {
        return userRole == UserRole.MUNICIPALITY_STAFF;
    }

    public boolean isCommunityMember() {
        return userRole == UserRole.COMMUNITY_MEMBER;
    }
}

