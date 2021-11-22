package com.wispr.wispr.modals;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wispr.wispr.R;


public class DialogBox extends BottomSheetDialogFragment {

    TextView choiceDialogMessage,dialogCancelProcess
            ,choiceDialogConfirmProcess;
    private String tittle,message;
    LinearLayout rootView;

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public interface dialogButtons{
        void onClickPositiveButton();
        void onClickNegativeButton();
    }

    private dialogButtons listeners;

    public void setListeners(dialogButtons listeners) {
        this.listeners = listeners;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        View contentView=View.inflate(getContext(), R.layout.dialog_box,null);
        dialog.setContentView(contentView);
        ( (View)contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        rootView=contentView.findViewById(R.id.rootView);
        choiceDialogMessage=contentView.findViewById(R.id.choiceDialogMessage);
        dialogCancelProcess=contentView.findViewById(R.id.dialogCancelProcess);
        choiceDialogConfirmProcess=contentView.findViewById(R.id.choiceDialogConfirmProcess);
         choiceDialogMessage.setText(message);

        dialogCancelProcess.setOnClickListener(v->{
            listeners.onClickNegativeButton();
        });
        choiceDialogConfirmProcess.setOnClickListener(v->{
            listeners.onClickPositiveButton();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }



}
