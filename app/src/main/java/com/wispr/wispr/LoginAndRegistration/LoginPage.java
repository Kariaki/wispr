package com.wispr.wispr.LoginAndRegistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.MainActivity;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class LoginPage extends AppCompatActivity {


    private final int RC_SIGN_IN = 0;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);

        signUp = findViewById(R.id.signUpWithPhone);
        signUp.setOnClickListener(v -> {
            startRegistrationProcess();

        });

    }

    private Button signUp;
    private DatabaseReference allUser = FirebaseDatabase.getInstance().getReference("users");

    public void openMainActivity(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        allUser.orderByChild("userName").equalTo(user)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot smallSnapShot) {
                        if (smallSnapShot.exists()) {
                            for (DataSnapshot snapshot : smallSnapShot.getChildren()) {


                                if (snapshot.exists()) {
                                    User currentUser = snapshot.getValue(User.class);
                                    assert currentUser != null;
                                    if (pass.equals(currentUser.getPassword())) {
                                        Toast.makeText(LoginPage.this, "success", Toast.LENGTH_SHORT).show();

                                        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("first time", false);
                                        editor.putString("user id", snapshot.getKey());
                                        editor.putString("school", currentUser.getSchool());
                                        editor.apply();
                                        Intent mainActivityIntent = new Intent(LoginPage.this, MainActivity.class);
                                        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

                                        startActivity(mainActivityIntent);
                                    } else {
                                        Toast.makeText(LoginPage.this, "password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginPage.this, "username not found, pls register", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginPage.this, "username not found,pls register", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(LoginPage.this, "request failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void startRegistrationProcess() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        // providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                        //.setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



                Intent intent = new Intent(this, RegistrationHost.class);
                assert user != null;
                intent.putExtra("phone number", user.getPhoneNumber());
                startActivity(intent);

            }
        }

    }

}