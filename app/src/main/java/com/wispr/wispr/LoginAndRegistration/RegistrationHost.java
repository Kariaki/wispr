package com.wispr.wispr.LoginAndRegistration;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wispr.wispr.adapter.HomePagerAdapter;
import com.wispr.wispr.R;

import java.util.ArrayList;
import java.util.List;

public class RegistrationHost extends FragmentActivity {

    List<Fragment> pages = new ArrayList<>();
    HomePagerAdapter pagerAdapter;
    ViewPager2 registrationPageHost;
    private int selectedSignUpOption;
    private final int registrationHostRoot = R.id.registrationHostRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_host);
        registrationPageHost = findViewById(R.id.registrationPageHost);

        fragmentPages();
        CreateUserName createUserName = new CreateUserName();
        createUserName.setOnNextClick(new OnNextClick() {
            @Override
            public void nextClick(UserRegistrationData user) {


                CreatePassword createPassword = new CreatePassword();
                createPassword.setUserRegistrationData(user);
                createPassword.setOnNextClick(onNextClick);
                createPassword.setOnCancelClick(onCancelClick);


                getSupportFragmentManager().beginTransaction().add(registrationHostRoot, createPassword).addToBackStack(null).commit();

            }
        });
        createUserName.setOnCancelClick(onCancelClick);


        getSupportFragmentManager().beginTransaction().add(registrationHostRoot, createUserName).addToBackStack(null).commit();


    }


    private void fragmentPages() {


    }

    List<UserRegistrationData> userList = new ArrayList<>();
    DatabaseReference allUsers = FirebaseDatabase.getInstance().getReference("users");

    OnCancelClick onCancelClick = new OnCancelClick() {
        @Override
        public void cancelClick() {
            onBackPressed();
        }
    };

    OnNextClick onNextClick = new OnNextClick() {
        @Override
        public void nextClick(UserRegistrationData userRegistrationData) {



        }

    };

}
