package com.wispr.wispr.entities;

public class Categories {

    String name;
    String chatChannelId;
    String postChannelId;

    public String getName() {
        return name;
    }

    public Categories(String name, String chatChannelId, String postChannelId) {
        this.name = name;
        this.chatChannelId = chatChannelId;
        this.postChannelId=postChannelId;
    }

    public void setPostChannelId(String postChannelId) {
        this.postChannelId = postChannelId;
    }

    public String getPostChannelId() {
        return postChannelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatChannelId() {
        return chatChannelId;
    }

    public void setChatChannelId(String chatChannelId) {
        this.chatChannelId = chatChannelId;
    }
    public Categories(){}
}
