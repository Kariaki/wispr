package com.wispr.wispr.adapter.postViewHolders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;


public class EmptyViewHolder extends MainViewHolder {
    public EmptyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

    }
}
