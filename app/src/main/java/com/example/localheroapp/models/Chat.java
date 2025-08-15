package com.example.localheroapp.models;

import java.util.List;
import java.util.Map;

public class Chat {
    private String chatId;
    private String chatType; // WARD_GROUP, PRIVATE, ISSUE_CHAT
    private String title;
    private String description;
    private String wardId;
    private String wardName;
    private String municipalityId;
    private String municipalityName;
    private List<String> participantIds;
    private Map<String, String> participantNames; // userId -> displayName
    private Map<String, String> participantRoles; // userId -> role
    private String lastMessageId;
    private String lastMessageText;
    private String lastMessageSenderId;
    private long lastMessageTime;
    private int unreadCount;
    private boolean isActive;
    private long createdAt;
    private long updatedAt;

    public enum ChatType {
        WARD_GROUP,
        PRIVATE,
        ISSUE_CHAT
    }

    // Default constructor for Firestore
    public Chat() {}

    public Chat(String chatId, String chatType, String title, String description, 
                String wardId, String wardName, String municipalityId, String municipalityName) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.title = title;
        this.description = description;
        this.wardId = wardId;
        this.wardName = wardName;
        this.municipalityId = municipalityId;
        this.municipalityName = municipalityName;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public String getChatType() { return chatType; }
    public void setChatType(String chatType) { this.chatType = chatType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWardId() { return wardId; }
    public void setWardId(String wardId) { this.wardId = wardId; }

    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }

    public String getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(String municipalityId) { this.municipalityId = municipalityId; }

    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }

    public List<String> getParticipantIds() { return participantIds; }
    public void setParticipantIds(List<String> participantIds) { this.participantIds = participantIds; }

    public Map<String, String> getParticipantNames() { return participantNames; }
    public void setParticipantNames(Map<String, String> participantNames) { this.participantNames = participantNames; }

    public Map<String, String> getParticipantRoles() { return participantRoles; }
    public void setParticipantRoles(Map<String, String> participantRoles) { this.participantRoles = participantRoles; }

    public String getLastMessageId() { return lastMessageId; }
    public void setLastMessageId(String lastMessageId) { this.lastMessageId = lastMessageId; }

    public String getLastMessageText() { return lastMessageText; }
    public void setLastMessageText(String lastMessageText) { this.lastMessageText = lastMessageText; }

    public String getLastMessageSenderId() { return lastMessageSenderId; }
    public void setLastMessageSenderId(String lastMessageSenderId) { this.lastMessageSenderId = lastMessageSenderId; }

    public long getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(long lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    public boolean isWardGroup() {
        return ChatType.WARD_GROUP.name().equals(chatType);
    }

    public boolean isPrivateChat() {
        return ChatType.PRIVATE.name().equals(chatType);
    }

    public boolean isIssueChat() {
        return ChatType.ISSUE_CHAT.name().equals(chatType);
    }
}

