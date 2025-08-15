package com.example.localheroapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.models.User;

public class ChatsFragment extends Fragment {
    private RecyclerView chatsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noChatsText;
    
    private MainActivity mainActivity;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            currentUser = mainActivity.getCurrentUser();
        }
        
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadChats();
    }

    private void initViews(View view) {
        chatsRecyclerView = view.findViewById(R.id.chatsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        noChatsText = view.findViewById(R.id.noChatsText);
    }

    private void setupRecyclerView() {
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: Set adapter for chats
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
    }

    private void loadChats() {
        if (currentUser != null) {
            // TODO: Load chats based on user role and ward
            // This would involve querying Firestore for chat conversations
            showNoChatsMessage();
        }
    }

    private void showNoChatsMessage() {
        noChatsText.setVisibility(View.VISIBLE);
        chatsRecyclerView.setVisibility(View.GONE);
    }

    private void showChatsList() {
        noChatsText.setVisibility(View.GONE);
        chatsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void refreshData() {
        loadChats();
        swipeRefreshLayout.setRefreshing(false);
    }
}



