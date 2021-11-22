package com.wispr.wispr.mainPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.adapter.GeneralAdapter;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.messaging.ChatMessageClickListener;
import com.wispr.wispr.adapter.messaging.ReceiverMessageViewHolder;
import com.wispr.wispr.adapter.messaging.SenderMessageViewHolder;
import com.wispr.wispr.adapter.postViewHolders.EmptyViewHolder;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.Message;
import com.wispr.wispr.entities.MessageState;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatPage extends AppCompatActivity {

    private TextView chatPageUerName;
    private RecyclerView chatMessagesContainer;
    private EditText write_chat;
    private FloatingActionButton sendButton;
    private GeneralAdapter generalAdapter;
    private Context context;
    private String chatId;
    private String chatChannelId;
    private List<SuperEntity> chatMessages = new ArrayList<>();
    private DatabaseReference chatChannelMessages = FirebaseDatabase.getInstance().getReference("ChatChannels");

    private String userId;
    private int chatType;
    private String chatName;
    private LinearLayout loader;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        context = ChatPage.this;

        userId = User.getUserID(this);
        Intent insideIntent = getIntent();
        chatId = insideIntent.getStringExtra("chat id");
        chatName = insideIntent.getStringExtra("chat name");
        chatChannelId = insideIntent.getStringExtra("chat channel id");
        chatChannelMessages = chatChannelMessages.child(chatChannelId).child("messages"); //channel messages
        chatType = insideIntent.getIntExtra("chat type", ItemTypes.DIRECT_MESSAGE);

        chatPageUerName = findViewById(R.id.chatPageUerName);
        chatMessagesContainer = findViewById(R.id.chatMessagesContainer);
        write_chat = findViewById(R.id.write_chat);
        loader = findViewById(R.id.loader);
        sendButton = findViewById(R.id.sendButton);


        //general adapter
        generalAdapter = new GeneralAdapter();

        generalAdapter.setItems(chatMessages);
        generalAdapter.setViewHolderPlug(viewHolderPlug);
        generalAdapter.setClickListeners(superClickListeners);

        //layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        chatMessagesContainer.setLayoutManager(linearLayoutManager); //message container layout manager
        chatMessagesContainer.setHasFixedSize(true);

        chatMessagesContainer.setAdapter(generalAdapter);

        chatPageUerName.setText(chatName);
        //text watcher

        write_chat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
        });


        loadMessages();
        // fetchNewMessages();


    }

    public void backPress(View view) {
        onBackPressed();
    }

    private GeneralAdapter.viewHolderPlug viewHolderPlug = new GeneralAdapter.viewHolderPlug() {
        @Override
        public MainViewHolder setPlug(ViewGroup group, int viewType) {
            switch (viewType) {
                case ItemTypes.SENT_MESSAGE:
                    View view = LayoutInflater.from(context).inflate(R.layout.sender_text_chat_design, group, false);

                    return new SenderMessageViewHolder(view);

                case ItemTypes.RECEIVED_MESSAGE:
                    view = LayoutInflater.from(context).inflate(R.layout.receiver_text_chat_design, group, false);
                    return new ReceiverMessageViewHolder(view);
                default:
                    view = LayoutInflater.from(context).inflate(R.layout.empty_view_holder, group, false);
                    return new EmptyViewHolder(view);
            }


        }
    };

    ChatMessageClickListener superClickListeners = new ChatMessageClickListener() {
        @Override
        public void updateMessageState(int position) {
            Message currentMessage = (Message) chatMessages.get(position);
            String messageId = currentMessage.getMessageID();
            int messageState = currentMessage.getMessageState();
            if (!messageId.equals(userId)) {
                if (messageState != MessageState.SEEN) {
                    DatabaseReference thisMessage = chatChannelMessages.child(messageId).child("messageState");
                    thisMessage.setValue(MessageState.SEEN);
                }
            }
        }

        @Override
        public void onClickItem(int position) {

        }
    };

    public void sendMessage(View view) {
        String writtenMessage = write_chat.getText().toString();
        if (!writtenMessage.isEmpty()) {
            DatabaseReference newMessage = chatChannelMessages.push();
            Message message = new Message(newMessage.getKey(), System.currentTimeMillis(), userId, writtenMessage, ItemTypes.MESSAGE_TEXT, MessageState.SENT);


            newMessage.setValue(message);
            message.setMessageType(ItemTypes.SENT_MESSAGE);
            chatMessages.add(message);
            messageIds.add(message.getMessageID());
            generalAdapter.notifyDataSetChanged();
            linearLayoutManager.scrollToPosition(generalAdapter.getItemCount());
            chatMessagesContainer.smoothScrollToPosition(generalAdapter.getItemCount());

            write_chat.getText().clear();
            sendButton.setEnabled(false);
        }


    }

    List<String> messageIds = new ArrayList<>();

    private void loadMessages() {
        chatChannelMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Message thisMessage = dataSnapshot.getValue(Message.class);
                        if (!messageIds.contains(thisMessage.getMessageID())) {


                            if ( !thisMessage.getMessageOwnerId().equals(User.getUserID(ChatPage.this))) {
                                thisMessage.setMessageType(ItemTypes.RECEIVED_MESSAGE);
                            } else {
                                thisMessage.setMessageType(ItemTypes.SENT_MESSAGE);
                            }
                            chatMessages.add(thisMessage);
                            messageIds.add(thisMessage.getMessageID());
                            generalAdapter.notifyDataSetChanged();
                        }
                        loader.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void fetchNewMessages() {
        chatChannelMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Message newMessage = snapshot.getValue(Message.class);
                    if (!messageIds.contains(newMessage.getMessageID())) {
                        messageIds.add(newMessage.getMessageID());
                        chatMessages.add(newMessage);
                        generalAdapter.notifyItemChanged(chatMessages.size());

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Message newMessage = snapshot.getValue(Message.class);
                    if (!messageIds.contains(newMessage.getMessageID())) {
                        messageIds.add(newMessage.getMessageID());
                        chatMessages.remove(newMessage);
                        generalAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}