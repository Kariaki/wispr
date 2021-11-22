package com.wispr.wispr.adapter.chatViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.messaging.ChatItemClickListener;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.ChatItem;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListItemViewHolder extends MainViewHolder {
    CircleImageView chatIcon;
    TextView chatName, lastMessage;
    private RelativeLayout namePlaceHolder;
    private TextView namePlaceHolderText;

    public ChatListItemViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        chatIcon = itemView.findViewById(R.id.chatIcon);
        lastMessage = itemView.findViewById(R.id.lastMessage);
        chatName = itemView.findViewById(R.id.chatName);
        namePlaceHolderText = itemView.findViewById(R.id.namePlaceHolderText);
        namePlaceHolder = itemView.findViewById(R.id.namePlaceHolder);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {
        ChatItem chatItem = (ChatItem) types;

        int[]coloredList={R.drawable.place_holder_background,R.drawable.place_holder_second_background,R.drawable.place_holder_third_background};

        Random random=new Random();

        ChatItemClickListener clickListener=(ChatItemClickListener)clickListeners;
        chatName.setText(chatItem.getChatName());
        //lastMessage.setText(chatItem.getLastMessageText());

        if (chatItem.getChatIcon().isEmpty()) {

            int randomSelected=coloredList[random.nextInt(coloredList.length)];
            namePlaceHolder.setBackground(context.getResources().getDrawable(randomSelected));
            char[] chatNameChar = chatItem.getChatName().toCharArray();
            namePlaceHolder.setVisibility(View.VISIBLE);
            chatIcon.setVisibility(View.INVISIBLE);
            namePlaceHolderText.setText(String.valueOf(chatNameChar[0]));
        } else {
            Glide.with(context).load(chatItem.getChatIcon()).into(chatIcon);
        }

        itemView.setOnClickListener(v->{
            clickListener.onClickItem(getAdapterPosition());
        });
        lastMessage.setText(chatItem.getLastMessageText());

    }
}
