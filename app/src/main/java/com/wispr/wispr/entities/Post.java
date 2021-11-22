package com.wispr.wispr.entities;


import com.wispr.wispr.adapter.SuperEntity;

public class Post extends SuperEntity {

    private long postTime;
    private String postCaption;
    private String postUrl;
    private String postUserID;
    private int type;

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Post(long postTime, String postCaption, String postUserID, int type, String postID) {
        this.postTime = postTime;
        this.postCaption = postCaption;
        this.postUrl = postUrl;
        this.postUserID = postUserID;
        this.type = type;
        this.postID = postID;
    }

    public Post(long postTime, String postCaption, String postUserID,String postUrl, int type, String postID) {
        this.postTime = postTime;
        this.postCaption = postCaption;
        this.postUrl = postUrl;
        this.postUserID = postUserID;
        this.type = type;
        this.postID = postID;
    }
    public Post(){}

    public String getPostID() {
        return postID;
    }

    private String postID;

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getPostCaption() {
        return postCaption;
    }

    public void setPostCaption(String postCaption) {
        this.postCaption = postCaption;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getPostUserID() {
        return postUserID;
    }

    public void setPostUserID(String postUserID) {
        this.postUserID = postUserID;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }
}
