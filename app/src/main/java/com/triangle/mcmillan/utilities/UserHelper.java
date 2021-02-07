package com.triangle.mcmillan.utilities;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.triangle.mcmillan.models.CartItem;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.models.User;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class UserHelper {
    public static User CurrentUser;
    public static void loadCurrentUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null){
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

            database.addChildEventListener(new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (Objects.requireNonNull(snapshot.getKey()).equals(firebaseUser.getUid())){
                        Log.e("object", snapshot.getValue().toString());
                        CurrentUser = snapshot.getValue(User.class);

                        assert CurrentUser != null;
                        CartHelper.cartItems = CurrentUser.getCart();
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
}
