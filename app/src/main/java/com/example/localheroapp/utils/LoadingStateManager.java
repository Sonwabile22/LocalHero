package com.example.localheroapp.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Centralized loading state manager to handle progress bars and loading indicators
 * across the application with improved performance and user experience.
 */
public class LoadingStateManager {
    private static LoadingStateManager instance;
    private final Map<String, LoadingState> loadingStates = new ConcurrentHashMap<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    // Loading timeout configuration (30 seconds)
    private static final long LOADING_TIMEOUT_MS = 30000;
    
    private LoadingStateManager() {}
    
    public static synchronized LoadingStateManager getInstance() {
        if (instance == null) {
            instance = new LoadingStateManager();
        }
        return instance;
    }
    
    /**
     * Show loading state for a specific component
     */
    public void showLoading(String loadingId, ProgressBar progressBar, View... disableViews) {
        if (isLoading(loadingId)) {
            return; // Prevent multiple simultaneous loading operations
        }
        
        LoadingState state = new LoadingState(loadingId, progressBar, disableViews);
        loadingStates.put(loadingId, state);
        
        mainHandler.post(() -> {
            progressBar.setVisibility(View.VISIBLE);
            for (View view : disableViews) {
                view.setEnabled(false);
            }
        });
        
        // Set timeout to prevent infinite loading
        mainHandler.postDelayed(() -> hideLoading(loadingId), LOADING_TIMEOUT_MS);
    }
    
    /**
     * Hide loading state for a specific component
     */
    public void hideLoading(String loadingId) {
        LoadingState state = loadingStates.remove(loadingId);
        if (state != null) {
            mainHandler.post(() -> {
                state.progressBar.setVisibility(View.GONE);
                for (View view : state.disabledViews) {
                    view.setEnabled(true);
                }
            });
        }
    }
    
    /**
     * Check if a specific component is currently loading
     */
    public boolean isLoading(String loadingId) {
        return loadingStates.containsKey(loadingId);
    }
    
    /**
     * Manage SwipeRefreshLayout with debouncing
     */
    public void handleSwipeRefresh(String refreshId, SwipeRefreshLayout swipeRefresh, Runnable refreshAction) {
        if (isLoading(refreshId)) {
            swipeRefresh.setRefreshing(false);
            return; // Prevent multiple simultaneous refresh operations
        }
        
        loadingStates.put(refreshId, new LoadingState(refreshId, null));
        
        // Execute refresh action
        refreshAction.run();
        
        // Auto-hide refresh after timeout
        mainHandler.postDelayed(() -> {
            swipeRefresh.setRefreshing(false);
            loadingStates.remove(refreshId);
        }, 10000); // 10 second timeout for refresh operations
    }
    
    /**
     * Stop refresh loading manually
     */
    public void stopRefresh(String refreshId, SwipeRefreshLayout swipeRefresh) {
        loadingStates.remove(refreshId);
        mainHandler.post(() -> swipeRefresh.setRefreshing(false));
    }
    
    /**
     * Clear all loading states (useful for cleanup)
     */
    public void clearAllLoadingStates() {
        for (LoadingState state : loadingStates.values()) {
            if (state.progressBar != null) {
                mainHandler.post(() -> {
                    state.progressBar.setVisibility(View.GONE);
                    for (View view : state.disabledViews) {
                        view.setEnabled(true);
                    }
                });
            }
        }
        loadingStates.clear();
    }
    
    /**
     * Get loading statistics for performance monitoring
     */
    public Map<String, Long> getLoadingStats() {
        Map<String, Long> stats = new HashMap<>();
        for (Map.Entry<String, LoadingState> entry : loadingStates.entrySet()) {
            stats.put(entry.getKey(), System.currentTimeMillis() - entry.getValue().startTime);
        }
        return stats;
    }
    
    private static class LoadingState {
        final String id;
        final ProgressBar progressBar;
        final View[] disabledViews;
        final long startTime;
        
        LoadingState(String id, ProgressBar progressBar, View... disabledViews) {
            this.id = id;
            this.progressBar = progressBar;
            this.disabledViews = disabledViews != null ? disabledViews : new View[0];
            this.startTime = System.currentTimeMillis();
        }
    }
}
