package com.example.localheroapp.models;

import java.util.List;

public class Municipality {
    private String municipalityId;
    private String municipalityName;
    private String municipalityCode;
    private String province;
    private String city;
    private String contactPhone;
    private String contactEmail;
    private String website;
    private String logoUrl;
    private List<Department> departments;
    private List<String> wardIds;
    private int wardCount;
    private double centerLatitude;
    private double centerLongitude;
    private String boundaryData;
    private boolean isActive;
    private long createdAt;
    private long updatedAt;

    public static class Department {
        private String departmentId;
        private String departmentName;
        private String departmentCode;
        private String description;
        private String headOfDepartmentId;
        private String headOfDepartmentName;
        private String contactPhone;
        private String contactEmail;
        private List<String> staffIds;
        private boolean isActive;

        public Department() {}

        public Department(String departmentId, String departmentName, String departmentCode, 
                        String description, String headOfDepartmentId, String headOfDepartmentName) {
            this.departmentId = departmentId;
            this.departmentName = departmentName;
            this.departmentCode = departmentCode;
            this.description = description;
            this.headOfDepartmentId = headOfDepartmentId;
            this.headOfDepartmentName = headOfDepartmentName;
            this.isActive = true;
        }

        // Getters and Setters
        public String getDepartmentId() { return departmentId; }
        public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

        public String getDepartmentCode() { return departmentCode; }
        public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getHeadOfDepartmentId() { return headOfDepartmentId; }
        public void setHeadOfDepartmentId(String headOfDepartmentId) { this.headOfDepartmentId = headOfDepartmentId; }

        public String getHeadOfDepartmentName() { return headOfDepartmentName; }
        public void setHeadOfDepartmentName(String headOfDepartmentName) { this.headOfDepartmentName = headOfDepartmentName; }

        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

        public String getContactEmail() { return contactEmail; }
        public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

        public List<String> getStaffIds() { return staffIds; }
        public void setStaffIds(List<String> staffIds) { this.staffIds = staffIds; }

        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }
    }

    // Default constructor for Firestore
    public Municipality() {}

    public Municipality(String municipalityId, String municipalityName, String municipalityCode, 
                      String province, String city) {
        this.municipalityId = municipalityId;
        this.municipalityName = municipalityName;
        this.municipalityCode = municipalityCode;
        this.province = province;
        this.city = city;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(String municipalityId) { this.municipalityId = municipalityId; }

    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }

    public String getMunicipalityCode() { return municipalityCode; }
    public void setMunicipalityCode(String municipalityCode) { this.municipalityCode = municipalityCode; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }

    public List<String> getWardIds() { return wardIds; }
    public void setWardIds(List<String> wardIds) { this.wardIds = wardIds; }

    public int getWardCount() { return wardCount; }
    public void setWardCount(int wardCount) { this.wardCount = wardCount; }

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



