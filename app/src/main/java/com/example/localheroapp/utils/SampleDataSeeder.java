package com.example.localheroapp.utils;

import android.util.Log;

import com.example.localheroapp.models.Issue;
import com.example.localheroapp.models.User;
import com.example.localheroapp.models.Ward;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SampleDataSeeder {
    private static final String TAG = "SampleDataSeeder";
    private FirebaseFirestore db;

    public SampleDataSeeder() {
        db = FirebaseFirestore.getInstance();
    }

    public void seedAllData() {
        seedMunicipalities();
        seedWards();
        seedSampleIssues();
        Log.d(TAG, "Sample data seeding initiated");
    }

    private void seedMunicipalities() {
        List<Map<String, Object>> municipalities = new ArrayList<>();
        
        // City of Johannesburg Metropolitan Municipality
        Map<String, Object> joburg = new HashMap<>();
        joburg.put("municipalityId", "mun_joburg_001");
        joburg.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        joburg.put("municipalityType", "Metropolitan");
        joburg.put("province", "Gauteng");
        joburg.put("isActive", true);
        joburg.put("createdAt", System.currentTimeMillis());
        municipalities.add(joburg);

        // City of Cape Town Metropolitan Municipality
        Map<String, Object> capetown = new HashMap<>();
        capetown.put("municipalityId", "mun_capetown_001");
        capetown.put("municipalityName", "City of Cape Town Metropolitan Municipality");
        capetown.put("municipalityType", "Metropolitan");
        capetown.put("province", "Western Cape");
        capetown.put("isActive", true);
        capetown.put("createdAt", System.currentTimeMillis());
        municipalities.add(capetown);

        // eThekwini Metropolitan Municipality (Durban)
        Map<String, Object> durban = new HashMap<>();
        durban.put("municipalityId", "mun_durban_001");
        durban.put("municipalityName", "eThekwini Metropolitan Municipality");
        durban.put("municipalityType", "Metropolitan");
        durban.put("province", "KwaZulu-Natal");
        durban.put("isActive", true);
        durban.put("createdAt", System.currentTimeMillis());
        municipalities.add(durban);

        // Seed municipalities to Firestore
        for (Map<String, Object> municipality : municipalities) {
            String municipalityId = (String) municipality.get("municipalityId");
            db.collection("municipalities").document(municipalityId)
                    .set(municipality)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Municipality seeded: " + municipality.get("municipalityName")))
                    .addOnFailureListener(e -> Log.e(TAG, "Error seeding municipality", e));
        }
    }

    private void seedWards() {
        List<Map<String, Object>> wards = new ArrayList<>();

        // Johannesburg Wards
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> ward = new HashMap<>();
            ward.put("wardId", "ward_joburg_" + String.format("%03d", i));
            ward.put("wardName", "Ward " + i + " - Johannesburg");
            ward.put("wardNumber", i);
            ward.put("municipalityId", "mun_joburg_001");
            ward.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
            ward.put("isActive", true);
            ward.put("createdAt", System.currentTimeMillis());
            wards.add(ward);
        }

        // Cape Town Wards
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> ward = new HashMap<>();
            ward.put("wardId", "ward_capetown_" + String.format("%03d", i));
            ward.put("wardName", "Ward " + i + " - Cape Town");
            ward.put("wardNumber", i);
            ward.put("municipalityId", "mun_capetown_001");
            ward.put("municipalityName", "City of Cape Town Metropolitan Municipality");
            ward.put("isActive", true);
            ward.put("createdAt", System.currentTimeMillis());
            wards.add(ward);
        }

        // Durban Wards
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> ward = new HashMap<>();
            ward.put("wardId", "ward_durban_" + String.format("%03d", i));
            ward.put("wardName", "Ward " + i + " - Durban");
            ward.put("wardNumber", i);
            ward.put("municipalityId", "mun_durban_001");
            ward.put("municipalityName", "eThekwini Metropolitan Municipality");
            ward.put("isActive", true);
            ward.put("createdAt", System.currentTimeMillis());
            wards.add(ward);
        }

        // Seed wards to Firestore
        for (Map<String, Object> ward : wards) {
            String wardId = (String) ward.get("wardId");
            db.collection("wards").document(wardId)
                    .set(ward)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Ward seeded: " + ward.get("wardName")))
                    .addOnFailureListener(e -> Log.e(TAG, "Error seeding ward", e));
        }
    }

    private void seedSampleIssues() {
        List<Map<String, Object>> issues = new ArrayList<>();

        // Sample Issue 1 - Pothole
        Map<String, Object> issue1 = new HashMap<>();
        issue1.put("issueId", UUID.randomUUID().toString());
        issue1.put("title", "Large Pothole on Main Road");
        issue1.put("description", "There's a large pothole on Main Road that's causing damage to vehicles. It's been there for weeks and getting worse with rain.");
        issue1.put("reporterId", "sample_user_001");
        issue1.put("reporterName", "John Smith");
        issue1.put("wardId", "ward_joburg_001");
        issue1.put("wardName", "Ward 1 - Johannesburg");
        issue1.put("municipalityId", "mun_joburg_001");
        issue1.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        issue1.put("latitude", -26.2041);
        issue1.put("longitude", 28.0473);
        issue1.put("address", "123 Main Road, Johannesburg");
        issue1.put("category", "ROADS_AND_TRANSPORT");
        issue1.put("priority", "HIGH");
        issue1.put("status", "REPORTED");
        issue1.put("createdAt", System.currentTimeMillis() - 86400000); // 1 day ago
        issue1.put("updatedAt", System.currentTimeMillis() - 86400000);
        issues.add(issue1);

        // Sample Issue 2 - Water Leak
        Map<String, Object> issue2 = new HashMap<>();
        issue2.put("issueId", UUID.randomUUID().toString());
        issue2.put("title", "Water Leak in Park");
        issue2.put("description", "Water is constantly leaking from a pipe in Central Park. It's wasting water and creating muddy conditions.");
        issue2.put("reporterId", "sample_user_002");
        issue2.put("reporterName", "Mary Johnson");
        issue2.put("wardId", "ward_joburg_002");
        issue2.put("wardName", "Ward 2 - Johannesburg");
        issue2.put("municipalityId", "mun_joburg_001");
        issue2.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        issue2.put("latitude", -26.1951);
        issue2.put("longitude", 28.0565);
        issue2.put("address", "Central Park, Johannesburg");
        issue2.put("category", "WATER_AND_SANITATION");
        issue2.put("priority", "MEDIUM");
        issue2.put("status", "IN_PROGRESS");
        issue2.put("createdAt", System.currentTimeMillis() - 172800000); // 2 days ago
        issue2.put("updatedAt", System.currentTimeMillis() - 86400000); // 1 day ago
        issues.add(issue2);

        // Sample Issue 3 - Broken Streetlight
        Map<String, Object> issue3 = new HashMap<>();
        issue3.put("issueId", UUID.randomUUID().toString());
        issue3.put("title", "Broken Streetlight");
        issue3.put("description", "The streetlight on Oak Street has been broken for over a week. It's making the area unsafe at night.");
        issue3.put("reporterId", "sample_user_003");
        issue3.put("reporterName", "David Wilson");
        issue3.put("wardId", "ward_capetown_001");
        issue3.put("wardName", "Ward 1 - Cape Town");
        issue3.put("municipalityId", "mun_capetown_001");
        issue3.put("municipalityName", "City of Cape Town Metropolitan Municipality");
        issue3.put("latitude", -33.9249);
        issue3.put("longitude", 18.4241);
        issue3.put("address", "456 Oak Street, Cape Town");
        issue3.put("category", "ELECTRICITY");
        issue3.put("priority", "HIGH");
        issue3.put("status", "RESOLVED");
        issue3.put("createdAt", System.currentTimeMillis() - 604800000); // 1 week ago
        issue3.put("updatedAt", System.currentTimeMillis() - 172800000); // 2 days ago
        issues.add(issue3);

        // Sample Issue 4 - Illegal Dumping
        Map<String, Object> issue4 = new HashMap<>();
        issue4.put("issueId", UUID.randomUUID().toString());
        issue4.put("title", "Illegal Dumping of Waste");
        issue4.put("description", "Someone has been dumping household waste in the vacant lot next to the community center. It's attracting rats and creating a health hazard.");
        issue4.put("reporterId", "sample_user_004");
        issue4.put("reporterName", "Sarah Davis");
        issue4.put("wardId", "ward_durban_001");
        issue4.put("wardName", "Ward 1 - Durban");
        issue4.put("municipalityId", "mun_durban_001");
        issue4.put("municipalityName", "eThekwini Metropolitan Municipality");
        issue4.put("latitude", -29.8587);
        issue4.put("longitude", 31.0218);
        issue4.put("address", "789 Community Center Road, Durban");
        issue4.put("category", "WASTE_MANAGEMENT");
        issue4.put("priority", "HIGH");
        issue4.put("status", "REPORTED");
        issue4.put("createdAt", System.currentTimeMillis() - 259200000); // 3 days ago
        issue4.put("updatedAt", System.currentTimeMillis() - 259200000);
        issues.add(issue4);

        // Sample Issue 5 - Damaged Road Sign
        Map<String, Object> issue5 = new HashMap<>();
        issue5.put("issueId", UUID.randomUUID().toString());
        issue5.put("title", "Damaged Road Sign");
        issue5.put("description", "The stop sign at the intersection of Pine and Maple Street has been knocked over and is lying on the ground.");
        issue5.put("reporterId", "sample_user_005");
        issue5.put("reporterName", "Michael Brown");
        issue5.put("wardId", "ward_joburg_003");
        issue5.put("wardName", "Ward 3 - Johannesburg");
        issue5.put("municipalityId", "mun_joburg_001");
        issue5.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        issue5.put("latitude", -26.1848);
        issue5.put("longitude", 28.0426);
        issue5.put("address", "Pine & Maple Street Intersection, Johannesburg");
        issue5.put("category", "ROADS_AND_TRANSPORT");
        issue5.put("priority", "MEDIUM");
        issue5.put("status", "IN_PROGRESS");
        issue5.put("createdAt", System.currentTimeMillis() - 345600000); // 4 days ago
        issue5.put("updatedAt", System.currentTimeMillis() - 86400000); // 1 day ago
        issues.add(issue5);

        // Seed issues to Firestore
        for (Map<String, Object> issue : issues) {
            String issueId = (String) issue.get("issueId");
            db.collection("issues").document(issueId)
                    .set(issue)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Issue seeded: " + issue.get("title")))
                    .addOnFailureListener(e -> Log.e(TAG, "Error seeding issue", e));
        }
    }

    public void seedSampleUsers() {
        List<Map<String, Object>> users = new ArrayList<>();

        // Sample Community Member
        Map<String, Object> user1 = new HashMap<>();
        user1.put("userId", "sample_user_001");
        user1.put("email", "john.smith@example.com");
        user1.put("fullName", "John Smith");
        user1.put("phoneNumber", "+27 123 456 789");
        user1.put("wardId", "ward_joburg_001");
        user1.put("wardName", "Ward 1 - Johannesburg");
        user1.put("municipalityId", "mun_joburg_001");
        user1.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        user1.put("userRole", "COMMUNITY_MEMBER");
        user1.put("isVerified", true);
        user1.put("createdAt", System.currentTimeMillis());
        user1.put("updatedAt", System.currentTimeMillis());
        users.add(user1);

        // Sample Councillor
        Map<String, Object> user2 = new HashMap<>();
        user2.put("userId", "sample_councillor_001");
        user2.put("email", "councillor.jones@joburg.gov.za");
        user2.put("fullName", "Councillor Amanda Jones");
        user2.put("phoneNumber", "+27 111 222 333");
        user2.put("wardId", "ward_joburg_001");
        user2.put("wardName", "Ward 1 - Johannesburg");
        user2.put("municipalityId", "mun_joburg_001");
        user2.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        user2.put("userRole", "COUNCILLOR");
        user2.put("isVerified", true);
        user2.put("createdAt", System.currentTimeMillis());
        user2.put("updatedAt", System.currentTimeMillis());
        users.add(user2);

        // Sample Municipality Worker
        Map<String, Object> user3 = new HashMap<>();
        user3.put("userId", "sample_worker_001");
        user3.put("email", "worker.mike@joburg.gov.za");
        user3.put("fullName", "Mike Municipal Worker");
        user3.put("phoneNumber", "+27 444 555 666");
        user3.put("wardId", null); // Municipality workers aren't tied to specific wards
        user3.put("wardName", null);
        user3.put("municipalityId", "mun_joburg_001");
        user3.put("municipalityName", "City of Johannesburg Metropolitan Municipality");
        user3.put("userRole", "MUNICIPALITY_STAFF");
        user3.put("isVerified", true);
        user3.put("createdAt", System.currentTimeMillis());
        user3.put("updatedAt", System.currentTimeMillis());
        users.add(user3);

        // Seed users to Firestore
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            db.collection("users").document(userId)
                    .set(user)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User seeded: " + user.get("fullName")))
                    .addOnFailureListener(e -> Log.e(TAG, "Error seeding user", e));
        }
    }
}
