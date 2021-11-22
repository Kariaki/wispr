package com.wispr.wispr.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.entities.User;


import de.hdodenhof.circleimageview.CircleImageView;

public class LibraryFunction {


    public static void likeCount(String postID, TextView counter, boolean IsThumbsUp) {
        DatabaseReference thisPost = FirebaseDatabase.getInstance().getReference("post").child(postID);
        DatabaseReference response;
        String thumbsUp = "thumbsUp";
        String thumbsDown = "thumbsDown";
        if (IsThumbsUp) {
            response = thisPost.child(thumbsUp);
        } else {
            response = thisPost.child(thumbsDown);
        }
        response.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getChildrenCount() != 0) {

                        String count = String.valueOf(snapshot.getChildrenCount());
                        counter.setText(count);
                    } else {
                        counter.setText("");
                    }

                } else {
                    counter.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static void reaction(String postID, String userId, boolean IsThumbsUp, boolean isChecked) {
        DatabaseReference thisPost = FirebaseDatabase.getInstance().getReference("post").child(postID);
        DatabaseReference response;
        String thumbsUp = "thumbsUp";
        String thumbsDown = "thumbsDown";
        DatabaseReference removal;
        if (IsThumbsUp) {
            response = thisPost.child(thumbsUp);
            removal = thisPost.child(thumbsDown);

        } else {
            response = thisPost.child(thumbsDown);
            removal = thisPost.child(thumbsUp);
        }
        response.child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()&&!isChecked) {

                                response.removeValue();


                        } else if(isChecked&&!snapshot.exists()){
                            response.child(userId).setValue(userId);
                            removal.child(userId).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public static void commentCount(String postID, TextView counter) {
        DatabaseReference thisPost = FirebaseDatabase.getInstance().getReference("post").child(postID);
        DatabaseReference response = thisPost.child("comments");

        response.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getChildrenCount() != 0) {

                        String count = String.valueOf(snapshot.getChildrenCount());
                        counter.setText(count);
                    } else {
                        counter.setText("");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static void fetchUserDetail(String userID, TextView displayName, TextView userName, TextView about,
                                       CircleImageView profileImage, Context context) {

        DatabaseReference thisUser = FirebaseDatabase.getInstance().getReference("users").child(userID);

        thisUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        if (about != null) {
                            about.setText(user.getBio());
                        }
                        if(userName!=null){

                            userName.setText(user.getUserName());
                        }
                        displayName.setText(user.getDisplayName());
                        if (user.getProfileUrl() != null) {
                            if (user.getProfileUrl().isEmpty()) {
                                user.setProfileUrl(Constants.imagePlaceHolder);
                            }
                            Glide.with(context.getApplicationContext()).load(user.getProfileUrl()).into(profileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void spanString(TextView text,String d_text,String style){
        Spannable spannable=new SpannableString(d_text);

        String[]hashTags=d_text.split(style);
        String actualHash="";
        int count=0;
        for(String currentHashSplit:hashTags){
            if(count>0){

                String []realHash=currentHashSplit.split(" ");
                actualHash="#"+realHash[0]+" ";

                if(Character.isLetter(actualHash.toCharArray()[1])){

                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE),
                            d_text.indexOf(actualHash),
                            (d_text.indexOf(actualHash))+actualHash.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            count++;
        }

        text.setText(spannable,TextView.BufferType.SPANNABLE);
    }



    public static void spanString(EditText text, String d_text, String style){
        Spannable spannable=new SpannableString(d_text);

        String[]hashTags=d_text.split(style);
        String actualHash="";
        int count=0;
        for(String currentHashSplit:hashTags){
            if(count>0){

                String []realHash=currentHashSplit.split(" ");
                actualHash="#"+realHash[0]+" ";

                if(Character.isLetter(actualHash.toCharArray()[1])){

                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE),
                            d_text.indexOf(actualHash),
                            (d_text.indexOf(actualHash))+actualHash.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            count++;
        }

        text.setText(spannable,TextView.BufferType.SPANNABLE);
    }
}
