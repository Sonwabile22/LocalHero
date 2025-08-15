package com.example.localheroapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.models.User;
import com.google.android.material.button.MaterialButton;

public class MapFragment extends Fragment {
    private TextView mapPlaceholderText;
    private MaterialButton backToHomeButton;
    
    private MainActivity mainActivity;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            currentUser = mainActivity.getCurrentUser();
        }
        
        initViews(view);
        setupClickListeners();
        loadMapData();
    }

    private void initViews(View view) {
        mapPlaceholderText = view.findViewById(R.id.mapPlaceholderText);
        backToHomeButton = view.findViewById(R.id.backToHomeButton);
    }

    private void setupClickListeners() {
        backToHomeButton.setOnClickListener(v -> {
            // Navigate back to home
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    private void loadMapData() {
        if (currentUser != null) {
            String mapInfo = "Map View for " + currentUser.getWardName() + 
                           "\nMunicipality: " + currentUser.getMunicipalityName() +
                           "\n\nThis would show a map with issues marked by location.";
            mapPlaceholderText.setText(mapInfo);
        } else {
            mapPlaceholderText.setText("Map view is currently under development.\n\nThis will show:\n- Issue locations\n- Ward boundaries\n- Community facilities\n- Interactive markers");
        }
        
        Toast.makeText(getContext(), "Map view opened successfully!", Toast.LENGTH_SHORT).show();
    }
}
