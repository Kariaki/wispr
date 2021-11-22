package com.wispr.wispr.modals;

import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

public class ProfilePostOption extends BottomSheetDialogFragment {


    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.profile_post_option, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }
}
