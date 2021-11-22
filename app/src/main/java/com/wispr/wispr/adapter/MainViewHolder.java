package com.wispr.wispr.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public  abstract class MainViewHolder extends RecyclerView.ViewHolder {

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindPostType(SuperEntity types, Context context,SuperClickListeners clickListeners);


}
