package com.wispr.wispr.mainPages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wispr.wispr.R;

public class EditProfile extends AppCompatActivity {

    private String userID;
    DatabaseReference thisUser = FirebaseDatabase.getInstance().getReference("users");
    private EditText changeDisplayName, addBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        userID = getIntent().getStringExtra("user id");
        addBio = findViewById(R.id.addBio);
        changeDisplayName = findViewById(R.id.changeDisplayName);

        thisUser = thisUser.child(userID);
    }

    public void update(View view) {
        String newDisplayName = changeDisplayName.getText().toString();
        String newBio = addBio.getText().toString();
        if (!newDisplayName.isEmpty()) {


            thisUser.child("displayName").setValue(newBio);

        }
        if (!newBio.isEmpty()) {
            thisUser.child("bio").setValue(newBio);
        }
        if (newDisplayName.isEmpty() && newBio.isEmpty()) {

        } else {
            Toast.makeText(this, "profile updated", Toast.LENGTH_SHORT).show();
        }
        onBackPressed();
    }
    public void backPress(View view){
        onBackPressed();
    }
}