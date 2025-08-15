package com.example.localheroapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localheroapp.R;
import com.example.localheroapp.models.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private List<Chat> chats;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatsAdapter(OnChatClickListener listener) {
        this.chats = new ArrayList<>();
        this.listener = listener;
    }

    public void updateChats(List<Chat> newChats) {
        this.chats.clear();
        this.chats.addAll(newChats);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        
        // Set chat title
        holder.chatTitle.setText(chat.getTitle() != null ? chat.getTitle() : "Chat");
        
        // Set last message
        if (chat.getLastMessageText() != null && !chat.getLastMessageText().isEmpty()) {
            holder.lastMessage.setText(chat.getLastMessageText());
            holder.lastMessage.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessage.setText("No messages yet");
            holder.lastMessage.setVisibility(View.VISIBLE);
        }
        
        // Set time
        if (chat.getLastMessageTime() > 0) {
            holder.messageTime.setText(formatTime(chat.getLastMessageTime()));
            holder.messageTime.setVisibility(View.VISIBLE);
        } else {
            holder.messageTime.setVisibility(View.GONE);
        }
        
        // Set unread count
        if (chat.getUnreadCount() > 0) {
            holder.unreadCount.setText(String.valueOf(chat.getUnreadCount()));
            holder.unreadCount.setVisibility(View.VISIBLE);
        } else {
            holder.unreadCount.setVisibility(View.GONE);
        }
        
        // Set chat type indicator
        String chatTypeText = "";
        if (chat.isWardGroup()) {
            chatTypeText = "Ward Group";
        } else if (chat.isPrivateChat()) {
            chatTypeText = "Private";
        } else if (chat.isIssueChat()) {
            chatTypeText = "Issue Chat";
        }
        
        if (!chatTypeText.isEmpty()) {
            holder.chatType.setText(chatTypeText);
            holder.chatType.setVisibility(View.VISIBLE);
        } else {
            holder.chatType.setVisibility(View.GONE);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    private String formatTime(long timestamp) {
        Date date = new Date(timestamp);
        Date now = new Date();
        
        // Check if it's today
        SimpleDateFormat todayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (todayFormat.format(date).equals(todayFormat.format(now))) {
            // Show time if today
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return timeFormat.format(date);
        } else {
            // Show date if not today
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
            return dateFormat.format(date);
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatTitle;
        TextView lastMessage;
        TextView messageTime;
        TextView unreadCount;
        TextView chatType;
        CardView chatCard;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTitle = itemView.findViewById(R.id.chatTitle);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            messageTime = itemView.findViewById(R.id.messageTime);
            unreadCount = itemView.findViewById(R.id.unreadCount);
            chatType = itemView.findViewById(R.id.chatType);
            chatCard = itemView.findViewById(R.id.chatCard);
        }
    }
}
