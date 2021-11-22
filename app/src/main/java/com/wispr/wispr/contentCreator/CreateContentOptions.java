package com.wispr.wispr.contentCreator;

import android.app.Dialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

public class CreateContentOptions  extends BottomSheetDialogFragment {


    private RecyclerView optionList;
    private AppCompatCheckBox defaultCheckBox;
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.create_content_option, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        viewById(contentView);

    }

    private void viewById(View view){
        optionList=view.findViewById(R.id.optionList);
        defaultCheckBox=view.findViewById(R.id.defaultCheckBox);
    }

}
