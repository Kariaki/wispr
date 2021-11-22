package com.wispr.wispr.util;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.entities.Categories;
import com.wispr.wispr.entities.ChatChannel;
import com.wispr.wispr.entities.ChatItem;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.PostChannel;
import com.wispr.wispr.entities.User;

import org.jetbrains.annotations.NotNull;

public class DatabaseHelper {

    private static DatabaseReference categories = FirebaseDatabase.getInstance().getReference("categories");
    private static DatabaseReference postFolder = FirebaseDatabase.getInstance().getReference("post");
    private static DatabaseReference tagsFolder = FirebaseDatabase.getInstance().getReference("tags");
    private static DatabaseReference chatChannels = FirebaseDatabase.getInstance().getReference("ChatChannels");

    private static DatabaseReference allusers = FirebaseDatabase.getInstance().getReference("users");


    public static void connect(User user, String userId) {

        
        allusers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (user.getSchool() != null && !user.getSchool().isEmpty()) {

                        categories.orderByChild("name").equalTo(user.getSchool())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot innerSnapShot : snapshot.getChildren()) {
                                                Categories categoryValue = innerSnapShot.getValue(Categories.class);
                                                addToChat(innerSnapShot.getRef(), categoryValue, userId, user.getSchool(), ItemTypes.SCHOOL_GROUP);
                                                if (user.getFaculty() != null && !user.getFaculty().isEmpty()) {
                                                    addSubCategoryToChat(innerSnapShot.getRef(), user.getFaculty(), userId, ItemTypes.FACULTY_GROUP);
                                                }
                                                if (user.getDepartment() != null && !user.getDepartment().isEmpty()) {
                                                    addSubCategoryToChat(innerSnapShot.getRef(), user.getDepartment(), userId, ItemTypes.DEPARTMENT_GROUP);

                                                }

                                            }

                                        } else {
                                            //add school to category
                                            DatabaseReference newChatItem = allusers.child(userId).child("chats");
                                            DatabaseReference newCategory = categories.push();
                                            DatabaseReference newTags = tagsFolder.push();
                                            DatabaseReference newChatChannel = chatChannels.push();
                                            Categories categoryValue = new Categories(user.getSchool(), newChatChannel.getKey(), newTags.getKey());
                                            PostChannel channel = new PostChannel(user.getSchool(), user.getSchool(), newTags.getKey());
                                            newCategory.setValue(categoryValue); //create new category and add value
                                            newTags.setValue(channel); //add new tag
                                            allusers.child(userId).child("tags").child(newTags.getKey()).setValue(newTags.getKey());
                                            ChatChannel chatChannelValue = new ChatChannel(newChatChannel.getKey(), System.currentTimeMillis());
                                            newChatChannel.setValue(chatChannelValue);
                                            ChatItem chatItem = new ChatItem(0, newChatChannel.getKey(), newChatChannel.getKey(), "", user.getSchool(), "", 0, ItemTypes.SCHOOL_GROUP);
                                            newChatItem.child(newCategory.getKey()).setValue(chatItem); //add category to chat
                                            if (user.getFaculty() != null && !user.getFaculty().isEmpty()) {

                                                addSubCategory(newCategory, userId, ItemTypes.FACULTY_GROUP, user.getFaculty());
                                            }
                                            if (user.getDepartment() != null && !user.getDepartment().isEmpty()) {

                                                addSubCategory(newCategory, userId, ItemTypes.DEPARTMENT_GROUP, user.getDepartment());
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private static void addSubCategory(DatabaseReference parentChild, String userId, int type, String name) {
        DatabaseReference newChatItem = allusers.child(userId).child("chats");
        DatabaseReference newCategory = parentChild.push();
        DatabaseReference newTags = tagsFolder.push();
        DatabaseReference newChatChannel = chatChannels.push();
        Categories categoryValue = new Categories(name, newChatChannel.getKey(), newTags.getKey());
        PostChannel channel = new PostChannel(name, name, newTags.getKey());
        newCategory.setValue(categoryValue); //create new category and add value
        newTags.setValue(channel); //add new tag
        allusers.child(userId).child("tags").child(newTags.getKey()).setValue(newTags.getKey());
        ChatChannel chatChannelValue = new ChatChannel(newChatChannel.getKey(), System.currentTimeMillis());
        newChatChannel.setValue(chatChannelValue);
        ChatItem chatItem = new ChatItem(0, newChatChannel.getKey(), newChatChannel.getKey(), "", name, "", 0, type);
        newChatItem.child(newCategory.getKey()).setValue(chatItem); //add category to chat

    }

    private static void addToChat(DatabaseReference innerSnapShot, Categories categoryValue, String userId, String name, int type) {
        DatabaseReference newChatItem = allusers.child(userId).child("chats");

        ChatItem chatItem = new ChatItem(0, categoryValue.getChatChannelId(), categoryValue.getChatChannelId(), "", name, "", 0, type);
        newChatItem.child(innerSnapShot.getKey()).setValue(chatItem); //add category to chat

    }

    private static void addSubCategoryToChat(DatabaseReference parentNode, String name, String userId, int type) {
        parentNode.orderByChild("name").equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot innerSnapShot : snapshot.getChildren()) {
                                Categories categoryValue = innerSnapShot.getValue(Categories.class);
                                if (categoryValue != null) {
                                    addToChat(innerSnapShot.getRef(), categoryValue, userId, name, type);
                                }

                            }
                        } else {
                            addSubCategory(parentNode, userId, type, name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


    }

}
