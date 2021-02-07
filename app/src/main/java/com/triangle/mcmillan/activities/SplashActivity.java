package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.models.User;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            moveToNext(currentUser);
        }
        else{
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }


    private void moveToNext(final FirebaseUser firebaseUser){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

        database.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (Objects.requireNonNull(snapshot.getKey()).equals(firebaseUser.getUid())){
                    User CurrentUser = snapshot.getValue(User.class);

                    if (CurrentUser.getRole().equals(User.UserRole.CUSTOMER)){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(SplashActivity.this, AdminMainActivity.class));
                    }
                    finish();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}