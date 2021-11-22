package com.wispr.wispr.mainPages.profiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.mainPages.ViewProfilePicture;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class SocialProfile extends Fragment {


    public SocialProfile() {
        // Required empty public constructor
    }


    private String Owner;

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getOwner() {
        return Owner;
    }

    Button sendDirectMessage;
    TextView description,about_user,username,displayName;
    CircleImageView profileImage;
    ImageView backButton;
    DatabaseReference allUsers= FirebaseDatabase.getInstance().getReference("users");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.social_profile, container, false);

        viewById(view);

        LibraryFunction.fetchUserDetail(Owner,displayName,username,about_user,profileImage,getContext());
        backButton.setOnClickListener(v->{
            getActivity().onBackPressed();
        });
        profileImage.setOnClickListener(v->{
            Intent intent=new Intent(getActivity(),ViewProfilePicture.class);
            allUsers.child(Owner).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user=snapshot.getValue(User.class);
                        String profile_url=user.getProfileUrl();
                        intent.putExtra("profile url",profile_url);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        });
        return view;
    }
    private void viewById(View view){
        displayName=view.findViewById(R.id.displayName);

        description=view.findViewById(R.id.description);
        sendDirectMessage=view.findViewById(R.id.sendDirectMessage);
        backButton=view.findViewById(R.id.backButton);
        profileImage=view.findViewById(R.id.profileImage);
        about_user=view.findViewById(R.id.about_user);
        username=view.findViewById(R.id.username);
    }

}