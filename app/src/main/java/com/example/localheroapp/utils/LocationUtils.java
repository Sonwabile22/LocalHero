package com.example.localheroapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class LocationUtils {
    
    /**
     * Check if location permissions are granted
     */
    public static boolean hasLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                    == PackageManager.PERMISSION_GRANTED ||
                   ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) 
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
    
    /**
     * Calculate distance between two locations in meters
     */
    public static float calculateDistance(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return -1;
        }
        return location1.distanceTo(location2);
    }
    
    /**
     * Check if a location is within a certain radius of another location
     */
    public static boolean isWithinRadius(Location center, Location point, float radiusMeters) {
        float distance = calculateDistance(center, point);
        return distance >= 0 && distance <= radiusMeters;
    }
    
    /**
     * Format distance for display
     */
    public static String formatDistance(float distanceMeters) {
        if (distanceMeters < 1000) {
            return String.format("%.0f m", distanceMeters);
        } else {
            float km = distanceMeters / 1000;
            return String.format("%.1f km", km);
        }
    }
    
    /**
     * Get location accuracy description
     */
    public static String getAccuracyDescription(float accuracyMeters) {
        if (accuracyMeters < 5) {
            return "Excellent";
        } else if (accuracyMeters < 10) {
            return "Good";
        } else if (accuracyMeters < 20) {
            return "Fair";
        } else {
            return "Poor";
        }
    }
}



