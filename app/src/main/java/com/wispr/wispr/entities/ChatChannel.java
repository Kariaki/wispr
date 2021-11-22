package com.wispr.wispr.entities;

public class ChatChannel {

    private String chatChannelId;
    private long timeCreated;

    public ChatChannel(String chatChannelId, long timeCreated) {
        this.chatChannelId = chatChannelId;
        this.timeCreated = timeCreated;
    }

    public String getChatChannelId() {
        return chatChannelId;
    }

    public void setChatChannelId(String chatChannelId) {
        this.chatChannelId = chatChannelId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
    public ChatChannel(){}
}
