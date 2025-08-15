package com.example.localheroapp.utils;

import android.util.Log;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Performance monitoring utility to track loading operations
 * and identify performance bottlenecks in the application.
 */
public class PerformanceMonitor {
    private static final String TAG = "PerformanceMonitor";
    private static PerformanceMonitor instance;
    private final Map<String, OperationMetrics> operationMetrics = new ConcurrentHashMap<>();
    
    // Performance thresholds
    private static final long SLOW_OPERATION_THRESHOLD_MS = 3000; // 3 seconds
    private static final long VERY_SLOW_OPERATION_THRESHOLD_MS = 10000; // 10 seconds
    
    private PerformanceMonitor() {}
    
    public static synchronized PerformanceMonitor getInstance() {
        if (instance == null) {
            instance = new PerformanceMonitor();
        }
        return instance;
    }
    
    /**
     * Start tracking an operation
     */
    public void startOperation(String operationId, String description) {
        OperationMetrics metrics = new OperationMetrics(operationId, description);
        operationMetrics.put(operationId, metrics);
        
        Log.d(TAG, "Started operation: " + operationId + " - " + description);
    }
    
    /**
     * End tracking an operation and log performance metrics
     */
    public void endOperation(String operationId) {
        OperationMetrics metrics = operationMetrics.remove(operationId);
        if (metrics != null) {
            long duration = System.currentTimeMillis() - metrics.startTime;
            
            // Log based on duration
            if (duration > VERY_SLOW_OPERATION_THRESHOLD_MS) {
                Log.w(TAG, "VERY SLOW operation completed: " + operationId + 
                    " took " + duration + "ms - " + metrics.description);
            } else if (duration > SLOW_OPERATION_THRESHOLD_MS) {
                Log.w(TAG, "Slow operation completed: " + operationId + 
                    " took " + duration + "ms - " + metrics.description);
            } else {
                Log.d(TAG, "Operation completed: " + operationId + 
                    " took " + duration + "ms - " + metrics.description);
            }
            
            // Update statistics
            updateStatistics(operationId, duration);
        }
    }
    
    /**
     * Mark an operation as failed
     */
    public void markOperationFailed(String operationId, String errorMessage) {
        OperationMetrics metrics = operationMetrics.remove(operationId);
        if (metrics != null) {
            long duration = System.currentTimeMillis() - metrics.startTime;
            Log.e(TAG, "Operation FAILED: " + operationId + 
                " after " + duration + "ms - " + errorMessage);
        }
    }
    
    /**
     * Track Firebase operation performance
     */
    public void trackFirebaseOperation(String operation, long duration) {
        if (duration > SLOW_OPERATION_THRESHOLD_MS) {
            Log.w(TAG, "Slow Firebase operation: " + operation + " took " + duration + "ms");
        }
        updateStatistics("firebase_" + operation, duration);
    }
    
    /**
     * Track UI loading operation
     */
    public void trackUIOperation(String operation, long duration) {
        if (duration > 1000) { // UI operations should be < 1 second
            Log.w(TAG, "Slow UI operation: " + operation + " took " + duration + "ms");
        }
        updateStatistics("ui_" + operation, duration);
    }
    
    /**
     * Get performance report for debugging
     */
    public String getPerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Performance Report ===\n");
        
        for (Map.Entry<String, Statistics> entry : statistics.entrySet()) {
            Statistics stats = entry.getValue();
            report.append(String.format("%s: count=%d, avg=%.2fms, min=%dms, max=%dms\n",
                entry.getKey(), stats.count, stats.averageDuration, 
                stats.minDuration, stats.maxDuration));
        }
        
        return report.toString();
    }
    
    /**
     * Log current performance statistics
     */
    public void logPerformanceReport() {
        Log.i(TAG, getPerformanceReport());
    }
    
    /**
     * Clear all statistics (useful for testing)
     */
    public void clearStatistics() {
        statistics.clear();
        operationMetrics.clear();
        Log.d(TAG, "Performance statistics cleared");
    }
    
    // Statistics tracking
    private final Map<String, Statistics> statistics = new ConcurrentHashMap<>();
    
    private void updateStatistics(String operation, long duration) {
        statistics.compute(operation, (key, stats) -> {
            if (stats == null) {
                return new Statistics(duration);
            } else {
                stats.update(duration);
                return stats;
            }
        });
    }
    
    private static class OperationMetrics {
        final String operationId;
        final String description;
        final long startTime;
        
        OperationMetrics(String operationId, String description) {
            this.operationId = operationId;
            this.description = description;
            this.startTime = System.currentTimeMillis();
        }
    }
    
    private static class Statistics {
        long count = 1;
        long totalDuration;
        long minDuration;
        long maxDuration;
        double averageDuration;
        
        Statistics(long initialDuration) {
            this.totalDuration = initialDuration;
            this.minDuration = initialDuration;
            this.maxDuration = initialDuration;
            this.averageDuration = initialDuration;
        }
        
        void update(long duration) {
            count++;
            totalDuration += duration;
            minDuration = Math.min(minDuration, duration);
            maxDuration = Math.max(maxDuration, duration);
            averageDuration = (double) totalDuration / count;
        }
    }
}
