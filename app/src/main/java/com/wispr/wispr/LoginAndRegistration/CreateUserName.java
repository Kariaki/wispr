package com.wispr.wispr.LoginAndRegistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.R;


public class CreateUserName extends Fragment {


    private OnNextClick onNextClick;

    public void setOnNextClick(OnNextClick onNextClick) {
        this.onNextClick = onNextClick;
    }

    private OnCancelClick onCancelClick;

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    EditText displayName, userName;
    Button nextButton;
    ImageView backButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_initial_profile, container, false);
        displayName = view.findViewById(R.id.createDisplayName);
        userName = view.findViewById(R.id.createUserName);
        backButton = view.findViewById(R.id.backButton);
        nextButton = view.findViewById(R.id.nextButton);


        nextButton.setOnClickListener(v -> {
            createUserInitialProfile();
        });
        backButton.setOnClickListener(v -> {
            onCancelClick.cancelClick();
        });

        return view;
    }

    User newUser;

    DatabaseReference allUserList = FirebaseDatabase.getInstance().getReference("users");

    private void createUserInitialProfile() {

        String newUserDisplayName = displayName.getText().toString();
        String newUserUsername = userName.getText().toString();
        UserRegistrationData userRegistrationData = new UserRegistrationData(newUserUsername, newUserDisplayName, null, null, null, null);

        onNextClick.nextClick(userRegistrationData);

    }
}