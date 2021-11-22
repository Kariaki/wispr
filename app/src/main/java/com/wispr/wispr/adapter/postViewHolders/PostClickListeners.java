package com.wispr.wispr.adapter.postViewHolders;


import com.wispr.wispr.adapter.SuperClickListeners;

public abstract class PostClickListeners extends SuperClickListeners {

    public abstract void onClickComment(int position);

    public abstract void onOptionClick(int position);
    public abstract void onProfileClick(int position);


}
