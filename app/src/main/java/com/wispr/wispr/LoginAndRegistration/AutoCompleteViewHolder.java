package com.wispr.wispr.LoginAndRegistration;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

public class AutoCompleteViewHolder extends MainViewHolder {
    private TextView autoCompleteText;
    public AutoCompleteViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        autoCompleteText=itemView.findViewById(R.id.autoCompleteText);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        AutoCompleteEntity entity=(AutoCompleteEntity)types;
        if(entity!=null){
            autoCompleteText.setText(entity.getCompleteText());
        }
    }
}
