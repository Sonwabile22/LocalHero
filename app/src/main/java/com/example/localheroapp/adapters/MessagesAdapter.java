package com.example.localheroapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localheroapp.R;
import com.example.localheroapp.models.Message;
import com.example.localheroapp.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messages;
    private User currentUser;

    public MessagesAdapter(User currentUser) {
        this.messages = new ArrayList<>();
        this.currentUser = currentUser;
    }

    public void updateMessages(List<Message> newMessages) {
        this.messages.clear();
        this.messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUser.getUserId())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        
        // Set message content
        holder.messageContent.setText(message.getText());
        
        // Format timestamp
        long timestamp = message.getCreatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = sdf.format(new Date(timestamp));
        holder.messageTime.setText(time);
        
        // Show sender name for received messages if available
        if (holder.senderName != null && message.getSenderName() != null) {
            holder.senderName.setText(message.getSenderName());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageContent;
        TextView messageTime;
        TextView senderName;
        CardView messageCard;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageContent = itemView.findViewById(R.id.messageContent);
            messageTime = itemView.findViewById(R.id.messageTime);
            senderName = itemView.findViewById(R.id.senderName);
            messageCard = itemView.findViewById(R.id.messageCard);
        }
    }
}
