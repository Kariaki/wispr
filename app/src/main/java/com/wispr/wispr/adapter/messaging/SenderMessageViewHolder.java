package com.wispr.wispr.adapter.messaging;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Message;
import com.wispr.wispr.entities.MessageState;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;
import com.wispr.wispr.util.TimeFactor;

import de.hdodenhof.circleimageview.CircleImageView;

public class SenderMessageViewHolder extends MainViewHolder {
    private CircleImageView profileImage;
    private TextView timeText,senderName,messageContent;
    public SenderMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage=itemView.findViewById(R.id.senderImage);
        timeText=itemView.findViewById(R.id.messageTime);
        messageContent=itemView.findViewById(R.id.messageText);
        senderName=itemView.findViewById(R.id.senderName);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        ChatMessageClickListener chatMessageClickListener=(ChatMessageClickListener)clickListeners;
        Message message=(Message)types;
        messageContent.setText(message.getMessageContent());
        int messageState=message.getMessageState();

        chatMessageClickListener.updateMessageState(getAdapterPosition());
        LibraryFunction.fetchUserDetail(message.getMessageOwnerId(),senderName,null,null,profileImage,context);
        String time = TimeFactor.getDetailedDate(message.getMessageTime(), System.currentTimeMillis());
        timeText.setText(time);
        switch (messageState){
            case MessageState
                    .SENT:

                break;
            case MessageState.SEEN:
                break;



        }
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                chatMessageClickListener.onClickItem(getAdapterPosition());
                return true;
            }
        });

    }
}
