package com.example.localheroapp.models;

import java.util.List;
import java.util.Map;

public class Message {
    private String messageId;
    private String chatId;
    private String senderId;
    private String senderName;
    private String senderRole;
    private String messageType;
    private String text;
    private List<String> imageUrls;
    private String replyToMessageId;
    private String replyToText;
    private Map<String, Object> metadata;
    private boolean isRead;
    private List<String> readBy;
    private boolean isOffline;
    private long createdAt;
    private long updatedAt;

    public enum MessageType {
        TEXT,
        IMAGE,
        LOCATION,
        ISSUE_UPDATE,
        SYSTEM_MESSAGE
    }

    // Default constructor for Firestore
    public Message() {}

    public Message(String messageId, String chatId, String senderId, String senderName, 
                  String senderRole, String messageType, String text) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderRole = senderRole;
        this.messageType = messageType;
        this.text = text;
        this.isRead = false;
        this.isOffline = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public String getReplyToMessageId() { return replyToMessageId; }
    public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }

    public String getReplyToText() { return replyToText; }
    public void setReplyToText(String replyToText) { this.replyToText = replyToText; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public List<String> getReadBy() { return readBy; }
    public void setReadBy(List<String> readBy) { this.readBy = readBy; }

    public boolean isOffline() { return isOffline; }
    public void setOffline(boolean offline) { isOffline = offline; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isTextMessage() {
        return MessageType.TEXT.name().equals(messageType);
    }

    public boolean isImageMessage() {
        return MessageType.IMAGE.name().equals(messageType);
    }

    public boolean isLocationMessage() {
        return MessageType.LOCATION.name().equals(messageType);
    }

    public boolean isReply() {
        return replyToMessageId != null && !replyToMessageId.isEmpty();
    }
}

