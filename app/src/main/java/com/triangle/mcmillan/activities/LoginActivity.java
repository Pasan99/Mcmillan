package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.models.User;
import com.triangle.mcmillan.utilities.CartHelper;
import com.triangle.mcmillan.utilities.UserHelper;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextView mRegisterBtn;
    private EditText mEmail;
    private EditText mPassword;
    private MaterialButton mLoginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            moveToNext(currentUser);
        }
    }

    private void initViews() {
        mRegisterBtn = findViewById(R.id.textRegister);
        mEmail = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextPassword);
        mLoginBtn = findViewById(R.id.btnLogin);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    private void login(){
        mLoginBtn.setVisibility(View.INVISIBLE);
        if (!isValidEmail(mEmail.getText().toString())){
            mEmail.setError("Not a valid Email");
            mLoginBtn.setVisibility(View.VISIBLE);
        }
        else if (mPassword.getText().toString().length() < 6){
            mPassword.setError("Password should be more than 6 characters long");
            mLoginBtn.setVisibility(View.VISIBLE);
        }
        else{
            Snackbar.make(mEmail, "Logging in user", Snackbar.LENGTH_LONG).show();
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserHelper.loadCurrentUser();
                                // Get User
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                final FirebaseUser firebaseUser = auth.getCurrentUser();
                                if (firebaseUser != null){
                                    moveToNext(firebaseUser);
                                }
                            } else {
                                mLoginBtn.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mLoginBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
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