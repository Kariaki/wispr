package com.wispr.wispr.entities;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.adapter.SuperEntity;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class User extends SuperEntity {

    private String userName;
    private String profileUrl;
    private String bio;
    private String displayName;
    private String password;
    private String school;
    private String faculty;
    private String department;




    public User(String userName, String displayName,String school,String faculty,String department,String password,String bio,String profileUrl) {
        this.userName = userName;

        this.displayName = displayName;
        this.school=school;
        this.faculty=faculty;
        this.department=department;
        this.password=password;
        this.bio=bio;
        this.profileUrl=profileUrl;
        this.faculty=faculty;
        this.department=department;
        this.school=school;

    }

    public User() {

    }



    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static String getUserID(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        //boolean first_time = sharedPreferences.getBoolean("first time", true);
        return sharedPreferences.getString("user id","");

    }
    private static DatabaseReference allUsers= FirebaseDatabase.getInstance().getReference("users");
    public static void displayCommunityName(String communityType, String userId,TextView display){
        allUsers.child(userId).child(communityType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String value=snapshot.getValue(String.class);
                    display.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }



}
