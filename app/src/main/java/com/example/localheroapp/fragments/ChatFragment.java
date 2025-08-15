package com.example.localheroapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localheroapp.MainActivity;
import com.example.localheroapp.R;
import com.example.localheroapp.adapters.MessagesAdapter;
import com.example.localheroapp.models.Message;
import com.example.localheroapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatFragment extends Fragment {
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private MaterialButton sendButton;
    private TextView chatTitleText;
    private TextView noMessagesText;
    
    private MainActivity mainActivity;
    private User currentUser;
    private FirebaseFirestore db;
    private MessagesAdapter messagesAdapter;
    private ListenerRegistration messagesListener;
    
    private String chatId;
    private String chatTitle;
    
    public static ChatFragment newInstance(String chatId, String chatTitle) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("chatId", chatId);
        args.putString("chatTitle", chatTitle);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatId = getArguments().getString("chatId");
            chatTitle = getArguments().getString("chatTitle");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            currentUser = mainActivity.getCurrentUser();
        }
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        
        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadMessages();
    }

    private void initViews(View view) {
        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        chatTitleText = view.findViewById(R.id.chatTitleText);
        noMessagesText = view.findViewById(R.id.noMessagesText);
        
        if (chatTitle != null) {
            chatTitleText.setText(chatTitle);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true); // Show latest messages at bottom
        messagesRecyclerView.setLayoutManager(layoutManager);
        
        messagesAdapter = new MessagesAdapter(currentUser);
        messagesRecyclerView.setAdapter(messagesAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());
        
        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void loadMessages() {
        if (chatId == null) return;
        
        // Listen for real-time message updates
        messagesListener = db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        android.widget.Toast.makeText(getContext(), 
                            "Error loading messages: " + e.getMessage(), 
                            android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    if (queryDocumentSnapshots != null) {
                        List<Message> messages = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Message message = convertDocumentToMessage(doc);
                            if (message != null) {
                                messages.add(message);
                            }
                        }
                        updateUI(messages);
                    }
                });
    }
    
    private Message convertDocumentToMessage(QueryDocumentSnapshot doc) {
        try {
            Message message = new Message();
            message.setMessageId(doc.getId());
            message.setChatId(doc.getString("chatId"));
            message.setSenderId(doc.getString("senderId"));
            message.setSenderName(doc.getString("senderName"));
            message.setText(doc.getString("content"));
            message.setMessageType(doc.getString("messageType"));
            
            Long timestamp = doc.getLong("timestamp");
            if (timestamp != null) {
                message.setCreatedAt(timestamp);
            }
            
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty() || currentUser == null || chatId == null) {
            return;
        }
        
        // Create message object
        String messageId = UUID.randomUUID().toString();
        Map<String, Object> message = new HashMap<>();
        message.put("messageId", messageId);
        message.put("chatId", chatId);
        message.put("senderId", currentUser.getUserId());
        message.put("senderName", currentUser.getFullName());
        message.put("content", messageText);
        message.put("messageType", "TEXT");
        message.put("timestamp", System.currentTimeMillis());
        
        // Send message to Firestore
        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .document(messageId)
                .set(message)
                .addOnSuccessListener(aVoid -> {
                    messageEditText.setText("");
                    // Update chat's last message info
                    updateChatLastMessage(messageText);
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(getContext(), 
                        "Failed to send message: " + e.getMessage(), 
                        android.widget.Toast.LENGTH_SHORT).show();
                });
    }
    
    private void updateChatLastMessage(String lastMessage) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastMessage", lastMessage);
        updates.put("lastMessageSender", currentUser.getFullName());
        updates.put("lastMessageTime", System.currentTimeMillis());
        updates.put("updatedAt", System.currentTimeMillis());
        
        db.collection("chats").document(chatId)
                .update(updates)
                .addOnFailureListener(e -> {
                    // Don't show error to user as this is a background operation
                    System.err.println("Failed to update chat last message: " + e.getMessage());
                });
    }
    
    private void updateUI(List<Message> messages) {
        if (messages.isEmpty()) {
            showNoMessagesMessage();
        } else {
            showMessagesList();
            messagesAdapter.updateMessages(messages);
            // Scroll to the latest message
            messagesRecyclerView.scrollToPosition(messages.size() - 1);
        }
    }
    
    private void showNoMessagesMessage() {
        noMessagesText.setVisibility(View.VISIBLE);
        messagesRecyclerView.setVisibility(View.GONE);
    }
    
    private void showMessagesList() {
        noMessagesText.setVisibility(View.GONE);
        messagesRecyclerView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (messagesListener != null) {
            messagesListener.remove();
        }
    }
}
