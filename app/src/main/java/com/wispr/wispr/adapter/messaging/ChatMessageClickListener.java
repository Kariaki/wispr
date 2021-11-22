package com.wispr.wispr.adapter.messaging;

import com.wispr.wispr.adapter.SuperClickListeners;

public abstract class ChatMessageClickListener extends SuperClickListeners {

    public abstract void updateMessageState(int position);

}
