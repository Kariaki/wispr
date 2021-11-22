package com.wispr.wispr.modals;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wispr.wispr.mainPages.Settings;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

public class ProfileOption extends BottomSheetDialogFragment {

    public interface OptionSelection{
        void editProfile();
        void changeProfilePicture();
    }

    private OptionSelection optionSelection;

    public void setOptionSelection(OptionSelection optionSelection) {
        this.optionSelection = optionSelection;
    }

    private TextView settings, editProfile, changeProfilePicture;

    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.profile_option_bottomsheet, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        changeProfilePicture = contentView.findViewById(R.id.changeProfilePicture);
        settings = contentView.findViewById(R.id.settings);
        editProfile = contentView.findViewById(R.id.editProfile);
        changeProfilePicture.setOnClickListener(v->{
            optionSelection.changeProfilePicture();
            dismiss();
        });

        editProfile.setOnClickListener(v->{
            optionSelection.editProfile();
            dismiss();
        });
        settings.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), Settings.class));
        });
    }
}
