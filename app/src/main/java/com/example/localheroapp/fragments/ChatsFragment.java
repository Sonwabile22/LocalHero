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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.adapters.ChatsAdapter;
import com.example.localheroapp.models.Chat;
import com.example.localheroapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

// Import LoadingStateManager for improved performance
import com.example.localheroapp.utils.LoadingStateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatsFragment extends Fragment implements ChatsAdapter.OnChatClickListener {
    private RecyclerView chatsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView noChatsText;
    
    private MainActivity mainActivity;
    private User currentUser;
    private FirebaseFirestore db;
    private ChatsAdapter chatsAdapter;
    private ListenerRegistration chatsListener;
    private LoadingStateManager loadingManager;
    private long lastRefreshTime = 0;
    private static final long REFRESH_COOLDOWN_MS = 2000; // 2 second cooldown between refreshes

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
        
        // Initialize Firestore and LoadingStateManager
        db = FirebaseFirestore.getInstance();
        loadingManager = LoadingStateManager.getInstance();
        
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
        chatsAdapter = new ChatsAdapter(this);
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Implement debouncing to prevent excessive refresh requests
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRefreshTime < REFRESH_COOLDOWN_MS) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Please wait before refreshing again", Toast.LENGTH_SHORT).show();
                return;
            }
            lastRefreshTime = currentTime;
            
            loadingManager.handleSwipeRefresh("chats_refresh", swipeRefreshLayout, this::refreshData);
        });
        
        // Set refresh colors for better UX
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        );
    }

    private void loadChats() {
        if (currentUser == null) return;
        
        // Query chats where the current user is a participant
        chatsListener = db.collection("chats")
                .whereArrayContains("participantIds", currentUser.getUserId())
                .whereEqualTo("isActive", true)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Error loading chats: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    if (queryDocumentSnapshots != null) {
                        List<Chat> chats = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Chat chat = convertDocumentToChat(doc);
                            if (chat != null) {
                                chats.add(chat);
                            }
                        }
                        updateUI(chats);
                        // Stop refresh loading if active
                        loadingManager.stopRefresh("chats_refresh", swipeRefreshLayout);
                    }
                });
    }
    
    private Chat convertDocumentToChat(QueryDocumentSnapshot doc) {
        try {
            Chat chat = new Chat();
            chat.setChatId(doc.getId());
            chat.setChatType(doc.getString("chatType"));
            chat.setTitle(doc.getString("title"));
            chat.setDescription(doc.getString("description"));
            chat.setWardId(doc.getString("wardId"));
            chat.setWardName(doc.getString("wardName"));
            chat.setMunicipalityId(doc.getString("municipalityId"));
            chat.setMunicipalityName(doc.getString("municipalityName"));
            chat.setLastMessageText(doc.getString("lastMessageText"));
            chat.setLastMessageSenderId(doc.getString("lastMessageSenderId"));
            
            Long lastMessageTime = doc.getLong("lastMessageTime");
            if (lastMessageTime != null) {
                chat.setLastMessageTime(lastMessageTime);
            }
            
            Long unreadCount = doc.getLong("unreadCount");
            if (unreadCount != null) {
                chat.setUnreadCount(unreadCount.intValue());
            }
            
            Boolean isActive = doc.getBoolean("isActive");
            if (isActive != null) {
                chat.setActive(isActive);
            }
            
            return chat;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void updateUI(List<Chat> chats) {
        if (chats.isEmpty()) {
            showNoChatsMessage();
        } else {
            showChatsList();
            chatsAdapter.updateChats(chats);
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
        // Only refresh if not already loading
        if (!loadingManager.isLoading("chats_refresh")) {
            loadChats();
        }
        // Note: swipeRefreshLayout.setRefreshing(false) is now handled by LoadingStateManager
    }
    
    @Override
    public void onChatClick(Chat chat) {
        // Navigate to individual chat
        ChatFragment chatFragment = ChatFragment.newInstance(chat.getChatId(), chat.getTitle());
        
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, chatFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
    // Method called from MainActivity when FAB is pressed in Chats fragment
    public void createNewChat() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // For now, create a simple private chat with sample data
        // In a real app, you'd show a dialog to select participants
        String chatId = UUID.randomUUID().toString();
        String chatTitle = "New Chat";
        
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("chatId", chatId);
        chatData.put("chatType", Chat.ChatType.PRIVATE.name());
        chatData.put("title", chatTitle);
        chatData.put("description", "Private conversation");
        chatData.put("wardId", currentUser.getWardId());
        chatData.put("wardName", currentUser.getWardName());
        chatData.put("municipalityId", currentUser.getMunicipalityId());
        chatData.put("municipalityName", currentUser.getMunicipalityName());
        
        // Add current user as participant
        List<String> participants = new ArrayList<>();
        participants.add(currentUser.getUserId());
        chatData.put("participantIds", participants);
        
        Map<String, String> participantNames = new HashMap<>();
        participantNames.put(currentUser.getUserId(), currentUser.getFullName());
        chatData.put("participantNames", participantNames);
        
        Map<String, String> participantRoles = new HashMap<>();
        participantRoles.put(currentUser.getUserId(), currentUser.getUserRole().name());
        chatData.put("participantRoles", participantRoles);
        
        chatData.put("isActive", true);
        chatData.put("createdAt", System.currentTimeMillis());
        chatData.put("updatedAt", System.currentTimeMillis());
        
        // Create chat in Firestore
        db.collection("chats").document(chatId)
                .set(chatData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "New chat created!", Toast.LENGTH_SHORT).show();
                    // Open the new chat
                    ChatFragment chatFragment = ChatFragment.newInstance(chatId, chatTitle);
                    
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, chatFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to create chat: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (chatsListener != null) {
            chatsListener.remove();
        }
        // Clean up loading states
        if (loadingManager != null) {
            loadingManager.stopRefresh("chats_refresh", swipeRefreshLayout);
        }
    }
}



