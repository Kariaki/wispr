package com.wispr.wispr.mainPages;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.wispr.wispr.R;

public class ViewProfilePicture extends AppCompatActivity {


    private ImageView displayImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_picture);
        displayImage=findViewById(R.id.displayImage);
        String imageUrl=getIntent().getStringExtra("profile url");
        if(imageUrl!=null){

            if(!imageUrl.isEmpty()){

                Glide.with(this).load(imageUrl).into(displayImage);
            }
        }
    }

    public void backPress(View view){
        onBackPressed();

    }
}