package com.wispr.wispr.adapter.messaging;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Message;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;
import com.wispr.wispr.util.TimeFactor;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiverMessageViewHolder extends MainViewHolder {
    CircleImageView profileImage;
    private TextView timeText, senderName, messageContent;

    public ReceiverMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.senderImage);
        timeText = itemView.findViewById(R.id.messageTime);
        messageContent = itemView.findViewById(R.id.messageText);
        senderName = itemView.findViewById(R.id.senderName);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        Message message = (Message) types;
        int messageState=message.getMessageState();

        messageContent.setText(message.getMessageContent());

        LibraryFunction.fetchUserDetail(message.getMessageOwnerId(), senderName, null, null, profileImage, context);

        String time = TimeFactor.getDetailedDate(message.getMessageTime(), System.currentTimeMillis());
        timeText.setText(time);


    }
}
