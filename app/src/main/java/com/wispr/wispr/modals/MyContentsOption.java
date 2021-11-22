package com.wispr.wispr.modals;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

public class MyContentsOption extends BottomSheetDialogFragment {

    public interface OptionClickListener{
        void onClickDelete();
        void onClickCopy();

    }

    private OptionClickListener OnOptionClickListener;


    public void setOnOptionClickListener(OptionClickListener onOptionClickListener) {
        OnOptionClickListener = onOptionClickListener;
    }

    private TextView deletePost,copyPost;
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.content_option, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));


        deletePost=contentView.findViewById(R.id.deletePost);
        copyPost=contentView.findViewById(R.id.copyPost);
        deletePost.setOnClickListener(v->{
            OnOptionClickListener.onClickDelete();
        });

        copyPost.setOnClickListener(v->{
            OnOptionClickListener.onClickCopy();
        });
    }
}
