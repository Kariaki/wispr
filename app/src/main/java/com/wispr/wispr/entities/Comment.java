package com.wispr.wispr.entities;


import com.wispr.wispr.adapter.SuperEntity;

public class Comment extends SuperEntity {

    private long commentTime;
    private String commentContent;
    private String commentOwnerID;
    private String commentID;


    public Comment(){

    }
    public Comment(long commentTime, String commentContent, String commentOwnerID, String commentID,int type) {
        this.commentTime = commentTime;
        this.commentContent = commentContent;
        this.commentOwnerID = commentOwnerID;
        this.commentID = commentID;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentOwnerID() {
        return commentOwnerID;
    }

    public void setCommentOwnerID(String commentOwnerID) {
        this.commentOwnerID = commentOwnerID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
}
