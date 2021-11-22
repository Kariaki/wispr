package com.wispr.wispr.LoginAndRegistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.MainActivity;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class CreatePassword extends Fragment {


    OnNextClick onNextClick;
    OnCancelClick onCancelClick;

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }



    public void setOnNextClick(OnNextClick onNextClick) {
        this.onNextClick = onNextClick;
    }

    EditText password, confirmPassword;
    ImageView backButton;
    private UserRegistrationData userRegistrationData;

    public void setUserRegistrationData(UserRegistrationData userRegistrationData) {
        this.userRegistrationData = userRegistrationData;
    }

    Button nextButton;
    DatabaseReference allUsers = FirebaseDatabase.getInstance().getReference("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_password, container, false);


        password = view.findViewById(R.id.password);
        nextButton = view.findViewById(R.id.nextButton);
        backButton = view.findViewById(R.id.backButton);
        confirmPassword = view.findViewById(R.id.confirmPassword);

        nextButton.setOnClickListener(v -> {
            if (confirmPassword.getText().toString().equals(password.getText().toString())) {
                //  UserRegistrationData userRegistrationData=new UserRegistrationData(null,null,null,null,null,confirmPassword.getText().toString());
                userRegistrationData.setPassword(password.getText().toString());
                //onNextClick.nextClick(userRegistrationData);
                process();

            }
        });
        backButton.setOnClickListener(v -> {
            onCancelClick.cancelClick();
        });
        return view;
    }

    private void process() {

        User newUser = new User(userRegistrationData.getUsername(), userRegistrationData.getDisplayName(), "", "",
                "", password.getText().toString(), "", "");

        DatabaseReference userSpace = allUsers.push();
        userSpace.setValue(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("first time", false);
                        editor.putString("user id", userSpace.getKey());
                        editor.apply();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        //openFinishRegistration(userSpace.getKey());


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });

    }


}