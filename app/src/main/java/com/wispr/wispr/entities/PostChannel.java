package com.wispr.wispr.entities;

public class PostChannel {

    private String channelName;
    private String channelTag;
    private String tagId;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelTag() {
        return channelTag;
    }

    public void setChannelTag(String channelTag) {
        this.channelTag = channelTag;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }


    public PostChannel(String channelName, String channelTag, String tagId) {
        this.channelName = channelName;
        this.channelTag = channelTag;
        this.tagId = tagId;
    }
}
