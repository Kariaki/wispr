package com.wispr.wispr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.wispr.wispr.LoginAndRegistration.LoginPage;

public class SplashScreen extends AppCompatActivity {

    Handler handler;

    private ImageView launchLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_page);
        handler = new Handler();
        launchLogo=findViewById(R.id.launchLogo);

       // YoYo.with(Techniques.Bounce).duration(1000).playOn(launchLogo);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              //  Intent loginIntent = new Intent(LaunchPage.this, StartUpPage.class);
             //   LaunchPage.this.startActivity(loginIntent);
               // LaunchPage.this.finish();
               // checkFirstTime();
            }
        }, 3000);



    }


    private void checkFirstTime() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        boolean first_time = sharedPreferences.getBoolean("first time", true);
        String userID = sharedPreferences.getString("user id", "");

        String school = sharedPreferences.getString("school", "");
        if (!first_time) {
            Intent loginIntent = new Intent(this, MainActivity.class);
            SplashScreen.this.startActivity(loginIntent);
            SplashScreen.this.finish();

        } else {

            Intent loginIntent = new Intent(this, LoginPage.class);
            SplashScreen.this.startActivity(loginIntent);
            SplashScreen.this.finish();
        }

    }
}