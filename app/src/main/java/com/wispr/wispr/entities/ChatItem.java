package com.wispr.wispr.entities;

import com.wispr.wispr.adapter.SuperEntity;

public class ChatItem  extends SuperEntity {

    long lastMessageTime;
    String chatChannelID;
    String chatID;
    String chatIcon;
    String chatName;
    String lastMessageText;
    int newMessageCount;

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getChatChannelID() {
        return chatChannelID;
    }

    public void setChatChannelID(String chatChannelID) {
        this.chatChannelID = chatChannelID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public ChatItem(){}
    public ChatItem( String chatName,String chatChannelID, String chatID, int chatType) {

        this.chatChannelID = chatChannelID;
        this.chatName=chatName;
        this.chatID = chatID;
        this.chatType = chatType;
    }

    public ChatItem(long lastMessageTime, String chatChannelID, String chatID, String chatIcon, String chatName, String lastMessageText, int newMessageCount, int chatType) {
        this.lastMessageTime = lastMessageTime;
        this.chatChannelID = chatChannelID;
        this.chatID = chatID;
        this.chatIcon = chatIcon;
        this.chatName = chatName;
        this.lastMessageText = lastMessageText;
        this.newMessageCount = newMessageCount;
        this.chatType = chatType;
    }

    public String getChatIcon() {
        return chatIcon;
    }

    public void setChatIcon(String chatIcon) {
        this.chatIcon = chatIcon;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public int getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(int newMessageCount) {
        this.newMessageCount = newMessageCount;
    }

    int chatType;

}
