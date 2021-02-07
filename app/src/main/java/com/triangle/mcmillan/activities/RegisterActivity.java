package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.models.User;
import com.triangle.mcmillan.utilities.UserHelper;

import static com.triangle.mcmillan.activities.LoginActivity.isValidEmail;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initViews();
    }

    private void initViews() {
        mEmail = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextPassword);
        mName = findViewById(R.id.editTextName);
        mRegisterBtn = findViewById(R.id.btnRegister);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        mRegisterBtn.setVisibility(View.GONE);
        if (!isValidEmail(mEmail.getText().toString())){
            mEmail.setError("Not a valid Email");
            mRegisterBtn.setVisibility(View.VISIBLE);
        }
        else if (mPassword.getText().toString().length() < 6){
            mPassword.setError("Password should be more than 6 characters long");
            mRegisterBtn.setVisibility(View.VISIBLE);
        }
        else if (mName.getText().toString().length() < 1){
            mPassword.setError("Name cannot be empty");
            mRegisterBtn.setVisibility(View.VISIBLE);
        }
        else{
            Snackbar.make(mEmail, "Please wait while creating user", Snackbar.LENGTH_LONG).show();

            final String email = mEmail.getText().toString().trim();
            final String password = mPassword.getText().toString().trim();
            final String name = mName.getText().toString().trim();

            // register
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // login
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // add to database
                                                    User user = new User();
                                                    user.setName(name);
                                                    user.setRole(User.UserRole.CUSTOMER);
                                                    user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                    FirebaseDatabase.getInstance().getReference("users")
                                                            .child(user.getId())
                                                            .setValue(user);

                                                    UserHelper.loadCurrentUser();
                                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                    finish();
                                                    finish();
                                                } else {
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }
}