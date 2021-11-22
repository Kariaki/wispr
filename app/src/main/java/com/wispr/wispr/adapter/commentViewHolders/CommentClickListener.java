package com.wispr.wispr.adapter.commentViewHolders;


import com.wispr.wispr.adapter.SuperClickListeners;

public abstract class CommentClickListener extends SuperClickListeners {

    public abstract void onUserClick(int position);

    public abstract void onReplyClick(int position);
}
