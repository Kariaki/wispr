package com.wispr.wispr.mainPages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.adapter.chatViewHolder.ChatListItemViewHolder;
import com.wispr.wispr.adapter.GeneralAdapter;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.messaging.ChatItemClickListener;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.ChatItem;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.Message;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagingPage extends Fragment {

    public MessagingPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String userID;
    DatabaseReference allUsers = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference chatChannels = FirebaseDatabase.getInstance().getReference("ChatChannels");

    DatabaseReference myChats;
    private LinearLayout loader;
    private RecyclerView chatListItems;
    private GeneralAdapter generalAdapter;
    private List<SuperEntity> chatList = new ArrayList<>();
    private String school;

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }

    GeneralAdapter.viewHolderPlug viewHolderPlug = new GeneralAdapter.viewHolderPlug() {
        @Override
        public MainViewHolder setPlug(ViewGroup group, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.chat_list_design, group, false);


            return new ChatListItemViewHolder(view);
        }
    };

    ChatItemClickListener chatItemClickListener = new ChatItemClickListener() {
        @Override
        public void onClickItem(int position) {
            ChatItem currentChat = (ChatItem) chatList.get(position);
            String chatId = currentChat.getChatID();
            String chatChannelId = currentChat.getChatChannelID();
            Intent intent = new Intent(getActivity(), ChatPage.class);
            intent.putExtra("chat id", chatId);
            intent.putExtra("chat name", currentChat.getChatName());
            intent.putExtra("chat channel id", chatChannelId);
            startActivity(intent);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.messaging_page, container, false);
        loader = view.findViewById(R.id.loader);
        chatListItems = view.findViewById(R.id.chatListItems);
        generalAdapter = new GeneralAdapter();
        generalAdapter.setItems(chatList);

        userID = User.getUserID(Objects.requireNonNull(getContext()));
        myChats = allUsers.child(userID).child("chats");
        generalAdapter.setItems(chatList);
        generalAdapter.setViewHolderPlug(viewHolderPlug);
        generalAdapter.setClickListeners(chatItemClickListener);
        chatListItems.setLayoutManager(new LinearLayoutManager(getContext()));
        chatListItems.setHasFixedSize(true);
        chatListItems.setAdapter(generalAdapter);

        loadChatList();
        return view;
    }


    private void loadChatList() {
        myChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot singleChatItem : snapshot.getChildren()) {
                        ChatItem currentChat = singleChatItem.getValue(ChatItem.class);
                        addItemToList(currentChat);

                    }
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void addItemToList(ChatItem currentChat) {
        allUsers.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            chatChannels.child(currentChat.getChatChannelID()).child("messages")
                                    .limitToLast(1)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot lastMessage : snapshot.getChildren()) {

                                                    Message newLastMessage = lastMessage.getValue(Message.class);
                                                    currentChat.setLastMessageText(newLastMessage.getMessageContent());
                                                    currentChat.setLastMessageTime(newLastMessage.getMessageTime());

                                                }
                                            } else {
                                                currentChat.setLastMessageTime(0);
                                                currentChat.setLastMessageText("");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });


                            User user = snapshot.getValue(User.class);
                            switch (currentChat.getChatType()) {
                                case ItemTypes
                                        .SCHOOL_GROUP:
                                    currentChat.setChatName(user.getSchool());
                                    currentChat.setChatIcon("");
                                    break;
                                case ItemTypes.FACULTY_GROUP:

                                    currentChat.setChatName(user.getFaculty());
                                    currentChat.setChatIcon("");
                                    break;
                                case ItemTypes.DEPARTMENT_GROUP:

                                    currentChat.setChatName(user.getDepartment());
                                    currentChat.setChatIcon("");
                                    break;

                            }
                            chatList.add(currentChat);
                            generalAdapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


    }

}