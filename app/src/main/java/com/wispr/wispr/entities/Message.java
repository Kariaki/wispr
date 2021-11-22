package com.wispr.wispr.entities;

import com.wispr.wispr.adapter.SuperEntity;

public class Message extends SuperEntity {

    private String messageID;

    private long messageTime;
    private String messageOwnerId;
    private String messageContent;
    private int messageType;
    private int messageState;


    public Message(){

    }

    public Message(String messageID, long messageTime, String messageOwnerId, String messageContent, int messageType,int messageState) {
        this.messageState=messageState;
        this.messageID = messageID;
        this.messageTime = messageTime;
        this.messageOwnerId = messageOwnerId;
        this.messageContent = messageContent;
        this.messageType = messageType;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }

    public int getMessageState() {
        return messageState;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageOwnerId() {
        return messageOwnerId;
    }

    public void setMessageOwnerId(String messageOwnerId) {
        this.messageOwnerId = messageOwnerId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public int getType() {
        return messageType;
    }
}
